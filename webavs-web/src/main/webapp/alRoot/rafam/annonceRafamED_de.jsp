<%@page import="ch.globaz.al.business.models.rafam.AnnonceRafamErrorComplexModel"%>
<%@page import="ch.globaz.al.business.constantes.enumerations.RafamLegalBasis"%>
<%@page import="ch.globaz.al.business.constantes.enumerations.RafamError"%>
<%@page import="ch.globaz.al.business.constantes.enumerations.RafamEtatAnnonce"%>
<%@page import="globaz.jade.i18n.JadeI18n"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.al.vb.rafam.ALAnnonceRafamEDViewBean"%>
<%@page import="ch.globaz.al.business.constantes.enumerations.RafamFamilyAllowanceType" %>
<%@page import="ch.globaz.al.business.constantes.enumerations.RafamReturnCode" %>
<%@page import="ch.globaz.al.business.constantes.enumerations.RafamFamilyStatus" %>
<%@page import="ch.globaz.al.business.constantes.enumerations.RafamOccupationStatus" %>
<%@page import="ch.globaz.al.business.constantes.enumerations.RafamTypeAnnonce" %>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/ALTaglib.tld" prefix="al" %>
<%@ include file="/theme/detail/header.jspf" %>

<%-- tpl:insert attribute="zoneInit" --%>
<%
ALAnnonceRafamEDViewBean viewBean = (ALAnnonceRafamEDViewBean) session.getAttribute("viewBean"); 
	selectedIdValue=viewBean.getId();

	btnUpdLabel = objSession.getLabel("MODIFIER");
	btnDelLabel = objSession.getLabel("SUPPRIMER");
	btnValLabel = objSession.getLabel("VALIDER");
	btnCanLabel = objSession.getLabel("ANNULER");
	btnNewLabel = objSession.getLabel("NOUVEAU");
		
	idEcran="AL0033";
	
	String baseLegale = "";
	String cantonBaseLegale= ""; 
	if(!viewBean.getAnnonce().isNew() ){
		if(!JadeStringUtil.isBlankOrZero(viewBean.getAnnonce().getBaseLegale())) {
			baseLegale = objSession.getCodeLibelle(RafamLegalBasis.getLegalBasis(viewBean.getAnnonce().getBaseLegale()).getCS());
		}
		cantonBaseLegale= viewBean.getAnnonce().getCanton();
	}
	
	
%>
<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>
<%@page import="globaz.fweb.util.JavascriptEncoder"%>
<%@page import="globaz.jade.client.util.JadeUtil"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>

<%@page import="globaz.jade.client.util.JadeDateUtil"%>
<%@page import="ch.globaz.al.business.models.dossier.DossierComplexModel"%>
<%@page import="ch.globaz.al.business.constantes.ALCSDossier"%>
<%@page import="ch.globaz.al.business.services.ALServiceLocator"%>
<%@page import="ch.globaz.al.business.constantes.ALCSDroit"%>
<%@page import="ch.globaz.naos.business.service.AffiliationService"%>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<script type="text/javascript" src="<%=servletContext%>/alRoot/util_webaf.js"></script>
<script type="text/javascript" src="<%=servletContext%>/alRoot/ajax_webaf.js"></script>

<script type="text/javascript">

function add() {
}
function upd() {
}

function validate() {
	
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="al.rafam.annonceRafamED.ajouter";
    else 
        document.forms[0].elements('userAction').value="al.rafam.annonceRafamED.modifier";
    return state;
}

function cancel() {
	var methodElement = document.forms[0].elements('_method');
	action(methodElement.value);
	if(methodElement.value == ADD) {
		document.forms[0].elements('userAction').value="al.rafam.annonceRafamED.chercher";
	} else {
        document.forms[0].elements('userAction').value="al.rafam.annonceRafamED.afficher";
	}
}

function del() {
	var msgDelete = '<%=JavascriptEncoder.getInstance().encode(objSession.getLabel("MESSAGE_SUPPRESSION"))%>';
    if (window.confirm(msgDelete)){
        document.forms[0].elements('userAction').value="al.rafam.annonceRafamED.supprimer";
        document.forms[0].submit();
    }
}

