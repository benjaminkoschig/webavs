<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%@page import="globaz.helios.db.ecritures.*,globaz.helios.db.comptes.*,globaz.helios.db.interfaces.*, globaz.helios.translation.*,globaz.helios.parser.*" %>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.helios.db.lynx.fournisseur.CGLXFournisseur"%>

<script language="JavaScript">
<%
  idEcran="GCF0003";

  CGGestionEcritureViewBean viewBean = (CGGestionEcritureViewBean) session.getAttribute ("viewBean");
  CGExerciceComptableViewBean exerciceComptable = (CGExerciceComptableViewBean)session.getAttribute (CGNeedExerciceComptable.SESSION_EXERCICECOMPTABLE);
  selectedIdValue = viewBean.getIdJournal();
    userActionValue = "helios.ecritures.gestionEcriture.modifier";

     String aucun = "Aucun";
  if (languePage.equalsIgnoreCase("DE")) {
    aucun = "Kein";
  }

  java.util.Vector centreChargeListe = globaz.helios.translation.CGListes.getCentreChargeListe(aucun, session, exerciceComptable.getIdMandat());

  if (viewBean.isDetteAvoir() || !viewBean.isJournalEditable()) {
    bButtonNew=false;
    bButtonDelete=false;
    bButtonUpdate = false;
  }

  boolean showValue = !(request.getParameter("forceNew") != null && !(new Boolean(request.getParameter("forceNew")).booleanValue()));
  if (!showValue) {
    viewBean.setShowRows(0);
  }
%>

</script> <%-- /tpl:put --%>

<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>

<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<script type="text/javascript">
	notationManager.b_stop = true;
</script>
<script language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

shortKeys[107] = "plus";
shortKeys[49] = "plus";
shortKeys[48] = "flv";
shortKeys[96] = "flv";

var nextRowToShow = <%=viewBean.getShowRows()%>;
var maxRows = <%=viewBean.getMaxRows()%>;
var centreChargeAfficheB = false;
var montantEtrangerAfficheB = true;
var ecritureMultiple = false || (nextRowToShow > 2);

function onLibelleFailure(event) {
  //si touche different de [DEL] ou [BACKSPACE]
  if(event.keyCode != 8 && event.keyCode != 46) {
    alert(" Le libellé n'existe pas.");
  }
}

function add() {
    document.forms[0].elements('userAction').value="helios.ecritures.gestionEcriture.ajouter";
    document.getElementById("forceValid").value = "true";
  disableCentreCharge();
}

function upd() {
  document.forms[0].elements('userAction').value="helios.ecritures.gestionEcriture.modifier";
  disableCentreCharge();
}

function validate() {
    state = validateFields();

  disableCentreCharge();

    if (document.forms[0].elements('_method').value == "add") {
        document.forms[0].elements('userAction').value="helios.ecritures.gestionEcriture.ajouter";
        document.getElementById("forceValid").value = "true";
    } else {
        document.forms[0].elements('userAction').value="helios.ecritures.gestionEcriture.modifier";
  }

    return state;

}

function cancel() {
  if (document.forms[0].elements('_method').value == "add")
    document.forms[0].elements('userAction').value="back";
  else
    document.forms[0].elements('userAction').value="helios.comptes.journal.afficher";
}
function del() {
    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
        document.forms[0].elements('userAction').value="helios.ecritures.gestionEcriture.supprimer";
        document.forms[0].submit();
    }
}

function init(){
  <%
    if (request.getParameter("forceValid") != null && request.getParameter("forceValid").equals("true")) {
  %>
    document.getElementById("_valid").value = "new";
  <%
    }
  %>

  hideRows();
  enableMontantEtranger();
  document.getElementById("btnVal").disabled = true;

  self.focus();
    document.getElementById("dateValeur").focus();

    updateSum();

    disableCentreCharge();

    document.getElementById('montantEtrangerAffiche').value = montantEtrangerAfficheB;
    document.getElementById('centreChargeAffiche').value = centreChargeAfficheB;
    <%=viewBean.isShowCentreCharge() || new Boolean(request.getParameter("centreChargeAffiche")).booleanValue() || viewBean.getCentreChargeAffiche()?"switchCentreCharge();":"" %>

    <%=new Boolean(request.getParameter("montantEtrangerAffiche")).booleanValue() || viewBean.getMontantEtrangerAffiche()?"":"switchMontantEtranger();" %> // Hide
}

