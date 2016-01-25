<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
	idEcran ="GTI0040";
	globaz.pyxis.vb.adressecourrier.TITheAvoirGroupeLocaliteViewBean viewBean = (globaz.pyxis.vb.adressecourrier.TITheAvoirGroupeLocaliteViewBean) session.getAttribute("viewBean");
	selectedIdValue = request.getParameter("selectedId");
	String nomTiers ="";
	String idTiers = "";
	globaz.pyxis.db.tiers.TITiers tiers = (globaz.pyxis.db.tiers.TITiers) request.getAttribute("tiers");
	if(tiers==null){
		nomTiers = viewBean.getNomTiers();
		idTiers = viewBean.getIdTiers();
	} else{
		nomTiers = tiers.getNom();
		idTiers = tiers.getIdTiers();
	}
%>
<SCRIPT language="JavaScript">
</SCRIPT> 
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness"  --%>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<script language="JavaScript">
//top.document.title = "Adresse de courrier - Groupe localités"
function add() {
	document.forms[0].elements('userAction').value="pyxis.adressecourrier.theAvoirGroupeLocalite.ajouter"
}
function upd() {
}
function validate() {
	state = validateFields(); 
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="pyxis.adressecourrier.theAvoirGroupeLocalite.ajouter";
    else
	document.forms[0].elements('userAction').value="pyxis.adressecourrier.theAvoirGroupeLocalite.modifier";
	return (state);
}
function cancel() {
 if (document.forms[0].elements('_method').value == "add")
  document.forms[0].elements('userAction').value="back";
 else
  document.forms[0].elements('userAction').value="pyxis.adressecourrier.theAvoirGroupeLocalite.afficher"
}

function del() {
	if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?"))
	{
		document.forms[0].elements('userAction').value="pyxis.adressecourrier.theAvoirGroupeLocalite.supprimer";
		document.forms[0].submit();
	}
}

function init(){
}
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Groupes de localités d'un tiers
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%>
							<tr>
								<td><span style="font-family:webdings;font-size:12 pt">€</span>&nbsp;<A href="<%=request.getContextPath()%>\pyxis?userAction=pyxis.tiers.tiers.diriger&idTiers=<%=idTiers%>">Partner</A></td>
								<td colspan="3">
				                     <INPUT type="text" name="nomTiers" tabindex="-1" value="<%=nomTiers%>" class="libelleLongDisabled" readonly>
								</td>
							</tr>
							<tr>
								<td>Ortschaftsgruppe</td>
								<td colspan="3">
									<ct:inputHidden name="idTiers" defaultValue="<%=idTiers%>"/>
									<ct:inputHidden name="idAvoirGroupeLocalite"/>
									<ct:inputHidden name="idGroupeLocalite"/>
									<%
										Object[] groupeLocMethodsNames =	new Object[] { 
											new String[] { "idGroupeLocalite", "id" }, 
//											new String[] { "csType", "csType" },
											new String[] { "type", "csType" },
											new String[] { "nomFr", "nomFr" },
											new String[] { "nomDe", "nomDe" },
											new String[] { "nomIt", "nomIt" },
										};
										Object[] localiteParams = new Object[] { 
//											new String[] { "numPostal", "_pos" }, 
										};%> 
								<ct:inputText name="type"  styleClass="libelleLongDisabled" readonly="readonly"/>
								<ct:inputText name="nom" styleClass="libelleLongDisabled" readonly="readonly"/>
								<ct:FWSelectorTag name="groupeLocaliteSelected"
									 methods="<%=groupeLocMethodsNames%>"
									providerApplication="pyxis" providerPrefix="TI"
									providerAction="pyxis.adressecourrier.theGroupeLocalite.chercher"
									providerActionParams="<%=localiteParams%>"/> &nbsp;
								</td>
							</tr>
							<tr>
								<td>Typ</td>
								<td colspan="3">
									<ct:select name="csType" wantBlank="false" styleClass="libelleLong">
										<ct:optionsCodesSystems csFamille="PYTIELOCA"/>
									</ct:select>
								</td>
							</tr>
							<tr>
								<td>Beginndatum</td>
								<td><ct:FWCalendarTag name="startDate" value="<%=viewBean.getStartDate()%>"/></td>
								<td>Enddatum</td>
								<td><ct:FWCalendarTag name="endDate" value="<%=viewBean.getEndDate()%>"/></td>
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