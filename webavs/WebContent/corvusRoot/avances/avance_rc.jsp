<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>

<%@ page import="globaz.commons.nss.NSUtil"%>
<%@ page import="globaz.corvus.api.avances.IREAvances"%>
<%@ page import="globaz.corvus.db.avances.REAvanceManager"%>
<%@ page import="globaz.corvus.vb.avances.REAvanceViewBean"%>
<%@ page import="globaz.corvus.vb.demandes.RENSSDTO"%>
<%@ page import="globaz.globall.db.BSession"%>
<%@ page import="globaz.prestation.interfaces.tiers.PRTiersHelper"%>
<%@ page import="globaz.prestation.interfaces.tiers.PRTiersWrapper"%>
<%@ page import="globaz.prestation.jsp.taglib.PRDisplayRequerantInfoTag"%>
<%@ page import="globaz.prestation.tools.PRSessionDataContainerHelper"%>
<%@ page import="globaz.pyxis.application.TIApplication"%>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/corvusRoot/corvustaglib.tld" prefix="re" %>

<%@ include file="/theme/find/header.jspf" %>
<%
	// Les labels de cette page commence par la préfix "JSP_LOT_R"

	idEcran = "PRE0080";
	rememberSearchCriterias = true;
	bButtonNew = bButtonNew;
%>
<%@ include file="/theme/find/javascripts.jspf" %>

<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="menu" />
<ct:menuChange displayId="options" menuId="corvus-optionsempty" />

<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js">
</script>

<script type="text/javascript">
	bFind = true;
	usrAction = "corvus.avances.avance.lister";
</script>

<%@ include file="/theme/find/bodyStart.jspf" %>
				<ct:FWLabel key="JSP_AVANCE_R_TITRE" />
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<tr>
<%
	String idTiersRequerant =  request.getParameter("idTierRequerant");
%>								<td colspan="2">
									<re:PRDisplayRequerantInfoTag	session="<%=(BSession) controller.getSession()%>" 
																	idTiers="<%=idTiersRequerant%>" 
																	style="<%=PRDisplayRequerantInfoTag.STYLE_CONDENSED_BEN%>" />
								</td>
<%
	if ( PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO) != null) {
		RENSSDTO nssDto = (RENSSDTO) PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO);
		String nssF = TIApplication.pyxisApp().getAvsFormater().format("756" + nssDto.getNSS());
	}
%>								<input type="hidden" name="idTiersRequerant" value="<%=idTiersRequerant%>" />
							</tr>
							<tr>
								<td>
									<ct:FWLabel key="JSP_AVANCE_R_A_PARTIR_DE" />
									&nbsp; 
									<input	id="fromDateDebutAcompte"
											name="fromDateDebutAcompte"
											data-g-calendar="type:default" 
											value="" />
								</td>
								<td>
									<ct:FWLabel key="JSP_AVANCE_R_ETAT_ACOMPTE" />
									&nbsp;
									<ct:select name="forCsEtatAcomptes" wantBlank="true">
										<ct:optionsCodesSystems csFamille="<%=IREAvances.CS_GROUPE_ETAT_ACOMPTES%>" />
									</ct:select>
								</td>
							</tr>
<%@ include file="/theme/find/bodyButtons.jspf" %>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%@ include file="/theme/find/bodyClose.jspf" %>
