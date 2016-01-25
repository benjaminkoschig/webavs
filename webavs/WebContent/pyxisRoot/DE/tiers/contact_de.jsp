<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%>
<%
	idEcran ="GTI0029";
	globaz.pyxis.db.tiers.TIContactViewBean viewBean = (globaz.pyxis.db.tiers.TIContactViewBean)session.getAttribute ("viewBean");
	//selectedIdValue = request.getParameter("selectedId");
	
%>
<SCRIPT language="JavaScript">
</SCRIPT> <%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness"  --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%><SCRIPT language="JavaScript">


<!--hide this script from non-javascript-enabled browsers -->
top.document.title = "Tiers - Kontakt Détail"
function add() {

	document.forms[0].elements('userAction').value="pyxis.tiers.contact.ajouter"
	
}
function upd() {
}
function validate() {

/*
	state = validateFields(); 
*/
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="pyxis.tiers.contact.ajouter";
	else
		document.forms[0].elements('userAction').value="pyxis.tiers.contact.modifier";
	//return (state);
	return true;
}
function cancel() {
	  document.forms[0].elements('userAction').value="pyxis.tiers.contact.chercher";
}
function del() {	if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?"))
	{
		document.forms[0].elements('userAction').value="pyxis.tiers.contact.supprimer";
		document.forms[0].submit();
	}}
function init(){}
/*
*/
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Kontakt - Détail<%-- /tpl:put  --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%> 

		<TR>
			<TD colspan="3">&nbsp;</TD>
		</TR>
		<TR>
			<TD>Kontakt (Nom / Prénom)</TD>
			<TD>
				<INPUT type="hidden" name="colonneSelection" value="<%=request.getParameter("colonneSelection")%>">
				<INPUT maxlength="40" size="40" name="nom" type="text" value="<%=viewBean.getNom()%>">
				&nbsp;
				<INPUT maxlength="40" size="40" name="prenom" type="text" value="<%=viewBean.getPrenom()%>">
			</TD>
		</TR>


<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage"  --%>
<%if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> 
<SCRIPT>
</SCRIPT> 
<%}%>

<ct:menuChange displayId="options" menuId="TIMenuVide" showTab="menu">
</ct:menuChange>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>