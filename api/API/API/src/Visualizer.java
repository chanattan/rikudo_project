import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Visualizer implements KeyListener {

	private JFrame frame = null;
	private JPanel pane;
	public final static String prefix = "[Visualizer] ";
	public int global_x = 0, global_y = 0;
	public float zoom=1f;
	
    public Visualizer(Graph graph) { //Should be Rikudo rikudo
    	if (graph == null) {
    		System.err.println(prefix + "Error, tried to visualize a null graph.");
    		System.exit(1);
    	}
        this.frame = new JFrame();
        GraphV g = new GraphV(graph);
        pane = new RikudoPane(this, g);
        frame.setContentPane(pane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.addKeyListener(this);
    }
    
    public void show() {
    	if (this.frame != null) {
    		this.frame.setVisible(true);
    	}
    }

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getKeyChar() == 'd') RikudoPane.DEBUG_MODE = !RikudoPane.DEBUG_MODE;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			global_x+=5*1/zoom;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			global_x-=5*1/zoom;
		}
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			global_y+=5*1/zoom;
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			global_y-=5*1/zoom;
		}
		if (e.getKeyCode() == KeyEvent.VK_Z) {
			zoom-=.05f;
		}
		if (e.getKeyCode() == KeyEvent.VK_A) {
			zoom+=.05f;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}
}