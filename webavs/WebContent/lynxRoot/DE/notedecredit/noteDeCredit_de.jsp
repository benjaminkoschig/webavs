<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java"
	errorPage="/errorPage.jsp" import="globaz.globall.http.*"
	contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
<%@ include file="/theme/detail/header.jspf"%>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran = "GLX0016";
%>

<%@ page import="globaz.lynx.db.notedecredit.*"%>
<%@ page import="globaz.lynx.parser.*"%>
<%@ page import="globaz.globall.util.*"%>
<%@ page import="globaz.jade.client.util.*"%>
<%@page import="globaz.lynx.utils.LXSocieteDebitriceUtil"%>
<%@page import="globaz.lynx.utils.LXConstants"%>
<%@page import="globaz.lynx.db.operation.LXOperation"%>
<%@page import="globaz.lynx.db.section.LXSection"%>

<%
	LXNoteDeCreditViewBean viewBean = (LXNoteDeCreditViewBean) session.getAttribute("viewBean");
	selectedIdValue = viewBean.getIdOperation();

	boolean showValue = !(request.getParameter("forceNew") != null && !(new Boolean(request.getParameter("forceNew")).booleanValue()));

	actionNew += "&idSociete=" + viewBean.getIdSociete();
	actionNew += "&idJournal=" + viewBean.getIdJournal();
	actionNew += "&csTypeOperation=" + globaz.lynx.db.operation.LXOperationViewBean.CS_TYPE_NOTEDECREDIT_DEBASE;
	
	String tmpTypeOp = request.getParameter("csTypeOperation");
	if (!globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getCsTypeOperation())) {
		tmpTypeOp = viewBean.getCsTypeOperation();
	}
	
	if (!viewBean.isJournalEditable()) {
		bButtonNew = false;
		bButtonDelete = false;
		bButtonUpdate = false;
	}
%>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf"%>
<%-- tpl:put name="zoneScripts" --%>

<script type="text/javascript" src="<%=servletContext%>/scripts/dates.js"></script>
<SCRIPT	language="javascript">

var echeance = "0";

function add() {
    document.forms[0].elements('userAction').value="lynx.notedecredit.noteDeCredit.ajouter";
    document.getElementById("forceValid").value = "true";
    disableCentreCharge();
}

function upd() {
	document.forms[0].elements('userAction').value="lynx.notedecredit.noteDeCredit.modifier";
	disableCentreCharge();
}

function validate() {
    state = validateFields();
    
	disableCentreCharge();
	
    if (document.forms[0].elements('_method').value == "add") {
        document.forms[0].elements('userAction').value="lynx.notedecredit.noteDeCredit.ajouter";
        document.getElementById("forceValid").value = "true";
    } else {
        document.forms[0].elements('userAction').value="lynx.notedecredit.noteDeCredit.modifier";
    }

    return state;

}

function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="lynx.notedecredit.noteDeCredit.afficher";
}

function del() {
	if (window.confirm("Sie sind dabei, die ausgewählte Gutschrift zu löschen! Wollen Sie fortfahren?")) {
        document.forms[0].elements('userAction').value="lynx.notedecredit.noteDeCredit.supprimer";
        document.forms[0].submit();
    }
}

function updateEcheancePourTaux() {
//nothing
}

function updateDateEcheance(forceUpdate) {
	//nothing
}

function updateLibelleFacture(libelleFacture) {
	var element = document.getElementById("libelle");

	if(libelleFacture != "" && libelleFacture != null && element.value == "") {
		element.value = libelleFacture;
	}
}

function updateBlocage() {

	for (i = 0; i < Number(document.getElementById("csMotifBlocage").options.length-1); i++){ 			
		if(document.getElementById("csMotifBlocage").options[ Number(i+1) ].value == "-1") {	
			document.getElementById("csMotifBlocage").options[ Number(i+1) ] = null; 
		}
	}
	
	if(document.getElementById("estBloque").checked) {
		document.getElementById("csMotifBlocage").disabled = false;
		
	}else {
		var length = document.getElementById("csMotifBlocage").options.length ;
		document.getElementById("csMotifBlocage").options[length]= new Option('', '-1');
		document.getElementById("csMotifBlocage").options.selectedIndex = length;
		document.getElementById("csMotifBlocage").disabled = true;
	}
}

