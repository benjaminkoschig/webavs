<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java"
	errorPage="/errorPage.jsp" import="globaz.globall.http.*"
	contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
<%@ include file="/theme/detail/header.jspf"%>
<%-- tpl:put name="zoneInit" --%>
<%@ page import="globaz.lynx.db.escompte.*"%>
<%@page import="globaz.lynx.db.operation.LXOperation"%>
<%@page import="globaz.lynx.db.section.LXSection"%>
<%@ page import="globaz.lynx.parser.*"%>
<%@ page import="globaz.globall.util.*"%>
<%@ page import="globaz.jade.client.util.*"%>
<%@page import="globaz.lynx.utils.LXSocieteDebitriceUtil"%>
<%@page import="globaz.lynx.utils.LXConstants"%>

<%
	idEcran = "GLX0029";
	LXEscompteViewBean viewBean = (LXEscompteViewBean) session.getAttribute("viewBean");
	selectedIdValue = viewBean.getIdOperation();

	boolean showValue = true;
	
	String tmpTypeOp = request.getParameter("csTypeOperation");
	if (!globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getCsTypeOperation())) {
		tmpTypeOp = viewBean.getCsTypeOperation();
	}
	
	bButtonDelete = false;
	
	if (!JadeStringUtil.isIntegerEmpty(viewBean.getIdJournal())|| !LXOperation.CS_ETAT_OUVERT.equals(viewBean.getCsEtat())) {
		bButtonUpdate = false;
	}
%>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf"%>
<%-- tpl:put name="zoneScripts" --%>

<script type="text/javascript" src="<%=servletContext%>/scripts/dates.js"></script>

<SCRIPT	language="javascript"><!--

var echeance = "0";

function add() {

}

function upd() {
	document.forms[0].elements('userAction').value="lynx.escompte.escompte.modifier";
	disableCentreCharge();
	disableChamps();
	updateBlocage();
	document.getElementById('montant').focus();
}

function validate() {
    state = validateFields();
    
	disableCentreCharge();
	
    document.forms[0].elements('userAction').value="lynx.escompte.escompte.modifier";

    return state;

}

function cancel() {
	document.forms[0].elements('userAction').value="lynx.escompte.escompte.afficher";
}

function del() {
}

function updateEcheancePourTaux() {

}

function updateDateEcheance(forceUpdate) {

}

function updateLibelleFacture(libelleFacture) {

}

function updateBlocage() {
	for (i = 0; i < Number(document.getElementById("csMotifBlocage").options.length-1); i++){ 			
		if(document.getElementById("csMotifBlocage").options[ Number(i+1) ].value == "-1") {	
			document.getElementById("csMotifBlocage").options[ Number(i+1) ] = null; 
		}
	}
	var length = document.getElementById("csMotifBlocage").options.length ;
	document.getElementById("csMotifBlocage").options[length]= new Option('', '-1');
	document.getElementById("csMotifBlocage").options.selectedIndex = length;
	document.getElementById("csMotifBlocage").disabled = true;

	document.getElementById("estBloque").disabled = true;
}

function init() {
	top.document.title = "D?tail d'un escompte - " + top.location.href;

	<%if (request.getParameter("forceValid") != null && request.getParameter("forceValid").equals("true")) {%>
	document.getElementById("_valid").value = "new";
	<%}%>
	
	hideRows();
	updateSum();
	postInit();
}

function postInit() {
	disableChamps();
	cheminTabulationEscompte();
	updateBlocage();
}

function disableChamps() {
		
	//D?sactivation du motif
	document.getElementById("motif").className = "libelleCaisseDisabled";
	document.getElementById("motif").readOnly = "readOnly";

	//Impossibilit? de changer la devise
	document.getElementById("csCodeIsoMonnaie").disabled = true;

	//Desactivation du choix du fournisseur
	document.getElementById("idExterneFournisseur").disabled = true;
}

function fillCell(cell) {

}
--></SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf"%>
<%-- tpl:put name="zoneTitle" --%>
D&eacute;tail d'un escompte
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf"%>
<%-- tpl:put name="zoneMain" --%>

