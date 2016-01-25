<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	detailLink = "naos?userAction=naos.nombreAssures.nombreAssures.afficher&selectedId=";
	globaz.naos.db.nombreAssures.AFNombreAssuresListViewBean viewBean = (globaz.naos.db.nombreAssures.AFNombreAssuresListViewBean)request.getAttribute ("viewBean");
	size = viewBean.getSize ();
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	<%
		
	%>
	<TH width="30">&nbsp;</TH>
	<TH width="250">Versicherung</TH>
	<TH width="70">Jahr</TH>
	<TH width="130">Anzahl Versicherte</TH>
	<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
	    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<%    
		actionDetail = targetLocation + "='" + detailLink + viewBean.getNbrAssuresId(i) + "'";
	%>
	<TD class="mtd" width="30" >
	<ct:menuPopup menu="AFMenuVide" labelId="MENU_OPTIONS" target="top.fr_main" detailLabelId="DETAIL_POPUP" detailLink="<%=detailLink + viewBean.getNbrAssuresId(i)%>"/>
	</TD>
	<TD class="mtd" onClick="<%=actionDetail%>" width="250"><%=viewBean.getAssurance(i).getAssuranceLibelle()%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>" width="70"><%=viewBean.getAnnee(i)%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>" width="150"><%=viewBean.getNbrAssures(i)%></TD>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>