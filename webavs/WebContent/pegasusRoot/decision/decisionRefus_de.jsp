	<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>

<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.pegasus.utils.PCGestionnaireHelper"%>

<%@page import="globaz.pegasus.vb.decision.PCDecisionRefusViewBean"%>
<%@page import="java.util.ArrayList" %>
<%@ page import="globaz.jade.json.MultiSelectHandler" %>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="ch.globaz.pegasus.business.services.models.home.HomeService"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDemandes"%>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>



<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/decision/detail.css"/>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%
	//Les labels de cette page commence par le préfix "JSP_PC_DEC_D"
	idEcran="PPC0082";
	
	PCDecisionRefusViewBean viewBean = (PCDecisionRefusViewBean) session.getAttribute("viewBean");
	

	autoShowErrorPopup = true;
	MultiSelectHandler handler = new MultiSelectHandler();
	bButtonDelete = false;
	btnNewLabel = "Ajouter";
	boolean viewBeanIsNew="add".equals(request.getParameter("_method"));
	
	//Labels pour les boutons non standard
	String btnPreVal = objSession.getLabel("JSP_PC_DECREF_D_BTN_PREVALIDER");
	String btnVal = objSession.getLabel("JSP_PC_DECREF_D_BTN_VALIDER");
	String titleErroxBox = objSession.getLabel("JSP_GLOBAL_ERROR_BOX_TITLE");//titre box erreur
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>

<%@ include file="/theme/detail/javascripts.jspf" %>
<%@ include file="/pegasusRoot/ajax/javascriptsAndCSS.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<!--  Pour gestion de la liste a deux niveau -->

<ct:menuChange displayId="menu" menuId="pegasus-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="pegasus-optionsempty">
</ct:menuChange>


<script language="JavaScript">
var MAIN_URL="<%=formAction%>";
//Tableau taille code systeme motif

 
function add() {}
function upd() {}
function init (){}
var ACTION_DECISION="<%=IPCActions.ACTION_DECISION%>";
var ACTION_DECISION_REFUS ="<%= IPCActions.ACTION_DECISION_DETAIL_REFUS %>";
var ACTION_DECISION_PREVALID = "<%=IPCActions.ACTION_DECISION_REF_PREVALIDATION%>";
var ACTION_DECISION_VALID = "<%=IPCActions.ACTION_DECISION_REF_VALIDATION%>"
var actionMethod;
var userAction;
var motifRenonciation = "<%= viewBean.getCsCodeRenonciation() %>";
var motifEntity = "<%= viewBean.getDecisionRefus().getSimpleDecisionRefus().getCsMotif() %>";
var $champsLibreMotif;
var $champMotif;
var $valChampLibreMotif;

$(function(){
	
	$champMotif = $('#motifListe');
	$champsLibreMotif = $('#champLibreMotif').hide();
	$valChampLibreMotif = $('#valChampLibreMotif');
	
	if(motifEntity === motifRenonciation){
		$champsLibreMotif.show();
	}
	//dealMotifRenonciation(motifRenonciation);
	
	$champMotif.change(function () {
		var csMotif = $('#motifListe :selected').attr('value');
		
		
		
		if(csMotif === motifRenonciation){	
			
			$champsLibreMotif.show();
		}else{
			$champsLibreMotif.hide();
			$valChampLibreMotif.html('');
			
		}
	});
	
	actionMethod=$('[name=_method]',document.forms[0]).val();
	userAction=$('[name=userAction]',document.forms[0])[0];
	
	//gestion des erreurs
	pegasusErrorsUtils.dealErrors(<%= request.getParameter("decisionErrorMsg") %>,"<%= titleErroxBox %>");
	
	// attribue une id à tous les champs ayant un nom mais pas encore d'id
	$('*',document.forms[0]).each(function(){
		if(this.name!=null && this.id==""){
			this.id=this.name;
		}
	});
	
	
	
	//Ajout bouton valider
	setPreValidBouton(true);
	<%
	if(viewBean.isPreValider()){%>	
		setValidBouton();
		setPreValidBouton(false);
	<%}%>

	<%
	if(viewBean.isValider()){%>	
	//on enleve le bouton modifier
		$('#btnUpd').hide();
		setPreValidBouton(false);
	<%}%>
	
	//Gestion evenement onclick sur bouton widget adresse
	$('#buttonAdresse').click(function(){
		$('#homeWidget1').css('margin-left','10px');
		$('#homeWidget1').show();
		$('#homeWidget1').focus();
	});

	$('#homeWidget1').blur(function(){
		$('#homeWidget1').hide();	
	});
	

	//valeur init adresse widget
	$('#homeWidget1').hide();
	$('#buttonAdresse').css('margin-left','70px');
});



function cancel() {
	userAction.value=ACTION_DECISION_REFUS+".reAfficher";
}  

function validate() {
	
	state = true;
	userAction.value=ACTION_DECISION_REFUS+".modifier";
	return state;
}    

