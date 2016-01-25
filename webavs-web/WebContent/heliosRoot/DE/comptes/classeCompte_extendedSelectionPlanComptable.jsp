<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/helios.tld" prefix="he" %>
<%@ page import="globaz.helios.db.comptes.*,globaz.helios.db.interfaces.*,globaz.globall.db.*, globaz.globall.util.*" %>

<HTML>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Page Designer V4.0 for Windows">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/theme/master.css">
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/heliosRoot/treeSelection.js"></SCRIPT>

<TITLE></TITLE>

</HEAD>

<%
	String idClassification = request.getParameter("idClassification");
	String idClasseCompte = request.getParameter("idClasseCompte");
%>

<BODY bgcolor="#B3C4DB" style="margin-left:0px ; border:1px solid black">

<%if (idClassification == null || idClassification .equals("")) {%>

<br>
<b>Wählen Sie bitte eine Klassifikation !!!</b>

<%} else {%>

<STYLE TYPE="text/css">
	TR.TreeNode0{background:#B3C4DB}
	TR.TreeNode1{background:#C4D5EC}
	TR.TreeNode2{background:#D5E6FD}
	TR.TreeChild{background:#FFFFFF}
       A.TreeNode:LINK {color:black;text-decoration:none}
       A.TreeNode:HOVER {color:pink;font-weight:bold;text-decoration:none}
       A.TreeNode:VISITED {color:black;text-decoration:none}
       A.TreeNode:ACTIVE {color:black;text-decoration:none}
</STYLE>

<he:CGTreeSelectionPlanComptableTag name="classesCompte" defaut="<%=idClasseCompte%>" data="<%=globaz.helios.translation.CGTrees.getClassesCompteExtendedTreePlanComptable(session,idClassification)%>" exportValues="false" exportValuesInputName="parent.mainForm.classesCompteList" expandLevel="1" rootLabel="Tous" rootValue=" " displayRoot="true"/>

<%}%>
</BODY>
</HTML>
