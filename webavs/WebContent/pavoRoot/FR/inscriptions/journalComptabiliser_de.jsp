<%try{ %> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML><!-- #BeginTemplate "/templates/process.dwt" -->
<!--# set echo="url" -->
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<HEAD>
<%
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
String processStarted = request.getParameter("process");
boolean processLaunched = "launched".equalsIgnoreCase(processStarted);
boolean showProcessButton = !processLaunched;
String okButtonLabel = "Ok";
%>
<!-- #BeginEditable "zoneInit" --> 
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
		globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
		globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
		String userActionUpd = request.getParameter("userAction").substring(0,request.getParameter("userAction").lastIndexOf('.')) + ".modifier";
    	globaz.pavo.db.inscriptions.CIJournalComptabiliserViewBean viewBean = (globaz.pavo.db.inscriptions.CIJournalComptabiliserViewBean)session.getAttribute ("viewBean");
		selectedIdValue = viewBean.getIdJournal();
		userActionValue = "pavo.inscriptions.journalComptabiliser.executer";
		String idEcran = "CCI3002";
		String emailAdresse = !globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getEmailAddress())?viewBean.getEmailAddress():"";
%>
<SCRIPT language="JavaScript">
top.document.title = "CI - Inscription au CI du journal"
</SCRIPT>
<!-- #EndEditable -->
<!-- #BeginEditable "zoneBusiness" --> 
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<!-- #EndEditable -->
<META name="GENERATOR" content="IBM WebSphere Page Designer V3.5.3 for Windows">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<meta http-equiv="Content-Style-Type" content="text/css" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta http-equiv="Cache-Control" content="no-cache" /> 
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/moduleStyle.css"/>
<SCRIPT language="JavaScript">
var langue = "<%=languePage%>"; 

</SCRIPT>
<SCRIPT language="JavaScript" src="<%=servletContext%>/scripts/params.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=servletContext%>/scripts/actionsForButtons.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=servletContext%>/scripts/shortKeys.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=servletContext%>/scripts/utils.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=servletContext%>/scripts/ValidationGroups.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=servletContext%>/scripts/selectionPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=servletContext%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=servletContext%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=servletContext%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=servletContext%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<script language="JavaScript" src="<%=servletContext%>/scripts/menu.js"></script>
<script>
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
<!-- #BeginEditable "zoneScripts" --> 
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

function toutOrNotTout(source) {
// Si la case "Tout le journal" est cochée les champs "De" et "à" sont désactivés
	if (source.checked) {
		document.forms[0].fromAvs.value = "";
		document.forms[0].partialfromAvs.value = "";
		document.forms[0].partialfromAvs.disabled = true;
		document.forms[0].toAvs.value = "";
		document.forms[0].partialtoAvs.value = "";
		document.forms[0].partialtoAvs.disabled = true;
	} else {
		document.forms[0].partialfromAvs.disabled = false;
		document.forms[0].partialtoAvs.disabled = false;
	}
}

function init(){
}
/*
*/
// stop hiding -->
</SCRIPT>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %> 
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<!-- #EndEditable -->
</HEAD>
<BODY onload="this.focus();showErrors();" onKeyDown="keyDown();actionKeyDown();" onKeyUp="keyUp();actionKeyUp();">
<TABLE bgcolor="#B3C4DB" cellspacing="0" cellpadding="0" width="100%" height="<%=tableHeight%>">
	<TBODY>
	<TR>
			<TH colspan="3" height="10" class="title">
				<DIV style="width: 100%">
					<SPAN class="idEcran"><%=(null==idEcran)?"":idEcran%></SPAN>
