
<%@page import="globaz.pegasus.vb.decision.PCPrepDecisionApresCalculViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%@ include file="/theme/process/header.jspf" %>
<%@page import="globaz.pegasus.utils.BusinessExceptionHandler"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDecision"%>
<%@page import="ch.globaz.pyxis.business.model.TiersSimpleModel"%>
<%@page import="ch.globaz.pyxis.business.model.PersonneSimpleModel"%>
<%@page import="globaz.pegasus.utils.PCGestionnaireHelper"%>
<%@page import="globaz.pyxis.db.adressecourrier.TIPays"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.prestation.interfaces.tiers.PRTiersHelper"%>
<%@page import="globaz.globall.parameters.FWParametersCodeManager" %>
<%@page import="globaz.globall.vb.BJadePersistentObjectViewBean"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="java.util.ArrayList" %>
<%@page import="globaz.jade.admin.user.service.JadeUserService"%>
<%@page import="globaz.jade.admin.JadeAdminServiceLocatorProvider"%>
<%--  ********************************** Paramétrage global de la page ******************************* --%>
<%
globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();

PCPrepDecisionApresCalculViewBean viewBean = (PCPrepDecisionApresCalculViewBean) session.getAttribute("viewBean"); 
//LES PREFIXES DE CETTE PAGES COMMENCENT PAR JSP_PC_PREP_DECISION_CALCUL_DE
selectedIdValue=viewBean.getId();
boolean viewBeanIsNew="add".equals(request.getParameter("_method"));
//bButtonDelete = false;
idEcran="PPC0084";
autoShowErrorPopup = false;
String titleErroxBox = objSession.getLabel("JSP_GLOBAL_ERROR_BOX_TITLE");//titre box erreur

%>

<%-- ********************************** Zone scripts et css ********************************** --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%@ include file="/pegasusRoot/ajax/javascriptsAndCSS.jspf" %>
<%-- ********************************** HACK CACHER LE LIEN AFFICHER LES ERREURS ********************** --%>
<% 
vBeanHasErrors = false;
%>
<!--  notation spécifique pc -->
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath+"Root")%>/scripts/notationsCandidate/globazPreventDoubleClick.js"></script>
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/decision/preparation.css"/>

<script type="text/javascript">

//Action préparation des décisions


var actionMethod;
var userAction;


globazGlobal.action = {
		ACTION_DECISION_PREP : "<%= IPCActions.ACTION_DECISION_AC_PREPARATION %>"
}
globazGlobal.labels = {
		PROCESS_STARTING_LABEL : "<%= objSession.getLabel("JSP_PC_PREP_DECISION_CALCUL_DE_PROCESS_MSG") %>",
		TITLE_ERROR_BOX : "<%= objSession.getLabel("JSP_GLOBAL_ERROR_BOX_TITLE") %>",
		DECISION_ERROR_MSG : <%= request.getParameter("decisionErrorMsg") %>
}
globazGlobal.cs = {
		CS_PREPARATION_COURANTE : "<%=IPCDecision.CS_PREP_COURANT%>",
		CS_PREPARATION_STANDARD : "<%=IPCDecision.CS_PREP_STANDARD%>",
		CS_PREPARATION_RETRO : "<%=IPCDecision.CS_PREP_RETRO%>"
}
globazGlobal.$element = {
		LISTE_TYPE_PREPARATION : '',
		BOUTON_OK_PROCESS: '',
		ID_VERSION_DROIT_HIDDEN_FIELD : ''
}
globazGlobal.isAmalIncoherent = <%= viewBean.getIsAmalIncoherent()%>;
globazGlobal.idVersionDroit = <%= viewBean.getIdVersionDroit()%>;
globazGlobal.isDecisionComplete = <%= viewBean.isDecisionComplete() %>;
globazGlobal.isCalculRetro = <%= !viewBean.getIsCalculRetro()%>;

/*
 * Fonction qui désactive l'affichage de l'option 
 *	RETRO de la liste des genre de préparation
 * Le parametre passé est un booleen true pour afficher l'option, false pour la cacher
 */
function dealTypeOfDecisionList(currentDecisionState){
	if(!currentDecisionState){
		globazGlobal.$element.LISTE_TYPE_PREPARATION.find("option").each(function(){
			if($(this).val() === globazGlobal.cs.CS_PREPARATION_COURANTE
					||$(this).val()===globazGlobal.cs.CS_PREPARATION_STANDARD){
				$(this).remove();
			}
		});
	}else{
		globazGlobal.$element.LISTE_TYPE_PREPARATION.find("option").each(function(){
			if($(this).val()===globazGlobal.cs.CS_PREPARATION_RETRO){
				$(this).remove();
			}
		});
	}
}

