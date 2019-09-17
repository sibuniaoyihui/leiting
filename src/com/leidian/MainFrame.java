package com.leidian;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import javax.swing.*;
import java.io.*;
public class MainFrame extends JFrame  {
	JFrame frame1 = new JFrame("���ʷɻ���ս");
	JPanel panel = new JPanel();
	// JButton button1 = new JButton("��ʼ��Ϸ");
	// JButton button2 = new JButton("��Ϸ����");
	// JButton button3 = new JButton("��Ϸ����");
	// JButton button4 = new JButton("�˳���Ϸ");
	static boolean up = false;
	static boolean down = false;
	static boolean left = false;
	static boolean right = false;
	static boolean space = false;
	static boolean changeBullet = true;
	mainUI panel2 = new mainUI();
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
	public static ArrayList<Player> playerList = new ArrayList<Player>();// �������
	public MainFrame() {

		panel2.setOpaque(false);
		panel2.setLayout(null);// Ϊ��ʹ�ð�ť�Ķ�λ
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
	public static ArrayList<Enemy> enemyAll;
	// ��ű�ըЧ���ļ���
	public static ArrayList<Explode> explodeAll;
	// ����ӵ��ļ���
	public static ArrayList<Bullet> bulletAll;
	public static ArrayList<EnemyBullet> bulletEnemy;
	public static ArrayList<Food> FoodAll;
	public static int score;

	class NewGame extends JPanel {

		public int bg_y = 0;
		public NewGame() {

			String music = "/sound/�׵�.wav";
			clip = Applet.newAudioClip(getClass().getResource(music));
			clip.loop();// ѭ�����ű�������
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
			for (int i = 0; i < enemyAll.size(); i++) {
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
				      bulletAll.add(bullet);
			} else {
				Bullet bullet = new Bullet(MyPlane.getMy_x() + 25, MyPlane.getMy_y() - 20, 5, 20, 30, 20,
						img_myBullet[1]);
			            bulletAll.add(bullet);
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

	class MyThread4 extends Thread {
		public void run() {
			while (true) {
				panel2.repaint();
				try {
					Thread.sleep(65);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	class MyThread extends Thread {

		@Override
		public void run() {

			while (true) {
				// if(MyPlane.getMy_y()>6){
				// MyPlane.setMy_y(MyPlane.getMy_y() +2);
				// }
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

	class mainUI extends JPanel {
		boolean start;
		boolean list;
		boolean help;
		boolean exit;
		MyThread4 t4 = new MyThread4();

		public mainUI() {
			this.addMouseListener(new MouseAdapter() {
				public void mouseReleased(MouseEvent e) {
					int x = e.getX();
					int y = e.getY();
					if (x > 160 && x < 260 && y > 150 && y < 180) {// ��ʼ��ť
						enemyAll = new ArrayList<Enemy>();
						explodeAll = new ArrayList<Explode>();
						bulletAll = new ArrayList<Bullet>();
						bulletEnemy = new ArrayList<EnemyBullet>();
						FoodAll = new ArrayList<Food>();
						start = false;
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
					if (x > 160 && x < 260 && y > 200 && y < 230) {// ��Ϸ������ť
						panel2.setVisible(false);
						helppanel.setVisible(true);
						help = false;
						frame1.add(helppanel);
					}

					if (x > 160 && x < 260 && y > 250 && y < 280) {// ��Ϸ���а�ť
						panel2.setVisible(false);
						rl = new RankingList();
						rl.setVisible(true);
						list = false;
						frame1.add(rl);
					}
					if (x > 160 && x < 260 && y > 300 && y < 330) {// ��Ϸ�˳���ť
						exit = false;
						System.exit(0);
					}

				}

				public void mousePressed(MouseEvent e) {
					int x = e.getX();
					int y = e.getY();
					if (x > 160 && x < 260 && y > 150 && y < 180) {// ��ʼ��ť
						start = true;
					}
					if (x > 160 && x < 260 && y > 200 && y < 230) {// ��Ϸ������ť
						help = true;
					}

					if (x > 160 && x < 260 && y > 250 && y < 280) {// ��Ϸ���а�ť
						list = true;
					}
					if (x > 160 && x < 260 && y > 300 && y < 330) {// ��Ϸ�˳���ť
						exit = true;
					}
				}
			});
			t4.start();
		}

		public void paintComponent(Graphics g) {
			Image bg = new ImageIcon(getClass().getResource("/image/bg1.png")).getImage();
			g.drawImage(bg, 0, 0, 412, 572, null);
			if (start) {// ��ʼ
				Image start1 = new ImageIcon(getClass().getResource("/image/satrt2.png")).getImage();
				g.drawImage(start1, 160, 150, 100, 30, null);
				t4.stop();
			} else {
				Image start2 = new ImageIcon(getClass().getResource("/image/start1.png")).getImage();
				g.drawImage(start2, 160, 150, 100, 30, null);
			}
			if (help) {// ����
				Image help1 = new ImageIcon(getClass().getResource("/image/help2.png")).getImage();
				g.drawImage(help1, 160, 200, 100, 30, null);

			} else {
				Image help2 = new ImageIcon(getClass().getResource("/image/help1.png")).getImage();
				g.drawImage(help2, 160, 200, 100, 30, null);
			}
			if (list) {// ����
				Image list1 = new ImageIcon(getClass().getResource("/image/list2.png")).getImage();
				g.drawImage(list1, 160, 250, 100, 30, null);

			} else {
				Image list2 = new ImageIcon(getClass().getResource("/image/list1.png")).getImage();
				g.drawImage(list2, 160, 250, 100, 30, null);
			}
			if (exit) {// �˳�
				Image exit1 = new ImageIcon(getClass().getResource("/image/exit2.png")).getImage();
				g.drawImage(exit1, 160, 300, 100, 30, null);
				t4.stop();
			} else {
				Image exit2 = new ImageIcon(getClass().getResource("/image/exit1.png")).getImage();
				g.drawImage(exit2, 160, 300, 100, 30, null);
			}
		}
	}

	class RankingList extends JPanel {

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
					if (x > 250 && x < 350 && y > 505 && y < 535) {// ��ť����������ʱ�ı䰴ťͼ��
						panel2.setVisible(true);
						rl.setVisible(false);
					}
					if (x > 50 && x < 150 && y > 505 && y < 535) {// ��ť����������ʱ�ı䰴ťͼ��
						MainFrame.playerList.clear();
						for (int i = 0; i < 3; i++) {
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
					if (x > 250 && x < 350 && y > 505 && y < 535) {// ��ť����������ʱ�ı䰴ťͼ��
						fanhui = true;
					}
					if (x > 50 && x < 150 && y > 505 && y < 535) {// ��ť����������ʱ�ı䰴ťͼ��
						reset = true;
					}
				}
			});
			t3.start();
		}
		public void paintComponent(Graphics g) {
			Image bg = new ImageIcon(getClass().getResource("/image/list.jpg")).getImage();
			g.drawImage(bg, 0, 0,412, 572, null);
			if (fanhui) {// ���Ʒ��ذ�ť
				Image fan2 = new ImageIcon(getClass().getResource("/image/return2.png")).getImage();
				g.drawImage(fan2, 250, 505, 100, 30, null);
				t3.stop();
			} else {
				Image fan = new ImageIcon(getClass().getResource("/image/return1.png")).getImage();
				g.drawImage(fan, 250, 505, 100, 30, null);
			}
			if (reset) {
				Image img_reset = new ImageIcon(getClass().getResource("/image/reset2.png")).getImage();
				g.drawImage(img_reset, 50, 505, 100, 30, null);
				reset = false;
			} else {
				Image img_reset2 = new ImageIcon(getClass().getResource("/image/reset1.png")).getImage();
				g.drawImage(img_reset2, 50, 505, 100, 30, null);
			}

			int y = 205;
			for (int i = 0; i < 3; i++) {
				g.drawString(MainFrame.playerList.get(i).getName(), 71, y);
				g.drawString("" + MainFrame.playerList.get(i).getScore(), 280, y);
				y += 80;
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

	class GameOver extends JPanel implements ActionListener {
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
					if (x > 250 && x < 350 && y > 505 && y < 535) {// ��ť����������ʱ�ı䰴ťͼ��
						panel2.setVisible(true);
						MainFrame.Go.setVisible(false);
					}
				}

				@Override
				public void mousePressed(MouseEvent e) {
					int x = e.getX();
					int y = e.getY();
					if (x > 250 && x < 350 && y > 505 && y < 535) {// ��ť����������ʱ�ı䰴ťͼ��
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
				g.drawImage(fan2, 250, 505, 100, 30, null);
				t2.stop();
			} else {
				Image fan = new ImageIcon(getClass().getResource("/image/return1.png")).getImage();
				g.drawImage(fan, 250, 505, 100, 30, null);
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

	class HelpPanel extends JPanel {// ��Ϸ˵���ڲ���
		boolean fanhui2;
		MyThread5 t5 = new MyThread5();

		public HelpPanel() {
			// JButton button = new JButton("����");
			// this.setLayout(null);
			// button.setSize(100, 20);
			// button.setLocation(0, 0);
			// button.addActionListener(this);
			// this.add(button);
			this.addMouseListener(new MouseAdapter() {
				public void mouseReleased(MouseEvent e) {
					int x = e.getX();
					int y = e.getY();
					if (x > 250 && x < 350 && y > 505 && y < 535) {// ��ť����������ʱ�ı䰴ťͼ��
						panel2.setVisible(true);
						helppanel.setVisible(false);
					}
				}

				public void mousePressed(MouseEvent e) {
					int x = e.getX();
					int y = e.getY();
					if (x > 250 && x < 350 && y > 505 && y < 535) {// ��ť����������ʱ�ı䰴ťͼ��
						fanhui2 = true;
					}
				}
			});
			t5.start();
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
			if (fanhui2) {// ���Ʒ��ذ�ť
				Image fan2 = new ImageIcon(getClass().getResource("/image/return2.png")).getImage();
				g.drawImage(fan2, 250, 505, 100, 30, null);
				// t5.stop();
				fanhui2 = false;
			} else {
				Image fan = new ImageIcon(getClass().getResource("/image/return1.png")).getImage();
				g.drawImage(fan, 250, 505, 100, 30, null);
			}
		}

		class MyThread5 extends Thread {

			public void run() {
				while (true) {
					helppanel.repaint();
					try {
						Thread.sleep(62);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
