<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>
<%-- /tpl:insert --%>
<script type="text/javascript"
	src="<%=servletContext%>/scripts/nss.js"></script>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>

<%@page import="java.util.Vector"%>
<%
	//Les labels de cette page commence par le préfix "JSP_PC_DOS_R"
	idEcran="PPC0001";
	//Liste pour tri	
	Vector orderList=new Vector(2);
	orderList.add(new String[]{"nomPrenom",objSession.getLabel("JSP_PC_DOS_R_TRIER_PAR_NOM")});
	//on garde les critères de recherches
	rememberSearchCriterias = true;
	//Récuération du paramètre idDossier
	String idDossier = request.getParameter("idDossier");
	String nss = request.getParameter("dossierSearch.likeNss");
	//Si null, on le set à vide
	if (JadeStringUtil.isNull(idDossier)){
		idDossier="";
	}
	boolean cocheCasFamille = ("true").equalsIgnoreCase(request.getParameter("cocheCasFamille"));
	
%>
<%@page import="globaz.pegasus.utils.PCGestionnaireHelper"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions" %>
<%@ include file="/theme/find/javascripts.jspf" %>

<%-- tpl:insert attribute="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="pegasus-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="pegasus-optionsdossiers">
</ct:menuChange>


<script type="text/javascript">


usrAction = "<%=IPCActions.ACTION_DOSSIER+".lister"%>";
actionNew =  "<%=servletContext + mainServletPath + "?userAction="+IPCActions.ACTION_DOSSIER + ".afficher&_method=add"%>";


$(function(){
	//Champ caché checkbox
	var $_membreFamilleValueSet = $('#membreFamilleValueSet');
	//Checkbox
	var $_checkBoxForMembreFamille = $('#forRechercheMembreFamille');
	//init objet jquery
	var $_nssHidenSearchField = $('#hiddenDossierSearchlikeNss');//champ caché de recherche du taglib nss
	var $_nssSearchField = $('#partialdossierSearchlikeNss');//champ de recherche du taglib nss
	var $_nssDossierSearch = $('[name=dossierSearchlikeNss]');
	//on set la valeur au chargement
	$_nssHidenSearchField.val($_nssDossierSearch.val());
	//on force le champ caché checbox à false
	$_membreFamilleValueSet.attr('value','true');
	//Sur chanhe on set la valeur
	$_nssSearchField.change(function(){
		$_nssHidenSearchField.val($_nssDossierSearch.val());
	});
	//Si idDossier null, on cache le champ idDossier
	<%if(JadeStringUtil.isBlankOrZero(idDossier)){%>
		$('#forIdDossier,[for=forIdDossier]').hide().after('&nbsp;');
	<%}%>
	
	//Gestion case à cocher, pour permettre un réaffichage de la case à cocher avec le bouton back, on va setter un champ caché
	//qui permettra la prise en compte de la valeur du checkbox
	$_checkBoxForMembreFamille.click(function () {
		if($(this).is(':checked')){
			$_membreFamilleValueSet.attr('value','true');
		}else{
			$_membreFamilleValueSet.attr('value','false');
		}
	});
});

function clearFields () {
	$('.clearable,#dossierSearch\\.forCsSexe,#partiallikeNumeroAVS,#hiddenlikeNumeroAVS,#partialdossierSearchlikeNss').val('');
	$('#dossierSearch\\.enCours,#dossierSearch\\.enRevision').attr('checked', false);
	$('#partiallikeNumeroAVS').focus();
}

/**
 * gestion des champs du rc
 */
var dealFields = function () {
	var b_fieldsSet = false;
	//Champ caché checkbox
	var $_membreFamilleValueSet = $('#membreFamilleValueSet');
	
	//itertaion sur les champs, exclusion hidde, button et submit, et du prefixe nss, et orderBy
	$(':input:not(:hidden):not(:button):not(:submit):not([name="dossierSearchlikeNssNssPrefixe"]):not([name="dossierSearch.orderBy"])').each(function (){
		//Si champ vide
		if(this.value!==''&&this.value!=='0'){
			//Si c'est recheche famille checkbox
			if(this.name==='isRechercheMembreFamille'){
				//on gere la case a cocher, la recherche auto se lancera uniquement si un autre champ est setter
				if($_membreFamilleValueSet.prop('value')==='true'){
					$('#forRechercheMembreFamille').prop('checked','checked');
					$_membreFamilleValueSet.attr('value','true');
				}
			}else{
				b_fieldsSet = true;
			}	
		}
	});
	return b_fieldsSet;
}
/**
 * Fonction postinit, une fois que tout est pret...
 */
function postInit() {
	//on set recherche auto si idDossier envoyé
	bFind = <%=!JadeStringUtil.isBlankOrZero(idDossier)%>;
	
	//Si bFind à false. on check les champs de retour (back)
	if(!bFind){
		bFind = dealFields();
	}
}



</script>
<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:insert attribute="zoneTitle" --%><ct:FWLabel key="JSP_PC_DOS_R_TITRE"/><%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:insert attribute="zoneMain" --%>
						<tr><td>