function fillCell(cell) {
  /**
  * touche '=' pressée
  */
  if (event.keyCode==61 && cell.value=='') {
    if (cell.name == "piece") {
      <% if (viewBean.getPiece() != null) { %>
        cell.value="<%=viewBean.getPiece()%>";
      <% } %>
    } else if (cell.name == "remarque") {
      <% if (viewBean.getRemarque() != null) { %>
        cell.value="<%=viewBean.getRemarque()%>";
      <% } %>
    } else if (cell.name == "dateValeur") {
      <% if (viewBean.getDateValeur() != null) { %>
        cell.value="<%=viewBean.getDateValeur()%>";
      <% } %>
    }

    <%
      for (int i=0; i<viewBean.getMaxRows(); i++) {
        if (viewBean.getIdExt(i) != null) {
    %>
      if (cell.name == "idext<%=i%>") {
        cell.value="<%=viewBean.getIdExt(i)%>";
      }
    <%
        }

        if (viewBean.getLibelle(i) != null) {
    %>
      if (cell.name == "l<%=i%>") {
        cell.value="<%=viewBean.getLibelle(i)%>";
      }
    <%
        }
      }
    %>
    event.keyCode=null;
  }
}

function onCompteFailure(event) {
  //si touche different de [DEL] ou [BACKSPACE]
  if(event.keyCode != 8 && event.keyCode != 46) {
    alert(" Le compte n'existe pas.");
  }
}

function onLibelleFailure(event) {
  //si touche different de [DEL] ou [BACKSPACE]
  if(event.keyCode != 8 && event.keyCode != 46) {
    alert("Le libellé n'existe pas.");
  }
}

function disableCentreCharge() {
  for (i=0; i<maxRows; i++) {
    if (document.getElementById("idcc" + i).value == 0) {
      document.getElementById("idcc" + i).className = "selectDisabled";
      document.getElementById("idcc" + i).disabled = true;
    }
  }
}

function enableMontantEtranger() {
  for (i=0; i<maxRows; i++) {
    if (document.getElementById("me" + i).value != null && document.getElementById("me" + i).value != "0.00") {
      document.getElementById("me" + i).className = "montantShort";
      document.getElementById("me" + i).disabled = false;
      document.getElementById("me" + i).readOnly = false;
    }

    if (document.getElementById("c" + i).value != null && document.getElementById("c" + i).value != "0.00000") {
      document.getElementById("c" + i).className = "cours";
      document.getElementById("c" + i).disabled = false;
      document.getElementById("c" + i).readOnly = false;
    }
  }
}

function hideRows() {
  for (i=nextRowToShow; i<maxRows; i++) {
    document.getElementById("ligne" + i).style.display = "none";
  }
}

function showNextRow() {
  if (nextRowToShow < maxRows) {
    document.getElementById("ligne" + nextRowToShow).style.display = "block";
    if (montantEtrangerAfficheB) {
      document.getElementById("colCours" + nextRowToShow).style.display = "block";
      document.getElementById("colMnt" + nextRowToShow).style.display = "block";
      document.getElementById("ccCours" + nextRowToShow).style.display = "block";
      document.getElementById("ccMnt" + nextRowToShow).style.display = "block";
    } else {
      document.getElementById("colCours" + nextRowToShow).style.display = "none";
      document.getElementById("colMnt" + nextRowToShow).style.display = "none";
      document.getElementById("ccCours" + nextRowToShow).style.display = "none";
      document.getElementById("ccMnt" + nextRowToShow).style.display = "none";
    }
    if (centreChargeAfficheB) {
      document.getElementById("ligneB" + nextRowToShow).style.display = "block";
    } else {
      document.getElementById("ligneB" + nextRowToShow).style.display = "none";
    }
    nextRowToShow ++;
  }
  ecritureMultiple = true;
}

