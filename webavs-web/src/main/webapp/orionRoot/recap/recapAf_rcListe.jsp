<%@page import="ch.globaz.orion.business.models.af.RecapAf"%>
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>
<%@page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="globaz.orion.vb.recap.EBRecapAfListViewBean"%>
<%@page import="ch.globaz.orion.business.models.af.StatutRecapAfWebAvsEnum" %>


<%
	EBRecapAfListViewBean viewBean = (EBRecapAfListViewBean) request.getAttribute("viewBean");
	
	size = viewBean.getSize();

// 	size = viewBean.getSize();

	detailLink = "orion?userAction=orion.recap.recapAf.afficher&selectedId=";
	String clotureLink = "orion?userAction=orion.recap.recapAf.afficher&selectedId=";
%>
<%-- /tpl:insert --%>

<%@ include file="/theme/list/javascripts.jspf" %>

 <%-- tpl:insert attribute="zoneHeaders" --%>
			
<%@page import="globaz.orion.vb.pucs.EBPucsFileViewBean"%>			
<%@page import="ch.globaz.orion.business.models.pucs.PucsSearchCriteria"%>
<%@page import="globaz.framework.util.FWCurrency"%>

<th colspan="2">
<th><ct:FWLabel key="NUM_AFFILIE"/></th>
<th><ct:FWLabel key="RECAP_NOM"/></th>
<th><ct:FWLabel key="RECAP_PERIODE"/></th>
<th><ct:FWLabel key="RECAP_DATE_RECEPTION"/></th>
<th><ct:FWLabel key="RECAP_STATUT"/></th>
<%-- /tpl:insert --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
<%@ include file="/theme/list/lineStyle.jspf" %>
<%-- <%-- tpl:insert attribute="zoneCondition" --%> 
<%-- /tpl:insert --%>
	<%		
		RecapAf recap = viewBean.getListRecapAf().get(i);
		actionDetail = targetLocation  + "='" + detailLink + recap.getIdRecap()+"'";
	%>
		<td></td>
		<TD class="mtd" width="">
			<%
				if(recap.getControleManuelle() != null  && recap.getControleManuelle()
						&& (StatutRecapAfWebAvsEnum.GENEREE.equals(recap.getStatut()) || StatutRecapAfWebAvsEnum.TRAITEE.equals(recap.getStatut())
							|| StatutRecapAfWebAvsEnum.AUCUN_CHANGEMENT.equals(recap.getStatut()))){
			%>
				<ct:menuPopup menu="orion-optionsrecapitulationsaf" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=detailLink + recap.getIdRecap()%>">
					<ct:menuParam key="selectedId" value="<%=recap.getIdRecap().toString()%>"/>
				</ct:menuPopup>
			<%
				}
			%>
   		 </TD>
			<td class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>" ><%=recap.getPartner().getNumeroAffilie()%></td>
			<td class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>" ><%=recap.getPartner().getNom()%></td>
			<td class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>" ><%=recap.getAnneeMoisRecapStr()%></td>
			<td class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>" ><%=recap.getLastModificationDateStr()%></td>
			
			<td class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>" >
			<% if(recap.getStatut() != null && recap.getStatut().equals(StatutRecapAfWebAvsEnum.GENEREE)) { %>
				<ct:FWLabel key="RECAP_STATUT_GENEREE"/>
			<% } else if(recap.getStatut() != null && recap.getStatut().equals(StatutRecapAfWebAvsEnum.A_TRAITER)) { %>
				<ct:FWLabel key="RECAP_STATUT_A_TRAITER"/>
			<% } else if(recap.getStatut() != null && recap.getStatut().equals(StatutRecapAfWebAvsEnum.TRAITEE)) { %>
				<ct:FWLabel key="RECAP_STATUT_TRAITEE"/>
			<% } else if(recap.getStatut() != null && recap.getStatut().equals(StatutRecapAfWebAvsEnum.AUCUN_CHANGEMENT)) {%>
				<ct:FWLabel key="RECAP_STATUT_AUCUN_CHANGEMENT"/>
			<% }else if(recap.getStatut() != null && recap.getStatut().equals(StatutRecapAfWebAvsEnum.CLOTUREE)) {%>
				<ct:FWLabel key="RECAP_STATUT_CLOTUREE"/>
			<% } %>
			</td>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:insert attribute="zoneTableFooter" --%> 
<%-- /tpl:insert --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	