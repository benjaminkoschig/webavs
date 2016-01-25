<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java"
	errorPage="/errorPage.jsp" import="globaz.globall.http.*"
	contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
<%@ include file="/theme/detail/header.jspf"%>
<%-- tpl:put name="zoneInit" --%>
<%@ page import="globaz.lynx.db.facture.*"%>
<%@ page import="globaz.lynx.parser.*"%>
<%@ page import="globaz.globall.util.*"%>
<%@ page import="globaz.jade.client.util.*"%>
<%@ page import="globaz.lynx.utils.LXSocieteDebitriceUtil"%>
<%@ page import="globaz.lynx.utils.LXConstants"%>
<%@ page import="globaz.lynx.db.operation.LXOperation"%>
<%@ page import="globaz.lynx.db.section.LXSection"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%
	idEcran = "GLX0013";
	LXFactureViewBean viewBean = (LXFactureViewBean) session.getAttribute("viewBean");
	selectedIdValue = viewBean.getIdOperation();

	boolean showValue = !(request.getParameter("forceNew") != null && !(new Boolean(request.getParameter("forceNew")).booleanValue()));

	actionNew += "&idSociete=" + viewBean.getIdSociete();
	actionNew += "&idJournal=" + viewBean.getIdJournal();
	actionNew += "&forceNew=true";
	actionNew += "&csTypeOperation=";

	String actionNewFactureBvrOrange = actionNew + globaz.lynx.db.operation.LXOperationViewBean.CS_TYPE_FACTURE_BVR_ORANGE;
	String actionNewFactureBvrRouge = actionNew + globaz.lynx.db.operation.LXOperationViewBean.CS_TYPE_FACTURE_BVR_ROUGE;
	String actionNewFactureVirement = actionNew + globaz.lynx.db.operation.LXOperationViewBean.CS_TYPE_FACTURE_VIREMENT;
	String actionNewFactureLsv = actionNew + globaz.lynx.db.operation.LXOperationViewBean.CS_TYPE_FACTURE_LSV;
	String actionNewFactureCaisse = actionNew + globaz.lynx.db.operation.LXOperationViewBean.CS_TYPE_FACTURE_CAISSE;

	String tmpTypeOp = request.getParameter("csTypeOperation");
	if (!globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getCsTypeOperation())) {
		tmpTypeOp = viewBean.getCsTypeOperation();
	}

	if (!viewBean.isJournalEditable()) {
		bButtonNew = false;
		bButtonDelete = false;

		bButtonUpdate = LXOperation.CS_ETAT_COMPTABILISE.equals(viewBean.getCsEtat()) && objSession.hasRight("lynx.facture.facture.afficher", FWSecureConstants.UPDATE);
	}
%>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf"%>
<%-- tpl:put name="zoneScripts" --%>
<script type="text/javascript" src="<%=servletContext%>/scripts/dates.js"></script>

<SCRIPT	language="javascript">

<%if (viewBean.isJournalEditable() && "add".equals(request.getParameter("_method"))) {%>
shortKeys[79] = "btnNewFactureBvrOrange";
shortKeys[82] = "btnNewFactureBvrRouge";
shortKeys[86] = "btnNewFactureVirement";
shortKeys[76] = "btnNewFactureLsv";
shortKeys[67] = "btnNewFactureCaisse";
<%}%>

var echeance = "0";

function add() {
    document.forms[0].elements('userAction').value="lynx.facture.facture.ajouter";
    document.getElementById("forceValid").value = "true";
    disableCentreCharge();
}

function upd() {
	document.forms[0].elements('userAction').value="lynx.facture.facture.modifier";
	disableCentreCharge();

	<%	if (!viewBean.isJournalEditable() && LXOperation.CS_ETAT_COMPTABILISE.equals(viewBean.getCsEtat())) {%>
		readOnly(true);

		document.getElementById('estBloque').disabled = false;
		document.getElementById('forceEstBloqueUpdate').value = "true";
	<%}%>

	updateBlocage();
}

