<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	detailLink = "naos?userAction=naos.controleEmployeur.reviseur.afficher&selectedId=";
	globaz.naos.db.controleEmployeur.AFReviseurListViewBean viewBean = (globaz.naos.db.controleEmployeur.AFReviseurListViewBean)request.getAttribute ("viewBean");
	size = viewBean.getSize ();
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>

	<TH width="30">&nbsp;</TH>
	<TH width="150">Revisor-Id.</TH>
	<TH width="150">Visum</TH>
	<TH width="200">Beschreibung</TH>
	<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<%    
		actionDetail = targetLocation + "='" + detailLink + viewBean.getIdReviseur(i)+"'";
	%>
	<TD class="mtd" width="16" >
	<ct:menuPopup menu="AFMenuVide" labelId="MENU_OPTIONS" target="top.fr_main" detailLabelId="DETAIL_POPUP" detailLink="<%=detailLink + viewBean.getIdReviseur(i)%>"/>
	</TD>
	<TD class="mtd" onClick="<%=actionDetail%>" width="150"><%=viewBean.getIdReviseur(i)%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>" width="150"><%=viewBean.getVisa(i)%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>" width="200"><%=viewBean.getNomReviseur(i)%></TD>

	
	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>