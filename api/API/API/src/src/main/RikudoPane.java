package src.main;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.event.MouseInputListener;

public class RikudoPane extends JPanel implements ActionListener, MouseInputListener {
	
	/*** CONSTANTS ***/
	//frame constants
	private JFrame frame;
	private Visualizer visualizer;
	private final int WIDTH = 1200;
	private final int HEIGHT = 800;
	public static boolean DEBUG_MODE = false;
	public static int MODE = 0; //0 : PLAY MODE / 1 : CREATOR MODE
	private Rectangle2D button_load;
	private Rectangle2D button_generate;
	private Rectangle2D button_check;
	private Rectangle2D button_;
	private boolean load_clicked = false;
	private boolean generate_clicked = false;
	private boolean check_clicked = false;
	private boolean check_solution = false;
	
	//game constants
	public static final float CELL_SCALE = .5f; //defines the scale of each node by default is .5f
	public static final int CELL_WIDTH = 100;
	public static final int CELL_HEIGHT = 100;
	
	//fields
	private GraphV graph;
	private Timer timer;
	
	//pane related
	private AffineTransform transform;
	private BufferedImage pane;
	private GraphVOff hexagraph;
	
	public RikudoPane(JFrame frame, Visualizer visualizer, GraphV graph) { //should be Rikudo rikudo
		super();
		this.frame = frame;
		this.visualizer = visualizer;
		this.graph = graph;
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setBackground(Color.WHITE);
		this.button_load = new Rectangle(WIDTH-150, 30, 100, 30);
		this.button_generate = new Rectangle(WIDTH-150, 70, 100, 30);
		this.button_check = new Rectangle(WIDTH-180, 30, 150, 30);
		
		this.pane = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		Graph hexagraph = Utils.getHexaGraph(7);
		this.hexagraph = new GraphVOff(hexagraph);
		
		this.timer = new Timer(50, this);
		timer.start();
	}
	
	public void loadCreator(Graph graph, boolean fill) {
		this.hexagraph = new GraphVOff(graph);	
		if (fill) {
			for (NodeV n : this.hexagraph.getNodesV()) {
				((NodeVOff) n).toggleOn();
			}
		}
	}
	
	public void loadGraph(Graph g) {
		this.graph = new GraphV(g);
	}
	
