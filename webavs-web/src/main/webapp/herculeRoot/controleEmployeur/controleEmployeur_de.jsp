<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<%
	idEcran ="CCE0004";
	CEControleEmployeurViewBean viewBean = (CEControleEmployeurViewBean)session.getAttribute ("viewBean");
	String method = request.getParameter("_method");
	
	String idAtttributionPts = viewBean._getIdAttributionPts();
	String redirectEvaluation = formAction + "?userAction=hercule.controleEmployeur.gestionAttributionPts.afficher&selectedId="+idAtttributionPts;

	String idGroupe = viewBean._getIdGroupe();
	String redirectGroupe = formAction + "?userAction=hercule.groupement.membre.chercher&idGroupe="+idGroupe;

	String idCompteAnnexe = viewBean._getIdCompteAnnexe();
	String redirectCompteAnnexe = servletContext + "/osiris?userAction=osiris.comptes.apercuComptes.afficher&id=" + idCompteAnnexe;
	
	String redirectAffilie = servletContext + "/naos?userAction=naos.affiliation.autreDossier.modifier&numAffilie=" + viewBean.getNumAffilie();
	
	String actif = "<img id=\"actif\" style=\"display:none\" title=\"Actif\" src=\"" + request.getContextPath()+"/images/ok.gif\" />";
	String inactif = "<img id=\"inactif\" style=\"display:none\" title=\"Inactif\" src=\"" + request.getContextPath()+"/images/verrou.gif\" />";
	
	String gedFolderType = "";
	String gedServiceName = "";
	try {
		globaz.globall.api.BIApplication osiApp = globaz.globall.api.GlobazSystem.getApplication(CEApplication.DEFAULT_APPLICATION_HERCULE);
		gedFolderType = osiApp.getProperty("ged.folder.type", "");
		gedServiceName = osiApp.getProperty("ged.service.name", "");
	} catch (Exception e){
		// Le reste de la page doit tout de même fonctionner
	}
	
	boolean bButtonNewAttribution = true;
	if(JadeStringUtil.isEmpty(viewBean.getDateEffective())) {
		bButtonNewAttribution = false;
	}
	String redirectNewAttribution = servletContext + "/hercule?userAction=hercule.controleEmployeur.gestionAttributionPts.afficher&_method=add&numAffilie=" + viewBean.getNumAffilie() + "&idControle="+viewBean.getIdControleEmployeur()+ "&idAffilie="+viewBean.getAffiliationId();
%>

<%@page import="globaz.hercule.db.controleEmployeur.CEAffilieManager"%>
<%@page import="globaz.hercule.db.reviseur.CEReviseurWidgetManager"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.hercule.db.controleEmployeur.CEControleEmployeurViewBean"%>
<%@page import="globaz.hercule.service.CEAttributionPtsService"%>
<%@page import="globaz.globall.db.BSession"%>
<%@page import="globaz.hercule.application.CEApplication"%>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
top.document.title = "Web@AVS - <ct:FWLabel key='CONTROLE_EMPLOYEUR'/>";

var MAIN_URL = "<%=formAction%>";


function add() {
	document.forms[0].elements('userAction').value="hercule.controleEmployeur.controleEmployeur.ajouter";
}

function upd() {
	document.getElementById("inscriptionRC").disabled = <%=viewBean.isInscrit()%>;
}

function newAttribution() {
	document.location.href='<%=redirectNewAttribution%>';
}

function validate() {
	
	var exit = validateFields();

	document.forms[0].elements('userAction').value="hercule.controleEmployeur.controleEmployeur.modifier";
	
	if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="hercule.controleEmployeur.controleEmployeur.ajouter";
    else
        document.forms[0].elements('userAction').value="hercule.controleEmployeur.controleEmployeur.modifier";
	return (exit);
}

function cancel() {
	
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
 		document.forms[0].elements('userAction').value="hercule.controleEmployeur.controleEmployeur.afficher";
}

function del() {
	if (window.confirm("<%=objSession.getLabel("CONFIRM_SUPPRESSION_OBJECT")%>")) {
		document.forms[0].elements('userAction').value="hercule.controleEmployeur.controleEmployeur.supprimer";
		document.forms[0].submit();
	}
}

