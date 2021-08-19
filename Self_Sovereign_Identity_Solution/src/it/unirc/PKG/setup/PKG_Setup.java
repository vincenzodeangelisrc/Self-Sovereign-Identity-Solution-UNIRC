package it.unirc.PKG.setup;


import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import javax.swing.JPanel;



import org.apache.commons.codec.binary.Hex;

import it.unirc.LiangScheme.CryptoCPABPRE;

import java.awt.Color;


public class PKG_Setup {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PKG_Setup window = new PKG_Setup();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public PKG_Setup() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(50, 50, 100, 70);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.CYAN);
		frame.getContentPane().add(panel);
		
		JButton btnSetup = new JButton("Setup");
		panel.add(btnSetup);
		btnSetup.setBackground(Color.RED);
		btnSetup.setForeground(Color.BLACK);
		btnSetup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(); 
			    chooser.setCurrentDirectory(new java.io.File("."));
			    chooser.setDialogTitle("Select Folder");
			    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			    chooser.showOpenDialog(btnSetup);
			    String path= chooser.getSelectedFile().toString(); 
			   
			    byte[][]keys=CryptoCPABPRE.Setup();
			    String PK=String.valueOf(Hex.encodeHex(keys[0]));
			    String MSK=String.valueOf(Hex.encodeHex(keys[1]));
			    
				try {
					File f=new File(path+"\\PK.txt");
					if(!f.exists()) {f.createNewFile();}
					PrintWriter out = new PrintWriter(path+"\\PK.txt");
					out.println(PK);				
					out.close();
					
					f=new File(path+"\\MSK.txt");
					if(!f.exists()) {f.createNewFile();}
					out = new PrintWriter(path+"\\MSK.txt");
					out.println(MSK);				
					out.close();
					
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			    
			    
			    
			}
		});

	}

}
