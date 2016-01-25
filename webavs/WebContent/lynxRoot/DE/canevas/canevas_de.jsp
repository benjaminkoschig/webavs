<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java"
	errorPage="/errorPage.jsp" import="globaz.globall.http.*"
	contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
<%@ include file="/theme/detail/header.jspf"%>
<%-- tpl:put name="zoneInit" --%>
<%@ page import="globaz.lynx.parser.*"%>
<%@ page import="globaz.globall.util.*"%>
<%@ page import="globaz.jade.client.util.*"%>
<%@ page import="globaz.lynx.utils.LXSocieteDebitriceUtil"%>
<%@ page import="globaz.lynx.utils.LXConstants"%>
<%@ page import="globaz.lynx.db.operation.LXOperation"%>
<%@ page import="globaz.lynx.db.section.LXSection"%>
<%@page import="globaz.lynx.db.canevas.LXCanevasViewBean"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%
	idEcran = "GLX0038";
	LXCanevasViewBean viewBean = (LXCanevasViewBean) session.getAttribute("viewBean");
	selectedIdValue = viewBean.getIdOperationCanevas();

	boolean showValue = true;

	actionNew += "&csTypeOperation=";
	
	String actionNewCanevasBvrOrange = actionNew + globaz.lynx.db.operation.LXOperationViewBean.CS_TYPE_FACTURE_BVR_ORANGE;
	String actionNewCanevasBvrRouge = actionNew+globaz.lynx.db.operation.LXOperationViewBean.CS_TYPE_FACTURE_BVR_ROUGE;
	String actionNewCanevasVirement = actionNew+globaz.lynx.db.operation.LXOperationViewBean.CS_TYPE_FACTURE_VIREMENT;
	String actionNewCanevasLsv = actionNew+globaz.lynx.db.operation.LXOperationViewBean.CS_TYPE_FACTURE_LSV;
	String actionNewCanevasCaisse = actionNew+globaz.lynx.db.operation.LXOperationViewBean.CS_TYPE_FACTURE_CAISSE;
	
	String tmpTypeOp = request.getParameter("csTypeOperation");
	if (!globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getCsTypeOperation())) {
		tmpTypeOp = viewBean.getCsTypeOperation();
	}
	
%>

<%-- /tpl:put --%><%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf"%>
<%-- tpl:put name="zoneScripts" --%>

<script type="text/javascript" src="<%=servletContext%>/scripts/dates.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/jquery-ui.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/jquery.js"></script>

<SCRIPT	language="javascript">

$(document).ready(function() {

	//Les colonnes pour le pourcentage sont cachées
 <% for (int i = 0; i < viewBean.getMaxRows() ; i++) { %>
  $('#colPourcentageDeb<%=i%>').hide();
  $('#colPourcentageCred<%=i%>').hide();
 <% } %>
  $('#colPourcentageDebLib').hide();
  $('#colPourcentageCredLib').hide();
  $('#colPourcentageDebSum').hide();
  $('#colPourcentageCredSum').hide();
  $('#colPourcentageDebBalance').hide();
  $('#colPourcentageCredBalance').hide();

  //Fonction qui permet de switcher entre pourcentage et montant
  $('a#pourcentage-show').click(function() {

	//Les colonnes pour le pourcentage sont affichées
	$('#colPourcentageDebLib').toggle(0);
	$('#colPourcentageCredLib').toggle(0);
    $('#colPourcentageDebSum').toggle(0);
    $('#colPourcentageCredSum').toggle(0);
    $('#colPourcentageDebBalance').toggle(0);
    $('#colPourcentageCredBalance').toggle(0);

    //Les colonnes pour le montant sont affichées
    $('#colMontantDebLib').toggle(0);
    $('#colMontantCredLib').toggle(0);
    $('#colMontantDebBalance').toggle(0);
    $('#colMontantCredBalance').toggle(0);
    $('#colMontantDebSum').toggle(0);
    $('#colMontantCredSum').toggle(0);
    
<% for (int i = 0; i < viewBean.getMaxRows() ; i++) { %>
	  $('#colMontantDeb<%=i%>').toggle(0);
	  $('#colMontantCred<%=i%>').toggle(0);
	  $('#colPourcentageDeb<%=i%>').toggle(0);
	  $('#colPourcentageCred<%=i%>').toggle(0);
<% } %>

    return false;
  });
});

