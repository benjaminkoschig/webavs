
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA3021"; %>
<%@ page import="globaz.globall.util.*" %>
<%@ page import="globaz.osiris.db.ordres.*" %>
<%
globaz.osiris.db.process.CARemboursementAutomatiqueViewBean viewBean = (globaz.osiris.db.process.CARemboursementAutomatiqueViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
userActionValue = "osiris.process.remboursementAutomatique.executer";

// Récupération du contrôleur et de la session
globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();

String jspLocation = servletContext + mainServletPath + "Root/compteCourant_select.jsp";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> <%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
function SetTimer() {
    document.forms[0].lancement.value = "Lancé...";
    vSubmit();
}
function fLancement() {
  document.forms[0].lancer.value = "lancer";
  SetTimer();

}
function vSubmit() {
  document.forms[0].submit();
}
top.document.title = "Process - Remboursement automatique - " + top.location.href;

function updateCompteCourant(el) {
  if (el == null || el.value== "" || el.options[el.selectedIndex] == null) {
    document.forms[0].forIdCompteCourant.value = "";
    document.forms[0].forIdCompteCourantEcran.value = "";
  } else {
    var elementSelected = el.options[el.selectedIndex];
    document.forms[0].forIdCompteCourant.value = elementSelected.idCompteCourant;
    document.forms[0].forIdCompteCourantEcran.value = elementSelected.idExterneCompteCourantEcran;
  }
}
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
      <%-- tpl:put name="zoneTitle" --%>Remboursement automatique<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
            <%-- tpl:put name="zoneMain" --%>
<TR>
<TD width="30%">E-mail</TD>
<TD><INPUT type="text" name="eMailAddress" class="libelleLong" value="<%= viewBean.getEMailAddress()%>"/></TD>
</TR>
<TR>
<TD width="30%">Libell&eacute;</TD>
<TD><INPUT type="text" name="libelle" style="width:7cm" size="40" maxlength="40" value="<%=viewBean.getLibelle()%>" class="libelleStandard"/></TD>
</TR>
<TR>
<TD width="30%">Date d'&eacute;ch&eacute;ance</TD>
<%
	String tmp = globaz.globall.util.JACalendar.today().toStr(".");
%>
<TD><ct:FWCalendarTag name="forDateEcheance" doClientValidation="CALENDAR" value="<%=tmp%>"/></TD>
</TR>
<TR>
<TD width="30%">Nature des versements</TD>
<TD>
<%
	globaz.globall.parameters.FWParametersSystemCodeManager manag = viewBean.getCsNatureOrdres();
	globaz.globall.parameters.FWParametersSystemCode _natureOrdre = null;
%>
<SELECT name="forNatureOrdre" class="libelleCourt">
<%
	for (int i=0; i < manag.size(); i++) {
		_natureOrdre = (globaz.globall.parameters.FWParametersSystemCode) manag.get(i);

		if (!globaz.jade.client.util.JadeStringUtil.isBlank(_natureOrdre.getIdCode())) {
%>
			<option value="<%=_natureOrdre.getIdCode()%>"><%=_natureOrdre.getCurrentCodeUtilisateur().getLibelle()%></option>
<%
		}
	}
%>
</SELECT>
</TD>
</TR>
<TR>
<TD width="30%">Montant minimum à rembourser</TD>
<TD><input type="text" name="forMontantLimit" onchange="validateFloatNumber(this)" onkeypress="filterCharForFloat(window.event)" size="10" maxlength="10" value="<%=JANumberFormatter.formatNoRound(viewBean.getForMontantLimit())%>"  class="montant" /></TD>
</TR>
<TR>
<TD width="30%">Compte courant</TD>
<TD>
<input type="hidden" name="forIdCompteCourant" value="<%=viewBean.getForIdCompteCourant()%>"/>
<ct:FWPopupList name="forIdCompteCourantEcran"
        onChange="updateCompteCourant(tag.select);"
        value="<%=viewBean.getIdExterneCompteCourant()%>"
        className="libelle"
        jspName="<%=jspLocation%>"
        minNbrDigit="1"
        forceSelection="true"
        validateOnChange="false" />
</TD>
</TR>
<TR>
<TD width="30%">Rôle</TD>
<TD>
<select name="forIdRole" class="libelleCourt">
  <option value=""></option>
  <%=globaz.osiris.db.comptes.CARoleViewBean.createOptionsTags(objSession, "", false)%>
</select>
</TD>
</TR>
<TR>
<TD width="30%">Organe d'ex&eacute;cution d&eacute;sir&eacute;</TD>
<TD>
<%
  CAOrganeExecution _organeExecution = null;
  CAOrganeExecutionManager _CsOrganeExecution = new CAOrganeExecutionManager();
  _CsOrganeExecution.setSession(objSession);
  _CsOrganeExecution.setForIdTypeTraitementOG(true);
  _CsOrganeExecution.find();
%>
<select name="forIdOrganeExecution" class="libelleCourt">
<%
  for (int i=0; i < _CsOrganeExecution.size(); i++) {
    _organeExecution = (CAOrganeExecution) _CsOrganeExecution.getEntity(i);
%>
  <option selected value="<%=_organeExecution .getIdOrganeExecution()%>"><%=_organeExecution .getNom()%></option>
<%
  }
%>
</TD>
</TR>
          <%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<%	}%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>