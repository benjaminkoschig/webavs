<%-- tpl:insert page="/theme/capage.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/capage/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/corvusRoot/corvustaglib.tld" prefix="re" %>
<%
	// Les labels de cette page commence par la préfix "JSP_FAR_R"
	idEcran="PRE0025";

	REFactureARestituerViewBean viewBean = (REFactureARestituerViewBean) request.getAttribute("viewBean");
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");

	if(viewBean == null){
		viewBean = new REFactureARestituerViewBean();
		viewBean.setSession((BSession)controller.getSession());
	}
	
	String idRenteAccordee = request.getParameter("idRenteAccordee");
	if(JadeStringUtil.isIntegerEmpty(idRenteAccordee)){
		idRenteAccordee = "";
	}
	
	String idTiersBeneficiaire = request.getParameter("idTiersBeneficiaire");
	if(JadeStringUtil.isIntegerEmpty(idTiersBeneficiaire)){
		idTiersBeneficiaire = request.getParameter("idTierRequerant");
		if(JadeStringUtil.isIntegerEmpty(idTiersBeneficiaire)){
			idTiersBeneficiaire = "";
		}
	}
	
	String noDemandeRente = request.getParameter("noDemandeRente");
	String idBaseCalcul = request.getParameter("idBaseCalcul");
	String csTypeBasesCalcul = request.getParameter("csTypeBasesCalcul");
	String csEtatRenteAccordee = request.getParameter("csEtatRenteAccordee");
	String dateFinDroit = request.getParameter("dateFinDroit");
	String isPreparationDecisionValide = request.getParameter("isPreparationDecisionValide");
	
	String menuOptionToLoad = request.getParameter("menuOptionToLoad");

	IFrameListHeight 	= "200";
	IFrameDetailHeight 	= "250";
	
	bButtonNew = false;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.commons.nss.*"%>
<%@page import="globaz.prestation.tools.PRSessionDataContainerHelper"%>
<%@page import="globaz.corvus.vb.rentesaccordees.REFactureARestituerViewBean"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.corvus.api.basescalcul.IREFactureARestituer"%>
<%@page import="globaz.corvus.db.rentesaccordees.REFacturesARestituerManager"%>
<%@page import="globaz.globall.db.BSession"%>
<%@page import="globaz.corvus.api.basescalcul.IRERenteAccordee"%>
<%@page import="globaz.corvus.vb.demandes.RENSSDTO"%>
<%@page import="globaz.corvus.vb.demandes.REDemandeParametresRCDTO"%>
<%@page import="globaz.globall.db.BSession"%>
<%@page import="globaz.corvus.utils.REPmtMensuel"%>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/nss.js"></script>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>

<%if(JadeStringUtil.isNull(menuOptionToLoad) || JadeStringUtil.isEmpty(menuOptionToLoad)){%>
	<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="menu"/>
	<ct:menuChange displayId="options" menuId="corvus-optionsempty"/>
<%}else if("rentesaccordees".equals(menuOptionToLoad)){%>
	<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="options"/>
	<ct:menuChange displayId="options" menuId="corvus-optionsrentesaccordees">
		<ct:menuSetAllParams key="selectedId" value="<%=idRenteAccordee%>"/>
		<ct:menuSetAllParams key="idRenteAccordee" value="<%=idRenteAccordee%>"/>
		<ct:menuSetAllParams key="noDemandeRente" value="<%=noDemandeRente%>"/>
		<ct:menuSetAllParams key="idTiersRequerant" value="<%=idTiersBeneficiaire%>"/>
		<ct:menuSetAllParams key="idTiersBeneficiaire" value="<%=idTiersBeneficiaire%>"/>
		<ct:menuSetAllParams key="idBaseCalcul" value="<%=idBaseCalcul%>"/>
		<ct:menuSetAllParams key="csTypeBasesCalcul" value="<%=csTypeBasesCalcul%>"/>
		<% if ((IRERenteAccordee.CS_ETAT_AJOURNE.equals(csEtatRenteAccordee)
			    || IRERenteAccordee.CS_ETAT_CALCULE.equals(csEtatRenteAccordee)
			    || IRERenteAccordee.CS_ETAT_DIMINUE.equals(csEtatRenteAccordee))
			    
			  || (!globaz.jade.client.util.JadeStringUtil.isBlankOrZero(dateFinDroit))
			  || !REPmtMensuel.isValidationDecisionAuthorise((BSession)controller.getSession())) { %>
			<ct:menuActivateNode active="no" nodeId="optdiminution"/>
		<%}%>
		<%if ("false".equals(isPreparationDecisionValide)){%>
			<ct:menuActivateNode active="no" nodeId="preparerDecisionRA"/>
		<%}%>
	</ct:menuChange>
<%}%>

