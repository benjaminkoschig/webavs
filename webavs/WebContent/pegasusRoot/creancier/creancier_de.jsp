<%@page import="ch.globaz.pegasus.navigation.NavigatorInterface"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.framework.menu.FWMenuBlackBox"%>
<%@page import="ch.globaz.pegasus.navigation.creancier.PCCreancierLink"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="ch.globaz.pegasus.business.models.creancier.SimpleCreancier"%>
<%@page import="ch.globaz.pegasus.business.models.droit.DroitMembreFamilleEtendu"%>
<%@page import="ch.globaz.hera.business.models.famille.MembreFamille"%>
<%@page import="globaz.jade.persistence.model.JadeAbstractModel"%>
<%@page import="ch.globaz.pyxis.business.service.PersonneEtendueService"%>
<%@page import="ch.globaz.pyxis.business.service.AdministrationService"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCCreancier"%>
<%@page import="globaz.externe.IPRConstantesExternes"%>
<%@page import="globaz.pegasus.vb.creancier.PCCreancierViewBean"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDroits"%>
<%@page import="ch.globaz.pegasus.business.models.droit.DroitSearch"%>
<%@page import="ch.globaz.pegasus.business.models.droit.Droit"%>
<%@page import="ch.globaz.pegasus.business.services.PegasusServiceLocator"%>
<%@ page language="java" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="globaz.globall.db.BSession"%>
<%@page import="globaz.pegasus.utils.PCCreancierHandler"%>
<%@ include file="/theme/detail_ajax/header.jspf" %>
<%
// Les labels de cette page commence par la préfix "JSP_PC_CREANCIER_R"
	idEcran="PPC0050";
	//IFrameDetailHeight = "520";
	String idDemande = request.getParameter("idDemandePc");
	String idDecision = request.getParameter("idDecision");
	
	actionNew = actionNew+"&idDemandePc="+idDemande;
	
	PCCreancierViewBean viewBean = (PCCreancierViewBean)session.getAttribute("viewBean");
	//boolean viewBeanIsValid = "fail".equals(request.getParameter("_valid"));
	boolean viewBeanIsNew= true;
	bButtonCancel = true; 
	bButtonValidate = true;
	bButtonDelete = true;
	selectedIdValue = viewBean.getId();
	JadeAbstractModel[] listAssuree = viewBean.getListAssure();
	SimpleCreancier sCreancier =  viewBean.getCreancier().getSimpleCreancier();
	//bButtonNew = "add".equals(request.getParameter("_method"));
	bButtonDelete = !PegasusServiceLocator.getCreancierService().hasCreanceAccordee(selectedIdValue);
	
	boolean afficherRepartireCrance = true;
	
	
	if(viewBean.isDroitCourantCalculeOrValide()){
		afficherRepartireCrance = true;
	}else{
		afficherRepartireCrance = false;
	}
%>
 
<%-- tpl:insert attribute="zoneInit" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>
<%@ include file="/pegasusRoot/ajax/javascriptsAndCSS.jspf" %>

<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/droit/droit.css"/>

<%NavigatorInterface linkNavBar = new PCCreancierLink(idDemande); %>
<%@ include file="/pegasusRoot/scripts/navBar.jspf" %>
<ct:menuChange displayId="menu" menuId="pegasus-menuprincipal" showTab="options"/>
<ct:menuChange displayId="options" menuId="pegasus-optionscreanciers">
	<%if (idDecision == null || idDecision.isEmpty()) {%>
		<ct:menuActivateNode active="no" nodeId="DETAIL_DECISION"/>
		<ct:menuChange displayId="options" menuId="pegasus-optionsempty"/>
	<%} else {%>
		<ct:menuActivateNode active="yes" nodeId="DETAIL_DECISION"/>
		<ct:menuSetAllParams key="idDecision" value="<%= idDecision %>"/>
	<%}%> 
</ct:menuChange>



