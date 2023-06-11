<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>PKG-SP Interface</title>
</head>
<body>
Hi, service provider, I'm PKG.
<br>
Please, give me your attributes separated by ;.
<form action="../PKG_GenerateSPKey" method="post">
List of attributes: <input tabindex="1" size="18" id="attributes" name="attributes" type="text" value=""> <br>
	<button tabindex="7" type="submit" class="btn btn-succes btn-large">Generate key</button>
</form>
<%String privateKey=(String)request.getAttribute("privateKey");
if(privateKey!=null){ %>
Please, here's your private key:



<%=privateKey%> <br>


<%} %>
</body>
</html>