<%if ("add".equals(request.getParameter("_method"))) {%>
shortKeys[79] = "btnNewCanevasBvrOrange";
shortKeys[82] = "btnNewCanevasBvrRouge";
shortKeys[86] = "btnNewCanevasVirement";
shortKeys[76] = "btnNewCanevasLsv";
shortKeys[67] = "btnNewCanevasCaisse";
<%}%>

var echeance = "0";

function add() {
    document.forms[0].elements('userAction').value="lynx.canevas.Canevas.ajouter";
    document.getElementById("forceValid").value = "true";
    disableCentreCharge();
}

function upd() {
	document.forms[0].elements('userAction').value="lynx.canevas.Canevas.modifier";
	disableCentreCharge();
} 

function validate() {
    state = validateFields();
    
	disableCentreCharge();
	
    if (document.forms[0].elements('_method').value == "add") {
        document.forms[0].elements('userAction').value="lynx.canevas.canevas.ajouter";
        document.getElementById("forceValid").value = "true";
    } else {
        document.forms[0].elements('userAction').value="lynx.canevas.canevas.modifier";
    }

    return state;

}

function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="lynx.canevas.canevas.afficher";
}

function del() {
	if (window.confirm("Sie sind dabei, den ausgewählten Kanevas zu löschen! Wollen Sie fortfahren?")) {
        document.forms[0].elements('userAction').value="lynx.canevas.canevas.supprimer";
        document.forms[0].submit();
    }
}

function updateEcheancePourTaux() {

}

function updateDateEcheance(forceUpdate) {

}

function updateLibelleFacture(libelleFacture) {
	var element = document.getElementById("libelle");

	if(libelleFacture != "" && libelleFacture != null && element.value == "") {
		element.value = libelleFacture;
	}
}

function updateBlocage() {

}

function init() {
	top.document.title = "Detail eines Kanevasses- " + top.location.href;

	<%if (request.getParameter("forceValid") != null && request.getParameter("forceValid").equals("true")) {%>
		document.getElementById("_valid").value = "new";
	<%}%>

	hideRows();
	showMontantEtranger();
    updateSum();
}

function postInit() {
	//Positionnement des tabulations
	<% if(globaz.lynx.db.operation.LXOperationViewBean.CS_TYPE_FACTURE_BVR_ORANGE.equals(tmpTypeOp)) { %>
		cheminTabulationBVR();
	<% } else if(globaz.lynx.db.operation.LXOperationViewBean.CS_TYPE_FACTURE_BVR_ROUGE.equals(tmpTypeOp)) {%>
		cheminTabulationBVR();
	<% } else if(globaz.lynx.db.operation.LXOperationViewBean.CS_TYPE_FACTURE_VIREMENT.equals(tmpTypeOp) || 
				 globaz.lynx.db.operation.LXOperationViewBean.CS_TYPE_FACTURE_CAISSE.equals(tmpTypeOp)  || 
				 globaz.lynx.db.operation.LXOperationViewBean.CS_TYPE_FACTURE_LSV.equals(tmpTypeOp) ) {%>
		cheminTabulationVirementCaisseLSV();
	<% } %>
	
    document.getElementById("btnVal").disabled = true;
}

function fillCell(cell) {

}
</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf"%>
<%-- tpl:put name="zoneTitle" --%>Detail eines Kanevasses<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf"%>
<%-- tpl:put name="zoneMain" --%>

