<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
		<%idEcran = "GCA0013"; %>
		<%@ page import="java.util.Enumeration" %>
		<%@ page import="globaz.osiris.db.comptes.*" %>
		<%@ page import="globaz.framework.util.*" %>
		<%@ page import="globaz.osiris.application.*" %>
		<%
	CAEcritureViewBean viewBean = (CAEcritureViewBean) session.getAttribute (globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
	userActionValue = "osiris.comptes.journalOperationEcriture.modifier";
	//selectedIdValue = viewBean.getIdJournal();
	selectedIdValue = viewBean.getIdOperation();
	// Interdire les modifications si le journal n'est pas mutable
	if (viewBean.getJournal() != null) {
      if (!viewBean.getJournal().isUpdatable()) {
	    bButtonNew = false;
	    bButtonUpdate = false;
	  } else {
		  bButtonNew = objSession.hasRight(userActionValue, globaz.framework.secure.FWSecureConstants.ADD);
	  }
	}
  	bButtonUpdate = (bButtonUpdate && viewBean.isUpdatable());
	bButtonDelete = (bButtonDelete && !viewBean.getEstComptabilise().booleanValue());
	bButtonNew = (bButtonNew && viewBean.hasRightAdd() && !viewBean.getEstComptabilise().booleanValue());
	actionNew = actionNew + "&idJournal="+viewBean.getIdJournal()+"&Etat="+viewBean.getEtat();//+"&Message="+viewBean.getMessage();

	int autoCompleteStart = globaz.osiris.parser.CAAutoComplete.getCompteAnnexeAutoCompleteStart(objSession);
%>

	<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<script type="text/javascript"
	src="osirisRoot/scripts/historiqueChamps.js">
</script>
<% 		if (!"1".equals(viewBean.getIdJournal())) { %>
		<ct:menuChange displayId="options" menuId="CA-JournalOperation" showTab="options">
			<ct:menuSetAllParams key="id" value="<%=viewBean.getIdJournal()%>"/>
			<% if ((viewBean.getJournal() != null) && (!viewBean.getJournal().isAnnule())) {%>
			<ct:menuActivateNode active="no" nodeId="journal_rouvrir"/>
			<% } else { %>
			<ct:menuActivateNode active="yes" nodeId="journal_rouvrir"/>
			<% } %>
		</ct:menuChange>
<% 	} %>

<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

$(function(){
	<%viewBean._synchroChgValUtili();%>
	historiqueChamps.init("#idExterneRoleEcran, #idExterneSectionEcran, #date, #idExterneRubriqueEcran, #anneeCotisation, #masseEcran, #taux, #libelle, #piece, #idCaisseProfessionnelleEcran, #montantEcran, #idExterneCompteCourantEcran",<%=CAOperation.convertHashMapForJQuery(viewBean.getMapValeurUtilisateur())%>);
		
   }) 
   
function add() {

	document.forms[0].idOperation.value="0";

	<%if (!globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getIdRoleEcran())) {%>
		document.forms[0].idRoleEcran.value="<%=viewBean.getIdRoleEcran()%>";
	<%}%>

	<%if (!globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getIdTypeSectionEcran())) {%>
		document.forms[0].idTypeSectionEcran.value="<%=viewBean.getIdTypeSectionEcran()%>";
	<%}%>

	document.forms[0].date.value="<%=viewBean.getDate()%>";
	document.forms[0].idExterneRubriqueEcran.value="<%=viewBean.getIdExterneRubriqueEcran()%>";
	document.forms[0].anneeCotisation.value="<%if (CAApplication.getApplicationOsiris().getCAParametres().isComptabiliteAvs())%><%=viewBean.getAnneeCotisation()%>";
	document.forms[0].masseEcran.value="<%if (CAApplication.getApplicationOsiris().getCAParametres().isComptabiliteAvs())%><%=viewBean.getMasseEcran()%>";
	document.forms[0].taux.value="<%if (CAApplication.getApplicationOsiris().getCAParametres().isComptabiliteAvs())%><%="".equalsIgnoreCase(globaz.globall.util.JANumberFormatter.formatZeroValues(viewBean.getTaux(),false,true))?"":viewBean.getTaux()%>";
	document.forms[0].codeDebitCreditEcran.value="<%=viewBean.getCodeDebitCreditEcran()%>";
	document.forms[0].libelle.value="<%=globaz.jade.client.util.JadeStringUtil.change(viewBean.getLibelle(), "\"", "\\\"")%>";
	document.forms[0].piece.value="<%=viewBean.getPiece()%>";
	document.forms[0].idExterneCompteCourantEcran.value="<%=viewBean.getIdExterneCompteCourantEcran()%>";

	document.forms[0].elements('userAction').value="osiris.comptes.journalOperationEcriture.ajouter";
	document.forms[0].idExterneRoleEcran.focus();
}

