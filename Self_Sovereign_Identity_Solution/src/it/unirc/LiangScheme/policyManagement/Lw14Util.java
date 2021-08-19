package it.unirc.LiangScheme.policyManagement;


import it.unisa.dia.gas.jpbc.Element;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import org.mvel2.MVEL;

import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;



public class Lw14Util {
    private enum ElementType { G1, G2, GT, Zr}
    
    
    
    public static ElementVector getWk(LsssMatrix matrix, Set<String> privateKeyAttributes, Pairing p) {
    	 int l = matrix.getAttributes();
         int n = matrix.getColumns();

         // Create an intersection between ciphertext attributes and private key attributes
         List<String> cipherTextAttributes = matrix.getAttributeList();
         Set<String> filteredUserAttributes = privateKeyAttributes;
         filteredUserAttributes.retainAll(cipherTextAttributes);

//     System.out.println("filteredUserAttributes " + Arrays.toString(filteredUserAttributes.toArray()));
//     if (filteredUserAttributes.size() < n) {
//         throw new AbeDecryptionException("Not enough attributes to decrypt");
//     }

         int minSize = Math.min(n, filteredUserAttributes.size());
//     System.out.println("minSize " + minSize);

         ElementVector w_k = null;
         List<String> attributeList = new ArrayList<String>(n);
         List<ElementVector> attributeVectorList = new ArrayList<ElementVector>(n);

         /*
          * The intersection of the attribute set used in the ciphertext and the attribute set in the private key are
          * is created. It is then used to create a power set and sorted in ascending order by set size.
          * The reasoning is that the matrix for smaller sets are faster to compute and less error prone:
          * Sometimes it happens that the matrix is not invertible. That is why many potential sets are tried to
          * create a correct LSSS solution.
          * */
         for(Set<String> set : new SortedPowerSet<String>(filteredUserAttributes)) {
             if (!satisfy(matrix,set)) { //creare metodo che verifica se la policy del chipertext Ë soddisfatta dagli attributi in set
                 continue;
             }
             minSize = set.size();

             Matrix<Element> mat = new Matrix<Element>(minSize, minSize, new ElementField(p.getZr()));
             attributeList.clear();
             attributeVectorList.clear();
             int i = 0;
             for(String attribute : set) {
                 attributeList.add(attribute);
                 ElementVector row = matrix.getAttributeRow(attribute, p.getZr());
                 attributeVectorList.add(row);
                 for(int j = 0; j < minSize /*row.getDimension()*/; j++) {
                     mat.set(j, i, row.get(j));
                 }
                 i++;
             }
        //System.out.println("AL: " + Arrays.toString(attributeList.toArray()));
//         System.out.println("AVL: " + Arrays.toString(attributeVectorList.toArray()));

//         System.out.println("mat\n" + mat);
             try {
                 mat.invert();
             } catch(IllegalStateException e) {
                 System.err.println("FAILED TO INVERT");
                 continue;
             }

//         System.out.println("mat inv\n" + mat);

             ElementVector temp_w_k = new ElementVector(minSize);
             for(int row = 0; row < minSize; row++) {
                 temp_w_k.set(row, mat.get(row, 0));
             }

             // check that w_k is indeed a matching solution for the LSSS...
             ElementVector v = new ElementVector(n, p.getZr().newZeroElement());
             for(int k = 0; k < minSize; k++) {
             /*
              * here k refers to the correct attribute, because attributeVectorList,
              * mat and temp_w_k where created from the same set
              * */
                 v.add(attributeVectorList.get(k).duplicate().mul(temp_w_k.get(k)));
             }

//         System.out.println("test v: " + v);

             boolean verified = v.get(0).isOne();
             for(int k = 1; k < minSize; k++) {
                 verified = verified && v.get(k).isZero();
             }
             if (verified) {
                 w_k = temp_w_k;
                 w_k.setAttributes(attributeList);
                 break;
             }
         }
         if (w_k == null) {
             System.out.println(("Solution for LSSS couldn't be found"));
         }

//     System.out.println("w_k: " + w_k);
         return w_k;
    }
    
    
    
