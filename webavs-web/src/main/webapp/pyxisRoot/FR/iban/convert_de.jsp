<%-- tpl:insert page="/theme/processUpload.jtpl" --%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<!--# set echo="url" -->
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<HEAD>
<%
globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
String userActionUpd = request.getParameter("userAction").substring(0,request.getParameter("userAction").lastIndexOf('.')) + ".modifier";
String languePage = globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session);
String selectedIdValue = "";
String userActionValue = "";
String servletContext = request.getContextPath();
String mainServletPath = (String)request.getAttribute("mainServletPath");
if (mainServletPath == null) {
	mainServletPath = "";
}
String formAction = servletContext + mainServletPath;
int tableHeight = 243; 
String subTableWidth = "100%";
%>
<%-- tpl:put name="zoneInit"  --%> 
<!-- Creer l'enregitrement s'il n'existe pas -->
<%@ page import="globaz.globall.util.*"%>
<%
	String idEcran ="GTI2006";
	globaz.pyxis.vb.iban.TIConvertViewBean viewBean = (globaz.pyxis.vb.iban.TIConvertViewBean) session.getAttribute("viewBean");
	userActionValue = "pyxis.iban.convert.executer";
	formAction= request.getContextPath()+mainServletPath+"Root/"+languePage+"/iban/convertFile_de.jsp";
	
%>
<SCRIPT language="JavaScript">
top.document.title = "Tiers"

</SCRIPT>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness"  --%> 
<%
	String eMailAddress = objSession.getUserEMail();
%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/ValidationGroups.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<%-- /tpl:put --%>
<META name="GENERATOR" content="IBM WebSphere Page Designer V3.5.3 for Windows">
<META http-equiv="Content-Style-Type" content="text/css">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/theme/master.css">
<TITLE></TITLE>
<SCRIPT language="JavaScript">
var langue = "<%=languePage%>"; 

</SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/params.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/actionsForButtons.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/swap.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/shortKeys.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/ValidationGroups.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<SCRIPT language="javascript"> 
var errorObj = new Object();
errorObj.text = "";

function showErrors() {
	if (errorObj.text != "") {
		showModalDialog('errorModalDlg.jsp',errorObj,'dialogHeight:20;dialogWidth:25;status:no;resizable:no');	
	}
}
</SCRIPT>
<% 
/*
A faire:
function add() {}
function upd() {}
function validate() {}
function cancel() {}
function del() {}
function init(){}
*/
%>
<%-- tpl:put name="zoneScripts"  --%> 
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/autocomplete.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/selectionPopup.js"></SCRIPT>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

function init(){}

function validateform() {
	if (document.getElementsByName("filename")[0].value == "") {
		if(langue=='FR') {
			var value="Vous devez sélectionner un fichier."
		} else {
			var value="Sie müssen eine Datei auswählen."
		}
		alert(value);			
		return false;
	} else {
		return true;
	}
}


// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
</HEAD>
<BODY onload="this.focus();showErrors();" onKeyDown="keyDown();actionKeyDown();" onKeyUp="keyUp();actionKeyUp();">
<TABLE bgcolor="#B3C4DB" cellspacing="0" cellpadding="0" width="100%" height="<%=tableHeight%>">
	<TBODY>
		<TR>
			<TH colspan="3" height="10" class="title">
				<DIV style="width: 100%">
					<SPAN class="idEcran"><%=(null==idEcran)?"":idEcran%></SPAN><ct:FWLabel key='MENU_CONVERSION_IBAN' /></DIV>
			</TH>
		</TR>
		<TR>
			<TD bgcolor="gray" colspan="3"  style="height:1px"></TD>
		</TR>
		<TR>
			<TD colspan="3">&nbsp;</TD>
		</TR>
		<TR>
			<TD width="5">&nbsp;</TD>
			<TD valign="top">
			<FORM name="mainForm" action="<%=formAction%>" enctype='multipart/form-data' method="post"  >
				<TABLE border="0" cellspacing="0" cellpadding="0" width="<%=subTableWidth%>">
					<TBODY>
						<%-- tpl:put name="zoneMain"  --%> 
          <tr> 
            <td>Adresse E-Mail</td>
			<td><input type="text" name="eMailAddress" maxlength="40" size="40" style="width:8cm;" value="<%=eMailAddress!=null?eMailAddress:""%>">&nbsp;*</td>
          </tr>
          
          <tr>
          	
          	<td><ct:FWLabel key='FICHIER_SOURCE' /></td>
          	<td>          		
          		<!-- <div style="padding:1; overflow: scroll"> -->
          		<input  type="file" size="40" name="filename" maxlength="256">&nbsp;*
          		<!-- </div> -->
			</td>
          </tr>
          
          <%-- /tpl:put --%>
					</TBODY>
				</TABLE>
				<INPUT type="hidden" name="selectedId" value="<%=selectedIdValue%>">
				<INPUT type="hidden" name="userAction" value="<%=userActionValue%>">
				<INPUT type="hidden" name="_method" value='<%=request.getParameter("_method")%>'>
				<INPUT type="hidden" name="_valid" value='<%=request.getParameter("_valid")%>'>
				<INPUT type="hidden" name="_sl" value="">
			</FORM>
			</TD>
			<TD width="5">&nbsp;</TD>
		</TR>
		<TR>
			<TD bgcolor="#FFFFFF" colspan="3" align="center"><INPUT type="submit" value="Ok" style="width:60" onClick="if(validateform())document.forms[0].submit();"></TD>
		</TR>
		<TR>
			<TD bgcolor="#FFFFFF"></TD>
			<TD bgcolor="#FFFFFF" colspan="2" align="left"><FONT  color="#FF0000">
				<% if (viewBean.getMsgType().equals (viewBean.ERROR) == true) {%>
					<script>
						errorObj.text = "<%=globaz.framework.util.FWTextFormatter.slash(globaz.framework.util.FWTextFormatter.newLineToBr(viewBean.getMessage()), '\"')%>";
						<%
							viewBean.setMessage("");
							viewBean.setMsgType(viewBean.OK);
						%>
					</script>
				<% } %>
			</FONT></TD>
		</TR>
	</TBODY>
</TABLE>
<!-- p align="right"><input type="button" name="Button" value="Lancer" onClick="document.forms[0].submit();"></p-->
<%-- tpl:put name="zoneEndPage"  --%> 


<%  if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>

<%  }  %>


<script>
// menu 

//top.fr_menu.location.replace('appMenu.jsp?_optionMenu=-defaut-&changeTab=Menu');	
</script>
<%-- /tpl:put --%>
</BODY>
</HTML><%-- /tpl:insert --%>