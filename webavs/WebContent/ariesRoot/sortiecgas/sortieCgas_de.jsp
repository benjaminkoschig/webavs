<%@page language="java" contentType="text/html;charset=ISO-8859-1"%>
<%@page import="globaz.aries.vb.sortiecgas.ARSortieCgasViewBean"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>

<%@include file="/theme/detail_ajax/header.jspf"%>

<%-- tpl:insert attribute="zoneInit" --%>
<%
ARSortieCgasViewBean viewBean = (ARSortieCgasViewBean) request.getAttribute(FWServlet.VIEWBEAN);
%>
<%-- /tpl:insert --%>

<%@include file="/theme/detail_ajax/javascripts.jspf"%>

<%-- tpl:insert attribute="zoneScripts" --%>

<ct:menuChange displayId="menu" menuId="AFMenuPrincipal" showTab="menu"/>

<script type="text/javascript" src="<%=servletContext%>/scripts/ajax/DefaultTableAjax.js"></script>
<script type="text/javascript" src="<%=servletContext%>/ariesRoot/scripts/sortieCgas_part.js"></script>
<script type="text/javascript">
</script>
<%-- /tpl:insert --%>

<%@include file="/theme/detail_ajax/bodyStart.jspf"%>

<%-- tpl:insert attribute="zoneTitle" --%>
<ct:FWLabel key="JSP_SORTIE_CGAS_TITRE" />
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
						<td><ct:FWCodeSelectTag name="searchModel.forEtat" defaut="" codeType="ARETATSORT" wantBlank="true" /></td>
					</tr>
					<tr>
						<th><ct:FWLabel key="JSP_SORTIE_CGAS_AFFILIE" /></th>
						<th><ct:FWLabel key="JSP_SORTIE_CGAS_EXTOURNE" /></th>
						<th><ct:FWLabel key="JSP_SORTIE_CGAS_PASSAGE" /></th>
						<th><ct:FWLabel key="JSP_SORTIE_CGAS_ETAT" /></th>
					</tr>
				</thead>
				<tbody />
			</table>
			<table class="areaDetail">
				<tr>
					<td colspan="2">
						<b><ct:FWLabel key="JSP_SORTIE_CGAS_TITRE_DETAIL" /></b>
					</td>
				<tr>
				<tr>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td width="200px">
						<ct:FWLabel key="JSP_SORTIE_CGAS_AFFILIE" />
					</td>
					<td>
						<ct:inputText name="complexSortieCgas.affiliation.affilieNumero" id="complexSortieCgas.affiliation.affilieNumero" />
						<ct:inputText name="complexSortieCgas.affiliation.raisonSocialeCourt" id="complexSortieCgas.affiliation.raisonSocialeCourt" style="width: 350px;" />
						<ct:inputText name="typeAffiliationLibelle" id="typeAffiliationLibelle" />
					</td>
				</tr>
				<tr>
					<td>
						<ct:FWLabel key="JSP_SORTIE_CGAS_EXTOURNE" />
					</td>
					<td>
						<ct:inputText name="complexSortieCgas.sortieCgas.montantExtourne" id="complexSortieCgas.sortieCgas.montantExtourne" />
					</td>
				</tr>
				<tr>
					<td>
						<ct:FWLabel key="JSP_SORTIE_CGAS_PASSAGE" />
					</td>
					<td>
						<ct:inputText name="complexSortieCgas.sortieCgas.idPassageFacturation" id="complexSortieCgas.sortieCgas.idPassageFacturation" />
					</td>
				</tr>
				<tr>
					<td>
						<ct:FWLabel key="JSP_SORTIE_CGAS_ETAT" />
					</td>
					<td>
						<ct:inputText name="etatSortieLibelle" id="etatSortieLibelle" />
					</td>
				</tr>
				<tr>
					<td>
						<ct:FWLabel key="JSP_SORTIE_CGAS_DATE" />
					</td>
					<td>
						<ct:inputText name="complexSortieCgas.sortieCgas.dateSortie" id="complexSortieCgas.sortieCgas.dateSortie" />
					</td>
				</tr>
				<tr>
					<td>
						<ct:FWLabel key="JSP_SORTIE_CGAS_PERIODE_DECISION" />
					</td>
					<td>
						<ct:inputText name="complexSortieCgas.decisionCgas.dateDebut" id="complexSortieCgas.decisionCgas.dateDebut" /> - 
						<ct:inputText name="complexSortieCgas.decisionCgas.dateFin" id="complexSortieCgas.decisionCgas.dateFin" />
					</td>
				</tr>
				<tr style="display: none;">
					<td colspan="2">
						<ct:inputHidden name="complexSortieCgas.decisionCgas.idDecisionRectifiee" id="complexSortieCgas.decisionCgas.idDecisionRectifiee" /> 
					</td>
				</tr>
				<tr class="rectif" style="display: none;">
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
				<tr class="rectif" style="display: none;">
					<td>
						<ct:FWLabel key="JSP_SORTIE_CGAS_COTISATION_RECTIFICATIVE" />
					</td>
					<td>
						<ct:inputText name="complexSortieCgas.decisionCgas.cotisationPeriode" id="complexSortieCgas.decisionCgas.cotisationPeriode"/>
					</td>
				</tr>
				<tr class="rectif" style="display: none;">
					<td>
						<ct:FWLabel key="JSP_SORTIE_CGAS_COTISATION_RECTIFIEE" />
					</td>
					<td>
						<ct:inputText name="complexSortieCgas.decisionCgasRectifiee.cotisationPeriode" id="complexSortieCgas.decisionCgasRectifiee.cotisationPeriode"/>
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
