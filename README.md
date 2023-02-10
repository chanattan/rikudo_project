# rikudo_project

Hello,

it is the Algorithms 1 class project wihch is about the Rikudo game made by
Regaud Gaëtan, Sok Chanattan and Timmerman Jules

# Available Algorithm

## SAT

We are using a SAT-Solver (Sat4j) to solve the Rikudo problem. We are converting an instance of the problem to a logical formula. The exact working of the transformation and other remarks about the use of the library can be found in `SAT.pdf`

You can use the SAT algorithm using the function : `satSolve(Graph)`. It will return a boolean signifying whether there is a solution and modify the graph. 

## Backtrack