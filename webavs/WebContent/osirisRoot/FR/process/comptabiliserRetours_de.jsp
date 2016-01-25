 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%idEcran = "GCA3025"; %>
<%@ page import="globaz.globall.util.*" %>
<%@ page import="globaz.osiris.db.ordres.*" %>
<%
globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();

CAComptabiliserRetoursViewBean viewBean = (CAComptabiliserRetoursViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
userActionValue = "osiris.process.comptabiliserRetours.executer";
showProcessButton = objSession.hasRight("osiris.process.comptabiliserRetours.executer", FWSecureConstants.ADD) &&  !processLaunched;
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> <%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<%@page import="globaz.osiris.db.process.CAComptabiliserRetoursViewBean"%>
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
top.document.title = "Process - Comptabilisation des retours - " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Comptabilisation des retours<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          <TR> 
            <TD nowrap width="30%" height="14">E-mail</TD>
            <TD nowrap width="50%" height="14"> 
              <INPUT type="text" name="eMailAddress" class="libelleLong" value="<%= viewBean.getEMailAddress()%>">
            </TD>
            <TD nowrap width="20%" height="14">&nbsp;</TD>
          </TR>
          <%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<%	}%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>