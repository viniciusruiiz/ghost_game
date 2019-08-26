package com.learning.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import com.learning.controller.PlayerController;
import com.learning.entities.Entity;
import com.learning.entities.Player;
import com.learning.graphics.Spritesheet;
import com.learning.world.World;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning;
	public static final int WIDTH = 240;
	public static final int HEIGHT = 160;
	public static final int SCALE = 4;
	
	private BufferedImage image;
	
	public static Player player;
	
	private PlayerController playerController;
	public static List<Entity> entities;
	public static Spritesheet spritesheet;
	
	public static World world;
	
	public Game() {
		
		//inicializando o frame
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		initFrame();
		
		//inicializando entidades
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		
		spritesheet = new Spritesheet("/spritesheet.png");
		
		entities = new ArrayList<Entity>();
		player = new Player(0, 0, 16, 16, spritesheet.getSprite(48, 0, 16, 16));
		entities.add(player);
		
		world = new World("/map.png");
		
		playerController = new PlayerController(player);
		addKeyListener(playerController);
	}
	
	public void initFrame() {
		frame = new JFrame("DankiCode Zelda");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public synchronized void start() {
		isRunning = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public synchronized void stop() throws InterruptedException {
		isRunning = false;
		thread.join();
	}
	
	public static void main(String[] args) {
			Game game = new Game();
			game.start();
	}
	
	public void tick() {
		for(Entity e : entities)
			e.tick();
	}
	
	public void render() {
		BufferStrategy bufferStrategy = getBufferStrategy();
		if(bufferStrategy == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		world.render(g);
		
		for(Entity e : entities)
			e.render(g);
		
		g.dispose();
		g = bufferStrategy.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
		bufferStrategy.show();
	}
	
	@Override
	public void run() {
		
		requestFocus();
		
		double amountOfTicks = 60;
		long lastTime = System.nanoTime();
		double nanoSeconds = 1000000000 / amountOfTicks;
		double delta = 0;
		
		//debug
		int frames = 0;
		double timer = System.currentTimeMillis();
		
		while(isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / nanoSeconds;
			lastTime = now;
			
			if(delta >= 1) {
				tick();
				render();
				delta--;
				
				//debug
				frames++;
			}
			
			if(System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS: " + frames);
				frames = 0;
				timer += 1000;
			}
		}
		
		try {
			stop();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
