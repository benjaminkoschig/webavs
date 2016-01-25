<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<%
    globaz.pyxis.db.tiers.TIHistoriqueAffilieListViewBean viewBean = (globaz.pyxis.db.tiers.TIHistoriqueAffilieListViewBean)request.getAttribute ("viewBean");
    size =viewBean.size();
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders"  --%> 
  


<Th width="15%">N° Affiliato</Th>
<Th width="35%">Cognome</Th>
<Th width="15%">Di</Th>
<Th width="15%">a</Th>
<Th width="35%">Motivo</Th>    

    
    
<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition"  --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList"  --%> 
     
     
     <TD class="mtd" width="15%"><%=viewBean.getNumAffilie(i)%>&nbsp;</TD>
     <TD class="mtd" width="35%"><%=viewBean.getNom(session, i)%>&nbsp;</TD>
     <TD class="mtd" width="15%"><%=viewBean.getEntreeVigueur(i)%>&nbsp;</TD>
     <TD class="mtd" width="15%"><%=viewBean.getFinVigueur(i)%>&nbsp;</TD>
     
     
     <TD class="mtd" width="35%"><%=viewBean.getMotif(i)%>&nbsp;</TD>
  
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter"  --%>  
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>