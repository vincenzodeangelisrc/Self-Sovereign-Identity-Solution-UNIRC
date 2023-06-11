package it.unirc.LiangScheme.structures;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

public class SecretKey {
	private static Pairing e;
	public static String pathPairing="it\\unirc\\LiangScheme\\Params\\a_128_params";
	Element K;
	Element L;
	Element[] KX;
	String[]attributes;


	public SecretKey() {
		PairingFactory.getInstance().setUsePBCWhenPossible(true);
		e =PairingFactory.getPairing(pathPairing);
	}
	public SecretKey(Element K, Element L, Element[] KX, String[] attributes) {

		this.K = K;
		this.L = L;
		this.KX = KX;
		this.attributes=attributes;
	}



	public Element getK() {
		return K;
	}
	public void setK(Element k) {
		K = k;
	}
	public Element getL() {
		return L;
	}
	public void setL(Element l) {
		L = l;
	}
	public Element[] getKX() {
		return KX;
	}
	public void setKX(Element[] kX) {
		KX = kX;
	}



	public String[] getAttributes() {
		return attributes;
	}
	public void setAttributes(String[] attributes) {
		this.attributes = attributes;
	}
	public byte[] toBytes() {
		byte[]res=null;
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );

			outputStream.write( K.toBytes() );
			outputStream.write( L.toBytes() );

			for(int i=0;i<KX.length;i++) {
				outputStream.write(KX[i].toBytes());

			}
			
			for(int i=0;i<attributes.length;i++) {
				outputStream.write( Arrays.copyOf(attributes[i].getBytes(),16));

			}

			res = outputStream.toByteArray( );}
		catch(Exception e) {}
		return res;	
	}

	public void setFromBytes(byte[]dk) {

		Field<?> G=e.getG1();



		K=G.newElement();
		K.setFromBytes(Arrays.copyOfRange(dk,0,384));

		L=G.newElement();
		L.setFromBytes(Arrays.copyOfRange(dk,384,384+384));

		
		KX=new Element[(dk.length-(384*2))/400];
		for(int i=0; i<KX.length;i++) {
			KX[i]=G.newElement();
			KX[i].setFromBytes(Arrays.copyOfRange(dk,384+384+384*i,384+384+384*i+384));
		}

		attributes= new String[KX.length];
		for(int i=0; i<attributes.length;i++) {
			attributes[i]=new String(Arrays.copyOfRange(dk,384+384+384*KX.length+16*i,384+384+384*KX.length+16*i+16)).trim();
			
		}
	}

}



