<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA3017"; %>
<%
formAction= request.getContextPath()+mainServletPath+"Root/"+languePage+"/process/processImportOperationsLoadFile_de.jsp";
globaz.osiris.db.process.CAProcessImportOperationsViewBean viewBean = (globaz.osiris.db.process.CAProcessImportOperationsViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
userActionValue = "osiris.process.processImportOperations.executer";
formEncType = "'multipart/form-data'";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="options" menuId="CA-OnlyDetail" showTab="menu"/>

<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

top.document.title = "Prozess - Operationen importieren - " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Operationen importieren<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
		  <TR> 
            <TD align="left" width="180" height="21" valign="middle">E-Mail</TD>
            <TD align="left"> 
              <INPUT type="text" name="eMailAddress" class="libelleLong" value="<%=viewBean.getEMailAddress()%>"/>
            </TD>
          </TR>	 
          
          <TR> 
            <TD align="left" width="180" height="21" valign="middle">Valutadatum</TD>
            <TD align="left"> 
              <ct:FWCalendarTag name="dateTraitement" doClientValidation="CALENDAR" value="<%=globaz.globall.util.JACalendar.todayJJsMMsAAAA()%>"/>
            </TD>
          </TR>	

          <TR> 
            <TD align="left" width="180" height="21" valign="middle">Bezeichnung</TD>
            <TD align="left"> 
              <INPUT type="text" name="libelleJournal" maxlength="50" class="libelleLong"/>
            </TD>
          </TR> 
          
          <TR> 
            <TD align="left" width="180" height="21" valign="middle">Pfad und XML-Dateiname</TD>
            <TD align="left"> 
				<input type="file" name="fileName" maxlength="256" class="libelleLong"/>
            </TD>
          </TR>	
          
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> 
<SCRIPT>
document.forms[0].enctype = "multipart/form-data";
document.forms[0].method = "post";
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>