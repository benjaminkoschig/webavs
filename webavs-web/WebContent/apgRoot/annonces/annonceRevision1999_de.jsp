<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.framework.controller.FWAction"%>
<%@page import="globaz.apg.servlet.IAPActions"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran="PAP0018";

	globaz.apg.vb.annonces.APAnnonceRevision1999ViewBean viewBean = (globaz.apg.vb.annonces.APAnnonceRevision1999ViewBean) session.getAttribute("viewBean");
	selectedIdValue = viewBean.getIdAnnonce();
	
	bButtonDelete = bButtonDelete && (!viewBean.getEtat().equals(globaz.apg.api.annonces.IAPAnnonce.CS_ENVOYE));
	bButtonDelete = false;
	bButtonUpdate = bButtonUpdate && viewBean.isModifiable();
	String typePrestation = "typePrestation=" + viewBean.getTypePrestation();
	String forIdDroit  = "forIdDroit=" + viewBean.getIdDroit();
	String lienSurPrestation = request.getContextPath() + "/apg?userAction=" + IAPActions.ACTION_PRESTATION_JOINT_LOT_TIERS_DROIT + "." + FWAction.ACTION_CHERCHER + "&" + typePrestation + "&" + forIdDroit;
%>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<!--si APG -->
<%if ((String)globaz.prestation.tools.PRSessionDataContainerHelper.getData(session,globaz.prestation.tools.PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION)==globaz.prestation.api.IPRDemande.CS_TYPE_APG) {%>
	<ct:menuChange displayId="menu" menuId="ap-menuprincipalapg" showTab="menu"/>
	<ct:menuChange displayId="options" menuId="ap-optionsempty"/>
<!--sinon, maternité -->
<%} else if ((String)globaz.prestation.tools.PRSessionDataContainerHelper.getData(session,globaz.prestation.tools.PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION)==globaz.prestation.api.IPRDemande.CS_TYPE_MATERNITE) {%>
	<ct:menuChange displayId="menu" menuId="ap-menuprincipalamat" showTab="menu"/>
	<ct:menuChange displayId="options" menuId="ap-optionsempty"/>
<%}%>
<script language="JavaScript">

  function add() {
    document.forms[0].elements('userAction').value="apg.annonces.annonceRevision1999.ajouter";
  }
  function upd() {
  		document.forms[0].elements('userAction').value="apg.annonces.annonceRevision1999.modifier";
  }

  function validate() {
    state = true;
    if (document.forms[0].elements('_method').value == "add"){
        document.forms[0].elements('userAction').value="apg.annonces.annonceRevision1999.ajouter";
    
    }else{
        document.forms[0].elements('userAction').value="apg.annonces.annonceRevision1999.modifier";
    }
    return state;
  }

  function cancel() {
    if (document.forms[0].elements('_method').value == "add")
      document.forms[0].elements('userAction').value="back";
    else
      document.forms[0].elements('userAction').value="apg.annonces.annonceRevision1999.chercher";
  }

  function del() {
    if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
        document.forms[0].elements('userAction').value="apg.annonces.annonceRevision1999.supprimer";
        document.forms[0].submit();
    }
  }

  	function init(){

  	}
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_ANNONCE_REVISION_1999"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><ct:FWLabel key="JSP_ANNONCES_CODE_APPLICATION"/></TD>
							<TD><INPUT type="text" name="codeApplication" value="<%=viewBean.getCodeApplication()%>"></TD>
							<TD><ct:FWLabel key="JSP_ANNONCES_CODE_ENREGISTREMENT"/></TD>
							<TD><INPUT type="text" name="codeEnregistrement" value="<%=viewBean.getCodeEnregistrement()%>"></TD>
							<TD><ct:FWLabel key="JSP_ANNONCES_NUMERO_CAISSE_AGENCE"/></TD>
							<TD><INPUT type="text" name="numeroCaisse" value="<%=viewBean.getNumeroCaisse()%>" size="3">
							    <INPUT type="text" name="numeroAgence" value="<%=viewBean.getNumeroAgence()%>" size="3"></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_ANNONCES_MOIS_ANNEE_COMPTABLE"/></TD>
							<TD><INPUT type="text" name="moisAnneeComptable" value="<%=viewBean.getMoisAnneeComptable()%>"></TD>
							<TD><ct:FWLabel key="JSP_ANNONCES_GENRE_CARTE"/></TD>
							<TD><INPUT type="text" name="contenuAnnonce" value="<%=viewBean.getContenuAnnonce()%>"></TD>
							<TD><ct:FWLabel key="JSP_ANNONCES_GENRE_SERVICE"/></TD>
							<TD><INPUT type="text" name="genre" value="<%=viewBean.getGenre()%>"></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_ANNONCES_NUMERO_COMPTE"/></TD>
							<TD><INPUT type="text" name="numeroCompte" value="<%=viewBean.getNumeroCompte()%>"></TD>
							<TD><ct:FWLabel key="JSP_ANNONCES_NUMERO_ASSURE"/></TD>
							<TD><INPUT type="text" name="numeroAssure" value="<%=globaz.commons.nss.NSUtil.formatAVSUnknown(viewBean.getNumeroAssure())%>"></TD>
							<TD><ct:FWLabel key="JSP_ANNONCES_NUMERO_CONTROLE"/></TD>
							<TD><INPUT type="text" name="numeroControle" value="<%=viewBean.getNumeroControle()%>"></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_ANNONCES_RECRUE"/></TD>
							<TD><INPUT type="text" size="2" name="isRecrue" value="<%=viewBean.getIsRecrue()%>"></TD>
							<TD><ct:FWLabel key="JSP_ANNONCES_PERIODE_DE"/></TD>
							<TD><ct:FWCalendarTag name="periodeDe" value="<%=viewBean.getPeriodeDe()%>"/></TD>
							<TD><ct:FWLabel key="JSP_ANNONCES_A"/></TD>
							<TD><ct:FWCalendarTag name="periodeA" value="<%=viewBean.getPeriodeA()%>"/></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_ANNONCES_NOMBRE_JOURS_SERVICE"/></TD>
							<TD><INPUT type="text" name="nombreJoursService" value="<%=viewBean.getNombreJoursService()%>"></TD>
							<TD><ct:FWLabel key="JSP_ANNONCES_GENRE_ACTIVITE"/></TD>
							<TD><INPUT type="text" name="genreActivite" value="<%=viewBean.getGenreActivite()%>"></TD>
							<TD><ct:FWLabel key="JSP_ANNONCES_REVENU_JOURNALIER_MOYEN"/></TD>
							<TD><INPUT type="text" name="revenuMoyenDeterminant" value="<%=viewBean.getRevenuMoyenDeterminant()%>"> </TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_ANNONCES_ALLOCATION_PERSONNE_SEULE"/></TD>
							<TD><INPUT type="text" size="2" name="isAllocationPersonneSeule" value="<%=viewBean.getIsAllocationPersonneSeule()%>"></TD>
							<TD><ct:FWLabel key="JSP_ANNONCES_ALLOCATION_MENAGE"/></TD>
							<TD><INPUT type="text" size="2" name="isAllocationMenage" value="<%=viewBean.getIsAllocationMenage()%>"></TD>
							<TD><ct:FWLabel key="JSP_ANNONCES_NOMBRE_ENFANTS"/></TD>
							<TD><INPUT type="text" name="nombreEnfants" value="<%=viewBean.getNombreEnfants()%>"></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_ANNONCES_ALLOCATION_EXPLOITATION"/></TD>
							<TD><INPUT type="text" size="2" name="isAllocationExploitation" value="<%=viewBean.getIsAllocationExploitation()%>"></TD>
							<TD><ct:FWLabel key="JSP_ANNONCES_ALLOCATION_ASSISTANCE_JOUR"/></TD>
							<TD><INPUT type="text" name="montantAllocationAssistance" value="<%=viewBean.getMontantAllocationAssistance()%>"></TD>
							<TD colspan="2"></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_ANNONCES_TAUX_JOURNALIER"/></TD>
							<TD><INPUT type="text" name="tauxJournalier" value="<%=viewBean.getTauxJournalier()%>"></TD>
							<TD><ct:FWLabel key="JSP_ANNONCES_TOTAL_APG"/></TD>
							<TD><INPUT type="text" name="totalAPG" value="<%=viewBean.getTotalAPG()%>"></TD>
							<TD><ct:FWLabel key="JSP_ANNONCES_SIGNE_TOTAL_APG"/></TD>
							<TD><INPUT type="text" size="2" name="signeMontantAllocation" value="<%=viewBean.getSigneMontantAllocation()%>"></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_ANNONCES_ALLOCATION_BASE"/></TD>
							<TD><INPUT type="text" size="2" name="isAllocationBase" value="<%=viewBean.getIsAllocationBase()%>"></TD>
							<TD><ct:FWLabel key="JSP_ANNONCES_TAUX_JOURNALIER_ALLOCATION_BASE"/></TD>
							<TD><INPUT type="text" name="tauxJournalierAllocationBase" value="<%=viewBean.getTauxJournalierAllocationBase()%>"></TD>
							<TD colspan="2"></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_ANNONCES_FRAIS_GARDE"/></TD>
							<TD><INPUT type="text" size="2" name="isAllocationFraisGarde" value="<%=viewBean.getIsAllocationFraisGarde()%>"></TD>
							<TD><ct:FWLabel key="JSP_ANNONCES_MONTANT_ALLOCATION_FRAIS_GARDE"/></TD>
							<TD><INPUT type="text" name="montantAllocationFraisGarde" value="<%=viewBean.getMontantAllocationFraisGarde()%>"></TD>
							<TD><ct:FWLabel key="JSP_ANNONCES_SIGNE_ALLOCATION_FRAIS_GARDE"/></TD>
							<TD><INPUT type="text" name="signeAllocationFraisGarde" value="<%=viewBean.getSigneAllocationFraisGarde()%>"></TD>
						</TR> 
						<TR>
							<TD><ct:FWLabel key="JSP_ANNONCES_ALLOCATION_ISOLEE_FRAIS_GARDE"/></TD>
							<TD><INPUT type="text" size="2" name="isAllocationIsoleeFraisGarde" value="<%=viewBean.getIsAllocationIsoleeFraisGarde()%>"></TD>
							<TD colspan="4">
							<a href="<%=lienSurPrestation%>"> 
								<ct:FWLabel key="PRESTATION"/>
							</a>
							</TD>
						</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>