<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%

// Les labels de cette page commence par la préfix "JSP_PAR_L"

globaz.corvus.vb.parametrages.REApiListViewBean viewBean = (globaz.corvus.vb.parametrages.REApiListViewBean) request.getAttribute("viewBean");
size = viewBean.getSize();

detailLink = baseLink + "afficher&selectedId=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	    <%@page import="globaz.corvus.vb.parametrages.REApiListViewBean"%>
		<%@page import="globaz.corvus.vb.parametrages.REApiViewBean"%>
		<TH><ct:FWLabel key="JSP_PAR_D_DEBUT"/></TH>
	    <TH><ct:FWLabel key="JSP_PAR_D_FIN"/></TH>
	    <TH><ct:FWLabel key="JSP_PAR_D_MONTMAX"/></TH>
	    <TH><ct:FWLabel key="JSP_PAR_D_MONTMIN"/></TH>
    	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
		<%
			globaz.corvus.vb.parametrages.REApiViewBean courant= (globaz.corvus.vb.parametrages.REApiViewBean) viewBean.get(i);
			String detailUrl = "parent.fr_detail.location.href='" + detailLink + courant.getIdMontantApi() + "'";
		%>

		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getDateDebut()%></TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getDateFin()%></TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getMontantApiMax()%></TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getMontantApiMin()%></TD>
		
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
<%-- /tpl:insert --%>