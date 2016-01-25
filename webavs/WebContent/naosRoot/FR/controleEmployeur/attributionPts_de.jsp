<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java"
	errorPage="/errorPage.jsp" import="globaz.globall.http.*"
	contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>

<%@ include file="/theme/detail/header.jspf"%>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss"%>
<!-- Creer l'enregistrement si il n'existe pas -->
<%@ taglib uri="/WEB-INF/naos.tld" prefix="naos"%>
<%
idEcran = "CAF2009";
AFAttributionPtsViewBean viewBean = (AFAttributionPtsViewBean) request.getAttribute("viewBean");
if(viewBean == null){
	 viewBean = (AFAttributionPtsViewBean) session.getAttribute("viewBean");
}
bButtonCancel = false;
String jspLocation = servletContext + mainServletPath + "Root/affilie_select.jsp";
int autoDigiAff = globaz.naos.util.AFUtil.getAutoDigitAff(session);
%>
<%
	String MSG_PROCESS_OK = "The process successfully started.";
	if ("FR".equalsIgnoreCase(languePage)) {
		MSG_PROCESS_OK = "La tâche a démarré avec succès.";
	} else if ("DE".equalsIgnoreCase(languePage)) {
		MSG_PROCESS_OK = "Prozess erfolgreich gestartet.";
	}
%>

</style>
<%@page import="globaz.globall.util.JAUtil"%>
<%@page import="java.util.Vector"%>
<%@page import="globaz.naos.db.controleEmployeur.AFAttributionPts"%>
<%@page
	import="globaz.naos.db.controleEmployeur.AFAttributionPtsViewBean"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<SCRIPT language="JavaScript">

</SCRIPT>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf"%>

<%-- tpl:put name="zoneScripts" --%>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<SCRIPT language="JavaScript"
	src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript"
	src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript"
	src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript"
	src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<SCRIPT language="JavaScript"
	src="<%=request.getContextPath()%>/scripts/swap.js"></SCRIPT>
<SCRIPT language="JavaScript"
	src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<SCRIPT language="JavaScript">

function updateNumAffilie(tag){
	if(tag.select && tag.select.selectedIndex != -1){
		document.getElementById('numAffilie').value =  tag.select[tag.select.selectedIndex].value;
		parent.fr_main.location.href ="<%=(servletContext + mainServletPath)%>?userAction=naos.controleEmployeur.attributionPts.afficherDerniere&numAffilie=" + document.getElementById('numAffilie').value;
	}	
}

function add() {
}

function upd() {
}


function validate() {
	document.forms[0].elements('userAction').value="naos.controleEmployeur.attributionPts.enregistrer";
	return true;
}

function cancel() {
}

function del() {
}

function init(){
	document.forms[0].elements('_method').value ="upd";
	calculNombrePts();
	calculCategorie();
	calculDelaiControle();
}

function postInit(){
	document.forms[0].elements("dateEvaluation").tabIndex= "-1";
}


function trouverAdresse(){
		 parent.fr_main.location.href ="<%=(servletContext + mainServletPath)%>?userAction=naos.beneficiairepc.impression.afficher&numAffilie=" + document.forms[0].elements('numAffilie').value;
}

function updateForm(tag){
	field = document.forms[0].elements('nomPrenom');
	if (tag.select) {
		nom = tag.select[tag.select.selectedIndex].nom;
		field.value = nom;
		if(field.readOnly==false) {
			field.readOnly = true;
			field.className = 'disabled';
			field.tabIndex = -1;						
		}
		document.dernieresEcritures.location.href='<%=request.getContextPath()%>/pavoRoot/lastentries.jsp?compteIndividuelId='+tag.select[tag.select.selectedIndex].idci;
	}
}