function init() {
	top.document.title = "Detail einer Gutschrift - " + top.location.href;

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
	cheminTabulationNoteDeCredit();
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
Detail einer Gutschrift
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
	boolean isLectureOptique = LXSocieteDebitriceUtil.isLectureOptique(objSession, viewBean.getIdSociete());
	
	String jspSectionLocation = servletContext + "/lynxRoot/autocomplete/section_select.jsp";
	String typeSection = LXSection.CS_TYPE_NOTEDECREDIT;
	String paramsSection = "forIdSociete=" + viewBean.getIdSociete() + "&forIdFournisseur=" + viewBean.getIdFournisseur() + "&forCsTypeSection=" + LXSection.CS_TYPE_FACTURE;
	
	boolean tmpValidateAutoComplete = false;
	if ("add".equalsIgnoreCase(request.getParameter("_method")) && (request.getParameter("_valid") == null || request.getParameter("_valid").equals("fail"))) {
		tmpValidateAutoComplete = true;
	}
	
	String selectCodeIsoMonnaieSelect = LXSelectBlockParser.getCsCodeIsoMonnaie(objSession, viewBean.getCsCodeIsoMonnaie());
	String params = "withAdrPmt=true&withInfoComptable=true&idSociete="+viewBean.getIdSociete()+"&typeFacture="+tmpTypeOp;
	String paramsLectureOptique = "idSociete="+viewBean.getIdSociete();
	
	String selectOrganeExecutionSelect = LXSelectBlockParser.getIdOrganeExecutionSelectBlock(objSession, viewBean.getIdSociete(), "", null);
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
  Include du template du bulletin : Note de crédit
-------------------------------------------------------------------------------->
<%@ include file="/lynxRoot/include/noteDeCredit.jsp"%>

<!------------------------------------------------------------------------------
  Affichage des informations de la note de crédit
-------------------------------------------------------------------------------->
<TABLE border="0" cellspacing="0" cellpadding="0" width="100%">
	<TBODY>
		<TR>
			<TD height="11" colspan="5"><hr size="3" width="100%"></TD>
		</TR>
		<TR>
			<TD width="255">Bezeichnung</TD>
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
			<TD>Datum</td>
			<TD><ct:FWCalendarTag name="dateFacture"
				doClientValidation="CALENDAR" value="<%=dateFacture%>" /> <script
				language="JavaScript">document.getElementById("dateFacture").onchange = function(){updateDateEcheance("true");};</script>
			</TD>
		</TR>
		<TR>
			<TD height="11" colspan="5"><hr size="3" width="100%"></TD>
		</TR>
		<TR>
			<TD>Interne Gutschrift-Nr.</TD>
			<%
				String tmpIdExterne = showValue?viewBean.getIdExterne():""; 
			%>
			<TD><ct:FWPopupList name="idExterne" onChange="" validateOnChange="false" params="<%=paramsSection%>" value="<%=tmpIdExterne%>" className="libelle" jspName="<%=jspSectionLocation%>" minNbrDigit="1" forceSelection="false" /> 
				<script language="JavaScript">
						element = document.getElementById("idExterne");
					  	element.onkeypress=function() {fillCell(this);}
				</script>
			</TD>
			<TD>&nbsp;</TD>
			<TD>Lieferant Gutschrift-Nr.</TD>
			<TD><INPUT type="text" onKeyPress="fillCell(this);"
				name="referenceExterne" style="width: 7cm" size="41" maxlength="40"
				value="<%=showValue ? viewBean.getReferenceExterne() : ""%>"
				class="libelleLong">
			</TD>
		</TR>
		<TR>
			<TD height="11" colspan="5"><hr size="3" width="100%"></TD>
		</TR>
        <TR>
            <TD>Gliederung :</TD>
            <TD colspan="2">&nbsp;</TD>
        </TR>
	</TBODY>
</TABLE>

<!------------------------------------------------------------------------------
  Include la partie ventilation non spécifique de début
-------------------------------------------------------------------------------->
<%@ include file="/lynxRoot/FR/include/ventilationBegin.jsp"%>
					
						<td style="vertical-align: middle; text-align: center;" class="mtdShortPadding">
						<% if (i == 0 || i >= viewBean.getShowRows() || !globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getMontantDebit(i))) { %>
						<input onchange="validateFloatNumber(this);clearDebitCredit('mc', <%=i%>);updateSum();" onkeypress="return filterCharForFloat(window.event);" type="text" class="montantShort" style="text-align : right" name="md<%=i%>" value="<%=showValue?viewBean.getMontantDebit(i):""%>"/>
						<% } else { %>
						<input type="hidden" name="md<%=i%>" value=""/>
						<% } %>
						</td>
						
						<td style="vertical-align: middle; text-align: center;" class="mtdShortPadding">
						<% if (i > 0 || i >= viewBean.getShowRows() || !globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getMontantCredit(i))) { %>
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
			<TD width="255">Status</TD>
			<TD><INPUT type="text" name="csEtatOperation" value="<%=etatAdd%>" class="libelleLongDisabled"></TD>
		</TR>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf"%>
<%-- tpl:put name="zoneButtons" --%>
<ct:ifhasright element="lynx.notedecredit.noteDeCredit.afficher" crud="u">
<% if( !"add".equals(request.getParameter("_method")) && !viewBean.isJournalEditable() && !viewBean.isJournalAnnule()) { %>
	<% if(!viewBean.isExisteNoteDeCreditLiee() && viewBean.isPossibleEncaissable() && LXOperation.CS_TYPE_NOTEDECREDIT_DEBASE.equals(viewBean.getCsTypeOperation())) { %>
		<INPUT type="button" name="encaisser" value="Einkassieren" onClick="document.location.href='<%=request.getContextPath()%>/lynx?userAction=lynx.notedecredit.noteDeCredit.encaisser&idJournal=<%=viewBean.getIdJournal()%>&idSociete=<%=viewBean.getIdSociete()%>&idFournisseur=<%=viewBean.getIdFournisseur()%>&idSection=<%=viewBean.getIdSection()%>&idOperation=<%=viewBean.getIdOperation()%>&libelleNote=<%=viewBean.getLibelle()%>&montantNote=<%=viewBean.getMontant()%>'">
	<% } 
		if(!viewBean.isExisteNoteDeCreditEncaissee() && LXOperation.CS_TYPE_NOTEDECREDIT_DEBASE.equals(viewBean.getCsTypeOperation())) { %>
		<INPUT type="button" name="lier" value="Verknüpfen" onClick="document.location.href='<%=request.getContextPath()%>/lynx?userAction=lynx.notedecreditlier.noteDeCreditLier.chercher&idJournal=<%=viewBean.getIdJournal()%>&idSociete=<%=viewBean.getIdSociete()%>&idFournisseur=<%=viewBean.getIdFournisseur()%>&idSection=<%=viewBean.getIdSection()%>&idOperation=<%=viewBean.getIdOperation()%>&idOperationSrc=<%=viewBean.getIdOperation()%>&libelleNote=<%=viewBean.getLibelle()%>&montantNote=<%=viewBean.getMontant()%>'">
	<% } %>
	<INPUT type="button" name="extourner" value="Stornieren" onClick="document.location.href='<%=request.getContextPath()%>/lynx?userAction=lynx.extourne.extourneProcess.extourner&idJournal=<%=viewBean.getIdJournal()%>&idSection=<%=viewBean.getIdSection()%>&idOperation=<%=viewBean.getIdOperation()%>&idSociete=<%=viewBean.getIdSociete()%>'">	
			
<% } %>	
</ct:ifhasright>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf"%>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf"%>
<%-- /tpl:insert --%>
