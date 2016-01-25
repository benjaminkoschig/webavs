<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java"
	errorPage="/errorPage.jsp" import="globaz.globall.http.*"
	contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
<%@ include file="/theme/detail/header.jspf"%>
<%-- tpl:put name="zoneInit" --%>
<%
	//Les labels de cette page commence par la préfix "JSP_AD10_D"

	idEcran = "PRE0074";

	globaz.corvus.vb.annonces.REAnnoncesDiminution10EmeViewBean viewBean
		= (globaz.corvus.vb.annonces.REAnnoncesDiminution10EmeViewBean) session.getAttribute("viewBean");

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
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf"%>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.caisse.helper.CaisseHelperFactory"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.corvus.servlet.IREActions"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" />
<ct:menuChange displayId="options" menuId="corvus-optionsannonces"
	showTab="menu">
</ct:menuChange>

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
	
	function formatMensualitePrestations(mensualitePrestation){
		mensualite = mensualitePrestation.value;
		if(mensualite.length>0){
			while(mensualite.length<5){
				mensualite = '0' + mensualite;
			}
			mensualitePrestation.value = mensualite;
		}
	}
</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf"%>
<%-- tpl:put name="zoneTitle" --%>
<ct:FWLabel key="JSP_ANN_D_TITRE_DIM_10" />
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf"%>
<%-- tpl:put name="zoneMain" --%>

	<TR>
		<TD><ct:FWLabel key='JSP_AD10_D_ID_ANNONCE'/></TD>
		<TD>
			<input type="text" name="idAnnonce" value="<%=viewBean.getIdAnnonce()%>" class=disabled readonly>
			<input type="hidden" name="idRenteAccordee" value="<%=viewBean.getIdRenteAccordee()%>">
			<input type="hidden" name="idLienAnnonce" value="<%=viewBean.getIdLienAnnonce()%>">
			<input type="hidden" name="idTiers" value="<%=viewBean.getIdTiers()%>">
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
		<TD colspan="6"><b><ct:FWLabel key='JSP_AD10_D_CODE_ENREG_01'/></b></TD>
	</TR>
	<TR>
		<TD colspan="6"><hr></TD>
	</TR>
	<TR>
		<TD><ct:FWLabel key='JSP_AD10_D_CODE_APPLI'/></TD>
		<TD><input type="text" name="codeApplication" value="<%=viewBean.isNew()?"45":viewBean.getCodeApplication()%>" class=disabled readonly></TD>
		<TD><ct:FWLabel key='JSP_AD10_D_CODE_ENREGISTREMENT'/></TD>
		<TD><input type="text" name="codeEnregistrement01" value="<%=viewBean.isNew()?"01":viewBean.getCodeEnregistrement01()%>" class=disabled readonly></TD>
		<TD><ct:FWLabel key='JSP_AD10_D_NO_CAISSE'/></TD>
		<TD><input type="text" name="numeroCaisse" value="<%=viewBean.isNew()?caisseHelper.getNoCaisse(viewBean.getSession().getApplication()):viewBean.getNumeroCaisse()%>"></TD>
	</TR>
	<TR>
		<TD><ct:FWLabel key='JSP_AD10_D_NO_AGENCE'/></TD>
		<TD><input type="text" name="numeroAgence" value="<%=viewBean.isNew()?caisseHelper.getNoAgence(viewBean.getSession().getApplication()):viewBean.getNumeroAgence()%>"></TD>
		<TD><ct:FWLabel key='JSP_AD10_D_NO_ANNONCE'/></TD>
		<TD><input type="text" name="numeroAnnonce" value="<%=viewBean.getNumeroAnnonce()%>"></TD>
		<TD><ct:FWLabel key='JSP_AD10_D_REF_CAISSE_INTERNE'/></TD>
		<TD><input type="text" name="referenceCaisseInterne" value="<%=viewBean.getReferenceCaisseInterne()%>"></TD>
	</TR>
	<TR>
		<TD><ct:FWLabel key='JSP_AD10_D_NO_ASSURE_AYANT_DROIT'/></TD>
		<TD><input type="text" name="noAssAyantDroit" value="<%=viewBean.getNoAssAyantDroit()%>"></TD>
		<TD><ct:FWLabel key='JSP_AD10_D_PREMIER_NO_AYANT_DROIT'/></TD>
		<TD><input type="text" name="premierNoAssComplementaire" value="<%=viewBean.getPremierNoAssComplementaire()%>"></TD>
		<TD><ct:FWLabel key='JSP_AD10_D_SECOND_NO_AYANT_DROIT'/></TD>
		<TD><input type="text" name="secondNoAssComplementaire" value="<%=viewBean.getSecondNoAssComplementaire()%>"></TD>
	</TR>
	<TR>
		<TD><ct:FWLabel key='JSP_AD10_D_NOUV_NO_AYANT_DROIT'/></TD>
		<TD><input type="text" name="nouveauNumeroAssureAyantDroit" value="<%=viewBean.getNouveauNumeroAssureAyantDroit()%>"></TD>
		<TD><ct:FWLabel key='JSP_AD10_D_ETAT_CIVIL'/></TD>
		<TD><input type="text" name="etatCivil" value="<%=viewBean.getEtatCivil()%>"></TD>
		<TD><ct:FWLabel key='JSP_AD10_D_REFUGIE'/></TD>
		<TD><input type="text" name="isRefugie" value="<%=viewBean.getIsRefugie()%>"></TD>
	</TR>
	<TR>
		<TD><ct:FWLabel key='JSP_AD10_D_CANTON_ETAT_DOMICILE'/></TD>
		<TD><input type="text" name="cantonEtatDomicile" value="<%=viewBean.getCantonEtatDomicile()%>"></TD>
		<TD><ct:FWLabel key='JSP_AD10_D_GENRE_PRESTATION'/></TD>
		<TD><input type="text" name="genrePrestation" value="<%=viewBean.getGenrePrestation()%>"></TD>
		<TD><ct:FWLabel key='JSP_AD10_D_DEBUT_DROIT'/></TD>
		<TD><input type="text" name="debutDroit" value="<%=viewBean.getDebutDroit()%>"></TD>
	</TR>
	<TR>
		<TD><ct:FWLabel key='JSP_AD10_D_MENS_PRESTATIONS'/></TD>
		<TD><input type="text" name="mensualitePrestationsFrancs" value="<%=viewBean.getMensualitePrestationsFrancs()%>" onchange="formatMensualitePrestations(this);"></TD>
		<TD><ct:FWLabel key='JSP_AD10_D_FIN_DROIT'/></TD>
		<TD><input type="text" name="finDroit" value="<%=viewBean.getFinDroit()%>"></TD>
		<TD><ct:FWLabel key='JSP_AD10_D_MOIS_RAPPORT'/></TD>
		<TD><input type="text" name="moisRapport" value="<%=viewBean.getMoisRapport()%>"></TD>
	</TR>
	<TR>
		<TD><ct:FWLabel key='JSP_AD10_D_CODE_MUTATION'/></TD>
		<TD><input type="text" name="codeMutation" value="<%=viewBean.getCodeMutation()%>"></TD>
	</TR>
	<TR>
		<TD colspan="6"><hr></TD>
	</TR>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf"%>
<%-- tpl:put name="zoneButtons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf"%>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf"%>
<%-- /tpl:insert --%>