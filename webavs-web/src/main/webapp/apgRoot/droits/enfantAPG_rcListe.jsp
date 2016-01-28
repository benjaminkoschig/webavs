<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%

globaz.apg.vb.droits.APEnfantAPGListViewBean viewBean = (globaz.apg.vb.droits.APEnfantAPGListViewBean) request.getAttribute("viewBean");
size = viewBean.getSize ();
detailLink = baseLink + "actionAfficherEnfantDeListe&selectedId=";	
menuDetailLabel = viewBean.getSession().getLabel("MENU_OPTION_DETAIL");

%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    <TH><ct:FWLabel key="JSP_ENFANT_DETAIL"/></TH>
    <TH><ct:FWLabel key="JSP_DEBUT_DROIT"/></TH>
    <TH><ct:FWLabel key="JSP_FIN_DROIT"/></TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
<%
globaz.apg.vb.droits.APEnfantAPGViewBean courant = (globaz.apg.vb.droits.APEnfantAPGViewBean) viewBean.get(i);
//on redirigera la fenetre parente vers le detail d'un enfantAPG
String detailUrl = "parent.location.href='" + detailLink + courant.getIdEnfant() + "&" + globaz.apg.servlet.APAbstractDroitDTOAction.PARAM_ID_DROIT + "=" + request.getParameter(globaz.apg.servlet.APAbstractDroitDTOAction.PARAM_ID_DROIT) + "'";
%>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getDetailRequerant()%>&nbsp;</TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getDateDebutDroit()%>&nbsp;</TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getDateFinDroit()%>&nbsp;</TD>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>