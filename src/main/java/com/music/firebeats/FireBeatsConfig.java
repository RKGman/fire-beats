package com.music.firebeats;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("firebeats")
public interface FireBeatsConfig extends Config
{
	@ConfigItem(
			keyName = "muteOwnAreaSounds",
			name = "Mute player area sounds",
			description = "Mute area sounds caused by yourself",
			position = 0
	)
	default boolean muteOwnAreaSounds()
	{
		return false;
	}

	@ConfigItem(
			keyName = "muteOtherAreaSounds",
			name = "Mute other players' area sounds",
			description = "Mute area sounds caused by other players",
			position = 1
	)
	default boolean muteOtherAreaSounds()
	{
		return false;
	}

	@ConfigItem(
			keyName = "muteOtherAreaNPCSounds",
			name = "Mute NPCs' area sounds",
			description = "Mute area sounds caused by NPCs",
			position = 2
	)
	default boolean muteNpcAreaSounds()
	{
		return false;
	}

	@ConfigItem(
			keyName = "muteOtherAreaEnvironmentSounds",
			name = "Mute environment area sounds",
			description = "Mute area sounds caused by neither NPCs nor players",
			position = 3
	)
	default boolean muteEnvironmentAreaSounds()
	{
		return false;
	}

	@ConfigItem(
			keyName = "mutePrayerSounds",
			name = "Mute prayer sounds",
			description = "Mute prayer activation and deactivation sounds",
			position = 4
	)
	default boolean mutePrayerSounds()
	{
		return false;
	}

	@ConfigItem(
			keyName = "showCurrentTrackName",
			name = "Show the current track name",
			description = "Displays the current track name without having to open the music tab.",
			position = 5
	)
	default boolean showCurrentTrackName() { return false; }

	@ConfigItem(
			keyName = "musicVolume",
			name = "",
			description = "",
			hidden = true
	)
	default int getMusicVolume()
	{
		return 0;
	}

	@ConfigItem(
			keyName = "musicVolume",
			name = "",
			description = "",
			hidden = true
	)
	void setMusicVolume(int vol);

	@ConfigItem(
			keyName = "soundEffectVolume",
			name = "",
			description = "",
			hidden = true
	)
	default int getSoundEffectVolume()
	{
		return 0;
	}

	@ConfigItem(
			keyName = "soundEffectVolume",
			name = "",
			description = "",
			hidden = true
	)
	void setSoundEffectVolume(int val);

	@ConfigItem(
			keyName = "areaSoundEffectVolume",
			name = "",
			description = "",
			hidden = true
	)
	default int getAreaSoundEffectVolume()
	{
		return 0;
	}

	@ConfigItem(
			keyName = "areaSoundEffectVolume",
			name = "",
			description = "",
			hidden = true
	)
	void setAreaSoundEffectVolume(int vol);
}
