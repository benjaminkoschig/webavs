<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@page import="ch.globaz.pyxis.business.service.AdministrationService"%>
<%@page import="ch.globaz.pyxis.business.service.AdresseService"%>
<%@page import="globaz.pegasus.utils.PCGestionnaireHelper"%>
<%@page import="globaz.pegasus.vb.transfertdossier.PCDemandeTransfertDossierViewBean"%>
<%@page import="java.util.Iterator"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%-- tpl:put name="zoneInit" --%>
<%
	// Les labels de cette page commence par la préfix "JSP_DEMANDE_TRANSFERT_DOSSIER"

	idEcran="PPC2007";
	
	PCDemandeTransfertDossierViewBean viewBean = (PCDemandeTransfertDossierViewBean)session.getAttribute("viewBean");
	
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
<SCRIPT language="javascript">
var userAction = "<%=IPCActions.ACTION_DEMANDE_TRANSFERT_DOSSIER_TRANSFERT %>.executer";

$(function(){
	$("#btnCtrlJade").hide();
	
	$("#printCommBtn").click(function(){
		$("[name=userAction]").val(userAction);
		$("[name=annexes]").val(getSelectOptionValues($('#annexeSelect')));
		$("[name=copies]").val(getSelectOptionValues($('#copieSelect')));
		$("[name=mainForm]").submit();
	});
});

