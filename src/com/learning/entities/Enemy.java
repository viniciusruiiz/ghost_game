package com.learning.entities;

import java.awt.image.BufferedImage;

import com.learning.main.Game;
import com.learning.world.World;

public class Enemy extends Entity{

	private double speed = 0.6;
	
	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
	}

	public void tick() {
		if(getX() < Game.player.getX() && World.isFree((int)(x + speed), getY())) {
			x += speed;
		}
		
		else if(getX() > Game.player.getX() && World.isFree((int)(x - speed), getY())) {
			x -= speed;
		}
		
		if(getY() > Game.player.getY() && World.isFree(getX(), (int)(y - speed))) {
			y -= speed;
		}
		
		else if(getY() < Game.player.getY() && World.isFree(getX(), (int)(y + speed))) {
			y += speed;
		}
	}
	
}
