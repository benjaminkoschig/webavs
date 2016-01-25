<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	// Les labels de cette page commence par la préfix "JSP_MVE_R"

	idEcran="PRE0017";
	
	globaz.corvus.vb.rentesaccordees.REPrestationsDuesJointDemandeRenteViewBean viewBean = (globaz.corvus.vb.rentesaccordees.REPrestationsDuesJointDemandeRenteViewBean) request.getAttribute("viewBean");

	String noDemandeRente = request.getParameter("noDemandeRente");
	String idTierRequerant = request.getParameter("idTierRequerant");
	String idTiersBeneficiaire = request.getParameter("idTiersBeneficiaire");
	String idRenteAccordee = request.getParameter("idRenteAccordee");
	String idBaseCalcul = request.getParameter("idBaseCalcul");
	String csTypeBasesCalcul = request.getParameter("csTypeBasesCalcul");
	String csEtatRenteAccordee = request.getParameter("csEtatRenteAccordee");
	String dateFinDroit = request.getParameter("dateFinDroit");
	String isPreparationDecisionValide = request.getParameter("isPreparationDecisionValide");
	
	String menuOptionToLoad = request.getParameter("menuOptionToLoad");

	actionNew =  servletContext + mainServletPath + "?userAction=" + globaz.corvus.servlet.IREActions.ACTION_PRESTATIONS_DUES_JOINT_DEMANDE_RENTE + ".afficher&_method=add&noDemandeRente="+noDemandeRente+"&idTierRequerant="+idTierRequerant+
			     "&idRenteAccordee="+idRenteAccordee+"&menuOptionToLoad="+menuOptionToLoad;
	userActionNew =  globaz.corvus.servlet.IREActions.ACTION_PRESTATIONS_DUES_JOINT_DEMANDE_RENTE + ".afficher";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.corvus.api.basescalcul.IRERenteAccordee"%>
<%@page import="globaz.corvus.utils.REPmtMensuel"%>

<%if(JadeStringUtil.isNull(menuOptionToLoad) || JadeStringUtil.isEmpty(menuOptionToLoad)){%>
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
			  || !REPmtMensuel.isValidationDecisionAuthorise(objSession)) { %>
			<ct:menuActivateNode active="no" nodeId="optdiminution"/>
		<%}%>
		<%if ("false".equals(isPreparationDecisionValide)){%>
			<ct:menuActivateNode active="no" nodeId="preparerDecisionRA"/>
		<%}%>
	</ct:menuChange>
<%}%>

<SCRIPT language="javascript">
	bFind = true;
	usrAction = "corvus.rentesaccordees.prestationsDuesJointDemandeRente.lister";
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_MVE_R_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD>
								<TABLE border="0" cellspacing="0" cellpadding="0" width="100%">
									<TR>
										<TD>
										<input type="hidden" name="noDemandeRente" value="<%=noDemandeRente%>">
										<input type="hidden" name="idTierRequerant" value="<%=idTierRequerant%>">
										<input type="hidden" name="csEtatRenteAccordee" value="<%=csEtatRenteAccordee%>">	
										<input type="hidden" name="menuOptionToLoad" value="<%=menuOptionToLoad%>">						
										<LABEL for="forNoRenteAccordee"><ct:FWLabel key="JSP_MVE_R_NO_RENTE_ACCORDEE"/></LABEL></TD>
										<TD><INPUT type="text" name="forNoRenteAccordee" value="<%=JadeStringUtil.isNull(idRenteAccordee)?"":idRenteAccordee%>" class="disabled" readonly></TD>
										<TD><LABEL for="forNoDemandeRente"><ct:FWLabel key="JSP_MVE_R_NO_DEMANDE_RENTE"/></LABEL></TD>
										<TD><INPUT type="text" name="forNoDemandeRente" value="<%=noDemandeRente%>" class="disabled" readonly></TD>
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