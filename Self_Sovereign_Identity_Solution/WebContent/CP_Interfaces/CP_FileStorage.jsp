<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>CP-FileStorageInterface</title>
</head>
<body>
Hi, user, I'm CP.
<br>
Please, give me your Ethereum Address and the Storage Information File
<form action="../CP_FileStorage" method="post" enctype='multipart/form-data'>
Ethereum Address:<input tabindex="1" size="100" id="EthAddress" name="EthAddress" type="text" value=""> <br>
Storage File: 	 <input type="file" tabindex="3" size="25" name="StorageFile" > <br>
Credential in Json Format: <input type="file" name="credential" />	<br>
	<button tabindex="7" type="submit" class="btn btn-succes btn-large">Send</button>
</form>

<%String contractAddr=(String)request.getAttribute("SCAddress");
if(contractAddr!=null){ %>
Please, confirm the notarization with the Smart Contract at address:

<%=contractAddr%> <br>

<%} %>

</body>
</html>