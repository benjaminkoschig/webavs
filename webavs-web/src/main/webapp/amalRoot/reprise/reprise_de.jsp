<%@page import="globaz.fweb.util.JavascriptEncoder"%>
<%@page import="ch.horizon.jaspe.util.JACalendar"%>
<%@page import="ch.horizon.jaspe.util.JAUtil"%>
<%@page import="globaz.jade.client.util.JadeDateUtil"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%@ include file="/theme/detail/header.jspf"%>

<script type="text/javascript" src="<%=servletContext%>/scripts/jquery.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/jquery-ui.js"></script>


<%-- tpl:put name="zoneInit" --%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.globall.util.JADate"%>
<%@page import="globaz.amal.vb.reprise.AMRepriseViewBean"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="ch.globaz.amal.business.constantes.IAMActions"%>
<%@page import="globaz.jade.log.*"%>
<%
	//
	idEcran="AM_Reprise_Tiers";

	boolean viewBeanIsNew = "add".equals(request.getParameter("_method"));
	boolean contribuableIsNew = false;

	AMRepriseViewBean viewBean = (AMRepriseViewBean) session.getAttribute("viewBean");

	// Disable button
	bButtonNew=false;
	bButtonUpdate=false;
	bButtonDelete=false;
	
	// Action par défaut, base
	String actionAfficher = IAMActions.ACTION_REPRISE+".afficher";
	String actionLaunchRepriseTiers = IAMActions.ACTION_REPRISE+".launchRepriseTiersJob";
	String actionCheckDB2Tables = IAMActions.ACTION_REPRISE+".launchCheckDB2TablesJob";
	String actionGenerateLibra = IAMActions.ACTION_REPRISE+".launchGenerateLibraJob";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/amal.css" rel="stylesheet" />
<%-- tpl:put name="zoneScripts" --%>
<script type="text/javascript">

$(function(){
	actionMethod=$('[name=_method]',document.forms[0]).val();
	userAction=$('[name=userAction]',document.forms[0])[0];

	// attribue une id à tous les champs ayant un nom mais pas encore d'id
	$('*',document.forms[0]).each(function(){
		if(this.name!=null && this.id==""){
			this.id=this.name;
		}
	});
	
});

function init(){
}

function postInit(){
	// Enable les boutons d'action
	$('#launchRepriseTiersJob').removeProp('disabled');
	$('#stopRepriseTiersJob').removeProp('disabled');
	// Enable les checkbox
	$('#checkBoxCheckTables').removeProp('disabled');
	$('#checkBoxSimulate').removeProp('disabled');
}

function executeNotepad(inputparms){
	// Instantiate the Shell object and invoke its execute method.
	var oShell = new ActiveXObject("Shell.Application");
	var commandtoRun = "C:\\Program Files (x86)\\Notepad++\\Notepad++.exe";
	// Invoke the execute method.  
  	oShell.ShellExecute(commandtoRun, inputparms,"", "open", "1");
}

$(document).ready(function() {	
	// Activer les actions sur les boutons
	// ----------------------------------------------------
	$(':button').click(function() {
		var idCurrent=this.id;
		var idButtonLaunchRepriseTiers = "launchRepriseTiersJob";
		var idButtonStopRepriseTiers = "stopRepriseTiersJob";
		var checkTables = false;
		var simulate = false;
		if($('#checkBoxCheckTables').prop('checked')){
			checkTables = true;	
		}
		if($('#checkBoxSimulate').prop('checked')){
			simulate = true;	
		}
		if(idCurrent==idButtonLaunchRepriseTiers){
		    document.forms[0].elements('userAction').value = "<%=actionLaunchRepriseTiers%>";
		    document.forms[0].elements('checkDataBeforeReprise').value = checkTables;
		    document.forms[0].elements('isSimulation').value = simulate;
		    document.forms[0].elements('askToStop').value = false;
			document.forms[0].submit();
		}
		if(idCurrent==idButtonStopRepriseTiers){
		    document.forms[0].elements('userAction').value = "<%=actionLaunchRepriseTiers%>";
		    document.forms[0].elements('askToStop').value = true;
			document.forms[0].submit();
		}
	});
	
	// Refresh the screen
	// ----------------------------------------------------------
	$('#refreshPageImg').click(function() {
		location.reload();
	});	

			
});


</script>

<style>
/* progress bar container */
#progressbar{
        border:1px solid black;
        width:200px;
        height:20px;
        position:relative;
        color:black; 
}
/* color bar */
#progressbar div.progress{
        position:absolute;
        width:0;
        height:100%;
        overflow:hidden;
        background-color:#369;
}
/* text on bar */
#progressbar div.progress .text{
        position:absolute;
        text-align:center;
        color:white;
}
/* text off bar */
#progressbar div.text{
        position:absolute;
        width:100%;
        height:100%;
        text-align:center;
}

