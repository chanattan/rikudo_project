package src.main;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Visualizer implements KeyListener {

	private JFrame frame = null;
	private RikudoPane pane;
	public final static String prefix = "[Visualizer] ";
	public int global_x[], global_y[]; //translations depend on the mode
	public float zoom[]; //zoom too
	
    public Visualizer(Graph graph) { //Should be Rikudo rikudo
    	if (graph == null) {
    		System.err.println(prefix + "Error, tried to visualize a null graph.");
    		System.exit(1);
    	}
        this.frame = new JFrame();
        this.frame.setTitle("Rikudo Creator JGC v1.0");
        GraphV g = new GraphV(graph);
        pane = new RikudoPane(this, g);
        this.global_x = new int[] {0,0};
        this.global_y = new int[] {0,0};
        this.zoom = new float[] {1f,1f};
        frame.setContentPane(pane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) {
			}
			@Override
			public void windowClosing(WindowEvent e) {
		        System.out.println(Visualizer.prefix + "Ending program.");				
			}
			@Override
			public void windowClosed(WindowEvent e) {
			}
			@Override
			public void windowIconified(WindowEvent e) {
			}
			@Override
			public void windowDeiconified(WindowEvent e) {
			}
			@Override
			public void windowActivated(WindowEvent e) {
			}
			@Override
			public void windowDeactivated(WindowEvent e) {
			}
        });
        frame.pack();
        frame.setResizable(false);
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
		if (e.getKeyChar() == 'd' || e.getKeyChar() == 'D') RikudoPane.DEBUG_MODE = !RikudoPane.DEBUG_MODE;
		if (e.getKeyChar() == 'm' || e.getKeyChar() == 'M') {
			RikudoPane.MODE = (RikudoPane.MODE + 1) % 2;
			if (RikudoPane.MODE == 1) {
				CreatorDialog cd = new CreatorDialog(frame, pane);
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			global_x[RikudoPane.MODE]+=5*zoom[RikudoPane.MODE];
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			global_x[RikudoPane.MODE]-=5*zoom[RikudoPane.MODE];
		}
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			global_y[RikudoPane.MODE]+=5*zoom[RikudoPane.MODE];
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			global_y[RikudoPane.MODE]-=5*zoom[RikudoPane.MODE];
		}
		if (e.getKeyCode() == KeyEvent.VK_Z) {
			zoom[RikudoPane.MODE]-=.05f;
		}
		if (e.getKeyCode() == KeyEvent.VK_A) {
			zoom[RikudoPane.MODE]+=.05f;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}
}