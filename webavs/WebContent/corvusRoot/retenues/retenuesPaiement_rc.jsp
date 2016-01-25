<%-- tpl:insert page="/theme/capage.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/capage/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/corvusRoot/corvustaglib.tld" prefix="re" %>
<%
	// Les labels de cette page commence par la préfix "JSP_RET_R"

	idEcran="PRE0020";

	globaz.corvus.vb.retenues.RERetenuesPaiementViewBean viewBean = (globaz.corvus.vb.retenues.RERetenuesPaiementViewBean) request.getAttribute("viewBean");

	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");

	//actionNew =  servletContext + mainServletPath + "?userAction=" + globaz.corvus.servlet.IREActions.ACTION_RETENUES_SUR_PMT + ".afficher&_method=add";

	String idTierRequerant = request.getParameter("idTierRequerant");
	String idTiersBeneficiaire = request.getParameter("idTiersBeneficiaire");
	String idRenteAccordee = request.getParameter("idRenteAccordee");
	String noDemandeRente = request.getParameter("noDemandeRente");
	String idBaseCalcul = request.getParameter("idBaseCalcul");
	String csTypeBasesCalcul = request.getParameter("csTypeBasesCalcul");
	String csEtatRenteAccordee = request.getParameter("csEtatRenteAccordee");
	String dateFinDroit = request.getParameter("dateFinDroit");
	String isPreparationDecisionValide = request.getParameter("isPreparationDecisionValide");
	
	String montantRenteAccordee = request.getParameter("montantRenteAccordee");
	if (JadeStringUtil.isBlank(montantRenteAccordee)){
		montantRenteAccordee = viewBean.getMontantRenteAccordee();
	}
	if (idTierRequerant == null){
		idTierRequerant = viewBean.getForIdTiersBeneficiaire();
	}
	if (idTiersBeneficiaire == null){
		idTiersBeneficiaire = viewBean.getForIdTiersBeneficiaire();
		if(JadeStringUtil.isBlank(idTiersBeneficiaire)){
			idTiersBeneficiaire = idTierRequerant;
		}
	}

	if (idRenteAccordee == null){
		idRenteAccordee = viewBean.getForIdRenteAccordee();
	}


	
	String menuOptionToLoad = request.getParameter("menuOptionToLoad");

	IFrameListHeight 	= "170";
	IFrameDetailHeight 	= "400";

	actionNew += "&forIdTiersBeneficiaire="+idTiersBeneficiaire+"&forIdRenteAccordee="+idRenteAccordee+
	             "&montantRenteAccordee="+montantRenteAccordee+"&menuOptionToLoad="+menuOptionToLoad;
	
	bButtonNew = bButtonNew && controller.getSession().hasRight(IREActions.ACTION_RETENUES_SUR_PMT, FWSecureConstants.UPDATE);

%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.corvus.utils.REPmtMensuel"%>
<%@page import="globaz.corvus.api.basescalcul.IRERenteAccordee"%>
<%if(JadeStringUtil.isNull(menuOptionToLoad) || JadeStringUtil.isEmpty(menuOptionToLoad)){%>
	<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="globaz.corvus.servlet.IREActions"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="menu"/>
	<ct:menuChange displayId="options" menuId="corvus-optionsempty"/>
<%}else if("rentesaccordees".equals(menuOptionToLoad)){%>
	<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="options"/>
	<ct:menuChange displayId="options" menuId="corvus-optionsrentesaccordees">
		<ct:menuSetAllParams key="selectedId" value="<%=idRenteAccordee%>"/>
		<ct:menuSetAllParams key="noDemandeRente" value="<%=noDemandeRente%>"/>
		<ct:menuSetAllParams key="idTierRequerant" value="<%=idTierRequerant%>"/>
		<ct:menuSetAllParams key="idTiersBeneficiaire" value="<%=idTiersBeneficiaire%>"/>
		<ct:menuSetAllParams key="idRenteAccordee" value="<%=idRenteAccordee%>"/>
		<ct:menuSetAllParams key="idBaseCalcul" value="<%=idBaseCalcul%>"/>
		<ct:menuSetAllParams key="csTypeBasesCalcul" value="<%=csTypeBasesCalcul%>"/>
		<% if ((IRERenteAccordee.CS_ETAT_AJOURNE.equals(csEtatRenteAccordee)
			    || IRERenteAccordee.CS_ETAT_CALCULE.equals(csEtatRenteAccordee)
			    || IRERenteAccordee.CS_ETAT_DIMINUE.equals(csEtatRenteAccordee))
			    
			  || (!globaz.jade.client.util.JadeStringUtil.isBlankOrZero(dateFinDroit))
			  || !REPmtMensuel.isValidationDecisionAuthorise((globaz.globall.db.BSession)controller.getSession())) { %>
			<ct:menuActivateNode active="no" nodeId="optdiminution"/>
		<%}%>
		<%if ("false".equals(isPreparationDecisionValide)){%>
			<ct:menuActivateNode active="no" nodeId="preparerDecisionRA"/>
		<%}%>
	</ct:menuChange>
<%}%>

