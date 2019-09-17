package com.leidian;

import java.awt.*;
import javax.swing.ImageIcon;

public class Bullet {
	private int x, y;
	private int speed;
	private String img;
	private int kill;
	private int width;
	private int height;
	private boolean isLive = true;

	public int getKill() {
		return kill;
	}

	public void setKill(int kill) {
		this.kill = kill;
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

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
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

	public Bullet(int x, int y, int width, int height, int speed, int kill, String img) {
		super();
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.width = width;
		this.height = height;
		this.kill = kill;
		this.img = img;
	}

	public void drawBullet(Graphics g) {
		Image img1 = new ImageIcon(getClass().getResource("/image/" + img)).getImage();
		// ª≠Õº
		g.drawImage(img1, x, y, width, height, null);// ªÊ÷∆Õº
		if(y<0){
			this.setLive(false);//“∆≥˝◊”µØ
		}
	}
}