<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<SCRIPT language="javascript">

	bFind = true;
	detailLink = "<%=actionNew%>";
	usrAction = "<%=globaz.corvus.servlet.IREActions.ACTION_FACTURE_A_RESTITUER%>.lister";

	function onClickNew() {
		// desactive le grisement des boutons dans l'ecran rc quand on clique sur le bouton new
	}
	
	function clearFields () {
		document.getElementsByName("likeNumeroAVS")[0].value="";
		document.getElementsByName("forDateNaissance")[0].value="";
		document.getElementsByName("likeNom")[0].value="";
		document.getElementsByName("likePrenom")[0].value="";
		document.getElementsByName("partiallikeNumeroAVS")[0].value="";
		document.getElementsByName("forCsEtat")[0].value="<%=REFacturesARestituerManager.CLE_NON_TRAITE%>";
		
		document.forms[0].elements('partiallikeNumeroAVS').focus();
	}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_FAR_R_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						
						<TR>
							<td>
								<table>					
									<%if(JadeStringUtil.isIntegerEmpty(idRenteAccordee)){%>
									<TR>
										<TD><ct:FWLabel key="JSP_FAR_R_NSS"/></TD>
										<%if (null!=PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO) &&
											  PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO) instanceof RENSSDTO) {
											RENSSDTO nssDto = (RENSSDTO) PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO);
											%>
											<TD>
												<ct1:nssPopup avsMinNbrDigit="99"
														  nssMinNbrDigit="99"
														  newnss=""
														  name="likeNumeroAVS"
														  onChange=""
														  value="<%=nssDto.getNSSForSearchField()%>" />
											</TD>
										<%} else {%>
											<TD>
												<ct1:nssPopup avsMinNbrDigit="99"
														  nssMinNbrDigit="99"
														  newnss=""
														  name="likeNumeroAVS"
														  onChange="" />
											</TD>
										<%}%>
										<TD><LABEL for="likeNom"><ct:FWLabel key="JSP_FAR_R_NOM"/></LABEL>&nbsp;</TD>
										<%if (null!=PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO) &&
											  PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO) instanceof  REDemandeParametresRCDTO) {%>
											<TD><INPUT type="text" name="likeNom" value="<%=((REDemandeParametresRCDTO) PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO)).getLikeNom()%>"></TD>
										<%} else {%>
											<TD><INPUT type="text" name="likeNom" value=""></TD>
										<%}%>
										<TD><LABEL for="likePrenom"><ct:FWLabel key="JSP_FAR_R_PRENOM"/></LABEL>&nbsp;</TD>
										<%if (null!=PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO) &&
											  PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO) instanceof  REDemandeParametresRCDTO) {%>
											<TD><INPUT type="text" name="likePrenom" value="<%=((REDemandeParametresRCDTO) PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO)).getLikePrenom()%>"></TD>
										<%} else {%>
											<TD><INPUT type="text" name="likePrenom" value=""></TD>
										<%}%>
									</TR>	
									<TR>
										<TD><ct:FWLabel key="JSP_FAR_R_DATE_NAISSANCE"/></TD>
										<%if (null!=PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO) &&
											  PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO) instanceof  REDemandeParametresRCDTO) {%>
											<TD>
												<input	id="forDateNaissance" 
														name="forDateNaissance"
														data-g-calendar="yearRange:¦1900:<%=java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)%>¦"
														value="<%=((REDemandeParametresRCDTO) PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO)).getForDateNaissance()%>" />
											</TD>
										<%} else {%>
											<TD>
												<input	id="forDateNaissance" 
														name="forDateNaissance"
														data-g-calendar="yearRange:¦1900:<%=java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)%>¦"
														value="" />
											</TD>
										<%}%>
										<TD><ct:FWLabel key="JSP_FAR_R_SEXE"/></TD>
										<TD colspan="3"><ct:FWCodeSelectTag name="forCsSexe" codeType="PYSEXE" defaut="" wantBlank="true"/></TD>
									</TR>
									<TR>
										<TD colspan="6">&nbsp;</TD>
									</TR>	
									<TR>
										<TD><ct:FWLabel key="JSP_FAR_R_ETAT"/></TD>
										<TD colspan="5">
											<ct:FWListSelectTag name="forCsEtat" data="<%=viewBean.getEtatFactureData()%>" defaut="<%=REFacturesARestituerManager.CLE_NON_TRAITE%>"/>
										</TD>
									</TR>
									<TR><TD colspan="6">&nbsp;</TD></TR>
									<TR>
										<TD colspan="6"><input type="button" onclick="clearFields()" accesskey="C" value="Effacer">  [ALT+C] &nbsp;</TD>
									</TR>
									<%}else{%>
									<TR>
										<TD><ct:FWLabel key="JSP_FAR_R_NSS"/></TD>
										<TD colspan="3">
												<re:PRDisplayRequerantInfoTag
														session="<%=(globaz.globall.db.BSession)controller.getSession()%>"
														idTiers="<%=idTiersBeneficiaire%>"
														style="<%=globaz.prestation.jsp.taglib.PRDisplayRequerantInfoTag.STYLE_CONDENSED_WITHOUT_LABEL%>"/>
										</TD>
										<TD><ct:FWLabel key="JSP_FAR_R_NO_RENTE_ACCORDEE"/></TD>
										<TD>
											<INPUT type="text" name="forIdRA" value="<%=idRenteAccordee%>" class="disabled" readonly tabindex="-1">
											<input type="hidden" name="menuOptionToLoad" value="<%=menuOptionToLoad%>">	
										</TD>
									</TR>
									<%}%>
								</table>
							</td>
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