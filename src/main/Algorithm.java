package src.main;
import java.util.ArrayList;

import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;

public class Algorithm {
    
    //General backtrack algorithm
    public static void backtrack(Graph g, boolean boolDiam){ //The first node and the last node have to be labeled 
        ArrayList<Node> nodes = g.getNodes();
        Node start = g.getSource();
        Node terminal = g.getDestination();
        if(boolDiam){
            backtrackingDiamondsRec(g, start, terminal);
        }
        else{
            backtrackingRec(g, start, terminal);
        }
    }

    // Recursive backtracking solver without the diamonds (Depth-first search) on a labeled graph starting on the start node and ending on the terminal node 
	private static Graph backtrackingRec(Graph g, Node start, Node terminal){
        Node[] neigh;                   // Neighbors of the node start
        Graph solution = null;          // The graph with all nodes labeled. Is null if there is no solution
        System.out.print("*** start ");
        System.out.print(start.getLabel());
        System.out.println(" ***");
        Graph.pp(g);
        System.out.println("***************");
        if(start.getLabel() == terminal.getLabel()){          // if the path crosses all cells ->  terminal.getLabel()=g.size()
            return g;
        }
        else{                           // if start.getLabel() != g.size()
            neigh = start.getNeighbors();
            for(int i=0;i<6 && solution == null;i++){   // 6 because it has always 6 neighbors
                System.out.println(start.getLabel());
                if(neigh[i]!=null){
                    if((neigh[i].isFixed() && neigh[i].getLabel()==start.getLabel()+1) || (neigh[i].getLabel() == -1)){
                        neigh[i].setLabel(start.getLabel()+1);
                        solution = backtrackingRec(g,neigh[i],terminal);
                    }
                }
            }
            return solution;
        }
    }

    // Recursive backtracking solver with the diamonds (Depth-first search)
    private static Graph backtrackingDiamondsRec(Graph g, Node start, Node terminal){
        Node[] neigh;                     // Neighbors of the node start
        Graph solution = null;            // The graph with all nodes labeled. Is null if there is no solution

        if(start == terminal){
            return g;
        }
        else{
            Node.DIR[] dia=start.getDiamonds();
            Node[] prio_nei = new Node[] {start.getNeighbor(dia[0]),start.getNeighbor(dia[1])};
            //No diamond
            if(prio_nei[0]==null && prio_nei[1]==null){
                neigh = start.getNeighbors();
                for(int i=0;i<6 && solution == null;i++){   // 6 because it has always 6 neighbors
                    if(neigh[i]!=null){
                        if((neigh[i].isFixed() && neigh[i].getLabel()==start.getLabel()+1) || (neigh[i].getLabel() == -1)){
                            neigh[i].setLabel(start.getLabel()+1);
                            solution = backtrackingDiamondsRec(g,neigh[i],terminal);
                        }
                    }
                }
            }
            //One diamond and it is available
            else if((prio_nei[1]==null) && ((prio_nei[0].isFixed() && prio_nei[0].getLabel()==start.getLabel()+1) || (prio_nei[0].getLabel() == -1))){
                prio_nei[0].setLabel(start.getLabel()+1);
                solution = backtrackingDiamondsRec(g,prio_nei[0],terminal);
            }
            //Symetrical case
            else if((prio_nei[0]==null) && ((prio_nei[1].isFixed() && prio_nei[1].getLabel()==start.getLabel()+1) || (prio_nei[1].getLabel() == -1))){
                prio_nei[1].setLabel(start.getLabel()+1);
                solution = backtrackingDiamondsRec(g,prio_nei[1],terminal);
            }
            //Two diamonds but one is not available anymore
            else if(prio_nei[1]!=null && (prio_nei[0].getLabel() != -1)){
                prio_nei[1].setLabel(start.getLabel()+1);
                solution = backtrackingDiamondsRec(g,prio_nei[1],terminal);
            }
            //Symetrical case
            else if(prio_nei[0]!=null && (prio_nei[1].getLabel() != -1)){
                prio_nei[0].setLabel(start.getLabel()+1);
                solution = backtrackingDiamondsRec(g,prio_nei[0],terminal);
            }
            //In all the other cases the rules are broken, so it is not a valid graph

            return solution;
     
        }
    }



	// ************************************* SAT Solver *************************************


