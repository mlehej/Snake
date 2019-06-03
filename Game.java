import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
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

//Mechanics
public class Game extends TimerTask{
	
	public static GUI GUI;
	
	private static boolean over = false;
	
	public static boolean firstTime=true;
	
	public Game() {
		super();
		
		//Skonstruiramo graficni vmesnik
		GUI = new GUI();
		GUI.setVisible(true);	
	}
	
	public static void reset() {
		GUI.panel.tom.reset();
	}
	
	public static void start() {
		reset();
	}
	
	public static void end() {
		//Da je ob prvem pogonu drugacen prikaz
		if(firstTime)
			firstTime=false;
		if (over) {
			reset();
			over = false;
		}
		GUI.panel.changeGame();
	}
	
	
	public static boolean status() {
		return over;
	}
	
	
	
	@Override
    public void run(){
		GUI.panel.update();
	}
	public static void main(String[] args){
		TimerTask timerTask = new Game();
		Timer timer = new Timer(true);
		timer.scheduleAtFixedRate(timerTask, 0, 110);	
	}

	public static void over() {
		GUI.panel.changeGame();
		over = true;
	}

}

class Food {
	
	int x;
	int y;
	Dimension dim;
	Random rand = new Random();
	
	public Food() {
		//Zgenerira jabolko
		this.x = rand.nextInt(28);
		this.y = rand.nextInt(28);
		this.dim = new Dimension(x, y);
	}
	
	public void newFood() {
		//Zgenerira novo jabolko
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
		//Osnovna postavitev kace
		dir = 1;
		hungry = true;
		this.snake = new ArrayList<Dimension>();
		this.snake.add(new Dimension(2,5));
		this.snake.add(new Dimension(2,6));
		this.snake.add(new Dimension(2,7));
	}
	
	public void changeDir(int x) {
		//Funkcija za manipuliranje smeri kace
		switch (x) {
		case 0:
			if (!(dir == 1)) {
				dir = 0;
			} break;
		case 1:
			if (!(dir == 0)) {
				dir = 1;
			} break;
		case 2:
			if (!(dir == 3)) {
				dir = 2;
			} break;
		case 3:
			if (!(dir == 2)) {
				dir = 3;
			} break;
		}
	}
	
	public void reset()	{
		//Kaco postavi na inicialne vrenosti
		this.snake.clear();
		dir = 1;
		this.snake.add(new Dimension(2,5));
		this.snake.add(new Dimension(2,6));
		this.snake.add(new Dimension(2,7));
	}
	
