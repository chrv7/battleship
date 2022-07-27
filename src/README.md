# BATTLESHIP #
The classic game of sea battle for two players in the console.  
To run the game, you need to compile the code and run it in the windows console.  
If you start the game through the IDE, then when changing moves,
the console window will not be cleared and the opponent will see the location of your ships.  
When the game starts, players place their ships in sequence on a 10x10 field.
### **Playing field example** ###
      1 2 3 4 5 6 7 8 9 10
    A ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
    B ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
    C ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
    D ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
    E ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
    F ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
    G ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
    H ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
    I ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
    J ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

#### There are 5 types of ships in the game, differing in size: ####
    AIRCRAFT_CARRIER - 5 cells
    BATTLESHIP - 4 cells
    SUBMARINE - 3 cells
    CRUISER - 3 cells
    DESTROYER - 2 cells
#### The game prompts which ship will be placed at the moment, for example: ####
    Enter the coordinates of the Aircraft Carrier (5 cells):
#### Coordinate input can be done in different ways: ####
    A1A5
    c5c8
    A7 A9
    J1        J3

#### In case you specified the wrong ship length, a warning will be issued: ####   
    Error! Wrong length of the Destroyer! Try again:

#### If you specify coordinate, which is used, a warning will be issued: ####
    Error! You placed it too close to another one. Try again: