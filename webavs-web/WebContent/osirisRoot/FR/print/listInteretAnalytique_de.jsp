<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA2030"; %>
<%@ page import="globaz.osiris.db.print.*" %>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%@ page import="globaz.osiris.servlet.action.CADefaultServletAction" %>
<%
CAListInteretAnalytiqueViewBean viewBean = (CAListInteretAnalytiqueViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);

globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
<%
userActionValue = globaz.osiris.application.CAApplication.DEFAULT_OSIRIS_NAME + ".print.listInteretAnalytique.executer";
%>
top.document.title = "Liste analytiques des intérêts - " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Liste analytiques des intérêts<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

          <tr>
            <td width="128">E-mail</td>
            <td>
              <input type="text" name="eMailAddress" value="<%=viewBean.getEMailAddress()%>" class="libelleLong">
            </td>
          </tr>
          
          <tr>
	      	<td>Date comptable dès</td>
	      	<td><ct:FWCalendarTag name="forDateDebut" doClientValidation="CALENDAR" value=""/>
	     	</td>
	      </tr>
	      
          <tr>
	      	<td>Date comptable jusqu'à</td>
	      	<td><ct:FWCalendarTag name="forDateFin" doClientValidation="CALENDAR" value=""/>
	     	</td>
	      </tr>

						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>