<%	
	String jspLocationSociete = servletContext + "/lynxRoot/autocomplete/societeForCanevas_select.jsp";
	String paramsSociete = "withAdresse=true&csTypeOperation="+tmpTypeOp;

	String jspFournisseurLocation = servletContext + "/lynxRoot/autocomplete/fournisseur_select.jsp";
	int autoCompleteStart = globaz.helios.parser.CGAutoComplete.getAutoCompleteAutoStart();

	String jspFournisseurLigneCodage = servletContext + "/lynxRoot/autocomplete/ligneCodage_select.jsp";
	String jspFournisseurLigneCodageBVRRouge = servletContext + "/lynxRoot/autocomplete/ligneCodageBVRRouge_select.jsp";
	boolean isLectureOptique = true; //viewBean.getSociete().isLectureOptique().booleanValue();
	
	String jspSectionLocation = servletContext + "/lynxRoot/autocomplete/section_select.jsp";
	String typeSection = LXSection.CS_TYPE_FACTURE;
	String paramsSection = "&forCsTypeSection=" + typeSection;
	
	boolean tmpValidateAutoComplete = false;
	if ("add".equalsIgnoreCase(request.getParameter("_method")) && (request.getParameter("_valid") == null || request.getParameter("_valid").equals("fail"))) {
		tmpValidateAutoComplete = true;
	}
	
	String selectCodeIsoMonnaieSelect = LXSelectBlockParser.getCsCodeIsoMonnaie(objSession, viewBean.getCsCodeIsoMonnaie());
	String params = "withAdrPmt=true&withInfoComptable=true&idSociete="+viewBean.getIdSociete()+"&typeFacture="+tmpTypeOp;
	String paramsLectureOptique = "idSociete="+viewBean.getIdSociete();
	
	String selectOrganeExecutionSelect = LXSelectBlockParser.getIdOrganeExecutionSelectBlock(objSession, viewBean.getIdSociete(), viewBean.getIdOrganeExecution(), tmpTypeOp);
	String selectCsCodeTVASelect = globaz.lynx.parser.LXSelectBlockParser.getTauxEtCodeTvaSelectBlock(objSession, viewBean.getCsCodeTVA());
	
	String jspLocation = servletContext + "/lynxRoot/autocomplete/compte_select.jsp";
	String params2 = "forDate=" + globaz.globall.util.JACalendar.todayJJsMMsAAAA() + "&idMandat=" + viewBean.getIdMandat();

	String jspLocationLibelle = servletContext + "/heliosRoot/" + languePage + "/comptes/label_select.jsp";
	String paramsLibelle = "idMandat=" + viewBean.getIdMandat() + "&langue=" + languePage;
	
	String tmpIdExterneFournisseur = showValue?viewBean.getIdExterneFournisseur():"";
	
	String aucun = "Aucun";
	if (languePage.equalsIgnoreCase("DE")) {
		aucun = "Kein";
	}
	
	java.util.Vector centreChargeListe = globaz.helios.translation.CGListes.getCentreChargeListe(aucun, session, viewBean.getIdMandat());
%>

<!------------------------------------------------------------------------------
  Les champs hidden 
-------------------------------------------------------------------------------->
<input type="hidden" name="idSociete" value="<%=viewBean.getIdSociete()%>" />
<input type="hidden" name="idSectionCanevas" value="<%=viewBean.getIdSectionCanevas()%>" />
<input type="hidden" name="idOperationCanevas" value="<%=viewBean.getIdOperationCanevas()%>" />
<input type="hidden" name="csTypeOperation" value="<%=tmpTypeOp%>" />
<input type="hidden" name="forceValid" value="false" />

</TBODY>
</TABLE>

<!------------------------------------------------------------------------------
  Include des fontions javascripts pour la gestion de la ventilation 
-------------------------------------------------------------------------------->
<%@ include file="/lynxRoot/include/canevas_ventilation.jsp"%>

<!------------------------------------------------------------------------------
  Include des fontions javascripts pour la gestion des templates des bulletins 
-------------------------------------------------------------------------------->
<%@ include file="/lynxRoot/include/canevas_bulletin.jsp"%>

<!------------------------------------------------------------------------------
  Include du template du bulletin : Suivant le type de facture 
-------------------------------------------------------------------------------->

