<%@page import="com.iticket.Config"%>
<div>Server:<%=Config.getHostname()%></div>
<div>ServerIp:<%=Config.getServerIp()%></div>
<%
%>
<div>this PORT: <%=request.getServerPort()%></div>
<div>IP: <%=request.getHeader("X-Forwarded-For")%></div>
<div>PORT: <%=request.getRemoteAddr()%></div>

