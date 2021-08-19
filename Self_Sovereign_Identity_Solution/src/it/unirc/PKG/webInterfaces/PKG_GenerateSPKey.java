package it.unirc.PKG.webInterfaces;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Hex;

import it.unirc.LiangScheme.CryptoCPABPRE;

/**
 * Servlet implementation class PKG_GenerateSPKey
 */
@WebServlet("/PKG_GenerateSPKey")
public class PKG_GenerateSPKey extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PKG_GenerateSPKey() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] attributes=request.getParameter("attributes").split(";");
		

		//Perform check with Attribute Providers


		
		String path=this.getServletContext().getRealPath("src/it/unirc/PKG/Keys/").replace("\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps", "");
		FileReader f;
		BufferedReader b;
		f=new FileReader(path+"PK.txt");			
		b=new BufferedReader(f);
		String pkS=b.readLine();
		f=new FileReader(path+"MSK.txt");
		b=new BufferedReader(f);
		String mskS=b.readLine();
		b.close();
		try {
		byte[]PK=Hex.decodeHex(pkS);
		byte[]MSK=Hex.decodeHex(mskS);
		

		String privateKey=String.valueOf(Hex.encodeHex(CryptoCPABPRE.KeyGen(attributes,PK,MSK)));
	
		request.setAttribute("privateKey", privateKey);
	    request.getRequestDispatcher("PKG_Interfaces/PKG_SPInterface.jsp").forward(request, response);
		
		}
		catch(Exception e) {}
				
	}

}
