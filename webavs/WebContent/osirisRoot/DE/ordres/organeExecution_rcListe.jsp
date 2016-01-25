<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	detailLink ="osiris?userAction=osiris.ordres.organeExecution.afficher&selectedId=";
	globaz.osiris.db.ordres.CAOrganeExecutionListViewBean viewBean = (globaz.osiris.db.ordres.CAOrganeExecutionListViewBean)request.getAttribute("viewBean");
	size = viewBean.getSize();
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 
    <th nowrap colspan="2">Nummer</th>
    <th nowrap width="554">Bezeichnung</th>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%> 
  
  <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%> 
		<% globaz.osiris.db.ordres.CAOrganeExecution line = (globaz.osiris.db.ordres.CAOrganeExecution)viewBean.getEntity(i); %>
		<% actionDetail = targetLocation+"='"+detailLink+line.getIdOrganeExecution()+"'"; %>
		<td class="mtd" width="16" >
			<ct:menuPopup menu="CA-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=(detailLink+line.getIdOrganeExecution())%>"/>		
		</td> 
	    <td class="mtd" nowrap onClick="<%=actionDetail%>"><%=line.getIdOrganeExecution()%></td>
	    <td class="mtd" nowrap onClick="<%=actionDetail%>"><%=line.getNom()%></td>	
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>