<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%

globaz.cepheus.vb.tauxImposition.DOTauxImpositionListViewBean viewBean = (globaz.cepheus.vb.tauxImposition.DOTauxImpositionListViewBean) request.getAttribute("viewBean");
size = viewBean.getSize ();
detailLink = baseLink + "afficher&selectedId=";

%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	    <TH><ct:FWLabel key="JSP_TIM_L_CANTON"/></TH>
	    <TH><ct:FWLabel key="JSP_TIM_L_TYPE_IMPOSITION"/></TH>
	    <TH><ct:FWLabel key="JSP_TIM_L_TAUX_IMPOSITION"/></TH>
	    <TH><ct:FWLabel key="JSP_TIM_L_DATE_DEBUT"/></TH>
	    <TH><ct:FWLabel key="JSP_TIM_L_DATE_FIN"/></TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
		<%
			globaz.cepheus.vb.tauxImposition.DOTauxImpositionViewBean courant = (globaz.cepheus.vb.tauxImposition.DOTauxImpositionViewBean) viewBean.get(i);
			
			String detailUrl = "parent.fr_detail.location.href='" + detailLink + courant.getIdTauxImposition() + "'";
		%>

		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getCsCantonLibelle()%></TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getTypeImpotSourceLibelle()%></TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getTaux()%></TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getDateDebut()%></TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getDateFin()%></TD>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>