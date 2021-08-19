package it.unirc.LiangScheme.structures;


import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;



public class MasterSecretKey{
	public static String pathPairing="it\\unirc\\LiangScheme\\Params\\a_128_params";
	private static Pairing e;
	private Element galpha;


	public MasterSecretKey(Element galpha) {

		PairingFactory.getInstance().setUsePBCWhenPossible(true);
		e =PairingFactory.getPairing(pathPairing);
		this.galpha=galpha;
	}

	public MasterSecretKey() {
		PairingFactory.getInstance().setUsePBCWhenPossible(true);
		e =PairingFactory.getPairing(pathPairing);
	}



	public Element getGalpha() {
		return galpha;
	}

	public void setGalpha(Element galpha) {
		this.galpha = galpha;
	}

	public byte[] toBytes() {
		byte[]res=null;
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
			outputStream.write( galpha.toBytes() );




			res = outputStream.toByteArray( );}
		catch(Exception e) {}

		return res;	
	}
	public void setFromBytes(byte[]msk) {

		Field<?> G=e.getG1();

		galpha=G.newElement();
		galpha.setFromBytes(Arrays.copyOfRange(msk,0,384));

	}


}
