<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%

globaz.libra.vb.groupes.LIGroupesListViewBean viewBean = (globaz.libra.vb.groupes.LIGroupesListViewBean) request.getAttribute("viewBean");
size = viewBean.getSize();

detailLink = baseLink + "afficher&selectedId=";

menuName = "li-menuprincipal";
menuDetailLabel = viewBean.getSession().getLabel("MENU_OPTION_DETAIL");

%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    		<TH><ct:FWLabel key="ECRAN_GES_LIBELLE"/></TH>
    		<TH><ct:FWLabel key="ECRAN_GES_DOMAINE"/></TH>
    		<TH><ct:FWLabel key="ECRAN_GES_NB_USER"/></TH>
	    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
		<%
			globaz.libra.vb.groupes.LIGroupesViewBean courant = (globaz.libra.vb.groupes.LIGroupesViewBean) viewBean.get(i);
			String detailUrl = "parent.fr_detail.location.href='" + detailLink + courant.getIdGroupe() + "'";
		%>

		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getLibelleGroupe() %></TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getLibelleDomaine(courant.getIdDomaine()) %></TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>" align="center"><%=courant.getNbUsersGroupe()%></TD>
		<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>