<%@page import="ch.globaz.pyxis.business.service.AdministrationService"%>
<%@page import="globaz.pegasus.vb.decision.PCPrepDecisionSuppressionViewBean"%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%@ include file="/theme/process/header.jspf" %>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDecision"%>
<%@page import="globaz.pegasus.utils.BusinessExceptionHandler"%>
<%@page import="ch.globaz.pyxis.business.model.TiersSimpleModel"%>
<%@page import="ch.globaz.pyxis.business.model.PersonneSimpleModel"%>
<%@page import="globaz.pegasus.utils.PCGestionnaireHelper"%>
<%@page import="globaz.pyxis.db.adressecourrier.TIPays"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.prestation.interfaces.tiers.PRTiersHelper"%>
<%@page import="globaz.globall.parameters.FWParametersCodeManager" %>
<%@page import="globaz.globall.vb.BJadePersistentObjectViewBean"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@ page import="globaz.jade.json.MultiSelectHandler" %>
<%@page import="java.util.ArrayList" %>
<%@page import="globaz.jade.admin.user.service.JadeUserService"%>
<%@page import="globaz.jade.admin.JadeAdminServiceLocatorProvider"%>
<%
globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();

PCPrepDecisionSuppressionViewBean viewBean = (PCPrepDecisionSuppressionViewBean) session.getAttribute("viewBean"); 

//LES PREFIXES DE CETTE PAGES COMMENCENT PAR JSP_PC_PREP_DECISION_SUPPR_DE

selectedIdValue=viewBean.getId();
boolean viewBeanIsNew="add".equals(request.getParameter("_method"));

//bButtonDelete = false;
idEcran="PPC0086";
autoShowErrorPopup = false;
MultiSelectHandler handler = new MultiSelectHandler();
String titleErroxBox = objSession.getLabel("JSP_GLOBAL_ERROR_BOX_TITLE");//titre box erreur

%>
<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%@ include file="/pegasusRoot/ajax/javascriptsAndCSS.jspf" %>
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/decision/preparation.css"/>
<%-- ********************************** HACK CACHER LE LIEN AFFICHER LES ERREURS ********************** --%>
<% 
vBeanHasErrors = false;
%>
<%-- tpl:insert attribute="zoneScripts" --%>
<!--  Pour gestion de la liste a deux niveau -->
<!-- 
<script type="text/javascript" src="<%=servletContext%>/pegasusRoot/scripts/decision/decisionListHandlerPrep.js"></script>
-->
<!--  notation spécifique pc -->
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath+"Root")%>/scripts/notationsCandidate/globazPreventDoubleClick.js"></script>
<script type="text/javascript">

var ACTION_DECISION = "pegasus.decision.prepDecisionSuppression";
var actionMethod;
var userAction;

var motifTexteLibre = "<%= viewBean.getCsCodeTexteLibreMotif() %>";
var $champLibreMotifDiv;
var $champMotif;
var $valChampLibreMotif;

globazGlobal.labels = {
		PROCESS_STARTING_LABEL : "<%= objSession.getLabel("JSP_PC_PREP_DECISION_CALCUL_DE_PROCESS_MSG") %>"
};

$(function(){
	actionMethod=$('[name=_method]',document.forms[0]).val();
	userAction=$('[name=userAction]',document.forms[0])[0];
	
	$champLibreMotifDiv = $('#champLibreMotif').hide();
	$champMotif = $('#motifListe');
	$valChampLibreMotif = $('#valChampLibreMotif');
	
	
	//initialisation si motif texte libre en premier
	var initMotif = $('#motifListe :selected').attr('value');
	
	if(initMotif === motifTexteLibre){
		$champLibreMotifDiv.show();
	}
	
	$champMotif.change(function () {
		var csMotif = $('#motifListe :selected').attr('value');
		//alert(csMotif);
		
		if(csMotif === motifTexteLibre){	
			$champLibreMotifDiv.show();
		}else{
			$champLibreMotifDiv.hide();
			$valChampLibreMotif.html('');
		}
	});
	
	
	//gestion erreurs
	pegasusErrorsUtils.dealErrors(<%= request.getParameter("decisionErrorMsg") %>,"<%= titleErroxBox %>");
	
	//Action préparation des décisions
	$('#preparer_btn_sup').one('click',function () {
		$('[name=idVersionDroit]').val('<%= viewBean.getIdVersionDroit()%>');
		state = true;
		userAction.value=ACTION_DECISION+".preparer";
		document.forms[0].submit();	
	});
	

});
function add() {}
function upd() {}


