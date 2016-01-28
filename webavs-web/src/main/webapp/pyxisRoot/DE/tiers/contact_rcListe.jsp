<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<%	
	globaz.pyxis.db.tiers.TIContactListViewBean viewBean = (globaz.pyxis.db.tiers.TIContactListViewBean)request.getAttribute ("viewBean");
	//viewBean = new globaz.pyxis.db.tiers.TIContactListViewBean();
	size = viewBean.getSize ();
	session.setAttribute("listViewBean",viewBean);
	menuName="tiers-detail";
	detailLink="pyxis?userAction=pyxis.tiers.avoirContact.afficher&selectedId=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders"  --%>
   
     <%
		if (!((request.getParameter("colonneSelection")!=null)&&(request.getParameter("colonneSelection").equals("yes")))) {
     %>
           <th width="16">&nbsp;</th>
	<%}%>
      <TH>Name des Kontakts</TH>
      <TH>Prénom du contact</TH>
      <TH  width="*">Am Partner vereinigt</TH>
      
<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition"  --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList"  --%>
   	
		<%
		globaz.pyxis.db.tiers.TIContactViewBean lineBean = (globaz.pyxis.db.tiers.TIContactViewBean)viewBean.getEntity(i);
		actionDetail ="parent.location.href='pyxis?userAction=pyxis.tiers.contact.afficher&selectedId="+lineBean.getIdContact()+"'";

		%>

	
	      <%
	if (!((request.getParameter("colonneSelection")!=null)&&("yes".equals(request.getParameter("colonneSelection"))))) {
	%>
            <TD class="mtd" width="16" ></TD>
      <% } %>
      <TD class="mtd"  onClick="<%=actionDetail%>"><%=lineBean.getNom()%>&nbsp;</TD>
      <TD class="mtd"  onClick="<%=actionDetail%>"><%=lineBean.getPrenom()%>&nbsp;</TD>
	  <TD class="mtd" width="*" onClick="<%=actionDetail%>"><%=lineBean.getDescription()%>&nbsp;</TD>

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter"  --%>


<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>