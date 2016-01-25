<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ page import="globaz.globall.db.*,globaz.helios.db.comptes.*,globaz.helios.db.interfaces.*, globaz.helios.translation.*, globaz.globall.util.*, globaz.framework.util.*" %>
<html><META name="GENERATOR" content="IBM WebSphere Page Designer V4.0 for Windows">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/theme/master.css">

<script>
function doCancel() {
	window.returnValue = "";
	window.close();
}
function doValidate() {
	document.mainForm.createLabel.value = "ok";
	document.mainForm.submit();
}
function showError(text) {
	document.getElementById("errorLabel").innerHTML = text;
}
</script>

<TITLE></TITLE>
<body style="margin-left:0px ; margin-bottom:0px">
<%
	String libelle 	= (request.getParameter("libelle") == null)?"":request.getParameter("libelle");
	String libelleSTD 	= (request.getParameter("libelleSTD") == null)?"":request.getParameter("libelleSTD");
	String langue 	= request.getParameter("langue");
	String idMandat 	= request.getParameter("idMandat");
%>
<iframe id="internalFrame" name="internalFrame" style="width:0px ; height:0px"></iframe>
<TABLE bgcolor="#B3C4DB" cellspacing="0" cellpadding="0" width="100%" height="170px">
		<TR>
			<TH colspan="3" height="10" class="title">Création de libellé</TH>
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

<form name="mainForm" id="mainForm" target="internalFrame" action="<%=request.getContextPath()%><%=(request.getParameter("mainServletPath")+"Root")%>/<%=request.getParameter("langue")%>/comptes/creerLabel_dialog.jsp">
<input type="hidden" name="langue" value="<%=langue%>">
<input type="hidden" name="idMandat" value="<%=idMandat%>">
<input type="hidden" name="createLabel" value="">
<%
	if (request.getParameter("createLabel") != null && request.getParameter("createLabel").equals("ok")) {


		BSession bsession = (BSession)CodeSystem.getSession(session);

		CGLibelleStandardViewBean entity = new CGLibelleStandardViewBean();
		entity.setSession(bsession);
		entity.setIdMandat(idMandat);
		entity.setIdLibelleStandard(libelleSTD);
		
		if ("DE".equalsIgnoreCase(langue))
			entity.setLibelleDE(libelle);
		else if ("IT".equalsIgnoreCase(langue))
			entity.setLibelleIT(libelle);
		else
			entity.setLibelleFR(libelle);
			
		try {
			entity.add();
		} catch (Exception e) {;}
		if (entity.isNew()) {
%>
	<script>
		parent.showError("Le libellé n'a pas été créé. Vérifier que son acronyme n'existe pas déjà.");
	</script>
<%
		} else {
%>
	<script>
		window.returnValue = "<%=libelle%>";
		window.close();
	</script>
<%
		}
	} else {

%>
<%
		if (!libelleSTD.equals("") && libelleSTD.charAt(0) == '*') {
			libelleSTD = libelleSTD.substring(1);
%>
		Si vous souhaitez créer un libellé pour l'acronyme <b><%=libelleSTD%></b>, rentrez sa description et cliquez sur Valider.<br>
		<br>Sinon cliquez sur Annuler.<br><br>
		<b>Libellé : </b><input name="libelle" class="libelleLong" value="<%=libelle%>">
		<input type="hidden" name="libelleSTD" value="<%=libelleSTD%>">&nbsp;<span style="color:red" id="errorLabel" name="errorLabel"></span>

<%
 		} else {
%>
		Si vous souhaitez créer un libellé dont la description est <b><%=libelle%></b>, rentrez son acronyme et cliquez sur Valider.<br>
		<br>Sinon cliquez sur Annuler.<br><br>
		<b>Acronyme : </b><input name="libelleSTD" class="libelle" value="<%=libelleSTD%>" size="5">
		<input name="libelle" type="hidden" value="<%=libelle%>">&nbsp;<span style="color:red" id="errorLabel" name="errorLabel"></span>
<%
		}
	}
%>
<br>
</form>
		</TD>
		</TR>
		<TR>
			<TD bgcolor="#FFFFFF" colspan="3" align="right" height="18"><IMG style="filter:progid:DXImageTransform.Microsoft.BasicImage(grayscale=1, xray=0, mirror=0, invert=0, opacity=0.65, rotation=0, enabled=false)" name="buttons" src="<%=request.getContextPath()%>/images/<%=request.getParameter("langue")%>/btnOkCancel.gif" border="0" usemap="#btnOkCancel"></TD>
		</TR>
</TABLE>
<MAP name="btnOkCancel">
	<AREA shape="rect" coords="1,1,78,17" onclick="javascript:doValidate();" href="#">
	<AREA shape="rect" coords="78,1,157,17" onclick="javascript:doCancel();" href="#">
	<AREA shape="default" nohref>
</MAP>
</body>
</html>