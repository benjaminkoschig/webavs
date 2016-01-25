<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	globaz.naos.db.masse.AFMasseListViewBean viewBean = (globaz.naos.db.masse.AFMasseListViewBean)request.getAttribute ("viewBean");
	size = viewBean.getSize ();
	detailLink = "naos?userAction=naos.masse.masse.afficher&selectedId=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>

	<TH width="30">&nbsp;</TH>
	<TH nowrap align="left" width="110">Versicherung</TH>
	<TH align="center" width="110">Beginn</TH>
	<TH align="center" width="110">Ende</TH>
	<TH align="left" width="200">Jahres Lohnsumme</TH>
	<TH align="left" nowrap width="200">Lohnsumme der Periodizität</TH>
	<TH align="left" nowrap width="70">Verarbeitung</TH>
	<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
	    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<%    
		actionDetail = targetLocation + "='" + detailLink + viewBean.getMasseId(i)+"'";
	%>
	<TD class="mtd" width="16" >
	<ct:menuPopup menu="AFMenuVide" labelId="MENU_OPTIONS" target="top.fr_main" detailLabelId="DETAIL_POPUP" detailLink="<%=detailLink + viewBean.getMasseId(i)%>"/>
	</TD>
	<TD class="mtd" onClick="<%=actionDetail%>"align="left" width="110"><%=viewBean.getLibelleAssurance(i)%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>"align="center" width="110"><%=viewBean.getDateDebut(i)%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>"align="center" width="110"><%=viewBean.getDateFin(i)%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>"align="right" width="200"><%=viewBean.getNouvelleMasseAnnuelle(i)%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>"align="right" width="200"><%=viewBean.getNouvelleMassePeriodicite(i)%></TD>
	<% if (viewBean.isTraitement(i).booleanValue()) { %>
		<TD class="mtd" onClick="<%=actionDetail%>"align="center" width="70"><IMG src="<%=request.getContextPath()%>/images/select2.gif" ></TD>
	<% } else { %>
		<TD class="mtd" onClick="<%=actionDetail%>"align="center" width="70"><%=""%></TD>
	<% } %>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>