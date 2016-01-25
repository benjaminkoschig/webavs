<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA0017"; %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ page import="java.util.Enumeration" %>

<%@ page import="globaz.osiris.db.comptes.*" %>
<%@ page import="globaz.osiris.db.ordres.*" %>
<%@ page import="globaz.framework.util.*" %>
<%
	CAPaiementBVRViewBean viewBean = (CAPaiementBVRViewBean) session.getAttribute (globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
	userActionValue = "osiris.comptes.journalOperationPaiementBVR.modifier";
	selectedIdValue = viewBean.getIdOperation();
	// Interdire les modifications si le journal n'est pas mutable
	if (viewBean.getJournal() != null) {
      if (!viewBean.getJournal().isUpdatable()) {
	    bButtonNew = false;
	    bButtonUpdate = false;
	  } else {
		  bButtonNew = true;
	  }
	}
  	bButtonUpdate = (bButtonUpdate && viewBean.isUpdatable());
	bButtonDelete = (bButtonDelete && !viewBean.getEstComptabilise().booleanValue());
	bButtonNew = (bButtonNew && viewBean.hasRightAdd() && !viewBean.getEstComptabilise().booleanValue());
	actionNew = actionNew + "&idJournal="+viewBean.getIdJournal()+"&Etat="+viewBean.getEtat();

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
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<ct:menuChange displayId="options" menuId="CA-JournalOperation" showTab="options">
	<ct:menuSetAllParams key="id" value="<%=viewBean.getIdJournal()%>"/>
	<% if ((viewBean.getJournal() != null) && (!viewBean.getJournal().isAnnule())) {%>
	<ct:menuActivateNode active="no" nodeId="journal_rouvrir"/>
	<% } else { %>
	<ct:menuActivateNode active="yes" nodeId="journal_rouvrir"/>
	<% } %>
</ct:menuChange>

<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

$(function(){
	<%viewBean._synchroChgValUtili();%>
	historiqueChamps.init("#idExterneRoleEcran, #idExterneSectionEcran, #date, #idExterneRubriqueEcran, #libelle, #piece,  #montantEcran, #idExterneCompteCourantEcran, #dateTraitement, #dateDepot, #dateInscription, #genreTransaction, #referenceBVR, #referenceInterne",<%=CAOperation.convertHashMapForJQuery(viewBean.getMapValeurUtilisateur())%>);
		
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
	document.forms[0].codeDebitCreditEcran.value="<%=viewBean.getCodeDebitCreditEcran()%>";
	document.forms[0].libelle.value="<%=globaz.jade.client.util.JadeStringUtil.change(viewBean.getLibelle(), "\"", "\\\"")%>";
	document.forms[0].piece.value="<%=viewBean.getPiece()%>";
	document.forms[0].idExterneCompteCourantEcran.value="<%=viewBean.getIdExterneCompteCourantEcran()%>";
	document.forms[0].dateTraitement.value="<%=viewBean.getDateTraitement()%>";
	document.forms[0].dateDepot.value="<%=viewBean.getDateDepot()%>";
	document.forms[0].dateInscription.value="<%=viewBean.getDateInscription()%>";
	document.forms[0].idOrganeExecution.value="<%=viewBean.getIdOrganeExecution()%>";
	// ald, mise en commentaire car on se trouve sur un journal dont on doit affiche l'état
	//document.forms[0].etatJournal.value="";
  	document.forms[0].elements('userAction').value="osiris.comptes.journalOperationPaiementBVR.ajouter";
	document.forms[0].idExterneRoleEcran.focus();
}

function upd() {
  document.forms[0].elements('userAction').value="osiris.comptes.journalOperationPaiementBVR.modifier";
	document.forms[0].idExterneRoleEcran.focus();
}

function del() {
	if (window.confirm("Vous êtes sur le point de supprimer le paiement BVR sélectionné! Voulez-vous continuer?")) {
        document.forms[0].elements('userAction').value="osiris.comptes.journalOperationPaiementBVR.supprimer";
        document.forms[0].submit();
    }

}

function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="osiris.comptes.journalOperationPaiementBVR.ajouter";
    else
        document.forms[0].elements('userAction').value="osiris.comptes.journalOperationPaiementBVR.modifier";

    return state;

}
function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="osiris.comptes.journalOperationPaiementBVR.afficher";
}
function init(){}