</style>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:insert attribute="zoneTitle" --%>
Travaux de reprise AS/400 >> DB2
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>

 	<INPUT type="hidden" name="checkDataBeforeReprise" value="">
 	<INPUT type="hidden" name="isSimulation" value="">
 	<INPUT type="hidden" name="askToStop" value="">

	<%-- tpl:put name="zoneMain" --%>
	<tr>
		<td>
 			<div style="background-image:url('<%=request.getContextPath()%>/images/summary_bg.png');
 						background-repeat:repeat-x">
				<table id="tableauExplication"
						width="100%" 
						align="center" 
						style="border-collapse:collapse; font-size: 11px; border:2px solid #226194;">
					<tr>
						<td colspan="3">
		                   	<h4>Gestion de la reprise Tiers AS/400 -> DB2</h4><br>
		                   	Cet écran vous permet de contrôler le processus de reprise Tiers pour AMAL. Les étapes préalables doivent avoir été accomplies : 
		  					<ul>
		  						<li>Récupération des données d'origine : AS/400 CCJU -> PCORIG ASGLOB1
			  					<li>Préparation des tables DB2 : tables de reprises (RP_AMAL_xxxxxxxx, RP_INNER_MF) et tables finales prêtes sous DB2, ainsi que les codes systèmes chargés
			  					<li>Reprise Kettle : Reprise Amal refactored accomplie sans erreur.
		  					</ul>
							Pour cette étape, les actions possibles sont les suivantes :
							<ul>
								<li>Action 'Démarrer la reprise' : démarrer la reprise tiers
								<li>Option 'Contrôler les tables de reprise': contrôle les tables de reprise RP_AMAL_MACTBINF, RP_AMAL_MACONTRI, RP_AMAL_MAFAMILL, RP_INNER_MF
								<li>Option 'Simuler la reprise' : simuler la reprise tiers, permet d'avoir des indicateurs avant d'effectuer la reprise propement dite.
							</ul>
						</td>
						<td></td>
					</tr>
					<%
					if(viewBean.getRepriseStatus().equals("1")){
					%>
						<tr>
							<td align="right">
								Contrôler les tables de reprise
								<input id="checkBoxCheckTables" type="checkbox">
								</button>
							</td>
							<td align="right">
								Simuler la reprise
								<input id="checkBoxSimulate" type="checkbox" checked='yes'>
								</button>
							</td>
							<td></td>
							<td align="right">
								<button id="launchRepriseTiersJob" type="button">
											<b>Démarrer la reprise</b>
								</button>
							</td>
						</tr>
					<%
					}else if(viewBean.getRepriseStatus().equals("-1")){
					%>
						<tr>
							<td colspan="4" align="center">ARRET DE LA REPRISE EN COURS...
								<img id="refreshPageImg" 
									title="Rafraichir" 
									onMouseOver="this.style.cursor='hand';"
									onMouseOut="this.style.cursor='pointer';"
									src="<%=request.getContextPath()%>/images/amal/refresh-icon.png"/>
							</td>
						</tr>
					<%
					}else if(viewBean.getRepriseStatus().equals("2")){
					%>
						<tr>
							<td colspan = "3" align = "center">
								Reprise en cours - progression : <%=viewBean.getNbEnregistrementsEnCours()%>/<%=viewBean.getNbEnregistrements()%>
								<img id="refreshPageImg" 
									title="Rafraichir" 
									onMouseOver="this.style.cursor='hand';"
									onMouseOut="this.style.cursor='pointer';"
									src="<%=request.getContextPath()%>/images/amal/refresh-icon.png"/>
							</td>
							<td align="right">
								<button id="stopRepriseTiersJob" type="button">
											<b>Stopper la reprise</b>
								</button>
							</td>
						</tr>
					<%
					}
					%>					
					<tr><td>&nbsp;</td></tr>
				</table>
 			</div>
 		</td>
 	</tr>
 	<tr><td>&nbsp;<td></tr>
 	<tr>
 		<td>
			<table id="tableauLiensFichiers"
					width="100%" 
					align="center" 
					style="border-collapse:collapse; font-size: 11px; border:2px solid #226194;">
				<tr>
					<th style="border:0px"></th>
					<th style="border:0px">Mis à jour</th>
					<th style="border:0px"></th>
					<th style="border:0px">Créé</th>
					<th style="border:0px"></th>
					<th style="border:0px">En erreur</th>
					<th style="border:0px"></th>
					<th style="border:0px">Console</th>
					<th style="border:0px"></th>
				</tr>
				<tr class="amalRowOdd"><td colspan="9">&nbsp;</td></tr>
				<tr class="amalRow">
					<td></td>
					<td align="center"><a href='javascript:executeNotepad("d:/temp/lamal_reprise_updatedTiers.csv")'>Tiers mis à jour</a></td>
					<td></td>
					<td align="center"><a href='javascript:executeNotepad("d:/temp/lamal_reprise_newTiers.csv")'>Tiers créés</a></td>
					<td></td>
					<td align="center"><a href='javascript:executeNotepad("d:/temp/lamal_reprise_errorTiers.csv")'>Tiers en erreur</a></td>
					<td></td>
					<td align="center"><a href='javascript:executeNotepad("d:/temp/lamal_reprise_stderr.log")'>stderr.log</a></td>
					<td></td>
				</tr>
				<tr class="amalRowOdd"><td colspan="9">&nbsp;</td></tr>
				<tr class="amalRow">
					<td></td>
					<td align="center"><a href='javascript:executeNotepad("d:/temp/lamal_reprise_updatedAdresses.csv")'>Adresses mises à jour</a></td>
					<td></td>
					<td align="center"><a href='javascript:executeNotepad("d:/temp/lamal_reprise_newAdresses.csv")'>Adresses créées</a></td>
					<td></td>
					<td align="center"><a href='javascript:executeNotepad("d:/temp/lamal_reprise_errorAdresses.csv")'>Adresses en erreur</a></td>
					<td></td>
					<td align="center"><a href='javascript:executeNotepad("d:/temp/lamal_reprise_stdout.log")'>stdout.log</a></td>
					<td></td>
				</tr>
				<tr class="amalRowOdd"><td colspan="9">&nbsp;</td></tr>
			</table>
 		</td>
 	</tr>
	<%-- /tpl:put --%>

<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<ct:menuChange displayId="menu" menuId="amal-menuprincipal" showTab="menu" />

<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>