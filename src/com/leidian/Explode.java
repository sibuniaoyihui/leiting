package com.leidian;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;

public class Explode {
	private int x;
	private int y;
	String img[] = { "boom1.png", "boom2.png", "boom3.png", "boom4.png", "boom5.png", "boom6.png", "boom7.png" };
	private boolean isLive = true;
	private int index = 0;

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

	public boolean isLive() {
		return isLive;
	}

	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Explode(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public void drawExplode(Graphics g) {
		String image = img[index];
		Image img1 = new ImageIcon(getClass().getResource("/image/" + image)).getImage();
		g.drawImage(img1, x, y, null);
		index++;
		if (index >= img.length) {
			index = 0;
			// ±¬Õ¨ÏûÊ§
			isLive = false;
		}
	}

}