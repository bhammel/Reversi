/*
 * File: Board.java
 * Author: Brandon Hammel
 * Class: CS 165A, Winter 2016
 * Assignment: Machine Problem 2
 */

import java.util.HashSet;

/*
 * Board class - represents a Reversi board.
 */

public class Board {

	// !---------------------------- Class variables ----------------------------!

	/*
	 * Represent empty, dark, and light disks on the board
	 */
	private static int empty = 0;
	private static int dark = 1;
	private static int light = 2;

	// !-------------------------- Instance variables --------------------------!

	/*
	 * The actual board as a two-dimensional int array. Each slot will hold one of
	 * three values at any given time - empty (0), dark (1), or light (2)
	 */
	private int[][] board;

	/*
	 * The weights of each board slot - used to prioritize edge moves over other
	 * moves.
	 */
	private int[][] weights;

	// The size of one side of the board
	private int boardSize;

	// Holds all current possible moves, regardless of validity
	private HashSet<Integer> possibleMoves;

	// !----------------------------- Constructors -----------------------------!

	/*
	 * Creates a new Board given its size, and sets up the initial configuration
	 * of the board.
	 */
	public Board(int boardSize) {
		this.boardSize = boardSize;
		board = new int[boardSize][boardSize];
		possibleMoves = new HashSet<Integer>();

		int one = (boardSize / 2) - 1;
		int two = boardSize / 2;

		board[one][one] = light;
		board[one][two] = dark;
		board[two][one] = dark;
		board[two][two] = light;

		updatePossibleMoves(one, one);
		updatePossibleMoves(one, two);
		updatePossibleMoves(two, one);
		updatePossibleMoves(two, two);

		weights = new int[boardSize][boardSize];

		int current = 0;
		while (current != boardSize - current) {
			for (int i = current; i < boardSize - current; i++) {
				for (int j = current; j < boardSize - current; j++) {
					if ((i == 0 && j == 0)
							|| (i == boardSize - 1 && j == boardSize - 1)
							|| (i == 0 && j == boardSize - 1)
							|| (i == boardSize - 1 && j == 0)) {
						weights[i][j] = boardSize;
					} else {
						weights[i][j] = boardSize / 2 - current;
					}
				}
			}
			current++;
		}
	}

	/*
	 * Copy constructor - creates a new Board that is a copy of another. This is
	 * used when performing minimax in order to maintain different possible states
	 * of the board given different sequences of moves without affecting the
	 * current board.
	 */
	public Board(Board board) {
		boardSize = board.getSize();
		this.board = new int[boardSize][boardSize];
		possibleMoves = new HashSet<Integer>(board.getPossibleMoves());

		int[][] otherBoard = board.getBoard();

		for (int i = 0; i < boardSize; i++) {
			System.arraycopy(otherBoard[i], 0, this.board[i], 0, boardSize);
		}

		weights = new int[boardSize][boardSize];

		int current = 0;
		while (current != boardSize - current) {
			for (int i = current; i < boardSize - current; i++) {
				for (int j = current; j < boardSize - current; j++) {
					if ((i == 0 && j == 0)
							|| (i == boardSize - 1 && j == boardSize - 1)
							|| (i == 0 && j == boardSize - 1)
							|| (i == boardSize - 1 && j == 0)) {
						weights[i][j] = boardSize;
					} else {
						weights[i][j] = boardSize / 2 - current;
					}
				}
			}
			current++;
		}
	}

	// !-------------------------- Getters and setters --------------------------!

	// Returns the board's configuration array
	public int[][] getBoard() {
		return board;
	}

	// Returns the board's size
	public int getSize() {
		return boardSize;
	}

	// Returns the board's current possible moves
	public HashSet<Integer> getPossibleMoves() {
		return possibleMoves;
	}

	// Returns the associated weight at a given board location
	public int getWeight(int row, int column) {
		return weights[column][row];
	}

	// !-------------------------------- Methods --------------------------------!

	/*
	 * Updates the board's current possible moves given the coordinates of a new
	 * move.
	 */
	public void updatePossibleMoves(int row, int column) {
		possibleMoves.remove(Integer.valueOf(row * boardSize + column));

		for (int i = row - 1; i < row + 2; i++) {
			for (int j = column - 1; j < column + 2; j++) {
				if (i >= 0 && j >= 0 && i < boardSize && j < boardSize
						&& board[i][j] == empty) {
					possibleMoves.add(Integer.valueOf(i * boardSize + j));
				}
			}
		}
	}

	// Prints out the board using basic ASCII art
	public void print() {
		System.out.print("  ");

		for (int i = 0; i < boardSize; i++) {
			System.out.print("   " + (char) (i + 97));
		}

		System.out.println();
		System.out.print("   +");

		for (int i = 0; i < boardSize; i++) {
			System.out.print("---+");
		}

		System.out.println();

		for (int i = 0; i < boardSize; i++) {
			System.out.format("%2d |", (i + 1));

			for (int j = 0; j < boardSize; j++) {
				if (board[i][j] == dark) {
					System.out.print(" D |");
				} else if (board[i][j] == light) {
					System.out.print(" L |");
				} else {
					System.out.print("   |");
				}
			}

			System.out.println();
			System.out.print("   +");

			for (int j = 0; j < boardSize; j++) {
				System.out.print("---+");
			}

			System.out.println();
		}
	}

