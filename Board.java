/*
 * File: Board.java
 * Author: Brandon Hammel
 * Class: CS 165A, Winter 2016
 * Assignment: Machine Problem 2
 */

import java.util.HashSet;

public class Board {

	// !--- Class variables ---!
	private static int empty = 0;
	private static int dark = 1;
	private static int light = 2;

	// !--- Instance variables ---!
	private int[][] board;
	private int boardSize;
	private HashSet<Integer> validMoves;

	// !--- Constructors ---!
	public Board(int boardSize) {
		this.boardSize = boardSize;
		board = new int[boardSize][boardSize];
		validMoves = new HashSet<Integer>();

		int one = (boardSize / 2) - 1;
		int two = boardSize / 2;

		board[one][one] = light;
		board[one][two] = dark;
		board[two][one] = dark;
		board[two][two] = light;

		addValidMoves(one, one);
		addValidMoves(one, two);
		addValidMoves(two, one);
		addValidMoves(two, two);
	}

	public Board(Board board) {
		boardSize = board.getSize();
		this.board = new int[boardSize][boardSize];
		validMoves = new HashSet<Integer>(board.getValidMoves());

		int[][] otherBoard = board.getBoard();

		for (int i = 0; i < boardSize; i++) {
			System.arraycopy(otherBoard[i], 0, this.board[i], 0, boardSize);
		}
	}

	// !--- Getters and setters ---!
	public int[][] getBoard() {
		return board;
	}

	public int getSize() {
		return boardSize;
	}

	public HashSet<Integer> getValidMoves() {
		return validMoves;
	}

	// !--- Methods ---!
	public void addValidMoves(int row, int column) {
		validMoves.remove(Integer.valueOf(twoDimToOneDim(row, column)));

		for (int i = row - 1; i < row + 2; i++) {
			for (int j = column - 1; j < column + 2; j++) {
				if (i >= 0 && j >= 0 && i < boardSize && j < boardSize && board[i][j] == empty) {
					validMoves.add(Integer.valueOf(twoDimToOneDim(i, j)));
				}
			}
		}
	}

	public int twoDimToOneDim(int x, int y) {
		return x * boardSize + y;
	}

	public int getRowFromOneDim(int x) {
		return x / boardSize;
	}

	public int getColumnFromOneDim(int x) {
		return x % boardSize;
	}

	public void print() {
		System.out.print("  ");

		for (int i = 97; i < 97 + boardSize; i++) {
			System.out.print("   " + (char) i);
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
		if (row - 2 >= 0 && column - 2 >= 0 && board[row - 1][column - 1] == opponentColor) {
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
		if (row - 2 >= 0 && column + 2 < boardSize && board[row - 1][column + 1] == opponentColor) {
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
		if (row + 2 < boardSize && column - 2 >= 0 && board[row + 1][column - 1] == opponentColor) {
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
		if (row + 2 < boardSize && column + 2 < boardSize && board[row + 1][column + 1] == opponentColor) {
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

	public int update(int color, int row, int column, HashSet<String> sidesToChange) {
		board[row][column] = color;
		addValidMoves(row, column);

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
}
