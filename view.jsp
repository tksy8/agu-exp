<%@page contentType="text/html" pageEncoding="UTF-8"%>


<%
    twi.Model model = new twi.Model();
    String token="";
    String stoken="";
    String tweeet="";
    token = request.getParameter("oauth_token");
    stoken = request.getParameter("oauth_verifier");
    tweeet = request.getParameter("tweeet");
%>

<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Twitter OAuth認証開始</title>
    <script src="http://d3js.org/d3.v3.min.js" charset="utf-8"></script>
  </head>
  <body>
      <% if (token==null || stoken==null){ %>
      <p><a href="<%=model.getAuth()%>">Twitter OAuth認証開始</a></p>
      <% }else if(tweeet != null){%>
      <% model.twi4j();
         model.tweet4(tweeet);%>
            <a class="twitter-timeline" href="https://twitter.com/Umeco_co" data-widget-id="737534269157842944">@Umeco_coさんのツイート</a>
            <script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0],p=/^http:/.test(d.location)?'http':'https';if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src=p+"://platform.twitter.com/widgets.js";fjs.parentNode.insertBefore(js,fjs);}}(document,"script","twitter-wjs");</script>
      <% }else{ %>
      <dt>Access Token</dt><dd><%=token%></dd>
      <dt>Token Secret</dt><dd><%=stoken%></dd>
      <% model.registerToken(token, stoken);
      model.twi4j();
      model.getTimeline();
      model.printUser();%>
      <Div Align="center">
          <h6>★あなたと仲の良いユーザーランキング★</h6><br><br>
          <% for(int i=0;i<5;i++){%>
          <font size=<%=7-i%> color="#99ff00">
          <%=i+1%>位：
          <img src=<%=model.pics[i]%> />
          <%=model.ranking[i]+"      "%>
          Score：<%=model.ranknum[i]%><br></font>
      <% }model.kaiseki(model.text); %>
      <form action="example.cgi">
          <p><input type="submit" value="結果をツイートする！"></p>
      </form>
      </Div>
      <form action="http://127.0.0.1:8080/exp_app/view.jsp" method="get">
          <p>ついーとする<input type="text" name="tweeet"></p>
          <input name="secret" value="<%=model.consumer.getTokenSecret()%>">
          <p><input type="submit" value="送信する"></p>
      </form>
          あなたのぼっち度: <%=model.getBotti()%><br>
      <script>
         var dataset = [
          {graphLegend:"", graphValue:63, graphColor:"darkblue"},
          {graphLegend:"", graphValue:37, graphColor:"transparent"}
         ];

        var width = 960,
          height = 500,
          radius = Math.min(width, height) / 2;

        var arc = d3.svg.arc()
          .outerRadius(radius - 10)
          .innerRadius(0);

        var pie = d3.layout.pie()
          .sort(null)
          .value(function(d) { return d.graphValue; });

        var tooltip = d3.select("body")
          .append("div")
          .style("position", "absolute")
          .style("z-index", "20")
          .style("visibility", "hidden");

        var svg = d3.select("body").append("svg")
          .attr("width", width)
          .attr("height", height)
          .append("g")
          .attr("transform", "translate(" + width / 2 + "," + height / 2 + ")");


        var g = svg.selectAll(".arc")
          .data(pie(dataset))
          .enter().append("g")

        g.append("path")
          .attr("d", arc)
          .style("fill", function(d) { return d.data.graphColor; })
          // アニメーション効果
          .transition()
          .duration(1000) // 1秒間でアニメーションさせる
          .attrTween("d", function(d){
            var interpolate = d3.interpolate(
              { startAngle : 0, endAngle : 0 },
              { startAngle : d.startAngle, endAngle : d.endAngle }
            );
            return function(t){
              return arc(interpolate(t));
          }
        });
      
        g.append("text")
          .attr("transform", function(d) { return "translate(" + arc.centroid(d) + ")"; })
          .attr("dy", ".35em")
          .style("text-anchor", "middle")
          .text(function(d) { return d.data.graphLegend; });
      </script>
      <% for(int i=0;i<24;i++){ %>
      <%=i+1%>時：
      <svg viewBox="0 0 300 8">
      <title>tweet数！</title>
    <desc>circle要素の属性値が同じ点に注目</desc>
    <rect x="2" y="2" width="<%=model.timetwi[i]/2%>" height="5" stroke-width="1" stroke="red" fill="pink"/>
    </svg>

      <% } %>
      <% } %>
  </body>
</html>
