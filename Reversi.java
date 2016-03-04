/*
 * File: Reversi.java
 * Author: Brandon Hammel
 * Class: CS 165A, Winter 2016
 * Assignment: Machine Problem 2
 */

// Game class

public class Reversi {
	public static void main(String[] args) {
		int boardSize = 8;
		boolean humanIsDark = true;

		int i = 0;
		for (String arg : args) {
			if (arg.equals("-n")) {
				boardSize = Integer.parseInt(args[i + 1]);
				if (boardSize % 2 != 0 || boardSize > 26 || boardSize < 4) {
					System.err.println("Board size must be an even number between 4 and 26");
					System.exit(1);
				}
			} else if (arg.equals("-l")) {
				humanIsDark = false;
			}
			i++;
		}

		ReversiGame game = new ReversiGame(boardSize, humanIsDark);
		game.start();
	}
}
