package com.leidian;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

public class MyPlane {
	private static int my_x;
	private static int my_y;
	private int hp = 100;
	private int width, height;
	private boolean isLive;

	public boolean isLive() {
		return isLive;
	}

	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}

	public static int getMy_x() {
		return my_x;
	}

	public static void setMy_x(int my_x) {
		MyPlane.my_x = my_x;
	}

	public static int getMy_y() {
		return my_y;
	}

	public static void setMy_y(int my_y) {
		MyPlane.my_y = my_y;
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

	public MyPlane() {
		super();
		my_x = 175;
		my_y = 480;
		this.hp = 100;
		this.width = 55;
		this.height = 60;
	}

	public void drawMyPlane(Graphics g) {
		Image img_myplane = new ImageIcon(getClass().getResource("/image/MyPlane.png")).getImage();
		g.drawImage(img_myplane, my_x, my_y, width, height, null);// 绘制自己飞机
		this.hitEach();
		this.eat();
		this.hit();
	}

	public Rectangle getRec() {
		Rectangle re = new Rectangle(my_x, my_y, 55, 60);
		return re;
	}

	public void eat() {
		Rectangle myplane1 = this.getRec();
		for (int i = 0; i < MainFrame.FoodAll.size(); i++) {
			Rectangle food = new Rectangle(MainFrame.FoodAll.get(i).getX(), MainFrame.FoodAll.get(i).getY(), 25, 25);
			if (myplane1.intersects(food) == true) {
				MainFrame.FoodAll.get(i).setLive(false);
				if (MainFrame.FoodAll.get(i).getImg() == "Food1.png") {
					if (hp < 100) {
						hp += 10;// 吃到Food1 ，血量加10
					}
				}
				if (MainFrame.FoodAll.get(i).getImg() == "Food2.png") {
					MainFrame.changeBullet = false;// 改变子弹
				}
				if (MainFrame.FoodAll.get(i).getImg() == "Food3.png") {
					hp = 100;
				}
				if (MainFrame.FoodAll.get(i).getImg() == "Food4.png") {
					MainFrame.changeBullet = true;
				}

			}
		}
	}

	public void hit() {// 被敌机子弹打中
		Rectangle myplane = this.getRec();
		for (int i = 0; i < MainFrame.bulletEnemy.size(); i++) {
			Rectangle eb = new Rectangle(MainFrame.bulletEnemy.get(i).getX(), MainFrame.bulletEnemy.get(i).getY(), 15,
					15);
			if (myplane.intersects(eb) == true) {
				hp -= 5;

			}
		}
	}

	public void hitEach() {// 与敌机互撞情况
		Rectangle myplane = this.getRec();
		for (int i = 0; i < MainFrame.enemyAll.size(); i++) {
			Rectangle enemy = new Rectangle(MainFrame.enemyAll.get(i).getX(), MainFrame.enemyAll.get(i).getY(), 40, 40);
			if (myplane.intersects(enemy) == true) {
				hp -= 5;
			}
		}
	}
}
