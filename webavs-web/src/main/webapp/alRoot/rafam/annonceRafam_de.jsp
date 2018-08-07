<%@page import="ch.globaz.al.business.models.rafam.AnnonceRafamErrorComplexModel"%>
<%@page import="ch.globaz.al.business.constantes.enumerations.RafamLegalBasis"%>
<%@page import="ch.globaz.al.business.constantes.enumerations.RafamError"%>
<%@page import="ch.globaz.al.business.constantes.enumerations.RafamEtatAnnonce"%>
<%@page import="globaz.jade.i18n.JadeI18n"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.al.vb.rafam.ALAnnonceRafamViewBean"%>
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
	ALAnnonceRafamViewBean viewBean = (ALAnnonceRafamViewBean) session.getAttribute("viewBean"); 
	selectedIdValue=viewBean.getId();

	btnUpdLabel = objSession.getLabel("MODIFIER");
	btnDelLabel = objSession.getLabel("SUPPRIMER");
	btnValLabel = objSession.getLabel("VALIDER");
	btnCanLabel = objSession.getLabel("ANNULER");
	btnNewLabel = objSession.getLabel("NOUVEAU");
		
	idEcran="AL0031";
	
	String baseLegale = "";
	String cantonBaseLegale= ""; 
	if(!viewBean.getAnnonce().isNew() ){
		if(!JadeStringUtil.isBlankOrZero(viewBean.getAnnonce().getAnnonceRafamModel().getBaseLegale())) {
			baseLegale = objSession.getCodeLibelle(RafamLegalBasis.getLegalBasis(viewBean.getAnnonce().getAnnonceRafamModel().getBaseLegale()).getCS());
		}
		cantonBaseLegale= viewBean.getAnnonce().getAnnonceRafamModel().getCanton();
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

<%@page import="globaz.al.vb.rafam.ALAnnonceRafamViewBean"%>

<script type="text/javascript" src="<%=servletContext%>/alRoot/util_webaf.js"></script>
<script type="text/javascript" src="<%=servletContext%>/alRoot/ajax_webaf.js"></script>



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
        document.forms[0].elements('userAction').value="al.rafam.annonceRafam.ajouter";
    else 
        document.forms[0].elements('userAction').value="al.rafam.annonceRafam.modifier";
    return state;
}

function cancel() {
	var methodElement = document.forms[0].elements('_method');
	action(methodElement.value);
	if(methodElement.value == ADD) {
		document.forms[0].elements('userAction').value="al.rafam.annonceRafam.chercher";
	} else {
        document.forms[0].elements('userAction').value="al.rafam.annonceRafam.afficher";
	}
}

function del() {
	var msgDelete = '<%=JavascriptEncoder.getInstance().encode(objSession.getLabel("MESSAGE_SUPPRESSION"))%>';
    if (window.confirm(msgDelete)){
        document.forms[0].elements('userAction').value="al.rafam.annonceRafam.supprimer";
        document.forms[0].submit();
    }
}

function init(){
	
}
function validerAnnonce(){
	var req = "<%=servletContext + mainServletPath%>?userAction=al.rafam.annonceRafam.validerAnnonce&id=<%=viewBean.getId()%>";
	var ajaxQuery = new AjaxQuery(req);

	ajaxQuery.noResultCallback = alert;
	ajaxQuery.noResultCallbackParams = new Array("fini");
	ajaxQuery.launch();
}

