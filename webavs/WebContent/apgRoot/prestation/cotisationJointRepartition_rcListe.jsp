<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%

globaz.apg.vb.prestation.APCotisationJointRepartitionListViewBean viewBean = (globaz.apg.vb.prestation.APCotisationJointRepartitionListViewBean) request.getAttribute("viewBean");

size = viewBean.getSize ();
detailLink = servletContext + mainServletPath + "?userAction=" + globaz.apg.servlet.IAPActions.ACTION_COTISATION_JOINT_REPARTITION +".afficher&selectedId=";	
menuDetailLabel = viewBean.getSession().getLabel("MENU_OPTION_DETAIL");

%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	    <TH><ct:FWLabel key="JSP_COTISATION"/></TH>
	    <TH><ct:FWLabel key="JSP_DU"/></TH>
	    <TH><ct:FWLabel key="JSP_AU"/></TH>
	    <TH><ct:FWLabel key="JSP_TAUX_PC"/></TH>
	    <TH><ct:FWLabel key="JSP_MONTANT_BRUT"/></TH>
	    <TH><ct:FWLabel key="JSP_COTISATION"/></TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
<%
globaz.apg.vb.prestation.APCotisationJointRepartitionViewBean courant = (globaz.apg.vb.prestation.APCotisationJointRepartitionViewBean) viewBean.get(i);
String detailUrl = "parent.fr_detail.location.href='" + detailLink + courant.getIdCotisation() + "'";
%>
	    <TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getNomExterne()%></TD>
	    <TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getDateDebut()%></TD>
	    <TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getDateFin()%></TD>
	    <TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getTaux()%></TD>
	    <TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getMontantBrutCotisation()%></TD>
	    <TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getMontant()%></TD>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>