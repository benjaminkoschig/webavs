<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java"
	errorPage="/errorPage.jsp" import="globaz.globall.http.*"
	contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
<%@ include file="/theme/detail/header.jspf"%>
<%-- tpl:put name="zoneInit" --%>
<%
	//Les labels de cette page commence par la préfix "JSP_AA9_D"
	
	idEcran = "PRE0073";

	globaz.corvus.vb.annonces.REAnnoncesAugmentationModification9EmeViewBean viewBean
		= (globaz.corvus.vb.annonces.REAnnoncesAugmentationModification9EmeViewBean) session.getAttribute("viewBean");

	selectedIdValue = viewBean.getIdAnnonce();

	bButtonUpdate = bButtonUpdate && viewBean.isModifierPermis() && viewBean.getSession().hasRight(IREActions.ACTION_ANNONCES, FWSecureConstants.UPDATE);
	bButtonDelete = bButtonDelete && viewBean.isSupprimerPermis() && viewBean.getSession().hasRight(IREActions.ACTION_ANNONCES, FWSecureConstants.UPDATE);

	CaisseHelperFactory caisseHelper = CaisseHelperFactory.getInstance();
	
	String forMoisRapport = request.getParameter("forMoisRapport");
	if(JadeStringUtil.isNull(forMoisRapport)){
		forMoisRapport = "";
	}
	
	String forCsEtat = request.getParameter("forCsEtat");
	if(JadeStringUtil.isNull(forCsEtat)){
		forCsEtat = "";
	}
	
	String forCsCodeTraitement = request.getParameter("forCsCodeTraitement");
	if(JadeStringUtil.isNull(forCsCodeTraitement)){
		forCsCodeTraitement = "";
	}
	
	String menuOptionToLoad = request.getParameter("menuOptionToLoad");
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf"%>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.caisse.helper.CaisseHelperFactory"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%if(JadeStringUtil.isNull(menuOptionToLoad) || JadeStringUtil.isEmpty(menuOptionToLoad)){%>
	<%@page import="globaz.corvus.servlet.IREActions"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" />
	<ct:menuChange displayId="options" menuId="corvus-optionsannonces" showTab="menu">
	</ct:menuChange>
<%}%>

<SCRIPT language="javascript">

	function add(){
	    document.forms[0].elements('userAction').value="corvus.annonces.annonce.actionAjouterAnnonce";
	}

	function upd(){
	}

	function validate() {
	    state = validateFields();
	    if (document.forms[0].elements('_method').value == "add"){
	        document.forms[0].elements('userAction').value="corvus.annonces.annonce.actionAjouterAnnonce";
	    }else{
	        document.forms[0].elements('userAction').value="corvus.annonces.annonce.actionModifierAnnonce";
	    }
	    return state;
	}

	function cancel() {
		if (document.forms[0].elements('_method').value == "add"){
		  document.forms[0].elements('userAction').value="back";
		}else{
		  document.forms[0].elements('userAction').value="corvus.annonces.annonce.chercher";
		}
	}

	function del() {
	    if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
	        document.forms[0].elements('userAction').value="corvus.annonces.annonce.actionSupprimerAnnonce";
	        document.forms[0].submit();
	    }
	}

	function init(){
	}
