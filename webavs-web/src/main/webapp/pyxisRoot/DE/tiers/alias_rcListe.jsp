<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<%
    globaz.pyxis.db.tiers.TIAliasListViewBean viewBean = (globaz.pyxis.db.tiers.TIAliasListViewBean )request.getAttribute ("viewBean");
    size = viewBean.size ();
    detailLink ="pyxis?userAction=pyxis.tiers.alias.afficher&selectedId=";

%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders"  --%> 
  


<!-- <Th width="">Identifiant</Th> -->
      <Th nowrap width="16">&nbsp;</Th>
      <TH width="100%" align="left">Alias</TH>
    
    
<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition"  --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList"  --%>

<%
	actionDetail = "parent.location.href='"+detailLink+viewBean.getIdAlias(i)+"'";

%>

<!--      <TD class="mtd" width="40%" align="right"><%=viewBean.getIdAlias(i)%></TD> -->
      
       
       
    <td class="mtd" width="16" >
	<%String url = request.getContextPath()+"/pyxis?userAction=pyxis.tiers.alias.afficher&selectedId="+viewBean.getIdAlias(i);%>
	<ct:menuPopup menu="TIMenuVide" detailLabelId="Detail" detailLink="<%=url%>">
	</ct:menuPopup> 
	</td>
      
	<TD class="mtd" onClick="<%=actionDetail%>" width="30%"><%=viewBean.getAlias(i)%>&nbsp;</TD>
   
  
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter"  --%>  
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>