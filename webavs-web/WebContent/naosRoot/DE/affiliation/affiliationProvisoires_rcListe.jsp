<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	globaz.naos.db.affiliation.AFAffiliationProvisoiresListViewBean viewBean = (globaz.naos.db.affiliation.AFAffiliationProvisoiresListViewBean)request.getAttribute("viewBean"); 
	size = viewBean.getSize();
	detailLink = "naos?userAction=naos.affiliation.affiliationProvisoires.afficher&selectedId=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    <TH>Abr.-Nr.</TH>
    <TH>SVN</TH>    
    <TH>Name, Vorname</TH>    
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<%    
		globaz.naos.db.affiliation.AFAffiliationProvisoiresViewBean line = (globaz.naos.db.affiliation.AFAffiliationProvisoiresViewBean)viewBean.getEntity(i);
		//actionDetail = targetLocation + "='" + detailLink+"&selectedId="+line.getAffiliationId()+"'";
	%>
	<TD class="mtd"><%="".equals(line.getAffilieNumero())?"&nbsp;":line.getAffilieNumero()%></TD>
	<TD class="mtd"><%="".equals(line.getTiers().getNumAvsActuel())?"&nbsp;":line.getTiers().getNumAvsActuel()%></TD>	
	<TD class="mtd"><%="".equals(line.getTiers().getNom())?"&nbsp;":line.getTiers().getNom()%></TD>		
	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>