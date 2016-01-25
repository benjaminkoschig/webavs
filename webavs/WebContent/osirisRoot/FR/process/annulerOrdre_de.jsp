
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA3020"; %>
<%@ page import="globaz.globall.util.*" %>
<%@ page import="globaz.osiris.db.process.*" %>
<%
globaz.osiris.db.process.CAAnnulerOrdreViewBean viewBean = (globaz.osiris.db.process.CAAnnulerOrdreViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
userActionValue = "osiris.process.annulerOrdre.executer";
selectedIdValue=viewBean.getIdOrdreGroupe();
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
function validate() {
	//jscss("add", document.getElementById("btnOk"), "hidden");
	document.getElementById("btnOk").disabled = true;
	return true;
}
top.document.title = "Process - Annuler ordre - " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Annuler ordre<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
          <TR>
            <TD nowrap width="30%" height="14">E-mail</TD>
            <TD nowrap width="50%" height="14">
              <INPUT type="text" name="eMailAddress" class="libelleLong" value="<%= viewBean.getEMailAddress()%>">
              <input type="hidden" name="idOrdreGroupe" value="<%=viewBean.getIdOrdreGroupe()%>"/>
            </TD>
            <TD nowrap width="20%" height="14">&nbsp;</TD>
          </TR>
          <TR>
            <TD nowrap width="30%">Description du journal</TD>
            <TD nowrap width="50%">
              <INPUT type="text" name="_libelleLong" value="<%=(viewBean.getOrdreGroupe().getIdOrdreGroupe() + " " + viewBean.getOrdreGroupe().getMotif())%>" maxlength="80" class="libelleLongDisabled" readonly>
            </TD>
            <TD nowrap width="20%">&nbsp;</TD>
          </TR>
          <%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<%	}%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>