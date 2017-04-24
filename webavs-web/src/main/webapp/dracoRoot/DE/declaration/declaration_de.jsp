<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@page import="globaz.draco.application.DSApplication"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "CDS0001"; %>
<%@page import="globaz.draco.translation.*"%>
<%@page import="globaz.draco.db.declaration.*"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.draco.util.DSUtil"%>
<!-- Creer l'enregistrement si il n'existe pas -->
<%
	bButtonNew = objSession.hasRight(userActionNew, "ADD");

	globaz.draco.db.declaration.DSDeclarationViewBean viewBean = (globaz.draco.db.declaration.DSDeclarationViewBean)session.getAttribute ("viewBean");
	selectedIdValue = viewBean.getIdDeclaration();
	userActionValue = "draco.declaration.declaration.modifier";
	viewBean.setMasseSalTotalEcran(viewBean.getMasseSalTotal());
	String jspLocation = servletContext + mainServletPath + "Root/tiForDeclaration_select.jsp";
	int autoDigitAff = globaz.draco.util.DSUtil.getAutoDigitAff(session);
	boolean isAdd = ((request.getParameter("_method")!=null)&&(request.getParameter("_method").equals("add")));
	
	String gedFolderType = "";
	String gedServiceName = "";
	try {
		globaz.globall.api.BIApplication osiApp = globaz.globall.api.GlobazSystem.getApplication(DSApplication.DEFAULT_APPLICATION_DRACO);
		gedFolderType = osiApp.getProperty("ged.folder.type", "");
		gedServiceName = osiApp.getProperty("ged.servicename.id", "");
	} catch (Exception e){
		// Le reste de la page doit tout de même fonctionner
	}
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<SCRIPT language="JavaScript">
<!-- // hide this script from non-javascript-enabled browsers

function add() {
	document.forms[0].elements('userAction').value="draco.declaration.declaration.ajouter";
	//document.forms[0].elements('affilieNumero').value='';
//	document.forms[0].elements('affilieDesEcran').value='';
//	document.forms[0].elements('affilieRadieEcran').value='';
//	document.forms[0].elements('typeAffiliationEcran').value='';
}
function upd() {
	document.forms[0].elements('userAction').value="draco.declaration.declaration.modifier";
	document.forms[0].annee.readOnly = "true";
	document.forms[0].annee.disabled = true;
	document.forms[0].typeDeclarationEcran.readOnly = "true";
	document.forms[0].typeDeclarationEcran.disabled = true;
	controlupd();

}
function validate() {
	var exit = true;
	var message = "FEHLER : Alle obligatorische Feldern sind nicht ausgefüllt !";

	if (exit == false)
	{
		alert (message);
		return (exit);
	}
	document.forms[0].elements('userAction').value="draco.declaration.declaration.modifier";
	 if (document.forms[0].elements('_method').value == "add") {
        document.forms[0].elements('userAction').value="draco.declaration.declaration.ajouter";
       }
    else {
        document.forms[0].elements('userAction').value="draco.declaration.declaration.modifier";
    }
	return (exit);
}
function cancel() {
	document.forms[0].elements('userAction').value="draco.declaration.declaration.chercher";
}
function del() {
	if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?"))
	{
		document.forms[0].elements('userAction').value="draco.declaration.declaration.supprimer";
		document.forms[0].submit();
	}
}
function init(){
}

function postInit(){
	control();
}
function updateInfoAffilie(tag) {
	if(tag.select && tag.select.selectedIndex != -1){
 		document.getElementById('descriptionTiers').value = tag.select[tag.select.selectedIndex].nom;
 		document.getElementById('affiliationId').value = tag.select[tag.select.selectedIndex].affiliationId;
 		document.getElementById('affilieDesEcran').value = tag.select[tag.select.selectedIndex].affilieDesEcran;
 		document.getElementById('affilieRadieEcran').value = tag.select[tag.select.selectedIndex].affilieRadieEcran;
 		document.getElementById('typeAffiliationEcran').value = tag.select[tag.select.selectedIndex].typeAffiliationEcran;
 	}
}
function resetInfoAffilie() {
 	document.getElementById('descriptionTiers').value = '';
 	document.getElementById('affiliationId').value = '';
 	document.getElementById('affilieDesEcran').value = '';
 	document.getElementById('affilieRadieEcran').value = '';
 	document.getElementById('typeAffiliationEcran').value = '';
}

