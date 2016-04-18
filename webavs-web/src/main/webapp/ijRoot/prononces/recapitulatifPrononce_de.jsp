<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="PIJ0004";
	globaz.ij.vb.prononces.IJRecapitulatifPrononceViewBean viewBean = (globaz.ij.vb.prononces.IJRecapitulatifPrononceViewBean)session.getAttribute("viewBean");
	globaz.ij.db.prononces.IJMesureJointAgentExecution[] agentsExecution = viewBean.getAgentsExecution();
	
	bButtonCancel = true;
	bButtonValidate = false;
	bButtonDelete =  globaz.ij.regles.IJPrononceRegles.isSupprimerPermis(viewBean) && bButtonDelete;
	bButtonUpdate = globaz.ij.regles.IJPrononceRegles.isModifierPermis(viewBean) && bButtonUpdate;
	bButtonNew = false;
	
	selectedIdValue = viewBean.getIdPrononce();
	
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.ij.servlet.IIJActions"%>
<%@page import="globaz.ij.application.IJApplication"%>
<ct:menuChange displayId="menu" menuId="ij-menuprincipal"/>
<ct:menuChange displayId="options" menuId="ij-optionmenuprononce" showTab="options">
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdPrononce()%>"/>
	<ct:menuSetAllParams key="csTypeIJ" value="<%=viewBean.getCsTypeIJ()%>"/>
	<ct:menuSetAllParams key="idPrononce" value="<%=viewBean.getIdPrononce()%>"/>
	<ct:menuSetAllParams key="noAVS" value="<%=viewBean.getNoAVS()%>"/>

	<% if (!viewBean.getCsEtat().equals(globaz.ij.api.prononces.IIJPrononce.CS_ATTENTE)) {%>
	
		<ct:menuActivateNode active="no" nodeId="calculerait"/>
		<ct:menuActivateNode active="no" nodeId="calculeraa"/>
		<ct:menuActivateNode active="no" nodeId="calculerijgp"/>
		
	<%} else {%>
	
		<%if(viewBean.isAit()){%>
			<ct:menuActivateNode active="yes" nodeId="calculerait"/>
			<ct:menuActivateNode active="no" nodeId="calculeraa"/>
			<ct:menuActivateNode active="no" nodeId="calculerijgp"/>
		<%} else if(viewBean.isAllocAssist()){%>
			<ct:menuActivateNode active="yes" nodeId="calculeraa"/>
			<ct:menuActivateNode active="no" nodeId="calculerait"/>
			<ct:menuActivateNode active="no" nodeId="calculerijgp"/>
		<%}else{%>
			<ct:menuActivateNode active="yes" nodeId="calculerijgp"/>
			<ct:menuActivateNode active="no" nodeId="calculerait"/>
			<ct:menuActivateNode active="no" nodeId="calculeraa"/>
		<%} %>
	<%}%>
	
	<% if (viewBean.getCsEtat().equals(globaz.ij.api.prononces.IIJPrononce.CS_COMMUNIQUE)) {%>
		<ct:menuActivateNode active="yes" nodeId="terminerprononce"/>
	<%} else {%>
		<ct:menuActivateNode active="no" nodeId="terminerprononce"/>
	<%}%>

	<% if (viewBean.getCsEtat().equals(globaz.ij.api.prononces.IIJPrononce.CS_ANNULE)) {%>
		<ct:menuActivateNode active="no" nodeId="corrigerprononce"/>
	<%} else {%>
		<ct:menuActivateNode active="yes" nodeId="corrigerprononce"/>
	<%}%>
	
			<% if (viewBean.getCsEtat().equals(globaz.ij.api.prononces.IIJPrononce.CS_ATTENTE)
				|| viewBean.getCsEtat().equals(globaz.ij.api.prononces.IIJPrononce.CS_VALIDE)
				|| viewBean.getCsEtat().equals(globaz.ij.api.prononces.IIJPrononce.CS_DECIDE)){ %>
				<ct:menuActivateNode active="yes" nodeId="annulerprononce"/>
			<%} else {%>
				<ct:menuActivateNode active="no" nodeId="annulerprononce"/>
			<%}%>
	

</ct:menuChange>

<SCRIPT language="javaScript">


function add() {}
function upd() {
  	document.forms[0].elements('userAction').value = "<%=globaz.ij.servlet.IIJActions.ACTION_REQUERANT%>.afficher";
  	document.forms[0].submit();
}
function validate() {}


  function cancel() {
    document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_PRONONCE_JOINT_DEMANDE%>.chercher";
  }




