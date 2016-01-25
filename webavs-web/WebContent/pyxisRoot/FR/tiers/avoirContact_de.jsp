<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%>

<%
	idEcran ="GTI0027";
	globaz.pyxis.db.tiers.TIAvoirContactViewBean viewBean = (globaz.pyxis.db.tiers.TIAvoirContactViewBean)session.getAttribute ("viewBean");
	selectedIdValue = request.getParameter("selectedId");
	bButtonNew = objSession.hasRight(userActionNew, "ADD");
    if ("add".equals(request.getParameter("_method"))) {
	    bButtonValidate = objSession.hasRight(userActionNew, "ADD");
	    bButtonCancel = objSession.hasRight(userActionNew, "ADD");
    } else {
	    bButtonValidate = objSession.hasRight(userActionNew, "UPDATE");
	    bButtonCancel   = objSession.hasRight(userActionNew, "UPDATE");
    }

    actionNew += "&idTiers="+viewBean.getIdTiers();
%>
<SCRIPT language="JavaScript">
</SCRIPT> 
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness"  --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<style>
	.navLinkTiers {
		cursor:hand;
		border : 0 0 0 0;
		color:blue;
		text-decoration:underline;
		background : #B3C4DB;
		margin : 0 0 0 0;
		padding : 0 0 0 0;
		width : 100%;
		font-weight:normal;
		font-size: 8pt
	}
</style>
<!-- hide this script from non-javascript-enabled browsers -->
<SCRIPT language="JavaScript">
top.document.title = "Tiers";

function add() {
	document.forms[0].elements('userAction').value="pyxis.tiers.avoirContact.ajouter"
}

function upd() {
}

function validate() {
//	state = validateFields();
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="pyxis.tiers.avoirContact.ajouter";
	else
		document.forms[0].elements('userAction').value="pyxis.tiers.avoirContact.modifier";
	//return (state);*/
	return true;
}

function cancel() {
  if (document.forms[0].elements('_method').value == "add") {
  	document.forms[0].elements('userAction').value="";
  	top.fr_appicons.icon_back.click();
  } else {
  	document.forms[0].elements('userAction').value="pyxis.tiers.avoirContact.afficher";
  }
}

function del() {
	if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")) {
		document.forms[0].elements('userAction').value="pyxis.tiers.avoirContact.supprimer";
		document.forms[0].submit();
	}
}

function init() {

}

function postInit(){
	var navLinks = document.getElementsByName("navLink");
	for (i=0;i<navLinks.length;i++) {
		navLinks[i].disabled = '';
	}
}

// stop hiding -->
</SCRIPT> 
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
	<%-- tpl:put name="zoneTitle" --%>Contacts d'un tiers - Détail<%-- /tpl:put  --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
	<%-- tpl:put name="zoneMain"  --%>

		<tr>
<td width="100%">

<table id="innerTable" border ="1" width="100%">
<tr>

<td width="400" valign="top">
<table>

<%if ("add".equals(request.getParameter("_method"))) {%>
		<tr><td colspan=3>
			<h3><u>Nouveau</u></h3>
		</td></tr>
	<%}%>
     <tr>
            <TD ><span style="font-family:webdings;font-size:12 pt">€</span><A href="<%=request.getContextPath()%>\pyxis?userAction=pyxis.tiers.tiers.diriger&idTiers=<%=viewBean.getIdTiers()%>">Tiers</A></TD>
            <td><INPUT name="tiers" type="text" value="<%=viewBean.getNomTiers()%>" tabindex="-1" class="libelleLongDisabled" readonly></td>
      </tr>
		<TR >
			<TD colspan="3">&nbsp;</TD>
		</TR>
		<TR>
			<TD width="30">Contact</TD>
			<TD width="40"><INPUT class="libelleLongDisabled" tabindex="-1" type="text" value="<%=viewBean.getNomContact() + " " + viewBean.getPrenomContact()%>" readonly></TD>
			<TD width="5" style="text-align:left">
			<%
				Object[] contactMethodsName = new Object[]{
					new String[]{"setIdContact","getIdContact"},
					new String[]{"setNomContact","getNom"},
					new String[]{"setPrenomContact","getPrenom"}
				};
				Object[] contactParams = new Object[]{new String[]{"localiteCode","_pos"} };
			%>
				<ct:FWSelectorTag
					name="localiteSelector"

					methods="<%=contactMethodsName %>"
					providerApplication ="pyxis"
					providerPrefix="TI"
					providerAction ="pyxis.tiers.contact.chercher"
					providerActionParams ="<%=contactParams%>"
				/>
			</TD>
			<TD width="1000">&nbsp;<INPUT type="hidden" name="idContact" value="<%=viewBean.getIdContact()%>"></TD>
		</TR>

	<tr>
		<td colspan=2>
	<%
		java.util.Vector listIdExterne = viewBean.getIdExterneList();
		if (listIdExterne.size()>1) { %>
			<br><ct:FWLabel key='NUMERO' /><br>
			<ct:FWListSelectTag name="idExterne" data="<%=listIdExterne%>" defaut="<%=viewBean.getIdExterne()%>"/>
	<% } else { %>
	   		<INPUT type="hidden" name="idTiidExterneers" value='<%=viewBean.getIdExterne()%>'>
	<% } %>
		</td>
	</tr>

			</table>

		</td>

		<td width="*">
			<iframe scrolling="YES"  style="border : solid 1px black; width:100%;" height="100%" src="<%=request.getContextPath()%>/pyxis?userAction=pyxis.tiers.tiers.contactsTiers&idTiers=<%=viewBean.getIdTiers()%>">
			</iframe>
			<input type="hidden" name="idTiers" value="<%=viewBean.getIdTiers()%>">

		</td>
		</tr>
		</table>
