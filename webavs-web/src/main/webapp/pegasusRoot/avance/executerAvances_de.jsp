<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@page import="globaz.corvus.api.avances.IREAvances"%>
<%@page import="globaz.corvus.db.avances.REAvance"%>
<%@ page language="java" import="globaz.globall.http.*"  contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="ch.globaz.pyxis.business.model.PersonneEtendueComplexModel"%>
<%@page import="globaz.corvus.utils.REPmtMensuel"%>
<%@page import="globaz.pegasus.utils.PCDroitHandler"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDroits"%>
<%@page import="globaz.externe.IPRConstantesExternes"%>
<%@page import="ch.globaz.pegasus.business.models.droit.Droit"%>
<%@page import="globaz.pegasus.vb.avance.PCExecuterAvancesViewBean"%>
<%@page import="globaz.pegasus.utils.PCUserHelper"%>
<%@page import="java.util.Vector"%>
<%@ include file="/theme/detail_ajax/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>


<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/bootstrap.css"/>

<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%
	/**
	Mandat INFOROM 547 - 19.02.2013 - SCE
	Page d'exécution du traitement des avances pour le module PC
	Cette page permet de lancer le traitement de paiement des avances unqiues, et de sortir la liste des avances (global pour WebAvs)
	*/
	
	//Les labels de cette page commence par le préfix "JSP_PC_AVANCE_D"
	idEcran="PPC0112";

	bButtonNew = false;
	bButtonValidate = false; 
	bButtonDelete = false;
	bButtonUpdate = false;
	bButtonCancel = false;
	
	//recup eventuel message d'erreur
	String errrorMsg = request.getParameter("errorMsg");
	//retour process
	String processState = request.getParameter("process");
	boolean processOk = false;
	boolean fromProcess = false;
	
	if(processState!=null){
		processOk = processState!="ko";
		fromProcess = true;
	}
	PCExecuterAvancesViewBean viewBean = (PCExecuterAvancesViewBean)  session.getAttribute("viewBean");
	
%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%@ include file="/pegasusRoot/ajax/javascriptsAndCSS.jspf" %>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath+"Root")%>/scripts/avance/executerAvances_de.js"></script>
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/avance/executerAvances_de.css"></script>

<script type="text/javascript">
var processOk;
var fromProcess;
var lblProcessOk;
var lblProcessKo;

//Fonction validate, avant lancement process
function validate() {
	//récupération du type d'avance, 
	var typeAvance = $('#selectTypeTraitement option:selected').val();
	//on set user action
	document.forms[0].elements('userAction').value = "<%=IPCActions.ACTION_AVANCE_DETAIL_EXECUTE%>";
    var form=document.forms[0];
  //Si traitement liste, on check pas les paramètres mandatory, 
	if (typeAvance == <%=IREAvances.CS_TYPE_LISTES%>) {
		state = true;
	}else{
		//ok, si mandatory ok...
		state = notationManager.validateAndDisplayError();
	}
    return state;
}

function selectChangeISO20022(s) {
	changeAffichageISO20022(s[s.selectedIndex].id);
}

function changeAffichageISO20022(cs) {
	if(cs==258003){ // 258003 traitement ISO20022
		$('.classIso').css('display', 'block'); $('.classNonIso').css('display', 'none');
	} else {
		$('.classNonIso').css('display', 'block'); $('.classIso').css('display', 'none');
	} 
}
function selectChangePriority(s) {
	showPriorWarning(s[s.selectedIndex].value);
}
function showPriorWarning(val){
	if(val==1){
		$('.classPrioWarn').css('display', 'block');
	}else{
		$('.classPrioWarn').css('display', 'none');
	}
}
function init () {
	selectChangeISO20022(document.getElementById("idOrganeExecution"));
	selectChangePriority(document.getElementById("isoHighPriority"));
	processOk = <%= processOk %>;
	fromProcess = <%= fromProcess %>;
	lblProcessOk = "<%= objSession.getLabel("JSP_PC_PC_AVANCES_EXECUTER_D_PROCESS_FINISH") %>";
	lblProcessKo = "<%= objSession.getLabel("JSP_PC_PC_AVANCES_EXECUTER_D_PROCESS_FINISH_KO") %>";
	$(function () {
		dealInputEvents();
		dealGui();
	});
};

function add () {
	//date valeur
	$('#valdateValeur').attr('disabled','disabled');
	
};
function upd () {
	alert('upd');
};



</script>