    public static boolean satisfy(LsssMatrix matrix, Set<String> attributes) {
    	String policy=matrix.getPolicy();
    	policy=policy.replace("or", "||");
    	policy=policy.replace("and", "&&");
    	
    	Map<String, Object> context = new java.util.HashMap<String, Object>();
    	for(String att: matrix.getAttributeList()) {
    		if(attributes.contains(att))
    		context.put(att, true);
    		else
    		context.put(att, false);
    		
        	  		
    	}
    	
    	
    	return (boolean) MVEL.eval(policy, context);
    	
    	
    	
    
    }
    
    
    public static ElementVector orderWk(ElementVector wk, List<String> sharesAttributes, Pairing p) {
       Field Zr=p.getZr();
    	Element[] wk1= new Element[sharesAttributes.size()];
    	for(int i=0; i<wk1.length;i++) {
    		wk1[i]=Zr.newZeroElement();
    	}
    	int i=0;
    	for(String attribute: wk.getAttributes()) {
    		
    		int index=sharesAttributes.indexOf(attribute);
    		wk1[index]=Zr.newElement(wk.get(i));
    	    i++;
    	}
    	return new ElementVector(wk1);
     	
    
    }
    
    
    
    
    

    private static Element elementFromString(ElementType et, String s, Pairing p) {
        try {
            MessageDigest hasher = MessageDigest.getInstance("SHA-1");
            byte[] digest = hasher.digest(s.getBytes());
            Field field;
            switch (et){
                case G1:
                    field = p.getG1();
                    break;
                case G2:
                    field = p.getG2();
                    break;
                case GT:
                    field = p.getGT();
                    break;
                case Zr:
                    field = p.getZr();
                    break;
                default:
                    return null;
            }
            return field.newElementFromHash(digest, 0, digest.length);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    public static Element elementG1FromString(String s, Pairing p) {
        return elementFromString(ElementType.G1, s, p);
    }

    public static Element elementG2FromString(String s, Pairing p) {
        return elementFromString(ElementType.G2, s, p);
    }

    public static Element elementGtFromString(String s, Pairing p) {
        return elementFromString(ElementType.GT, s, p);
    }

    public static Element elementZrFromString(String s, Pairing p) {
        return elementFromString(ElementType.Zr, s, p);
    }



    public static void writeArray(byte[] array, DataOutputStream stream) throws IOException {
        if (array == null || array.length == 0) {
            stream.writeInt(0);
        } else {
            stream.writeInt(array.length);
            stream.write(array);
        }
    }

   
    public static byte[] readByteArray(DataInputStream stream) throws IOException {
        int len = stream.readInt();
        byte[] result =  new byte[len];
        stream.read(result);
        return result;
    }


    public static String getSpaces(int number) {
        StringBuilder sb = new StringBuilder(number);
        for(int i = 0; i < number; i++) {
            sb.append(" ");
        }
        return sb.toString();
    }

//    private static AbePrivateKey mockPrivateKey(Set<String> attributes, AbePublicKey publicKey) {
//        ArrayList<Lw14PrivateKeyComponent> components = new ArrayList<Lw14PrivateKeyComponent>();
//        for (String s : attributes) {
//            components.add(new Lw14PrivateKeyComponent(s, elementZrFromString(s, publicKey), null, null,null, null));
//        }
//        return new AbePrivateKey(null, null, null, null, null, components, publicKey);
//    }
//
//    public static Lw14PolicyAbstractNode getPolicyTree(String policy, AbePublicKey publicKey) throws ParseException {
//        String postFixPolicy = PolicyParsing.parsePolicy(policy);
//        return Lw14PolicyAbstractNode.parsePolicy(postFixPolicy, publicKey);
//    }
//
//    public static boolean satisfies(String policy, AbePrivateKey privateKey) throws ParseException {
//        return satisfies(getPolicyTree(policy, privateKey.getPublicKey()), privateKey);
//    }
//
//    public static boolean satisfies(Lw14PolicyAbstractNode policy, AbePrivateKey privateKey) {
//        return policy.checkSatisfy(privateKey);
//    }
//
//    /**
//     * Generates a parse tree and uses it to check whether the passed set of attributes satisfies the access tree.
//     *
//     * @param policy    policy
//     * @param set       Attribute string set
//     * @return  Set satisfies the policy
//     * @throws ParseException Policy parsing failed
//     */
//    public static boolean satisfies(String policy, Set<String> set, AbePublicKey publicKey) throws ParseException {
//        return satisfies(policy, mockPrivateKey(set, publicKey));
//    }
//
//    /**
//     * Uses the parse tree to check whether the passed set of attributes satisfies the access tree.
//     *
//     * @param policyTree    Root node of the tree
//     * @param set           Attribute string set
//     * @param publicKey     Public key for some internal elements
//     * @return  Set satisfies the tree
//     */
//    public static boolean satisfies(Lw14PolicyAbstractNode policyTree, Set<String> set, AbePublicKey publicKey) {
//        return satisfies(policyTree, mockPrivateKey(set, publicKey));
//    }

    /**
     * Computes the power set of the original set, but limits the items to a length or <code>length</code>.
     * @param originalSet    Original items as a set
     * @param length         Intended length of the items of the power set
     * @param <T>            Item type
     * @return filtered power set
     */
    public static <T> Set<Set<T>> powerSet(Set<T> originalSet, int length) {
        if (originalSet.size() < length) {
            throw new RuntimeException("Filter length cannot be larger than the set size");
        }
        Set<Set<T>> sets = powerSet(originalSet);
        Set<Set<T>> removeSet = new HashSet<Set<T>>();
        for (Set<T> set : sets) {
            if (set.size() != length) {
                removeSet.add(set);
            }
        }
        sets.removeAll(removeSet);
        return sets;
    }

    /**
     * Computes the power set of the given set.
     * Copied from <a href="http://stackoverflow.com/a/1670871">StackOverflow by Jo√£o Silva</a>.
     * @param originalSet    Set to build the power set from
     * @param <T>            Set element type
     * @return  Power set of {@code T}
     */
    public static <T> Set<Set<T>> powerSet(Set<T> originalSet) {
        Set<Set<T>> sets = new HashSet<Set<T>>();
        if (originalSet.isEmpty()) {
            sets.add(new HashSet<T>());
            return sets;
        }
        List<T> list = new ArrayList<T>(originalSet);
        T head = list.get(0);
        Set<T> rest = new HashSet<T>(list.subList(1, list.size()));
        for (Set<T> set : powerSet(rest)) {
            Set<T> newSet = new HashSet<T>();
            newSet.add(head);
            newSet.addAll(set);
            sets.add(newSet);
            sets.add(set);
        }
        return sets;
    }

    public static String toString(Element[] array, int initialIndent, int additionalIndent){
        return toString(Arrays.asList(array), initialIndent, additionalIndent);
    }

    public static String toString(Collection<Element> collection, int initialIndent, int additionalIndent){
        StringBuilder sb;

        String iiString = getSpaces(initialIndent);
        String aiString = getSpaces(additionalIndent);

        sb = new StringBuilder();
        sb.append(iiString);
        sb.append("[\n");
        int i = 0;
        for(Element el : collection) {
            if (i != 0) {
                sb.append(",\n");
            }
            sb.append(iiString);
            sb.append(aiString);
            sb.append('"');
            sb.append(el);
            sb.append('"');
            i++;
        }
        sb.append("\n");
        sb.append(iiString);
        sb.append("]");
        return sb.toString();
    }

    /**
     * Returns one pascal row as array. For example would be
     * <code>getPascalRow(4) == { 1, 3, 3, 1}</code>
     * @param n    Pascal row index
     * @return  Row as long array
     */
    public static long[] getPascalRow(int n) {
        long[] row = new long[n];
        row[0] = 1L;

        for(int col = 1; col < n; col++) {
            row[col] = row[col - 1] * (n - col) / col;
        }

        return row;
    }

    /**
     * It computes lexicographically the next permutation of the bits in the given number.
     *
     * For example 3 = 0b000011<br>
     * - next: 0b000101 (5)<br>
     * - next: 0b000110 (6)<br>
     * - next: 0b001001 (9)<br>
     * - next: 0b001010 (10)<br>
     * - next: 0b001100 (12)<br>
     * - next: 0b010001 (17)<br>
     *
     * Copied from <a href="http://graphics.stanford.edu/~seander/bithacks.html#NextBitPermutation">Bit Twiddling Hacks: Compute the lexicographically next bit permutation</a>
     * @param v    Source number
     * @return  Bit-twiddled number
     */
    public static long getNextLexicographicalPermutation(long v) {
        long t = (v | (v - 1)) + 1;
        return t | ((((t & -t) / (v & -v)) >> 1) - 1);
    }

    /**
     * It computes lexicographically the next permutation of the bits in the given number.
     *
     * For example 3 = 0b000011<br>
     * - next: 0b000101 (5)<br>
     * - next: 0b000110 (6)<br>
     * - next: 0b001001 (9)<br>
     * - next: 0b001010 (10)<br>
     * - next: 0b001100 (12)<br>
     * - next: 0b010001 (17)<br>
     *
     * Copied from <a href="http://graphics.stanford.edu/~seander/bithacks.html#NextBitPermutation">Bit Twiddling Hacks: Compute the lexicographically next bit permutation</a>
     * @param v    Source number
     * @return  Bit-twiddled number
     */
    public static BigInteger getNextLexicographicalPermutation(BigInteger v) {
        BigInteger t = v.or(v.subtract(BigInteger.ONE)).add(BigInteger.ONE);
        return t.and(t.negate()).divide(v.and(v.negate())).shiftRight(1).subtract(BigInteger.ONE).or(t);
    }
}
