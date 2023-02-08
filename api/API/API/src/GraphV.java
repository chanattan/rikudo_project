
/*
 * GraphV acts as a handler for Graph, it allows the parsing of nodes to NodeV.
 */
public class GraphV {
	private NodeV[] nodes;
	private Graph graph;
	
	public GraphV(Graph g) {
		this.graph = g;
		this.nodes = new NodeV[graph.getNodes().size()];
		
		for (Node node : graph.getNodes()) {
			nodes[node.id] = new NodeV(node);
		}
		
		//we consider the source of the graph to be at the center of the screen
		nodes[graph.getSource().id].setPosition(0, 0);
		//we want to set the position to other cells relative to the source
		DFS(g.getSource().id);
	}
	
	public NodeV[] getNodesV() {
		return nodes;
	}
	
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
		            switch (counter) { //TODO fix gap to remove hardcoded values
		            	case 0: //NE
		            		x+=RikudoPane.CELL_WIDTH/2-7;
		            		y-=RikudoPane.CELL_HEIGHT-26;
		            		break;
		            	case 1: //E
		            		x+=RikudoPane.CELL_WIDTH-15;
		            		break;
		            	case 2: //SE
		            		x+=RikudoPane.CELL_WIDTH/2-7;
		            		y+=RikudoPane.CELL_HEIGHT-26;
		            		break;
		            	case 3: //SW
		            		x-=RikudoPane.CELL_WIDTH/2-7;
		            		y+=RikudoPane.CELL_HEIGHT-26;
		            		break;
		            	case 4: //W
		            		x-=RikudoPane.CELL_WIDTH-15;
		            		break;
		            	case 5: //NW
		            		x-=RikudoPane.CELL_WIDTH/2-7;
		            		y-=RikudoPane.CELL_HEIGHT-26;
		            		break;
		            }
		            
		            nodes[next].setPosition(x, y);

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

    	System.out.println("DFS ended.");
    }
}