function del() {
    if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
	document.forms[0].elements('userAction').value = "<%=globaz.ij.servlet.IIJActions.ACTION_PRONONCE%>.supprimer";
	document.forms[0].submit();
    }
}

function init(){}

function simulerPaiement(){
	document.forms[0].elements('userAction').value = "<%=IIJActions.ACTION_PRONONCE_JOINT_DEMANDE%>.simulerPaiementDroit";
	document.forms[0].submit();
}
  
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
			<span class="postItIcon"><ct:FWNote sourceId="<%=viewBean.getIdPrononce()%>" tableSource="<%=IJApplication.KEY_POSTIT_PRONONCES%>"/></span>
			<ct:FWLabel key="JSP_RECAPITULATIF_PRONONCE"/>
			<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><ct:FWLabel key="JSP_ASSURE"/></TD>
							<TD COLSPAN="5">
							   <%=viewBean.getDetailRequerantListe()%>
							   <%if (globaz.jade.ged.client.JadeGedFacade.isInstalled()) {%>
							   &nbsp;/&nbsp;
							    <A href="#" onclick="window.open('<%=servletContext%><%=("/ij")%>?userAction=<%=globaz.ij.servlet.IIJActions.ACTION_PRONONCE%>.actionAfficherDossierGed&amp;noAVSId=<%=viewBean.getNoAVS()%>&amp;serviceNameId=<%=viewBean.getSession().getApplication().getProperty(globaz.externe.IPRConstantesExternes.PROPERTY_AFFICHAGE_DOSSIER_GED)%>','GED_CONSULT')" ><ct:FWLabel key="JSP_GED"/></A>
							    <%}%>
							</TD>
						</TR>
						<TR>
							<TD COLSPAN="6">
								<INPUT type="hidden" name="selectedId" value="<%=viewBean.getIdPrononce()%>">
								&nbsp;
							</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_DATE_PRONONCE"/></TD>
							<TD><INPUT type="text" value="<%=viewBean.getDatePrononce()%>"></TD>
							<TD><ct:FWLabel key="JSP_OFFICE_AI"/></TD>
							<TD><INPUT type="text" value="<%=viewBean.getOfficeAI()%>"></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_DATE_DEBUT_PRONONCE"/></TD>
							<TD><INPUT type="text" value="<%=viewBean.getDateDebutPrononce()%>"></TD>
							<TD><ct:FWLabel key="JSP_DATE_FIN_PRONONCE"/></TD>
							<TD><INPUT type="text" value="<%=viewBean.getDateFinPrononce()%>"></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_GENRE_READAPTATION"/></TD>
							<TD><INPUT type="text" value="<%=viewBean.getLibelleGenreReadaptation()%>"></TD>							
							<TD><ct:FWLabel key="JSP_TYPE_HEBERGEMENT"/></TD>
							<TD><INPUT type="text" value="<%=viewBean.getCsTypeHebergementLibelle()%>"></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_ETAT"/></TD>
							<TD><INPUT type="text" value="<%=viewBean.getLibelleEtat()%>"></TD>
							<TD><ct:FWLabel key="JSP_NO_DECISION_AI_COMMUNICATION"/></TD>
							<TD colspan="5"><INPUT type="text" value="<%=viewBean.getNoDecisionAI()%>" class="libelleLong"></TD>
						</TR>
						<TR>
							<TD colspan="6"><HR></TD>
						</TR>
						<TR>
							<TD colspan="6">
								<ct:FWLabel key="JSP_GRANDE_IJ"/><INPUT type="radio" name="typeIJ" <%=viewBean.isGrandeIJ()?"CHECKED":""%>>
								<ct:FWLabel key="JSP_PETITE_IJ"/><INPUT type="radio" name="typeIJ" <%=viewBean.isPetiteIJ()?"CHECKED":""%>>
								<ct:FWLabel key="JSP_AIT"/><INPUT type="radio" name="typeIJ" <%=viewBean.isAit()?"CHECKED":""%>>
								<ct:FWLabel key="JSP_ALLOC_ASSIST"/><INPUT type="radio" name="typeIJ" <%=viewBean.isAllocAssist()?"CHECKED":""%>>
							</TD>
						</TR>
						<TR>
							<TD colspan="6"><HR></TD>
						</TR>
					<%if (viewBean.isGrandeIJ() ||
						  viewBean.isPetiteIJ()){%>
						<TR>
							<TD><ct:FWLabel key="JSP_MONTANT_GARANTI_AA_REDUIT"/></TD>
							<TD><INPUT type="checkbox" <%=viewBean.getMontantGarantiAAReduit().booleanValue()?"CHECKED":""%>></TD>
							<TD><ct:FWLabel key="JSP_MONTANT_GARANTI_AA"/></TD>
							<TD><INPUT type="text" value="<%=viewBean.getMontantGarantiAA()%>"></TD>
							<TD><ct:FWLabel key="JSP_DEMI_IJ_AC"/></TD>
							<TD><INPUT type="text" value="<%=viewBean.getDemiIJAC()%>"></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_STATUT"/></TD>
							<TD><INPUT type="text" value="<%=viewBean.getLibelleStatut()%>"></TD>
						</TR>
						<TR>
							<TD>&nbsp;</TD>
						</TR>
						<TR>
							<TD colspan="6"><B><ct:FWLabel key="JSP_REVENU_DURANT_READAPTATION"/></B></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_MONTANT"/></TD>
							<TD><INPUT type="text" value="<%=viewBean.getMontantRevenuReadaptation()%>"></TD>
							<TD><ct:FWLabel key="JSP_PERIODICITE"/></TD>
							<TD><INPUT type="text" value="<%=viewBean.getLibellePeriodiciteReadaptation()%>"></TD>
							<TD><ct:FWLabel key="JSP_HEURES_SEMAINE"/></TD>
							<TD><INPUT type="text" value="<%=viewBean.getHeuresSemainesReadaptation()%>"></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_ANNEE_NIVEAU"/></TD>
							<TD><INPUT type="text" value="<%=viewBean.getAnneeNiveauReadaptation()%>"></TD>
						</TR>
						<TR>
							<TD colspan="6"><HR></TD>
						</TR>
					<%}else if(viewBean.isAit()){%>
						<TR>
							<TD><ct:FWLabel key="JSP_MONTANT"/></TD>
							<TD colspan="5"><INPUT type="text" value="<%=viewBean.getMontantAit()%>"></TD>
						</TR>
						<TR>
							<TD colspan="6"><HR></TD>
						</TR>
					
					<%}else if(viewBean.isAllocAssist()){%>
						<TR>
							<TD><ct:FWLabel key="JSP_MONTANT_TOTAL"/></TD>
							<TD><INPUT type="text" value="<%=viewBean.getMontantTotal()%>"></TD>
							<TD><ct:FWLabel key="JSP_GENRE_READAPTATION"/></TD>
							<TD colspan="3"><INPUT type="text" value="<%=viewBean.getCsGenreReadaptationLibelle()%>"></TD>
						</TR>
					<%}%>
						
						
				<%if (viewBean.isGrandeIJ()){%>
				<%if (!viewBean.is4emeRevision()){%>
						<TR>
							<TD><ct:FWLabel key="JSP_MONTANT_INDEMNITE_ASSISTANCE"/></TD>
							<TD><INPUT type="text" value="<%=viewBean.getMontantIndemniteAssistance()%>"></TD>
							<TD><ct:FWLabel key="JSP_INCAPACITE_POURCENT"/></TD>
							<TD><INPUT type="text" value="<%=viewBean.getIncapacite3emeRevision()%>"></TD>
							<TD><ct:FWLabel key="JSP_ALLOCATION_EXPLOITATION"/></TD>
							<TD><INPUT type="checkbox" <%=viewBean.getAlocationExploitation().booleanValue()?"CHECKED":""%>></TD>
						</TR>
						<TR>
							<TD colspan="6"><HR></TD>
						</TR>
				<%}else{%>
						<TR>
							<TD><ct:FWLabel key="JSP_INCAPACITE_POURCENT"/></TD>
							<TD><INPUT type="text" value="<%=viewBean.getIncapacite3emeRevision()%>"></TD>
						</TR>
						<TR>
							<TD colspan="6"><HR></TD>
						</TR>
				<%}%>
				<%}%>
						
				<%for (int i=0;i<agentsExecution.length;i++){
					globaz.ij.db.prononces.IJMesureJointAgentExecution agentExecution = agentsExecution[i];
				%>
						<TR>
							<TD><ct:FWLabel key="JSP_AGENT_EXECUTION"/></TD>
							<TD><INPUT type="text" value="<%=agentExecution.getNomAgentExecution()%>"></TD>
							<TD><ct:FWLabel key="JSP_DATE_DEBUT"/></TD>
							<TD><INPUT type="text" value="<%=agentExecution.getDateDebut()%>"></TD>
							<TD><ct:FWLabel key="JSP_DATE_FIN"/></TD>
							<TD><INPUT type="text" value="<%=agentExecution.getDateFin()%>"></TD>
						</TR>
				<%}%>
						<TR>
							<TD colspan="6"><HR></TD>
						</TR>
						<TR>
							<TD colspan="6"><B><ct:FWLabel key="JSP_SITUATION_FAMILIALE"/></B></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_NOMBRE_ENFANT_CHARGE"/></TD>
							<TD><INPUT type="text" value="<%=viewBean.getNombreEnfantsACharge()%>"></TD>
							<TD><ct:FWLabel key="JSP_ETAT_CIVIL"/></TD>
							<TD><INPUT type="text" value="<%=viewBean.getLibelleEtatCivil()%>"></TD>
						</TR>
				<% if (viewBean.isPetiteIJ()){%>
						<TR>
							<TD colspan="6"><HR></TD>
						</TR>
						<TR>
							<TD colspan="6"><B><ct:FWLabel key="JSP_SITUATION_ASSURE"/></B></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_MODE_CALCUL"/></TD>
							<TD><INPUT type="text" value="<%=viewBean.getLibelleModeCalcul()%>"></TD>
						</TR>
						<TR>
							<TD colspan="6">&nbsp;</TD>
						</TR>
						<TR>
							<TD colspan="6"><B><ct:FWLabel key="JSP_DERNIER_REVENU_DURANT_BLABLABLA_MANQUE_A_GAGNER"/></B></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_MONTANT"/></TD>
							<TD><INPUT type="text" value="<%=viewBean.getMontantManqueAGagner()%>"></TD>
							<TD><ct:FWLabel key="JSP_PERIODICITE"/></TD>
							<TD><INPUT type="text" value="<%=viewBean.getLibellePeriodiciteManqueAGagner()%>"></TD>
							<TD><ct:FWLabel key="JSP_HEURES_SEMAINE"/></TD>
							<TD><INPUT type="text" value="<%=viewBean.getHeuresSemaineManqueAGagner()%>"></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_ANNEE_NIVEAU"/></TD>
							<TD><INPUT type="text" value="<%=viewBean.getAnneeNiveauManqueAGagner()%>"></TD>
						</TR>
				<%} else if(viewBean.isGrandeIJ() || viewBean.isAit()) {%>
						<TR>
							<TD colspan="6"><HR></TD>
						</TR>
						<TR>
							<TD><B><ct:FWLabel key="JSP_EMPLOYEURS"/></B></TD>
							<TD colspan="5">
								<TABLE BORDER="3">
									<THEAD>
										<TR>
											<TH><ct:FWLabel key="JSP_NO_AFFILIE"/></TH>
											<TH><ct:FWLabel key="JSP_EMPLOYEUR"/></TH>
											<TH><ct:FWLabel key="JSP_VERSEMENT_ASSURE"/></TH>
										</TR>
									</THEAD>
									<TBODY>
									<%
									int count = 0;
									
									for (java.util.Iterator employeurs = viewBean.getEmployeurs().iterator(); employeurs.hasNext(); ++count) {
										if (count > 5) { %>
										<TR>
											<TD style="text-align: center" colspan="3">...</TD>
										</TR>
										<%
											break;
										} else {
										globaz.ij.db.prononces.IJSitProJointEmployeur employeur = (globaz.ij.db.prononces.IJSitProJointEmployeur) employeurs.next();
									%>
										<TR>
											<TD><%=employeur.getNoAffilie()%></TD>
											<TD><%=employeur.getNom()%></TD>
											<TD style="text-align: center">
												<IMG SRC="<%=request.getContextPath()+(!employeur.getVersementEmployeur().booleanValue()?"/images/ok.gif":"/images/erreur.gif")%>" ALT="">
											</TD>
											<%if (globaz.jade.ged.client.JadeGedFacade.isInstalled()) {%>
											<TD><A  href="<%=servletContext%><%=("/ij")%>?userAction=<%=globaz.ij.servlet.IIJActions.ACTION_PRONONCE%>.actionAfficherDossierGed&amp;noAffiliationId=<%=employeur.getNoAffilie()%>&amp;noAVSId=<%=employeur.retrieveNoAVS()%>&amp;serviceNameId=<%=viewBean.getSession().getApplication().getProperty(globaz.externe.IPRConstantesExternes.PROPERTY_AFFICHAGE_DOSSIER_GED_COTI)%>" target="GED_CONSULT"><ct:FWLabel key="JSP_GED"/></A></TD>
											<%}%>
										</TR>
									<% }} %>
									</TBODY>
								</TABLE>
							</TD>
						</TR>
				<%}%>
						
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%if(viewBean.isAdministrateur()){ %>
					<INPUT type="button" name="simulerPaiement" onclick="simulerPaiement()" value="<ct:FWLabel key="MENU_OPTION_SIMULER_PAIEMENT"/>">
				<%}%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>