<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_PC_AVANCES_EXECUTER_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<TR>
	<TD colspan="6" >
		<!--  ************************* Zone paramètres de base *************************  -->
		<div id="zone_param" class="titre">
			<h1 class="ui-widget-header "><ct:FWLabel key="JSP_PC_AVANCES_EXECUTER_D_PARAMETRES"/></h1>
			
			
			<!-- adresse email -->
			<div class="row-fluid topLine" >
				<div class="span2">
					<span id="lblEmail" class="lbl"><ct:FWLabel key="JSP_PC_AVANCES_EXECUTER_D_EMAIL"/></span>
				</div>
				<div class="span2">
					<input type="text" id="valAdresseemail" name="email" value="<%= objSession.getUserEMail() %>"/>
				</div>
			</div>
			
			<!--  Type d'avances -->
			<div class="row-fluid">
				<div class="span2">
					<span id="lblType" class="lbl"><ct:FWLabel key="JSP_PC_AVANCES_EXECUTER_D_TYPE"/></span>
				</div>
				<div class="span2">
					<ct:select  id="selectTypeTraitement" name="csTypeAvance" defaultValue="" wantBlank="false" >
						<ct:optionsCodesSystems csFamille="RETYPAVANC">	
							<ct:excludeCode code="52854001"/>											
						</ct:optionsCodesSystems>
					</ct:select>
				</div>
			</div>
			
			<!--  Date de valeur -->
			<div class="row-fluid">
				<div class="span2" >
					<span id="lblDateValeur" class="lbl dateValeurFields"><ct:FWLabel key="JSP_PC_AVANCES_EXECUTER_D_DATE_VALEUR"/></span>
				</div>
				<div class="span2">
					<input class="dateValeurFields" type="text" id="valdateValeur" name="dateVal" value="<%=REPmtMensuel.getDateDernierPmt(objSession)%>" />
				</div>
			</div>
			
		</div>	
		
		<!--  ************************* Zone ordre groupés *************************  -->
		<div id="zone_og" class="titre">
			<h1 class="ui-widget-header "><ct:FWLabel key="JSP_PC_AVANCES_EXECUTER_D_OG"/></h1>
			
			<!--  zone date échéances -->
			<div class="row-fluid topLine">
				<div class="span2">
					<span id="lblDateEcheance" class="lbl"><ct:FWLabel key="JSP_PC_AVANCES_EXECUTER_D_DATE_ECHEANCES"/></span>
				</div>
				<div class="span2">
					<input type="text" name="dateEcheance" data-g-calendar="mandatory:true" id="dateEcheance" />
				</div>
			</div>
			
			<!-- zone choix organe execution -->
			<div class="row-fluid">
				<div class="span2">
					<span id="lblOrganeExecution" class="lbl"><ct:FWLabel key="JSP_PC_AVANCES_EXECUTER_D_ORGANE_EX"/></span>
				</div>
				<div class="span2">
<%-- 					<ct:FWListSelectTag	name="idOrganeExecution" data="<%=viewBean.getOrganesExecution()%>" defaut="" /> --%>
				<% 	Vector<String[]> _CsOrganeExecution = viewBean.getOrganesExecution();%> 
				<select id="idOrganeExecution" name="idOrganeExecution" onchange="selectChangeISO20022(this)">
                <%for (int i=0; i < _CsOrganeExecution.size(); i++) {
					String[] _organeExecution = _CsOrganeExecution.get(i);%>
					
	                <option value="<%=_organeExecution[0]%>" id="<%=_organeExecution[2]%>"><%=_organeExecution[1]%></option>
	           	<%} %>
              </select>
				</div>
			</div>
			
			<!--  zone numéro ordre -->
			<div class="classNonIso">
				<div class="row-fluid">
					<div class="span2">
						<span id="lblNoOg" class="lbl"><ct:FWLabel key="JSP_PC_AVANCES_EXECUTER_D_NO_OG"/></span>
					</div>
					<div class="span2">
						<input type="text" data-g-integer="mandatory:true, sizeMax:2, autoFit:true" name="noOg" id="noOg" />
					</div>
				</div>
			</div>
			<div class="classIso">
				<div class="row-fluid">
					<div class="span2">
						<span id="lblGest" class="lbl"><ct:FWLabel key="JSP_PC_AVANCES_EXECUTER_D_ISO_GEST"/></span>
					</div>
					<div class="span2">
						<input type="text" name="isoGestionnaire" id="isoGestionnaire" value="<%=viewBean.getIsoGestionnaire()%>" data-g-string="mandatory:true" />
					</div>
				</div>
				<div class="row-fluid">
					<div class="span2">
						<span id="lblPrio" class="lbl"><ct:FWLabel key="JSP_PC_AVANCES_EXECUTER_D_ISO_PRIO"/></span>
					</div>
					<div class="span2">
						<select id="isoHighPriority" name="isoHighPriority" onchange="selectChangePriority(this)">
			                <OPTION selected value="0"><ct:FWLabel key="JSP_COMMOM_ISO_PRIO_NORMALE"/></OPTION>
			                <OPTION value="1"><ct:FWLabel key="JSP_COMMOM_ISO_PRIO_HAUTE"/></OPTION>
		              	</select><span class="classPrioWarn" style="color:red"><ct:FWLabel key="JSP_COMMOM_ISO_PRIO_WARNING"/></span>
					</div>
				</div>
			</div>
		</div>
			<!--  bouton lancer process -->
		<!--  ************************ Bouton lancer process de paiement ******************* -->
		<div id="zone_bouton_launch">
			<input type="button" id="lancer" name="lancer" value="<ct:FWLabel key="JSP_PC_AVANCES_EXECUTER_D_BUTTON"/>"/>
		</div>
			
		
		
		
				
		<!--  ************************* etat process running ************************* -->
		<div id="processRunning">
			<img id=processRunningGif src="<%= servletContext %>/images/loader_horizontal.gif"/><br />
			<label id="processRunningLbl" class="lbl"><ct:FWLabel key="JSP_PC_PC_AVANCES_EXECUTER_D_PROCESS_RUN"/></label>
		</div>
	</TD>
</TR>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>