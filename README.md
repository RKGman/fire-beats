# Fire Beats


This plugin plays a remix of a track that is currently playing in game. 

## Using the Fire Beats plugin

After installing the plugin, there should be a Fire Beats icon in the Runelite sidebar.  When you click it, the controls panel will appear.

![Alt Text](http://rknako.com/wp-content/uploads/2020/11/rl_rb_1.1.2_controls.gif)

### Track Remix Tagging

In the Music tab, tracks containing a remix link will be colored cyan.

![Alt Text](http://rknako.com/wp-content/uploads/2020/11/rl_rb_1.1.2_tags.gif)

## Settings (Notes)

### Volume Slider
The Volume slider controls the volume remixes and original game music (not sound effects and area sounds).

### Remix Offset
The Remix Offset slider controls the amount of volume to reduce from the remix track for the sake of mixing / blanacing to match the volume of the original music.  
  - Note that this also defines the lowest possible value for the Volume setting.
  
### Mute
The Mute checkbox will mute all music in game.
  - Note that this still does not include sound effects and area sounds.
  
### Loop (Radio Button)
This is the default play state for the plugin.  Tracks will loop indefinitely.  

### Shuffle Mode (Radio Button)
When a track ends, randomly select another available remix to begin playing.

### Shuffle to Next Track (Button)
Immediately skip to a new randomly selected remix.  This forces the plugin into Shuffle Mode.
  
### Show Area's Track Name
The Show Area's Track Name checkbox makes the box at the top of the overlay visible or invisible.  

![Alt Text](http://rknako.com/wp-content/uploads/2020/11/rl_rb_1.1.2_showTrack.gif)

### Play Original When No Remix
The Play Original When No Remix checkbox will allow or disallow the original music to play if a remix link is not found for the original track.
  - Basically, if you only want to hear remixes and not original tracks, you can uncheck this.
  
### Update Track List (Button)
This button will force the plugin to update and overwrite the listing of tracks from the official repository

### Auto Update List From Repo
The Auto Update List From Repo checkbox will enable or disable automatically overwriting the listing of tracks pulled from the official repository when the client starts.
  - If you want to manually have control over the links, you will want to disable this.
  
## Credit to producers

A message will appear in the game chat when a remix is played, showing who produced the remix.

![Alt Text](http://rknako.com/wp-content/uploads/2020/11/rl_rb_1.1.2_playTrack.gif)

## Customizing the track list

NOTE: First ensure that you have unchecked the Auto Update List From Repo checkbox in the settings.

There are three columns in the CSV.  

The first is the name of the in game track (which should not be modified).

The second is the link to an anonfile download page.

The third is the producing artist to credit in the game chat.

Links must be the download page of a file uploaded to https://anonfiles.com/ 

For example: `https://anonfiles.com/T2Fbf7n5pd/OSRSBeatz_-_Wilderness_mp3`

    This is not a direct mp3 link.  The direct link will be scraped later. 

Upload whatever mp3 file you want to anonfiles, then replace the link in the CSV. 

The file you upload must be an mp3.

## Known issues

- The controls do not apply to the log in screen.
- In some cases the Current Track overlay display doesn't show up; this may be due to a conflict with other external plugins.
- Mute kills shuffle.  Button to shuffle to new track must be clicked to resume.
- Track name with commas in name broken.
- Small resolution / window size cuts off control panel.
- Multiple failures to get mp3 causes game tick freezes in some cases.
- If anonfiles goes down or download speeds are slow, streams are a pain to listen to (see features in developement).

## Features in developement

- Ability to use the SoundCloud API to stream tracks.  Not sure why SoundCloud dropped Java support for the API, but I could sure use some help.  I am open to suggestions on other mp3 streaming pipelines.