function postInit() {
	document.getElementById("idLog").disabled = false;
}

// Les fonctionaliteee

function jsRechercheRubriqueEcran(){
	document.forms[0].rechercheRubriqueEcran.value="true";
	getSl('osiris.comptes.journalOperationPaiementBVR.afficher','osiris.comptes.rechercheRubrique.chercher', 'idCompte','' );
}
function jsInitRechercheRubriqueEcran(){
	document.forms[0].rechercheRubriqueEcran.value="false";
}

function jsRechercheCompteCourantEcran(){
	document.forms[0].rechercheCompteCourantEcran.value="true";
	getSl('osiris.comptes.journalOperationPaiementBVR.afficher','osiris.comptes.rechercheCompteCourant.chercher', 'idCompteCourant','' );
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
function rubriqueManuelleOn(){
	document.forms[0].idCompte.value="";
	//document.forms[0].idExterneRubriqueEcran.value="";
	document.forms[0].rubriqueDescription.value="";
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
top.document.title = "Comptes - détail d'un paiement BVR - " + top.location.href;

<!-- AUTO COMPLETE SECTION -->

<%
	String jspAffilieSelectLocation = servletContext + mainServletPath + "Root/affilie_select.jsp";
	String jspSectionsSelectLocation = servletContext + mainServletPath + "Root/sections_select.jsp";
	String jspSectionsAuxSelectLocation = servletContext + mainServletPath + "Root/sections_aux_select.jsp";
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

function updateIdSectionAux(tag){
	if(tag.select && tag.select.selectedIndex != -1){
		document.getElementById('idSectionAux').value = tag.select[tag.select.selectedIndex].selectedIdSectionAux;
		document.getElementById('idCompteAnnexeAuxDesc').value = tag.select[tag.select.selectedIndex].selectedIdCompteAnnexeAuxDesc;
	}
}

function updateSectionsAutoCompleteLink() {
	tempIdExterneRole = document.getElementById('idExterneRoleEcran').value;
	if (document.getElementById('idRoleEcran') != null) {
		tempIdRole = document.getElementById('idRoleEcran').value;
	}

	idExterneSectionEcranPopupTag.updateJspName('<%=jspSectionsSelectLocation%>?tempIdExterneRole=' + tempIdExterneRole + '&tempIdRole=' + tempIdRole + '&like=');
}

function refreshSectionsAuxParams() {
	tempIdSection = document.getElementById('idSection').value;
	idExterneSectionAuxEcranPopupTag.updateJspName('<%=jspSectionsAuxSelectLocation%>?tempIdSection=' + tempIdSection + '&like=');
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
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Détail d'un paiement BVR<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
			  <input type="hidden" name="saisieEcran" value="true" tabindex="-1" >
              <input type="hidden" name="anneeCotisation" value="<%=viewBean.getAnneeCotisation()%>"/>
              <input type="hidden" name="codeDebitCredit" value="<%=viewBean.getCodeDebitCredit()%>"/>
              <input type="hidden" name="codeMaster" value="<%=viewBean.getCodeMaster()%>"/>
              <input type="hidden" name="codeMaster" value="<%=viewBean.getEtat()%>"/>
              <input type="hidden" name="idCompteEcran" value="<%=viewBean.getIdCompteEcran()%>"/>
              <input type="hidden" name="idContrepartie" value="<%=viewBean.getIdContrepartie()%>"/>
              <input type="hidden" name="idEcheancePlan" value="<%=viewBean.getIdEcheancePlan()%>"/>
              <input type="hidden" name="idTypeOperation" value="<%=viewBean.getIdTypeOperation()%>"/>
              <input type="hidden" name="masse" value="<%=viewBean.getMasse()%>"/>
              <input type="hidden" name="masseEcran" value="<%=viewBean.getMasseEcran()%>"/>
              <input type="hidden" name="montant" value="<%=viewBean.getMontant()%>"/>
              <input type="hidden" name="noEcritureCollective" value="<%=viewBean.getNoEcritureCollective()%>"/>
              <input type="hidden" name="nomEcran" value="<%=viewBean.getNomEcran()%>"/>
              <input type="hidden" name="taux" value="<%=viewBean.getTaux()%>"/>
              <input type="hidden" name="returnview" value="<%=request.getParameter("returnview")%>"/>

          <TR>
            <TD nowrap width="129" height="34">
				<% if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getIdCompteAnnexe())) { %>
					<A href="osiris?userAction=osiris.comptes.apercuComptes.afficher&selectedId=<%=viewBean.getCompteAnnexe().getIdCompteAnnexe()%>">Compte</A>
				<% } else { %>
					Compte
				<% } %>
			</TD>
            <TD width="147">
              <input type="hidden" name="idCompteAnnexe" value="<%=viewBean.getIdCompteAnnexe()%>" tabindex="-1" >

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
            <TD width="7">&nbsp;</TD>
            <TD width="300">
              <select name="idRoleEcran" style="width:100%">
              	<%=CARoleViewBean.createOptionsTags(objSession, viewBean.getIdRoleEcran(), false)%>
              </select>
            </TD>
            <TD nowrap height="34" align="right">
              <input type="text" name="descCompteAnnexe" size="30" maxlength="30" value="<%if (viewBean.getCompteAnnexe() != null) %><%=globaz.jade.client.util.JadeStringUtil.change(viewBean.getCompteAnnexe().getTiers().getNom(), "\"", "&quot;")%>" class="libelleLongDisabled" tabindex=-1 readonly>
            </TD>
          </TR>

          <TR>
            <TD>
				<% if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getIdSection())) { %>
					<A href="osiris?userAction=osiris.comptes.apercuParSection.afficher&id=<%=viewBean.getSection().getIdSection()%>">Section</A>
				<% } else { %>
					Section
				<% } %>
			</TD>
            <TD width="147">
              <input type="hidden" name="idSection" value="<%=viewBean.getIdSection()%>" tabindex="-1" >
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
            <TD width="7">&nbsp;</TD>
            <TD width="300">
              <select name="idTypeSectionEcran"  style="width:100%">
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
            <TD nowrap align="right">
              <input type="text" name="descSection" size="30" maxlength="30" value="<%if (viewBean.getSection() != null) {%><%=viewBean.getSection().getDescription()%><%}%>" class="libelleLongDisabled" tabindex=-1 readonly>
            </TD>
          </TR>

          <tr id="sursis" <%if (!(viewBean.getSection() != null && !viewBean.getSection().getIdPlanRecouvrement().equals("0"))) {%> style='display:none' <%} %>>
          	<td>&nbsp;</td>
          	<td colspan="4" class="label">
	          	<font color='red'><strong>
	          		Cette section est couverte par
	          		<a href="<%if (viewBean.getSection() != null && !viewBean.getSection().getIdPlanRecouvrement().equals("0")) {%>osiris?userAction=osiris.recouvrement.planRecouvrement.afficher&selectedId=<%=viewBean.getSection().getIdPlanRecouvrement() %><%} %>" id="linkPlan">
	          		le sursis aux paiements
	          		</a>
	          	</strong></font>
          	</td>
          </tr>
          <tr id="sursisAdd" style='display:none'>
          	<td>&nbsp;</td>
          	<td colspan="4" class="label">
	          	<font color='red'><strong>
	          		Cette section est couverte par le sursis aux paiements :
					<INPUT style="border:solid 0px;background-color:Transparent;color=red" type="text" maxlength="8" id="idPlan" value="">
	          	</strong></font>
          	</td>
          </tr>

			<TR><TD nowrap colspan="5"><HR/></TD></TR>
          <TR>
            <TD nowrap width="129">Date valeur</TD>
            <TD width="147">
            		<ct:FWCalendarTag name="date" doClientValidation="CALENDAR" value="<%=viewBean.getDate()%>"/>
            </TD>
            <TD colspan="3">&nbsp;</TD>
          </TR>

