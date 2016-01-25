 <%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
 <%
	globaz.pyxis.db.divers.TIGroupeDomaineListViewBean viewBean = (globaz.pyxis.db.divers.TIGroupeDomaineListViewBean)request.getAttribute ("viewBean");
	size = viewBean.size ();
	detailLink ="pyxis?userAction=pyxis.divers.groupeDomaine.afficher&selectedId=";
    %>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	<%-- tpl:put name="zoneHeaders"  --%>
		  <Th width="16">&nbsp;</Th>    
		  <Th width="45%"><ct:FWLabel key='GROUPE' /></Th>    
		  <Th width="39%"><ct:FWLabel key='DOMAINE' /></Th>    
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition"  --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList"  --%>
		<%
			globaz.pyxis.db.divers.TIGroupeDomaine line = (globaz.pyxis.db.divers.TIGroupeDomaine)viewBean.getEntity(i);
			actionDetail = targetLocation  + "='" + detailLink + line.getIdGroupeDomaine()+"'";
		%>
	  <TD valign="top" class="mtd" width="16">
		<%String url = request.getContextPath()+"/pyxis?userAction=pyxis.divers.groupeDomaine.afficher&selectedId="+line.getIdGroupeDomaine();%>
		<ct:menuPopup menu="TIMenuVide" detailLabelId="Detail" detailLink="<%=url%>">
		</ct:menuPopup> 
	  </td>
      <TD class="mtd" onClick="<%=actionDetail%>" width="45%"><%=line.getIdGroupe()%></TD>
      <TD class="mtd" onClick="<%=actionDetail%>" width="39%"><%=viewBean.getSession().getCodeLibelle(line.getCsDomaine())%></TD>     
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter"  --%>  
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>