<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<!-- Creer l'enregistrement si il n'existe pas -->
<%
	idEcran ="CAF0020";
	globaz.naos.db.tauxAssurance.AFTauxAssuranceViewBean viewBean = (globaz.naos.db.tauxAssurance.AFTauxAssuranceViewBean)session.getAttribute ("viewBean");
	
	
	bButtonUpdate = bButtonUpdate && !globaz.naos.translation.CodeSystem.TYPE_TAUX_FORCE.equals(viewBean.getTypeId());
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<SCRIPT language="JavaScript">


function add() {
	document.forms[0].elements('userAction').value="naos.tauxAssurance.tauxAssurance.ajouter"
}

function upd() {
}

function validate() {
	var exit = true;
	/*var message = "ERREUR tous les champs obligatoire ne sont pas rempli!";
	if (document.forms[0].elements('DateDebut').value == "")
	{
		message = message + "\nVous n'avez pas saisi la date de début!";
		exit = false;
	}
	
	if (exit == false)
	{
		alert (message);
		return (exit);
	}*/
	document.forms[0].elements('userAction').value="naos.tauxAssurance.tauxAssurance.modifier";
		
	if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="naos.tauxAssurance.tauxAssurance.ajouter";
    else
        document.forms[0].elements('userAction').value="naos.tauxAssurance.tauxAssurance.modifier";
	return (exit);
}

function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="naos.tauxAssurance.tauxAssurance.afficher";
}

function del() {
	if (window.confirm("Vous n'avez pas le droit de supprimer un taux d'assurance!")) {
		//document.forms[0].elements('userAction').value="naos.tauxAssurance.tauxAssurance.supprimer";
		//document.forms[0].submit();
	}
}

function init(){
	onChangeGenre();
	onChangeType();
}

function onChangeGenre(){
	var newGenreValeur = document.forms[0].elements('genreValeur').value;
	
	if (newGenreValeur == <%=globaz.naos.translation.CodeSystem.GEN_VALEUR_ASS_TAUX%>) {
		document.all('tauxVariable').style.display = 'none';
		document.all('montant').style.display      = 'none';
		document.all('taux').style.display         = 'block';
		document.all('type').style.display         = 'block';
	} else if (newGenreValeur == <%=globaz.naos.translation.CodeSystem.GEN_VALEUR_ASS_TAUX_VARIABLE%>) {
		document.all('taux').style.display         = 'none';
		document.all('montant').style.display      = 'none';
		document.all('tauxVariable').style.display = 'block';
		document.all('type').style.display         = 'block';
	} else if (newGenreValeur == <%=globaz.naos.translation.CodeSystem.GEN_VALEUR_ASS_MONTANT%>) {
		document.all('taux').style.display         = 'none';
		document.all('tauxVariable').style.display = 'none';
		document.all('montant').style.display      = 'block';		
		document.all('type').style.display         = 'none';
	}
}

function onChangeType(){
	var typeId = document.getElementsByName('typeId')[0].value;
	
	if (typeId == <%=globaz.naos.translation.CodeSystem.TYPE_TAUX_GROUPE%>) {
		document.all('categorie').style.display = 'block';
		document.all('caisse').style.display    = 'none';
		document.all('canton').style.display    = 'none';
	} else if (typeId == <%=globaz.naos.translation.CodeSystem.TYPE_TAUX_CAISSE%>) {
		document.all('categorie').style.display = 'none';
		document.all('caisse').style.display    = 'block';
		document.all('canton').style.display    = 'none';
	} else if (typeId == <%=globaz.naos.translation.CodeSystem.TYPE_TAUX_CANTON%>) {
		document.all('categorie').style.display = 'none';
		document.all('caisse').style.display    = 'none';
		document.all('canton').style.display    = 'block';		
	} else {
		document.all('categorie').style.display = 'none';
		document.all('caisse').style.display    = 'none';
		document.all('canton').style.display    = 'none';		
	}
}

function onChangePart() {
	// recalculer le taux total
	document.getElementsByName("valeurReadOnly2")[0].value = 
		((parseFloat(document.getElementsByName("valeurEmployeur")[0].value) +
		  parseFloat(document.getElementsByName("valeurEmploye")[0].value)) /
		 parseFloat(document.getElementsByName("fraction")[0].value) *
		 parseFloat(document.getElementsByName("fractionReadOnly2")[0].value)).toFixed(5);
}

