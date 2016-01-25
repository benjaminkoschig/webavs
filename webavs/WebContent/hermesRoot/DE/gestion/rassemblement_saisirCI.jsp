<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>

<style type="text/css">
.hiddenPage {
	visibility: hidden;
	display: none;
}

.visible {
	visibility: visible;
	display: inline;
}

.selected {
	text-decoration: underline;
	cursor: default;
	font-weight: bold;
}

.notselected {
	text-decoration: none;
	cursor: hand;
	font-weight: normal;
}
</style>

	<%	globaz.hermes.db.gestion.HERassemblementViewBean viewBean = (globaz.hermes.db.gestion.HERassemblementViewBean)session.getAttribute("viewBean");
  
		String jspLocation = servletContext + mainServletPath + "Root/ci_select.jsp";
		String jspLocation2 = servletContext + mainServletPath + "Root/ti_select.jsp";
		String fieldDisable = "class='disabled' readonly tabindex='-1'";
		boolean modeExtourne = "extourne".equals(request.getParameter("modeAjout"))?true:false;
		String formState = fieldDisable;
		boolean isAdd = ((request.getParameter("_method")!=null)&&(request.getParameter("_method").equals("add")));
		bButtonCancel = false;
		idEcran="GAZ0022";
		
		boolean isNSSdisplay = globaz.hermes.utils.HEUtil.isNNSSdisplay(viewBean.getSession());
		
		
	%>
<SCRIPT language="JavaScript">
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

function postInit()
{
	document.getElementById('partnerNumNssPrefixe').style.visibility='hidden';
	document.getElementById('partnerNumNssPrefixe').style.display='none';
}

