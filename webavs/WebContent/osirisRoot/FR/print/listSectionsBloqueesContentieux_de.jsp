<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%idEcran = "GCA2021"; %>
<%@ page import="globaz.globall.util.*" %>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%@ page import="globaz.osiris.servlet.action.CADefaultServletAction" %>
<%@ page import="globaz.osiris.db.print.*" %>
<%@ page import="globaz.globall.parameters.FWParametersSystemCodeManager" %>
<%
CAListSectionsBloqueesContentieuxViewBean viewBean = (CAListSectionsBloqueesContentieuxViewBean) session.getAttribute(CADefaultServletAction.VB_ELEMENT);

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
userActionValue = globaz.osiris.application.CAApplication.DEFAULT_OSIRIS_NAME + ".print.listSectionsBloqueesContentieux.executer"; 
%>
top.document.title = "Liste - Impression de la liste des sections bloquées du contentieux - " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Impression de la liste des sections bloquées du contentieux<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
						
	<tr>
	  <td style="width: 130px;">E-mail</td>
	  <td style="width: 837px;">
	  		<input type="text" name="eMailAddress" value="<%=viewBean.getEMailAddress()%>" class="libelleLong">	  		
	  </td>
	</tr>
	
	<tr> 
		<td nowrap width="128">Section</td>
		<td nowrap width="576"> 
		  <select name="forIdTypeSection" tabindex="2">
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
      <td>R&ocirc;le</td>
      <td>
      	<select name="forSelectionRole" tabindex="2">
			<%=CARoleViewBean.createOptionsTags(objSession, request.getParameter("forSelectionRole"))%>
        </select>
      
      </td>
    </tr>	
	<tr> 
	  <td nowrap width="128">1er niveau de tri</td>
	  <td nowrap width="576"> 
	    <select name="forSelectionTriCA" tabindex="3">
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
	    <select name="forSelectionTriSection" tabindex="4">
	      <option value="1">Section (numéro)</option>
	      <option value="2">Section (date)</option>
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
		String selectCategorieSelect = globaz.osiris.parser.CASelectBlockParser.getForIdCategorieSelectBlock(objSession);
	              		
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
      <td>Motif de blocage</td>
      <td>
      	<%	
      	FWParametersSystemCodeManager csMotifContentieuxSuspendus = new FWParametersSystemCodeManager();
		csMotifContentieuxSuspendus.setSession(objSession);
		csMotifContentieuxSuspendus.getListeCodesSup("OSIMOTCTX", objSession.getIdLangue());
      	
		globaz.globall.parameters.FWParametersSystemCode _motifContentieuxSus = null; 
		%>
        <select name="idMotConSus" tabindex="5" style="width : 7cm;">
          <%	
          for (int i=0; i < csMotifContentieuxSuspendus.size(); i++) { 
          		_motifContentieuxSus = (globaz.globall.parameters.FWParametersSystemCode) csMotifContentieuxSuspendus.getEntity(i);
				
				%>
         		 <option value="<%=_motifContentieuxSus.getIdCode()%>"><%=_motifContentieuxSus.getCurrentCodeUtilisateur().getCodeUtiLib()%></option>
		<% } %>
                </select>
		      
		      </td>
		    </tr>
	    	<tr>
		      <td>Avec blocage inactif</td>
		      <td><input type="checkbox" name="blocageInactif"></td>
		    </tr>
	    	<tr>	                     
          <%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<%	}%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>