<%
String jspLocation = request.getContextPath()+ mainServletPath + "Root/rubrique_select.jsp";
%>

          <TR>
            <TD width="129">Rubrique</TD>
            <TD width="147">
            <ct:FWPopupList name="idExterneRubriqueEcran" onFailure="rubriqueManuelleOn();"
				onChange="updateRubrique(tag.select);"
				value="<%=viewBean.getIdExterneRubriqueEcran()%>"
				className="libelle"
				jspName="<%=jspLocation%>"
				minNbrDigit="1"
				forceSelection="true"
				validateOnChange="false"
				 />

              <input type="hidden" name="idCompte" value="<%=viewBean.getIdCompte()%>" tabindex="-1" >
              <input type="hidden" name="rechercheRubriqueEcran" value="<%=viewBean.getRechercheRubriqueEcran()%>" tabindex="-1" >
            </TD>
            <TD width="7">&nbsp;</TD>
            <TD width="300">
              <input type="text" name="rubriqueDescription" size="30" value="<%if (viewBean.getCompte() != null) {%><%=viewBean.getCompte().getDescription()%><%}%>" class="libelleLongDisabled" tabindex="-1" readonly>
            </TD>
            <TD>&nbsp;</TD>
          </TR>

          <TR>
            <TD>Montant</TD>

            <TD width="147">
              <INPUT type="text" onchange="validateFloatNumber(this)" onkeypress="filterCharForFloat(window.event)" id="montantEcran" name="montantEcran" size="30" maxlength="30" value="<%="".equalsIgnoreCase(globaz.globall.util.JANumberFormatter.formatZeroValues(viewBean.getMontantEcran(),false,true))?"":viewBean.getMontantEcran()%>"  class="montant">
            </TD>
            <TD width="7">&nbsp;</TD>
            <TD width="300">
              <SELECT name="codeDebitCreditEcran" style="width : 2cm;"  >
                <% if (viewBean.getCodeDebitCreditEcran().equalsIgnoreCase("D")){%>
                <OPTION value="D">D&eacute;bit</OPTION>
                <option value="C" >Cr&eacute;dit</option>
                <%}else{%>
                <option value="D" >D&eacute;bit</option>
                <option value="C" selected>Cr&eacute;dit</option>
                <%}%>
              </SELECT>
            </TD>
            <TD>&nbsp;</TD>
          </TR>

          <TR>
            <TD nowrap width="129">Libell&eacute;</TD>
            <TD colspan="4">
              <INPUT type="text" id="libelle" name="libelle" size="64" maxlength="40" value="<%=globaz.jade.client.util.JadeStringUtil.change(viewBean.getLibelle(), "\"", "&quot;")%>">
            </TD>
          </TR>

          <TR>
            <TD nowrap width="129">N&deg; de pi&egrave;ce comptable</TD>
			<TD colspan="4">
              <input type="text" id="piece" name="piece" size="10" maxlength="10" value="<%=viewBean.getPiece()%>">
            </TD>
          </TR>

