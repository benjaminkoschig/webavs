<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@page import="globaz.pegasus.vb.demanderenseignement.PCDemandeRenseignementViewBean"%>
<%@page import="globaz.globall.db.BSessionUtil"%>
<%@page import="globaz.pegasus.vb.transfertdossier.PCDemandeTransfertRenteViewBean"%>
<%@page import="ch.globaz.pyxis.business.service.AdministrationService"%>
<%@page import="ch.globaz.pyxis.business.service.AdresseService"%>
<%@page import="globaz.pegasus.utils.PCGestionnaireHelper"%>
<%@page import="java.util.Iterator"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="ch.globaz.pegasus.business.constantes.EPCProperties"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%-- tpl:put name="zoneInit" --%>
<%
	// Les labels de cette page commence par la préfix "JSP_DEMANDE_RENSEIGNEMENT_RENTE"

	idEcran="PPC2007";
	
	PCDemandeRenseignementViewBean viewBean = (PCDemandeRenseignementViewBean)session.getAttribute("viewBean");
	
	String rootPath = servletContext+(mainServletPath+"Root");
	
	// Desactivation des boutons de détail
	bButtonCancel=false;
	bButtonUpdate=false;
	bButtonValidate=false;
	bButtonDelete = false;
%>
<%-- /tpl:put --%>

<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>


<%@ include file="/theme/detail/javascripts.jspf" %>
<script type="text/javascript" src="<%=servletContext%>/scripts/widget/globazwidget.js"></script> 
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/demanderenseignement/detail.css"/>
<SCRIPT language="javascript">
var userAction = "<%=IPCActions.ACTION_DEMANDE_RENSEIGNEMENT %>.executer";

var t_idCaissesRefusant='<%=BSessionUtil.getSessionFromThreadContext().getApplication().getProperty(EPCProperties.LIST_ID_CAISSE_REFUSANT.getProperty()) %>'.split(',');

$(function(){
	setPrintBouton();
	
	$("#leftPane li").on("mouseover",function(){$(this).addClass("hover");}).on("mouseout",function(){$(this).removeClass("hover");});
});

function setPrintBouton(){
	$('<input/>',{
		type: 'button',
		value:'<%=objSession.getLabel("JSP_DEMANDE_RENSEIGNEMENT_D_BTN_TRANSFERT") %>',
		id:'btnPrint',
		click:function(){
			$("[name=userAction]").val(userAction);
			//$("[name=annexes]").val(getSelectOptionValues($('[name=annexeSelect]')));
			//$("[name=copies]").val(getSelectOptionValues($('[name=copieSelect]')));		
			getSelectOptionValuesCopie($('#copieSelect'));
			getSelectOptionValuesAnnexe($('#annexeSelect'));
			$("[name=mainForm]").submit();
		}
	}).prependTo('#btnCtrlJade');
}

function getSelectOptionValuesCopie($select){
	var results=[];
	//Suppression de tous les input name==copieSelect (widget)
	
	$select.children('option').each(function(){
		results.push(this.value);
		$('[name=mainForm]').append("<input type='hidden' name='copies' value='"+this.value+"'/>");
	});
	
}

function getSelectOptionValuesAnnexe($select){
	var results=[];
	//Suppression de tous les input name==copieSelect (widget)
	
	$select.children('option').each(function(){
		results.push(this.value);
		$('[name=mainForm]').append("<input type='hidden' name='annexes' value='"+this.value+"'/>");
	});
	
}



function init(){}
function add(){}
function upd(){}
function del(){}

