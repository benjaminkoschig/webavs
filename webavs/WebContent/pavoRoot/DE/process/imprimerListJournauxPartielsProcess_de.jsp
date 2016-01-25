<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
	<%	
		CIImprimerListJournauxPartielsProcessViewBean viewBean = (CIImprimerListJournauxPartielsProcessViewBean)session.getAttribute("viewBean");
		userActionValue = "pavo.process.imprimerListJournauxPartielsProcess.executer";
		idEcran ="A renseigner";
		String emailAdresse = !globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getEmailAddress())?viewBean.getEmailAddress():"";
		String jspLocation2 = servletContext + mainServletPath + "Root/tiForJournal_typeAff_select.jsp";
		int autoDigiAff = globaz.pavo.util.CIUtil.getAutoDigitAff(viewBean.getSession());
	%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Liste des journaux partiels<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

		
<%@page import="globaz.pavo.print.list.CIImprimerListJournauxPartielsProcess"%>
<%@page import="globaz.pavo.db.process.CIImprimerListJournauxPartielsProcessViewBean"%><tr>
					      <td>Ann&eacute;e</td>
					      <td><input type="text" name="forAnnee" size="4" maxlength="4" onkeypress="return filterCharForPositivInteger(window.event);"/></td>
					      <td>Type</td>
					      <td>
					      	<%
							java.util.HashSet typeCompte = new java.util.HashSet();
							typeCompte.add(globaz.pavo.db.inscriptions.CIJournal.CS_ASSURANCE_FACULTATIVE);
    						%>
					      	<ct:FWCodeSelectTag name="forIdType" defaut="" except="<%=typeCompte%>" codeType="CITYPINS" wantBlank="true"/>
					      </td>
					    </tr>
					    <tr>
					      <td>Num&eacute;ro d'affili&eacute;</td>
					      <td><ct:FWPopupList name="likeIdAffiliation" value="" className="libelle" jspName="<%=jspLocation2%>" autoNbrDigit="<%=autoDigiAff%>" size="14" minNbrDigit="3"/></td>
					    </tr>
					    <tr>
					      <td>Date de création</td>
					      <td>
					      	<ct:FWCalendarTag name="forDate" doClientValidation="CALENDAR" value="" />
				            <script>
				            	document.getElementById("forDate").onkeypress= new Function ("","return filterCharForPositivFloat(window.event);");
				            </script>
					      </td>
					      <td>Utilisateur</td>
					      <td><input type="text" name="fromUser" size="25"></td>
					     </tr>
					     <tr>
					      <td>Date de comptabilisation</td>
					      <td>
					      	<ct:FWCalendarTag name="fordateInscription" doClientValidation="CALENDAR" value="" />
				            <script>
				            	document.getElementById("fordateInscription").onkeypress= new Function ("","return filterCharForPositivFloat(window.event);");
				            </script>	
					      </td>
					      <td></td>
					      <td></td>
					    </tr>
					    <tr>						
							<td>E-mail</td><td><input type="text" name="eMailAddress" maxlength="40" size="40" style="width:8cm;" value="<%=emailAdresse%>">&nbsp;</td>
						</tr>

		<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>