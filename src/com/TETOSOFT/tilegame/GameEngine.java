package com.TETOSOFT.tilegame;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Iterator;

import com.TETOSOFT.graphics.*;
import com.TETOSOFT.input.*;
import com.TETOSOFT.test.GameCore;
import com.TETOSOFT.tilegame.sprites.*;


/**
 * GameManager manages all parts of the game.
 */
public class GameEngine extends GameCore 
{
    
    public static void main(String[] args) 
    {
        javafx.application.Application.launch(com.TETOSOFT.tilegame.GameMenu.class);

        //new GameEngine().run();
    }
    
    public static final float GRAVITY = 0.002f;
    
    private Point pointCache = new Point();
    private TileMap map;
    private MapLoader mapLoader;
    private InputManager inputManager;
    private TileMapDrawer drawer;
    private GameAction moveLeft;
    private GameAction moveRight;
    private GameAction jump;
    private GameAction exit;
    private GameAction restart;
    private int collectedStars=0;
    private int numLives=6;
    ScoreTimer timer = new ScoreTimer();
    private int score;
    SoundManager st = new SoundManager();
    private Boolean gameover = false;


    public void startGame() {
      	 // start resource manager
          mapLoader = new MapLoader(screen.getFullScreenWindow().getGraphicsConfiguration());
          
          // load resources
          drawer = new TileMapDrawer();
          drawer.setBackground(mapLoader.loadImage("background.jpg"));
          
          
          // load first map
          map = mapLoader.loadNextMap();
      }
    
    public void init()
    {
        super.init();
        
        // set up input manager
        initInput();

        st.loopMusic("audio/background.wav");

        //Sound.background.loop();
        startGame();

        timer.start();
        
        System.out.println(mapLoader.currentMap);
    }
    
    
    /**
     * Closes any resurces used by the GameManager.
     */
    public void stop() {
        super.stop();
        
    }
    
    
    private void initInput() {
        moveLeft = new GameAction("moveLeft");
        moveRight = new GameAction("moveRight");
        jump = new GameAction("jump", GameAction.DETECT_INITAL_PRESS_ONLY);
        exit = new GameAction("exit",GameAction.DETECT_INITAL_PRESS_ONLY);
        restart = new GameAction("restart",GameAction.DETECT_INITAL_PRESS_ONLY);
        
        inputManager = new InputManager(screen.getFullScreenWindow());
        inputManager.setCursor(InputManager.INVISIBLE_CURSOR);
        
        inputManager.mapToKey(moveLeft, KeyEvent.VK_LEFT);
        inputManager.mapToKey(moveRight, KeyEvent.VK_RIGHT);
        inputManager.mapToKey(jump, KeyEvent.VK_SPACE);
        inputManager.mapToKey(exit, KeyEvent.VK_ESCAPE);
        inputManager.mapToKey(restart, KeyEvent.VK_R);
        
    }
    
    
    private void checkInput(long elapsedTime) 
    {
        
        if (exit.isPressed()) {
            stop();
        }
        
        if(restart.isPressed()) {
        	//startGame();

            new GameEngine().run();
        }
        
        Player player = (Player)map.getPlayer();
        if (player.isAlive()) 
        {
            float velocityX = 0;
            if (moveLeft.isPressed()) 
            {
                velocityX-=player.getMaxSpeed();
            }
            if (moveRight.isPressed()) {
                velocityX+=player.getMaxSpeed();
            }
            if (jump.isPressed()) {
                player.jump(false);
                st.playMusic("audio/jump.wav");
            }
            player.setVelocityX(velocityX);
        }
        
    }
    
    
   
    
    
