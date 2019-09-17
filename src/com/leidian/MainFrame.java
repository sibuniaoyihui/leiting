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
	JFrame frame1 = new JFrame("重邮飞机大战");
	JPanel panel = new JPanel();
	// JButton button1 = new JButton("开始游戏");
	// JButton button2 = new JButton("游戏帮助");
	// JButton button3 = new JButton("游戏排行");
	// JButton button4 = new JButton("退出游戏");
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
	public static ArrayList<Player> playerList = new ArrayList<Player>();// 玩家排名
	public MainFrame() {

		panel2.setOpaque(false);
		panel2.setLayout(null);// 为了使用按钮的定位
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
			playerList.add(new Player("无名氏", 0));// 加入5个空排行
		}
	}
	public static ArrayList<Enemy> enemyAll;
	// 存放爆炸效果的集合
	public static ArrayList<Explode> explodeAll;
	// 存放子弹的集合
	public static ArrayList<Bullet> bulletAll;
	public static ArrayList<EnemyBullet> bulletEnemy;
	public static ArrayList<Food> FoodAll;
	public static int score;

	class NewGame extends JPanel {

		public int bg_y = 0;
		public NewGame() {

			String music = "/sound/雷电.wav";
			clip = Applet.newAudioClip(getClass().getResource(music));
			clip.loop();// 循环播放背景音乐
		}

		public void paintComponent(Graphics g) {
			Image img_bg = new ImageIcon(getClass().getResource("/image/bg3.png")).getImage();
			g.drawImage(img_bg, 0, bg_y, 420, 600, null);
			bg_y = bg_y + 10;
			g.drawImage(img_bg, 0, -600 + bg_y, 420, 600, null);// 使得图片向后移动，而飞机在不动的情况下向前移动的假象（相对运动）
			if (bg_y >= 590) {
				bg_y = 0;
			}
			// 创建自己的飞机
			myplane.drawMyPlane(g);
			g.drawRect(5, 5, 100, 10);
			g.setColor(Color.red);
			g.fillRect(5, 5, myplane.getHp(), 10);// 绘制自己飞机血量
			g.drawString("分数：" + score, 300, 20);// 分数
			if (new Random().nextInt(100) > 95) {// 产生food的概率
				Food food = new Food(img_food[new Random().nextInt(4)]);
				FoodAll.add(food);
			}
			if (new Random().nextInt(100) > 95) {// 产生敌机的概率
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
					if (new Random().nextInt(100) > 70) {// 敌机产生子弹的概率
						EnemyBullet eb = new EnemyBullet(enemyAll.get(i).getX() + 20, enemyAll.get(i).getY() + 50,
								new Random().nextInt(7) + 1);
						bulletEnemy.add(eb);
					}
				} else
					enemyAll.remove(enemy);
			}
			// 绘画爆炸
			for (int i = 0; i < explodeAll.size(); i++) {
				Explode e = explodeAll.get(i);
				// 判断是否是活字弹
				if (e.isLive()) {
					e.drawExplode(g);
				} else {
					explodeAll.remove(e);
				}
			}
			// 绘画子弹
			for (int i = 0; i < bulletAll.size(); i++) {
				Bullet bullet = bulletAll.get(i);
				// 判断是否是活子弹
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

		public void move() {// 自己飞机的移动情况
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

		// 缓冲，避免闪烁现象出现
		public void update(Graphics g) {
			if (img == null) {
				img = this.createImage(420, 600);
			}
			// 利用img创建虚拟画笔
			Graphics gb = img.getGraphics();
			// 调用paint方法
			paint(gb);
			// 利用真实的画笔g来画图片
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
				// 每时每刻都判断是否要移动飞机
				newgame.move();
				try {
					// 让子弹前进,速度要比玩家速度快很多
					for (int i = 0; i < bulletAll.size(); i++) {
						Bullet bullet = bulletAll.get(i);
						bullet.setY(bullet.getY() - bullet.getSpeed());
					}
					Thread.sleep(65);// 线程休眠
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
					if (x > 160 && x < 260 && y > 150 && y < 180) {// 开始按钮
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
						// sound = new GameSound("src/sound/雷电 .wav");
						panel2.setVisible(false);
						newgame.setVisible(true);
						frame1.addKeyListener(new MyKeyDown());
						frame1.requestFocus();// 把输入焦点放在调用这个方法的控件上
						frame1.add(newgame);
						t.start();

					}
					if (x > 160 && x < 260 && y > 200 && y < 230) {// 游戏帮助按钮
						panel2.setVisible(false);
						helppanel.setVisible(true);
						help = false;
						frame1.add(helppanel);
					}

					if (x > 160 && x < 260 && y > 250 && y < 280) {// 游戏排行按钮
						panel2.setVisible(false);
						rl = new RankingList();
						rl.setVisible(true);
						list = false;
						frame1.add(rl);
					}
					if (x > 160 && x < 260 && y > 300 && y < 330) {// 游戏退出按钮
						exit = false;
						System.exit(0);
					}

				}

				public void mousePressed(MouseEvent e) {
					int x = e.getX();
					int y = e.getY();
					if (x > 160 && x < 260 && y > 150 && y < 180) {// 开始按钮
						start = true;
					}
					if (x > 160 && x < 260 && y > 200 && y < 230) {// 游戏帮助按钮
						help = true;
					}

					if (x > 160 && x < 260 && y > 250 && y < 280) {// 游戏排行按钮
						list = true;
					}
					if (x > 160 && x < 260 && y > 300 && y < 330) {// 游戏退出按钮
						exit = true;
					}
				}
			});
			t4.start();
		}

		public void paintComponent(Graphics g) {
			Image bg = new ImageIcon(getClass().getResource("/image/bg1.png")).getImage();
			g.drawImage(bg, 0, 0, 412, 572, null);
			if (start) {// 开始
				Image start1 = new ImageIcon(getClass().getResource("/image/satrt2.png")).getImage();
				g.drawImage(start1, 160, 150, 100, 30, null);
				t4.stop();
			} else {
				Image start2 = new ImageIcon(getClass().getResource("/image/start1.png")).getImage();
				g.drawImage(start2, 160, 150, 100, 30, null);
			}
			if (help) {// 帮助
				Image help1 = new ImageIcon(getClass().getResource("/image/help2.png")).getImage();
				g.drawImage(help1, 160, 200, 100, 30, null);

			} else {
				Image help2 = new ImageIcon(getClass().getResource("/image/help1.png")).getImage();
				g.drawImage(help2, 160, 200, 100, 30, null);
			}
			if (list) {// 排行
				Image list1 = new ImageIcon(getClass().getResource("/image/list2.png")).getImage();
				g.drawImage(list1, 160, 250, 100, 30, null);

			} else {
				Image list2 = new ImageIcon(getClass().getResource("/image/list1.png")).getImage();
				g.drawImage(list2, 160, 250, 100, 30, null);
			}
			if (exit) {// 退出
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
					if (x > 250 && x < 350 && y > 505 && y < 535) {// 按钮按键被按下时改变按钮图像
						panel2.setVisible(true);
						rl.setVisible(false);
					}
					if (x > 50 && x < 150 && y > 505 && y < 535) {// 按钮按键被按下时改变按钮图像
						MainFrame.playerList.clear();
						for (int i = 0; i < 3; i++) {
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
					if (x > 250 && x < 350 && y > 505 && y < 535) {// 按钮按键被按下时改变按钮图像
						fanhui = true;
					}
					if (x > 50 && x < 150 && y > 505 && y < 535) {// 按钮按键被按下时改变按钮图像
						reset = true;
					}
				}
			});
			t3.start();
		}
		public void paintComponent(Graphics g) {
			Image bg = new ImageIcon(getClass().getResource("/image/list.jpg")).getImage();
			g.drawImage(bg, 0, 0,412, 572, null);
			if (fanhui) {// 绘制返回按钮
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

	class GameOver extends JPanel implements ActionListener {
		JTextField filed;
		JLabel pName;
		JButton buttonG;
		Player player = new Player();
		boolean fanhui;
		MyThread2 t2 = new MyThread2();
		public GameOver() {
			super();
			pName = new JLabel("请输入姓名：");
			pName.setForeground(Color.black);
			filed = new JTextField(20);
			JLabel scoreLabel = new JLabel("本次得分：" + MainFrame.score + "分");
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
					if (x > 250 && x < 350 && y > 505 && y < 535) {// 按钮按键被按下时改变按钮图像
						panel2.setVisible(true);
						MainFrame.Go.setVisible(false);
					}
				}

				@Override
				public void mousePressed(MouseEvent e) {
					int x = e.getX();
					int y = e.getY();
					if (x > 250 && x < 350 && y > 505 && y < 535) {// 按钮按键被按下时改变按钮图像
						fanhui = true;
					}
				}

			});
			t2.start();
		}

		public void paintComponent(Graphics g) {
			Image bg = new ImageIcon(getClass().getResource("/image/bg4.jpg")).getImage();
			g.drawImage(bg, 0, 0, null);

			if (fanhui) {// 绘制返回按钮
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
				os.writeObject(MainFrame.playerList);// 将集合序列化
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

	class HelpPanel extends JPanel {// 游戏说明内部类
		boolean fanhui2;
		MyThread5 t5 = new MyThread5();

		public HelpPanel() {
			// JButton button = new JButton("返回");
			// this.setLayout(null);
			// button.setSize(100, 20);
			// button.setLocation(0, 0);
			// button.addActionListener(this);
			// this.add(button);
			this.addMouseListener(new MouseAdapter() {
				public void mouseReleased(MouseEvent e) {
					int x = e.getX();
					int y = e.getY();
					if (x > 250 && x < 350 && y > 505 && y < 535) {// 按钮按键被按下时改变按钮图像
						panel2.setVisible(true);
						helppanel.setVisible(false);
					}
				}

				public void mousePressed(MouseEvent e) {
					int x = e.getX();
					int y = e.getY();
					if (x > 250 && x < 350 && y > 505 && y < 535) {// 按钮按键被按下时改变按钮图像
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
			g.drawString("游戏说明", 130, 70);
			g.setFont(new Font(null, 2, 20));
			g.drawString("1、上下左右键控制飞机的四个方向移动", 5, 100);
			g.drawString("2、空格键控制飞机子弹发射，长按连发", 5, 150);
			g.drawString("3、击中敌人飞机可得到一定分数", 5, 200);
			g.drawString("4、被敌机击中一次会减少一定血量", 5, 250);
			g.drawString("5、敌机死亡有一定几率产生一个可吃的字母", 5, 300);
			g.drawString("6、各个字母分别可使飞机产生不同变化效果", 5, 350);
			if (fanhui2) {// 绘制返回按钮
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
