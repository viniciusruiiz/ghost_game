package com.learning.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.learning.enumerate.DirectionEnum;
import com.learning.main.Game;
import com.learning.world.Camera;
import com.learning.world.World;

public class Player extends Entity {
	
	public boolean right, left, up, down;
	public double speed = 1.5;
	
	
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	private BufferedImage[] upPlayer;
	private BufferedImage[] downPlayer;
	private BufferedImage currentSprite;
	private DirectionEnum currentDirection;
	private int currentAnimation;
	private int maxAnimation = 2;
	private int frames = 0;
	private int maxFrames = 4;

	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		upPlayer = new BufferedImage[4];
		downPlayer = new BufferedImage[4];
		
		for(int i = 0; i < rightPlayer.length; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(48 + i*16, 32, 16, 16);
		}
		
		for(int i = 0; i < leftPlayer.length; i++) {
			leftPlayer[i] = Game.spritesheet.getSprite(48 + i*16, 48, 16, 16);
		}
		
		for(int i = 0; i < upPlayer.length; i++) {
			upPlayer[i] = Game.spritesheet.getSprite(48 + i*16, 16, 16, 16);
		}
		
		for(int i = 0; i < downPlayer.length; i++) {
			downPlayer[i] = Game.spritesheet.getSprite(48 + i*16, 0, 16, 16);
		}
		
		currentSprite = rightPlayer[0];
	}

	public void tick() {
		
		double spdDiagonal = 1.25;
		
		if(right && up && World.isFree((int)(x+speed), getY()) && World.isFree(getX(), (int)(y-speed))) {
			speed /= spdDiagonal;
			moveRight();
			y -= speed;
			speed *= spdDiagonal;
		}
		
		else if(left && up && World.isFree((int)(x-speed), getY()) && World.isFree(getX(), (int)(y-speed))) {
			speed /= spdDiagonal;
			moveLeft();
			y -= speed;
			speed *= spdDiagonal;
		}
		
		else if(right && down && World.isFree((int)(x+speed), getY()) && World.isFree(getX(), (int)(y+speed))) {
			speed /= spdDiagonal;
			moveRight();
			y += speed;
			speed *= spdDiagonal;
		}
		
		else if(left && down && World.isFree((int)(x-speed), getY()) && World.isFree(getX(), (int)(y+speed))) {
			speed /= spdDiagonal;
			moveLeft();
			y += speed;
			speed *= spdDiagonal;
		}
		
		else if(right && World.isFree((int)(x+speed), getY())) {
			moveRight();
		} 
		
		else if (left && World.isFree((int)(x-speed), getY())) {
			moveLeft();
		}
		
		else if(down && World.isFree(getX(), (int)(y+speed))) {
			moveDown();
		} 
		
		else if(up && World.isFree(getX(), (int)(y-speed))){
			moveUp();
		}
		
		Camera.x = Camera.clamp((int)x - (Game.WIDTH/2), 0, World.WIDTH*16 - Game.WIDTH);
		Camera.y = Camera.clamp((int)y - (Game.HEIGHT/2), 0, World.HEIGHT*16 - Game.HEIGHT);
	}
	
	private void moveRight() {
		frames++;
		if(currentDirection == DirectionEnum.RIGHT) {
			if(frames > maxFrames) {
					frames = 0;
					currentAnimation = currentAnimation > maxAnimation ? 0 : currentAnimation + 1;
					currentSprite = rightPlayer[currentAnimation];
			}
		} else {
			frames = 0;
			currentDirection = DirectionEnum.RIGHT;
			currentAnimation = 0;
			currentSprite = rightPlayer[0];
		}
		x += speed;
	}
	
	private void moveLeft() {
		frames++;
		if(currentDirection == DirectionEnum.LEFT) {
			if(frames > maxFrames) {
				frames = 0;
				currentAnimation = currentAnimation > maxAnimation ? 0 : currentAnimation + 1;
				currentSprite = leftPlayer[currentAnimation];
			}
		}else {
			frames = 0;
			currentDirection = DirectionEnum.LEFT;
			currentAnimation = 0;
			currentSprite = leftPlayer[0];
		}
		x -= speed;
	}
	
	private void moveUp() {
		frames++;
		if(currentDirection == DirectionEnum.UP) {
			if(frames > maxFrames) {
				frames = 0;
				currentAnimation = currentAnimation > maxAnimation ? 0 : currentAnimation + 1;
				currentSprite = upPlayer[currentAnimation];
			}
		} else {
			frames = 0;
			currentDirection = DirectionEnum.UP;
			currentAnimation = 0;
			currentSprite = upPlayer[0];
		}
		y -= speed;
	}
	
	private void moveDown() {
		frames++;
		if(currentDirection == DirectionEnum.DOWN) {
			if(frames > maxFrames) {
				frames = 0;
				currentAnimation = currentAnimation > maxAnimation ? 0 : currentAnimation + 1;
				currentSprite = downPlayer[currentAnimation];
			}
		}else {
			frames = 0;
			currentDirection = DirectionEnum.DOWN;
			currentAnimation = 0;
			currentSprite = downPlayer[0];
		}
		y += speed;
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(currentSprite, (int)x - Camera.x, (int)y - Camera.y, null);
	}
	
}
