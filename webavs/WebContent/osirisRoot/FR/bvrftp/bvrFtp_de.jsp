
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%idEcran = "GCA3013"; %>
<%@ page import="globaz.osiris.db.bvrftp.CABvrFtpViewBean" %>
<% 
CABvrFtpViewBean viewBean = (CABvrFtpViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
selectedIdValue = viewBean.getFileName();
bButtonUpdate = false;
bButtonDelete = false;
bButtonNew = false;
bButtonCancel = false;
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="javascript"> 

function init() {}

top.document.title = "D�tail fichier distant - " + top.location.href;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>D�tail fichier distant<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
	       <TR> 
            <TD nowrap width="125">Fichier</TD>
            <TD width="30">&nbsp;</TD>
            <TD nowrap> 
            <INPUT type="text" name="fileName" style="width:7cm" size="16" maxlength="15" value="<%=viewBean.getFileName()%>" class="libelleDisabled" tabindex="-1" readonly>
            </TD>
            <TD width="10">&nbsp;</TD>
          </TR>
          <TR> 
            <TD nowrap width="125">Date</TD>
            <TD width="30">&nbsp;</TD>
            <TD nowrap> 
            <INPUT type="text" name="formatedFileDate" size="30" maxlength="30" value="<%=viewBean.getFormatedFileDate()%>" class="libelleStandard">
            </TD>
            <TD width="10">&nbsp;</TD>
          </TR>
          <TR> 
            <TD nowrap width="125">Dossier distant</TD>
            <TD width="30">&nbsp;</TD>
            <TD nowrap> 
            <INPUT type="text" name="distantDirectory" size="30" maxlength="30" value="<%=viewBean.getDistantDirectory()%>" class="libelleStandard">
            </TD>
            <TD width="10">&nbsp;</TD>
          </TR>
          <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<%	} %>

<%
	if ("add".equalsIgnoreCase(request.getParameter("_method")) && request.getParameter("_valid") == null) {
	} else {
%>
	<ct:menuChange displayId="options" menuId="CA-BVR" showTab="options"/>
<%
	}
%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>