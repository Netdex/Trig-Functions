import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.HashMap;

import javax.swing.JPanel;


public class TrigPanel extends JPanel {
	
	private static final int WIDTH = 1600;
	private static final int HEIGHT = 900;
	
	private static double SCALE = 150;
	private static double EPSILON = 0.05;
	
	private static Point TRANSLATION = new Point(0,0);
	private static Point lastPoint;
	
	private static HashMap<String, Function> functions;
	
	public TrigPanel(){
		this.setFocusable(true);
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setBackground(Color.WHITE);
		
		this.addKeyListener(new KeyAdapter(){
			@Override
			public void keyPressed(KeyEvent event){
				int keycode = event.getKeyCode();
				if(keycode == KeyEvent.VK_RIGHT){
					if(EPSILON > 0.05){
						EPSILON -= 0.05;
						repaint();
					}
				}
				else if(keycode == KeyEvent.VK_LEFT){
					EPSILON += 0.05;
					repaint();
				}
			}
		});
		this.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent event){
				
			}
			@Override
			public void mouseReleased(MouseEvent event){
				lastPoint = null;
			}
		});
		this.addMouseMotionListener(new MouseMotionAdapter(){
			@Override
			public void mouseDragged(MouseEvent event){
				Point p = event.getPoint();
				if(lastPoint != null){
					Point diff = new Point(lastPoint.x - p.x, lastPoint.y - p.y);
					TRANSLATION.translate(diff.x, diff.y);
					repaint();
				}
				lastPoint = p;
			}
		});
		this.addMouseWheelListener(new MouseWheelListener(){
			@Override
			public void mouseWheelMoved(MouseWheelEvent event){
				int notches = event.getWheelRotation();
				if(notches > 0){
					if(SCALE > 10){
						SCALE -= 10;
						repaint();
					}
				}
				else{
					SCALE += 10;
					repaint();
				}
			}
		});
		
		functions = new HashMap<String, Function>();
		
		functions.put("sin", new Function(){
			@Override
			public double f(double x){
				return Math.sin(x);
			}
		});
		functions.put("cos", new Function(){
			@Override
			public double f(double x){
				return Math.cos(x);
			}
		});
		functions.put("tan", new Function(){
			@Override
			public double f(double x){
				return Math.tan(x);
			}
		});
		functions.put("test", new Function(){
			@Override
			public double f(double x){
				return x * x;
			}
		});
		
	}
	
	public void render(Graphics2D g){
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.drawString("Scale: " + SCALE, 10, 15);
		g.drawString("Epsilon: " + EPSILON, 10, 30);
		
		g.translate(-TRANSLATION.x, -TRANSLATION.y);
		g.drawLine(WIDTH / 2, TRANSLATION.y, WIDTH / 2, HEIGHT + TRANSLATION.y);
		g.drawLine(TRANSLATION.x, HEIGHT / 2, WIDTH + TRANSLATION.x, HEIGHT / 2);
		
		g.drawLine(WIDTH / 2 - 3, (int)(HEIGHT / 2 - SCALE), WIDTH / 2 + 3, (int)(HEIGHT / 2 - SCALE));
		g.drawLine(WIDTH / 2 - 3, (int)(HEIGHT / 2 + SCALE), WIDTH / 2 + 3, (int)(HEIGHT / 2 + SCALE));
		g.drawLine((int)(WIDTH / 2 - SCALE), HEIGHT / 2 + 3, (int)(WIDTH / 2 - SCALE), HEIGHT / 2 - 3);
		g.drawLine((int)(WIDTH / 2 + SCALE), HEIGHT / 2 + 3, (int)(WIDTH / 2 + SCALE), HEIGHT / 2 - 3);
		g.drawString("1", WIDTH / 2 - 10, (int)(HEIGHT / 2 - SCALE) + 5);
		g.drawString("-1", WIDTH / 2 - 15, (int)(HEIGHT / 2 + SCALE) + 5);
		g.drawString("1", (int)(WIDTH / 2 + SCALE + 5), HEIGHT / 2 + 13);
		g.drawString("-1", (int)(WIDTH / 2 - SCALE - 13), HEIGHT / 2 + 13);
		
		g.setStroke(new BasicStroke(2));
		Point2D prev = null;
		for(double x = -WIDTH / 2; x < WIDTH / 2; x += EPSILON){
			double op = functions.get("sin").f(x);
			Point2D curr = new Point2D.Double(x * SCALE + WIDTH / 2, -op * SCALE + HEIGHT / 2);
			g.setColor(new Color(Math.min((int)(Math.abs(op * 100)), 255),0,0));
			if(prev != null && Double.isFinite(op))
				g.draw(new Line2D.Double(prev, curr));
			prev = curr;
		}
	}
	
	@Override
	public void paintComponent(Graphics gr){
		super.paintComponent(gr);
		Graphics2D g = (Graphics2D) gr;
		render(g);
	}
}
