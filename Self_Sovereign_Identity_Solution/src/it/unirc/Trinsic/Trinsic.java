package it.unirc.Trinsic;



import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import com.google.gson.Gson;
import com.google.protobuf.InvalidProtocolBufferException;

import trinsic.TrinsicUtilities;
import trinsic.services.TrinsicService;
import trinsic.services.verifiablecredentials.v1.IssueFromTemplateRequest;
import trinsic.services.verifiablecredentials.v1.VerifyProofRequest;


public class Trinsic {
static String trinsicToken="CiVodHRwczovL3RyaW5zaWMuaWQvc2VjdXJpdHkvdjEvb2Jlcm9uEmEKK3Vybjp0cmluc2ljOndhbGxldHM6ejZoc3RMSFZmck1aWVBBRVZXWWlKZTEiMnVybjp0cmluc2ljOmVjb3N5c3RlbXM6cGVkYW50aWMtaGVydHotemViYXRwYWRwb25xGjCBPkJonLuwVwFzVayY1zy7PCf5SdF7aYM61RkzEGTw3ms_KnAXeekqFzW4N2OWUKciAA";
	
public static String issueCredential(String RealIdentity, String Eth) throws InvalidProtocolBufferException, InterruptedException, ExecutionException {
	 var serverConfig = TrinsicUtilities.getTrinsicTrinsicOptions();
	 var trinsic = new TrinsicService(serverConfig);
	 trinsic.setAuthToken(trinsicToken);
	 var valuesMap = new HashMap<String, Object>();
     valuesMap.put("RealIdentity",RealIdentity);
     valuesMap.put("EthereumAddress", Eth);
     var valuesJson = new Gson().toJson(valuesMap);

     var issueResult =
         trinsic
             .credential()
             .issueFromTemplate(
                 IssueFromTemplateRequest.newBuilder()
                     .setTemplateId("urn:template:pedantic-hertz-zebatpadponq:identitycredential")
                     .setValuesJson(valuesJson)
                     .build())
             .get();
//
 String signedCredentialJson = issueResult.getDocumentJson();	
	
	return signedCredentialJson;
}

public static String issueAttributeCredential(String att) throws InvalidProtocolBufferException, InterruptedException, ExecutionException {
	 var serverConfig = TrinsicUtilities.getTrinsicTrinsicOptions();
	 var trinsic = new TrinsicService(serverConfig);
	 trinsic.setAuthToken(trinsicToken);
	 var valuesMap = new HashMap<String, Object>();
    valuesMap.put("att",att);
    var valuesJson = new Gson().toJson(valuesMap);
 
    var issueResult =
        trinsic
            .credential()
            .issueFromTemplate(
                IssueFromTemplateRequest.newBuilder()
                    .setTemplateId("urn:template:pedantic-hertz-zebatpadponq:identitycredential")
                    .setValuesJson(valuesJson)
                    .build())
            .get();
//
String signedCredentialJson = issueResult.getDocumentJson();	
	
	return signedCredentialJson;
}

public static  boolean verifyCredential(String credential) throws InvalidProtocolBufferException, InterruptedException, ExecutionException {

	 var serverConfig = TrinsicUtilities.getTrinsicTrinsicOptions();
	 var trinsic = new TrinsicService(serverConfig);
	 trinsic.setAuthToken(trinsicToken);
	    var verifyProofResponse =
	        trinsic
	            .credential()
	            .verifyProof(
	                VerifyProofRequest.newBuilder().setProofDocumentJson(credential).build())
	            .get();

	 return verifyProofResponse.getIsValid();
}
}
