package it.unirc.LiangScheme;


import java.io.ByteArrayOutputStream;

import java.io.IOException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import it.unirc.LiangScheme.policyManagement.ElementVector;
import it.unirc.LiangScheme.policyManagement.LsssMatrix;
import it.unirc.LiangScheme.policyManagement.Lw14Util;
import it.unirc.LiangScheme.policyManagement.ParseException;
import it.unirc.LiangScheme.structures.CipherText;
import it.unirc.LiangScheme.structures.MasterSecretKey;
import it.unirc.LiangScheme.structures.PublicKey;
import it.unirc.LiangScheme.structures.RKCipherText;
import it.unirc.LiangScheme.structures.ReEncryptionKey;
import it.unirc.LiangScheme.structures.SecretKey;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;


public class CryptoCPABPRE {

	public static String pathPairing="it\\unirc\\LiangScheme\\Params\\a_128_params";


	public static byte[][] Setup() {

		PairingFactory.getInstance().setUsePBCWhenPossible(true);
		Pairing pairing =PairingFactory.getPairing(pathPairing);

		Field<?> G = pairing.getG1();
		Field<?> GT= pairing.getGT();
		Field<?> Zp = pairing.getZr();

		Element g=G.newRandomElement();
		Element g1=G.newRandomElement();
		Element a=Zp.newRandomElement();
		Element alpha=Zp.newRandomElement();

		ElementPowPreProcessing gpp =g.getElementPowPreProcessing();

		Element ga=gpp.powZn(a);
		Element galpha=gpp.powZn(alpha);

		Element egalpha=GT.newElement();


		egalpha=pairing.pairing(g, g).powZn(alpha);

		MasterSecretKey msk=new MasterSecretKey(galpha);
		PublicKey pk=new PublicKey(g,g1,ga,egalpha);
		byte[][] result=new byte[2][];
		result[0]=pk.toBytes();
		result[1]=msk.toBytes();

		return result;
	}	


