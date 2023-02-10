package src.main;

import java.util.ArrayList;

public class Node {

	public static int COUNTER = 0;
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

	//Copy constructor
	public Node(Node n, int id) {
		this.id = id;
		this.label = n.label;
		this.diamonds = new boolean[6];
		for (int i = 0; i < n.diamonds.length; i++) this.diamonds[i] = n.diamonds[i];
		this.isFixed = n.isFixed;
		this.neigh = new Node[n.neigh.length];
		for (int i = 0; i < n.neigh.length; i++) this.neigh[i] = n.neigh[i];
	}
	
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


	/**
	 * @param label : the new label we want to use
	 * @return Whether this label is valid considering diamonds and neighbors
	 */
	public boolean isLegal(int label){
		Node[] neigh = getActualDiamondNodes();
		//if there is only -1 around it
		boolean ism1=true;
		for(int i=0;i<6;i++){
			if(this.getNeighbor(getDirection(i)).getLabel()!=-1) ism1=false;
		}
		if(ism1) return true;
		//if there is the predecesor around it
		boolean isPred=false;
		for(int i=0;i<6;i++){
			if(this.getNeighbor(getDirection(i)).getLabel()!=label-1 || this.getNeighbor(getDirection(i)).getLabel()!=label+1) isPred=true;
		}
		if(!isPred) return false;


		switch(this.nbDiam(this.getDiamonds())){
			case 0:
				return true;
			case 1:
				// the linked diamond needs to have to be free or to be the predecesor or to be the succesor
				if(neigh[0].getLabel()==label-1 || neigh[0].getLabel()==-1 || neigh[0].getLabel()==label+1){
					return true;
				}
				else{
					return false;
				}
			case 2:
				if((neigh[0].getLabel()==label-1 && neigh[1].getLabel()==label+1) 
				|| (neigh[1].getLabel()==label-1 && neigh[0].getLabel()==label+1)
				|| (neigh[0].getLabel()==label-1 && neigh[1].getLabel()==label-1) 
				|| (neigh[1].getLabel()==label-1 && neigh[0].getLabel()==label-1)
				|| (neigh[0].getLabel()==label-1 && neigh[1].getLabel()==-1)
				|| (neigh[1].getLabel()==label-1 && neigh[0].getLabel()==-1)){
					return true;
				}
				else{
					return false;
				}
			default:
				return false;
		}
	}

	/**
	 * Link {@code}Node{@code} 1 and 2 on a direction
	 * @param n1 : The first {@code}Node{@code}
	 * @param n2 : The second {@code}Node{@code}
	 * @param d : The direction between the nodes, from {@code}n1{@code} to {@code}n2{@code}
	 */
	public static void linkNodes(Node n1, Node n2, DIR d){
		n1.setNeighbor(n2, d);
		n2.setNeighbor(n1, oppositeDirection(d));
	}

	/**
	 * Checks if 2 {@code}Node{@code} are neighbors
	 * @param n1 : the first {@code}Node{@code}
	 * @param n2 : the second {@code}Node{@code}
	 * @return Whether {@code}n1{@code} and {@code}n2{@code} are linked
	 */
	public static boolean areNeighbors(Node n1, Node n2){
		for(Node n : n1.getNeighbors()){
			if(n == n2){
				return true;
			}
		}
		return false;
	}

	/**
	 * Check if 2 {@code}Node{@code} are neighbor and separated by a diamond
	 * @param n1 : the first {@code}Node{@code}
	 * @param n2 : the second {@code}Node{@code}
	 * @return Whether {@code}n1{@code} and {@code}n2{@code} are separated by a diamond
	 */
	public static boolean areDiamonded(Node n1, Node n2){
		boolean[] n1Diamonds = n1.getDiamonds();
		for(int i = 0; i < n1Diamonds.length; i++){
			if(n1Diamonds[i] && n2 == n1.getNeighbor(getDirection(i))){
				return true;
			}
		}
		return false;
	}

	/**
	 * Computes the opposite direction of a direction
	 * @param d : A direction
	 * @return The opposite direction
	 */
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

	/**
	 * Finds the neighbor with the next label
	 * @return the {@code}Node{@code} object of the neighbor, {@code}null{@code} if it doesn't exist
	 */
	public Node getNextNeighbor(){
		int label = this.getLabel();
		for(Node n : this.getNeighbors()){
			if(n!= null && n.getLabel() == label + 1) { return n;}
		}
		return null;
	}

	/**
	 * Computes the relative direction of a given neighbor
	 * @param n : a {@code}Node{@code}
	 * @return the direction between {@code}this{@code} and {@code}n{@code}, {@code}null{@code} if it doesn't exist
	 */
	public DIR getNeighborDirection(Node n){
		Node[] neigh = this.getNeighbors();
		for(int i = 0 ; i < neigh.length; i++){
			if (n == neigh[i]) { return getDirection(i);}
		}
		return null;
	}
	

	/**
	 * Add a diamond in a direction
	 * @param d : A direction
	 */
	private void addDiamond(DIR d){
		if(this.getDiamonds()[dirToIndex(d)]==false){
			this.getDiamonds()[dirToIndex(d)]=true;
		}
		else{
			System.err.println("Direction already set");
		}

	}

	/**
	 * Pretty prints a {@code}Node{@code}
	 * @param n : the {@code}Node{@code} to pretty print
	 */
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


