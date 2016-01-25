<%-- tpl:insert page="/theme/capage.jtpl" --%><%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/capage/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="PIJ0007";
	globaz.ij.vb.prononces.IJSituationProfessionnelleViewBean viewBean = (globaz.ij.vb.prononces.IJSituationProfessionnelleViewBean) request.getAttribute("viewBean");
	String forIdPrononce = viewBean.getIdPrononce();
	String noAVS = viewBean.getNoAVS();
	String dateDebutPrononce = request.getParameter("dateDebutPrononce");
	String csTypeIJ = request.getParameter("csTypeIJ");
	String detailRequerant = viewBean.getDetailRequerant();
	if (csTypeIJ == null){
		csTypeIJ = "";
	}
	
	IFrameListHeight = "100";
	IFrameDetailHeight ="400";
	
	actionNew+="&idPrononce="+forIdPrononce+"&noAVS="+noAVS+"&csTypeIJ="+csTypeIJ+"&dateDebutPrononce="+dateDebutPrononce;
	
	bButtonNew = bButtonNew && viewBean.isModifierPermis() && viewBean.getSession().hasRight("ij.prononces.situationProfessionnelle.ajouter", FWSecureConstants.UPDATE);
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
	usrAction = "<%=globaz.ij.servlet.IIJActions.ACTION_SITUATION_PROFESSIONNELLE%>.lister";
	detailLink = servlet + "?userAction=<%=globaz.ij.servlet.IIJActions.ACTION_SITUATION_PROFESSIONNELLE%>.afficher&_method=add&idPrononce=<%=forIdPrononce%>&noAVS=<%=noAVS%>&dateDebutPrononce=<%=dateDebutPrononce%>&csTypeIJ=<%=csTypeIJ%>";
	
	function arret() {
		document.location.href = servlet + "?userAction=<%=globaz.apg.servlet.IAPActions.ACTION_DROIT_LAPG%>.chercher";
	}
	
	function onClickNew() {
		return 1;
	}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_SITPROS"/><%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><B><ct:FWLabel key="JSP_DETAIL_REQUERANT"/></B></TD>
							<TD colspan="3">
								<INPUT type="text" value="<%=detailRequerant%>" size="100" class="disabled" readonly>
								<INPUT type="hidden" name="detailRequerant" value="<%=detailRequerant%>">
								<INPUT type="hidden" name="csTypeIJ" value="<%=csTypeIJ%>">
							</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_DATE_DEBUT_PRONONCE"/></TD>
							<TD><INPUT type="text" name="dateDebutPrononce" class="disabled" readonly value="<%=dateDebutPrononce%>"></TD>
							<TD colspan="2">&nbsp;</TD>
						</TR>
						<TR>
							<TD><INPUT type="hidden" name="forIdPrononce" value="<%=forIdPrononce%>"></TD>
						</TR>
	 					<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyClose.jspf" %>
<%-- /tpl:insert --%>