function cancel() {
	userAction.value=ACTION_DECISION+".chercher";
}  



var multiSelectData = <%= handler.createMotifsWithSousMotifs(viewBean.CS_MOTIF) %>;
var initValue = {"masterValue":"","childValue":""};




</script>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:insert attribute="zoneTitle" --%><ct:FWLabel key="JSP_PC_PREP_DECISION_SUPPR_DE_TITRE"/><%-- /tpl:insert --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:insert attribute="zoneMain" --%>  
						<td> 	
			<table width="90%">
				<tr>
					<td>
						<span id="lblRequerant">
							<ct:FWLabel key="JSP_PC_PREP_DECISION_SUPPR_DE_REQU"/>
						</span>
					</td>
					<td>
						<span id="requerant">
							<%= viewBean.getRequerantInfos(viewBean.getPersonneEtendue(),viewBean.getTiers()) %>
						</span>
					</td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td>
						<span id="lblDroitNo">
							<ct:FWLabel key="JSP_PC_PREP_DECISION_SUPPR_DE_DROITNO"/>
						</span>
					</td>
					<td>
						<span id="droitno">
							<%=viewBean.getIdDroit() %>
						</span>
					</td>
				</tr>
				<tr>
					<td>
						<span id="lblVerdrono">
							<ct:FWLabel key="JSP_PC_PREP_DECISION_SUPPR_DE_VERDROITNO"/>
						</span>	
					</td>
					<td id="tdVerdoNo">
						<span id="verdrono">
							<%= viewBean.getIdVersionDroit() %>
						</span>
					</td>
					
				</tr>
			</table>
			<div id="decisionDuR">
				<span id="lblDecisionDuR">
					<ct:FWLabel key="JSP_PC_PREP_DECISION_SUPPR_DE_DECDU"/>
				</span>
				<span id="valDecisionDuR">
					<input  type="text" data-g-calendar="mandatory:true" name="decisionSuppression.decisionHeader.simpleDecisionHeader.dateDecision" value="<%= viewBean.getDateNow() %>"/>
				</span>
			</div>
			<div id="dateRefus">
				<span id="lblDateRefus">	
					<ct:FWLabel key="JSP_PC_PREP_DECISION_SUPPR_DE_DATEREFUS"/>
				</span>
				<span id="valDateRefus">
					<input type="text" data-g-calendar="mandatory:true,type:month" name="decisionSuppression.simpleDecisionSuppression.dateSuppression" value="<%= viewBean.getDateDernierPaiement() %>"/>
				</span>
			</div>
			
			<!--  <div id="restitution">
				<span class="label" id="lblRestitution"><ct:FWLabel key="JSP_PC_DECSUPP_D_RESTITUTION"/></span>
				<input type="checkbox" id="chkRestitution" name="decisionSuppression.simpleDecisionSuppression.isRestitution" />
			</div><br />-->
			<div id="zoneInfosRefusSupp">
						
				<span id="motifsLabel"><ct:FWLabel key="JSP_PC_PREP_DECISION_REFUS_DE_MOTIF"/>
				</span>
				
				<select id="motifListe" name="decisionSuppression.simpleDecisionSuppression.csMotif" 
					data-g-multiselect=" dataList:multiSelectData,initValues :initValue,subLevelId:sousMotifListe,subLevelLibelle:sous-motifs">
				</select>
				
				<span id="sousMotifsLabel" class="selectSm"><ct:FWLabel key="JSP_PC_PREP_DECISION_REFUS_DE_SOUSMOTIF"/></span>
				<select id="sousMotifListe" class="selectSm" name="decisionSuppression.simpleDecisionSuppression.csSousMotif"></select>
				<div id="champLibreMotif">
						<span id="lblChampLibreMotif">
							<ct:FWLabel key="JSP_PC_PREP_DECISION_SUPPR_DE_CHAMP_LIBRE_MOTIF"/>
						</span>
						<span>
							<textarea  id="valChampLibreMotif" name="decisionSuppression.simpleDecisionSuppression.champLibreMotif" maxlength="255" ><%=viewBean.getDecisionSuppression().getSimpleDecisionSuppression().getChampLibreMotif()%></textarea>
						</span>
					</div>
				<div id="zoneTransfertDossier" data-g-commutator="
						master:$('#motifListe'),
						condition:($('#motifListe').val()=='<%=ch.globaz.pegasus.business.constantes.IPCDecision.CS_MOTIF_SUPPRESSION_TRANSFERT_DOSSIER %>'),
						actionTrue:¦show('#zoneTransfertDossier')¦,
						actionFalse:¦hide('#zoneTransfertDossier')¦
						">
						<table>
								<tr> 
					<td><label id="nouvelleCaisseLabel" for="transfertNouvelleCaisse"><ct:FWLabel key="JSP_PREP_DECISION_SUPPR_D_NOUVELLE_CAISSE"/></label></td>
									<td>
										<ct:widget id='nouvelleCaisse' name='nouvelleCaisse' notation='data-g-string="mandatory:true"' styleClass="libelleLong selecteurHome" defaultValue=''>
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
									
									<td><label id="motifTransfertLabel" for="transfertMotif"><ct:FWLabel key="JSP_PREP_DECISION_SUPPR_D_MOTIF_TRANSFERT"/></label></td>
									<td colspan="3">
										<ct:select name="csMotifTransfert" defaultValue="<%= IPCDecision.CS_MOTIF_TRANSFERT_SUPPRESSION_TRANSFERT_DOMICILE %>" wantBlank="true" notation="data-g-select='mandatory:true'">
											<ct:optionsCodesSystems csFamille="PCMOTRSU"/>
										</ct:select>									
									</td>
									</tr>
									</table>
				</div>
			</div>
			<br/><br/>
					</td>
						<input type="hidden" name="idVersionDroit" value="" />
					<%-- /tpl:put --%>
					</TBODY>
				</TABLE>
				
				<INPUT type="hidden" name="userAction" value="<%=userActionValue%>">
				<INPUT type="hidden" name="_method" value='<%=request.getParameter("_method")%>'>
				<INPUT type="hidden" name="_valid" value='<%=request.getParameter("_valid")%>'>
				<INPUT type="hidden" name="_sl" value="">
			</FORM>
			</TD>
			<TD width="5"><%=(autoShowErrorPopup || !vBeanHasErrors) ? "&nbsp;" : "[ <a id=\"showErrorLink\" href=\"javascript:showErrors();\">visualiser les erreurs</a> ]"%></TD>
		</TR>
		<% if (processLaunched) {%>
		<tr>
			<td colspan="3" style="height: 2em; color: white; font-weight: bold; text-align: center;background-color: green"><ct:FWLabel key="FW_PROCESS_STARTED"/></td>
		</tr>
		<% } 
			if (showProcessButton) { %>
		<tr>
			<td bgcolor="#FFFFFF" colspan="3" align="center">
				<input id="preparer_btn_sup" type="button" value="<%= objSession.getLabel("JSP_PC_PREP_DECISION_SUPPR_DE_TITRE") %>" data-g-preventdoubleclick="label:<%= objSession.getLabel("JSP_PC_PREP_DECISION_CALCUL_DE_PROCESS_MSG") %>,labelCssClass:lbl_process_running" />
			</td>
		</tr>
		<% } %>
		<TR>
			<TD bgcolor="#FFFFFF"></TD>
			<TD bgcolor="#FFFFFF" colspan="2" align="left"><FONT  color="#FF0000">
				<% if (globaz.framework.bean.FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {%>
					<script>
						errorObj.text = "<%=globaz.framework.util.FWTextFormatter.slash(globaz.framework.util.FWTextFormatter.newLineToBr(viewBean.getMessage()), '\"')%>";
						<%
							viewBean.setMessage("");
							viewBean.setMsgType(globaz.framework.bean.FWViewBeanInterface.OK);
						%>
					</script>
				<% } %>
			</FONT></TD>
		</TR>
	</TBODY>
</TABLE>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>