	// Solve Rikudo with a SAT-Solver (base with diamond)
	public static boolean satSolve(Graph g){
		return satSolve(g, true);
	}

	// Solve Rikudo. Allow for a diamond-less solve
	public static boolean satSolve(Graph g, boolean withDiamond){

		ArrayList<Node> nodes = g.getNodes();
		
		// Computes the logical formula in CNF form that encodes the problem
		ArrayList<ArrayList<Integer>> dimacs;
		if(withDiamond){
			dimacs = graphToDimacsDiamond(g);
		}else{
			dimacs = graphToDimacs(g);
		}
		

		int[] model = solveDimacs(dimacs);
		// No solution (or timeout)
		if(model == null){
			return false;
		}
		modelToGraph(model, nodes);
		return true;
	
	}


	private static int[] solveDimacs(ArrayList<ArrayList<Integer>> dimacs){

		int m = dimacs.size(); // Number of clauses

		// Use Sat4j solver cf code example
		ISolver solver = SolverFactory.newDefault();
		// solver.newVar(bijection(n-1,n-1)); 			// Highest identifier 
		solver.setExpectedNumberOfClauses(m);			// Number of clauses
		for(ArrayList<Integer> clause : dimacs){		// Add clauses to solver
			// We convert our ArrayList<Integer> to int[]
			int[] primitive = clause.stream().mapToInt(Integer::intValue).toArray();
			try{
				solver.addClause(new VecInt(primitive));
			}catch(ContradictionException e){
				System.err.println("Contradiction...");
				e.printStackTrace();
				return null;
			}
		}
		

		IProblem problem = solver;
		try{
			int[] model = null;
			if(problem.isSatisfiable()){
				// We get the model
				model = problem.model();
			}
			return model;
		} catch (org.sat4j.specs.TimeoutException e) {
			e.printStackTrace();
			System.err.println("Timeout");
			return null;
		}
	}

	private static void modelToGraph(int[] model, ArrayList<Node> nodes){
		for(int k : model){
			// Negative vars are the one that are false 
			// We only consider the positive variables that are actual conditions
			if(k > 0){
				int[] iv = inverseBijection(k);; // We use the inverse to get i,v
				nodes.get(iv[1]).setLabel(iv[0] + 1);
			}
		}
	}


	/*
	 * Graph to Dimacs function
	 * It converts a Rikudo problem to Dimacs CNF form
	 * i.e. it will be converted as a collection of clauses, clauses being written as an collections of integers different from 0
	 * We won't be writing the 0s separating clauses.
	 */
	private static ArrayList<ArrayList<Integer>> graphToDimacs(Graph g){
		
		// We will create a CNF clause from the graph representing the constraints
		// cf an explanation file for the exact form

		// x_i,v will represent whether the node v is labeled i (actually i+1 as we will number i from 0 to n-1)
		// The number v will be the index in ArrayList
		// In the Dimacs format, we need to give integers to each variables.
		// The bijection used is below

		ArrayList<Node> nodes = g.getNodes();

		ArrayList<ArrayList<Integer>> cnf = new ArrayList<ArrayList<Integer>>();
		
		// Each v is precisely once
		cnf.addAll(vPreciselyOnce(nodes));

		// Each i is precisely once
		cnf.addAll(iPreciselyOnce(nodes));

		// Consecutive i are adjacent in graphs
		cnf.addAll(vConsecutiveAdjacent(nodes));

		// Fixed nodes
		cnf.addAll(vFixed(nodes));

		return cnf;
	}


	private static ArrayList<ArrayList<Integer>> graphToDimacsDiamond(Graph g){
		ArrayList<Node> nodes = g.getNodes();
		ArrayList<ArrayList<Integer>> cnf = graphToDimacs(g);

		// Diamonds must be satisfied
		cnf.addAll(vwDiamond(nodes));

		return cnf;
	}


	// Bijection (and inverse) used to map x_i_v to numbers for Dimacs
	// We can't have 0 as a var so the bijection is from N * N -> N\0
	private static int bijection(int i, int j){
		return (i+j)*(i+j+1)/2 + j + 1;
	}

	private static int[] inverseBijection(int n){
		int k = 0;
		while (k*(k+1) <= 2*(n - 1)){
			k++;
		}
		
		k -= 1;
		int j = (n-1) - (k*(k+1))/2;
		return new int[] {k-j, j};
	}