<%
	// Initialisation des variables suivant si on est en ajout ou en modif
	String dateFacture = viewBean.getDateFacture();
	
	int autoCompleteStart = globaz.helios.parser.CGAutoComplete.getAutoCompleteAutoStart();
	
	String jspSectionLocation = servletContext + "/lynxRoot/autocomplete/section_select.jsp";
	String typeSection = LXSection.CS_TYPE_FACTURE;
	String paramsSection = "forIdSociete=" + viewBean.getIdSociete() + "&idFournisseur=" + viewBean.getIdFournisseur()  + "&forCsTypeSection=" + typeSection;
		
	String selectCodeIsoMonnaieSelect = LXSelectBlockParser.getCsCodeIsoMonnaie(objSession, viewBean.getCsCodeIsoMonnaie());
	
	//Pour la ventilation
	String jspLocation = servletContext + "/lynxRoot/autocomplete/compte_select.jsp";
	String params2 = "forDate=" + globaz.globall.util.JACalendar.todayJJsMMsAAAA() + "&idMandat=" + viewBean.getIdMandat();

	String jspLocationLibelle = servletContext + "/heliosRoot/" + languePage + "/comptes/label_select.jsp";
	String paramsLibelle = "idMandat=" + viewBean.getIdMandat() + "&langue=" + languePage;
		
	String tmpIdExterneFournisseur = viewBean.getIdExterneFournisseur();
	
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
<%@ include file="/lynxRoot/include/escompte.jsp"%>

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
				<INPUT type="hidden" name="idOrganeExecution" value="<%=viewBean.getIdOrganeExecution()%>"/>
				<INPUT type="text" name="libelleOrganeExecution" style="width: 2.5cm" size="41" maxlength="40" value="<%=viewBean.getOrganeExecutionLibelle()%>" class="libelleLongDisabled">
			</TD>
			<TD>&nbsp;</TD>
			<TD>Taux d'escompte &nbsp;
				<% if(!JadeStringUtil.isBlankOrZero(viewBean.getIdOperationLiee())) { %>
					(<A href="<%=request.getContextPath()%>/lynx?userAction=lynx.paiement.paiement.afficher&idOperation=<%=viewBean.getIdOperationLiee()%>" class="link">Paiement associ?</A>)
				<% } %>			
			</TD>
			<TD><INPUT type="text" id="tauxEscompte" name="tauxEscompte" style="width: 2.5cm" size="11" maxlength="10" value="<%=viewBean.getTauxEscompte()%>" class="libelleLongDisabled">%</TD>
		</TR>
		<TR>
			<TD>Libell&eacute;</TD>
			<TD><INPUT type="text" name="libelle" style="width: 7cm" size="41" maxlength="40" value="<%=viewBean.getLibelle()%>" class="libelleLong" tabindex="1"></TD>
			<TD>&nbsp;</TD>
			<% String strEstBloque = viewBean.getEstBloque().booleanValue() ? "checked=\"checked\"" : ""; %>
			<TD>Bloquer paiement</TD>
			<TD><input type="checkbox" id="estBloque" onclick="javascript:updateBlocage();" name="estBloque" <%= strEstBloque %>/></TD>
		</TR>
		<TR>
			<TD>Date facture</td>
			<TD><INPUT type="text" name="dateFacture" style="width: 2.5cm" size="41" maxlength="40" value="<%=viewBean.getDateFacture()%>" class="libelleLongDisabled"></TD>
			<TD>&nbsp;</TD>
			<TD>Motif du blocage</TD>
			<%
				String tmpCsMotifBlocage = showValue?viewBean.getCsMotifBlocage():"";
			%>
			<TD><ct:FWCodeSelectTag name="csMotifBlocage" defaut="<%=tmpCsMotifBlocage%>" codeType="LXMOTIFBL" wantBlank="true" /></TD>
		</TR>
		<TR>
			<TD height="11" colspan="5"><hr size="3" width="100%"></TD>
		</TR>
		<TR>
			<TD>N? de facture interne &nbsp; (<A href="<%=request.getContextPath()%>/lynx?userAction=lynx.facture.facture.afficher&idOperation=<%=viewBean.getIdOperationSrc()%>" class="link">Facture associ?e</A>)</TD>
			<TD><INPUT type="text" name="idExterne" style="width: 4cm" size="41" maxlength="40" value="<%=viewBean.getIdExterne()%>" class="libelleLongDisabled"></TD>
			<TD>&nbsp;</TD>
			<TD>N? de facture fournisseur</TD>
			<TD><INPUT type="text" name="referenceExterne" style="width: 7cm" size="41" maxlength="40"	value="<%=viewBean.getReferenceExterne()%>"	class="libelleLongDisabled"></TD>
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
  Include la partie ventilation non sp?cifique de d?but
