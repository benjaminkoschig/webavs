<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@ page language="java" import="globaz.globall.http.*" errorPage="/errorPage.jsp"  contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="ch.globaz.pyxis.business.model.PersonneEtendueComplexModel"%>

<%@page import="globaz.pegasus.utils.PCDroitHandler"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDroits"%>
<%@page import="ch.globaz.pegasus.business.models.droit.Droit"%>
<%@page import="globaz.pegasus.vb.droit.PCCalculDroitViewBean"%>
<%@page import="globaz.pegasus.utils.PCUserHelper"%>
<%@ include file="/theme/detail_ajax/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<script type="text/javascript" src="<%=servletContext%>/scripts/jquery.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/jquery-ui.js"></script>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath+"Root")%>/scripts/droit/calculDroit_de.js"/></script>
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/bootstrap.css"/>

<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%
	//Les labels de cette page commence par le préfix "JSP_PC_CACUL_DROIT_D"
	idEcran="PPC0005";

	PCCalculDroitViewBean viewBean = (PCCalculDroitViewBean) session.getAttribute("viewBean");
	Droit droitCourant=viewBean.getDroit();
	PersonneEtendueComplexModel personne= droitCourant.getDemande().getDossier().getDemandePrestation().getPersonneEtendue();
	
	autoShowErrorPopup = true;
	
	bButtonNew      = false;
	bButtonUpdate   = false;
	bButtonValidate = false;
	bButtonCancel 	= false;	
	bButtonDelete   = false;
	
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/droit/calculDroit_de.css"/>
<!--  notation spécifique pc -->
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath+"Root")%>/scripts/notationsCandidate/globazPreventDoubleClick.js"></script>

<%-- tpl:put name="zoneScripts" --%>
<script type="text/javascript">
var ACTION_DROIT="<%=IPCActions.ACTION_DROIT_CALCULER%>";
//Autorisation du calcul mois suivant, deux cas date de fin de demande et blocage modifs périodes 
var isCMSBloquedDemandeClose = <%=viewBean.isCMSBlockedDemandeClose()%>;

var labelProcessStartingLabel = "<%= objSession.getLabel("JSP_PC_CALCUL_DROIT_D_PROCESS_RUN") %>";

//Calcul standard bloqué, pas de donnée financières pour la version
var isStandardCalBloquedNonDfFoVersion = <%= viewBean.isCalculStandardBloqueNonDFModif() %>;
//Calcul standard bloqué les dates de debut des df en dehors plage calcul	
var isStandardCalBloquedNoPeriod = <%= viewBean.isCalculStandardBloqueNonDFPlageCalcul() %>;
//calcul standard bloque df clore periode
var isStandardCalBloquedClorePeriod = <%= viewBean.isCMSBloqueDFClorePeriode()%>;

//Blocage calcul mois suivant si erreur dans les périodes
var isCMSBlockedDueManyPeriodModfis = <%=viewBean.isCMSBlockedDuToError()%>;
//SC CALCUL SANS RETRO
var csCalculSansRetro = "<%=IPCDroits.CS_DROIT_TYPE_DE_CALCUL_SANS_RETRO %>";
//Libelles pour les message d'erreur du blocage CMS 
var titreCMSBlocked = "<%=objSession.getLabel("JSP_CMS_MANY_DF_ERROR_TITLE")%>";
var contentCMSBlocked = "<%=objSession.getLabel("JSP_CMS_MANY_DF_ERROR_CONTENT")%>";
// et warn priodes retro...
var titreRetroWarn = "<%=objSession.getLabel("JSP_CMS_RETRO_WARN_TITLE")%>";
var contentRetroWarn = "<%=objSession.getLabel("JSP_CMS_RETRO_WARN_CONTENT")%>";
//Vlocage cms demande close
var titreCMSDemandeCloseWarn = "<%=objSession.getLabel("JSP_CMS_DEMANDE_CLOSE_WARN_TITLE")%>";
var contentCMSDemandeCloseWarn = "<%=objSession.getLabel("JSP_CMS_DEMANDE_CLOSE_WARN_CONTENT")%>";

var dateDebutPlageCalcul = "<%= viewBean.getDateDebutPlageCalcul() %>";

var noVersion = <%= viewBean.getDroit().getSimpleVersionDroit().getNoVersion() %>;
var isDFCreeOrModifForVersion = <%= viewBean.getIsDfModifForVersion()%>;

var isCalculeBloqueDuAujourDappoint = <%= viewBean.isCalculBloqueDuAujourDappoint()%>;

var isCalculInterdit = <%= viewBean.isCalculInterdit()%>;

var motifAdaptation = <%= viewBean.getMotifDroitAdapatation() %>;
var csMotifDroit = <%= viewBean.getDroit().getSimpleVersionDroit().getCsMotif() %>;

var $zoneDonneeFinancieres;

//blocage effet mois suivant si janvier
var isEffetMoisSuivantBloque = <%=viewBean.isEffetMoisSuivantBloque()%>;
var titreMoisSuivantBlocked = "<%=objSession.getLabel("JSP_CMS_SUIVANT_BLOCKED_WARN_TITLE")%>";
var contentMoisSuivantBlocked = "<%=objSession.getLabel("JSP_CMS_SUIVANT_BLOCKED_WARN_CONTENT")%>";

