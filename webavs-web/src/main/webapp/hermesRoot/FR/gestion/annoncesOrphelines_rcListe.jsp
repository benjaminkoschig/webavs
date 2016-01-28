<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2 //EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
globaz.hermes.db.gestion.HEAnnoncesOrphelinesListViewBean viewBean = (globaz.hermes.db.gestion.HEAnnoncesOrphelinesListViewBean)request.getAttribute("viewBean");
size = viewBean.size();
detailLink = "hermes?userAction=hermes.gestion.annoncesOrphelines.afficher&selectedId=";

%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    <!--<TH>&nbsp;</TH>-->
    <th>Num. ARC</th>
    <th>NSS</th>
    <th>Motif</th>
    <th>Date</th>
    <!--<th>Benutzer</th>-->
    <th>Type</th>
	<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	

    <%
		globaz.hermes.db.gestion.HEAnnoncesOrphelinesViewBean line = (globaz.hermes.db.gestion.HEAnnoncesOrphelinesViewBean)viewBean.getEntity(i);
		actionDetail = targetLocation  + "='" + detailLink + line.getIdAnnonce()+"'";
		String refOption = "'"+line.getRefUnique();
	%>
	
	<TD class="mtd" onClick="<%=actionDetail%>"><%= line.getIdAnnonce().equals("")?"":line.getIdAnnonce()%>&nbsp;</TD>	
<%--	<TD class="mtd" onClick="<%=actionDetail%>"><%= line.getNumeroAvsFormatted().equals("")?"":line.getNumeroAvsFormatted()%>&nbsp;</TD> --%>
	
<TD class="mtd" onClick="<%=actionDetail%>"><%= (line.getNumeroAvsFormatted().equals(""))?"&nbsp;": globaz.commons.nss.NSUtil.formatAVSNew(line.getNumAvs(), line.getNumeroAvsNNSS().equals("true")) %>&nbsp;</TD>
		
	<TD class="mtd" onClick="<%=actionDetail%>"><%= line.getMotif().equals("")?"":line.getMotif()%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>"><%= line.getDateAnnonce().equals("")?"":line.getDateAnnonce()%></TD>
	<!--	<TD class="mtd" onClick="<%=actionDetail%>"><%= line.getUtilisateur().equals("")?"":line.getUtilisateur()%></TD>-->
	<TD class="mtd" onClick="<%=actionDetail%>"><%= line.getTypeAnnonce().equals("")?"":line.getTypeAnnonce()%></TD>	
	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>