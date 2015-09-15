package gui;

import javax.swing.JFrame;

import config.Config;


public class JFRAME extends JFrame {
	private static final long serialVersionUID = 1L;
	private Surface surface;

	public Surface getSurface() {
		return surface;
	}

	public JFRAME() {
		initUI();
	}

	private void initUI() {
		surface = new Surface();
		add(surface);
		addMouseListener(new MouseClickHandler());
		addMouseMotionListener(new MouseMotionHandler());
		setSize(Config.windowX,Config.windowY);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}