function updateCompte(tag, i) {
  if (tag.select) {
    var element = tag.select.options[tag.select.selectedIndex];
    document.getElementById("idc" + i).value = element.idCompte;

    if (element.idNature == <%=CGCompte.CS_CENTRE_CHARGE%>) {
      document.getElementById("idcc" + i).className = "libelle";
      document.getElementById("idcc" + i).disabled = false;
      document.getElementById("idcc" + i).className = "selectEnabled";

      if (element.defaultIdCentreCharge > 0) {
        document.getElementById("idcc" + i).value = element.defaultIdCentreCharge;
      }
      if(!centreChargeAfficheB) {
        switchCentreCharge();
      }
    } else {
      document.getElementById("idcc" + i).className = "selectDisabled";
      document.getElementById("idcc" + i).disabled = true;
      document.getElementById("idcc" + i).value = "0";
    }

    if (element.idNature == <%=CGCompte.CS_MONNAIE_ETRANGERE%>) {
      document.getElementById("me" + i).className = "montantShort";
      document.getElementById("me" + i).disabled = false;
      document.getElementById("me" + i).readOnly = false;
      document.getElementById("c" + i).className = "cours";
      document.getElementById("c" + i).disabled = false;
      document.getElementById("c" + i).readOnly = false;
      if(!montantEtrangerAfficheB) {
        switchMontantEtranger();
      }
    } else {
      document.getElementById("me" + i).className = "montantShortDisabled";
      document.getElementById("me" + i).disabled = true;
      document.getElementById("me" + i).readOnly = true;
      document.getElementById("me" + i).value = "0.00";
      document.getElementById("c" + i).className = "coursDisabled";
      document.getElementById("c" + i).disabled = true;
      document.getElementById("c" + i).readOnly = true;
      document.getElementById("c" + i).value = "0.00000";
    }
  }
}

function clearDebitCredit(debitOrCredit, i) {
  document.getElementById(debitOrCredit + i).value = "";

  // Mandat 700 (inforom090)
  // Gestion des ecritures doubles : reprendre le montant lors de la saisie d'écritures
  if(!ecritureMultiple) {

    var debitOrCreditEcritureDouble = debitOrCredit; //par defaut c'est la bonne case ou le montant sera reproduit
    var idLigneDouble = 1; // par defaut le numero de ligne ou le montant sera reproduit

    //On cherche le montant a reprendre
    if(debitOrCredit == 'mc') {
      debitOrCredit = 'md';
    }else {
      debitOrCredit = 'mc';
    }

    //On cherche la ligne a modifier et on fait le focus sur le prochain élement
    if(i > 0) {
      idLigneDouble = 0 //la ligne ou le montant sera reproduit
      document.getElementById("btnVal").focus();
    }else {
      document.getElementById("idext" + idLigneDouble).focus();
    }

    // Copie du montant
    document.getElementById(debitOrCreditEcritureDouble + idLigneDouble).value = document.getElementById(debitOrCredit + i).value;
    // On vide les cellules a coté
    document.getElementById(debitOrCredit + idLigneDouble).value = "";
  }
}


