package src.main;
import java.util.ArrayList;

import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;

public class Algorithm {
    
    //General backtrack algorithm
    public static boolean backtrack(Graph g, boolean boolDiam){ //The first node and the last node have to be labeled 
        ArrayList<Node> nodes = g.getNodes();
        Node start = g.getSource();
        Node terminal = g.getDestination();
        Boolean hasFound = false;

        if(boolDiam){
            hasFound = backtrackingDiamondsRec(g, start, terminal);
        }
        else{
            hasFound = backtrackingRec(g, start, terminal);
        }
        return hasFound;
    }

    // Recursive backtracking solver without the diamonds (Depth-first search) on a labeled graph starting on the start node and ending on the terminal node 
	private static boolean backtrackingRec(Graph g, Node start, Node terminal){
        Node[] neigh;                   // Neighbors of the node start
        boolean solution = false;          // The graph with all nodes labeled. Is null if there is no solution
        if(start.getLabel() == terminal.getLabel() && start.isFixed()){          // if the path crosses all cells ->  terminal.getLabel()=g.size()
            System.out.println("GOAL REACHED!!!");
            return true;
        }
        else{                                                 // if start.getLabel() != g.size()
            neigh = start.getNeighbors();
            for(int i=0;i<6 && solution == false;i++){   // 6 because it has always 6 neighbors
                if(neigh[i]!=null){
                    if((neigh[i].isFixed() && neigh[i].getLabel()==start.getLabel()+1) || (neigh[i].getLabel() == -1)){
                        neigh[i].setLabel(start.getLabel()+1);
                        solution = backtrackingRec(g,neigh[i],terminal);

                        if(solution==false && !neigh[i].isFixed()){
                            neigh[i].setLabel(-1);
                        }
                    }
                }
            }
            return solution;
        }
    }

