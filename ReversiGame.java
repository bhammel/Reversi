/*
 * File: ReversiGame.java
 * Author: Brandon Hammel
 * Class: CS 165A, Winter 2016
 * Assignment: Machine Problem 2
 */

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

// ReversiGame class

public class ReversiGame {

	// !--- Class variables ---!
	private static int empty = 0;
	private static int dark = 1;
	private static int light = 2;

	// !--- Instance variables ---!
	private Board board;
	private HashSet<String> sidesToChange;
	private String movePlayed = "--";
	private Player currentPlayer;
	private Player darkPlayer;
	private Player lightPlayer;

	// !--- Constructors ---!
	public ReversiGame(int boardSize, boolean humanIsDark) {
		board = new Board(boardSize);

		if (humanIsDark) {
			darkPlayer = new Player("Dark", dark, "human");
			lightPlayer = new Player("Light", light, "COM");
		} else {
			darkPlayer = new Player("Dark", dark, "COM");
			lightPlayer = new Player("Light", light, "human");
		}

		currentPlayer = darkPlayer;
	}

	// !--- Getters and setters ---!
	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	// !--- Methods ---!
	public void printBoard() {
		board.print();
	}

	public void printInfo() {
		System.out.println("Move played: " + movePlayed);
		System.out.println(currentPlayer.getDarkOrLight() + " player (" + currentPlayer.getHumanOrCOM() + ") plays now");
		System.out.println("Score: Light " + lightPlayer.getScore() + " - Dark " + darkPlayer.getScore());
	}
	
	public void start() {
		printBoard();
		printInfo();
	}

	public void printResults() {
		if (darkPlayer.getScore() > lightPlayer.getScore()) {
			System.out.println();
			System.out.println("Dark player (" + darkPlayer.getHumanOrCOM() + ") wins!");
		} else if (lightPlayer.getScore() > darkPlayer.getScore()) {
			System.out.println();
			System.out.println("Light player (" + lightPlayer.getHumanOrCOM() + ") wins!");
		} else {
			System.out.println();
			System.out.println("It's a tie!");
		}
	}

	public static String getMove(int row, int column) {
		return String.valueOf((char) (column + 97)) + String.valueOf(row + 1);
	}

	public void move(int row, int column, int color) {
		int gain = board.update(color, row, column, sidesToChange);

		if (color == dark) {
			darkPlayer.addToScore(gain + 1);
			lightPlayer.subtractFromScore(gain);
		} else {
			darkPlayer.subtractFromScore(gain);
			lightPlayer.addToScore(gain + 1);
		}

		movePlayed = getMove(row, column);
	}

	public boolean hasValidMove(int color) {
		Iterator itr = board.getValidMoves().iterator();
		while (itr.hasNext()) {
			Integer temp = (Integer) itr.next();
			int validMove = temp.intValue();
			int row = board.getRowFromOneDim(validMove);
			int column = board.getColumnFromOneDim(validMove);

			if (!board.validMove(color, row, column).isEmpty()) {
				return true;
			}
		}

		return false;
	}

	public void nextPlayer() {
		if (currentPlayer == darkPlayer) {
			currentPlayer = lightPlayer;
		} else {
			currentPlayer = darkPlayer;
		}
	}

	public boolean gameOver() {
		if (darkPlayer.getScore() + lightPlayer.getScore() == board.getSize() * board.getSize()) {
			return true;
		}

		return false;
	}
}
