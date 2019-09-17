package com.leidian;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.Random;

import javax.swing.ImageIcon;

public class Food {
	private int x;
	private int y;
	private int width = 15;
	private int length = 15;
	private String img;
	private boolean isLive;
	public int speed = 15;
	public double k = 1.7;
	public int moveType = 0;// Food移动路径类型

	public boolean isLive() {
		return isLive;
	}

	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public Food(String img) {
		super();
		this.x = new Random().nextInt(420);
		this.y = 20;
		this.width = 25;
		this.length = 25;
		this.isLive = true;
		moveType = new Random().nextInt(4);
		this.img = img;
	}

	public void drawFood(Graphics g) {
		Image img1 = new ImageIcon(getClass().getResource("/image/" + img)).getImage();
		g.drawImage(img1, x, y, width, length, null);
		move();
	}
	public void move() {
		y += speed;
		switch (moveType) {
		case 0:
			x += 12 * Math.sin(k);
			k += 0.1;
			break;
		case 1:
			x++;
			break;
		case 2:
			x--;
			break;
		case 3:
			x += 12 * Math.sin(-k);
			k += 0.2;
			break;
		}
	}

}
