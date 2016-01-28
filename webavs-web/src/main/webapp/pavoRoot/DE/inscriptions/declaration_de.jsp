 <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<%@page import="globaz.pavo.util.CIUtil"%><HTML><!-- #BeginTemplate "/templates/processUpload.dwt" -->
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
<!-- #BeginEditable "zoneInit" --> 
<!-- Creer l'enregitrement s'il n'existe pas -->
<%@ page import="globaz.globall.util.*"%>
<%
	globaz.pavo.db.inscriptions.CIDeclarationViewBean viewBean = (globaz.pavo.db.inscriptions.CIDeclarationViewBean) session.getAttribute("viewBean");
	userActionValue = "pavo.inscriptions.declaration.executer";
	formAction= request.getContextPath()+mainServletPath+"Root/"+languePage+"/inscriptions/declarationFile_de.jsp";
	String jspLocation = servletContext + mainServletPath + "Root/tiForJournal_typeAff_select.jsp";
	
	int autoDigitAff = globaz.pavo.util.CIUtil.getAutoDigitAff(session);
	int tailleChampsAff = globaz.pavo.util.CIUtil.getTailleChampsAffilie(session);
	String idEcran = "CCI3001";
%>
<SCRIPT language="JavaScript">
top.document.title = "IK - Importierung von IK-Buchungen"

</SCRIPT>
<!-- #EndEditable -->
<!-- #BeginEditable "zoneBusiness" --> 
<%
	String eMailAddress = objSession.getUserEMail();
%>

<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<!-- #EndEditable -->
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
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/dates.js"></SCRIPT>
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
<!-- #BeginEditable "zoneScripts" --> 
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/autocomplete.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/selectionPopup.js"></SCRIPT>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

function init()
{
/*if (document.forms[0].elements('_method').value == "add")
   document.forms[0].elements('KcidIn').disabled = true;
   document.forms[0].elements('KcidLabel').disabled = true;
else
   document.forms[0].elements('KcidIn').disabled = true;
   document.forms[0].elements('KcidLabel').disabled = true;*/	
}
/*
*/


function validateform() {
	
	if (document.getElementsByName("filename")[0].value == "") {
		if(langue=='FR') {
			var value="Vous devez sélectionner un fichier."
			//var value="Sie müssen eine Datei auswählen."
		} else {
			var value="Sie müssen eine Datei auswählen."
		}
		alert(value);			
		return false;
	} else {
		if(document.getElementById("eMailAddress").value == ""){
			if(langue=='FR') {
				var value="Vous devez saisir une adresse e-mail";
			
			}else{
				var value="Sie müssen eine E-Mail Addresse eingeben";
			}
			alert(value);
			return false;
		}
	}
		return true;
	
}
function updateHiddenCa(){

	if(document.getElementById('ecrituresNegatives').checked){
		document.getElementById('accepteEcrituresNegatives').value = "True";
	}else{
		document.getElementById('accepteEcrituresNegatives').value = "False";
	}
	
}
function updateHiddenAnnee(){

	if(document.getElementById('anneeEnCours').checked){
		document.getElementById('accepteAnneeEnCours').value = "True";
	}else{
		document.getElementById('accepteAnneeEnCours').value = "False";
	}
	
}
function updateLienDraco(){

	if(document.getElementById('lienDraco').checked){
		document.getElementById('accepteLienDraco').value = "True";
	}else{
		document.getElementById('accepteLienDraco').value = "False";
	}
	
}
function updateSimulation(){
//	alert("simul");
	if(document.getElementById('simulationButton').checked){
		document.getElementById('simulation').value = "on";
		
	}else{
		document.getElementById('simulation').value = "";
		
	}
}

function updateInfoAffilie(tag) {
	if (tag.select && tag.select.selectedIndex != -1)
 		document.getElementById('infoAffilie').value = tag.select[tag.select.selectedIndex].nom;
}
function resetInfoAffilie() {
 	document.getElementById('infoAffilie').value = '';
}