</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf"%>
<%-- tpl:put name="zoneTitle" --%>
<ct:FWLabel key="JSP_ANN_D_TITRE_AUG_9" />
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf"%>
<%-- tpl:put name="zoneMain" --%>

	<TR>
		<TD><ct:FWLabel key='JSP_AA9_D_ID_ANNONCE'/></TD>
		<TD>
			<input type="text" name="idAnnonce" value="<%=viewBean.getIdAnnonce()%>" class=disabled readonly>
			<input type="hidden" name="idLienAnnonce" value="<%=viewBean.getIdLienAnnonce()%>">
			<input type="hidden" name="idTiers" value="<%=viewBean.getIdTiers()%>">
			<input type="hidden" name="idRenteAccordee" value="<%=viewBean.getIdRenteAccordee()%>">
			<input type="hidden" name="forMoisRapport" value="<%=forMoisRapport%>">
			<input type="hidden" name="forCsEtat" value="<%=forCsEtat%>">
			<input type="hidden" name="forCsCodeTraitement" value="<%=forCsCodeTraitement%>">
		</TD>
		<TD colspan="4"></TD>
	</TR>
	<TR>
		<TD colspan="6"><hr></TD>
	</TR>
	<TR>
		<TD colspan="6"><b><ct:FWLabel key='JSP_AA9_D_CODE_ENREG_01'/></b></TD>
	</TR>
	<TR>
		<TD colspan="6"><hr></TD>
	</TR>
	<TR>
		<TD><ct:FWLabel key='JSP_AA9_D_CODE_APPLI'/></TD>
		<TD><input type="text" name="codeApplication" value="<%=viewBean.isNew()?"41":viewBean.getCodeApplication()%>" class=disabled readonly></TD>
		<TD><ct:FWLabel key='JSP_AA9_D_CODE_ENREGISTREMENT'/></TD>
		<TD><input type="text" name="codeEnregistrement01" value="<%=viewBean.isNew()?"01":viewBean.getCodeEnregistrement01()%>" class=disabled readonly></TD>
		<TD><ct:FWLabel key='JSP_AA9_D_NO_CAISSE'/></TD>
		<TD><input type="text" name="numeroCaisse" value="<%=viewBean.isNew()?caisseHelper.getNoCaisse(viewBean.getSession().getApplication()):viewBean.getNumeroCaisse()%>"></TD>
	</TR>
	<TR>
		<TD><ct:FWLabel key='JSP_AA9_D_NO_AGENCE'/></TD>
		<TD><input type="text" name="numeroAgence" value="<%=viewBean.isNew()?caisseHelper.getNoAgence(viewBean.getSession().getApplication()):viewBean.getNumeroAgence()%>"></TD>
		<TD><ct:FWLabel key='JSP_AA9_D_NO_ANNONCE'/></TD>
		<TD><input type="text" name="numeroAnnonce" value="<%=viewBean.getNumeroAnnonce()%>"></TD>
		<TD><ct:FWLabel key='JSP_AA9_D_REF_CAISSE_INTERNE'/></TD>
		<TD><input type="text" name="referenceCaisseInterne" value="<%=viewBean.getReferenceCaisseInterne()%>"></TD>
	</TR>
	<TR>
		<TD><ct:FWLabel key='JSP_AA9_D_NO_ASSURE_AYANT_DROIT'/></TD>
		<TD><input type="text" name="noAssAyantDroit" value="<%=viewBean.getNoAssAyantDroit()%>"></TD>
		<TD><ct:FWLabel key='JSP_AA9_D_PREMIER_NO_AYANT_DROIT'/></TD>
		<TD><input type="text" name="premierNoAssComplementaire" value="<%=viewBean.getPremierNoAssComplementaire()%>"></TD>
		<TD><ct:FWLabel key='JSP_AA9_D_SECOND_NO_AYANT_DROIT'/></TD>
		<TD><input type="text" name="secondNoAssComplementaire" value="<%=viewBean.getSecondNoAssComplementaire()%>"></TD>
	</TR>
	<TR>
		<TD><ct:FWLabel key='JSP_AA9_D_NOUV_NO_AYANT_DROIT'/></TD>
		<TD><input type="text" name="nouveauNoAssureAyantDroit" value="<%=viewBean.getNouveauNoAssureAyantDroit()%>"></TD>
		<TD><ct:FWLabel key='JSP_AA9_D_ETAT_CIVIL'/></TD>
		<TD><input type="text" name="etatCivil" value="<%=viewBean.getEtatCivil()%>"></TD>
		<TD><ct:FWLabel key='JSP_AA9_D_REFUGIE'/></TD>
		<TD><input type="text" name="isRefugie" value="<%=viewBean.getIsRefugie()%>"></TD>
	</TR>
	<TR>
		<TD><ct:FWLabel key='JSP_AA9_D_CANTON_ETAT_DOMICILE'/></TD>
		<TD><input type="text" name="cantonEtatDomicile" value="<%=viewBean.getCantonEtatDomicile()%>"></TD>
		<TD><ct:FWLabel key='JSP_AA9_D_GENRE_PRESTATION'/></TD>
		<TD><input type="text" name="genrePrestation" value="<%=viewBean.getGenrePrestation()%>"></TD>
		<TD><ct:FWLabel key='JSP_AA9_D_DEBUT_DROIT'/></TD>
		<TD><input type="text" name="debutDroit" value="<%=viewBean.getDebutDroit()%>"></TD>
	</TR>
	<TR>
		<TD><ct:FWLabel key='JSP_AA9_D_MENS_PRESTATIONS'/></TD>
		<TD><input type="text" name="mensualitePrestationsFrancs" value="<%=viewBean.getMensualitePrestationsFrancs()%>"></TD>
		<TD><ct:FWLabel key='JSP_AA9_D_MENS_RENTE_ORDIN_REMPLACEE'/></TD>
		<TD><input type="text" name="mensualiteRenteOrdRemp" value="<%=viewBean.getMensualiteRenteOrdRemp()%>"></TD>
	</TR>
	<TR>
		<TD><ct:FWLabel key='JSP_AA9_D_FIN_DROIT'/></TD>
		<TD><input type="text" name="finDroit" value="<%=viewBean.getFinDroit()%>"></TD>
		<TD><ct:FWLabel key='JSP_AA9_D_MOIS_RAPPORT'/></TD>
		<TD><input type="text" name="moisRapport" value="<%=viewBean.getMoisRapport()%>"></TD>
		<TD><ct:FWLabel key='JSP_AA9_D_CODE_MUTATION'/></TD>
		<TD><input type="text" name="codeMutation" value="<%=viewBean.getCodeMutation()%>"></TD>
	</TR>
	<TR>
		<TD colspan="6"><hr></TD>
	</TR>
	<% if (viewBean.isNew() || !viewBean.getCodeEnregistrement02().isNew()) { %>
		<TR>
			<TD colspan="6"><b><ct:FWLabel key='JSP_AA9_D_CODE_ENREG_02'/></b></TD>
		</TR>
		<TR>
			<TD colspan="6"><hr></TD>
		</TR>
		<TR>
			<TD><ct:FWLabel key='JSP_AA9_D_CODE_APPLI'/></TD>
			<TD><input type="text" name="codeApplication" value="<%=viewBean.isNew()?"41":viewBean.getCodeEnregistrement02().getCodeApplication()%>" class=disabled readonly></TD>
			<TD><ct:FWLabel key='JSP_AA9_D_CODE_ENREGISTREMENT'/></TD>
			<TD><input type="text" name="codeEnr02CodeEnregistrement01" value="<%=viewBean.isNew()?"02":viewBean.getCodeEnregistrement02().getCodeEnregistrement01()%>" class=disabled readonly></TD>
			<TD><ct:FWLabel key='JSP_AA9_D_RAM'/></TD>
			<TD><input type="text" name="ramDeterminant" value="<%=viewBean.getCodeEnregistrement02().getRamDeterminant()%>"></TD>
		</TR>
		<TR>
			<TD><ct:FWLabel key='JSP_AA9_D_DUREE_COTI_POUR_DET_RAM'/></TD>
			<TD><input type="text" name="dureeCotPourDetRAM" value="<%=viewBean.getCodeEnregistrement02().getDureeCotPourDetRAM()%>"></TD>
			<TD><ct:FWLabel key='JSP_AA9_D_ANNEE_NIVEAU'/></TD>
			<TD><input type="text" name="anneeNiveau" value="<%=viewBean.getCodeEnregistrement02().getAnneeNiveau()%>"></TD>
			<TD><ct:FWLabel key='JSP_AA9_D_REVENU_PRIS_EN_COMPTE'/></TD>
			<TD><input type="text" name="revenuPrisEnCompte" value="<%=viewBean.getCodeEnregistrement02().getRevenuPrisEnCompte()%>"></TD>
		</TR>
		<TR>
			<TD><ct:FWLabel key='JSP_AA9_D_ECHELLE_RENTE'/></TD>
			<TD><input type="text" name="echelleRente" value="<%=viewBean.getCodeEnregistrement02().getEchelleRente()%>"></TD>
			<TD><ct:FWLabel key='JSP_AA9_D_DUREE_COTI_AVANT_1973'/></TD>
			<TD><input type="text" name="dureeCoEchelleRenteAv73" value="<%=viewBean.getCodeEnregistrement02().getDureeCoEchelleRenteAv73()%>"></TD>
			<TD><ct:FWLabel key='JSP_AA9_D_DUREE_COTI_DES_1973'/></TD>
			<TD><input type="text" name="dureeCoEchelleRenteDes73" value="<%=viewBean.getCodeEnregistrement02().getDureeCoEchelleRenteDes73()%>"></TD>
		</TR>
		<TR>
			<TD><ct:FWLabel key='JSP_AA9_D_COTI_MANQUANTE_48_72'/></TD>
			<TD><input type="text" name="dureeCotManquante48_72" value="<%=viewBean.getCodeEnregistrement02().getDureeCotManquante48_72()%>"></TD>
			<TD><ct:FWLabel key='JSP_AA9_D_ANNEE_COTI_CLASSE_AGE'/></TD>
			<TD><input type="text" name="anneeCotClasseAge" value="<%=viewBean.getCodeEnregistrement02().getAnneeCotClasseAge()%>"></TD>
			<TD><ct:FWLabel key='JSP_AA9_D_DUREE_AJOURNEMENT'/></TD>
			<TD><input type="text" name="dureeAjournement" value="<%=viewBean.getCodeEnregistrement02().getDureeAjournement()%>"></TD>
		</TR>
		<TR>
			<TD><ct:FWLabel key='JSP_AA9_D_SUPP_AJOURNEMENT'/></TD>
			<TD><input type="text" name="supplementAjournement" value="<%=viewBean.getCodeEnregistrement02().getSupplementAjournement()%>"></TD>
			<TD><ct:FWLabel key='JSP_AA9_D_DATE_REV_AJOURNEMENT'/></TD>
			<TD><input type="text" name="dateRevocationAjournement" value="<%=viewBean.getCodeEnregistrement02().getDateRevocationAjournement()%>"></TD>
			<TD><ct:FWLabel key='JSP_AA9_D_LIMITE_REVENU'/></TD>
			<TD><input type="text" name="isLimiteRevenu" value="<%=viewBean.getCodeEnregistrement02().getIsLimiteRevenu()%>"></TD>
		</TR>
		<TR>
			<TD><ct:FWLabel key='JSP_AA9_D_MIN_GARANTI'/></TD>
			<TD><input type="text" name="isMinimumGaranti" value="<%=viewBean.getCodeEnregistrement02().getIsMinimumGaranti()%>"></TD>
			<TD><ct:FWLabel key='JSP_AA9_D_OFFICE_AI_COMPETENT'/></TD>
			<TD><input type="text" name="officeAICompetent" value="<%=viewBean.getCodeEnregistrement02().getOfficeAICompetent()%>"></TD>
			<TD><ct:FWLabel key='JSP_AA9_D_OFFICE_AI_COMPETENT_EPOUSE'/></TD>
			<TD><input type="text" name="officeAiCompEpouse" value="<%=viewBean.getCodeEnregistrement02().getOfficeAiCompEpouse()%>"></TD>
		</TR>
		<TR>
			<TD><ct:FWLabel key='JSP_AA9_D_DEGRE_INVALIDITE'/></TD>
			<TD><input type="text" name="degreInvalidite" value="<%=viewBean.getCodeEnregistrement02().getDegreInvalidite()%>"></TD>
			<TD><ct:FWLabel key='JSP_AA9_D_DEGRE_INVALIDITE_EPOUSE'/></TD>
			<TD><input type="text" name="degreInvaliditeEpouse" value="<%=viewBean.getCodeEnregistrement02().getDegreInvaliditeEpouse()%>"></TD>
			<TD><ct:FWLabel key='JSP_AA9_D_CODE_INFIRMITE'/></TD>
			<TD><input type="text" name="codeInfirmite" value="<%=viewBean.getCodeEnregistrement02().getCodeInfirmite()%>"></TD>
		</TR>
		<TR>
			<TD><ct:FWLabel key='JSP_AA9_D_CODE_INFIRMITE_EPOUSE'/></TD>
			<TD><input type="text" name="codeInfirmiteEpouse" value="<%=viewBean.getCodeEnregistrement02().getCodeInfirmiteEpouse()%>"></TD>
			<TD><ct:FWLabel key='JSP_AA9_D_SURVENANCE_EVT_ASSURE'/></TD>
			<TD><input type="text" name="survenanceEvenAssure" value="<%=viewBean.getCodeEnregistrement02().getSurvenanceEvenAssure()%>"></TD>
			<TD><ct:FWLabel key='JSP_AA9_D_SURVENANCE_EVT_ASSURE_EPOUSE'/></TD>
			<TD><input type="text" name="survenanceEvtAssureEpouse" value="<%=viewBean.getCodeEnregistrement02().getSurvenanceEvtAssureEpouse()%>"></TD>
		</TR>
		<TR>
			<TD><ct:FWLabel key='JSP_AA9_D_AGE_DEBUT_INVALIDITE'/></TD>
			<TD><input type="text" name="ageDebutInvalidite" value="<%=viewBean.getCodeEnregistrement02().getAgeDebutInvalidite()%>"></TD>
			<TD><ct:FWLabel key='JSP_AA9_D_AGE_DEBUT_INVALIDITE_EPOUSE'/></TD>
			<TD><input type="text" name="ageDebutInvaliditeEpouse" value="<%=viewBean.getCodeEnregistrement02().getAgeDebutInvaliditeEpouse()%>"></TD>
			<TD><ct:FWLabel key='JSP_AA9_D_GENRE_DROIT_API'/></TD>
			<TD><input type="text" name="genreDroitAPI" value="<%=viewBean.getCodeEnregistrement02().getGenreDroitAPI()%>"></TD>
		</TR>
		<TR>
			<TD><ct:FWLabel key='JSP_AA9_D_REDUCTION'/></TD>
			<TD><input type="text" name="reduction" value="<%=viewBean.getCodeEnregistrement02().getReduction()%>"></TD>
			<TD><ct:FWLabel key='JSP_AA9_D_CAS_SPECIAL_1'/></TD>
			<TD><input type="text" name="casSpecial1" value="<%=viewBean.getCodeEnregistrement02().getCasSpecial1()%>"></TD>
			<TD><ct:FWLabel key='JSP_AA9_D_CAS_SPECIAL_2'/></TD>
			<TD><input type="text" name="casSpecial2" value="<%=viewBean.getCodeEnregistrement02().getCasSpecial2()%>"></TD>
		</TR>
		<TR>
			<TD><ct:FWLabel key='JSP_AA9_D_CAS_SPECIAL_3'/></TD>
			<TD><input type="text" name="casSpecial3" value="<%=viewBean.getCodeEnregistrement02().getCasSpecial3()%>"></TD>
			<TD><ct:FWLabel key='JSP_AA9_D_CAS_SPECIAL_4'/></TD>
			<TD><input type="text" name="casSpecial4" value="<%=viewBean.getCodeEnregistrement02().getCasSpecial4()%>"></TD>
			<TD><ct:FWLabel key='JSP_AA9_D_CAS_SPECIAL_5'/></TD>
			<TD><input type="text" name="casSpecial5" value="<%=viewBean.getCodeEnregistrement02().getCasSpecial5()%>"></TD>
		</TR>
		<TR>
			<TD><ct:FWLabel key='JSP_AA9_D_DUREE_COTI_MANQUANTES_73_78'/></TD>
			<TD><input type="text" name="dureeCotManquante73_78" value="<%=viewBean.getCodeEnregistrement02().getDureeCotManquante73_78()%>"></TD>
			<TD><ct:FWLabel key='JSP_AA9_D_RAM_SANS_BTE'/></TD>
			<TD><input type="text" name="revenuAnnuelMoyenSansBTE" value="<%=viewBean.getCodeEnregistrement02().getRevenuAnnuelMoyenSansBTE()%>"></TD>
			<TD><ct:FWLabel key='JSP_AA9_D_BTE_MOYENNES'/></TD>
			<TD><input type="text" name="bteMoyennePrisEnCompte" value="<%=viewBean.getCodeEnregistrement02().getBteMoyennePrisEnCompte()%>"></TD>
		</TR>
			<TD><ct:FWLabel key='JSP_AA9_D_NBR_ANNEES_BTE'/></TD>
			<TD><input type="text" name="nombreAnneeBTE" value="<%=viewBean.getCodeEnregistrement02().getNombreAnneeBTE()%>"></TD>
		</TR>

	<% } %>


<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf"%>
<%-- tpl:put name="zoneButtons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf"%>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf"%>
<%-- /tpl:insert --%>