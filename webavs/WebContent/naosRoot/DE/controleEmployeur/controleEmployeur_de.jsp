<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<!-- Creer l'enregistrement si il n'existe pas -->
<%@ taglib uri="/WEB-INF/naos.tld" prefix="naos" %>
<%
	idEcran ="CAF0034";
	globaz.naos.db.controleEmployeur.AFControleEmployeurViewBean viewBean = (globaz.naos.db.controleEmployeur.AFControleEmployeurViewBean)session.getAttribute ("viewBean");
	String jspLocation = servletContext + mainServletPath + "Root/affilie_select.jsp";
	String jspLoc = servletContext + mainServletPath + "Root/reviseur_select.jsp";
	String method = request.getParameter("_method");
	int autoDigiAff = globaz.naos.util.AFUtil.getAutoDigitAff(session);
%>
<SCRIPT language="JavaScript">
</SCRIPT> <%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">


function add() {
	document.forms[0].elements('userAction').value="naos.controleEmployeur.controleEmployeur.ajouter"
}

function upd() {
	document.getElementById("inscriptionRC").disabled = <%=viewBean.isInscrit()%>;
}

function validate() {
	var exit = true;
	var message = "FEHLER : Alle obligatorische Feldern sind nicht ausgefüllt !";
	if (document.forms[0].elements('datePrevue').value == "")
	{
		message = message + "\nSie haben das vorhergesehene Datum nicht erfasst !";
		exit = false;
	}
	
	if (exit == false)
	{
		alert (message);
		return (exit);
	}
	document.forms[0].elements('userAction').value="naos.controleEmployeur.controleEmployeur.modifier";
	
	if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="naos.controleEmployeur.controleEmployeur.ajouter";
    else
        document.forms[0].elements('userAction').value="naos.controleEmployeur.controleEmployeur.modifier";
	return (exit);
}

function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
 		document.forms[0].elements('userAction').value="naos.controleEmployeur.controleEmployeur.afficher";
}

function del() {
	if (window.confirm("Sie sind dabei, die ausgewählte Arbeitgeberkontrolle zu löschen! Wollen Sie fortfahren?")) {
		document.forms[0].elements('userAction').value="naos.controleEmployeur.controleEmployeur.supprimer";
		document.forms[0].submit();
	}
}
function updateAffilieNumero(tag){
	if(tag.select && tag.select.selectedIndex != -1) {
		document.getElementById('numAffilie').value     = tag.select[tag.select.selectedIndex].value;
		document.getElementById('idTiers').value           = tag.select.options[tag.select.selectedIndex].idTiers;
		document.getElementById('descriptionTiers1').value = tag.select.options[tag.select.selectedIndex].designation1;
		document.getElementById('descriptionTiers2').value = tag.select.options[tag.select.selectedIndex].designation2;
	} 
}
function updateVisa(tag){
	if(tag.select && tag.select.selectedIndex != -1) {
		document.getElementById('controleurVisa').value     = tag.select[tag.select.selectedIndex].value;
		document.getElementById('controleurNom').value = tag.select.options[tag.select.selectedIndex].nomReviseur;
	} 
}

