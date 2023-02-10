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

public class CreatorDialog extends JDialog {
	private final JLabel lblInputTheWidth = new JLabel("Input the width of the hexagon :");
	private JTextField textField;
	private int k = -1;
	
	public CreatorDialog(JFrame f, RikudoPane p) {
		super(f);
		this.setTitle("New puzzle? Close to ignore");
		getContentPane().setBackground(new Color(154, 153, 150));
		getContentPane().setLayout(null);
		lblInputTheWidth.setBounds(0, 0, 450, 19);
		lblInputTheWidth.setFont(new Font("LM Mono Prop 10", Font.BOLD, 14));
		getContentPane().add(lblInputTheWidth);
		
		textField = new JTextField();
		textField.setBounds(10, 27, 204, 19);
		getContentPane().add(textField);
		textField.setColumns(10);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) {
			}
			@Override
			public void windowClosing(WindowEvent e) {
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
		
		JButton btnOk = new JButton("Create");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (textField.getText().trim().isEmpty()) throw new NumberFormatException();
					k = Integer.valueOf(textField.getText());
					p.loadCreator(k);
					dispose();
				} catch (NumberFormatException e1) {
					System.err.println(Visualizer.prefix + "Error, input a valid number for the width of the hexagon.");
				}
			}
		});
		btnOk.setForeground(new Color(222, 221, 218));
		btnOk.setBackground(new Color(94, 92, 100));
		btnOk.setBounds(231, 31, 85, 25);
		getContentPane().add(btnOk);
		
		this.setSize(328, 92);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setModalityType(ModalityType.APPLICATION_MODAL); //to wait for the answer until the dialog is disposed
		this.setVisible(true);
	}
	
	public int getValue() {
		return k;
	}
}
