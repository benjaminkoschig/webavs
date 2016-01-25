<%-- tpl:insert page="/theme/find.jtpl" --%>
<%@page import="globaz.perseus.utils.PFNssHelper"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="ch.globaz.perseus.business.constantes.IPFActions"%>
<%@page import="globaz.perseus.utils.PFGestionnaireHelper"%>
<%--<%@page import="globaz.jade.persistence.model.JadeAbstractModel"--%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>

<%-- tpl:insert attribute="zoneInit" --%>
	<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
	<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%
// Les labels de cette page commence par le préfix "JSP_PF_DECISION_R"
	idEcran="PPF0212";
	rememberSearchCriterias = false;
	
	String idDecision = request.getParameter ("idDecision");
	String idDemande = "";
	if (request.getParameter("idDemande") != null) {
		idDemande = request.getParameter ("idDemande");
	}
	bButtonNew = false;
%>

<%-- /tpl:insert --%>
<%@ include file="/theme/find/javascripts.jspf" %>

<%-- tpl:insert attribute="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="perseus-menuprincipal" showTab="menu"/>

<script type="text/javascript" language="JavaScript" >
	bFind = true;
	detailLink = "<%=actionNew%>";
	var usrAction = "<%=IPFActions.ACTION_DECISION%>.lister";

	
	
	//sur envoi 
	function onClickFind(){
		//Recup champ no dec deux partie
		var partAn = $('#inpNoDecAn').attr('value');
		var partInc= $('#inpNoDecInc').attr('value');

		
		//Chaine a setter sur champ hidden (que si un des deux champs n'est pas vide)
		if(partAn!="" || partInc!=""){
			var chaineNoDec = partAn+"-"+partInc;
			$('#forNumeroDecision').attr('value',chaineNoDec);
		}
	}
	
	$(function(){
	
		<%if(JadeStringUtil.isEmpty(idDemande)){%>
			// hide forIdDossier field and fill cell with blank
			$('#forIdDemande,[for=forIdDemande]').hide().after('&nbsp;');
		<%}%>
	});
	
	function clearFields () {
		$('.clearable,#searchModel\\.forCsSexe,#searchModel\\.forCsEtat,[name=searchModel\\.likeNss],#partialsearchModel\\.likeNss,#searchModel\\.forCsTypeDecision,#searchModel\\.forUtilisateurPreparation,#searchModel\\.forUtilisateurValidation').val('');
		$('#searchModel\\.forDansDernierLot').attr('checked', false);
		$('#partialsearchModel\\.likeNss').focus();
	}
	
</script>

<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyStart.jspf" %>

<%-- tpl:insert attribute="zoneTitle" --%><ct:FWLabel key="JSP_PF_DECISION_TITRE"/><%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>