function upd() {
	document.forms[0].elements('userAction').value="osiris.comptes.journalOperationEcriture.modifier";
	document.forms[0].idExterneRoleEcran.focus();
}

function del() {
	if (window.confirm("Sie sind dabei, die ausgew?hlte Buchung zu l?schen! Wollen Sie fortfahren?")) {
        document.forms[0].elements('userAction').value="osiris.comptes.journalOperationEcriture.supprimer";
        document.forms[0].submit();
    }

}

function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="osiris.comptes.journalOperationEcriture.ajouter";
    else
        document.forms[0].elements('userAction').value="osiris.comptes.journalOperationEcriture.modifier";

    return state;

}
function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="osiris.comptes.journalOperationEcriture.afficher";
}
function init(){
	updateSectionsAutoCompleteLink();
    initAuxSection();

    <%
    	if (viewBean.getCompte() != null && viewBean.getCompte().isUseCaissesProf()) {
    %>
	    idCaisseProfessionnelleEcranPopupTag.disabled = true;
	<%
    	} else if (viewBean.getCompte() != null && !viewBean.getCompte().isUseCaissesProf()) {
    %>
    	idCaisseProfessionnelleEcranPopupTag.disabled = false;
    <%
    	}
    %>
}

function postInit() {
	document.getElementById("idLog").disabled = false;
}

// Les fontionnalites !!!

function jsRechercheRubriqueEcran(){
	document.forms[0].rechercheRubriqueEcran.value="true";
	getSl('osiris.comptes.journalOperationEcriture.afficher','osiris.comptes.rechercheRubrique.chercher', 'idCompte','');
}
function jsInitRechercheRubriqueEcran(){
	document.forms[0].rechercheRubriqueEcran.value="false";
}

function jsRechercheCompteCourantEcran(){
	document.forms[0].rechercheCompteCourantEcran.value="true";
	getSl('osiris.comptes.journalOperationEcriture.afficher','osiris.comptes.rechercheCompteCourant.chercher', 'idCompteCourant','' );
}
function jsInitRechercheCompteCourantEcran(){
	document.forms[0].rechercheCompteCourantEcran.value="false";
}

function quittancer() {
	if (document.forms[0]._quittanceLogEcran.checked == true)
		document.forms[0].quittanceLogEcran.value="on";
	else
		document.forms[0].quittanceLogEcran.value="";
}

top.document.title = "Konto - Detail einer Buchung " + top.location.href;

<!-- AUTO COMPLETE SECTION -->

<%
	String jspAffilieSelectLocation = servletContext + mainServletPath + "Root/affilie_select.jsp";
	String jspSectionsSelectLocation = servletContext + mainServletPath + "Root/sections_select.jsp";
	String jspSectionsAuxSelectLocation = servletContext + mainServletPath + "Root/sections_aux_select.jsp";

	String jspLocation = servletContext + mainServletPath + "Root/rubrique_select.jsp";
	String jspLocation1 = servletContext + mainServletPath + "Root/compteCourant_select.jsp";

	String jspCaissesProfSelectLocation = servletContext + mainServletPath + "Root/caissesprof_select.jsp";
%>

var tempIdSection = "";

var tempIdExterneRole = "";
var tempIdRole = "";

function updateIdCompteAnnexe(tag){
	if(tag.select && tag.select.selectedIndex != -1){
		if ((tag.select[tag.select.selectedIndex].selectedIdRole != "") && (document.getElementById('idRoleEcran').value != tag.select[tag.select.selectedIndex].selectedIdRole)) {
			document.getElementById('idRoleEcran').value = tag.select[tag.select.selectedIndex].selectedIdRole;
		}

		document.getElementById('descCompteAnnexe').value = tag.select[tag.select.selectedIndex].selectedCompteAnnexeDesc;

		document.getElementById('idCompteAnnexe').value = tag.select[tag.select.selectedIndex].selectedIdCompteAnnexe;
	} else {
		document.getElementById('idCompteAnnexe').value = "";
	}
}

