/*
 * File: Reversi.java
 * Author: Brandon Hammel
 * Class: CS 165A, Winter 2016
 * Assignment: Machine Problem 2
 */

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

/*
 * Reversi class - reprsents a game of Reversi.
 */

public class Reversi {

	// !---------------------------- Class variables ----------------------------!

	/*
	 * Represent empty, dark, and light disks on the board
	 */
	private static int empty = 0;
	private static int dark = 1;
	private static int light = 2;

	// Specifies the depth for the minimax algorithm
	private static int MINIMAX_DEPTH = 2;

	// !-------------------------- Instance variables --------------------------!

	// The Reversi board
	private Board board;

	// The current player
	private Player currentPlayer;

	// The Dark player
	private Player darkPlayer;

	// The Light player
	private Player lightPlayer;

	// Holds the previous move played
	private String movePlayed = "--";

	/*
	 * Keeps track of how many times a player had no valid moves. If this reaches
	 * two, then both players had no valid moves and the game ends.
	 */
	private int noValidMovesCount;

	/*
	 * Holds the sides of the current move in which there are disks that can be
	 * reversed.
	 */
	private HashSet<String> sidesToChange;

	// !----------------------------- Constructors -----------------------------!

	/*
	 * Creates a new game of Reversi - sets up the board and assigns each player
	 * their color.
	 */
	public Reversi(int boardSize, boolean humanIsDark) {
		board = new Board(boardSize);
		noValidMovesCount = 0;

		if (humanIsDark) {
			darkPlayer = new Player("Dark", dark, "human");
			lightPlayer = new Player("Light", light, "COM");
		} else {
			darkPlayer = new Player("Dark", dark, "COM");
			lightPlayer = new Player("Light", light, "human");
		}

		currentPlayer = darkPlayer;
	}

	// !-------------------------- Getters and setters --------------------------!

	// Returns the current player
	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	// !-------------------------------- Methods --------------------------------!

	// Calculates the best possible move using the minimax algorithm
	public String calculateMove() {
		HashMap<String, HashSet<String>> validMoves
																			= new HashMap<String, HashSet<String>>();

		System.out.println(currentPlayer.getDarkOrLight() + " player ("
												+ currentPlayer.getHumanOrCOM()
												+ ") is calculating its next move... (this might "
												+ "take up to 30 seconds)");

		// Get all valid moves
		for (int i = 0; i < board.getSize(); i++) {
			for (int j = 0; j < board.getSize(); j++) {
				HashSet<String> sidesToChange
														= board.validMove(currentPlayer.getColor(), i, j);
				if (!sidesToChange.isEmpty()) {
					String moveString = String.valueOf((char) (j + 97))
															+ String.valueOf(i + 1);
					validMoves.put(moveString, sidesToChange);
				}
			}
		}

		// No valid moves
		if (validMoves.isEmpty()) {
			return null;
		}

		Player opponent;
		if (currentPlayer == darkPlayer) {
			opponent = lightPlayer;
		} else {
			opponent = darkPlayer;
		}

		int score = currentPlayer.getScore();
		int opponentScore = opponent.getScore();
		int opponentColor = opponent.getColor();

		HashSet<String> bestMoves = new HashSet<String>();

		// Best score so far starts off as worst possible score
		int best = -(board.getSize() * board.getSize()) - 1;

		for (String validMove : validMoves.keySet()) {
			/*
			 * alpha - best score so far for current player
			 * beta - best score so far for opponent
			 */
			int alpha = best;
			int beta = -best;
			int current = getCurrentScore(board, currentPlayer.getColor(),
																		opponentColor, true, validMove,
																		validMoves.get(validMove), score,
																		opponentScore, 0, alpha, beta,
																		MINIMAX_DEPTH);

			if (current > best) {
				best = current;
				bestMoves.clear();
				bestMoves.add(validMove);
			} else if (current == best) {
				bestMoves.add(validMove);
			}
		}

		String bestMove = null;
		int randomMove = new Random().nextInt(bestMoves.size());

		// If multiple best possible moves, choose one at random
		int currentMove = 0;
		for (String move : bestMoves) {
			if (currentMove == randomMove) {
				bestMove = move;
			}
			currentMove++;
		}

		sidesToChange = validMoves.get(bestMove);

		return bestMove;
	}

	/*
	 * Determines whether or not the game is over - if the combined score of both
	 * players is equal to the total number of squares on the board, then there
	 * are no more possible moves and the game is over.
	 */
	public boolean gameOver() {
		if (darkPlayer.getScore() + lightPlayer.getScore()
				== board.getSize() * board.getSize()) {
			return true;
		}

		return false;
	}

