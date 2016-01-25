<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%

globaz.libra.vb.formules.LIChampsListViewBean viewBean = (globaz.libra.vb.formules.LIChampsListViewBean) request.getAttribute("viewBean");
size = viewBean.getSize();

detailLink = baseLink + "afficher&selectedId=";

menuName = "libra-optionsformules";
menuDetailLabel = viewBean.getSession().getLabel("MENU_OPTION_DETAIL");

%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    		<TH>Groupe</TH>
    		<TH>Nom champs</TH>
    		<TH>Activé</TH>
	    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
		<%
			globaz.libra.vb.formules.LIChampsViewBean courant = (globaz.libra.vb.formules.LIChampsViewBean) viewBean.get(i);
			String detailUrl = "parent.fr_detail.location.href='" + detailLink + courant.getIdChamp() + "'";
		%>

		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getCsGroupe() %></TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getCsChamp() %></TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"></TD>
		<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>