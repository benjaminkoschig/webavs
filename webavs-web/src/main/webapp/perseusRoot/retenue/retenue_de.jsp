	<%-- tpl:insert page="/theme/detail_ajax.jtpl" --%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="ch.globaz.perseus.business.constantes.IPFConstantes"%>
<%@page import="ch.globaz.pyxis.business.model.PersonneEtendueComplexModel"%>
<%@page import="globaz.perseus.utils.PFUserHelper"%>
<%@page import="ch.globaz.perseus.business.constantes.CSTypeRetenue"%>
<%@page import="globaz.perseus.vb.retenue.PFRetenueViewBean"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail_ajax/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>	
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="java.util.Iterator"%>
<%@page import="globaz.jade.client.util.JadeDateUtil"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.io.ObjectOutput"%>
<%@page import="java.io.ObjectOutputStream"%>
<%@page import="org.apache.commons.codec.binary.Hex"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="java.util.Date" %>

<%
	
	idEcran = "PPF1511";
	
	PFRetenueViewBean viewBean = (PFRetenueViewBean) session.getAttribute("viewBean");
	autoShowErrorPopup = true;

	bButtonDelete = false;
	bButtonCancel = false;
	bButtonUpdate = false;
	bButtonValidate = false;
	
	PersonneEtendueComplexModel personne = viewBean.getPcfAccordee().getDemande().getDossier().getDemandePrestation().getPersonneEtendue();
%>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/javascripts.jspf" %>
<%@ include file="/perseusRoot/ajax/javascriptsAndCSS.jspf" %>

<%-- tpl:put name="zoneScripts" --%>

<script language="JavaScript">
	var ACTION_AJAX_RETENUE = "perseus.retenue.retenueAjax";
	var ID_PCFACCORDEE = "<%=viewBean.getPcfAccordee().getId()%>";
	var JSP_DELETE_MESSAGE_INFO="<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>"; 
	
	$(function(){
		//Gestion de l'affichage des champs en fonction du type de retenue
		$csTypeRetenue = $('#csTypeRetenue');
		$rPaiement = $('.rPaiement');
		$rFacture = $('.rFacture');
		$rImpot = $('.rImpot');
		$rBoutonIS = $('.rBoutonIS');
		$rBouton = $('.rBouton');
		$csTypeRetenue.change(function() {
			if ($csTypeRetenue.val() === '<%=CSTypeRetenue.ADRESSE_PAIEMENT.getCodeSystem()%>') {
				$rImpot.hide();
				$rFacture.hide();
				$rPaiement.show();
				$rBoutonIS.hide();
				$rBouton.show();
			} else if ($csTypeRetenue.val() === '<%=CSTypeRetenue.FACTURE_EXISTANTE.getCodeSystem()%>') {
				$rImpot.hide();
				$rPaiement.hide();		
				$rFacture.show();
				$rBoutonIS.hide();
				$rBouton.show();
			} else if ($csTypeRetenue.val() === '<%=CSTypeRetenue.IMPOT_SOURCE.getCodeSystem()%>') {
				$rFacture.hide();		
				$rPaiement.hide();		
				$rImpot.show();
				$rBoutonIS.show();
				$rBouton.hide();
			}
		});	
		$csTypeRetenue.change();
	});
</script>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath + "Root")%>/scripts/retenue/retenue_MembrePart.js"/></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/widget/globazwidget.js"></script>

<%-- /tpl:put --%>

<%@ include file="/theme/detail_ajax/bodyStart.jspf" %>

			<%-- tpl:put name="zoneTitle" --%>
	<ct:FWLabel key="JSP_PF_RETENUES_PAYMENT_MENSUEL"/>
