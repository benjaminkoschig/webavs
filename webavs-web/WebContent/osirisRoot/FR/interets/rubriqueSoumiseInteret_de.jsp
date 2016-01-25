
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java"  import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA4033"; %>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%@ page import="globaz.osiris.db.interets.*" %>
<%
CARubriqueSoumiseInteretViewBean viewBean = (CARubriqueSoumiseInteretViewBean)session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
String idRubrique = viewBean.getIdRubrique();
if (idRubrique == null || idRubrique.length() == 0) {
	idRubrique = (String) session.getAttribute("idRubrique");
	viewBean.setIdRubrique(idRubrique);
}
String jspLocation =  servletContext + mainServletPath + "Root/rubrique_select.jsp";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
function add() {
  document.forms[0].elements('userAction').value="osiris.interets.rubriqueSoumiseInteret.ajouter"
}

function upd() {
  document.forms[0].elements('userAction').value="osiris.interets.rubriqueSoumiseInteret.modifier"
	document.forms[0].idTypeSection.disabled = true;
}

function del() {
	if (window.confirm("<ct:FWLabel key='GCA4033_MESSAGE_SUPPRESSION'/>")) {
        document.forms[0].elements('userAction').value="osiris.interets.rubriqueSoumiseInteret.supprimer";
        document.forms[0].submit();
    }

}
function rubriqueManuelleOn(){
	document.forms[0].idRubrique.value="";
	//document.forms[0].idExterneRubriqueEcran.value="";
	document.forms[0].rubriqueDescription.value="";
}
function updateRubrique(el) {
	if (el == null || el.value== "" || el.options[el.selectedIndex] == null)
		rubriqueManuelleOn();
	else {
		var elementSelected = el.options[el.selectedIndex];
		document.forms[0].idRubrique.value = elementSelected.idRubrique;
		document.forms[0].idExterneRubriqueEcran.value = elementSelected.idExterneRubriqueEcran;
		document.forms[0].rubriqueDescription.value = elementSelected.rubriqueDescription;
	}
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="osiris.interets.rubriqueSoumiseInteret.ajouter";
    else
        document.forms[0].elements('userAction').value="osiris.interets.rubriqueSoumiseInteret.modifier";

    return state;

}
function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else{
		document.forms[0].elements('userAction').value="osiris.interets.rubriqueSoumiseInteret.afficher";
	}
}
function init(){}

top.document.title = "<ct:FWLabel key='GCA4033_TITRE_ECRAN'/> - " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key='GCA4033_TITRE_ECRAN'/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

           <TR>
            <TD nowrap width="174">
              <p><ct:FWLabel key='GCA4033_PLAN_D_INTERET'/></p>
            </TD>
            <TD width="10">&nbsp;</TD>
            <TD nowrap width="393">
              <input type="text" name="" size="40" maxlength="40" value="<% if (viewBean.getPlanCalculInteret() != null) { %><%=viewBean.getIdPlanCalculInteret()%> - <%=viewBean.getPlanCalculInteret().getLibelle() %><%}%>" tabindex="-1" class="libelleLongDisabled" readonly >
              <input type="hidden" name="idPlanCalculInteret" value="<% if (viewBean.getPlanCalculInteret() != null) { %><%=viewBean.getIdPlanCalculInteret()%><%}%>" >
            </TD>
            <TD width="118">&nbsp;</TD>
          </TR>

          <TR>

           <td nowrap width="174" height="43" colspan="2"><ct:FWLabel key='GCA4033_RUBRIQUE'/></td>
							<td><ct:FWPopupList
							onFailure="rubriqueManuelleOn();"
							onChange="updateRubrique(tag.select);" value="<%=viewBean.getNumeroRubrique()%>"
							name="numeroRubrique" size="15" className="visible" jspName="<%=jspLocation%>"
							minNbrDigit="3" autoNbrDigit="11" />&nbsp;
							<input type="hidden" name="idRubrique" value="<%=viewBean.getIdRubrique()%>">
							<input type="text" name="" value="<%if (viewBean.getRubrique() != null) { %><%=viewBean.getRubrique().getDescription()%><% } %>" tabindex="-1" class="libelleLongDisabled" readonly>
							</td>
							<td>&nbsp;</td>
          <TR>


          <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<%	} %>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>