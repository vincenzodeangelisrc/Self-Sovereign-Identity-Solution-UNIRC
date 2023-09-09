package it.unirc;



import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import com.google.gson.Gson;
import com.google.protobuf.InvalidProtocolBufferException;

import trinsic.TrinsicUtilities;
import trinsic.services.TrinsicService;
import trinsic.services.provider.v1.CreateEcosystemRequest;
//import trinsic.services.universalwallet.v1.CreateWalletRequest;
import trinsic.services.universalwallet.v1.CreateWalletRequest;
import trinsic.services.verifiablecredentials.templates.v1.CreateCredentialTemplateRequest;
import trinsic.services.verifiablecredentials.templates.v1.DeleteCredentialTemplateRequest;
import trinsic.services.verifiablecredentials.templates.v1.GetCredentialTemplateRequest;
import trinsic.services.verifiablecredentials.templates.v1.SearchCredentialTemplatesRequest;
import trinsic.services.verifiablecredentials.templates.v1.TemplateField;
import trinsic.services.verifiablecredentials.v1.CreateProofRequest;
import trinsic.services.verifiablecredentials.v1.IssueFromTemplateRequest;
import trinsic.services.verifiablecredentials.v1.RevealTemplateAttributes;
import trinsic.services.verifiablecredentials.v1.SendRequest;



public class Test {
public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
	 var serverConfig = TrinsicUtilities.getTrinsicTrinsicOptions();
	 var trinsic = new TrinsicService(serverConfig);
	 
	 trinsic.setAuthToken("CiVodHRwczovL3RyaW5zaWMuaWQvc2VjdXJpdHkvdjEvb2Jlcm9uEmEKK3Vybjp0cmluc2ljOndhbGxldHM6ejZoc3RMSFZmck1aWVBBRVZXWWlKZTEiMnVybjp0cmluc2ljOmVjb3N5c3RlbXM6cGVkYW50aWMtaGVydHotemViYXRwYWRwb25xGjCBPkJonLuwVwFzVayY1zy7PCf5SdF7aYM61RkzEGTw3ms_KnAXeekqFzW4N2OWUKciAA");
	 

	   
//	    var issuerVerifier =
//	            trinsic
//	                .wallet()
//	                .createWallet(CreateWalletRequest.newBuilder().setEcosystemId(ecosystemId).build())
//	                .get();
//	       var holder =
//	            trinsic
//	                .wallet()
//	                .createWallet(CreateWalletRequest.newBuilder().setEcosystemId(ecosystemId).build())
//	                .get();

	//        trinsic.setAuthToken(issuerVerifier.getAuthToken());
	 
	 var searchResponse =
			    trinsic
			        .template()
			        .search(
			            SearchCredentialTemplatesRequest.newBuilder().setQuery("SELECT * FROM c")
			                .build())
			        .get();

	
//	        var template =
//	            trinsic
//	                .template()
//	                .create(
//	                    CreateCredentialTemplateRequest.newBuilder()
//	                        .setName("SecondTemplateTest")
//	                        .putFields(
//	                            "firstName", TemplateField.newBuilder().setTitle("First Name").build())
//	                        .putFields("lastName", TemplateField.newBuilder().setTitle("Last Name").build())
//	                        .build())
//	                .get();
//
	        var valuesMap = new HashMap<String, Object>();
	        valuesMap.put("RealIdentity", "myRealIdentity1");
	        valuesMap.put("EthereumAddress", "myEthereumAddress1");
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
        var signedCredentialJson = issueResult.getDocumentJson();
//
//	     
	        System.out.println("Credential:"+signedCredentialJson);
	     
	        try {
	            // sendCredential() {
	            trinsic
	                .credential()
	                .send(
	                    SendRequest.newBuilder()
	                        .setDocumentJson(signedCredentialJson)
	                        .setEmail("vincenzo.deangelisrc@gmail.com")
	                        .build());
	            // }
	          } catch (RuntimeException re) {
	          	System.out.println(re);
	            // This is okay, we don't expect that account to exist.
	          }
	        
//	        trinsic.setAuthToken(holder.getAuthToken());
//	        // createProof() {
//	        var createProofResponse =
//	            trinsic
//	                .credential()
//	                .createProof(
//	                    CreateProofRequest.newBuilder().setDocumentJson(signedCredentialJson).build())
//	                .get();
//
//	        var credentialProof = createProofResponse.getProofDocumentJson();