	/*
	 * Adds a move to the board, and reverses any disks that are captured by this
	 * move. Returns the number of disks that were reversed.
	 */
	public int update(int color, int row, int column,
										HashSet<String> sidesToChange) {
		board[row][column] = color;
		updatePossibleMoves(row, column);

		int numChanges = 0;

		// Compute numChanges from left disks
		if (sidesToChange.contains("Left")) {
			for (int i = 1; i < boardSize; i++) {
				if (board[row][column - i] == color) {
					break;
				}

				board[row][column - i] = color;
				numChanges++;
			}
		}

		// Compute numChanges from right disks
		if (sidesToChange.contains("Right")) {
			for (int i = 1; i < boardSize; i++) {
				if (board[row][column + i] == color) {
					break;
				}

				board[row][column + i] = color;
				numChanges++;
			}
		}

		// Compute numChanges from top disks
		if (sidesToChange.contains("Top")) {
			for (int i = 1; i < boardSize; i++) {
				if (board[row - i][column] == color) {
					break;
				}

				board[row - i][column] = color;
				numChanges++;
			}
		}

		// Compute numChanges from bottom disks
		if (sidesToChange.contains("Bottom")) {
			for (int i = 1; i < boardSize; i++) {
				if (board[row + i][column] == color) {
					break;
				}

				board[row + i][column] = color;
				numChanges++;
			}
		}

		// Compute numChanges from top-left disks
		if (sidesToChange.contains("TopLeft")) {
			for (int i = 1; i < boardSize; i++) {
				if (board[row - i][column - i] == color) {
					break;
				}

				board[row - i][column - i] = color;
				numChanges++;
			}
		}

		// Compute numChanges from top-right disks
		if (sidesToChange.contains("TopRight")) {
			for (int i = 1; i < boardSize; i++) {
				if (board[row - i][column + i] == color) {
					break;
				}

				board[row - i][column + i] = color;
				numChanges++;
			}
		}

		// Compute numChanges from bottom-left disks
		if (sidesToChange.contains("BottomLeft")) {
			for (int i = 1; i < boardSize; i++) {
				if (board[row + i][column - i] == color) {
					break;
				}

				board[row + i][column - i] = color;
				numChanges++;
			}
		}

		// Compute numChanges from bottom-right disks
		if (sidesToChange.contains("BottomRight")) {
			for (int i = 1; i < boardSize; i++) {
				if (board[row + i][column + i] == color) {
					break;
				}

				board[row + i][column + i] = color;
				numChanges++;
			}
		}

		return numChanges;
	}

	/*
	 * Determines if there are any disks that can be reversed by the player of the
	 * given color and the given move. If so, returns which sides they are located
	 * on; otherwise, returns an empty set.
	 */
	public HashSet<String> validMove(int color, int row, int column) {
		HashSet<String> sidesToChange = new HashSet<String>();

		if (row < 0 || column < 0 || row > boardSize - 1 || column > boardSize - 1) {
			return sidesToChange;
		}

		if (board[row][column] != empty) {
			return sidesToChange;
		}

		int opponentColor;
		if (color == dark) {
			opponentColor = light;
		} else {
			opponentColor = dark;
		}

		// Check left side
		if (column - 2 >= 0 && board[row][column - 1] == opponentColor) {
			for (int i = 2; i < column + 1; i++) {
				if (board[row][column - i] == empty) {
					break;
				} else if (board[row][column - i] == color) {
					sidesToChange.add("Left");
					break;
				}
			}
		}

		// Check right side
		if (column + 2 < boardSize && board[row][column + 1] == opponentColor) {
			for (int i = 2; i < boardSize - column; i++) {
				if (board[row][column + i] == empty) {
					break;
				} else if (board[row][column + i] == color) {
					sidesToChange.add("Right");
					break;
				}
			}
		}

		// Check top side
		if (row - 2 >= 0 && board[row - 1][column] == opponentColor) {
			for (int i = 2; i < row + 1; i++) {
				if (board[row - i][column] == empty) {
					break;
				} else if (board[row - i][column] == color) {
					sidesToChange.add("Top");
					break;
				}
			}
		}

		// Check bottom side
		if (row + 2 < boardSize && board[row + 1][column] == opponentColor) {
			for (int i = 2; i < boardSize - row; i++) {
				if (board[row + i][column] == empty) {
					break;
				} else if (board[row + i][column] == color) {
					sidesToChange.add("Bottom");
					break;
				}
			}
		}

		// Check top-left corner
		if (row - 2 >= 0 && column - 2 >= 0
				&& board[row - 1][column - 1] == opponentColor) {
			for (int i = 2; i < Math.min(row + 1, column + 1); i++) {
				if (board[row - i][column - i] == empty) {
					break;
				} else if (board[row - i][column - i] == color) {
					sidesToChange.add("TopLeft");
					break;
				}
			}
		}

		// Check top-right corner
		if (row - 2 >= 0 && column + 2 < boardSize
				&& board[row - 1][column + 1] == opponentColor) {
			for (int i = 2; i < Math.min(row + 1, boardSize - column); i++) {
				if (board[row - i][column + i] == empty) {
					break;
				} else if (board[row - i][column + i] == color) {
					sidesToChange.add("TopRight");
					break;
				}
			}
		}

		// Check bottom-left corner
		if (row + 2 < boardSize && column - 2 >= 0
				&& board[row + 1][column - 1] == opponentColor) {
			for (int i = 2; i < Math.min(boardSize - row, column + 1); i++) {
				if (board[row + i][column - i] == empty) {
					break;
				} else if (board[row + i][column - i] == color) {
					sidesToChange.add("BottomLeft");
					break;
				}
			}
		}

		// Check bottom-right corner
		if (row + 2 < boardSize && column + 2 < boardSize
				&& board[row + 1][column + 1] == opponentColor) {
			for (int i = 2; i < Math.min(boardSize - row, boardSize - column); i++) {
				if (board[row + i][column + i] == empty) {
					break;
				} else if (board[row + i][column + i] == color) {
					sidesToChange.add("BottomRight");
					break;
				}
			}
		}

		return sidesToChange;
	}
}
