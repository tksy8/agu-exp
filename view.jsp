<%@page contentType="text/html" pageEncoding="UTF-8"%>


<%
    twi.Model model = new twi.Model();
    String token="";
    String stoken="";
    token = request.getParameter("oauth_token");
    stoken = request.getParameter("oauth_verifier");
%>

<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Twitter OAuth認証開始</title>
  </head>
  <body>
      <% if (token==null || stoken==null){ %>
      <p><a href="<%=model.getAuth()%>">Twitter OAuth認証開始</a></p>
      <% }else{ %>
      <dt>Access Token</dt><dd><%=token%></dd>
      <dt>Token Secret</dt><dd><%=stoken%></dd>
      <% model.registerToken(token, stoken);
      model.tweet("授業のアプリのテストです"); %>
      <% } %>
  </body>
</html>