function controlupd() {
<% String get="";
	if (viewBean.getTypeDeclaration() != null)
		get = viewBean.getTypeDeclaration();
%>
	var typecs =  <%=globaz.draco.db.declaration.DSDeclarationViewBean.CS_CONTROLE_EMPLOYEUR%>;
	var typedec = '<%=get%>';
	var typeICI =  <%=globaz.draco.db.declaration.DSDeclarationViewBean.CS_ICI%>;
	var typeDividende =  <%=globaz.draco.db.declaration.DSDeclarationViewBean.CS_DIVIDENDE%>;

	if (document.getElementById("typeDeclarationEcran")!= null) {
		if (typedec == typecs) {
			document.getElementById("rapportControl").disabled = false;
			document.getElementById("controlEmployeur").disabled = false;
		} else if (typeICI == typedec) {
			document.getElementById("soumisInteret").disabled = true;
			document.getElementById("soumisInteret").className = "disabled";
			document.forms[0].soumisInteret.readOnly = "true";
		} else if (typeDividende == typedec) {
			document.getElementById("soumisInteret").disabled = true;
			document.getElementById("soumisInteret").className = "disabled";
			document.forms[0].soumisInteret.readOnly = "true";
		}
	}
}

function controlnew() {
	var typeControleEmployeur =  <%=globaz.draco.db.declaration.DSDeclarationViewBean.CS_CONTROLE_EMPLOYEUR%>;
	var typeSalaireDifferes =  <%=globaz.draco.db.declaration.DSDeclarationViewBean.CS_SALAIRE_DIFFERES%>;
	var typeICI =  <%=globaz.draco.db.declaration.DSDeclarationViewBean.CS_ICI%>;
	var typeDividende =  <%=globaz.draco.db.declaration.DSDeclarationViewBean.CS_DIVIDENDE%>;

	if (document.getElementById("typeDeclaration")!= null) {
		document.getElementById("soumisInteret").disabled = false;
		document.getElementById("soumisInteret").className = "enabled";
		if (document.getElementById('typeDeclaration').value == typeControleEmployeur) {
			document.getElementById("rapportControl").disabled = false;
			document.getElementById("controlEmployeur").disabled = false;
			document.getElementById("annee").value = '';
			document.getElementById("annee").className = "disabled";
			document.getElementById("annee").disabled = true;
			document.getElementById("anneeTaux").visibility = false;
			document.getElementById("tdAnneeTaux").style.display = 'none';
			document.getElementById("tdAnneeTaux2").style.display = 'none';
			document.getElementById("tdRapportControle").style.visibility = 'visible';
			document.getElementById("tdRapportControle2").style.visibility = 'visible';
			document.getElementById("tdAnneeTaux").style.visibility = 'hidden';
			document.getElementById("anneeTaux").style.visibility = 'hidden';
		} else if (document.getElementById('typeDeclaration').value == typeSalaireDifferes) {
			document.getElementById("rapportControl").disabled = true;
			document.getElementById("controlEmployeur").disabled = true;
			document.getElementById("annee").disabled = false;
			document.getElementById("annee").className = "enabled";
			document.getElementById("tdAnneeTaux").style.display = 'block';
			document.getElementById("tdAnneeTaux2").style.display = 'block';
			document.getElementById("tdAnneeTaux").style.visibility = 'visible';
			document.getElementById("anneeTaux").style.visibility = 'visible';
			document.getElementById("tdRapportControle").style.visibility = 'hidden';
			document.getElementById("tdRapportControle2").style.visibility = 'hidden';
			document.getElementById("soumisInteret").value='<%=globaz.osiris.db.interets.CAInteretMoratoire.CS_EXEMPTE%>';
		} else if (typeICI == document.getElementById('typeDeclaration').value) {
			document.getElementById("soumisInteret").value='<%=globaz.osiris.db.interets.CAInteretMoratoire.CS_EXEMPTE%>';
			document.getElementById("soumisInteret").disabled = true;
			document.getElementById("soumisInteret").className = "disabled";
			document.getElementById("tdAnneeTaux").style.visibility = 'hidden';
			document.getElementById("anneeTaux").style.visibility = 'hidden';
		} else if (typeDividende == document.getElementById('typeDeclaration').value) {
			document.getElementById("soumisInteret").value='<%=globaz.osiris.db.interets.CAInteretMoratoire.CS_EXEMPTE%>';
			document.getElementById("soumisInteret").disabled = true;
			document.getElementById("soumisInteret").className = "disabled";
			document.getElementById("tdAnneeTaux").style.visibility = 'hidden';
			document.getElementById("anneeTaux").style.visibility = 'hidden';
		} else {
			document.getElementById("rapportControl").disabled = true;
			document.getElementById("controlEmployeur").disabled = true;
			document.getElementById("annee").disabled = false;
			document.getElementById("annee").className = "enabled";
			document.getElementById("tdAnneeTaux").style.display = 'none';
			document.getElementById("tdAnneeTaux2").style.display = 'none';
			document.getElementById("tdRapportControle").style.visibility = 'hidden';
			document.getElementById("tdRapportControle2").style.visibility = 'hidden';
			document.getElementById("tdAnneeTaux").style.visibility = 'hidden';
			document.getElementById("anneeTaux").style.visibility = 'hidden';
		}
	}
}
function updMassePeriode(cell){
	<%if(viewBean.existeCotisationPeriode()){ %>
		if (event.keyCode==61 && cell.value!='') {
			try{
				document.getElementById("massePeriode").value = Math.round(cell.value/4);
			}catch(e){
				//catch vide, évite de faire planter la page, pousser dans un 1-7-1sp1
			}
		}
	<%}%>
}

