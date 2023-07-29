# McDiver
Data structures and algorithms in Java course project. This project involves a two-stage game. The first stage, the seek stage, McDiver (the character)
only knows the neighboring tiles and the manhattan distance to the ring (goal). Thus, by using a DFS algorithm, we can attempt to minimize the path to 
the ring in the varying mazes, which will increase the bonus multiplier for the next stage.

The second stage, the scram stage, is one where the cave is collapsing and McDiver needs to reach the exit in x amount of steps. this stage is full of coins
with varying values and the goal is to collect as many coins with the highest value total and reach the exit with the given amount of steps.

This project tested me in GUI applications, Dijkstras algorithm, DFS and BFS, and gamestates. I also found it intriguing to attempt to navigate the scram
phase by having to set an arbitrary priority ratio for a coins value versus its distance. I also learned priorioty queues.

**TO RUN THIS PROJECT**
a6/src/game/main

Note, the main files which I modified are diver/McDiver.java, game/GUIcontrol.java, graph/ShortestPaths.java, tests/ShortestPathsTests.java