	public static byte[]encrypt(byte[] message, String policy, byte[] publicKey) {

		if(message.length<16) {
			message=Arrays.copyOf(message,16);
		}

		PairingFactory.getInstance().setUsePBCWhenPossible(true);
		Pairing pairing =PairingFactory.getPairing(pathPairing);

		PublicKey pk=new PublicKey();
		pk.setFromBytes(publicKey);

		Field<?> Zp = pairing.getZr();

		LsssMatrix matrix;
		try {
			matrix = LsssMatrix.createMatrixFromThresholdFormula(policy, pairing);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		byte[] beta = new byte[16];
		new Random().nextBytes(beta);

		byte[]mconcbeta=conc(message, beta);

		Element s=H1(mconcbeta);
		Element[] v= new Element[matrix.getColumns()];
		v[0]=s;
		for(int i=1; i<v.length;i++) {
			v[i]=Zp.newRandomElement();
		}

		ElementVector v1=new ElementVector(v);
		ElementVector lambda=matrix.prod(v1, pairing);




		Element[] r= new Element[matrix.getAttributes()];
		for(int i=0; i<r.length;i++) {

			r[i]=Zp.newRandomElement();


		}
		Element g=pk.getG();
		Element g1=pk.getG1();
		Element ga=pk.getGa();
		Element egalpha=pk.getEgalpha();
		byte[]A1=XOR(mconcbeta, H2(egalpha.duplicate().powZn(s)));
		ElementPowPreProcessing gpp =g.getElementPowPreProcessing();
		ElementPowPreProcessing gapp =ga.getElementPowPreProcessing();
		Element A2=gpp.powZn(s);
		Element A3=g1.duplicate().powZn(s);
		Element[] B= new Element[matrix.getAttributes()];
		Element[] C= new Element[matrix.getAttributes()];

		for(int i=0; i<B.length;i++) {

			B[i]=(gapp.powZn(lambda.get(i))).duplicate().mul(H3(matrix.getAttribute(i).getBytes()).powZn(r[i]).invert());
			C[i]=gpp.powZn(r[i].duplicate());
		}

		byte[]conc=conc(A1,A3.toBytes());
		for(int i=0; i<B.length;i++) {
			conc=conc(conc,B[i].toBytes());
			conc=conc(conc,C[i].toBytes());
		}
		conc=conc(conc,policy.getBytes());
		Element D=H4(conc).powZn(s);





		CipherText ct=new CipherText(policy,A1,A2,A3,B,C,D);
		return ct.toBytes();

	}







	public static byte[] KeyGen(String[]attributes, byte[]publicKey, byte[] masterKey) {
		PairingFactory.getInstance().setUsePBCWhenPossible(true);
		Pairing pairing =PairingFactory.getPairing(pathPairing);


		PublicKey pk=new PublicKey();
		pk.setFromBytes(publicKey);
		MasterSecretKey msk=new MasterSecretKey();
		msk.setFromBytes(masterKey);

		Field<?> Zp = pairing.getZr();

		Element t=Zp.newRandomElement();
		Element K=(pk.getGa().duplicate().powZn(t)).mul(msk.getGalpha());
		Element L=pk.getG().duplicate().powZn(t);

		Element KX[]=new Element[attributes.length];
		for(int i=0; i<KX.length;i++) {
			KX[i]=H3(attributes[i].getBytes()).powZn(t);		
		}
		SecretKey sk= new SecretKey(K,L,KX,attributes);

		return sk.toBytes();

	}


	public static byte[] decrypt(byte[] ciphertext, byte[]secretkey, byte[] publickey) {


		PairingFactory.getInstance().setUsePBCWhenPossible(true);
		Pairing pairing =PairingFactory.getPairing(pathPairing);


		CipherText ct=new CipherText();
		ct.setFromBytes(ciphertext);

		SecretKey sk=new SecretKey();
		sk.setFromBytes(secretkey);

		PublicKey pk=new PublicKey();
		pk.setFromBytes(publickey);



		if(!pairing.pairing(ct.getA2(),pk.getG1()).isEqual(pairing.pairing(pk.getG(),ct.getA3()))) {
			System.out.println("Failed check 1");
			return null;
		}

		byte[]conc=conc(ct.getA1(),ct.getA3().toBytes());
		for(int i=0; i<ct.getB().length;i++) {
			conc=conc(conc,ct.getB()[i].toBytes());
			conc=conc(conc,ct.getC()[i].toBytes());
		}
		conc=conc(conc,ct.getPolicy().getBytes());
		Element z=H4(conc);



		if(!pairing.pairing(ct.getA3(),z).isEqual(pairing.pairing(pk.getG1(),ct.getD()))) {
			System.out.println("Failed check 2");
			return null;
		}







		LsssMatrix matrix=null;
		try {
			matrix = LsssMatrix.createMatrixFromThresholdFormula(ct.getPolicy(), pairing);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;

		}

		Set<String>attributes=new HashSet<String>();
		for(String att: sk.getAttributes()) {
			attributes.add(att);
		}

		if(!Lw14Util.satisfy(matrix, attributes)) {
			System.out.println("Failed check 3");
			return null;
		}





		ElementVector wk=Lw14Util.getWk(matrix, attributes, pairing);

		//Primo termine

		Element B=pairing.getG1().newOneElement();
		for(int i=0; i<wk.getAttributes().size();i++) {

			int index=matrix.getAttributeRowIndex(wk.getAttributes().get(i));
			B.mul(ct.getB()[index].duplicate().powZn(wk.get(i)));		

		}

		Element sx=pairing.pairing(B, pk.getG());


		//Secondo termine

		Element C=pairing.getGT().newOneElement();

		for(int i=0; i<wk.getAttributes().size();i++) {

			int index=matrix.getAttributeRowIndex(wk.getAttributes().get(i));
			C.mul(pairing.pairing(ct.getC()[index].duplicate().invert(), (H3(wk.getAttributes().get(i).getBytes()).powZn(wk.get(i)))));		


		}


		Element dx=pairing.pairing(ct.getA2(),pk.getGa()).mul(C);

		if(!sx.isEqual(dx)) {
			System.out.println("Failed check 4");
			return null;			
		}

		Element den= pairing.getGT().newOneElement();
		for(int i=0; i<wk.getAttributes().size();i++) {
			int index=matrix.getAttributeRowIndex(wk.getAttributes().get(i));
			int index2=Arrays.asList(sk.getAttributes()).indexOf(wk.getAttributes().get(i));
			Element d=(pairing.pairing(ct.getB()[index], sk.getL()).mul(pairing.pairing(ct.getC()[index], sk.getKX()[index2]))).powZn(wk.get(i));
			den.mul(d);	


		}
		Element num= pairing.pairing(ct.getA2(), sk.getK());


		Element Z=num.div(den);
		
		byte[]mconcbeta=XOR(H2(Z), ct.getA1());

		if(!ct.getA3().isEqual(pk.getG1().powZn(H1(mconcbeta)))) {
			return null;
		}

		return Arrays.copyOf(mconcbeta, 16);
	}



	public static byte[] decryptRC(byte[] ciphertext, byte[] secretkey, byte[] publickey) {

		PairingFactory.getInstance().setUsePBCWhenPossible(true);
		Pairing pairing =PairingFactory.getPairing(pathPairing);
		
		RKCipherText rkct=new RKCipherText();
		rkct.setFromBytes(ciphertext);

		SecretKey sk=new SecretKey();
		sk.setFromBytes(secretkey);

		PublicKey pk=new PublicKey();
		pk.setFromBytes(publickey);
		
		
		//Validity of rk4
		CipherText C1 =new CipherText();
		C1.setFromBytes(rkct.getRk4());
		byte[]conc=conc(C1.getA1(),C1.getA2().toBytes());
		for(int i=0; i<C1.getB().length;i++) {
			conc=conc(conc,C1.getB()[i].toBytes());
			conc=conc(conc,C1.getC()[i].toBytes());
		}
		for(int i=0; i<rkct.getAttributesOwner().length;i++) {
			conc=conc(conc,rkct.getAttributesOwner()[i].getBytes());
		}
		conc=conc(conc,C1.getPolicy().getBytes());
		
		if(!pairing.pairing(C1.getA2(), H6(conc)).isEqual(pairing.pairing(pk.getG(),C1.getD()))){
			System.out.println("Failed Check 1");
			return null;
		}
		
		
		LsssMatrix matrix=null;
		try {
			matrix = LsssMatrix.createMatrixFromThresholdFormula(C1.getPolicy(), pairing);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;

		}

		Set<String>attributes=new HashSet<String>();
		for(String att: sk.getAttributes()) {
			attributes.add(att);
		}

		if(!Lw14Util.satisfy(matrix, attributes)) {
			System.out.println("Failed check 2");
			return null;
		}
		
		Element num=pairing.pairing(C1.getA2(), sk.getK());
		
		
		ElementVector wk=Lw14Util.getWk(matrix, attributes, pairing);
		
		Element den= pairing.getGT().newOneElement();
		for(int i=0; i<wk.getAttributes().size();i++) {
			int index=matrix.getAttributeRowIndex(wk.getAttributes().get(i));
			int index2=Arrays.asList(sk.getAttributes()).indexOf(wk.getAttributes().get(i));
			Element d=(pairing.pairing(C1.getB()[index], sk.getL()).mul(pairing.pairing(C1.getC()[index], sk.getKX()[index2]))).powZn(wk.get(i));
			den.mul(d);	
		}
		
		Element Z1=num.div(den);
		byte[] deltaconcbeta1=XOR(H2(Z1),C1.getA1());
		
		byte[]delta=Arrays.copyOfRange(deltaconcbeta1,0,16);
		
		
		
		if(!C1.getA2().equals(pk.getG().duplicate().powZn(H1(deltaconcbeta1)))) {
			System.out.println("Failed check 3");
			return null;
		}
		
		
		byte[] mconcbeta=XOR(H2(rkct.getA4().duplicate().powZn(H5(delta).invert())), rkct.getA1());
		
		
		if(!rkct.getA3().isEqual(pk.getG1().duplicate().powZn(H1(mconcbeta)))) {
			System.out.println("Failed check 4");
			return null;
		}
		
		
	

		byte[]conc1=conc(rkct.getA1(),rkct.getA3().toBytes());
		for(int i=0; i<rkct.getB().length;i++) {
			conc1=conc(conc1,rkct.getB()[i].toBytes());
			conc1=conc(conc1,rkct.getC()[i].toBytes());
		}
		conc1=conc(conc1,rkct.getOldPolicy().getBytes());
		Element D=H4(conc1).powZn(H1(mconcbeta));
		
		if(!D.equals(rkct.getD())) {
			System.out.println("Failed check 5");
			return null;
		}
		
		LsssMatrix matrixOld=null;
		try {
			matrixOld = LsssMatrix.createMatrixFromThresholdFormula(rkct.getOldPolicy(), pairing);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;

		}

	
		
		Set<String>attributesOld=new HashSet<String>();
		for(String att: rkct.getAttributesOwner()) {
			
			attributesOld.add(att);
		}

		if(!Lw14Util.satisfy(matrixOld, attributesOld)) {
			System.out.println("Failed check 6");
			return null;
		}
		
		
		
		
		
		

		
		return Arrays.copyOf(mconcbeta, 16);
		
		
		
		

	}



	public static byte[]RKGen(byte[]secretkey, byte[] publicKey, String newPolicy) {
		PairingFactory.getInstance().setUsePBCWhenPossible(true);
		Pairing pairing =PairingFactory.getPairing(pathPairing);

		PublicKey pk=new PublicKey();
		pk.setFromBytes(publicKey);

		SecretKey sk=new SecretKey();
		sk.setFromBytes(secretkey);

		Field<?> Zp = pairing.getZr();

		LsssMatrix matrix;
		try {
			matrix = LsssMatrix.createMatrixFromThresholdFormula(newPolicy, pairing);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		byte[] beta = new byte[16];
		new Random().nextBytes(beta);

		byte[] delta = new byte[16];
		new Random().nextBytes(delta);

		byte[]deltaconcbeta=conc(delta, beta);

		Element s=H1(deltaconcbeta);
		Element[] v= new Element[matrix.getColumns()];
		v[0]=s;
		for(int i=1; i<v.length;i++) {
			v[i]=Zp.newRandomElement();
		}

		ElementVector v1=new ElementVector(v);
		ElementVector lambda=matrix.prod(v1, pairing);


		Element[] r= new Element[matrix.getAttributes()];
		for(int i=0; i<r.length;i++) {
			r[i]=Zp.newRandomElement();
		}
		Element g=pk.getG();
		Element ga=pk.getGa();
		Element egalpha=pk.getEgalpha();
		byte[]A1=XOR(deltaconcbeta, H2(egalpha.duplicate().powZn(s)));
		ElementPowPreProcessing gpp =g.getElementPowPreProcessing();
		ElementPowPreProcessing gapp =ga.getElementPowPreProcessing();
		Element A2=gpp.powZn(s);

		Element[] B= new Element[matrix.getAttributes()];
		Element[] C= new Element[matrix.getAttributes()];

		for(int i=0; i<B.length;i++) {

			B[i]=(gapp.powZn(lambda.get(i))).duplicate().mul(H3(matrix.getAttribute(i).getBytes()).powZn(r[i]).invert());
			C[i]=gpp.powZn(r[i].duplicate());
		}

		byte[]conc=conc(A1,A2.toBytes());
		for(int i=0; i<B.length;i++) {
			conc=conc(conc,B[i].toBytes());
			conc=conc(conc,C[i].toBytes());
		}
		for(int i=0; i<sk.getAttributes().length;i++) {
			conc=conc(conc,sk.getAttributes()[i].getBytes());
		}
		conc=conc(conc,newPolicy.getBytes());
		Element D=H6(conc).powZn(s);

		CipherText C1= new CipherText(newPolicy, A1, A2, D, B, C, D);

		Element theta= Zp.newRandomElement();
		Element rk1=sk.getK().duplicate().powZn(H5(delta)).mul(pk.getG1().duplicate().powZn(theta));
		Element rk2=gpp.powZn(theta);
		Element rk3=sk.getL().duplicate().powZn(H5(delta));
		Element[] RX= new Element[sk.getAttributes().length];
		for(int i=0; i<sk.getAttributes().length;i++) {
			RX[i]=sk.getKX()[i].duplicate().powZn(H5(delta));
		}
		ReEncryptionKey rk= new ReEncryptionKey(rk1, rk2, rk3, C1.toBytes(), RX, sk.getAttributes());

		return rk.toBytes();



	}
	public static byte[] ReEncrypt(byte[]ReEncKey,byte[]Chipertext, byte[] publickey){

		PairingFactory.getInstance().setUsePBCWhenPossible(true);
		Pairing pairing =PairingFactory.getPairing(pathPairing);


		CipherText ct=new CipherText();
		ct.setFromBytes(Chipertext);
		ReEncryptionKey rk=new ReEncryptionKey();
		rk.setFromBytes(ReEncKey);
		PublicKey pk=new PublicKey();
		pk.setFromBytes(publickey);
		
		

		//First Check
		CipherText C1 =new CipherText();
		C1.setFromBytes(rk.getRk4());
		byte[]conc=conc(C1.getA1(),C1.getA2().toBytes());
		for(int i=0; i<C1.getB().length;i++) {
			conc=conc(conc,C1.getB()[i].toBytes());
			conc=conc(conc,C1.getC()[i].toBytes());
		}
		for(int i=0; i<rk.getAttributes().length;i++) {
			conc=conc(conc,rk.getAttributes()[i].getBytes());
		}
		conc=conc(conc,C1.getPolicy().getBytes());
		
		if(!pairing.pairing(C1.getA2(), H6(conc)).isEqual(pairing.pairing(pk.getG(),C1.getD()))){
			System.out.println("Failed Check 1");
			return null;
		}


		if(!pairing.pairing(ct.getA2(),pk.getG1()).isEqual(pairing.pairing(pk.getG(),ct.getA3()))) {
			System.out.println("Failed check 2");
			return null;
		}

	    conc=conc(ct.getA1(),ct.getA3().toBytes());
		for(int i=0; i<ct.getB().length;i++) {
			conc=conc(conc,ct.getB()[i].toBytes());
			conc=conc(conc,ct.getC()[i].toBytes());
		}
		conc=conc(conc,ct.getPolicy().getBytes());
		Element z=H4(conc);



		if(!pairing.pairing(ct.getA3(),z).isEqual(pairing.pairing(pk.getG1(),ct.getD()))) {
			System.out.println("Failed check 3");
			return null;
		}







		LsssMatrix matrix=null;
		try {
			matrix = LsssMatrix.createMatrixFromThresholdFormula(ct.getPolicy(), pairing);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;

		}

		Set<String>attributes=new HashSet<String>();
		for(String att: rk.getAttributes()) {
			attributes.add(att);
		}

		if(!Lw14Util.satisfy(matrix, attributes)) {
			System.out.println("Failed check 4");
			return null;
		}





		ElementVector wk=Lw14Util.getWk(matrix, attributes, pairing);

		//Primo termine

		Element B=pairing.getG1().newOneElement();
		for(int i=0; i<wk.getAttributes().size();i++) {

			int index=matrix.getAttributeRowIndex(wk.getAttributes().get(i));
			B.mul(ct.getB()[index].duplicate().powZn(wk.get(i)));		

		}

		Element sx=pairing.pairing(B, pk.getG());


		//Secondo termine

		Element C=pairing.getGT().newOneElement();

		for(int i=0; i<wk.getAttributes().size();i++) {

			int index=matrix.getAttributeRowIndex(wk.getAttributes().get(i));
			C.mul(pairing.pairing(ct.getC()[index].duplicate().invert(), (H3(wk.getAttributes().get(i).getBytes()).powZn(wk.get(i)))));		


		}


		Element dx=pairing.pairing(ct.getA2(),pk.getGa()).mul(C);

		if(!sx.isEqual(dx)) {
			System.out.println("Failed check 5");
			return null;			
		}
		

		
		Element den= pairing.getGT().newOneElement();
		for(int i=0; i<wk.getAttributes().size();i++) {
			int index=matrix.getAttributeRowIndex(wk.getAttributes().get(i));
			int index2=Arrays.asList(rk.getAttributes()).indexOf(wk.getAttributes().get(i));
			Element d=(pairing.pairing(ct.getB()[index], rk.getRk3()).mul(pairing.pairing(ct.getC()[index], rk.getRX()[index2]))).powZn(wk.get(i));
			den.mul(d);	


		}
		Element num= pairing.pairing(ct.getA2(), rk.getRk1()).div(pairing.pairing(ct.getA3(), rk.getRk2()));
		Element A4=num.div(den);
		
		RKCipherText rkct= new RKCipherText(ct.getPolicy(),rk.getAttributes(),ct.getA1(),ct.getA3(),A4, ct.getB(), ct.getC(), ct.getD(), rk.getRk4());
		
		
		return rkct.toBytes();	
	}

	public static Element H1 (byte[] input) {
		MessageDigest hasher=null;
		try {
			hasher = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
		byte[] digest = hasher.digest(input);

		PairingFactory.getInstance().setUsePBCWhenPossible(true);
		Pairing pairing =PairingFactory.getPairing(pathPairing);
		Field<?> Zp = pairing.getZr();

		return Zp.newElementFromHash(digest, 0, digest.length);

	}

	public static byte[] H2 (Element input) {

		byte[] elementByte= input.toBytes();

		MessageDigest hasher=null;
		try {
			hasher = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
		return hasher.digest(elementByte);

	}

	public static Element H3 (byte[] input) {
		MessageDigest hasher=null;
		try {
			hasher = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}


		byte[] digest = hasher.digest(input);

		PairingFactory.getInstance().setUsePBCWhenPossible(true);
		Pairing pairing =PairingFactory.getPairing(pathPairing);
		Field<?> G = pairing.getG1();



		return G.newElementFromHash(digest, 0, digest.length);


	}

	public static Element H4 (byte[] input) {
		return H3(input);

	}

	public static Element H5 (byte[] input) {
		return H1(input);

	}

	public static Element H6 (byte[] input) {
		return H3(input);

	}

	public static byte[] XOR( byte[] array1, byte[] array2) {
		int i = 0;
		if(array1.length!=array2.length)
			return null;
		byte[] array3= new byte[array1.length];
		for (byte b : array1)
			array3[i] = (byte) (b ^ array2[i++]);
		return array3;
	}

	public static byte[]conc( byte[] array1, byte[] array2) {

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		try {
			outputStream.write(array1);
			outputStream.write(array2);

		} catch (IOException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}

		return outputStream.toByteArray();


	}






}
