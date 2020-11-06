package com.music.firebeats;

import com.google.inject.Provides;
import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.ClientTick;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetID;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetType;
import net.runelite.client.RuneLite;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.overlay.OverlayManager;

import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.io.*;
import java.net.URL;

import jaco.mp3.player.MP3Player;
import net.runelite.client.util.ImageUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

@Slf4j
@PluginDescriptor(
	name = "Fire Beats",
	description = "Plays remixes of the current track being played in game.",
	tags = {"music"}
)
public class FireBeatsPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ClientToolbar clientToolbar;

	@Inject
	private FireBeatsConfig config;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private FireBeatsOverlay overlay;

	private final int FADING_TRACK_STATE = 0;

	private final int PLAYING_TRACK_STATE = 1;

	private int currentPlayerState = PLAYING_TRACK_STATE;

	private NavigationButton navButton;

	private FireBeatsPanel panel;

	private Widget currentTrackBox;

	private String previousTrack = "";

	private String nextTrack = "";

	private MP3Player trackPlayer = new MP3Player();

	private Map<String, Track> mp3Map = new HashMap<String, Track>();

	private boolean remixAvailable = false;

	private boolean changingTracks = false;

	private boolean initializeTrack = true;

	private void buildMp3TrackMap()
	{
		try
		{
			// Check if track listing CSV exists.
			File trackFile = new File(RuneLite.RUNELITE_DIR, "Osrs-Track-Remix-List.csv");
			URL trackResource = getClass().getClassLoader().getResource("Osrs-Track-Remix-List.csv");
			if(trackFile.exists() == false) {
				// Copy default track list from resources.
				Files.copy(
						new File(trackResource.getPath()).toPath(),
						trackFile.toPath(),
						StandardCopyOption.REPLACE_EXISTING);
			}

			String line = "";
			String delimiter = ",";

			BufferedReader br = new BufferedReader(new FileReader(trackFile.toPath().toString()));
			while ((line = br.readLine()) != null)   //returns a Boolean value
			{
				String[] track = line.split(delimiter);    // use comma as separator
				if (track.length == 1)
				{
					System.out.println("Track: [Name=" + track[0] + "]");
					Track newTrack = new Track();
					newTrack.name = track[0];
					mp3Map.put(track[0], newTrack);
				}
				else if (track.length == 2)
				{
					System.out.println("Track: [Name=" + track[0] + ", Link=" + track[1] + "]");
					Track newTrack = new Track();
					newTrack.name = track[0];
					newTrack.link = track[1];
					mp3Map.put(track[0], newTrack);
				}
				else
				{
					System.out.println("Track: [Name=" + track[0] + ", Link=" + track[1] + ", Credit=" + track[2] + "]");
					Track newTrack = new Track();
					newTrack.name = track[0];
					newTrack.link = track[1];
					newTrack.credit = track[2];
					mp3Map.put(track[0], newTrack);
				}
			}

			log.info("Tracks successfully added to map.");
		}
		catch (Exception e)
		{
			log.error(e.getMessage());
		}
	}

	private String getTrackLink(String anonFilesLink)
	{
		String link = "";

		try
		{
			Document doc = Jsoup.connect(anonFilesLink).get();

			Element downloadUrl = doc.getElementById("download-url");

			link = downloadUrl.attr("href");

			link = link.replace(" ", "%20");

			log.info("Link: " + link);
		}
		catch (Exception e)
		{
			log.error(e.getMessage());
		}

		return link;
	}

	private void fadeCurrentTrack()
	{
		if (trackPlayer.getVolume() == 0)
		{
			client.setMusicVolume(0);
			previousTrack = nextTrack;
			currentPlayerState = PLAYING_TRACK_STATE;
		}
		else
		{
			trackPlayer.setVolume(trackPlayer.getVolume() - 7);

			if (trackPlayer.getVolume() < 7)
			{
				trackPlayer.setVolume(0);
				trackPlayer.stop();
				previousTrack = nextTrack;
				currentPlayerState = PLAYING_TRACK_STATE;
			}
		}

	}

	@Override
	protected void startUp() throws Exception
	{
		final BufferedImage icon = ImageUtil.getResourceStreamFromClass(getClass(), "/fire_beats_icon.png");

		panel = new FireBeatsPanel(this);

		navButton = NavigationButton.builder()
				.tooltip("Fire Beats")
				.icon(icon)
				.priority(50)
				.panel(panel)
				.build();

		clientToolbar.addNavigation(navButton);

		// Build map of mp3 track links
		buildMp3TrackMap();

		overlayManager.add(overlay);


		log.info("Fire Beats started!");
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Fire Beats stopped!");
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			config.setMusicVolume(0);
		}
		if (gameStateChanged.getGameState() == GameState.LOGIN_SCREEN)
		{
			try
			{
				client.setMusicVolume(0); // Attempt to force mute.
				// Stop current track
				trackPlayer.stop();
				trackPlayer.getPlayList().clear();
				// Start playing new track
				Track track = mp3Map.get("Scape Main");
				if (track.link != null)
				{
					remixAvailable = true;
					trackPlayer.setVolume(config.volume() - config.remixVolumeOffset());
					// Get actual track link
					String directLink = getTrackLink(track.link);
					trackPlayer.addToPlayList(new URL(directLink));
					trackPlayer.play();
				}
				else
				{
					remixAvailable = false;
					//  TODO: Handle playing normal song, or not
				}
			}
			catch (Exception e)
			{
				log.error(e.getMessage());
			}
		}
	}

	@Subscribe
	public void onWidgetLoaded(WidgetLoaded widgetLoaded)
	{
		if (widgetLoaded.getGroupId() == WidgetID.RESIZABLE_VIEWPORT_OLD_SCHOOL_BOX_GROUP_ID)
		{
			Widget viewport = client.getWidget(WidgetInfo.RESIZABLE_VIEWPORT_OLD_SCHOOL_BOX);
			currentTrackBox = viewport.createChild(-1, WidgetType.TEXT);
		}
	}

	@Subscribe
	public void onClientTick(ClientTick clientTick)
	{
		if (client.getGameState() == GameState.LOGIN_SCREEN)
		{
			log.info("HELLO!!!");
		}
	}

	@Subscribe
	public void onGameTick(GameTick gameTick)
	{
		final Widget currentTrack = client.getWidget(
				MusicWidgetInfo.MUSIC_CURRENT_TRACK.getGroupId(),
				MusicWidgetInfo.MUSIC_CURRENT_TRACK.getChildId());

		if (previousTrack != currentTrack.getText())
		{
			changingTracks = true;
			nextTrack = currentTrack.getText();
			currentPlayerState = FADING_TRACK_STATE;
			initializeTrack = true;
		}
		else
		{
			changingTracks = false;
		}

		try
		{
			if (changingTracks == true && currentPlayerState == FADING_TRACK_STATE)
			{
				fadeCurrentTrack();
			}
			else
			{
				if (initializeTrack == true)
				{
					trackPlayer.getPlayList().clear();
					// Start playing new track
					Track track = mp3Map.get(nextTrack);
					if (track.link != null)
					{
						remixAvailable = true;
						client.setMusicVolume(0);
						trackPlayer.setVolume(config.volume() - config.remixVolumeOffset());
						// Get actual track link
						String directLink = getTrackLink(track.link);
						trackPlayer.addToPlayList(new URL(directLink));
						trackPlayer.play();
						client.addChatMessage(ChatMessageType.GAMEMESSAGE,
								"",
								"Fire Beats Notice: " + track.name + " remix produced by " + track.credit,
								null);
						initializeTrack = false;
					}
					else
					{
						remixAvailable = false;
						if (config.playOriginalIfNoRemix() == true)
						{
							client.setMusicVolume(config.volume());
							initializeTrack = false;
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			log.error(e.getMessage());
		}

		if (config.mute() == true)
		{
			trackPlayer.setVolume(0);
			client.setMusicVolume(0);
		}
		else
		{
			if (remixAvailable == true)
			{ 	// If not in a fading state...
				if (currentPlayerState == PLAYING_TRACK_STATE)
				{
					// TODO: Make this not trash.
					if (trackPlayer.getVolume() < (config.volume() - config.remixVolumeOffset()))
					{
						trackPlayer.setVolume(trackPlayer.getVolume() + 7);
					}
					else if (trackPlayer.getVolume() > (config.volume() - config.remixVolumeOffset()))
					{
						trackPlayer.setVolume(config.volume() - config.remixVolumeOffset());
					}

					client.setMusicVolume(0);
				}
			}
			else
			{
				if (config.playOriginalIfNoRemix() == true)
				{
					trackPlayer.setVolume(0);
					client.setMusicVolume(config.volume());
				}
				else
				{
					trackPlayer.setVolume(0);
					client.setMusicVolume(0);
				}
			}
		}

		if (currentTrackBox != null)
		{
			currentTrackBox.setText(currentTrack.getText());
		}
	}

	@Provides
	FireBeatsConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(FireBeatsConfig.class);
	}

	public Widget getCurrentTrackBox()
	{
		return currentTrackBox;
	}

	public FireBeatsConfig getMusicConfig()
	{
		return config;
	}
}

class Track
{
	public String name;
	public String link;
	public String credit;
}