<%@ include file="/theme/detail_ajax/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
			<tr>
				<td colspan="2">&nbsp;</td>
			</tr>
			<tr>
				<td valign="top">			
					<label><ct:FWLabel key="JSP_PF_RETENUE_R_REQUERANT" /></label>
				 </td>
				 <td>
					<%= PFUserHelper.getDetailAssure(objSession, personne)%>
				</td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
			</tr>
			<tr>
				<td width="300px"><ct:FWLabel key="JSP_PF_RETENUE_R_PERIODE_DEMANDE"/></td>
				<td>
					<ct:FWLabel key="JSP_PF_RETENUE_R_PERIODE_DU"/>
					<span><strong><%=" "+ viewBean.getPcfAccordee().getDemande().getSimpleDemande().getDateDebut() + " " %></strong></span>
					<ct:FWLabel key="JSP_PF_RETENUE_R_PERIODE_AU"/>
					<span><strong><%=" "+ viewBean.getPcfAccordee().getDemande().getSimpleDemande().getDateFin()%></strong></span>
				</td>
			</tr>
			<tr>
				<td valign="top">			
					<label><ct:FWLabel key="JSP_PF_RETENUE_R_MONTANT_PCFA" /></label>
				 </td>
				 <td>
					<%= new FWCurrency(viewBean.getPcfAccordee().getSimplePCFAccordee().getMontant()).toStringFormat() %>
				</td>
			</tr>
			
			<tr>
				<td colspan="6">&nbsp;</td>
			</tr>
			
			<TR>		
				<td colspan="4">
					<div class="area">
					
						<table class="areaTabel" width="100%">
							<thead>
								<tr> 
							 		<th><ct:FWLabel key="JSP_PF_RETENUE_TYPE"/></th>
							 		<th data-g-amountformatter="blankAsZero:false"><ct:FWLabel key="JSP_PF_RETENUE_MONTANT"/></th>
							 		<th data-g-amountformatter="blankAsZero:false" ><ct:FWLabel key="JSP_PF_RETENUE_MONTANT_RETENIR"/></th>
							 		<th data-g-amountformatter="blankAsZero:false" ><ct:FWLabel key="JSP_PF_RETENUE_MONTANT_DEJA_RETENU"/></th>
							 		<th><ct:FWLabel key="JSP_PF_RETENUE_NO"/></th>
							 	</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
						
						<div id="main" class="formTableLess areaDetail"> 
							<table>
								<tr>
									<td width="200px">
										<label for="creancier.simpleCreancier.csTypeCreance">
											<ct:FWLabel key="JSP_PF_RETENUE_TYPE" />
										</label>
									</td>
									<td>
										<ct:select id="csTypeRetenue"
										           name="simpleRetenue.csTypeRetenue" 
												   wantBlank="false"  
										           notation="data-g-select='mandatory:true'" >
										 	<ct:optionsCodesSystems csFamille="<%=IPFConstantes.CSGROUP_TYPE_RETENUE %>"/>
										</ct:select>
									</td>
								</tr>
								<tr>
									<td>
										<label for="simpleRetenue.dateDebutRetenue">
											<ct:FWLabel key="JSP_PF_RETENUE_DATE_DEBUT_RETENUE" />
										</label>
									</td>
									<td>
										<ct:inputText name="simpleRetenue.dateDebutRetenue" id="dateDebutRetenue" notation="data-g-calendar='mandatory:true,type:month'" /> 
									</td>
								</tr>
								<tr>
									<td>
										<label for="simpleRetenue.dateFinRetenue">
											<ct:FWLabel key="JSP_PF_RETENUE_DATE_FIN_RETENUE" />
										</label>
									</td>
									<td>
										<ct:inputText name="simpleRetenue.dateFinRetenue" id="dateFinRetenue" notation="data-g-calendar='type:month'" /> 
									</td>
								</tr>
								<tr>
									<td>
										<label for="simpleRetenue.montantRetenuMensuel">
											<ct:FWLabel key="JSP_PF_RETENUE_MONTANT" />
										</label>
									</td>
									<td>
										<ct:inputText name="simpleRetenue.montantRetenuMensuel" id="montantRetenuMensuel" notation="data-g-amount='mandatory:true'" /> 
									</td>
								</tr>
								<tr class="rPaiement rFacture">
									<td>
										<label for="simpleRetenue.montantTotalARetenir">
											<ct:FWLabel key="JSP_PF_RETENUE_MONTANT_RETENIR" />
										</label>
									</td>
									<td>
										<ct:inputText name="simpleRetenue.montantTotalARetenir" id="montantTotalARetenir" notation="data-g-amount=' '" /> 
									</td>
								</tr>
								<tr class="rPaiement rFacture">
									<td>
										<label for="simpleRetenue.montantDejaRetenu">
											<ct:FWLabel key="JSP_PF_RETENUE_MONTANT_DEJA_RETENU" />
										</label>
									</td>
									<td>
										<ct:inputText readonly="readonly" defaultValue="0.00" name="simpleRetenue.montantDejaRetenu" id="montantDejaRetenu" notation="data-g-amount=' ' class='disabled'" /> 
									</td>
								</tr>
								<tr class="rImpot">
									<td>
										<label for="simpleRetenue.tauxImposition">
											<ct:FWLabel key="JSP_PF_RETENUE_TAUX" />
										</label>
									</td>
									<td>
										<ct:inputText name="simpleRetenue.tauxImposition" id="tauxImposition" notation="data-g-amount='mandatory:true'" /> 
									</td>
									<td>
										<label for="simpleRetenue.baremeIS">
											<ct:FWLabel key="JSP_PF_RETENUE_BAREME" />
										</label>
									</td>
									<td>
										<ct:inputText name="simpleRetenue.baremeIS" id="baremeIS" readonly="readonly" notation="data-g-string='' class='disabled' " /> 
									</td>
								</tr>
								<tr class="rFacture">
									<td>
										<label for="simpleRetenue.idSection">
											<ct:FWLabel key="JSP_PF_RETENUE_SECTION" />
										</label>
									</td>
									<td>
										<select name="simpleRetenue.idTypeSection" id="cleSection">
											<option value=""></option>
											<% for (String keyMap : viewBean.getListSections().keySet()) { %>
												<option value="<%=keyMap %>"><%=viewBean.getListSections().get(keyMap) %></option>
											<% } %>
										</select>
									</td>
								</tr>
								<tr id="adresseCreancier" class="rPaiement">
									<td valign="top">
										<label for="simpleRetenue.idTiersAdressePmt">
											<ct:FWLabel key="JSP_PF_RETENUE_ADRESSE_PAIEMENT" />
										</label>
									</td>
									<td>
										<div data-g-adresse="service:findAdressePaiement" id="adressePaiementValue" defaultvalue="" >
											<input type="hidden" id="idTiersAdressePmt" class="avoirPaiement.idTiers"
											       name="simpleRetenue.idTiersAdressePmt"  value="" /> 
											<input type="hidden" id="idDomaineApplicatif" class="avoirPaiement.idApplication"
											       name="simpleRetenue.idDomaineApplicatif"  value="" />
										</div>
									</td>
								</tr>
								<tr id="boutonIS" class="rBoutonIS">
									<td colspan="2">
										<div class="btnAjax">
											<ct:ifhasright element="<%=partialUserActionAction%>" crud="c">
											<input class="btnAjaxAdd" type="button" value="<%=btnNewLabel%>">
											</ct:ifhasright>
											<input class="btnAjaxCancel" type="button" value="<%=btnCanLabel%>">
										</div>
									</td>
								</tr>	
								<tr id="bouton" class="rBouton">
									<td colspan="2">
										<%@ include file="/theme/detail_ajax/capageButtons.jspf" %>
									</td>
								</tr>
							</table>
						</div>
					</div>
				
				</TD>
			</TR>	
<%@ include file="/theme/detail_ajax/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/footer.jspf" %>
<%-- /tpl:insert --%>