function suspendreAnnonce(){
	var req = "<%=servletContext + mainServletPath%>?userAction=al.rafam.annonceRafam.suspendreAnnonce&id=<%=viewBean.getId()%>";
	var ajaxQuery = new AjaxQuery(req);

	ajaxQuery.noResultCallback = alert;
	ajaxQuery.noResultCallbackParams = new Array("fini");
	ajaxQuery.launch();
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
	                    	<input type="text" value="<%=JadeI18n.getInstance().getMessage(objSession.getIdLangue(), RafamTypeAnnonce.getRafamTypeAnnonce(viewBean.getAnnonce().getAnnonceRafamModel().getTypeAnnonce()).getCodeLibelle())%> (<%=viewBean.getAnnonce().getAnnonceRafamModel().getTypeAnnonce()%>)" <%=viewBean.getEditionModeFields()%>  />		
	                    </td>
                        <td></td>
                        <td></td>
                        <td colspan="2"></td>
                     </tr>
					<tr>
						<td class="label" style="color:red"><ct:FWLabel key="AL0031_RECORD_NUMBER"/></td>

                        <td>
                           <input type="text" value="<%=viewBean.getAnnonce().getAnnonceRafamModel().getRecordNumber()%>" <%=viewBean.getEditionModeFields()%> />
                        </td>
                        <td class="label"><ct:FWLabel key="AL0031_ORIGINE"/></td>
                        <td> 	
                        	<%if ("".equals(viewBean.getEditionModeFields())){ %>
                        	<ct:select name="annonce.annonceRafamModel.evDeclencheur" >
		                    	<ct:optionsCodesSystems csFamille="ALRAFAMEVD"/>
	                    	</ct:select> 
	                    	<%}else{ %>
	                    		 <input type="text" value="<%=objSession.getCodeLibelle(viewBean.getAnnonce().getAnnonceRafamModel().getEvDeclencheur())%>" <%=viewBean.getEditionModeFields()%>  />
	                    	<%} %>  		
                        </td>
                        <td colspan="4"></td>
					</tr>
                     <tr>
                        <td class="label"><ct:FWLabel key="AL0031_NO_DOSSIER"/></td>

                        <td>
                           <input type="text" value="<%=viewBean.getAnnonce().getDroitComplexModel().getDroitModel().getIdDossier()%>" disabled="disabled" class="readOnly" readonly="readonly" />
                        </td>

                        <td class="label"><ct:FWLabel key="AL0031_ETAT"/></td>

                        <td>
                        <%if ("".equals(viewBean.getEditionModeFields())){ %>
                              <ct:select name="annonce.annonceRafamModel.etat" >
		                    	<ct:optionsCodesSystems csFamille="ALRAFAMETA"/>
	                    	</ct:select>   	
	                    	<%}else{ %>
	                    		 <input type="text" value="<%=objSession.getCodeLibelle(viewBean.getAnnonce().getAnnonceRafamModel().getEtat())%>" <%=viewBean.getEditionModeFields()%>  />
	                    	<%} %>  
                        </td>

                        <td class="label"><ct:FWLabel key="AL0031_DATE"/></td>

                        <td>
                           <input type="text" value="<%=viewBean.getAnnonce().getAnnonceRafamModel().getDateCreation()%>" disabled="disabled" class="readOnly" readonly="readonly" />
                        </td>
                     </tr>
                     
                     <tr>
                        <td class="label"><ct:FWLabel key="AL0031_CODE_RETOUR"/></td>

                        <td>
                           <input type="text" value="<%=JadeI18n.getInstance().getMessage(objSession.getIdLangue(), RafamReturnCode.getRafamReturnCode(viewBean.getAnnonce().getAnnonceRafamModel().getCodeRetour()).getCodeLibelle())%>" disabled="disabled" class="readOnly" readonly="readonly" />
                        </td>
                        <td class="label"><ct:FWLabel key="AL0031_LEGAL_OFFICE"/></td>
                        <td><input type="text" value="<%=viewBean.getAnnonce().getAnnonceRafamModel().getLegalOffice()%>" disabled="disabled" class="readOnly" readonly="readonly" /></td>
                        <td class="label"><ct:FWLabel key="AL0031_DATE_MUTATION"/></td>
                        <td>
                           <input type="text" value="<%=JadeStringUtil.isBlankOrZero(viewBean.getAnnonce().getAnnonceRafamModel().getDateMutation())?"":viewBean.getAnnonce().getAnnonceRafamModel().getDateMutation()%>" <%=viewBean.getEditionModeFields() %>/>
                        </td>
                     </tr>
                  </table>

					<hr />
				<%if(viewBean.getErrors().getSize() > 0) { %>
					<h3 class="subtitle"><ct:FWLabel key="AL0031_TITRE_ERREUR"/></h3>
					<div style="width:100%; height:170px; overflow-y:scroll; overflow-x:hidden">
					<table class="al_list" >
						<thead>
							<tr>
								<th><ct:FWLabel key="AL0031_CODE_ERREUR"/></th>
								<th><ct:FWLabel key="AL0031_MESSAGE_ERREUR"/></th>
								<th><ct:FWLabel key="AL0031_PERIODE_ERREUR"/></th>
								<th><ct:FWLabel key="AL0031_PERIODE_CHEVAUCHEMENT"/></th>
								<th><ct:FWLabel key="AL0031_CAISSE_CONFLIT"/></th>
								<th></th>
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
									
									<%if(errorRafam.getOverlapInformationModel().getInsignificance()) { %>
										<td><img src="images/dialog-ok-apply.png" alt="Erreur de moindre importance" width="16" height="16"/></td>
									<% } else { %>
										<td></td>
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
                			<div id="idTiers"> ( <ct:FWLabel key="AL0031_TIERS"/> <a href="<%=servletContext+"/pyxis?userAction=pyxis.tiers.tiers.afficher&selectedId="+viewBean.getAnnonce().getDroitComplexModel().getEnfantComplexModel().getEnfantModel().getIdTiersEnfant()%>"><%=viewBean.getAnnonce().getDroitComplexModel().getEnfantComplexModel().getPersonneEtendueComplexModel().getId()%></a>)</div>
                	</td>
                					

                		
 
                     <tr>
                        <td class="label"><ct:FWLabel key="AL0031_NSS_ENFANT"/></td>

                        <td>
                           <input type="text" value="<%=viewBean.getAnnonce().getDroitComplexModel().getEnfantComplexModel().getPersonneEtendueComplexModel().getPersonneEtendue().getNumAvsActuel()%>" disabled="disabled" class="readOnly" readonly="readonly" />
                        </td>
                        
                         <td class="label"><ct:FWLabel key="AL0031_NAISSANCE_ENFANT"/></td>
                         
                         <td>
                           <input type="text" value="<%=viewBean.getAnnonce().getDroitComplexModel().getEnfantComplexModel().getPersonneEtendueComplexModel().getPersonne().getDateNaissance()%>" disabled="disabled" class="readOnly" readonly="readonly" />
                        </td>

                        <td colspan="2">
                        </td>
                     </tr>

                     <tr>
                        <td class="label"><ct:FWLabel key="AL0031_NOM_ENFANT"/></td>

                        <td>
                           <input type="text" value="<%=viewBean.getAnnonce().getDroitComplexModel().getEnfantComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation1()%>" disabled="disabled" class="readOnly" readonly="readonly" />
                        </td>

                        <td class="label"><ct:FWLabel key="AL0031_PRENOM_ENFANT"/></td>

                        <td>
                           <input type="text" value="<%=viewBean.getAnnonce().getDroitComplexModel().getEnfantComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation2()%>" disabled="disabled" class="readOnly" readonly="readonly" />
                        </td>

                        <td colspan="2">
                        </td>
                     </tr>

                     <tr>
                        <td class="label"><ct:FWLabel key="AL0031_TYPE_PRESTATION"/></td>
                        <td>
                        <%if ("".equals(viewBean.getEditionModeFields())){ %>
                        	<ct:select name="annonce.annonceRafamModel.genrePrestation" >
		                    	<al:alOptionEnumTag mLibelle="getLibelle" mValue="getCode" enumName="ch.globaz.al.business.constantes.enumerations.RafamFamilyAllowanceType" />
	                    	</ct:select>
	                    	<%}else{ %>
	                    		 <input type="text" value="<%=JadeI18n.getInstance().getMessage(objSession.getIdLangue(), RafamFamilyAllowanceType.getFamilyAllowanceType(viewBean.getAnnonce().getAnnonceRafamModel().getGenrePrestation()).getCodeLibelle())%>" <%=viewBean.getEditionModeFields()%> />
	                    	<%} %>  
                        
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
                           <input type="text" value="<%=(JadeStringUtil.isBlankOrZero(viewBean.getAnnonce().getAnnonceRafamModel().getDebutDroit()))?"":viewBean.getAnnonce().getAnnonceRafamModel().getDebutDroit()%>" name="annonce.annonceRafamModel.debutDroit" <%=viewBean.getEditionModeFields() %>/>
                        </td>

                        <td class="label"><ct:FWLabel key="AL0031_ECHEANCE_DROIT"/></td>

                        <td>
                           <input type="text" value="<%=(JadeStringUtil.isBlankOrZero(viewBean.getAnnonce().getAnnonceRafamModel().getEcheanceDroit()))?"":viewBean.getAnnonce().getAnnonceRafamModel().getEcheanceDroit()%>" name="annonce.annonceRafamModel.echeanceDroit" <%=viewBean.getEditionModeFields() %>/>
                        </td>

                        <td class="label"><ct:FWLabel key="AL0031_PAYS_DOMICILE_ENFANT"/></td>

                        <td>
                           <input type="text" value="<%=viewBean.getAnnonce().getAnnonceRafamModel().getCodeCentralePaysEnfant()%>" name="annonce.annonceRafamModel.echeanceDroit" <%=viewBean.getEditionModeFields() %>/>
                        </td>
                     </tr>
                  </table>
                  
                  <table class="tab3Col">
                     <caption><ct:FWLabel key="AL0031_ENFANT_CENTRALE"/></caption>

                     <tr>
                        <td class="label"><ct:FWLabel key="AL0031_NSS_ENFANT"/></td>

                        <td>
                           <input type="text" value="<%=(!JadeStringUtil.isEmpty(viewBean.getAnnonce().getAnnonceRafamModel().getNewNssEnfant()))?viewBean.getAnnonce().getAnnonceRafamModel().getNewNssEnfant() + ", ancien : ":"" %> <%=viewBean.getAnnonce().getAnnonceRafamModel().getNssEnfant()%>" disabled="disabled" class="readOnly" readonly="readonly" />
                        </td>
                        
                        <td class="label"><ct:FWLabel key="AL0031_NAISSANCE_ENFANT"/></td>
                         
                         <td>
                           <input type="text" value="<%=viewBean.getAnnonce().getAnnonceRafamModel().getDateNaissanceEnfant()%>" disabled="disabled" class="readOnly" readonly="readonly" />
                        </td>

                        <td colspan="2">
                        </td>
                     </tr>

                     <tr>
                        <td class="label"><ct:FWLabel key="AL0031_NOM_ENFANT"/></td>

                        <td>
                           <input type="text" value="<%=viewBean.getAnnonce().getAnnonceRafamModel().getNomEnfant()%>" disabled="disabled" class="readOnly" readonly="readonly" />
                        </td>

                        <td class="label"><ct:FWLabel key="AL0031_PRENOM_ENFANT"/></td>

                        <td>
                           <input type="text" value="<%=viewBean.getAnnonce().getAnnonceRafamModel().getPrenomEnfant()%>" disabled="disabled" class="readOnly" readonly="readonly" />
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
                			<div id="idTiers"> ( <ct:FWLabel key="AL0031_TIERS"/> <a href="<%=servletContext+"/pyxis?userAction=pyxis.tiers.tiers.afficher&selectedId="+viewBean.getAnnonce().getAllocataireComplexModel().getAllocataireModel().getIdTiersAllocataire()%>"><%=viewBean.getAnnonce().getAllocataireComplexModel().getPersonneEtendueComplexModel().getId()%></a>)</div>
                	</td>

                     <tr>
                        <td class="label"><ct:FWLabel key="AL0031_NSS_BENEFICIAIRE"/></td>

                        <td>
                           <input type="text" value="<%=viewBean.getAnnonce().getAllocataireComplexModel().getPersonneEtendueComplexModel().getPersonneEtendue().getNumAvsActuel()%>" disabled="disabled" class="readOnly" readonly="readonly" />
                        </td>
                        
                          <td class="label"><ct:FWLabel key="AL0031_NAISSANCE_BENEFICIAIRE"/></td>
                         
                         <td>
                           <input type="text" value="<%=viewBean.getAnnonce().getAnnonceRafamModel().getDateNaissanceAllocataire()%>" disabled="disabled" class="readOnly" readonly="readonly" />
                        </td>
                        
                          <td class="label"><ct:FWLabel key="AL0031_IDE_EMPLOYEUR"/></td>
                         
                         <td>
                           <input type="text" value="<%=(JadeStringUtil.isBlankOrZero(viewBean.getAnnonce().getAnnonceRafamModel().getNumeroIDE()))?"":viewBean.getAnnonce().getAnnonceRafamModel().getNumeroIDE()%>" disabled="disabled" class="readOnly" readonly="readonly" />
                        </td>
                     </tr>

                     <tr>
                     <td class="label"><ct:FWLabel key="AL0031_NOM_BENEFICIAIRE"/></td>
                     
                   
                        

                        <td>
                           <input type="text" value="<%=viewBean.getAnnonce().getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation1()%>" disabled="disabled" class="readOnly" readonly="readonly" />
                        </td>

                        <td class="label"><ct:FWLabel key="AL0031_PRENOM_BENEFICIAIRE"/></td>

                        <td>
                           <input type="text" value="<%=viewBean.getAnnonce().getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation2()%>" disabled="disabled" class="readOnly" readonly="readonly" />
                        </td>

                        <td colspan="2">
                        </td>
                     </tr>

                     <tr>
                        <td class="label"><ct:FWLabel key="AL0031_STATUT_FAMILIAL"/></td>

                        <td>
                           <input type="text" value="<%=JadeStringUtil.isBlankOrZero(viewBean.getAnnonce().getAnnonceRafamModel().getCodeStatutFamilial())?"":objSession.getCodeLibelle(RafamFamilyStatus.getFamilyStatus(viewBean.getAnnonce().getAnnonceRafamModel().getCodeStatutFamilial()).getCS())%>" disabled="disabled" class="readOnly" readonly="readonly" />
                        </td>

                        <td class="label"><ct:FWLabel key="AL0031_TYPE_ACTIVITE"/></td>

                        <td>
                           <input type="text" value="<%=JadeStringUtil.isBlankOrZero(viewBean.getAnnonce().getAnnonceRafamModel().getCodeTypeActivite())?"":objSession.getCodeLibelle(RafamOccupationStatus.getOccupationStatus(viewBean.getAnnonce().getAnnonceRafamModel().getCodeTypeActivite()).getCS())%>" disabled="disabled" class="readOnly" readonly="readonly" />
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
                           <input type="text" value="<%=viewBean.getAnnonce().getAnnonceRafamModel().getNssAllocataire()%>" disabled="disabled" class="readOnly" readonly="readonly" />
                        </td>
                        
                          <td class="label"><ct:FWLabel key="AL0031_NAISSANCE_BENEFICIAIRE"/></td>
                         
                         <td>
                           <input type="text" value="<%=viewBean.getAnnonce().getAnnonceRafamModel().getDateNaissanceAllocataire()%>" disabled="disabled" class="readOnly" readonly="readonly" />
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
                           <input type="text" value="<%=viewBean.getAnnonce().getAnnonceRafamModel().getNomAllocataire()%>" disabled="disabled" class="readOnly" readonly="readonly" />
                        </td>

                        <td class="label"><ct:FWLabel key="AL0031_PRENOM_BENEFICIAIRE"/></td>

                        <td>
                           <input type="text" value="<%=viewBean.getAnnonce().getAnnonceRafamModel().getPrenomAllocataire()%>" disabled="disabled" class="readOnly" readonly="readonly" />
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
<%if (viewBean.canValidate()) {%>
	<input class="btnCtrl" id="btnValider" type="button" value="Valider Annonce" onclick="validerAnnonce()">
<%}%>
<%if (RafamEtatAnnonce.RECU.equals(RafamEtatAnnonce.getRafamEtatAnnonceCS(viewBean.getAnnonce().getAnnonceRafamModel().getEtat()))) 
	{%><input class="btnCtrl" id="btnSuspendu" type="button" value="Suspendre Annonce" onclick="suspendreAnnonce()"><%}
%>

				<%-- tpl:insert attribute="zoneButtons" --%>
<script type="text/javascript">
document.getElementById("btnUpd").style.visibility="hidden";
document.getElementById("btnVal").style.visibility="hidden";
document.getElementById("btnCan").style.visibility="hidden";
<% if(!viewBean.canDelete()) {%>
document.getElementById("btnDel").style.visibility="hidden";
<%}%>
</script>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf"%>
<ct:menuChange displayId="menu" menuId="menuWEBAF" showTab="menu"/>	
<ct:menuChange displayId="options" menuId="dossier-droit-detail" checkAdd="no"
	showTab="options">
	<ct:menuSetAllParams key="selectedId" checkAdd="no"
		value="<%=viewBean.getAnnonce().getDossierModel().getIdDossier()%>" />
	<ct:menuSetAllParams key="idDroit"  checkAdd="no"
		value="<%=viewBean.getAnnonce().getDroitComplexModel().getDroitModel().getIdDroit()%>"  />
</ct:menuChange>
<%-- tpl:insert attribute="zoneEndPage" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf"%>