<% if(globaz.lynx.db.operation.LXOperationViewBean.CS_TYPE_FACTURE_BVR_ORANGE.equals(tmpTypeOp)) { %>
	<%@ include file="/lynxRoot/include/canevasBvrOrange.jsp"%>
<% } else if(globaz.lynx.db.operation.LXOperationViewBean.CS_TYPE_FACTURE_BVR_ROUGE.equals(tmpTypeOp)) { %>
	<%@ include file="/lynxRoot/include/canevasBvrRouge.jsp"%>
<% } else if(globaz.lynx.db.operation.LXOperationViewBean.CS_TYPE_FACTURE_VIREMENT.equals(tmpTypeOp)) {%>
	<%@ include file="/lynxRoot/include/canevasVirementBancaire.jsp"%>
<% } else if(globaz.lynx.db.operation.LXOperationViewBean.CS_TYPE_FACTURE_CAISSE.equals(tmpTypeOp)) {%>
	<%@ include file="/lynxRoot/include/canevasCaisse.jsp"%>
<% } else if(globaz.lynx.db.operation.LXOperationViewBean.CS_TYPE_FACTURE_LSV.equals(tmpTypeOp)) {%>
	<%@ include file="/lynxRoot/include/canevasLsv.jsp"%>
<% } %>

<!------------------------------------------------------------------------------
  Affichage des informations de la facture
-------------------------------------------------------------------------------->
<TABLE border="0" cellspacing="0" cellpadding="0" width="100%">
	<TBODY>
		<TR>
			<TD height="11" colspan="5"><hr size="3" width="100%"></TD>
		</TR>
		<TR>
			<TD width="255">Ausführungsorgan</TD>
			<TD>
			<%
				if (!globaz.jade.client.util.JadeStringUtil.isBlank(selectOrganeExecutionSelect)) {
					out.print(selectOrganeExecutionSelect);
				}
			%>
			</TD>
			<TD>&nbsp;</TD>
			<TD>Diskontsatz</TD>
			<TD><INPUT type="text" onKeyPress="fillCell(this);"
				onchange="javascript:updateEcheancePourTaux();" id="tauxEscompte"
				name="tauxEscompte" style="width: 2.5cm" size="11" maxlength="10"
				value="<%=showValue ? viewBean.getTauxEscompte() : ""%>"
				class="libelleLong">%</TD>
		</TR>
		<TR>
			<TD>Bezeichnung</TD>
			<TD><INPUT type="text" onKeyPress="fillCell(this);"
				name="libelle" style="width: 7cm" size="41" maxlength="40"
				value="<%=showValue ? viewBean.getLibelle() : ""%>" class="libelleLong">
			</TD>
			<TD>&nbsp;</TD>
			<TD>MWST Code</TD>
			<TD>			
			<%
				if (!globaz.jade.client.util.JadeStringUtil.isBlank(selectCsCodeTVASelect)) {
					out.print(selectCsCodeTVASelect);
				}
			%>
			<script	language="JavaScript">
					element = document.getElementById("csCodeTva");
				  	element.onchange = function() {updateMontant();updateSum();}
				</script>
			</TD>
		</TR>
		<TR>
			<TD height="11" colspan="5"><hr size="3" width="100%"></TD>
		</TR>
		<TR>
			<TD>Interne Kanevas-Nr.</TD>
			<%
				String tmpIdExterne = showValue?viewBean.getIdExterne():""; 
			%>
			<TD><ct:FWPopupList name="idExterne" onChange="" validateOnChange="false" params="<%=paramsSection%>" value="<%=tmpIdExterne%>" className="libelle" jspName="<%=jspSectionLocation%>" minNbrDigit="1" forceSelection="false"/> 
			<script language="JavaScript">
					element = document.getElementById("idExterne");
				  	element.onkeypress=function() {fillCell(this);}
				</script></TD>
			<TD>&nbsp;</TD>
			<TD>Lieferant Kanevas-Nr.</TD>
			<TD><INPUT type="text" onKeyPress="fillCell(this);"
				name="referenceExterne" style="width: 7cm" size="41" maxlength="40"
				value="<%=showValue ? viewBean.getReferenceExterne() : ""%>"
				class="libelleLong"></TD>
		</TR>
		<TR>
			<TD height="11" colspan="5"><hr size="3" width="100%"></TD>
		</TR>
         <TR>
            <TD>Gliederung :</TD>
            <TD colspan="4">&nbsp;</TD>
        </TR>
        <TR>
            <TD><a href="#" id="pourcentage-show">Betrag/Prozentsatz</a></TD>
            <TD colspan="4">&nbsp;</TD>
        </TR>
	</TBODY>