function updateIdSection(tag){
	if(tag.select && tag.select.selectedIndex != -1){
		document.getElementById('idSection').value = tag.select[tag.select.selectedIndex].selectedIdSection;

		if ((tag.select[tag.select.selectedIndex].selectedIdTypeSection != "") && (document.getElementById('idTypeSectionEcran').value != tag.select[tag.select.selectedIndex].selectedIdTypeSection)) {
			document.getElementById('idTypeSectionEcran').value = tag.select[tag.select.selectedIndex].selectedIdTypeSection;
		}

		document.getElementById('descSection').value = tag.select[tag.select.selectedIndex].selectedSectionDesc;

		if (tag.select[tag.select.selectedIndex].selectedIdPrincipale == "true") {
			allowAuxSection();
		} else {
			blindAuxSection();
		}

		if (tag.select[tag.select.selectedIndex].selectedIdPlan != "0") {
			if (document.forms[0].elements('_method').value != "add") {
		 		var cible = "osiris?userAction=osiris.recouvrement.planRecouvrement.afficher&selectedId=" + tag.select[tag.select.selectedIndex].selectedIdPlan;
				document.getElementById('linkPlan').href = cible;
				document.getElementById('sursis').style.display='';
			} else {
				document.getElementById('idPlan').value=tag.select[tag.select.selectedIndex].selectedIdPlan;
				document.getElementById('sursisAdd').style.display='';
			}
		} else {
			document.getElementById('sursis').style.display='none';
			document.getElementById('sursisAdd').style.display='none';
		}
	} else {
		document.getElementById('idSection').value = "";
		document.getElementById('sursis').style.display='none';
		document.getElementById('sursisAdd').style.display='none';
	}
}

function updateIdCaissesProf(tag){
	if(tag.select && tag.select.selectedIndex != -1){
		document.getElementById('idCaisseProfessionnelle').value = tag.select[tag.select.selectedIndex].selectedIdCaissesProf;
		document.getElementById('caisseProfDescription').value = tag.select[tag.select.selectedIndex].selectedCaissesProfLibelle;
	} else {
		document.getElementById('idCaisseProfessionnelle').value = "";
		document.getElementById('caisseProfDescription').value = "";
	}
}

function updateIdSectionAux(tag){
	if(tag.select && tag.select.selectedIndex != -1){
		document.getElementById('idSectionAux').value = tag.select[tag.select.selectedIndex].selectedIdSectionAux;
		document.getElementById('idCompteAnnexeAuxDesc').value = tag.select[tag.select.selectedIndex].selectedIdCompteAnnexeAuxDesc;
	} else {
		document.getElementById('idSectionAux').value = "";
	}
}

function updateSectionsAutoCompleteLink() {
	tempIdExterneRole = document.getElementById('idExterneRoleEcran').value;
	if (document.getElementById('idRoleEcran') != null) {
		tempIdRole = document.getElementById('idRoleEcran').value;
	}
	idExterneSectionEcranPopupTag.updateJspName('<%=jspSectionsSelectLocation%>?tempIdExterneRole=' + tempIdExterneRole + '&tempIdRole=' + tempIdRole + '&like=');
	idCaisseProfessionnelleEcranPopupTag.updateJspName('<%=jspCaissesProfSelectLocation%>?tempIdExterneRole=' + tempIdExterneRole + '&tempIdRole=' + tempIdRole + '&like=');
}

function refreshSectionsAuxParams() {
	tempIdSection = document.getElementById('idSection').value;
	idExterneSectionAuxEcranPopupTag.updateJspName('<%=jspSectionsAuxSelectLocation%>?tempIdSection=' + tempIdSection + '&like=');
}

function updateRubrique(el) {
	if (el == null || el.value== "" || el.options[el.selectedIndex] == null)
		rubriqueManuelleOn();
	else {
		var elementSelected = el.options[el.selectedIndex];
		document.forms[0].idCompte.value = elementSelected.idCompte;
		document.forms[0].idExterneRubriqueEcran.value = elementSelected.idExterneRubriqueEcran;
		document.forms[0].rubriqueDescription.value = elementSelected.rubriqueDescription;
	}
}

