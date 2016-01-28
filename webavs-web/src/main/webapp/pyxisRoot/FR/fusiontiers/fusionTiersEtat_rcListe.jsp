 <%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
 <%
	globaz.pyxis.db.fusiontiers.TIFusionTiersEtatListViewBean viewBean = (globaz.pyxis.db.fusiontiers.TIFusionTiersEtatListViewBean)request.getAttribute ("viewBean");
	size = viewBean.size();
    %>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	<%-- tpl:put name="zoneHeaders"  --%>
		  <Th><ct:FWLabel key='NOM_APPLICATION' /></Th>
		  <Th><ct:FWLabel key='DATE_TRAITEMENT' /></Th>
		  <Th><ct:FWLabel key='COMMENTAIRE' /></Th>
		  <Th><ct:FWLabel key='STATUT' /></Th>
		  <Th><ct:FWLabel key='TYPE' /></Th>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition"  --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList"  --%>
		<%
			globaz.pyxis.db.fusiontiers.TIFusionTiersEtatViewBean line = (globaz.pyxis.db.fusiontiers.TIFusionTiersEtatViewBean)viewBean.getEntity(i);
		%>
      <TD class="mtd"><%=line.getNomApplication()%></TD>
      <TD class="mtd"><%=line.getDateTraitement()%></TD>
      <TD class="mtd"><%=line.getCommentaire()%></TD>
      <TD class="mtd"><%=viewBean.getSession().getCodeLibelle(line.getCsStatut())%></TD>
      <TD class="mtd"><%=viewBean.getSession().getCodeLibelle(line.getCsType())%></TD>
     <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter"  --%>  
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>