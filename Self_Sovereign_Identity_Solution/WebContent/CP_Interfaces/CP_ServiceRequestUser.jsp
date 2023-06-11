<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Service Request Interface</title>
</head>
<body>
Hi, user, I'm CP.
<br>
Please, give me the service required and the set of label separated by ";":
<form action="../CP_ServiceRequest" method="post">
ID Service:<input tabindex="1" size="18" id="id" name="idServiceU" type="text" value=""> <br>
List of labels <input tabindex="1" size="18" id="labelList" name="labelList" type="text" value=""> <br>
	<button tabindex="7" type="submit" class="btn btn-succes btn-large">Send</button>
</form>
<%String resp=(String)request.getAttribute("resp");
if(resp!=null){%>
Thanks you, I will provide SP with the files you selected.
<%} %>
</body>
</html>