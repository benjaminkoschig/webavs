<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
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

%>

<%
globaz.pavo.db.compte.CICalculeMasseSalarialeViewBean viewBean = (globaz.pavo.db.compte.CICalculeMasseSalarialeViewBean)session.getAttribute ("viewBean");
String jspLocation = servletContext + mainServletPath + "Root/ti_select_all.jsp";
int autoDigitAff = globaz.pavo.util.CIUtil.getAutoDigitAff(session);
int tailleChampsAff = globaz.pavo.util.CIUtil.getTailleChampsAffilie(session);
String idEcran = "CCI3005";
%>



<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/autocomplete.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/selectionPopup.js"></SCRIPT>
<SCRIPT language="JavaScript">
top.document.title = "IK - Vergleichung der Lohnsumme eines Arbeitgebers";
</script>

<META name="GENERATOR" content="IBM WebSphere Page Designer V3.5.3 for Windows">
<META http-equiv="Content-Style-Type" content="text/css">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/theme/master.css">
<TITLE>Vergleichung der Lohnsummen eines Arbeitgebers</TITLE>
<SCRIPT language="JavaScript">
var langue = "<%=languePage%>"; 
var timeWaiting = 1;
var timeWaitingId = -1;

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

<SCRIPT>
	function updateId(tag){
		if (tag.select && tag.select.selectedIndex != -1) {
			document.getElementById('idAffiliation').value=tag.select[tag.select.selectedIndex].value;
			document.getElementById('btnOk').disabled=false;
		}
	}
	function refreshEcritures(){
		if(langue=="FR"){		
				document.dernieresEcritures.location.href="<%=request.getContextPath()%>/pavoRoot/lastFiveYears.jsp?idAffiliation=" + document.getElementById('idAffiliation').value 
				+"&anneeDebut=" + document.getElementById('anneeDebut').value
				+"&anneeFin=" + document.getElementById('anneeFin').value;
				
		}else{
			document.dernieresEcritures.location.href="<%=request.getContextPath()%>/pavoRoot/lastFiveYearsDE.jsp?idAffiliation=" + document.getElementById('idAffiliation').value
			+"&anneeDebut=" + document.getElementById('anneeDebut').value
			+"&anneeFin=" + document.getElementById('anneeFin').value;
		}
				document.getElementById('btnOk').disabled=true;
	}
	function updateInfoAffilie(tag){
		if(tag.select && tag.select.selectedIndex != -1){
			document.getElementById('infoAffilie').value = tag.select[tag.select.selectedIndex].nom;
		}
	}
	function resetInfoAffilie(){
		document.getElementById('infoAffilie').value = "";		
	}
</SCRIPT>

</HEAD>
<BODY onload="this.focus();showErrors();document.getElementById('dernieresEcritures').onreadystatechange=fnStartInit" onKeyDown="keyDown();actionKeyDown();" onKeyUp="keyUp();actionKeyUp();">
<TABLE bgcolor="#B3C4DB" cellspacing="0" cellpadding="0" border="0" width="100%" height="<%=tableHeight%>">
	<TBODY>
		<TR>
			<TD colspan="3" height="10" class="title">
				<DIV style="width: 100%">
					<SPAN style="float:right; width:100px; font-weight: normal;text-align:right; font-size:8pt"><%=(null==idEcran)?"":idEcran%></SPAN>
						Vergleichung der Lohnsumme eines Arbeitgebers
				</DIV>
			</TD>
			
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

						<tr>
							<TD>Abr.-Nr.
								
							</TD>
							<TD>
								<ct:FWPopupList name="employeur" value="" onFailure="resetInfoAffilie();" onChange="updateId(tag);updateInfoAffilie(tag);" jspName="<%=jspLocation%>" autoNbrDigit="<%=autoDigitAff%>" size="15" minNbrDigit="3"/>
								&nbsp;
								<input type="text" class="disabled" readonly name="infoAffilie" size="50" maxlength="50" value="" tabIndex="-1">
								<input type="hidden" value="" name="idAffiliation">
							</td>
						</tr>
						<TR>
							<TD>
								&nbsp;
							</TD>
						</TR>
						<TR>
							<TD>
								Periode</TD>
							</TD>
							<TD>
								von <input type="text" name="anneeDebut" size="4" maxlength="4" onkeypress="return filterCharForPositivInteger(window.event);"
								value="<%=java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)-2%>">
								&nbsp;
								bis <input type="text" name="anneeFin" size="4" maxlength="4" onkeypress="return filterCharForPositivInteger(window.event);" 
								value="<%=java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)-1%>">
							</TD>
						</TR>
						<TR>
							<td>
								&nbsp;	
							</td>
						</TR>
						<tr>
							<TD valign="top">
								Vergleichung der Lohnsumme
							</TD>
							<td colspan="2">
								<IFRAME name="dernieresEcritures"
							scrolling="YES" style="border: solid 1px black; width: 14.2cm"
							height="200" tabindex="-1"> </IFRAME> 
							<div id="waitingPopup"
								style="width: 120; height: 50; position: absolute; top: 50; visibility: hidden">
								<table border="0" cellspacing="0" cellpadding="0" bgColor="#FFFFFF"
								style="border: solid 1 black; width: 200; height: 100%">
									<tr>
										<td><img
											src="<%=request.getContextPath()%>/images/<%=languePage%>/labelRecherche.gif"></td>
										<td><img src="<%=request.getContextPath()%>/images/points.gif"></td>
										<td><img src="<%=request.getContextPath()%>/images/disc.gif"></td>
									</tr>
								</table>
							</div>
							</td>
						</tr>
						<TR>
							<TD>
								&nbsp;
							</TD>
						</TR>
						

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
			<TD bgcolor="#FFFFFF" colspan="3" align="center"><INPUT type="button" disabled name="btnOk" value="Ok" style="width:60" onClick="refreshEcritures(); showWaitingPopup();"></TD>
		</TR>
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
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> 


<script>
	function showWaitingPopup() {
		if (timeWaiting != -1 && timeWaitingId == -1)
			timeWaitingId = setTimeout("if (document.getElementById('dernieresEcritures').readyState!='complete') document.getElementById('waitingPopup').style.visibility='visible';",timeWaiting*1000);		
		
		return true;
	}
	
	document.getElementById("waitingPopup").style.left = document.body.clientWidth/2 - document.getElementById("waitingPopup").offsetWidth/2;
	document.getElementById("waitingPopup").style.top  = getAnchorPosition("waitingPopup").y + 290/2 - document.getElementById("waitingPopup").clientHeight/2;
	

	function fnStartInit() {		
   		if (document.getElementById("dernieresEcritures").readyState=="complete") {
			document.getElementById("waitingPopup").style.visibility="hidden";
			timeWaitingId = -1;

		}
	}
</script>

</BODY>
</HTML>