<%-- tpl:insert page="/theme/capage.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/capage/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran="PAP0039";

globaz.apg.vb.prestation.APRepartitionPaiementsViewBean viewBean = (globaz.apg.vb.prestation.APRepartitionPaiementsViewBean) request.getAttribute("viewBean");

IFrameListHeight = "100";
IFrameDetailHeight = "300";
bButtonNew = false;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<!--si APG -->
<%if ((String)globaz.prestation.tools.PRSessionDataContainerHelper.getData(session,globaz.prestation.tools.PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION)==globaz.prestation.api.IPRDemande.CS_TYPE_APG) {%>
	<%@page import="globaz.apg.servlet.IAPActions"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<ct:menuChange displayId="menu" menuId="ap-menuprincipalapg" showTab="menu"/>
	<ct:menuChange displayId="options" menuId="ap-optionsempty"/>
<!--sinon, maternité -->
<%} else if ((String)globaz.prestation.tools.PRSessionDataContainerHelper.getData(session,globaz.prestation.tools.PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION)==globaz.prestation.api.IPRDemande.CS_TYPE_MATERNITE) {%>
	<ct:menuChange displayId="menu" menuId="ap-menuprincipalamat" showTab="menu"/>
	<ct:menuChange displayId="options" menuId="ap-optionsempty"/>
<%}%>

