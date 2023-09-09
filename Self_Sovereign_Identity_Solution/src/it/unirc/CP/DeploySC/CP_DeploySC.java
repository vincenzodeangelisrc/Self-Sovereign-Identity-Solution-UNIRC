package it.unirc.CP.DeploySC;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigInteger;

import javax.swing.JButton;

import javax.swing.JFrame;

import javax.swing.JPanel;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.StaticGasProvider;

import it.unirc.Ethereum.SmartContract;

import java.awt.Color;


public class CP_DeploySC {

	private JFrame frame;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CP_DeploySC window = new CP_DeploySC();
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
	public CP_DeploySC() {
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
		
		JButton btnSetup = new JButton("Deploy");
		panel.add(btnSetup);
		btnSetup.setBackground(Color.RED);
		btnSetup.setForeground(Color.BLACK);
		btnSetup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				String endPoint = "https://ropsten.infura.io/v3/b416fd1f93c8450d849e176e06d37c88";		
				FileReader f;
				BufferedReader b;
				try {
					f=new FileReader("src\\it\\unirc\\CP\\Keys\\CP_PrivateEthereumKey.txt");
					b=new BufferedReader(f);
					String privateEthKeyCP=b.readLine();
					Credentials credsCP = Credentials.create(privateEthKeyCP);
					StaticGasProvider SGP=new StaticGasProvider(BigInteger.valueOf(4_100_000_000L),new BigInteger("999999"));
					Web3j web3 = Web3j.build(new HttpService(endPoint));
					SmartContract n = SmartContract.deploy(web3, credsCP, SGP).send();
					System.out.println(n.getContractAddress());
					
					
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}	
				
				
				
			    
			    
			    
			}
		});

	}

}