<script language="JavaScript">

	var linkToFromDecision = <%= idDecision != null && !idDecision.isEmpty()%>;
	var bFind = true;
	var detailLink = "<%=actionNew%>";
	var JSP_DELETE_MESSAGE_INFO="<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>";
	var ACTION_CREANCE_ACCORDEE= "<%=IPCActions.ACTION_CREANCE_ACCORDEE%>";
	var ACTION_AJAX_CREANCIER = "<%=IPCActions.ACTION_CREANCIER_AJAX%>";
	var CS_DOMAINE_APPLICATION_RENTE =  "<%=IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE%>";
	var messageDelete = "<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>";
	var LANGUAGES ="<ct:FWLabel key='JSP_PC_CREANCIER_W_CREANCIER'/>";
	var LABEL_BUTTON_CRANCE_ACCRODEE='Répartir les créances';
	var aucuneAdresseDePaiement = "<ct:FWLabel key='JSP_PC_CREANCIER_L_AUCUNE_ADRESSE'/>"; 
	
	//var usrAction = "<%=IPCActions.ACTION_CREANCIER + ".lister" %>";
	var idDemande = <%=idDemande%>;
	var b_afficheBoutton = <%=Boolean.toString(afficherRepartireCrance)%>
	var creance = {
			addButtonRepartirCreance: function(){
				var $userAction = $('[name=userAction]');
				var $form= $('form')
				var s_target=$form.attr('target');
				$('<input/>',{
					type: 'button',
					value:LABEL_BUTTON_CRANCE_ACCRODEE,
					click: function() {
						$userAction.val(ACTION_CREANCE_ACCORDEE+".afficher"); 
						$form.attr('target','_self');
						//$form.attr('target','fr_detail');
						$form.submit();
						form.attr('target',s_target);
					},
					"class": 'btnCtrl'
				}).insertBefore('#afficheRepartition');
			}
	}
	$(function (){ 
		//creance.addButtonRepartirCreance();
		var $repartirCreance = $('#afficheRepartition');
		$repartirCreance.button();
		$repartirCreance.hide();
		if(b_afficheBoutton){
			
			//if(linkToFromDecision){
				var href = $repartirCreance.prop('href');
				var str = href.concat('&idDecision=<%= idDecision %>');
				$repartirCreance.attr('href',str);
			//}
			
			$repartirCreance.show();
		}
	})


</script>
<style>
	.span span {
		padding-right:25px;
		padding-left:5px;
	}
	label{
		padding:5px 15px 0 10px;
		color: gray;
	}
	.ui-widget h1 {
    	font-size: 10pt;
    	margin: 10px 0 0 2px;
	}
</style>

<script type="text/javascript" src="<%=rootPath %>/scripts/creancier/creancierPart.js"></script>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax/bodyStart.jspf" %>
				<%-- tpl:insert attribute="zoneTitle" --%>
				<ct:FWLabel key="JSP_PC_CREANCIER_D_CREANCIER"/>
				<%-- /tpl:insert --%>
				
