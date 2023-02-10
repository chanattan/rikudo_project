package src.main;
public class Main {
	
    public static void main(String[] args) {
        System.out.println(Visualizer.prefix + "Starting program.");
        Visualizer v = new Visualizer(TestGraph.test1());
        System.out.println(Visualizer.prefix + "Showing frame.");
        v.show();
    }
}