	public int getDir() {
		//Getter za smer kace
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
		if(!canMove()) {
			Game.over();
		}
		else {		
			if (hungry){ //Ce ne poje jabolka se pomaknemo naprej
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
		//Moznost premika?
		
		//Zidovi
		if ((dir == 0 && this.snake.get(this.snake.size() - 1).height == 0) || 
				(dir == 1 && this.snake.get(this.snake.size() - 1).height == 27) ||
				(dir == 2 && this.snake.get(this.snake.size() - 1).width == 0) ||
				(dir == 3 && this.snake.get(this.snake.size() - 1).width == 29)) {
			return false;
		} 
		
		//Sama sebe
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
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


//Visual
class GUI extends JFrame {

	private static final long serialVersionUID = 1L;
	
	Main panel = new Main();
	
	public GUI() {
		super();
	    setTitle("Snake");
	    setResizable(false);
	    setPreferredSize(new Dimension(607,607));
	    setMinimumSize(new Dimension(607, 607));
	    setLayout(new BorderLayout());
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    add(panel);
		//Toolbar
	    JMenuBar bar = new JMenuBar();
	    setJMenuBar(bar);
	    
	    //Meni v toolbaru
	    JMenu menu = new JMenu("Game");
	      bar.add(menu);
	      //Opcija NEW
	      JMenuItem item = new JMenuItem("New");
	      item.setAccelerator(KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	      item.setIcon(UIManager.getIcon("FileView.fileIcon"));
	      menu.add(item);
	      item.addActionListener(new ActionListener() {
	          
	          @Override
	          public void actionPerformed(ActionEvent e) {
	        	  Game.start();
	          }
	      });   
	      
	      //Opcija PAUSE
	      item = new JMenuItem("Pause");
	      item.setAccelerator(KeyStroke.getKeyStroke('P', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	      item.setIcon(UIManager.getIcon("FileView.fileIcon"));
	      menu.add(item);
	      item.addActionListener(new ActionListener() {
	          
	          @Override
	          public void actionPerformed(ActionEvent e) {
	        	  Game.end();;
	          }
	      });   

	}
}

class Main extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	
	private boolean game = false;
	
	final Color RED = new Color(255,0,0);
	final Color BLACK = new Color(0,0,0);
	final Color WHITE = new Color(255,255,255);
	final Color DIMSCREEN = new Color(0,0,0,170);
	
	final int SNAKE_SIZE = 20;
	
	Snake tom = new Snake();
	Food apple = new Food();
	
	public Main() {
		super();
		setBackground(new Color(0,200,150,200));
		this.setFocusable(true);
		this.requestFocusInWindow();
				
	    addKeyListener(new KeyListener() {

	      @Override
	      public void keyTyped(KeyEvent e) {
	      }

	      @Override
	      public void keyPressed(KeyEvent e) {
		      int keyCode = e.getKeyCode();
		      if (game)
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
	      

	      @Override
	      public void keyReleased(KeyEvent e) {
		      /*int keyCode = e.getKeyCode();
		      if (game)
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
	      }*/
	     }
	    });
	}
	    
	    public void update() {
	    	if (game) {
		    	tom.move();
		    	tom.setHungry(true);
		    	if (tom.checkHungry(apple.dim)) {
		    		tom.setHungry(false);
		    	}
		    	if (!tom.hungry) {
		    		apple.newFood();
		    	}
	    	}	
	    	repaint();	
	    }
	   
	
	    public void changeGame() {
	    	game = !game;
	    }

	
	@Override
	public void paint(Graphics g) {
		//Glavna funkcija za izrisovanje
		super.paint(g);
	    Graphics2D graphics = (Graphics2D)g;
	    
	    for (int i = 0; i < tom.snake.size() -1; i++) {
	    	Dimension xy = new Dimension(tom.snake.get(i));
	    	graphics.setColor(BLACK);
	    	graphics.fillRect(xy.width*SNAKE_SIZE, xy.height*SNAKE_SIZE, SNAKE_SIZE, SNAKE_SIZE);
	    	
	     }
	    graphics.fillOval(tom.snake.get(tom.snake.size()-1).width*SNAKE_SIZE, tom.snake.get(tom.snake.size()-1).height*SNAKE_SIZE, SNAKE_SIZE, SNAKE_SIZE);
	    
	    switch (tom.getDir()){
		case 0: //up
		    graphics.fillRect(tom.snake.get(tom.snake.size()-1).width*SNAKE_SIZE, tom.snake.get(tom.snake.size()-1).height*SNAKE_SIZE+SNAKE_SIZE/2, SNAKE_SIZE, SNAKE_SIZE/2+2);
		    graphics.setColor(WHITE);
		    graphics.fillOval(tom.snake.get(tom.snake.size()-1).width*SNAKE_SIZE+SNAKE_SIZE/3, tom.snake.get(tom.snake.size()-1).height*SNAKE_SIZE+SNAKE_SIZE/2, SNAKE_SIZE/3, SNAKE_SIZE/3);
		    graphics.fillOval(tom.snake.get(tom.snake.size()-1).width*SNAKE_SIZE+SNAKE_SIZE/3*2, tom.snake.get(tom.snake.size()-1).height*SNAKE_SIZE+SNAKE_SIZE/2, SNAKE_SIZE/3, SNAKE_SIZE/3);break;
		case 1: //down
		    graphics.fillRect(tom.snake.get(tom.snake.size()-1).width*SNAKE_SIZE, tom.snake.get(tom.snake.size()-1).height*SNAKE_SIZE, SNAKE_SIZE, SNAKE_SIZE/2);
		    graphics.setColor(WHITE);
		    graphics.fillOval(tom.snake.get(tom.snake.size()-1).width*SNAKE_SIZE+SNAKE_SIZE/3, tom.snake.get(tom.snake.size()-1).height*SNAKE_SIZE+SNAKE_SIZE/4, SNAKE_SIZE/3, SNAKE_SIZE/3);
		    graphics.fillOval(tom.snake.get(tom.snake.size()-1).width*SNAKE_SIZE+SNAKE_SIZE/3*2, tom.snake.get(tom.snake.size()-1).height*SNAKE_SIZE+SNAKE_SIZE/4, SNAKE_SIZE/3, SNAKE_SIZE/3);break;
		case 2: //left
		    graphics.fillRect(tom.snake.get(tom.snake.size()-1).width*SNAKE_SIZE+SNAKE_SIZE/2, tom.snake.get(tom.snake.size()-1).height*SNAKE_SIZE, SNAKE_SIZE/2, SNAKE_SIZE);
		    graphics.setColor(WHITE);
		    graphics.fillOval(tom.snake.get(tom.snake.size()-1).width*SNAKE_SIZE+SNAKE_SIZE/3, tom.snake.get(tom.snake.size()-1).height*SNAKE_SIZE+SNAKE_SIZE/4, SNAKE_SIZE/3, SNAKE_SIZE/3);
		    graphics.fillOval(tom.snake.get(tom.snake.size()-1).width*SNAKE_SIZE+SNAKE_SIZE/3*2, tom.snake.get(tom.snake.size()-1).height*SNAKE_SIZE+SNAKE_SIZE/4, SNAKE_SIZE/3, SNAKE_SIZE/3);break;
		case 3: //right
		    graphics.fillRect(tom.snake.get(tom.snake.size()-1).width*SNAKE_SIZE, tom.snake.get(tom.snake.size()-1).height*SNAKE_SIZE, SNAKE_SIZE/2, SNAKE_SIZE);
		    graphics.setColor(WHITE);
		    graphics.fillOval(tom.snake.get(tom.snake.size()-1).width*SNAKE_SIZE+SNAKE_SIZE/3, tom.snake.get(tom.snake.size()-1).height*SNAKE_SIZE+SNAKE_SIZE/4, SNAKE_SIZE/3, SNAKE_SIZE/3);
		    graphics.fillOval(tom.snake.get(tom.snake.size()-1).width*SNAKE_SIZE+SNAKE_SIZE/3*2, tom.snake.get(tom.snake.size()-1).height*SNAKE_SIZE+SNAKE_SIZE/4, SNAKE_SIZE/3, SNAKE_SIZE/3);break;
		    }    
	    
	    graphics.setColor(RED);
	    graphics.fillOval(apple.x*SNAKE_SIZE-1, apple.y*SNAKE_SIZE-1, SNAKE_SIZE+2, SNAKE_SIZE+2);
	    graphics.setColor(BLACK);
	    graphics.setStroke(new BasicStroke(2));
	    graphics.drawLine(apple.x*SNAKE_SIZE+SNAKE_SIZE/2, apple.y*SNAKE_SIZE+SNAKE_SIZE/4, apple.x*SNAKE_SIZE+SNAKE_SIZE/3, apple.y*SNAKE_SIZE-3);
	    
	    if (!game) {
	    	graphics.setColor(DIMSCREEN);
	    	graphics.fillRect(0, 0, 607, 607);
	    	graphics.setColor(RED);
	    	graphics.setFont(new Font("TimesRoman", Font.BOLD, 50));
	    	if(Game.status()) { //Igra je bila zakljucena
	    		graphics.drawString("GAME OVER", 150, 260);
	    		graphics.drawString("CTRL-P to restart", 110, 320);
	    	}
	    	else if(Game.firstTime) { //Zacetni prikaz drugacen kot za navadno pavzo
				graphics.drawString("Arrow keys to move", 80, 260);
		    	graphics.drawString("CTRL-P to start/pause", 55, 320);
	    	}
	    	else 
	    		graphics.drawString("PAUSED", 200, 300);
	    }	 
	}	
}