    public void draw(Graphics2D g) {
        
        drawer.draw(g, map, screen.getWidth(), screen.getHeight());
        g.setColor(Color.WHITE);
        g.drawString("Press ESC for EXIT.",10.0f,20.0f);
        g.setColor(Color.GREEN);
        g.drawString("Coins: "+collectedStars,250.0f,20.0f);
        g.setColor(Color.YELLOW);
        g.drawString("Lives: "+(numLives),400.0f,20.0f );
        //TIMER
        g.setColor(Color.RED);
        g.drawString("Time: "+ timer.secondPassed,550.0f,20.0f);

        g.setColor(Color.WHITE);
        g.drawString("Level: "+mapLoader.currentMap,700.0f,20.0f);
        
        g.setColor(Color.CYAN);
        g.drawString("Score: "+score,700.0f,50.0f);


        if(gameover) {
        	
        	String s1 = "Game Over";
        	String s2 = "Press R to Restart";
        	String s3 = "Press ESC to Exit";

            g.setColor(Color.black);
            g.fillRect(0, 0, screen.getWidth(), screen.getHeight());
            Font big = new Font("Helvetica", Font.BOLD, 50);
            Font small = new Font("Helvetica", Font.BOLD, 20);
            FontMetrics f = g.getFontMetrics(big);
            FontMetrics f2 = g.getFontMetrics(small);
            g.setColor(Color.white);
            g.setFont(big);
            g.drawString(s1, (screen.getWidth() - f.stringWidth(s1))/2, screen.getHeight()/2);
            g.setColor(Color.white);
            g.setFont(small);
            g.drawString(s2, (screen.getWidth() - f2.stringWidth(s2))/2, screen.getHeight()/2 + 50);
            g.setColor(Color.white);
            g.setFont(small);
            g.drawString(s3, (screen.getWidth() - f2.stringWidth(s3))/2, screen.getHeight()/2 + 100);
            
        } 
        
        
        if (mapLoader.currentMap == 5) {
             String cm = "Congratulations";
             String scoreString = "Your Score is " + score; 
             String s2 = "Press R to Restart";
          	 String s3 = "Press ESC to Exit";
        	 g.setColor(Color.black);
             g.fillRect(0, 0, screen.getWidth(), screen.getHeight());
             Font big = new Font("Helvetica", Font.BOLD, 50);
             Font small = new Font("Helvetica", Font.BOLD, 20);
             FontMetrics fm = g.getFontMetrics(big);
             FontMetrics fm2 = g.getFontMetrics(small);
             g.setColor(Color.white);
             g.setFont(big);
             g.drawString(cm, (screen.getWidth() - fm.stringWidth(cm))/2 , screen.getHeight()/3);
             g.setColor(Color.yellow);
             g.setFont(small);
             g.drawString(scoreString, (screen.getWidth() - fm2.stringWidth(scoreString))/2 , screen.getHeight()/3 + 50);
             g.setColor(Color.white);
             g.setFont(small);
             g.drawString(s2, (screen.getWidth() - fm2.stringWidth(s2))/2, screen.getHeight()/2 + 100);
             g.setColor(Color.white);
             g.setFont(small);
             g.drawString(s3, (screen.getWidth() - fm2.stringWidth(s3))/2, screen.getHeight()/2 + 150);
             
        }
        
    }
    
    
    /**
     * Gets the current map.
     */
    public TileMap getMap() {
        return map;
    }
    
