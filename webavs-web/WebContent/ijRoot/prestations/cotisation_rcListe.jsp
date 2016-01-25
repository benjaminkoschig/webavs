<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%--

INFO !!!
Les labels de cette page sont prefixes avec 'LABEL_JSP_COT_L'

--%>
<%

globaz.ij.vb.prestations.IJCotisationListViewBean viewBean = (globaz.ij.vb.prestations.IJCotisationListViewBean) request.getAttribute("viewBean");

size = viewBean.getSize ();
detailLink = servletContext + mainServletPath + "?userAction=" + globaz.ij.servlet.IIJActions.ACTION_COTISATIONS +".afficher&selectedId=";	
menuDetailLabel = viewBean.getSession().getLabel("MENU_OPTION_DETAIL");

%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	    <TH><ct:FWLabel key="JSP_COT_L_COTISATION"/></TH>
	    <TH><ct:FWLabel key="JSP_DU"/></TH>
	    <TH><ct:FWLabel key="JSP_AU"/></TH>
	    <TH><ct:FWLabel key="JSP_COT_L_TAUX_PC"/></TH>
	    <TH><ct:FWLabel key="JSP_COT_L_MONTANT_BRUT"/></TH>
	    <TH><ct:FWLabel key="JSP_COT_L_COTISATION"/></TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
<%
globaz.ij.vb.prestations.IJCotisationViewBean courant = (globaz.ij.vb.prestations.IJCotisationViewBean) viewBean.get(i);
String detailUrl = "parent.fr_detail.location.href='" + detailLink + courant.getIdCotisation() + "'";
%>
	    <TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getNomExterne()%></TD>
	    <TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getDateDebut()%></TD>
	    <TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getDateFin()%></TD>
	    <TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getTaux()%></TD>
	    <TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getMontantBrut()%></TD>
	    <TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getMontant()%></TD>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>