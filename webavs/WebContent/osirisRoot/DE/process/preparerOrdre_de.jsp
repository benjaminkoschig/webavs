
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA3010"; %>
<%@ page import="globaz.globall.util.*" %>
<%@ page import="globaz.osiris.db.ordres.*" %>
<%
globaz.osiris.process.CAProcessAttacherOrdre viewBean = (globaz.osiris.process.CAProcessAttacherOrdre) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
userActionValue = "osiris.process.preparerOrdre.executer";
selectedIdValue = viewBean.getIdOrdreGroupe();
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> <%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
function fLancement() {
	document.forms[0].lancer.value = "lancer";
	document.forms[0].submit();
}
function validate() {
	//jscss("add", document.getElementById("btnOk"), "hidden");
	document.getElementById("btnOk").disabled = true;
	return true;
}
top.document.title = "Prozess - Vorbereiten Sammelauftrag " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Vorbereiten Sammelauftrag<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
          <TR>
            <td nowrap width="128">E-Mail</td>
            <td nowrap width="576">
              <input type="text" name="eMailAddress" value="<%=viewBean.getEMailAddress()%>" class="libelleLong">
            </td>
            <TD width="65">&nbsp;</TD>
            <TD width="67" nowrap>&nbsp;</TD>
            <TD nowrap width="167">&nbsp;</TD>
          </TR>
          <tr>
            <td nowrap width="128">Sammelauftrag</td>
            <td nowrap width="576">
              <INPUT type="text" name="_libelleOrdreGroupe" value='<%if (viewBean.getOrdreGroupe() != null) {%><%=viewBean.getIdOrdreGroupe()+"-"+viewBean.getOrdreGroupe().getMotif()%><%}%>' class="libelleLong" readonly >
            </td>
            <td width="65">
              <input type="hidden" name="idOrdreGroupe" value="<%=viewBean.getIdOrdreGroupe()%>">
            </td>
            <td width="67" nowrap>&nbsp; </td>
            <td nowrap width="167">&nbsp;</td>
          </tr>
          <%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<%	}%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>