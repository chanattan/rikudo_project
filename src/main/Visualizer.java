package src.main;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Visualizer extends JFrame implements KeyListener {

	private RikudoPane pane;
	public final static String prefix = "[Visualizer] ";
	public int global_x[], global_y[]; //translations depend on the mode
	public float zoom[]; //zoom too
	
    public Visualizer(Graph graph) {
    	setIconImage(new ImageIcon(getClass().getResource("/jgc.jpg")).getImage()); //Should be Rikudo rikudo
    	if (graph == null) {
    		System.err.println(prefix + "Error, tried to visualize a null graph.");
    		System.exit(1);
    	}
        this.setTitle("Rikudo Creator JGC v1.0");
        GraphV g = new GraphV(graph);
        pane = new RikudoPane(this, this, g);
        this.global_x = new int[] {0,0};
        this.global_y = new int[] {0,0};
        this.zoom = new float[] {1f,1f};
        setContentPane(pane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new WindowListener() {
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
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        addKeyListener(this);
    }
    
	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getKeyChar() == 'd' || e.getKeyChar() == 'D') RikudoPane.DEBUG_MODE = !RikudoPane.DEBUG_MODE;
		if (e.getKeyChar() == 'm' || e.getKeyChar() == 'M') {
			RikudoPane.MODE = (RikudoPane.MODE + 1) % 2;
			if (RikudoPane.MODE == 1) {
				CreatorDialog cd = new CreatorDialog(this, pane);
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