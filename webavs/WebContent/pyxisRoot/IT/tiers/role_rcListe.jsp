<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<%
    globaz.pyxis.db.tiers.TIRoleListViewBean viewBean = (globaz.pyxis.db.tiers.TIRoleListViewBean)request.getAttribute ("viewBean");
    size = viewBean.size ();
    detailLink ="pyxis?userAction=pyxis.tiers.role.afficher&selectedId=";

%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders"  --%> 
  

<Th nowrap width="16">&nbsp;</Th>
<Th width="">Ruolo</Th>
<Th width="">Inizio del ruolo</Th>
<Th width="">Fine del ruolo</Th>    

    
<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition"  --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList"  --%> 
<%
	actionDetail = "parent.location.href='"+detailLink+viewBean.getIdRole(i)+"'";
%>
     
    <td class="mtd" width="16" >
	<%String url = request.getContextPath()+"/pyxis?userAction=pyxis.tiers.role.afficher&selectedId="+viewBean.getIdRole(i);%>
	<ct:menuPopup menu="TIMenuVide" detailLabelId="Detail" detailLink="<%=url%>">
		<ct:menuParam key="selectedId" value="<%=viewBean.getIdRole(i)%>"/>
		<ct:menuParam key="userAction" value="pyxis.tiers.role.afficher"/>
	</ct:menuPopup> 
	</td>

     
     
     <TD class="mtd" onClick="<%=actionDetail%>" width="40%"><%=viewBean.getRole(i)%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width="30%"><%=viewBean.getDebutRole(i)%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width="30%"><%=viewBean.getFinRole(i)%>&nbsp;</TD>
  
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter"  --%>  
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>