<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.globall.db.BSessionUtil"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<!-- Creer l'enregistrement si il n'existe pas -->
<%@ taglib uri="/WEB-INF/naos.tld" prefix="naos" %>
<%
	idEcran = "CAF0010";
	globaz.naos.db.cotisation.AFCotisationViewBean viewBean = (globaz.naos.db.cotisation.AFCotisationViewBean)session.getAttribute ("viewBean");
	String method = request.getParameter("_method");
%>
<%@page import="globaz.naos.db.tauxAssurance.AFTauxAssurance"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<SCRIPT language="JavaScript">
</SCRIPT>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<SCRIPT language="JavaScript">

function add() {
	if ("<%=request.getParameter("_valid")%>" != "fail") {
		document.forms[0].elements('dateDebut').value   = "<%=viewBean.getAffiliation().getDateDebut()%>";	
		document.forms[0].elements('dateFin').value     = "<%=viewBean.getAffiliation().getDateFin()%>";	
		document.forms[0].elements('motifFin').value    = "<%=viewBean.getAffiliation().getMotifFin()%>";
		document.forms[0].elements('periodicite').value = "<%=viewBean.getAffiliation().getPeriodicite()%>";	
	}
	document.forms[0].elements('userAction').value  = "naos.cotisation.cotisation.ajouter";
}

function upd() {
	showAddUpdate();
}

function validate() {
	var exit = true;
	/*var message = "FEHLER : Alle obligatorische Feldern sind nicht ausgefüllt !";
	if (document.forms[0].elements('dateDebut').value == "")
	{
		message = message + "\nSie haben das Beginndatum nicht eingegeben !";
		exit = false;
	}
	
	if (exit == false)
	{
		alert (message);
		return (exit);
	}*/
	document.forms[0].elements('userAction').value="naos.cotisation.cotisation.modifier";
	
	if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="naos.cotisation.cotisation.ajouter";
    else
        document.forms[0].elements('userAction').value="naos.cotisation.cotisation.modifier";
	return (exit);
}

function cancel() {
	if (document.forms[0].elements('_method').value == "add")
	 	document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="naos.cotisation.cotisation.afficher";
}

function del() {
	if (window.confirm("Sie sind dabei, der ausgewählte Beitrag zu löschen! Wollen Sie fortfahren?")) {
		document.forms[0].elements('userAction').value="naos.cotisation.cotisation.supprimer";
		document.forms[0].submit();
	}
}

function init() {
	var methodElement = document.forms[0].elements('_method');
	var validElement = document.forms[0].elements('_valid');

	if (methodElement.value == ADD || 
	    validElement.value == "fail") {
		showAddUpdate();
	} else {
		showDisplay();
	}
	if (<%=viewBean.getExceptionPeriodicite()!=null?viewBean.getExceptionPeriodicite().booleanValue():false%>) {
		showPeriodiciteCotisation();
	} else {
		showPeriodiciteAffiliation();
	}
	
	showMoisAnneeFact();
}

function showAddUpdate() {
	document.all('displayPar').style.display='none';
	document.all('displayPersTypeManuel').style.display='none';
	document.all('displayPeriode').style.display='none';
	document.all('addUpdatePeriode').style.display='block';
	if(<%=viewBean.getAssurance()==null%>) {
		document.all('displayPers').style.display='none';
		document.all('addUpdatePar').style.display='none';
		document.all('displayPersTypeManuel').style.display='none';
	} else {
		if(<%=globaz.naos.translation.CodeSystem.GENRE_ASS_PARITAIRE.equals(viewBean.getAssurance()!=null?viewBean.getAssurance().getAssuranceGenre():"")%>) {
			document.all('addUpdatePar').style.display='block';
			document.all('displayPers').style.display='none';
			document.all('displayPersTypeManuel').style.display='none';
		}else {
		if(<%="812021".equals(viewBean.getAssurance()!=null?viewBean.getAssurance().getTypeAssurance():"")%>) {
			document.all('addUpdatePar').style.display='none';
			document.all('displayPersTypeManuel').style.display='block';
			document.all('displayPers').style.display='none';
		}else {
			document.all('addUpdatePar').style.display='none';
			document.all('displayPers').style.display='block';
			document.all('displayPersTypeManuel').style.display='none';
		}
	}
}
}

