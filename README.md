# Java Tetris

Java Tetris is an implementation of the classic Tetris game using Java and utilises the [BAGEL library](https://people.eng.unimelb.edu.au/mcmurtrye/bagel-doc/index.html?bagel/AbstractGame.html).

## Game Demo

<p align="center">
<img alt="Tetris demo" src="https://i.imgur.com/1JEUdcf.gif" width="300"/>
</p>

## Game Description

The game features a grid-based playing area where tetrominoes (shapes made up of four blocks) descend from the top of the screen. The player can move and rotate the falling tetrominoes to create horizontal lines with no gaps. When a line is completed, it is cleared from the grid, and the player earns points. As the game progresses, the tetrominoes fall faster, increasing the difficulty.

## Features

- Simple and intuitive controls:
    - Move the tetromino left: `←`
    - Move the tetromino right: `→`
    - Rotate the tetromino clockwise: `↑`
    - Rotate the tetromino counterclockwise: `Z`
    - Soft drop (faster descent): `↓`
    - Hard drop (instant drop): `SPACE`
    - Hold the current tetromino: `C`
    - Pause the game: `P`
    - Quit the game: `ESC`
- Visual indicators:
    - Display of the next tetrominoes in a queue on the right side of the screen
    - Display of the held tetromino above the queue
    - Display of the current score
    - Countdown before the game starts

## Getting Started

To run the Java Tetris game on your local machine, follow these steps:

1. Clone the repository or download the source code.
2. Open the project in an Integrated Development Environment (IDE) such as Eclipse or IntelliJ IDEA.
3. Build and compile the project.
4. Run the `javaTetris` class, which contains the `main` method.

## Dependencies

Java Tetris uses the [BAGEL library](https://people.eng.unimelb.edu.au/mcmurtrye/bagel-doc/index.html?bagel/AbstractGame.html), which provides a simple interface for creating games and graphical applications in Java. The library handles window creation, rendering, and user input.

## Acknowledgments

Java Tetris was inspired by the classic Tetris game and the Bagel library.

