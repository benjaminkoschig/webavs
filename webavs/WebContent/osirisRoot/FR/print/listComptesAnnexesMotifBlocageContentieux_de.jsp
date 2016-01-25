<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%idEcran = "GCA2017"; %>
<%@ page import="globaz.globall.util.*" %>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%@ page import="globaz.osiris.servlet.action.CADefaultServletAction" %>
<%@ page import="globaz.osiris.db.print.*" %>
<%@ page import="globaz.globall.parameters.FWParametersSystemCodeManager" %>
<%
CAListComptesAnnexesMotifBlocageContentieuxViewBean viewBean = (CAListComptesAnnexesMotifBlocageContentieuxViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);

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
userActionValue = globaz.osiris.application.CAApplication.DEFAULT_OSIRIS_NAME + ".print.listComptesAnnexesMotifBlocageContentieux.executer"; 
%>
top.document.title = "Liste - Impression de la liste des comptes annexes avec motif de blocage du contentieux - " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Impression de la liste des comptes annexes avec motif de blocage du contentieux<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
						<tr>
					      <td style="width: 130px;">E-mail</td>
					      <td style="width: 837px;"><input type="text" name="eMailAddress" value="<%=viewBean.getEMailAddress()%>" class="libelleLong"></td>
					    </tr>
					    <tr>
					      <td>R&ocirc;le</td>
					      <td>
					      	<select name="forSelectionRole" tabindex="2">
								<%=CARoleViewBean.createOptionsTags(objSession, request.getParameter("forSelectionRole"))%>
				            </select>
					      
					      </td>
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
			                <select name="idContMotifBloque" style="width : 15cm;">
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
					      <td>Solde</td>
					      <td>
					      <select name="forSelectionCompte" style="width : 2.5cm;">
                			<option value="">tous</option>
                			<option value="1">ouverts</option>
                			<option value="2">sold&eacute;s</option>
              			  </select>
					      </td>
					    </tr>
					    <tr>
					      <td>Avec blocage inactif</td>
					      <td><input type="checkbox" name="blocageInactif"></td>
					    </tr>
					    <tr>
					      <td>Tri</td>
					      <td>
					      <select name="forSelectionTri" class="libelleCourt">
			                <option value="1">Num&eacute;ro</option>
			                <option value="2">Nom</option>
			              </select>
					      </td>
					    </tr>                         
          <%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<%	}%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>