function showDisplay() {
	document.all('addUpdatePar').style.display='none';
	document.all('displayPeriode').style.display='block';
	document.all('displayPersTypeManuel').style.display='none';
	document.all('addUpdatePeriode').style.display='none';
	if(<%=globaz.naos.translation.CodeSystem.GENRE_ASS_PARITAIRE.equals(viewBean.getAssurance()!=null?viewBean.getAssurance().getAssuranceGenre():"")%>) {
		document.all('displayPar').style.display='block';
		document.all('displayPers').style.display='none';
		document.all('displayPersTypeManuel').style.display='none';
	} else {
	if(<%="812021".equals(viewBean.getAssurance()!=null?viewBean.getAssurance().getTypeAssurance():"")%>) {
			document.all('addUpdatePar').style.display='none';
			document.all('displayPersTypeManuel').style.display='block';
			document.all('displayPers').style.display='none';
		}else {
		document.all('displayPar').style.display='none';
		document.all('displayPers').style.display='block';
		document.all('displayPersTypeManuel').style.display='none';
	
	}
}
}
function onChangeException() {
	var valueException = document.forms[0].elements('exceptionPeriodicite');
	if (valueException.checked == true) {
		showPeriodiciteCotisation();
	} else {
		showPeriodiciteAffiliation();	
	}
}

function showPeriodiciteCotisation() {
	document.all('periodiciteAffiliationDisplay').style.display='none';
	document.all('periodiciteCotisationDisplay').style.display='block';
	document.all('periodiciteAffiliationDisplayRO').style.display='none';
	document.all('periodiciteCotisationDisplayRO').style.display='block';
	showMoisAnneeFact();
}

function showPeriodiciteAffiliation() {
	document.all('periodiciteCotisationDisplay').style.display='none';
	document.all('periodiciteAffiliationDisplay').style.display='block';
	document.all('periodiciteCotisationDisplayRO').style.display='none';
	document.all('periodiciteAffiliationDisplayRO').style.display='block';	
}

function showMoisAnneeFact(){
// Met les champs pays inactif sauf si la période est de type assurance 
// Met les champs détenteur inactif sauf si la période est de garde BTE
	//var moisAnneeFact = document.forms[0].elements('traitementMoisAnnee');
	var moisAnneeFact = document.all("moisPeriodicite");
	var moisAnneeFactRO = document.all("moisPeriodiciteRO");
	// Si la periodicite de la cotisation est affichée et que la periodicité est annuelle
	if (document.all('periodiciteAffiliationDisplay').style.display == 'none' 
			&& document.forms[0].elements('periodicite').value == "<%=globaz.naos.translation.CodeSystem.PERIODICITE_ANNUELLE%>") {
		moisAnneeFact.style.display = 'inline';
		moisAnneeFactRO.style.display = 'inline';
	} else {
		moisAnneeFact.style.display = 'none';
		moisAnneeFactRO.style.display = 'none';
	}
/*	if (typePeriode.value == "<%--=globaz.hera.api.ISFSituationFamiliale.CS_TYPE_PERIODE_GARDE_BTE--%>") {
		document.forms[0].elements('idDetenteurBTE').style.visibility = 'visible';
		document.forms[0].elements('idDetenteurBTE').style.display='inline';
		document.getElementById('labelDetenteur').style.visibility = 'visible';
		document.getElementById('labelDetenteur').style.display='inline';
	} 
*/	
}