	/*
	 * Calculates the best possible score for a given move using the minimax
	 * algorithm.
	 */
	public int getCurrentScore(Board board, int color, int opponentColor,
														 boolean currentPlayerMove, String move,
														 HashSet<String> sidesToChange, int score,
														 int opponentScore, int count, int alpha, int beta,
														 int depth) {
		if (depth == 0) {
			return score - opponentScore;
		}

		depth--;

		/*
		 * If this path of moves leads to both players having no valid moves, return
		 * current score.
		 */
		if (count == 2) {
			return score - opponentScore;
		}

		// Create a copy of the current board so it doesn't get affected
		Board updatedBoard = new Board(board);

		if (move != null) {
			int row = Integer.parseInt(move.substring(1)) - 1;
			int column = Character.getNumericValue(move.charAt(0))
										- Character.getNumericValue('a');

			int numChanges;
			if (currentPlayerMove) {
				numChanges = updatedBoard.update(color, row, column, sidesToChange);
				score += numChanges + 1;
				opponentScore -= numChanges;
			} else {
				numChanges = updatedBoard.update(opponentColor, row, column,
																				 sidesToChange);
				score -= numChanges;
				opponentScore += numChanges + 1;
			}
		}

		if (score + opponentScore
				== updatedBoard.getSize() * updatedBoard.getSize()) {
			return score - opponentScore;
		}

		if (currentPlayerMove) {
			currentPlayerMove = false;
		} else {
			currentPlayerMove = true;
		}

		HashMap<String, HashSet<String>> validMoves
																			= new HashMap<String, HashSet<String>>();

		for (Integer possibleMove : updatedBoard.getPossibleMoves()) {
			int row = possibleMove.intValue() / updatedBoard.getSize();
			int column = possibleMove.intValue() % updatedBoard.getSize();

			HashSet<String> sides = new HashSet<String>();
			if (currentPlayerMove) {
				sides = updatedBoard.validMove(color, row, column);
			} else {
				sides = updatedBoard.validMove(opponentColor, row, column);
			}

			if (!sides.isEmpty()) {
				String moveString = String.valueOf((char) (column + 97))
															+ String.valueOf(row + 1);
				validMoves.put(moveString, sides);
			}
		}

		// Player has no valid moves, increment counter and switch to next player
		if (validMoves.isEmpty()) {
			count++;

			if (currentPlayerMove) {
				currentPlayerMove = false;
			} else {
				currentPlayerMove = true;
			}

			return getCurrentScore(updatedBoard, color, opponentColor,
														 currentPlayerMove, null, null, score,
														 opponentScore, count, alpha, beta, depth);
		}

		/*
		 * Set best score for current player to worst possible score, and set best
		 * score for opponent to best possible score.
		 */
		int best;
		if (currentPlayerMove) {
			best = -(updatedBoard.getSize() * updatedBoard.getSize()) - 1;
		} else {
			best = (updatedBoard.getSize() * updatedBoard.getSize()) + 1;
		}

		for (String validMove : validMoves.keySet()) {
			int current = getCurrentScore(updatedBoard, color, opponentColor,
																		currentPlayerMove, validMove,
																		validMoves.get(validMove), score,
																		opponentScore, 0, alpha, beta, depth);

			if (currentPlayerMove) {
				best = Math.max(best, current);
				alpha = Math.max(alpha, best);

				/*
				 * Alpha-Beta pruning - if opponent's best score is less than or equal
				 * to current player's best so far, play will never reach further down
				 * this path.
				 */
				if (beta <= alpha) {
					break;
				}
			} else {
				best = Math.min(best, current);
				beta = Math.min(beta, best);

				// Alpha-Beta pruning - same as above
				if (beta <= alpha) {
					break;
				}
			}
		}

		return best;
	}

	// Determines whether the player of the given color has any valid moves
	public boolean hasValidMoves(int color) {
		for (Integer possibleMove : board.getPossibleMoves()) {
			int row = possibleMove.intValue() / board.getSize();
			int column = possibleMove.intValue() % board.getSize();

			if (!board.validMove(color, row, column).isEmpty()) {
				return true;
			}
		}

		return false;
	}

	/*
	 * Updates the board with the given move and color, and adjusts each player's
	 * score accordingly.
	 */
	public void move(int color, int row, int column) {
		int numChanges = board.update(color, row, column, sidesToChange);

		if (color == dark) {
			darkPlayer.addToScore(numChanges + 1);
			lightPlayer.subtractFromScore(numChanges);
		} else {
			darkPlayer.subtractFromScore(numChanges);
			lightPlayer.addToScore(numChanges + 1);
		}

		movePlayed = String.valueOf((char) (column + 97)) + String.valueOf(row + 1);
	}

	// Moves control to the other player
	public void nextPlayer() {
		if (currentPlayer == darkPlayer) {
			currentPlayer = lightPlayer;
		} else {
			currentPlayer = darkPlayer;
		}
	}

	// Determines whether both players had no valid moves
	public boolean noMoreMoves() {
		if (noValidMovesCount == 2) {
			return true;
		}

		return false;
	}

	// When a player has no valid moves, this increments the counter
	public void noValidMoves() {
		noValidMovesCount++;
	}

	// Prints out the board
	public void printBoard() {
		board.print();
	}

	// Prints the last move played, whose turn it is, and the current score
	public void printInfo() {
		System.out.println("Move played: " + movePlayed);
		System.out.println(currentPlayer.getDarkOrLight() + " player ("
												+ currentPlayer.getHumanOrCOM() + ") plays now");
		System.out.println("Score: Light " + lightPlayer.getScore() + " - Dark "
												+ darkPlayer.getScore());
	}

	// Prints out which player won, or a tie
	public void printResults() {
		if (darkPlayer.getScore() > lightPlayer.getScore()) {
			System.out.println();
			System.out.println("Dark player (" + darkPlayer.getHumanOrCOM()
													+ ") wins!");
		} else if (lightPlayer.getScore() > darkPlayer.getScore()) {
			System.out.println();
			System.out.println("Light player (" + lightPlayer.getHumanOrCOM()
													+ ") wins!");
		} else {
			System.out.println();
			System.out.println("It's a tie!");
		}
	}

	/*
	 * Resets the counter of no valid moves to zero. This must be called after
	 * each player's move in case that move gives the other player a valid move.
	 */
	public void resetNoValidMovesCount() {
		noValidMovesCount = 0;
	}

	/*
	 * Determines whether the given move is valid for the player of the given
	 * color.
	 */
	public boolean validMove(int color, int row, int column) {
		sidesToChange = board.validMove(color, row, column);
		if (sidesToChange.isEmpty()) {
			return false;
		}

		return true;
	}
}
