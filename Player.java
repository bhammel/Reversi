/*
 * File: Player.java
 * Author: Brandon Hammel
 * Class: CS 165A, Winter 2016
 * Assignment: Machine Problem 2
 */

import java.util.Scanner;

public class Player {

	// !--- Instance variables ---!
	private String darkOrLight;
	private int color;
	private String humanOrCOM;
	private int score;

	// !--- Constructors ---!
	public Player(String darkOrLight, int color, String humanOrCOM) {
		this.darkOrLight = darkOrLight;
		this.color = color;
		this.humanOrCOM = humanOrCOM;
		score = 2;
	}

	// !--- Getters and setters ---!
	public String getDarkOrLight() {
		return darkOrLight;
	}

	public int getColor() {
		return color;
	}

	public String getHumanOrCOM() {
		return humanOrCOM;
	}

	public int getScore() {
		return score;
	}

	// !--- Methods ---!
	public String getMove() {
		Scanner in = new Scanner(System.in);
		System.out.println("> ");

		return in.next();
	}

	public void addToScore(int score) {
		this.score += score;
	}

	public void subtractFromScore(int score) {
		this.score -= score;
	}
}