var enterValidate = false;
function reloadPage() {
	if(!enterValidate) {
		// seulement si pas presser valider
		fieldFormat(document.all('dateDebut'),'CALENDAR');
		document.forms[0].elements('userAction').value = 'naos.cotisation.cotisation.reload';
		document.forms[0].submit();
	}
}

function checkMasseLimit(masse) {
	//alert("check "+masse.value.replace('\'',''));
	mnt = masse.value;
	while(mnt.indexOf('\'')!=-1) {
		mnt = mnt.replace('\'','');
	}
	// calculer le montant annuel
	if(document.getElementsByName('periodiciteNouvelleMasse')[2].checked) {
		mntToTest = mnt * 12;
	} else if(document.getElementsByName('periodiciteNouvelleMasse')[1].checked){
		mntToTest = mnt * 4;
	} else {
		mntToTest = mnt;
	}
	
	if(mntToTest>200000) {
		if(<%=!viewBean.getPeriodicite().equals(globaz.naos.translation.CodeSystem.PERIODICITE_MENSUELLE) && viewBean.getAssurance()!=null && globaz.naos.translation.CodeSystem.TYPE_ASS_COTISATION_AVS_AI.equals(viewBean.getAssurance().getTypeAssurance())%>) {
			alert("<%=viewBean.getSession().getLabel("NAOS_SAISIE_MASSE_SUP")%>");
		}
	}
}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
					Versicherungserfassung - Detail
					<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
						<TR> 
							<TD> 
								<TABLE border="0" cellspacing="0" cellpadding="0">
									<TBODY> 
									<naos:AFInfoAffiliation name="affiliationId" affiliation="<%=viewBean.getAffiliation()%>" colspan="5"/>
									<TR> 
										<TD nowrap  height="11" colspan="6"> 
											<hr size="3" width="100%">
											<INPUT type="hidden" name="selectedId" value='<%=viewBean.getCotisationId()%>'>
											<INPUT type="hidden" name="assuranceId" value='<%=viewBean.getAssuranceId()%>'>
											<INPUT type="hidden" name="planAffiliationId" value='<%=viewBean.getPlanAffiliationId()%>'>
										</TD>
									</TR>

									<TR>
										<TD nowrap width="175" height="31"><ct:FWLabel key="ID_ASSURANCE"/></TD>
										<TD nowrap colspan="2"><%=viewBean.getAssuranceId()%></TD>
									</TR>

									<TR> 
										<TD nowrap width="175" height="31">Erfassungsplan</TD>
										
										<TD nowrap width="270">
											<select name="newPlanAffiliationId">
												<%=globaz.naos.util.AFUtil.getPlanAffiliationInfoRom280(viewBean.getAffiliation().getAffiliationId(), viewBean.getPlanAffiliationId(), session,false)%>
											</select>	
										</TD>
										<TD width="32">&nbsp;</TD>
										<TD nowrap width="125">Kasse - Versicherungsplan</TD>
										<TD width="31"></TD>
										<TD nowrap width="308"> 
											<%--
												String stCaisse = "";
												if (viewBean.getCaisse().getAdministration() != null) {
													stCaisse = viewBean.getCaisse().getAdministration().getNom();
												}									
											%>
											<INPUT type="text" name="caisse" size="35" value="<%=stCaisse%>" class="libelleLongDisabled" tabindex="-1" readOnly--%>
											<ct:FWListSelectTag name="planCaisseId" data="<%=viewBean.getPlanCaisseList()%>" defaut="<%=viewBean.getPlanCaisseId()%>" doClientValidation="' onchange='reloadPage()"/>
										</TD>
									</TR>
									<TR> 
										<TD nowrap width="175" height="31" >Versicherung</TD>
										
										<TD nowrap width="270"> 
											<%
												String libelleAssurance = "";
												if (viewBean != null &&  viewBean.getAssurance() != null) {
													libelleAssurance = viewBean.getAssurance().getAssuranceLibelleCourt();
												}
											%>
											<INPUT type="text" name="libelleAssurance" size="35" maxlength="40" value="<%=libelleAssurance%>"class="libelleLongDisabled" tabindex="-1" readOnly>
										</TD>
										<TD colspan="4"> 
											<% if (method != null && method.equalsIgnoreCase("add")) { 
												Object[] caisseMethods= new Object[]{
													new String[]{"setAssuranceId","getAssuranceId"}
												};
											%>
											<ct:FWSelectorTag 
												name="assuranceSelector" 
												
												methods="<%=caisseMethods%>"
												providerApplication ="naos"
												providerPrefix="AF"
												providerAction ="naos.assurance.assurance.chercher"/> 
											<% } %>
										</TD>
									</TR>
									<TR> 
										<TD nowrap width="175" height="31">Periode</TD>
										
										<TD nowrap width="270"> 
											<ct:FWCalendarTag name="dateDebut" value="<%=viewBean.getDateDebut()%>" doClientValidation="' onchange='reloadPage()"/> 
											bis 
											<ct:FWCalendarTag name="dateFin" value="<%=viewBean.getDateFin()%>"/> 
										</TD>
										<TD width="32"></TD>
										<TD nowrap width="125">Abgangsgrund</TD>
										<TD width="31"></TD>
										<TD nowrap width="308">
											<ct:FWCodeSelectTag 
												name="motifFin"
												defaut="<%=viewBean.getMotifFin()%>"
												codeType="VEMOTIFFIN"
												wantBlank="true"/> 
										</TD>
									</TR>

									</TBODY> 
								</TABLE>
								<TABLE border="0" cellspacing="0" cellpadding="0" id="addUpdatePeriode">
									<TBODY>
									<TR>
										<TD nowrap width="175" height="31">Rechnungsperiodizität</TD>
										
										<TD nowrap width="270"> 
											<TABLE border="0" cellspacing="0" cellpadding="0" id="periodiciteCotisationDisplay">
											<TBODY>
											<TR>
												<TD>
													<ct:FWCodeSelectTag 
														name="periodicite"
														defaut="<%=viewBean.getPeriodicite()%>"
														codeType="VEPERIODIC"
														except="<%=viewBean.getExceptPeriodicite()%>"
														doClientValidation="' onchange='showMoisAnneeFact()" /> 
												</TD>
												<TD>&nbsp;</TD><TD align="center">
												<TABLE border="0" cellspacing="0" cellpadding="0" id="moisPeriodicite">
												<TR>
												<TD>Mois&nbsp;</TD>
												<TD><ct:FWCodeSelectTag name="traitementMoisAnnee"
													 wantBlank="true" 
													 codeType="VEMOIS" 
													 defaut="<%=viewBean.getTraitementMoisAnnee()%>"/>
												</TD>
												</TR></TABLE></TD>
											</TR>
											</TBODY> 
											</TABLE>
											<TABLE border="0" cellspacing="0" cellpadding="0" id="periodiciteAffiliationDisplay">
											<TBODY>
											<TR><TD>
												<INPUT type="text" name="periodiciteReadOnly"  value="<%=globaz.naos.translation.CodeSystem.getLibelle(session, viewBean.getAffiliation().getPeriodicite())%>" class="Disabled" tabindex="-1" readOnly>
											</TD></TR>
											</TBODY> 
											</TABLE>
										</TD>
										<TD width="32"></TD>
										<TD nowrap width="125">Ausnahme an der Hauptperiodizität</TD>
										<TD width="31"></TD>
										<TD nowrap height="31" width="307"> 
											<INPUT type="checkbox" name="exceptionPeriodicite" <%=viewBean.getExceptionPeriodicite().booleanValue() ? "checked" : ""%> 
												onClick="onChangeException();">
										</TD>
									</TR>
									</TBODY> 
								</TABLE>
								<TABLE border="0" cellspacing="0" cellpadding="0" id="displayPeriode">
									<TBODY>
									<TR>
										<TD nowrap width="175" height="31">Rechnungsperiodizität</TD>
										
										
										<TD nowrap width="270"> 
											
											
											<TABLE border="0" cellspacing="0" cellpadding="0" id="periodiciteCotisationDisplayRO">
											<TBODY>
											<TR>
												<TD>
													<INPUT type="text" name="periodiciteReadOnly" size="10" value="<%=globaz.naos.translation.CodeSystem.getLibelle(session, viewBean.getPeriodicite())%>" class="Disabled" tabindex="-1" readOnly>
												</TD>
												<TD>&nbsp;</TD><TD align="center">
												<TABLE border="0" cellspacing="0" cellpadding="0" id="moisPeriodiciteRO">
												<TR>
												<TD>Mois&nbsp;</TD>
												<TD>
													<INPUT type="text" name="traitementMoisAnneeRO" size="10" value="<%=globaz.naos.translation.CodeSystem.getLibelle(session, viewBean.getTraitementMoisAnnee())%>" class="Disabled" tabindex="-1" readOnly>
												</TD>
												</TR></TABLE></TD>
											</TR>
											</TBODY> 
											</TABLE>
											<TABLE border="0" cellspacing="0" cellpadding="0" id="periodiciteAffiliationDisplayRO">
											<TBODY>
											<TR><TD>
												<INPUT type="text" name="periodiciteReadOnly"  value="<%=globaz.naos.translation.CodeSystem.getLibelle(session, viewBean.getAffiliation().getPeriodicite())%>" class="Disabled" tabindex="-1" readOnly>
											</TD></TR>
											</TBODY> 
											</TABLE>
											
										</TD>
										<TD width="32"></TD>
										<TD nowrap width="125">Ausnahme an der Hauptperiodizität</TD>
										<TD width="31"></TD>
										<TD nowrap width="307"> 
											<INPUT type="checkbox" name="exceptionPeriodiciteReadOnly" <%=viewBean.getExceptionPeriodicite().booleanValue() ? "checked" : ""%>>
										</TD>
									</TR>
									</TBODY> 
								</TABLE>
								<TABLE border="0" cellspacing="0" cellpadding="0" id="addUpdatePar">
									<TBODY>
									<!--TR> 
										<TD nowrap height="31" width="175">Paritätischer Beitrag</TD>
										<TD colspan="6"></TD>
									</TR-->
									<TR>
										<TD nowrap width="175" height="31" >Neue Lohnsumme oder Beitrag ?</TD>
										
										<TD nowrap width="270"> 
											<INPUT type="text" name="nouvelleMasse" size="19" value="<%=viewBean.getMassePeriodicite()%>" style="text-align : right;" onchange="validateFloatNumber(this);checkMasseLimit(this);">
										</TD>
										<TD width="32"></TD>										
										<TD nowrap colspan="3"><!--/TD>	
										<TD nowrap width="31"></TD>									
										<TD width="307"--> 
											<%
												String tmpPeriodicite = "";
												if (globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getPeriodicite())) {
													tmpPeriodicite = viewBean.getAffiliation().getPeriodicite();
												} else {
													tmpPeriodicite = viewBean.getPeriodicite();
												}
											%>
											<INPUT type="radio" name="periodiciteNouvelleMasse" 
												value="<%=globaz.naos.translation.CodeSystem.PERIODICITE_ANNUELLE%>" 
												<%=(tmpPeriodicite.equals(globaz.naos.translation.CodeSystem.PERIODICITE_ANNUELLE))? "checked" : ""%>>
												Jährlich
											<INPUT type="radio" name="periodiciteNouvelleMasse" 
												value="<%=globaz.naos.translation.CodeSystem.PERIODICITE_TRIMESTRIELLE%>"
												<%=(tmpPeriodicite.equals(globaz.naos.translation.CodeSystem.PERIODICITE_TRIMESTRIELLE))? "checked" : ""%>>
												Vierteljährlich 
											<INPUT type="radio" name="periodiciteNouvelleMasse" 
												value="<%=globaz.naos.translation.CodeSystem.PERIODICITE_MENSUELLE%>" 
												<%=(tmpPeriodicite.equals(globaz.naos.translation.CodeSystem.PERIODICITE_MENSUELLE))? "checked" : ""%>>
												Monatlich 
										</TD>
									</TR>
									</TBODY>
								</TABLE>

								<TABLE border="0" cellspacing="0" cellpadding="0" id="displayPar">
									<TBODY>
									<!--TR> 
										<TD nowrap height="31" width="175">Paritätischer Beitrag</TD>
										<TD colspan="6"></TD>
									</TR-->
									<TR> 
										<TD nowrap width="175">Lohnsumme oder Beitrag der Periodizität</TD>
										
										<TD nowrap width="270"> 
											<INPUT name="massePeriodiciteReadOnly" size="20" type="text" value="<%=viewBean.getMassePeriodicite()%>" style="text-align : right;" class="montantDisabled" tabindex="-1" readOnly>
										</TD>
										<TD width="32"></TD>
										<TD nowrap width="125" height="31">Lohnsumme oder Jahresbeitrag</TD>
										<TD nowrap width="31"></TD>
										<TD nowrap width="307"> 
											<INPUT name="massePeriodiciteReadOnly" size="20" type="text" value="<%=viewBean.getMasseAnnuelle()%>" style="text-align : right;" class="montantDisabled" tabindex="-1" readOnly>
										</TD>
									</TR>
									</TBODY>
								</TABLE>
								<TABLE border="0" cellspacing="0" cellpadding="0" id="displayPersTypeManuel">
                                    <TBODY> 
                                    <TR> 
                                        <TD nowrap height="31"