function updateVisa(tag){
	if(tag.select && tag.select.selectedIndex != -1) {
		document.getElementById('controleurVisa').value     = tag.select[tag.select.selectedIndex].value;
		document.getElementById('controleurNom').value = tag.select.options[tag.select.selectedIndex].nomReviseur;
		document.getElementById('idReviseur').value = tag.select.options[tag.select.selectedIndex].idReviseur;
	} 
}

function init() {
}

function postInit() {
	<% if (!JadeStringUtil.isEmpty(viewBean.getNumAffilie()) ) {  %>
		$('#widgetNumeroAffilie').val('<%=viewBean.getNumAffilie()%>');
	<%}			
	   if (!JadeStringUtil.isEmpty(viewBean.getControleurVisa()) ) {  %>
    	$('#widgetReviseur').val('<%=viewBean.getControleurVisa()%>');
	<%}%>	
	
	if (document.forms[0].elements('_method').value == "add" && document.forms[0].elements('widgetNumeroAffilie').value == "") {
		$('#btnVal').attr("disabled", true);
	}
	
	$('.dataFixe').css('font-weight','bold');
	$('.dataFixe').css('font-family','Verdana,Arial');

	changeEtat();
	
	$('#btnNewAtt').removeAttr('disabled');
	
	//Affichage du champ code SUVA
	<% if (!JadeStringUtil.isEmpty(viewBean.getTypeReviseur())  && "852003".equals(viewBean.getTypeReviseur()) ) {  %>
		$('#displayCodeSuva').show();
		$('#displayLabelSuva').show();
	<%}	else { %>
		$('#displayCodeSuva').hide();
		$('#displayLabelSuva').hide();
	<%}%>	
	
	document.getElementById("formeJuri").tabIndex="-1";
	document.getElementById("brancheEco").tabIndex="-1";
}

function showPartie1() {
	document.all('tPartie2').style.display='none';
	document.all('tPartie3').style.display='none';
	document.all('tPartie1').style.display='block';
}

function showPartie2() {
	document.all('tPartie1').style.display='none';
	document.all('tPartie3').style.display='none';
	document.all('tPartie2').style.display='block';

}

function showPartie3() {
	document.all('tPartie1').style.display='none';
	document.all('tPartie2').style.display='none';
	document.all('tPartie3').style.display='block';

}

function maxLength(zone,max)
{
	if(zone.value.length>=max){
		zone.value=zone.value.substring(0,max);
	}
}

function changeEtat() {
	if($('#flagDernierRapport').is(':checked')) {
		$("#actif").show();
		$("#inactif").hide();
	}else {
		$("#actif").hide();
		$("#inactif").show();
	}
}