function confirmeMoisVide(){

	if(document.forms(0).all('moisDebut').value=="" || document.forms(0).all('moisFin').value=="")
	{
		if(confirm("<%=	viewBean.getSession().getLabel("HERMES_10000")%>"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	else
	{
		return true;
	}
}

function resetNom() {
	field = document.forms[0].elements('nomPrenom');
	field.value = '';
	field.readOnly = false;
	field.className = 'libelle';
	field.tabIndex = 0;
}

function selectPartner() {
	document.getElementById('partner').className = 'selected';
	document.getElementById('mitglieder').className = 'notselected';	
	
	resetEmployeurPartenaire();
	document.getElementById('partialmitgliedNum').className = 'hiddenPage';
	document.getElementById('partialpartnerNum').className = 'visible';
	
	document.getElementById('mitgliedNumNssPrefixe').style.visibility='hidden';
	document.getElementById('mitgliedNumNssPrefixe').style.display='none';
	
	<%if (isNSSdisplay)
	{	
	%>
	document.getElementById('partnerNumNssPrefixe').style.visibility='visible';
	document.getElementById('partnerNumNssPrefixe').style.display='inline';
	<%}%>
	
}
function selectMitglieder() {
	document.getElementById('partner').className = 'notselected';
	document.getElementById('mitglieder').className = 'selected';
	

	resetEmployeurPartenaire();	
	document.getElementById('partialmitgliedNum').className = 'visible';
	document.getElementById('partialpartnerNum').className = 'hiddenPage';
	
	document.getElementById('partnerNumNssPrefixe').style.visibility='hidden';
	document.getElementById('partnerNumNssPrefixe').style.display='none';
	<%if (isNSSdisplay)
	{	
	%>
	document.getElementById('mitgliedNumNssPrefixe').style.visibility='visible';
	document.getElementById('mitgliedNumNssPrefixe').style.display='inline';
	<%}%>
		
}

function updateEmployeurPartenaire(tag) {
	if (tag.select) {
		document.getElementById('numeroDetailInv').value = tag.select[tag.select.selectedIndex].nom;
		document.getElementById('employeurPartenaire').value = tag.select[tag.select.selectedIndex].value;
		document.getElementById('lieuEmployeur').value = tag.select[tag.select.selectedIndex].lieu;
		if(document.getElementById('mitgliedNum').className == 'visible'){
			document.getElementById('brancheEco').value = tag.select[tag.select.selectedIndex].brancheeco;
		}	
	}
}

function resetEmployeurPartenaire() {
	document.getElementById('partnerNum').value='';
	document.getElementById('mitgliedNum').value='';
	document.getElementById('numeroDetailInv').value='';
	document.getElementById('employeurPartenaire').value='';
	document.getElementById('lieuEmployeur').value = '';
	document.getElementById('brancheEco').value = '';	
}

function setNotFound(tag){
	document.getElementById('lieuEmployeur').value = '';
	document.getElementById('brancheEco').value = '';
	document.getElementById('numeroDetailInv').value='';
	tag.select = false;
}
</SCRIPT>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
function add() {
}
function upd() {
}
function validate() {
	document.forms[0].elements('userAction').value = "hermes.gestion.genererCIPapier.executer";
  	return true;
}
function cancel() {
	if (document.forms[0].elements('_method').value == "add")
  document.forms[0].elements('userAction').value="back";
 else
  document.forms[0].elements('userAction').value="hermes.gestion.rassemblement.chercher";	
}
function del() {
}
function init() { 
<%if ("add".equals(request.getParameter("_method"))) {%>
	var elemCount = 3;
	var elementsToInit = new Array("gre", "moisDebut", "moisFin");
	var initValues = new Array("01", "01", "12");
	for (var i = 0; i < elemCount; i++) {
		var currElement = document.all[elementsToInit[i]];
		if (currElement.value == "") {
			currElement.value = initValues[i];
		}
	}
<%}%>
}
function AddEcritureCI(){ 
    document.forms[0].elements('userAction').value="hermes.gestion.rassemblement.ajouterEcriture"
	document.forms[0].submit();
}


</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Manuelle Erfassung des erwartenden IK-Auszuges<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<tr>
						<td>Versicherte</td>
						<td colspan="4">
						<input name='Motif' type="hidden" value='<%=viewBean.getMotif()%>'>
						<input name='numAvs' size="17" <%=fieldDisable%>
							 value='<%=globaz.commons.nss.NSUtil.formatAVSNew(viewBean.getNumAVS(), viewBean.getNumeroAvsNNSS().equals("true"))%>'> 
								
						<input name='nomPrenom' size="55" 
							value='<%=request.getParameter("etatNominatif").toString()%>' tabindex="-1"> 
						</td>
					</tr>
					<tr>
						<td colspan='5'>
						<hr size='1'>
						</td>
					</tr>
					<tr>
						<td>
							<span id="mitglieder"
								class="selected" onclick="selectMitglieder();">Mitglied</span> oder
							<span id="partner" class="notselected" onclick="selectPartner();">Partner</span>
						</td>
						<td colspan="4" width = "750">
							<input type="hidden" value="" name="employeurPartenaire">
							<input type="hidden" value="" name="lieuEmployeur"> 
							
							
							
					   	<nss:nssPopup  onChange="updateEmployeurPartenaire(tag);"
											onFailure="setNotFound(tag);"
											forceSelection="true"
											useUpDownKeys="false"
											name="mitgliedNum" cssclass ="visible"
											 jspName="<%=jspLocation%>"
											avsMinNbrDigit="5" nssMinNbrDigit="8" /> 
											
						<nss:nssPopup onChange="updateEmployeurPartenaire(tag);"
											onFailure="setNotFound(tag);"
											name="partnerNum"
											jspName="<%=jspLocation2%>"										
											forceSelection="true"  cssclass="hiddenPage"
											avsMinNbrDigit="5" nssMinNbrDigit="8" />							
						
							  <input name='numeroDetailInv' size="55" value=""> 
						</td>
					</tr>
					<tr>
						<td>SZ</td>
						<td colspan="4"><input
							onkeypress="return filterCharForPositivInteger(window.event);"
							name="gre" size="3" maxlength="3"
							value="">

						</td>
					</tr>

					<tr>
						<td>Periode</td>
						<td colspan="4"><input
							onkeypress="return filterCharForPositivInteger(window.event);"
							name='moisDebut' size='3' maxlength="2"
							value=""> <b> - </b> <input
							onkeypress="return filterCharForPositivInteger(window.event);"
							name='moisFin' size='3' maxlength="2"
							value=""> <b> . </b> <input
							onkeypress="return filterCharForPositivInteger(window.event);"
							name='annee' size='5' maxlength="4" 
							value="">

						</td>
					</tr>

					<tr>
						<td>Betrag</td>
						<td colspan="4">
						<input onchange="validateFloatNumber(this);"
							   onkeypress="return filterCharForFloat(window.event);"
							   name='montant' size='12'
							   value=""></td>
					</tr>
					<tr>
						<td>SZ</td>
						<%	java.util.HashSet set = new java.util.HashSet();
							set.add("313005");
							set.add("313004");%>
						<td colspan="4"><ct:FWCodeSelectTag
							name="code" libelle="both" defaut="" except="<%=set%>"
							codeType="CICODAMO" wantBlank="true" /> <script>document.all("code").style.width="14cm"</script>
						</td>
					</tr>

					<tr>
						<td><input name="partBtaInv" type="hidden"
							value=""
							tabIndex="-1"></td>
					</tr>
					<tr>
						<td>Sonderfallcode</td>
						<td colspan="4"><ct:FWCodeSelectTag
							name="codeSpecial" libelle="both" defaut="" codeType="CICODSPE"
							wantBlank="true" /> <script>document.all("codeSpecial").style.width="14cm"</script>
						</td>
					</tr>
					<tr>
						<td>Erwerbzweig</td>
						<td colspan="4">
							<input name="brancheEco" size="74">
						</td>
					</tr>
					<tr>
						<td colspan="5">&nbsp;</td>
					</tr>
					<tr>
					<td colspan="5" align="right">
						<INPUT type="button" value="Hinzufügen" onclick="AddEcritureCI()"></td>
					</tr>
					<tr>
						<td colspan='5'>
						<hr size='1'>
						</td>
						</tr>
					<tr>
					<tr>
						<td colspan=5><input name="categoriePersonnelInv" type="hidden"
							value="">
						</td>
					</tr>

					<tr>
						<td valign="top">IK-Buchungen&nbsp;</td>
						<td valign="top" colspan="4">
						<IFRAME name="dernieresEcritures"
							scrolling="YES" style="border: solid 1px black; width: 100%"
							height="150"> 
						</IFRAME> 
						<script>document.dernieresEcritures.location.href='<%=request.getContextPath()%>/hermesRoot/FR/gestion/rassemblement_listeEcrituresSaisies.jsp';</script>
						</td>
					</tr>
						
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>