function validateDecision() {
	state = true;
	userAction.value=ACTION_DECISION_VALID;
	return state;
}    

function preValidateDecision() {
	
	state = true;
	userAction.value=ACTION_DECISION_PREVALID;
	return state;
}   

function del() {
    if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
    	userAction.value=ACTION_DEMANDE+".supprimer";
        document.forms[0].submit();
    }
}

function hideInputC(){
	$('#homeWidget1').hide();
	$('#homeWidget1').val('');
}



function setPreValidBouton(state){

	if(state){
		$('<input/>',{
			type: 'button',
			value:'<%= btnPreVal %>',
			id:'btnPreValid',
			click:function(){

			
			if(preValidateDecision()){
				action(COMMIT);
			} 
		}
		}).prependTo('#btnCtrlJade');
	}
	else{
		$('#btnPreValid').hide();
	}
	
}

function setValidBouton(){
	//Ajout bouton valider
	$('<input/>',{
		type: 'button',
		value:'<%= btnVal %>',
		id:'btnValid',
		click:function(){
			
			if(validateDecision()){
				action(COMMIT);
			} 
		}
	}).prependTo('#btnCtrlJade');

}

	
var multiSelectData = <%= handler.createMotifsWithSousMotifs(viewBean.CS_MOTIF) %>;
var initValue = {"masterValue":"<%= viewBean.getDecisionRefus().getSimpleDecisionRefus().getCsMotif()%>","childValue":"<%= viewBean.getDecisionRefus().getSimpleDecisionRefus().getCsSousMotif()%>"};


</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
	<span class="postItIcon" data-g-note="idExterne:<%=viewBean.getDecisionRefus().getSimpleDecisionRefus().getIdDecisionHeader()%>, 
					tableSource: PCDECHEA"></span>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_PC_DECREF_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
	<TR>
		<TD colspan="6" id="mainTd" align="center">
			<!--  bloc zone informations générales -->
			<div id="zoneInfoRefus">
				
				<div id="ligneRequerant">
					<span class="label"><ct:FWLabel key="JSP_PC_DECREF_D_REQ"/></span>
					<span id="valRequerant"><%=viewBean.getRequerantInfos() %></span>
					<a id="aGed" href="#"><ct:FWLabel key="JSP_PC_GED_LINK_LABEL"/></a>
				</div>	
				<!--  adresse de courrier label et widget -->
				<div id="adresseZone">
					<div id="blocAdrCRS">
						<span id="labelAdresse" class="label" >
							<ct:FWLabel key="JSP_PC_DECREF_D_ADRCOUR"/>
							<button id="buttonAdresse">
								...
							</button>
						</span>
					    
					    	<div id="descAdresse">
						    	<pre><span class="detailAdresseTiersC"><%= viewBean.getAdresseCourrier() %></span></pre>
		       					<input type="hidden" class="idHome" />
								<ct:widget id='<%="homeWidget1"%>' name='<%="homeWidget1"%>' styleClass=" selecteurHome">
									<ct:widgetService methodName="search" className="<%=HomeService.class.getName()%>">
										<ct:widgetCriteria criteria="likeNumeroIdentification" label="JSP_PC_TAXE_JOURNALIERE_HOME_W_NO_IDENTIFICATION"/>								
										<ct:widgetCriteria criteria="likeDesignation" label="JSP_PC_TAXE_JOURNALIERE_HOME_W_DESIGNATION"/>
										<ct:widgetCriteria criteria="likeNpa" label="JSP_PC_TAXE_JOURNALIERE_HOME_W_NPA"/>									
										<ct:widgetCriteria criteria="likeLocalite" label="JSP_PC_TAXE_JOURNALIERE_HOME_W_LOCALITE"/>								
										<ct:widgetLineFormatter format="${simpleHome.numeroIdentification} ${simpleHome.nomBatiment} ${(tiersHome.tiers.titreTiers)} ${tiersHome.tiers.designation2} ${tiersHome.tiers.designation1} - (${tiersHome.localite.numPostal} ${tiersHome.localite.localite})"/>
										<ct:widgetJSReturnFunction>
											<script type="text/javascript">
												function(element){
													$(this).parents('.descAdresse').find('.idHome').val($(element).attr('simpleHome.id'));
													$(this).parents('.descAdresse').find('.idHome').trigger('change');
													this.value=$(element).attr('simpleHome.numeroIdentification')+' '+$(element).attr('simpleHome.nomBatiment')+' '+$(element).attr('tiersHome.tiers.designation2')+' '+$(element).attr('tiersHome.tiers.designation1')+' '+$(element).attr('simpleHome.numeroIdentification');
													hideInputC();
													buildStringC(element);
													}
											</script>										
										</ct:widgetJSReturnFunction>
									</ct:widgetService>
								</ct:widget>
						   		
						    </div>
						   
					   
					</div>
					
				</div>
		
				<div id="ligneDemande">	
					<span class="label"><ct:FWLabel key="JSP_PC_DECREF_D_NO_DEMANDE"/></span>
					<span id="nodemande"><%=viewBean.getIdDemande() %></span>
				
					<span class="label" id="lblEtatRef"><ct:FWLabel key="JSP_PC_DECREF_D_ETAT"/></span>
					<span id="valEtat"><%=objSession.getCodeLibelle(viewBean.getDecisionRefus().getDecisionHeader().getSimpleDecisionHeader().getCsEtatDecision())%></span>
				</div>
				
				<div id="ligneDecision">
					<span class="label"><ct:FWLabel key="JSP_PC_DECREF_D_DATE_DECISION"/></span>
					<span id="valDecision"><%= viewBean.getDecisionRefus().getDecisionHeader().getSimpleDecisionHeader().getDateDecision()%></span>
				
					<span class="label" id="lblNoDecisionRef"><ct:FWLabel key="JSP_PC_DECREF_D_NO_DECISION"/></span>
					<span id="valDecision"><%= viewBean.getDecisionRefus().getDecisionHeader().getSimpleDecisionHeader().getNoDecision()%></span>
				</div>
				<div id="ligneDecisionCompl">
					<span class="label"><ct:FWLabel key="JSP_PC_DECREF_D_DATE_REFUS"/></span>
						<span id="dateRefus">
							<input type="text"  data-g-calendar="mandatory:true" name="decisionRefus.simpleDecisionRefus.dateRefus" value="<%= viewBean.getDecisionRefus().getSimpleDecisionRefus().getDateRefus()%>"</span>
					</span>
					
					<span class="label" id="lblGenreDecRef"><ct:FWLabel key="JSP_PC_DECREF_D_GENREDEC"/></span>
					<span id="valGenreRef"><%= objSession.getCodeLibelle(viewBean.getDecisionRefus().getDecisionHeader().getSimpleDecisionHeader().getCsGenreDecision())%></span>
				
				</div>
					
					
					
				
					
				<div id="lignePersonneRef">
					<span class="label"><ct:FWLabel key="JSP_PC_DECREF_D_PERSONEREF"/></span>
					<ct:FWListSelectTag data="<%=PCGestionnaireHelper.getResponsableData(objSession)%>" defaut="<%=viewBean.getGestionnaire()%>" name="decisionRefus.decisionHeader.simpleDecisionHeader.preparationPar"/>
				</div>
				
