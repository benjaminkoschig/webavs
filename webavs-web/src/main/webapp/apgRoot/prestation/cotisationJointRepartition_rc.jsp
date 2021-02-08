<%-- tpl:insert page="/theme/capage.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ page import="globaz.prestation.api.IPRDemande" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/capage/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran="PAP0032";

globaz.apg.vb.prestation.APRepartitionJointPrestationViewBean viewBean = (globaz.apg.vb.prestation.APRepartitionJointPrestationViewBean) request.getAttribute("viewBean");

IFrameDetailHeight = "260";

bButtonNew = false;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<!--si APG -->
<%if ((String)globaz.prestation.tools.PRSessionDataContainerHelper.getData(session,globaz.prestation.tools.PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION)==globaz.prestation.api.IPRDemande.CS_TYPE_APG) {%>
	<ct:menuChange displayId="menu" menuId="ap-menuprincipalapg" showTab="menu"/>
	<ct:menuChange displayId="options" menuId="ap-optionsempty"/>
<!--sinon, maternité -->
<%} else if ((String)globaz.prestation.tools.PRSessionDataContainerHelper.getData(session,globaz.prestation.tools.PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION)==globaz.prestation.api.IPRDemande.CS_TYPE_MATERNITE) {%>
	<ct:menuChange displayId="menu" menuId="ap-menuprincipalamat" showTab="menu"/>
	<ct:menuChange displayId="options" menuId="ap-optionsempty"/>
<%}else if ((String)globaz.prestation.tools.PRSessionDataContainerHelper.getData(session,globaz.prestation.tools.PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION)== IPRDemande.CS_TYPE_PATERNITE) {%>
	<ct:menuChange displayId="menu" menuId="ap-menuprincipalapat" showTab="menu"/>
	<ct:menuChange displayId="options" menuId="ap-optionsempty"/>
<%}%>

<SCRIPT>
	bFind = true;
	usrAction = "<%=globaz.apg.servlet.IAPActions.ACTION_COTISATION_JOINT_REPARTITION%>.lister";
	detailLink = servlet + "?userAction=<%=globaz.apg.servlet.IAPActions.ACTION_COTISATION_JOINT_REPARTITION%>.afficher&_method=add";
	
	function onClickNew() {
		// desactive le grisage des boutons
	}
	
	function retourARepartition() {
		document.location.href = servlet + '?userAction=<%=globaz.apg.servlet.IAPActions.ACTION_REPARTITION_PAIEMENTS%>.chercher&idPrestationCourante=<%=viewBean.getIdPrestationApg()%>&genreService=<%=viewBean.getGenreService()%>&idDroit=<%=viewBean.getIdDroit()%>';
	}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_TITRE_COTISATIONS"/><%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR><TD>
							<TABLE border="0">
								<TR>
									<TD><b><ct:FWLabel key="JSP_DETAIL_ASSURE"/></b></TD>
									<td colspan="5"><INPUT type="text" value="<%=viewBean.getDetailRequerantDetail()%>" size="100" class="disabled" readonly></td>
								</TR>
								<TR>
									<TD><ct:FWLabel key="JSP_DATE_DEBUT"/></TD>
									<TD><INPUT type="text" name="dateDebutDroit" value="<%=viewBean.getDateDebutDroit()%>" class="dateDisabled" readonly></TD>
									<TD colspan="4">&nbsp;</TD>
								</TR>
								<TR>
									<TD colspan="6"><HR></TD>
								</TR>
								<TR>
									<TD colspan="6"><H6><ct:FWLabel key="JSP_PRESTATION"/></H6></TD>
								</TR>
								<TR>
									<TD><ct:FWLabel key="JSP_BENEFICIAIRE_DE_BASE"/></TD>
									<TD><INPUT type="text" name="beneficiaireBase" value="<%=viewBean.getNom()%>" class="disabled" readonly></TD>
									<TD><ct:FWLabel key="JSP_MONTANT_BRUT"/></TD>
									<TD><INPUT type="text" name="montantBrut" value="<%=viewBean.getMontantBrut()%>" class="montantDisabled" readonly></TD>
									<TD><ct:FWLabel key="JSP_PERIODE"/></TD>
									<TD>
										<ct:FWLabel key="JSP_DU"/>&nbsp;
										<INPUT type="text" name="du" value="<%=viewBean.getDateDebut()%>" class="dateDisabled" readonly>&nbsp;
										<ct:FWLabel key="JSP_AU"/>&nbsp;
										<INPUT type="text" name="au" value="<%=viewBean.getDateFin()%>" class="dateDisabled" readonly>
										<INPUT type="hidden" name="forIdRepartitionBeneficiairePaiement" value="<%=viewBean.getIdRepartitionBeneficiairePaiement()%>">
									</TD>
								</TR>
							</TABLE>
						</TD></TR>
	 					<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<P align="right"><INPUT type="button" value="<ct:FWLabel key="JSP_RETOUR_A_REPARTITIONS"/>" onclick="retourARepartition()"></P>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyClose.jspf" %>
<%-- /tpl:insert --%>
