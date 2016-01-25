<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="ch.globaz.envoi.business.services.models.parametrageEnvoi.SimpleDefinitionFormuleService"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%@page import="ch.globaz.pyxis.business.service.AdresseService"%>
<%@page import="ch.globaz.envoi.business.services.models.parametrageEnvoi.SimpleDefinitionFormuleService"%>
<%-- tpl:put name="zoneInit" --%>
<%-- @ taglib uri="/WEB-INF/amtaglib.tld" prefix="ai" --%>

<%
    globaz.amal.vb.formule.AMFormuleViewBean viewBean = (globaz.amal.vb.formule.AMFormuleViewBean)session.getAttribute("viewBean");
	selectedIdValue = viewBean.getId();	
	boolean isNew = viewBean.isNew();
	userActionValue = "amal.formule.formule.modifier";
	
	
    btnUpdLabel = objSession.getLabel("MODIFIER");
    btnDelLabel = objSession.getLabel("SUPPRIMER");
    btnValLabel = objSession.getLabel("VALIDER");
    btnCanLabel = objSession.getLabel("ANNULER");
    btnNewLabel = objSession.getLabel("NOUVEAU");
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
<!--hide this script from non-javascript-enabled browsers
function add() {
    document.forms[0].elements('userAction').value="amal.formule.formule.ajouter";
}
function upd() {
	//onChangeEnvoiDepuis();
	//document.getElementById('formule.definition.formule.document').value = document.getElementById('widget1').value;
	//document.getElementsByName("formule.definitionformule.csDocument")[0].disabled=false;
	//document.getElementsByName("formule.definitionformule.csDocument")[0].readOnly=false;	
	//document.getElementById('formule.definition.formule.csDocument').value = document.getElementById('widget1').id;
	document.getElementsByName("formule.formule.libelleDocument")[0].disabled=false;
	document.getElementsByName("formule.formule.libelleDocument")[0].readOnly=false;
}
function desactivate(){
	//document.getElementsByName("libelleDocument")[0].readOnly=false;			
}
function validate() {
	//alert("VALIDATE");
    //state = validateFields();
    if (document.forms[0].elements('_method').value == "add"){
    	//alert("ADD"+ document.getElementById('widget1').value + "ID"+ document.getElementById('widget1').id);
    	document.getElementById('formule.definition.formule.csDocument').value = document.getElementById('widget1').id;
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
	var msgDelete = '';
    if (window.confirm(msgDelete)){
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
					<%-- Définiton --%>
					<TR>
						<TD width="20%"><ct:FWLabel key="JSP_AM_LIBELLE_LIB"/></TD>
						<TD width="80%" colspan="4"  align="left">
						
							<%if(viewBean.isNew()){%>
								<%--<INPUT  type="text" id="formule.definitionformule.document" name="formule.definitionformule.document" value="<%=viewBean.getDefinitionFormule().getDefinitionformule().getDocument()%>" class="libelleLongDisabled" tabindex="-1">--%>
									
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
									<input id="formule.definition.formule.csDocument" name="formule.definitionformule.csDocument" type="hidden" class="formule1" tabindex="-1"/>
									<ct:widget  name="widget1"  id="widget1" notation='data-g-string="mandatory:true"' styleClass="libelle" defaultValue="">
									<ct:widgetService defaultLaunchSize="1" methodName="searchLibelle" className="<%=ch.globaz.envoi.business.services.models.parametrageEnvoi.SimpleDefinitionFormuleService.class.getName()%>">
										<ct:widgetCriteria criteria="forLibelle" label="JSP_AM_FORMULE_D_LIBELLE"/>				
										<ct:widgetLineFormatter format="#{nom}"/>
										<ct:widgetJSReturnFunction>
											<script type="text/javascript">
												function(element){
													this.value=$(element).attr('nom')+'';this.id=$(element).attr('csDocument')
												}
											</script>										
										</ct:widgetJSReturnFunction>
									</ct:widgetService>
									</ct:widget>
									
									
									
								</div>
								
								
															
							<%} else{%>
							<INPUT type="hidden"" id="formule.definitionformule.csDocument" name="formule.definitionformule.csDocument" value="<%=viewBean.getDefinitionFormule().getDefinitionformule().getCsDocument()%>" tabindex="-1">
							<INPUT type="text" id="formule.definitionformule.lcsDocument" name="formule.definitionformule.lcsDocument" value="<%=objSession.getCodeLibelle(viewBean.getDefinitionFormule().getDefinitionformule().getCsDocument())%>" class="libelleLongDisabled" readonly="readonly" tabindex="-1">
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
								<table  width="100%" height="150px" >
									<TR>
										<TD width="100%" colspan="5" valign="top">
										<TABLE border="0" cellpadding="2"  cellspacing="0" width="100%" style="margin:10px 0px;">
											<TR>
												<TD width="20%"><ct:FWLabel key="JSP_AM_NOMWORD_LIB"/></TD>
												<TD width="25%">
												<%if(viewBean.isNew()){%>
													 <INPUT type="text" class="numeroId" name="formule.formule.libelleDocument" value="<%=viewBean.getLibelleDocument()%>" tabindex="1">							
												<%} else{%>
													 <INPUT type="text" class="numeroId" name="formule.formule.libelleDocument" disabled="disabled" value="<%=viewBean.getLibelleDocument()%>" tabindex="1" readonly="readonly">
												<%}%>
												
												   
												</TD>
												<TD width="10%">&nbsp;</TD>
												<TD width="20%"></TD>
												<TD width="25%">
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
												<TD width="20%"><ct:FWLabel key="JSP_AM_LANGUE_LIB"/></TD>
												<TD width="35%" colspan="2">
													<ct:FWCodeSelectTag codeType="PYLANGUE" name="formule.formule.csLangue" wantBlank="false" defaut="<%=viewBean.getFormule().getFormule().getCsLangue()%>"/>
												</TD>
												<TD width="45%" colspan="2">&nbsp;</TD>
											</TR>
				
											<%-- Type envoi (Envoi manuel) --%>
											<TR>
												<TD width="20%"><ct:FWLabel key="JSP_AM_TYPEENVOI_LIB"/></TD>
												<TD width="35%" colspan="2">
													<%--<INPUT type="hidden" name="csManuAuto" value='viewBean.getCsManuAuto()'> 
													<ct:select styleClass="typeEnvoi" defaultValue=""  name="champTypeEnvoi"  notation="data-g-select='mandatory:true'">
															<ct:optionsCodesSystems  csFamille="AMTYENVOI">
															</ct:optionsCodesSystems>
													</ct:select> --%>
													<ct:FWCodeSelectTag codeType="AMTYENVOI" name="formule.definitionformule.csManuAuto"  wantBlank="false" defaut="<%=viewBean.getFormule().getDefinitionformule().getCsManuAuto()%>"/>
																
												</TD>
												<TD width="45%" colspan="2">&nbsp;</TD>
											</TR>
				
											<%-- Séquence impres. // Ged --%>
											<TR>
												<TD width="20%"><ct:FWLabel key="JSP_AM_TYPEIMPRESSION_LIB"/></TD>
												<TD width="35%" colspan="2">
													<%--<INPUT type="hidden" name="csSequenceImpression" value='viewBean.getCsSequenceImpression()'> 
													<ct:select styleClass="typeImpression" defaultValue=""  name="champTypeImpression"  notation="data-g-select='mandatory:true'">
															<ct:optionsCodesSystems  csFamille="AMIMPR">
																<%--ct:excludeCode code="64009003"/>
															</ct:optionsCodesSystems>
													</ct:select>--%>
													<ct:FWCodeSelectTag codeType="AMIMPR" name="formule.formule.csSequenceImpression"  wantBlank="false" defaut="<%=viewBean.getFormule().getFormule().getCsSequenceImpression()%>"/>
												</TD>
												<TD width="45%" colspan="2" style="font-style: italic"><ct:FWLabel key="JSP_AM_GED_LIB"/></TD>
											</TR>
				
											<%-- Sauvegarde // Indexation automat. --%>
											<TR>
												<TD width="20%"></TD>
												<TD width="25%">
												</TD>
												<TD width="10%">&nbsp;</TD>
												<TD width="20%"><ct:FWLabel key="JSP_AM_INDEXAUTO_LIB"/></TD>
												<TD width="25%">
												<%-- 	<INPUT type="hidden" name="csIndexationGed" value='viewBean.getCsIndexationGed()'> 
													<ct:select styleClass="destinataire" defaultValue=""  name="champDestinataire"  notation="data-g-select='mandatory:true'">
															<ct:optionsCodesSystems  csFamille="LEOUINON">
															</ct:optionsCodesSystems>
													</ct:select> --%>
													<ct:FWCodeSelectTag codeType="LEOUINON" name="formule.formule.csIndexationGed"  wantBlank="false" defaut="<%=viewBean.getFormule().getFormule().getCsIndexationGed()%>"/>
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
													<ct:FWCodeSelectTag codeType="LEOUINON" name="formule.definitionformule.csDestinataire"  wantBlank="false" defaut="<%=viewBean.getFormule().getDefinitionformule().getCsDestinataire()%>"/>
												</TD>
												<TD width="10%">&nbsp;</TD>
												<TD width="20%"><ct:FWLabel key="JSP_AM_COPIE_LIB"/></TD>
												<TD width="25%">
												<%--	<INPUT type="hidden" name="csCopie" value='viewBean.getCsCopie()'> 
									
													<ct:select styleClass="copie" defaultValue=""  name="copie"  notation="data-g-select='mandatory:true'">
															<ct:optionsCodesSystems  csFamille="LEOUINON">
															</ct:optionsCodesSystems>
													</ct:select> --%>
													<ct:FWCodeSelectTag codeType="LEOUINON" name="formule.definitionformule.csCopie"  wantBlank="false" defaut="<%=viewBean.getFormule().getDefinitionformule().getCsCopie()%>"/>
												</TD>
											</TR>
					
											<%-- Delai de garde // Nombre d'originaux --%>
											<TR>
												<TD width="20%"><ct:FWLabel key="JSP_AM_DELAIGARDE_LIB"/></TD>
												<TD width="25%">
													<INPUT type="text" class="numeroCourt" name="formule.formule.delaiDeGarde" value="<%=viewBean.getFormule().getFormule().getDelaiDeGarde()%>" tabindex="9">
													&nbsp;
													<ai:AIProperty key="JOURS"/>
												</TD>
												<TD width="10%">&nbsp;</TD>
												<TD width="20%"><ct:FWLabel key="JSP_AM_NBORIGINAUX_LIB"/></TD>
												<TD width="25%">
													<INPUT type="text" class="numeroCourt" name="formule.formule.nbCopie" value="<%=viewBean.getFormule().getFormule().getNbCopie()%>" tabindex="14">
												</TD>
							 				</TR>
								
											<%-- Séparateur --%>
											<TR>
												<TD width="100%" colspan="5"><HR></TD>
											</TR>
	
											<%-- Information --%>
											<% int tabIndex=15; %>
			
										</TABLE>
										</TD>
									</TR>
								</table>
							<%--/div--%>
						
						
						</td>	
					</tr>

								
					<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%--<%if(!("add".equalsIgnoreCase(request.getParameter("_method")))){%>
	<ct:menuChange displayId="options" menuId="optionsAIAdminFormule"/>
	<ct:menuSetAllParams key="idFormule" value="<%=viewBean.getId()%>" menuId="optionsAIAdminFormule"/>
	<ct:menuSetAllParams key="csProvenance" value="13" menuId="optionsAIAdminFormule"/>
<%}%>--%>
<%if(!("add".equalsIgnoreCase(request.getParameter("_method")))){%>
<ct:menuChange displayId="options" menuId="amal-optionsformules"/>
<ct:menuSetAllParams key="idFormule" value="viewBean.getIdFormule()" menuId="amal-optionsformules"/>
<ct:menuSetAllParams key="csProvenance" value="globaz.ai.constantes.IConstantes.CS_PRO_ENVOI_FORMULE" menuId="amal-optionsformules"/>
<%}%>
<SCRIPT language="javascript">
if (document.forms[0].elements('_method').value != "add") {
	reloadMenuFrame(top.fr_menu, MENU_TAB_OPTIONS);
}
</SCRIPT>
<script language="javascript">
	document.getElementById('btnVal').tabIndex='<%=++tabIndex%>';
	document.getElementById('btnCan').tabIndex='<%=++tabIndex%>';
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>
