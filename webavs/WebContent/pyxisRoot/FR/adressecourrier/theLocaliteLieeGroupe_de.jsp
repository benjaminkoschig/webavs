<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
	idEcran ="GTI0038";
	globaz.pyxis.vb.adressecourrier.TITheLocaliteLieeGroupeViewBean viewBean = (globaz.pyxis.vb.adressecourrier.TITheLocaliteLieeGroupeViewBean) session.getAttribute("viewBean");
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
function add() {
	document.forms[0].elements('userAction').value="pyxis.adressecourrier.theLocaliteLieeGroupe.ajouter"
}
function upd() {
}
function validate() {
	state = validateFields(); 
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="pyxis.adressecourrier.theLocaliteLieeGroupe.ajouter";
    else
	document.forms[0].elements('userAction').value="pyxis.adressecourrier.theLocaliteLieeGroupe.modifier";
	return (state);
}
function cancel() {
 if (document.forms[0].elements('_method').value == "add")
  document.forms[0].elements('userAction').value="pyxis.adressecourrier.theLocaliteLieeGroupe.chercher";
 else
  document.forms[0].elements('userAction').value="pyxis.adressecourrier.theLocaliteLieeGroupe.afficher"
}

function del() {
	if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?"))
	{
		document.forms[0].elements('userAction').value="pyxis.adressecourrier.theLocaliteLieeGroupe.supprimer";
		document.forms[0].submit();
	}
}

function init(){
}
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Détail localité liée au groupe
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%>
							<tr>
								<td>Localite</td>
								<td>
									<ct:inputText name="numPostal" style="width: 62px;"/>
									<ct:inputHidden name="idGroupeLocalite"/>
									<ct:inputHidden name="idLocalite"/>
							<%
								Object[] localiteMethodsNames =	new Object[] { 
									new String[] { "idLocalite", "idLocalite" }, 
									new String[] { "localite", "localite" },
									new String[] { "numPostal", "numPostalEntier" },
									new String[] { "paysDe", "libellePaysAl" },
									new String[] { "paysIt", "libellePaysIt" },
									new String[] { "paysFr", "libellePaysFr" }
								};
								Object[] localiteParams = new Object[] { 
									new String[] { "numPostal", "_pos" }, 
								};%> 
						<ct:FWSelectorTag name="localiteSelected"
							 methods="<%=localiteMethodsNames%>"
							providerApplication="pyxis" providerPrefix="TI"
							providerAction="pyxis.adressecourrier.localite.chercher"
							providerActionParams="<%=localiteParams%>"/> &nbsp;
						<ct:inputText name="localite" readonly="readonly" styleClass="libelleLongDisabled"/>
							</tr>
							<tr>
								<td>Pays</td>
								<td style="padding-left: 110px;"><ct:inputText name="pays" readonly="readonly" styleClass="libelleLongDisabled"/></td>
							</tr>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage"  --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <SCRIPT>
		</SCRIPT> <%	} 
%> 

<ct:menuChange displayId="options" menuId="TIMenuVide" showTab="menu">
</ct:menuChange>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>