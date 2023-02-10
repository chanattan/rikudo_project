package src.main;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Point2D;

/*
 * NodeV stands for NodeVisualizer, used as a handler for Node to be shown with the visualizer.
 */
public class NodeV {
	
	//draw related
	protected Polygon pol;

	protected Polygon subpol;
	
	//graph related
	protected int x, y;
	protected Node node;
	protected boolean forceColor = false;
	
	public NodeV(Node n, int x, int y) {
		this.node = n;
		this.x = x;
		this.y = y;
	}
	
	public NodeV(Node n) {
		this(n, 0, 0);
	}
	
	public Node getNode() {
		return node;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public Polygon getPolygon() {
		return pol;
	}
	
	public Polygon getSubPolygon() {
		return subpol;
	}
		
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
		this.pol = Utils.getHexagon(x, y, RikudoPane.CELL_SCALE);
		this.subpol = Utils.getHexagon(x+2, y+2, RikudoPane.CELL_SCALE*.95f);
	}
	
	public boolean isHovered(Point2D p) {
		if (pol == null) {
			System.err.println(Visualizer.prefix + "Warning: polygon is null for node of id " + node.id + " (" + node.getLabel() + ")");
			return false;
		}
		return pol.contains(p);
	}
	
	/**
	 * The color of a node is defined according to its state, its color
	 * should not be set manually, however for the source and bottom we can
	 * set a specific color.
	 * 
	 * @param c : color to be set by force, if null then there is no force color
	 */
	public void forceColor(Color c) {
		if (c == null) {
			this.forceColor = false;
			return;
		}
		this.forceColor = true;
		this.c = c;
	}
	
	private Color c;
	public void show(Graphics2D g) {
		if (pol != null || subpol != null) {
			//node drawing
			if (!forceColor) {
				if (this.node.isFixed())
					c = Color.ORANGE;
				else c = Color.WHITE;
			}
			Utils.drawCell(g, this, c);
		}
	}
	
	public void drawDiamond(Graphics2D g) {
		//diamond drawing
		for (int i = 0; i < node.getDiamonds().length; i++) {
			if (node.getDiamonds()[i]) {
				Node.DIR dir = Node.getDirection(i);

//				g.drawRect((int) pol.getBounds2D().getX(), (int) pol.getBounds2D().getY(), (int) pol.getBounds2D().getWidth(), (int) pol.getBounds2D().getHeight());
				Utils.drawDiamond(g, this, dir, c);
			}
		}
	}
}

