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

	@Override
	protected void startUp() throws Exception
	{
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
			client.addChatMessage(ChatMessageType.GAMEMESSAGE,
					"", "Fire Beats says AYYYY", null);
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

		System.out.println("The current track is " + currentTrack.getText());
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
