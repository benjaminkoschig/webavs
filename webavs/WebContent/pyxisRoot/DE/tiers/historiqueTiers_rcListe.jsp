<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>

<%
    globaz.pyxis.db.tiers.TIHistoriqueTiersManager viewBean = (globaz.pyxis.db.tiers.TIHistoriqueTiersManager)request.getAttribute ("viewBean");
    size =viewBean.size();
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders"  --%> 
 <Th >Wert</Th>
<Th >Datum</Th>
<Th >Grund</Th>    
    
<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition"  --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList"  --%> 
     
     <%
     	globaz.pyxis.db.tiers.TIHistoriqueTiers bean = (globaz.pyxis.db.tiers.TIHistoriqueTiers)viewBean.getEntity(i);
     %>
     
     <TD class="mtd" ><%=bean.getValeur(viewBean.getForChamp())%>&nbsp;</TD>
     <TD class="mtd" ><%=bean.getDateDebut()%>&nbsp;</TD>
     <TD class="mtd" ><%=bean.getSession().getCodeLibelle(bean.getMotif())%>&nbsp;</TD>
  
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter"  --%>  
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>