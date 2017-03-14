<%-- tpl:insert page="/theme/find.jtpl" --%>
<%@ page language="java" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="globaz.pegasus.utils.PCGestionnaireHelper"%>

<%@page import="globaz.pegasus.vb.demande.PCDemandeViewBean"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDemandes"%>
<%@page import="java.util.Vector"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="ch.globaz.pegasus.businessimpl.checkers.demande.SimpleDemandeChecker"%>

<link type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/smoothness/jquery-ui-1.7.2.custom.css" rel="stylesheet" />	
<script type="text/javascript"
	src="<%=servletContext%>/scripts/nss.js"></script>
	
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%
	//Les labels de cette page commence par le préfix "JSP_PC_DEM_R"
	idEcran="PPC0003";
	//Liste pour tri	
	Vector orderList=new Vector(2);
	orderList.add(new String[]{"nomPrenom",objSession.getLabel("JSP_PC_DOS_R_TRIER_PAR_NOM")});
	//on garde les critères de recherches
 	rememberSearchCriterias = true;
 	//Gestion des paramètres
	String idDossier = request.getParameter("idDossier");
	String idDemandePc = request.getParameter("idDemandePc");
	//Si null, on le set à vide
	if(JadeStringUtil.isNull(idDemandePc)){
		idDemandePc = "";
	}
	//Si idDossier null, pas de bouton new demande
	if (JadeStringUtil.isNull(idDossier)){
		idDossier="";
		bButtonNew=false;
	}else{
		//Sinon, on check si etat new demande ok
		
	//	bButtonNew = SimpleDemandeChecker.isPossibleToCreateNewDeamande(idDossier);
		actionNew =  servletContext + mainServletPath + "?userAction=" + IPCActions.ACTION_DEMANDE_DETAIL + ".afficher&_method=add&idDossier="+idDossier;
	}
%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<ct:menuChange displayId="menu" menuId="pegasus-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="pegasus-optionsdemandes">
	<ct:menuSetAllParams key="idDossier" value="<%=idDossier%>"/>
</ct:menuChange>

<SCRIPT language="JavaScript">
	bFind = <%=!JadeStringUtil.isBlankOrZero(idDossier)||!JadeStringUtil.isBlankOrZero(idDemandePc)%>;
	usrAction = "<%=IPCActions.ACTION_DEMANDE%>.lister";
	$(function(){
		//init objet jquery
		$_nssHidenSearchField = $('#hiddenlikeNumeroAVS');//champ caché de recherche du taglib nss
		$_nssSearchField = $('#partiallikeNumeroAVS');//champ de recherche du taglib nss
		$_nssDossierSearch = $('[name=likeNumeroAVS]');
		//on set la valeur au chargement
		$_nssHidenSearchField.val($_nssDossierSearch.val());
		//Sur chanhe on set la valeur
		$_nssSearchField.change(function(){
			$_nssHidenSearchField.val($_nssDossierSearch.val());
		});
		//Si idDossier vide
		
		
		
		<%if(JadeStringUtil.isEmpty(idDossier)){%>
			// on cache le champ idDossier
			$('#forIdDossier,[for=forIdDossier]').hide().after('&nbsp;');
			<%}else{%>
				//Si pas vide lien vers les dossier
				$('[for=forIdDossier]').wrap('<a id="toDossierLink" href="" />');
				//Clic sur le lien, rcListe dossier
				$('#toDossierLink').click(function () {
					window.location.href='pegasus?userAction=pegasus.dossier.dossier.chercher&idDossier=<%=idDossier%>';
				});
		<%}%>
		
		//Si idDemande vide, 
		<%if(JadeStringUtil.isEmpty(idDemandePc)){%>
			// hide forIdDemande field and fill cell with blank
			$('#forIdDemande,[for=forIdDemande]').hide().after('&nbsp;');
		<%}%>
	});
	
	/**
	 * gestion des champs du rc
	 */
	var dealFields = function () {
		var b_fieldsSet = false;
		
		//itertaion sur les champs, exclusion hidde, button et submit, et du prefixe nss, et orderBy
		$(':input:not(:hidden):not(:button):not(:submit):not([name="likeNumeroAVSNssPrefixe"]):not([name="listDemandesSearch.orderBy"])').each(function (){
			//Si champ vide
			if(this.value!==''&&this.value!=='0'){
				b_fieldsSet = true;
			}
		});
		return b_fieldsSet;
	}
	function clearFields () {
		$('.clearable,#listDemandesSearch\\.forCsSexe,#partiallikeNumeroAVS,#hiddenlikeNumeroAVS,#listDemandesSearch\\.forCsEtatDemande').val('');
		$('#listDemandesSearch\\.orderBy').val("HTLDE1,HTLDE2,YADDEB DESC");
		$('#partiallikeNumeroAVS').focus();
	}
	
	/**
	 * Fonction postinit, une fois que tout est pret...
	 */
	function postInit() {
		//on set recherche auto si idDossier ou idDemande envoyé
		bFind = <%=!JadeStringUtil.isBlankOrZero(idDossier)||!JadeStringUtil.isBlankOrZero(idDemandePc)%>;
		
		//Si bFind à false. on check les champs de retour (back)
		if(!bFind){
			bFind = dealFields();
		}
		
	}
