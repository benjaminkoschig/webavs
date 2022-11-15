<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%>
<%
    bButtonDelete = false;
	idEcran ="GTI0061";
	globaz.pyxis.db.tiers.TIReferencePaiementViewBean viewBean = (globaz.pyxis.db.tiers.TIReferencePaiementViewBean)session.getAttribute ("viewBean");
	selectedIdValue = request.getParameter("selectedId");
    if (JadeStringUtil.isBlank(selectedIdValue)) {
        viewBean.setIdTiers((request.getParameter("forIdTiers")!=null)?request.getParameter("forIdTiers"):"");
        viewBean.setIdAdressePaiement((request.getParameter("forIdAdressePaiement")!=null)?request.getParameter("forIdAdressePaiement"):"");
    }
%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<SCRIPT language="JavaScript">
</SCRIPT> <%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness"  --%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/CodeSystemPopup.js"></SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%><SCRIPT language="JavaScript">

<!--hide this script from non-javascript-enabled browsers -->
top.document.title = "Référence paiement Détail"

function checkModification() {

    var result = true;

    return result;
}

function add() {
	document.forms[0].elements('userAction').value="pyxis.tiers.referencePaiement.ajouter"
}
function upd() {
}
function validate() {

	state = validateFields();

	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="pyxis.tiers.referencePaiement.ajouter";
	else {
		document.forms[0].elements('userAction').value="pyxis.tiers.referencePaiement.modifier";
		state = checkModification();

	}
	return (state);
}
function cancel() {
 if (document.forms[0].elements('_method').value == "add")
  document.forms[0].elements('userAction').value="back";
 else
  document.forms[0].elements('userAction').value="pyxis.tiers.referencePaiement.afficher";
}
function del() {
	if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?"))
	{
		document.forms[0].elements('userAction').value="pyxis.tiers.referencePaiement.supprimer";
		document.forms[0].submit();
	}
}
function init(){}

// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Riferimento pagamento - Dettaglio<%-- /tpl:put  --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%>

          <TR>
            <TD width="119"></TD>
            <TD nowrap width="211"></TD>
            <TD width="50"></TD>
          </TR>

          <TR>
            <TD width="119">Etichetta</TD>
            <TD nowrap width="211">
              <INPUT type="text" name="libelle" class="libelleLong" value="<%=viewBean.getLibelle()%>" maxlength="50"></TD>
            <TD width="50"></TD>

            <TD width="180">Konto-Nr.</TD>
            <TD nowrap width="125" align="left">
                <INPUT type="text" name="numCompteBancaire" readonly class="libelleLongDisabled" value="<%=viewBean.getDetailNumCompteBancaire()%>"></TD>
          </TR>

          <TR>
            <TD width="119">Riferimento QR</TD>
            <TD nowrap width="211">
              <INPUT type="text" name="referenceQR" class="libelleLong" value="<%=viewBean.getReferenceQR()%>" maxlength="27"></TD>
            <TD width="50"></TD>
            <TD width="180"></TD>
            <TD nowrap width="125" align="left"></TD>
          </TR>

          <TR>
            <TD width="119">Data d'inizio</TD>
            <TD nowrap width="211">
                <ct:FWCalendarTag name="dateDebut" doClientValidation="CALENDAR" value="<%=viewBean.getDateDebut()%>"/></TD>
            <TD width="50" nowrap></TD>
            <TD width="180"></TD>
            <TD nowrap width="125"></TD>
          </TR>

          <TR>
            <TD width="119">Data di fine</TD>
            <TD nowrap width="211">
                <ct:FWCalendarTag name="dateFin" doClientValidation="CALENDAR" value="<%=viewBean.getDateFin()%>"/></TD>
            <TD width="50"></TD>
            <TD width="180"></TD>
            <TD nowrap width="125"></TD>
          </TR>

          <TR>
            <TD width="119">Indirizzo di pagamento</TD>
            <TD nowrap width="211" style="align-content: center">
                <label>
                    <TEXTAREA tabindex="-1" rows="9" cols="25" readonly class="libelleLongDisabled"><%=viewBean.getDetailAdresseLong()%></TEXTAREA>
                </label>
            </TD>
            <TD width="50"></TD>
          </TR>

          <TR>
            <TD width="119">&nbsp;</TD>
            <TD nowrap width="211"></TD>
            <TD width="50"></TD>
            <TD width="180"></TD>
            <TD nowrap width="125"></TD>
          </TR>

          <input type="hidden"  name="forIdTiers" value="<%=viewBean.getIdTiers()%>">
          <input type="hidden"  name="forIdAdressePaiement" value="<%=viewBean.getIdAdressePaiement()%>">
		  <input type="hidden"  name="forCompteLike" value="<%=viewBean.getDetailNumCompteBancaire()%>">
          <input type="hidden"  name="colonneSelection" value="yes">
          <input type="hidden"  name="hideColonneSelection" value="<%=request.getParameter("hideColonneSelection")%>"/>

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

<ct:menuChange displayId="options" menuId="reference-paiement" showTab="options">
	<ct:menuSetAllParams key="idReferenceQR" value="<%=viewBean.getIdReferenceQR()%>"/>
</ct:menuChange>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>