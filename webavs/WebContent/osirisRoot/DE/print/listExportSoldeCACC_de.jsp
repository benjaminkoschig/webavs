 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%idEcran = "GCA2002"; %>
<%@ page import="globaz.globall.util.*" %>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%@ page import="globaz.osiris.servlet.action.CADefaultServletAction" %>
<%@ page import="globaz.osiris.db.print.*" %>
<%@ page import="globaz.osiris.db.export.*" %>
<%
CAListExportSoldeCACCViewBean viewBean = (CAListExportSoldeCACCViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);

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
userActionValue = globaz.osiris.application.CAApplication.DEFAULT_OSIRIS_NAME + ".print.listExportSoldeCACC.executer"; 
%>
top.document.title = "Liste - Ausdruck Saldoliste - " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Ausdruck Saldoliste <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          <tr> 
            <td nowrap width="128">E-Mail</td>
            <td nowrap width="576"> 
              <input type="text" name="eMailAddress" value="<%=viewBean.getEMailAddress()%>" class="libelleLong" tabindex="1" >
            </td>
            <td width="65">&nbsp;</td>
            <td width="67" nowrap>&nbsp;</td>
            <td nowrap width="167">&nbsp;</td>
          </tr>
          <tr> 
            <td nowrap width="128">Kontoart</td>
            <td nowrap width="576"> 
              <select name="forSelectionRole" tabindex="2" >
              	<%=CARoleViewBean.createOptionsTags(objSession, request.getParameter("forSelectionRole"))%>
              </select>
            </td>
            <td width="65">&nbsp;</td>
            <td width="67" nowrap>&nbsp;</td>
            <td nowrap width="167">&nbsp;</td>
          </tr>
	  
<%
	String selectBlock = globaz.osiris.parser.CASelectBlockParser.getForIdGenreSelectBlock(objSession);
              		
	if (!globaz.jade.client.util.JadeStringUtil.isBlank(selectBlock)) {
		out.print("<tr>");
		out.print("<td nowrap align=\"left\">Art</td>");
		out.print("<td nowrap align=\"left\">");
		out.print(selectBlock);
		out.print("</td>");
		out.print("</tr>");
	}
%>
  
<%
	String selectCategorieSelect= globaz.osiris.parser.CASelectBlockParser.getForIdCategorieSelectBlock(objSession);
              		
	if (!globaz.jade.client.util.JadeStringUtil.isBlank(selectCategorieSelect)) {
		out.print("<tr>");
		out.print("<td nowrap align=\"left\">Kategorie</td>");
		out.print("<td nowrap align=\"left\">");
		out.print(selectCategorieSelect);
		out.print("</td>");
		out.print("</tr>");
	}
%>        
          <tr> 
            <td nowrap width="128">Bis Datum</td>
            <td nowrap width="576"> 
	            <ct:FWCalendarTag name="forDate" doClientValidation="CALENDAR" value="" />
	            <script>
					document.getElementById("forDate").tabIndex="3";
					document.getElementById("anchor_forDate").tabIndex="4";
				</script>
            </td>
            <td width="65">&nbsp;</td>
            <td width="67" nowrap>&nbsp;</td>
            <td nowrap width="167">&nbsp;</td>
          </tr>
          <tr> 
            <td nowrap width="128">Kontokorrent</td>
            <td nowrap width="576"> 
	            <select name="forSelectionCC" tabindex="5">
                <option value="1000">Alle</option>
                <%CACompteCourant tempCC;
					 		CACompteCourantManager manCC = new CACompteCourantManager();
							manCC.setSession(viewBean.getSession());
							manCC.find();
							for(int i = 0; i < manCC.size(); i++){
								tempCC = (CACompteCourant)manCC.getEntity(i); %>
                <option value="<%=tempCC.getIdCompteCourant()%>"><%=tempCC.getIdExterne()%></option>
                <% } %>
              </select>
            </td>
            <td width="65">&nbsp;</td>
            <td width="67" nowrap>&nbsp;</td>
            <td nowrap width="167">&nbsp;</td>
          </tr>
          <tr> 
            <td nowrap width="128">1. Sortierstufe</td>
            <td nowrap width="576"> 
              <select name="forSelectionTri" class="libelleCourt" tabindex="6">
                <option value="1">Nummer</option>
                <option value="2">Name</option>
              </select>
            </td>
            <td width="65">&nbsp;</td>
            <td width="67" nowrap>&nbsp;</td>
            <td nowrap width="167">&nbsp;</td>
          </tr>
          <TR> 
            <td nowrap width="128" valign="top">Merkmale der Saldi</td>
            <td nowrap width="576"> 
              <input type="radio" name="forSelectionSigne" value="1" checked tabindex="7">
              Positiv und negativ <BR>
              <input type="radio" name="forSelectionSigne" value="2" >
              Positiv <BR>
              <input type="radio" name="forSelectionSigne" value="3">
              Negativ </td>
            <TD width="65">&nbsp;</TD>
            <TD width="67" nowrap>&nbsp;</TD>
            <TD nowrap width="167">&nbsp;</TD>
          </TR>                              
          <%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<%	}%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>