function calculNombrePts(){
	var total = 0;
	if(document.forms[0].elements("derniereRevision").value == 851001){
		total = total + 0;
	}else if(document.forms[0].elements("derniereRevision").value == 851002){
	    total = total + 3;
	}else if(document.forms[0].elements("derniereRevision").value == 851003){
	    total = total + 9;	
	}	
	
	if(document.forms[0].elements("qualiteRH").value == 851004){
		total = total + 0;
	}else if(document.forms[0].elements("qualiteRH").value == 851005){
		total = total + 1;
	}else if(document.forms[0].elements("qualiteRH").value == 851006){
		total = total + 2;
	}else if(document.forms[0].elements("qualiteRH").value == 851007){
		total = total + 3;
	}
	
	if(document.forms[0].elements("collaboration").value == 851008){
		total = total + 0;
	}else if(document.forms[0].elements("collaboration").value == 851009){
		total = total + 1;
	}else if(document.forms[0].elements("collaboration").value == 851010){
		total = total + 2;
	}else if(document.forms[0].elements("collaboration").value == 851011){
		total = total + 3;
	}
	
	if(document.forms[0].elements("criteresEntreprise").value == 851012){
		total = total + 0;
	}else if(document.forms[0].elements("criteresEntreprise").value == 851013){
		total = total + 1;
	}else if(document.forms[0].elements("criteresEntreprise").value == 851014){
		total = total + 2;
	}else if(document.forms[0].elements("criteresEntreprise").value == 851015){
		total = total + 3;
	}
	
	document.forms[0].elements("nbrePts").value = total;
	document.forms[0].elements("nbrePoints").value = total;
}

function calculDelaiControle(){
	calculNombrePts();
	var categorie = document.forms[0].elements("categorie").value; 
	var nbrePoints = document.forms[0].elements("nbrePoints").value;
	if(categorie == 0){
		document.forms[0].elements("delaiControle").value = 'D0002';
	}else if(categorie == 1){
		document.forms[0].elements("delaiControle").value = 'D0005';
	}else if(categorie == 2){
		if(nbrePoints <= 5){
			document.forms[0].elements("delaiControle").value = '9';
		}else if((5<nbrePoints)&&(nbrePoints<=10)){
			document.forms[0].elements("delaiControle").value = '7';
		}else if(nbrePoints>10){
			document.forms[0].elements("delaiControle").value = '5';
		}
	}else if(categorie == 3){
		if(nbrePoints <= 3){
			document.forms[0].elements("delaiControle").value = '9';
		}else if((3<nbrePoints)&&(nbrePoints<=8)){
			document.forms[0].elements("delaiControle").value = '7';
		}else if(nbrePoints>8){
			document.forms[0].elements("delaiControle").value = '5';
		}
	}else if(categorie == 4){
		if(nbrePoints <= 4){
			document.forms[0].elements("delaiControle").value = '7';
		}else if(nbrePoints>4){
			document.forms[0].elements("delaiControle").value = '5';
		}
	} 
	calculPeriode();
}

function calculPeriode(){
var total = 0;
//var annee = document.forms[0].elements("anneeControle").value;
var date = document.forms[0].elements("finPeriodeControle").value;
var dateFinAff = document.forms[0].elements("anneeFinAffiliation").value;
var annee = 0;
if(date.length <= 0){
	annee = document.forms[0].elements("anneeDebutAffiliation").value;
}else{
	annee = parseInt(date.substring(6,10)) +1;
}
var delai = document.forms[0].elements("delaiControle").value;
var total = parseInt(annee) + parseInt(delai) - 1;
	if(annee > 0 && !dateFinAff.length >0){
		document.forms[0].elements("periodePrevueDebut").value = '01.01.'+parseInt(annee);
		if(document.forms[0].elements("delaiControle").value.length > 0){
			if(isNaN(document.forms[0].elements("delaiControle").value)){
			}else{
				document.forms[0].elements("periodePrevueFin").value = '31.12.'+total;
				document.forms[0].elements("anneePrevue").value = parseInt(annee) + parseInt(delai);
			}
		}
	}
}

