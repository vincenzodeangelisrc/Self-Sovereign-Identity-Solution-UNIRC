<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Service Request Interface</title>
</head>
<body>
Hi, service provider, I'm CP.
<br>
Please, give me the service required by the user and your Ethereum Address:
<form action="../CP_ServiceRequest" method="post">
ID Service:<input tabindex="1" size="18" id="id" name="idServiceSP" type="text" value=""> <br>
Ethereum Address <input tabindex="1" size="18" id="Eth_Add" name="Eth_Add" type="text" value=""> <br>
	<button tabindex="7" type="submit" class="btn btn-succes btn-large">Require Files</button>
</form>
<%String contractAddr=(String)request.getAttribute("SCAddress");
if(contractAddr!=null){ %>
Please, confirm the notarization with the Smart Contract at address:

<%=contractAddr%> <br>

<%} %>
</body>
</html>