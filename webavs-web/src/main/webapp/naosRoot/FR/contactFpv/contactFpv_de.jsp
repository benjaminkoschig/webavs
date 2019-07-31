<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*"
         contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/WEB-INF/naos.tld" prefix="naos" %>
<%@page import="globaz.naos.db.contactFpv.AFContactFpvViewBean" %>
<%
    AFContactFpvViewBean viewBean = (AFContactFpvViewBean) session.getAttribute("viewBean");
    idEcran = "Nouvel �cran";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>Contact FPV - D�tail<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>

<SCRIPT language="JavaScript">

    function init() {
        document.getElementById("sexe").disabled = true;
        document.getElementById("btnDel").style.display = 'none';
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
        document.getElementById('userAction').value = "back";
    }

</SCRIPT>

<TABLE border="0" cellspacing="0" cellpadding="0">
    <TR>
        <TD nowrap>N&deg; d'affili&eacute;</TD>
        <TD nowrap>
            <INPUT id="affiliationNumero" name="affiliationNumero" size="20" maxlength="20" type="text"
                   value="<%=(viewBean.getAffiliationNumero().isEmpty()) ? request.getParameter("likeNumAffilie") : viewBean.getAffiliationNumero()%>"
                   readonly class="disabled">
        </TD>
    </TR>
    <TR>
        <TD nowrap>Nom</TD>
        <TD nowrap>
            <INPUT id="nom" name="nom" type="text" size="20" maxlength="20"
                   value="<%=viewBean.getNom()%>" disabled>
        </TD>
    </TR>
    <TR>
        <TD nowrap>Pr&eacute;nom</TD>
        <TD nowrap>
            <INPUT id="prenom" name="prenom" type="text" size="20" maxlength="20"
                   value="<%=viewBean.getPrenom()%>" disabled>
        </TD>
    </TR>
    <TR>
        <TD nowrap>Sexe</TD>
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
        <TD nowrap width="135">Stop prospection</TD>
        <TD nowrap>
            <INPUT name="stopProspection" type="radio" disabled <%=(viewBean.isStopProspection())? "checked" : ""%>>Oui
            <INPUT name="stopProspection" type="radio" disabled <%=(!viewBean.isStopProspection())? "checked" : ""%>>Non
        </TD>
    </TR>
    <%@ include file="/theme/detail/bodyButtons.jspf" %>
    <%-- /tpl:put --%>
    <%@ include file="/theme/detail/bodyErrors.jspf" %>
    <%@ include file="/theme/detail/footer.jspf" %>
    <%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%-- /tpl:insert --%>