 <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/helios.tld" prefix="ct" %>
<%@ page import="globaz.helios.db.comptes.*,globaz.helios.db.interfaces.*,globaz.globall.db.*, globaz.globall.util.*" %>

<HTML>
<HEAD>
	<META name="GENERATOR" content="IBM WebSphere Page Designer V4.0 for Windows">
	<META http-equiv="Content-Style-Type" content="text/css">
	<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/theme/master.css">
	<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/tree.js"></SCRIPT>
	<TITLE></TITLE>
</HEAD>

<%
	String idDefinitionListe = request.getParameter("idDefinitionListe");
%>

<BODY bgcolor="#B3C4DB" style="margin-left:0px ; border:1px solid black">

<%if (idDefinitionListe == null || idDefinitionListe .equals("")) {%>

<br>
<b>Veuillez sélectionner une option dans le menu de définition de listes pour choisir les classes de compte à imprimer.</b>

<%} else {%>

<STYLE TYPE="text/css">
	TR.TreeNode0{background:#B3C4DB}
	TR.TreeNode1{background:#C4D5EC}
	TR.TreeNode2{background:#D5E6FD}
	TR.TreeChild{background:#FFFFFF}
    A.TreeNode:LINK {color:black;text-decoration:none}
    A.TreeNode:HOVER {font-weight:bold;text-decoration:none}
    A.TreeNode:VISITED {color:black;text-decoration:none}
    A.TreeNode:ACTIVE {color:black;text-decoration:none}
</STYLE>

<ct:CGTreeCheckboxTag name="classesCompte" defaut="" data="<%=globaz.helios.translation.CGTrees.getClassesCompteTree(session,idDefinitionListe)%>" exportValues="true" exportValuesInputName="window.parent.document.getElementById(\"classesCompteList\")" expandLevel="1" rootLabel="Tous" rootValue=" " displayRoot="true"/>
<%}%>
</BODY>
</HTML>