function updateSum() {
  var decote=/'/g;

  montantDebit = parseFloat('0.0');
  montantCrebit = parseFloat('0.0');
  montantEtranger = parseFloat('0.0');

  for (i=0; i<maxRows; i++) {
    if (document.getElementById("md" + i).value != null && document.getElementById("md" + i).value != '') {
      tmp = document.getElementById("md" + i).value;
      tmp = tmp.replace(decote,"");
      montantDebit += parseFloat(tmp);
    }

    if (document.getElementById("mc" + i).value != null && document.getElementById("mc" + i).value != '') {
      tmp = document.getElementById("mc" + i).value;
      tmp = tmp.replace(decote,"");
      montantCrebit += parseFloat(tmp);
    }

    if (document.getElementById("me" + i).value != null && document.getElementById("me" + i).value != '') {
      tmp = document.getElementById("me" + i).value;
      tmp = tmp.replace(decote,"");
      montantEtranger += parseFloat(tmp);
    }
  }

  document.getElementById("sd").value = '';
  document.getElementById("sd").value = montantDebit+'';
  validateFloatNumber(document.getElementById("sd"));

  document.getElementById("sc").value = '';
  document.getElementById("sc").value = montantCrebit+'';
  validateFloatNumber(document.getElementById("sc"));

  document.getElementById("se").value = '';
  document.getElementById("se").value = montantEtranger+'';
  validateFloatNumber(document.getElementById("se"));

  if (montantDebit.toFixed(2) == montantCrebit.toFixed(2) && montantDebit.toFixed(2) != 0.0) {
    document.getElementById("btnVal").disabled = false;
  } else {
    document.getElementById("btnVal").disabled = true;
  }

  updateBalance(montantDebit, montantCrebit);
}

function updateBalance(montantDebit, montantCrebit) {
  document.getElementById("bd").value = '';
  document.getElementById("bc").value = '';

  if (montantDebit > montantCrebit) {
    tmp = montantDebit - montantCrebit;
    document.getElementById("bc").value = tmp+'';
    validateFloatNumber(document.getElementById("bc"));
  } else if (montantDebit < montantCrebit) {
    tmp = montantCrebit - montantDebit;
    document.getElementById("bd").value = tmp+'';
    validateFloatNumber(document.getElementById("bd"));
  }
}

function focusOnNextCompte() {
  for (i=0; i<maxRows; i++) {
    if (document.getElementById("idext" + i).value == "") {
      self.focus();
         document.getElementById("idext" + i).focus();

         return;
    }
  }
}

function equilibrate() {
  var decote=/'/g;

  montantDebit = parseFloat('0.0');
  montantCrebit = parseFloat('0.0');

  count = 0;

  for (i=0; i<maxRows; i++) {
    if (document.getElementById("md" + i).value != null && document.getElementById("md" + i).value != '' && document.getElementById("md" + i).value != '0.00') {
      tmp = document.getElementById("md" + i).value;
      tmp = tmp.replace(decote,"");
      montantDebit += parseFloat(tmp);

      if (montantDebit != 0.00) {
        count++;
      }
    }

    if (document.getElementById("mc" + i).value != null && document.getElementById("mc" + i).value != '' && document.getElementById("mc" + i).value != '0.00') {
      tmp = document.getElementById("mc" + i).value;
      tmp = tmp.replace(decote,"");
      montantCrebit += parseFloat(tmp);

      if (montantCrebit != 0.00) {
        count++;
      }
    }
  }

  if (montantDebit != montantCrebit && count != nextRowToShow) {
    index = nextRowToShow - 1;
    if (montantDebit > montantCrebit) {
      tmp = montantDebit - montantCrebit;
      document.getElementById("mc" + index).value = tmp;
      validateFloatNumber(document.getElementById("mc" + index));
      clearDebitCredit('md', index);
    } else {
      tmp = montantCrebit - montantDebit;
      document.getElementById("md" + index).value = tmp;
      validateFloatNumber(document.getElementById("md" + index));
      clearDebitCredit('mc', index);
    }
  }

  updateSum();
}

