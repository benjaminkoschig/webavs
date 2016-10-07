<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDecision"%>
<%@page import="ch.globaz.pegasus.business.models.decision.DecisionApresCalcul"%>
<%@page import="globaz.jade.persistence.model.JadeAbstractModel"%>
<%@page import="globaz.pegasus.vb.decision.PCValidationDecisionsViewBean"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>

<%-- tpl:insert attribute="zoneInit" --%>
 
<%// Les labels de cette page commence par la préfix ""
	idEcran="PPC0087";
    PCValidationDecisionsViewBean viewBean = (PCValidationDecisionsViewBean) session.getAttribute("viewBean");
    viewBean.setPath(servletContext+mainServletPath);
	//boolean viewBeanIsValid = "fail".equals(request.getParameter("_valid"));
	boolean viewBeanIsNew= false;
	bButtonCancel = false;
	bButtonValidate = true;
	bButtonDelete = false;
	autoShowErrorPopup = false;
	String titleErroxBox = objSession.getLabel("JSP_GLOBAL_ERROR_BOX_TITLE");//titre box erreur
	%>
<%@ include file="/theme/detail/javascripts.jspf" %>


<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored ="false" %>
<% String rootPath = servletContext+(mainServletPath+"Root");%>
<link rel="stylesheet" type="text/css" href="<%=servletContext%>/theme/widget.css"/>
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/dataTableStyle.css"/>
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/saisieStyle.css"/>
<script type="text/javascript" src="<%=servletContext%>/scripts/widget/globazwidget.js"></script>
<script type="text/javascript" src="<%=rootPath%>/scripts/pegasusErrorsUtil.js"></script>  
<%-- ********************************** HACK CACHER LE LIEN AFFICHER LES ERREURS ********************** --%>
<% 
vBeanHasErrors = false;
%>

<script type="text/javascript" src="<%=rootPath %>/scripts/decision/validationDecision.js"></script>
<!--  notation spécifique pc -->
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath+"Root")%>/scripts/notationsCandidate/globazPreventDoubleClick.js"></script>
<link rel="stylesheet" type="text/css" media="screen" href="<%=rootPath%>/css/decision/validationDecision.css">

<%-- tpl:insert attribute="zoneScripts" --%>
<script type="text/javascript">
  var messageDelete = "<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>";
  var messageButtonValidContinue = "<ct:FWLabel key='JSP_PC_PARAM_CONVERSION_RENTE_D_VALIDCONTINUE'/>" ;
  var userAction = "<%= IPCActions.ACTION_DECISION_VALIDER_TOUT%>.valider";
  var labelProcessStarting = '<%= objSession.getLabel("JSP_PC_VALIDATION_DECISIONS_D_PROCESS_RUN")%>';
  var b_ = null;
  var isAmalIncoherent = <%=viewBean.getIsAmalIncoherent()%>;
  var montantDecision = <%= viewBean.getMontant().floatValue()%>;
  function add() {}
  function upd() {}
  function validate() {}
  function isAdd(){}
  function cancel() { }
  function del() {}	
  function init(){}
  
  $(function () {
	 
	  if(isAmalIncoherent){
		  $('#validerTous').hide();
	  }
	//gestion erreurs
	pegasusErrorsUtils.dealErrors(<%= request.getParameter("decisionErrorMsg") %>,"<%= titleErroxBox %>");
  })
</script>

<%-- /tpl:insert --%>
<!-- <script type="text/javascript" src="<%=rootPath %>/scripts/parametre/zoneLocalite_js.js"></script> -->
<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:insert attribute="zoneTitle" --%>
<ct:FWLabel key='JSP_PC_VALIDATION_DECISIONS_D_TITRE'/>

<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<tr>
	<td><ct:FWLabel key="JSP_PC_CREANCIE_ACCORDEE_D_REQUERANT" /> </td>
	<td> <%=viewBean.getRequerant()%></td>
</tr>
<tr>
	<td><ct:FWLabel key="JSP_PC_VALIDATION_DECISIONS_D_ID_DROIT_NO_VERSION" /> </td>
	<td><%=viewBean.getIdDroit()%> / <%=viewBean.getNoVersion()%></td>
