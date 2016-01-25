<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>

<%
    globaz.pyxis.db.tiers.TIHistoriqueAvsListViewBean viewBean = (globaz.pyxis.db.tiers.TIHistoriqueAvsListViewBean)request.getAttribute ("viewBean");
    size =viewBean.size();
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders"  --%> 
  


<Th width="15%">SVN</Th>
<Th width="35%">Name</Th>
<Th width="15%">Von</Th>
<Th width="15%">Bis</Th>
<Th width="35%">Grund</Th>    
    
    
<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition"  --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList"  --%> 
     
     
     <TD class="mtd" width="15%"><%=viewBean.getNumAvs(i)%>&nbsp;</TD>
     <TD class="mtd" width="25%"><%=viewBean.getNom(session, i)%>&nbsp;</TD>
     <TD class="mtd" width="15%"><%=viewBean.getEntreeVigueur(i)%>&nbsp;</TD>
     <TD class="mtd" width="15%"><%=viewBean.getFinVigueur(i)%>&nbsp;</TD>
     <TD class="mtd" width="30%"><%=viewBean.getMotif(i)%>&nbsp;</TD>
  
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter"  --%>  
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>