function updateCompteCourant(el) {
	if (el == null || el.value== "" || el.options[el.selectedIndex] == null)
		rubriqueManuelleOn();
	else {
		var elementSelected = el.options[el.selectedIndex];
		document.forms[0].idCompteCourant.value = elementSelected.idCompteCourant;
		document.forms[0].idExterneCompteCourantEcran.value = elementSelected.idExterneCompteCourantEcran;
		document.forms[0].CCEcran.value = elementSelected.CCEcran;
	}
}

function compteCourantManuelleOn(){
	document.forms[0].idCompteCourant.value="";
	//document.forms[0].idExterneCompteCourantEcran.value="";
	document.forms[0].CCEcran.value="";
}

function rubriqueManuelleOn(){
	document.forms[0].idCompte.value="";
	//document.forms[0].idExterneRubriqueEcran.value="";
	document.forms[0].rubriqueDescription.value="";
}

function blindAuxSection() {
	document.getElementById('idExterneSectionAuxEcran').value = "";
	document.getElementById('idExterneSectionAuxEcran').disabled = true;
	document.getElementById("auxSection").style.display = "none";
}

function allowAuxSection() {
	document.getElementById('idExterneSectionAuxEcran').disabled = false;
	document.getElementById("auxSection").style.display = "block";
}

function initAuxSection() {
	if (document.getElementById('idExterneSectionAuxEcran').value == "") {
		blindAuxSection();
	} else {
		allowAuxSection();
	}
}

