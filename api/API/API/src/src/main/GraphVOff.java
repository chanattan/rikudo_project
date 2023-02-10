package src.main;
public class GraphVOff extends GraphV {
	
	private NodeVOff source, bottom;
	
	public GraphVOff(Graph g) {
		super(g);
		this.nodes = new NodeVOff[graph.getNodes().size()];
		
		for (Node node : graph.getNodes()) {
			nodes[node.id] = new NodeVOff(node); //false id true id
		}
		
		//we consider the source of the graph to be at the center of the screen
		nodes[graph.getSource().id].setPosition(0, 0);
		//we want to set the position to other cells relative to the source
		DFS(g.getSource().id);
	}
	
	public NodeVOff getSource() {
		return this.source;
	}
	
	public boolean hasSource() {
		return this.source != null;
	}
	
	public NodeVOff getBottom() {
		return this.bottom;
	}
	
	public boolean hasBottom() {
		return this.bottom != null;
	}
	
	public boolean isSource(NodeVOff node) {
		return this.source == node;
	}
	
	public boolean isBottom(NodeVOff node) {
		return this.bottom == node;
	}
	
	public void setSource(NodeVOff node) {
		System.out.println("Source set");
		if (node == null) {
			if (source != null) {
				source.getNode().setLabel(-1);
				source.getNode().setIsFixed(false);
				source.forceColor(null);
			}
			source = null;
			return;
		}
		node.getNode().setLabel(1);
		node.getNode().setIsFixed(true);
		this.graph.setSource(node.getNode());
		this.source = node;
	}
	
	public void setBottom(NodeVOff node) {
		System.out.println("Bottom set");
		if (node == null) {
			if (bottom != null) {
				bottom.getNode().setLabel(-1);
				bottom.getNode().setIsFixed(false);
				bottom.forceColor(null);
			}
			bottom = null;
			return;
		}
		node.getNode().setLabel(NodeVOff.COUNTER);
		node.getNode().setIsFixed(true);
		this.graph.setDestination(node.getNode());
		this.bottom = node;
	}
	
	/**
	 * Export the graph created using DFS to copy the graph containing existing nodes.
	 * The graph must be correct : connected and have source and destination points.
	 * @return existing graph
	 */
	public Graph export() {
		Node.COUNTER = 0;
		if (source == null || bottom == null) {
			System.err.println(Visualizer.prefix + "Error, cannot export an incomplete graph, insert source/destination.");
			return null;
		}
		Graph copy_graph = DFS_2(source.getNode().id);
		return copy_graph;
	}
	
	private static int i = 0;
	
	/*
	 * Recursive DFS used in DFS(int v) to create the exported graph.
	 */
    private void DFS_bis_2(int cur, boolean visited[], Node father, Node.DIR dir, Graph exported_graph)
    {
    	NodeV node = this.nodes[cur];
    	if (!((NodeVOff) node).exists()) return;
    	Node copy = new Node(node.getNode(), i);
    	for (int i = 0; i < 6; i++)
    		copy.setNeighbor(null, Node.getDirection(i));
    	i++;
    	if (father != null)
    		Node.linkNodes(father, copy, dir);
    	if (graph.getSource() == node.getNode()) {
    		exported_graph.setSource(copy);
    	}
    	else if (graph.getDestination() == node.getNode()) exported_graph.setDestination(copy);
    	exported_graph.addNode(copy);
    	
        visited[cur] = true;
        //for all neighbors of v
        int counter = 0;
        
        while (counter < 6) {
        	Node neigh = node.getNode().getNeighbors()[counter];
        	if (neigh != null) {
	            int next = neigh.id;
	            if (((NodeVOff) nodes[next]).exists()) {
		            if (!visited[next]) {
//		            	exported_graph.
//			            nodes[next].setPosition(xb, yb);
			            DFS_bis_2(next, visited, copy, Node.getDirection(counter), exported_graph);
		            }
	            }
        	}
            counter++; //next neighbor
        }
    }
 
    /**
     * Deep First Search
     * @param v
     */
    private Graph DFS_2(int v)
    {
        //by default all items are false
    	i=0;
        boolean visited[] = new boolean[graph.getNodes().size()];
        Graph exported_graph = new Graph();
        DFS_bis_2(v, visited, null, null, exported_graph);
        if (exported_graph.getSource() == null || exported_graph.getDestination() == null) {
        	System.err.println(Visualizer.prefix + "Error, did not find source or destination in exported graph.");
        	return null;
        }
        return exported_graph;
    }

}
