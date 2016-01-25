<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
	<%
		detailLink ="pavo?userAction=pavo.comparaison.anomalieCI.afficher&selectedId=";
		globaz.pavo.db.comparaison.CIAnomalieCIListViewBean viewBean = (globaz.pavo.db.comparaison.CIAnomalieCIListViewBean)request.getAttribute("viewBean");
		size = viewBean.getSize();
	%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    	<Th width="">NSS</th>
    	<Th width="">Type d'anomalie</th>
	   	<Th width="">Nom</th>
		<Th width="">Pays</th>
    	<Th width="">Etat</th>
    	
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<%globaz.pavo.db.comparaison.CIAnomalieCI line = (globaz.pavo.db.comparaison.CIAnomalieCI)viewBean.getEntity(i); %>
	<% actionDetail = targetLocation+"='"+detailLink+line.getAnomalieId()+"'"; %>
	<td class="mtd" width = "15" onclick="<%=actionDetail%>"><%=globaz.commons.nss.NSUtil.formatAVSUnknown(line.getNumeroAvs())%> </td>
	<td class="mtd" onclick="<%=actionDetail%>"><%=globaz.pavo.translation.CodeSystem.getLibelle(line.getTypeAnomalie(), session)%></td>
	<td class="mtd" onclick="<%=actionDetail%>"><%=line.getNomPrenom()%></td>
	<td class="mtd" onclick="<%=actionDetail%>"><%=globaz.pavo.translation.CodeSystem.getLibelle(line.getPays(), session)%></td>
	<td class="mtd" onclick="<%=actionDetail%>"><%=globaz.pavo.translation.CodeSystem.getLibelle(line.getEtat(), session)%></td>
	
	
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>