function init(){
	
}
function ajaxValidationAnnonce(){
	var idAnnonce ="<%=viewBean.getAnnonce().getId()%>";
	var o_options= {
			serviceClassName: 'ch.globaz.al.business.services.rafam.AnnonceRafamDelegueBusinessService',
			serviceMethodName:'validationCafAnnonce',
			parametres: idAnnonce,
			callBack: callbackAjaxValidationAnnonce
		}
	globazNotation.readwidget.options=o_options;		
	globazNotation.readwidget.read();	
	
}

function callbackAjaxValidationAnnonce(){
	
	document.location.reload();
}


</script>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:insert attribute="zoneTitle" --%>
				<ct:FWLabel key="AL0031_TITRE"/><%=viewBean.getId()%>
			<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>

			<%-- tpl:insert attribute="zoneTitle" --%>
				<tr><td>
                  <table class="tab3Col">
                     <caption class="thDetail"></caption>

                     <tr>
                        <td class="label"><ct:FWLabel key="AL0031_NO_ANNONCE"/></td>

                        <td>
                           <input type="text" value="<%=viewBean.getAnnonce().getId()%>" disabled="disabled" class="readOnly" readonly="readonly" />
                        </td>
                         <td class="label"><ct:FWLabel key="AL0031_TYPE_ANNONCE"/></td>

                        <td> 	
	                    	<input type="text" value="<%=viewBean.getAnnonce().getTypeAnnonce()%>" disabled="disabled" class="readOnly" readonly="readonly"  />		
                        </td>
                        
                        <td></td>
                        <td></td>
                        <td colspan="2">
                        </td>
                     </tr>
					<tr>
						<td class="label" style="color:red"><ct:FWLabel key="AL0031_RECORD_NUMBER"/></td>

                        <td>
                           <input type="text" value="<%=viewBean.getAnnonce().getRecordNumber()%>" disabled="disabled" class="readOnly" readonly="readonly" />
                        </td>
                        <td class="label"><ct:FWLabel key="AL0031_ORIGINE"/></td>
                        <td> 	 	
	                    	<input type="text" value="<%=objSession.getCodeLibelle(viewBean.getAnnonce().getEvDeclencheur())%>" disabled="disabled" class="readOnly" readonly="readonly"  />          	 		
                        </td>
                        <td colspan="4"></td>
					</tr>
                     <tr>
                        <td class="label"><ct:FWLabel key="AL0031_NO_DOSSIER"/></td>

                        <td>
                           <input type="text" value="-" disabled="disabled" class="readOnly" readonly="readonly" />
                        </td>

                        <td class="label"><ct:FWLabel key="AL0031_ETAT"/></td>

                        <td>
	                    	 <input type="text" value="<%=objSession.getCodeLibelle(viewBean.getAnnonce().getEtat())%>" disabled="disabled" class="readOnly" readonly="readonly"  />
                        </td>

                        <td class="label"><ct:FWLabel key="AL0031_DATE"/></td>

                        <td>
                           <input type="text" value="<%=viewBean.getAnnonce().getDateCreation()%>" disabled="disabled" class="readOnly" readonly="readonly" />
                        </td>
                     </tr>
                     
                     <tr>
                        <td class="label"><ct:FWLabel key="AL0031_CODE_RETOUR"/></td>

                        <td>
                           <input type="text" value="<%=JadeI18n.getInstance().getMessage(objSession.getIdLangue(), RafamReturnCode.getRafamReturnCode(viewBean.getAnnonce().getCodeRetour()).getCodeLibelle())%>" disabled="disabled" class="readOnly" readonly="readonly" />
                        </td>

                        <td class="label"></td>
                        <td></td>

                        <td class="label"><ct:FWLabel key="AL0031_DATE_MUTATION"/></td>
                        <td>
                           <input type="text" value="<%=JadeStringUtil.isBlankOrZero(viewBean.getAnnonce().getDateMutation())?"":viewBean.getAnnonce().getDateMutation()%>" disabled="disabled" class="readOnly" readonly="readonly"/>
                        </td>
                     </tr>
                  </table>

					<hr />
				<%if(viewBean.getErrors().getSize() > 0) { %>
					<h3 class="subtitle">Erreurs</h3>
					<div style="width:100%; height:170px; overflow-y:scroll; overflow-x:hidden">
					<table class="al_list" >
						<thead>
							<tr>
								<th>Code</th>
								<th>Message</th>
								<th>Période en erreur</th>
								<th>Période de chevauchement</th>
								<th><ct:FWLabel key="AL0031_CAISSE_CONFLIT"/></th>
							</tr>
						</thead>
						<tbody>
							<%
							String rowStyle = "";
							for(int i=0;i<viewBean.getErrors().getSize();i++){ 
								AnnonceRafamErrorComplexModel errorRafam = (AnnonceRafamErrorComplexModel)viewBean.getErrors().getSearchResults()[i];
								
								if(!(i % 2 == 0))
			                		rowStyle= "odd";
			                	else
			                		rowStyle = "nonodd";
							%>
								<tr class="<%=rowStyle%>">
									<td><%=errorRafam.getErreurAnnonceRafamModel().getCode()%></td>
									<td><%=objSession.getCodeLibelle(RafamError.getRafamError(errorRafam.getErreurAnnonceRafamModel().getCode()).getCS())%></td>
									<td>
										<%if(!JadeStringUtil.isBlank(errorRafam.getErrorPeriodModel().getErrorPeriodStart())) { %>
											<%=errorRafam.getErrorPeriodModel().getErrorPeriodStart()%>&nbsp;-&nbsp;<%=errorRafam.getErrorPeriodModel().getErrorPeriodEnd()%>
										<% } else { %>
										-
										<% }%>
									</td>
									<%if(!JadeStringUtil.isBlank(errorRafam.getOverlapInformationModel().getOverlapPeriodeStart())) { %>
										<td><%=errorRafam.getOverlapInformationModel().getOverlapPeriodeStart()%>&nbsp;-&nbsp;<%=errorRafam.getOverlapInformationModel().getOverlapPeriodeEnd()%></td>
										<td><%=errorRafam.getOverlapInformationModel().getOfficeIdentifier()%>.<%=errorRafam.getOverlapInformationModel().getBranch()%></td>
									<% } else { %>
										<td>-</td>
										<td>-</td>
									<% }%>
		                	<%} %>
						</tbody>
					</table>
					</div>
                  <hr />
                  <% } %>

                  <table class="tab3Col">
                  
                  <td class="label subtitle" colspan="6">
                       <ct:FWLabel key="AL0031_ENFANT"/>
                			<div id="idTiers"> ( <ct:FWLabel key="AL0031_TIERS"/>)</div>
                	</td>
                					
                     <tr>
                        <td class="label"><ct:FWLabel key="AL0031_NSS_ENFANT"/></td>

                        <td>
                           <input type="text" value="-" disabled="disabled" class="readOnly" readonly="readonly" />
                        </td>
                        
                         <td class="label"><ct:FWLabel key="AL0031_NAISSANCE_ENFANT"/></td>
                         
                         <td>
                           <input type="text" value="-" disabled="disabled" class="readOnly" readonly="readonly" />
                        </td>

                        <td colspan="2">
                        </td>
                     </tr>

                     <tr>
                        <td class="label"><ct:FWLabel key="AL0031_NOM_ENFANT"/></td>

                        <td>
                           <input type="text" value="-" disabled="disabled" class="readOnly" readonly="readonly" />
                        </td>

                        <td class="label"><ct:FWLabel key="AL0031_PRENOM_ENFANT"/></td>

                        <td>
                           <input type="text" value="-" disabled="disabled" class="readOnly" readonly="readonly" />
                        </td>

                        <td colspan="2">
                        </td>
                     </tr>

                     <tr>
                        <td class="label"><ct:FWLabel key="AL0031_TYPE_PRESTATION"/></td>
                        <td>
	                    	<input type="text" value="<%=JadeI18n.getInstance().getMessage(objSession.getIdLangue(), RafamFamilyAllowanceType.getFamilyAllowanceType(viewBean.getAnnonce().getGenrePrestation()).getCodeLibelle())%>" disabled="disabled" class="readOnly" readonly="readonly" />
                        </td>

                        <td class="label"><ct:FWLabel key="AL0031_BASE_LEGALE"/></td>

                        <td>
                           <input type="text" value="<%=baseLegale+cantonBaseLegale%>" disabled="disabled" class="readOnly" readonly="readonly" />
                        </td>

                        <td colspan="2">
                        </td>
                     </tr>

                     <tr>
                        <td class="label"><ct:FWLabel key="AL0031_DEBUT_DROIT"/></td>

                        <td>
                           <input type="text" value="<%=(JadeStringUtil.isBlankOrZero(viewBean.getAnnonce().getDebutDroit()))?"":viewBean.getAnnonce().getDebutDroit()%>" name="annonce.debutDroit" disabled="disabled" class="readOnly" readonly="readonly"/>
                        </td>

                        <td class="label"><ct:FWLabel key="AL0031_ECHEANCE_DROIT"/></td>

                        <td>
                           <input type="text" value="<%=(JadeStringUtil.isBlankOrZero(viewBean.getAnnonce().getEcheanceDroit()))?"":viewBean.getAnnonce().getEcheanceDroit()%>" name="annonce.echeanceDroit" disabled="disabled" class="readOnly" readonly="readonly"/>
                        </td>
						<td class="label"><ct:FWLabel key="AL0031_PAYS_DOMICILE_ENFANT"/></td>
                        <td>
                           <input type="text" value="<%=(JadeStringUtil.isBlankOrZero(viewBean.getPays()))?"":viewBean.getPays()%>" name="annonce.codeCentralePaysEnfant" disabled="disabled" class="readOnly" readonly="readonly"/>
                        </td>
                     </tr>
                  </table>
                  
                  <table class="tab3Col">
                     <caption><ct:FWLabel key="AL0031_ENFANT_CENTRALE"/></caption>

                     <tr>
                        <td class="label"><ct:FWLabel key="AL0031_NSS_ENFANT"/></td>

                        <td>
                           <input type="text" value="<%=(!JadeStringUtil.isEmpty(viewBean.getAnnonce().getNewNssEnfant()))?viewBean.getAnnonce().getNewNssEnfant() + ", ancien : ":"" %> <%=viewBean.getAnnonce().getNssEnfant()%>" disabled="disabled" class="readOnly" readonly="readonly" />
                        </td>
                        
                        <td class="label"><ct:FWLabel key="AL0031_NAISSANCE_ENFANT"/></td>
                         
                         <td>
                           <input type="text" value="<%=viewBean.getAnnonce().getDateNaissanceEnfant()%>" disabled="disabled" class="readOnly" readonly="readonly" />
                        </td>

                        <td colspan="2">
                        </td>
                     </tr>

                     <tr>
                        <td class="label"><ct:FWLabel key="AL0031_NOM_ENFANT"/></td>

                        <td>
                           <input type="text" value="<%=viewBean.getAnnonce().getNomEnfant()%>" disabled="disabled" class="readOnly" readonly="readonly" />
                        </td>

                        <td class="label"><ct:FWLabel key="AL0031_PRENOM_ENFANT"/></td>

                        <td>
                           <input type="text" value="<%=viewBean.getAnnonce().getPrenomEnfant()%>" disabled="disabled" class="readOnly" readonly="readonly" />
                        </td>
						<!-- Pour la mise en page -->
                        <td >&nbsp;
                        </td>
                        <td >&nbsp;
                     </tr>
                  </table>

                  <hr />

                  <table class="tab3Col">
                     
                     
                        <td class="label subtitle" colspan="6">
                       <ct:FWLabel key="AL0031_BENEFICIAIRE"/>
                			<div id="idTiers"> ( <ct:FWLabel key="AL0031_TIERS"/>)</div>
                	</td>

                     <tr>
                        <td class="label"><ct:FWLabel key="AL0031_NSS_BENEFICIAIRE"/></td>

                        <td>
                           <input type="text" value="-" disabled="disabled" class="readOnly" readonly="readonly" />
                        </td>
                        
                          <td class="label"><ct:FWLabel key="AL0031_NAISSANCE_BENEFICIAIRE"/></td>
                         
                         <td>
                           <input type="text" value="<%=viewBean.getAnnonce().getDateNaissanceAllocataire()%>" disabled="disabled" class="readOnly" readonly="readonly" />
                        </td>
						<!-- Pour la mise en page -->
                        <td >&nbsp;
                        </td>
                        <td >&nbsp;
                        </td>
                     </tr>

                     <tr>
                     <td class="label"><ct:FWLabel key="AL0031_NOM_BENEFICIAIRE"/></td>

                        <td>
                           <input type="text" value="-" disabled="disabled" class="readOnly" readonly="readonly" />
                        </td>

                        <td class="label"><ct:FWLabel key="AL0031_PRENOM_BENEFICIAIRE"/></td>

                        <td>
                           <input type="text" value="-" disabled="disabled" class="readOnly" readonly="readonly" />
                        </td>
						<!-- Pour la mise en page -->
                        <td >&nbsp;
                        </td>
                        <td >&nbsp;
                        </td>
                     </tr>

                     <tr>
                        <td class="label"><ct:FWLabel key="AL0031_STATUT_FAMILIAL"/></td>

                        <td>
                           <input type="text" value="<%=JadeStringUtil.isBlankOrZero(viewBean.getAnnonce().getCodeStatutFamilial())?"":objSession.getCodeLibelle(RafamFamilyStatus.getFamilyStatus(viewBean.getAnnonce().getCodeStatutFamilial()).getCS())%>" disabled="disabled" class="readOnly" readonly="readonly" />
                        </td>

                        <td class="label"><ct:FWLabel key="AL0031_TYPE_ACTIVITE"/></td>

                        <td>
                           <input type="text" value="<%=JadeStringUtil.isBlankOrZero(viewBean.getAnnonce().getCodeTypeActivite())?"":objSession.getCodeLibelle(RafamOccupationStatus.getOccupationStatus(viewBean.getAnnonce().getCodeTypeActivite()).getCS())%>" disabled="disabled" class="readOnly" readonly="readonly" />
                        </td>

                        <td colspan="2">
                        </td>
                     </tr>
                  </table>
                  
                  <table class="tab3Col">
                       <caption><ct:FWLabel key="AL0031_BENEFICIAIRE_CENTRALE"/></caption>
						
                     <tr>
                        <td class="label"><ct:FWLabel key="AL0031_NSS_BENEFICIAIRE"/></td>

                        <td>
                           <input type="text" value="<%=viewBean.getAnnonce().getNssAllocataire()%>" disabled="disabled" class="readOnly" readonly="readonly" />
                        </td>
                        
                          <td class="label"><ct:FWLabel key="AL0031_NAISSANCE_BENEFICIAIRE"/></td>
                         
                         <td>
                           <input type="text" value="<%=viewBean.getAnnonce().getDateNaissanceAllocataire()%>" disabled="disabled" class="readOnly" readonly="readonly" />
                        </td>
						<!-- Pour la mise en page -->
                        <td >&nbsp;
                        </td>
                        <td >&nbsp;
                        </td>
                     </tr>

                     <tr>
                        <td class="label"><ct:FWLabel key="AL0031_NOM_BENEFICIAIRE"/></td>

                        <td>
                           <input type="text" value="<%=viewBean.getAnnonce().getNomAllocataire()%>" disabled="disabled" class="readOnly" readonly="readonly" />
                        </td>

                        <td class="label"><ct:FWLabel key="AL0031_PRENOM_BENEFICIAIRE"/></td>

                        <td>
                           <input type="text" value="<%=viewBean.getAnnonce().getPrenomAllocataire()%>" disabled="disabled" class="readOnly" readonly="readonly" />
                        </td>
						<!-- Pour la mise en page -->
                        <td >&nbsp;
                        </td>
                        <td >&nbsp;
                        </td>
                     </tr>
                  </table>
                  <hr />
			<%-- /tpl:insert --%>
			</td></tr>						
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%if (viewBean.canValidationCAF()) {%>
	<input class="btnCtrl" id="btnValider" type="button" value="Validation CAF" onclick="ajaxValidationAnnonce()">
<%}%>


				<%-- tpl:insert attribute="zoneButtons" --%>
<script type="text/javascript">
document.getElementById("btnUpd").style.visibility="hidden";
document.getElementById("btnVal").style.visibility="hidden";
document.getElementById("btnCan").style.visibility="hidden";
document.getElementById("btnDel").style.visibility="hidden";

</script>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf"%>
<ct:menuChange displayId="menu" menuId="menuWEBAF" showTab="menu"/>	

<%-- tpl:insert attribute="zoneEndPage" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf"%>


