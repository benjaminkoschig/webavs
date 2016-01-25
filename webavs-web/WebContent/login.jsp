<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@page import="globaz.webavs.common.WebavsDocumentionLocator"%>
<HTML>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%
//String languePage = globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session);
//String servletContext = request.getContextPath();
%>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Page Designer V4.0 for Windows">
<META http-equiv="Content-Style-Type" content="text/css">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/theme/master.css">
<TITLE>WebAVS - Accueil *-* WebAHV - Startseite</TITLE>
<script>
if (self != top){
	top.location.href=window.location.href;
}
</script>
</HEAD>
      <%
      String focus = "user";
      if (request.getAttribute("message") != null && request.getParameter("user") != null) {
    	  focus ="password";
      }
      %>
<BODY onLoad="javascript:document.forms[0].elements('<%=focus %>').focus();">
<FORM name = form1 method="POST">
<center>
<TABLE cellpadding="0" cellspacing="0" border="0" align="center" height="60">
  	<TR>
      <TD align="center" class="gText">&nbsp;</TD>
    </TR>
    <TR>
      <TD align="center" class="gText">L'accès à ce service est autorisé exclusivement aux utilisateurs enregistrés!</TD>
   	</TR>
    <TR>
      <TD align="center" class="gText">&nbsp;</TD>
    </TR>
	<TR>
      <TD align="center" class="gText">Der Zugriff zu diesem Dienst ist ausschliesslich für die registrierten Benutzer zugelassen!</TD>
    </TR>
</TABLE>

<TABLE cellpadding="0" cellspacing="0" border="0" align="center">
	<TR><TD>&nbsp;</TD></TR>
	<TR>
      <TD align="center" class="gText">
      	<IMG id="logoImg" border="0" src="images/<%=globaz.jade.common.Jade.getInstance().getClientLogoFile()%>">
		<script>
			var imgname = "images/<%=globaz.jade.common.Jade.getInstance().getClientLogoFile()%>?"+new Date().getTime();
			document.getElementById("logoImg").src=imgname;
		</script>
	  </TD>
	</TR>
	<TR>
		<TD align="center">
		<%
		try {
			globaz.jade.jdbc.JadeJdbcDatasourceDirect dsDirect = (globaz.jade.jdbc.JadeJdbcDatasourceDirect) globaz.jade.jdbc.JadeJdbcDriver.getInstance().getDatasource(globaz.jade.common.Jade.getInstance().getDefaultJdbcUrl().substring(10));
			out.write(dsDirect.getUrl());
		} catch (Exception e) {
		}
		%>
	  </TD>
    </TR>
    <TR>
      <TD align="center" />
    </TR>
    <TR>
      <TD align="center">
	  	Web@AVS / Web@AHV (version <%=WebavsDocumentionLocator.getVersion()%><%=globaz.webavs.common.WebavsDocumentionLocator.getServicePackVersion()%> released on <%=WebavsDocumentionLocator.getDate()%>)
	  </TD>
    </TR>
    
</TABLE>

<TABLE cellpadding="0" cellspacing="0" border="0" align="center">
	<TR>
      <TD align="center" class="gText" height="80">
	  <DIV style="text-align: center; color: #B3C4DB">
	  <H1>Login</H1>
	  </DIV>
	  </TD>
    </TR>
</TABLE>

<TABLE cellpadding="0" cellspacing="0" border="0" align="center" width="380">
    <TR>
      <TD class="title" height="19">&nbsp;</TD>
      <TD class="title">Utilisateur</TD>
      <TD class="title">Mot de passe</TD>
      <TD class="title">&nbsp;</td>
    </TR>
    <TR>
      <TD class="title" height="19">&nbsp;</TD>
      <TD class="title">Benutzer</TD>
      <TD class="title">Passwort</TD>
      <TD class="title">&nbsp;</td>
    </TR>
    <TR style="background-color: #B3C4DB;">
      <TD height="35">&nbsp;</TD>
      <%
      String userLogin = "";
      if (request.getAttribute("message") != null && request.getParameter("user") != null) {
    	  userLogin = request.getParameter("user");
      }%>
      <TD ><INPUT style="border:solid 1px gray" type="text" maxlength="20" name="user" value="<%=userLogin %>"></TD>
      <TD ><INPUT style="border:solid 1px gray" type="password" maxlength="16" name="password"></TD>
	  <TD align="left">
		<INPUT type="hidden" name="novaApp" value="<%=request.getContextPath()%>/pyxis">
		<INPUT type="hidden" name="userAction" value="chooseApp">
		<INPUT type="image" title="Ok" src="<%=request.getContextPath()%>/images/btnOk.gif" onClick="document.forms[0].action=document.forms[0].elements('novaApp').value">
		&nbsp;
	  </TD>
    </TR>
    <TR style="background-color: #B3C4DB;">
      <TD colspan="4" height="1">&nbsp;</TD>
    </TR>
</TABLE>

<TABLE cellpadding="0" cellspacing="0" border="0" align="center">
    <% globaz.framework.controller.FWDispatcher controller = (globaz.framework.controller.FWDispatcher)session.getAttribute("objController");
	     if(controller != null && controller.isTimeOut()) { %>
		 <TR>
		 <TD align="center" height="35"><font color="#FF0000">Votre
			session a expiré, veuillez vous identifier à nouveau.</font></TD>
		</TR>
		<TR>
		<TD align="center" colspan="2" height="35"><font color="#FF0000">Ihre
			Sitzung ist abgelaufen, melden Sie sich bitte neu an.</font></TD>
		</TR>
	<% }
		if (controller != null) {
			String messageForUser = (String)request.getAttribute("message");
			if (messageForUser != null) { %>
				<TR>
					<TD align="center" colspan="2" height="35"><font color="#FF0000"><%=messageForUser%></font></TD>
				</TR>
	<%
			}
		}
	%>
</TABLE>

</center>
</FORM>
</BODY>
</HTML>
