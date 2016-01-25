<%-- tpl:insert page="/theme/capage.jtpl" --%><%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/capage/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran="PIJ0002";
globaz.ij.vb.prononces.IJMesureJointAgentExecutionViewBean viewBean = (globaz.ij.vb.prononces.IJMesureJointAgentExecutionViewBean) request.getAttribute("viewBean");
String noAVS = request.getParameter("noAVS");
String prenomNom = request.getParameter("prenomNom");
String dateDebutPrononce = request.getParameter("dateDebutPrononce");
String forIdPrononce = request.getParameter("forIdPrononce");
String csTypeIJ = request.getParameter("csTypeIJ");
actionNew+="&idPrononce="+forIdPrononce+"&noAVS="+noAVS+"&dateDebutPrononce="+dateDebutPrononce+"&csTypeIJ="+csTypeIJ;
IFrameDetailHeight = "260";
boolean hasRigthForNouveauAgeant =  viewBean.getSession().hasRight("ij.prononces.mesureJointAgentExecution.ajouter", FWSecureConstants.UPDATE);
bButtonNew = bButtonNew && viewBean.isModifierPermis() && hasRigthForNouveauAgeant;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="ij-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="ij-optionsempty"/>

<SCRIPT>
// pour le gestion des avertissements après l'action EcranSuivant
	var isModification = false;
	var isNouveau = true;

	bFind = true;
	usrAction = "<%=globaz.ij.servlet.IIJActions.ACTION_MESURE_JOINT_AGENT_EXECUTION%>.lister";
	detailLink = servlet + "?userAction=<%=globaz.ij.servlet.IIJActions.ACTION_MESURE_JOINT_AGENT_EXECUTION%>.afficher&_method=add&idPrononce=<%=forIdPrononce%>&noAVS=<%=noAVS%>&prenomNom=<%=prenomNom%>&dateDebutPrononce=<%=dateDebutPrononce%>&csTypeIJ=<%=csTypeIJ%>";

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_AGENTS_EXECUTION"/><%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR><TD><TABLE width="100%">
						<TR>
							<TD>
								<INPUT type="hidden" name="forIdPrononce" value="<%=forIdPrononce%>">
								<INPUT type="hidden" name="noAVS" value="<%=noAVS%>">
								<INPUT type="hidden" name="prenomNom" value="<%=prenomNom%>">
								<INPUT type="hidden" name="dateDebutPrononce" value="<%=dateDebutPrononce%>">						
								<INPUT type="hidden" name="csTypeIJ" value="<%=csTypeIJ%>">
								<b><ct:FWLabel key="JSP_DETAIL_REQUERANT"/></b>
							</TD>
							<TD colspan="3"><INPUT type="text" value="<%=viewBean.getDetailRequerantDetail()%>" size="100" class="disabled" readonly></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_DATE_DEBUT_PRONONCE"/></TD>
							<TD><INPUT type="text" name="dateDebutPrononce" value="<%=dateDebutPrononce%>" readonly class="disabled"></TD>
							<td colspan="2">&nbsp;</td>
						</TR>
						</TABLE></TD></TR>
	 					<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyClose.jspf" %>
<%-- /tpl:insert --%>