<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="CCP0023";
	globaz.phenix.db.principale.CPSortieMontantViewBean viewBean = (globaz.phenix.db.principale.CPSortieMontantViewBean)session.getAttribute ("viewBean");
	selectedIdValue = request.getParameter("selectedId");

//bButtonDelete = true;
%>
<SCRIPT language="JavaScript">
</SCRIPT> <%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/ValidationGroups.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/CodeSystemPopup.js"></SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CP-MenuPrincipal" showTab="menu"/>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
top.document.title = "Cotisation - Montants des sorties"
function add() {
	document.forms[0].elements('userAction').value="phenix.principale.sortieMontant.ajouter"
}
function upd() {
}
function validate() {
	state = validateFields(); 

	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="phenix.principale.sortieMontant.ajouter";
	else
		document.forms[0].elements('userAction').value="phenix.principale.sortieMontant.modifier";
	return (state);
}
function cancel() {
	document.forms[0].elements('userAction').value="phenix.principale.sortieMontant.chercher";
	<%--document.forms[0].elements('selectedId').value="<%=viewBean.getIdSortie()%>";--%>
}
function del() {
	if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?"))
	{	
		if (window.confirm("N'oubliez pas de supprimer l'affact dans la facturation si le passage est en traitement!"))
		{
			document.forms[0].elements('userAction').value="phenix.principale.sortieMontant.supprimer";
			document.forms[0].submit();
		}
	}
}
function init(){}
/*
*/
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Montant d'une sortie<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
					
					<TR>
						<TD nowrap width="180">&nbsp;</TD>
						<TD nowrap></TD>
						<TD nowrap>
						<INPUT name="idSortie" type="hidden" 
							value="<%=viewBean.getIdSortie()%>">
						</TD>
					</TR>
					<TR>
						<TD nowrap width="180">Montant</TD>
						<TD nowrap colspan="2">
							<INPUT type="text" name="montant" maxlength="18" size="25"
								value="<%=viewBean.getMontant()%>" class="libelleLong">
						</TD>
						<TD nowrap></TD>
					</TR>
					<TR> 
			            <TD nowrap width="180">Assurance</TD>
            			<TD nowrap colspan="2">
						<ct:FWListSelectTag name="assurance"
            				defaut="<%=viewBean.getAssurance()%>"
            				data="<%=globaz.naos.db.cotisation.AFCotisation.getListAssuranceAffiliation(session, viewBean.getIdAffiliation())%>"/>
            			</TD>
            			<TD nowrap></TD>
			        </TR>					
					<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <SCRIPT>
		</SCRIPT> <%	} 
%>
<script>
// menu 

//top.fr_menu.location.replace('appMenu.jsp?_optionMenu=tiers-banque&id=<%=request.getParameter("selectedId")%>&changeTab=Options');	
</script>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>