<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="ch.globaz.amal.business.constantes.IAMParametres"%>
<%@page import="globaz.envoi.constantes.ENConstantes"%>
<%@page import="ch.globaz.envoi.business.services.models.parametrageEnvoi.SimpleDefinitionFormuleService"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%@page import="ch.globaz.pyxis.business.service.AdresseService"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="ch.globaz.envoi.business.services.models.parametrageEnvoi.SimpleDefinitionFormuleService"%>
<%-- tpl:put name="zoneInit" --%>
<%-- @ taglib uri="/WEB-INF/amtaglib.tld" prefix="ai" --%>

<%
    globaz.amal.vb.formule.AMFormuleViewBean viewBean = (globaz.amal.vb.formule.AMFormuleViewBean)session.getAttribute("viewBean");
	selectedIdValue = viewBean.getId();	
	boolean isNew = viewBean.isNew();
	userActionValue = "amal.formule.formule.modifier";

%>
<% idEcran="AMAL0001"; %>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%--<%@ include file="/pegasusRoot/ajax/javascriptsAndCSS.jspf" %> --%>
<script type="text/javascript" src="<%=servletContext%>/scripts/widget/globazwidget.js"></script>
<SCRIPT type="text/javascript" src="<%=servletContext%>/scripts/onglet.js"></SCRIPT>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">

$(document).ready(function() {
	// -------------------------------------------------------------------------------
	// INITIALISATION
	// -------------------------------------------------------------------------------
	// attribue une id à tous les champs ayant un nom mais pas encore d'id
	$('*',document.forms[0]).each(function(){
		if(this.name!=null && this.id==""){
			this.id=this.name;
		}
	});
	
	$('input:checkbox').click(function(){
		var s_baseName = 'parametreModelComplex\\.simpleParametreModel\\.';
		var s_completeId = s_baseName+this.id;
		$('#'+s_completeId).prop('value',$(this).prop('checked'));
		$(this).prop('value',$(this).prop('checked'));
	});
});

function onClickExporter(){
	//alert("Exporter:");
    document.forms[0].action.value = "exporter";
    document.forms[0].elements('userAction').value="amal.formule.formule.exporter";
	//alert("actionTTO:" + document.forms[0].elements('userAction').value );
	action(COMMIT);
}

function add() {
    document.forms[0].elements('userAction').value="amal.formule.formule.ajouter";
}
function upd() {
	//onChangeEnvoiDepuis();
	//document.getElementById('formule.definition.formule.document').value = document.getElementById('widget1').value;
	//document.getElementsByName("formule.definitionformule.csDocument")[0].disabled=false;
	//document.getElementsByName("formule.definitionformule.csDocument")[0].readOnly=false;	
	//document.getElementById('formule.definition.formule.csDocument').value = document.getElementById('widget1').id;
	document.getElementsByName("formule.formule.libelleDocument").disabled=false;
	document.getElementsByName("formule.formule.libelleDocument").readOnly=false;
}
function desactivate(){
	//document.getElementsByName("libelleDocument")[0].readOnly=false;			
}
function validate() {
	//alert("VALIDATE");
    //state = validateFields();
    if (document.forms[0].elements('_method').value == "add"){
   	//alert("ADD"+ document.getElementById('widget1').value + "ID"+ document.getElementById('widget1').id);    	
    	document.getElementById('definitionformule.csDocument').value = document.getElementById('widget1').id;
        document.forms[0].elements('userAction').value="amal.formule.formule.ajouter";
    } else {
        document.forms[0].elements('userAction').value="amal.formule.formule.modifier";
    }
    
    return true;

}
function cancel() {
	  document.forms[0].elements('userAction').value="amal.formule.formule.afficher";
}
function del() {
	
    if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
        document.forms[0].elements('userAction').value="amal.formule.formule.supprimer";
        document.forms[0].submit();
    }
}

function init(){
	//switchOnglets(document.getElementById('ongletActif').value, new Array('onglet1', 'onglet2'));
	//document.getElementsByName("libelleDocument")[0].disabled=true;	
	//alert("test");
}


