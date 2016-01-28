<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA2019"; %>
<%@ page import="globaz.osiris.db.print.*" %>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%@ page import="globaz.osiris.servlet.action.CADefaultServletAction" %>
<%
CAListRecapParRubriquesViewBean viewBean = (CAListRecapParRubriquesViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);

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
userActionValue = globaz.osiris.application.CAApplication.DEFAULT_OSIRIS_NAME + ".print.listRecapParRubriques.executer"; 
%>
top.document.title = "Liste récapitulative par rubrique - " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Liste récapitulative par rubrique<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

          <tr> 
            <td width="128">E-mail</td>
            <td> 
              <input type="text" name="eMailAddress" value="<%=viewBean.getEMailAddress()%>" class="libelleLong">
            </td>
          </tr>		
          
          <tr> 
            <td width="128">R&ocirc;le</td>
            <td> 
              <select name="forSelectionRole" tabindex="2">
              	<%=CARoleViewBean.createOptionsTags(objSession, viewBean.getForSelectionRole())%>
              </select>
            </td>
            </tr>				
				
<%
	String selectBlock = globaz.osiris.parser.CASelectBlockParser.getForIdGenreSelectBlock(objSession);
              		
	if (!globaz.jade.client.util.JadeStringUtil.isBlank(selectBlock)) {
		out.print("<tr>");
		out.print("<td align=\"left\">Genre</td>");
		out.print("<td align=\"left\">");
		out.print(selectBlock);
		out.print("</td>");
		out.print("</tr>");
	}
%>
  
<%
	String selectCategorieSelect= globaz.osiris.parser.CASelectBlockParser.getForIdCategorieSelectBlock(objSession);
              		
	if (!globaz.jade.client.util.JadeStringUtil.isBlank(selectCategorieSelect)) {
		out.print("<tr>");
		out.print("<td align=\"left\">Cat&eacute;gorie</td>");
		out.print("<td align=\"left\">");
		out.print(selectCategorieSelect);
		out.print("</td>");
		out.print("</tr>");
	}
%> 				

				<tr>
	            	<td width="128">Date valeur de...</td>
	            	<td><ct:FWCalendarTag name="fromDateValeur" doClientValidation="CALENDAR" value=""/>
	            	&nbsp;à...&nbsp;
	            	<ct:FWCalendarTag name="toDateValeur" doClientValidation="CALENDAR" value=""/>
	            	</td>
	        	</tr>
	        	
				<tr>
	            	<td width="128">N° débiteur de...</td>
	            	<td><input type="text" name="fromIdExterneRole" value="" class="libelleLong"/>
	            	&nbsp;à...&nbsp;
	            	<input type="text" name="toIdExterneRole" value="" class="libelleLong"/>
	            	</td>
	        	</tr>	
	        	
				<tr>
	            	<td width="128">Rubrique de...</td>
	            	<td><input type="text" name="fromIdExterne" value="" class="libelleLong"/>
	            	&nbsp;à...&nbsp;
	            	<input type="text" name="toIdExterne" value="" class="libelleLong"/>
	            	</td>
	        	</tr>	        		        		
						
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>