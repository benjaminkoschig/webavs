<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
globaz.ij.vb.prononces.IJSituationProfessionnelleListViewBean viewBean = (globaz.ij.vb.prononces.IJSituationProfessionnelleListViewBean) request.getAttribute("viewBean");
size = viewBean.getSize ();
detailLink = baseLink + ".afficher&selectedId=";	
menuDetailLabel = viewBean.getSession().getLabel("MENU_OPTION_DETAIL");
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
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
globaz.ij.vb.prononces.IJSituationProfessionnelleViewBean courant = (globaz.ij.vb.prononces.IJSituationProfessionnelleViewBean) viewBean.get(i);
String detailUrl = "parent.fr_detail.location.href='" + detailLink + courant.getIdSituationProfessionnelle() + "&idPrononce="+viewBean.getForIdPrononce()+"&noAVS="+request.getParameter("noAVS")+"&dateDebutPrononce="+request.getParameter("dateDebutPrononce")+"&csTypeIJ="+request.getParameter("csTypeIJ")+"'";
%>
		<%--TD class="mtd" width=""><ct:FWOptionSelectorTag name="<%=\"item\"+i%>" selectedId="<%=courant.getIdSituationProf()%>"/></TD--%>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getNumAffilieEmployeur()%></TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getNomEmployeur()%></TD>
		<TD class="mtd" nowrap align="center" onclick="<%=detailUrl%>"><IMG src="<%=request.getContextPath()+courant.getImageVersementAssure()%>" alt=""></TD>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>