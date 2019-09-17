package com.leidian;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class RankingList extends JPanel {

	boolean fanhui;
	boolean reset;
	MyThread3 t3 = new MyThread3();

	public RankingList() {
		try {
			ObjectInputStream is = new ObjectInputStream(new FileInputStream("GameList.ser"));
			MainFrame.playerList = (ArrayList<Player>) is.readObject();// 解序列化
			ScoreCompare sc = new ScoreCompare();
			Collections.sort(MainFrame.playerList, sc);// 对列表进行降幂排序
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				if (x > 247 && x < 397 && y > 505 && y < 558) {// 按钮按键被按下时改变按钮图像
					MainFrame.panel2.setVisible(true);
					MainFrame.rl.setVisible(false);
				}
				if (x > 21 && x < 138 && y > 505 && y < 558) {// 按钮按键被按下时改变按钮图像
					MainFrame.playerList.clear();
					for (int i = 0; i < 5; i++) {
						MainFrame.playerList.add(new Player("无名氏", 0));// 加入5个空排行
					}
					try {// 重新序列化
						FileOutputStream filestream = new FileOutputStream("GameList.ser");
						ObjectOutputStream os = new ObjectOutputStream(filestream);
						os.writeObject(MainFrame.playerList);// 将集合序列化
						os.close();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}

			}

			public void mousePressed(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				if (x > 247 && x < 397 && y > 505 && y < 558) {// 按钮按键被按下时改变按钮图像
					fanhui = true;
				}
				if (x > 21 && x < 138 && y > 505 && y < 558) {// 按钮按键被按下时改变按钮图像
					reset = true;
				}
			}
		});
		t3.start();
	}

	public void paintComponent(Graphics g) {
		Image bg = new ImageIcon(getClass().getResource("/image/list.jpg")).getImage();
		g.drawImage(bg, 0, 0, null);
		if (fanhui) {// 绘制返回按钮
			Image fan2 = new ImageIcon(getClass().getResource("/image/return4.png")).getImage();
			g.drawImage(fan2, 247, 505, null);
			t3.stop();
		} else {
			Image fan = new ImageIcon(getClass().getResource("/image/return3.png")).getImage();
			g.drawImage(fan, 247, 505, null);
		}
		if (reset) {
			Image img_reset = new ImageIcon(getClass().getResource("/image/reset1.png")).getImage();
			g.drawImage(img_reset, 21, 505, null);
			reset = false;
		} else {
			Image img_reset2 = new ImageIcon(getClass().getResource("/image/reset2.png")).getImage();
			g.drawImage(img_reset2, 21, 505, null);
		}

		int y = 212;
		for (int i = 0; i < 5; i++) {
			g.drawString(MainFrame.playerList.get(i).getName(), 100, y);
			g.drawString("" + MainFrame.playerList.get(i).getScore(), 250, y);

			y += 52;
		}

	}

	class ScoreCompare implements Comparator<Player> {// 内部类
		public int compare(Player o1, Player o2) {
			return o2.getScore() - o1.getScore();// 降幂排列列表
		}
	}

}

class MyThread3 extends Thread {

	public void run() {
		while (true) {
			MainFrame.rl.repaint();
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