</tr>
<tr>	
	<td colspan="3">
	 	<input type="hidden" name="idDroit" value="<%= viewBean.getIdDroit() %>">
	 	<input type="hidden" name="idVersionDroit" value="<%= viewBean.getIdVersionDroit() %>">
	 	<input type="hidden" name="noVersion" value="<%= viewBean.getNoVersion() %>">
	 	
		<br/>
		<table id="decision" >
			<thead>
				<tr>
					<th><ct:FWLabel key="JSP_PC_VALIDATION_DECISIONS_D_NO_DECISION" /></th>
					<th><ct:FWLabel key="JSP_PC_VALIDATION_DECISIONS_D_DATE_DECSISION" /></th>
					<th><ct:FWLabel key="JSP_PC_VALIDATION_DECISIONS_D_BENEFICIAIRE" /></th> 
					<th><ct:FWLabel key="JSP_PC_VALIDATION_DECISIONS_D_ETAT" /></th>
				    <th><ct:FWLabel key="JSP_PC_VALIDATION_DECISIONS_D_TYPE" /></th>
					<th><ct:FWLabel key="JSP_PC_VALIDATION_DECISIONS_D_A_CORRIGER" /></th>
					<th><ct:FWLabel key="JSP_PC_VALIDATION_DECISIONS_D_DECISION" /></th>
				<tr>
			</thead>
			<tbody>
			<%for(JadeAbstractModel model:viewBean.getListDecision()){
				
				DecisionApresCalcul decision = (DecisionApresCalcul)model; 
				/*idDemandePc="+decision.getVersionDroit().getSimpleDroit().getIdDemandePC()+*/
				String param =  "&idDecision="+decision.getDecisionHeader().getSimpleDecisionHeader().getIdDecisionHeader()+
				                "&idDroit="+decision.getVersionDroit().getSimpleDroit().getId()+
				                "&idVersionDroit="+decision.getVersionDroit().getSimpleVersionDroit().getId()+
				                "&noVersion="+decision.getVersionDroit().getSimpleVersionDroit().getNoVersion();
			%>   
			<tr>
			   <td><%=decision.getDecisionHeader().getSimpleDecisionHeader().getNoDecision()%></td>
			   <td><%=decision.getDecisionHeader().getSimpleDecisionHeader().getDateDecision()%></td>
			   <td><%=decision.getDecisionHeader().getPersonneEtendue().getPersonneEtendue().getNumAvsActuel()+" "+decision.getDecisionHeader().getPersonneEtendue().getTiers().getDesignation1()+" "+decision.getDecisionHeader().getPersonneEtendue().getTiers().getDesignation2() %></td>
			   <td><%=objSession.getCodeLibelle(decision.getDecisionHeader().getSimpleDecisionHeader().getCsEtatDecision())%></td>
			   <td><%=objSession.getCodeLibelle(decision.getDecisionHeader().getSimpleDecisionHeader().getCsTypeDecision())%></td>
			   <td><%=viewBean.controleDecision(decision) %></td>  
			   <td><a data-g-externallink="selectorForClose:¦#btnPreValid,#btnCan,#btnVal¦" href ="<%=viewBean.getPath()%>?userAction=<%=IPCActions.ACTION_DECISION_DETAIL_AP_CALCUL%>.afficher&<%=param%>"><ct:FWLabel key="JSP_PC_VALIDATION_DECISIONS_D_DECISION" /></a></td>  
			</tr>
		<% } %>
			</tbody>
		</table>
		</br></br>
		
		<!--  messages spécifiques au décisions basées sur un calcul retro -->
		<c:if test="${viewBean.isDecisionBaseSurCalculRetro}">
		
			<c:choose>
				<c:when test="${viewBean.isMontantDispoPositif}">
					<div class="infoWarnRetro">
						<b><ct:FWLabel key="JSP_PC_DECALCUL_D_SOLDE_FAVEUR_BENEFICIARE"/></b>
						<span data-g-amountformatter=" "><c:out value="${viewBean.montantDispo}"/></span> CHF
					</div>
				</c:when>
				
				<c:otherwise>
					<div data-g-boxmessage="type:WARN" class="infoWarnRetro">
						<ct:FWLabel key="JSP_PC_DECALCUL_D_INFO_COMPTABILISATIONS"/></br>
						<b><ct:FWLabel key="JSP_PC_DECALCUL_D_SOLDE_FAVEUR"/></b>
						<span data-g-amountformatter=" "><c:out value="${viewBean.montantDispo}"/></span> CHF
					</div>
				</c:otherwise>
			</c:choose>
		
		</c:if>
		
		
		<div id="dialog-confirm-creation-lot" title="<%= objSession.getLabel("JSP_PC_DECALCUL_D_CONFIRMATION_COMPTA_AUTO_TITRE")%>">
    		<p><span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span><%= objSession.getLabel("JSP_PC_DECALCUL_D_CONFIRMATION_COMPTA_AUTO_DES")%></p>
<!--     		<p> -->
<%-- 							<%= objSession.getLabel("JSP_VALID_LOT_D_EMAIL")%> --%>
<%-- 							<INPUT type="text" name="mailProcessCompta" value="<%=viewBean.getMailProcessCompta()%>" class="libelleLong"> --%>
<!-- 			</p> -->
		</div>
			
		<input type="hidden" name="isComptabilisationAuto" id="isComptabilisationAuto" value="<%=viewBean.isComptabilisationAuto()%>"/>
	</td>
</tr>
<tr>
	<td colspan="3" align="center" id="validerTout">
	  <%if(viewBean.checkValiderTout() == null) {
		%><%=viewBean.getAmalWarning()%>  <%
	  %>
	 	 <input type="button" id="validerTous" name="validerTous" value="<%=objSession.getLabel("JSP_PC_VALIDATION_DECISIONS_D_VALIDER_TOUT")%>" data-g-preventdoubleclick="label:labelProcessStarting,labelCssClass:processRunningLabel"/>

	 	
	  <%}else{ %>
	  	<strong class="errorText"><%= viewBean.checkValiderTout()%></strong>
	  <% }%>
	</td>
</tr>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%-- tpl:insert attribute="zoneButtons" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>
