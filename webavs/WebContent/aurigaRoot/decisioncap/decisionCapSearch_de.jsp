<%@page import="globaz.globall.db.BSessionUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="globaz.auriga.vb.decisioncap.AUDecisionCapSearchViewBean"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="ch.globaz.naos.business.service.AffiliationService"%>
<%@page language="java" contentType="text/html;charset=ISO-8859-1"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>

<%@include file="/theme/detail_ajax/header.jspf"%>

<%-- tpl:insert attribute="zoneInit" --%>
<%
AUDecisionCapSearchViewBean viewBean = (AUDecisionCapSearchViewBean) request.getAttribute(FWServlet.VIEWBEAN);
%>
<%-- /tpl:insert --%>

<%@include file="/theme/detail_ajax/javascripts.jspf"%>

<%-- tpl:insert attribute="zoneScripts" --%>

<ct:menuChange displayId="menu" menuId="AFMenuPrincipal" showTab="menu"/>

<script type="text/javascript" src="<%=servletContext%>/scripts/calendar/AnchorPosition.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/calendar/PopupWindow.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/menuPopup.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/ajax/DefaultTableAjax.js"></script>
<script type="text/javascript" src="<%=servletContext%>/aurigaRoot/scripts/decisionCapSearch_part.js"></script>

<style type="text/css">
	.montant{
    	width : 100px;
	}
</style>

<script type="text/javascript">
	$(document).ready(function() {
// 	  	$('#forNumAffilie').keyup(function(){
// 	  		var idAffilieVal = $("#idAffilie").val();
// 		  	if($(this).length!=0 && idAffilieVal.length!=0){
// 		  		alert("afficher bouton");
// 		  	}else{
// 		  		alert("cacher bouton");
// 		  	}
// 		});
	});
</script>

<%-- /tpl:insert --%>

<%@include file="/theme/detail_ajax/bodyStart.jspf"%>

<%-- tpl:insert attribute="zoneTitle" --%>
<ct:FWLabel key="JSP_DECISION_CAP_SEARCH_TITRE" />
<%-- /tpl:insert --%>

<%@include file="/theme/detail_ajax/bodyStart2.jspf"%>

<%-- tpl:insert attribute="zoneMain" --%>

<tr>
	<td>
		<table width="100%">
				<tr>
					<td width="100px">
 						<label for="forNumAffilie"><ct:FWLabel key="JSP_DECISION_CAP_SEARCH_AFFILIE" /></label>
	 				</td>
					<td>
						<ct:widget tabindex="3" name="forNumAffilie" id="forNumAffilie" styleClass="normal" >
						<ct:widgetService methodName="widgetFind" className="<%=AffiliationService.class.getName()%>" defaultSearchSize="20">
							<ct:widgetCriteria criteria="likeNumeroAffilie" label="JSP_DECISION_CAP_SEARCH_W_NUM"/>
							<ct:widgetCriteria criteria="inTypeAffiliationString" label="JSP_DECISION_CAP_SEARCH_W_NUM" fixedValue="<%=viewBean.getListTypeAffForWidgetString()%>"/>
							<ct:widgetLineFormatter format="#{affiliation.affilieNumero} #{affiliation.raisonSocialeCourt} "/>
							<ct:widgetJSReturnFunction>
								<script type="text/javascript">
									function(element){
										this.value=$(element).attr('affiliation.affilieNumero');
										$('#nomAffilie').val($(element).attr('affiliation.raisonSocialeCourt'));
										$('#idAffilie').val($(element).attr('affiliation.affiliationId'));
										decisionCapSearch.ajaxFind();
										manageDisplay();
									}
								</script>										
							</ct:widgetJSReturnFunction>
						</ct:widgetService>			
						</ct:widget>
					</td>
				</tr>
				
				<tr>
					<td>&nbsp;</td>
					<td><ct:inputText name="nomAffilie" id="nomAffilie" style="width: 350px;" readonly="readonly" disabled="disabled"/></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td><ct:inputHidden name="idAffilie" id="idAffilie"/></td>
				</tr>
								
				<tr>
					<td colspan="3">&nbsp;</td>
				</tr>
		</table>
		<table width="100%" >
			<tr class="hiddenIfNoAffilie">
				<!-- 
					Le client ne désire pas avoir la possibilité d'afficher les décisions supprimées
					Afin de ne pas devoir redévelopper cette fonction s'il change d'avis, la case à cocher a simplement été masquée  
				-->
				<td class="alwaysHidden"><label for="afficherDecisionsSupprimmes"><ct:FWLabel key="JSP_DECISION_CAP_SEARCH_AFFICHER_DECISION_SUPPRIMEE" /></label> &nbsp; &nbsp; <input id="afficherDecisionsSupprimmes" type="checkbox" /></td>
				
				<ct:ifhasright element="<%=partialUserActionAction%>" crud="c">
					<td colspan="2" align="right"><input type="button" id="btCreateDecision"  onclick="window.top.fr_main.window.location='auriga?userAction=auriga.decisioncap.decisionCap.afficher&idAffilie='+ $('#idAffilie').val()" value="<%=BSessionUtil.getSessionFromThreadContext().getLabel("JSP_DECISION_CAP_BT_NOUVEAU")%>"></td>
				</ct:ifhasright>
			</tr>
		</table>
				
		<div class="area">
			<table width="100%" class="areaTable">
				<thead>
					<tr>
						<th width="60px"></th>
						<th><ct:FWLabel key="JSP_DECISION_CAP_RESULT_ANNEE" /></th>
						<th><ct:FWLabel key="JSP_DECISION_CAP_RESULT_TYPE" /></th>
						<th><ct:FWLabel key="JSP_DECISION_CAP_RESULT_PERIODE" /></th>
						<th><ct:FWLabel key="JSP_DECISION_CAP_RESULT_DATE_DONNEE" /></th>
						<th><ct:FWLabel key="JSP_DECISION_CAP_RESULT_COTISATION_BRUTE" /></th>
						<th><ct:FWLabel key="JSP_DECISION_CAP_RESULT_PRESTATION" /></th>
						<th><ct:FWLabel key="JSP_DECISION_CAP_RESULT_COTISATION" /></th>
						<th><ct:FWLabel key="JSP_DECISION_CAP_RESULT_ETAT" /></th>
					</tr>
				</thead>
				<tbody />
			</table>
			<div class="areaDetail" />
		</div>
	</td>
</tr>
<%-- /tpl:insert --%>


<%@include file="/theme/detail_ajax/bodyButtons.jspf"%>
<%@include file="/theme/detail_ajax/bodyErrors.jspf"%>
<%@include file="/theme/detail_ajax/footer.jspf"%>