package it.unirc.CP.webInterfaces;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Numeric;

import it.unirc.Ethereum.SmartContract;
import it.unirc.Trinsic.Trinsic;

/**
 * Servlet implementation class FileStorage
 */
@WebServlet("/CP_FileStorage")
@MultipartConfig
public class CP_FileStorage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CP_FileStorage() {
		super();
		// TODO Auto-generated constructor stub
	}



	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			
			Part filePart1 = request.getPart("credential");
			String credential=new String(filePart1.getInputStream().readAllBytes());
			try {
				if(!Trinsic.verifyCredential(credential)) {return;}
			} catch (Exception e) {
			}

			
			
			
			
			String EthAddress=request.getParameter("EthAddress");
			
			boolean found=false;
			try (BufferedReader br = new BufferedReader(new FileReader(this.getServletContext().getRealPath("src/it/unirc/CP/Tables/").replace("\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps", "")+"User_Association_Table.txt"))) {
			    String line;
			    while ((line = br.readLine()) != null) {
			       String Eth_add=line.split(";")[1];
			       if(Eth_add.equals(EthAddress)) {
			    	   found=true;
			    	   break;
			       }
			    }
			    br.close();
			}
			
			
			if(!found) {
			//Contact CA to verify the mapping between Eth_u and ID_u
			
				FileWriter fw = new FileWriter(this.getServletContext().getRealPath("src/it/unirc/CP/Tables/").replace("\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps", "")+"User_Association_Table.txt",true);
				BufferedWriter bw = new BufferedWriter(fw); 
				PrintWriter pw = new PrintWriter(bw);

				pw.println("ID_u;"+EthAddress);				
				pw.close();
			}
			
			
			Part filePart=request.getPart("StorageFile");

			InputStream in=filePart.getInputStream();


			BufferedReader reader = new BufferedReader(new InputStreamReader(in));

			String sf=reader.readLine();
			String IPFSindex=reader.readLine();
			String label=reader.readLine();
			in.close();

			
			FileWriter fw = new FileWriter(this.getServletContext().getRealPath("src/it/unirc/CP/Tables/").replace("\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps", "")+"File_Table.txt",true);
			BufferedWriter bw = new BufferedWriter(fw); 
			PrintWriter pw = new PrintWriter(bw);

			pw.println(sf+";"+IPFSindex+";"+label+";"+"ID_u");			
			pw.close();
			
		
			
			//Notarization
			String contractAddr="0xdbc89db0f94815f72237f87dc656a0fd01680da4";
			String endPoint = "https://ropsten.infura.io/v3/b416fd1f93c8450d849e176e06d37c88";
			FileReader f=new FileReader(this.getServletContext().getRealPath("src/it/unirc/CP/Keys/CP_PrivateEthereumKey.txt").replace("\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps", ""));
			BufferedReader b=new BufferedReader(f);
			String privateEthKeyCP=b.readLine();
			Credentials credsCP = Credentials.create(privateEthKeyCP);
			StaticGasProvider SGP=new StaticGasProvider(BigInteger.valueOf(4_100_000_000L),new BigInteger("999999"));
			Web3j web3 = Web3j.build(new HttpService(endPoint));
			SmartContract n= SmartContract.load(contractAddr, web3, credsCP,SGP );
			b.close();
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] encodedhash = digest.digest((sf+IPFSindex+label).getBytes(StandardCharsets.UTF_8));
			
			//TransactionReceipt receipt = n.startNotarization(BigInteger.ONE, EthAddress,
					//Numeric.hexStringToByteArray(bytesToHex(encodedhash))).send();
			
			//System.out.println(receipt);
			
		
			
			request.setAttribute("SCAddress", contractAddr);
		    request.getRequestDispatcher("CP_Interfaces/CP_FileStorage.jsp").forward(request, response);
			

		} catch(Exception e) {
			e.printStackTrace();    
		}

	

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