function init() {
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
</SCRIPT> 
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
					Arbeitgeberkontrolle - Detail 
					<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR><TD>&nbsp;</TD></TR>
						<TR>
						<TD nowrap width="121" height="31">Abr.-Nr.	
							<INPUT type="hidden" name="controleEmployeur" value='saisie'>
							<INPUT type="hidden" name="idTiers"    value='<%=viewBean.getIdTiers()%>'>
							<INPUT type="hidden" name="selectedId" value='<%=viewBean.getControleEmployeurId()%>'>
						</TD>
						<TD nowrap> 
							<% 
								String nAff = request.getParameter("numAffilie");
								if(nAff==null){
									nAff="";
								}							
								if (method != null && method.equalsIgnoreCase("add")) { 
									String 	designation1 = "";
									String 	designation2 = "";
									if ( viewBean.getTiers() != null) {
										designation1 = viewBean.getTiers().getDesignation1();
										designation2 = viewBean.getTiers().getDesignation2();
									}
							%>
							<ct:FWPopupList 
								name="numAffilie" 
								value="<%=nAff%>" 
								className="libelle" 
								jspName="<%=jspLocation%>" 
								autoNbrDigit="<%=autoDigiAff%>" 
								size="15"
								minNbrDigit="3"
								onChange="updateAffilieNumero(tag);"
								/>
							<IMG
								src="<%=servletContext%>/images/down.gif"
								alt="presser sur la touche 'flèche bas' pour effectuer une recherche"
								title="presser sur la touche 'flèche bas' pour effectuer une recherche"
								onclick="if (document.forms[0].elements('affilieNumero').value != '') affilieNumeroPopupTag.validate();">
							&nbsp; 
							<input type="text" name="descriptionTiers1" value="<%=designation1%>" readonly="readonly" tabindex="-1" class="libelleLongDisabled">&nbsp;
							<input type="text" name="descriptionTiers2" value="<%=designation2%>" readonly="readonly" tabindex="-1" class="libelleLongDisabled">
						<% } else  { %>
							<input name="numAffilie" value="<%=viewBean.getNumAffilie()%>" readonly="readonly" tabindex="-1" class="disabled" style="width:100px">&nbsp;
							<input type="text" name="descriptionTiers1" value="<%=viewBean.getTiers().getDesignation1()%>" readonly="readonly" tabindex="-1" class="libelleLongDisabled">&nbsp;
							<input type="text" name="descriptionTiers2" value="<%=viewBean.getTiers().getDesignation2()%>" readonly="readonly" tabindex="-1" class="libelleLongDisabled">
						<% } %>
						</TD>
					</TR>
					<TR><TD>&nbsp;</TD></TR>
					<TR> 
						<TD nowrap  height="11" colspan="2">
							<hr size="3">
						</TD>
					</TR>
			<tr><td colspan="2">
				<TABLE border="0" cellspacing="0" cellpadding="0" id="tPartie1">
					<TBODY>
						<TR>
							<TD nowrap height="31">Vorgesehenes Datum</TD>
							
							<TD nowrap colspan="2"> 
								<%if (method != null && method.equalsIgnoreCase("add")) { 
									String date = request.getParameter("dateDebutControle");
									if(date==null){
										date="";
									}
								%>
									<ct:FWCalendarTag name="datePrevue" value="<%=date%>" /> 
								<%}else{%>
									<ct:FWCalendarTag name="datePrevue" value="<%=viewBean.getDatePrevue()%>" /> 
								<%}%>
							</TD>
							<TD>&nbsp;&nbsp;</TD>
							<TD nowrap>Berichtnummer</TD>
							<TD nowrap> 
								<input name="nouveauNumRapport" type="text" size="20" maxlength="12" value="<%=viewBean.getNouveauNumRapport()%>">
							</TD>
						</TR>	
						<TR>
							<TD nowrap height="31">Kontrollart</TD>
							
							<TD nowrap colspan="2">
								<ct:FWCodeSelectTag 
									name="genreControle"
									defaut="<%=viewBean.getGenreControle()%>"
									codeType="VEGENRECON"/> 
							</TD>
							<TD>&nbsp;&nbsp;</TD>
							<TD nowrap>Revisor</TD>
							
							<TD nowrap> 
								<!--% 							
								if (method != null && method.equalsIgnoreCase("add")) { 
								%-->
								<ct:FWPopupList 
									name="controleurVisa" 
									value="<%=viewBean.getControleurVisa()%>" 
									className="libelle" 
									jspName="<%=jspLoc%>" 
									autoNbrDigit="<%=autoDigiAff%>" 
									size="10"
									minNbrDigit="1"
									onChange="updateVisa(tag);"
									/>
								&nbsp;
								<INPUT name="controleurNom" type="text" readonly="readonly" tabindex="-1" class="libelleLongDisabled" value="<%=viewBean.getControleurNom()%>">
								<!--% } else  { %>
									<input name="controleurVisa" type="text" value="<%=viewBean.getControleurVisa()%>" size="10">&nbsp;
									<INPUT name="controleurNom" type="text" value="<%=viewBean.getControleurNom()%>" readonly="readonly" tabindex="-1" class="libelleLongDisabled"-->
								<!--% } %-->
							</TD>
						</TR>
						<TR>
							<TD nowrap height="31">Effektives Kontrolldatum</TD>
							
							<TD nowrap colspan="2">
								<ct:FWCalendarTag name="dateEffective" value="<%=viewBean.getDateEffective()%>" /> 
							</TD>
							<TD>&nbsp;&nbsp;</TD>
							<TD nowrap>Bericht mit Differenz</TD>
							
							<TD nowrap> 
								<input type="checkbox" name="erreur" <%=(viewBean.isErreur().booleanValue())? "checked" : ""%> >
							</TD>
						</TR>
						<TR>
							<TD nowrap height="31">Datum zu kontrollieren</TD>
							<%

								String dateDeb = (viewBean.getDateDebutControle()== null)?"":viewBean.getDateDebutControle();
								String dateFin = (viewBean.getDateFinControle()== null)?"":viewBean.getDateFinControle();
							
							%>
							<TD nowrap colspan="2">
								<ct:FWCalendarTag name="dateDebutControle" value="<%=dateDeb%>" /> 
								au &nbsp;
								<ct:FWCalendarTag name="dateFinControle" value="<%=dateFin%>" /> 
							</TD>
							<TD>&nbsp;&nbsp;</TD>
							<TD nowrap>Grund</TD>
							
							<TD nowrap>
								<ct:FWCodeSelectTag 
									name="debitCredit"
									defaut="<%=viewBean.getDebitCredit()%>"
									codeType="VEDEBITCRE"
									wantBlank="true"/> 
							</TD>
						</TR>
						<TR>
							<TD nowrap height="31" >Datum der vorigen Kontrolle&nbsp;</TD>
							
							<TD nowrap colspan="2"> 
								<ct:FWCalendarTag name="datePrecedente" value="<%=viewBean.getDatePrecedentControle()%>" /> 
							</TD>
							<TD>&nbsp;&nbsp;</TD>
							<TD nowrap>Kontrollzeit</TD>
							<TD nowrap> 
								<input name="tempsJour" type="text" size="10" value="<%=viewBean.getTempsJour()%>"> in Tagen
							</TD>
						</TR>
						<TR>
							<TD nowrap height="31" >Num&eacute;ro du rapport interne</TD>
							<TD nowrap colspan="2"> 
								<input name="rapportNumero" type="text" size="20" value="<%=viewBean.getRapportNumero()%>">
							</TD>
							<TD>&nbsp;&nbsp;</TD>
							<TD nowrap>Externe Abr.-Nr.</TD>
							
							<TD nowrap> 
								<INPUT name="numeroExterne" type="text" size="20" maxlength="15" value="<%=viewBean.getNumeroExterne()%>">
							</TD>
						</TR>
						<TR> 
							<TD nowrap  height="11" colspan="6">
								<hr size="3" width="100%">
							</TD>
						</TR>
						<TR> 
							<TD nowrap height="31" >Eintragung im HR</TD>
							
							<TD nowrap colspan="2">
								<input type="checkbox" disabled="disabled" name="inscriptionRC" <%=(viewBean.getInscriptionRCParticularite().booleanValue())? "checked" : ""%> >
							</TD>
							<TD>&nbsp;&nbsp;</TD>
							<TD nowrap>Anzahl Arbeitnehmern</TD>
							<TD nowrap> 
								<INPUT name="nombreSalariesFixes" type="text" size="10" value="<%=viewBean.getNombreSalariesFixes()%>">
							</TD>							
						</TR>
						<TR> 
							<TD nowrap height="31" >Abschlussdatum</TD>
							<TD nowrap colspan="2"> 
								<ct:FWCalendarTag name="dateBouclement" value="<%=viewBean.getDateBouclement()%>" /> 
							</TD>
							<TD>&nbsp;&nbsp;</TD>
							<TD nowrap>Buchhaltung geführt durch</TD>
							<TD nowrap> 
								<INPUT name="comptaTenuPar" type="text" size="35" value="<%=viewBean.getComptaTenuPar()%>">
							</TD>							
						</TR>
						<TR>
							<TD nowrap height="31" >Kontaktpersonen</TD>
							<TD nowrap colspan="2"> 
								<INPUT name="personneContact1" type="text" size="20" value="<%=viewBean.getPersonneContact1()%>" class="libelleLong">
							</TD>
							<TD>&nbsp;&nbsp;</TD>
							<TD nowrap>Erwerbszweig</TD>
							<TD nowrap>
								<ct:FWCodeSelectTag 
	                				name="brancheEco" 
									defaut="<%=viewBean.getBrancheEco()%>"
									wantBlank="true"
									codeType="VEBRANCHEE"/> 
								<SCRIPT>
									document.getElementById("brancheEco").tabIndex="-1";
								</SCRIPT>									
							</TD>
						</TR>
						<TR>
							<TD nowrap height="31" ></TD>
							<TD nowrap colspan="2"> 
								<INPUT name="personneContact2" type="text" size="20" value="<%=viewBean.getPersonneContact2()%>" class="libelleLong">
							</TD>
							<TD>&nbsp;&nbsp;</TD>
							<TD nowrap>Rechtsform</TD>
							<TD nowrap>
								<ct:FWCodeSelectTag 
	                				name="formeJuri"
	                				libelle="both" 
									defaut="<%=viewBean.getFormeJuri()%>"
									codeType="VEPERSONNA"
									wantBlank="true"/> 	
								<SCRIPT>
									document.getElementById("formeJuri").tabIndex="-1";
								</SCRIPT>								
							</TD>
							
						</TR>
						<TR>
							<TD nowrap height="31" ></TD>
							<TD nowrap colspan="2"> 
								<INPUT name="personneContact3" type="text" size="20" value="<%=viewBean.getPersonneContact3()%>" class="libelleLong">
							</TD>
							<TD>&nbsp;&nbsp;</TD>
						</TR>
						<TR>
														
							<TD nowrap height="31" >Filialen</TD>
							<TD nowrap colspan="2">
								<input type="checkbox" name="succursales" <%=(viewBean.getSuccursales().booleanValue())? "checked" : ""%> >
							</TD>
						</TR>				
						<TR> 
							<TD nowrap height="31"></TD>
							<TD nowrap colspan="4">
								<B><A href="javascript:showPartie1()">Seite 1</A></B> 
								-- 
								<A href="javascript:showPartie2()">Seite 2</A>
								--								
								<A href="javascript:showPartie3()">Seite 3</A>
							</TD>
						</TR>
					</TBODY> 
				</TABLE>
				<TABLE border="0" cellspacing="0" cellpadding="0" id="tPartie2" style="display:none">
					<TBODY>
						<TR><TD colspan="3"><H4>Kontrollierte Dokumente beim Mitglied</H4></TD></TR>
						<TR>
							<TD nowrap colspan="2">
								<B>Lohnbuchhaltung (Journale, Individuelle Karteien)</B>
							</TD>							
						</TR>
						<TR>
							<TD nowrap width="161" height="31" >Vollständig</TD>
							<TD nowrap colspan="1"> 
								<INPUT name="docComptaComplet" type="text" size="35" value="<%=viewBean.getDocComptaComplet()%>">
							</TD>
							<td style="width: 5em"></td>
							<TD nowrap>Umfragen</TD>
							<TD nowrap>
								<INPUT name="docComptaSondage" type="text" size="35" value="<%=viewBean.getDocComptaSondage()%>">
							</TD>
							
						</TR>
						<TR>
							<TD nowrap colspan="2">
								<B>Bilanz, Gewinn- und Verlustrechnung, Hauptbuch und Buchungsbelege</B>
							</TD>							
						</TR>
						<TR>
							<TD nowrap width="161" height="31" >Vollständig</TD>
							
							<TD nowrap colspan="1">
								<INPUT name="docBilanComplet" type="text" size="35" value="<%=viewBean.getDocBilanComplet()%>">
							</TD>
							<td style="width: 5em"></td>
							<TD nowrap>Umfragen</TD>
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
							<TD colspan="3"><H4>Bestandteile des massgebenden Lohnes</H4></TD>
						</TR>
						<TR>
							<TD nowrap width="161" height="31" >
								<input type="checkbox" name="elePourboire" <%=(viewBean.getElePourboire().booleanValue())? "checked" : ""%> >
								Trinkgeld
							</TD>
							<TD nowrap colspan="2"> 
								<input type="checkbox" name="eleHonoraire" <%=(viewBean.getEleHonoraire().booleanValue())? "checked" : ""%> >
								Verwalterhonorare
							</TD>
							<TD nowrap> 
								<input type="checkbox" name="eleMensuel" <%=(viewBean.getEleMensuel().booleanValue())? "checked" : ""%> >
								Monatlich
							</TD>
							<TD nowrap> 
								<input type="checkbox" name="eleHeure" <%=(viewBean.getEleHeure().booleanValue())? "checked" : ""%> >
								Stunden
							</TD>
						</TR>
						<TR>
							<TD nowrap width="161" height="31" > 
								<input type="checkbox" name="eleNature" <%=(viewBean.getEleNature().booleanValue())? "checked" : ""%> >
								Naturallohn
							</TD>
							<TD nowrap colspan="2"> 
								<input type="checkbox" name="eleIndemnite" <%=(viewBean.getEleIndemnite().booleanValue())? "checked" : ""%> >
								Ferienentschädigung / Feiertage
							</TD>
							<TD nowrap> 
								<input type="checkbox" name="elePiece" <%=(viewBean.getElePiece().booleanValue())? "checked" : ""%> >
								Belege
							</TD>
							<TD nowrap> 
								<input type="checkbox" name="eleDomicile" <%=(viewBean.getEleDomicile().booleanValue())? "checked" : ""%> >
								Wohnsitz
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
								Kommission
							</TD>
							<TD nowrap> 
								<input type="checkbox" name="eleGratification" <%=(viewBean.getEleGratification().booleanValue())? "checked" : ""%> >
								Gratifikation
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
		                  <TD><B>Festgestellt</B></TD>
		                  <TD nowrap colspan="4"> 
		                    <TEXTAREA name="champConstate" rows="5" cols="100" width="800" onkeypress="maxLength(this, 1024);"><%=viewBean.getChampConstate()%></TEXTAREA>
		                  </TD>
		                </TR>						
						<TR> 
							<TD nowrap width="161" height="31"></TD>
							<TD nowrap colspan="4">
								<A href="javascript:showPartie1()">Seite 1</A>
								-- 
								<B><A href="javascript:showPartie2()">Seite 2</A></B>
								--								
								<A href="javascript:showPartie3()">Seite 3</A>
							</TD>
						</TR>	
					</TBODY> 
				</TABLE>
				<TABLE border="0" cellspacing="0" cellpadding="0" id="tPartie3" style="display:none">
					<TBODY>
						<TR>
							<TD colspan="3"><H4>Andere Kontrolle</H4></TD>
						</TR>
						<TR>
							<TD nowrap colspan="2">
								<B>Ergänzende Leistungen an angehörige der Armee</B>
							</TD>
						</TR>
						<TR>
							<TD nowrap width="161" height="31" >Vollständig</TD>
							<TD nowrap colspan="2"> 
								<INPUT name="docAllocMiliComplet" type="text" size="35" value="<%=viewBean.getDocAllocMiliComplet()%>">
							</TD>
							<td style="width: 5em"></td>
							<TD nowrap>Umfragen</TD>
							<TD nowrap> 
								<INPUT name="docAllocMiliSondage" type="text" size="35" value="<%=viewBean.getDocAllocMiliSondage()%>">
							</TD>
						</TR>
						<TR>
							<TD nowrap colspan="2">
								<B>Anspruch auf Zulagen (FZ)</B>
							</TD>
						</TR>
						<TR>
							<TD nowrap width="161" height="31" >Vollständig</TD>
							<TD nowrap colspan="2"> 
								<INPUT name="docDroitAllocComplet" type="text" size="35" value="<%=viewBean.getDocDroitAllocComplet()%>">
							</TD>
							<td style="width: 5em"></td>
							<TD nowrap>Umfragen</TD>
							<TD nowrap> 
								<INPUT name="docDroitAllocSondage" type="text" size="35" value="<%=viewBean.getDocDroitAllocSondage()%>">
							</TD>
						</TR>
						<TR><TD nowrap width="161" height="31" ></TD></TR>
						<TR>
							<TD nowrap colspan="2" >
								<B>Erwerbsausfallentschädigungen(EO, MSE)</B>
							</TD>
						</TR>
						<TR>
							<TD nowrap width="161" height="31" >Vollständig</TD>
							<TD nowrap colspan="2"> 
								<INPUT name="docAllocPerteComplet" type="text" size="35" value="<%=viewBean.getDocAllocPerteComplet()%>">
							</TD>
							<td style="width: 5em"></td>
							<TD nowrap>Umfragen</TD>
							<TD nowrap>
								<INPUT name="docAllocPerteSondage" type="text" size="35" value="<%=viewBean.getDocAllocPerteSondage()%>">
							</TD>
						</TR>
						<TR>
							<TD nowrap width="161" height="31" >Separaten Rapport der Familienzulagen</TD>
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
		                  <TD><B>Vergebene Ratschläge<br/>und Empfehlungen</B></TD>
		                  <TD nowrap colspan="5"> 
		                    <TEXTAREA name="champConseil" rows="5" cols="100" width="800" onkeypress="maxLength(this, 1024);"><%=viewBean.getChampConseil()%></TEXTAREA>
		                  </TD>
		                </TR>
		                <TR> 
		                  <TD><B>Bemerkungen</B></TD>
		                  <TD nowrap colspan="5"> 
		                    <TEXTAREA name="champRemarque" rows="5" cols="100" width="800" onkeypress="maxLength(this, 1024);"><%=viewBean.getChampRemarque()%></TEXTAREA>
		                  </TD>
		                </TR>
						<TR> 
							<TD width="100" height="31"></TD>
							<TD colspan="4">
								<A href="javascript:showPartie1()">Seite 1</A>
								-- 
								<A href="javascript:showPartie2()">Seite 2</A>
								--								
								<B><A href="javascript:showPartie3()">Seite 3</A></B>
							</TD>
						</TR>
					</TBODY> 
				</TABLE></td></tr>
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
	<ct:menuChange displayId="menu" menuId="AFMenuPrincipal"/>
	<ct:menuChange displayId="options" menuId="AFOptionsControlEmployeur" showTab="options">
		<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getControleEmployeurId()%>"/>
	</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>