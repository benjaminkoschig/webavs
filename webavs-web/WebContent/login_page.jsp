<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@page import="globaz.webavs.common.WebavsDocumentionLocator"%>
<HTML>
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
     
<BODY onLoad="javascript:document.forms[0].elements('j_username').focus();">
<FORM  method="POST" action="j_security_check">
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
      
      <TD ><INPUT style="border:solid 1px gray" type="text" maxlength="20" name="j_username" ></TD>
      <TD ><INPUT style="border:solid 1px gray" type="password" maxlength="16" name="j_password"></TD>
	  <TD align="left">
		<INPUT type="hidden" name="novaApp" value="<%=request.getContextPath()%>/pyxis">
		<INPUT type="hidden" name="userAction" value="chooseApp">
		<INPUT type="image" title="Ok" src="<%=request.getContextPath()%>/images/btnOk.gif" onClick="submit()">
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
		if (request.getParameter("error") != null) {
	%>
		<TR>
			<TD align="center" colspan="2" height="35"><font color="#FF0000">Valeur incorrecte de nom d'utilisateur ou/et de mot de passe</font></TD>
		</TR>
		<TR>
			<TD align="center" colspan="2" height="35"><font color="#FF0000">Benutzername und / oder Passwort ungültig</font></TD>
		</TR>
				
	<% } %>
</TABLE>

</center>
</FORM>
</BODY>
</HTML>