function updateMontantChf(i) {
  var decote=/'/g;

  if (document.getElementById("me" + i).value != null && document.getElementById("me" + i).value != '' && document.getElementById("me" + i).value != '0.00'
    &&
    document.getElementById("c" + i).value != null && document.getElementById("c" + i).value != '' && document.getElementById("c" + i).value != '0.00000') {
    tmpEtr = document.getElementById("me" + i).value;
    tmpEtr = tmpEtr.replace(decote,"");

    cours = document.getElementById("c" + i).value;

    tmp = Math.round((parseFloat(tmpEtr) * parseFloat(cours))*100)/100;

    if (document.getElementById("md" + i).value == '0.00') {
      document.getElementById("md" + i).value = tmp+'';
      validateFloatNumber(document.getElementById("md" + i));
    } else if (document.getElementById("mc" + i).value == '0.00') {
      document.getElementById("mc" + i).value = tmp+'';
      validateFloatNumber(document.getElementById("mc" + i));
    }
  }
}

// Met à jour la description du compte
function updateDescCompte(tag, i) {
  //var element = tag.select.options[tag.select.selectedIndex];
  document.getElementById("rubriqueDescription" + i).value = tag.select.options[tag.select.selectedIndex].libelleCompte;
}

function switchCentreCharge() {
  centreChargeAfficheB = !centreChargeAfficheB;
  document.getElementById('centreChargeAffiche').value = centreChargeAfficheB;

  for (i=0; i<nextRowToShow; i++) {
    shoh('ligneB' + i);
  }
}

function switchMontantEtranger() {
  montantEtrangerAfficheB = !montantEtrangerAfficheB;
  document.getElementById('montantEtrangerAffiche').value = montantEtrangerAfficheB;
  shoh('colMntTh');
  shoh('colCoursTh');
  for (i=0; i<nextRowToShow; i++) {
    shoh('colMnt' + i);
    shoh('colCours' + i);
    shoh('ccMnt' + i);
    shoh('ccCours' + i);
  }
  shoh('colMnt999');
  shoh('colCours999')
  shoh('colMntB1')
  shoh('colCoursB1')
}

//show OR hide funtion depends on if element is shown or hidden
function shoh(id) {
  if (document.getElementById) { // DOM3 = IE5, NS6
    if (document.getElementById(id).style.display == "none"){
      document.getElementById(id).style.display = 'block';
    } else {
      document.getElementById(id).style.display = 'none';
    }
  } else {
    if (document.layers) {
      if (document.id.display == "none"){
        document.id.display = 'block';
      } else {
        document.id.display = 'none';
      }
    } else {
      if (document.all.id.style.visibility == "none"){
        document.all.id.style.display = 'block';
      } else {
        document.all.id.style.display = 'none';
      }
    }
  }
}

// stop hiding -->
</script>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>Ecritures<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>

<tr>
  <td width="140">Numéro du journal</td>
  <td width="140" align="left">
    <input name="noJournal" class="libelleDisabled" readonly value="<%=viewBean.getJournal().getNumero()%>"/>
    <input name="idJournal" type="hidden" value="<%=viewBean.getIdJournal()%>"/>
    <input name="idFournisseur" type="hidden" value="<%=viewBean.getIdFournisseur()%>"/>
    <input name="idSection" type="hidden" value="<%=viewBean.getIdSection()%>"/>
    <input name="forceValid" type="hidden" value="false"/>
    <input name="idExerciceComptable" type="hidden" value="<%=exerciceComptable.getIdExerciceComptable()%>"/>
    <input name="montantEtrangerAffiche" type="hidden" value="" />
    <input name="centreChargeAffiche" type="hidden" value="" />
    <input type="hidden" name="saisieEcran" value="true">
  </td>
  <td width="140">Numéro d'écriture</td>
  <td align="left"><input name="idEnteteEcriture" class="libelleDisabled" readonly value="<%=viewBean.getIdEnteteEcriture()%>"/></td>
  <td align="right">
    <%
    String GEDText = "&nbsp;";
    String gedFolderType = "";
    String gedServiceName = "";

    if (!JadeStringUtil.isIntegerEmpty(viewBean.getIdFournisseur()) && !JadeStringUtil.isIntegerEmpty(viewBean.getIdSection())) {
      try {
        globaz.globall.api.BIApplication lynxApplication = globaz.globall.api.GlobazSystem.getApplication("LYNX");
        gedFolderType = lynxApplication.getProperty("ged.folder.type", "");
        gedServiceName = lynxApplication.getProperty("ged.servicename.id", "");
      } catch (Exception e){
        // Le reste de la page doit tout de même fonctionner
      }

      if (globaz.jade.ged.client.JadeGedFacade.isInstalled()) {
        GEDText = "<a href=\"#\" onclick=\"window.open('" + servletContext +
          "/naos?userAction=naos.affiliation.affiliation.gedafficherdossier&amp;lynx.fournisseur.idExterne=" + viewBean.getIdExterneFournisseur() +
          "&amp;serviceNameId=" + gedServiceName +
          "&amp;gedFolderType=" + gedFolderType +
          "&amp;idRole=" + CGLXFournisseur.ROLE_FOURNISSEUR +
          "&amp;lynx.section.idExterne=" + viewBean.getIdExterneSection() +
          "')\"  class=\"external_link\">GED</a>";
      }
        }
    %>

    <%=GEDText%>
  </td>
