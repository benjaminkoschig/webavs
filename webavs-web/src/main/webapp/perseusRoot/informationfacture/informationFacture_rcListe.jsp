<%@page import="globaz.perseus.vb.informationfacture.PFInformationFactureViewBean"%>
<%@page import="globaz.perseus.vb.informationfacture.PFInformationFactureListViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>
<%
PFInformationFactureListViewBean viewBean = (PFInformationFactureListViewBean) request.getAttribute("viewBean");
size = viewBean.getSize();
menuName = "perseus-menuprincipal";	
detailLink = "perseus?userAction=perseus.informationfacture.informationFacture.afficher&selectedId=";
%>
<%-- /tpl:insert --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:insert attribute="zoneHeaders" --%>
	<tr>
		<th width="20px">
			<ct:FWLabel key="JSP_PF_NUMERO_INFORMATION_FACTURE"/>
		</th>
		<th>
			<ct:FWLabel key="JSP_PF_DATE_INFORMATION_FACTURE"/>
		</th>
		<th>
			<ct:FWLabel key="JSP_PF_DESCRIPTION_INFORMATION_FACTURE"/>
		</th>
	<tr>
	    <%-- /tpl:insert --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:insert attribute="zoneCondition" --%>
    <%-- /tpl:insert --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:insert attribute="zoneList" --%>
<%
PFInformationFactureViewBean informationFacture = (PFInformationFactureViewBean) viewBean.get(i);
String detailUrl = "parent.location.href='" + detailLink + informationFacture.getId() +"&idDossier="+informationFacture.getInformationFacture().getDossier().getDossier().getId()+"'";
%>
	<td class="mtd" nowrap width="20px">
		<ct:menuPopup menu="perseus-optionsInformations" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=detailLink + informationFacture.getId()%>">
		 		<ct:menuParam key="idDossier" value="<%=informationFacture.getInformationFacture().getSimpleInformationFacture().getIdDossier()%>"/>
		</ct:menuPopup>
	</td>
	<td class="mtd" nowrap onclick="<%=detailUrl%>"><%=informationFacture.getInformationFacture().getSimpleInformationFacture().getDate()%></td>
	<td class="mtd" nowrap onclick="<%=detailUrl%>"><%=informationFacture.getInformationFacture().getSimpleInformationFacture().getDescription()%></td>
		<%-- /tpl:insert --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:insert attribute="zoneTableFooter" --%>
	<%-- /tpl:insert --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	