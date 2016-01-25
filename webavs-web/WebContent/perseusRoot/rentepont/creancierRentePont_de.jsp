<%@page import="ch.globaz.perseus.business.constantes.CSTypeCreance"%>
<%@page import="globaz.perseus.vb.rentepont.PFCreancierRentePontViewBean"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="globaz.perseus.utils.PFUserHelper"%>
<%@page import="ch.globaz.pyxis.business.model.PersonneEtendueComplexModel"%>
<%@page import="ch.globaz.perseus.business.constantes.IPFConstantes"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail_ajax/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>

<%

	idEcran="PPF1241";

	PFCreancierRentePontViewBean viewBean = (PFCreancierRentePontViewBean)session.getAttribute("viewBean");
	String idRentePont = viewBean.getIdRentePont();	
	
	PersonneEtendueComplexModel personne = viewBean.getRentePont().getDossier().getDemandePrestation().getPersonneEtendue();
	String affichePersonnne = "";
	
	affichePersonnne = PFUserHelper.getDetailAssure(objSession,personne);

%>

<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>

<% String rootPath = servletContext+(mainServletPath+"Root");%>
<script type="text/javascript" src="<%=rootPath %>/scripts/rentepont/creancierRentePontPart.js"></script>
<link rel="stylesheet" type="text/css" href="<%=servletContext%>/theme/widget.css"/>
<script type="text/javascript" src="<%=servletContext%>/scripts/widget/globazwidget.js"></script> 
<script language="JavaScript">
	var JSP_DELETE_MESSAGE_INFO="<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>";
	var ACTION_AJAX_CREANCIER = "perseus.rentepont.creancierRentePontAjax";
	var CS_DOMAINE_APPLICATION =  "<%=IPFConstantes.CS_DOMAINE_ADRESSE%>";
	var messageDelete = "<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>";
	var LANGUAGES ="<ct:FWLabel key='JSP_PF_CREANCIER_W_CREANCIER'/>";
	
	var idRentePont = <%=idRentePont%>;
	var b_afficheBoutton = true;
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
			$repartirCreance.show();
		}
		
		$('#montantRevendique').focusout(function() {
			$('#montantAccorde').val($('#montantRevendique').val());	
		});
		
		$csTypeCreance = $('#csTypeCreance');
		$csTypeCreance.change(function() {
			if ($csTypeCreance.val() === '<%=CSTypeCreance.TYPE_CREANCE_IMPOT_SOURCE.getCodeSystem()%>') {
				$('#adresseCreancierRentePont').hide();
				$('#idTiers').val('');
			} else {
				$('#adresseCreancierRentePont').show();	
			}
		});
		
	})
</script>


