package com.leidian;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public abstract class GameBase extends Frame implements Framework {
	Toolkit toolkit = Toolkit.getDefaultToolkit();
	protected Line line;//����һ���߳�
	private int windows_width;
	private int windows_height;
	private int refresh=60;

	Graphics off;
	Image offImage;


	public void start() {
		init();
		line = new Line(this, refresh);
		this.setSize(windows_width, windows_height);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		this.setBackground(Color.black);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});//����
		line.start();//�����߳�
	}

	@Override
	public void update(Graphics g) {
		if (offImage == null) {
			offImage = createImage(windows_width, windows_height);
		}
		off = offImage.getGraphics();
		paint(off);
		off.dispose();
		g.drawImage(offImage, 0, 0, windows_width, windows_height, null);
	}

	public int getWidth() {
		return windows_width;
	}

	public void setWidth(int width) {
		this.windows_width = width;
	}

	public int getHeight() {
		return windows_height;
	}

	public void setHeight(int height) {
		this.windows_height = height;
	}

	public int getRefresh() {
		return refresh;
	}

	public void setRefresh(int refresh) {
		this.refresh = refresh;
	}

}
