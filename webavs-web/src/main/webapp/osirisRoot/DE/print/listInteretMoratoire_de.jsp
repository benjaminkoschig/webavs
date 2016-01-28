<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA2004"; %>
<%@ page import="globaz.globall.util.*" %>
<%@ page import="globaz.osiris.db.print.*" %>
<%@ page import="globaz.osiris.servlet.action.CADefaultServletAction" %>
<%
globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();

CAListInteretMoratoireViewBean viewBean = (CAListInteretMoratoireViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
userActionValue = globaz.osiris.application.CAApplication.DEFAULT_OSIRIS_NAME + ".print.listInteretMoratoire.executer";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<script>
function checkSuspens()
{
	if(document.forms['mainForm'].elements['suspens'].checked) {
		document.forms['mainForm'].elements['idJournalFacturation'].disabled = true;
		document.forms['mainForm'].elements['idJournalFacturation'].value = '';
	}
	else {
		document.forms['mainForm'].elements['idJournalFacturation'].disabled = false;
	}
		
}
</script>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Liste der VerzugszinsVerfügungen<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						    <tr>
						      <td>E-Mail</td>
						      <td>
						      	<input type="hidden" name="forSelectionTri" value="rolenumeronom">
						      	<input type="text" name="eMailAddress" value="<%=viewBean.getEMailAddress()%>" style="width:285">
						      </td>
						    </tr>
						    <tr>
						      <td style="width: 200px;">Zinsart</td>
						      <td style="width: 845px;">
						      	<ct:FWSystemCodeSelectTag
				           			name="idGenreInteret" 
				           			defaut="" 
				           			codeSystemManager="<%=globaz.osiris.translation.CACodeSystem.getLcsGenreInteret(objSession)%>"
				           		/>
				           		<script language="Javascript">
					           	document.getElementById("idGenreInteret").style.width=285;
					           	</script>
						      </td>
						    </tr>
						    <tr>
						      <td>Grund</td>
						      <td>
			<%
			   	java.util.HashSet except = new java.util.HashSet();
			   	except.add(globaz.osiris.db.interets.CAInteretMoratoire.CS_MANUEL);
			   	except.add(globaz.osiris.db.interets.CAInteretMoratoire.CS_AUTOMATIQUE);            	
			%>
			
						      	<ct:FWSystemCodeSelectTag
				           			name="idMotifCalcul" 
				           			defaut="" 
				           			codeSystemManager="<%=globaz.osiris.translation.CACodeSystem.getLcsMotifDecisionInteret(objSession)%>"
									except="<%=except%>"
				           		/>
				           		<script language="Javascript">
					           	document.getElementById("idMotifCalcul").style.width=160;
					           	</script>
						      </td>
						    </tr>
						    <tr>
						      <td>Berechnungsjournal-Nr.</td>
						      <td><input name="idJournalCalcul" value="" /></td>
						    </tr>
						    <tr>
						      <td>Fakturierungsjournal-Nr.</td>
						      <td><input name="idJournalFacturation" value="" disabled="disabled" /></td>
						    </tr>
						    <tr>
						      <td>Berechnungsdatum</td>
						      <td>von <ct:FWCalendarTag name="dateCalculDebut" value="" /> <span style="margin-left:10px">bis</span> <ct:FWCalendarTag name="dateCalculFin" value="" /></td>
						    </tr>						    
						    <tr>
						      <td>Pendent</td>
						      <td><input name="suspens" type="checkbox" value="0" onclick="checkSuspens()" checked="checked"></td>
						    </tr>
												
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>
