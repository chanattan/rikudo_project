package src.main;
import java.util.ArrayList;

public class Graph {
	private ArrayList<Node> nodes;
	
	// We consider that the first and the last nodes are always fixed
	private Node source;		// First nodes (labeled 1)
	private Node dest;			// Last nodes


	
	public Graph(ArrayList<Node> nodes){
		this.nodes = nodes;
		this.source = getMinNode(nodes);
		this.dest = getMaxNode(nodes);
	}

	// No sanity check on actual first and last
	public Graph(ArrayList<Node> nodes, Node source, Node dest){
		this.nodes = nodes;
		this.source = source;
		this.dest = dest;
	}


	public ArrayList<Node> getNodes(){
		return this.nodes;
	}

	public Node getSource(){
		return this.source;
	}

	public Node getDestination(){
		return this.dest;
	}

	// *********************** PRIVATE ***********************

	// Computes the last node of the array (with max label)
	private static Node getMaxNode(ArrayList<Node> nodes){
		int maxLabel = -1;
		Node retNode = null;
		for(Node n : nodes){
			int l = n.getLabel();
			if(l > maxLabel){
				maxLabel = l;
				retNode = n;
			}
		}
		return retNode;
	}

	// Computes the first node of the array (with min label)
	private static Node getMinNode(ArrayList<Node> nodes){
		Node retNode = nodes.get(0);
		int minLabel = retNode.getLabel();
		for(int i = 1; i < nodes.size(); i++){
			Node n = nodes.get(i);
			int l = n.getLabel();
			if(l < minLabel && l > 0){
				minLabel = l;
				retNode = n;
			}
		}
		return retNode;
	}



	public static void pp(Graph g){
		ArrayList<Node> nodes = g.getNodes();
		for(int i=0;i<nodes.size();i++){
			Node.pp(nodes.get(i));
		}
	}
}