<%@ include file="/theme/detail_ajax/bodyStart2.jspf" %>
						<%-- tpl:insert attribute="zoneMain" --%>
	 <tr>
		<td colspan="3"><%=viewBean.getRequerantDetail()%></td>		
	 </tr>
	 
	 <tr>
	 	<td colspan="3">
 		 	<div class="ui-widget">
				<h1 class="ui-widget-header">Infos Spécifique</h1>
				<div class="ui-widget-content">
					<label><ct:FWLabel key="JSP_PC_CREANCIER_R_ID_DEMANDE" /></label>
					<span><%= idDemande%></span>

					<%
					if(viewBean.isDroitCourantCalculeOrValide() && viewBean.getMontantADisposition(idDemande) > 0){
					%>
						<label><ct:FWLabel key="JSP_PC_CREANCIER_R_MONTANT_DISPO" /></label>
						<span><strong><%= new FWCurrency(viewBean.getMontantADisposition(idDemande)).toStringFormat() %></strong></span>
					<%
					}
					%>
				</div>
			</div>
	 	</td>
	</tr>
	
		
	<tr>
		<td colspan="3">&nbsp;</td>
	</tr>
	
	<tr>
		<td colspan="3" style="text-align: right;">	
			<a id="afficheRepartition" href="?userAction=<%=IPCActions.ACTION_CREANCE_ACCORDEE+".afficher&idDemandePc="+idDemande %>">
				<ct:FWLabel key='JSP_PC_CREANCIER_D_REPARTITION'/>
			</a>
		</td>
	</tr>
	
	
	<tr>
		<td colspan="3">&nbsp;</td>
	</tr>
	
	<tr>
		<td colspan="3">
			<div class="area">
			
				<table class="areaTabel" width="100%">
					<thead>
						<tr> 
					    	<th data-orderKey='nomPrenom'><ct:FWLabel key="JSP_PC_CREANCIER_L_CREANCIER"/></th>
					 		<th data-orderKey="montant" data-g-amountformatter="blankAsZero:false"><ct:FWLabel key="JSP_PC_CREANCIER_L_MONTANT_REVENDIQUE"/></th>
					 		<th class="notSortable" data-g-amountformatter="blankAsZero:false" ><ct:FWLabel key="JSP_PC_CREANCIER_L_MONTANT_MONTANT_REPARTI"/></th>
					 		<th><ct:FWLabel key="JSP_PC_CREANCIER_L_MONTANT_TYPE"/></th>
					 		<th><ct:FWLabel key="JSP_PC_CREANCIER_L_MONTANT_NO"/><input type="hidden" name="creancierSearch.forIdDemande" value="idDemande" /></th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
				
				<div id="main" class="formTableLess areaDetail"> 
					<table>
						<tr>
							<td>
								<label for="creancier.simpleCreancier.csTypeCreance">
									<ct:FWLabel key="JSP_PC_CREANCIER_D_TYPE_CREANCE" />
								</label>
							</td>
							<td>
								<ct:select id="csTypeCreance"
								           name="creancier.simpleCreancier.csTypeCreance" 
										   wantBlank="false"  
								           notation="data-g-select='mandatory:true'" >
								 	<ct:optionsCodesSystems csFamille="PCTYPECRE"/>
								</ct:select>
							</td>
							<td>
								<label for="creancier.simpleCreancier.montant">
									<ct:FWLabel key="JSP_PC_CREANCIER_D_MONTANT" />
								</label>
							</td>
							<td>
								<span class='td'>
									<ct:inputText name="creancier.simpleCreancier.montant" id="montant" notation="data-g-amount='mandatory:true'" /> 
								</span>
								</td>
							<td>
								<label for="creancier.simpleCreancier.idTiersRegroupement">
									<ct:FWLabel key="JSP_PC_CREANCIER_D_ASSURE" />
								</label>
							</td>
							<td>
								<ct:select name="creancier.simpleCreancier.idTiersRegroupement" 
										   id="idTiersRegroupement"
										   wantBlank="true" >
								    <%for(JadeAbstractModel model:listAssuree){ 
								        MembreFamille membre = ((DroitMembreFamilleEtendu)model).getDroitMembreFamille().getMembreFamille();
								     	String desc = membre.getPrenom()+" "+membre.getNom(); 
								     %>
										<ct:option id="<%=membre.getPersonneEtendue().getTiers().getIdTiers()%>" value="<%=membre.getPersonneEtendue().getTiers().getIdTiers()%>"  label="<%=desc%>"/>
									<%} %>
								</ct:select>
						</td>
					</tr>
					<tr id="adresseCreancier">
						<td>
							<label for="creancier.simpleCreancier.idTiers">
								<ct:FWLabel key="JSP_PC_CREANCIER_D_CREANCIER" />
							</label>
							<input type="hidden" 
							       name="creancier.simpleCreancier.idTiers"
								   id='idTiers'   
								   value="" />
						</td>
						<td>	
						<div data-g-multiwidgets="languages:LANGUAGES,widgetEtendu:true,mandatory:true" class="multiWidgets">	
			
							<ct:widget id='widgetTiers' name='widgetTiers' 
							           defaultValue="<%=viewBean.getInfoTiers()%>"
							           styleClass="widgetTiers">
								<ct:widgetService methodName="findByAlias" className="<%=ch.globaz.pyxis.business.service.PersonneEtendueService.class.getName()%>">
									<ct:widgetCriteria criteria="forDesignation1Like" label="JSP_PC_CREANCIER_W_NOM"/>								
									<ct:widgetCriteria criteria="forDesignation2Like" label="JSP_PC_CREANCIER_W_PRENOM"/>
									<ct:widgetCriteria criteria="forNumeroAvsActuel" label="JSP_PC_CREANCIER_W_AVS"/>									
									<ct:widgetCriteria criteria="forDateNaissance" label="JSP_PC_CREANCIER_W_NAISS"/>		
									<ct:widgetCriteria criteria="forAlias" label="JSP_PC_CREANCIER_W_ALIAS"/>																				
									<ct:widgetLineFormatter format="#{tiers.designation1} #{tiers.designation2} #{personneEtendue.numAvsActuel} #{personne.dateNaissance}"/>
									<ct:widgetJSReturnFunction>
										<script type="text/javascript">
											function(element){
												$('#idTiers').val($(element).attr('tiers.id')).change();
												$('#idTiersAdressePaiement').val($(element).attr('tiers.id'));
												$('#idDomaineApplicatif').val(CS_DOMAINE_APPLICATION_RENTE);
												this.value=$(element).attr('tiers.designation1')+' '+$(element).attr('tiers.designation2');
											}
										</script>										
									</ct:widgetJSReturnFunction>
									</ct:widgetService>
							</ct:widget>
							<ct:widget  id='widgetAdmin' name='widgetAdmin' 
							            styleClass="widgetAdmin"  
							            defaultValue="<%=viewBean.getInfoTiers()%>">
								<ct:widgetService methodName="find" className="<%=ch.globaz.pyxis.business.service.AdministrationService.class.getName()%>">										
									<ct:widgetCriteria criteria="forDesignation1Like" label="JSP_PC_CREANCIER_W_CODE_DESIGNATION"/>	
									<ct:widgetCriteria criteria="forCodeAdministrationLike" label="JSP_PC_CREANCIER_W_CODE_ADMIN"/>	
									<ct:widgetCriteria criteria="forCanton" label="JSP_PC_CREANCIER_W_CODE_CANTON"/>																
									<ct:widgetLineFormatter format="#{tiers.designation2} #{tiers.designation1}, #{tiers.idTiers} "/>
									<ct:widgetJSReturnFunction>
										<script type="text/javascript">
											function(element){
												$('#idTiers').val($(element).attr('tiers.id')).change();
												$('#idTiersAdressePaiement').val($(element).attr('tiers.id'));
												$('#idDomaineApplicatif').val(CS_DOMAINE_APPLICATION_RENTE);
												this.value=$(element).attr('tiers.designation1')+' '+$(element).attr('tiers.designation2');
											}
										</script>										
									</ct:widgetJSReturnFunction>
								</ct:widgetService>
							</ct:widget>
						</div>
						</td>
						<td>
						</tr>
						<tr>
							<td>            
								<label for="creancier.simpleCreancier.idTiersAdressePaiement">
									<ct:FWLabel key="JSP_PC_CREANCIER_D_ADRESSE_PAIEMENT" />
								</label> 
							</td> 
							<td colspan="5">
								<div data-g-adresse="service:findAdressePaiement" id="adressePaiement" defaultvalue="" >
									<input type="hidden" id="idTiersAdressePaiement" class="avoirPaiement.idTiers"
									       name="creancier.simpleCreancier.idTiersAdressePaiement"  value="" /> 
									<input type="hidden" id="idDomaineApplicatif" class="avoirPaiement.idApplication"
									       name="creancier.simpleCreancier.idDomaineApplicatif"  value="" />
									<input type="hidden" id="idAffilieAdressePaiment" class="avoirPaiement.idExterne"
									       name="creancier.simpleCreancier.idAffilieAdressePaiment" value="" />
								</div>
							</td>
					</tr>
					<tr>
						<td>
							<label for="creancier.simpleCreancier.referencePaiement">
								<ct:FWLabel key="JSP_PC_CREANCIER_D_REFERENCE_PAIEMENT" />
							</label>
						</td>
						<td colspan="5">
							<textarea rows="2" cols="40" id="referencePaiement" name="creancier.simpleCreancier.referencePaiement" >
							
							</textarea>
						</td>
					</tr>
					<tr>
						<td colspan="6">
							<div class="btnAjax">
								<input class="btnAjaxUpdate" type="button" value="<%=btnUpdLabel%>">
								<input class="btnAjaxDelete" type="button" value="<%=btnDelLabel%>">
								<input class="btnAjaxAdd" type="button" value="<%=btnNewLabel%>">
								<input class="btnAjaxValidate" type="button" value="<%=btnValLabel%>">
								<input class="btnAjaxCancel" type="button" value="<%=objSession.getLabel("JSP_PC_SGL_D_ANNULER")%>">
							</div>
						</td>
					</tr>
				</table>
				</div>
			</div>
		</td>
	</tr>
	<input type="hidden" name="idDecision" value="<%= idDecision %>" />
	 		<%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/footer.jspf" %>
