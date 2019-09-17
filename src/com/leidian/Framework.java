package com.leidian;

import java.awt.Frame;

public interface Framework {

	public void start();

	public void init();

	class Line extends Thread {//Ïß³Ì
		Frame frame;
		int refesh;
		boolean isStop = false;

		public Line(Frame frame, int refesh) {
			this.frame = frame;
			this.refesh = refesh;
		}

		public void runStop() {
			isStop = true;
		}

		public void run() {
			while (!isStop) {
				try {
					frame.repaint();
					Thread.sleep(refesh);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
