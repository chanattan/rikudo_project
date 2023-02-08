import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Point2D;

/*
 * NodeV stands for NodeVisualizer, used as a handler for Node to be shown with the visualizer.
 */
public class NodeV {
	
	//draw related
	private Polygon pol, subpol;
	
	//graph related
	private int x, y;
	private Node node;
	
	public NodeV(Node n, int x, int y) {
		this.node = n;
		this.x = x;
		this.y = y;
		this.pol = Utils.getHexagon(x, y, RikudoPane.CELL_SCALE);
		this.subpol = Utils.getHexagon(x+2, y+2, RikudoPane.CELL_SCALE*.95f);
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
		return pol.contains(p);
	}
	
	Color c;
	public void show(Graphics2D g) {
		c = Color.WHITE;
		if (node.isFixed()) c = Color.ORANGE;
		Utils.drawCell(g, this, c);
	}
}

