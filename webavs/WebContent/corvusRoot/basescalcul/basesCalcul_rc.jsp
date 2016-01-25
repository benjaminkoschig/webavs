<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/corvusRoot/corvustaglib.tld" prefix="re" %>
<%
	// Les labels de cette page commence par la préfix "JSP_BAC_R"

	idEcran="PRE0014";

	globaz.corvus.vb.basescalcul.REBasesCalculViewBean viewBean = (globaz.corvus.vb.basescalcul.REBasesCalculViewBean) request.getAttribute("viewBean");

	String noDemandeRente = request.getParameter("noDemandeRente");
	String idTierRequerant = request.getParameter("idTierRequerant");
	String idTiersBeneficiaire = request.getParameter("idTiersBeneficiaire");
	String idRenteCalculee = request.getParameter("idRenteCalculee");

	actionNew =  servletContext + mainServletPath + "?userAction=" + globaz.corvus.servlet.IREActions.ACTION_SAISIE_MANUELLE_RABCPD	+ ".afficher&_method=add&noDemandeRente="+noDemandeRente+"&idTierRequerant="+idTierRequerant+"&idTiersBeneficiaire="+idTiersBeneficiaire;
	userActionNew =  globaz.corvus.servlet.IREActions.ACTION_BASES_DE_CALCUL + ".afficher";

	bButtonNew = viewBean.isNouveau(idRenteCalculee) && controller.getSession().hasRight(IREActions.ACTION_BASES_DE_CALCUL, FWSecureConstants.UPDATE);

%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.corvus.servlet.IREActions"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="corvus-optionsempty"/>

<SCRIPT language="javascript">
	bFind = true;
	usrAction = "corvus.basescalcul.basesCalcul.lister";
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_BAC_R_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD>
								<TABLE border="0" cellspacing="0" cellpadding="0" width="100%">
									<TR>
										<TD colspan="6">
											<input type="hidden" name="noDemandeRente" value="<%=noDemandeRente%>">
											<input type="hidden" name="idTierRequerant" value="<%=idTierRequerant%>">
											<input type="hidden" name="idTiersBeneficiaire" value="<%=idTiersBeneficiaire%>">
											<input type="hidden" name="forIdRenteCalculee" value="<%=idRenteCalculee%>">
											<input type="hidden" name="idBasesCalcul" value="null">
											<re:PRDisplayRequerantInfoTag
													session="<%=(globaz.globall.db.BSession)controller.getSession()%>"
													idTiers="<%=idTierRequerant%>"
													style="<%=globaz.prestation.jsp.taglib.PRDisplayRequerantInfoTag.STYLE_CONDENSED%>"/>
										</TD>
									</TR>
									<TR><TD colspan="6">&nbsp;</TD></TR>
									<TR>
										<TD colspan="6"><LABEL for="noDemande"><ct:FWLabel key="JSP_BAC_R_DEMANDE_NO"/></LABEL>
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										<INPUT type="text" name="noDemande" value="<%=noDemandeRente%>" class="disabled" readonly></TD>
									</TR>
								</TABLE>
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