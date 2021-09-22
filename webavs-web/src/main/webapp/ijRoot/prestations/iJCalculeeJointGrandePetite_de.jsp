<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="PIJ0010";
	globaz.ij.vb.prestations.IJIJCalculeeJointGrandePetiteViewBean viewBean = (globaz.ij.vb.prestations.IJIJCalculeeJointGrandePetiteViewBean) session.getAttribute("viewBean");
	
	bButtonUpdate = bButtonUpdate && viewBean.isModifierPermis();
	bButtonValidate = bButtonValidate && viewBean.isModifierPermis();
	bButtonCancel = bButtonCancel && viewBean.isModifierPermis();
	
	bButtonDelete = false;
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page import="globaz.ij.db.prononces.IJPrononce" %>
<ct:menuChange displayId="menu" menuId="ij-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="ij-optionsempty"/>

<SCRIPT language="javascript"> 
var servlet = "<%=(servletContext + mainServletPath)%>";

	function add() {
	}
	
	function upd() {
	}
	
	function validate() {
	    state = true
	    if (document.forms[0].elements('_method').value == "add")
	        document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_IJ_CALCULEE_JOINT_GRANDE_PETITE%>.ajouter";
	    else
	        document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_IJ_CALCULEE_JOINT_GRANDE_PETITE%>.modifier";
	    return state;
	}
	
	function cancel() {
      document.forms[0].elements('userAction').value="back";
	}
	
	function del() {
	}

	function init(){
	}
	
	function clicPrecedent(){
		document.location.href = servlet + '?userAction=ij.prestations.iJCalculeeJointGrandePetite.afficher&selectedId=<%=viewBean.getIdIJCalculeePrecedente()%>';
	}
	
	function clicSuivant(){
		document.location.href = servlet + '?userAction=ij.prestations.iJCalculeeJointGrandePetite.afficher&selectedId=<%=viewBean.getIdIJCalculeeSuivante()%>';
	}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_IJ_CALCULEE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><B><ct:FWLabel key="JSP_DETAIL_REQUERANT"/></B></TD>
							<TD colspan="5">
								<INPUT type="text" value="<%=viewBean.getDetailRequerantDetail()%>" size="100" class="disabled" readonly>
							</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_DATE_PRONONCE"/></TD>
							<TD><INPUT type="text" class="disabled" readonly="readonly" value="<%=viewBean.getVraieDatePrononce()%>"></TD>
							<TD></TD>
							<TD></TD>
							<TD></TD>
							<TD><INPUT type="text" class="disabled" readonly="readonly" value="<%=viewBean.getIdIJCalculee()%>"></TD>
						</TR>
						<TR>
							<TD colspan="6"><HR></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_DATE_DEBUT"/></TD>
							<TD><INPUT type="text" name="dateDebutDroit" value="<%=viewBean.getDateDebutDroit()%>"></TD>
							<TD><ct:FWLabel key="JSP_DATE_FIN"/></TD>
							<TD><INPUT type="text" name="dateFinDroit" value="<%=viewBean.getDateFinDroit()%>"></TD>
							<TD><ct:FWLabel key="JSP_TYPE_IJ"/></TD>
							<TD>
								<input type="text" name="csTypeIJLibelle" value="<%=viewBean.getCsTypeIJLibelle()%>" class="disabled" readonly="readonly">
								<input type="hidden" name="csTypeIJ" value="<%=viewBean.getCsTypeIJ()%>">
							</TD>
						</TR>
						<TR>
							<TD colspan="6">&nbsp;</TD>
						</TR>
						<TR>
							<%if(globaz.ij.api.prononces.IIJPrononce.CS_ALLOC_INIT_TRAVAIL.equals(viewBean.getCsTypeIJ())){ %>
								<TD colspan="4">&nbsp;</TD>
							<%}else{%>
								<TD><ct:FWLabel key="JSP_NSS_ABREGE"/></TD>
								<TD><INPUT type="text" name="noAVS" value="<%=viewBean.getNoAVS()%>"></TD>
								<TD><ct:FWLabel key="JSP_TYPE_BASE"/></TD>
								<TD>
									<ct:FWCodeSelectTag codeType="<%=globaz.ij.api.prestations.IIJIJCalculee.CS_GROUPE_TYPE_BASE%>" name="csTypeBase" defaut="<%=viewBean.getCsTypeBase()%>"/>
								</TD>
							<%}%>
							<TD><ct:FWLabel key="JSP_OFFICE_AI"/></TD>
							<TD><INPUT type="text" name="officeAI" value="<%=viewBean.getOfficeAI()%>"></TD>
						</TR>
						
						<%if(IJPrononce.isCommonTypeIJ(viewBean.getCsTypeIJ())){ %>
						<TR>
							<TD colspan="6">&nbsp;</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_GENRE_READAPTATION"/></TD>
							<TD>
								<ct:FWCodeSelectTag codeType="<%=globaz.ij.api.prononces.IIJPrononce.CS_GROUPE_GENRE_READAPTATION%>" name="csGenreReadaptation" defaut="<%=viewBean.getCsGenreReadaptation()%>"/>	
							</TD>
							<TD><ct:FWLabel key="JSP_DATE_PRONONCE"/></TD>
							<TD><INPUT type="text" name="datePrononce" value="<%=viewBean.getDatePrononce()%>"></TD>
							<TD>&nbsp;</TD>
							<TD>&nbsp;</TD>
						</TR>
						<TR>
							<TD colspan="6">&nbsp;</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_DATE_REVENU_DETERMINANT"/></TD>
							<TD><INPUT type="text" name="dateRevenu" value="<%=viewBean.getDateRevenu()%>"></TD>
							<TD><ct:FWLabel key="JSP_MONTANT_REVENU_DETERMINANT"/></TD>
							<TD><INPUT type="text" name="revenuDeterminant" value="<%=viewBean.getRevenuDeterminant()%>"></TD>
							<TD><ct:FWLabel key="JSP_MONTANT_BASE"/></TD>
							<TD><INPUT type="text" name="montantBase" value="<%=viewBean.getMontantBase()%>"></TD>
						</TR>
						<TR>
							<TD colspan="6">&nbsp;</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_SUPPLEMENT_PERSONNE_SEULE"/></TD>
							<TD><INPUT type="text" name="supplementPersonneSeule" value="<%=viewBean.getSupplementPersonneSeule()%>"></TD>
							<TD><ct:FWLabel key="JSP_REVENU_JOURNALIER_DURANT_READAPTATION"/></TD>
							<TD><INPUT type="text" name="revenuJournalierReadaptation" value="<%=viewBean.getRevenuJournalierReadaptation()%>"></TD>
							<TD><ct:FWLabel key="JSP_STATUT"/></TD>
							<TD>
								<ct:FWCodeSelectTag codeType="<%=globaz.ij.api.prononces.IIJPrononce.CS_GROUPE_STATUT_PROFESSIONNEL%>" name="csStatutProfessionnel" defaut="<%=viewBean.getCsStatutProfessionnel()%>"/>	
							</TD>
						</TR>
						<TR>
							<TD colspan="6">&nbsp;</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_INCAPACITE_POURCENT"/></TD>
							<TD><INPUT type="text" name="pourcentDegreIncapaciteTravail" value="<%=viewBean.getPourcentDegreIncapaciteTravail()%>"></TD>
							<TD><ct:FWLabel key="JSP_DEMI_IJ_AC_BRUT"/></TD>
							<TD colspan="3">
								<INPUT type="text" name="demiIJACBrut" value="<%=viewBean.getDemiIJACBrut()%>">
							</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_IJ_CAL_NO_REVISION"/></TD>
							<TD><INPUT type="text" name="noRevision" value="<%=viewBean.getNoRevision()%>"></TD>
							<TD><ct:FWLabel key="JSP_IJ_CAL_DROIT_PRST_ENFANT"/></TD>
							<%
							String value = null;
							if (viewBean.getIsDroitPrestationPourEnfant()==null) {
								value="";
							}
							else if (viewBean.getIsDroitPrestationPourEnfant().booleanValue()) {
								value = "1";
							}
							else {
								value = "0";
							}
							%>
							<TD><INPUT type="text" value="<%=value%>"></TD>
							<TD></TD>
							<TD></TD>							
						</TR>
						<%}%>				
						<TR>
							<TD colspan="6"><HR></TD>
						</TR>
						<%if (globaz.ij.api.prononces.IIJPrononce.CS_GRANDE_IJ.equals(viewBean.getCsTypeIJ())){%>
						<TR>
							<TD><ct:FWLabel key="JSP_MONTANT_INDEMNITE_ENFANT"/></TD>
							<TD><INPUT type="text" name="montantIndemniteEnfant" value="<%=viewBean.getMontantIndemniteEnfant()%>"></TD>
							<TD><ct:FWLabel key="JSP_NB_ENFANTS"/></TD>
							<TD><INPUT type="text" name="nbEnfants" value="<%=viewBean.getNbEnfants()%>"></TD>
							<TD><ct:FWLabel key="JSP_MONTANT_INDEMNITE_ASSISTANCE"/></TD>
							<TD><INPUT type="text" name="montantIndemniteAssistance" value="<%=viewBean.getMontantIndemniteAssistance()%>"></TD>
						</TR>
						<%} else if (globaz.ij.api.prononces.IIJPrononce.CS_PETITE_IJ.equals(viewBean.getCsTypeIJ())) {%>
						<TR>
							<TD><ct:FWLabel key="JSP_MODE_CALCUL"/></TD>
							<TD colspan="2">							
								<ct:FWCodeSelectTag codeType="<%=globaz.ij.api.prestations.IIJPetiteIJCalculee.CS_GROUPE_MODE_CALCUL%>" name="csModeCalcul" defaut="<%=viewBean.getCsModeCalcul()%>"/>		
							</TD>
						</TR>
						<%} else if (globaz.ij.api.prononces.IIJPrononce.CS_FPI.equals(viewBean.getCsTypeIJ())) {%>
						<TR>
							<TD><ct:FWLabel key="JSP_MODE_CALCUL"/></TD>
							<TD colspan="2">
								<ct:FWCodeSelectTag codeType="<%=globaz.ij.api.prestations.IIJPrestation.CS_GROUPE_MODE_CALCUL_FPI%>" name="csModeCalcul" defaut="<%=viewBean.getCsModeCalcul()%>"/>
							</TD>
						</TR>
						<%}else{%>
						<TR>
							<TD colspan="6">&nbsp;</TD>
						</TR>
						<%}%>

				<%if (!viewBean.getIndemniteJournaliereInterne().isNew()){%>
					<TR>
						<TD colspan="6"><HR></TD>
					</TR>
					<TR>
						<TD><B><ct:FWLabel key="JSP_PARTIE_INTERNE"/></B></TD>
					</TR>
					<TR>
						<TD colspan="6">&nbsp;</TD>
					</TR>
					<TR>
						<TD><ct:FWLabel key="JSP_MONTANT_SUPPLEMENT_READAPTATION"/></TD>
						<TD><INPUT type="text" name="ijInterneMontantSupplementaireReadaptation" value="<%=viewBean.getIndemniteJournaliereInterne().getMontantSupplementaireReadaptation()%>"></TD>
						<TD><ct:FWLabel key="JSP_GARANTIE_AA_NON_REDUITE"/></TD>
						<TD><INPUT type="text" name="ijInterneMontantGarantiAANonReduit" value="<%=viewBean.getIndemniteJournaliereInterne().getMontantGarantiAANonReduit()%>"></TD>
						<TD><ct:FWLabel key="JSP_INDEMNITE_AVANT_REDUCTION"/></TD>
						<TD><INPUT type="text" name="ijInterneIndemniteAvantReduction" value="<%=viewBean.getIndemniteJournaliereInterne().getIndemniteAvantReduction()%>"></TD>
					</TR>
					<TR>
						<TD colspan="6">&nbsp;</TD>
					</TR>
					<TR>
						<TD><ct:FWLabel key="JSP_DEDUCTION_RENTES_AI"/></TD>
						<TD><INPUT type="text" name="ijInterneDeductionRenteAI" value="<%=viewBean.getIndemniteJournaliereInterne().getDeductionRenteAI()%>"></TD>
						<TD><ct:FWLabel key="JSP_FRACTION_REDUCTION_SI_REVENU_DURANT_READAPTATION"/></TD>
						<TD><INPUT type="text" name="ijInterneFractionReductionSiRevenuAvantReadaptation" value="<%=viewBean.getIndemniteJournaliereInterne().getFractionReductionSiRevenuAvantReadaptation()%>"></TD>
						<TD><ct:FWLabel key="JSP_MONTANT_REDUCTION_SI_REVENU_DURANT_READAPTATION"/></TD>
						<TD><INPUT type="text" name="ijInterneMontantReductionSiRevenuAvantReadaptation" value="<%=viewBean.getIndemniteJournaliereInterne().getMontantReductionSiRevenuAvantReadaptation()%>"></TD>
					</TR>
					<TR>
						<TD colspan="6">&nbsp;</TD>
					</TR>
					<TR>
						<TD><ct:FWLabel key="JSP_MONTANT_JOURNALIER_INDEMNITE"/></TD>
						<TD><INPUT type="text" name="ijInterneMontantJournalierIndemnite" value="<%=viewBean.getIndemniteJournaliereInterne().getMontantJournalierIndemnite()%>"></TD>
						<TD><ct:FWLabel key="JSP_GARANTIE_AA"/></TD>
						<TD><INPUT type="text" name="ijInterneMontantGarantiAAReduit" value="<%=viewBean.getIndemniteJournaliereInterne().getMontantGarantiAAReduit()%>"></TD>
						<TD><ct:FWLabel key="JSP_MONTANT_COMPLET"/></TD>
						<TD><INPUT type="text" name="ijInterneMontantComplet" value="<%=viewBean.getIndemniteJournaliereInterne().getMontantComplet()%>"></TD>
					</TR>
					<TR>
						<TD colspan="6">&nbsp;</TD>
					</TR>
					<TR>
						<TD><ct:FWLabel key="JSP_MONTANT_PLAFONNE"/></TD>
						<TD><INPUT type="text" name="ijInterneMontantPlafonne" value="<%=viewBean.getIndemniteJournaliereInterne().getMontantPlafonne()%>"></TD>
						<TD><ct:FWLabel key="JSP_MONTANT_PLAFONNE_MINIMUM"/></TD>
						<TD><INPUT type="text" name="ijInterneMontantPlafonneMinimum" value="<%=viewBean.getIndemniteJournaliereInterne().getMontantPlafonneMinimum()%>"></TD>
					</TR>
				<%}
				if (!viewBean.getIndemniteJournaliereExterne().isNew()){%>
					<TR>
						<TD colspan="6"><HR></TD>
					</TR>
					<TR>
						<TD><B><ct:FWLabel key="JSP_PARTIE_EXTERNE"/></B></TD>
					</TR>
					<TR>
						<TD colspan="6">&nbsp;</TD>
					</TR>
					<TR>
						<TD><ct:FWLabel key="JSP_MONTANT_SUPPLEMENT_READAPTATION"/></TD>
						<TD><INPUT type="text" name="ijExterneMontantSupplementaireReadaptation" value="<%=viewBean.getIndemniteJournaliereExterne().getMontantSupplementaireReadaptation()%>"></TD>
						<TD><ct:FWLabel key="JSP_GARANTIE_AA_NON_REDUITE"/></TD>
						<TD><INPUT type="text" name="ijExterneMontantGarantiAANonReduit" value="<%=viewBean.getIndemniteJournaliereExterne().getMontantGarantiAANonReduit()%>"></TD>
						<TD><ct:FWLabel key="JSP_INDEMNITE_AVANT_REDUCTION"/></TD>
						<TD><INPUT type="text" name="ijExterneIndemniteAvantReduction" value="<%=viewBean.getIndemniteJournaliereExterne().getIndemniteAvantReduction()%>"></TD>
					</TR>
					<TR>
						<TD colspan="6">&nbsp;</TD>
					</TR>
					<TR>
						<TD><ct:FWLabel key="JSP_DEDUCTION_RENTES_AI"/></TD>
						<TD><INPUT type="text" name="ijExterneDeductionRenteAI" value="<%=viewBean.getIndemniteJournaliereExterne().getDeductionRenteAI()%>"></TD>
						<TD><ct:FWLabel key="JSP_FRACTION_REDUCTION_SI_REVENU_DURANT_READAPTATION"/></TD>
						<TD><INPUT type="text" name="ijExterneFractionReductionSiRevenuAvantReadaptation" value="<%=viewBean.getIndemniteJournaliereExterne().getFractionReductionSiRevenuAvantReadaptation()%>"></TD>
						<TD><ct:FWLabel key="JSP_MONTANT_REDUCTION_SI_REVENU_DURANT_READAPTATION"/></TD>
						<TD><INPUT type="text" name="ijExterneMontantReductionSiRevenuAvantReadaptation" value="<%=viewBean.getIndemniteJournaliereExterne().getMontantReductionSiRevenuAvantReadaptation()%>"></TD>
					</TR>
					<TR>
						<TD colspan="6">&nbsp;</TD>
					</TR>
					<TR>
						<TD><ct:FWLabel key="JSP_MONTANT_JOURNALIER_INDEMNITE"/></TD>
						<TD><INPUT type="text" name="ijExterneMontantJournalierIndemnite" value="<%=viewBean.getIndemniteJournaliereExterne().getMontantJournalierIndemnite()%>"></TD>
						<TD><ct:FWLabel key="JSP_GARANTIE_AA"/></TD>
						<TD><INPUT type="text" name="ijExterneMontantGarantiAAReduit" value="<%=viewBean.getIndemniteJournaliereExterne().getMontantGarantiAAReduit()%>"></TD>
						<TD><ct:FWLabel key="JSP_MONTANT_COMPLET"/></TD>
						<TD><INPUT type="text" name="ijExterneMontantComplet" value="<%=viewBean.getIndemniteJournaliereExterne().getMontantComplet()%>"></TD>
					</TR>
					<TR>
						<TD colspan="6">&nbsp;</TD>
					</TR>
					<TR>
						<TD><ct:FWLabel key="JSP_MONTANT_PLAFONNE"/></TD>
						<TD><INPUT type="text" name="ijExterneMontantPlafonne" value="<%=viewBean.getIndemniteJournaliereExterne().getMontantPlafonne()%>"></TD>
						<TD><ct:FWLabel key="JSP_MONTANT_PLAFONNE_MINIMUM"/></TD>
						<TD><INPUT type="text" name="ijExterneMontantPlafonneMinimum" value="<%=viewBean.getIndemniteJournaliereExterne().getMontantPlafonneMinimum()%>"></TD>
					</TR>
				<%}
				if (!viewBean.getIndemniteJournaliereAit().isNew()){%>
					<TR>
						<TD colspan="6">&nbsp;</TD>
					</TR>
					<TR>
						<TD><ct:FWLabel key="JSP_MONTANT_JOURNALIER_INDEMNITE"/></TD>
						<TD><INPUT type="text" value="<%=viewBean.getIndemniteJournaliereAit().getMontantJournalierIndemnite()%>"></TD>
						<TD colspan="4">&nbsp;</TD>
					</TR>
				<%}%>					
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%>
				<%if (!viewBean.isPremierElement()){%>
				<INPUT type="button" value="<ct:FWLabel key="JSP_PRECEDENT"/>" onclick="clicPrecedent()">
				<%}%>
				<%if (!viewBean.isDernierElement()){%>
				<INPUT type="button" value="<ct:FWLabel key="JSP_SUIVANT"/>" onclick="clicSuivant()">
				<%}%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>