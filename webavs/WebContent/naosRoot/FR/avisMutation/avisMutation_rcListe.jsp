<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	globaz.naos.db.avisMutation.AFAvisMutationListViewBean viewBean = (globaz.naos.db.avisMutation.AFAvisMutationListViewBean)request.getAttribute ("viewBean");
	size = viewBean.getSize ();
	detailLink = "naos?userAction=naos.avisMutation.avisMutation.afficher&selectedId=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>

	<TH width="30">&nbsp;</TH>
	<TH align="left" width="150">Genre d'annonce</TH>
	<TH align="center" width="100">Date d'annonce</TH>
	<TH align="center" width="100">Caisse recepteur</TH>
	<TH align="left" width="150">Motif opposition</TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<%    
		actionDetail = targetLocation + "='" + detailLink + viewBean.getAvisMutationId(i) + "'";
	%>
	<TD class="mtd" width="30" >
	<ct:menuPopup menu="AFMenuVide" labelId="MENU_OPTIONS" target="top.fr_main" detailLabelId="DETAIL_POPUP" detailLink="<%=detailLink + viewBean.getAvisMutationId(i)%>"/>
	</TD>
	<TD class="mtd" onClick="<%=actionDetail%>"align="left" width="150"><%=globaz.naos.translation.CodeSystem.getLibelle(session,viewBean.getGenreAnnonce(i))%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>"align="center" width="100"><%=viewBean.getDateAnnonce(i)%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>"align="center" width="100"><%=viewBean.getCaisseReception(i)%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>"align="left" width="150"><%=viewBean.getMotifOpposition(i)%></TD>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>