<SCRIPT>
	bFind = true;
	usrAction = "<%=globaz.apg.servlet.IAPActions.ACTION_REPARTITION_PAIEMENTS%>.lister";
	detailLink = servlet + "?userAction=<%=globaz.apg.servlet.IAPActions.ACTION_REPARTITION_PAIEMENTS%>.afficher&idDroit=<%=viewBean.getIdDroit()%>&genreService=<%=viewBean.getGenreService()%>";
	
	function onClickNew() {
		// desactive le grisage des boutons
	}
	
	function prestationSuivante() {
		document.forms[0].elements('userAction').value = "<%=globaz.apg.servlet.IAPActions.ACTION_REPARTITION_PAIEMENTS%>.actionPrestationSuivante";
		document.forms[0].target = "fr_main";
		document.forms[0].submit();
	}
	
	function prestationPrecedante() {
		document.forms[0].elements('userAction').value = "<%=globaz.apg.servlet.IAPActions.ACTION_REPARTITION_PAIEMENTS%>.actionPrestationPrecedante";
		document.forms[0].target = "fr_main";
		document.forms[0].submit();
	}
	
	function repartirLesMontants() {
		document.forms[0].elements('userAction').value = "<%=globaz.apg.servlet.IAPActions.ACTION_REPARTITION_PAIEMENTS%>.actionRepartirLesMontants";
		document.forms[0].target = "fr_main";
		document.forms[0].submit();
	}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_TITRE_REPARTITION_PAIEMENTS"/><%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR><TD>
							<TABLE border="0">
								<TR>
									<TD>
										<b><ct:FWLabel key="JSP_DETAIL_ASSURE"/></b>
										
										<INPUT type="hidden" name="idDroit" value="<%=viewBean.getIdDroit()%>">
										<INPUT type="hidden" name="genreService" value="<%=viewBean.getGenreService()%>">
										<INPUT type="hidden" name="idOfIdPrestationCourante" value="<%=viewBean.getIdOfIdPrestationCourante()%>">
										<INPUT type="hidden" name="forIdPrestation" value="<%=viewBean.getIdPrestationCourante()%>">
										<input type="hidden" name= "noAVS" value="<%=viewBean.getNoAVSAssure()%>">
									</TD>
									<td colspan="5"><INPUT type="text" value="<%=viewBean.getDetailRequerantDetail()%>" size="100" class="disabled" readonly></td>
								</TR>
								<TR>
									<TD><ct:FWLabel key="JSP_DATE_DEBUT"/></TD>
									<TD>
										<INPUT type="text" name="dateDebutDroit" value="<%=viewBean.getDateDebutDroit()%>" class="dateDisabled" readonly>
									</TD>
									<TD colspan="4">&nbsp;</TD>
								</TR>
								<TR>
									<TD colspan="6"><H6><ct:FWLabel key="JSP_PRESTATION"/></H6></TD>
								</TR>
								<TR>
									<TD><ct:FWLabel key="JSP_TAUX_JOURNALIER_ALLOC_BASE"/></TD>
									<TD><INPUT type="text" name="tauxJournalier" value="<%=viewBean.getTauxPrestation()%>" class="montantDisabled" readonly></TD>
									<TD><ct:FWLabel key="JSP_NB_JOURS_SOLDES"/></TD>
									<TD><INPUT type="text" name="nombreJoursSoldes" value="<%=viewBean.getNbJoursPrestation()%>" class="numeroCourtDisabled" readonly></TD>
									<TD colspan="2"></TD>
								</TR>
								<TR>
									<TD><ct:FWLabel key="JSP_MONTANT_BRUT"/></TD>
									<TD><INPUT type="text" name="montantBrut" value="<%=viewBean.getMontantBrutPrestation()%>" class="montantDisabled" readonly></TD>
									
									<TD><ct:FWLabel key="JSP_DONT_MONTANT_ALLOC_EXPLOIT"/></TD>
									<TD><INPUT type="text" name="montantTotalAllocExploitation" value="<%=viewBean.getMontantTotalAllocExploitation()%>" class="montantDisabled" readonly></TD>
								</TR>
								<TR>
									<TD><ct:FWLabel key="JSP_PRESTATION"/></TD>
									<TD>
										<ct:FWLabel key="JSP_DU"/>&nbsp;
										<INPUT type="text" name="dateDebutPrestation" value="<%=viewBean.getDateDebutPrestation()%>" class="dateDisabled" readonly>&nbsp;
										<ct:FWLabel key="JSP_AU"/>&nbsp;
										<INPUT type="text" name="dateFinPrestation" value="<%=viewBean.getDateFinPrestation()%>" class="dateDisabled" readonly>
									</TD>
									<TD><ct:FWLabel key="JSP_PRESTATION_VALIDEE"/></TD>
									<TD><INPUT type="checkbox" name="isValide" value="" readonly disabled <%=viewBean.isValidee()?"checked":""%>></TD>
								</TR>
								<TR>
									<TD><LABEL for="droitAcquis"><ct:FWLabel key="JSP_DROIT_ACQUIS"/></LABEL></TD>
									<TD>
										<INPUT type="text" name="droitAcquis" value="<%=viewBean.getDroitAcquis()%>" class="montantDisabled" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);">
									</TD>
									<TD><ct:FWLabel key="JSP_GENRE_PRESTATION"/></TD>							
									<TD colspan="3"><INPUT type="text" name="genrePrestation" readonly value="<%=viewBean.getLibelleGenrePrestation()%>" class="disabled"></TD>
									
								</TR>
								<% if (viewBean.hasPrestationSuivante() || viewBean.hasPrestationPrecedante()) { %>
								<TR>
									<TD align="left">
										<% if (!globaz.apg.api.prestation.IAPPrestation.CS_ETAT_PRESTATION_DEFINITIF.equals(viewBean.getEtatPrestation()) &&
											   !viewBean.isRestitution() &&
											   viewBean.getSession().hasRight(IAPActions.ACTION_REPARTITION_PAIEMENTS, FWSecureConstants.UPDATE)) { %>
											<INPUT type="button" value="Répartir les montants" onclick="repartirLesMontants()">
										<% } %>
									</TD>
									<TD colspan="5" align="right">
										<% if (viewBean.hasPrestationPrecedante()) { %>
										<INPUT type="button" value="<ct:FWLabel key="JSP_PRESTATION_PRECEDANTE"/>" onclick="prestationPrecedante()">
										<% } %>
										<% if (viewBean.hasPrestationSuivante()) { %>
										<INPUT type="button" value="<ct:FWLabel key="JSP_PRESTATION_SUIVANTE"/>" onclick="prestationSuivante()">
										<% } %>
									</TD>
								</TR>
								<% }else{ %>
								<TR>
									<TD align="left" colspan="6">
										<% if (!globaz.apg.api.prestation.IAPPrestation.CS_ETAT_PRESTATION_DEFINITIF.equals(viewBean.getEtatPrestation()) &&
											   !viewBean.isRestitution() &&
											   viewBean.getSession().hasRight(IAPActions.ACTION_REPARTITION_PAIEMENTS, FWSecureConstants.UPDATE)) { %>
											<INPUT type="button" value="<ct:FWLabel key="JSP_REPARTIR_MONTANTS"/>" onclick="repartirLesMontants()">
										<% } %>
									</TD>
								</TR>
								<% } %>
							</TABLE>
						</TD></TR>
	 					<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyClose.jspf" %>
<%-- /tpl:insert --%>