    // Recursive backtracking solver with the diamonds (Depth-first search)
    private static boolean backtrackingDiamondsRec(Graph g, Node start, Node terminal){
        Node[] neigh;                     // Neighbors of the node start
        boolean solution = false;            // The graph with all nodes labeled. Is null if there is no solution

        if(start.getLabel() == terminal.getLabel()){          // if the path crosses all cells ->  terminal.getLabel()=g.size()
            boolean[] dia=start.getDiamonds();
			switch(start.nbDiam(start.getDiamonds())){
				case 0:
					return true;
				case 1:

					int iD=0;
					while (dia[iD] == false){
						iD++;
					}
					Node.DIR neighD = Node.getDirection(iD);
					return start.getNeighbor(neighD).getLabel()==start.getLabel()-1;

				case 2:
					return false;
			}
			System.out.println("GOAL REACHED!!!");
            return true;
        }
        else{
            boolean[] dia=start.getDiamonds();
            int nbD = start.nbDiam(dia);
            //No diamond
            if(nbD==0){
                neigh = start.getNeighbors();
                for(int i=0;i<6 && solution == false;i++){   // 6 because it has always 6 neighbors
                    if(neigh[i]!=null){
                        if((neigh[i].isFixed() && neigh[i].getLabel()==start.getLabel()+1) || (neigh[i].getLabel() == -1)){
                            neigh[i].setLabel(start.getLabel()+1);
                            solution = backtrackingDiamondsRec(g,neigh[i],terminal);

                            if(solution==false && !neigh[i].isFixed()){
                                neigh[i].setLabel(-1);
                            }
                        }
                    }
                }
            }
            //One diamond
            else if (nbD==1){
                int iD=0;
                while (dia[iD] == false){
                    iD++;
                }
                Node.DIR neighD = Node.getDirection(iD);

                if((start.getNeighbor(neighD).getLabel() != -1) && (start.getNeighbor(neighD).getLabel() == start.getLabel()-1)){
                    neigh = start.getNeighbors();
					for(int i=0;i<6 && solution == false;i++){   // 6 because it has always 6 neighbors
						if(neigh[i]!=null){
							if((neigh[i].isFixed() && neigh[i].getLabel()==start.getLabel()+1) || (neigh[i].getLabel() == -1)){
								neigh[i].setLabel(start.getLabel()+1);
								solution = backtrackingDiamondsRec(g,neigh[i],terminal);

								if(solution==false && !neigh[i].isFixed()){
									neigh[i].setLabel(-1);
								}
							}
						}
					}
                }
				else if(start.getNeighbor(neighD).getLabel() == -1 || (start.getNeighbor(neighD).isFixed() && start.getNeighbor(neighD).getLabel() == start.getLabel()+1)){
					start.getNeighbor(neighD).setLabel(start.getLabel()+1);
					solution = backtrackingDiamondsRec(g,start.getNeighbor(neighD),terminal);
					if(solution==false && !start.getNeighbor(neighD).isFixed()){
                    	start.getNeighbor(neighD).setLabel(-1);
                	}
				}
            }
			//Two diamonds
            else if(nbD==2){
				int iD1=0;
				int iD2=0;
                while (dia[iD1] == false){
                    iD1++;
                }
				iD2=iD1+1;
				while (dia[iD2] == false){
                    iD2++;
                }

                Node.DIR neighD1 = Node.getDirection(iD1);
                Node.DIR neighD2 = Node.getDirection(iD2);
				if((start.getNeighbor(neighD1).getLabel() == -1) || (start.getNeighbor(neighD1).getLabel() == start.getLabel()+1)){
                    start.getNeighbor(neighD1).setLabel(start.getLabel()+1);
					solution = backtrackingDiamondsRec(g,start.getNeighbor(neighD1),terminal);
					if(solution==false && !start.getNeighbor(neighD1).isFixed()){
                    	start.getNeighbor(neighD1).setLabel(-1);
                	}
                }
				else if(start.getNeighbor(neighD1).getLabel() == start.getLabel()-1){
					if((start.getNeighbor(neighD2).getLabel() == -1) || (start.getNeighbor(neighD2).getLabel() == start.getLabel()+1)){
						start.getNeighbor(neighD2).setLabel(start.getLabel()+1);
						solution = backtrackingDiamondsRec(g,start.getNeighbor(neighD2),terminal);
						if(solution==false && !start.getNeighbor(neighD2).isFixed()){
							start.getNeighbor(neighD2).setLabel(-1);
						}
					}
				}
				else{
					//return false;
				}
            }
            //In all the other cases the rules are broken, so it is not a valid graph
            else{
				System.out.println("Diam not valid");
                return false;
            }

            return solution;
        }
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
		cnf.addAll(iConsecutiveAdjacent(nodes));

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



	// Computes a CNF representing the constraint where all v should appear only once
	private static ArrayList<ArrayList<Integer>> vPreciselyOnce(ArrayList<Node> nodes){
		ArrayList<ArrayList<Integer>> cnf = new ArrayList<ArrayList<Integer>>();
		int n = nodes.size();

		// Each vertex v appears exactly once
		for(int v = 0; v < n; v++){
			// vOnceDnf is Phi_v from paper, that is, a DNF
			ArrayList<ArrayList<Integer>> vOnceDnf = new ArrayList<ArrayList<Integer>>();
			for(int i = 0; i < n; i++){
				ArrayList<Integer> clause = new ArrayList<Integer>();
				clause.add(bijection(i,v));
				for(int j = 0; j < n; j++){
					if(j != i){
						clause.add(-bijection(j,v));
					}
				}
				vOnceDnf.add(clause);
			}

			// We convert Phi_v to CNF then fuse it with our final CNF 
			cnf.addAll(dnfToCnf(vOnceDnf));
		}
		return cnf;
	}
	


	// Computes a CNF representing the constraint where all i should appear only once
	private static ArrayList<ArrayList<Integer>> iPreciselyOnce(ArrayList<Node> nodes){
		ArrayList<ArrayList<Integer>> cnf = new ArrayList<ArrayList<Integer>>();
		int n = nodes.size();

		// Equivalent to the Psi_i from paper, still a DNF
		ArrayList<ArrayList<Integer>> iOnceDnf = new ArrayList<ArrayList<Integer>>();
		
		// Each i is precisely once
		for(int i = 0; i < n; i++){
			iOnceDnf.clear();
			
			for(int v = 0; v < n; v++){
				ArrayList<Integer> clause = new ArrayList<Integer>();
				clause.clear();
				clause.add(bijection(i,v));
				for(int w = 0; w < n; w++){
					if(w != v){
						clause.add(-bijection(i,w));
					}
				}
				iOnceDnf.add(clause);
			}
			
			// Convert Psi_i to CNF then fuse it
			cnf.addAll(dnfToCnf(iOnceDnf));
		}
		return cnf;
	}

	// Computes a CNF representing the constraint about consecutive path being connected
	private static ArrayList<ArrayList<Integer>> iConsecutiveAdjacent(ArrayList<Node> nodes){
		ArrayList<ArrayList<Integer>> cnf = new ArrayList<ArrayList<Integer>>();
		int n = nodes.size();

		
		// Consecutive i are adjacent in graphs
		for(int i = 0; i < n-1; i++){
			// iConsecAdj is still a DNF, cf paper
			ArrayList<ArrayList<Integer>> iConsecAdj = new ArrayList<ArrayList<Integer>>();
			
			for(int v = 0; v < n;v++){
				// We check all neighbors
				for(Node wNode : nodes.get(v).getNeighbors()){
					if (wNode != null){
						int w = nodes.indexOf(wNode); // We retreive the index of a neighbor
						ArrayList<Integer> clause = new ArrayList<Integer>();
						clause.add(bijection(i,v));
						clause.add(bijection(i+1, w));
						iConsecAdj.add(clause);
					}
				}
			}

			cnf.addAll(dnfToCnf(iConsecAdj));
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
			Node vNode = nodes.get(v);
			for(int w = 0; w < n; w++){
				if(Node.areDiamonded(vNode, nodes.get(w))){
					ArrayList<ArrayList<Integer>> vwDiamond = new ArrayList<ArrayList<Integer>>();
					
					for(int i = 0; i < n-1; i++){
						ArrayList<Integer> clause1 = new ArrayList<Integer>();
						clause1.add(bijection(i,v));
						clause1.add(bijection(i+1,w));

						ArrayList<Integer> clause2 = new ArrayList<Integer>();
						clause2.add(bijection(i,w));
						clause2.add(bijection(i+1,v));

						vwDiamond.add(clause1);
						vwDiamond.add(clause2);
					}

					cnf.addAll(dnfToCnf(vwDiamond));
				}
			}
			
		}


		return cnf;
	}
	


	/*
	 * Convert a DNF to CNF.
	 * Each formula is represented in Dimacs DNF to Dimacs CNF
	 * We are "just" distributing OR over AND
	 * Here, clauses are representing the conjonction in the OR
	 */
	private static ArrayList<ArrayList<Integer>> dnfToCnf(ArrayList<ArrayList<Integer>> dnf){
		ArrayList<ArrayList<Integer>> cnf = new ArrayList<ArrayList<Integer>>();
		
		// n is the number of disjonctions
		int n = dnf.size();

		// We retrieve the size of every clauses
		// cf m_i on paper
		int[] sizes = new int[n];
		for(int i = 0; i < n; i++){
			sizes[i] = dnf.get(i).size();
		}

		// cf paper for "better" explanation
		// We first iterate over each OR
		// I will be the indexes to pick (the subset of the products of the segments)
		// It will change at each round. We will be "adding 1" each time and act as if we were doing
		// an addition in a base that changes at each digits
		int[] I = new int[n];
		for (int i = 0; i < n; i++){
			I[i] = 0;
		}

		do{
			// We pick a var in each clauses
			ArrayList<Integer> clauses = new ArrayList<Integer>();
			for(int i = 0; i < n; i++){
				clauses.add(dnf.get(i).get(I[i]));
			}
			cnf.add(clauses);
			incrI(I, sizes);
		}while(!isFullZero(I));


		return cnf;
	}

	// Check if a int tab is full of zeros
	private static boolean isFullZero(int[] I){
		for(int i = 0; i < I.length; i++){
			if(I[i] != 0){
				return false;
			}
		}
		return true;
	}

	private static int[] incrI(int[] I, int[] bases){
		int n = bases.length;
		int i = n-1;
		I[i] += 1;
		// Remainder propagation
		while(i > 0 && I[i] >= bases[i]){
			I[i] = 0;
			I[i-1] += 1;
			i -= 1;
		}
		// We might have overflowed the first "bit"
		if(I[0] >= bases[0]){
			I[0] = 0;
		}

		return I;
	}
	
	 


}