function getSelectOptionValues($select){
	var results=[];
	$select.find('option').each(function(){
		results.push(this.value);
	});
	return results.join('¦');
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
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_DEMANDE_TRANSFERT_DOSSIER_D_TITRE"/> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><LABEL for="eMailAddress"><ct:FWLabel key="JSP_DEMANDE_TRANSFERT_DOSSIER_D_EMAIL"/></LABEL></TD>
							<TD><INPUT type="text" data-g-string="mandatory:true" name="mailAddress" value="<%=controller.getSession().getUserEMail()%>" class="libelleLong" /></TD>
						</TR>
						<tr> 						
							<TD><LABEL for="gestionnaire"><ct:FWLabel key="JSP_DEMANDE_TRANSFERT_DOSSIER_D_GESTIONNAIRE"/></LABEL></TD>
							<td><ct:FWListSelectTag data="<%=globaz.pegasus.utils.PCGestionnaireHelper.getResponsableData(objSession)%>"
																defaut="<%=viewBean.getIdGestionnaire()%>" 
																name="idGestionnaire"/></td>
						</TR> 						
						<tr>
							<!-- <TD><LABEL for="dateTransfert"><ct:FWLabel key="JSP_DEMANDE_TRANSFERT_DOSSIER_D_DATE_TRANSFERT"/></LABEL></TD>
							<td>
								<input type="text" data-g-calendar="mandatory:true,type:month,currentDate:true" value="<%=viewBean.getDateTransfert() %>" name="dateTransfert" />
							</td> -->
							<TD><LABEL for="dateSurDocument"><ct:FWLabel key="JSP_DEMANDE_TRANSFERT_DOSSIER_D_DATE_SUR_DOCUMENT"/></LABEL></TD>
							<td>
								<input type="text" data-g-calendar="mandatory:true,currentDate:true" value="<%=viewBean.getDateSurDocument() %>" name="dateSurDocument" />
							</td>
						</TR> 						
						<tr>
							<TD><LABEL for="dernierDomicileLegal"><ct:FWLabel key="JSP_DEMANDE_TRANSFERT_DOSSIER_D_DERNIER_DOMICILE_LEGAL"/></LABEL></TD>
							<td>
								<ct:widget name='dernierDomicileLegal' id='dernierDomicileLegal' notation='data-g-string="mandatory:true"' styleClass="libelleLong nomDernierDomicile" defaultValue='<%= viewBean.getCaptionDernierDomicileLegal() %>'>
									<ct:widgetService methodName="findLocalite" className="<%=ch.globaz.pyxis.business.service.AdresseService.class.getName()%>">
										<ct:widgetCriteria criteria="forNpaLike" label="JSP_PC_PARAM_HOMES_W_TIERS_NPA"/>
										<ct:widgetCriteria criteria="forLocaliteUpperLike" label="JSP_PC_PARAM_HOMES_W_TIERS_LOCALITE"/>
										<ct:widgetLineFormatter format="#{numPostal}, #{localite}"/>
										<ct:widgetJSReturnFunction>
											<script type="text/javascript">
												function(element){
													$(this).val($(element).attr('numPostal')+', '+$(element).attr('localite'));
													$('#idDernierDomicileLegal').val($(element).attr('idLocalite'));
												}
											</script>										
										</ct:widgetJSReturnFunction>
									</ct:widgetService>
								</ct:widget>
								<input type="hidden" name="idDernierDomicileLegal" id="idDernierDomicileLegal" value="<%= viewBean.getIdDernierDomicileLegal() %>"/>	
							</td>
						</TR> 						
						<tr>
							<TD><LABEL for="nouvelleCaisse"><ct:FWLabel key="JSP_DEMANDE_TRANSFERT_DOSSIER_D_NOUVELLE_CAISSE"/></LABEL></TD>
							<td>
								<ct:widget id='nouvelleCaisse' name='nouvelleCaisse' notation='data-g-string="mandatory:true"' styleClass="libelleLong selecteurHome">
									<input type="hidden" class="idAdmin1" />
									<ct:widgetService methodName="find" className="<%=ch.globaz.pyxis.business.service.AdministrationService.class.getName()%>">								
									<ct:widgetCriteria criteria="forCodeAdministrationLike" label="JSP_DEMANDE_TRANSFERT_DOSSIER_W_CODE"/>
									<ct:widgetCriteria criteria="forDesignation1Like" label="JSP_DEMANDE_TRANSFERT_DOSSIER_W_NOM"/>																						
									<ct:widgetLineFormatter format="#{admin.codeAdministration} #{tiers.designation1} #{tiers.designation2}"/>
									<ct:widgetJSReturnFunction>
										<script type="text/javascript">
											function(element){
												this.value=$(element).attr('admin.codeAdministration')+' '+$(element).attr('tiers.designation1')+' '+$(element).attr('tiers.designation2');
												$('#idNouvelleCaisse').val($(element).attr('admin.idTiersAdministration'));
											}
										</script>										
									</ct:widgetJSReturnFunction>
									</ct:widgetService>
								</ct:widget>
								<input type="hidden" name="idNouvelleCaisse" id="idNouvelleCaisse" value=""/>	
							</td>
						</TR>
						<tr>
							<td><ct:FWLabel key="JSP_DEMANDE_TRANSFERT_DOSSIER_D_ANNEXES"/></td>
							<td>
								<div data-g-multistring="tagName:annexeSelect,defaultValues:¦[{value:'mentionnée',readonly:true}]¦" ></div>
								<input type="hidden" name="annexes" />
							</td>
							<td><ct:FWLabel key="JSP_DEMANDE_TRANSFERT_DOSSIER_D_COPIES"/></td>
							<td>
								<div data-g-multistring="tagName:copieSelect,mode:autocompletion,languages:¦Tiers,Administration¦,defaultValues:¦[{value:'<%=viewBean.getIdRequerant() %>',libelle:'<%=viewBean.getNomPrenomRequerant() %>'}]¦" >
									<input	id="widget-multistring-copies-tiers" 
										class="jadeAutocompleteAjax widgetTiers" 
										data-g-autocomplete="service:¦ch.globaz.pyxis.business.service.PersonneEtendueService¦,
															method:¦findByAlias¦,
															criterias:¦{
																forDesignation1Like: '<ct:FWLabel key="JSP_DEMANDE_TRANSFERT_DOSSIER_W_NOM"/>',
																forDesignation2Like: '<ct:FWLabel key="JSP_DEMANDE_TRANSFERT_DOSSIER_W_PRENOM"/>',
																forNumeroAvsActuel: '<ct:FWLabel key="JSP_DEMANDE_TRANSFERT_DOSSIER_W_AVS"/>',
																forAlias: '<ct:FWLabel key="JSP_DEMANDE_TRANSFERT_DOSSIER_W_ALIAS"/>'
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
								<input type="hidden" name="copies" />
							</td>
						</tr> 						
						<tr>
							<td colspan="2" align="right">
								<button class="ignoreReadOnly" id="printCommBtn"><ct:FWLabel key="JSP_DEMANDE_TRANSFERT_DOSSIER_D_BTN_TRANSFERT"/></button>
							</td>
						</tr>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>