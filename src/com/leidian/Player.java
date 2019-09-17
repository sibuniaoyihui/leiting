package com.leidian;

import java.io.*;
import java.util.*;

public class Player implements Serializable {
	private String name;
	private int score;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public Player(String name, int score) {
		super();
		this.name = name;
		this.score = score;
	}

	public Player() {
		super();
	}

}