function control() {
	if (document.getElementById("typeDeclaration")!= null) controlnew();
	if (document.getElementById("typeDeclarationEcran")!= null) {
 		document.getElementById("rapportControl").disabled = true;
		document.getElementById("controlEmployeur").disabled = true;
	 }
}

$(function () {
	<%viewBean.fillWarningMessage();%>
	var warningMessage = "<%=viewBean.getWarningMessage()%>";
	
	if(warningMessage.length > 0) {
		globazNotation.utils.consoleWarn(warningMessage,'<ct:FWLabel key="RELEVE_AVERTISSEMENT" />', true);
	}
});

// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Lohnbescheinigung - Detail <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>

                                <TR>
                                    <td nowrap height="18" rowspan="3" width="128" valign="baseline"><input type="hidden" name="saisieEcran" value="true" tabindex="-1">Mitglied</td>
                                    <td>
                                    	<%if (viewBean.isNew()) {%>
                                    	<ct:FWPopupList name="affilieNumero"
											value="<%=viewBean.getAffilieForEcran()%>"
											validateOnChange="true"
											autoNbrDigit="<%=autoDigitAff%>"
                                    		jspName="<%=jspLocation%>"  minNbrDigit="3"  onChange="updateInfoAffilie(tag);"
                                    		onFailure="resetInfoAffilie()" maxlength="15" size="15"/>
                                    	<%} else {%>
                                    	<input name="affilieNumero"  value="<%=viewBean.getAffiliation().getAffilieNumero()%>" maxlength="15" size="15" class="disabled" readonly="readonly" />
                                    	<%}%>
                                    	<input type="hidden" name="affiliationId" value="<%=viewBean.getAffiliationId()%>">
                                    <td nowrap width="76">Erfasst seit</td>
                                    <td nowrap width="350">
                                    <p><input type="text" name="affilieDesEcran" size="35" maxlength="40" value="<%=viewBean.getAffilieDesEcranFind()%>" class="libelleLongDisabled" tabindex="-1" readonly="readonly"></p>
                                    </td>
                                </TR>
                                <TR>
                                	<td><TEXTAREA id="descriptionTiers" cols="35" class="libelleLongDisabled" tabindex="-1"
										readonly="readonly" rows="4"><%=viewBean.getDescriptionTiers()%></TEXTAREA></td>
                                    <td nowrap width="76">Gestrichen ab</td>
                                    <td nowrap width="350"><input type="text" name="affilieRadieEcran" size="35" maxlength="40" value="<%=viewBean.getAffilieRadieFind()%>" class="libelleLongDisabled" tabindex="-1" readonly="readonly"></td>
                                </TR>
                                <TR>
                                	<td></td>
                                    <td nowrap width="76">Erfassungsart</td>
                                    <td nowrap width="350"><input type="text" name="typeAffiliationEcran" size="35" maxlength="40" value="<%=viewBean.getAffilieTypeFind()%>" class="libelleLongDisabled" tabindex="-1" readonly="readonly"></td>
                                <TR>
                                    <td nowrap colspan="7" height="11">
                                    <hr size="4">
                                    </td>
                                </TR>
                                <TR>
                                    <TD nowrap height="14" width="128">Jahr</TD>
                                    <TD nowrap height="31" width="294">
                                    	<INPUT name="annee" id='annee' size="4" maxlength="4" type="text" onkeypress="return filterCharForPositivInteger(window.event);" value="<%=!JadeStringUtil.isIntegerEmpty(viewBean.getAnnee())?viewBean.getAnnee() : ""%>"></TD>
                                    <TD nowrap height="31" width="76"></TD>
                                    <TD nowrap height="31" width="350"></TD>
                                </TR>
                                <TR>
                                    <TD nowrap height="30" width="128">Empfangsdatum</TD>
                                    <TD nowrap height="31" width="294"><ct:FWCalendarTag name="dateRetourEff" value="<%=globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getDateRetourEff()) && isAdd ?globaz.globall.util.JACalendar.todayJJsMMsAAAA():viewBean.getDateRetourEff()%>"/></TD>
                                    <TD nowrap height="31" width="76"><%if (viewBean.getCodeSuspendu().equals(DSDeclarationViewBean.CS_AUTOMATIQUE)) { %>bis<%}%></TD>
                                    <TD nowrap height="31" width="350"><%if (viewBean.getCodeSuspendu().equals(DSDeclarationViewBean.CS_AUTOMATIQUE)) {%><ct:FWCalendarTag name="dateFinSuspendu" value="<%=viewBean.getDateFinSuspendu()%>"/><%}%></TD>
                                </TR>
                                <TR>
                                    <TD nowrap height="14" width="128">Abrechnungsnummer</TD>
                                    <TD nowrap height="31" width="294"><INPUT name="noDecompte" size="20" maxlength="20" type="text" value="<%=viewBean.getNoDecompte()%>"></TD>
                                    <TD nowrap height="31" width="76"><%if (viewBean.getCodeSuspendu().equals(DSDeclarationViewBean.CS_AUTOMATIQUE)) {%>Erfassungsart<%}%></TD>
                                    <TD nowrap height="31" width="350" rowspan="2" valign="top"><%if (viewBean.getCodeSuspendu().equals(DSDeclarationViewBean.CS_AUTOMATIQUE)) {%><TEXTAREA name="suspendu" cols="35" class="libelleLongDisabled" tabindex="-1" readonly="4" rows="3"><%=viewBean.getMotifSuspendu()%></TEXTAREA><%}%></TD>
                                </TR>
                                <TR>
                                    <TD nowrap height="14" width="128">Lohnsumme AHV</TD>
                                    <TD nowrap height="31" width="294"><INPUT name="masseSalTotal" size="20" maxlength="20" type="text" value="<%=viewBean.getMasseSalTotal()%>" <%if(!"022".equals(DSUtil.getNoCaisse(session))) {%>class="disabled" tabindex="-1" readonly="readonly"<%}%>></TD>
                                    <TD nowrap height="31" width="76"></TD>
                                </TR>
                                <TR>
                                    <TD nowrap height="14" width="128">Lohnsumme ALV I</TD>
                                    <TD nowrap height="31" width="294"><INPUT name="masseACTotal" size="20" maxlength="20" type="text" value="<%=viewBean.getMasseACTotal()%>" <%if(!"022".equals(DSUtil.getNoCaisse(session))) {%>class="disabled" tabindex="-1" readonly="readonly"<%}%>></TD>
                                    <TD nowrap height="31" width="76">Von</TD>
                                    <TD nowrap height="31" width="350"><input type="text" value="<%=viewBean.getLabelPourProvenance(viewBean.getSession(), viewBean.getProvenance())%>" class="disabled" readonly="readonly"/></TD>

                                </TR>
                                <TR>
                                    <TD nowrap height="14" width="128">Lohnsumme ALV II</TD>
                                    <TD nowrap height="31" width="294"><INPUT name="masseAC2Total" size="20" maxlength="20" type="text" value="<%=viewBean.getMasseAC2Total()%>" <%if(!"022".equals(DSUtil.getNoCaisse(session))) {%>class="disabled" tabindex="-1" readonly="readonly"<%}%>></TD>
									<TD nowrap height="31" width="150">Versanddatum</TD>
									<TD nowrap height="31" width="100"><input name="dateEnvoi" id="dateEnvoi" type="text" readonly="readonly" class="disabled" value="<%=viewBean.findDateEnvoi() %>" size="9" tabindex="-1"/></TD>
                                </TR>
                                <TR>
                                    <TD nowrap height="14" width="128">Kontrollbetrag</TD>
                                    <TD nowrap height="31" width="294"><INPUT name="totalControleDS" onkeypress="updMassePeriode(this);return filterCharForFloat(window.event);" size="20" maxlength="20" type="text" value="<%=viewBean.getTotalControleDSFormate()%>"></TD>
                                    <TD nowrap height="31" width="150">Mahnungsdatum</TD>
									<TD nowrap height="31" width="100"><input name="dateSommation" id="dateSommation" type="text" readonly="readonly" class="disabled" value="<%=viewBean.findDateSommation() %>" size="9" tabindex="-1"/></TD>
                                </TR>
 								<%if(viewBean.existeCotisationPeriode()){ %>
	                                 <TR>
	                                    <TD nowrap height="14" width="128">Periodische Lohnsumme</TD>
	                                    <TD nowrap height="31" width="294"><INPUT name="massePeriode" size="20" maxlength="20" type="text" value="<%=viewBean.getMassePeriodeFormate()%>"></TD>
	                                    <TD nowrap height="31" width="76"></TD>
	                                </TR>
                                <%}%>
                                <TR>
                                    <TD nowrap height="14" width="128">Arbeitnehmeranzahl GBBF</TD>
                                    <TD nowrap height="31" width="294"><INPUT name="nbPersonnel" size="20" maxlength="20" type="text" value="<%=viewBean.getNbPersonnel()%>"></TD>
                                    <TD nowrap height="31" width="150">Buchungsdatum</TD>
									<TD nowrap height="31" width="100"><input name="dateCompta" id="dateCompta" type="text" readonly="readonly" class="disabled" value="<%=viewBean.findDateCompta() %>" size="9" tabindex="-1"/></TD>
                                </TR>
                          <% if(viewBean.afficheInteret(viewBean.getSession())) {  %>
                                <TR>
                                    <TD nowrap height="14" width="128">Verzugszinsen</TD>
				            			<%
							     			java.util.HashSet except = new java.util.HashSet();
							            	except.add(globaz.osiris.db.interets.CAInteretMoratoire.CS_MANUEL);
							            	except.add(globaz.osiris.db.interets.CAInteretMoratoire.CS_A_CONTROLER);
							            %>
                                    	<TD nowrap height="31" width="294"><ct:FWCodeSelectTag name="soumisInteret" defaut="<%=viewBean.getSoumisInteret()%>" codeType="OSIIMINMO" except="<%=except%>"/></TD>
                                </TR>
                          <%} else { %>
                                	<input type="hidden" name="soumisInteret" value="<%=globaz.osiris.db.interets.CAInteretMoratoire.CS_AUTOMATIQUE%>"></TD>
                          <%} %>
                                <TR>
                                    <TD nowrap height="14" width="128">Abrechnungstyp</TD>
                                    <%if (viewBean.isNew()) {

										java.util.HashSet except = new java.util.HashSet();
										if(!"022".equals(DSUtil.getNoCaisse(session))) {
											except.add(CodeSystem.CS_BOUCLEMENT_ACCOMPTE);
										}
									%>
                                    <TD nowrap height="31" width="294"><ct:FWCodeSelectTag name="typeDeclaration" defaut="<%=viewBean.getTypeDeclaration()%>" codeType="DRATYPDECL" except="<%=except%>"/></TD>
                                    <TD id="tdAnneeTaux" height="31" width="150">Referenzjahr für den Beitragssatz und Höchstgrenze</TD>
									<TD id="tdAnneeTaux2" height="31" width="100"><INPUT name="anneeTaux" id="anneeTaux" size="4" maxlength="4"  onkeypress="return filterCharForPositivInteger(window.event);" value="<%=viewBean.getAnneeTaux()%>" ></TD>    
                                    <%} else {%>
                                    <TD nowrap height="31" width="76"><INPUT name="typeDeclarationEcran" value="<%=CodeSystem.getLibelle(session,viewBean.getTypeDeclaration())%>" class="libelleLongDisabled" readonly="readonly" /></TD>
                                   		<% if(CodeSystem.CS_SALAIRE_DIFFERES.equals(viewBean.getTypeDeclaration())) { %>
		                                    <TD nowrap height="31" width="150">Referenzjahr für den Beitragssatz und Höchstgrenze</TD>
											<TD nowrap height="31" width="100"><INPUT name="anneeTaux" id="anneeTaux" size="4" maxlength="4" value="<%=viewBean.getAnneeTaux()%>" ></TD>
										<% } else { %>
											<TD nowrap height="31" width="150">&nbsp;</TD>
											<TD nowrap height="31" width="100">&nbsp;</TD>
										<% } %>
                                    <% } %>  
                               </TR>
								<TR>
                                 	<%
	                                	String redirectUrl = ((String)request.getAttribute("mainServletPath")+"Root")+"/"+globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session)+"/declaration/declaration_de.jsp";

		                       			Object[] rapportControlMethods = null;

		                       			if(viewBean.isNouveauControle()) {
		                       				rapportControlMethods = new Object[]{
		        											new String[]{"setForControleEmployeurId","getIdControleEmployeur"},
		        											new String[]{"setForDatePrevue","getDatePrevue"},
		        											new String[]{"setForDateEffective","getDateEffective"}
		        										};

		                       			}else {
		                       				rapportControlMethods = new Object[]{
															new String[]{"setForControleEmployeurId","getControleEmployeurId"},
															new String[]{"setForDatePrevue","getDatePrevue"},
															new String[]{"setForDateEffective","getDateEffective"}
														};
		                       			}

										Object[] paramsControl= new Object[]{
														new String[]{"affilieNumero","_idAff"}
										};

										String date = "";
										if (JadeStringUtil.isBlank(viewBean.getForDateEffective())){
											date = viewBean.getForDatePrevue();
										}else{
											date = viewBean.getForDateEffective();
										}
									%>
                                    <TD id="tdRapportControle" nowrap height="14" width="128">Kontrollrapport</TD>
                                    <TD id="tdRapportControle2" nowrap height="31" width="294"><INPUT type="text" name="controlEmployeur" value="<%=date%>" readonly>
                                    <% if(viewBean.isNouveauControle()) { %>
	                                    <ct:FWSelectorTag name="rapportControl"
											methods="<%=rapportControlMethods%>"
											providerApplication ="hercule"
											providerPrefix="CE"
											providerAction ="hercule.controleEmployeur.listeControleEmployeur.chercher"
											providerActionParams="<%=paramsControl%>"
											redirectUrl="<%=redirectUrl%>"/>
											<input type="hidden" name="_fromDraco" value="true" />
									<% } else { %>
										<ct:FWSelectorTag name="rapportControl"
											methods="<%=rapportControlMethods%>"
											providerApplication ="naos"
											providerPrefix="AF"
											providerAction ="naos.controleEmployeur.controleEmployeur.chercher"
											providerActionParams="<%=paramsControl%>"
											redirectUrl="<%=redirectUrl%>"/>
									<% } %>
									</TD>
										<script type="text/javascript">
										if (document.getElementById("typeDeclaration")!= null){
											document.getElementById("typeDeclaration").onchange = controlnew;
											document.getElementById("typeDeclaration").onclick = controlnew;
										}
										</script>
									<TD nowrap height="31" width="150">Versanddatum des Schreibens AHV-Nummer</TD>
									<TD nowrap height="31" width="100"><input name="dateEnvoiLettre" id="dateEnvoiLettre" type="text" readonly="readonly" class="disabled" value="<%=viewBean.getDateEnvoiLettre()%>" size="9" tabindex="-1"/></TD>
                                </TR>

                                <TR>
                                    <TD nowrap height="14" width="128">Status</TD>
                                    <TD nowrap height="31" width="294"><INPUT type="text" name="etatEcran" value="<%=CodeSystem.getLibelle(session, viewBean.getEtat())%>" class="disabled" readonly="readonly"></TD>
									<TD nowrap height="31" width="150">Versanddatum der Mahnung</TD>
									<TD nowrap height="31" width="100"><input name="dateEnvoiRappel" id="dateEnvoiRappel" type="text" readonly="readonly" class="disabled" value="<%=viewBean.getDateEnvoiRappel()%>" size="9" tabindex="-1"/></TD>
                                </TR>
                                <TR>
                                    <TD nowrap height="14" width="128">&nbsp;</TD>
                                    <TD nowrap height="31" width="294">&nbsp;</TD>
									<TD nowrap height="31" width="150">Anzahl der Mahnungen</TD>
									<TD nowrap height="31" width="100"><input name="nbRappel" id="nbRappel" type="text" readonly="readonly" class="disabled" value="<%=viewBean.getNbRappel()%>" size="9" tabindex="-1"/></TD>
                                </TR>
   								 <%if(DSDeclarationViewBean.CS_AFACTURER.equals(viewBean.getEtat()) || DSDeclarationViewBean.CS_COMPTABILISE.equals(viewBean.getEtat())){ %>
                                	<TR>
                                    <TD nowrap height="14" width="128">Validiert durch</TD>
                                    <TD nowrap height="31" width="294"><INPUT type="text" name="validationSpy" value="<%=viewBean.getValidationSpy()%>" class="disabled" readonly></TD>
                                    <TD nowrap colspan = "2">Den&nbsp;<INPUT type="text" name="validationDateSpy" value="<%=viewBean.getValidationDateSpy()%>" class="disabled" readonly></TD>
                                    </TR>
                                <%} %>
                                <%if(DSDeclarationViewBean.CS_LTN.equals(viewBean.getTypeDeclaration()) && !JadeStringUtil.isBlankOrZero(viewBean.getDateImpressionAttestations())){ %>
                                	<TR>
                                    <TD nowrap height="14" width="128">BGSA Steuerbestätigung von</TD>
                                    <TD nowrap height="31" width="294"><INPUT type="text" name="dateImpressionAttestations" value="<%=viewBean.getDateImpressionAttestations()%>" class="disabled" readonly size="9"></TD>
                                    <TD nowrap height="31" width="76"></TD>
                                    <TD nowrap height="31" width="350"></TD>
                                    </TR>
                                <%} %>
                                <%if(DSDeclarationViewBean.CS_LTN.equals(viewBean.getTypeDeclaration()) && !JadeStringUtil.isBlankOrZero(viewBean.getDateImpressionDecompteImpots())){ %>
                                	<TR>
                                    <TD nowrap height="14" width="128">Steuernabrechnung von</TD>
                                    <TD nowrap height="31" width="100"><INPUT type="text" name="dateImpressionDecompteImpots" value="<%=viewBean.getDateImpressionDecompteImpots()%>" class="disabled" readonly size="9"></TD>
                                    <TD nowrap height="31" width="76"></TD>
                                    <TD nowrap height="31" width="350"></TD>
                                    </TR>
                                <%} %>
                                <tr>
          <TD valign="top"  width="100">
				<%
             		String gedAffilieNumero = viewBean.getAffiliation().getAffilieNumero();
             		String gedNumAvs = viewBean.getAffiliation().getTiers().getNumAvsActuel();
             		String gedIdTiers = viewBean.getIdTiers();
             		String gedIdRole = "";
             	%>
				
				<%@ include file="/theme/gedCall.jspf" %>
			</TD>
			</tr>
          <TR>

                        <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
	<SCRIPT>
	</SCRIPT>
<%	} %>

<ct:menuChange displayId="options" menuId="DS-OptionsDeclaration" showTab="options">

	<% if(DSDeclarationViewBean.CS_COMPTABILISE.equals(viewBean.getEtat())) { %>
		<ct:menuActivateNode active="yes" nodeId="MENU_LETTRE_RECLAMATION"/>
	<% } else { %>	
		<ct:menuActivateNode active="no" nodeId="MENU_LETTRE_RECLAMATION"/>	
	<% } %>

	<ct:menuSetAllParams key="idDeclaration" value="<%=viewBean.getIdDeclaration()%>"/>
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdDeclaration()%>"/>
	<ct:menuSetAllParams key="typeDeclaration" value="<%=viewBean.getTypeDeclaration()%>"/>
	<ct:menuSetAllParams key="numAffilie" value="<%=viewBean.getAffiliation().getAffilieNumero()%>"/>
	<ct:menuSetAllParams key="anneeDS" value="<%=viewBean.getAnnee()%>"/>
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>
