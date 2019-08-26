package com.learning.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.learning.entities.*;
import com.learning.main.Game;

public class World {
	
	private static Tile[] tiles;
	public static int WIDTH, HEIGHT;
	public static final int TILE_SIZE = 16;
	
	public World(String path) {
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			int[] pixels = new int[map.getWidth() * map.getHeight()];
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			tiles = new Tile[map.getWidth() * map.getHeight()];
			
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
			
			for (int xx = 0; xx < map.getWidth(); xx++) {
				for(int yy = 0; yy < map.getHeight(); yy++) {
					
					int curPositionTile = xx + yy * WIDTH;
					
					tiles[curPositionTile] = new FloorTile(xx * 16, yy * 16,Tile.TILE_FLOOR );
					switch(pixels[xx + yy*map.getWidth()]) {
						case(0xFF000000):
							//chao
							//o chao ja era renderizado antes , entao nao tem porque fazelo novamente
							break;
						case(0xFFFFFFFF):
							//parede
							tiles[curPositionTile] = new WallTile(xx * 16, yy * 16,Tile.TILE_WALL );
							break;
						case(0xFF0026FF):
							//player
							Game.player.setX(xx*16);
							Game.player.setY(yy*16);
							break;
						case(0xFF4CFF00):
							//inimigo
							Game.entities.add(new Enemy(xx * 16, yy * 16, 16, 16, Entity.ENEMY_EN));
							break;
						case(0xFFE500FF):
							//weapon
							Game.entities.add(new Weapon(xx * 16, yy * 16, 16, 16, Entity.WEAPON_EN));
							break;
						case(0XFFFFCC00):
							//bullet
							Game.entities.add(new Bullet(xx * 16, yy * 16, 16, 16, Entity.BULLET_EN));
							break;
						case(0XFFFF000C):
							//lifepack
							Game.entities.add(new LifePack(xx * 16, yy * 16, 16, 16, Entity.LIFEPACK_EN));
							break;
					
					}
				}
			}
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
	
	public static boolean isFree(int xNext, int yNext) {
		int x1 = xNext / TILE_SIZE;
		int y1 = yNext / TILE_SIZE;
		
		int x2 = (xNext + TILE_SIZE - 1) / TILE_SIZE;
		int y2 = yNext / TILE_SIZE;
		
		int x3 = xNext / TILE_SIZE;
		int y3 = (yNext + TILE_SIZE - 1) / TILE_SIZE;
		
		int x4 = (xNext + TILE_SIZE - 1) / TILE_SIZE;
		int y4 = (yNext + TILE_SIZE - 1) / TILE_SIZE;
		
		return !(tiles[x1 + y1 * WIDTH] instanceof WallTile
				|| tiles[x2 + y2 * WIDTH] instanceof WallTile
				|| tiles[x3 + y3 * WIDTH] instanceof WallTile
				|| tiles[x4 + y4 * WIDTH] instanceof WallTile);
	}
	
	public void render(Graphics g) {
		
		int xstart = Camera.x / TILE_SIZE;
		int ystart = Camera.y / TILE_SIZE;
		
		int xfinal = xstart + Game.WIDTH / TILE_SIZE;
		int yfinal = ystart + Game.HEIGHT / TILE_SIZE;
		
		for (int xx = xstart; xx <= xfinal; xx++) {
			for (int yy = ystart; yy <= yfinal; yy++) {
				if(xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT)
					continue;
				Tile tile = tiles[xx + (yy * WIDTH)];
				tile.render(g);
			}
		}
	}
}