<TABLE border="0" cellspacing="0" cellpadding="0" width="100%">
									<TR>
									
										<TD width="13%"><LABEL for="dossierSearch.likeNss"><ct:FWLabel key="JSP_PC_DOS_R_NSS"/></LABEL>&nbsp;<INPUT type="hidden" name="hasPostitField" value="<%=true%>"></TD>
										<TD width="20%">
											<nss:nssPopup avsMinNbrDigit="99"
													  nssMinNbrDigit="99"
													  newnss=""
													  cssclass="nssPrefixe"
													  value="<%=nss%>"
													  name="dossierSearchlikeNss" />
													  <input type="hidden" id="hiddenDossierSearchlikeNss" name="dossierSearch.likeNss">
										</TD>
										<TD width="16%"><LABEL for="likeNom"><ct:FWLabel key="JSP_PC_DOS_R_NOM"/></LABEL>&nbsp;</TD>
										<TD width="20%"><INPUT type="text" name="dossierSearch.likeNom" class="clearable" value=""></TD>
										<TD width="10%"><LABEL for="likePrenom"><ct:FWLabel key="JSP_PC_DOS_R_PRENOM"/></LABEL>&nbsp;</TD>
										<TD width="20%"><INPUT type="text" name="dossierSearch.likePrenom" class="clearable" value=""></TD>
									</TR>
									<TR class="areaRechercheFamille">
										<TD><LABEL for="forRechercheMembreFamille"><ct:FWLabel key="JSP_PC_DOS_R_RECHERCHE_MEMBRES_FAMILLE"/></LABEL>&nbsp;</TD>
										<TD>
											<input type="checkbox" <%=(cocheCasFamille)?"checked='checked'":"" %> id="forRechercheMembreFamille" class="forRechercheMembreFamille" name="isRechercheMembreFamille" 
												data-g-commutator="context:$(this).parents('.areaRechercheFamille'),
													 			   condition:context.find('.forRechercheMembreFamille').prop('checked')==false,
													 			   actionTrue:¦show(context.find('.notAvailableForRechercherFamille'))¦,
													 			   actionFalse:¦hide(context.find('.notAvailableForRechercherFamille'))¦"/>
											<input type="text" id="membreFamilleValueSet" style="display:none;" name="membreFamilleValueSet" />	
										</TD>									
										<TD class="notAvailableForRechercherFamille" ><LABEL for="forDateNaissance"><ct:FWLabel key="JSP_PC_DOS_R_DATE_NAISSANCE"/></LABEL>&nbsp;</TD>
										<TD class="notAvailableForRechercherFamille"><input type="text" name="dossierSearch.forDateNaissance" class="clearable" value="" data-g-calendar="mandatory:false"/></TD>
										<TD class="notAvailableForRechercherFamille"><LABEL for="forCsSexe"><ct:FWLabel key="JSP_PC_DOS_R_SEXE"/></LABEL>&nbsp;</TD>	
										<TD class="notAvailableForRechercherFamille"><ct:FWCodeSelectTag name="dossierSearch.forCsSexe" codeType="PYSEXE" defaut="" wantBlank="true"/></TD>
									</TR>
									<TR><TD colspan="6">&nbsp;</TD></TR>
									<TR>
										<TD><LABEL for="csEtat"><ct:FWLabel key="JSP_PC_DOS_R_ETAT"/></LABEL>&nbsp;</TD>
										<TD>
										    <select name="csEtat">
										   		<option value="0"></option>
										   		<option value="EN_REVISION"><ct:FWLabel key="JSP_PC_DOS_R_EN_REVISION"/></option>
										   		<option value="EN_COURS"><ct:FWLabel key="JSP_PC_DOS_R_EN_COURS"/></option>
										   		<option value="EN_PREMIERE_INSTRUCTION"><ct:FWLabel key="JSP_PC_DOS_R_EN_PREMIERE_INSTRUCTION"/></option>
										    </select>
										</TD>
										<TD><LABEL for="forIdDossier"><ct:FWLabel key="JSP_PC_DEM_R_NO_DOSSIER"/></LABEL></TD>
										<TD><INPUT type="text" size="22" name="dossierSearch.forIdDossier" id="forIdDossier" value="<%=idDossier%>" class="disabled" readonly tabindex="-1"></TD>
										<TD></TD> 
										<TD></TD>
									</TR>
									<TR><TD colspan="6">&nbsp;</TD></TR>
									<TR>
										<TD><LABEL for="forIdGestionnaire"><ct:FWLabel key="JSP_PC_DOS_R_GESTIONNAIRE"/></LABEL>&nbsp;</TD>
										<TD><ct:FWListSelectTag data="<%=PCGestionnaireHelper.getResponsableData(objSession)%>"
																defaut="" 
															    name="dossierSearch.forIdGestionnaire"/></TD>
										<TD colspan="2">&nbsp;</TD>
										<TD><ct:FWLabel key="JSP_TRIER_PAR"/>&nbsp;</TD>
										<TD><ct:FWListSelectTag data="<%=orderList%>" defaut="" name="dossierSearch.orderBy" /></TD>
									</TR>
									<TR><TD colspan="6">&nbsp;</TD></TR>
									<TR>
										<TD><input type="button" onclick="clearFields()" accesskey="C" value="Effacer">[ALT+C]</TD>
										<TD colspan="5">&nbsp;</TD>										
									</TR>
								</TABLE>						
						</td></tr>
						
						
	 					<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:insert attribute="zoneVieuxBoutons" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