// stop hiding -->
</SCRIPT>
	<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Erfassung einer Buchung<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
            <input type="hidden" name="codeDebitCredit" value="<%=viewBean.getCodeDebitCredit()%>"/>
            <input type="hidden" name="codeMaster" value="<%=viewBean.getCodeMaster()%>"/>
            <input type="hidden" name="etat" value="<%=viewBean.getEtat()%>"/>
            <input type="hidden" name="idCompteEcran" value="<%=viewBean.getIdCompteEcran()%>"/>
            <input type="hidden" name="idContrepartie" value="<%=viewBean.getIdContrepartie()%>"/>
            <input type="hidden" name="idEcheancePlan" value="<%=viewBean.getIdEcheancePlan()%>"/>
            <input type="hidden" name="idTypeOperation" value="<%=viewBean.getIdTypeOperation()%>"/>
            <input type="hidden" name="nomEcran" value="<%=viewBean.getNomEcran()%>"/>

            <TR>
            <TD nowrap width="129" height="34">
              <input type="hidden" name="saisieEcran" value="true">
				<% if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getIdCompteAnnexe())) { %>
					<A href="osiris?userAction=osiris.comptes.apercuComptes.afficher&selectedId=<%=viewBean.getCompteAnnexe().getIdCompteAnnexe()%>">Compte</A>
				<% } else { %>
					Konto
				<% } %>
            </TD>
            <TD width="1" height="34">&nbsp;</TD>
            <TD width="147">
              <input type="hidden" name="idCompteAnnexe" value="<%=viewBean.getIdCompteAnnexe()%>" >

              <ct:FWPopupList
	           name="idExterneRoleEcran"
	           value="<%=viewBean.getIdExterneRoleEcran()%>"
	           className="libelle"
	           jspName="<%=jspAffilieSelectLocation%>"
	           autoNbrDigit="<%=autoCompleteStart%>"
	           size="25"
	           onChange="updateIdCompteAnnexe(tag);updateSectionsAutoCompleteLink();"
	           minNbrDigit="1"
	          />

            </TD>
            <TD width="4">&nbsp;</TD>
            <TD colspan="2">
              <select style="width:100%" name="idRoleEcran">
              	<%=CARoleViewBean.createOptionsTags(objSession, viewBean.getIdRoleEcran(), false)%>
              </select>

            </TD>
            <TD width="4">&nbsp;</TD>
            <TD width="58">&nbsp;</TD>
            <TD width="4">&nbsp;</TD>
            <TD nowrap height="34" width="161" colspan="2">
              <input type="text" name="descCompteAnnexe" maxlength="29" value="<%if (viewBean.getCompteAnnexe() != null) %><%=globaz.jade.client.util.JadeStringUtil.change(viewBean.getCompteAnnexe().getTiers().getNom(), "\"", "&quot;")%>" class="libelleLongDisabled" tabindex="-1" readonly>
            </TD>
          </TR>
          <TR>
            <TD nowrap width="129">
				<% if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getIdSection())) { %>
					<A href="osiris?userAction=osiris.comptes.apercuParSection.afficher&id=<%=viewBean.getSection().getIdSection()%>">Sektion</A>
				<% } else { %>
					Sektion
				<% } %>
			</TD>
            <TD width="1">&nbsp;</TD>
            <TD width="147">
              <input type="hidden" name="idSection" value="<%=viewBean.getIdSection()%>">

              <ct:FWPopupList
	           name="idExterneSectionEcran"
	           value="<%=viewBean.getIdExterneSectionEcran()%>"
	           className="libelle"
	           jspName="<%=jspSectionsSelectLocation%>"
	           size="25"
	           onChange="updateIdSection(tag);refreshSectionsAuxParams();"
	           minNbrDigit="1"
	           params="tempIdExterneRole=' + tempIdExterneRole + '&tempIdRole=' + tempIdRole + '"
	          />

            </TD>
            <TD width="4">&nbsp;</TD>
            <TD colspan="2">
              <select style="width:100%" name="idTypeSectionEcran">
                <%CATypeSection tempTypeSection;
				  CATypeSectionManager manTypeSection = new CATypeSectionManager();
				  manTypeSection.setSession(objSession);
				  manTypeSection.find();
				  for(int i = 0; i < manTypeSection.size(); i++){
				    	tempTypeSection = (CATypeSection)manTypeSection.getEntity(i);
						if(viewBean.getSection() == null && !viewBean.getIdTypeSectionEcran().equals(tempTypeSection.getIdTypeSection())) { %>
                <option value="<%=tempTypeSection.getIdTypeSection()%>"><%=tempTypeSection.getDescription()%></option>
				<%} else if(viewBean.getSection() == null && viewBean.getIdTypeSectionEcran().equals(tempTypeSection.getIdTypeSection())) { %>
                <option selected value="<%=tempTypeSection.getIdTypeSection()%>"><%=tempTypeSection.getDescription()%></option>
                <%} else if  (viewBean.getSection().getIdTypeSection().equalsIgnoreCase(tempTypeSection.getIdTypeSection())) { %>
                <option selected value="<%=tempTypeSection.getIdTypeSection()%>"><%=tempTypeSection.getDescription()%></option>
                <% } else { %>
                <option value="<%=tempTypeSection.getIdTypeSection()%>"><%=tempTypeSection.getDescription()%></option>
                <% } %>
                <% } %>
              </select>
            </TD>
            <TD width="4">&nbsp;</TD>
            <TD width="58">&nbsp;</TD>
            <TD width="4">&nbsp;</TD>
            <TD nowrap width="161" colspan="2">
              <input type="text" name="descSection" maxlength="30" value="<%if (viewBean.getSection() != null) {%><%=viewBean.getSection().getDescription()%><%}%>" class="libelleLongDisabled" readonly tabindex="-1">
            </TD>
          </TR>

          <tr id="sursis" <%if (!(viewBean.getSection() != null && !viewBean.getSection().getIdPlanRecouvrement().equals("0"))) {%> style='display:none' <%} %>>
          	<td>&nbsp;</td>
          	<td colspan="8" class="label">
	          	<font color='red'><strong>
	          		Diese Sektion ist gedeckt durch
	          		<a href="<%if (viewBean.getSection() != null && !viewBean.getSection().getIdPlanRecouvrement().equals("0")) {%>osiris?userAction=osiris.recouvrement.planRecouvrement.afficher&selectedId=<%=viewBean.getSection().getIdPlanRecouvrement() %><%} %>" id="linkPlan">
	          		die Zahlungsvereinbarung
	          		</a>
	          	</strong></font>
          	</td>
          </tr>
          <tr id="sursisAdd" style='display:none'>
          	<td>&nbsp;</td>
          	<td colspan="8" class="label">
	          	<font color='red'><strong>
	          		Diese Sektion ist gedeckt durch die Zahlungsvereinbarung :
					<INPUT style="border:solid 0px;background-color:Transparent;color=red" type="text" maxlength="8" id="idPlan" value="">
	          	</strong></font>
          	</td>
          </tr>

          <TR><TD nowrap colspan="11"><HR/></TD></TR>

          <TR>
            <TD nowrap width="129">Valutadatum</TD>
            <TD width="1">&nbsp;</TD>
            <TD width="147" colspan="9">
				<ct:FWCalendarTag name="date" doClientValidation="CALENDAR" value="<%=viewBean.getDate()%>"/>
            </TD>
          </TR>

          <TR>
            <TD nowrap width="129">Rubrik</TD>
            <TD width="1">&nbsp;</TD>

            <TD width="147">
            	<ct:FWPopupList name="idExterneRubriqueEcran"
            	onFailure="rubriqueManuelleOn();"
				onChange="updateRubrique(tag.select);"
				value="<%=viewBean.getIdExterneRubriqueEcran()%>"
				className="libelle"
				jspName="<%=jspLocation%>"
				minNbrDigit="1"
				forceSelection="true"
				validateOnChange="false"
				 />
			</TD>
            <TD colspan="2">&nbsp;
            <input type="hidden" name="idCompte" value="<%=viewBean.getIdCompte()%>" >
            </TD>
            <TD width="300">
              <input type="text" name="rubriqueDescription" size="30" value="<%if (viewBean.getCompte() != null) {%><%=viewBean.getCompte().getDescription()%><%}%>" class="libelleLongDisabled"  readonly tabindex="-1">
            </TD>
            <TD colspan="5">&nbsp;</TD>
          </TR>

          <TR>
            <TD nowrap width="129">Verbandskasse</TD>
            <TD width="1">&nbsp;</TD>
            <TD width="147">
            <input type="hidden" id="idCaisseProfessionnelle" name="idCaisseProfessionnelle" value="<%=viewBean.getIdCaisseProfessionnelle()%>" >

            <ct:FWPopupList
	           name="idCaisseProfessionnelleEcran"
	           value="<%=viewBean.getCaisseProfessionnelleNumero()%>"
	           className="libelle"
	           jspName="<%=jspCaissesProfSelectLocation%>"
	           onChange="updateIdCaissesProf(tag);"
	           minNbrDigit="1"
	           params="tempIdExterneRole=' + tempIdExterneRole + '&tempIdRole=' + tempIdRole + '"
	          />
            </TD>
            <TD colspan="2">&nbsp;</TD>
            <TD width="300">
              <input type="text" name="caisseProfDescription" size="30" value="<%=viewBean.getCaisseProfessionnelleLibelle()%>" class="libelleLongDisabled"  readonly tabindex="-1">
            </TD>
            <TD colspan="8">&nbsp;</TD>
          </TR>

		   <% if (CAApplication.getApplicationOsiris().getCAParametres().isComptabiliteAvs()) {%>
          <TR>
            <TD nowrap width="129">Beitragsjahr</TD>
            <TD width="1">&nbsp;</TD>
            <TD width="147">
              <input type="text" id="anneeCotisation" name="anneeCotisation" size="16" maxlength="15" value="<%if (!globaz.globall.util.JANumberFormatter.formatZeroValues(viewBean.getAnneeCotisation(),false,true).equalsIgnoreCase(""))%><%=viewBean.getAnneeCotisation()%>"  >
            </TD>
            <TD colspan="8">&nbsp;</TD>
          </TR>

          <TR>
            <TD nowrap width="129">Summe + Beitragssatz %</TD>
            <TD width="1">&nbsp;</TD>
            <TD colspan="3">
              <input type="text" id="masseEcran" name="masseEcran" onchange="validateFloatNumber(this)" onkeypress="filterCharForFloat(window.event)" size="16" maxlength="15" value="<%=viewBean.getMasseEcran()%>"  class="montant" >
              <input type="hidden" name="unsynchronizeSigneMasse" value="<%=viewBean.getUnsynchronizeSigneMasse().booleanValue()?"on":""%>"/>
              <%if(viewBean.getUnsynchronizeSigneMasse().booleanValue()){%>Signe invers?!<%}%>
            </TD>
            <TD width="300">
              <input type="text" id="taux" name="taux" onchange="validateFloatNumber(this,5)" onkeypress="filterCharForFloat(window.event)" size="16" maxlength="15" value="<%if (!globaz.globall.util.JANumberFormatter.formatZeroValues(viewBean.getTaux(),false,true).equalsIgnoreCase(""))%><%=viewBean.getTaux()%>"  class="taux" >
            </TD>
            <TD colspan="5">&nbsp;</TD>
          </TR>
		  <%}else{%>
		  <input type="hidden" name="anneeCotisation" value="" >
		  <input type="hidden" name="masseEcran" value="" >
		  <input type="hidden" name="taux" value="" >
		  <%}%>

          <TR>
            <TD nowrap width="129">Betrag</TD>
            <TD width="1">&nbsp;</TD>
            <TD colspan="3">
              <INPUT type="text" id="montantEcran" name="montantEcran" size="30"
		maxlength="30"
		value="<%="".equalsIgnoreCase(globaz.globall.util.JANumberFormatter.formatZeroValues(viewBean.getMontantEcran(),false,true))?"":viewBean.getMontantEcran()%>"
		class="montant">
            </TD>
            <TD width="300">
              <SELECT name="codeDebitCreditEcran" style="width : 2cm;" >
                <% if (viewBean.getCodeDebitCreditEcran().equalsIgnoreCase("D")){%>
                <OPTION value="D">Soll</OPTION>
                <option value="C" >Haben</option>
                <%}else{%>
                <option value="D" >Soll</option>
                <option value="C" selected>Haben</option>
                <%}%>
              </SELECT>
            </TD>
            <TD colspan="7">&nbsp;</TD>
          </TR>

          <TR>
            <TD nowrap width="129">Bemerkung</TD>
            <TD width="1">&nbsp;</TD>
            <TD colspan="5">
              <INPUT type="text"  id="libelle" name="libelle" size="64" maxlength="40"
									value="<%=globaz.jade.client.util.JadeStringUtil.change(viewBean.getLibelle(), "\"", "&quot;")%>">
            </TD>
            <TD colspan="8">&nbsp;</TD>
          </TR>

          <TR>
            <TD nowrap width="129">Buchhaltungsbeleg Nr.</TD>
            <TD width="1">&nbsp;</TD>
            <TD width="147">
              <input type="text" id="piece" name="piece" size="10" maxlength="10" value="<%=viewBean.getPiece()%>"  >
            </TD>
            <TD colspan="8">&nbsp;</TD>
          </TR>

          <TR>
            <TD nowrap width="129">Kontokorrent</TD>
            <TD width="1">&nbsp;</TD>
            <TD width="147">
            <ct:FWPopupList name="idExterneCompteCourantEcran" onFailure="compteCourantManuelleOn();"
				onChange="updateCompteCourant(tag.select);"
				value="<%=viewBean.getIdExterneCompteCourantEcran()%>"
				className="libelle"
				jspName="<%=jspLocation1%>"
				minNbrDigit="1"
				forceSelection="true"
				validateOnChange="false" /> </TD>
            <TD colspan="2">&nbsp;
              <input type="hidden" name="idCompteCourant" value="<%=viewBean.getIdCompteCourant()%>" >
              <input type="hidden" name="rechercheCompteCourantEcran" value="<%=viewBean.getRechercheCompteCourantEcran()%>" >
            </TD>
            <TD width="300">
              <input type="text" name="CCEcran" size="30" maxlength="30" value="<%if(viewBean.getCompteCourant() != null) {%><%=viewBean.getCompteCourant().getRubrique().getDescription()%><% } %>" class="inputDisabled"  readonly tabindex="-1">
            </TD>
            <TD colspan="5">&nbsp;</TD>
          </TR>

          <TR id="auxSection">
          <TD width="91">Hilfssektion</TD>
          <TD width="1">&nbsp;</TD>
          <TD nowrap width="270">
	          <input type="hidden" name="idSectionAux" value="<%=viewBean.getIdSectionAux()%>">

          	<ct:FWPopupList
	           name="idExterneSectionAuxEcran"
	           value="<%=viewBean.getIdExterneSectionAuxEcran()%>"
	           className="libelle"
	           jspName="<%=jspSectionsAuxSelectLocation%>"
	           size="25"
	           onChange="updateIdSectionAux(tag);"
	           minNbrDigit="1"
	           params="tempIdSection =' + tempIdSection  + '"
	          />

          </TD>
            <TD colspan="2">&nbsp;</TD>
            <TD nowrap>
            	<input type="text" name="idCompteAnnexeAuxDesc" size="30" maxlength="30" value="<%=viewBean.getIdCompteAnnexeAuxDesc()%>" class="inputDisabled"  readonly tabindex="-1">
            </TD>
          </TR>

          <TR>
            <TD nowrap width="129">Journal</TD>
            <TD width="1">&nbsp;</TD>
            <TD colspan="4">
              <input type="hidden" name="idJournal" value="<%=viewBean.getIdJournal()%>">
              <%	globaz.osiris.db.comptes.CAJournal journal = viewBean.getJournal();              %>
              <input type="text" name="journalEcran" size="30" maxlength="30" value="<%=viewBean.getIdJournal()%> - <%=journal==null?"":journal.getLibelle()%>" class="inputDisabled" readonly tabindex="-1">
            </TD>
            <TD width="4">&nbsp;</TD>
            <TD width="58">
			<%
			Object[] journalMethodsName = new Object[]{
				new String[]{"setIdJournal","getIdJournal"}
			};
			%>
			<ct:FWSelectorTag
				name="JournalSelector"

				methods="<%=journalMethodsName%>"
				providerApplication ="osiris"
				providerPrefix="CA"
				providerAction ="osiris.comptes.apercuJournal.chercher"
				/>
            </TD>
            <TD colspan="3">&nbsp;</TD>
          </TR>

          <TR>
            <TD nowrap width="129">Status</TD>
            <TD width="1">&nbsp;</TD>
            <TD colspan="4">
              <input type="text" name="etatJournal" size="30" maxlength="30" value="<%=viewBean.getUcEtat().getLibelle()%>" class="inputDisabled"  readonly tabindex="-1">
            </TD>
            <TD colspan="5">&nbsp;</TD>
          </TR>

          <TR>
            <TD nowrap width="129">Mitteilung</TD>
            <TD width="1">&nbsp;</TD>
            <TD colspan="9">
              <select name="idLog" style="width : 16cm;" class="libelleErrorLongDisabled" tabindex="-1">
	                <%if (viewBean.getLog() != null) {%>
	                <%Enumeration e = viewBean.getLog().getMessagesToEnumeration();
						while (e.hasMoreElements()){
							FWMessage msg = (FWMessage)e.nextElement();%>
		                <%if(viewBean.getIdLog().equals(msg.getIdLog())) {%>
		                    <option value="<%=msg.getIdLog()%>" selected ><%=msg.getMessageText()%></option>
		                 <%} else {%>
		                    <option value="<%=msg.getIdLog()%>"><%=msg.getMessageText()%></option>
		                <% } %>
	                <% } %>
                <% } %>
              </select>
            </TD>
          </TR>

          <TR>
            <TD nowrap width="129">Quittieren</TD>
            <TD width="1">&nbsp;</TD>
            <TD colspan="9">
              <input type="checkbox" name="_quittanceLogEcran" value="on" disabled  onClick="quittancer()">
			  <input type="hidden" name="quittanceLogEcran" value="">
            </TD>
          </TR>

          <TR><TD nowrap colspan="11"><HR/></TD></TR>

          <tr>
            <td nowrap width="129">Nr. Sammelbuchung</td>
            <td width="1">&nbsp;</td>
            <td width="147">
              <input type="text" name="noEcritureCollective" size="10" maxlength="10" value="<%if (!globaz.globall.util.JANumberFormatter.formatZeroValues(viewBean.getNoEcritureCollective(),false,true).equalsIgnoreCase(""))%><%=viewBean.getNoEcritureCollective()%>" class="numeroDisabled"  readonly tabindex="-1">
            </td>
            <td colspan="4">&nbsp;</td>
            <td width="58">FB Journal</td>
            <td width="4">&nbsp;</td>
            <td colspan="2">
              <input type="text" name="noJournalEcran" size="30" maxlength="30" value="<%=journal==null?"":journal.getNumeroJournalComptaGen()%>" class="inputDisabled"  readonly tabindex="-1">
            </td>
          </tr>

          <TR>
            <TD nowrap width="129">Identifikation</TD>
            <TD width="1">&nbsp;</TD>
            <TD width="147">
              <input type="text" name="idOperation" size="10" maxlength="10" value="<%=viewBean.getIdOperation()%>" class="numeroDisabled"  readonly tabindex="-1">
            </TD>
            <TD colspan="8">&nbsp;</TD>
          </TR>

          <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <SCRIPT>
		//mettreTauxBase();
		</SCRIPT> <%	}
%> <%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>