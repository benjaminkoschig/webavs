<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java"
	errorPage="/errorPage.jsp" import="globaz.globall.http.*"
	contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
<%@ include file="/theme/detail/header.jspf"%>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran = "GLX0036";
%>

<%@ page import="globaz.lynx.parser.*"%>
<%@ page import="globaz.globall.util.*"%>
<%@ page import="globaz.jade.client.util.*"%>
<%@page import="globaz.lynx.utils.LXSocieteDebitriceUtil"%>
<%@page import="globaz.lynx.utils.LXConstants"%>
<%@page import="globaz.lynx.db.operation.LXOperation"%>
<%@page import="globaz.lynx.db.section.LXSection"%>
<%@page import="globaz.lynx.db.extourne.LXExtourneViewBean"%>

<%
	LXExtourneViewBean viewBean = (LXExtourneViewBean) session.getAttribute("viewBean");
	selectedIdValue = viewBean.getIdOperation();

	boolean showValue = !(request.getParameter("forceNew") != null && !(new Boolean(request.getParameter("forceNew")).booleanValue()));

	String tmpTypeOp = request.getParameter("csTypeOperation");
	if (!globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getCsTypeOperation())) {
		tmpTypeOp = viewBean.getCsTypeOperation();
	}

	bButtonNew = false;
	if(!viewBean.isJournalAnnule()) {
		bButtonDelete = true;
	}else {
		bButtonDelete = false;
	}
	bButtonUpdate = false;

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

}

function upd() {

}

function validate() {

}

function cancel() {

}

function del() {
	if (window.confirm("Vous êtes sur le point de supprimer l'extourne sélectionnée! Voulez-vous continuer?")) {
        document.forms[0].elements('userAction').value="lynx.extourne.extourne.supprimer";
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
	top.document.title = "Détail d'une note de crédit - " + top.location.href;

	<%if (request.getParameter("forceValid") != null && request.getParameter("forceValid").equals("true")) {%>
		document.getElementById("_valid").value = "new";
	<%}%>
	
	hideRows();
	showMontantEtranger();
	
	document.getElementById("btnVal").disabled = true;

    updateSum();
	

}

function fillCell(cell) {

}
</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf"%>
<%-- tpl:put name="zoneTitle" --%>
D&eacute;tail d'une extourne
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
	String typeSection = LXSection.CS_TYPE_FACTURE;
	String paramsSection = "forIdSociete=" + viewBean.getIdSociete() + "&idFournisseur=" + viewBean.getIdFournisseur() + "&forCsTypeSection=" + typeSection;
	
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
	
	String actionAfficherOperationSrc = "lynx.facture.facture.afficher";
	if(LXOperation.CS_TYPE_NOTEDECREDIT_DEBASE.equals(viewBean.getCsTypeOperartionSrc())
		|| LXOperation.CS_TYPE_NOTEDECREDIT_ENCAISSEE.equals(viewBean.getCsTypeOperartionSrc())) {
		actionAfficherOperationSrc = "lynx.notedecredit.noteDeCredit.afficher";
	}else if (LXOperation.CS_TYPE_ESCOMPTE.equals(viewBean.getCsTypeOperartionSrc())) {
		actionAfficherOperationSrc = "lynx.escompte.escompte.afficher";
	}else if (LXOperation.CS_TYPE_PAIEMENT_BVR_ORANGE.equals(viewBean.getCsTypeOperartionSrc())
			|| LXOperation.CS_TYPE_PAIEMENT_BVR_ROUGE.equals(viewBean.getCsTypeOperartionSrc())
			|| LXOperation.CS_TYPE_PAIEMENT_LSV.equals(viewBean.getCsTypeOperartionSrc())
			|| LXOperation.CS_TYPE_PAIEMENT_CAISSE.equals(viewBean.getCsTypeOperartionSrc())
			|| LXOperation.CS_TYPE_PAIEMENT_VIREMENT.equals(viewBean.getCsTypeOperartionSrc())) {
		actionAfficherOperationSrc = "lynx.paiement.paiement.afficher";
	}
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
<%@ include file="/lynxRoot/include/extourne.jsp"%>

<!------------------------------------------------------------------------------
  Affichage des informations de la note de crédit
-------------------------------------------------------------------------------->
<TABLE border="0" cellspacing="0" cellpadding="0" width="100%">
	<TBODY>
		<TR>
			<TD height="11" colspan="5"><hr size="3" width="100%"></TD>
		</TR>
		<TR>
			<TD width="255">Libell&eacute;</TD>
			<TD><INPUT type="text"
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
			</TD>
		</TR>
		<TR>
			<TD>Date</td>
			<TD><ct:FWCalendarTag name="dateFacture" doClientValidation="CALENDAR" value="<%=dateFacture%>" /> 
			</TD>
		</TR>
		<TR>
			<TD height="11" colspan="5"><hr size="3" width="100%"></TD>
		</TR>
		<TR>
			<TD>N° de facture interne &nbsp; (<A href="<%=request.getContextPath()%>/lynx?userAction=<%=actionAfficherOperationSrc%>&idOperation=<%=viewBean.getIdOperationSrc()%>" class="link">Operation associée</A>)</TD>
			<TD><INPUT type="text" name="idExterne" style="width: 4cm" size="41" maxlength="40" value="<%=viewBean.getIdExterne()%>" class="libelleLongDisabled"></TD>
			<TD>&nbsp;</TD>
			<TD>N° de facture fournisseur</TD>
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
            <TD>Ventilation :</TD>
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
			<TD width="255">Etat</TD>
			<TD><INPUT type="text" name="csEtatOperation" value="<%=etatAdd%>" class="libelleLongDisabled"></TD>
		</TR>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf"%>
<%-- tpl:put name="zoneButtons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf"%>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf"%>
<%-- /tpl:insert --%>
