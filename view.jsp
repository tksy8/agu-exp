<%@page contentType="text/html" pageEncoding="UTF-8"%>


<%
    twi.Model model = new twi.Model();
%>

<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Twitter OAuth認証開始</title>
  </head>
  <body>
    <p><a href="<%=model.getAuth()%>">Twitter OAuth認証開始</a></p>
  </body>
</html>
