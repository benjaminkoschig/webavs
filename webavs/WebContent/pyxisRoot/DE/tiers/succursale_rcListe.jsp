 
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>

<%
	globaz.pyxis.db.tiers.TISuccursaleListViewBean viewBean = (globaz.pyxis.db.tiers.TISuccursaleListViewBean )request.getAttribute ("viewBean");
	size = viewBean.size ();
	detailLink ="pyxis?userAction=pyxis.tiers.succursale.afficher&selectedId=";
	session.setAttribute("listViewBean",viewBean);
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders"  --%> 
    <Th nowrap width="16">&nbsp;</Th>
    <Th width="20%">Abr.-Nr.</Th>   
    <Th nowrap width="30%">Name</Th>
      
    <Th width="25%">Ort</Th>
	<Th width="25%">Periode</Th>
      
    
    
      
<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition"  --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList"  --%>
	<%
		actionDetail = "parent.location.href='"+detailLink+viewBean.getIdComposition(i)+"'";	
	%>

      <TD class="mtd" width="16" >
      
      
      	<%String url = request.getContextPath()+"/pyxis?userAction=pyxis.tiers.succursale.afficher&selectedId="+viewBean.getIdComposition(i);%>
		<ct:menuPopup menu="TIMenuVide" detailLabelId="Detail" detailLink="<%=url%>" />
      
      
      </TD>
      <TD class="mtd" onClick="<%=actionDetail%>" width="20%">
      <DIV align="center"><%=viewBean.getNumAffilie(i)%>&nbsp;</DIV>
      </TD>
      <TD class="mtd" onClick="<%=actionDetail%>" width="30%" align="left"><%=viewBean.getNom(i)%>&nbsp;</TD>
      <TD class="mtd" onClick="<%=actionDetail%>" width="25%">
      <DIV align="left"><%=viewBean.getLocaliteLong(i)%>&nbsp;</DIV>
      </TD>
      <TD class="mtd" onClick="<%=actionDetail%>" width="25%" align="left"><%=viewBean.getDateSuccursale(i)%>&nbsp;</TD>
      <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter"  --%>  
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>