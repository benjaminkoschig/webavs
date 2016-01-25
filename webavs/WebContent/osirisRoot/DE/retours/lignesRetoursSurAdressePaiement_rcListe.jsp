<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	CALignesRetoursListViewBean viewBean = (CALignesRetoursListViewBean) request.getAttribute("viewBean");
	size = viewBean.size();
	
%>
<%@page import="globaz.osiris.db.retours.CALignesRetoursListViewBean"%>
<%@page import="globaz.osiris.db.retours.CALignesRetoursViewBean"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<SCRIPT language="JavaScript">

function supprimerPeriode(id){
    if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?")){
        parent.location.href="<%=request.getContextPath()%>/osiris?userAction=osiris.retours.retours.actionSupprimerLignesRetoursSurAdressePaiement&selectedId="+id+"&idRetour"+id;
    }
}

function afficherPeriode(id){
 	parent.location.href="<%=request.getContextPath()%>/osiris?userAction=osiris.retours.retours.actionAfficherPeriodeINV&selectedId="+id;
}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	    	<TH>Begünstigter</TH>
		    <TH>Zahlungsadresse</TH>
			<TH>Betrag</TH>
			<TH>&nbsp;</TH>
	    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<%
		CALignesRetoursViewBean line = (CALignesRetoursViewBean) viewBean.getEntity(i);
		actionDetail = targetLocation + "='" + detailLink + line.getIdLigneRetour()+"'";
	%>
	<TD class="mtd" nowrap="nowrap"><PRE style="font-size:10pt"><%= line.getDescriptionLigneRetourSurAdressePaiementBeneficiaire()%></PRE></TD>
	<TD class="mtd" nowrap="nowrap"><PRE style="font-size:10pt"><%= line.getDescriptionLigneRetourSurAdressePaiementAdresse()%></PRE></TD>
	<TD class="mtd" nowrap="nowrap" align="right"><%= new FWCurrency(line.getMontant()).toStringFormat()%>&nbsp;</TD>
	<%if(line.isRetourModifiable()){%>
		<TD class="mtd" width="" onclick="supprimerPeriode(<%=line.getIdLigneRetour()%>)"><a href="#">Löschen</a></TD>
	<%}else{%>
		<TD class="mtd" width="" ></TD>
	<%}%>
		<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>