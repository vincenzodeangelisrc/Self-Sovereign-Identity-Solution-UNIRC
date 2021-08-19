package it.unirc.LiangScheme.structures;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

public class CipherText{
	private static Pairing e;
	public static String pathPairing="it\\unirc\\LiangScheme\\Params\\a_128_params";
	private String policy; //max 1024 bytes
	private byte[] A1;
	private Element A2;
	private Element A3;
	private Element[]B;
	private Element[]C;
	private Element D;

	public CipherText() {
		PairingFactory.getInstance().setUsePBCWhenPossible(true);
		e =PairingFactory.getPairing(pathPairing);
	}

	public CipherText(String policy, byte[] A1, Element A2, Element A3, Element[] B, Element [] C, Element D) {
		PairingFactory.getInstance().setUsePBCWhenPossible(true);
		e =PairingFactory.getPairing(pathPairing);	
		this.policy=policy;
		this.A1=A1;
		this.A2=A2;
		this.A3=A3;
		this.B=B;
		this.C=C;
		this.D=D;
	}
	
	
	

	public String getPolicy() {
		return policy;
	}

	public void setPolicy(String policy) {
		this.policy = policy;
	}

	public byte[] getA1() {
		return A1;
	}

	public void setA1(byte[] a1) {
		A1 = a1;
	}

	public Element getA2() {
		return A2;
	}

	public void setA2(Element a2) {
		A2 = a2;
	}

	public Element getA3() {
		return A3;
	}

	public void setA3(Element a3) {
		A3 = a3;
	}

	public Element[] getB() {
		return B;
	}

	public void setB(Element[] b) {
		B = b;
	}

	public Element[] getC() {
		return C;
	}

	public void setC(Element[] c) {
		C = c;
	}

	public Element getD() {
		return D;
	}

	public void setD(Element d) {
		D = d;
	}

	public byte[] toBytes() {
		byte[]res=null;
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
			
			outputStream.write(A1);
			outputStream.write(A2.toBytes());
			outputStream.write(A3.toBytes());
			outputStream.write(Arrays.copyOf(policy.getBytes(),1024));
			
			for(int i=0;i<B.length;i++) {
				outputStream.write( B[i].toBytes() );		
				
			}
			for(int i=0;i<C.length;i++) {
				outputStream.write( C[i].toBytes() );	
				
			}
			outputStream.write( D.toBytes());
			
			res = outputStream.toByteArray( );
		
		}
		catch(Exception e) {}
		return res;	
	}

	public void setFromBytes(byte[]ct) {

		Field<?> G=e.getG1();
		

		A1=(Arrays.copyOfRange(ct,0,32));

		A2=G.newElement();
		A2.setFromBytes(Arrays.copyOfRange(ct,32,416));

		A3=G.newElement();
		A3.setFromBytes(Arrays.copyOfRange(ct,416,800));

		policy= new String(Arrays.copyOfRange(ct,800,1824)).trim();
	
		B=new Element[((ct.length-1824-384)/384)/2];
		C=new Element[B.length];
		for(int i=0;i<B.length;i++) {
			B[i]= G.newElement();
			B[i].setFromBytes(Arrays.copyOfRange(ct,1824+i*384,1824+i*384+384));
	

		}
		for(int i=0;i<C.length;i++) {
			C[i]= G.newElement();
			C[i].setFromBytes(Arrays.copyOfRange(ct,1824+384*B.length+i*384,1824+384*B.length+i*384+384));
			
		}
  
		
		D=G.newElement();
		D.setFromBytes(Arrays.copyOfRange(ct,1824+384*2*B.length,1824+384*2*B.length+384));
		

		
	}


}