	        // }

	       
	   
//String jars="C:\\Users\\vinci\\.gradle\\caches\\modules-2\\files-2.1\\org.jetbrains.kotlinx\\kotlinx-coroutines-core-jvm\\1.6.4\\2c997cd1c0ef33f3e751d3831929aeff1390cb30\\kotlinx-coroutines-core-jvm-1.6.4.jar\r\n" + 
//		"C:\\Users\\vinci\\.gradle\\caches\\modules-2\\files-2.1\\org.jetbrains.kotlin\\kotlin-stdlib-jdk8\\1.7.10\\d70d7d2c56371f7aa18f32e984e3e2e998fe9081\\kotlin-stdlib-jdk8-1.7.10.jar\r\n" + 
//		"C:\\Users\\vinci\\.gradle\\caches\\modules-2\\files-2.1\\org.apache.tomcat\\annotations-api\\6.0.53\\94cfa8a6ebc6b36e966bff433d4eeebf933f3f41\\annotations-api-6.0.53.jar\r\n" + 
//		"C:\\Users\\vinci\\.gradle\\caches\\modules-2\\files-2.1\\com.google.protobuf\\protobuf-kotlin\\3.21.7\\43174657fe2e8afb29444c7ef5142882adf9adae\\protobuf-kotlin-3.21.7.jar\r\n" + 
//		"C:\\Users\\vinci\\.gradle\\caches\\modules-2\\files-2.1\\org.jetbrains.kotlin\\kotlin-stdlib-jdk7\\1.7.10\\1ef73fee66f45d52c67e2aca12fd945dbe0659bf\\kotlin-stdlib-jdk7-1.7.10.jar\r\n" + 
//		"C:\\Users\\vinci\\.gradle\\caches\\modules-2\\files-2.1\\org.jetbrains.kotlin\\kotlin-stdlib\\1.7.10\\d2abf9e77736acc4450dc4a3f707fa2c10f5099d\\kotlin-stdlib-1.7.10.jar\r\n" + 
//		"C:\\Users\\vinci\\.gradle\\caches\\modules-2\\files-2.1\\org.jetbrains\\annotations\\23.0.0\\8cc20c07506ec18e0834947b84a864bfc094484e\\annotations-23.0.0.jar\r\n" + 
//		"C:\\Users\\vinci\\.gradle\\caches\\modules-2\\files-2.1\\com.google.protobuf\\protobuf-java-util\\3.21.7\\b379ede46ca813491f8588d1ab981cbf627e7dcc\\protobuf-java-util-3.21.7.jar\r\n" + 
//		"C:\\Users\\vinci\\.gradle\\caches\\modules-2\\files-2.1\\io.grpc\\grpc-protobuf\\1.49.2\\8cb089f93f5a2f737271d465eeb9afeeec95feea\\grpc-protobuf-1.49.2.jar\r\n" + 
//		"C:\\Users\\vinci\\.gradle\\caches\\modules-2\\files-2.1\\com.google.api.grpc\\proto-google-common-protos\\2.9.0\\e4ada41aaaf6ecdedf132f44251d0d50813f7f90\\proto-google-common-protos-2.9.0.jar\r\n" + 
//		"C:\\Users\\vinci\\.gradle\\caches\\modules-2\\files-2.1\\com.google.protobuf\\protobuf-java\\3.21.7\\96cfc7147192f1de72c3d7d06972155ffb7d180c\\protobuf-java-3.21.7.jar\r\n" + 
//		"C:\\Users\\vinci\\.gradle\\caches\\modules-2\\files-2.1\\net.java.dev.jna\\jna\\5.12.1\\b1e93a735caea94f503e95e6fe79bf9cdc1e985d\\jna-5.12.1.jar\r\n" + 
//		"C:\\Users\\vinci\\.gradle\\caches\\modules-2\\files-2.1\\io.grpc\\grpc-kotlin-stub\\1.3.0\\4d0102df09897f3dc6144ea9d8a9bd9a8a622503\\grpc-kotlin-stub-1.3.0.jar\r\n" + 
//		"C:\\Users\\vinci\\.gradle\\caches\\modules-2\\files-2.1\\io.leonard\\base58\\0.0.2\\b85c9436f27f99305a39c115e338eb49e1fb6b78\\base58-0.0.2.jar\r\n" + 
//		"C:\\Users\\vinci\\.gradle\\caches\\modules-2\\files-2.1\\io.grpc\\grpc-netty-shaded\\1.49.2\\b039a2a0d771f071b57bae72e65222356693388e\\grpc-netty-shaded-1.49.2.jar\r\n" + 
//		"C:\\Users\\vinci\\.gradle\\caches\\modules-2\\files-2.1\\io.grpc\\grpc-stub\\1.49.2\\88790125429783dcd5e3df0610481fc8e34434f3\\grpc-stub-1.49.2.jar\r\n" + 
//		"C:\\Users\\vinci\\.gradle\\caches\\modules-2\\files-2.1\\com.google.guava\\guava\\31.1-android\\9222c47cc3ae890f07f7c961bbb3cb69050fe4aa\\guava-31.1-android.jar\r\n" + 
//		"C:\\Users\\vinci\\.gradle\\caches\\modules-2\\files-2.1\\io.grpc\\grpc-protobuf-lite\\1.49.2\\ab47ca33aebd0e3a6ae4324d76cb050f8ac826aa\\grpc-protobuf-lite-1.49.2.jar\r\n" + 
//		"C:\\Users\\vinci\\.gradle\\caches\\modules-2\\files-2.1\\io.grpc\\grpc-core\\1.49.2\\da8da3a79b0c94f0c95dcb826c8cbf32397af4b7\\grpc-core-1.49.2.jar\r\n" + 
//		"C:\\Users\\vinci\\.gradle\\caches\\modules-2\\files-2.1\\io.grpc\\grpc-api\\1.49.2\\30bbb785af1b2e589652c634022f3ef65df86895\\grpc-api-1.49.2.jar\r\n" + 
//		"C:\\Users\\vinci\\.gradle\\caches\\modules-2\\files-2.1\\com.google.errorprone\\error_prone_annotations\\2.14.0\\9f01b3654d3c536859705f09f8d267ee977b4004\\error_prone_annotations-2.14.0.jar\r\n" + 
//		"C:\\Users\\vinci\\.gradle\\caches\\modules-2\\files-2.1\\com.google.j2objc\\j2objc-annotations\\1.3\\ba035118bc8bac37d7eff77700720999acd9986d\\j2objc-annotations-1.3.jar\r\n" + 
//		"C:\\Users\\vinci\\.gradle\\caches\\modules-2\\files-2.1\\com.google.code.findbugs\\jsr305\\3.0.2\\25ea2e8b0c338a877313bd4672d3fe056ea78f0d\\jsr305-3.0.2.jar\r\n" + 
//		"C:\\Users\\vinci\\.gradle\\caches\\modules-2\\files-2.1\\com.google.code.gson\\gson\\2.8.9\\8a432c1d6825781e21a02db2e2c33c5fde2833b9\\gson-2.8.9.jar\r\n" + 
//		"C:\\Users\\vinci\\.gradle\\caches\\modules-2\\files-2.1\\javax.annotation\\javax.annotation-api\\1.3.2\\934c04d3cfef185a8008e7bf34331b79730a9d43\\javax.annotation-api-1.3.2.jar\r\n" + 
//		"C:\\Users\\vinci\\.gradle\\caches\\modules-2\\files-2.1\\org.jetbrains.kotlin\\kotlin-stdlib-common\\1.7.10\\bac80c520d0a9e3f3673bc2658c6ed02ef45a76a\\kotlin-stdlib-common-1.7.10.jar\r\n" + 
//		"C:\\Users\\vinci\\.gradle\\caches\\modules-2\\files-2.1\\com.google.guava\\failureaccess\\1.0.1\\1dcf1de382a0bf95a3d8b0849546c88bac1292c9\\failureaccess-1.0.1.jar\r\n" + 
//		"C:\\Users\\vinci\\.gradle\\caches\\modules-2\\files-2.1\\com.google.guava\\listenablefuture\\9999.0-empty-to-avoid-conflict-with-guava\\b421526c5f297295adef1c886e5246c39d4ac629\\listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar\r\n" + 
//		"C:\\Users\\vinci\\.gradle\\caches\\modules-2\\files-2.1\\org.checkerframework\\checker-qual\\3.12.0\\d5692f0526415fcc6de94bb5bfbd3afd9dd3b3e5\\checker-qual-3.12.0.jar\r\n" + 
//		"C:\\Users\\vinci\\.gradle\\caches\\modules-2\\files-2.1\\io.grpc\\grpc-context\\1.49.2\\2e29b2b08c795dc7ef0e978925f582331b75aa15\\grpc-context-1.49.2.jar\r\n" + 
//		"C:\\Users\\vinci\\.gradle\\caches\\modules-2\\files-2.1\\com.google.code.gson\\gson\\2.9.0\\8a1167e089096758b49f9b34066ef98b2f4b37aa\\gson-2.9.0.jar\r\n" + 
//		"C:\\Users\\vinci\\.gradle\\caches\\modules-2\\files-2.1\\io.perfmark\\perfmark-api\\0.25.0\\340a0c3d81cdcd9ecd7dc2ae00fb2633b469b157\\perfmark-api-0.25.0.jar\r\n" + 
//		"C:\\Users\\vinci\\.gradle\\caches\\modules-2\\files-2.1\\com.google.android\\annotations\\4.1.1.4\\a1678ba907bf92691d879fef34e1a187038f9259\\annotations-4.1.1.4.jar\r\n" + 
//		"C:\\Users\\vinci\\.gradle\\caches\\modules-2\\files-2.1\\org.codehaus.mojo\\animal-sniffer-annotations\\1.21\\419a9acd297cb6fe6f91b982d909f2c20e9fa5c0\\animal-sniffer-annotations-1.21.jar\r\n" + 
//		"C:\\Users\\vinci\\.gradle\\caches\\modules-2\\files-2.1\\org.junit.platform\\junit-platform-commons\\1.9.0\\b727889107fc28c7460b21d1083212f8ce7602c6\\junit-platform-commons-1.9.0.jar\r\n" + 
//		"C:\\Users\\vinci\\.gradle\\caches\\modules-2\\files-2.1\\org.junit.jupiter\\junit-jupiter-params\\5.9.0\\d6b6ccd3585351f617b06f066675271afebe9b28\\junit-jupiter-params-5.9.0.jar\r\n" + 
//		"C:\\Users\\vinci\\.gradle\\caches\\modules-2\\files-2.1\\org.junit.jupiter\\junit-jupiter-api\\5.9.0\\a44f7eba3ea214f6ec87ad9fccd3b2ac4681a4\\junit-jupiter-api-5.9.0.jar\r\n" + 
//		"C:\\Users\\vinci\\.gradle\\caches\\modules-2\\files-2.1\\org.opentest4j\\opentest4j\\1.2.0\\28c11eb91f9b6d8e200631d46e20a7f407f2a046\\opentest4j-1.2.0.jar\r\n" + 
//		"C:\\Users\\vinci\\.gradle\\caches\\modules-2\\files-2.1\\org.apiguardian\\apiguardian-api\\1.1.2\\a231e0d844d2721b0fa1b238006d15c6ded6842a\\apiguardian-api-1.1.2.jar\r\n" + 
//		"C:\\Users\\vinci\\.gradle\\caches\\modules-2\\files-2.1\\org.junit.jupiter\\junit-jupiter-engine\\5.9.0\\cc1cb5efae97493dcffb687253a0964ad2e7b99\\junit-jupiter-engine-5.9.0.jar\r\n" + 
//		"C:\\Users\\vinci\\.gradle\\caches\\modules-2\\files-2.1\\org.junit.platform\\junit-platform-engine\\1.9.0\\bd46891f01817b5ffdd368cb0482a34746610acb\\junit-platform-engine-1.9.0.jar\r\n";
//	
//String[] paths=jars.split("\r\n");
//for(String path: paths) {
//	String libname=path.split("\\\\")[path.split("\\\\").length-1];
//	String path_copied="C:\\Users\\vinci\\eclipse-workspace\\Self_Sovereign_Identity_Solution\\src\\TrinsicLib\\"+libname.trim();
//	System.out.println(path);
//	System.out.println(path_copied);
//	File original = new File(path.trim());
//	File copied = new File(path_copied);
//
//	FileUtils.copyFile(original, copied);
//}

}
}