</SCRIPT>
<script type="text/javascript" src="<%=servletContext%>/scripts/widget/globazwidget.js"></script>
<link rel="stylesheet" type="text/css" href="<%=servletContext%>/theme/widget.css"/>
 
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="TITRE_ECRAN_CE_DETAIL"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>

	<TR><TD>&nbsp;</TD></TR>
	<TR>
		<TD nowrap  width="82" height="31">
			<ct:FWLabel key="NUMERO_AFFILIE"/>
			<INPUT type="hidden" name="controleEmployeur" value='saisie'>
			<INPUT type="hidden" name="idTiers" id="idTiers" value='<%=viewBean.getIdTiers()%>'>
			<INPUT type="hidden" name="selectedId" value='<%=viewBean.getIdControleEmployeur()%>'>
		</TD>
		<TD>	
            <ct:widget name="widgetNumeroAffilie" id="widgetNumeroAffilie"  styleClass="numeroCourt">
				<ct:widgetManager managerClassName="<%=CEAffilieManager.class.getName()%>" defaultSearchSize="5">
					<ct:widgetCriteria criteria="likeNumeroAffilie" label="AFFILIE"/>								
					<ct:widgetLineFormatter format="#{numAffilie}  #{nom}  #{dateDebutAffiliation} - #{dateFinAffiliation} (#{typeAffiliationLabel})"/>
					<ct:widgetJSReturnFunction>
						<script type="text/javascript">
							function(element){			
								$('#widgetNumeroAffilie').val($(element).attr('numAffilie'));
								$('#numAffilie').val($(element).attr('numAffilie'));
								$('#affiliationId').val($(element).attr('idAffiliation'));
								$('#idTiers').val($(element).attr('idTiers'));
								$('#infoTiers').val($(element).attr('nom') + '\n' + $(element).attr('dateDebutAffiliation') + ' - ' + $(element).attr('dateFinAffiliation'));	 
								$('#btnVal').attr('disabled', false);
								$('#codeSUVA').val($(element).attr('codeSUVA'));
							}
						</script>										
					</ct:widgetJSReturnFunction>
				</ct:widgetManager>
			</ct:widget>			
			<ct:inputHidden name="affiliationId" id="affiliationId"/>
			<ct:inputHidden name="numAffilie" id="numAffilie"/>	     
			&nbsp;&nbsp;
			<% if(!JadeStringUtil.isEmpty(viewBean.getNumAffilie())) { %>
				<A href="<%=redirectAffilie%>" class="external_link"><ct:FWLabel key="AFFILIE"/></A>&nbsp;&nbsp;
			<% }%>                                  
			<% if(!JadeStringUtil.isEmpty(idCompteAnnexe)) { %>
				/&nbsp;&nbsp;<A href="<%=redirectCompteAnnexe%>" class="external_link"><ct:FWLabel key="COMPTE"/></A>
			<% }%>
			
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<%
             		String gedAffilieNumero = viewBean.getNumAffilie();
             		String gedNumAvs = "";
             		String gedIdTiers = viewBean.getIdTiers();
             		String gedIdRole = "";
            %>	
            <% if(!JadeStringUtil.isEmpty(viewBean.getNumAffilie())) { %>
				<%@ include file="/theme/gedCall.jspf" %>
			<% }%> 
		</TD>						
	</TR>
	<TR>
		<TD nowrap width="82" height="31">
			<% if(!JadeStringUtil.isEmpty(idAtttributionPts)) { %>
				<A href="<%=redirectEvaluation%>"><ct:FWLabel key="EVALUATION"/></A><BR/><BR/>
			<% } else if (bButtonNewAttribution) { %>
				<ct:ifhasright element="hercule.controleEmployeur.gestionAttributionPts.afficher" crud="cud">
					<input class="btnCtrl" id="btnNewAtt" type="button" value="<ct:FWLabel key="CE_NEW_EVALUATION"/>" onclick="newAttribution();">
				</ct:ifhasright>
			<% } %>
			<% if(!JadeStringUtil.isEmpty(idGroupe)) { %>
				<A href="<%=redirectGroupe%>"><ct:FWLabel key="GROUPE"/></A>
			<% }%>
			&nbsp;
		</TD>
		<TD><TEXTAREA name="infoTiers" id="infoTiers" cols="45"  rows="3" style="overflow:auto; background-color:#b3c4db;"   readonly="readonly"><%=viewBean.getInfoTiers()%></TEXTAREA></TD>
	</TR>
	<TR><TD>&nbsp;</TD></TR>
	<TR> 
		<TD nowrap  height="11" colspan="2"><hr size="3"></TD>
	</TR>
	<tr>
		<td colspan="2">
				<TABLE border="0" cellspacing="0" cellpadding="0" id="tPartie1">
					<TBODY>
						<TR>
							<TD nowrap height="31"><ct:FWLabel key="DATE_PREVUE"/></TD>
							<TD nowrap colspan="2"><ct:FWCalendarTag name="datePrevue" doClientValidation="CALENDAR,NOT_EMPTY" value="<%=viewBean.getDatePrevue()%>" /> </TD>
							<TD>&nbsp;&nbsp;</TD>
							<TD nowrap><ct:FWLabel key="NUMERO_RAPPORT"/></TD>
							<TD nowrap><input name="nouveauNumRapport" type="text" size="20" maxlength="12" value="<%=viewBean.getNouveauNumRapport()%>">
							</TD>
						</TR>	
						<TR>
							<TD nowrap height="31"><ct:FWLabel key="GENRE_CONTROLE"/></TD>
							
							<TD nowrap colspan="2">
								<ct:FWCodeSelectTag 
									name="genreControle"
									defaut="<%=viewBean.getGenreControle()%>"
									codeType="VEGENRECON"
									wantBlank="true" 
									doClientValidation="NOT_EMPTY"/> 
							</TD>
							<TD>&nbsp;&nbsp;</TD>
							<TD nowrap><ct:FWLabel key="REVISEUR"/></TD>
							
							<TD nowrap> 
								<ct:widget name="widgetReviseur" id="widgetReviseur" styleClass="numeroCourt">
									<ct:widgetManager managerClassName="<%=CEReviseurWidgetManager.class.getName()%>" defaultSearchSize="5" defaultLaunchSize="1">
										<ct:widgetCriteria criteria="likeVisa" label="REVISEUR" />
										<ct:widgetLineFormatter format="#{visa} - #{nomReviseur} - #{typeReviseur}" />
										<ct:widgetJSReturnFunction>
											<script type="text/javascript">
												function(element){			
													$('#widgetReviseur').val($(element).attr('visa'));
													$('#idReviseur').val($(element).attr('idReviseur'));
													$('#controleurNom').val($(element).attr('nomReviseur'));
													$('#typeReviseur').val($(element).attr('typeReviseur'));
													
													if($(element).attr('typeReviseur') == '852003') {
														$('#displayCodeSuva').show();
														$('#displayLabelSuva').show();
													}else {														
														$('#displayCodeSuva').hide();
														$('#displayLabelSuva').hide();
													}
												}
											</script>
										</ct:widgetJSReturnFunction>
									</ct:widgetManager>
								</ct:widget> 
								<ct:inputHidden name="idReviseur" id="idReviseur" />
								<ct:inputHidden name="typeReviseur" id="typeReviseur" />
								<INPUT name="controleurNom" id="controleurNom" type="text" readonly="readonly" tabindex="-1" class="libelleLongDisabled" value="<%=viewBean.getControleurNom()%>">
							</TD>
						</TR>
						<TR>
							<TD nowrap height="31"><ct:FWLabel key="DATE_EFFECTIVE"/></TD>
							
							<TD nowrap colspan="2"><ct:FWCalendarTag name="dateEffective" doClientValidation="CALENDAR" value="<%=viewBean.getDateEffective()%>" /></TD>
							<TD>&nbsp;&nbsp;</TD>
							<TD nowrap><ct:FWLabel key="CE_RAPPORT_AVEC_DIFFERENCE"/></TD>
							
							<TD nowrap><input type="checkbox" name="erreur" <%=(viewBean.isErreur().booleanValue())? "checked" : ""%> ></TD>
						</TR>
						<TR>
							<TD nowrap height="31"><ct:FWLabel key="CE_FROM_DATE_A_CONTROLER"/></TD>
							<TD nowrap colspan="2">
								<ct:FWCalendarTag name="dateDebutControle" doClientValidation="CALENDAR,NOT_EMPTY" value='<%=(viewBean.getDateDebutControle()== null)? "" :viewBean.getDateDebutControle()%>' /> 
								<ct:FWLabel key="CE_TO_DATE_A_CONTROLER"/>&nbsp;
								<ct:FWCalendarTag name="dateFinControle" doClientValidation="CALENDAR,NOT_EMPTY" value='<%=(viewBean.getDateFinControle()== null)? "" :viewBean.getDateFinControle()%>' /> 
							</TD>
							<TD>&nbsp;&nbsp;</TD>
							<TD nowrap><ct:FWLabel key="MOTIF"/></TD>
							
							<TD nowrap>
								<ct:FWCodeSelectTag 
									name="debitCredit"
									defaut="<%=viewBean.getDebitCredit()%>"
									codeType="VEDEBITCRE"
									wantBlank="true"/> 
							</TD>
						</TR>
						<TR>
							<TD nowrap height="31" ><ct:FWLabel key="CE_NUMERO_RAPPORT_INTERNE"/>&nbsp;</TD>
							<TD nowrap colspan="2"><input name="rapportNumero" type="text" size="20" value="<%=viewBean.getRapportNumero()%>"></TD>
							<TD>&nbsp;&nbsp;</TD>
							<TD nowrap><ct:FWLabel key="CE_TEMPS_CONTROLE"/></TD>
							<TD nowrap><input name="tempsJour" type="text" size="10" value="<%=viewBean.getTempsJour()%>"><ct:FWLabel key="EN_JOUR"/></TD>
						</TR>
						<TR>
							<TD nowrap height="31" ><ct:FWLabel key="CE_RAPPORT_ACTIF"/></TD>
							<TD nowrap colspan="2"> 
								<input type="checkbox" name="flagDernierRapport" id="flagDernierRapport" onclick="javascript:changeEtat();" <%=(viewBean.getFlagDernierRapport().booleanValue())? "checked=\"checked\"" : ""%> >
								&nbsp;&nbsp;&nbsp;
								<%=actif%><%=inactif%>
							</TD>
							<TD>&nbsp;&nbsp;</TD>
							<TD nowrap><ct:FWLabel key="CE_NUMERO_AFFILIATION_EXTERNE"/></TD>
							<TD nowrap><INPUT name="numeroExterne" type="text" size="20" maxlength="15" value="<%=viewBean.getNumeroExterne()%>"></TD>
						</TR>
						<TR>
							<TD nowrap height="31"><ct:FWLabel key="DATE_IMPRESSION"/></TD>
							
							<TD nowrap colspan="2"><ct:FWCalendarTag name="dateImpression" value="<%=viewBean.getDateImpression()%>" /></TD>
							<TD>&nbsp;&nbsp;</TD>
							<TD id="displayLabelSuva" nowrap><ct:FWLabel key="HERCULE_JSP_CCE0004_CODE_SUVA"/></TD>
							<TD id="displayCodeSuva" nowrap>
								<ct:FWCodeSelectTag 
									name="codeSuva"
									defaut="<%=viewBean.getCodeSuva()%>"
									codeType="VECODESUVA"
									wantBlank="true"
									libelle="" /> 
							</TD>
						</TR>
						<TR> 
							<TD nowrap  height="11" colspan="6">
								<hr size="3" width="100%">
							</TD>
						</TR>
						<TR> 
							<TD nowrap height="31" ><ct:FWLabel key="CE_INSCRIPTION_RC"/></TD>
							<TD nowrap colspan="2">
								<input type="checkbox" disabled="disabled" id="inscriptionRC" name="inscriptionRC" <%=(viewBean.getInscriptionRCParticularite().booleanValue())? "checked" : ""%> >
							</TD>
							<TD>&nbsp;&nbsp;</TD>
							<TD nowrap><ct:FWLabel key="CE_NOMBRE_SALARIE"/></TD>
							<TD nowrap><INPUT name="nombreSalariesFixes" type="text" size="10" value="<%=viewBean.getNombreSalariesFixes()%>"></TD>							
						</TR>
						<TR> 
							<TD nowrap height="31" ><ct:FWLabel key="CE_DATE_BOUCLEMENT"/></TD>
							<TD nowrap colspan="2"><ct:FWCalendarTag name="dateBouclement" value="<%=viewBean.getDateBouclement()%>" /></TD>
							<TD>&nbsp;&nbsp;</TD>
							<TD nowrap><ct:FWLabel key="CE_COMPTABILITE_TENUE_PAR"/></TD>
							<TD nowrap><INPUT name="comptaTenuPar" type="text" size="35" value="<%=viewBean.getComptaTenuPar()%>"></TD>							
						</TR>
						<TR>
							<TD nowrap height="31" ><ct:FWLabel key="CE_PERSONNE_CONTACT"/></TD>
							<TD nowrap colspan="2"><INPUT name="personneContact1" type="text" size="20" value="<%=viewBean.getPersonneContact1()%>" class="libelleLong"></TD>
							<TD>&nbsp;&nbsp;</TD>
							<TD nowrap><ct:FWLabel key="CE_BRANCHE_ECONOMIQUE"/></TD>
							<TD nowrap><span class='dataFixe'><%=viewBean._getBrancheEcoLibelle()%></span></TD>
						</TR>
						<TR>
							<TD nowrap height="31" ></TD>
							<TD nowrap colspan="2"> 
								<INPUT name="personneContact2" type="text" size="20" value="<%=viewBean.getPersonneContact2()%>" class="libelleLong">
							</TD>
							<TD>&nbsp;&nbsp;</TD>
							<TD nowrap><ct:FWLabel key="CE_FORME_JURIDIQUE"/></TD>
							<TD nowrap><span class='dataFixe'><%=viewBean._getFormeJuriLibelle()%></span></TD>
						</TR>
						<TR>
							<TD nowrap height="31" ></TD>
							<TD nowrap colspan="2"> 
								<INPUT name="personneContact3" type="text" size="20" value="<%=viewBean.getPersonneContact3()%>" class="libelleLong">
							</TD>
							<TD>&nbsp;&nbsp;</TD>
						</TR>				
						<TR> 
							<TD nowrap height="31"></TD>
							<TD nowrap colspan="4">
								<B><A href="javascript:showPartie1()"><ct:FWLabel key="PAGE_1"/></A></B> 
								-- 
								<A href="javascript:showPartie2()"><ct:FWLabel key="PAGE_2"/></A>
								--								
								<A href="javascript:showPartie3()"><ct:FWLabel key="PAGE_3"/></A>
							</TD>
						</TR>
					</TBODY> 
				</TABLE>
				<TABLE border="0" cellspacing="0" cellpadding="0" id="tPartie2" style="display:none">
					<TBODY>
						<TR><TD colspan="3"><H4><ct:FWLabel key="CE_DOCUMENT_CONTROLE_CHEZ_AFFILIE"/></H4></TD></TR>
						<TR>
							<TD nowrap colspan="2">
								<B><ct:FWLabel key="CE_COMPTABILITE_SALAIRE"/></B>
							</TD>							
						</TR>
						<TR>
							<TD nowrap width="161" height="31" ><ct:FWLabel key="COMPLET"/></TD>
							<TD nowrap colspan="1"> 
								<INPUT name="docComptaComplet" type="text" size="35" value="<%=viewBean.getDocComptaComplet()%>">
							</TD>
							<td style="width: 5em"></td>
							<TD nowrap><ct:FWLabel key="SONDAGE"/></TD>
							<TD nowrap>
								<INPUT name="docComptaSondage" type="text" size="35" value="<%=viewBean.getDocComptaSondage()%>">
							</TD>
							
						</TR>
						<TR>
							<TD nowrap colspan="2">
								<B><ct:FWLabel key="CE_BILAN"/></B>
							</TD>							
						</TR>
						<TR>
							<TD nowrap width="161" height="31" ><ct:FWLabel key="COMPLET"/></TD>
							
							<TD nowrap colspan="1">
								<INPUT name="docBilanComplet" type="text" size="35" value="<%=viewBean.getDocBilanComplet()%>">
							</TD>
							<td style="width: 5em"></td>
							<TD nowrap><ct:FWLabel key="SONDAGE"/></TD>
							<TD nowrap> 
								<INPUT name="docBilanSondage" type="text" size="35" value="<%=viewBean.getDocBilanSondage()%>">
							</TD>
						</TR>
						<TR></TR>
						<TR> 
							<TD nowrap  height="11" colspan="6">
								<hr size="3" width="100%">
							</TD>
						</TR>
						<TR>
							<TD colspan="3"><H4><ct:FWLabel key="CE_ELEMENT_DETERMINANT_SALAIRE"/></H4></TD>
						</TR>
						<TR>
							<TD nowrap width="161" height="31" >
								<input type="checkbox" name="elePourboire" <%=(viewBean.getElePourboire().booleanValue())? "checked" : ""%> >
								<ct:FWLabel key="POURBOIRE"/>
							</TD>
							<TD nowrap colspan="2"> 
								<input type="checkbox" name="eleHonoraire" <%=(viewBean.getEleHonoraire().booleanValue())? "checked" : ""%> >
								<ct:FWLabel key="HONORAIRE_ADMINISTRATEUR"/>
							</TD>
							<TD nowrap> 
								<input type="checkbox" name="eleMensuel" <%=(viewBean.getEleMensuel().booleanValue())? "checked" : ""%> >
								<ct:FWLabel key="MENSUEL"/>
							</TD>
							<TD nowrap> 
								<input type="checkbox" name="eleHeure" <%=(viewBean.getEleHeure().booleanValue())? "checked" : ""%> >
								<ct:FWLabel key="HEURE"/>
							</TD>
						</TR>
						<TR>
							<TD nowrap width="161" height="31" > 
								<input type="checkbox" name="eleNature" <%=(viewBean.getEleNature().booleanValue())? "checked" : ""%> >
								<ct:FWLabel key="EN_NATURE"/>
							</TD>
							<TD nowrap colspan="2"> 
								<input type="checkbox" name="eleIndemnite" <%=(viewBean.getEleIndemnite().booleanValue())? "checked" : ""%> >
								<ct:FWLabel key="INDEMNITE_VACANCE"/>
							</TD>
							<TD nowrap> 
								<input type="checkbox" name="elePiece" <%=(viewBean.getElePiece().booleanValue())? "checked" : ""%> >
								<ct:FWLabel key="PIECE"/>
							</TD>
							<TD nowrap> 
								<input type="checkbox" name="eleDomicile" <%=(viewBean.getEleDomicile().booleanValue())? "checked" : ""%> >
								<ct:FWLabel key="DOMICILE"/>
							</TD>
						</TR>
						<TR>
							<TD nowrap width="161" height="31" > 
								<input type="checkbox" name="eleAutre1" <%=(viewBean.getEleAutre1().booleanValue())? "checked" : ""%> > 
								<input name="eleLibelleAutre1" type="text" size="15" value="<%=viewBean.getEleLibelleAutre1()%>">
							</TD>
							<TD nowrap colspan="2"> 
								<input type="checkbox" name="eleAutre2" <%=(viewBean.getEleAutre2().booleanValue())? "checked" : ""%> >
								<input name="eleLibelleAutre2" type="text" size="15" value="<%=viewBean.getEleLibelleAutre2()%>">
							</TD>
							<TD nowrap> 
								<input type="checkbox" name="eleCommission" <%=(viewBean.getEleCommission().booleanValue())? "checked" : ""%> >
								<ct:FWLabel key="COMMISSION"/>
							</TD>
							<TD nowrap> 
								<input type="checkbox" name="eleGratification" <%=(viewBean.getEleGratification().booleanValue())? "checked" : ""%> >
								<ct:FWLabel key="GRATIFICATION"/>
							</TD>
						</TR>
						<TR></TR>
						<TR> 
							<TD nowrap  height="11" colspan="6">
								<hr size="3" width="100%">
							</TD>
						</TR>
						<TR></TR>
						<TR> 
		                  <TD><B><ct:FWLabel key="CONSTATE"/></B></TD>
		                  <TD nowrap colspan="4"> 
		                    <TEXTAREA name="champConstate" rows="5" cols="100" width="800" onkeypress="maxLength(this, 1024);"><%=viewBean.getChampConstate()%></TEXTAREA>
		                  </TD>
		                </TR>						
						<TR> 
							<TD nowrap width="161" height="31"></TD>
							<TD nowrap colspan="4">
								<A href="javascript:showPartie1()"><ct:FWLabel key="PAGE_1"/></A>
								-- 
								<B><A href="javascript:showPartie2()"><ct:FWLabel key="PAGE_2"/></A></B>
								--								
								<A href="javascript:showPartie3()"><ct:FWLabel key="PAGE_3"/></A>
							</TD>
						</TR>	
					</TBODY> 
				</TABLE>
				<TABLE border="0" cellspacing="0" cellpadding="0" id="tPartie3" style="display:none">
					<TBODY>
						<TR>
							<TD colspan="3"><H4><ct:FWLabel key="CE_AUTRE_CONTROLE"/></H4></TD>
						</TR>
						<TR>
							<TD nowrap colspan="2">
								<B><ct:FWLabel key="CE_ALLOCATION_COMPLEMENTAIRE"/></B>
							</TD>
						</TR>
						<TR>
							<TD nowrap width="161" height="31" ><ct:FWLabel key="COMPLET"/></TD>
							<TD nowrap colspan="2"> 
								<INPUT name="docAllocMiliComplet" type="text" size="35" value="<%=viewBean.getDocAllocMiliComplet()%>">
							</TD>
							<td style="width: 5em"></td>
							<TD nowrap><ct:FWLabel key="SONDAGE"/></TD>
							<TD nowrap> 
								<INPUT name="docAllocMiliSondage" type="text" size="35" value="<%=viewBean.getDocAllocMiliSondage()%>">
							</TD>
						</TR>
						<TR>
							<TD nowrap colspan="2">
								<B><ct:FWLabel key="CE_DROIT_AF"/></B>
							</TD>
						</TR>
						<TR>
							<TD nowrap width="161" height="31" ><ct:FWLabel key="COMPLET"/></TD>
							<TD nowrap colspan="2"> 
								<INPUT name="docDroitAllocComplet" type="text" size="35" value="<%=viewBean.getDocDroitAllocComplet()%>">
							</TD>
							<td style="width: 5em"></td>
							<TD nowrap><ct:FWLabel key="SONDAGE"/></TD>
							<TD nowrap> 
								<INPUT name="docDroitAllocSondage" type="text" size="35" value="<%=viewBean.getDocDroitAllocSondage()%>">
							</TD>
						</TR>
						<TR><TD nowrap width="161" height="31" ></TD></TR>
						<TR>
							<TD nowrap colspan="2" >
								<B><ct:FWLabel key="CE_ALLOCATION_APG"/></B>
							</TD>
						</TR>
						<TR>
							<TD nowrap width="161" height="31" ><ct:FWLabel key="COMPLET"/></TD>
							<TD nowrap colspan="2"> 
								<INPUT name="docAllocPerteComplet" type="text" size="35" value="<%=viewBean.getDocAllocPerteComplet()%>">
							</TD>
							<td style="width: 5em"></td>
							<TD nowrap><ct:FWLabel key="SONDAGE"/></TD>
							<TD nowrap>
								<INPUT name="docAllocPerteSondage" type="text" size="35" value="<%=viewBean.getDocAllocPerteSondage()%>">
							</TD>
						</TR>
						<TR>
							<TD nowrap width="161" height="31" ><ct:FWLabel key="CE_RAPPORT_AF_SEPARE"/></TD>
							<TD nowrap colspan="2"> 
								<input type="checkbox" name="rapportAFSepare" <%=(viewBean.getRapportAFSepare().booleanValue())? "checked" : ""%> >
							</TD>
							
						</TR>
						<TR></TR>
						<TR> 
							<TD nowrap  height="11" colspan="6">
								<hr size="3" width="100%">
							</TD>
						</TR>
						<TR></TR>
		                <TR> 
		                  <TD><B><ct:FWLabel key="CE_CONSEIL_PRODIGUE"/></B></TD>
		                  <TD nowrap colspan="5"> 
		                    <TEXTAREA name="champConseil" rows="5" cols="100" width="800" onkeypress="maxLength(this, 1024);"><%=viewBean.getChampConseil()%></TEXTAREA>
		                  </TD>
		                </TR>
		                <TR> 
		                  <TD><B><ct:FWLabel key="REMARQUE"/></B></TD>
		                  <TD nowrap colspan="5"> 
		                    <TEXTAREA name="champRemarque" rows="5" cols="100" width="800" onkeypress="maxLength(this, 1024);"><%=viewBean.getChampRemarque()%></TEXTAREA>
		                  </TD>
		                </TR>
						<TR> 
							<TD width="100" height="31"></TD>
							<TD colspan="4">
								<A href="javascript:showPartie1()"><ct:FWLabel key="PAGE_1"/></A>
								-- 
								<A href="javascript:showPartie2()"><ct:FWLabel key="PAGE_2"/></A>
								--								
								<B><A href="javascript:showPartie3()"><ct:FWLabel key="PAGE_3"/></A></B>
							</TD>
						</TR>
					</TBODY> 
				</TABLE>
			</td>
		</tr>
				
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>

	<ct:menuChange displayId="menu" menuId="CE-MenuPrincipal"/>
	<ct:menuChange displayId="options" menuId="CE-OptionsControlEmployeur" showTab="options">
		<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdControleEmployeur()%>"/>
		<ct:menuSetAllParams key="idTiers" value="<%=viewBean.getIdTiers()%>"/>
		<ct:menuSetAllParams key="dateDebutControle" value="<%=viewBean.getDateDebutControle()%>"/>
		<ct:menuSetAllParams key="dateFinControle" value="<%=viewBean.getDateFinControle()%>"/>
		<ct:menuSetAllParams key="dateEffective" value="<%=viewBean.getDateEffective()%>"/>
		<ct:menuSetAllParams key="visaReviseur" value="<%=viewBean.getControleurVisa()%>"/>
		<ct:menuSetAllParams key="numAffilie" value="<%=viewBean.getNumAffilie()%>"/>
		<ct:menuSetAllParams key="idAffiliation" value="<%=viewBean.getAffiliationId()%>"/>
	</ct:menuChange>
	
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>