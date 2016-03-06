/*
 * File: Main.java
 * Author: Brandon Hammel
 * Class: CS 165A, Winter 2016
 * Assignment: Machine Problem 2
 */

/*
 * Main class - application driver.
 */

public class Main {
	public static void main(String[] args) {
		int boardSize = 8;
		boolean humanIsDark = true;

		for (int i = 0; i < args.length; i++) {
			switch (args[i].charAt(0)) {
				case '-':
					if (args[i].length() != 2) {
						System.err.println("Error: Invalid option: " + args[i]);
						System.exit(1);
					}

					switch (args[i].charAt(1)) {
						case 'n':
							boardSize = Integer.parseInt(args[i + 1]);
							if (boardSize % 2 != 0 || boardSize < 4 || boardSize > 26) {
								System.err.println("Error: Board size must be an even "
																		+ "number between 4 and 26");
								System.exit(1);
							}
							break;
						case 'l':
							humanIsDark = false;
							break;
						default:
							System.err.println("Error: Invalid option: " + args[i]);
							System.exit(1);
							break;
					}
					break;
			}
		}

		Reversi game = new Reversi(boardSize, humanIsDark);

		game.printBoard();
		game.printInfo();

		while (true) {
			if (game.noMoreMoves()) {
				System.out.println("No valid moves left for either player. Game over.");
				break;
			}

			if (game.getCurrentPlayer().getHumanOrCOM().equals("human")
					&& !game.hasValidMoves(game.getCurrentPlayer().getColor())) {
				System.out.println(game.getCurrentPlayer().getDarkOrLight()
														+ " player has no valid moves");
				game.noValidMoves();
				game.nextPlayer();
				continue;
			} else {
				String currentMove = null;
				if (game.getCurrentPlayer().getHumanOrCOM().equals("human")) {
					currentMove = game.getCurrentPlayer().getMove();
				} else {
					currentMove = game.calculateMove();
					if (currentMove == null) {
						System.out.println(game.getCurrentPlayer().getDarkOrLight()
																+ " player has no valid moves");
						game.noValidMoves();
						game.nextPlayer();
						continue;
					}
				}

				game.resetNoValidMovesCount();
				int row, column;

				try {
					row = Integer.parseInt(currentMove.substring(1)) - 1;
					column = Character.getNumericValue(currentMove.charAt(0))
										- Character.getNumericValue('a');
				} catch (NumberFormatException ex) {
					System.err.println("Error: Move \"" + currentMove
															+ "\" not recognized, please try again");
					continue;
				}

				if (game.getCurrentPlayer().getHumanOrCOM().equals("human")
						&& !game.validMove(game.getCurrentPlayer().getColor(), row, column)) {
					String moveString = String.valueOf((char) (column + 97))
																+ String.valueOf(row + 1);
					System.out.println("Error: You tried an invalid move \""
															+ moveString + "\", please try another move");
				} else {
					game.move(game.getCurrentPlayer().getColor(), row, column);

					game.nextPlayer();
					game.printBoard();
					game.printInfo();

					if (game.gameOver()) {
						System.out.println("Board is full. Game over.");
						break;
					}
				}
			}
		}

		game.printResults();
	}
}