function validate() {
    state = validateFields();

	disableCentreCharge();

    if (document.forms[0].elements('_method').value == "add") {
        document.forms[0].elements('userAction').value="lynx.facture.facture.ajouter";
        document.getElementById("forceValid").value = "true";
    } else {
        document.forms[0].elements('userAction').value="lynx.facture.facture.modifier";
    }

    return state;

}

function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="lynx.facture.facture.afficher";
}

function del() {
	if (window.confirm("Vous êtes sur le point de supprimer la facture sélectionnée! Voulez-vous continuer?")) {
        document.forms[0].elements('userAction').value="lynx.facture.facture.supprimer";
        document.forms[0].submit();
    }
}

function updateEcheancePourTaux() {
	if(document.getElementById("tauxEscompte").value > 0) {
		document.getElementById("dateEcheance").value = "<%=JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDsMMsYYYY)%>";
	}
}

function updateDateEcheance(forceUpdate) {
	// HACK pour gérer la saisie rapide des dates
	fieldFormat(document.getElementById("dateFacture"),'CALENDAR', 0);
	if((document.getElementById("dateEcheance").value == "" || "true" == forceUpdate) && !document.getElementById("dateFacture").value == "" && echeance >= 0) {
		var dValue = document.getElementById("dateFacture").value;
		d = new Date(getDateFromFormat(dValue, "dd.MM.yyyy"));
		<% if(!globaz.lynx.db.operation.LXOperationViewBean.CS_TYPE_FACTURE_CAISSE.equals(tmpTypeOp)) {%>
			d.addDays(Number(echeance));
			d.gotoNextWorkingDay();
		<% }%>
		document.getElementById('dateEcheance').value = formatDate(d, "dd.MM.yyyy");
	}
}

function updateLibelleFacture(libelleFacture) {
	var element = document.getElementById("libelle");

	if(libelleFacture != "" && libelleFacture != null && element.value == "") {
		element.value = libelleFacture;
	}
}

function updateBlocage() {

	for (i = 0; i < Number(document.getElementById("csMotifBlocage").options.length-1); i++){
		if(document.getElementById("csMotifBlocage").options[ Number(i+1) ].value == "0") {
			document.getElementById("csMotifBlocage").options[ Number(i+1) ] = null;
		}
	}

	if(document.getElementById("estBloque").checked) {
		document.getElementById("csMotifBlocage").disabled = false;

	} else {
		var length = document.getElementById("csMotifBlocage").options.length ;
		document.getElementById("csMotifBlocage").options[length]= new Option('', '0');
		document.getElementById("csMotifBlocage").options.selectedIndex = length;
		document.getElementById("csMotifBlocage").disabled = true;
	}
}

function init() {
	top.document.title = "Détail d'une facture - " + top.location.href;

	<%if (request.getParameter("forceValid") != null && request.getParameter("forceValid").equals("true")) {%>
		document.getElementById("_valid").value = "new";
	<%}%>

	hideRows();
	showMontantEtranger();

	document.getElementById("btnVal").disabled = true;

    updateSum();

	postInit();
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

	updateBlocage();
}

function fillCell(cell) {
	/**
	* touche '=' pressée
	*/
	if (event.keyCode==61 && cell.value=='') {
		if (cell.name == "idExterneFournisseur") {
			<%if (viewBean.getIdExterneFournisseur() != null) {%>
				cell.value="<%=viewBean.getIdExterneFournisseur()%>";
			<%}%>
		} else if (cell.name == "montant") {
			<%if (viewBean.getMontant() != null) {%>
				cell.value="<%=viewBean.getMontant()%>";
			<%}%>
		} else if (cell.name == "referenceBVR") {
			<%if (viewBean.getReferenceBVR() != null) {%>
				cell.value="<%=viewBean.getReferenceBVR()%>";
			<%}%>
		} else if (cell.name == "libelle") {
			<%if (viewBean.getLibelle() != null) {%>
				cell.value="<%=viewBean.getLibelle()%>";
			<%}%>
		} else if (cell.name == "idExterne") {
			<%if (viewBean.getIdExterne() != null) {%>
				cell.value="<%=viewBean.getIdExterne()%>";
			<%}%>
		} else if (cell.name == "referenceExterne") {
			<%if (viewBean.getReferenceExterne() != null) {%>
				cell.value="<%=viewBean.getReferenceExterne()%>";
			<%}%>
		} else if (cell.name == "tauxEscompte") {
			<%if (viewBean.getTauxEscompte() != null) {%>
				cell.value="<%=viewBean.getTauxEscompte()%>";
			<%}%>
		}

		event.keyCode=null;
	}
}
</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf"%>
<%-- tpl:put name="zoneTitle" --%>
D&eacute;tail d'une facture
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf"%>
<%-- tpl:put name="zoneMain" --%>

