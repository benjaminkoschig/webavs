<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
		detailLink ="osiris?userAction=osiris.ventilation.vPTypeDeProcedureOrdre.afficher&selectedId=";
		globaz.osiris.db.ventilation.CAVPTypeDeProcedureOrdreListViewBean viewBean = (globaz.osiris.db.ventilation.CAVPTypeDeProcedureOrdreListViewBean)request.getAttribute("viewBean");
		size = viewBean.getSize();
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	    <th>Type de procedure</th>
	    <th>Rubrique</th>
	    <th>Type d'ordre</th>
	    <th>Ordre</th>
	    <th>Odre Col.</th>
	    <th>Pénal</th>
	    <%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<%globaz.osiris.db.ventilation.CAVPTypeDeProcedureOrdre line = (globaz.osiris.db.ventilation.CAVPTypeDeProcedureOrdre)viewBean.getEntity(i); %>
	<% actionDetail = targetLocation+"='"+detailLink+line.getTypeProcedureId()+"'"; %>
	<td class="mtd" onclick="<%=actionDetail%>"><%=globaz.osiris.translation.CACodeSystem.getLibelle(session,line.getTypeProcedure())%> </td>
	<td class="mtd" onclick="<%=actionDetail%>"><%=line.getLibelleRubrique()%> </td>
	<td class="mtd" onclick="<%=actionDetail%>"><%=globaz.osiris.translation.CACodeSystem.getLibelle(session,line.getTypeOrdre())%> </td>
	<td class="mtd" onclick="<%=actionDetail%>"><%=line.getOrdre()%> </td>
	<td class="mtd" onclick="<%=actionDetail%>"><%=line.getRubriqueOrdreColonne()%> </td>
	<td class="mtd" onclick="<%=actionDetail%>"><%=line.isPenal().booleanValue()?viewBean.getSession().getLabel("Oui"):viewBean.getSession().getLabel("Non")%> </td>
		<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>