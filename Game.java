import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

public class Game extends TimerTask{
	
	public static GUI GUI;
	
	public Game() {
		super();
		GUI = new GUI();
		GUI.setVisible(true);

		
	}
	
	public static void reset() {
		GUI = new GUI();
		GUI.setVisible(true);

	}
	
	@Override
    public void run(){
		GUI.panel.update();

	}
	public static void main(String[] args) {
		TimerTask timerTask = new Game();
		Timer timer = new Timer(true);
		timer.scheduleAtFixedRate(timerTask, 0, 10*10);		
	}

}

class Food {
	
	int x;
	int y;
	Dimension dim;
	Random rand = new Random();
	
	public Food() {
		this.x = rand.nextInt(28);
		this.y = rand.nextInt(28);
		this.dim = new Dimension(x, y);
	}
	
	public void newFood() {
		this.x = rand.nextInt(28);
		this.y = rand.nextInt(28);
		this.dim = new Dimension(x, y);
	}
}

class Snake {
	
	ArrayList<Dimension> snake;
	int dir;
	boolean hungry;
	
	public Snake() {
		
		dir = 0;
		hungry = true;
		this.snake = new ArrayList<Dimension>();
		this.snake.add(new Dimension(2,7));
		this.snake.add(new Dimension(2,6));
		this.snake.add(new Dimension(2,5));
	}
	
	public void changeDir(int x) {
		dir = x;
	}
	
	public int getDir() {
		return dir;
	}
	
	public boolean checkHungry(Dimension f) {
		for (Dimension xy : this.snake) {
			if (f.equals(xy)){
				return true;
			}
		}
		return false;
	}
	
	public void setHungry(boolean b) {
		hungry = b;
	}
	
	public void move() {

		if(canMove()) {
			if (hungry){
				this.snake.remove(0);
				
			}
			int x = this.snake.get(this.snake.size() - 1).width;
			int y = this.snake.get(this.snake.size() - 1).height;
			
			switch (dir){
			case 0: //up
				this.snake.add(new Dimension(x, y-1));break;
			case 1: //down
				this.snake.add(new Dimension(x, y+1));break;
			case 2: //left
				
				this.snake.add(new Dimension(x-1, y));break;
			case 3: //right
				this.snake.add(new Dimension(x+1, y));break;
			}
		}
	}

	private boolean canMove() {
		if ((dir == 0 && this.snake.get(this.snake.size() - 1).height == 0) || 
				(dir == 1 && this.snake.get(this.snake.size() - 1).height == 27) ||
				(dir == 2 && this.snake.get(this.snake.size() - 1).width == 0) ||
				(dir == 3 && this.snake.get(this.snake.size() - 1).width == 29)) {
			return false;
		} 
		for (int i = 0; i < this.snake.size()-2;i++) {
			if ((dir == 0 && this.snake.get(this.snake.size() - 1).height == this.snake.get(i).height+1 && this.snake.get(this.snake.size() - 1).width == this.snake.get(i).width) || 
					(dir == 1 && this.snake.get(this.snake.size() - 1).height == this.snake.get(i).height-1 && this.snake.get(this.snake.size() - 1).width == this.snake.get(i).width) ||
					(dir == 3 && this.snake.get(this.snake.size() - 1).width == this.snake.get(i).width-1 && this.snake.get(this.snake.size() - 1).height == this.snake.get(i).height) ||
					(dir == 2 && this.snake.get(this.snake.size() - 1).width == this.snake.get(i).width+1 && this.snake.get(this.snake.size() - 1).height == this.snake.get(i).height)) {
				return false;
			} 
		}
		return true;
	}
}

class GUI extends JFrame {

	private static final long serialVersionUID = 1L;
	
	Main panel = new Main();
	