</tr>

<tr>
  <td>Date</td>
  <td align="left">
  <ct:FWCalendarTag name="dateValeur" value='<%=showValue?viewBean.getDateValeur():""%>'/>
    <script language="JavaScript">
      element = document.getElementById("dateValeur");
        element.onkeypress=function() {fillCell(this);}
    </script>
  </td>
  <td>Pièce</td>
  <td align="left" colspan="2">
    <input name="piece" class="libelle" size="10" maxlength="10" value="<%=showValue?(!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getPiece()) || !viewBean.isJournalEditable())?viewBean.getPiece():CGPieceIncrementor.getNextNumero(viewBean.getSession(), exerciceComptable.getIdExerciceComptable()):""%>" onKeyPress="fillCell(this);"/>
  </td>
</tr>

<tr><td colspan="5">&nbsp;</td></tr>

</tbody>
</table>

<table border="0" cellspacing="0" cellpadding="0" width="100%">
<tbody>

<tr>
  <td>
    <a href="#" onclick="switchCentreCharge();" tabindex="-1">Centre de charge</a>
  </td>
  <td align="right">
    <a href="#" onclick="switchMontantEtranger();" tabindex="-1">Montant étranger</a>
  </td>
  <td align="right" height="24">
    <%if (!viewBean.isDetteAvoir() && viewBean.isJournalEditable()) {%>
    <a href="#" border="noborder" onclick="showNextRow();focusOnNextCompte();" tabindex="-1" id="plus"><img tabindex="-1" src="<%=request.getContextPath()%>/images/plus.jpg" border="0" title="Ajouter une ligne"/></a>
    <%} else {%>
    &nbsp;
    <%}%>
  </td>
</tr>

