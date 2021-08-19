package it.unirc.user.policySetting;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import javax.swing.JLabel;
import javax.swing.JTextField;

import org.apache.commons.codec.binary.Hex;
import org.web3j.abi.TypeEncoder;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Numeric;

import it.unirc.Ethereum.SmartContract;

import it.unirc.LiangScheme.CryptoCPABPRE;


import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;

import java.awt.Color;
import java.awt.Dimension;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.io.PrintWriter;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;

import java.util.List;
import java.util.stream.Stream;
import java.awt.event.ActionEvent;

public class User_SetPolicy {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_3;
	private String  selectedLabel="l1";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					User_SetPolicy window = new User_SetPolicy();
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
	public User_SetPolicy() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 190);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 436, 90);
		panel.setPreferredSize(new Dimension(20, 90));


		panel.setBackground(Color.YELLOW);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		JLabel lblSelectFileTo = new JLabel("Select Policy");
		lblSelectFileTo.setBounds(22, 14, 99, 13);
		panel.add(lblSelectFileTo);

		textField = new JTextField();
		textField.setBounds(126, 11, 96, 19);
		panel.add(textField);
		textField.setColumns(10);

		JButton btnSelect = new JButton("Select");
		btnSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(); 
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle("Select file");

				chooser.showOpenDialog(btnSelect);
				String path= chooser.getSelectedFile().toString(); 
				textField.setText(path);
			}
		});
		btnSelect.setBounds(227, 10, 85, 21);
		panel.add(btnSelect);


		JLabel label_1 = new JLabel("Select Label ");
		label_1.setBounds(22, 50, 99, 13);
		panel.add(label_1);
		int numberLabels=0;

		try (Stream<Path> files = Files.list(Paths.get("src\\it\\unirc\\user\\Keys"))) {
			numberLabels = (int) files.count()-2;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}



		String[] labels = new String[numberLabels];

		for(int i=0; i<labels.length;i++) {
			labels[i]="l"+(i+1);
		}


		JComboBox labelsList = new JComboBox(labels);
		labelsList.setSelectedIndex(0);
		labelsList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {


				selectedLabel=(String) labelsList.getSelectedItem();

			}
		});


		labelsList.setBounds(126, 48, 96, 19);
		panel.add(labelsList);



		JButton btnNewButton = new JButton("Set Policy");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					FileReader f=new FileReader(textField.getText());
					BufferedReader b=new BufferedReader(f);
					String policy=b.readLine();
					
					f=new FileReader("src\\it\\unirc\\user\\Keys\\PK.txt");	
					b=new BufferedReader(f);
					String pkS=b.readLine();
					b.close();
					byte[]PK=Hex.decodeHex(pkS);
					
					System.out.println(selectedLabel);
					f=new FileReader("src\\it\\unirc\\user\\Keys\\PrivateKey"+selectedLabel+".txt");	
					b=new BufferedReader(f);
					String privateKeyS=b.readLine();
					b.close();
					byte[]privateKey=Hex.decodeHex(privateKeyS);
					
					
					byte[]rk=CryptoCPABPRE.RKGen(privateKey, PK, policy);
					String rkHex=String.valueOf(Hex.encodeHex(rk));
					
					
				
					
					
					
					JFileChooser chooser = new JFileChooser(); 
					chooser.setCurrentDirectory(new java.io.File("."));
					chooser.setDialogTitle("Select Folder");
					chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					chooser.showOpenDialog(btnNewButton);
					String path= chooser.getSelectedFile().toString(); 
					java.io.File fileForCP=new java.io.File(path+"\\PolicyFile.txt");
					if(!fileForCP.exists()) {fileForCP.createNewFile();}
					PrintWriter out = new PrintWriter(path+"\\PolicyFile.txt");
					out.println(rkHex);	
					out.println(selectedLabel);	
					
					out.close();



				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}


			}
		});
		btnNewButton.setBounds(325, 64, 100, 21);
		panel.add(btnNewButton);

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.CYAN);
		panel_1.setBounds(0, 91, 436, 65);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);

		JLabel label_2 = new JLabel("Data To Notarize");
		label_2.setBounds(22, 14, 99, 13);
		panel_1.add(label_2);

		textField_3 = new JTextField();
		textField_3.setBounds(126, 11, 96, 19);
		textField_3.setColumns(10);
		panel_1.add(textField_3);

		JButton button_2 = new JButton("Select");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(); 
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle("Select file");

				chooser.showOpenDialog(button_2);
				String path= chooser.getSelectedFile().toString(); 
				textField_3.setText(path);
			}
		});
		button_2.setBounds(227, 10, 83, 21);
		panel_1.add(button_2);



		JButton button_4 = new JButton("Notarize");
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
				BufferedReader reader = new BufferedReader(new FileReader(textField_3.getText()));

				String rk=reader.readLine();
				String label=reader.readLine();
				reader.close();
				
				
				String contractAddr="0x04340909c8b52abe7964a1e0c4d993958c13b937";
				String endPoint = "https://ropsten.infura.io/v3/b416fd1f93c8450d849e176e06d37c88";
				FileReader f=new FileReader("src\\it\\unirc\\user\\Keys\\User_PrivateEthereumKey.txt");
				BufferedReader b=new BufferedReader(f);
				String privateEthKeyUser=b.readLine();
				Credentials credsUser = Credentials.create(privateEthKeyUser);
				StaticGasProvider SGP=new StaticGasProvider(BigInteger.valueOf(4_100_000_000L),new BigInteger("999999"));
				Web3j web3 = Web3j.build(new HttpService(endPoint));
				SmartContract n= SmartContract.load(contractAddr, web3, credsUser,SGP );
				
				MessageDigest digest = MessageDigest.getInstance("SHA-256");
				byte[] encodedhash = digest.digest((label+rk).getBytes(StandardCharsets.UTF_8));
				
				TransactionReceipt receipt = n.confirmNotarization(BigInteger.TWO, Numeric.hexStringToByteArray(bytesToHex(encodedhash))).send();
				
				System.out.println(receipt);
				b.close();
				
				
				
				n= SmartContract.load(contractAddr, web3, credsUser, SGP);
				List<byte[]> list= n.getHashList("0x89960b435c47eDB0C6bC1E707a387EA752273403", BigInteger.TWO).send();



				System.out.println("NOTARIZATION"+": "+list.size()+" files involved"); 
				for(int i=0; i<list.size();i++){ 
					System.out.println(i+".Index: "); 
					System.out.println(TypeEncoder.encode(new Bytes32(list.get(i)))); }
				
				
				}catch(Exception e1) {e1.printStackTrace();}
				
				
			}
		});
		button_4.setBounds(328, 37, 85, 21);
		panel_1.add(button_4);


	}
	private static String bytesToHex(byte[] hash) {
	    StringBuilder hexString = new StringBuilder(2 * hash.length);
	    for (int i = 0; i < hash.length; i++) {
	        String hex = Integer.toHexString(0xff & hash[i]);
	        if(hex.length() == 1) {
	            hexString.append('0');
	        }
	        hexString.append(hex);
	    }
	    return hexString.toString();
	}
}
