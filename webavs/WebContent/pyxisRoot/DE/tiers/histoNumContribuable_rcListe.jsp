<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>

<%
    globaz.pyxis.db.tiers.TIHistoNumContribuableListViewBean viewBean = (globaz.pyxis.db.tiers.TIHistoNumContribuableListViewBean)request.getAttribute ("viewBean");
    size =viewBean.size();
    
	String idTiers = "";
	if(!JadeStringUtil.isNull(request.getParameter("idTiers"))){
		idTiers = request.getParameter("idTiers");
	}
    
    detailLink ="pyxis?userAction=pyxis.tiers.histoNumContribuable.afficher&selectedId=";
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders"  --%> 
  
<%@page import="globaz.jade.client.util.JadeStringUtil"%><TH nowrap width="16">&nbsp;</TH>

<Th width="15%">N° de contribuable</Th>
<Th width="35%">Nom</Th>
<Th width="15%">De</Th>
<Th width="35%">Motif</Th>    
    
<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition"  --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList"  --%> 
     
     <%
	 actionDetail = "parent.location.href='"+detailLink+viewBean.getIdNumContribuable(i)+"&idTiers="+idTiers+"'";
     String dLink = request.getContextPath()+"/"+detailLink + viewBean.getIdNumContribuable(i)+"&idTiers="+idTiers;

	 %>
	 
	<TD class="mtd" width="16" >
	 <ct:menuPopup menu="TIMenuVide" detailLabelId="Detail" detailLink="<%=dLink%>" >
	 	<ct:menuParam key="selectedId" value="<%=viewBean.getIdNumContribuable(i)%>"/>
	 </ct:menuPopup>
     
     <TD class="mtd" width="15%" onclick="<%=actionDetail%>"><%=viewBean.getNumContribuable(i)%>&nbsp;</TD>
     <TD class="mtd" width="25%" onclick="<%=actionDetail%>"><%=viewBean.getNom(session, i)%>&nbsp;</TD>
     <TD class="mtd" width="15%" onclick="<%=actionDetail%>"><%=viewBean.getEntreeVigueur(i)%>&nbsp;</TD>
     <TD class="mtd" width="30%" onclick="<%=actionDetail%>"><%=viewBean.getMotif(i)%>&nbsp;</TD>    
  
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter"  --%>  
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>