<tr>
  <td colspan="3">
    <table width="100%" cellspacing="0" class="borderBlack">
    <tr>
      <th align="left">Compte</th>
      <th align="left"><span class="min_width">Libellé du compte</span></th>
      <th><span class="min_width">Libellé</span></th>
      <th>Débit</th>
      <th>Crédit</th>
      <th id="colMntTh">Montant (&euro;,&#36;...)</th>
      <th id="colCoursTh">Cours</th>
    </tr>

    <%
      String jspLocation = servletContext + "/heliosRoot/compte_select.jsp";
      String params = "idExerciceComptable=" + exerciceComptable.getIdExerciceComptable() + "&isMandatAVS=" + exerciceComptable.getMandat().isEstComptabiliteAVS();
      int autoCompleteStart = globaz.helios.parser.CGAutoComplete.getAutoCompleteAutoStart();

      String jspLocationLibelle = servletContext + "/heliosRoot/" + languePage + "/comptes/label_select.jsp";
      String paramsLibelle = "idMandat=" + exerciceComptable.getIdMandat() + "&langue=" + languePage;

      for (int i=0; i<viewBean.getMaxRows(); i++) {
        String style = "row";
        if (i % 2 == 1) {
          style = "rowOdd";
        }
    %>

    <tr class="<%=style%>" id="ligne<%=i%>">
      <td style="vertical-align: middle; text-align: left;" class="mtdShortPadding compte">
        <%
              String tmpIdext = "idext" + i;
            String tmpIdextScript = "updateCompte(tag, " + i + ");updateSum();updateDescCompte(tag, " + i + ")";
            String tmpIdextValue = showValue?viewBean.getIdExt(i):"";
          %>
        <ct:FWPopupList name="<%=tmpIdext%>" onFailure="onCompteFailure(window.event);" onChange="<%=tmpIdextScript%>" validateOnChange="true" params="<%=params%>" value="<%=tmpIdextValue%>" className="compte" jspName="<%=jspLocation%>" minNbrDigit="1" autoNbrDigit="<%=autoCompleteStart%>" forceSelection="true"/>
        <input type="hidden" name="idc<%=i%>" value="<%=viewBean.getIdCompte(i)%>"/>
        <input type="hidden" name="ide<%=i%>" value="<%=viewBean.getIdEcriture(i)%>"/>

        <script language="JavaScript">
          element = document.getElementById("<%=("idext" + i)%>");
            element.onkeypress=function() {fillCell(this);}
        </script>
      </td>
      <td style="vertical-align: middle; text-align: left;" class="mtdShortPadding">
                <input type="text" name="rubriqueDescription<%=i%>" style="width:100%;" maxlength="50" value="<%=("add".equalsIgnoreCase(request.getParameter("_method")))?"":viewBean.getLibelleCompte(i)%>" class="selectDisabled"  readonly tabindex="-1">
      </td>
      <td style="vertical-align: middle; text-align: left;" class="mtdShortPadding">
        <%
              String tmpLibelle = "l" + i;
              String tmpLibelleValue = showValue?viewBean.getLibelle(i):"";
          %>
        <ct:FWPopupList name="<%=tmpLibelle%>" size="30" maxlength="40" onFailure="onLibelleFailure(window.event);" onChange="fillCell(this);" params="<%=paramsLibelle%>" value="<%=tmpLibelleValue%>" validateOnChange="false" className="libelle2" jspName="<%=jspLocationLibelle%>" minNbrDigit="2" forceSelection="false"/>

        <script language="JavaScript">
          element = document.getElementById("<%=("l" + i)%>");
            element.onkeypress = function() {fillCell(this);}
        </script>
      </td>

      <td style="vertical-align: middle; text-align: center;" class="mtdShortPadding"><input onchange="validateFloatNumber(this);clearDebitCredit('mc', <%=i%>);updateSum();" onkeypress="return filterCharForFloat(window.event);" type="text" class="montantShort" style="text-align : right" name="md<%=i%>" value="<%=showValue?viewBean.getMontantDebit(i):""%>"/></td>
      <td style="vertical-align: middle; text-align: center;" class="mtdShortPadding"><input onchange="validateFloatNumber(this);clearDebitCredit('md', <%=i%>);updateSum();" onkeypress="return filterCharForFloat(window.event);" type="text" class="montantShort" style="text-align : right" name="mc<%=i%>" value="<%=showValue?viewBean.getMontantCrebit(i):""%>"/></td>
      <td style="vertical-align: middle; text-align: center;" class="mtdShortPadding" id="colMnt<%=i%>"><input onchange="validateFloatNumber(this);updateMontantChf(<%=i%>);updateSum();" onkeypress="return filterCharForFloat(window.event);" type="text" class="montantShortDisabled" style="text-align : right" name="me<%=i%>" value="<%=showValue?viewBean.getMontantEtranger(i):"0.00"%>" readonly="true" disabled="true"/></td>
      <td style="vertical-align: middle; text-align: center;" class="mtdShortPadding" id="colCours<%=i%>"><input onchange="validateFloatNumber(this,5);updateMontantChf(<%=i%>);updateSum();" onkeypress="return filterCharForFloat(window.event);" type="text" class="coursDisabled" style="text-align : right" name="c<%=i%>" value="<%=showValue?viewBean.getCours(i):"0.00000"%>" readonly="true" disabled="true"/></td>
    </tr>

    <%-- Ligne du centre de charge --%>
    <tr class="<%=style%>" style="display:none;" id="ligneB<%=i%>">
      <td>Centre de charge : </td>
      <td colspan="2" style="vertical-align: middle; text-align: left;" class="mtdShortPadding">
        <%
              String tmpIdcc = "idcc" + i;
              String tmpIdccValue = showValue?viewBean.getIdCompteCharge(i):"";
          %>
        <ct:FWListSelectTag name="<%=tmpIdcc%>" defaut="<%=tmpIdccValue%>" data="<%=centreChargeListe%>"/>
      </td>
      <td colspan="2"></td>
      <td id="ccMnt<%=i%>"></td>
      <td id="ccCours<%=i%>"></td>
    </tr>

    <%
      }
    %>

    <tr class="somme">
      <td colspan="3" align="right" class="mtdBold">Total :
        <input type="hidden" name="flv" value="" onclick="equilibrate()"/>
      </td>
      <td class="mtdMontant"><input onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" type="text" class="montantShort" style="text-align : right" name="sd" value="<%=showValue?globaz.globall.util.JANumberFormatter.fmt("0.00",true,true,false,2):""%>" readonly="true" disabled="true"/></td>
      <td class="mtdMontant"><input onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" type="text" class="montantShort" style="text-align : right" name="sc" value="<%=showValue?globaz.globall.util.JANumberFormatter.fmt("0.00",true,true,false,2):""%>" readonly="true" disabled="true"/></td>
      <td class="mtdMontant" id="colMnt999"><input onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" type="text" class="montantShortDisabled" style="text-align : right" name="se" value="<%=showValue?globaz.globall.util.JANumberFormatter.fmt("0.00",true,true,false,2):""%>" readonly="true" disabled="true"/></td>
      <td id="colCours999">&nbsp;</td>
    </tr>

    <tr class="balance">
      <td colspan="3" class="mtd" align="right">Balance :</td>
      <td class="mtdMontant"><input onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" type="text" class="montantShort" style="text-align : right" name="bd" value="<%=showValue?globaz.globall.util.JANumberFormatter.fmt("0.00",true,true,false,2):""%>" readonly="true" disabled="true"/></td>
      <td class="mtdMontant"><input onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" type="text" class="montantShort" style="text-align : right" name="bc" value="<%=showValue?globaz.globall.util.JANumberFormatter.fmt("0.00",true,true,false,2):""%>" readonly="true" disabled="true"/></td>
      <td id="colMntB1">&nbsp;</td>
      <td id="colCoursB1">&nbsp;</td>
    </tr>

    </table>
  </td>
</tr>

<tr>
  <td align="right" height="24" colspan="3">
    <%if (!viewBean.isDetteAvoir() && viewBean.isJournalEditable()) {%>
    <a href="#" border="noborder" onclick="showNextRow();focusOnNextCompte();" tabindex="-1"><img tabindex="-1" src="<%=request.getContextPath()%>/images/plus.jpg" border="0" title="Ajouter une ligne"/></a>
    <%} else {%>
    &nbsp;
    <%}%>
  </td>
</tr>

</tbody>
</table>


<table border="0" cellspacing="0" cellpadding="0" width="100%">
<tbody>

<td width="140">Remarque&nbsp;<img src="<%=request.getContextPath()%>/images/attach.png" border="noborder" tabindex="-1" /></td>
<td align="left">
  <textarea name="remarque" class="libelleLong" rows="3" width="250" align="left" onKeyPress="fillCell(this);"><%=showValue?viewBean.getRemarque():""%></textarea>
</td>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
        <%-- tpl:put name="zoneButtons" --%>
        <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>

<ct:menuChange displayId="options" menuId="CG-journaux" showTab="options">
  <ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdJournal()%>"/>
</ct:menuChange>
<%-- /tpl:put --%>

<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>