function onChangeTotal() {
	// recalculer les taux employeur et employe
	var taux = 
		parseFloat(document.getElementsByName("valeurReadOnly2")[0].value) /
		(2 * parseFloat(document.getElementsByName("fractionReadOnly2")[0].value)) *
		parseFloat(document.getElementsByName("fraction")[0].value);
		
	document.getElementsByName("valeurEmployeur")[0].value = taux.toFixed(5);
	document.getElementsByName("valeurEmploye")[0].value = taux.toFixed(5);
} 

function onChangePartVar() {
	// recalculer le taux variable total
	document.getElementsByName("valeurReadOnly")[0].value = 
		((parseFloat(document.getElementsByName("valeurEmployeurVar")[0].value) +
		  parseFloat(document.getElementsByName("valeurEmployeVar")[0].value)) /
		 parseFloat(document.getElementsByName("fractionVar")[0].value) *
		 parseFloat(document.getElementsByName("fractionReadOnly")[0].value)).toFixed(5);
}

function onChangeTotalVar() {
	// recalculer les taux variables employeur et employe
	var taux = 
		parseFloat(document.getElementsByName("valeurReadOnly")[0].value) /
		(2 * parseFloat(document.getElementsByName("fractionReadOnly")[0].value)) *
		parseFloat(document.getElementsByName("fractionVar")[0].value);
		
	document.getElementsByName("valeurEmployeurVar")[0].value = taux.toFixed(5);
	document.getElementsByName("valeurEmployeVar")[0].value = taux.toFixed(5);
} 

function onChangePartMontant() {
	// recalculer le taux variable total
	document.getElementsByName("valeurReadOnly3")[0].value = 
		(parseFloat(document.getElementsByName("valeurEmployeurMon")[0].value) +
		 parseFloat(document.getElementsByName("valeurEmployeMon")[0].value)).toFixed(5);
}

function onChangeTotalMontant() {
	// recalculer les taux variables employeur et employe
	var taux = 
		parseFloat(document.getElementsByName("valeurReadOnly3")[0].value) / 2;
		
	document.getElementsByName("valeurEmployeurMon")[0].value = taux.toFixed(5);
	document.getElementsByName("valeurEmployeMon")[0].value = taux.toFixed(5);
}

function changeFractionTotal(){
	// affecte la même fraction au total (cellule disabled)
	var valeurFraction = document.forms[0].elements('fraction').value;	
	document.getElementsByName("fractionReadOnly2")[0].value = valeurFraction;
	
	// mise à zéro des autres champs de taux pour ne pas fausser les calculs
	document.getElementsByName("valeurEmployeur")[0].value = "";
	document.getElementsByName("valeurReadOnly2")[0].value = "";
	document.getElementsByName("valeurEmploye")[0].value = "";
} 
function changeFractionTotal2(){
	// affecte la même fraction au total (cellule disabled)
	var valeurFraction = document.forms[0].elements('fraction').value;	
	document.getElementsByName("fractionReadOnly")[0].value = valeurFraction;
	
	// mise à zéro des autres champs de taux pour ne pas fausser les calculs
	document.getElementsByName("valeurEmployeurVar")[0].value = "";
	document.getElementsByName("valeurReadOnly")[0].value = "";
	document.getElementsByName("valeurEmployeVar")[0].value = "";
}


