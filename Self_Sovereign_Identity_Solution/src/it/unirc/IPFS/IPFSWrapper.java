package it.unirc.IPFS;
import java.io.IOException;

import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;

public class IPFSWrapper {


	public static String store (byte[] content) {

		String hash = "";
		try {
			IPFS ipfs = new IPFS("/dnsaddr/ipfs.infura.io/tcp/5001/https");
			NamedStreamable.ByteArrayWrapper bytearray = new NamedStreamable.ByteArrayWrapper(content);
			MerkleNode response;
			response = ipfs.add(bytearray).get(0);
			hash=response.hash.toBase58();
		} catch (IOException e) {

			e.printStackTrace();
		}

		return hash;
	}



	public static byte[] retrieve (String hash) {
	
		byte[] content=null;
		try {
			 IPFS ipfs = new IPFS("/dnsaddr/ipfs.infura.io/tcp/5001/https");
			  Multihash multihash = Multihash.fromBase58(hash);
			 content  = ipfs.cat(multihash);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  return content;
	}
}
