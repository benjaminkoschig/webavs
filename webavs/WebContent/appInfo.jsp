<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page import="globaz.webavs.common.WebavsDocumentionLocator"%>
<HTML>
<HEAD>
<%@ page
language="java"
contentType="text/html; charset=ISO-8859-1"
%>
<%
globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.api.BISession currBISession = controller.getSession();
globaz.globall.db.BSession currSession = (globaz.globall.db.BSession)currBISession;
String userFullName = currSession.getUserFullName();
String userName = currSession.getUserId();
String langue = currSession.getIdLangueISO();
String app = currSession.getApplicationId();
String sessionId = "#" + Integer.toString(currSession.getSessionId());
String servletContext = request.getContextPath();
String mainServletPath = (String)request.getAttribute("mainServletPath");
String titlePageUrl = servletContext + "/" + app.toLowerCase() + "Root" + globaz.framework.servlets.FWServlet.getLanguageTreeForApplication(app, session) + "/" + "appLabel.jsp";
%>
<STYLE>
    TD { padding-top:0 }
</STYLE>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/theme/master.css">
<SCRIPT>
	top.fr_pagelabel.location.href = '<%=titlePageUrl%>';
	//Hack(setTimemout et catch) pour ne pas avoir d'erreur si la page est top lente
	//Hack. Permet de ne pas avoir la perte du focus dans les champs de RC liste lors du Tab
	setTimeout (function (){
		try {
			var b_setFocus = true;
			if(	typeof parent.document.frames['fr_main'].b_hackFocus !== "undefined" ){
				b_setFocus = parent.document.frames['fr_main'].b_hackFocus ;
			}
			if (typeof top.fr_main.setFocus === "function" && b_setFocus ){
				if( top.fr_main.fr_detail !== undefined && top.fr_main.fr_list !== undefined){  //es une capage ?
					if(typeof top.fr_main.fr_detail.setFocus==="function" ){
						top.fr_main.fr_detail.setFocus();
					}
				}else {
					if(top.fr_main.fr_list !== undefined){
						top.fr_main.setFocus();
					}
				}
			}
		}catch(e) {
		}
	},
	100);
</SCRIPT>
<%@page import="globaz.jade.common.Jade"%>
<style type="text/css">
body {
	background-color: <%=Jade.getInstance().getWebappBackgroundColor()%>;
}
</style>
</HEAD>
<%
java.util.Date now = new java.util.Date(System.currentTimeMillis());
java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("HH:mm '-' d.MM.yyyy");
%>
<BODY style="position: absolute; top: 0">
<TABLE width="100%" style="position: relative; top: 0">
	<TR style="position: relative; top: 0">
		<TD><%=(null!=userFullName)?userFullName.toUpperCase():"?userFullName?"%> -
			<B> <%=(null!=userName)?userName.toUpperCase():"?user?"%></B>:&nbsp;
			<B><%=(null!=langue)?langue.toUpperCase():"?langue?"%></B>
			(<%=(app + " " + sessionId)%>)
		</TD>
		<TD>Version <%=WebavsDocumentionLocator.getVersion()%><%=globaz.webavs.common.WebavsDocumentionLocator.getServicePackVersion()%> released on <%=WebavsDocumentionLocator.getDate()%></TD>
		<TD><B><%=format.format(now)%></B></TD>
		<TD style="text-align:right">
		<A href="about.jsp" target="fr_main">About...</A> &nbsp; &nbsp; &nbsp;</TD>
	</TR>
</TABLE>
</BODY>
</HTML>