width="175">Beiträge</TD>
                                        <TD colspan="5"></TD>
                                    </TR>
                                    <TR> 
                                        <TD nowrap height="31"
width="175">Jährliche</TD>

                                        <TD nowrap width="270"> 
                                            <INPUT name="montantAnnuel"
size="20" type="text" value="<%=viewBean.getMontantAnnuel()%>"
style="text-align : right;" class="montant" tabindex="-1"  >
                                        </TD>
                                        <TD width="32"></TD>
                                        <TD nowrap
width="125">Halbjährliche</TD>
                                        <TD width="31"></TD>
                                        <TD nowrap width="308"> 
                                            <INPUT name="montantSemestriel"
size="20" type="text" value="<%=viewBean.getMontantSemestriel()%>"
style="text-align : right;" class="montant" tabindex="-1"  >
                                        </TD>
                                    </TR>
                                    <TR> 
                                        <TD nowrap height="31" 
width="175">Vierteljährliche</TD>

                                        <TD nowrap width="270"> 
                                            <INPUT name="montantTrimestriel"
size="20" type="text" value="<%=viewBean.getMontantTrimestriel()%>"
style="text-align : right;" class="montant" tabindex="-1"  >
                                        </TD>
                                        <TD width="32"></TD>
                                        <TD nowrap width="125">Monatliche</TD>
                                        <TD width="31"></TD>
                                        <TD nowrap width="308"> 
                                            <INPUT name="montantMensuel"