function calculCategorie(){
var cumulMasse = document.forms[0].elements("masseSalariale").value;
var PALIER_MASSE_SAL1 = 0.0;
var PALIER_MASSE_SAL2 = 100000.0;
var PALIER_MASSE_SAL3 = 500000.0;
var PALIER_MASSE_SAL4 = 5000000.0;
	if(cumulMasse <= PALIER_MASSE_SAL1){
			document.forms[0].elements("categorie").value = 0;
		}else if((PALIER_MASSE_SAL2 > cumulMasse) && (cumulMasse > PALIER_MASSE_SAL1)){
			document.forms[0].elements("categorie").value = 1;
		}else if((PALIER_MASSE_SAL3 > cumulMasse) && (cumulMasse >= PALIER_MASSE_SAL2)){
			document.forms[0].elements("categorie").value = 2;
		}else if((PALIER_MASSE_SAL4 > cumulMasse) && (cumulMasse >= PALIER_MASSE_SAL3)){
			document.forms[0].elements("categorie").value = 3;
		}else if(cumulMasse > PALIER_MASSE_SAL4){
			document.forms[0].elements("categorie").value = 4;
		}else{
			document.forms[0].elements("categorie").value = 0;
	}
}


</SCRIPT>


<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf"%>
<%-- tpl:put name="zoneTitle" --%>
Evaluation du risque pour le contrôle d'employeur
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf"%>
<%-- tpl:put name="zoneMain" --%>
<%try{ %>
<TR>
	<td>N° affili&eacute;</td>
	<td><ct:FWPopupList name="numAffilie"
		value="<%=viewBean.getNumAffilie()%>" className="libelle"
		jspName="<%=jspLocation%>" autoNbrDigit="<%=autoDigiAff%>" size="25"
		onChange="updateNumAffilie(tag);" minNbrDigit="6" /> <!--IMG
											src="<%=servletContext%>/images/down.gif"
											alt="presser sur la touche 'flèche bas' pour effectuer une recherche"
											title="presser sur la touche 'flèche bas' pour effectuer une recherche"
											onclick="if (document.forms[0].elements('numAffilie').value != '') numAffiliePopupTag.validate();"-->
	<%
		Object[] tiersMethodsName = new Object[] { 
					new String[] { "setNumAffilie", "getNumAffilieActuel" }, 
					new String[] { "setIdTiers", "getIdTiers" }, 
					};
		Object[] tiersParams = new Object[] { new String[] { "selection", "_pos" }, };
		String redirectUrl = ((String) request.getAttribute("mainServletPath") + "Root") + "/" + globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session) + "/controleEmployeur/attributionPts_de.jsp";
	%> <ct:FWSelectorTag name="tiersSelector"
		methods="<%=tiersMethodsName%>" providerApplication="pyxis"
		providerPrefix="TI" providerAction="pyxis.tiers.tiers.chercher"
		providerActionParams="<%=tiersParams%>" redirectUrl="<%=redirectUrl%>" />
	<input type="hidden" value="" name="idTiers">
	</td>
	<td><input type="text" name="nomAffilie" disabled="disabled"
		readonly="readonly" value="<%=viewBean.getNom()%>"></input></td>
