package it.unirc.LiangScheme.structures;

import java.io.ByteArrayOutputStream;


import java.util.Arrays;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;



public class RKCipherText {
	private static Pairing e;
	public static String pathPairing="it\\unirc\\LiangScheme\\Params\\a_128_params";
	private String OldPolicy; //max length 1024 bytes
	private String[] attributesOwner;
	private byte[] A1;
	private Element A3;
	private Element A4;
	private Element[]B;
	private Element[]C;
	private Element D;
	private byte[] rk4;
	int dimAttr; //max 128 attributes
	int dimElement; //max 128 elements

	public RKCipherText() {
		PairingFactory.getInstance().setUsePBCWhenPossible(true);
		e =PairingFactory.getPairing(pathPairing);
	}

	public RKCipherText(String policy,String[] attributesOwner, byte[] A1, Element A3, Element A4, Element[] B, Element [] C, Element D, byte[]rk4) {
		PairingFactory.getInstance().setUsePBCWhenPossible(true);
		e =PairingFactory.getPairing(pathPairing);	
		this.OldPolicy=policy;
		this.attributesOwner=attributesOwner;
		this.A1=A1;
		this.A3=A3;
		this.A4=A4;
		this.B=B;
		this.C=C;
		this.D=D;
		this.dimAttr=attributesOwner.length;
		this.dimElement=B.length;
		this.rk4=rk4;
	}




	public String getOldPolicy() {
		return OldPolicy;
	}

	public void setOldPolicy(String oldPolicy) {
		OldPolicy = oldPolicy;
	}

	public String[] getAttributesOwner() {
		return attributesOwner;
	}

	public void setAttributesOwner(String[] attributesOwner) {
		this.attributesOwner = attributesOwner;
	}

	public byte[] getA1() {
		return A1;
	}

	public void setA1(byte[] a1) {
		A1 = a1;
	}

	public Element getA3() {
		return A3;
	}

	public void setA3(Element a3) {
		A3 = a3;
	}

	public Element getA4() {
		return A4;
	}

	public void setA4(Element a4) {
		A4 = a4;
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

	public byte[] getRk4() {
		return rk4;
	}

	public void setRk4(byte[] rk4) {
		this.rk4 = rk4;
	}

	public byte[] toBytes() {
		byte[]res=null;
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );

			outputStream.write((byte)dimAttr);
			outputStream.write((byte)dimElement);
			outputStream.write(Arrays.copyOf(OldPolicy.getBytes(),1024));

			for(int i=0;i<dimAttr;i++) {
				outputStream.write( Arrays.copyOf(attributesOwner[i].getBytes(),16));
			}
			outputStream.write(A1);
			outputStream.write(A3.toBytes());
			outputStream.write(A4.toBytes());


			for(int i=0;i<dimElement;i++) {
				outputStream.write(B[i].toBytes());
			}
			for(int i=0;i<dimElement;i++) {
				outputStream.write(C[i].toBytes());
			}

			outputStream.write(D.toBytes());

			outputStream.write(rk4);



			res = outputStream.toByteArray( );}
		catch(Exception e) {
			e.printStackTrace();
			
		}
		return res;	
	}

	public void setFromBytes(byte[]rkct) {

		Field<?> G=e.getG1();
		Field<?> GT=e.getGT();


		dimAttr=Arrays.copyOfRange(rkct,0,1)[0]& 0xFF;
		dimElement=Arrays.copyOfRange(rkct,1,2)[0]& 0xFF;
		OldPolicy= new String(Arrays.copyOfRange(rkct,2,1026)).trim();

		attributesOwner= new String[dimAttr];
		for(int i=0;i<dimAttr;i++) {
			attributesOwner[i]=new String(Arrays.copyOfRange(rkct,1026+i*16,1026+i*16+16)).trim();
		}
		
		int start=1026+dimAttr*16; int end=start+32;
		A1=(Arrays.copyOfRange(rkct,start,end));
		
		start=end; end=start+384;
		A3=G.newElement();
		A3.setFromBytes(Arrays.copyOfRange(rkct,start,end));

		
		start=end; end=start+384;
		A4=GT.newElement();
		A4.setFromBytes(Arrays.copyOfRange(rkct,start,end));


		B=new Element[dimElement];
		
		for(int i=0;i<B.length;i++) {
			start=end; end=start+384;
			B[i]= G.newElement();
			B[i].setFromBytes(Arrays.copyOfRange(rkct,start,end));


		}
		C=new Element[B.length];
		for(int i=0;i<C.length;i++) {
			start=end; end=start+384;
			C[i]= G.newElement();
			C[i].setFromBytes(Arrays.copyOfRange(rkct,start,end));


		}

		start=end; end=start+384;
		D=G.newElement();
		D.setFromBytes(Arrays.copyOfRange(rkct,start,end));

		start=end; end=rkct.length;
		
		rk4=Arrays.copyOfRange(rkct,start,end);
	}


}
