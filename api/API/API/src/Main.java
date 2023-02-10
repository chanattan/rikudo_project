public class Main {
	
    public static void main(String[] args) {
        System.out.println(Visualizer.prefix + "Starting program.");
        Visualizer v = new Visualizer(TestGraph.testHexa(5));
        System.out.println(Visualizer.prefix + "Showing frame.");
        v.show();
    }
}