</TR>
<tr><td><hr></hr></td></tr>
						<tr>
							<TD align="left">Dernière période de contrôle</TD>
							<TD>
							<INPUT name="debutPeriodeControle" class="libelleCourt" value="<%=viewBean.getDateDebutControle()%>" readonly="readonly" disabled="disabled">
							 -
							<INPUT name="finPeriodeControle" class="libelleCourt" value="<%=viewBean.getDateFinControle()%>" readonly="readonly" disabled="disabled">
							/
							<INPUT onchange="isZero();" name="anneeControle" class="numeroCourt" value="<%=viewBean.getAnneePrecedente()%>" readonly="readonly" disabled="disabled">
							</TD>
						</tr>
						<tr><td><hr></hr></td></tr>
						<tr>
							<TD align="left">Résultat de la dernière révision</TD>
						</tr>
						<tr>
							<TD>
							<ct:FWCodeSelectTag name="derniereRevision"
							defaut="<%=viewBean.getDerniereRevision()%>"
							wantBlank="<%=false%>"
			        		codeType="AFCEDERNRE"
			        		/>
							</TD>
							<SCRIPT language="javascript">	    				        	
								document.forms[0].elements("derniereRevision").onchange= new Function ("","return calculDelaiControle();");
							</SCRIPT>
							<td>
							<textarea name="derniereRevisionCom" cols="50" rows="3"><%=viewBean.getDerniereRevisionCom()%></textarea>
							</td>
						</tr>
						<tr>
							<TD align="left">Qualité ressources humaines</TD>
						</tr>
						<tr>
							<TD align="left">
							<ct:FWCodeSelectTag name="qualiteRH"
							defaut="<%=viewBean.getQualiteRH()%>"
							wantBlank="<%=false%>"
			        		codeType="AFCEQUALRH"/>
							</TD>
							<SCRIPT language="javascript">	    				        	
								document.forms[0].elements("qualiteRH").onchange= new Function ("","return calculDelaiControle();");
							</SCRIPT>
							<td>
							<TEXTAREA rows=3 COLS=50 name="qualiteRHCom"><%=viewBean.getQualiteRHCom()%></TEXTAREA>
							</td>
						</tr>
						<tr>
							<TD align="left">Collaboration</TD>
						</tr>
						<tr>
							<TD align="left">
							<ct:FWCodeSelectTag name="collaboration"
							defaut="<%=viewBean.getCollaboration()%>"
							wantBlank="<%=false%>"
			        		codeType="AFCECOLLAB"/>
							</TD>
							<SCRIPT language="javascript">	    				        	
								document.forms[0].elements("collaboration").onchange= new Function ("","return calculDelaiControle();");
							</SCRIPT>
							<td>
								<TEXTAREA rows=3 COLS=50 name="collaborationCom"><%=viewBean.getCollaborationCom()%></TEXTAREA>
							</td>
						</tr>
						<tr>
							<TD align="left">Critères spécifiques entreprise</TD>
						</tr>
						<tr>
							<TD align="left">
							<ct:FWCodeSelectTag name="criteresEntreprise"
							defaut="<%=viewBean.getCriteresEntreprise()%>"
							wantBlank="<%=false%>"
			        		codeType="AFCECRIENT"/>
							</TD>
							<SCRIPT language="javascript">	    				        	
								document.forms[0].elements("criteresEntreprise").onchange= new Function ("","return calculDelaiControle();");
							</SCRIPT>
							<td>
							<TEXTAREA rows=3 COLS=50 name="criteresEntrepriseCom"><%=viewBean.getCriteresEntrepriseCom()%></TEXTAREA>
							</td>
						</tr>
						<INPUT name="periodeDebut" type="hidden" value="<%=viewBean.getDateDebutControle()%>">
						<INPUT name="periodeFin" type="hidden" value="<%=viewBean.getDateFinControle()%>">
						<tr><td><hr></hr></td></tr>
						<tr>
							<TD align="left">Points de risque</TD>
							<TD align="left">
							<INPUT name="nbrePts" class="numeroCourt" readonly="true" disabled="true" align="center"/>
							<input type="hidden" name="nbrePoints"/>
							</TD>
							<TD align="left">CVS</TD>
							<TD align="left">
							<INPUT onchange="calculCategorie();calculDelaiControle();" name="categorie" class="numeroCourt" readonly="true" disabled="true" align="center" value="<%=viewBean.getCategorieLibelle(viewBean.getAnneePrecedente())%>"/>
							<INPUT onchange="calculCategorie();calculDelaiControle();" name="masseSalariale" class="numeroLong" <%if(viewBean.getMasseSalariale(viewBean.getAnneePrecedente()).doubleValue() > 0){%>readonly="true" disabled="true" <%}%> align="center" value="<%=viewBean.getMasseSalarialeLibelle(viewBean.getAnneePrecedente())%>"/>
							</TD>
						</tr>
						<tr>
							<TD align="left">Délai de contrôle calculé</TD>
							<TD align="left"><INPUT name="delaiControle" class="numeroCourt" readonly="true" disabled="true" align="center"/></TD>
						</tr>
						<tr>
							<TD align="left">Période / Année prévue</TD>
							<TD align="left">
							<INPUT name="periodePrevueDebut" value="<%=viewBean.getPeriodePrevueDebut()%>" class="libelleCourt" readonly="true" disabled="true" align="center"/>
							-
							<INPUT name="periodePrevueFin" value="<%=viewBean.getPeriodePrevueFin()%>" class="libelleCourt" readonly="true" disabled="true" align="center"/>
							/
							<INPUT name="anneePrevue" class="numeroCourt" readonly="true" disabled="true" align="center"/>
							</TD>
						</tr>
						<tr>
							<TD align="left">Agendé manuellement</TD>
							<TD align="left">
							<ct:FWCodeSelectTag name="prevuManuellement"
							defaut="<%=viewBean.getPrevuManuellement()%>"
							wantBlank="<%=false%>"
			        		codeType="AFCEAGEMAN"/>
							</TD>
						</tr>
						<tr>
							<TD align="left">Motif de la mutation</TD>
							<td>
							<TEXTAREA rows=3 COLS=50 name="prevuManuellementCom"><%=viewBean.getPrevuManuellementCom()%></TEXTAREA>
							</td>
						</tr>
						<tr><td><hr></hr></td></tr>
						<tr>
						<td>Commentaires</td>
						<td>
							<TEXTAREA rows=3 COLS=50 name="commentaires"><%=viewBean.getCommentaires()%></TEXTAREA>
							</td>
						</tr>
						<tr>
						<td>Observations générales</td>
						<td>
						<TEXTAREA rows=3 COLS=50 name="observations"><%=viewBean.getObservations()%></TEXTAREA>
						</td>
						</tr>
						<tr>
						<td>Nb écritures reprises</td>
						<td><INPUT name="nbreEcritures" class="numeroCourt" value="<%=viewBean.getNbreEcritures()%>" /></td>
						</tr>
						<tr>
						<td>Cas spécial</td>
						<%if(viewBean.getIsCasSpecial() != null){%>
							<td><INPUT type="checkbox" name="isCasSpecial2" <%=(viewBean.getIsCasSpecial().booleanValue())? "checked" : "unchecked"%>/></td>
						<%}else{%>
							<td><INPUT type="checkbox" name="isCasSpecial2" /></td>
						<%}%>
						</tr>
						<tr>
						<td>Genre contrôle</td>
						<td>
							<%=viewBean.getGenreControle()%>
						</td>
						</tr>
						<tr>
						<td>Masse AVS</td>
						<td><INPUT name="masseAvs" value="<%=viewBean.getMasseAvs()%>" /></td>
						<td>Masse AF</td>
						<td><INPUT name="masseAF" value="<%=viewBean.getMasseAF()%>" /></td>
						</tr>
						<tr>
						<td>Masse AC</td>
						<td><INPUT name="masseAC" value="<%=viewBean.getMasseAC()%>" /></td>
						<td>Autres</td>
						<td><INPUT name="masseAutres" value="<%=viewBean.getMasseAutres()%>" /></td>
						</tr>
</tr>
<%}catch(Exception ex){ex.printStackTrace();}	%>

<!--

//-->
</script>
<input type="hidden" name="anneeDebutAffiliation" value="<%=viewBean.getAnneeDebutAffiliation()%>"/>
<input type="hidden" name="anneeFinAffiliation" value="<%=viewBean.getAnneeFinAffiliation()%>"/>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf"%>
<%-- tpl:put name="zoneButtons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf"%>
<%-- tpl:put name="zoneEndPage" --%>
<ct:menuChange displayId="menu" menuId="AFMenuPrincipal"/>
<ct:menuChange displayId="options" menuId="AFOptionsControlEmployeurAttributionPts" showTab="options">
		<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getNumAffilie()%>"/>
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf"%>
<%-- /tpl:insert --%>