	/**
	 * Computes a recommanded label based on the neighbors.
	 * It is to be used by the GUI as a starter number.
	 * It is returning the max + 1 of the neighboring labels, priorizing diamonds
	 * @implNote May return n+1 where n is the number of nodes
	 * @return A int representing the recommanded label
	 */
	public int guessLabel(){
		if(this.getNumberDiamond() > 0){
			Node[] actDiam = this.getActualDiamondNodes();
			for(Node n : actDiam){
				// If a diamond has a label already set (ie != -1)
				if(n.getLabel() != -1){
					return n.getLabel() + 1;
				}
			}	
		}
		Node[] actNeigh = this.getActualNeighbors();
		int maxLab = -1;
		for(Node n : actNeigh){
			int lab = n.getLabel();
			if(lab != -1 && lab > maxLab) { maxLab = lab;}
		}
		return maxLab + 1;
	}


	/**
	 * Computes the number of actual diamonds of an array
	 * @deprecated
	 * @param d : a boolean array representing whether a diamond is present
	 * @return The number of diamonds ({@code}true{@code})
	 */
	public int nbDiam(boolean[] d){
		int res = 0;
		for(int i=0;i<6;i++){
			if(d[i] == true){
				res += 1;
			}
		}
		return res;
	}


	/**
	 * Computes the number of actual diamonds
	 * @return The number of diamonds ({@code}true{@code})
	 */
	public int getNumberDiamond(){
		int res = 0;
		boolean[] d = this.getDiamonds();
		for(int i=0;i<6;i++){
			if(d[i] == true){
				res += 1;
			}
		}
		return res;
	}

	/**
	 * Get neighboring {@code}Node{@code} array
	 * @return A {@code}Node{@code} array with cells depending on direction, {@code}null{@code} if no neighbor in that direction
	 */
	public Node[] getNeighbors(){
		return neigh;
	}

	/**
	 * Gets the neighboring {@code}Node{@code} in the given direction
	 * @param d : a direction
	 * @return The neighboring {@code}Node{@code}, {@code}null{@code} if it doesn't exist
	 */
	public Node getNeighbor(DIR d){
		int i = dirToIndex(d);
		if(i == -1){
			return null;
		}
		return this.neigh[i];
	}

	/**
	 * Gets the diamond array. It is reprensented as a size 6 array with boolean in the directions
	 * @return The diamond array
	 */
	public boolean[] getDiamonds(){
		return this.diamonds;
	}

	/**
	 * Gets the {@code}Node{@code} label
	 * @return The label
	 */
	public int getLabel(){
		return this.label;
	}

	/**
	 * 
	 * @return Whether a {@code}Node{@code} is fixed as a constraint
	 */
	public boolean isFixed(){
		return this.isFixed;
	}

	/**
	 * Convert an array index to a direction
	 * @param i : the array index
	 * @return The direction associated with the direction
	 */
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

	/**
	 * @return Array containing the actual neighbors (removing null)
	 */
	public Node[] getActualNeighbors(){
		ArrayList<Node> retNeigh = new ArrayList<Node>();
		for(Node n : this.getNeighbors()){
			if (n != null){
				retNeigh.add(n);
			}
		}
		return retNeigh.toArray(new Node[0]);
	}

	/**
	 * 
	 * @return Array containing nodes that are linked with a diamond to this node
	 */
	public Node[] getActualDiamondNodes(){
		ArrayList<Node> retDiam = new ArrayList<Node>();
		boolean[] diams = this.getDiamonds();
		for(int i = 0; i < diams.length; i++){
			if(diams[i]) {retDiam.add(this.getNeighbor(Node.getDirection(i)));}
		}
		return retDiam.toArray(new Node[0]);
	}

	/**
	 * Change a {@code}Node{@code} neighbor.
	 * @implNote : This is not symmetrical !!
	 * @param neigh : the new neighbor
	 * @param d : the direction from {@code}this{@code} to {@code}neigh{@code}
	 */
	public void setNeighbor(Node neigh, DIR d){
		int i = dirToIndex(d);
		this.neigh[i] = neigh;
	}

	/**
	 * Sets the {@code}Node{@code} label
	 * @param l : the new label
	 */
	public void setLabel(int l){
		this.label = l;
	}

	/**
	 * Sets whether a {@code}Node{@code} is fixed
	 * @param f : whether to fix the {@code}Node{@code}
	 */
	public void setIsFixed(boolean f){
		this.isFixed = f;
	}

	// Makes the link in both sides
	/**
	 * Add a diamond in the given direction in a symmetrical way (i.e. also to the other {@code}Node{@code})
	 * @param d : the direction in which the diamond is
	 */
	public void setDiamond(DIR d){
		if(this.getNeighbor(d)!=null && this.getDiamonds()[dirToIndex(d)]==false && this.getNeighbor(d).getDiamonds()[dirToIndex(oppositeDirection(d))]==false){
			this.addDiamond(d);
			this.getNeighbor(d).addDiamond(oppositeDirection(d));
		}
		else{
			System.err.print("Link not available for ");System.err.print(this.getLabel());System.err.print(" in direction ");System.err.println(d);
		}
	}

	/**
	 * Converts a direction to array index
	 * @param d : a direction
	 * @return The index to be used in arrays
	 */
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
