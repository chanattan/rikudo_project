package src.main;
public class Node {

	public enum DIR{
		NE, NORTH_EAST,
		E, EAST,
		SE, SOUTH_EAST,
		SW, SOUTH_WEST,
		W, WEST,
		NW, NORTH_WEST,
		ERR
	}
	
	private int label; 				// Number of the node
	private final boolean isFixed;	// Constraint for the given problem 

	private Node neigh[];		// Neighboring nodes (a size 6 array with other nodes)
	private DIR diamonds[];		// The diamonds (a size 2 array with directions of diamonds)	


	public Node(){
		this.label = -1;
		this.isFixed = false;
		this.neigh = new Node[] {null,null,null,null,null,null};
		this.diamonds = new DIR[] {null, null};
	}

	public Node(int label, boolean isFixed){
		this.label = label;
		this.isFixed = isFixed;
	}

	public Node(int label, boolean isFixed, Node neigh[], DIR diamonds[]){
		this.label = label;
		this.isFixed = isFixed;
		this.neigh = neigh;
		this.diamonds = diamonds;
	}


	// Link Node 1 and 2 on a direction (node n1's direction toward n2) (ex left : n1, right : n2, d = EAST)
	public static void linkNodes(Node n1, Node n2, DIR d){
		n1.setNeighbor(n2, d);
		n2.setNeighbor(n1, oppositeDirection(d));
	}

	public static DIR oppositeDirection(DIR d){
		switch(d){
			case NE:
				return DIR.SW; 
			case NORTH_EAST:
				return DIR.SOUTH_WEST;
			case E: 
				return DIR.W;
			case EAST:
				return DIR.WEST;
			case SE: 
				return DIR.NW;
			case SOUTH_EAST:
				return DIR.NORTH_WEST;
			case SW: 
				return DIR.NE;
			case SOUTH_WEST:
				return DIR.NORTH_EAST;
			case W: 
				return DIR.E;
			case WEST:
				return DIR.EAST;
			case NW: 
				return DIR.SE;
			case NORTH_WEST:
				return DIR.SOUTH_EAST;
			default:
				System.err.println("Unknown direction");
				return DIR.ERR;
		}
	}


	// Getters

	public Node[] getNeighbors(){
		return neigh;
	}

	public Node getNeighbor(DIR d){
		int i = dirToIndex(d);
		if(i == -1){
			return null;
		}
		return this.neigh[i];
	}

	public DIR[] getDiamonds(){
		return this.diamonds;
	}

	public int getLabel(){
		return this.label;
	}

	public boolean getIsFixed(){
		return this.isFixed;
	}

	// Setters

	public void setNeighbor(Node neigh, DIR d){
		int i = dirToIndex(d);
		this.neigh[i] = neigh;
	}

	public void setLabel(int l){
		this.label = l;
	}

	// ********************* PRIVATE FUNCTIONS *********************

	private static int dirToIndex(DIR d){
		switch(d){
			case NE: case NORTH_EAST:
				return 0;
			case E: case EAST:
				return 1;
			case SE: case SOUTH_EAST:
				return 2;
			case SW: case SOUTH_WEST:
				return 3;
			case W: case WEST:
				return 4;
			case NW: case NORTH_WEST:
				return 5;
			default:
				System.err.println("Unknown direction");
				return -1;
		}
	}

}