</TABLE>
			
<!------------------------------------------------------------------------------
  Début partie sur la ventilation
-------------------------------------------------------------------------------->
<TABLE border="0" cellspacing="0" cellpadding="0" width="100%">
	<TBODY>
		<tr>
			<td align="right" height="24">
				<ct:ifhasright element="lynx.canevas.canevas.afficher" crud="u">
					<a href="#" border="noborder" onclick="showNextRow();focusOnNextCompte();" id="plus"><img src="<%=request.getContextPath()%>/images/plus.jpg" border="0" title="Zeile hinzufügen"/></a>
				</ct:ifhasright>
			</td>
		</tr>
		<tr>
			<td>
				<table width="100%" cellspacing="0" class="borderBlack">
					<tr>
						<th align="left">Konto</th>
						<th align="left">Kostenstelle</th>
						<th>Bezeichnung</th>
						<th id="colMontantDebLib">Soll</th>
						<th id="colMontantCredLib">Haben</th>
						<th id="colPourcentageDebLib">% Soll</th>
						<th id="colPourcentageCredLib">% Haben</th>
						<th>Betrag (&euro;,&#36;...)</th>
						<th>Kurs</th>
					</tr>
					<%
						for (int i = 0; i < viewBean.getMaxRows() ; i++) {
							String style = "row";
							if (i % 2 == 1) {
								style = "rowOdd";
							}
					%>
					<tr class="<%=style%>" id="ligne<%=i%>">
						<td style="vertical-align: middle; text-align: left;" class="mtdShortPadding">
						<%
					        String tmpIdext = "idext" + i;
					    	String tmpIdextScript = "updateCompte(tag, " + i + ");updateSum();";
					    	String tmpIdextValue = showValue?viewBean.getIdExt(i):"";
					    %>
					    <ct:FWPopupList name="<%=tmpIdext%>" onFailure="onCompteFailure(window.event);" onChange="<%=tmpIdextScript%>" validateOnChange="true" params="<%=params2%>" value="<%=tmpIdextValue%>" className="compte" jspName="<%=jspLocation%>" minNbrDigit="1" autoNbrDigit="<%=autoCompleteStart%>" forceSelection="true"/>
						<input type="hidden" name="idc<%=i%>" value="<%=viewBean.getIdCompte(i)%>"/>
						<input type="hidden" name="idv<%=i%>" value="<%=viewBean.getIdVentilation(i)%>"/>
					
						<script language="JavaScript">
							element = document.getElementById("<%=("idext" + i)%>");
						  	element.onkeypress=function() {fillCell(this);}
						</script>
					
						</td>
						<td style="vertical-align: middle; text-align: left;" class="mtdShortPadding">
						<%
					        String tmpIdcc = "idcc" + i;
					        String tmpIdccValue = showValue?viewBean.getIdCompteCharge(i):"0";
					        
					    %>
						<ct:FWListSelectTag name="<%=tmpIdcc%>" defaut="<%=tmpIdccValue%>" data="<%=centreChargeListe%>"/>
						<script language="JavaScript">
							document.getElementById("<%=tmpIdcc%>").style.width = '135px';
						</script>
						</td>
						<td style="vertical-align: middle; text-align: left;" class="mtdShortPadding">
						<%
					        String tmpLibelle = "l" + i;
					        String tmpLibelleValue = showValue?viewBean.getLibelle(i):"";
					    %>
						<ct:FWPopupList name="<%=tmpLibelle%>" size="26" maxlength="40" onFailure="onLibelleFailure(window.event);" onChange="fillCell(this);" params="<%=paramsLibelle%>" value="<%=tmpLibelleValue%>" validateOnChange="false" className="libelle" jspName="<%=jspLocationLibelle%>" minNbrDigit="2" forceSelection="false"/>
						
						<script language="JavaScript">
							element = document.getElementById("<%=("l" + i)%>");
						  	element.onkeypress=function() {fillCell(this);}
						</script>
						
						</td>
						
						<td id="colMontantDeb<%=i%>" style="vertical-align: middle; text-align: center;" class="mtdShortPadding">
						<% if (i == 0 || i >= viewBean.getShowRows() || !globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getMontantDebit(i))) { %>
						<input onchange="validateFloatNumber(this);clearDebitCredit('mc', <%=i%>);updateSum();" onkeypress="return filterCharForFloat(window.event);" type="text" class="montantShort" style="text-align : right" name="md<%=i%>" value="<%=viewBean.getMontantDebit(i)%>"/>
						<% } else { %>
						<input type="hidden" name="md<%=i%>" value=""/>
						<% } %>
						</td>
						
						<td id="colMontantCred<%=i%>" style="vertical-align: middle; text-align: center;" class="mtdShortPadding">
						<% if (i > 0 || i >= viewBean.getShowRows() || !globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getMontantCredit(i))) { %>
						<input onchange="validateFloatNumber(this);clearDebitCredit('md', <%=i%>);updateSum();" onkeypress="return filterCharForFloat(window.event);" type="text" class="montantShort" style="text-align : right" name="mc<%=i%>" value="<%=viewBean.getMontantCredit(i)%>"/>
						<% } else { %>
						<input type="hidden" name="mc<%=i%>" value=""/>
						<% } %>
						</td>
						
						<td id="colPourcentageDeb<%=i%>" style="vertical-align: middle; text-align: center;" class="mtdShortPadding"><input id="colPourcentageInput<%=i%>" onchange="validateFloatNumber(this);updateSum();" type="text" class="pourcentage" style="text-align : right" name="colPourcentageDeb<%=i%>" value="<%=JadeStringUtil.isEmpty(viewBean.getPourcentage(i))? "0.00" : viewBean.getPourcentage(i)%>"/></td>									
						<td id="colPourcentageCred<%=i%>" style="vertical-align: middle; text-align: center;" class="mtdShortPadding"><input id="colPourcentageInput<%=i%>" onchange="validateFloatNumber(this);updateSum();" type="text" class="pourcentage" style="text-align : right" name="colPourcentageCred<%=i%>" value="<%=JadeStringUtil.isEmpty(viewBean.getPourcentage(i))? "0.00" : viewBean.getPourcentage(i)%>"/></td>									

						<td style="vertical-align: middle; text-align: center;" class="mtdShortPadding"><input onchange="validateFloatNumber(this);updateMontantChf(<%=i%>);updateSum();" onkeypress="return filterCharForFloat(window.event);" type="text" class="montantShortDisabled" style="text-align : right" name="me<%=i%>" value="<%=showValue?viewBean.getMontantEtranger(i):"0.00"%>" readonly="readonly" disabled="disabled"/></td>
						<td style="vertical-align: middle; text-align: center;" class="mtdShortPadding"><input onchange="validateFloatNumber(this,5);updateMontantChf(<%=i%>);updateSum();" onkeypress="return filterCharForFloat(window.event);" type="text" class="montantShortDisabled" style="text-align : right;width : 2.5cm;" name="c<%=i%>" value="<%=showValue?viewBean.getCours(i):"0.00000"%>" readonly="readonly" disabled="disabled"/></td>
					</tr>
					<%} %>
					<tr class="somme">
						<td colspan="3" align="right" class="mtdBold">Total :
						<input type="hidden" name="flv" value="" onclick="equilibrate()"/>
						</td>
						<td id="colMontantDebSum" class="mtdMontant"><input onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" type="text" class="montantShort" style="text-align : right" name="sd" value="<%=showValue?globaz.globall.util.JANumberFormatter.fmt("0.00",true,true,false,2):""%>" readonly="readonly" disabled="readonly"/></td>
						<td id="colMontantCredSum" class="mtdMontant"><input onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" type="text" class="montantShort" style="text-align : right" name="sc" value="<%=showValue?globaz.globall.util.JANumberFormatter.fmt("0.00",true,true,false,2):""%>" readonly="readonly" disabled="readonly"/></td>
						<td id="colPourcentageDebSum" style="text-align: center"><input onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" type="text" class="pourcentage" style="text-align : right" name="spd" value="<%=showValue?globaz.globall.util.JANumberFormatter.fmt("0.00",true,true,false,2):""%>" readonly="readonly" disabled="readonly"/></td>
						<td id="colPourcentageCredSum" style="text-align: center"><input onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" type="text" class="pourcentage" style="text-align : right" name="spc" value="<%=showValue?globaz.globall.util.JANumberFormatter.fmt("0.00",true,true,false,2):""%>" readonly="readonly" disabled="readonly"/></td>
						<td class="mtdMontant"><input onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" type="text" class="montantShortDisabled" style="text-align : right" name="se" value="<%=showValue?globaz.globall.util.JANumberFormatter.fmt("0.00",true,true,false,2):""%>" readonly="readonly" disabled="readonly"/></td>
						<td>&nbsp;</td>
					</tr>

					<tr class="balance">
						<td colspan="3" class="mtd" align="right">Bilanz :</td>
						<td id="colMontantDebBalance" class="mtdMontant"><input onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" type="text" class="montantShort" style="text-align : right" name="bd" value="<%=showValue?globaz.globall.util.JANumberFormatter.fmt("0.00",true,true,false,2):""%>" readonly="true" disabled="true"/></td>
						<td id="colMontantCredBalance" class="mtdMontant"><input onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" type="text" class="montantShort" style="text-align : right" name="bc" value="<%=showValue?globaz.globall.util.JANumberFormatter.fmt("0.00",true,true,false,2):""%>" readonly="true" disabled="true"/></td>
						<td id="colPourcentageDebBalance">&nbsp;</td>
						<td id="colPourcentageCredBalance">&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td align="right" height="24">
				<ct:ifhasright element="lynx.canevas.canevas.afficher" crud="u">
					<a href="#" border="noborder" onclick="showNextRow();focusOnNextCompte();" ><img src="<%=request.getContextPath()%>/images/plus.jpg" border="0" title="Zeile hinzufügen"/></a>
				</ct:ifhasright>
			</td>
		</tr>
	</TBODY>
