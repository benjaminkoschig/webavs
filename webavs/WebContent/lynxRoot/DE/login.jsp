<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<jsp:useBean id="objSession" class="globaz.globall.db.BSession" scope="session"></jsp:useBean>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Page Designer V3.5.3 for Windows">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/theme/master.css">
<TITLE>Web@AHV - Kreditorenbuchhaltung - Accueil</TITLE>
<SCRIPT>
var errorObj = new Object();
errorObj.exceptionMsg = "";
errorObj.sessionErrors = "";
</SCRIPT>
<%  if (request.getParameter("user") != null) {
      try {
	    objSession.setApplication(globaz.lynx.application.LXApplication.DEFAULT_APPLICATION_LYNX);
        objSession.checkConnection(request.getParameter("user"), request.getParameter("password"));
        request.getSession().putValue("isConnected", new Boolean(true));
      } catch (Exception e) {
		%>
		<SCRIPT>
		errorObj.exceptionMsg = "<%=e.getMessage()%>";
		errorObj.sessionErrors = "<%=objSession.getErrors().toString().trim()%>";
		</SCRIPT>
		<%

      }
    }

    Boolean isConnected = (Boolean) request.getSession().getValue("isConnected");
    if (isConnected != null) {
	%><SCRIPT>
	pageWin = window.open('../master.jsp?_type=home&_dest=home', 'winMainLynx', 'top=0, left=0, width=' + (screen.availWidth - 10) + ', height=' + (screen.availHeight - 30) + ', scrollbars=yes, menubars=no, resizable=yes');
  	pageWin.focus();
	location.href="loggedin.html"
	</SCRIPT><%
    }
%>

<SCRIPT>
function envoyer() {
	return true;
}
function printExceptionMsg() {
	var msg = errorObj.exceptionMsg;
	if (msg != "") {
		document.write("<B style=\"color:red\">Connection problem &gt; &gt; </B> " + msg);
	}
}

function printSessionErrors() {
	var msg = errorObj.sessionErrors;
	if (msg != "") {
		document.write("<B style=\"color:red\">Connection error:</B> " + msg);
	}
}

</SCRIPT>
</HEAD>
<BODY <% if(isConnected == null) {%>onLoad="javascript:document.forms[0].elements('user').focus();"<%}%>>
<FORM name = form1 action="<%=request.getRequestURI()%>" method="POST" onSubmit="return envoyer()">
<TABLE cellpadding="0" cellspacing="0" border="0" width="990" height="640">
  <TBODY>
    <TR>
      <TD colspan="5" valign="top">&nbsp;</TD>
    </TR>
    <TR>
      <TD width="500" height="45">&nbsp;</TD>
      <TD colspan="3" align="center" class="gText" height="45">L'accès à ce service est autorisé exclusivement<BR>
      aux utilisateurs enregistrés!</TD>
      <TD width="500" height="45">&nbsp;</TD>
    </TR>
    <TR>
      <TD colspan="5">&nbsp;</TD>
    </TR>
    <TR>
      <TD height="19">&nbsp;</TD>
      <TD class="title" height="19">Utilisateur</TD>
      <TD colspan="2" class="title" height="19">Mot de passe</TD>
      <TD height="19">&nbsp;</TD>
    </TR>
    <TR>
      <TD height="26">&nbsp;</TD>
      <TD height="26"><INPUT size="26" type="text" maxlength="20" name="user")></TD>
      <TD height="26"><INPUT size="13" type="password" maxlength="10" name="password"></TD>
      <TD align="right" height="26"><INPUT type="image" src="<%=request.getContextPath()%>/o_images/btnOk.gif"></TD>
      <TD height="26">&nbsp;</TD>
    </TR>
	<TR>
		<TD colspan="5" height="200">
		<SCRIPT>
		printSessionErrors();
		</SCRIPT>
		</TD>
	</TR>
	<TR>
		<TD colspan="5" height="200">
		<SCRIPT>
		printExceptionMsg();
		</SCRIPT>
		</TD>
	</TR>
    <TR>
      <TD colspan="5" height="200">&nbsp;</TD>
    </TR>
  </TBODY>
</TABLE>
</FORM>
</BODY>
</HTML>
