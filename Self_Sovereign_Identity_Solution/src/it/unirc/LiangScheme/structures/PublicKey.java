package it.unirc.LiangScheme.structures;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

public class PublicKey {
	public static String pathPairing="it\\unirc\\LiangScheme\\Params\\a_128_params";
	private static Pairing e;
	private Element g;
	private Element g1;
	private Element ga;
	private Element egalpha;
	
	





	public PublicKey() {
		PairingFactory.getInstance().setUsePBCWhenPossible(true);
		e =PairingFactory.getPairing(pathPairing);
	}



	public PublicKey(Element g, Element g1, Element ga, Element egalpha) {

		PairingFactory.getInstance().setUsePBCWhenPossible(true);
		e =PairingFactory.getPairing(pathPairing);
        this.g=g;
        this.g1=g1;
        this.ga=ga;
        this.egalpha=egalpha;
   
	}

	
	
	
	
	
	
	
	public Element getG() {
		return g;
	}



	public void setG(Element g) {
		this.g = g;
	}



	public Element getG1() {
		return g1;
	}



	public void setG1(Element g1) {
		this.g1 = g1;
	}



	public Element getGa() {
		return ga;
	}



	public void setGa(Element ga) {
		this.ga = ga;
	}



	public Element getEgalpha() {
		return egalpha;
	}



	public void setEgalpha(Element egalpha) {
		this.egalpha = egalpha;
	}



	public byte[] toBytes() {
		byte[]res=null;
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );

			outputStream.write( g.toBytes() );
			outputStream.write( g1.toBytes() );
			outputStream.write( ga.toBytes() );
			outputStream.write( egalpha.toBytes() );
			

			res = outputStream.toByteArray( );}
		catch(Exception e) {}
		return res;	
	}

	public void setFromBytes(byte[]pk) {

		Field<?> G=e.getG1();
		Field<?> GT=e.getGT();
	

		g=G.newElement();
		g.setFromBytes(Arrays.copyOfRange(pk,0,384));
		
		g1=G.newElement();
		g1.setFromBytes(Arrays.copyOfRange(pk,384,768));
		
		ga=G.newElement();
		ga.setFromBytes(Arrays.copyOfRange(pk,768,1152));
		
		egalpha=GT.newElement();
		egalpha.setFromBytes(Arrays.copyOfRange(pk,1152,1536));
		
		
		
	}

}