<%
	// Initialisation des variables suivant si on est en ajout ou en modif
	String etatAdd = viewBean.getUcEtat().getLibelle();
	String dateFacture = viewBean.getDateFacture();

	if ("add".equalsIgnoreCase(request.getParameter("_method")) && (request.getParameter("_valid") == null || request.getParameter("_valid").equals("fail"))) {
		etatAdd = viewBean.getUcEtat(LXOperation.CS_ETAT_OUVERT).getLibelle();
		dateFacture = JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDsMMsYYYY);
	}

	String jspFournisseurLocation = servletContext + "/lynxRoot/autocomplete/fournisseur_select.jsp";
	int autoCompleteStart = globaz.helios.parser.CGAutoComplete.getAutoCompleteAutoStart();

	String jspFournisseurLigneCodage = servletContext + "/lynxRoot/autocomplete/ligneCodage_select.jsp";
	String jspFournisseurLigneCodageBVRRouge = servletContext + "/lynxRoot/autocomplete/ligneCodageBVRRouge_select.jsp";
	boolean isLectureOptique = viewBean.getSociete().isLectureOptique().booleanValue();

	String typeSection = LXSection.CS_TYPE_FACTURE;

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
  Include des champs hidden
-------------------------------------------------------------------------------->
<%@ include file="/lynxRoot/include/facture_hidden.jsp"%>

</TBODY>
</TABLE>

<!------------------------------------------------------------------------------
  Include des fontions javascripts pour la gestion de la ventilation
-------------------------------------------------------------------------------->
<%@ include file="/lynxRoot/include/facture_ventilation.jsp"%>


<!------------------------------------------------------------------------------
  Include des fontions javascripts pour la gestion des templates des bulletins
-------------------------------------------------------------------------------->
<%@ include file="/lynxRoot/include/facture_bulletin.jsp"%>


<!------------------------------------------------------------------------------
  Include du template du bulletin : Suivant le type de facture
