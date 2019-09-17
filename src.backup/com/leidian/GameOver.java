package com.leidian;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GameOver extends JPanel implements ActionListener {
	JTextField filed;
	JLabel pName;
	JButton buttonG;
	Player player = new Player();
	boolean fanhui;
	MyThread2 t2 = new MyThread2();

	public GameOver() {
		super();
		pName = new JLabel("������������");
		pName.setForeground(Color.black);
		filed = new JTextField(20);
		JLabel scoreLabel = new JLabel("���ε÷֣�" + MainFrame.score + "��");
		buttonG = new JButton("Save");
		buttonG.addActionListener(this);
		this.add(pName);
		this.add(filed);
		this.add(buttonG);
		this.add(scoreLabel);
		this.addMouseListener(new MouseAdapter() {

			public void mouseReleased(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				if (x > 247 && x < 397 && y > 505 && y < 558) {// ��ť����������ʱ�ı䰴ťͼ��
					MainFrame.panel2.setVisible(true);
					MainFrame.Go.setVisible(false);
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				if (x > 247 && x < 397 && y > 505 && y < 558) {// ��ť����������ʱ�ı䰴ťͼ��
					fanhui = true;
				}
			}

		});
		t2.start();
	}

	public void paintComponent(Graphics g) {
		Image bg = new ImageIcon(getClass().getResource("/image/bg4.jpg")).getImage();
		g.drawImage(bg, 0, 0, null);

		if (fanhui) {// ���Ʒ��ذ�ť
			Image fan2 = new ImageIcon(getClass().getResource("/image/return2.png")).getImage();
			g.drawImage(fan2, 247, 505, null);
			t2.stop();
		} else {
			Image fan = new ImageIcon(getClass().getResource("/image/return.png")).getImage();
			g.drawImage(fan, 247, 505, null);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			player.setName(filed.getText());
			player.setScore(MainFrame.score);
			MainFrame.playerList.add(player);
			FileOutputStream filestream = new FileOutputStream("GameList.ser");
			ObjectOutputStream os = new ObjectOutputStream(filestream);
			os.writeObject(MainFrame.playerList);// ���������л�
			os.close();
			this.remove(pName);
			this.remove(filed);
			this.remove(buttonG);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}

class MyThread2 extends Thread {

	public void run() {
		while (true) {
			MainFrame.Go.repaint();
			try {
				Thread.sleep(65);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
