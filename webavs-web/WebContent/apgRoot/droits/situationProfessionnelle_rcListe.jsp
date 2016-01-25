<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%

globaz.apg.vb.droits.APSituationProfessionnelleListViewBean viewBean = (globaz.apg.vb.droits.APSituationProfessionnelleListViewBean) request.getAttribute("viewBean");
size = viewBean.getSize ();
detailLink = baseLink + ".afficher&selectedId=";	
menuDetailLabel = viewBean.getSession().getLabel("MENU_OPTION_DETAIL");

%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    <!--TH></TH-->
    <TH><ct:FWLabel key="JSP_NO_AFFILIE"/></TH>
    <TH><ct:FWLabel key="JSP_EMPLOYEUR"/></TH>
    <TH><ct:FWLabel key="JSP_VERSEMENT_ASSURE"/></TH>
    <%-- /tpl:put --%> 
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
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>