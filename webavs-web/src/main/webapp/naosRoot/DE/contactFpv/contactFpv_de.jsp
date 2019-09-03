<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*"
         contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/WEB-INF/naos.tld" prefix="naos" %>
<%@page import="globaz.naos.db.contactFpv.AFContactFpvViewBean" %>
<%@ page import="java.util.Objects" %>
<%
    AFContactFpvViewBean viewBean = (AFContactFpvViewBean) session.getAttribute("viewBean");
    idEcran = "CAF0077";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>CPB Kontakt - Details<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>

<SCRIPT language="JavaScript">

    function init() {
        document.getElementById("sexe").disabled = true;
    }

    function upd() {
    }

    function validate() {
        if (<%=viewBean.getAffiliationNumero().isEmpty()%>) {
            document.getElementById('userAction').value = "naos.contactFpv.contactFpv.ajouter";
        } else {
            document.getElementById('userAction').value = "naos.contactFpv.contactFpv.modifier";
        }
        return true;
    }

    function cancel() {
        document.getElementById('userAction').value = "naos.contactFpv.contactFpv.afficher";
    }

    function del() {
        if (<%=Objects.nonNull(viewBean.getSpy())%>) {
            supprime();
        }
    }

    function supprime() {
        $("#dialog_confirm_suppression").dialog({
            resizable: false,
            height: 170,
            modal: true,
            buttons: [{
                id: "yes",
                text: "<ct:FWLabel key='NAOS_LIBELLE_OUI'/>",
                click: function () {
                    $(this).dialog("close");
                    document.getElementById('userAction').value = "naos.contactFpv.contactFpv.supprimer";
                    document.forms[0].submit();
                }
            }, {
                id: "no",
                text: "<ct:FWLabel key='NAOS_LIBELLE_NON'/>",
                click: function () {
                    $(this).dialog("close");
                    return;
                }
            }],
            open : function() {
            $("#no").focus();
        }
        });
    }

</SCRIPT>

    <INPUT type="hidden" id="selectedId" name="selectedId"
           value="<%=viewBean.getContactId()%>">

<div style="display:none" id="dialog_confirm_suppression"
     title="<ct:FWLabel key='NAOS_DIALOG_CONFIRM_SUPRRESSION_CONTACT_FPV_TITRE'/>" >
    <p>
        <ct:FWLabel key='NAOS_DIALOG_CONFIRM_SUPRRESSION_CONTACT_FPV_TEXTE'/>
    </p>
</div>

<TABLE border="0" cellspacing="0" cellpadding="0">

    <TR>
        <TD nowrap>Abr.-Nr.</TD>
        <TD nowrap>
            <INPUT id="affiliationNumero" name="affiliationNumero" size="20" maxlength="20" type="text"
                   value="<%=(viewBean.getAffiliationNumero().isEmpty()) ? request.getParameter("likeNumAffilie") : viewBean.getAffiliationNumero()%>"
                   readonly class="disabled">
        </TD>
    </TR>
    <TR>
        <TD nowrap>Name</TD>
        <TD nowrap>
            <INPUT id="nom" name="nom" type="text" size="20" maxlength="20"
                   value="<%=viewBean.getNom()%>" disabled>
        </TD>
    </TR>
    <TR>
        <TD nowrap>Vorname</TD>
        <TD nowrap>
            <INPUT id="prenom" name="prenom" type="text" size="20" maxlength="20"
                   value="<%=viewBean.getPrenom()%>" disabled>
        </TD>
    </TR>
    <TR>
        <TD nowrap>Geschlecht</TD>
        <TD nowrap>
            <ct:FWCodeSelectTag
                    name="sexe"
                    defaut="<%=viewBean.getSexe()%>"
                    codeType="PYSEXE"
                    wantBlank="true"/>
        </TD>
    </TR>
    <TR>
        <TD nowrap>E-mail</TD>
        <TD nowrap>
            <INPUT id="email" name="email" type="email" size="50" maxlength="50"
                   value="<%=viewBean.getEmail()%>" disabled>
        </TD>
    </TR>
    <TR>
        <TD nowrap width="135">Kundengewinnung aufhören</TD>
        <TD nowrap>
            <INPUT id="stopProspection" name="stopProspection" type="checkbox" disabled <%=(viewBean.isStopProspection())? "checked" : ""%>>
        </TD>
    </TR>

    <%@ include file="/theme/detail/bodyButtons.jspf" %>
    <%-- /tpl:put --%>
    <%@ include file="/theme/detail/bodyErrors.jspf" %>
    <%@ include file="/theme/detail/footer.jspf" %>
    <%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%-- /tpl:insert --%>