	public GUI() {
		super();
	    setTitle("Snakeeeeee");
	    setResizable(false);
	    setPreferredSize(new Dimension(607,607));
	    setMinimumSize(new Dimension(607, 607));
	    setLayout(new BorderLayout());
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    add(panel);
	    
		
	    JMenuBar bar = new JMenuBar();
	    setJMenuBar(bar);
	    
	    JMenu menu = new JMenu("File");
	      bar.add(menu);
	    
	      JMenuItem item = new JMenuItem("Reset");
	      item.setAccelerator(KeyStroke.getKeyStroke('R', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	      item.setIcon(UIManager.getIcon("FileView.fileIcon"));
	      item.setEnabled(true);
	      item.addActionListener(new ActionListener() {
	          
	          @Override
	          public void actionPerformed(ActionEvent e) {
	        	  Game.reset();
	          }
	      });
	      menu.add(item);
	    
	      item = new JMenuItem("Open...");
	      item.setAccelerator(KeyStroke.getKeyStroke('O', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	      item.setIcon(UIManager.getIcon("Tree.openIcon"));
	      item.setEnabled(false);
	      menu.add(item);
	}
}

class Main extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	boolean game;
	
	final Color RED = new Color(255,0,0);
	final Color BLACK = new Color(0,0,0);
	final Color WHITE = new Color(255,255,255);
	
	final int SNAKE_SIZE = 20;
	
	Snake tom = new Snake();
	Food apple = new Food();
	
	public Main() {
		super();

		setBackground(WHITE);
		this.setFocusable(true);
		this.requestFocusInWindow();
				
	    addKeyListener(new KeyListener() {

	      @Override
	      public void keyTyped(KeyEvent e) {
	      }

	      @Override
	      public void keyPressed(KeyEvent e) {
	    	  }
	      

	      @Override
	      public void keyReleased(KeyEvent e) {
		      int keyCode = e.getKeyCode();
		    	  switch( keyCode ) { 
		        	case KeyEvent.VK_UP:
		        		tom.changeDir(0);
		        		break;
		        	case KeyEvent.VK_DOWN:
		        		tom.changeDir(1);
		        		break;
		        	case KeyEvent.VK_LEFT:
		        		tom.changeDir(2);
		        		break;
		        	case KeyEvent.VK_RIGHT :
		        		tom.changeDir(3);
		        		break;
	      }
	     }
	    });
	}
	    
	    public void update() {
	    	tom.move();
	    	tom.setHungry(true);
	    	if (tom.checkHungry(apple.dim)) {
	    		tom.setHungry(false);
	    	}
	    	if (!tom.hungry) {
	    		apple.newFood();
	    	}
	    	repaint();
	    }
	   
	

	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
	    
	    Graphics2D graphics = (Graphics2D)g;
	    
	    Random rand = new Random();
	    
	    for (int i = 0; i <= 29;i++) {
	    	for (int j = 0; j <= 27;j++) {
	    		int r = rand.nextInt(5) + 200;
	    		Color c = new Color(0,r,0);
	    		graphics.setColor(c);
	    		graphics.fillRect(i*SNAKE_SIZE, j*SNAKE_SIZE, SNAKE_SIZE, SNAKE_SIZE);
	    	}
	    }
	   	
	    for (Dimension xy : tom.snake) {
	    	graphics.setColor(BLACK);
	    	graphics.fillOval(xy.width*SNAKE_SIZE, xy.height*SNAKE_SIZE, SNAKE_SIZE, SNAKE_SIZE);
	    
	    graphics.setColor(RED);
	    graphics.drawLine(tom.snake.get(tom.snake.size()-1).width*SNAKE_SIZE+5, tom.snake.get(tom.snake.size()-1).height*SNAKE_SIZE+5, tom.snake.get(tom.snake.size()-1).width*SNAKE_SIZE+15, tom.snake.get(tom.snake.size()-1).height*SNAKE_SIZE+15);
	    graphics.drawLine(tom.snake.get(tom.snake.size()-1).width*SNAKE_SIZE+15, tom.snake.get(tom.snake.size()-1).height*SNAKE_SIZE+5, tom.snake.get(tom.snake.size()-1).width*SNAKE_SIZE+5, tom.snake.get(tom.snake.size()-1).height*SNAKE_SIZE+15);
	    
	    graphics.setColor(RED);
	    graphics.fillOval(apple.x*SNAKE_SIZE, apple.y*SNAKE_SIZE, SNAKE_SIZE, SNAKE_SIZE);
	    	
	    }	    
	}	
}