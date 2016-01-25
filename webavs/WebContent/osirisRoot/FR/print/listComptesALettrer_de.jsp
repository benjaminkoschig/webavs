 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%idEcran = "GCA2015"; %>
<%@ page import="globaz.globall.util.*" %>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%@ page import="globaz.osiris.servlet.action.CADefaultServletAction" %>
<%@ page import="globaz.osiris.db.print.*" %>
<%
CAListComptesALettrerViewBean viewBean = (CAListComptesALettrerViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);

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
userActionValue = globaz.osiris.application.CAApplication.DEFAULT_OSIRIS_NAME + ".print.listComptesALettrer.executer"; 
%>
top.document.title = "Liste - Impression de la liste des comptes annexes à lettrer - " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Impression de la liste des comptes annexes à lettrer<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          <tr> 
            <td nowrap width="128">E-mail</td>
            <td nowrap width="576">
            <input type="hidden" name="aLettrer" value="1">
              <input type="text" name="eMailAddress" value="<%=viewBean.getEMailAddress()%>" class="libelleLong" tabindex="1">
            </td>
            <td width="65">&nbsp;</td>
            <td width="67" nowrap>&nbsp;</td>
            <td nowrap width="167">&nbsp;</td>
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
          
<%
	String selectBlock = globaz.osiris.parser.CASelectBlockParser.getForIdGenreSelectBlock(objSession);
              		
	if (!globaz.jade.client.util.JadeStringUtil.isBlank(selectBlock)) {
		out.print("<tr>");
		out.print("<td nowrap align=\"left\">Genre</td>");
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
		out.print("<td nowrap align=\"left\">Cat&eacute;gorie</td>");
		out.print("<td nowrap align=\"left\">");
		out.print(selectCategorieSelect);
		out.print("</td>");
		out.print("</tr>");
	}
%>         
          <tr> 
            <td nowrap width="128">Section</td>
            <td nowrap width="576"> 
              <select name="forIdTypeSection" tabindex="3">
                <option value="1000">Tous</option>
                <%CATypeSection tempTypeSection;
					 		CATypeSectionManager manTypeSection = new CATypeSectionManager();
							manTypeSection.setSession(viewBean.getSession());
							manTypeSection.find();
							for(int i = 0; i < manTypeSection.size(); i++){
								tempTypeSection = (CATypeSection)manTypeSection.getEntity(i); %>
                <option value="<%=tempTypeSection.getIdTypeSection()%>"><%=tempTypeSection.getDescription()%></option>
                <% } %>
              </select>
            </td>
            <td width="65">&nbsp;</td>
            <td width="67" nowrap>&nbsp;</td>
            <td nowrap width="167">&nbsp;</td>
          </tr>
          <tr> 
            <td nowrap width="128">1er niveau de tri</td>
            <td nowrap width="576"> 
              <select name="forSelectionTriCA" tabindex="4">
                <option value="1">Compte annexe (numéro)</option>
                <option value="2">Compte annexe (nom)</option>
              </select>
            </td>
            <td width="65">&nbsp;</td>
            <td width="67" nowrap>&nbsp;</td>
            <td nowrap width="167">&nbsp;</td>
          </tr>
          <tr> 
            <td nowrap width="128">2&egrave;me niveau de tri</td>
            <td nowrap width="576"> 
              <select name="forSelectionTriSection" tabindex="5">
                <option value="1">Section (numéro)</option>
                <option value="2">Section (date)</option>
              </select>
            </td>
            <td width="65">&nbsp;</td>
            <td width="67" nowrap>&nbsp;</td>
            <td nowrap width="167">&nbsp;</td>
          </tr>
          
        <TR>
			<td>Type d'impression</td>
        	<TD>
        		<input type="radio" name="typeImpression" value="pdf" <%="pdf".equals(viewBean.getTypeImpression()) ? "checked='checked'" : "" %>/>PDF&nbsp;
        		<input type="radio" name="typeImpression" value="xls" <%="xls".equals(viewBean.getTypeImpression()) ? "checked='checked'" : "" %>/>Excel
        	</TD>
    	</TR>
    
          <%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<%	}%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>