</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
					Taux assurance - D&eacute;tail
					<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
						<TR> 
						<TD> 
						<TABLE border="0" cellspacing="0" cellpadding="0" width="100%">
							<TBODY> 
								<TR> 
									<TD width="121">&nbsp;</TD>
								</TR>
								<TR> 
									<TD nowrap width="121" height="35">Assurance</TD>
									<TD nowrap width="29"></TD>
									<TD nowrap colspan="4"> 
										<INPUT type="hidden" name="selectedId" value='<%=viewBean.getTauxAssuranceId()%>'>
										<INPUT type="hidden" name="assuranceId" value="<%=viewBean.getAssuranceId()%>">
										<INPUT type="text" name="assuranceLibelle" size="35" maxlength="35"  value="<%=viewBean.getAssurance().getAssuranceLibelle()%>" class="Disabled" tabindex="-1" readonly><BR>
										<INPUT type="text" name="assuranceLibelleCourt" size="35" maxlength="35" value="<%=viewBean.getAssurance().getAssuranceLibelleCourt()%>" class="Disabled" tabindex="-1" readonly>
									</TD>
								</TR>
								<TR> 
									<TD nowrap width="121" height="31">Canton</TD>
									<TD nowrap width="29"></TD>
									<TD nowrap colspan="3"> 
										<INPUT type="text" name="assuranceCanton" size="35" maxlength="35" value="<%=globaz.naos.translation.CodeSystem.getLibelle(session, viewBean.getAssurance().getAssuranceCanton())%>" class="Disabled" tabindex="-1" readonly>
									</TD>
								</TR>
								<TR> 
									<TD width="121">&nbsp; </TD>
								</TR>
								<TR> 
									<TD nowrap  height="11" colspan="7"> 
										<HR size="3" width="100%">
									</TD>
								</TR>
								<TR> 
									<TD width="121" height="31">Genre</TD>
									<TD height="14" width="29"></TD>
									<TD height="31"> 	
									
									<%	if (viewBean.getSaisieGenreValeur().equals(globaz.naos.db.tauxAssurance.AFTauxAssurance.SAISIE_LIBRE)) { %>
										<ct:FWCodeSelectTag 
											name="genreValeur" 
											defaut="<%=viewBean.getGenreValeur()%>"
											codeType="VEGENVALTA"/>
										<script>
											document.getElementsByName('genreValeur')[0].onchange = function() {onChangeGenre();};
										</script>
									<!--select name="genreValeur" onChange="onChangeGenre();">
										<=globaz.naos.translation.CodeSystem.getOption(session, viewBean.getGenreValeur(), "VEGENVALTA")>
									</select-->	
									<%	} else { %>
									<INPUT type="hidden" name="genreValeur" value="<%=viewBean.getGenreValeur()%>" >
									<INPUT type="text" name="genreValeurLibelle"  size="15" 
										value="<%=globaz.naos.translation.CodeSystem.getLibelle(session, viewBean.getGenreValeur())%>" tabindex="-1" class="Disabled" readonly>
									<%	} %>								
									</TD>
								</TR>
								<TR> 
									<TD width="121" height="31">Sexe</TD>
									<TD width="29"></TD>
									<TD> 
										<%	if (viewBean.getSaisieSexe().equals(globaz.naos.db.tauxAssurance.AFTauxAssurance.SAISIE_LIBRE)) { %>
										<ct:FWCodeSelectTag 
											name="sexe" 
											defaut="<%=viewBean.getSexe()%>"
											codeType="PYSEXE"
											wantBlank="true"/> 
										<%	} else { %>
										<INPUT type="text" name="sexe" size="7" value="" class="Disabled" tabindex="-1" readonly>	
										<%  } %>
									</TD>
								</TR>
								<TR> 
									<TD nowrap width="121" height="31" >Date de d&eacute;but</TD>
									<TD width="29" align="right"></TD>
									<TD nowrap > 
										<ct:FWCalendarTag name="dateDebut" value="<%=viewBean.getDateDebut()%>" /> 
									</TD>
								</TR>
								<TR> 
									<TD nowrap width="121">&nbsp;</TD>
								</TR>
								</TBODY> 
							</TABLE>
							<TABLE border="0" cellspacing="0" cellpadding="0" id="taux">
								<TBODY> 
								<TR> 
									<TD nowrap width="120" height="31">Taux Employeur</td>
									<TD nowrap width="30"></td>
									<TD nowrap width="105"> 
										<INPUT name="valeurEmployeur" align="right" type="text" size="11" 
											value="<%=viewBean.getValeurEmployeur()%>" style="text-align : right;" onchange="validateFloatNumber(this, 5); onChangePart();" onkeypress="return filterCharForFloat(window.event);">
									</TD>
									<TD nowrap width="170" rowspan="2"> 
										/
										<INPUT name="fraction" align="right" type="text" size="11" 
											value="<%=globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getFraction())?"100":viewBean.getFraction()%>" style="text-align : right;" onchange="changeFractionTotal(this); validateIntegerNumber(this); onChangePart();" onkeypress="return filterCharForInteger(window.event);">
									</TD>
									<TD nowrap width="100" rowspan="2">Taux Total</td>
									<TD width="30" rowspan="2"></TD>
									<TD nowrap rowspan="2"> 
										<INPUT name="valeurReadOnly2" align="right" type="text" size="11" 
											value="<%=viewBean.getValeurTotal()%>" style="text-align : right;" tabindex="-1" onchange="validateFloatNumber(this, 5); onChangeTotal();" onkeypress="return filterCharForFloat(window.event);">
										/ 
										<INPUT name="fractionReadOnly2" align="right" type="text" size="11" 
											value="<%=globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getFraction())?"100":viewBean.getFraction()%>" style="text-align : right;" class="libelleDisabled" tabindex="-1" readonly>
									</TD>
									<TD width="0" rowspan="2" ></TD>
								</TR>
								<TR> 
									<TD nowrap width="120" height="31">Taux Employ&eacute;</TD>
									<TD nowrap width="30"></TD>
									<TD nowrap colspan="6"> 
										<INPUT name="valeurEmploye" align="right" type="text" size="11" 
											value="<%=viewBean.getValeurEmploye()%>" style="text-align : right;" onchange="validateFloatNumber(this, 5); onChangePart();" onkeypress="return filterCharForFloat(window.event);">
									</TD>
								</TR>
								<TR> 
									<TD colspan="8">&nbsp;</TD>
								</TR>
								<TR> 
									<TD colspan="8" height="31">&nbsp;</TD>
								</TR>
								<TR> 
									<TD colspan="8">&nbsp;</TD>
								</TR>
								</TBODY> 
							</TABLE>
							<TABLE border="0" cellspacing="0" cellpadding="0" id="tauxVariable">
								<TBODY> 
								<TR> 
									<TD nowrap width="120" height="31">Taux Employeur</TD>
									<TD nowrap width="30"></TD>
									<TD nowrap width="105"> 
										<INPUT name="valeurEmployeurVar" align="right" type="text" size="11" 
											value="<%=viewBean.getValeurEmployeur()%>" style="text-align : right;" onchange="validateFloatNumber(this, 5); onChangePartVar();" onkeypress="return filterCharForFloat(window.event);">
									</TD>
									<TD nowrap width="170" rowspan="2"> 
										/ 
										<INPUT name="fractionVar" align="right" type="text" size="11" 
											value="<%=viewBean.getFraction()%>" style="text-align : right;" onchange="validateIntegerNumber(this); onChangePartVar(); changeFractionTotal2(this);" onkeypress="return filterCharForInteger(window.event);">
									</TD>
									<TD nowrap width="100" rowspan="2">Taux Total</TD>
									<TD width="30" rowspan="2"></TD>
									<TD nowrap rowspan="2"> 
										<INPUT name="valeurReadOnly" align="right" type="text" size="11" 
											value="<%=viewBean.getValeurTotal()%>" style="text-align : right;" tabindex="-1" onchange="validateFloatNumber(this, 5); onChangeTotalVar();" onkeypress="return filterCharForFloat(window.event);">
										/ 
										<INPUT name="fractionReadOnly" align="right" type="text" size="11" 
											value="<%=viewBean.getFraction()%>" style="text-align : right;" class="libelleDisabled" tabindex="-1" readonly>
									</TD>
									<TD width="0" rowspan="2" ></TD>
								</TR>
								<TR> 
									<TD nowrap width="120" height="31">Taux Employ&eacute;</TD>
									<TD nowrap width="30"></TD>
									<TD nowrap width="105"> 
										<INPUT name="valeurEmployeVar" align="right" type="text" size="11" 
											value="<%=viewBean.getValeurEmploye()%>" style="text-align : right;" onchange="validateFloatNumber(this, 5); onChangePartVar();" onkeypress="return filterCharForFloat(window.event);">
									</TD>
								</TR>
								<TR> 
									<TD nowrap width="120">&nbsp;</TD>
								</TR>
								<TR> 
									<TD nowrap width="120" height="31">Rang</TD>
									<TD nowrap width="30"></TD>
									<TD nowrap> 
										<INPUT name="rang" align="right" type="text" size="2" maxlength="2" value="<%=viewBean.getRang()%>" style="text-align : right;">
									</TD>
									<TD nowrap ></TD>
									<TD nowrap width="100">Tranche</TD>
									<TD width="30"></TD>
									<TD nowrap width="174" > 
										<INPUT name="tranche" align="right" type="text" size="11" value="<%=viewBean.getTranche()%>" style="text-align : right;">
									</TD>
								</TR>
								<TR> 
									<TD nowrap width="120">&nbsp;</TD>
								</TR>
								</TBODY> 
							</TABLE>
							<TABLE border="0" cellspacing="0" cellpadding="0" id="montant">
								<TBODY> 
								<TR> 
									<TD nowrap width="120" height="31">Montant Employeur</TD>
									<TD nowrap width="30"></TD>
									<TD nowrap width="200"> 
										<INPUT name="valeurEmployeurMon" align="right" type="text" size="11" 
											value="<%=viewBean.getValeurEmployeur()%>" style="text-align : right;" onchange="validateFloatNumber(this, 5); onChangePartMontant();" onkeypress="return filterCharForFloat(window.event);">
									</TD>
									<TD nowrap width="100" rowspan="2">Montant Total</TD>
									<TD width="30" rowspan="2"></TD>
									<TD nowrap rowspan="2" width="173"> 
										<INPUT name="valeurReadOnly3" align="right" type="text" size="11" 
											value="<%=viewBean.getValeurTotal()%>" style="text-align : right;" tabindex="-1" onchange="validateFloatNumber(this, 5); onChangeTotalMontant();" onkeypress="return filterCharForFloat(window.event);">
									</TD>
									<TD width="0" rowspan="2"></TD>
								</TR>
								<TR> 
									<TD nowrap width="120" height="31">Montant Employ&eacute;</TD>
									<TD nowrap width="30"></TD>
									<TD nowrap width="200"> 
										<INPUT name="valeurEmployeMon" align="right" type="text" size="11" 
											value="<%=viewBean.getValeurEmploye()%>" style="text-align : right;" onchange="validateFloatNumber(this, 5); onChangePartMontant();" onkeypress="return filterCharForFloat(window.event);">
									</TD>
								</TR>
								<TR> 
									<TD nowrap width="120">&nbsp;</TD>
								</TR>
								<TR> 
									<TD nowrap width="120" height="31">&nbsp;</TD>
									<TD nowrap width="30"></TD>
									<TD nowrap width="200">&nbsp; </TD>
									<TD nowrap width="100">P&eacute;riodicit&eacute;</TD>
									<TD width="30"></TD>
									<TD nowrap width="173">
										<ct:FWCodeSelectTag 
											name="periodiciteMontant"
											defaut="<%=viewBean.getPeriodiciteMontant()%>"
											codeType="VEPERIODIC"
											except="<%=viewBean.getExceptPeriodicite()%>"/> 
									</TD>
									
								</TR>
								<TR> 
									<TD nowrap width="120">&nbsp;</TD>
								</TR>
								</TBODY> 
							</TABLE>
							<TABLE border="0" cellspacing="0" cellpadding="0" id="type">
								<TR>
									<TD>
										<TABLE border="0" cellspacing="0" cellpadding="0">
											<TR> 
												<TD nowrap width="150" height="31">Type</TD>
												<TD nowrap width="275">
													<ct:FWCodeSelectTag 
														name="typeId" 
														defaut="<%=viewBean.getTypeId()%>"
														codeType="VETYPETAUX"/>
													<script>
														document.getElementsByName('typeId')[0].onchange = function() {onChangeType();};
													</script>
						</TD>
					</TR>
										</TABLE>
									</TD>
									<TD>
										<TABLE border="0" cellspacing="0" cellpadding="0" id="categorie">
											<TR>
												<TD nowrap width="130">Catégorie</TD>
												<TD nowrap>
													<ct:FWCodeSelectTag 
														name="saisieCategorie" 
														defaut="<%=viewBean.getSaisieCategorie()%>"
														codeType="VECATETAUX"/>
												</TD>
											</TR>
										</TABLE>
										<TABLE border="0" cellspacing="0" cellpadding="0" id="caisse">
											<TR>
												<TD nowrap width="130">Caisse</TD>
												<TD nowrap>
									    			<%
														Object[] caisseMethods = new Object[] {
															new String[]{"setCategorieId", "getIdTiersAdministration"},
															new String[]{"setSaisieCodeAdministration", "getCodeAdministration"}
														};
														Object[] caisseParams = new Object[] {
															new String[]{"forGenreAdministration", "selGenre"}
														};
								              		%>
								              		<INPUT type="text" name="saisieCodeAdministration" value="<%=viewBean.getSaisieCodeAdministration()%>">
								              		<INPUT type="hidden" name="forGenreAdministration" value="509028">
													<ct:FWSelectorTag 
														name="caisseSelector" 
														
														methods="<%=caisseMethods%>"
														providerApplication ="pyxis"
														providerPrefix="TI"
														providerAction ="pyxis.tiers.administration.chercher"
														providerActionParams="<%=caisseParams%>"/> 
												</TD>
											</TR>
										</TABLE>
										<TABLE border="0" cellspacing="0" cellpadding="0" id="canton">
											<TR>
												<TD nowrap width="130">Canton</TD>
												<TD nowrap>
													<ct:FWCodeSelectTag 
														name="saisieCanton" 
														defaut="<%=viewBean.getSaisieCanton()%>"
														codeType="PYCANTON"/>
												</TD>
											</TR>
										</TABLE>
									</TD>
								</TR>
							</TABLE>
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
<ct:menuChange displayId="menu" menuId="AFMenuPrincipal" checkAdd="no"/>
	<ct:menuChange displayId="options" menuId="AFOptionsAssurances" showTab="options" checkAdd="no">
		<ct:menuSetAllParams key="assuranceId" value="<%=viewBean.getAssuranceId()%>" checkAdd="no"/>
	</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>