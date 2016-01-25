<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<TITLE>POSTIT</TITLE>
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/theme/master.css">
<%
String servletContext = request.getContextPath();
String mainServletPath = (String)request.getAttribute("mainServletPath");
String selectedIdValue = "";
String userActionValue = "";
//int tableHeight = 243;
//String subTableWidth = "100%";
//String theMethod = request.getParameter("_method");
//boolean isMethodAdd = (new String("ADD")).equalsIgnoreCase(theMethod);
if (mainServletPath == null) {
	mainServletPath = "";
}
String formAction = servletContext + mainServletPath;
//System.out.println("Form action de " + request.getServletPath() + formAction);
%>

<%
	globaz.framework.db.postit.FWPostIt viewBean = (globaz.framework.db.postit.FWPostIt)session.getAttribute("postIt");	
	String selectedKeyValue = viewBean.getKey();
	boolean isNew = viewBean.isNew();
%>
<SCRIPT language="JavaScript">
var langue = "FR";
</SCRIPT>

<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/params.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/actionsForButtons.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/swap.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/shortKeys.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/ValidationGroups.js"></SCRIPT>
<SCRIPT language="javascript"> 
var errorObj = new Object();
errorObj.text = "";

function showErrors() {
	if (errorObj.text != "") {
		showModalDialog('<%=formAction%>/errorModalDlg.html',errorObj,'dialogHeight:20;dialogWidth:25;status:no;resizable:no');	
	}
}
</SCRIPT>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
function add() {
//	document.forms[0].text.focus();
//	document.forms[0].btnSave.style.display="";
}
function upd() {
//	document.forms[0].text.focus();
}
function validate() {

	var exit = true;
	var message = "Erreur: des champs ne sont pas remplis correctement!";
	if (document.forms[0].elements('text').value == "")
	{
		message = message + "\nEntrez un texte";
		exit = false;
	}
	if (exit == false)
	{	
		errMsg.innerHTML = message;
		return (exit);
	}
	if (<%=isNew%>)
		document.forms[0].elements('userAction').value="framework.postit.add";
	else
		document.forms[0].elements('userAction').value="framework.postit.modify";
	return (exit);
}
function cancel() {
}
function del() {
	if (window.confirm("Veuillez confirmer la suppression du post-it."))
	{
		document.forms[0].elements('userAction').value="framework.postit.remove";
		document.forms[0].submit();
		window.close();
	}
}
function init(){
}
function key() {
}
function showSave() {
	document.forms[0].btnSave.style.display="";	
	document.forms[0].btnCancel.style.display="";
	if (!<%=isNew%>) {
		document.forms[0].btnDelete.style.display="";		
	}
}
/*
*/
servlet = "framework";

// stop hiding -->
</SCRIPT>
</HEAD>
<BODY bgcolor="FFFF99" onload="this.focus();init();if(document.forms[0].elements('_method').value == ADD)add();showErrors();">
<BR>
	<FORM name="mainForm" action="<%=formAction%>">
		<P align="center">
		<TEXTAREA rows="14" cols="40" name="text" bgcolor="FFFF99" onfocus="showSave()"><%=viewBean.getText()%></TEXTAREA>
		<BR>
		<BUTTON name="btnClose" id="btnClose" onclick="window.close()">Fermer</BUTTON>
		<BUTTON name="btnCancel" id="btnCancel" onclick="mainForm.reset()" style="display:none">Annuler</BUTTON>
		<BUTTON name="btnSave" id="btnSave" onclick="if(validate()){mainForm.submit();}" style="display:none">Enregistrer</BUTTON>
		<BUTTON name="btnDelete" id="btnDelete" onclick="del()" style="display:none">Supprimer</BUTTON>
		<INPUT type="hidden" name="selectedId" value="<%=selectedIdValue%>">
		<INPUT type="hidden" name="key" value="<%=selectedKeyValue%>">
		<INPUT type="hidden" name="pop" value="yes">
		<INPUT type="hidden" name="userAction" value="<%=userActionValue%>">
		<INPUT type="hidden" name="_method" value='<%=request.getParameter("_method")%>'>
		<INPUT type="hidden" name="_valid" value='<%=request.getParameter("_valid")%>'>
		<INPUT type="hidden" name="_sl" value="">	
	</FORM>	
	<DIV id="errMsg" align="center"></DIV>
</BODY>
</HTML>
