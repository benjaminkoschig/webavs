<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%

// Les labels de cette page commence par la préfix "JSP_TAU_L"

globaz.corvus.vb.taux.RETauxListViewBean viewBean = (globaz.corvus.vb.taux.RETauxListViewBean) request.getAttribute("viewBean");
size = viewBean.getSize ();
detailLink = baseLink + "afficher&selectedId=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	    <TH><ct:FWLabel key="JSP_TAU_L_TYPE"/></TH>
	    <TH><ct:FWLabel key="JSP_TAU_L_DU"/></TH>
	    <TH><ct:FWLabel key="JSP_TAU_L_AU"/></TH>
	    <TH><ct:FWLabel key="JSP_TAU_L_TAUX"/></TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
		<%
			globaz.corvus.vb.taux.RETauxViewBean courant = (globaz.corvus.vb.taux.RETauxViewBean) viewBean.get(i);
			
			String detailUrl = "parent.fr_detail.location.href='" + detailLink + courant.getIdTaux() + "'";
		%>

		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getCsTypeTauxLibelle()%></TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getDateDebut()%></TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getDateFin()%></TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getTaux()%></TD>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>