package com.leidian;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

public class EnemyBullet {
	private int x, y;
	private int speed;
	private int width;
	private int height;
	private boolean isLive = true;
	double k = 1.7;
	int type = 0;

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

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public boolean isLive() {
		return isLive;
	}

	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}

	public EnemyBullet(int x, int y, int type) {
		super();
		this.x = x;
		this.y = y;
		this.width = 15;
		this.height = 15;
		this.speed = 15;
		this.isLive = true;
		this.type = type;
	}

	public void drawEnemyBullet(Graphics g) {
		Image img1 = new ImageIcon(getClass().getResource("/image/enemybullet2.png")).getImage();
		// »­Í¼
		g.drawImage(img1, x, y, width, height, null);// »æÖÆÍ¼
		move();
		if(y>600){
			this.setLive(false);
		}
	}

	public void move() {// µÐ»ú×Óµ¯ÐÐ¶¯¹ì¼£
		y += speed;
		switch (type) {
		case 1:
			break;
		case 2:
			x--;
			break;
		case 3:
			x++;
			break;
		case 4:
			x += 2;
			break;
		case 5:
			x -= 2;
			break;
		case 6:
			x += 10 * Math.sin(k);
			k += 0.1;
			break;
		case 7:
			x += 10 * Math.sin(-k);
			k += 0.1;
			break;
		}
	}
}