<%
globaz.pyxis.application.TIApplication app = (globaz.pyxis.application.TIApplication)globaz.globall.db.GlobazServer.getCurrentSystem().getApplication("PYXIS");
if (!app.hiddeNavigationBar()) { %>
	<table cellpadding=0 cellspacing=0 width="100%">
		<tr>
			<td>
				<ct:ifhasright crud="r" element="pyxis.tiers.tiers">
					<input name="navLink"  class="navLinkTiers" value="1 Recherche" accesskey="1" type="button"
					onclick="location.href='<%=request.getContextPath()%>\\pyxis?userAction=pyxis.tiers.tiers.chercher'">
				</ct:ifhasright>
			</td>

			<td>
				<ct:ifhasright crud="r" element="pyxis.tiers.tiers">
					<input name="navLink" class="navLinkTiers"  value="2 Tiers" accesskey="2" type="button"
					onclick="location.href='<%=request.getContextPath()%>\\pyxis?userAction=pyxis.tiers.tiers.diriger&idTiers=<%=viewBean.getIdTiers()%>'">
				</ct:ifhasright>
			</td>

			<td>
				<ct:ifhasright crud="r" element="pyxis.adressecourrier.avoirAdresse">
					<input name="navLink" class="navLinkTiers"  value="3 Adresses de courrier" accesskey="3" type="button"
					onclick="location.href='<%=request.getContextPath()%>\\pyxis?userAction=pyxis.adressecourrier.avoirAdresse.afficher&_method=add&back=_sl&idTiers=<%=viewBean.getIdTiers()%>'">
				</ct:ifhasright>
			</td>

			<td>
				<ct:ifhasright crud="r" element="pyxis.adressepaiement.avoirPaiement">
					<input name="navLink" class="navLinkTiers"  value="4 Adresses de paiement" accesskey="4" type="button"
	 		 		onclick="location.href='<%=request.getContextPath()%>\\pyxis?userAction=pyxis.adressepaiement.avoirPaiement.afficher&_method=add&back=_sl&idTiers=<%=viewBean.getIdTiers()%>'">
 		 		</ct:ifhasright>
 		 	</td>

			<td>
				<ct:ifhasright crud="r" element="pyxis.tiers.avoirContact">
					<input name="navLink" class="navLinkTiers"  value="5 Contacts" accesskey="5" type="button"
	 		 		onclick="location.href='<%=request.getContextPath()%>\\pyxis?userAction=pyxis.tiers.avoirContact.afficher&_method=add&idTiers=<%=viewBean.getIdTiers()%>'">
		 			</ct:ifhasright>
		 		</td>

			<td>
				<ct:ifhasright crud="r" element="naos.affiliation.affiliation">
					<input name="navLink" class="navLinkTiers"  value="6 Affiliation" accesskey="6" type="button"
	 		 		onclick="location.href='<%=request.getContextPath()%>\\naos?userAction=naos.affiliation.affiliation.chercher&idTiers=<%=viewBean.getIdTiers()%>'">
 		 		</ct:ifhasright>
 		 	</td>

			<td>
				<ct:ifhasright crud="r" element="pyxis.tiers.compositionTiers">
					<input name="navLink" class="navLinkTiers"  value="7 Liens entre tiers" accesskey="7" type="button"
	 		 		onclick="location.href='<%=request.getContextPath()%>\\pyxis?userAction=pyxis.tiers.compositionTiers.chercher&idTiers=<%=viewBean.getIdTiers()%>'">
 		 		</ct:ifhasright>
 		 	</td>

			<td>
				<ct:ifhasright crud="u" element="pyxis.tiers.compositionTiers">
					<input name="navLink" class="navLinkTiers"  value="8 Conjoint" accesskey="8" type="button"
	 		 		onclick="location.href='<%=request.getContextPath()%>\\pyxis?userAction=pyxis.tiers.compositionTiers.dirigerConjoint&selectedId=<%=viewBean.getIdTiers()%>'">
 		 		</ct:ifhasright>
 		 	</td>
		</tr>
	</table>
<%}%>
		</td>
		</tr>

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

<script>
	document.getElementById('innerTable').style.setExpression("height","document.body.clientHeight-150");
	document.getElementsByTagName('table')[0].style.setExpression("height","document.body.clientHeight-35");
</script>

<% if (!"add".equals(request.getParameter("_method"))) { %>
	<ct:menuChange displayId="options" menuId="avoirContact-detail" showTab="options" checkAdd="no">
		<ct:menuSetAllParams key="idContact" value="<%=viewBean.getIdContact()%>"/>
		<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdContact()%>"/>
	</ct:menuChange>
<% } else { %>
	<ct:menuChange displayId="options" menuId="tiers-detail" showTab="options" checkAdd="no">
		<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdTiers()%>" checkAdd="no"/>
		<ct:menuSetAllParams key="idTiers" value="<%=viewBean.getIdTiers()%>" checkAdd="no"/>
	</ct:menuChange>
<% } %>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>