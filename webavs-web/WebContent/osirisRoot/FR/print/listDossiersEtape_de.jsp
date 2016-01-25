
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA2026"; %>
<%@ page import="globaz.globall.util.*" %>
<%@ page import="globaz.osiris.servlet.action.CADefaultServletAction" %>
<%@ page import="globaz.osiris.db.print.*" %>

<%@ page import="globaz.osiris.db.contentieux.*" %>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%
	CAListDossiersEtapeViewBean viewBean = (CAListDossiersEtapeViewBean) session.getAttribute("viewBean");
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> <%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
<%
userActionValue = "osiris.print.listDossiersEtape.executer";
%>
top.document.title = "Liste - Impression de la liste des sections pour une étape  - " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>Impression de la liste des sections pour une étape<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
	<tr>
		<td class="label">E-mail</td>
		<td class="control">
			<input type="text" name="eMailAddress" value="<%=viewBean.getEMailAddress()%>" class="libelleLong">
		</td>
	</tr>
	<tr>
		<td nowrap width="25%">Séquence</TD>
		<td nowrap width="25%">
		  <select name="sequence" >
		    <option selected value="-1">Toutes</option>
		    <%CASequenceContentieux tempSeqCont;
				CASequenceContentieuxManager manSeqCont = new CASequenceContentieuxManager();
				manSeqCont.setSession(viewBean.getSession());
				manSeqCont.find();
				for(int i = 0; i < manSeqCont.size(); i++){
							tempSeqCont = (CASequenceContentieux)manSeqCont.getEntity(i); %>
		    <option value="<%=tempSeqCont.getIdSequenceContentieux()%>"><%=tempSeqCont.getDescription()%></option>
		    <%}%>
		  </select>
		</td>
		<td nowrap>&nbsp; </TD>
		<td nowrap width="8%">&nbsp;</TD>
	</tr>
	<tr>
		<td class="label">Etape</td>
		<td class="control">
			<select name="csEtape">
	                <%CAEtape tempEtape;
				    CAEtapeManager manEtape = new CAEtapeManager();
					manEtape.setSession(viewBean.getSession());
				    manEtape.find();
					for (int i=0; i < manEtape.size(); i++) {
						tempEtape = (CAEtape) manEtape.getEntity(i); %>
	                	<option value="<%=tempEtape.getTypeEtape()%>"><%=tempEtape.getDescription()%></option>
	                <%	} %>
	        </select>
         </TD>
	</tr>
<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<%	}%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>