// stop hiding -->
</SCRIPT>
<!-- #EndEditable -->
</HEAD>
<BODY onload="this.focus();showErrors();" onKeyDown="keyDown();actionKeyDown();" onKeyUp="keyUp();actionKeyUp();">
<TABLE bgcolor="#B3C4DB" cellspacing="0" cellpadding="0" width="100%" height="<%=tableHeight%>">
	<TBODY>
		<TR>
						<TH colspan="3" height="10" class="title">
				<DIV style="width: 100%">
					<SPAN class="idEcran"><%=(null==idEcran)?"":idEcran%></SPAN>
					Importierung von IK-Buchungen
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
			<FORM name="mainForm" action="<%=formAction%>" enctype='multipart/form-data' method="post"  >
				<TABLE border="0" cellspacing="0" cellpadding="0" width="<%=subTableWidth%>">
					<TBODY>
						<!-- #BeginEditable "zoneMain" --> 
          <tr> 
            <td>E-Mail Adresse</td>
			<td><input type="text" name="eMailAddress" maxlength="40" size="40" style="width:8cm;" value="<%=eMailAddress!=null?eMailAddress:""%>">&nbsp;*</td>
          </tr>
          
          <tr>
          	<td>Quelldatei</td>
          	<td>          		
          		<!-- <div style="padding:1; overflow: scroll"> -->
          		<input align="right"  type="file" size="65" name="filename" maxlength="256">&nbsp;*
          		<!-- </div> -->
			</td>
          </tr>
          
          <tr> 
            <td>Beitragsjahr</td>
            <td>
              <input type="text" onkeypress="return filterCharForInteger(window.event);" name="anneeCotisation" size="4" maxlength="4" value="<%=viewBean.getAnneeCotisation()!=null?viewBean.getAnneeCotisation():""%>">
               
              </td>
          </tr>
          <tr> 
            <td>Abr.-Nr.</td>
			<td> 
				<ct:FWPopupList name="forNumeroAffilie" maxlength="<%=tailleChampsAff%>" size="<%=tailleChampsAff%>" value='<%=viewBean.getForNumeroAffilie()!=null?viewBean.getForNumeroAffilie():""%>' jspName="<%=jspLocation%>" autoNbrDigit="<%=autoDigitAff%>" minNbrDigit="3" validateOnChange="true" onChange="updateInfoAffilie(tag);" onFailure="resetInfoAffilie()"/>
 				<input  type="text" class="disabled" readonly name="infoAffilie" size="50" maxlength="50" value="" tabIndex="-1">
              
            </td>
          </tr>
          <tr>
          	<td>Kontrolltotal</td>
          	<td>
          		<input 
          			onchange="validateFloatNumber(this);" 
          			onkeypress="return filterCharForFloat(window.event);" 
          			type="text" class="libelle" style="text-align : right" 
          			name="totalControle" 
          			value="<%=viewBean.getTotalControle()!=null?viewBean.getTotalControle():""%>">
          	</td>
          	
          </tr>
		  <tr>
          	<td>Anzahl Buchungen</td>
          	<td>
          		<input type="text" onkeypress="return filterCharForInteger(window.event);"  class="libelle" name="nombreInscriptions" style="text-align : right" value="">
          	</td>
          </tr>
          <tr>
          	<td>Datenformat</td>
          	<td>
          		<ct:FWCodeSelectTag name="type"  codeType="CITYDECL" wantBlank="false" defaut="<%=CIUtil.getCSDefaultDS(session)%>"/>
          	</td>
          </tr>

          <tr>
          <td>mit Minusbuchungen</td>
          	<td>
          		<input type="checkBox" name="ecrituresNegatives" onclick="updateHiddenCa()">
          		<input type="hidden" name="accepteEcrituresNegatives" value="False">

          </td>
          </tr>
          <tr>
           <td>mit Buchungen laufendes Jahr</td>
          	<td>
          		<input type="checkBox" name="anneeEnCours" onclick="updateHiddenAnnee()">
          		<input type="hidden" name="accepteAnneeEnCours" value="False">

          </td>
          </tr>
          <tr>
           <td>Verbindung mit Draco</td>
          	<td>
          		<input type="checkBox" name="lienDraco" onclick="updateLienDraco()">
          		<input type="hidden" name="accepteLienDraco" value="False">
          </td>
          </tr>
          <tr>
          	<td>
          		Empfangsdatum
          	</td>
          	<td>
          		<ct:FWCalendarTag name="dateReceptionForced" value=""/>
          	</td>
          </tr>
          <tr>
          	<td>Simulation</td>
          	<td>
          		<input type="checkBox" CHECKED name="simulationButton" onClick="updateSimulation()"  >
          		<input type="hidden" name="simulation" value="on">
          	</td>
          </tr>
          
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
		<TR>
			<TD bgcolor="#FFFFFF" colspan="3" align="center"><INPUT type="submit" value="Ok" style="width:60" onClick="if(validateform())document.forms[0].submit();"></TD>
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
<!-- #BeginEditable "zoneEndPage" --> 




<%  if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>

<%  }  %>
<script>
// menu 

//top.fr_menu.location.replace('appMenu.jsp?_optionMenu=-defaut-&changeTab=Menu');	
</script>
<!-- #EndEditable -->
</BODY>
<!-- #EndTemplate --></HTML>