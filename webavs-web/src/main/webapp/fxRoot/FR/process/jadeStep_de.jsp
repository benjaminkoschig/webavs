<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%-- tpl:insert page="/theme/detail_ajax.jtpl" --%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page
	import="ch.globaz.jade.process.business.conf.JadeProcessDisplayValue"%>
<%@page import="java.util.Map"%>
<%@page
	import="ch.globaz.jade.process.business.enumProcess.JadeProcessEntityStateEnum"%>
<%@page
	import="ch.globaz.jade.process.business.enumProcess.JadeProcessStepStateEnum"%>
<%@page import="globaz.fx.vb.process.FXJadeStepViewBean"%>
<%@page import="org.apache.axis.utils.SessionUtils"%>
<%@page language="java" import="globaz.globall.http.*"
	contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
<%@ include file="/theme/detail_ajax/initializeVariables.jspf"%>

<%-- tpl:put name="zoneInit" --%>
<%
	//Les labels de cette page commencent par le préfix "JSP_PC_PRET_TIERS_D"
	idEcran = "??";
	String rootPath = servletContext + (mainServletPath + "Root");
	FXJadeStepViewBean viewBean = (FXJadeStepViewBean) session.getAttribute("viewBean");
	boolean hasRightNew = objSession.hasRight(userActionNew, FWSecureConstants.ADD);
	boolean hasRightUpdate = objSession.hasRight(userActionUpd, FWSecureConstants.UPDATE);
%>

<script type="text/javascript"
	src="<%=servletContext%>/scripts/jadeProcess/cookieForStepEntity.js" />
</script>
<script type="text/javascript"
	src="<%=servletContext%>/scripts/jadeProcess/jadeProcessStep.js" />
</script>

<%-- tpl:put name="zoneScripts" --%>
<style>
#listEntity {
	float: right;
}

.detailEntity th {
	padding: 5px 10px;
}

.detailEntity {

}

#tableDetailEntity {
	margin: 0;
	padding: 0;
}

.detailEntity td {
	padding: 2px 6px;
}
</style>
<script language="JavaScript">
	var S_NAME_PROCESS = "<%=viewBean.getKeyProcess()%>";
	var B_PROCESS_ON_ERROR = <%=viewBean.getProcessStep().getSimpleExecutionProcess().getHasError().toString()%>;
	var S_ACTION = "fx.process.jadeStepAjax" ;
	var f_paramDynamique = function () {
		  var forCsEtat = $(".forCsEtat").val();
		  var forStep = $(".forStep").val();
		  if(!forCsEtat.length){
			  forCsEtat = " ";
		  }
		  if(!forStep.length){
			  forStep = " ";
		  }
		  return ","+forCsEtat+","+forStep;
		}
</script>

