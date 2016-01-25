<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>

<%
	idEcran ="CGE0002";
	GEAnnoncesViewBean viewBean = (GEAnnoncesViewBean)session.getAttribute ("viewBean");
	if (GEAnnoncesViewBean.CS_ETAT_VALIDE.equals(viewBean.getCsEtatAnnonce()) || GEAnnoncesViewBean.CS_ETAT_COMPTABILISE.equals(viewBean.getCsEtatAnnonce())){
		bButtonUpdate=false;
		bButtonDelete=false;
	}
	if(!viewBean.isNew() && viewBean != null 
			&& viewBean.getSession().hasRight("campus.annonces.annonces.afficher", FWSecureConstants.UPDATE)){
		bButtonNew=true;
	}

	actionNew  += "&idLot=" + ((viewBean.getIdLot()!=null)?viewBean.getIdLot():"") ;
%>
<%@page import="globaz.globall.util.JAUtil"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.globall.util.JACalendar"%>
<%@page import="globaz.campus.vb.annonces.GEAnnoncesViewBean"%>
<SCRIPT language="JavaScript">
</SCRIPT> 
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">

function add() {
	document.forms[0].elements('userAction').value="campus.annonces.annonces.ajouter";
}

function upd() {
}

function validate() {
	if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="campus.annonces.annonces.ajouter";
    else
        document.forms[0].elements('userAction').value="campus.annonces.annonces.modifier";
	return (true);
}

function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="campus.annonces.annonces.afficher";
}

function del() {
	if (window.confirm("Vous êtes sur le point de supprimer le détail d'assurance sélectionné! Voulez-vous continuer?")) {
		document.forms[0].elements('userAction').value="campus.annonces.annonces.supprimer";
		document.forms[0].submit();
	}
}