<%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax/bodyStart.jspf" %>
			<%-- tpl:insert attribute="zoneTitle" --%><ct:FWLabel key="JSP_PF_CREANCIER_TITRE"/><%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax/bodyStart2.jspf" %>
						<%-- tpl:insert attribute="zoneMain" --%>
						
	<tr>
		<td>
	 		<label><ct:FWLabel key="JSP_PF_CREANCIER_R_ID_DEMANDE" /></label>
	 	</td>
	 	<td>
			<%=idRentePont%>
		</td>
	</tr>
	<tr>
		<td colspan="2">&nbsp;</td>
	</tr>
	<tr>
		<td valign="top">			
			<label><ct:FWLabel key="JSP_PF_CREANCIER_R_REQUERANT" /></label>
		 </td>
		 <td>
			<%= affichePersonnne%>
		</td>
	</tr>
	<tr>
		<td colspan="2">&nbsp;</td>
	</tr>
	<tr>
		<td width="300px"><ct:FWLabel key="JSP_PF_CREANCIER_R_PERIODE_DEMANDE"/></td>
		<td>
			<ct:FWLabel key="JSP_PF_CREANCIER_R_PERIODE_DU"/>
			<span><strong><%=" "+ viewBean.getRentePont().getSimpleRentePont().getDateDebut() + " " %></strong></span>
			<ct:FWLabel key="JSP_PF_CREANCIER_R_PERIODE_AU"/>
			<span><strong><%=" " + viewBean.getRentePont().getSimpleRentePont().getDateFin() %></strong></span>
		</td>
	</tr>

	<tr>
		<td colspan="6">&nbsp;</td>
	</tr>
	
	<tr>
		<td><ct:FWLabel key="JSP_PF_CREANCIER_D_MONTANT_RETROACTIF"/></td>
		<td>
			<input type="text" id="retroDisponible" data-g-amount=" " disabled="true"/>
			&nbsp;/&nbsp;
			<input type="text" id="retroTotal" value="<%=viewBean.getMontantRetro() %>" data-g-amount=" " disabled="true"/>
		</td>
	</tr>
	
	<tr>
		<td colspan="6">&nbsp;</td>
	</tr>

	<tr>
		<td colspan="6">
			<div class="area">
			
				<table class="areaTabel" width="100%">
					<thead>
						<tr> 
					    	<th><ct:FWLabel key="JSP_PF_CREANCIER_L_CREANCIER"/></th>
					 		<th data-g-amountformatter="blankAsZero:false"><ct:FWLabel key="JSP_PF_CREANCIER_L_MONTANT_REVENDIQUE"/></th>
					 		<th data-g-amountformatter="blankAsZero:false" ><ct:FWLabel key="JSP_PF_CREANCIER_L_MONTANT_MONTANT_REPARTI"/></th>
					 		<th><ct:FWLabel key="JSP_PF_CREANCIER_L_MONTANT_TYPE"/></th>
					 		<th><ct:FWLabel key="JSP_PF_CREANCIER_L_MONTANT_NO"/><input type="hidden" name="creancierRentePontSearch.forIdDemande" value="idDemande" /></th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
				
				<div id="main" class="formTableLess areaDetail"> 
					<table>
						<tr>
							<td>
								<label for="creancierRentePont.simpleCreancierRentePont.csTypeCreance">
									<ct:FWLabel key="JSP_PF_CREANCIER_D_TYPE_CREANCE" />
								</label>
							</td>
							<td>
								<ct:select id="csTypeCreance"
								           name="creancierRentePont.simpleCreancierRentePont.csTypeCreance" 
										   wantBlank="false"  
								           notation="data-g-select='mandatory:true'" >
								 	<ct:optionsCodesSystems csFamille="<%=IPFConstantes.CSGROUP_TYPE_CREANCIER %>"/>
								</ct:select>
							</td>
						</tr>
						<tr>
							<td>
								<label for="creancierRentePont.simpleCreancierRentePont.montantRevendique">
									<ct:FWLabel key="JSP_PF_CREANCIER_D_MONTANT_REVENDIQUE" />
								</label>
							</td>
							<td>
								<span class='td'>
									<ct:inputText name="creancierRentePont.simpleCreancierRentePont.montantRevendique" id="montantRevendique" notation="data-g-amount='mandatory:true'" /> 
								</span>
							</td>
						</tr>
						<tr>
							<td>
								<label for="creancierRentePont.simpleCreancierRentePont.montantAccorde">
									<ct:FWLabel key="JSP_PF_CREANCIER_D_MONTANT_ACCORDE" />
								</label>
							</td>
							<td>
								<span class='td'>
									<ct:inputText name="creancierRentePont.simpleCreancierRentePont.montantAccorde" id="montantAccorde" notation="data-g-amount='mandatory:false'" /> 
								</span>
							</td>
						</tr>
						<tr id="adresseCreancierRentePont">
							<td valign="top">
								<label for="creancierRentePont.simpleCreancierRentePont.idTiers">
									<ct:FWLabel key="JSP_PF_CREANCIER_D_CREANCIER" />
								</label>
							</td>
							<td>
								<div data-g-adresse="service:findAdressePaiement" id="adressePaiementValue" defaultvalue="" >
									<input type="hidden" id="idTiers" class="avoirPaiement.idTiers"
									       name="creancierRentePont.simpleCreancierRentePont.idTiers"  value="" /> 
									<input type="hidden" id="idDomaineApplicatif" class="avoirPaiement.idApplication"
									       name="creancierRentePont.simpleCreancierRentePont.idDomaineApplicatif"  value="" />
								</div>
							</td>
						</tr>
						<tr>
							<td valign="top">
								<label for="creancierRentePont.simpleCreancierRentePont.referencePaiement">
									<ct:FWLabel key="JSP_PF_CREANCIER_D_REFERENCE_PAIEMENT" />
								</label>
							</td>
							<td>
								<ct:inputText name="creancierRentePont.simpleCreancierRentePont.referencePaiement" id="referencePaiement" style="width:300"/> 
							</td>
						</tr>
						<% if (viewBean.getAreEditable()) { %>
							<tr>
								<td colspan="2">
								<%@ include file="/theme/detail_ajax/capageButtons.jspf" %>
<!-- 									<div class="btnAjax"> -->
<%-- 										<input class="btnAjaxUpdate" type="button" value="<%=btnUpdLabel%>"> --%>
<%-- 										<input class="btnAjaxDelete" type="button" value="<%=btnDelLabel%>"> --%>
<%-- 										<input class="btnAjaxAdd" type="button" value="<%=btnNewLabel%>"> --%>
<%-- 										<input class="btnAjaxValidate" type="button" value="<%=btnValLabel%>"> --%>
<%-- 										<input class="btnAjaxCancel" type="button" value="<%=btnCanLabel%>"> --%>
<!-- 									</div> -->
								</td>
							</tr>
						<% } %>
				</table>
				</div>
			</div>
		</td>
	</tr>
						
						
						<%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax/bodyErrors.jspf" %>
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax/footer.jspf" %>