-------------------------------------------------------------------------------->
<% if(globaz.lynx.db.operation.LXOperationViewBean.CS_TYPE_FACTURE_BVR_ORANGE.equals(tmpTypeOp)) { %>
	<%@ include file="/lynxRoot/include/bvrOrange.jsp"%>
<% } else if(globaz.lynx.db.operation.LXOperationViewBean.CS_TYPE_FACTURE_BVR_ROUGE.equals(tmpTypeOp)) { %>
	<%@ include file="/lynxRoot/include/bvrRouge.jsp"%>
<% } else if(globaz.lynx.db.operation.LXOperationViewBean.CS_TYPE_FACTURE_VIREMENT.equals(tmpTypeOp)) {%>
	<%@ include file="/lynxRoot/include/virementBancaire.jsp"%>
<% } else if(globaz.lynx.db.operation.LXOperationViewBean.CS_TYPE_FACTURE_CAISSE.equals(tmpTypeOp)) {%>
	<%@ include file="/lynxRoot/include/caisse.jsp"%>
<% } else if(globaz.lynx.db.operation.LXOperationViewBean.CS_TYPE_FACTURE_LSV.equals(tmpTypeOp)) {%>
	<%@ include file="/lynxRoot/include/lsv.jsp"%>
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
			<TD width="255">Organe d'ex&eacute;cution</TD>
			<TD>
			<%
				if (!globaz.jade.client.util.JadeStringUtil.isBlank(selectOrganeExecutionSelect)) {
					out.print(selectOrganeExecutionSelect);
				}
			%>
			</TD>
			<TD>&nbsp;</TD>
			<TD>Taux d'escompte</TD>
			<TD><INPUT type="text" onKeyPress="fillCell(this);"
				onchange="javascript:updateEcheancePourTaux();" id="tauxEscompte"
				name="tauxEscompte" style="width: 2.5cm" size="11" maxlength="10"
				value="<%=showValue ? viewBean.getTauxEscompte() : ""%>"
				class="libelleLong">%</TD>
		</TR>
		<TR>
			<TD>Libell&eacute;</TD>
			<TD><INPUT type="text" onKeyPress="fillCell(this);"
				name="libelle" style="width: 7cm" size="41" maxlength="40"
				value="<%=showValue ? viewBean.getLibelle() : ""%>" class="libelleLong">
			</TD>
			<TD>&nbsp;</TD>
			<TD>Code TVA</TD>
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
			<TD>Date facture</td>
			<TD><ct:FWCalendarTag name="dateFacture"
				doClientValidation="CALENDAR" value="<%=dateFacture%>"/> <script
				language="JavaScript">document.getElementById("dateFacture").onchange = function(){updateDateEcheance("true");};</script>
			</TD>
			<TD>&nbsp;</TD>
			<% String strEstBloque = viewBean.getEstBloque().booleanValue() ? "checked=\"checked\"" : ""; %>
			<TD>Bloquer paiement(s)</TD>
			<TD><input type="checkbox" id="estBloque"
				onclick="javascript:updateBlocage();" name="estBloque" <%= strEstBloque %>/>
				<input type="hidden" name="forceEstBloqueUpdate" id="forceEstBloqueUpdate" value="false"/>
			</TD>
		</TR>
		<tr>
			<td>Ech&eacute;ance</td>
			<%
				String tmpDateEcheance = showValue?viewBean.getDateEcheance():"";
			%>
			<td><ct:FWCalendarTag name="dateEcheance"
				doClientValidation="CALENDAR"
				value="<%=tmpDateEcheance%>"/></td>
			<TD>&nbsp;</TD>
			<TD>Motif du blocage</TD>
			<%
				String tmpCsMotifBlocage = showValue?viewBean.getCsMotifBlocage():"";
			%>
			<TD><ct:FWCodeSelectTag name="csMotifBlocage" defaut="<%=tmpCsMotifBlocage%>" codeType="LXMOTIFBL" wantBlank="true"/></TD>
		</tr>
		<TR>
			<TD height="11" colspan="5"><hr size="3" width="100%"></TD>
		</TR>
		<TR>
			<TD>N° de facture interne</TD>
			<TD><INPUT type="text" onKeyPress="fillCell(this);"
				name="idExterne" style="width: 7cm" size="41" maxlength="40"
				value="<%=showValue ? viewBean.getIdExterne() : ""%>"
				class="libelle"></TD>
			<TD>&nbsp;</TD>
			<TD>N° de facture fournisseur</TD>
			<TD><INPUT type="text" onKeyPress="fillCell(this);"
				name="referenceExterne" style="width: 7cm" size="41" maxlength="40"
				value="<%=showValue ? viewBean.getReferenceExterne() : ""%>"
				class="libelleLong"></TD>
		</TR>
		<TR>
			<TD height="11" colspan="5"><hr size="3" width="100%"></TD>
		</TR>
         <TR>
            <TD>Ventilation :</TD>
            <TD colspan="4">&nbsp;</TD>
        </TR>
	</TBODY>
</TABLE>

<!------------------------------------------------------------------------------
  Include la partie ventilation non spécifique de début
-------------------------------------------------------------------------------->
<%@ include file="/lynxRoot/FR/include/ventilationBegin.jsp"%>

						<td style="vertical-align: middle; text-align: center;" class="mtdShortPadding">
						<% if (i > 0 || i >= viewBean.getShowRows() || !globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getMontantDebit(i))) { %>
						<input onchange="validateFloatNumber(this);clearDebitCredit('mc', <%=i%>);updateSum();" onkeypress="return filterCharForFloat(window.event);" type="text" class="montantShort" style="text-align : right" name="md<%=i%>" value="<%=showValue?viewBean.getMontantDebit(i):""%>"/>
						<% } else { %>
						<input type="hidden" name="md<%=i%>" value=""/>
						<% } %>
						</td>

						<td style="vertical-align: middle; text-align: center;" class="mtdShortPadding">
						<% if (i == 0 || i >= viewBean.getShowRows() || !globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getMontantCredit(i))) { %>
						<input onchange="validateFloatNumber(this);clearDebitCredit('md', <%=i%>);updateSum();" onkeypress="return filterCharForFloat(window.event);" type="text" class="montantShort" style="text-align : right" name="mc<%=i%>" value="<%=showValue?viewBean.getMontantCredit(i):""%>"/>
						<% } else { %>
						<input type="hidden" name="mc<%=i%>" value=""/>
						<% } %>
						</td>

