<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%@page import="globaz.naos.util.AFUtil"%>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran ="CAF0039";
	String noAff = AFUtil.getAffiliationEnCours(session,request);
	actionNew =  servletContext + mainServletPath + "?userAction=" + request.getParameter("userAction").substring(0,request.getParameter("userAction").lastIndexOf('.')) + ".afficherPreSaisie&_method=add&noAff="+noAff;
	String jspLocation = servletContext + mainServletPath + "Root/affilie_select.jsp";
	int autoDigiAff = globaz.naos.util.AFUtil.getAutoDigitAff(session);
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<script>
usrAction = "naos.releve.apercuReleve.lister";
if('<%=noAff%>'!='') {
	bFind = true;
}
</script>
<% if("".equals(noAff)) { %>
	<ct:menuChange displayId="menu" menuId="AFMenuPrincipal"/>
	<ct:menuChange displayId="options" menuId="AFMenuVide" showTab="menu"/>
<% } %>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>
				Verwaltung der Auszüge<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD nowrap width="140" height="31">Abr.-Nr.</TD>
							<TD nowrap width="225">
								<ct:FWPopupList name="forAffilieNumero" 
									value="<%=noAff%>"
									className="libelle" 
									jspName="<%=jspLocation%>" 
									autoNbrDigit="<%=autoDigiAff%>" 
									size="15"
									minNbrDigit="3"
									onChange=""
								/>
								<script> document.getElementsByName("forAffilieNumero")[0].onchange = function() {action(COMMIT);}</script>
								
								<IMG
									src="<%=servletContext%>/images/down.gif"
									alt="presser sur la touche 'flèche bas' pour effectuer une recherche"
									title="presser sur la touche 'flèche bas' pour effectuer une recherche"
									onclick="if (document.forms[0].elements('forAffilieNumero').value != '') forAffilieNumeroPopupTag.validate();">
							</TD>
							<TD nowrap width="110">Status</TD>
							<TD nowrap width="160">
								<ct:FWCodeSelectTag 
	                				name="forEtat" 
									defaut=""
									codeType="VEETATRELE"
									wantBlank="true"/> 									
							</TD>
						</TR>
						<TR>
							<TD nowrap width="140" height="31">Datum Beginn des Auszugs</TD>
							<TD nowrap width="220">
								<ct:FWCalendarTag name="forDateDebut" 
									displayType ="month" value="" />Monat.Jahr
								<!--ct:FWCalendarTag name="forDateDebut" value="" /-->
								<!--script language="Javascript" >
									document.getElementById("dateDebut").style.width=135;
								</script-->
							</TD>
							<TD nowrap width="110">Mitarbeiter</TD>
							<TD nowrap width="160">
								<input name="forCollaborateur">
							</TD>
						</TR>
						<TR>
							<TD nowrap width="140" height="31">Rechnungsnummer</TD>
							<TD nowrap width="220">
								<input name="forIdExterneFacture">
							</TD>
							<TD nowrap width="110">Fakturierungsjournal</TD>
							<TD nowrap width="160">
								<input name="forJob">
							</TD>
						</TR>
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>