</TABLE>
		
<!------------------------------------------------------------------------------
	Affichage des boutons
------------------------------------------------------------------------------->
<TABLE border="0" cellspacing="0" cellpadding="0" width="100%">
	<TBODY>						
		<TR>
			<TD height="11" colspan="2">
			<hr size="3" width="100%">
			</TD>
		</TR>
		<%-- /tpl:put --%>
		<%@ include file="/theme/detail/bodyButtons.jspf"%>
		<%-- tpl:put name="zoneButtons" --%>
		<%
			if ("add".equals(request.getParameter("_method"))) {
		%>
		<INPUT type="button" name="btnNewCanevasBvrOrange" value="Oranger ESR" onClick="document.location.href='<%=actionNewCanevasBvrOrange%>'">
		<INPUT type="button" name="btnNewCanevasBvrRouge" value="Roter ESR" onClick="document.location.href='<%=actionNewCanevasBvrRouge%>'">
		<INPUT type="button" name="btnNewCanevasVirement" value="Überweisung" onClick="document.location.href='<%=actionNewCanevasVirement%>'">
		<INPUT type="button" name="btnNewCanevasLsv" value=" LSV " onClick="document.location.href='<%=actionNewCanevasLsv%>'">
		<INPUT type="button" name="btnNewCanevasCaisse" value="Kasse" onClick="document.location.href='<%=actionNewCanevasCaisse%>'">
		<%	} %>
		
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf"%>
<%-- tpl:put name="zoneEndPage" --%>
		<%
			if (!"add".equals(request.getParameter("_method"))) {
		%>
		<ct:menuChange displayId="options" menuId="LX-Canevas" showTab="options" checkAdd="no">
			<ct:menuSetAllParams key='selectedId' value='<%=viewBean.getIdOperationCanevas()%>' checkAdd='no'/>
			<ct:menuSetAllParams key='idOperationCanevas' value='<%=viewBean.getIdOperationCanevas()%>' checkAdd='no'/>
		</ct:menuChange>
		<%	} %>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf"%>
<%-- /tpl:insert --%>