/*
*/
// stop hiding -->
</SCRIPT>
<%-- /tpl:put  viewBean.getDefinitionFormule().getCsDocument()--%>

<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
			<ai:AIProperty key="DETAIL_FORMULE"/>
			<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%@page import="globaz.amal.vb.formule.AMFormuleViewBean"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Collection"%>
						<%-- tpl:put name="zoneMain" --%>
						<INPUT type="hidden" name="selectorName" value="">
<ct:inputHidden name="modeGeneration"/>
<ct:inputHidden name="fileInput"/>
<ct:inputHidden name="action"/>	
					<%-- Définiton --%>
			
					<TR>
						<TD width="20%"><ct:FWLabel key="JSP_AM_PARAMETRAGE_R_FORMULE"/></TD>
						<TD width="80%" colspan="4"  align="left">
						
							<%if(viewBean.isNew()){%>
								<%--<INPUT  type="text" id="formule.definitionformule.document" name="formule.definitionformule.document" value="<%=viewBean.getDefinitionFormule().getDefinitionFormule().getDocument()%>" class="libelleLongDisabled" tabindex="-1">--%>
									
								<div id="areaMembre1" class="areaMembre1" align="left">
										<%--<INPUT type="text" id="document" name="formule.definitionformule.document"  class="libelleLongDisabled" tabindex="-1">															
										<input id="formule.definition.formule.document" name="formule.definitionformule.document" type="hidden" class="formule1" tabindex="-1"/>--%>
							<%--			<script type="text/javascript">
								$(function(){
								$('#widgetTiers42').globazWidget(
									'ch.globaz.amal.business.services.models.formule.SimpleDefinitionFormuleService',
									'search',
									'',
									'forDocument',
									'',
									'#{document}',
									'document',
									['Libellé'],
									'20',
									function(element){$('#document').val($(element).attr('document'));
													  this.value=$(element).attr('document')+'';});
								});</script><input id="widgetTiers42" class="jadeAutocompleteAjax widgetTiers" name="widgetTiers42" type="text"/>--%>
									<input id="definitionformule.csDocument" name="parametreModelComplex.formuleList.definitionformule.csDocument" type="hidden" class="formule1" tabindex="-1"/>
									<ct:widget  name="widget1"  id="widget1" notation='data-g-string="mandatory:true"' styleClass="libelle" defaultValue="">
									<ct:widgetService defaultLaunchSize="1" methodName="searchFormule" className="<%=ch.globaz.envoi.business.services.models.parametrageEnvoi.SimpleDefinitionFormuleService.class.getName()%>">
										<ct:widgetCriteria criteria="likeLibelleCosp" label="JSP_AM_FORMULE_D_LIBELLE_PCOSLI"/>
										<ct:widgetCriteria criteria="likeLibelle" label="JSP_AM_FORMULE_D_LIBELLE"/>													
										<ct:widgetLineFormatter format="#{simpleFwcosp.libelle},#{simpleFwcoup.libelle}"/>
								
										<ct:widgetJSReturnFunction>
											<script type="text/javascript">
												function(element){
													this.value=$(element).attr('simpleFwcosp.libelle');
													$('#libel').val($(element).attr('simpleFwcoup.libelle'));
													$('#nomWord').val($(element).attr('simpleFwcosp.libelle'));
													this.id=$(element).attr('simpleFwcosp.pcosid');
												}
											</script>										
										</ct:widgetJSReturnFunction>
									</ct:widgetService>
									</ct:widget>
								</div>
							<%} else{%>
							<%--<INPUT type="hidden"" id="definitionformule.csDocument"  name="parametreModelComplex.formuleList.definitionformule.csDocument" value="<%=viewBean.getDefinitionFormule().getCsDocument()%>" tabindex="-1">
							<INPUT type="text" id="definitionformule.lcsDocument" name="parametreModelComplex.formuleList.definitionformule.lcsDocument" value="<%=objSession.getCodeLibelle(viewBean.getDefinitionFormule().getCsDocument())%>" class="libelleLongDisabled" readonly="readonly" tabindex="-1">--%>
							<INPUT type="text" class="numeroId" id="nomWord" name="formule.libelleDocument" disabled="disabled" value="<%=viewBean.getLibelleDocument()%>" tabindex="1" readonly="readonly">
							<%}%>
						</TD>
					</TR>
					
					<TR>
						<TD width="20%"><ct:FWLabel key="JSP_AM_LIBELLE_LIB"/></TD>
						<TD width="25%">
							<%if(viewBean.isNew()){%>
								<INPUT type="hidden" class="numeroId" id="nomWord" name="formule.libelleDocument"  tabindex="1">							
								<INPUT type="text" class="numeroId" id="libel" name="parametreModelComplex.formuleList.definitionformule.csDocument"  tabindex="1">							
							<%} else{%>
								<%--<INPUT type="text" class="numeroId" id="nomWord" name="formule.libelleDocument" disabled="disabled" value="<%=viewBean.getLibelleDocument()%>" tabindex="1" readonly="readonly">
								<INPUT type="hidden"" id="definitionformule.csDocument"  name="parametreModelComplex.formuleList.definitionformule.csDocument" value="<%=viewBean.getDefinitionFormule().getCsDocument()%>" tabindex="-1">
								--%>
								<INPUT type="text" id="definitionformule.lcsDocument" name="parametreModelComplex.formuleList.definitionformule.lcsDocument" value="<%=objSession.getCodeLibelle(viewBean.getDefinitionFormule().getCsDocument())%>" class="libelleLongDisabled" readonly="readonly" tabindex="-1">
							
							<%}%>
																						   
						</TD>
					</TR>
					
			
					<%-- Séparateur --%>
					<TR>
						<TD width="100%" colspan="5"><HR></TD>
					</TR>
					
		
					
						<tr valign="top">
						<td colspan="5" valign="top">
							<%--div id="onglet1Detail" style="background-color:#E8EEF4; border-left: 1px solid white; border-right: 1px solid white; padding-left: 10px; border-right: 1px solid white; border-bottom: 1px solid white; border-top: 1px solid white;"--%>
								<table   width="100%" height="150px" >
									<TR>
										<TD width="100%" colspan="5" valign="top">
										<TABLE border="0" cellpadding="3"  align="left"  width="45%" style="border: 1px solid black;	background-color: #D7E4FF;margin:10px 0px;">
											<TR><TD style="font: bold;" width="20%">GENERAL</TD></TR>
											
											<TR>
												<TD>&nbsp;</TD>
											</TR>
											<%-- <%
											String defaultValue = "";//viewBean.getCsLangue();
											//if(defaultValue == null || defaultValue.trim().length()==0){
												//defaultValue = objSession.getSystemCode("LANGU", objSession.getIdLangue());
											//}
											%>							
				
											<%-- Langue --%>
											<TR>
												<TD width="20%"><ct:FWLabel key="JSP_AM_LANGUE_LIB"/></TD>
												<TD width="25%">
													<ct:FWCodeSelectTag codeType="PYLANGUE" name="formule.csLangue" wantBlank="false" defaut="503001"/>
												</TD>
	
											</TR>
				
											<%-- Type envoi (Envoi manuel) --%>
											<TR>
												<TD width="20%"><ct:FWLabel key="JSP_AM_TYPEENVOI_LIB"/></TD>
												<TD width="25%">
													<%--<INPUT type="hidden" name="csManuAuto" value='viewBean.getCsManuAuto()'> 
													<ct:select styleClass="typeEnvoi" defaultValue=""  name="champTypeEnvoi"  notation="data-g-select='mandatory:true'">
															<ct:optionsCodesSystems  csFamille="AMTYENVOI">
															</ct:optionsCodesSystems>
													</ct:select> --%>
													<ct:FWCodeSelectTag codeType="AMTYENVOI" name="parametreModelComplex.formuleList.definitionformule.csManuAuto"  wantBlank="false" defaut="<%=viewBean.getDefinitionFormule().getCsManuAuto()%>"/>
																
												</TD>
						
											</TR>
				
											<%-- Séquence impres. // Ged --%>
											<TR>
												<TD width="20%"><ct:FWLabel key="JSP_AM_TYPEIMPRESSION_LIB"/></TD>
												<TD width="25%">
													<%--<INPUT type="hidden" name="csSequenceImpression" value='viewBean.getCsSequenceImpression()'> 
													<ct:select styleClass="typeImpression" defaultValue=""  name="champTypeImpression"  notation="data-g-select='mandatory:true'">
															<ct:optionsCodesSystems  csFamille="AMIMPR">
																<%--ct:excludeCode code="64009003"/>
															</ct:optionsCodesSystems>
													</ct:select>--%>
													<ct:FWCodeSelectTag codeType="AMIMPR" name="formule.csSequenceImpression"  wantBlank="false" defaut="<%=viewBean.getFormule().getCsSequenceImpression()%>"/>
												</TD>
	
											</TR>
				
											<%-- Sauvegarde // Indexation automat. --%>
											<TR>
												<TD width="20%"></TD>
												<TD width="25%">
												</TD>
					
											</TR>
				

				
											<%-- Destinataire // Copie --%>
											<TR>
												<TD width="20%"><ct:FWLabel key="JSP_AM_DESTINATAIRE_LIB"/></TD>
												<TD width="25%">
													<INPUT type="hidden" name="csDestinataire" value='viewBean.getCsDestinataire()'> 
													<%--<ct:select styleClass="destinataire" defaultValue=""  name="champDestinataire"  notation="data-g-select='mandatory:true'">
															<ct:optionsCodesSystems  csFamille="LEOUINON">
															</ct:optionsCodesSystems>
													</ct:select> --%>
													<ct:FWCodeSelectTag codeType="LEOUINON" name="parametreModelComplex.formuleList.definitionformule.csDestinataire"  wantBlank="false" defaut="<%=viewBean.getDefinitionFormule().getCsDestinataire()%>"/>
												</TD>
									
											</TR>
					
											<%-- Delai de garde // Nombre d'originaux --%>
											<TR>
												<TD width="20%"><ct:FWLabel key="JSP_AM_DELAIGARDE_LIB"/></TD>
												<TD width="25%">
													<INPUT type="text" class="numeroCourt" name="formule.delaiDeGarde" value="<%=viewBean.getFormule().getDelaiDeGarde()%>" tabindex="9">
													&nbsp;
													<ai:AIProperty key="JOURS"/>
												</TD>
									
							 				</TR>
							 				
							 				<%-- Sauvegarde // Indexation automat. --%>
											<TR>
	
												<TD width="20%"><ct:FWLabel key="JSP_AM_GED_LIB"/>&nbsp;:&nbsp;<ct:FWLabel key="JSP_AM_INDEXAUTO_LIB"/></TD>
												<TD width="25%">
												<%-- 	<INPUT type="hidden" name="csIndexationGed" value='viewBean.getCsIndexationGed()'> 
													<ct:select styleClass="destinataire" defaultValue=""  name="champDestinataire"  notation="data-g-select='mandatory:true'">
															<ct:optionsCodesSystems  csFamille="LEOUINON">
															</ct:optionsCodesSystems>
													</ct:select> --%>
													<ct:FWCodeSelectTag codeType="LEOUINON" name="formule.csIndexationGed"  wantBlank="false" defaut="<%=viewBean.getFormule().getCsIndexationGed()%>"/>
												</TD>
											</TR>
				

				
											<%-- Destinataire // Copie --%>
											<TR>

												<TD width="20%"><ct:FWLabel key="JSP_AM_COPIE_LIB"/></TD>
												<TD width="25%">
												<%--	<INPUT type="hidden" name="csCopie" value='viewBean.getCsCopie()'> 
									
													<ct:select styleClass="copie" defaultValue=""  name="copie"  notation="data-g-select='mandatory:true'">
															<ct:optionsCodesSystems  csFamille="LEOUINON">
															</ct:optionsCodesSystems>
													</ct:select> --%>
													<ct:FWCodeSelectTag codeType="LEOUINON" name="parametreModelComplex.formuleList.definitionformule.csCopie"  wantBlank="false" defaut="<%=viewBean.getDefinitionFormule().getCsCopie()%>"/>
												</TD>
											</TR>
					
											<%-- Delai de garde // Nombre d'originaux --%>
											<TR>
	
												<TD width="20%"><ct:FWLabel key="JSP_AM_NBORIGINAUX_LIB"/></TD>
												<TD width="25%">
													<INPUT type="text" class="numeroCourt" name="formule.nbCopie" value="<%=viewBean.getFormule().getNbCopie()%>" tabindex="14">
												</TD>
							 				</TR>
							 				
							 				<TR>
												<TD>&nbsp;</TD>
											</TR>
																		
	
											<%-- Information --%>
											<% int tabIndex=15; %>
			
										</TABLE>
										
									<%-- LFO --%>
										<table border="0" cellpadding="3"  align="right"  width="45%" style="border: 1px solid black;	background-color: #D7E4FF;margin:10px 0px;">
												
										
											<TR><TD style="font: bold;" width="20%">AMAL</TD></TR>
											<TR>
												<TD>&nbsp;</TD>
											</TR>
											<TR>

												<TD width="20%"><ct:FWLabel key="JSP_AM_FORMULE_D_ANNONCE_CAISSE"/></TD>
												<TD width="25%">
													<select disabled="disabled" name="parametreModelComplex.simpleParametreModel.codeAnnonceCaisse">
														<option <%=(!isNew && viewBean.getParametreModelComplex().getSimpleParametreModel().getCodeAnnonceCaisse()==false?"selected=\"selected\"":"")%> value="false">Non</option>
														<option <%=(!isNew && viewBean.getParametreModelComplex().getSimpleParametreModel().getCodeAnnonceCaisse()==true?"selected=\"selected\"":"")%> value="true">Oui</option>
													</select>
												</TD>
											</TR>
				
	
											<%-- <%
											String defaultValue = "";//viewBean.getCsLangue();
											//if(defaultValue == null || defaultValue.trim().length()==0){
												//defaultValue = objSession.getSystemCode("LANGU", objSession.getIdLangue());
											//}
											%>							
				
											<%-- Langue --%>
											<TR>
	
												<TD width="20%"><ct:FWLabel key="JSP_AM_FORMULE_D_CODE_DOSSIER"/></TD>
												<TD width="25%">
													<ct:FWCodeSelectTag codeType="AMTRDO" name="parametreModelComplex.simpleParametreModel.codeTraitementDossier"  wantBlank="false" defaut="<%=viewBean.getParametreModelComplex().getSimpleParametreModel().getCodeTraitementDossier()%>"/>
												</TD>
											</TR>
				
											<%-- Type envoi (Envoi manuel) --%>
											<TR>

												<TD width="20%"><ct:FWLabel key="JSP_AM_FORMULE_D_ANNEE_DEBUT"/></TD>
												<TD width="25%"><INPUT type="text" class="numeroCourt" name="parametreModelComplex.simpleParametreModel.anneeValiditeDebut" value="<%=JadeStringUtil.toNotNullString(viewBean.getParametreModelComplex().getSimpleParametreModel().getAnneeValiditeDebut())%>" tabindex="1" data-g-integer="sizeMax:4">
												</TD>
											</TR>
				
											<%-- Séquence impres. // Ged --%>
											<TR>
	
												<TD width="20%" style="margin-left: 10px;"><ct:FWLabel key="JSP_AM_FORMULE_D_ANNEE_FIN"/></TD>
												<TD width="25%"><INPUT type="text" class="numeroCourt" name="parametreModelComplex.simpleParametreModel.anneeValiditeFin" value="<%=JadeStringUtil.toNotNullString(viewBean.getParametreModelComplex().getSimpleParametreModel().getAnneeValiditeFin())%>" tabindex="1" data-g-integer="sizeMax:4"></TD>
											</TR>
											
											<%-- Type GED --%>
											<TR>
												<TD width="20%"><ct:FWLabel key="JSP_AM_TYPEGED"/></TD>
												<TD width="25%">
													<ct:FWCodeSelectTag codeType="AMTYGE" name="parametreModelComplex.simpleParametreModel.typeGed"  wantBlank="false" defaut="<%=viewBean.getParametreModelComplex().getSimpleParametreModel().getTypeGed()%>"/>
												</TD>
											</TR>
											
											<%-- Document destinataire Famille --%>
											<TR>
												<TD width="20%">Destinataire du document</TD>
												<TD width="25%">
													<ct:FWCodeSelectTag codeType="AMTYDOFAM" name="parametreModelComplex.simpleParametreModel.typeDocumentFamille"  wantBlank="false" defaut="<%=viewBean.getParametreModelComplex().getSimpleParametreModel().getTypeDocumentFamille()%>"/>
												</TD>
											</TR>
											<%-- Inscription Dossier Famille --%>
											<TR>
												<TD width="20%">Document avec liste</TD>
												<TD width="25%">
													<select disabled="disabled" name="parametreModelComplex.simpleParametreModel.documentAvecListe">
														<option <%=(!isNew && !viewBean.getParametreModelComplex().getSimpleParametreModel().getDocumentAvecListe()?"selected=\"selected\"":"")%> value="false">Non</option>
														<option <%=(!isNew && viewBean.getParametreModelComplex().getSimpleParametreModel().getDocumentAvecListe()?"selected=\"selected\"":"")%> value="true">Oui</option>
													</select>
													<INPUT type="text" class="numeroCourt" 
														name="parametreModelComplex.simpleParametreModel.nombreElementListe" 
														value="<%=JadeStringUtil.toNotNullString(viewBean.getParametreModelComplex().getSimpleParametreModel().getNombreElementListe())%>" data-g-integer="sizeMax:2">												
												</TD>
											</TR>

											<TR>
												<TD>Type de dossier</TD>
												<TD>
													<INPUT type="hidden" name="parametreModelComplex.simpleParametreModel.documentContribuable" value="<%=(!isNew && viewBean.getParametreModelComplex().getSimpleParametreModel().getDocumentContribuable()?"true":"false")%>">
													<input type="checkbox"
														name="documentContribuable"
														value="<%=(!isNew && viewBean.getParametreModelComplex().getSimpleParametreModel().getDocumentContribuable()?"true":"false")%>"
														<%=(!isNew && viewBean.getParametreModelComplex().getSimpleParametreModel().getDocumentContribuable()?" checked=\"checked\"":" ")%>
														/>Contribuable
												</TD>			
											</TR>
											<TR>
												<TD>&nbsp;</TD>
												<TD>
													<INPUT type="hidden" name="parametreModelComplex.simpleParametreModel.documentSourcier" value="<%=(!isNew && viewBean.getParametreModelComplex().getSimpleParametreModel().getDocumentSourcier()?"true":"false")%>">
													<input type="checkbox"
														name="documentSourcier"
														value="<%=(!isNew && viewBean.getParametreModelComplex().getSimpleParametreModel().getDocumentSourcier()?"true":"false")%>"
														<%=(!isNew && viewBean.getParametreModelComplex().getSimpleParametreModel().getDocumentSourcier()?" checked=\"checked\"":" ")%>
														/>Sourcier
												</TD>			
											</TR>
											<TR>
												<TD>&nbsp;</TD>
												<TD>
													<INPUT type="hidden" name="parametreModelComplex.simpleParametreModel.documentAssiste" value="<%=(!isNew && viewBean.getParametreModelComplex().getSimpleParametreModel().getDocumentAssiste()?"true":"false")%>">
													<input type="checkbox"
														name="documentAssiste"
														value="<%=(!isNew && viewBean.getParametreModelComplex().getSimpleParametreModel().getDocumentAssiste()?"true":"false")%>"
														<%=(!isNew && viewBean.getParametreModelComplex().getSimpleParametreModel().getDocumentAssiste()?" checked=\"checked\"":" ")%>
														/>Assisté
												</TD>			
											</TR>
											<TR>
												<TD>&nbsp;</TD>
												<TD>
													<INPUT type="hidden" name="parametreModelComplex.simpleParametreModel.documentPC" value="<%=(!isNew && viewBean.getParametreModelComplex().getSimpleParametreModel().getDocumentPC()?"true":"false")%>">
													<input type="checkbox"
														name="documentPC"
														value="<%=(!isNew && viewBean.getParametreModelComplex().getSimpleParametreModel().getDocumentPC()?"true":"false")%>"
														<%=(!isNew && viewBean.getParametreModelComplex().getSimpleParametreModel().getDocumentPC()?" checked=\"checked\"":" ")%>
														/>PC
												</TD>			
											</TR>

											<TR>
												<TD>Sélection</TD>
												<TD>
													<INPUT type="hidden" name="parametreModelComplex.simpleParametreModel.visibleCorrespondance" value="<%=(!isNew && viewBean.getParametreModelComplex().getSimpleParametreModel().getVisibleCorrespondance()?"true":"false")%>">
													<input type="checkbox"
														name="visibleCorrespondance"
														value="<%=(!isNew && viewBean.getParametreModelComplex().getSimpleParametreModel().getVisibleCorrespondance()?"true":"false")%>"
														<%=(!isNew && viewBean.getParametreModelComplex().getSimpleParametreModel().getVisibleCorrespondance()?" checked=\"checked\"":" ")%>
														/>Correspondance
												</TD>			
											</TR>
											<TR>
												<TD>&nbsp;</TD>
												<TD>
													<INPUT type="hidden" name="parametreModelComplex.simpleParametreModel.visibleAttribution" value="<%=(!isNew && viewBean.getParametreModelComplex().getSimpleParametreModel().getVisibleAttribution()?"true":"false")%>">
													<input type="checkbox"
														name="visibleAttribution"
														value="<%=(!isNew && viewBean.getParametreModelComplex().getSimpleParametreModel().getVisibleAttribution()?"true":"false")%>"
														<%=(!isNew && viewBean.getParametreModelComplex().getSimpleParametreModel().getVisibleAttribution()?" checked=\"checked\"":" ")%>
														/>Attribution
												</TD>			
											</TR>
											
											<TR>
												<TD>&nbsp;</TD>
											</TR>
				

		
									</TR>
								</table>
							
								
								
								<%-- Séparateur --%>
								<TR>
									<TD width="100%" colspan="5"><HR></TD>
								</TR>
								
								<TR>									
									<td>
										<img align="left" src="<%=servletContext%>/images/word.png" onclick="onClickExporter();" style="cursor: hand;"/><ct:FWLabel key="JSP_AM_FORMULE_D_GENERER_FORMULE"/>
									</td>
								</TR>
													
							</table>
						</td>	
					</tr>
					

								
					<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%--<%if(!("add".equalsIgnoreCase(request.getParameter("_method")))){%> --%>
<ct:menuChange displayId="options" menuId="amal-optionsformules"/>
<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getId()%>" menuId="amal-optionsformules"/>

<%--<%}%> --%>
<SCRIPT language="javascript">
//if (document.forms[0].elements('_method').value != "add") {
reloadMenuFrame(top.fr_menu, MENU_TAB_OPTIONS);
//}
</SCRIPT>
<script language="javascript">
	document.getElementById('btnVal').tabIndex='<%=++tabIndex%>';
	document.getElementById('btnCan').tabIndex='<%=++tabIndex%>';
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>
