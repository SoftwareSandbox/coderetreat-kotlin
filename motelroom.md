# MotelRoom Kata

This kata was inspired by episode 3 of De Mol 2023, a dutch-speaking belgian gameshow where deceit and bluffing matters a lot.

In this episode, contestants were put in motel rooms where they were each presented with a red button they could press and a tv monitor showing a message.

When a person in a room would press their button, the monitor would show a green screen saying "Your room will open in 1:00" (and start a 1 minute countdown).
At the same time, the other room's monitors would show a red screen saying "Room 1 will open in 1:00" (showing the same 1 minute countdown).

If another room would press their button, the monitor would turn to a green screen saying "Your room will open in 1:00" (and restart the 1 minute countdown).
The other room's monitors, including room 1, would show a red saying "Room 1 will open in 1:00" (showing the same, restarted, 1 minute countdown).

They'd be able to communicate between 2 rooms at the same time by using their room phone.

After a while there was a secondary countdown that added another condition: if the "room-timer" did not go off within the next 15 minutes, the room that pressed the button the least amount of times would open.

## Simplified rules
Given 2 rooms, Room 1 and Room 2

* [ ] When Room 1 presses the button, then the timer that will make Room 1 win is started.
* [ ] When Room 2 presses the button, then the timer that will make Room 1 win is reset and stopped.
* [ ] When Room 1 presses the button, then the timer that will make Room 2 win is reset and stopped.
* [ ] When Room 1’s timer is up, then Room 1 wins.
* [ ] When Room 2’s timer is up, then Room 2 wins.

## Extension with an end timer
* [ ] When there has been a total of 10 button presses (summing the button presses of Room 1 and Room 2), then an end timer starts.
* [ ] When neither Room’s timer has finished before the end timer finishes, then the Room with the least button presses wins.

## Extension with more than 2 rooms
Given 5 rooms

* [ ] When one Room presses the button, then the timers of the other Rooms are reset.
* [ ] When one Room's timer finishes, then that Room wins.
* [ ] When there has been a total of 25 button presses, then an end timer starts. We think a good metric to not have it last too long is 5 button presses per room. So if the timers are set to 1 minute, then it'll max. take 25 minutes before the end timer is started.
* [ ] When a Room's timer has not finished before the end timer finishes, then the Room with the least button presses still wins.