# Fire Beats


This plugin plays a remix of a track that is currently playing in game. 

## Using the Fire Beats plugin

Once the plugin is installed, there shouhld be a Fire Beats icon in the Runelite sidebar.  When you click it, the controls panel will appear.

![Alt Text](http://rknako.com/wp-content/uploads/2020/11/rl_fire_beats_controlMenu.gif)

## Settings (Notes)

### Volume Slider
The Volume slider controls the volume remixes and original game music (not sound effects and area sounds).

### Remix Offset
The Remix Offset slider controls the amount of volume to reduce from the remix track for the sake of mixing / blanacing to match the volume of the original music.  
  - Note that this also defines the lowest possible value for the Volume setting.
  
### Mute
The Mute checkbox will mute all music in game.
  - Note that this still does not include sound effects and area sounds.
  
### Show Track Name
The Show Track Name checkbox makes the box at the top of the overlay visible or invisible.  

![Alt Text](http://rknako.com/wp-content/uploads/2020/11/rl_fire_beats_showTrackName.gif)

### Play Original When No Remix
The Play Original When No Remix checkbox will allow or disallow the original music to play if a remix link is not found for the original track.
  - Basically, if you only want to hear remixes and not original tracks, you can uncheck this.
  
## Credit to producers

A message will appear in the game chat when a remix is played, showing who produced the remix.

![Alt Text](http://rknako.com/wp-content/uploads/2020/11/rl_fire_beats_remixInfo.gif)

## Customizing the track list

Links for tracks (in mp3 format) are pulled from a local CSV which resides in the user's local .runelite folder.

There are three columns in the CSV.  

The first is the name of the in game track (which should not be modified).

The second is the link to an mp3 file.

The third is the producing artist for creditting in the game chat.

A user can modify this CSV with custom mp3 links for any song.

## Known issues

- The controls do not apply to the log in screen.

## Features in developement

- Ability to update the CSV without having to delete the file in the local directory first.
