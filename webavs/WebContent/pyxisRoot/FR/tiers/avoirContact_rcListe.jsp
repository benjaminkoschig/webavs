<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<%	
	globaz.pyxis.db.tiers.TIAvoirContactListViewBean viewBean = (globaz.pyxis.db.tiers.TIAvoirContactListViewBean)request.getAttribute ("viewBean");
	//viewBean = new globaz.pyxis.db.tiers.TITiersListViewBean();
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
      <TH  width="*">Nom du contact</TH>
<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition"  --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList"  --%>
   	
		<%
		globaz.pyxis.db.tiers.TIAvoirContact lineBean = (globaz.pyxis.db.tiers.TIAvoirContact)viewBean.getEntity(i);
		// on ne peut pas aller au detail si on est en mode selection pour eviter de boucler
		if ((request.getParameter("colonneSelection")==null)||(!request.getParameter("colonneSelection").equals("yes"))) {
			actionDetail ="parent.location.href='pyxis?userAction=pyxis.tiers.avoirContact.afficher&idContact=" + lineBean.getIdContact() +"&idTiers="+lineBean.getIdTiers()+ "'";
		}
		%>

	
	      <%
	//if (!((request.getParameter("colonneSelection")!=null)&&(request.getParameter("colonneSelection").equals("yes")))) {
	%>
            <TD class="mtd" width="16" ></TD>
      <% //} %>
      <TD class="mtd" width="*" onClick="<%=actionDetail%>"><%=viewBean.getContactName(lineBean.getIdContact())%>&nbsp;</TD>

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter"  --%>


<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>