<!------------------------------------------------------------------------------
  Include la partie ventilation non spécifique de fin
-------------------------------------------------------------------------------->
<%@ include file="/lynxRoot/FR/include/ventilationEnd.jsp"%>

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
		<TR>
			<TD width="255">Etat</TD>
			<TD><INPUT type="text" name="csEtatOperation" value="<%=etatAdd%>" class="libelleLongDisabled" readonly="readonly"></TD>
		</TR>
		<%-- /tpl:put --%>
		<%@ include file="/theme/detail/bodyButtons.jspf"%>
		<%-- tpl:put name="zoneButtons" --%>
		<%
			if (viewBean.isJournalEditable() && "add".equals(request.getParameter("_method"))) {
		%>
		<INPUT type="button" name="btnNewFactureBvrOrange" value="BVR Orange" onClick="document.location.href='<%=actionNewFactureBvrOrange%>'">
		<INPUT type="button" name="btnNewFactureBvrRouge" value="BVR Rouge" onClick="document.location.href='<%=actionNewFactureBvrRouge%>'">
		<INPUT type="button" name="btnNewFactureVirement" value="Virement" onClick="document.location.href='<%=actionNewFactureVirement%>'">
		<INPUT type="button" name="btnNewFactureLsv" value=" LSV " onClick="document.location.href='<%=actionNewFactureLsv%>'">
		<INPUT type="button" name="btnNewFactureCaisse" value="Caisse" onClick="document.location.href='<%=actionNewFactureCaisse%>'">
		<%
			} else if( !"add".equals(request.getParameter("_method")) && !viewBean.isJournalEditable() && !viewBean.isJournalAnnule()) {
		%>
		<ct:ifhasright element="lynx.extourne.extourneProcess.extourner" crud="u">
			<INPUT type="button" name="extourner" value="Extourner" onClick="document.location.href='<%=request.getContextPath()%>/lynx?userAction=lynx.extourne.extourneProcess.extourner&idJournal=<%=viewBean.getIdJournal()%>&idSociete=<%=viewBean.getIdSociete()%>&idSection=<%=viewBean.getIdSection()%>&idOperation=<%=viewBean.getIdOperation()%>'">
		</ct:ifhasright>
		<% } %>

		<%-- /tpl:put --%>
		<%@ include file="/theme/detail/bodyErrors.jspf"%>
		<%-- tpl:put name="zoneEndPage" --%>

		<% if (viewBean.isRetourDepuisPyxis()) {%>
		<ct:menuChange displayId="menu" menuId="LX-MenuPrincipal"/>
		<% } %>

		<ct:menuChange displayId="options" menuId="LX-Journal" showTab="options" checkAdd="no">
			<ct:menuSetAllParams key='selectedId' value='<%=viewBean.getIdJournal()%>' checkAdd='no'/>
			<ct:menuSetAllParams key='idSociete' value='<%=viewBean.getIdSociete()%>' checkAdd='no'/>
			<ct:menuSetAllParams key='idJournal' value='<%=viewBean.getIdJournal()%>' checkAdd='no'/>
		</ct:menuChange>

		<%-- /tpl:put --%>
		<%@ include file="/theme/detail/footer.jspf"%>
		<%-- /tpl:insert --%>