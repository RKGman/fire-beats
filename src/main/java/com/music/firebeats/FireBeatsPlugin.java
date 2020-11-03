package com.music.firebeats;

import com.google.inject.Provides;
import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetID;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetType;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.music.MusicConfig;
import net.runelite.client.plugins.music.MusicPlugin;
import net.runelite.client.ui.overlay.OverlayManager;

import java.util.*;
import java.io.*;
import java.net.URL;

import jaco.mp3.player.MP3Player;
import org.graalvm.compiler.phases.graph.ScheduledNodeIterator;



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
	private FireBeatsConfig config;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private FireBeatsOverlay overlay;

	private Widget currentTrackBox;

	private String previousSong = "";

	private MP3Player trackPlayer = new MP3Player();

	private Map<String, Track> mp3Map = new HashMap<String, Track>();

	private void buildMp3TrackMap()
	{
		try
		{
			String line = "";
			String delimiter = ",";

			BufferedReader br = new BufferedReader(new FileReader("libs/Osrs-Track-Remix-List.csv"));
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

	@Override
	protected void startUp() throws Exception
	{
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
//			client.addChatMessage(ChatMessageType.GAMEMESSAGE,
//					"", "Fire Beats says AYYYY", null);
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
	public void onGameTick(GameTick gameTick)
	{
		final Widget currentTrack = client.getWidget(
				MusicWidgetInfo.MUSIC_CURRENT_TRACK.getGroupId(),
				MusicWidgetInfo.MUSIC_CURRENT_TRACK.getChildId());

		if (previousSong != currentTrack.getText())
		{
			try
			{
				previousSong = currentTrack.getText();
				// Stop current track
				trackPlayer.stop();
				trackPlayer = new MP3Player();
				// Start playing new track
				Track track = mp3Map.get(currentTrack.getText());
				if (track.link != null)
				{
					trackPlayer.addToPlayList(new URL(track.link));
					trackPlayer.play();
					client.addChatMessage(ChatMessageType.GAMEMESSAGE,
					"", "Fire Beats Notice: " + track.name + " remix produced by " + track.credit,
							null);
				}
				else
				{
					//  TODO: Handle playing normal song, or not
				}
			}
			catch (Exception e)
			{
				log.error(e.getMessage());
			}

		}

		// System.out.println("The current track is " + currentTrack.getText());
		currentTrackBox.setText(currentTrack.getText());
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