<div class="stepInfos">
<%
	if (!viewBean.isByIdProcess()) {
%>

	<div id="stepIdDisplay_<%=viewBean.getProcessStep().getSimpleStep().getIdStep()%>" class="formTableLess">
		<fieldset class="infoProcess ui-widget">
			<legend class="ui-widget-header">Infos processus </legend>
			<div class="content ui-widget-content">
				<label>Nom:</label> <span><%=viewBean.getProcessStep().getSimpleExecutionProcess().getName()%></span>
				<label>Utilisateur:</label> <span><%=viewBean.getProcessStep().getSimpleExecutionProcess().getIdUser()%></span>
				<label>Etat:</label> <span class="csEtatTranslate"><%=viewBean.getProcessStep().getSimpleExecutionProcess().getCsEtat().toLabel()%></span>
				<label>Etape:</label> <span><%=viewBean.getProcessStep().getSimpleStep().getOrdre()%>
					/ <span class="numberStep"><%=viewBean.getJadeProcessDefinitionBean().getSteps().size()%></span></span>
			</div>
		</fieldset>
		<fieldset class="infoProcesstime ui-widget">
			<legend class="ui-widget-header">Infos étape </legend>
			<div class="content ui-widget-content">
				<label>Temps écoulé:</label> <span class="time timeCurrentStep"><%=viewBean.getProcessInfos().getTimeCurrentStep()%></span>
				<label>Temps réstant:</label> <span
					class="time expectedTimeCurrentStep"> <%=viewBean.getProcessInfos().getTimeCurrentStep()%></span>
				<label>Temps moyen(ms):</label> <span class="averageTimeEntite">
					<%=viewBean.getProcessInfos().getAverageTimeEntite()%></span> <label>Nb
					à traiter : </label> <span class="nbEntiteSelected"> <%=viewBean.getProcessInfos().getNbEntiteSelected()%></span>
				<label>Nb restant : </label> <span class="nbEntiteNotTreat"> <%=viewBean.getProcessInfos().getNbEntiteNotTreat()%></span>
				<label>Nb en erreurs:</label> <span class="nbEntiteOnError"> <%=viewBean.getProcessInfos().getNbEntiteOnError()%></span>
				<div
					id="stepPbar_<%=viewBean.getProcessStep().getSimpleStep().getIdStep()%>"
					class="progressStep"
					data-g-progressbar="timer:10000,parametres:<%=viewBean.getIdExecutionProcess()%>"></div>
			</div>
			<div class="infosError"></div>
		</fieldset>
	</div>
	<%
		}
	%>
	<div class="currentStep">
		<div class="area">
			<input type="hidden" class="idStep"
				   value="<%=viewBean.getProcessStep().getSimpleStep().getIdStep()%>">
			<div class="zoneAjaxWithoutBackground zoneAjaxWithoutBorder">
				<%
					if (!JadeProcessStepStateEnum.VALIDATE.equals(viewBean.getProcessStep().getSimpleStep().getCsState())) {
				%>
				<%if (hasRightNew) { %>
				<span class="boutton" style="text-align: left; margin: 4px">
						<span class="executeSelected">Exécuter les entités sélectionnées</span>
						<span class="executeOnError">Exécuter les entités en erreurs</span>
                        <span class="isManualOnError">Mettre en Manuel toutes les entités en erreurs</span>
					</span>
					<% } %>
					<%
						}
					%>
					<ct:ifhasright element="widget.action.jade.download" crud="r">
						<span id="listEntity"> <a
							data-g-download="docType:csv,
										                    parametres:¦<%=viewBean.getIdExecutionProcess()%>¦,
										                    dynParametres:f_paramDynamique,
										                    serviceClassName:ch.globaz.jade.process.business.service.doc.JadeProcessDocumentService,
										                    docName:listOfTheEntity,
										                    serviceMethodName:createListeEntity
										                   ">Liste
								des entités </a>
						</span>
					</ct:ifhasright>
				</div>

				<div style="clear: both" />

				<table class="areaTabel areaDataTable" width="96%">

					<colgroup></colgroup>
					<colgroup style="background-color: #0000FF; width: 70px"></colgroup>
					<colgroup style="width: 130px"></colgroup>
					<colgroup style="width: 70px"></colgroup>
					<colgroup style="width: 70px"></colgroup>
					<colgroup style="width: 80px"></colgroup>
					<colgroup style="width: 80px"></colgroup>
					<%
						if (viewBean.getJadeProcessDefinitionBean().getDisplayValue() != null) {
							for (Map.Entry<String, JadeProcessDisplayValue> entry : viewBean.getJadeProcessDefinitionBean()
									.getDisplayValue().entrySet()) {
					%>
					<%
						if (entry.getValue().isDisplayInHtml()) {
					%>
					<colgroup style="width:<%=entry.getValue().getWidth()%>">
					</colgroup>
					<%
						}
					%>
					<%
						}
					}
				%>
				<thead>
				<tr class="ui-widget-header search">
					<td class="ui-widget-header"><input type="text" value=""
														class="forRecherche" /></td>
					<td class="ui-widget-header"><input type="text"
														value="<%=(viewBean.isByIdProcess()) ? "" : viewBean.getProcessStep().getSimpleStep().getOrdre()%>"
														class="forStep" /></td>
					<td class="ui-widget-header"><ct:select
							styleClass="forCsEtat" name="forCsEtat"
							notation="data-g-selectautocomplete='addIcon:false'"
							wantBlank="true">
						<%
							for (JadeProcessEntityStateEnum e : JadeProcessEntityStateEnum.values()) {
						%>
						<ct:option label="<%=e.toLabel()%>" value="<%=e.toString()%>" />
						<%
							}
						%>
					</ct:select></td>
					<td class="ui-widget-header" colspan="3"></td>
					<td class="ui-widget-header"><input type="text"
														class="forId" /></td>
					<%
						if (viewBean.getJadeProcessDefinitionBean().getDisplayValue() != null) {
							for (Map.Entry<String, JadeProcessDisplayValue> entry : viewBean.getJadeProcessDefinitionBean()
									.getDisplayValue().entrySet()) {
					%>
					<%
						if (entry.getValue().isDisplayInHtml()) {
							String k = "for" + entry.getValue().getKeyValue().substring(0, 1).toUpperCase()
									+ entry.getValue().getKeyValue().substring(1).toLowerCase() + "Recherche";
					%>

					<td class="ui-widget-header">
						<%
							if (entry.getValue().isSearch()) {
						%> <input type="text"
								  class="dynamicSearch <%=k%>"></input> <%
						}
					%>
					</td>
					<%
						}
					%>
					<%
							}
						}
					%>
					<%
						if (viewBean.getIsCurrentSetInjectable()) {
					%>
					<td class="inject"></td>
					<%
						}
					%>
				</tr>
				<tr>
					<th search="recherche">Entités</th>
					<th>Etapes</th>
					<th>Statuts</th>
					<th class="notSortable">Manuel
						<%if(hasRightUpdate) {%>
						<input type="checkbox" data-g-mastercheckbox=" " />
						<% } %>
					</th>
					<th class="notSortable">Réexécution
						<%if(hasRightUpdate) {%>
						<input type="checkbox" data-g-mastercheckbox=" " />
						<%} %>
					</th>
					<th data-orderKey='time'>Temps(ms)</th>
					<th>N°</th>

					<%
						if (viewBean.getJadeProcessDefinitionBean().getDisplayValue() != null) {
							for (Map.Entry<String, JadeProcessDisplayValue> entry : viewBean.getJadeProcessDefinitionBean()
									.getDisplayValue().entrySet()) {
					%>
					<%
						if (entry.getValue().isDisplayInHtml()) {
					%>
					<th><%=entry.getValue().getLibelle()%></th>
					<%
						}
					%>
					<%
							}
						}
					%>
					<%
						if (viewBean.getIsCurrentSetInjectable()) {
					%>
					<th class="inject">Réintégré</th>
					<%
						}
					%>
				</tr>
				</thead>
				<tbody></tbody>
			</table>
			<div class="areaDetail">
				<div class="getWithUserAction">
					<a href="">Voir dans l'application</a>
				</div>
				<span id="logInfos"></span> <span id="properties"></span>
			</div>
		</div>
	</div>
</div>