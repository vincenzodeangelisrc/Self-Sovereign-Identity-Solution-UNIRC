<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>PKG-User Interface</title>
</head>
<body>
Hi, user, I'm PKG.
<br>
Please, give me your identity number and the total number of labels you need.
<form action="../PKG_GenerateUserKey" method="post">
ID:<input tabindex="1" size="18" id="id" name="id" type="text" value=""> <br>
Total number of labels <input tabindex="1" size="18" id="number" name="number" type="text" value=""> <br>
	<button tabindex="7" type="submit" class="btn btn-succes btn-large">Generate keys</button>
</form>
<%String[] privateKeys=(String[])request.getAttribute("privateKeys");
if(privateKeys!=null){ %>
Please, here's your private keys (for each label):

<%for(int i=0; i<privateKeys.length;i++) {%>

<%=privateKeys[i] %> <br>
<%}} %>


</body>
</html>