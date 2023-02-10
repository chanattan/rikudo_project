package src.main;
import java.awt.Color;

/*
 * GraphV acts as a handler for Graph, it allows the parsing of nodes to NodeV.
 */
public class GraphV {
	protected NodeV[] nodes;
	protected Graph graph;
	
	public GraphV(Graph g) {
		this.graph = g;
		this.nodes = new NodeV[graph.getNodes().size()];
		System.out.println("node size : " + graph.getNodes().size());
		for (Node node : graph.getNodes()) {
			System.out.println("node id : " + node.id);
			nodes[node.id] = new NodeV(node);
		}
		System.out.println("end");
		
		if (graph.getSource() == null || graph.getDestination() == null) {
			System.err.println(Visualizer.prefix + "Error, found in building of GraphV source or destination null.");
		} else {
			//we consider the source of the graph to be at the center of the screen
			nodes[graph.getSource().id].forceColor(Color.RED);
			nodes[graph.getSource().id].setPosition(0, 0);
			
			nodes[graph.getDestination().id].forceColor(Color.GREEN);
			graph.getDestination().setLabel(graph.getNodes().size());
			//we want to set the position to other cells relative to the source
			DFS(g.getSource().id);
		}
	}
	
	public Graph getGraph() {
		return graph;
	}
	
	public NodeV[] getNodesV() {
		return nodes;
	}
	
	/*
	 * Recursive DFS used in DFS(int v).
	 */
    private void DFS_bis(int cur, boolean visited[])
    {
    	NodeV node = nodes[cur];
    	int x = node.getX();
    	int y = node.getY();

        visited[cur] = true;
        //for all neighbors of v
        int counter = 0;
        
        while (counter < 6) {
        	Node neigh = node.getNode().getNeighbors()[counter];
        	if (neigh != null) {
	            int next = neigh.id;
	            if (!visited[next]) {
	            	int xb = x;
	            	int yb = y;
		            switch (counter) { //TODO fix gap to remove hardcoded values
		            	case 0: //NE
		            		xb+=RikudoPane.CELL_WIDTH/2-7;
		            		yb-=RikudoPane.CELL_HEIGHT-26;
		            		break;
		            	case 1: //E
		            		xb+=RikudoPane.CELL_WIDTH-15;
		            		break;
		            	case 2: //SE
		            		xb+=RikudoPane.CELL_WIDTH/2-7;
		            		yb+=RikudoPane.CELL_HEIGHT-26;
		            		break;
		            	case 3: //SW
		            		xb-=RikudoPane.CELL_WIDTH/2-7;
		            		yb+=RikudoPane.CELL_HEIGHT-26;
		            		break;
		            	case 4: //W
		            		xb-=RikudoPane.CELL_WIDTH-15;
		            		break;
		            	case 5: //NW
		            		xb-=RikudoPane.CELL_WIDTH/2-7;
		            		yb-=RikudoPane.CELL_HEIGHT-26;
		            		break;
		            }
		            nodes[next].setPosition(xb, yb);

		            DFS_bis(next, visited);
	            }
        	}
            counter++; //next neighbor
        }
    }
 
    /**
     * Deep First Search
     * @param v
     */
    void DFS(int v)
    {
        //by default all items are false
        boolean visited[] = new boolean[graph.getNodes().size()];
        
        DFS_bis(v, visited);
    }
}
