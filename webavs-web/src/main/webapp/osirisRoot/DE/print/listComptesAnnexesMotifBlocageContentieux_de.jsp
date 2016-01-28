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
top.document.title = "Ausdruck der Liste der Abrechnungskonti mit Rechtspflegesperrungsgrund - " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Ausdruck der Liste der Abrechnungskonti mit Rechtspflegesperrungsgrund<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
						<tr>
					      <td style="width: 130px;">E-Mail</td>
					      <td style="width: 837px;"><input type="text" name="eMailAddress" value="<%=viewBean.getEMailAddress()%>" class="libelleLong"></td>
					    </tr>
					    <tr>
					      <td>Kontoart</td>
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
					      <td>Grund</td>
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
					      <td>Saldo</td>
					      <td>
					      <select name="forSelectionCompte" style="width : 2.5cm;">
                			<option value="">Alle</option>
                			<option value="1">offene</option>
                			<option value="2">saldierte</option>
              			  </select>
					      </td>
					    </tr>
					    <tr>
					      <td>Mit inaktiver Sperrung</td>
					      <td><input type="checkbox" name="blocageInactif"></td>
					    </tr>
					    <tr>
					      <td>Auswahl</td>
					      <td>
					      <select name="forSelectionTri" class="libelleCourt">
			                <option value="1">Nummer</option>
			                <option value="2">Name</option>
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