package com.music.firebeats;

import net.runelite.client.plugins.music.MusicConfig;
import net.runelite.client.plugins.music.MusicPlugin;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.TitleComponent;

import javax.inject.Inject;
import java.awt.*;

import static net.runelite.api.MenuAction.RUNELITE_OVERLAY_CONFIG;
import static net.runelite.client.ui.overlay.OverlayManager.OPTION_CONFIGURE;

public class FireBeatsOverlay extends OverlayPanel {
    private final FireBeatsPlugin plugin;
    private final FireBeatsConfig config;

    @Inject
    private FireBeatsOverlay(FireBeatsPlugin plugin, FireBeatsConfig config)
    {
        super(plugin);
        setPosition(OverlayPosition.TOP_CENTER);
        this.plugin = plugin;
        this.config = config;
        getMenuEntries().add(new OverlayMenuEntry(RUNELITE_OVERLAY_CONFIG, OPTION_CONFIGURE, "Fire Beats overlay"));
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        if (plugin.getMusicConfig().showCurrentTrackName() == true)
        {
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text("Current Track: " + plugin.getCurrentTrackBox().getText())
                    .color(Color.GREEN)
                    .build());

            panelComponent.setPreferredSize(new Dimension(
                    graphics.getFontMetrics().stringWidth("Current Track: " +
                            plugin.getCurrentTrackBox().getText()) + 10,
                    0));

            return super.render(graphics);
        }
        else
        {
            return null;
        }
    }
}
