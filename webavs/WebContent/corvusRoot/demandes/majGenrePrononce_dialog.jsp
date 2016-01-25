<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>

<%@ page import="globaz.globall.db.*, globaz.framework.util.*" %>
<%@page import="globaz.prestation.tools.PRSession"%>
<%@page import="globaz.corvus.db.demandes.REDemandeRente"%>
<%@page import="globaz.corvus.api.demandes.IREDemandeRente"%>
<%@page import="globaz.corvus.db.demandes.REDemandeRenteAPI"%>
<%@page import="globaz.corvus.db.demandes.REDemandeRenteInvalidite"%><html><META name="GENERATOR" content="IBM WebSphere Page Designer V4.0 for Windows">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/theme/master.css">

<script>
function doCancel() {
	window.returnValue = <%=request.getParameter("csGenrePrononce")%>;
	window.close();
}
function doValidate() {
	document.mainForm.status.value = "ok";
	document.mainForm.submit();
}
function showError(text) {
	document.getElementById("errorLabel").innerHTML = text;
}
</script>

<TITLE></TITLE>
<body style="margin-left:0px ; margin-bottom:0px">
<%
	String csGenrePrononce     = (request.getParameter("csGenrePrononce") == null)?"":request.getParameter("csGenrePrononce");
	String idDemandeRente 	  = request.getParameter("idDemandeRente");
	String csTypeDemandeRente = request.getParameter("csTypeDemandeRente");
	BSession bsession = (BSession)PRSession.getSession(session);
%>
<iframe id="internalFrame" name="internalFrame" style="width:0px ; height:0px"></iframe>
<TABLE bgcolor="#B3C4DB" cellspacing="0" cellpadding="0" width="100%" height="170px">
		<TR>
			<TH colspan="3" height="10" class="title"><%=bsession.getLabel("MODAL_DB_GENR_PR_TITLE")%></TH>
		</TR>
		<TR>
			<TD bgcolor="gray" colspan="3" height="0"></TD>
		</TR>
		<TR>
			<TD colspan="3">&nbsp;</TD>
		</TR>
		<TR>
			<TD width="5">&nbsp;</TD>
			<TD valign="top">

<form name="mainForm" id="mainForm" target="internalFrame" action="<%=request.getContextPath()%><%=(request.getParameter("mainServletPath")+"Root")%>/demandes/majGenrePrononce_dialog.jsp">
<input type="hidden" name="idDemandeRente" value="<%=idDemandeRente%>">
<input type="hidden" name="status" value="">

<%	

	if (request.getParameter("status") != null && request.getParameter("status").equals("ok")) {
%>
		
		<input type="hidden" name="csGenrePrononce" value="<%=csGenrePrononce%>">
<%
		REDemandeRente dem = REDemandeRente.loadDemandeRente(bsession, null, idDemandeRente, csTypeDemandeRente);
		if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_API.equals(dem.getCsTypeDemandeRente())) {
			((REDemandeRenteAPI)dem).setCsGenrePrononceAI(csGenrePrononce);
		}
		else if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE.equals(dem.getCsTypeDemandeRente())) {
			((REDemandeRenteInvalidite)dem).setCsGenrePrononceAI(csGenrePrononce);			
		}
		
		boolean error = false;
		try {
			dem.update();
		} catch (Exception e) {
			error = true;
		}
		if (error) {
%>
			<script>
				parent.showError('<%=bsession.getLabel("MODAL_DB_GENR_PR_ERROR1")%>');
			</script>
<%
		} else {
%>
			<script>
				window.returnValue = <%=csGenrePrononce%>;
				window.close();
			</script>			
<%
		}
	} else {
		
		
%>	
		<%=bsession.getLabel("MODAL_DB_GENR_PR_TXT1")%><br>
		<br><%=bsession.getLabel("MODAL_DB_GENR_PR_TXT2")%><br><br>
		<b><%=bsession.getLabel("MODAL_DB_GENR_PR_LIB1")%> </b> 
			 <ct:FWCodeSelectTag name="csGenrePrononce" tabindex="1" codeType="REGEPRON" wantBlank="false" defaut="<%=csGenrePrononce%>"/>							
	<%}%>


<br>
</form>
		</TD>
		</TR>
		<TR>
		  <%
		    String imgRef = request.getContextPath() + "/images/"+ bsession.getIdLangueISO().toUpperCase()+ "/btnOkCancel.gif";		    
		  %>
			<TD bgcolor="#FFFFFF" colspan="3" align="right" height="18"><IMG style="filter:progid:DXImageTransform.Microsoft.BasicImage(grayscale=1, xray=0, mirror=0, invert=0, opacity=0.65, rotation=0, enabled=false)" name="buttons" src="<%=imgRef%>" border="0" usemap="#btnOkCancel"></TD>
		</TR>
</TABLE>
<MAP name="btnOkCancel">
	<AREA tabindex="2" shape="rect" coords="1,1,78,17" onclick="javascript:doValidate();" href="#">
	<AREA tabindex="3" shape="rect" coords="78,1,157,17" onclick="javascript:doCancel();" href="#">
	<AREA shape="default" nohref>
</MAP>
</body>
</html>