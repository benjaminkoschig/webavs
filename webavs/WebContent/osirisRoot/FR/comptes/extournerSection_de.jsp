
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA3002"; %>
<%@ page import="globaz.globall.util.*" %>
<%@ page import="globaz.osiris.db.ordres.*" %>
<%
globaz.osiris.db.comptes.CAExtournerSectionViewBean viewBean = (globaz.osiris.db.comptes.CAExtournerSectionViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
userActionValue = "osiris.comptes.extournerSection.executerExtourne";
selectedIdValue=viewBean.getIdSection();
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> <%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
function vSubmit() {
	document.forms[0].submit();
}
function validate() {
	//jscss("add", document.getElementById("btnOk"), "hidden");
	document.getElementById("btnOk").disabled = true;
	return true;
}
top.document.title = "Process - Extourner la section - " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Extourner la section<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
          <TR>
            <TD nowrap width="30%">Description de la section</TD>
            <TD nowrap width="70%" colspan ="2">
              <INPUT type="text" name="_libelleLong" value="<%=viewBean.getDescription()%>" size="65" maxlength="80" class="Disabled" readonly>
            </TD>

          </TR>

          <TR>
            <TD nowrap width="30%">Commmentaire</TD>
            <TD nowrap width="70%" colspan="2">
              <INPUT type="text" name="comment" value="<%=viewBean.getComment()%>" size="65" maxlength="40">
            </TD>
          </TR>
          <tr>
            <% if (viewBean.getSection().getAttenteLSVDD().booleanValue()) { %>

            <TD nowrap width="30%">Information</TD>
            <TD nowrap width="70%" colspan="2">
	          	<INPUT style="color:#FF0000" type="text" name="" value="Attente LSV/DD" class="Disabled" tabindex="-1" readonly>
            </TD>
          </tr>
          <% } %>

          <%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<%	}%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>