</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>
			
				<ct:FWLabel key="JSP_PC_DEM_R_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
<TR>
							<TD>
							<!--  <INPUT type="hidden" name="demandeSearch.forIdDemande" id="foridDemandePc" value="<%=idDemandePc%>" class="disabled" readonly tabindex="-1"> -->
								<TABLE border="0" cellspacing="0" cellpadding="0" width="100%" class="rcFields">
									<TR>
									
										<TD><LABEL for="likeNumeroAVS"><ct:FWLabel key="JSP_PC_DEM_R_NSS"/></LABEL>&nbsp;<INPUT type="hidden" name="listDemandesSearch.hasPostitField" id="hasPostitField" value="true"></TD>
										<TD>
											<nss:nssPopup avsMinNbrDigit="99"
													  nssMinNbrDigit="99"
													  newnss=""
													  name="likeNumeroAVS" />
											<input type="hidden" id="hiddenlikeNumeroAVS" name="listDemandesSearch.likeNss">													  
										</TD>
										<TD><LABEL for="likeNom"><ct:FWLabel key="JSP_PC_DEM_R_NOM"/></LABEL>&nbsp;</TD>
										<TD><INPUT type="text" id="likeNom" name="listDemandesSearch.likeNom" class="clearable" value=""></TD>
										<TD><LABEL for="likePrenom"><ct:FWLabel key="JSP_PC_DEM_R_PRENOM"/></LABEL>&nbsp;</TD>
										<TD><INPUT type="text" id="likePrenom" name="listDemandesSearch.likePrenom" class="clearable" value=""></TD>
									</TR>
									<TR>
										<td>&nbsp;</td><td>&nbsp;</td>
										<TD><LABEL for="forDateNaissance"><ct:FWLabel key="JSP_PC_DEM_R_DATE_NAISSANCE"/></LABEL>&nbsp;</TD>
										<TD><input type="text" id="forDateNaissance" name="forDateNaissance" class="clearable" value="" data-g-calendar="mandatory:false"/></TD>
										<TD><LABEL for="forCsSexe"><ct:FWLabel key="JSP_PC_DEM_R_SEXE"/></LABEL>&nbsp;</TD>	
										<TD colspan="3"><ct:FWCodeSelectTag name="listDemandesSearch.forCsSexe" codeType="PYSEXE" defaut="" wantBlank="true"/></TD>
									</TR>
									<TR><TD colspan="6">&nbsp;</TD></TR>
									<TR>
										<TD><LABEL for="forIdDemande"><ct:FWLabel key="JSP_PC_DEM_R_NO_DEMANDE"/></LABEL></TD>
										<TD><INPUT type="text" name="listDemandesSearch.forIdDemande" id="forIdDemande" value="<%=idDemandePc%>" class="disabled" readonly tabindex="-1"></TD>
										<TD><LABEL for="forIdDossier"><ct:FWLabel key="JSP_PC_DEM_R_NO_DOSSIER"/></LABEL></TD>
										<TD><INPUT type="text" name="listDemandesSearch.forIdDossier" id="forIdDossier" value="<%=idDossier%>" class="disabled" readonly tabindex="-1"></TD>
										<TD><LABEL for="forCsEtatDemande"><ct:FWLabel key="JSP_PC_DEM_R_ETAT_DEMANDE"/></LABEL>&nbsp;</TD>
										<TD colspan="3"><ct:FWCodeSelectTag codeType="<%=IPCDemandes.CS_ETAT_DEMANDE%>" name="listDemandesSearch.forCsEtatDemande" wantBlank="true" defaut="blank"/></TD>
									</TR>
									<TR><TD colspan="6">&nbsp;</TD></TR>
									<TR>
										<TD><LABEL for="forIdGestionnaireDemande"><ct:FWLabel key="JSP_PC_DEM_R_GESTIONNAIRE"/></LABEL>&nbsp;</TD>
										<TD><ct:FWListSelectTag data="<%=PCGestionnaireHelper.getResponsableData(objSession)%>"
																defaut="" 
																name="listDemandesSearch.forIdGestionnaire"/></TD>
										<TD colspan="2">&nbsp;</TD>
										<TD><ct:FWLabel key="JSP_TRIER_PAR"/>&nbsp;</TD>
										<TD><ct:FWListSelectTag data="<%=orderList%>" defaut="" name="listDemandesSearch.orderBy" /></TD>
									</TR>
									<TR><TD colspan="6">&nbsp;</TD></TR>
									<TR>
										<TD>
											<input 	type="button" 
													onclick="clearFields()" 
													accesskey="<ct:FWLabel key="AK_EFFACER"/>" 
													value="<ct:FWLabel key="JSP_EFFACER"/>">
											<label>
												[ALT+<ct:FWLabel key="AK_EFFACER"/>]
											</label>
										</TD>
										<TD colspan="5">&nbsp;</TD>
									</TR>
								</TABLE>
							</TD>
						</TR>		
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>