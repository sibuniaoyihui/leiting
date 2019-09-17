package com.leidian;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Loading extends GameBase {
	int jindu = 0;
	Random random = new Random();

	public static void main(String[] args) {
		Loading loading = new Loading();
		loading.start();
	}

	@Override
	public void paint(Graphics g) {
		g.drawRect(0, 0, 400, 50);
		g.setColor(Color.red);
		g.fillRect(0, 0, jindu, 50);     
		jindu += random.nextInt(5);
		if (jindu >= 400) {
			this.setVisible(false);
			new MainFrame();
		}
	}
	public void init() {
		setWidth(400);
		setHeight(50);
		this.setUndecorated(true);// 去除窗体边框效果
		setRefresh(10);
	}
}
