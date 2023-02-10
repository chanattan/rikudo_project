package src.main;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class GeneratorDialog extends JDialog {
	private final JLabel lblInputTheWidth = new JLabel("Input the parameters of the map :");
	private JTextField widthField;
	private int w = -1, f = -1, d = -1;
	private JTextField diamsField;
	private JTextField fixedField;
	private JLabel lblWidth_1;
	private JLabel lblMaxFixed;
	private Graph graph;
	
	public GeneratorDialog(JFrame fr, RikudoPane p) {
		super(fr);
		this.setTitle("Generate a map with at least one solution");
		getContentPane().setBackground(new Color(154, 153, 150));
		getContentPane().setLayout(null);
		lblInputTheWidth.setBounds(0, 0, 450, 19);
		lblInputTheWidth.setFont(new Font("LM Mono Prop 10", Font.BOLD, 14));
		getContentPane().add(lblInputTheWidth);
		
		widthField = new JTextField();
		widthField.setBounds(67, 26, 45, 19);
		getContentPane().add(widthField);
		widthField.setColumns(10);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) {
			}
			@Override
			public void windowClosing(WindowEvent e) {
				graph = null;
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
		
		JButton btnOk = new JButton("Generate");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (widthField.getText().trim().isEmpty() ||
							fixedField.getText().trim().isEmpty() ||
							diamsField.getText().trim().isEmpty()) throw new NumberFormatException();
					w = Integer.valueOf(widthField.getText());
					f = Integer.valueOf(fixedField.getText());
					d = Integer.valueOf(diamsField.getText());
					graph = Utils.generateHexaMap(w, f, d);
					dispose();
				} catch (NumberFormatException e1) {
					System.err.println(Visualizer.prefix + "Error, input valid numbers.");
				}
			}
		});
		btnOk.setForeground(new Color(222, 221, 218));
		btnOk.setBackground(new Color(94, 92, 100));
		btnOk.setBounds(255, 66, 120, 25);
		getContentPane().add(btnOk);
		
		diamsField = new JTextField();
		diamsField.setBounds(198, 53, 45, 19);
		getContentPane().add(diamsField);
		diamsField.setColumns(10);
		
		fixedField = new JTextField();
		fixedField.setBounds(258, 26, 45, 19);
		getContentPane().add(fixedField);
		fixedField.setColumns(10);
		
		JLabel lblWidth = new JLabel("max number of diamonds:");
		lblWidth.setFont(new Font("LM Mono Prop 10", Font.BOLD, 14));
		lblWidth.setBounds(10, 51, 198, 22);
		getContentPane().add(lblWidth);
		
		lblWidth_1 = new JLabel("width:");
		lblWidth_1.setFont(new Font("LM Mono Prop 10", Font.BOLD, 14));
		lblWidth_1.setBounds(10, 29, 70, 15);
		getContentPane().add(lblWidth_1);
		
		lblMaxFixed = new JLabel("max fixed cells:");
		lblMaxFixed.setFont(new Font("LM Mono Prop 10", Font.BOLD, 14));
		lblMaxFixed.setBounds(130, 28, 114, 15);
		getContentPane().add(lblMaxFixed);
		
		this.setSize(382, 128);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setModalityType(ModalityType.APPLICATION_MODAL); //to wait for the answer until the dialog is disposed
		this.setVisible(true);
	}
	
	public Graph getGraph() {
		return this.graph;
	}
}