//blocage du calcul pour droit motif adaptation
var calculInterditMotifDroitAdaptation = (csMotifDroit === motifAdaptation);

function validate() {
	state = true;
    var form=document.forms[0];
    form.elements('userAction').value=ACTION_DROIT+".calculer";
    return state;
}  




globazGlobal.label = {
	calculInterdit:"<%=objSession.getLabel("JSP_PC_CALCUL_DROIT_D_CALCUL_INTERDIT")%>",
	calculInterditJourAppoint:"<%=objSession.getLabel("JSP_PC_CALCUL_DROIT_D_CALCUL_INTERDIT_JOUR_APPOINT")%>",
	calculStandardInterditAucuneDonneeFinanciere:"<%=objSession.getLabel("JSP_PC_CALCUL_DROIT_D_CALCUL_STANDARD_INTERDIT_PAS_DE_DONNEE_FINANCIERE")%>",
	calculStandardInterditPasDansLaPlageDeCalcule:"<%=objSession.getLabel("JSP_PC_CALCUL_DROIT_D_CALCUL_STANDARD_INTERDIT_PLAGE")%>",
	calculStrandardInterditClorePeriode:"<%=objSession.getLabel("JSP_PC_CALCUL_DROIT_D_CALCUL_STANDARD_INTERDIT_CLORE_PERIODE") + " " + viewBean.getDateProchainPaiement()%>",
	calculInterditMotifAdaptation:"<%= objSession.getLabel("JSP_PC_CALCUL_DROIT_D_CALCUL__INTERDIT_MOTIF_ADAPTATION")%>"
}
</script>


<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_PC_CALCULDROIT_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
<TR>
		<TD colspan="6" >
			
			<!--  ************************* Zone infos requerant  *************************  -->
			<div id="infos_requerant" class="titre">
				<h1 class="ui-widget-header "><ct:FWLabel key="JSP_PC_INFOS_REQUERANT"/></h1>
				<%=PCDroitHandler.getInfoHtmlRequerant(droitCourant.getSimpleVersionDroit(), personne)%>
			</div>
			
			<!--  ************************* Zone type de calcul  *************************  -->
			<div id="type_calcul" class="titre">
				<h1 class="ui-widget-header "><ct:FWLabel key="JSP_PC_CACUL_DROIT_D_TYPE_CALCULE"/></h1>
				<div id="contenu_typecalcul" class="gl-pre-content" style="background-color: #FFFFFF;" >
					<!--  ligne boutons radios -->
					<div class="row-fluid">
						<div class="span3 typeDeCalculStandard">
							<input type="radio" name="typeDeCalcule" id="typeDeCalcule_NORMAL" 
							       value="<%=IPCDroits.CS_DROIT_TYPE_DE_CALCUL_STANDARD %>"/>
							<div id="lbl_standard"><%=objSession.getCodeLibelle(IPCDroits.CS_DROIT_TYPE_DE_CALCUL_STANDARD) %> </div>
						</div>
						<div class="span2 typeCalculSansRetro">
							<input type="radio" name="typeDeCalcule" id="typeDeCalcule_SANS_RETRO" 
						       value="<%=IPCDroits.CS_DROIT_TYPE_DE_CALCUL_SANS_RETRO %>" />
							<div id="lbl_retro"> <%=objSession.getCodeLibelle(IPCDroits.CS_DROIT_TYPE_DE_CALCUL_SANS_RETRO) %> </div>
						</div>
					</div>
					<!--  ************************ Bouton calculer ******************* -->
					<div id="zone_bouton_calcul">
						
						<input id="calculer_btn" value="<%=objSession.getLabel("JSP_PC_CALCUL_DROIT_D_BOUTON_CALCULER") %>" type="button" data-g-preventdoubleclick="label:labelProcessStartingLabel,labelCssClass:lbl_process_running "/>
						
					</div>
				</div>
				<!--  ************************* etat process running ************************* -->
				
				
			</div>
			
			
			
			
			<!--  ************************* liste DF modifie ou cree ************************* -->
			<%= viewBean.getDFForVersionListHtml() %>
			<!-- **************************** Confimation modification date df -->
			<div id="dialog-modifiydf-confim" title="<%= objSession.getLabel("JSP_CMS_CONFIRM_TILTE")%>">
    			<p class="confirmContent"><span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span>
    			<%=viewBean.getMessageConfirmCalculMoisSuivants()%></p>
    			<p class="confirmAttention">
    				<%= objSession.getLabel("JSP_CMS_CONFIRM_CONTENT_ATTENTION")%>
    			</p>
			</div>
			
		</TD>
	</TR>
	<input type="hidden" name="idDroit" value="<%=viewBean.getDroit().getId() %>"/>
					<input type="hidden" name="noVersion" value="<%=viewBean.getDroit().getSimpleVersionDroit().getNoVersion() %>"/>
					<input type="hidden" name="idDemande" value="<%=viewBean.getIdDemande() %>"/>
					<input type="hidden" name="idVersionDroit" value="<%=viewBean.getIdVersionDroit() %>"/>
					<input type="hidden" name="idsEntityGroup" value=" <%= viewBean.getDonneesFinancieresForVersionAsString() %>" />
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>