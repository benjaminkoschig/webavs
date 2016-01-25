
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA0066"; %>
<%
globaz.osiris.db.historique.CAHistoriqueBulletinSoldeViewBean viewBean = (globaz.osiris.db.historique.CAHistoriqueBulletinSoldeViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
selectedIdValue = viewBean.getIdHistorique();

bButtonUpdate = false;
bButtonNew = false;
bButtonCancel = false;
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="javascript">

function init() {}

function del() {
    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
        document.forms[0].elements('userAction').value="osiris.historique.historiqueBulletinSolde.supprimer";
        document.forms[0].submit();
    }
}

top.document.title = "Detail Saldo-ESR Verlauf - " + top.location.href;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Detail Saldo-ESR Verlauf<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
	       <TR>
            <TD nowrap width="125">Durchführung Datum</TD>
            <TD width="30">&nbsp;</TD>
            <TD nowrap>
            <ct:FWCalendarTag name="dateHistorique" doClientValidation="CALENDAR" value="<%=viewBean.getDateHistorique()%>"/>
            </TD>
            <TD width="10">&nbsp;</TD>
          </TR>
          <TR>
            <TD nowrap width="125">Saldo</TD>
            <TD width="30">&nbsp;</TD>
            <TD nowrap>
            <input type="text" name="solde" onchange="validateFloatNumber(this)" size="16" maxlength="15" value="<%=viewBean.getSoldeFormatter()%>"  class="montant"/>
            </TD>
            <TD width="10">&nbsp;</TD>
          </TR>
          <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>