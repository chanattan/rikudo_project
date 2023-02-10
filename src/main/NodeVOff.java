package src.main;
import java.awt.Color;
import java.awt.Graphics2D;

public class NodeVOff extends NodeV {
	
	private boolean exist = false;
	public static int COUNTER = 0; //counter for nodes id
	
	public NodeVOff(Node n) {
		super(n);

		this.pol = Utils.getHexagon(x, y, RikudoPane.CELL_SCALE);
		this.subpol = Utils.getHexagon(x+2, y+2, RikudoPane.CELL_SCALE*.95f);
	}
	
	public boolean exists() {
		return exist;
	}

	/**
	 * Toggle on activates this current node to be shown on creator mode as existing,
	 * the node will be added into the exported graph.
	 */
	public void toggleOn() {
		exist = true;
		//this.node.id = NodeVOff.COUNTER;
	}
	
	/**
	 * Toggle off removes the node and will not be in the exported graph.
	 */
	public void toggleOff() {
		exist = false;
		this.node.setLabel(-1);
	}
	
	public void forceColor(Color c) {
		if (c == null) forceColor = false;
		else {
			this.forceColor = true;
			this.c = c;
		}
	}
	
	private Color c;
	@Override
	public void show(Graphics2D g) {
		if (pol != null || subpol != null) {
			//node drawing
			if (!forceColor) {
				if (this.node.isFixed())
					c = Color.ORANGE;
				else if (exist)
					c = Color.WHITE;
				else c = Color.GRAY;
			}
			Utils.drawCell(g, this, c);
			super.drawDiamond(g);
		}
	}

}