<!--					<div id="zoneInfosRefusSupp">-->
<!--						<div class="selectM">-->
<!--							<span class="labelRefus">-->
<!--								<ct:FWLabel key="JSP_PC_PREP_DECISION_REFUS_DE_MOTIF"/>-->
<!--							</span>-->
<!--							<span id="selectMotifSpan">-->
<!--								<ct:select styleClass="motif"  id="selectMotif" name="decisionRefus.simpleDecisionRefus.csMotif" wantBlank="true">-->
<!--									<ct:optionsCodesSystems csFamille="PCMOTIFDER">-->
<!--									</ct:optionsCodesSystems>-->
<!--								</ct:select>-->
<!--							</span>-->
<!--						</div>-->
<!--						<div class="selectSm">-->
<!--							<span class="labelRefus">-->
<!--								<ct:FWLabel key="JSP_PC_PREP_DECISION_REFUS_DE_SOUSMOTIF"/>-->
<!--							</span>-->
<!--							<span>-->
<!--								<select id="selectSousMotif" name="decisionRefus.simpleDecisionRefus.csSousMotif"></select>-->
<!--							</span>-->
<!--						</div>-->
<!--				</div>-->
				<div id="zoneInfosRefusSupp">
						
						<label id="motifsLabel"><ct:FWLabel key="JSP_PC_PREP_DECISION_REFUS_DE_MOTIF"/>
						</label>
						<select id="motifListe" name="decisionRefus.simpleDecisionRefus.csMotif" data-g-multiselect=" dataList:multiSelectData,initValues :initValue,subLevelId:sousMotifListe,subLevelLibelle:sous-motifs"></select>
						<label id="sousMotifsLabel"></label>
						<select id="sousMotifListe" name="decisionRefus.simpleDecisionRefus.csSousMotif"></select>
					</div>
					
					<div id="champLibreMotif">
						<span id="lblChampLibreMotif">
								<ct:FWLabel key="JSP_PC_PREP_DECISION_REFUS_DE_CHAMP_LIBRE_MOTIF"/>
							</span>
							<span id="spanChampLibreMotif">
								<textarea id="valChampLibreMotif" data-g-string="sizeMax:255" name="decisionRefus.simpleDecisionRefus.champLibreMotif" maxlength="255" ><%=viewBean.getDecisionRefus().getSimpleDecisionRefus().getChampLibreMotif()%></textarea>
							</span>
					</div>
			</div>
			<input type="hidden" name="idDemandePc" value="<%=viewBean.getIdDemande()%>"/>
			<input type="hidden" name="idDecision" value="<%=viewBean.getIdDecision() %>"/>
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