-------------------------------------------------------------------------------->
<%@ include file="/lynxRoot/FR/include/ventilationBegin.jsp"%>
					
						<td style="vertical-align: middle; text-align: center;" class="mtdShortPadding">
						<% if (i == 0 || i >= viewBean.getShowRows() || !globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getMontantDebit(i))) { %>
						<input onchange="validateFloatNumber(this);clearDebitCredit('mc', <%=i%>);updateSum();" onkeypress="return filterCharForFloat(window.event);" type="text" class="montantShort" style="text-align : right" name="md<%=i%>" value="<%=viewBean.getMontantDebit(i)%>"/>
						<% } else { %>
						<input type="hidden" name="md<%=i%>" value=""/>
						<% } %>
						</td>
						
						<td style="vertical-align: middle; text-align: center;" class="mtdShortPadding">
						<% if (i > 0 || i >= viewBean.getShowRows() || !globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getMontantCredit(i))) { %>
						<input onchange="validateFloatNumber(this);clearDebitCredit('md', <%=i%>);updateSum();" onkeypress="return filterCharForFloat(window.event);" type="text" class="montantShort" style="text-align : right" name="mc<%=i%>" value="<%=viewBean.getMontantCredit(i)%>"/>
						<% } else { %>
						<input type="hidden" name="mc<%=i%>" value=""/>
						<% } %>
						</td>
						
<!------------------------------------------------------------------------------
  Include la partie ventilation non sp?cifique de fin
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
			<TD><INPUT type="text" name="csEtatOperation" value="<%=viewBean.getUcEtat().getLibelle()%>" class="libelleLongDisabled"></TD>
		</TR>
		
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf"%>
<%-- tpl:put name="zoneButtons" --%>

		<% if( !"add".equals(request.getParameter("_method")) && !viewBean.isJournalEditable() && !viewBean.isJournalAnnule() && !JadeStringUtil.isIntegerEmpty(viewBean.getIdJournal())) { %>
		<INPUT type="button" name="extourner" value="Extourner" onClick="document.location.href='<%=request.getContextPath()%>/lynx?userAction=lynx.extourne.extourneProcess.extourner&idJournal=<%=viewBean.getIdJournal()%>&idSociete=<%=viewBean.getIdSociete()%>&idSection=<%=viewBean.getIdSection()%>&idOperation=<%=viewBean.getIdOperation()%>'">	
		<% } %>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf"%>
<%-- tpl:put name="zoneEndPage" --%>		

		<% if (!JadeStringUtil.isBlank(request.getParameter("idOrdreGroupe")) && LXOperation.CS_ETAT_OUVERT.equals(viewBean.getCsEtat())) { %>
		<ct:menuChange displayId="options" menuId="LX-OrdreGroupe" showTab="options">
			<ct:menuSetAllParams key="selectedId" value='<%=viewBean.getIdOrdreGroupe()%>' checkAdd="no"/>
			<ct:menuSetAllParams key="idSociete" value='<%=viewBean.getIdSociete()%>' checkAdd="no"/>
			<ct:menuSetAllParams key="idOrdreGroupe" value='<%=viewBean.getIdOrdreGroupe()%>' checkAdd="no"/>
			<ct:menuSetAllParams key="idOrganeExecution" value='<%=viewBean.getIdOrganeExecution()%>' checkAdd="no"/>
		</ct:menuChange>
		<% } %>
		
		<% if (!JadeStringUtil.isBlank(request.getParameter("idJournal")) && LXOperation.CS_ETAT_COMPTABILISE.equals(viewBean.getCsEtat())) { %>
		<ct:menuChange displayId="options" menuId="LX-Journal" showTab="options" checkAdd="no">
			<ct:menuSetAllParams key='selectedId' value='<%=viewBean.getIdJournal()%>' checkAdd='no'/>
			<ct:menuSetAllParams key='idSociete' value='<%=viewBean.getIdSociete()%>' checkAdd='no'/>
			<ct:menuSetAllParams key='idJournal' value='<%=viewBean.getIdJournal()%>' checkAdd='no'/>
		</ct:menuChange>
		<% } %>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf"%>
<%-- /tpl:insert --%>