    /**
     * Gets the tile that a Sprites collides with. Only the
     * Sprite's X or Y should be changed, not both. Returns null
     * if no collision is detected.
     */
    public Point getTileCollision(Sprite sprite, float newX, float newY) 
    {
        float fromX = Math.min(sprite.getX(), newX);
        float fromY = Math.min(sprite.getY(), newY);
        float toX = Math.max(sprite.getX(), newX);
        float toY = Math.max(sprite.getY(), newY);
        
        // get the tile locations
        int fromTileX = TileMapDrawer.pixelsToTiles(fromX);
        int fromTileY = TileMapDrawer.pixelsToTiles(fromY);
        int toTileX = TileMapDrawer.pixelsToTiles(
                toX + sprite.getWidth() - 1);
        int toTileY = TileMapDrawer.pixelsToTiles(
                toY + sprite.getHeight() - 1);
        
        // check each tile for a collision
        for (int x=fromTileX; x<=toTileX; x++) {
            for (int y=fromTileY; y<=toTileY; y++) {
                if (x < 0 || x >= map.getWidth() ||
                        map.getTile(x, y) != null) {
                    // collision found, return the tile
                    pointCache.setLocation(x, y);
                    return pointCache;
                }
            }
        }
        
        // no collision found
        return null;
    }
    
    
    /**
     * Checks if two Sprites collide with one another. Returns
     * false if the two Sprites are the same. Returns false if
     * one of the Sprites is a Creature that is not alive.
     */
    public boolean isCollision(Sprite s1, Sprite s2) {
        // if the Sprites are the same, return false
        if (s1 == s2) {
            return false;
        }
        
        // if one of the Sprites is a dead Creature, return false
        if (s1 instanceof Creature && !((Creature)s1).isAlive()) {
            return false;
        }
        if (s2 instanceof Creature && !((Creature)s2).isAlive()) {
            return false;
        }
        
        // get the pixel location of the Sprites
        int s1x = Math.round(s1.getX());
        int s1y = Math.round(s1.getY());
        int s2x = Math.round(s2.getX());
        int s2y = Math.round(s2.getY());
        
        // check if the two sprites' boundaries intersect
        return (s1x < s2x + s2.getWidth() &&
                s2x < s1x + s1.getWidth() &&
                s1y < s2y + s2.getHeight() &&
                s2y < s1y + s1.getHeight());
    }
    
    
    /**
     * Gets the Sprite that collides with the specified Sprite,
     * or null if no Sprite collides with the specified Sprite.
     */
    public Sprite getSpriteCollision(Sprite sprite) {
        
        // run through the list of Sprites
        Iterator i = map.getSprites();
        while (i.hasNext()) {
            Sprite otherSprite = (Sprite)i.next();
            if (isCollision(sprite, otherSprite)) {
                // collision found, return the Sprite
                return otherSprite;
            }
        }
        
        // no collision found
        return null;
    }
    
    
    /**
     * Updates Animation, position, and velocity of all Sprites
     * in the current map.
     */
    public void update(long elapsedTime) {
        Creature player = (Creature)map.getPlayer();
        
        
        // player is dead! start map over
        if (player.getState() == Creature.STATE_DEAD) {
            map = mapLoader.reloadMap();
            return;
        }
        
        // get keyboard/mouse input
        checkInput(elapsedTime);
        
        // update player
        updateCreature(player, elapsedTime);
        player.update(elapsedTime);
        
        // update other sprites
        Iterator i = map.getSprites();
        while (i.hasNext()) {
            Sprite sprite = (Sprite)i.next();
            if (sprite instanceof Creature) {
                Creature creature = (Creature)sprite;
                if (creature.getState() == Creature.STATE_DEAD) {
                    i.remove();
                } else {
                    updateCreature(creature, elapsedTime);
                }
            }
            // normal update
            sprite.update(elapsedTime);
        }
    }
    
    
    /**
     * Updates the creature, applying gravity for creatures that
     * aren't flying, and checks collisions.
     */
    private void updateCreature(Creature creature,
            long elapsedTime) {
        
        // apply gravity
        if (!creature.isFlying()) {
            creature.setVelocityY(creature.getVelocityY() +
                    GRAVITY * elapsedTime);
        }
        
        // change x
        float dx = creature.getVelocityX();
        float oldX = creature.getX();
        float newX = oldX + dx * elapsedTime;
        Point tile =
                getTileCollision(creature, newX, creature.getY());
        if (tile == null) {
            creature.setX(newX);
        } else {
            // line up with the tile boundary
            if (dx > 0) {
                creature.setX(
                        TileMapDrawer.tilesToPixels(tile.x) -
                        creature.getWidth());
            } else if (dx < 0) {
                creature.setX(
                        TileMapDrawer.tilesToPixels(tile.x + 1));
            }
            creature.collideHorizontal();
        }
        if (creature instanceof Player) {
            checkPlayerCollision((Player)creature, false);
        }
        
        // change y
        float dy = creature.getVelocityY();
        float oldY = creature.getY();
        float newY = oldY + dy * elapsedTime;
        tile = getTileCollision(creature, creature.getX(), newY);
        if (tile == null) {
            creature.setY(newY);
        } else {
            // line up with the tile boundary
            if (dy > 0) {
                creature.setY(
                        TileMapDrawer.tilesToPixels(tile.y) -
                        creature.getHeight());
            } else if (dy < 0) {
                creature.setY(
                        TileMapDrawer.tilesToPixels(tile.y + 1));
            }
            creature.collideVertical();
        }
        if (creature instanceof Player) {
            boolean canKill = (oldY < creature.getY());
            checkPlayerCollision((Player)creature, canKill);
        }
        
    }
    
    
    /**
     * Checks for Player collision with other Sprites. If
     * canKill is true, collisions with Creatures will kill
     * them.
     */
    public void checkPlayerCollision(Player player,
            boolean canKill) {
        if (!player.isAlive()) {
            return;
        }
        
        // check for player collision with other sprites
        Sprite collisionSprite = getSpriteCollision(player);
        if (collisionSprite instanceof PowerUp) {
            acquirePowerUp((PowerUp)collisionSprite);
        } else if (collisionSprite instanceof Creature) {
            Creature badguy = (Creature)collisionSprite;
            if (canKill) {
                // kill the badguy and make player bounce
                badguy.setState(Creature.STATE_DYING);
                player.setY(badguy.getY() - player.getHeight());
                player.jump(true);
                st.playMusic("audio/stomp.wav");
                score = score + 100; 
                
            } else {
                // player dies!
                player.setState(Creature.STATE_DYING);
                numLives--;
                st.stopMusic();
                st.playMusic("audio/marioDies.wav");
                
                

                if(numLives==0) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    st.stopMusic();
                    //gameover = false;
                    gameover= true;
                    //stop();
                }
            }
        }
    }
    
    
    /**
     * Gives the player the speicifed power up and removes it
     * from the map.
     */
    public void acquirePowerUp(PowerUp powerUp) {
        // remove it from the map
        map.removeSprite(powerUp);
        
        if (powerUp instanceof PowerUp.Star) {
            // do something here, like give the player points
            collectedStars++;
            st.playMusic("audio/coin.wav");
            //if(collectedStars==100)
            if(collectedStars==20)
            {
                numLives++;
                collectedStars=0;
            }
            
        } else if (powerUp instanceof PowerUp.Music) {
            // change the music
            
        } else if (powerUp instanceof PowerUp.Goal) {
            // advance to next map      
      
            map = mapLoader.loadNextMap();
           
            
        }
    }
    
      
}