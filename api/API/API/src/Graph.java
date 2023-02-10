import java.util.ArrayList;

public class Graph {
	private ArrayList<Node> nodes;
	
	// We consider that the first and the last nodes are always fixed
	private Node source;		// First nodes (labeled 1)
	private Node dest;			// Last nodes


	public Graph(){
		this.nodes = new ArrayList<Node>();
		this.source = null;
		this.dest = null;
	}
	
	public Graph(ArrayList<Node> nodes){
		this.nodes = nodes;
		this.finalizeGraph();
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
	
	public void setSource(Node source) {
		this.source = source;
	}
	
	public void setDestination(Node dest) {
		this.dest = dest;
	}

	public void setNodes(ArrayList<Node> nodes) {
		this.nodes = nodes;
	}
	
	public void addNode(Node n){
		this.nodes.add(n);
	}

	// Finalize the graph (MANDATORY when created with empty constructor)
	// Sets the source and destination
	public void finalizeGraph(){
		this.source = getMinNode(this.nodes);
		this.dest = getMaxNode(this.nodes);
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