	// I highly recommand looking at the paper that explains which fomulas we are using


	// vertices are present once (we do it for all v here)
	private static ArrayList<ArrayList<Integer>> vPreciselyOnce(ArrayList<Node> nodes){
		ArrayList<ArrayList<Integer>> cnf = new ArrayList<ArrayList<Integer>>();

		int n = nodes.size();

		for(int v = 0; v < n; v++){
			// v appears at least once
			ArrayList<Integer> clause1 = new ArrayList<Integer>();
			for(int i = 0; i < n; i++){
				clause1.add(bijection(i,v));
			}
			cnf.add(clause1);

			for(int i = 0; i < n; i++){
				for(int j = 0; j < n; j++){
					if(j != i){
						ArrayList<Integer> clause = new ArrayList<Integer>();
						clause.add(-bijection(i,v));
						clause.add(-bijection(j,v));
						cnf.add(clause);
					}
				}
			}
		}

		return cnf;
	}


	// Label i is only given once
	private static ArrayList<ArrayList<Integer>> iPreciselyOnce(ArrayList<Node> nodes){
		ArrayList<ArrayList<Integer>> cnf = new ArrayList<ArrayList<Integer>>();

		int n = nodes.size();

		for(int i = 0; i < n; i++){
			ArrayList<Integer> clause1 = new ArrayList<Integer>();
			for(int v = 0; v < n; v++){
				clause1.add(bijection(i,v));
			}
			cnf.add(clause1);

			for(int v = 0; v < n; v++){
				for(int w = 0; w < n; w++){
					if(v != w){
						ArrayList<Integer> clause = new ArrayList<Integer>();
						clause.add(-bijection(i,v));
						clause.add(-bijection(i,w));
						cnf.add(clause);
					}
				}
			}
		}

		return cnf;
	}


	// Every summit have its neighbors labeled correctly
	private static ArrayList<ArrayList<Integer>> vConsecutiveAdjacent(ArrayList<Node> nodes){
		ArrayList<ArrayList<Integer>> cnf = new ArrayList<ArrayList<Integer>>();

		int n = nodes.size();

		for(int v = 0; v < n; v++){
			Node nv = nodes.get(v);
			for(int i = 0; i < n - 1; i++){
				ArrayList<Integer> clause = new ArrayList<Integer>();

				clause.add(-bijection(i,v));
				for(int w = 0; w < n; w++){
					if(Node.areNeighbors(nodes.get(w), nv)){
						clause.add(bijection(i+1,w));
					}
				}
				cnf.add(clause);
			}
		}
		return cnf;
	}

	// Creates clauses to force fixed vertices (clauses are just 1 litteral)
	private static ArrayList<ArrayList<Integer>> vFixed(ArrayList<Node> nodes){
		ArrayList<ArrayList<Integer>> cnf = new ArrayList<ArrayList<Integer>>();

		for(int i = 0; i < nodes.size(); i++){
			Node n = nodes.get(i);
			if(n.isFixed()){
				ArrayList<Integer> clause = new ArrayList<Integer>();
				// -1 to map visual labels (1 to n) to "logical" labels (0 to n-1)
				clause.add(bijection(n.getLabel()-1, i));
				cnf.add(clause);
			}
		}

		return cnf;
	}


	private static ArrayList<ArrayList<Integer>> vwDiamond(ArrayList<Node> nodes){
		ArrayList<ArrayList<Integer>> cnf = new ArrayList<ArrayList<Integer>>();

		int n = nodes.size();

		for(int v = 0; v < n; v++){
			Node nv = nodes.get(v);
			for(int w = 0; w < n; w++){
				if(Node.areDiamonded(nv, nodes.get(w))){
					for(int i = 1; i < n - 1; i++){
						ArrayList<Integer> clause1 = new ArrayList<Integer>();
						clause1.add(-bijection(i,v));
						clause1.add(bijection(i+1,w));
						clause1.add(bijection(i-1,w));
						cnf.add(clause1);


						// This half will we considerd in the reverse case for v and w
						// ArrayList<Integer> clause2 = new ArrayList<Integer>();
						// clause2.add(-bijection(i,w));
						// clause2.add(bijection(i+1,v));
						// clause2.add(bijection(i-1,v));
						// cnf.add(clause2);
					}
				}
			}
		}
		return cnf;
	}



}