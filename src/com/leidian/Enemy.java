package com.leidian;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.Random;
import javax.swing.ImageIcon;
import com.leidian.MainFrame.NewGame;
import java.applet.*;
public class Enemy {
	private int x;
	private int y;
	private int hp = 100;
	private int width, height;
	private String img;
	public int index;
	public double l = 1.5;
	public int type = 0;

	// 杀伤力
	private int kill;
	// 是否存活
	private boolean isLive = true;
	// 速度
	private int speed;

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public boolean isLive() {
		return isLive;
	}

	public void setLive(boolean isLive) {
		this.isLive = isLive;
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

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
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

	public int getKill() {
		return kill;
	}

	public void setKill(int kill) {
		this.kill = kill;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public Enemy(int x, int y, int hp, int width, int height, int kill, int speed, String img) {
		super();
		this.x = x;
		this.y = y;
		this.hp = hp;
		this.width = width;
		this.height = height;
		this.kill = kill;
		this.speed = speed;
		this.img = img;
		this.type = new Random().nextInt(3);
	}

	public void drawEnemy(Graphics g) {

		Image img1 = new ImageIcon(getClass().getResource("/image/" + img)).getImage();
		g.drawImage(img1, x, y, width, height, null);
		move();
		if(y>600){
			this.setLive(false);
		}
	}

	public Rectangle getRec() {
		Rectangle re = new Rectangle(x, y, 40, 40);
		return re;
	}

	public void hitEach() {
		Rectangle Myplane = new Rectangle(MyPlane.getMy_x(), MyPlane.getMy_y(), 55, 60);
		Rectangle enemy1 = this.getRec();
		if (Myplane.intersects(enemy1) == true) {
			this.setLive(false);
			// 产生爆炸效果
			Explode e = new Explode(this.getX(), this.getY());
			// 将e放入爆炸效果集合
			e.setLive(true);
			MainFrame.explodeAll.add(e);
		}
	}

	public void hitBullet() {// 敌机被子弹打中
		Rectangle enemy2 = this.getRec();
		for (int i = 0; i < MainFrame.bulletAll.size(); i++) {
			Rectangle bullet = new Rectangle(MainFrame.bulletAll.get(i).getX(), MainFrame.bulletAll.get(i).getY(), 20,
					30);
			if (enemy2.intersects(bullet) == true) {
				if (this.hp == 0) {
//					GameSound b = new GameSound("src/sound/boomSound.wav");
//					b.start();// 爆炸声音
					String music = "/sound/boomSound.wav";
					AudioClip clip = Applet.newAudioClip(getClass().getResource(music));
					clip.play();//循环播放背景音乐
					MainFrame.score += 50;
					this.setLive(false);
					MainFrame.bulletAll.get(i).setLive(false);
					Explode e = new Explode(this.getX(), this.getY());
					e.setLive(true);
					MainFrame.explodeAll.add(e);
					MainFrame.bulletAll.remove(MainFrame.bulletAll.get(i));
				} else {
					MainFrame.bulletAll.get(i).setLive(false);
					hp -= MainFrame.bulletAll.get(i).getKill();
				}

			}
		}
	}

	public void move() {
		y += speed;
		switch (type) {
		case 0:
			x += 15 * Math.sin(l);
			l += 0.15;
			break;
		case 1:
			x += 1;
			break;
		case 2:
			x -= 1;
			break;
		}
		this.hitEach();
		this.hitBullet();

	}

}
