<%@page language="java" contentType="text/html;charset=ISO-8859-1"%>
<%@page import="globaz.auriga.vb.sortiecap.AUSortieCapViewBean"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>

<%@include file="/theme/detail_ajax/header.jspf"%>

<%-- tpl:insert attribute="zoneInit" --%>
<%
AUSortieCapViewBean viewBean = (AUSortieCapViewBean) request.getAttribute(FWServlet.VIEWBEAN);
%>
<%-- /tpl:insert --%>

<%@include file="/theme/detail_ajax/javascripts.jspf"%>

<%-- tpl:insert attribute="zoneScripts" --%>

<ct:menuChange displayId="menu" menuId="AFMenuPrincipal" showTab="menu"/>

<script type="text/javascript" src="<%=servletContext%>/scripts/ajax/DefaultTableAjax.js"></script>
<script type="text/javascript" src="<%=servletContext%>/aurigaRoot/scripts/sortieCap_part.js"></script>
<script type="text/javascript">
</script>
<%-- /tpl:insert --%>

<%@include file="/theme/detail_ajax/bodyStart.jspf"%>

<%-- tpl:insert attribute="zoneTitle" --%>
<ct:FWLabel key="JSP_SORTIE_CAP_TITRE" />
<%-- /tpl:insert --%>

<%@include file="/theme/detail_ajax/bodyStart2.jspf"%>

<%-- tpl:insert attribute="zoneMain" --%>
<tr>
	<td>
		<div class="area">
			<table width="100%" class="areaTable">
				<thead>
					<tr id="searchZone">
						<td><input id="searchModel.likeNumeroAffilie"/></td>
						<td></td>
						<td><input id="searchModel.forIdPassageFacturation"/></td>
						<td><ct:FWCodeSelectTag name="searchModel.forEtat" defaut="" codeType="AUETATSORT" wantBlank="true" /></td>
					</tr>
					<tr>
						<th><ct:FWLabel key="JSP_SORTIE_CAP_AFFILIE" /></th>
						<th><ct:FWLabel key="JSP_SORTIE_CAP_EXTOURNE" /></th>
						<th><ct:FWLabel key="JSP_SORTIE_CAP_PASSAGE" /></th>
						<th><ct:FWLabel key="JSP_SORTIE_CAP_ETAT" /></th>
					</tr>
				</thead>
				<tbody />
			</table>
			<table class="areaDetail">
				<tr>
					<td colspan="2">
						<b><ct:FWLabel key="JSP_SORTIE_CAP_TITRE_DETAIL" /></b>
					</td>
				<tr>
				<tr>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td width="200px">
						<ct:FWLabel key="JSP_SORTIE_CAP_AFFILIE" />
					</td>
					<td>
						<ct:inputText name="complexSortieCap.affiliation.affilieNumero" id="complexSortieCap.affiliation.affilieNumero" />
						<ct:inputText name="complexSortieCap.affiliation.raisonSocialeCourt" id="complexSortieCap.affiliation.raisonSocialeCourt" style="width: 350px;" />
						<ct:inputText name="typeAffiliationLibelle" id="typeAffiliationLibelle" />
					</td>
				</tr>
				<tr>
					<td>
						<ct:FWLabel key="JSP_SORTIE_CAP_EXTOURNE" />
					</td>
					<td>
						<ct:inputText name="complexSortieCap.sortieCap.montantExtourne" id="complexSortieCap.sortieCap.montantExtourne"/>
					</td>
				</tr>
				<tr>
					<td>
						<ct:FWLabel key="JSP_SORTIE_CAP_PASSAGE" />
					</td>
					<td>
						<ct:inputText name="complexSortieCap.sortieCap.idPassageFacturation" id="complexSortieCap.sortieCap.idPassageFacturation" />
					</td>
				</tr>
				<tr>
					<td>
						<ct:FWLabel key="JSP_SORTIE_CAP_ETAT" />
					</td>
					<td>
						<ct:inputText name="etatSortieLibelle" id="etatSortieLibelle" />
					</td>
				</tr>
				<tr>
					<td>
						<ct:FWLabel key="JSP_SORTIE_CAP_DATE" />
					</td>
					<td>
						<ct:inputText name="complexSortieCap.sortieCap.dateSortie" id="complexSortieCap.sortieCap.dateSortie" />
					</td>
				</tr>
				<tr>
					<td>
						<ct:FWLabel key="JSP_SORTIE_CAP_PERIODE_DECISION" />
					</td>
					<td>
						<ct:inputText name="complexSortieCap.decisionCap.dateDebut" id="complexSortieCap.decisionCap.dateDebut" /> - 
						<ct:inputText name="complexSortieCap.decisionCap.dateFin" id="complexSortieCap.decisionCap.dateFin" />
					</td>
				</tr>
				<tr style="display: none;">
					<td colspan="2">
						<ct:inputHidden name="complexSortieCap.decisionCap.idDecisionRectifiee" id="complexSortieCap.decisionCap.idDecisionRectifiee" /> 
					</td>
				</tr>
				<tr class="rectif" style="display: none;">
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
				<tr class="rectif" style="display: none;">
					<td>
						<ct:FWLabel key="JSP_SORTIE_CAP_COTISATION_RECTIFICATIVE" />
					</td>
					<td>
						<ct:inputText name="complexSortieCap.decisionCap.cotisationPeriode" id="complexSortieCap.decisionCap.cotisationPeriode"/>
					</td>
				</tr>
				<tr class="rectif" style="display: none;">
					<td>
						<ct:FWLabel key="JSP_SORTIE_CAP_COTISATION_RECTIFIEE" />
					</td>
					<td>
						<ct:inputText name="complexSortieCap.decisionCapRectifiee.cotisationPeriode" id="complexSortieCap.decisionCapRectifiee.cotisationPeriode"/>
					</td>
				</tr>
			</table>
		</div>
	</td>
</tr>
<%-- /tpl:insert --%>

<%@include file="/theme/detail_ajax/bodyButtons.jspf"%>
<%@include file="/theme/detail_ajax/bodyErrors.jspf"%>
<%@include file="/theme/detail_ajax/footer.jspf"%>
