<%@page import="globaz.hercule.db.groupement.CEGroupeListViewBean"%><%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@page import="globaz.hercule.db.groupement.CEGroupe"%>

<%
	detailLink = "hercule?userAction=hercule.groupement.membre.chercher&selectedId=";
	CEGroupeListViewBean viewBean = (CEGroupeListViewBean)request.getAttribute ("viewBean");
	size = viewBean.getSize ();
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
<%-- tpl:put name="zoneHeaders" --%>
	    
	<th><ct:FWLabel key="GROUPE_LIBELLE"/></th>
	<th><ct:FWLabel key="GROUPE_COUVERTURE"/></th>

<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
<%-- tpl:put name="zoneCondition" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
<%-- tpl:put name="zoneList" --%>

		<%
			CEGroupe line = (CEGroupe)viewBean.getEntity(i);
			actionDetail = targetLocation+"='hercule?userAction=hercule.groupement.membre.chercher&idGroupe=" + line.getIdGroupe()+"'";
		%>
		
		<TD class="mtd" onClick="<%=actionDetail%>" align="center"><%=line.getLibelleGroupe()%></td>
		<TD class="mtd" onClick="<%=actionDetail%>" align="center"><%=line.getAnneeCouvertureMinimal()%></td>
		
<%-- /tpl:put --%>

<ct:menuChange displayId="options" menuId="CE-OptionsDefaut" showTab="menu" checkAdd="false">
			<ct:menuSetAllParams key="selectedId" value=""/>
</ct:menuChange>

<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:put name="zoneTableFooter" --%><%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
<%-- /tpl:insert --%>