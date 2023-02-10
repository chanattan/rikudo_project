package src.main;
public class Node {

	private static int COUNTER = 0;
	public final int id;

	public enum DIR{
		NE, NORTH_EAST,
		E, EAST,
		SE, SOUTH_EAST,
		SW, SOUTH_WEST,
		W, WEST,
		NW, NORTH_WEST,
	}
	
	private int label; 				// Number of the node
	private boolean isFixed;	// Constraint for the given problem 

	private Node neigh[];		// Neighboring nodes (a size 6 array with other nodes)
	private boolean diamonds[];		// The diamonds (a size 6 array with directions of diamonds)	


	public Node(){
		this(-1, false);
	}

	public Node(int label, boolean isFixed){
		this(label, isFixed, new Node[] {null,null,null,null,null,null}, new boolean[] {false, false, false, false, false, false});
	}

	public Node(int label, boolean isFixed, Node neigh[], boolean diamonds[]){
		this.id = COUNTER;
		COUNTER++;
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

	public static boolean areNeighbors(Node n1, Node n2){
		for(Node n : n1.getNeighbors()){
			if(n == n2){
				return true;
			}
		}
		return false;
	}

	public static boolean areDiamonded(Node n1, Node n2){
		boolean[] n1Diamonds = n1.getDiamonds();
		for(int i = 0; i < n1Diamonds.length; i++){
			if(n1Diamonds[i] && n2 == n1.getNeighbor(getDirection(i))){
				return true;
			}
		}
		return false;
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
				return null;
		}
	}

	private void addDiamond(DIR d){
		if(this.getDiamonds()[dirToIndex(d)]==false){
			this.getDiamonds()[dirToIndex(d)]=true;
		}
		else{
			System.err.println("Direction already set");
		}

	}

	public static void pp(Node n){
		if(n!=null && n.isFixed()){
			System.out.print("(");
			System.out.print(n.getLabel());
			System.out.print(")");
		}
		else if(n==null){
			System.out.print("n null");
		}
		else{
			System.out.print(n.getLabel());
		}
		System.out.print(" : ");
		Node[] neigh = n.getNeighbors();
		boolean[] dia = n.getDiamonds();
		for(int i=0;i<6;i++){
			if(neigh[i]!=null){
				if(dia[i]==true){
					System.out.print("(");
					System.out.print(neigh[i].getLabel());
					System.out.print(")");
					System.out.print(" ");
				}
				else{
					System.out.print(neigh[i].getLabel());
					System.out.print(" ");
				}
			}
		}
		System.out.println("\n");
	}

	// Getters

	public int nbDiam(boolean[] d){
		int res = 0;
		for(int i=0;i<6;i++){
			if(d[i] == true){
				res += 1;
			}
		}
		return res;
	}

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

	public boolean[] getDiamonds(){
		return this.diamonds;
	}

	public int getLabel(){
		return this.label;
	}

	public boolean isFixed(){
		return this.isFixed;
	}

	public static DIR getDirection(int i){
		switch(i){
			case 0:
				return DIR.NE;
			case 1:
				return DIR.E;
			case 2:
				return DIR.SE;
			case 3:
				return DIR.SW;
			case 4:
				return DIR.W;
			case 5:
				return DIR.NW;
			default:
				System.err.println("Unknown direction");
				return null;
		}
	}

	// Setters

	public void setNeighbor(Node neigh, DIR d){
		int i = dirToIndex(d);
		this.neigh[i] = neigh;
	}

	public void setLabel(int l){
		this.label = l;
	}

	public void setIsFixed(boolean f){
		this.isFixed = f;
	}
	// Makes the link in both sides
	public void setDiamond(DIR d){
		if(this.getNeighbor(d)!=null && this.getDiamonds()[dirToIndex(d)]==false && this.getNeighbor(d).getDiamonds()[dirToIndex(oppositeDirection(d))]==false){
			this.addDiamond(d);
			this.getNeighbor(d).addDiamond(oppositeDirection(d));
		}
		else{
			System.err.print("Link not available for ");System.err.print(this.getLabel());System.err.print(" in direction ");System.err.println(d);
		}
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
