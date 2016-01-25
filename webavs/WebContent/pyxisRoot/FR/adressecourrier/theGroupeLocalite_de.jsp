<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
	idEcran ="GTI0036";
	globaz.pyxis.vb.adressecourrier.TITheGroupeLocaliteViewBean viewBean = (globaz.pyxis.vb.adressecourrier.TITheGroupeLocaliteViewBean) session.getAttribute("viewBean");
	selectedIdValue = request.getParameter("selectedId");
%>
<SCRIPT language="JavaScript">
</SCRIPT> 
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness"  --%>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<script language="JavaScript">
top.document.title = "Adresse de courrier - Groupe localités"
function add() {
	document.forms[0].elements('userAction').value="pyxis.adressecourrier.theGroupeLocalite.ajouter"
}
function upd() {
}
function validate() {
	state = validateFields(); 
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="pyxis.adressecourrier.theGroupeLocalite.ajouter";
    else
	document.forms[0].elements('userAction').value="pyxis.adressecourrier.theGroupeLocalite.modifier";
	return (state);
}
function cancel() {
 if (document.forms[0].elements('_method').value == "add")
  document.forms[0].elements('userAction').value="back";
 else
  document.forms[0].elements('userAction').value="pyxis.adressecourrier.theGroupeLocalite.afficher"
}

function del() {
	if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?"))
	{
		document.forms[0].elements('userAction').value="pyxis.adressecourrier.theGroupeLocalite.supprimer";
		document.forms[0].submit();
	}
}

function init(){
}
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Détail d'un groupe de localités
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%>
							<tr>
								<td>Type</td>
								<td>
									<ct:select name="csType" defaultValue="" styleClass="libelleLong">
										<ct:optionsCodesSystems csFamille="PYTYPLOCA"/>
									</ct:select>
								</td>
								<td>Nom allemand</td>
								<td><ct:inputText name="nomDe" styleClass="libelleLong"/></td>
							</tr>
							<tr>
								<td colspan="2">&nbsp;</td>
								<td>Nom français</td>
								<td><ct:inputText name="nomFr" styleClass="libelleLong"/></td>
							</tr>
							<tr>
								<td colspan="2">&nbsp;</td>
								<td>Nom italien</td>
								<td><ct:inputText name="nomIt" styleClass="libelleLong"/></td>
							</tr>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage"  --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <SCRIPT>
		</SCRIPT> <%	} 
%> 
<ct:menuChange displayId="options" menuId="groupeLocalite" showTab="options">
			<ct:menuSetAllParams key="selectedId" value="<%=selectedIdValue%>"/>
</ct:menuChange>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>