<%
String jspLocation1 = request.getContextPath()+ mainServletPath + "Root/compteCourant_select.jsp";
%>

          <TR>
            <TD nowrap width="129">Compte courant</TD>

            <TD width="147"> <ct:FWPopupList name="idExterneCompteCourantEcran" onFailure="compteCourantManuelleOn();"
				onChange="updateCompteCourant(tag.select);"
				value="<%=viewBean.getIdExterneCompteCourantEcran()%>"
				className="libelle"
				jspName="<%=jspLocation1%>"
				minNbrDigit="1"
				forceSelection="true"
				validateOnChange="false"
				/>

            </TD>
            <TD width="7">&nbsp;</TD>
            <TD>
            <input type="hidden" name="idCompteCourant" value="<%=viewBean.getIdCompteCourant()%>" tabindex="-1" >
              <input type="hidden" name="rechercheCompteCourantEcran" value="<%=viewBean.getRechercheCompteCourantEcran()%>" tabindex="-1" >
              <input type="text" name="CCEcran" size="30" maxlength="30" value="<%if(viewBean.getCompteCourant() != null) {%><%=viewBean.getCompteCourant().getRubrique().getDescription()%><% } %>" class="inputDisabled" tabindex="-1" readonly>
            </TD>
            <TD>&nbsp;</TD>
          </TR>

          <TR id="auxSection">
          <TD width="91">Section aux.</TD>
          <TD width="270">
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
            <TD width="1">&nbsp;</TD>
            <TD nowrap align="right">
            	<input type="text" name="idCompteAnnexeAuxDesc" size="30" maxlength="30" value="<%=viewBean.getIdCompteAnnexeAuxDesc()%>" class="inputDisabled"  readonly tabindex="-1">
            </TD>
            <TD>&nbsp;</TD>
          </TR>

          <SCRIPT language="JavaScript">
          updateSectionsAutoCompleteLink();
          initAuxSection();
          </SCRIPT>

          <TR>
            <TD nowrap width="129">Journal</TD>

            <TD colspan="3">
              <input type="hidden" name="idJournal" value="<%=viewBean.getIdJournal()%>" tabindex="-1" >
              <%globaz.osiris.db.comptes.CAJournal journal = viewBean.getJournal();              %>
              <input type="text" name="journalEcran" size="30" maxlength="30" value="<%=viewBean.getIdJournal()%> - <%=journal==null?"":journal.getLibelle()%>" class="inputDisabled" tabindex="-1" readonly>
            </TD>

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

          </TR>

          <TR>
            <TD nowrap width="129">Etat</TD>
            <TD colspan="3">
              <input type="text" name="etatJournal" size="30" maxlength="30" value="<%=viewBean.getUcEtat().getLibelle()%>" class="inputDisabled" tabindex="-1" readonly>
            </TD>
            <TD>&nbsp;</TD>
          </TR>
          <TR>
            <TD nowrap width="129">Message</TD>

            <TD colspan="4">
              <select name="idLog" style="width : 16cm;" class="libelleErrorLongDisabled" tabindex="-1">
                <%if (viewBean.getLog() != null) {%>
                <%Enumeration e = viewBean.getLog().getMessagesToEnumeration();
					while (e.hasMoreElements()){
						FWMessage msg = (FWMessage)e.nextElement();%>
                <%if(msg.getIdLog().equalsIgnoreCase(viewBean.getIdLog())) {%>
                  <option selected  value="<%=msg.getIdLog()%>"><%=msg.getMessageText()%></option>
                <%}else {%>
                  <option value="<%=msg.getIdLog()%>"><%=msg.getMessageText()%></option>
                <%}%>
                <% } %>
                <% } %>
              </select>
            </TD>
          </TR>

          <TR>
            <TD>Quittancer</TD>
            <TD colspan="4" align="left">
            	<input type="checkbox" name="_quittanceLogEcran"  onClick="quittancer()" disabled>
				  <input type="hidden" name="quittanceLogEcran" value="">
            </TD>
          </TR>

          <TR><TD nowrap colspan="5"><HR/></TD></TR>

          <tr>
            <td nowrap width="129">Genre transaction BVR</td>
            <td width="147">
              <input type="text" id="genreTransaction" name="genreTransaction" size="10" maxlength="10" value="<%=viewBean.getGenreTransaction()%>"  >
            </td>

            <td width="4">&nbsp;</td>

            <td colspan="2">
            <table width="100%">
            <tr>
            <td width="130">Date de traitement</td>
            <td><ct:FWCalendarTag name="dateTraitement" doClientValidation="CALENDAR" value="<%=viewBean.getDateTraitement()%>"/></td>
            </tr>
            </table>
            </td>
          </tr>

          <tr>
            <td width="129">N&deg; de r&eacute;f&eacute;rence BVR</td>
            <td width="147">
              <input type="text" id="referenceBVR" name="referenceBVR" size="35" maxlength="27" value="<%=viewBean.getReferenceBVR()%>"  >
            </td>

            <td width="4">&nbsp;</td>

            <td colspan="2">
            <table width="100%">
            <tr>
            <td width="130">Date de d&eacute;p&ocirc;t</td>
            <td><ct:FWCalendarTag name="dateDepot" doClientValidation="CALENDAR" value="<%=viewBean.getDateDepot()%>"/></td>
            </tr>
            </table>
            </td>
          </tr>

          <tr>
            <td width="129">R&eacute;f&eacute;rence interne</td>
            <td width="147">
              <input type="text" id="referenceInterne" name="referenceInterne" size="35" maxlength="27" value="<%=viewBean.getReferenceInterne()%>"  >
            </td>

            <td width="4">&nbsp;</td>

            <td colspan="2">
            <table width="100%">
            <tr>
            <td width="130">Date d'inscription</td>
            <td><ct:FWCalendarTag name="dateInscription" doClientValidation="CALENDAR" value="<%=viewBean.getDateInscription()%>"/></td>
            </tr>
            </table>
            </td>
          </tr>

          <tr>
            <td nowrap width="129">Organe d'ex&eacute;cution</td>

            <td colspan="4">
              <select name="idOrganeExecution" class="list" >
                <%CAOrganeExecutionManager mgr = new CAOrganeExecutionManager();
			    CAOrganeExecution org;
				mgr.setSession(objSession);
			    mgr.find();
				for (int i = 0; i < mgr.size(); i++) {
				  org = (CAOrganeExecution)mgr.getEntity(i);
				  if (!org.getIdTypeTraitementBV().equals(CAOrganeExecution.BVR_AUCUN)) {
				  %>
                <option value="<%=org.getIdOrganeExecution()%>" <% if(org.getIdOrganeExecution().equals(viewBean.getIdOrganeExecution())){%> selected <%}%> ><%=org.getNom()%></option>
                <%
				  }
				}
			  %>
              </select>
            </td>

          </tr>

		  <TR><TD nowrap colspan="5"><HR/></TD></TR>

          <tr>
            <td nowrap width="129">N&deg; &eacute;criture collective</td>
            <td width="147">
              <input type="text" name="noEcritureCollectiveEcran" size="10" maxlength="10" value="<%if (!globaz.globall.util.JANumberFormatter.formatZeroValues(viewBean.getNoEcritureCollective(),false,true).equalsIgnoreCase(""))%><%=viewBean.getNoEcritureCollective()%>" class="numeroDisabled" tabindex="-1" readonly>
            </td>

            <td>&nbsp;</td>

            <td align="right">Journal CG</td>
            <td nowrap height="34" align="right">
              <input type="text" name="noJournalEcran" size="30" maxlength="30" value="<%=journal==null?"":journal.getNumeroJournalComptaGen()%>" class="libelleLongDisabled" tabindex="-1" readonly >
            </td>
           </tr>

          <TR>
            <TD nowrap width="129">Identifiant</TD>

            <TD colspan="2">
              <input type="text" name="idOperation" size="10" maxlength="10" value="<%=viewBean.getIdOperation()%>" class="numeroDisabled"  tabindex="-1" readonly>
            </TD>
            <td align="right">Id compte annexe</td>
            <td nowrap height="34" align="right">
              <input type="text" name="idCompteAnnexeEcran" size="30" maxlength="30" value="<%=viewBean.getIdCompteAnnexe()%>" class="libelleLongDisabled" tabindex="-1" readonly >
            </td>
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