<SCRIPT language="javascript">

	bFind = true;
	detailLink = "<%=actionNew%>";
	usrAction = "<%=globaz.corvus.servlet.IREActions.ACTION_RETENUES_SUR_PMT%>.lister";

	function rechargerPage() {
		document.forms[0].elements("userAction").value = "<%=globaz.corvus.servlet.IREActions.ACTION_RETENUES_SUR_PMT%>.chercher";
		document.forms[0].target = "fr_main";
		document.forms[0].submit();
	}

	function onClickNew() {
		// desactive le grisement des boutons dans l'ecran rc quand on clique sur le bouton new
	}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_RET_R_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
				<TR>
					<TD>
						<TABLE border="0" cellspacing="0" cellpadding="0" width="100%">			
							<TR>
								<TD><b><ct:FWLabel key="JSP_RET_R_BENEFICIAIRE"/></b></TD>
								<TD colspan="8">&nbsp;</TD>
							</TR>
							<TR><TD colspan="9">&nbsp;</TD></TR>
							<TR>		
								<TD colspan="2"><ct:FWLabel key="JSP_RET_R_TIERS"/></TD>
								<TD colspan="7">
										<re:PRDisplayRequerantInfoTag
												session="<%=(globaz.globall.db.BSession)controller.getSession()%>"
												idTiers="<%=idTiersBeneficiaire%>"
												style="<%=globaz.prestation.jsp.taglib.PRDisplayRequerantInfoTag.STYLE_LONG_WITHOUT_LABEL%>" 
												/>
								</TD>
							</TR>
							<TR><TD colspan="9">&nbsp;</TD></TR>
							<TR>
								<TD colspan="2"><ct:FWLabel key="JSP_RET_R_RENTE_ACCORDEE_NO"/></TD>
								<TD >
									<INPUT style="width:100px;" type="text" id="forIdRenteAccordee" name="forIdRenteAccordee" value="<%=idRenteAccordee%>" class="numeroCourtDisabled">
									<SCRIPT>
										document.getElementById('forIdRenteAccordee').style.textAlign='right';
									</SCRIPT>
									<INPUT type="hidden" name="forIdTiersBeneficiaire" value="<%=idTierRequerant%>">
									<INPUT type="hidden" name="montantRenteAccordee" value="<%=montantRenteAccordee%>">
									<INPUT type="hidden" name="idTierRequerant" value="<%=idTierRequerant%>">
									<input type="hidden" name="menuOptionToLoad" value="<%=menuOptionToLoad%>">
									&nbsp;
								</TD>
								<TD><ct:FWLabel key="JSP_RET_R_MONTANT"/></TD>
								<TD><INPUT type="text" name="montant" value="<%=new FWCurrency(montantRenteAccordee).toStringFormat()%>" class="montantDisabled"></TD>
								<TD colspan="2"><ct:FWLabel key="JSP_RET_R_ETAT"/>&nbsp;</TD>
								<TD colspan="2">
									<SELECT name="forEtat">
										<OPTION value=""></OPTION>
										<OPTION value="enCours"><ct:FWLabel key="JSP_RET_D_EN_COURS"/></OPTION>
										<OPTION value="futures"><ct:FWLabel key="JSP_RET_D_FUTURES"/></OPTION>
										<OPTION value="liquidees"><ct:FWLabel key="JSP_RET_D_LIQUIDES"/></OPTION>
									</SELECT>
								</TD>
							</TR>
						</TABLE>
					</TD>
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