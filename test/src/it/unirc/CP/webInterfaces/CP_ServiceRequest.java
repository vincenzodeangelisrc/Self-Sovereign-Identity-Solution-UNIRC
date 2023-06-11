package it.unirc.CP.webInterfaces;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Numeric;

import it.unirc.Ethereum.SmartContract;
import it.unirc.LiangScheme.CryptoCPABPRE;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CP_ServiceRequest
 */
@WebServlet("/CP_ServiceRequest")
public class CP_ServiceRequest extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static HashMap<String,String> services= new HashMap<String,String>();
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CP_ServiceRequest() {
		super();
		// TODO Auto-generated constructor stub
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


		String IDServiceU=request.getParameter("idServiceU");


		if(IDServiceU!=null) {
			services.put(IDServiceU, request.getParameter("labelList")+";ID_u");
			request.setAttribute("resp", "x");
			request.getRequestDispatcher("CP_Interfaces/CP_ServiceRequestUser.jsp").forward(request, response);
			return;
		}
		else {

			String EthAddressSP=request.getParameter("Eth_Add");

			boolean found=false;
			try (BufferedReader br = new BufferedReader(new FileReader(this.getServletContext().getRealPath("src/it/unirc/CP/Tables/").replace("\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps", "")+"SP_Association_Table.txt"))) {
				String line;
				while ((line = br.readLine()) != null) {
					String Eth_add=line.split(";")[1];
					if(Eth_add.equals(EthAddressSP)) {
						found=true;
						break;
					}
				}
				br.close();
			}


			if(!found) {
				//Contact CA to verify the mapping between Eth_SP and ID_SP

				FileWriter fw = new FileWriter(this.getServletContext().getRealPath("src/it/unirc/CP/Tables/").replace("\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps", "")+"SP_Association_Table.txt",true);
				BufferedWriter bw = new BufferedWriter(fw); 
				PrintWriter pw = new PrintWriter(bw);

				pw.println("ID_SP;"+EthAddressSP);				
				pw.close();
			}







			String IDServiceSP=request.getParameter("idServiceSP");	
			String IDInvolvedUser=services.get(IDServiceSP).split(";")[services.get(IDServiceSP).split(";").length-1];
			Set<String> labelList = Set.copyOf(Arrays.asList(services.get(IDServiceSP).split(";")));
			services.remove(IDServiceSP);

			String path=this.getServletContext().getRealPath("src/it/unirc/CP/Tables/").replace("\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps", "")+"File_Table.txt";
			List<String> fileTable = new ArrayList<String>(Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8));

			path=this.getServletContext().getRealPath("src/it/unirc/CP/Tables/").replace("\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps", "")+"Policy_Table.txt";
			List<String> policyTable = new ArrayList<String>(Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8));


			ArrayList<String> result= new ArrayList<String>(); 
			for( String s: fileTable) {
				if(labelList.contains(s.split(";")[2]) && s.split(";")[3].equals(IDInvolvedUser)) {

					String IPFSindex=s.split(";")[1];
					String s_f=s.split(";")[0];

					String rkHex="";
					for(String s1: policyTable) {
						if(s1.split(";")[0].equals(IDInvolvedUser)&& s1.split(";")[1].equals(s.split(";")[2])) {
							rkHex=s1.split(";")[2];
						}
					}

					path=this.getServletContext().getRealPath("src/it/unirc/CP/Keys/").replace("\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps", "");
					FileReader f;
					BufferedReader b;
					f=new FileReader(path+"PK.txt");			
					b=new BufferedReader(f);
					String pkS=b.readLine();
					b.close();


					String v_f="";
					try {
						Hex.decodeHex(pkS);
						Hex.decodeHex(rkHex);
						Hex.decodeHex(s_f);

						v_f=String.valueOf(Hex.encodeHex(CryptoCPABPRE.ReEncrypt(Hex.decodeHex(rkHex), Hex.decodeHex(s_f), Hex.decodeHex(pkS))));
					} catch (DecoderException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					
					result.add(v_f+";"+IPFSindex);
				}




			}

			String finalRes="";
			for(String s: result) {
				
				finalRes=finalRes+s+"\n";
			}
			System.out.println(finalRes);
			try {
				//Notarization
				String contractAddr="0x04340909c8b52abe7964a1e0c4d993958c13b937";
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
				byte[] encodedhash = digest.digest((finalRes).getBytes(StandardCharsets.UTF_8));
				TransactionReceipt receipt = n.startNotarization(BigInteger.valueOf(3), EthAddressSP,
						Numeric.hexStringToByteArray(bytesToHex(encodedhash))).send();

				System.out.println(receipt);


			}catch(Exception e) {e.printStackTrace();;}



			ServletOutputStream out = response.getOutputStream();
			response.setContentType("text/plain");
			response.setHeader("Content-Disposition","attachment;filename=DecryptFile.txt");

			byte[]download=finalRes.getBytes();

			out.write(download);
			out.flush();
			out.close();
			
	
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