$(function(){
	//initialisation variables et valeurs
	globazGlobal.$element.LISTE_TYPE_PREPARATION = $("[name=listeGenrePrep]");
	globazGlobal.$element.BOUTON_OK_PROCESS = $('#btnOk');
	globazGlobal.$element.ID_VERSION_DROIT_HIDDEN_FIELD = $('[name=idVersionDroit]');
	
	actionMethod=$('[name=_method]',document.forms[0]).val();
	userAction=$('[name=userAction]',document.forms[0])[0];
	//champ caché idVersionDroit
	globazGlobal.$element.ID_VERSION_DROIT_HIDDEN_FIELD.val(''+globazGlobal.idVersionDroit);
	
	//on désactive le bouton ok standard venant du FW
	globazGlobal.$element.BOUTON_OK_PROCESS.remove();
	
	//Action préparation des décisions
	$('#preparer_btn').one('click',function () {
		state = true;
		userAction.value=globazGlobal.action.ACTION_DECISION_PREP;
		document.forms[0].submit();	
	});
	
	//gestion erreurs
	pegasusErrorsUtils.dealErrors(globazGlobal.labels.DECISION_ERROR_MSG,globazGlobal.labels.TITLE_ERROR_BOX);
	
	//traitement de l'option retro de la liste
	if(globazGlobal.isCalculRetro){
		globazGlobal.$element.LISTE_TYPE_PREPARATION.find("option").each(function(){
			if($(this).val() === globazGlobal.cs.CS_PREPARATION_RETRO
					||$(this).val() === globazGlobal.cs.CS_PREPARATION_COURANTE){
				$(this).remove();
			}
		});
	}else{
		dealTypeOfDecisionList(globazGlobal.isDecisionComplete);
	}
	
	//Gestion bouton incoherence amal
	if(globazGlobal.isAmalIncoherent){
		globazGlobal.$element.BOUTON_OK_PROCESS.hide();
	}
	
	
});
</script>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:insert attribute="zoneTitle" --%><ct:FWLabel key="JSP_PC_PREP_DECISION_CALCUL_DE_TITRE"/><%-- /tpl:insert --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:insert attribute="zoneMain" --%>  
						<td> 	
			<table width="90%">
				<tr>
					<td>
						<span id="lblRequerant">
							<ct:FWLabel key="JSP_PC_PREP_DECISION_CALCUL_DE_REQU"/>
						</span>
					</td>
					<td colspan="3">
						<span id="requerant">
							<%= viewBean.getRequerantInfos(viewBean.getPersonneEtendue(), viewBean.getTiers()) %>
						</span>
					</td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td>
						<span id="lblDroitNo">
							<ct:FWLabel key="JSP_PC_PREP_DECISION_CALCUL_DE_DROITNO"/>
						</span>
					</td>
					<td>
						<span id="droitno"><%= viewBean.getIdDroit() %></span>
					</td>
				</tr>
				<tr>
					<td>
						<span id="lblVerdrono">
							<ct:FWLabel key="JSP_PC_PREP_DECISION_CALCUL_DE_VERDROITNO"/>
						</span>
					</td>
					<td id="tdVerdroNo">
						<span id="verdrono"><%= viewBean.getNoVersion() %></span>
					</td>
				</tr>
			</table>
			
			<div id="decisionDu">
				<span id="lblDecisionDu">
					<ct:FWLabel key="JSP_PC_PREP_DECISION_CALCUL_DE_DECDU"/>
				</span>
				<span id="valDecisionDu">
					<input type="text" name="decisionApresCalcul.decisionHeader.simpleDecisionHeader.dateDecision" class="clearable" value="<%= viewBean.getDateNow() %>" data-g-calendar="mandatory:true"/>	
				</span> 
			</div>
		<%--
			<div id="genre">
				<span id="lblGenre">
					<ct:FWLabel key="JSP_PC_PREP_DECISION_CALCUL_DE_GENRE"/>
				</span>
				<span>
					<ct:FWCodeSelectTag name="listeGenrePrep" codeType="PCGENPRE" defaut="" />
				</span>
			</div>  --%>
			
			<!-- message amal incoherence -->
			<%= viewBean.getAmalWarning() %>
			
			
				
			
		</td>
				
		
		<input type="hidden" name="idVersionDroit" value="" />
		
					
<%-- /tpl:put --%>
					</TBODY>
				</TABLE>
				<INPUT type="hidden" name="selectedId" value="<%=selectedIdValue%>">
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
				<input id="preparer_btn" type="button" value="<%= objSession.getLabel("JSP_PC_PREP_DECISION_CALCUL_DE_BOUTON") %>" data-g-preventdoubleclick="label:<%= objSession.getLabel("JSP_PC_PREP_DECISION_CALCUL_DE_PROCESS_MSG") %>,labelCssClass:lbl_process_running" />
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