<%-- tpl:insert attribute="zoneMain" --%>
	<TR>
		<TD>
			<TABLE border="0" cellspacing="0" cellpadding="0" width="100%">
				<TR>
					<TD>
						<LABEL for="likeNss"><ct:FWLabel key="JSP_PF_DECISION_R_NSS"/></LABEL>
					</TD>
					<TD>
						<nss:nssPopup avsMinNbrDigit="99"
						  nssMinNbrDigit="99"
						  newnss=""
						  name="searchModel.likeNss" value="<%=PFNssHelper.getNssLike(objSession) %>" />
					</TD>
					<TD><LABEL for="likeNom"><ct:FWLabel key="JSP_PF_DECISION_R_NOM"/></LABEL>&nbsp;</TD>
					<TD><INPUT type="text" name="searchModel.likeNom" value="" class="clearable"></TD>
					<TD><LABEL for="likePrenom"><ct:FWLabel key="JSP_PF_DECISION_R_PRENOM"/></LABEL>&nbsp;</TD>
					<TD><INPUT type="text" name="searchModel.likePrenom" value="" class="clearable"></TD>
				</TR>
				<TR><TD colspan="0">&nbsp;</TD>
					<td></td>
					<TD><LABEL for="forDateNaissance"><ct:FWLabel key="JSP_PF_DECISION_R_DATE_NAISS"/></LABEL>&nbsp;</TD>
					<TD><input type="text" name="searchModel.forDateNaissance" value="" data-g-calendar="mandatory:false" class="clearable"/>&nbsp;</TD>
					<TD><LABEL for="forCsSexe"><ct:FWLabel key="JSP_PF_DECISION_R_SEXE"/></LABEL>&nbsp;</TD>	
					<TD><ct:FWCodeSelectTag name="searchModel.forCsSexe" codeType="PYSEXE" defaut="" wantBlank="true"/></TD>
				</TR>
				<TR><TD colspan="6">&nbsp;</TD></TR>
				<TR>
					<TD><LABEL for="forUtilisateurPreparation"><ct:FWLabel key="JSP_PF_DECISION_R_PREPARER_PAR"/></LABEL>&nbsp;</TD>
					<TD><ct:FWListSelectTag data="<%=PFGestionnaireHelper.getResponsableData(objSession)%>" defaut="" name="searchModel.forUtilisateurPreparation"/>&nbsp;</TD>
					<TD><LABEL for="forUtilisateurValidation"><ct:FWLabel key="JSP_PF_DECISION_R_VALIDER_PAR"/></LABEL>&nbsp;</TD>
					<TD><ct:FWListSelectTag data="<%=PFGestionnaireHelper.getResponsableData(objSession)%>" defaut="" name="searchModel.forUtilisateurValidation"/>&nbsp;</TD>
					<TD colspan="2">&nbsp;</TD>
				</TR>
				<TR>
					<TD><LABEL for="forDateDebutDroit"><ct:FWLabel key="JSP_PF_DECISION_R_DATE_DEBUT_DU_DROIT"/></LABEL>&nbsp;</TD>
					<TD><input type="text" name="searchModel.forDateDebutDroit" value="" data-g-calendar="mandatory:false" class="clearable"/></TD>
					<TD><LABEL for="forDateDebutValidation"><ct:FWLabel key="JSP_PF_DECISION_R_DEPUIS_DATE_VALIDATION"/></LABEL>&nbsp;</TD>
					<TD><input type="text" name="searchModel.forDateDebutValidation" value="" data-g-calendar="mandatory:false" class="clearable"/></TD>
					<TD><LABEL for="forIdDemande"><ct:FWLabel key="JSP_PF_DECISION_R_ID_DEMANDE"/></LABEL>&nbsp;</TD>
					<TD><INPUT type="text" name="searchModel.forIdDemande" id="forIdDemande" value="<%=idDemande %>" class="disabled" readonly tabindex="-1">&nbsp;</TD>
				</TR>
				<TR><TD colspan="6">&nbsp;</TD></TR>
				<TR>
					<td><label for="forNumeroDecision"><ct:FWLabel key="JSP_PF_DECISION_L_NUMERO" /></label>&nbsp;</td>
					<td>
						<input id="inpNoDecAn" style="width:45px" maxlength="4" type="text" name="searchModel.forNumeroDecisionAn" value="" class="clearable"/>-
						<input id="inpNoDecInc" style="width:60px" maxlength="6" type="text" name="searchModel.forNumeroDecisionInc" value="" class="clearable"/>
						<input type="hidden" id="forNumeroDecision" name="searchModel.forNumeroDecision" value="" class="clearable"/>
					</td>
					<TD><LABEL for="forCsEtat"><ct:FWLabel key="JSP_PF_DECISION_R_ETAT"/></LABEL>&nbsp;</TD>
					<TD><ct:FWCodeSelectTag name="searchModel.forCsEtat" codeType="PFETATDEC" defaut="" wantBlank="true"/>&nbsp;</TD>					
					<TD><LABEL for="forCsTypeDecision"><ct:FWLabel key="JSP_PF_DECISION_R_TYPE"/></LABEL>&nbsp;</TD>
					<TD><ct:FWCodeSelectTag name="searchModel.forCsTypeDecision" codeType="PFTYPEDEC" defaut="" wantBlank="true"/></TD>	
<!--				<TD><LABEL for="forDernierLot"><ct:FWLabel key="JSP_PF_DECISION_R_DERNIER_LOT"/></LABEL>&nbsp;</TD>-->
<!--				<TD><INPUT type="checkbox" id="searchModel.forDernierLot" value="" />&nbsp;</TD>-->
				</TR>
				<TR><TD colspan="6">&nbsp;</TD></TR>
				<TR>
					<TD><input type="button" onclick="clearFields()" accesskey="C" value="Effacer">[ALT+C]</TD>
					<TD colspan="5">&nbsp;</TD>										
				</TR>
			</TABLE>
		</TD>
	</TR>



	 					<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:insert attribute="zoneVieuxBoutons" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyClose.jspf" %>