size="20" type="text" value="<%=viewBean.getMontantMensuel()%>"
style="text-align : right;" class="montant" tabindex="-1"  >
                                        </TD>
                                    </TR>
                                </TBODY>
                            </TABLE>
								
								<TABLE border="0" cellspacing="0" cellpadding="0" id="displayPers">
									<TBODY> 
									<TR> 
										<TD nowrap height="31" width="175">Beiträge</TD>
										<TD colspan="5"></TD>
									</TR>
									<TR> 
										<TD nowrap height="31" width="175">Jährliche</TD>
										
										<TD nowrap width="270"> 
											<INPUT name="montantAnnuelReadOnly1" size="20" type="text" value="<%=viewBean.getMontantAnnuel()%>" style="text-align : right;" class="montantDisabled" tabindex="-1"  readOnly>
										</TD>
										<TD width="32"></TD>
										<TD nowrap width="125">Halbjährliche</TD>
										<TD width="31"></TD>
										<TD nowrap width="308"> 
											<INPUT name="montantSemestrielReadOnly1" size="20" type="text" value="<%=viewBean.getMontantSemestriel()%>" style="text-align : right;" class="montantDisabled" tabindex="-1"  readOnly>
										</TD>
									</TR>
									<TR> 
										<TD nowrap height="31"  width="175">Vierteljährliche</TD>
										
										<TD nowrap width="270"> 
											<INPUT name="montantTrimestrielReadOnly1" size="20" type="text" value="<%=viewBean.getMontantTrimestriel()%>" style="text-align : right;" class="montantDisabled" tabindex="-1"  readOnly>
										</TD>
										<TD width="32"></TD>
										<TD nowrap width="125">Monatliche</TD>
										<TD width="31"></TD>
										<TD nowrap width="308"> 
											<INPUT name="montantMensuelReadOnly1" size="20" type="text" value="<%=viewBean.getMontantMensuel()%>" style="text-align : right;" class="montantDisabled" tabindex="-1"  readOnly>
										</TD>
									</TR>
								</TBODY>
							</TABLE>
							<TABLE border="0" cellspacing="0" cellpadding="0">
								<TBODY>
									<TR> 
										<TD nowrap width="194" height="31" >Abrechnung am Hauptsitz</TD>
										<TD nowrap width="302"> 
											<INPUT type="checkbox" name="maisonMere" <%=(viewBean.getMaisonMere().booleanValue())? "checked" : ""%> >
										</TD>
									</TR>
									
									<TR> 
										<TD nowrap width="194" height="31">Beitragsatz forciert</TD>
										<TD nowrap width="302">
											<%
												String tauxAssuranceString = "";
												try {
													globaz.naos.db.tauxAssurance.AFTauxAssurance tauxAssurance = new globaz.naos.db.tauxAssurance.AFTauxAssurance();
													tauxAssurance.setSession(viewBean.getSession());
													tauxAssurance.setTauxAssuranceId(viewBean.getTauxAssuranceId());
													tauxAssurance.retrieve();
													tauxAssuranceString = tauxAssurance.getValeurTotal();
												} catch (Exception e) {
													// Ignore
												}	
											
												Object[] tauxAssuranceMethods = new Object[]{
													new String[]{"setTauxAssuranceId","getTauxAssuranceId"}
												};
												Object[] tauxAssuranceParams = new Object[] {
														new String[]{"forTypeId", "selType"}
												};
											%>
						              		<INPUT type="text" id="tauxAssurance" value="<%=tauxAssuranceString%>" tabindex="-1" readonly class="montantDisabled">
											<INPUT type="hidden" id="tauxAssuranceId" name="tauxAssuranceId" value="<%=viewBean.getTauxAssuranceId()%>">
											<INPUT type="hidden" id="forTypeId" name="forTypeId" value="<%=globaz.naos.translation.CodeSystem.TYPE_TAUX_FORCE%>">
											<% if (!globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getAssuranceId())) {%>
											<ct:FWSelectorTag 
												name="tauxAssuranceSelector" 
												
												methods="<%=tauxAssuranceMethods%>"
												providerApplication ="naos"
												providerPrefix="AF"
												providerAction ="naos.tauxAssurance.tauxAssurance.chercher"
												providerActionParams="<%=tauxAssuranceParams%>"/> 
											<INPUT type="button" value="X" onClick="document.all('tauxAssurance').value=''; document.all('tauxAssuranceId').value=''; reloadPage()"> 
											<% } %>
										</TD>
										<TD nowrap width="156">Kategorie</TD>
										<TD nowrap>
											<ct:FWCodeSelectTag 
												name="categorieTauxId" 
												defaut="<%=viewBean.getCategorieTauxId()%>"
												codeType="VECATETAUX"
												wantBlank="true"
												doClientValidation="' onchange='reloadPage()"/>
										</TD>
									</TR>
									
									<TR> 
										<TD nowrap height="14" colspan="4"></TD>
									</TR>
									<TR> 
										<TD nowrap colspan="4"><font size="2"><b>Standartwerte von dieser Versicherung</b></font></TD>
									</TR>
									<TR> 
										<TD height="11" colspan="4"> 
											<hr size="3" >
										</TD>
									</TR>
									<TR> 
										<TD nowrap height="31" width="194">Beitragsatz</TD>
										<TD nowrap width="302">
											<%
												String valeurTotal = "";
												String fraction = "";
												
												String date = globaz.globall.util.JACalendar.todayJJsMMsAAAA();
												if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getDateFin())) {
													date = viewBean.getDateFin();
												}
												
												if(BSessionUtil.compareDateFirstGreater(viewBean.getSession(), viewBean.getDateDebut(),date)){
													date = viewBean.getDateDebut();
												}
												
												//java.util.List tauxList = viewBean.getTauxList(date);
												if(!JadeStringUtil.isEmpty(viewBean.getMasseAnnuelle()) && JadeStringUtil.isIntegerEmpty(viewBean.getMontant())) {
													AFTauxAssurance tauxAssurance = viewBean.findTaux(date,viewBean.getMasseAnnuelle(),true);
													if (tauxAssurance!=null) {
														//valeurTotal = ((globaz.naos.db.tauxAssurance.AFTauxAssurance) tauxList.get(0)).getValeurTotal();
														valeurTotal = tauxAssurance.getValeurTotal();
														//fraction = ((globaz.naos.db.tauxAssurance.AFTauxAssurance) tauxList.get(0)).getFraction();
														fraction = tauxAssurance.getFraction();
													}
												}
											%>
											<INPUT name="tauxReadOnly" size="10" type="text" value="<%=valeurTotal%>" tabindex="-1"  readonly style="text-align : right;" class="libelleDisabled">
											/ 
											<INPUT name="fractionReadOnly" size="10" type="text" value="<%=fraction%>" tabindex="-1"  readonly style="text-align : right;" class="libelleDisabled">
										</TD>
									</TR>
									<TR> 
										<TD nowrap width="194" height="31" >Jährlicher Betrag </TD>
										<TD nowrap width="302"> 
											<INPUT type="text" name="montantReadOnly" size="20" value="<%=viewBean.getMontant()%>" tabindex="-1"  readonly style="text-align : right;" class="montantDisabled">
										</TD>
										<TD nowrap width="156">Periodizität</TD>
										<TD nowrap> 
											<INPUT type="text" name="periodiciteMontantReadOnly" size="25" maxlength="25" value='<%=!JadeStringUtil.isIntegerEmpty(viewBean.getMontant())?globaz.naos.translation.CodeSystem.getLibelle(session,viewBean.getPeriodiciteMontant()):""%>' tabindex="-1" readonly class="libelleLongDisabled">
										</TD>
									</TR>
									
									</TBODY> 
								</TABLE>
							</TD>
						</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<SCRIPT language="JavaScript">
				document.getElementById("btnVal").onmouseenter=new Function("","enterValidate=true;");
				document.getElementById("btnVal").onmouseleave=new Function("","enterValidate=false;");
				</script>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<SCRIPT>
</SCRIPT>
<% } %>
	<ct:menuChange displayId="menu" menuId="AFMenuPrincipal"/>
	<ct:menuChange displayId="options" menuId="AFOptionsCotisation" showTab="options">
		<ct:menuSetAllParams key="cotisationId" value="<%=viewBean.getCotisationId()%>"/>
	</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>