function readOnly(flag) {
 	// empeche la propriete disabled des elements etant de la classe css 'forceDisable' d'etre modifiee
}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_DEMANDE_RENSEIGNEMENT_D_TITRE"/> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
	<TR>
		<TD id="mainContainer" colspan="6" align="left">
								
			<!-- Menu des renseignements -->
			<div id="leftPane">
				<ul>
					<li class="selectedhover"><ct:FWLabel key="JSP_DEMANDE_RENSEIGNEMENT_D_AGENCE_COMMUNALE_AVS"/>Agence communale AVS</li>
				</ul>
			</div>
								
			<div id="centralPane">	
				<div id="ligneRequerant">
					<span class="label"><ct:FWLabel key="JSP_PC_DECSUPP_D_REQ"/></span>
					<span id="valRequerant"><%=viewBean.getRequerantInfos() %></span>
					<a id="aGed" href="#" target="GED_CONSULT"><ct:FWLabel key="JSP_PC_GED_LINK_LABEL"/></a>
					
				</div>	
				<div id="ligneGestionnaire">
					<span class="label"><ct:FWLabel key="JSP_DEMANDE_RENSEIGNEMENT_D_GESTIONNAIRE"/></span>
					<ct:FWListSelectTag data="<%=PCGestionnaireHelper.getResponsableData(objSession)%>" defaut="<%=controller.getSession().getUserId()%>" name="idGestionnaire"/>
					<span class="label" id="lblEmail"><ct:FWLabel key="JSP_DEMANDE_RENSEIGNEMENT_D_EMAIL"/></span>
					<span id="valEmail"><input type="text" name="mailAddress" value="<%=controller.getSession().getUserEMail()%>" /></span>					
				</div>
				
				<div id="ligneCaisseAgence">
				
					<span class="labelAnnexeCopie"><ct:FWLabel key="JSP_DEMANDE_RENSEIGNEMENT_D_AGENCE"/></span>
					<span><%=viewBean.getAgenceInfo() %></span>
					
				</div>
				<br>
				<div id="ligneTextLibre">
					<div class="labelAnnexeCopie"><ct:FWLabel key="JSP_DEMANDE_RENSEIGNEMENT_D_ZONE_TEXTE_LIBRE"/></div>
					<textarea name="zoneTexteLibre" rows="5" cols="60"></textarea>
				</div>
				
				<div>
					<div class="annexeCopie">
					<span class="labelAnnexeCopie"><ct:FWLabel key="JSP_DEMANDE_RENSEIGNEMENT_D_ANNEXES"/></span>
					<div data-g-multistring="tagName:annexeSelect,defaultValues:¦[{value:'<%=viewBean.getDefaultAnnexe() %>',readonly:true}]¦" ></div>
										<input type="hidden" name="annexeSelect" value="<%=viewBean.getDefaultAnnexe() %>"/>
					</div>	
					<div class="annexeCopie">
					<span class="labelAnnexeCopie"><ct:FWLabel key="JSP_DEMANDE_RENSEIGNEMENT_D_COPIES"/></span>
					<div data-g-multistring="tagName:copieSelect,mode:autocompletion,languages:¦Tiers,Administration¦,defaultValues:¦[{value:'<%=viewBean.getIdRequerant() %>',libelle:'<%=viewBean.getNomPrenomRequerant() %>'}]¦" >
						<input	id="widget-multistring-copies-tiers" 
							class="jadeAutocompleteAjax widgetTiers" 
							data-g-autocomplete="service:¦ch.globaz.pyxis.business.service.PersonneEtendueService¦,
												method:¦findByAlias¦,
												criterias:¦{
													forDesignation1Like: '<ct:FWLabel key="JSP_DEMANDE_RENSEIGNEMENT_W_NOM"/>',
													forDesignation2Like: '<ct:FWLabel key="JSP_DEMANDE_RENSEIGNEMENT_W_PRENOM"/>',
													forNumeroAvsActuel: '<ct:FWLabel key="JSP_DEMANDE_RENSEIGNEMENT_W_AVS"/>',
													forAlias: '<ct:FWLabel key="JSP_DEMANDE_RENSEIGNEMENT_W_ALIAS"/>'
												}¦,
												lineFormatter:¦#{tiers.designation1} #{tiers.designation2} #{personneEtendue.numAvsActuel}¦,
												modelReturnVariables:¦tiers.id,tiers.designation1,tiers.designation2,personneEtendue.numAvsActuel,personne.dateNaissance¦,
												functionReturn:¦
													function(element){
														return {
															id:$(element).attr('tiers.id'),
															text:$(element).attr('tiers.designation1')+' '+globazNotation.utilsString.toProperCase($(element).attr('tiers.designation2'))
														};
													}
												¦" 
							type="text" />	
						<input	id="widget-multistring-copies-admin" 
							class="widgetAdmin"
							data-g-autocomplete="service:ch.globaz.pyxis.business.service.AdministrationService,
												method:¦find¦,
												criterias:¦{
													forCodeAdministrationLike: 'Code administration'
												}¦,
												lineFormatter:¦#{tiers.designation1} #{tiers.designation2}, #{tiers.idTiers}¦,
												modelReturnVariables:¦tiers.id,tiers.designation1,tiers.designation2¦,
												functionReturn:¦
													function(element){
														return {
															id:$(element).attr('tiers.id'),
															text:$(element).attr('tiers.designation1')+' '+$(element).attr('tiers.designation2')
														};
													}
												¦" 
							type="text" />
																					
					</div>
					</div>	
					
					<input type="hidden" name="copieSelect" value="<%=viewBean.getIdRequerant() %>" />	
					<input type="hidden" name="idDossier" value="<%=viewBean.getIdDossier()%>"/>
								
				</div>
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