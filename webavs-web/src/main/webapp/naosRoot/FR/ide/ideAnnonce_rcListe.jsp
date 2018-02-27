<%-- tpl:insert page="/theme/list.jtpl" --%><%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.naos.db.ide.AFIdeAnnonceListViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	detailLink = "naos?userAction=naos.ide.ideAnnonce.afficher&selectedId=";
	AFIdeAnnonceListViewBean viewBean = (AFIdeAnnonceListViewBean)request.getAttribute ("viewBean");
	session.setAttribute("viewBean", viewBean);
	size = viewBean.getSize ();
%>
<SCRIPT language="JavaScript">
var liste = new Array();
var compteur = 0;
$('#btnDel', window.parent.document).get(0).disabled=true;

function selectAll() {	
	$('input[name=checkdelete]').attr('checked', $('#checkAll').is(':checked'));
	onChange();
}
	
function onChange() {	
	 if( $('[name=checkdelete]:checked').length == 0) {
		 $('#btnDel', window.parent.document).get(0).disabled=true;
	 } else {
		 $('#btnDel', window.parent.document).get(0).disabled=false;
	 };
}
	

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
<%-- tpl:put name="zoneHeaders" --%>

	<TH>&nbsp;<INPUT id="checkAll" type="checkbox" onclick="selectAll();"></TH>
	<TH><ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_RC_LIST_NUMERO_IDE"/></TH>
	<TH><ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_STATUT_IDE"/></TH>
	<TH><ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_NUMERO_AFFILIE"/></TH>
	<TH><ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_RAISON_SOCIALE"/></TH>
	<TH><ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_DATE_CREATION"/></TH>
	<TH><ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_CATEGORIE"/></TH>
	<TH><ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_TYPE"/></TH>
	<TH><ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_ETAT"/></TH>
	<TH><ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_DATE_TRAITEMENT"/></TH>
	
<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
<%-- tpl:put name="zoneCondition" --%><%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
<%-- tpl:put name="zoneList" --%>

	<%    
		actionDetail = targetLocation + "='" + detailLink + viewBean.getIdAnnonce(i)+"'";
	%>
	
	<TD class="mtd" style="text-align:center;">
	<%if (viewBean.isErreur(i)) {%>
		<INPUT name="checkdelete" type="checkbox" value="<%=viewBean.getIdAnnonce(i)%>" onclick="onChange()">
	<% } else {%>
		&nbsp;
	<% } %>
	</TD>
	<TD class="mtd" onClick="<%=actionDetail%>"><%=viewBean.getNumeroIde(i)%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>"><%=viewBean.getStatutIde(i)%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>"><%=viewBean.getNumeroAffilie(i)%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>"><%=viewBean.getRaisonSociale(i)%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>"><%=viewBean.getDateCreation(i)%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>"><%=viewBean.getCategorie(i)%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>"><%=viewBean.getType(i)%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>"><%=viewBean.getEtat(i)%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>"><%=viewBean.getDateTraitement(i)%></TD>
	

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:put name="zoneTableFooter" --%><%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
<%-- /tpl:insert --%>