	public void paint(Graphics gg) {
		super.paint(gg);
		/*Graphics2D g = (Graphics2D) gg;
		*/
		
		Graphics2D g = pane.createGraphics();

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		g.setFont(g.getFont().deriveFont(15f));
		
		if (MODE == 0) {//play mode
			g.setBackground(Color.WHITE);
			g.clearRect(0, 0, WIDTH, HEIGHT);
			
			AffineTransform prev_trans = g.getTransform();

			g.scale(visualizer.zoom[MODE], visualizer.zoom[MODE]);
			g.translate(visualizer.global_x[MODE]+440, visualizer.global_y[MODE]+150);
			transform = g.getTransform();
			
			for (NodeV node : graph.getNodesV()) {
				node.show(g);
			}
			
			for (NodeV node : graph.getNodesV()) {
				node.drawDiamond(g);
			}
			
			g.setTransform(prev_trans);
			
			//gui
			//check
			g.setColor(Color.DARK_GRAY);
			g.fill(button_check);
			if (!check_clicked) {
				g.setColor(Color.CYAN);
				g.drawString("Is solution : " + check_solution, WIDTH-170, 80);
				g.setColor(Color.WHITE);
			} else g.setColor(Color.RED);
			g.drawString("Check solution", WIDTH-165, 50);

			g.setColor(Color.BLACK);
			g.drawString("PLAY MODE", 15, 20);
			
		} else if (MODE == 1) {//creator mode
			g.setBackground(Color.GRAY);
			g.clearRect(0, 0, WIDTH, HEIGHT);
			
			AffineTransform prev_trans = g.getTransform();
			
			//map
			g.scale(visualizer.zoom[MODE], visualizer.zoom[MODE]);
			g.translate(visualizer.global_x[MODE]+440, visualizer.global_y[MODE]+150);
			transform = g.getTransform();
			
			for (NodeV node : hexagraph.getNodesV()) {
				node.show(g);
			}
			
			for (NodeV node : hexagraph.getNodesV()) {
				node.drawDiamond(g);
			}
			
			//gui
			g.setTransform(prev_trans);
			//title
			g.setFont(g.getFont().deriveFont(20f));
			g.setColor(Color.CYAN);
			g.drawString("CREATOR MODE", 15, 20);
			
			//menu
			//load
			g.setColor(Color.DARK_GRAY);
			g.setFont(g.getFont().deriveFont(14f));
			g.fill(button_load);
			if (!load_clicked)
				g.setColor(Color.WHITE);
			else g.setColor(Color.RED);
			g.drawString("Load", WIDTH-120, 50);
			
			//generate
			g.setColor(Color.DARK_GRAY);
			g.fill(button_generate);
			if (!generate_clicked)
				g.setColor(Color.WHITE);
			else g.setColor(Color.RED);
			g.drawString("Generate", WIDTH-135, 90);
			
			//help
			
			g.setFont(g.getFont().deriveFont(11f));
			
			Composite prev = g.getComposite();
			AlphaComposite alcom = AlphaComposite.getInstance(
	                AlphaComposite.SRC_OVER, .5f);
	        g.setComposite(alcom);
	        
			g.setColor(Color.WHITE);
			g.fillRect(WIDTH-230, HEIGHT-280, 230, 280);
			g.setColor(Color.BLACK);
			g.drawString("- Use arrows to move around", WIDTH - 220, HEIGHT-250);
			g.drawString("- Left click to add a cell", WIDTH - 220, HEIGHT-230);
			g.drawString("- Right click to delete a cell", WIDTH - 220, HEIGHT-210);
			g.drawString("- Left+Shift click to set a cell fixed", WIDTH - 220, HEIGHT-190);
			g.drawString("- Left/Left+Ctrl click to increment", WIDTH - 220, HEIGHT-170);
			g.drawString("/decrement value of a fixed cell", WIDTH - 220, HEIGHT-150);
			g.drawString("- A/Z to zoom in/out", WIDTH - 220, HEIGHT-130);
			g.drawString("- Left/Right+Alt click to set", WIDTH - 220, HEIGHT-110);
			g.drawString("source/bottom", WIDTH - 220, HEIGHT-90);
			g.drawString("Check the available solution", WIDTH - 220, HEIGHT-50);
			g.drawString("Load and check in play mode", WIDTH - 220, HEIGHT-30);
			g.setComposite(prev);
		}
		
		gg.drawImage(pane, 0, 0, null);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println(e.getX() + " " + e.getY());
		if (MODE == 0) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				for (NodeV node : graph.getNodesV()) {
					if (transform != null) {
						try {
							Point2D p = transform.inverseTransform(e.getPoint(), null);
							if (!e.isControlDown()) {
								if (node.isHovered(p)) {
									if (!node.getNode().isFixed())
										node.getNode().setLabel((node.getNode().getLabel()+1)%graph.getNodesV().length);
								}
							} else if (node.isHovered(p)) node.getNode().setLabel(Math.max(0, node.getNode().getLabel()-1));
						} catch (NoninvertibleTransformException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		} else if (MODE == 1) {
			//map draw
			for (NodeV n : hexagraph.getNodesV()) {
				if (transform != null) {
					try {
						Point2D p = transform.inverseTransform(e.getPoint(), null);
						NodeVOff node = (NodeVOff) n;
						if (node.isHovered(p)) {
							if (e.getButton() == MouseEvent.BUTTON1) { //left click
								if (e.isShiftDown()) { //set a cell fixed / increment it
									if (!node.exists()) { //toggleon and increment
										node.toggleOn();
									}
									if (!hexagraph.isSource(node) && !hexagraph.isBottom(node)) {
										node.getNode().setLabel(NodeVOff.COUNTER);
										NodeVOff.COUNTER = (NodeVOff.COUNTER + 1) % hexagraph.getNodesV().length;
										if (hexagraph.hasBottom()) hexagraph.getBottom().getNode().setLabel(Math.max(hexagraph.getBottom().getNode().getLabel()+1, NodeVOff.COUNTER));
										if (node.getNode().isFixed()) {
											node.getNode().setIsFixed(false);
											node.getNode().setLabel(-1);
										} else {
											node.getNode().setIsFixed(true);
											node.forceColor(null);
										}
									}
								} else if (e.isAltDown()) { //set source
									if (!node.exists()) node.toggleOn();
									if (!hexagraph.isSource(node) && !hexagraph.hasSource()) {
										hexagraph.setSource(node);
										node.forceColor(Color.RED);
									} else if (hexagraph.isSource(node)) {
										node.forceColor(null);
										hexagraph.setSource(null);
									}
								} else if (e.isControlDown()) { //decrement a fixed cell
									if (node.exists() && node.getNode().isFixed() && !hexagraph.isSource(node) && !hexagraph.isBottom(node)) { //decrement
										node.getNode().setLabel(Math.max(0, node.getNode().getLabel()-1));
									}
								} else {
									if (node.exists() && node.getNode().isFixed()) {
										if (!hexagraph.isSource(node) && !hexagraph.isBottom(node)) {
											node.getNode().setLabel((node.getNode().getLabel()+1)%hexagraph.getNodesV().length);
										}
									} else {
										node.toggleOn();
										if (hexagraph.hasBottom()) hexagraph.getBottom().getNode().setLabel(hexagraph.getBottom().getNode().getLabel()+1);
									}
								}
							}
							
							if (e.getButton() == MouseEvent.BUTTON3) { //right click
								if (e.isAltDown()) { //set bottom
									if (!node.exists()) node.toggleOn();
									if (!hexagraph.isBottom(node) && !hexagraph.hasBottom()) {
										hexagraph.setBottom(node);
										node.forceColor(Color.GREEN);
									} else if (hexagraph.isBottom(node)) {
										node.forceColor(null);
										hexagraph.setBottom(null);
									}
								} else if (node.exists()) {
									NodeVOff.COUNTER = Math.max(0, NodeVOff.COUNTER-1);
									node.toggleOff();
									if (hexagraph.isBottom(node)) {
										node.forceColor(null);
										hexagraph.setBottom(null);
									} else if (hexagraph.isSource(node)) {
										node.forceColor(null);
										hexagraph.setSource(null);
									} else if (node.getNode().isFixed()) node.getNode().setIsFixed(false);
								}
							}
						}
					} catch (NoninvertibleTransformException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}
	
	/*
	 * Diamonds mechanics
	 */
	private int drag = 0;
	private NodeV node1, node2;

	@Override
	public void mousePressed(MouseEvent e) {
		if (RikudoPane.MODE == 1) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				try {
					Point2D p = transform.inverseTransform(e.getPoint(), null);
					drag = 1;
					for (NodeV n : this.hexagraph.getNodesV()) {
						if (((NodeVOff) n).exists() && n.isHovered(p)) {
							System.out.println("Dragged, found first node");
							node1=n;
						}
					}
					if (button_load.contains(e.getPoint())) {
						load_clicked = true;
						Graph built_graph = hexagraph.export();
						if (built_graph != null) {
							System.out.println(Visualizer.prefix + "Loading generated map.");
							this.loadGraph(built_graph);
						}
					}
					if (button_generate.contains(e.getPoint())) {
						generate_clicked = true;
						GeneratorDialog gd = new GeneratorDialog(frame, this);
						Graph generated_graph = gd.getGraph();
						if (generated_graph != null) {
							System.out.println(Visualizer.prefix + "Generating map with at least one solution.");
							this.loadCreator(generated_graph, true);
						}
						generate_clicked = false;
					}
				} catch (NoninvertibleTransformException e1) {
					e1.printStackTrace();
				}
			}
		} else if (MODE == 0) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				if (button_check.contains(e.getPoint())) {
					check_clicked = true;
					check_solution = this.graph.getGraph().verify();
					repaint();
				}
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (MODE == 1) {
			if (drag == 1) {
				drag = 2; //second condition is validated
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (drag > 0) {
			//check if 2 cells have been in the selection
			if (drag == 2) {
				if (node1 != null) {
					try {
						Point2D p = transform.inverseTransform(e.getPoint(), null);
						for (NodeV n : this.hexagraph.getNodesV()) {
							if (((NodeVOff) n).exists()) {
								if (n.isHovered(p) && n != node1) { //we found the second node in the dragging
									if (Node.areNeighbors(node1.getNode(), n.getNode())) {
										node2=n;
										if (Node.areDiamonded(node1.getNode(), node2.getNode())) {
											node2.getNode().removeDiamond(node2.getNode().getNeighborDirection(node1.getNode()));
										} else {
											node2.getNode().setDiamond(node2.getNode().getNeighborDirection(node1.getNode()));
										}
									}
								}
							}
						}
					} catch (NoninvertibleTransformException e1) {
						e1.printStackTrace();
					}
				}
			}
			drag = 0;
		}
		if (RikudoPane.MODE == 1 && e.getButton() == MouseEvent.BUTTON1 && button_load.contains(e.getPoint()) && load_clicked) load_clicked = false;
		if (RikudoPane.MODE == 0 && e.getButton() == MouseEvent.BUTTON1 && button_check.contains(e.getPoint()) && check_clicked) check_clicked = false;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		GraphV g = null;
		if (MODE == 0) g = this.graph;
		else if (MODE == 1) g = this.hexagraph;
		Point2D p;
		try {
			p = transform.inverseTransform(e.getPoint(), null);
			for (NodeV node : g.getNodesV()) {
				if (g.getGraph().getSource() != node.getNode() && g.getGraph().getDestination() != node.getNode()
						&& !node.getNode().isFixed()) {
					if (node.isHovered(p))
						node.forceColor(Color.PINK);
					else node.forceColor(null);
				}
			}
		} catch (NoninvertibleTransformException e1) {
			e1.printStackTrace();
		}
	}
	
}
