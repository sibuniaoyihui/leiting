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
			MainFrame.playerList = (ArrayList<Player>) is.readObject();// �����л�
			ScoreCompare sc = new ScoreCompare();
			Collections.sort(MainFrame.playerList, sc);// ���б���н�������
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				if (x > 247 && x < 397 && y > 505 && y < 558) {// ��ť����������ʱ�ı䰴ťͼ��
					MainFrame.panel2.setVisible(true);
					MainFrame.rl.setVisible(false);
				}
				if (x > 21 && x < 138 && y > 505 && y < 558) {// ��ť����������ʱ�ı䰴ťͼ��
					MainFrame.playerList.clear();
					for (int i = 0; i < 5; i++) {
						MainFrame.playerList.add(new Player("������", 0));// ����5��������
					}
					try {// �������л�
						FileOutputStream filestream = new FileOutputStream("GameList.ser");
						ObjectOutputStream os = new ObjectOutputStream(filestream);
						os.writeObject(MainFrame.playerList);// ���������л�
						os.close();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}

			}

			public void mousePressed(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				if (x > 247 && x < 397 && y > 505 && y < 558) {// ��ť����������ʱ�ı䰴ťͼ��
					fanhui = true;
				}
				if (x > 21 && x < 138 && y > 505 && y < 558) {// ��ť����������ʱ�ı䰴ťͼ��
					reset = true;
				}
			}
		});
		t3.start();
	}

	public void paintComponent(Graphics g) {
		Image bg = new ImageIcon(getClass().getResource("/image/list.jpg")).getImage();
		g.drawImage(bg, 0, 0, null);
		if (fanhui) {// ���Ʒ��ذ�ť
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

	class ScoreCompare implements Comparator<Player> {// �ڲ���
		public int compare(Player o1, Player o2) {
			return o2.getScore() - o1.getScore();// ���������б�
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
