 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%idEcran = "GCA2022"; %>
<%@ page import="globaz.globall.util.*" %>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%@ page import="globaz.osiris.servlet.action.CADefaultServletAction" %>
<%@ page import="globaz.osiris.db.print.*" %>
<%
CAListBulletinSoldeViewBean viewBean = (CAListBulletinSoldeViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);

globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();    
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> <%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
<%
userActionValue = globaz.osiris.application.CAApplication.DEFAULT_OSIRIS_NAME + ".print.listBulletinSolde.executer"; 
%>
top.document.title = "Liste - Impression des bulletins de soldes - " + top.location.href;
// stop hiding -->
</SCRIPT>

<SCRIPT language="JavaScript">
	function manageFrom() {
		
	}
</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Impression des bulletins de soldes<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 

          <tr> 
            <td nowrap width="128">E-mail</td>
            <td nowrap width="576"> 
              <input type="text" name="eMailAddress" value="<%=viewBean.getEMailAddress()%>" class="libelleLong">
            </td>
          </tr>
          <tr> 
            <td nowrap width="128">R&ocirc;le</td>
            <td nowrap width="576"> 
              <select name="forSelectionRole" tabindex="2">
              	<%=CARoleViewBean.createOptionsTags(objSession, request.getParameter("forSelectionRole"))%>
              </select>
            </td>
            <td width="65">&nbsp;</td>
            <td width="67" nowrap>&nbsp;</td>
            <td nowrap width="167">&nbsp;</td>
          </tr>

          <tr> 
            <td nowrap width="128">N° Compte annexe du &nbsp;</td>
            <td nowrap width="576"> 
            	<input type="text" name="fromNoCompteAnnexe" value="" >              
            	&nbsp;au &nbsp;
            	<input type="text" name="untilNoCompteAnnexe" value="">
            </td>
          </tr>

          <tr> 
            <td nowrap width="128">Tri compte annexe</td>
            <td nowrap width="576"> 
              <select name="forSelectionTriCompteAnnexe" class="libelleCourt" tabindex="3">
                <option value="1">Num&eacute;ro</option>
                <option value="2">Nom</option>
              </select>
            </td>
            <td width="65">&nbsp;</td>
            <td width="67" nowrap>&nbsp;</td>
            <td nowrap width="167">&nbsp;</td>
          </tr>
          
          <tr id="fromDateField"> 
          <td nowrap width="128">Date à partir de</td>
          <td nowrap width="576">
          <%
          String year = "01.01.";
          year += "" + (globaz.globall.util.JACalendarGregorian.today().getYear() - 1);
          %>
          
          <ct:FWCalendarTag name="fromDate" doClientValidation="CALENDAR" value="<%=year%>"/>	
          
          &nbsp; jusqu'au &nbsp;
          <ct:FWCalendarTag name="untilDate" doClientValidation="CALENDAR" value="<%=viewBean.getFormatedDateToday()%>"/>
          </td> 
          </tr>
                    
          <SCRIPT language="JavaScript">
          manageFrom();
          </SCRIPT>

   			<input type="hidden" name="fromNoAffilie" value="%=viewBean.getIdExterneRole()%>">
           	<input type="hidden" name="untilNoAffilie" value="%=viewBean.getIdExterneRole()%>">
                                        
          <%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<%	}%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>