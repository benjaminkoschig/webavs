<%@page import="globaz.perseus.vb.echeance.PFEcheanceLibreViewBean"%>
<%@page import="globaz.perseus.vb.echeance.PFEcheanceLibreListViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>
<%
PFEcheanceLibreListViewBean viewBean = (PFEcheanceLibreListViewBean) request.getAttribute("viewBean");
size = viewBean.getSize();
menuName = "perseus-menuprincipal";	
detailLink = "perseus?userAction=perseus.echeance.echeanceLibre.afficher&selectedId=";
%>
<%-- /tpl:insert --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:insert attribute="zoneHeaders" --%>
	<tr>
		<th width="20px">
			<ct:FWLabel key="JSP_PF_ECHEANCELIBRE_L_ECHEANCELIBRE"/>
		</th>
		<th>
			<ct:FWLabel key="JSP_PF_ECHEANCELIBRE_L_DATEBUTOIRE"/>
		</th>
		<th>
			<ct:FWLabel key="JSP_PF_ECHEANCELIBRE_L_MOTIF"/>
		</th>
	<tr>
	    <%-- /tpl:insert --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:insert attribute="zoneCondition" --%>
    <%-- /tpl:insert --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:insert attribute="zoneList" --%>
<%
PFEcheanceLibreViewBean echeanceLibre = (PFEcheanceLibreViewBean) viewBean.get(i);
String detailUrl = "parent.location.href='" + detailLink + echeanceLibre.getId() +"&idDossier="+echeanceLibre.getEcheanceLibre().getDossier().getDossier().getId()+"'";
%>
	<td class="mtd" nowrap width="20px">
		<ct:menuPopup menu="perseus-optionsEcheanceLibre" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=detailLink + echeanceLibre.getId()%>">
		 		<ct:menuParam key="idDossier" value="<%=echeanceLibre.getEcheanceLibre().getSimpleEcheanceLibre().getIdDossier()%>"/>
		</ct:menuPopup>
	</td>
	<td class="mtd" nowrap onclick="<%=detailUrl%>"><%=echeanceLibre.getEcheanceLibre().getSimpleEcheanceLibre().getDateButoire()%></td>
	<td class="mtd" nowrap onclick="<%=detailUrl%>"><%=echeanceLibre.getEcheanceLibre().getSimpleEcheanceLibre().getMotif()%></td>	
		<%-- /tpl:insert --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:insert attribute="zoneTableFooter" --%>
	<%-- /tpl:insert --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	