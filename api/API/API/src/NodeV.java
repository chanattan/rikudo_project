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

	private Node neigh[];		// Neighboring nodes (a size 6 array with other nodes)
	private Node.DIR diamonds[];		// The diamonds (a size 2 array with directions of diamonds)	
	
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
	
	public NodeVIterator getNeighborIterator() {
		return new NodeVIterator(this);
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
	
	public void show(Graphics2D g) {
		Utils.drawCell(g, this);
	}
	
	private class NodeVIterator {
		private NodeV nodev;
		private int counter = 0;
		public NodeVIterator(NodeV n) {
			this.nodev = n;
		}
		
		public boolean hasNext() {
			return counter < Node.DIR.values().length-1;
		}
		
		public void reset() {
			this.counter = 0;
		}
		
		public NodeV getNext() {
			NodeV n = new NodeV(nodev.node.getNeighbors()[counter]);
			counter++;
			return n;
		}
	}
}

