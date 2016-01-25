<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%

globaz.libra.vb.domaines.LIDomainesListViewBean viewBean = (globaz.libra.vb.domaines.LIDomainesListViewBean) request.getAttribute("viewBean");
size = viewBean.getSize();

detailLink = baseLink + "afficher&selectedId=";

menuName = "li-menuprincipal";
menuDetailLabel = viewBean.getSession().getLabel("MENU_OPTION_DETAIL");

%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    		<TH><ct:FWLabel key="ECRAN_GES_DOM_NOM_APP"/></TH>
    		<TH><ct:FWLabel key="ECRAN_GES_DOM_DOMAINE"/></TH>
    		<TH><ct:FWLabel key="ECRAN_GES_NB_GROUPES"/></TH>
    		<TH><ct:FWLabel key="ECRAN_GES_NB_USER"/></TH>
	    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
		<%
			globaz.libra.vb.domaines.LIDomainesViewBean courant = (globaz.libra.vb.domaines.LIDomainesViewBean) viewBean.get(i);
			String detailUrl = "parent.fr_detail.location.href='" + detailLink + courant.getIdDomaine() + "'";
		%>

		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getNomApplication()%></TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getSession().getCodeLibelle(courant.getCsDomaine())%></TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>" align="center"><%=courant.getNbGroupeDomaine()%></TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>" align="center"><%=courant.getNbUsersDomaine()%></TD>
		<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>