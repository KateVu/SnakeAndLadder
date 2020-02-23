# SnakeAndLadderSnake and Ladder game was built by Java and Java Swing.
The game will followed these rules: The goal is to be the first player to reach the end of the board by moving across the board from square one to the final square.
1.	A human player plays with the computer.
2.	Decide who plays first: each player rolls the die. Whoever gets the bigger face value plays first.
3.	Take turns to roll the dice. Move the game pieces or counter forward the number of spaces shown on the dice. For example, if you roll a two, move your piece to square two. On your next turn, if you roll a five, move your piece forward five squares, ending up on square seven.
4.	If your piece lands at the bottom of a ladder, you move up to the top of the ladder.
5.	If your piece lands on the head of a snake, you must slide down to the bottom of the snake.
6.	Take an extra turn if you roll a six. If you roll a six, then you get an extra turn. First, move your piece forward six squares and then roll the die again.
7.	Land exactly on the last square to win. If you roll too high, your piece will stay and not move for this turn. You can only win by rolling the exact number needed to land on the last square.
Handle the logic of the application: 
The whole application was handled by splitting into 7 states as below: 
 
Depend on the state of the application, the appropriate functions are called to handle the state

Build the GUI of the application:
The objects in Swing package which are used are JFrame, JPanel and Canvas. 
Below is the very first screen of the game 
![Image of Yaktocat](https://octodex.github.com/images/yaktocat.png)

Figure1: Very first screen of the game

The whole GUI was established by using JFrame, then adding the other components into this JFrame:
-	JPanel including image of a Dice, a message and a Start Button
-	Main board in the middle of the Frame was built by using Canvas 
-	Information message was added below the main board
-	Legends is on the very left side to show the meaning of the images which are used in the main board
To emphasize who is currently play, there are some animations have been added into the project. The piece of the current player will spin or jump inside the space. The classes are used to handle all animations are SpriteSheed and SpriteTransition.
