<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.cygnus.vb.contributions.RFContributionsAssistanceAIDetailViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>

<%@ page import="globaz.cygnus.vb.contributions.RFContributionsAssistanceAIListViewBean" %>
<%@ page import="globaz.cygnus.servlet.IRFActions" %>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>

<%@ include file="/theme/list/header.jspf" %>
<%
	RFContributionsAssistanceAIListViewBean viewBean = (RFContributionsAssistanceAIListViewBean) request.getAttribute("viewBean");

	size = viewBean.getContainerSize();

	detailLink = "cygnus?userAction=" + IRFActions.ACTION_CONTRIBUTIONS_ASSISTANCE_AI + ".afficher";
%>
<%@ include file="/theme/list/javascripts.jspf" %>
			<th width="18%">
				<ct:FWLabel key="JSP_CAAI_PERIODE" />
			</th>
			<th width="8%">
				<ct:FWLabel key="JSP_CAAI_MONTANT_CA_AI" />
			</th>
			<th width="13%">
				<ct:FWLabel key="JSP_CAAI_DATE_DEPOT_DEMANDE_CA_AI" />
			</th>
			<th width="13%">
				<ct:FWLabel key="JSP_CAAI_DATE_DECISION_CA_AI" />
			</th>
			<th width="18%">
				<ct:FWLabel key="JSP_CAAI_API" />
			</th>
			<th width="10%">
				<ct:FWLabel key="JSP_CAAI_MONTANT_API" />
			</th>
			<th width="18%">
				<ct:FWLabel key="JSP_CAAI_RECOURS" />
			</th>
			<th width="3%">
				<ct:FWLabel key="JSP_CAAI_NUMERO" />
			</th>
<%@ include file="/theme/list/tableHeader.jspf" %>
<%
		RFContributionsAssistanceAIDetailViewBean courant = null;
		try {
			courant = (RFContributionsAssistanceAIDetailViewBean) viewBean.get(i);
		} catch (Exception e) {
			break;
		}
%>
<%@ include file="/theme/list/lineStyle.jspf" %>
<%
		String detailUrl = 	"parent.fr_detail.location.href='" + detailLink 
							+ "&idContributionAssistanceAI=" + courant.getIdContributionAssistanceAI()
							+ "'";
%>
			<td class="mtd" nowrap onClick="<%=detailUrl%>" align="left">
				<%=courant.getDateDebutPeriode() + " - " + courant.getDateFinPeriode()%>
			</td>
			<td class="mtd" nowrap onClick="<%=detailUrl%>" align="right">
				<%=JadeStringUtil.isBlank(courant.getMontantContribution()) ? "&nbsp;" : courant.getMontantContribution()%>
			</td>
			<td class="mtd" nowrap onClick="<%=detailUrl%>" align="center">
				<%=JadeStringUtil.isBlankOrZero(courant.getDateReceptionDecisionCAAI()) ? "&nbsp;" : courant.getDateReceptionDecisionCAAI()%>
			</td>
			<td class="mtd" nowrap onClick="<%=detailUrl%>" align="center">
				<%=JadeStringUtil.isBlankOrZero(courant.getDateDepotDemandeCAAI()) ? "&nbsp;" : courant.getDateDepotDemandeCAAI()%>
			</td>
			<td class="mtd" nowrap onClick="<%=detailUrl%>" align="center">
				<%=JadeStringUtil.isBlankOrZero(courant.getCodeAPI()) ? "&nbsp;" : courant.getDetailCodeAPI()%>
			</td>
			<td class="mtd" nowrap onClick="<%=detailUrl%>" align="right">
				<%=JadeStringUtil.isBlankOrZero(courant.getMontantAPI()) ? "&nbsp;" : courant.getMontantAPI()%>
			</td>
			<td class="mtd" nowrap onClick="<%=detailUrl%>" align="left">
				<%=JadeStringUtil.isBlankOrZero(courant.getDateDebutRecours()) && JadeStringUtil.isBlankOrZero(courant.getDateFinRecours()) ? "&nbsp;" : (courant.getDateDebutRecours() + " - " + courant.getDateFinRecours())%>
			</td>
			<td class="mtd" nowrap onClick="<%=detailUrl%>" align="center">
				<%=courant.getIdContributionAssistanceAI()%>
			</td>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%@ include file="/theme/list/tableEnd.jspf" %>
