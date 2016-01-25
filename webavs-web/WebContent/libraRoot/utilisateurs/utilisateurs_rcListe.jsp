<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%

globaz.libra.vb.utilisateurs.LIUtilisateursListViewBean viewBean = (globaz.libra.vb.utilisateurs.LIUtilisateursListViewBean) request.getAttribute("viewBean");
size = viewBean.getSize();

detailLink = baseLink + "afficher&selectedId=";

menuName = "li-menuprincipal";
menuDetailLabel = viewBean.getSession().getLabel("MENU_OPTION_DETAIL");

isSelection = "yes".equals(request.getParameter("colonneSelection"));

%>

<script type="text/javascript" src="<%=servletContext%>/scripts/selectionPopup.js"></script>

<script>
var selection = <%=isSelection ? "true" : "false" %>;
</script>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    		<TH><ct:FWLabel key="ECRAN_GES_UTI_UTILISATEUR"/></TH>
    		<TH><ct:FWLabel key="ECRAN_GES_UTI_GROUPE"/></TH>
    		<TH><ct:FWLabel key="ECRAN_GES_UTI_DOMAINE"/></TH>
    		<TH><ct:FWLabel key="ECRAN_GED_UTI_IS_DEFAULT"/></TH>
	    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
		<%
			globaz.libra.vb.utilisateurs.LIUtilisateursViewBean courant = (globaz.libra.vb.utilisateurs.LIUtilisateursViewBean) viewBean.get(i);
			String detailUrl = "parent.fr_detail.location.href='" + detailLink + courant.getIdUtilisateur() + "'";
		%>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getIdUtilisateurExterne()%></TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getLibelleGroupe(courant.getIdGroupe())%></TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getLibelleDomaine(courant.getIdGroupe())%></TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>" align="center">
			<% if (courant.getIsDefault().booleanValue()) { %>
				<img src="<%=request.getContextPath()+"/images/ok.gif"%>">
			<% } else { %>
				<img src="<%=request.getContextPath()+"/images/erreur.gif"%>">
			<% } %>
		</TD>

		<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>