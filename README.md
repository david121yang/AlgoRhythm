# TeamA
Sprint 2:
Our game is mostly functional, but it just has 4 default songs.
We have an algorithm working to some extent, but we could not import it into Android Studio to work along with our app, so we used it externally to create a default library.
Sea Shanty 2 is the short one for testing purposes, Sea Shanty 2 Hard... is the one we created through the algorithm, and the other 2 are more intense songs also made with the algorithm.
Here are some differences from our wireframe.
Our rhythm meter does not use a gradient, as getting a custom style took a lot of time and we couldn’t figure out how to get a 3 color gradient.
Hold notes were too hard to generate programatically in the algorithm, and hard to store in text files, so we left them out for now.
We didn't include a pause function - android functionality doesn’t allow a straight up pause given the way we built our app, but restarting a song is doable. Might be possible, but would require major refactoring
The edit button requires a level editor, so we didn’t include it in song select.
Sound effects for tapping and such are not included, as we decided it takes away too much from the song.
The store page is a stretch goal, and we didn’t get to it. So, the button doesn’t lead anywhere.

Sprint 1:
So far, our app mainly only covers core gameplay features.
We have a title screen that links to a song selection of two hardcoded in songs, the first of which, Sea Shanty 2, gives us a proper level, while the second is more of a barebones test level.
Our song list will expand and show info on high score and combo, as well as being able to be started and deleted, while ranking is not implemented yet.
We also have not quite sorted out our song list, but that shouldn't be too difficult, as we can jsut add a comparable to a SongItem class.
Our basic gameplay, however, is mostly done. We have notes that come in at the right time, and a button that will make the oncoming note disappear.
If pressed at the right time it will add to score and combo systems, but if not they will not add to score and reset combo.
We also store max combo for our eventual victory screen, which has yet to be implemented.
In addition for hitting and missing notes, we have our status bar at the bottom, which will go up if we hit notes and go down if we miss. We have not implemented a failure screen yet, so the song will not stop if we hit the bottom of the bar.
We also have added swipes in different directions, while in our presentation we only had tap support at the time.

Overall a large chunk of our gameplay is done, with the best way to test this being selecting Sea Shanty 2 on the song selection screen.
Things we are missing at the moment to be aware of is overall style/layout and some error handling.
