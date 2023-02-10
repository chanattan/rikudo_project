import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.ArrayList;

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
		int x = node.getX();
		int y = node.getY();
		drawHexagon(g, hex);
		g.setColor(color);
		drawHexagon(g, hex2);
		g.setColor(Color.BLACK);
		float f = g.getFont().getSize2D();
		String val = String.valueOf(label);
		if (label != -1) {
			g.setFont(g.getFont().deriveFont(Font.BOLD, 18f));
			int w = g.getFontMetrics().stringWidth(val);
			int h = g.getFontMetrics().getHeight();
			g.drawString(val, (x + (RikudoPane.CELL_WIDTH*scale-w)/2 + 22), (y + ((RikudoPane.CELL_HEIGHT*scale-h) + 7) + g.getFontMetrics().getAscent()));
			g.setFont(g.getFont().deriveFont(f));
		}
		if (RikudoPane.DEBUG_MODE) {
			g.setColor(Color.RED);
			g.drawRect(x, y, 3, 3);
			g.setColor(Color.ORANGE);
			g.drawRect(x+RikudoPane.CELL_WIDTH/2, y+RikudoPane.CELL_HEIGHT, 4, 4);
			
			g.setColor(Color.RED);
			
			g.drawString("id " + node.getNode().id, x + 2, y + 2);
			
			g.setColor(Color.GREEN);
			g.drawRect(x+RikudoPane.CELL_WIDTH, y, 4, 4);
			g.setColor(Color.RED);
			g.draw(node.pol.getBounds2D());
		}
	}
	
	/**
	 * Generates an hexagonal graph with a random start (source) and end (bottom) points.
	 * 
	 * @param n : the size of the middle row, n must be greater than 4.
	 */
	public static Graph getHexaGraph(int n){
		if(n<4){
			System.out.println("Erreur dans le passage de l'argument de testHexa");
			return null;
		}
		//tmp reset counter for ids
		Node.COUNTER=0;
		
		ArrayList<Node> nodes = new ArrayList<Node>();
		int len = n;
		for(int i=4;i<n;i++){
			len+=2*i;
		}
		for(int i = 0; i < len; i++){
			nodes.add(new Node());
		}
		// First line (size 4)
		nodes.get(0).setNeighbor(nodes.get(1),Node.DIR.EAST);
		nodes.get(0).setNeighbor(nodes.get(4),Node.DIR.SOUTH_WEST);
		nodes.get(0).setNeighbor(nodes.get(5),Node.DIR.SOUTH_EAST);


		nodes.get(1).setNeighbor(nodes.get(2),Node.DIR.EAST);
		nodes.get(1).setNeighbor(nodes.get(0),Node.DIR.WEST);
		nodes.get(1).setNeighbor(nodes.get(5),Node.DIR.SOUTH_WEST);
		nodes.get(1).setNeighbor(nodes.get(6),Node.DIR.SOUTH_EAST);


		nodes.get(2).setNeighbor(nodes.get(3),Node.DIR.EAST);
		nodes.get(2).setNeighbor(nodes.get(1),Node.DIR.WEST);
		nodes.get(2).setNeighbor(nodes.get(6),Node.DIR.SOUTH_WEST);
		nodes.get(2).setNeighbor(nodes.get(7),Node.DIR.SOUTH_EAST);

		nodes.get(3).setNeighbor(nodes.get(2),Node.DIR.WEST);
		nodes.get(3).setNeighbor(nodes.get(7),Node.DIR.SOUTH_WEST);
		nodes.get(3).setNeighbor(nodes.get(8),Node.DIR.SOUTH_EAST);

		//Northern lines
		int fst_pos=0;
		for(int num=5;num<n;num++){
			fst_pos += num-1;// nb of the first node of the line
			//First node
			nodes.get(fst_pos).setNeighbor(nodes.get(fst_pos-num+1),Node.DIR.NORTH_EAST);
			nodes.get(fst_pos).setNeighbor(nodes.get(fst_pos+1),Node.DIR.EAST);
			nodes.get(fst_pos).setNeighbor(nodes.get(fst_pos+num+1),Node.DIR.SOUTH_EAST);
			nodes.get(fst_pos).setNeighbor(nodes.get(fst_pos+num),Node.DIR.SOUTH_WEST);
			//Middle nodes
			for(int j=1;j<num-1;j++){
				nodes.get(fst_pos+j).setNeighbor(nodes.get((fst_pos+j)-num+1),Node.DIR.NORTH_EAST);
				nodes.get(fst_pos+j).setNeighbor(nodes.get((fst_pos+j)+1),Node.DIR.EAST);
				nodes.get(fst_pos+j).setNeighbor(nodes.get((fst_pos+j)+num+1),Node.DIR.SOUTH_EAST);
				nodes.get(fst_pos+j).setNeighbor(nodes.get((fst_pos+j)+num),Node.DIR.SOUTH_WEST);
				nodes.get(fst_pos+j).setNeighbor(nodes.get((fst_pos+j)-1),Node.DIR.WEST);
				nodes.get(fst_pos+j).setNeighbor(nodes.get((fst_pos+j)-num),Node.DIR.NORTH_WEST);
			}
			//Last node
			nodes.get(fst_pos+num-1).setNeighbor(nodes.get((fst_pos+num-1)+num+1),Node.DIR.SOUTH_EAST);
			nodes.get(fst_pos+num-1).setNeighbor(nodes.get((fst_pos+num-1)+num),Node.DIR.SOUTH_WEST);
			nodes.get(fst_pos+num-1).setNeighbor(nodes.get((fst_pos+num-1)-1),Node.DIR.WEST);
			nodes.get(fst_pos+num-1).setNeighbor(nodes.get((fst_pos+num-1)-num),Node.DIR.NORTH_WEST);
		}

		//Middle line (size n)
		fst_pos += n-1;
		//First node
		nodes.get(fst_pos).setNeighbor(nodes.get(fst_pos-n+1),Node.DIR.NORTH_EAST);
		nodes.get(fst_pos).setNeighbor(nodes.get(fst_pos+1),Node.DIR.EAST);
		nodes.get(fst_pos).setNeighbor(nodes.get(fst_pos+n),Node.DIR.SOUTH_EAST);
		//Middle nodes
		for(int j=1;j<n-1;j++){
			nodes.get(fst_pos+j).setNeighbor(nodes.get((fst_pos+j)-n+1),Node.DIR.NORTH_EAST);
			nodes.get(fst_pos+j).setNeighbor(nodes.get((fst_pos+j)+1),Node.DIR.EAST);
			nodes.get(fst_pos+j).setNeighbor(nodes.get((fst_pos+j)+n),Node.DIR.SOUTH_EAST);
			nodes.get(fst_pos+j).setNeighbor(nodes.get((fst_pos+j)+n-1),Node.DIR.SOUTH_WEST);
			nodes.get(fst_pos+j).setNeighbor(nodes.get((fst_pos+j)-1),Node.DIR.WEST);
			nodes.get(fst_pos+j).setNeighbor(nodes.get((fst_pos+j)-n),Node.DIR.NORTH_WEST);
		}
		//Last node
		nodes.get(fst_pos+n-1).setNeighbor(nodes.get((fst_pos+n-1)+n-1),Node.DIR.SOUTH_WEST);
		nodes.get(fst_pos+n-1).setNeighbor(nodes.get((fst_pos+n-1)-1),Node.DIR.WEST);
		nodes.get(fst_pos+n-1).setNeighbor(nodes.get((fst_pos+n-1)-n),Node.DIR.NORTH_WEST);

		//Southern lines
		for(int num=n-1;num>4;num--){
			fst_pos += num+1;// nb of the first node of the line
			//First node
			nodes.get(fst_pos).setNeighbor(nodes.get(fst_pos-num-1),Node.DIR.NORTH_WEST);
			nodes.get(fst_pos).setNeighbor(nodes.get(fst_pos-num),Node.DIR.NORTH_EAST);
			nodes.get(fst_pos).setNeighbor(nodes.get(fst_pos+1),Node.DIR.EAST);
			nodes.get(fst_pos).setNeighbor(nodes.get(fst_pos+num),Node.DIR.SOUTH_EAST);
			//Middle nodes
			for(int j=1;j<num-1;j++){
				nodes.get(fst_pos+j).setNeighbor(nodes.get((fst_pos+j)-num-1),Node.DIR.NORTH_WEST);
				nodes.get(fst_pos+j).setNeighbor(nodes.get((fst_pos+j)-num),Node.DIR.NORTH_EAST);
				nodes.get(fst_pos+j).setNeighbor(nodes.get((fst_pos+j)+1),Node.DIR.EAST);
				nodes.get(fst_pos+j).setNeighbor(nodes.get((fst_pos+j)+num),Node.DIR.SOUTH_EAST);
				nodes.get(fst_pos+j).setNeighbor(nodes.get((fst_pos+j)+num-1),Node.DIR.SOUTH_WEST);
				nodes.get(fst_pos+j).setNeighbor(nodes.get((fst_pos+j)-1),Node.DIR.WEST);
			}
			//Last node
			nodes.get(fst_pos+num-1).setNeighbor(nodes.get((fst_pos+num-1)-num-1),Node.DIR.NORTH_WEST);
			nodes.get(fst_pos+num-1).setNeighbor(nodes.get((fst_pos+num-1)-num),Node.DIR.NORTH_EAST);
			nodes.get(fst_pos+num-1).setNeighbor(nodes.get((fst_pos+num-1)-1),Node.DIR.WEST);
			nodes.get(fst_pos+num-1).setNeighbor(nodes.get((fst_pos+num-1)+num-1),Node.DIR.SOUTH_WEST);

		}
		//Last line (size 4)
		nodes.get(len-4).setNeighbor(nodes.get(len-9),Node.DIR.NORTH_WEST);
		nodes.get(len-4).setNeighbor(nodes.get(len-8),Node.DIR.NORTH_EAST);
		nodes.get(len-4).setNeighbor(nodes.get(len-3),Node.DIR.EAST);

		nodes.get(len-3).setNeighbor(nodes.get(len-8),Node.DIR.NORTH_WEST);
		nodes.get(len-3).setNeighbor(nodes.get(len-7),Node.DIR.NORTH_EAST);
		nodes.get(len-3).setNeighbor(nodes.get(len-2),Node.DIR.EAST);
		nodes.get(len-3).setNeighbor(nodes.get(len-4),Node.DIR.WEST);

		nodes.get(len-2).setNeighbor(nodes.get(len-7),Node.DIR.NORTH_WEST);
		nodes.get(len-2).setNeighbor(nodes.get(len-6),Node.DIR.NORTH_EAST);
		nodes.get(len-2).setNeighbor(nodes.get(len-1),Node.DIR.EAST);
		nodes.get(len-2).setNeighbor(nodes.get(len-3),Node.DIR.WEST);

		nodes.get(len-1).setNeighbor(nodes.get(len-2),Node.DIR.WEST);
		nodes.get(len-1).setNeighbor(nodes.get(len-6),Node.DIR.NORTH_WEST);
		nodes.get(len-1).setNeighbor(nodes.get(len-5),Node.DIR.NORTH_EAST);
		/*
		int s = 0;
		int t = len-1;
		nodes.get(s).setLabel(1);
		nodes.get(s).setIsFixed(true);
		nodes.get(t).setLabel(len);
		nodes.get(t).setIsFixed(true);*/

		Graph g = new Graph(nodes);
		return g;
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
