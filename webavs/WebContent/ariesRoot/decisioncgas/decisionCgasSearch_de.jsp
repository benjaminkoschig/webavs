<%@page import="ch.globaz.naos.business.service.AffiliationService"%>
<%@page import="globaz.globall.db.BSessionUtil"%>
<%@page import="globaz.aries.vb.decisioncgas.ARDecisionCgasSearchViewBean"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page language="java" contentType="text/html;charset=ISO-8859-1"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>

<%@include file="/theme/detail_ajax/header.jspf"%>

<%-- tpl:insert attribute="zoneInit" --%>
<%
ARDecisionCgasSearchViewBean viewBean = (ARDecisionCgasSearchViewBean) request.getAttribute(FWServlet.VIEWBEAN);
%>
<%-- /tpl:insert --%>

<%@include file="/theme/detail_ajax/javascripts.jspf"%>

<%-- tpl:insert attribute="zoneScripts" --%>

<ct:menuChange displayId="menu" menuId="AFMenuPrincipal" showTab="menu"/>

<script type="text/javascript" src="<%=servletContext%>/scripts/calendar/AnchorPosition.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/calendar/PopupWindow.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/menuPopup.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/ajax/DefaultTableAjax.js"></script>
<script type="text/javascript" src="<%=servletContext%>/ariesRoot/scripts/decisionCgasSearch_part.js"></script>

<style type="text/css">
	.montant{
    	width : 100px;
	}
</style>
<%-- /tpl:insert --%>

<%@include file="/theme/detail_ajax/bodyStart.jspf"%>

<%-- tpl:insert attribute="zoneTitle" --%>
<ct:FWLabel key="JSP_AR_DECISION_SEARCH_TITRE" />
<%-- /tpl:insert --%>

<%@include file="/theme/detail_ajax/bodyStart2.jspf"%>

<%-- tpl:insert attribute="zoneMain" --%>

<tr>
	<td>
		
		<table width="100%">
						
			<tr>
				<td width="100px">
					<label for="forNumAffilie"><ct:FWLabel key="JSP_AR_DECISION_SEARCH_AFFILIE" /></label>
				</td>
				<td>
					<ct:widget tabindex="3" name="forNumAffilie" id="forNumAffilie" styleClass="normal" >
						<ct:widgetService methodName="widgetFind" className="<%=AffiliationService.class.getName()%>" defaultSearchSize="20">
							<ct:widgetCriteria criteria="likeNumeroAffilie" label="CRITERIA_NUM_AFFILIE"/>
							<ct:widgetCriteria criteria="inTypeAffiliationString" label="CRITERIA_NUM_AFFILIE" fixedValue="<%=viewBean.getTypesAffForWidgetString()%>"/>
							<ct:widgetLineFormatter format="#{affiliation.affilieNumero} #{affiliation.raisonSocialeCourt} "/>
							<ct:widgetJSReturnFunction>
								<script type="text/javascript">
									function(element){
										this.value=$(element).attr('affiliation.affilieNumero');
										$('#nomAffilie').val($(element).attr('affiliation.raisonSocialeCourt'));
										$('#idAffilie').val($(element).attr('affiliation.affiliationId'));
										globazGlobal.zoneDecisionCgasAjax.ajaxFind();
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
			<tr class="hiddenIfNoAffilie" >
				<!-- 
					Le client ne désire pas avoir la possibilité d'afficher les décisions supprimées
					Afin de ne pas devoir redévelopper cette fonction s'il change d'avis, la case à cocher a simplement été masquée  
				-->
				
				<td class="alwaysHidden"><label for="afficherDecisionsSupprimmes"><ct:FWLabel key="JSP_AR_DECISION_SEARCH_AFFICHER_DECISION_SUPPRIMEE" /></label> &nbsp; &nbsp; <input id="afficherDecisionsSupprimmes" type="checkbox" /></td>
				<ct:ifhasright element="<%=partialUserActionAction%>" crud="c">
					<td colspan="2" align="right" ><input type="button" id="boutonNouvelDecisionCGAS"  onclick="window.top.fr_main.window.location='aries?userAction=aries.decisioncgas.decisionCgas.afficher&amp;idAffiliation=' + $('#idAffilie').val()" value="<%=BSessionUtil.getSessionFromThreadContext().getLabel("JSP_AR_DECISION_SEARCH_BOUTON_CREER_DECISION")%>"></td>
				</ct:ifhasright>
			</tr>
		</table>
					
		<div class="area">
			<table width="100%" class="areaTable">
				<thead>
					<tr>
						<th width="60px"></th>
						<th><ct:FWLabel key="JSP_AR_DECISION_SEARCH_ANNEE" /></th>
						<th><ct:FWLabel key="JSP_AR_DECISION_SEARCH_TYPE" /></th>
						<th><ct:FWLabel key="JSP_AR_DECISION_SEARCH_PERIODE" /></th>
						<th><ct:FWLabel key="JSP_AR_DECISION_SEARCH_DATE_DECISION" /></th>
						<th><ct:FWLabel key="JSP_AR_DECISION_SEARCH_COTISATION_PERIODE" /></th>
						<th><ct:FWLabel key="JSP_AR_DECISION_SEARCH_ETAT" /></th>
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