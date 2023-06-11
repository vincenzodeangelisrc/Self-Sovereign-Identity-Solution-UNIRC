package it.unirc.LiangScheme.structures;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

public class ReEncryptionKey {
	public static String pathPairing="it\\unirc\\LiangScheme\\Params\\a_128_params";
	private static Pairing e;
	int dim;
	Element rk1;
	Element rk2;
	Element rk3;
	byte[] rk4;
	Element RX[];
	String[] attributes;


	public ReEncryptionKey() {
		PairingFactory.getInstance().setUsePBCWhenPossible(true);
		e =PairingFactory.getPairing(pathPairing);
	}
	public ReEncryptionKey(Element rk1, Element rk2, Element rk3, byte[]rk4, Element[]RX, String[]attributes) {
		PairingFactory.getInstance().setUsePBCWhenPossible(true);
		e =PairingFactory.getPairing(pathPairing);

		this.rk1=rk1;
		this.rk2=rk2;
		this.rk3=rk3;
		this.rk4=rk4;
		this.RX=RX;
		this.attributes=attributes;
		this.dim=attributes.length;


	}


	public int getDim() {
		return dim;
	}
	public void setDim(int dim) {
		this.dim = dim;
	}
	public Element getRk1() {
		return rk1;
	}
	public void setRk1(Element rk1) {
		this.rk1 = rk1;
	}
	public Element getRk2() {
		return rk2;
	}
	public void setRk2(Element rk2) {
		this.rk2 = rk2;
	}
	public Element getRk3() {
		return rk3;
	}
	public void setRk3(Element rk3) {
		this.rk3 = rk3;
	}
	public byte[] getRk4() {
		return rk4;
	}
	public void setRk4(byte[] rk4) {
		this.rk4 = rk4;
	}
	public Element[] getRX() {
		return RX;
	}
	public void setRX(Element[] rX) {
		RX = rX;
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

			outputStream.write((byte)dim);
			for(int i=0;i<dim;i++) {
				outputStream.write( Arrays.copyOf(attributes[i].getBytes(),16));
			}
			for(int i=0;i<dim;i++) {
				outputStream.write(RX[i].toBytes());
			}
			
			outputStream.write(rk1.toBytes());
			outputStream.write(rk2.toBytes());
			outputStream.write(rk3.toBytes());
			outputStream.write(rk4);
		


			res = outputStream.toByteArray( );}
		catch(Exception e) {}
		return res;	
	}

	public void setFromBytes(byte[]rk) {

		Field<?> G=e.getG1();

		dim=Arrays.copyOfRange(rk,0,1)[0]& 0xFF;
		
		attributes= new String[dim];
        for(int i=0; i<dim ;i++) {
           attributes[i]=new String(Arrays.copyOfRange(rk,1+i*16,17+i*16)).trim();
        }
        
    	RX= new Element[dim];
    	int start=1+16*dim;
        for(int i=0; i<dim ;i++) {
           RX[i]=G.newElementFromBytes(Arrays.copyOfRange(rk,start+i*384,start+i*384+384));
        }
        start=1+16*dim+384*dim;
        rk1=G.newElementFromBytes(Arrays.copyOfRange(rk,start,start+384));
        start=1+16*dim+384*dim+384;
        rk2=G.newElementFromBytes(Arrays.copyOfRange(rk,start,start+384));
        start=1+16*dim+384*dim+384+384;
        rk3=G.newElementFromBytes(Arrays.copyOfRange(rk,start,start+384));
        start=1+16*dim+384*dim+384+384+384;
        rk4=(Arrays.copyOfRange(rk,start,rk.length));
		
	}

}
