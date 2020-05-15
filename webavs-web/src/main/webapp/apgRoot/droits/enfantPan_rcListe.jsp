<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%

globaz.apg.vb.droits.APEnfantPanListViewBean viewBean = (globaz.apg.vb.droits.APEnfantPanListViewBean) request.getAttribute("viewBean");
size = viewBean.getSize ();
detailLink = baseLink + "afficher&selectedId=";
menuDetailLabel = viewBean.getSession().getLabel("MENU_OPTION_DETAIL");

%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    <TH><ct:FWLabel key="JSP_NSS_ABREGE"/></TH>
    <TH><ct:FWLabel key="JSP_NOM"/></TH>
    <TH><ct:FWLabel key="JSP_PRENOM"/></TH>
    <TH><ct:FWLabel key="JSP_DATE_NAISSANCE"/></TH>
    <TH><ct:FWLabel key="JSP_TYPE_SIT_FAM"/></TH>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
<%
globaz.apg.vb.droits.APEnfantPanViewBean courant = (globaz.apg.vb.droits.APEnfantPanViewBean) viewBean.get(i);
String detailUrl = "parent.fr_detail.location.href='" + detailLink + courant.getIdSitFam() + "&" + globaz.apg.servlet.APAbstractDroitDTOAction.PARAM_ID_DROIT + "=" + courant.getIdDroit() + "'";
%>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getNoAVS()%></TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getNom()%></TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getPrenom()%></TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getDateNaissance()%></TD>
        <TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getTypeLibelle()%></TD>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>