import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;

public class Utils {
	
	public static void getHexagon(double x, double y) {
	}
	
	/**
	 * This function draws the cells as the labeled nodes of the graph.
	 * 
	 * @param g
	 * @param x
	 * @param y
	 * @param label
	 * @param scale
	 */
	public static void drawCell(Graphics2D g, NodeV node, Color color) {
		g.setColor(Color.BLACK);
		Polygon hex = node.getPolygon();
		Polygon hex2 = node.getSubPolygon();
		float scale = RikudoPane.CELL_SCALE;
		int label = node.getNode().getLabel();
		int x = node.getX(), y = node.getY();
		drawHexagon(g, hex);
		g.setColor(color);
		drawHexagon(g, hex2);
		//drawHexagon(g, x+2, y+2, scale*.95f);
		g.setColor(Color.BLACK);
		float f = g.getFont().getSize2D();
		String val = String.valueOf(label);
		g.setFont(g.getFont().deriveFont(Font.BOLD, 18f));
		int w = g.getFontMetrics().stringWidth(val);
		int h = g.getFontMetrics().getHeight();
		g.drawString(val, (x + (RikudoPane.CELL_WIDTH*scale-w) + 5), (y + ((RikudoPane.CELL_HEIGHT*scale-h) + 5) + g.getFontMetrics().getAscent()));
		g.setFont(g.getFont().deriveFont(f));
		if (RikudoPane.DEBUG_MODE) {
			g.setColor(Color.RED);
			g.drawRect(x, y, 3, 3);
			g.setColor(Color.ORANGE);
			g.drawRect(x+RikudoPane.CELL_WIDTH/2, y+RikudoPane.CELL_HEIGHT, 4, 4);
			g.setColor(Color.GREEN);
			
			g.drawRect(x+RikudoPane.CELL_WIDTH, y, 4, 4);
		}
	}
	
	private static void drawHexagon(Graphics2D g, Polygon p) {
		g.fillPolygon(p);
	}
    
	/**
	 * This function is only for private use, its sole purpose is to give
	 * primitive to draw hexagons.
	 * 
	 * @param g
	 * @param x
	 * @param y
	 * @param scale
	 */
	public static Polygon getHexagon(int x, int y, float scale) {
		Polygon h = new Polygon();	
		for (int i = 0; i < 6; i++){
			h.addPoint((int) (x + RikudoPane.CELL_WIDTH*scale * (1.+Math.cos(i * 2 * Math.PI / 6+Math.PI/2))),
					  (int) (y + RikudoPane.CELL_HEIGHT*scale * (1.+Math.sin(i * 2 * Math.PI / 6+Math.PI/2))));
		}
		return h;
	}

}
