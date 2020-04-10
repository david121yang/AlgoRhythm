# TeamA
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