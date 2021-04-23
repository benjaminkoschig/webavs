<%@ page import="globaz.apg.vb.droits.APSituationProfessionnelleListViewBean" %>
<%@ page import="globaz.apg.vb.droits.APTypePresationDemandeResolver" %>
<%@ page import="globaz.prestation.api.PRTypeDemande" %>
<%@ page language="java" isELIgnored ="false" errorPage="/errorPage.jsp" %>
<%@ page isELIgnored ="false" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/theme/list/header.jspf" %>

<%

APSituationProfessionnelleListViewBean viewBean = (APSituationProfessionnelleListViewBean) request.getAttribute("viewBean");
size = viewBean.getSize ();
viewBean.setTypeDemande(APTypePresationDemandeResolver.resolveEnumTypePrestation(session));
detailLink = baseLink + ".afficher&selectedId=";
menuDetailLabel = viewBean.getSession().getLabel("MENU_OPTION_DETAIL");

%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
    <TH><ct:FWLabel key="JSP_NO_AFFILIE"/></TH>
    <TH><ct:FWLabel key="JSP_EMPLOYEUR"/></TH>
    <TH><ct:FWLabel key="JSP_VERSEMENT_ASSURE"/></TH>
    <c:if test="${viewBean.displayJourIndemnise()}">
        <TH><ct:FWLabel key="JSP_NB_JOUR_INDEMNISE"/></TH>
    </c:if>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
<%
	globaz.apg.vb.droits.APSituationProfessionnelleViewBean courant = (globaz.apg.vb.droits.APSituationProfessionnelleViewBean) viewBean.get(i);

	String detailUrl = "parent.fr_detail.location.href='" + detailLink + courant.getIdSituationProf() + "&" + globaz.apg.servlet.APAbstractDroitDTOAction.PARAM_ID_DROIT + "=" + courant.getIdDroit()  + "'";
	boolean isProtected = false;

	if (courant.isProtectedCI() || courant.isProtectedAffiliation()){
		detailUrl = "";
		isProtected = true;
	}

%>
		<%--TD class="mtd" width=""><ct:FWOptionSelectorTag name="<%=\"item\"+i%>" selectedId="<%=courant.getIdSituationProf()%>"/></TD--%>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>">
			<% if (isProtected){ %>
				<img src="<%=request.getContextPath()+"/images/cadenas.gif"%>">
			<% } %>
			<%=courant.getNumAffilieEtTypeAffiliationEmployeur()%>

		</TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getNomEmployeur()%></TD>
        <TD class="mtd" nowrap align="center" onclick="<%=detailUrl%>"><IMG src="<%=request.getContextPath()+courant.getVersementAssure()%>" alt=""></TD>
        <c:if test="${viewBean.displayJourIndemnise()}">
            <TD class="mtd" nowrap align="center" onclick="<%=detailUrl%>"><%=courant.getNbJourIndemnise()%></TD>
        </c:if>

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>