package com.music.firebeats;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import javax.inject.Provider;
import javax.smartcardio.Card;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.sun.tools.javac.comp.Flow;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.DynamicGridLayout;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.IconTextField;
import net.runelite.client.util.Text;

@Slf4j
public
class FireBeatsPanel extends PluginPanel implements ChangeListener, ActionListener
{
    private final JPanel configPanel = new JPanel();

    FireBeatsPlugin fireBeatsPlugin;

    public FireBeatsPanel(FireBeatsPlugin fireBeatsPlugin)
    {
        super(false);

        this.fireBeatsPlugin = fireBeatsPlugin;

        setBackground(ColorScheme.DARK_GRAY_COLOR);
        setLayout(new BorderLayout());

        // Title
        JPanel titlePanel = new JPanel();
        titlePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        titlePanel.setLayout(new BorderLayout());

        JLabel title = new JLabel();
        title.setText("Fire Beats Controls");
        title.setForeground(Color.WHITE);
        titlePanel.add(title, BorderLayout.WEST);

        add(titlePanel, BorderLayout.NORTH);

        // End Title Panel

        JPanel volumePanel = new JPanel();
        volumePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        volumePanel.setLayout(new FlowLayout());

        // Volume
        JLabel volumeLabel = new JLabel();
        volumeLabel.setText("Volume");
        volumeLabel.setForeground(Color.WHITE);
        JSlider volumeSlider = new JSlider(JSlider.HORIZONTAL, 0, 100,
                fireBeatsPlugin.getMusicConfig().volume());
        volumeSlider.setBackground(Color.LIGHT_GRAY);
        volumeSlider.setName("volume");
        volumeSlider.addChangeListener((ChangeListener) this);
        volumePanel.add(volumeLabel);
        volumePanel.add(volumeSlider);

        // Remix Offset
        JLabel remixOffsetLabel = new JLabel();
        remixOffsetLabel.setText("Remix Offset");
        remixOffsetLabel.setForeground(Color.WHITE);
        JSlider remixOffsetSlider = new JSlider(JSlider.HORIZONTAL, 0, 100,
                fireBeatsPlugin.getMusicConfig().remixVolumeOffset());
        remixOffsetSlider.setBackground(Color.LIGHT_GRAY);
        remixOffsetSlider.setName("remixOffset");
        remixOffsetSlider.addChangeListener((ChangeListener) this);
        volumePanel.add(remixOffsetLabel);
        volumePanel.add(remixOffsetSlider);

        JPanel togglePanel = new JPanel();
        togglePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        togglePanel.setLayout(new GridLayout(0, 1));

        // Mute
        JLabel muteLabel = new JLabel();
        muteLabel.setText("Mute:");
        muteLabel.setForeground(Color.WHITE);
        JCheckBox muteCheckBox = new JCheckBox();
        muteCheckBox.setSelected(fireBeatsPlugin.getMusicConfig().mute());
        muteCheckBox.setForeground(Color.WHITE);
        muteCheckBox.setName("mute");
        muteCheckBox.addActionListener((ActionListener) this);
        togglePanel.add(new JSeparator());
        togglePanel.add(muteLabel);
        togglePanel.add(muteCheckBox);
        togglePanel.add(new JSeparator());

        // Show Track Name
        JLabel showTrackLabel = new JLabel();
        showTrackLabel.setText("Show Track Name:");
        showTrackLabel.setForeground(Color.WHITE);
        JCheckBox showTrackCheckBox = new JCheckBox();
        showTrackCheckBox.setSelected(fireBeatsPlugin.getMusicConfig().showCurrentTrackName());
        showTrackCheckBox.setForeground(Color.WHITE);
        showTrackCheckBox.setName("showTrackName");
        showTrackCheckBox.addActionListener((ActionListener) this);
        togglePanel.add(showTrackLabel);
        togglePanel.add(showTrackCheckBox);
        togglePanel.add(new JSeparator());

        // Play Original Music
        JLabel playOriginalLabel = new JLabel();
        playOriginalLabel.setText("Play Original When No Remix:");
        playOriginalLabel.setForeground(Color.WHITE);
        JCheckBox playOriginalCheckBox = new JCheckBox();
        playOriginalCheckBox.setSelected(fireBeatsPlugin.getMusicConfig().playOriginalIfNoRemix());
        playOriginalCheckBox.setForeground(Color.WHITE);
        playOriginalCheckBox.setName("playOriginal");
        playOriginalCheckBox.addActionListener((ActionListener) this);
        togglePanel.add(playOriginalLabel);
        togglePanel.add(playOriginalCheckBox);
        togglePanel.add(new JSeparator());

        volumePanel.add(togglePanel);

        add(volumePanel, BorderLayout.CENTER);
    }

    public void stateChanged(ChangeEvent e)
    {
        JSlider source = (JSlider)e.getSource();
        if (!source.getValueIsAdjusting()) {
            if (source.getName() == "volume")
            {
                log.info("Volume is " + source.getValue());
                if (source.getValue() < fireBeatsPlugin.getMusicConfig().remixVolumeOffset())
                {
                    fireBeatsPlugin.getMusicConfig().setVolume(fireBeatsPlugin.getMusicConfig().remixVolumeOffset());
                }
                else
                {
                    fireBeatsPlugin.getMusicConfig().setVolume(source.getValue());
                }
            }
            else if (source.getName() == "remixOffset")
            {
                log.info("Remix offset is " + source.getValue());
                fireBeatsPlugin.getMusicConfig().setRemixVolumeOffset(source.getValue());
            }

        }
    }

    public void actionPerformed(ActionEvent e)
    {
        JCheckBox source = (JCheckBox)e.getSource();
        if (source.getName() == "mute")
        {
            log.info("Value of mute is " + source.isSelected());
            fireBeatsPlugin.getMusicConfig().setMute(source.isSelected());
        }
        else if (source.getName() == "showTrackName")
        {
            log.info("Value of showTrackName is " + source.isSelected());
            fireBeatsPlugin.getMusicConfig().setShowCurrentTrackName(source.isSelected());
        }
        else if (source.getName() == "playOriginal")
        {
            log.info("Value of playOriginal is " + source.isSelected());
            fireBeatsPlugin.getMusicConfig().setPlayOriginalIfNoRemix(source.isSelected());
        }
    }
}