function init() {}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
					Détail d'une annonce
					<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

						<TR>
							<TD width="20%">N° Lot</TD>
							<TD width="30%"><input name='idLot' class="numeroCourtDisabled" readonly 
								value='<%=viewBean.getIdLot()!=null?viewBean.getIdLot():request.getParameter("idLot")%>'></TD>
							<TD width="20%">N° séquence</TD>
							<TD width="30%"><input name='idLot' class="libelleLongDisabled" readonly 
								value='<%=viewBean.getNumSequence()!=null?viewBean.getNumSequence():""%>'></TD>
						</TR>
						<TR>
							<TD>Nom</TD>
							<TD><input name='nom' class='libelleLong' tabindex="1" value="<%=viewBean.getNom()!=null?viewBean.getNom():""%>"></TD>
							<TD>N° Immatriculation</TD>
							<TD><input name='numImmatriculationTransmis' class="numeroLong" tabindex="6"
								value="<%=viewBean.getNumImmatriculationTransmis()!=null?viewBean.getNumImmatriculationTransmis():""%>"></TD>
						</TR>
						<TR>
							<TD>Prénom</TD>
							<TD><input name='prenom' class='libelleLong' tabindex="2" value="<%=viewBean.getPrenom()!=null?viewBean.getPrenom():""%>"></TD>
							<TD>Etat civil</TD>
							<TD><ct:FWCodeSelectTag name="csEtatCivil" 
								defaut="<%=viewBean.getCsEtatCivil()%>" 
								codeType="PYETATCIVI" wantBlank="true"/>
								<script>
									document.getElementsByName("csEtatCivil")[0].tabIndex = 7;
								</script>
						    </TD>
						<TR>
							<TD>NSS</TD>
							<TD>
							<%
							String ean13 ="false";
							if(!JadeStringUtil.isNull(viewBean.getNumAvs())){
								if (viewBean.getNumAvs().length()==16) {
						  	     	 ean13 ="true";
								}	
							}
							if(ean13.equals("true")){
							%>
							<nss:nssPopup tabindex="3" avsMinNbrDigit="99" nssMinNbrDigit="99" newnss="<%=ean13%>" name="numAvs" value="<%=viewBean.getNumAvs().substring(3,16)%>" />
	            			<%}else{ %>
	            			<nss:nssPopup tabindex="3" avsMinNbrDigit="99" nssMinNbrDigit="99" newnss="<%=ean13%>" name="numAvs" value="<%=viewBean.getNumAvs()%>" />
							<%} %>
							
							</TD>
							<TD>Sexe</TD>
							<TD><ct:FWCodeSelectTag name="csSexe"
							    	defaut="<%=viewBean.getCsSexe()%>"
						      		codeType="PYSEXE"
						      		wantBlank="true"/>
						      		<script>
										document.getElementsByName("csSexe")[0].tabIndex = 8;
									</script>
						    </TD>
						</TR>
						<TR>
							<TD>Date de naissance</TD>
							<TD><% String dateNaissanceValue = viewBean.getDateNaissance()!=null?viewBean.getDateNaissance():""; %>
							    <ct:FWCalendarTag name="dateNaissance" 
									value="<%=dateNaissanceValue%>"
									doClientValidation="CALENDAR"/>
									<script>
										document.getElementsByName("dateNaissance")[0].tabIndex = 4;
									</script>
							</TD>
						    <TD>Code doctorant</TD>
							<TD><ct:FWCodeSelectTag name="csCodeDoctorant"
							    	defaut="<%=viewBean.getCsCodeDoctorant()%>"
						      		codeType="GECODE_DOC"
						      		wantBlank="true"/>
						      		<script>
										document.getElementsByName("csCodeDoctorant")[0].tabIndex = 9;
									</script>
						    </TD>
						</TR>
						<%
						if(viewBean.getNom().length()>2){
							viewBean.setNomCourt(viewBean.getNom().substring(0,2));
						}else{
							viewBean.setNomCourt(viewBean.getNom());
						}
						String nomCourt = viewBean.getNomCourt(); 	
						%>

						<input type="hidden" name="nomCourt" value="<%=nomCourt%>">
						</TR>
							<TD>Sélectionner un tiers</TD>
							<TD>				
							<%
							Object[] tiersMethodsName = new Object[]{
								new String[]{"setIdTiersEtudiant","getIdTiers"},
								new String[]{"setNom","getDesignation1_tiers"},
								new String[]{"setPrenom","getDesignation2_tiers"},
								new String[]{"setDateNaissance","getDateNaissance"},
								new String[]{"setCsEtatCivil","getEtatCivil"},
								new String[]{"setCsSexe","getSexe"},
								new String[]{"setNumAvs","getNumAvsActuel"},
								new String[]{"setRueDomicile","getRue"},
								new String[]{"setNumeroDomicile","getNumero"},
								new String[]{"setNpaLegal","getNpa"},
								new String[]{"setLocaliteLegal","getLocalite"},
								new String[]{"setSuffixePostalDomicile","getNpa_sup"},
								new String[]{"setAdresseLegale","attention"},
							};
							
							Object[]  tiersParams = new Object[]{
								new String[]{"nomCourt","forDesignationUpper1Like"},
								new String[]{"dateNaissance","forDateNaissance"},
								new String[]{"csSexe","forSexe"}
							};
							String redirectUrl = ((String)request.getAttribute("mainServletPath")+"Root")+"/"+ globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session)+"/annonces/annonces_de.jsp";
													
							if(!JadeStringUtil.isBlankOrZero(viewBean.getIdTiersEtudiant())){
								//Recherche de l'adresse de courrier.
								viewBean.rechercheAdresseCourrier(viewBean.getIdTiersEtudiant());
							}
							%>
				            <ct:FWSelectorTag 
								name="tiersSelector"
								methods="<%=tiersMethodsName%>"
								providerApplication ="pyxis"
								providerPrefix="TI"
								providerAction ="pyxis.tiers.tiers.chercher"
								providerActionParams ="<%=tiersParams%>"
								redirectUrl="<%=redirectUrl%>"
							/>
							<script>
								document.getElementsByName("tiersSelector")[0].tabIndex = 5;
							</script>
							<input type="hidden" name="selectorName" value="">
							<input name="idTiersEtudiant" type="hidden" value='<%=viewBean.getIdTiersEtudiant()!=null?viewBean.getIdTiersEtudiant():""%>'/>
							<input name='rueDomicile' type="hidden" value="<%=viewBean.getRueDomicile()!=null?viewBean.getRueDomicile():""%>"/>
							<input name='numeroDomicile' type="hidden" value="<%=viewBean.getNumeroDomicile()!=null?viewBean.getNumeroDomicile():""%>"/>
							<input name='suffixePostalDomicile' type="hidden" value="<%=viewBean.getSuffixePostalDomicile()!=null?viewBean.getSuffixePostalDomicile():""%>"/>
							<INPUT type="hidden" name="selectedId" value="<%=viewBean.getIdAnnonce()%>">
							</TD>
						</TR>

						<TR>
							<TD width="100">Tiers forcé</TD>
            				<TD width="200">
								<INPUT type="checkbox" name="isTiersForce" <%=(viewBean.getIsTiersForce().booleanValue())? "checked" : "unchecked"%>>
	     					</TD>
	     					<TD>Force la création du tiers !</TD>
						</TR>
						<TR><TD>&nbsp</TD></TR>
						<TR> <TD nowrap  height="11" colspan="4"> <hr size="3" width="100%"></TD></TR>
						<TR><TD>&nbsp</TD></TR>
						<TR>
						<TH align="left">Adresse de domicile</TH>
						<TD></TD>
						<TH align="left">Adresse de courrier</TH>
						</TR>
						<TR>
						<TD>&nbsp</TD>
						</TR>
						<TR>
							<TD>P. A. légal</TD>
							<TD><input name='adresseLegale' class='libelleLong' tabindex="10"
								value="<%=viewBean.getAdresseEtude()!=null?viewBean.getAdresseLegale():""%>">
							</TD>
							<TD>P. A. Etude</TD>
							<TD><input name='adresseEtude' class='libelleLong' tabindex="15"
								value="<%=viewBean.getAdresseEtude()!=null?viewBean.getAdresseEtude():""%>">
							</TD>
						</TR>
						<TR>
							<TD>Rue et n°</TD>
							<TD><input name='rueLegal' class='libelleLong' tabindex="11"
								value="<%=viewBean.getRueLegal()!=null?viewBean.getRueLegal():""%>">
							</TD>
						    <TD>Rue et n°</TD>
							<TD><input name='rueEtude' class='libelleLong' tabindex="16"
								value="<%=viewBean.getRueEtude()!=null?viewBean.getRueEtude():""%>">
							</TD>
						</TR>
						<TR>
							<TD>NPA</TD>
							<TD><input name='npaLegal' class='numeroLong' tabindex="12"
								value="<%=viewBean.getNpaLegal()!=null?viewBean.getNpaLegal():""%>">
							</TD>
						    <TD>NPA</TD>
							<TD><input name='npaEtude' class='numeroLong' tabindex="17"
								value="<%=viewBean.getNpaEtude()!=null?viewBean.getNpaEtude():""%>">
							</TD>
						</TR>

						<TR>
							<TD>Localité</TD>
							<TD><input name='localiteLegal' class='libelleLong' tabindex="13"
								value="<%=viewBean.getLocaliteLegal()!=null?viewBean.getLocaliteLegal():""%>">
							</TD>
						    <TD>Localité</TD>
							<TD><input name='localiteEtude' class='libelleLong' tabindex="18"
								value="<%=viewBean.getLocaliteEtude()!=null?viewBean.getLocaliteEtude():""%>">
							</TD>
						</TR>
						<TR>
							<TD>Suffixe postal</TD>
							<TD><input name='suffixePostalLegal' class='numeroCourt' tabindex="14"
								value="<%=viewBean.getSuffixePostalLegal()!=null?viewBean.getSuffixePostalLegal():""%>">
							</TD>
						    <TD>Suffixe postal</TD>
							<TD><input name='suffixePostalEtude' class='numeroCourt' tabindex="19"
								value="<%=viewBean.getSuffixePostalEtude()!=null?viewBean.getSuffixePostalEtude():""%>">
							</TD>
						</TR>
						<TR>
						<TD>&nbsp</TD>
						</TR>
						<TR> 
							<TD nowrap height="11" colspan="4"><hr size="3" width="100%"></TD>
						</TR>
						<TR>
						<TD>&nbsp</TD>
						</TR>
						<TR>
						    <TD>Réserve Ligne 1</TD>
							<TD><input name='reserve1' class='libelleLong' tabindex="20"
								value="<%=viewBean.getReserve1()!=null?viewBean.getReserve1():""%>">
							</TD>
							<TD>Réserve Ligne 2</TD>
							<TD><input name='reserve3' class='libelleLong' tabindex="22"
								value="<%=viewBean.getReserve3()!=null?viewBean.getReserve3():""%>">
							</TD>							
						</TR>
						<TR>
							<TD>Réserve Ligne 1 bis</TD>
							<TD><input name='reserve2' class='libelleLong' tabindex="21"
								value="<%=viewBean.getReserve2()!=null?viewBean.getReserve2():""%>">
							</TD>
						   	<TD>Réserve Ligne 3</TD>
							<TD><input name='reserve4' class='libelleLong' tabindex="23"
								value="<%=viewBean.getReserve4()!=null?viewBean.getReserve4():""%>">
							</TD>
						</TR>
						<TR><TD>&nbsp</TD></TR>
						<TR> <TD nowrap  height="11" colspan="4"> <hr size="3" width="100%"></TD></TR>
						<TR><TD>&nbsp</TD></TR>
						<TR>
						    <TD>Etat Annonce</TD>
							<TD><ct:FWCodeSelectTag name="csEtatAnnonce"
							    	defaut="<%=viewBean.getCsEtatAnnonce()%>"
						      		codeType="GEETAT_ANN"
						      		wantBlank="false"/>
						      	<script>
								document.getElementsByName("csEtatAnnonce")[0].tabIndex = 24;
								</script>
						    </TD>
						</TR>
						<TR>
						<TD>&nbsp</TD>
						</TR>
						<TR> 
							<TD nowrap  height="11" colspan="4"> <hr size="3" width="100%"></TD>
						</TR>
						<TR><TD>&nbsp</TD></TR>

						<TR>
							<TH align="left">Messages d'erreurs</TH>
							<TD>
							&nbsp
            				<A href="<%=request.getContextPath()%>\pyxis?userAction=pyxis.tiers.tiers.chercher&forDesignationUpper1Like=<%=viewBean.getNom()%>&forDesignationUpper2Like=<%=viewBean.getPrenom()%>&forDateNaissance=<%=viewBean.getDateNaissance()%>" class="external_link">Tiers</A>
           					&nbsp
           					<A href="<%=request.getContextPath()%>\campus?userAction=campus.etudiants.etudiants.chercher&forNomLike=<%=viewBean.getNom()%>&forPrenomLike=<%=viewBean.getPrenom()%>&forNumImmatriculation=<%=viewBean.getNumImmatriculationTransmis()%>&forNumAvsLike=<%=viewBean.getNumAvs()%>">Etudiant</A>
           					&nbsp
           					<%if (!JadeStringUtil.isBlankOrZero(viewBean.getIdEtudiant())){ %>
            					<A href="<%=request.getContextPath()%>\naos?userAction=naos.affiliation.affiliation.chercher&idTiers=<%=viewBean.getIdTiers(session)%>" class="external_link">Affiliation</A>
           					<%} %>
           					<%if(!JadeStringUtil.isBlankOrZero(viewBean.getIdDecision())){ %>
           						&nbsp
            					<A href="<%=request.getContextPath()%>\naos?userAction=naos.affiliation.affiliation.rechercheDecisionCP&affiliationId=<%=viewBean.getIdAffiliation(session)%>" class="external_link">Décision</A>
           					<%} %>
           					</TD>
						</TR>
						<TR><TD>&nbsp</TD></TR>
						<TR>
						<TD colspan="4" height="99">
						<textarea rows="5" style="width: 100%"><%=viewBean.getMessageLog()%></textarea>
						<input type="hidden" name="idLog" value="<%=viewBean.getIdLog()%>">
						</TD>
						</TR>

						
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<% if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> 
<SCRIPT>
</SCRIPT> 
<% } %> 
	<ct:menuChange displayId="menu" menuId="GEMenuPrincipal"/>
	<ct:menuChange displayId="options" menuId="GEOptionsAnnonce" showTab="options">
		<ct:menuSetAllParams key="idLot" value="<%=viewBean.getIdLot()%>"/>
		<ct:menuSetAllParams key="idAnnonceParent" value="<%=viewBean.getIdAnnonce()%>"/>
		<ct:menuSetAllParams key="numImmatriculationTransmis" value="<%=viewBean.getNumImmatriculationTransmis()%>"/>
		<ct:menuSetAllParams key="numAvs" value="<%=viewBean.getNumAvs()%>"/>
		<ct:menuSetAllParams key="nom" value="<%=viewBean.getNom()%>"/>
		<ct:menuSetAllParams key="prenom" value="<%=viewBean.getPrenom()%>"/>
	</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>