
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA3012";
rememberSearchCriterias = true;
%>
<%@ page import="globaz.osiris.db.bvrftp.CABvrFtpListViewBean" %>
<%
CABvrFtpListViewBean listViewBean = (CABvrFtpListViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT);
bButtonNew = false;
%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="options" menuId="CA-BVR" showTab="options"/>

<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

<%
if (listViewBean.isFtpConnectionAvailable()) {
	out.print("usrAction = \"" + globaz.osiris.application.CAApplication.DEFAULT_OSIRIS_NAME + ".bvrftp.bvrFtp.lister\";");
	out.print("bFind = true;");
} else {
	bButtonFind = false;

	out.print("bFind = false;");
}
%>

naviShortKeys[82] = 'r1_c2';//  R (paramètres)
naviShortKeys[73] = 'r2_c2';//  I (incréments)
naviShortKeys[76] = 'r3_c2';//  L (libellés)
naviShortKeys[84] = 'r4_c2';//  T (inc. types)

function removeParam(str_source, str_param) {
  var result = str_source;
  var paramPos = result.indexOf(str_param);
//   if no param, do nothing
  if (paramPos < 0)
    return result;

  nextParamPos = result.indexOf("&", paramPos + 1);
  var str_end = "";
  if (nextParamPos > -1)//   there are more parameters after this one
    str_end = result.slice(nextParamPos);

  result = result.slice(0, paramPos);
  result += str_end;

//  alert ("returning " + result);
  return result;
}

top.document.title = "Connexion serveur PostFinance - " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Connexion serveur PostFinance<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

			<TR>
			<TD width="70" align="left">Host/Port</TD>
            <TD width="200" align="left">
            <INPUT type="text" name="host_port" style="width:7cm" maxlength="30" value="<%=listViewBean.getHost()%>:<%=listViewBean.getPort()%>" class="libelleDisabled" tabindex="-1" readonly>
            </TD>
            <TD width="300" align="right">&nbsp;</TD>
            <TD width="179" align="right">Status&nbsp;</TD>
            <TD align="left">
            <INPUT type="text" name="state" style="width:7cm" maxlength="30" value="<%=listViewBean.getFtpConnectionState()%>" class="libelleDisabled" tabindex="-1" readonly>
			</TD>
			</TR>

			<TR>
			<TD width="70" align="left">Login</TD>
            <TD width="200" align="left">
            <INPUT type="text" name="login" style="width:7cm" maxlength="30" value="<%=listViewBean.getLogin()%>" class="libelleDisabled" tabindex="-1" readonly>
            </TD>
            <TD width="300" align="right">&nbsp;</TD>
            <TD width="179" align="right">Dossier distant&nbsp;</TD>
            <TD align="left">
            <SELECT name="distantDirectoryName">
            	<OPTION value="<%=CABvrFtpListViewBean.FTP_READ_OPAE_REMOTE_DIRECTORY%>"><%=CABvrFtpListViewBean.FTP_READ_OPAE_REMOTE_DIRECTORY%> (BVR)</OPTION>
            	<OPTION value="<%=CABvrFtpListViewBean.FTP_READ_LSV_REMOTE_DIRECTORY%>"><%=CABvrFtpListViewBean.FTP_READ_LSV_REMOTE_DIRECTORY%> (Débit direct)</OPTION>
            </SELECT>
            </TD>
			</TR>
          <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%> <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>