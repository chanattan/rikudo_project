import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

import javax.swing.JPanel;
import javax.swing.Timer;

public class RikudoPane extends JPanel implements ActionListener, MouseListener {
	
	/*** CONSTANTS ***/
	//frame constants
	private Visualizer visualizer;
	private final int WIDTH = 500;
	private final int HEIGHT = 500;
	public static boolean DEBUG_MODE = false;
	//game constants
	public static final float CELL_SCALE = .5f; //defines the scale of each node
	public static final int CELL_WIDTH = 100;
	public static final int CELL_HEIGHT = 100;
	
	//fields
	private GraphV graph;
	private Timer timer;
	
	private AffineTransform transform;
	
	
	public RikudoPane(Visualizer visualizer, GraphV graph) { //should be Rikudo rikudo
		super();
		this.visualizer = visualizer;
		this.graph = graph;
		this.addMouseListener(this);
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setBackground(Color.WHITE);
		this.timer = new Timer(50, this);
		timer.start();
	}
	
	public void paint(Graphics gg) {
		super.paint(gg);
		Graphics2D g = (Graphics2D) gg;
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		g.setFont(g.getFont().deriveFont(15f));
		g.setColor(Color.BLACK);
		g.drawString("RIKUDO GAMU VERSSION TWO", 15, 20);
		g.scale(visualizer.zoom, visualizer.zoom);
		g.translate(visualizer.global_x+100, visualizer.global_y+100);
		
		transform = g.getTransform();
		for (NodeV node : graph.getNodesV()) {
			node.show(g);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println(e.getX() + " " + e.getY());
		for (NodeV node : graph.getNodesV()) {
			if (transform != null) {
				try {
					Point2D p = transform.inverseTransform(e.getPoint(), null);
					if (node.isHovered(p)) {
						if (!node.getNode().isFixed())
							node.getNode().setLabel((node.getNode().getLabel()+1)%graph.getNodesV().length);
					}
				} catch (NoninvertibleTransformException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
	
}
