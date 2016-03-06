/*
 * File: Player.java
 * Author: Brandon Hammel
 * Class: CS 165A, Winter 2016
 * Assignment: Machine Problem 2
 */

import java.util.Scanner;

/*
 * Player class - represents a Reversi player.
 */

public class Player {

	// !-------------------------- Instance variables --------------------------!

	// The player's color as an int - either Dark (1) or Light (2)
	private int color;

	// The player's color as a string - either "Dark" or "Light"
	private String darkOrLight;

	// Is the player a human or computer? Either "human" or "COM"
	private String humanOrCOM;

	// The player's current score
	private int score;

	// !----------------------------- Constructors -----------------------------!

	/*
	 * Creates a new Player given whether it is playing as Dark or Light and
	 * whether it is a human or computer.
	 */
	public Player(String darkOrLight, int color, String humanOrCOM) {
		this.darkOrLight = darkOrLight;
		this.color = color;
		this.humanOrCOM = humanOrCOM;
		score = 2;
	}

	// !-------------------------- Getters and setters --------------------------!

	// Returns the player's color
	public int getColor() {
		return color;
	}

	// Returns whether the player is playing as Dark or Light
	public String getDarkOrLight() {
		return darkOrLight;
	}

	// Returns whether the player is a human or computer
	public String getHumanOrCOM() {
		return humanOrCOM;
	}

	// Returns the player's score
	public int getScore() {
		return score;
	}

	// !-------------------------------- Methods --------------------------------!

	// Increments the player's score by the specified amount
	public void addToScore(int score) {
		this.score += score;
	}

	// Displays a prompt asking user to enter a move
	public String getMove() {
		Scanner in = new Scanner(System.in);
		System.out.print("> ");

		return in.next();
	}

	// Decrements the player's score by the specified amount
	public void subtractFromScore(int score) {
		this.score -= score;
	}
}
