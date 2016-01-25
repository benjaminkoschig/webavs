<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>

<script>
// Custom
function init() {
		jsdetailLink ="pyxis?userAction=pyxis.tiers.moyenCommunication.afficher&_method=add&idContact=<%=request.getParameter("forIdContact")%>"; 
		if (parent.fr_detail.location.href == "about:blank") {
			parent.fr_detail.location.href = jsdetailLink;
		}
}
</script>

<%	

	globaz.pyxis.db.tiers.TIMoyenCommunicationListViewBean viewBean = (globaz.pyxis.db.tiers.TIMoyenCommunicationListViewBean)request.getAttribute ("viewBean");
	//viewBean = new globaz.pyxis.db.tiers.TITiersListViewBean();
	size = viewBean.getSize ();
	session.setAttribute("listViewBean",viewBean);
	

//	menuName="tiers-detail";
	detailLink="pyxis?userAction=pyxis.tiers.moyenCommunication.afficher&idContact=";
	target="parent.fr_detail";
	targetLocation = target + ".location.href";

%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders"  --%>
   
	
     <%
		if (!((request.getParameter("colonneSelection")!=null)&&(request.getParameter("colonneSelection").equals("yes")))) {
     %>
           <TH width="16">&nbsp;</TH>
	<%}%>
      <TH  width="150">Type</TH>
      <TH  width="400">Valeur</TH>
      <TH  width="*">Domaine d'application</TH>
<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition"  --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList"  --%>
   	
		<%
		globaz.pyxis.db.tiers.TIMoyenCommunication lineBean = (globaz.pyxis.db.tiers.TIMoyenCommunication)viewBean.getEntity(i);
		actionDetail = targetLocation + "='" + detailLink +
			lineBean.getIdContact()+
			"&typeCommunication="+lineBean.getTypeCommunication()+
			"&idApplication="+lineBean.getIdApplication()+
			"'";		
		%>

	
	      <%
	//if (!((request.getParameter("colonneSelection")!=null)&&(request.getParameter("colonneSelection").equals("yes")))) {
	%>
            <TD class="mtd" width="16" ></TD>
      <% //} %>
      <TD class="mtd" width="*" onClick="<%=actionDetail%>"><%=objSession.getCodeLibelle(lineBean.getTypeCommunication())%>&nbsp;</TD>
      <TD class="mtd" width="*" onClick="<%=actionDetail%>"><%=lineBean.getMoyen()%>&nbsp;</TD>
      <TD class="mtd" width="*" onClick="<%=actionDetail%>"><%=objSession.getCodeLibelle(lineBean.getIdApplication())%>&nbsp;</TD>

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter"  --%>


<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>