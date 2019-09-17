package com.leidian;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import java.io.*;

public class MainFrame extends JFrame implements ActionListener {
	JFrame frame1 = new JFrame("�׵�2017");
	JPanel panel = new JPanel();
	JButton button1 = new JButton("��ʼ��Ϸ");
	JButton button2 = new JButton("��Ϸ����");
	JButton button3 = new JButton("��Ϸ����");
	JButton button4 = new JButton("�˳���Ϸ");
	static boolean up = false;
	static boolean down = false;
	static boolean left = false;
	static boolean right = false;
	static boolean space = false;
	static boolean changeBullet = true;
	static JPanel panel2 = new JPanel();
	HelpPanel helppanel = new HelpPanel();
	NewGame newgame;
	MyPlane myplane;
	AudioClip clip;
	static GameOver Go;
	static RankingList rl;
	MyThread t;
	String img_enemy[] = { "enemy1.png", "enemy2.png", "enemy3.png", "enemy4.png", "enemy5.png" };
	String img_food[] = { "Food1.png", "Food2.png", "Food3.png", "Food4.png" };
	String img_myBullet[] = { "bullet.png", "bulletlight.png" };
	public int index;
	public static ArrayList<Player> playerList = new ArrayList<Player>();//�������

	public MainFrame() {

		//helppanel.setOpaque(false);
		panel2.setOpaque(false);
		panel2.setLayout(null);// Ϊ��ʹ�ð�ť�Ķ�λ
		button1.setSize(100, 20);
		button1.setLocation(150, 150);
		button2.setSize(100, 20);
		button2.setLocation(150, 200);
		button3.setSize(100, 20);
		button3.setLocation(150, 250);
		button4.setSize(100, 20);
		button4.setLocation(150, 300);
		panel2.add(button1);
		panel2.add(button2);
		panel2.add(button3);
		panel2.add(button4);
		button1.addActionListener(this);
		button2.addActionListener(this);
		button3.addActionListener(this);
		button4.addActionListener(this);
		frame1.setSize(420, 600);
		ImageIcon bg = new ImageIcon(getClass().getResource("/image/bg1.png"));
		JLabel label = new JLabel(bg);
		label.setBounds(0, 0, bg.getIconWidth(), bg.getIconHeight());
		frame1.getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));
		panel = (JPanel) frame1.getContentPane();
		panel.setOpaque(false);
		frame1.add(panel2);
		frame1.setLocation(800, 300);
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame1.setVisible(true);
		frame1.setResizable(false);
		for (int i = 0; i < 5; i++) {
			playerList.add(new Player("������", 0));// ����5��������
		}

	}

	class HelpPanel extends JPanel implements ActionListener {// ��Ϸ˵���ڲ���
		public HelpPanel() {
			JButton button = new JButton("����");
			this.setLayout(null);
			button.setSize(100, 20);
			button.setLocation(0, 0);
			button.addActionListener(this);
			this.add(button);
		}

		public void paintComponent(Graphics g) {
			Image bg = new ImageIcon(getClass().getResource("/image/bg2.png")).getImage();
			g.drawImage(bg, 0, 0, this);
			g.setColor(Color.YELLOW);
			g.setFont(new Font(null, 2, 30));
			g.drawString("��Ϸ˵��", 130, 70);
			g.setFont(new Font(null, 2, 20));
			g.drawString("1���������Ҽ����Ʒɻ����ĸ������ƶ�", 5, 100);
			g.drawString("2���ո�����Ʒɻ��ӵ����䣬��������", 5, 150);
			g.drawString("3�����е��˷ɻ��ɵõ�һ������", 5, 200);
			g.drawString("4�����л�����һ�λ����һ��Ѫ��", 5, 250);
			g.drawString("5���л�������һ�����ʲ���һ���ɳԵ���ĸ", 5, 300);
			g.drawString("6��������ĸ�ֱ��ʹ�ɻ�������ͬ�仯Ч��", 5, 350);
			g.drawString("      �����ˣ���ΰ     ѧ�ţ�2015214271", 5, 500);
			
		}

		public void actionPerformed(ActionEvent e) {
			helppanel.setVisible(false);
			panel2.setVisible(true);
		}
	}

	// public static int my_x = 175;// �Լ��ɻ��ĺ�����
	// public static int my_y = 480;// �Լ��ɻ���������
	public static ArrayList<Enemy> enemyAll;
	// ��ű�ըЧ���ļ���
	public static ArrayList<Explode> explodeAll;
	// ����ӵ��ļ���
	public static ArrayList<Bullet> bulletAll;
	public static ArrayList<EnemyBullet> bulletEnemy;
	public static ArrayList<Food> FoodAll;
	public static int score;

	class NewGame extends JPanel {

		private static final long serialVersionUID = 1L;
		public int bg_y = 0;

		public NewGame() {

			String music = "/sound/�׵�.wav";
			clip = Applet.newAudioClip(getClass().getResource(music));
			clip.loop();//ѭ�����ű�������
		}

		public void paintComponent(Graphics g) {

			Image img_bg = new ImageIcon(getClass().getResource("/image/bg3.png")).getImage();
			g.drawImage(img_bg, 0, bg_y, 420, 600, null);
			bg_y = bg_y + 10;
			g.drawImage(img_bg, 0, -600 + bg_y, 420, 600, null);// ʹ��ͼƬ����ƶ������ɻ��ڲ������������ǰ�ƶ��ļ�������˶���
			if (bg_y >= 590) {
				bg_y = 0;
			}
			// �����Լ��ķɻ�
			myplane.drawMyPlane(g);
			g.drawRect(5, 5, 100, 10);
			g.setColor(Color.red);
			g.fillRect(5, 5, myplane.getHp(), 10);// �����Լ��ɻ�Ѫ��
			g.drawString("������" + score, 300, 20);// ����
			if (new Random().nextInt(100) > 95) {// ����food�ĸ���
				Food food = new Food(img_food[new Random().nextInt(4)]);
				FoodAll.add(food);
			}
			if (new Random().nextInt(100) > 95) {// �����л��ĸ���
				Enemy enemy = new Enemy(new Random().nextInt(365), 45, 100, 40, 40, 100, 10,
						img_enemy[new Random().nextInt(5)]);
				enemyAll.add(enemy);
			}
			for (int i = 0; i < FoodAll.size(); i++) {
				Food food1 = FoodAll.get(i);
				if (food1.isLive()) {
					food1.drawFood(g);
				} else {
					FoodAll.remove(food1);
				}
			}
			for (int i = 0; i<enemyAll.size(); i++) {
				Enemy enemy = enemyAll.get(i);
				if (enemy.isLive()) {
					enemy.drawEnemy(g);
					if (new Random().nextInt(100) > 70) {// �л������ӵ��ĸ���
						EnemyBullet eb = new EnemyBullet(enemyAll.get(i).getX() + 20, enemyAll.get(i).getY() + 50,
								new Random().nextInt(7) + 1);
						bulletEnemy.add(eb);
					}
				} else
					enemyAll.remove(enemy);
			}
			// �滭��ը
			for (int i = 0; i < explodeAll.size(); i++) {
				Explode e = explodeAll.get(i);
				// �ж��Ƿ��ǻ��ֵ�
				if (e.isLive()) {
					e.drawExplode(g);
				} else {
					explodeAll.remove(e);
				}
			}
			// �滭�ӵ�
			for (int i = 0; i < bulletAll.size(); i++) {
				Bullet bullet = bulletAll.get(i);
				// �ж��Ƿ��ǻ��ӵ�
				if (bullet.isLive()) {
					bullet.drawBullet(g);
				} else {
					bulletAll.remove(bullet);
				}
			}
			for (int i = 0; i < bulletEnemy.size(); i++) {
				EnemyBullet eb2 = bulletEnemy.get(i);
				if (eb2.isLive()) {
					eb2.drawEnemyBullet(g);
				} else {
					bulletEnemy.remove(eb2);
				}

			}
			if (myplane.getHp() < 0) {
				Go = new GameOver();
				newgame.setVisible(false);
				frame1.requestFocus();
				Go.setVisible(true);
				frame1.add(Go);
				t.stop();
				clip.stop();

			}

		}

		public void move() {// �Լ��ɻ����ƶ����
			if (MainFrame.left && MyPlane.getMy_x() > -15)
				MyPlane.setMy_x(MyPlane.getMy_x() - 15);
			if (MainFrame.right && MyPlane.getMy_x() < 365)
				MyPlane.setMy_x(MyPlane.getMy_x() + 15);
			if (MainFrame.down && MyPlane.getMy_y() < 500)
				MyPlane.setMy_y(MyPlane.getMy_y() + 15);
			if (MainFrame.up && MyPlane.getMy_y() > -15)
				MyPlane.setMy_y(MyPlane.getMy_y() - 15);
			if (MainFrame.space) {
				Fire();
			}
		}

		public void Fire() {
			if (changeBullet) {
				Bullet bullet = new Bullet(MyPlane.getMy_x() + 25, MyPlane.getMy_y() - 20, 5, 20, 30, 10,
						img_myBullet[0]);
				MainFrame.bulletAll.add(bullet);
			} else {
				Bullet bullet = new Bullet(MyPlane.getMy_x() + 25, MyPlane.getMy_y() - 20, 5, 20, 30, 20,
						img_myBullet[1]);
				MainFrame.bulletAll.add(bullet);
			}
		}

		Image img = null;

		// ���壬������˸�������
		public void update(Graphics g) {
			if (img == null) {
				img = this.createImage(420, 600);
			}
			// ����img�������⻭��
			Graphics gb = img.getGraphics();
			// ����paint����
			paint(gb);
			// ������ʵ�Ļ���g����ͼƬ
			g.drawImage(img, 0, 0, 420, 600, null);
		}

	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource().equals(button1)) {
			enemyAll = new ArrayList<Enemy>();
			explodeAll = new ArrayList<Explode>();
			bulletAll = new ArrayList<Bullet>();
			bulletEnemy = new ArrayList<EnemyBullet>();
			FoodAll = new ArrayList<Food>();
			newgame = new NewGame();
			myplane = new MyPlane();
			score = 0;
			t = new MyThread();
			// sound = new GameSound("src/sound/�׵� .wav");
			panel2.setVisible(false);
			newgame.setVisible(true);
			frame1.addKeyListener(new MyKeyDown());
			frame1.requestFocus();// �����뽹����ڵ�����������Ŀؼ���
			frame1.add(newgame);
			t.start();
		
		}
		if (e.getSource().equals(button2)) {
			panel2.setVisible(false);
			helppanel.setVisible(true);
			frame1.add(helppanel);
		}
		if (e.getSource().equals(button3)) {
			panel2.setVisible(false);
			rl = new RankingList();
			rl.setVisible(true);
			frame1.add(rl);
		}
		if (e.getSource().equals(button4)) {
			System.exit(0);
		}
	}

	class MyThread extends Thread {

		@Override
		public void run() {

			while (true) {

				newgame.repaint();
				// ÿʱÿ�̶��ж��Ƿ�Ҫ�ƶ��ɻ�
				newgame.move();
				try {
					// ���ӵ�ǰ��,�ٶ�Ҫ������ٶȿ�ܶ�
					for (int i = 0; i < bulletAll.size(); i++) {
						Bullet bullet = bulletAll.get(i);
						bullet.setY(bullet.getY() - bullet.getSpeed());
					}
					Thread.sleep(65);// �߳�����
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	class MyKeyDown extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == e.VK_SPACE) {
				MainFrame.space = true;

			}
			if (e.getKeyCode() == e.VK_UP) {
				MainFrame.up = true;
			}
			if (e.getKeyCode() == e.VK_DOWN) {
				MainFrame.down = true;
			}
			if (e.getKeyCode() == e.VK_LEFT) {
				MainFrame.left = true;
			}
			if (e.getKeyCode() == e.VK_RIGHT) {
				MainFrame.right = true;
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == e.VK_SPACE) {
				MainFrame.space = false;
			}
			if (e.getKeyCode() == e.VK_UP) {
				MainFrame.up = false;
			}
			if (e.getKeyCode() == e.VK_DOWN) {
				MainFrame.down = false;
			}
			if (e.getKeyCode() == e.VK_LEFT) {
				MainFrame.left = false;
			}
			if (e.getKeyCode() == e.VK_RIGHT) {
              MainFrame.right = false;
			
			}
		}
	}
}
