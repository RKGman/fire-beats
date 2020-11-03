package com.music.firebeats;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

@ConfigGroup("firebeats")
public interface FireBeatsConfig extends Config
{
	@ConfigItem(
			keyName = "mute",
			name = "Mute",
			description = "Mutes everything.",
			position = 0
	)
	default boolean mute()
	{
		return false;
	}

	@ConfigItem(
			keyName = "mute",
			name = "Mute",
			description = "Mutes everything.",
			hidden = true
	)
	void setMute(boolean value);

	@Range(
			max = 100
	)
	@ConfigItem(
			keyName = "volume",
			name = "Volume",
			description = "Specify the volume.",
			position = 1
	)
	default int volume()
	{
		return 100;
	}

	@ConfigItem(
			keyName = "volume",
			name = "Volume",
			description = "",
			hidden = true
	)
	void setVolume(int val);

	@ConfigItem(
			keyName = "playOriginalIfNoRemix",
			name = "Play original track if no remix",
			description = "Play the original track if the remix link is broken or does not exist.",
			position = 2
	)
	default boolean playOriginalIfNoRemix()
	{
		return true;
	}

	@ConfigItem(
			keyName = "playOriginalIfNoRemix",
			name = "Play original track if no remix",
			description = "Play the original track if the remix link is broken or does not exist.",
			hidden = true
	)
	void setPlayOriginalIfNoRemix(boolean value);

	@ConfigItem(
			keyName = "showCurrentTrackName",
			name = "Show the current track name",
			description = "Displays the current track name without having to open the music tab.",
			position = 3
	)
	default boolean showCurrentTrackName() { return true; }

	@ConfigItem(
			keyName = "showCurrentTrackName",
			name = "Show the current track name",
			description = "Displays the current track name without having to open the music tab.",
			hidden = true
	)
	void setShowCurrentTrackName(boolean value);

	@Range(
			max = 100
	)
	@ConfigItem(
			keyName = "remixVolumeOffset",
			name = "Remix volume offset",
			description = "Amount to decrease volume of remix to match in-game volume.",
			position = 4
	)
	default int remixVolumeOffset()
	{
		return 45;
	}

	@ConfigItem(
			keyName = "remixVolumeOffset",
			name = "Remix volume offset",
			description = "",
			hidden = true
	)
	void setRemixVolumeOffset(int val);

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