Inscrire le journal au CI
				</DIV>
			</TH>
		</TR>
		<TR>
			<TD bgcolor="#FFFFFF" colspan="3" height="3"></TD>
		</TR>
		<TR>
			<TD colspan="3">&nbsp;</TD>
		</TR>
		<TR>
			<TD width="5">&nbsp;</TD>
			<TD valign="top">
			<FORM name="mainForm" action="<%=formAction%>">
				<TABLE border="0" cellspacing="0" cellpadding="0" width="<%=subTableWidth%>">
					<TBODY>
						<!-- #BeginEditable "zoneMain" --> 
          <tr> 
            <td  width="180" height="27" >Journal</td>
            <td  colspan="2" height="27" > 
              <input type="text" name="idJournal" size="20" class="disabled" readonly value="<%=viewBean.getIdJournal()%>" maxlength="40" tabindex="-1">
              <input type="hidden" name="idJournal" value="<%=viewBean.getIdJournal()%>">
              <input type="hidden" name="lancer" id="lancer" value=<%=request.getParameter("lancer")%>>
              <input type="hidden" name="lancement" id="lancement" value="Lancer" >
            </td>
            <td height="27">&nbsp;</td>
          </tr>
          <TR>
          	<TD>
          		Description
          	</TD>
          	<TD>
          		<input type="text" size="73" value="<%=viewBean.getDescriptionJournal()%>" class="disabled" readonly>
          	</TD>
          </TR>
          <tr> 
            <td width="180" height="2">Adresse E-Mail</td>
            <td colspan="3" height="2"> 
              <input type="text" name="eMailAddress" maxlength="40" size="40" style="width:8.5cm;" value="<%=emailAdresse%>">
              * </td>
            <td height="2"> 
              <div align="left"></div>
            </td>
          </tr>
          <!--tr> 
            <td width="180" height="2">Imprimer le journal d&eacute;taill&eacute;</td>
            <td colspan="2" height="2"> 
              <input type="checkbox" name="impressionJournal" value="checkbox" checked>
            </td>
            <td height="2">&nbsp;</td>
          </tr>
          <tr> 
            <td width="180" height="2">Imprimer la r&eacute;capitulation</td>
            <td colspan="2" height="2"> 
              <input type="checkbox" name="impressionRecap" value="checkbox" checked>
            </td>
            <td height="2">&nbsp;</td>
          </tr-->
          <!-- #EndEditable -->
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
		<% if (processLaunched) {%>
		<tr>
			<td colspan="3" style="height: 2em; color: white; font-weight: bold; text-align: center;background-color: green"><ct:FWLabel key="FW_PROCESS_STARTED"/></td>
		</tr>
		<% } 
			if (showProcessButton) { %>
		<tr>
			<td bgcolor="#FFFFFF" colspan="3" align="center"><input type="button" value="<%=okButtonLabel%>" style="width:60" onClick="document.forms[0].submit();"></td>
		</tr>
		<% } %>

		<TR>
			<TD bgcolor="#FFFFFF"></TD>
			<TD bgcolor="#FFFFFF" colspan="2" align="left"><FONT  color="#FF0000">
				<% if (viewBean.getMsgType().equals (globaz.framework.bean.FWViewBeanInterface.ERROR) == true) {%>
					<script>
						errorObj.text = "<%=globaz.framework.util.FWTextFormatter.slash(globaz.framework.util.FWTextFormatter.newLineToBr(viewBean.getMessage()), '\"')%>";
						<%
							viewBean.setMessage("");
							viewBean.setMsgType(globaz.framework.bean.FWViewBeanInterface.OK);
						%>
					</script>
				<% } %>
			</FONT></TD>
		</TR>
	</TBODY>
</TABLE>
<!-- p align="right"><input type="button" name="Button" value="Lancer" onClick="document.forms[0].submit();"></p-->
<!-- #BeginEditable "zoneEndPage" --> 
<%  if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<SCRIPT>
</SCRIPT>
<%  }  %>


<% if(objSession.hasRight(userActionUpd, "UPDATE")) { %>
			<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdJournal()%>" menuId="journalNoRight-detail"/>
			<ct:menuSetAllParams key="idJournal" value="<%=viewBean.getIdJournal()%>" menuId="journalNoRight-detail"/>
			<ct:menuSetAllParams key="fromIdJournal" value="<%=viewBean.getIdJournal()%>" menuId="journalNoRight-detail"/>	
	<ct:menuChange displayId="options" menuId="journalNoRight-detail" showTab="options">

		</ct:menuChange>	
<% } else { %>
			<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdJournal()%>" menuId="journal-detail"/>
			<ct:menuSetAllParams key="idJournal" value="<%=viewBean.getIdJournal()%>" menuId="journal-detail"/>
			<ct:menuSetAllParams key="fromIdJournal" value="<%=viewBean.getIdJournal()%>" menuId="journal-detail"/>
		<ct:menuChange displayId="options" menuId="journal-detail" showTab="options">

		</ct:menuChange>	
<% } %>
	

<!-- #EndEditable -->
</BODY>
<!-- #EndTemplate --></HTML>
<%}catch(Exception e){
	e.printStackTrace();
}%>