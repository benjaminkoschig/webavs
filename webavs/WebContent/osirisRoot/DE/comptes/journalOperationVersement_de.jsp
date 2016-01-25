<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
		<%idEcran = "GCA0022"; %>
		<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
		<%@ page import="java.util.Enumeration" %>

		<%@ page import="globaz.osiris.db.comptes.*" %>
		<%@ page import="globaz.osiris.db.ordres.*" %>
		<%@ page import="globaz.framework.util.*" %>
		<%
	CAVersementViewBean viewBean = (CAVersementViewBean) session.getAttribute (globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
	userActionValue = "osiris.comptes.journalOperationVersement.modifier";
//	selectedIdValue = viewBean.getIdJournal();
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
	actionNew = actionNew + "&idJournal="+viewBean.getIdJournal();

%>
	<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
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
function add() {

	document.forms[0].idOperation.value="0";
	document.forms[0].idExterneRoleEcran.value="<%=viewBean.getIdExterneRoleEcran()%>";
	document.forms[0].idRoleEcran.value="<%=viewBean.getIdRoleEcran()%>";
	document.forms[0].idExterneSectionEcran.value="<%=viewBean.getIdExterneSectionEcran()%>";
	document.forms[0].idTypeSectionEcran.value="<%=viewBean.getIdTypeSectionEcran()%>";
	document.forms[0].date.value="<%=viewBean.getDate()%>";
	document.forms[0].idExterneRubriqueEcran.value="<%=viewBean.getIdExterneRubriqueEcran()%>";
	document.forms[0].codeDebitCreditEcran.value="<%=viewBean.getCodeDebitCreditEcran()%>";
	document.forms[0].libelle.value="<%=globaz.jade.client.util.JadeStringUtil.change(viewBean.getLibelle(), "\"", "\\\"")%>";
	document.forms[0].piece.value="<%=viewBean.getPiece()%>";
	document.forms[0].idExterneCompteCourantEcran.value="<%=viewBean.getIdExterneCompteCourantEcran()%>";
  document.forms[0].elements('userAction').value="osiris.comptes.journalOperationVersement.ajouter";
	document.forms[0].idExterneRoleEcran.focus();
}

function upd() {
  document.forms[0].elements('userAction').value="osiris.comptes.journalOperationVersement.modifier";
	document.forms[0].idExterneRoleEcran.focus();

}
function del() {
	if (window.confirm("Sie sind dabei, die ausgwählte Einzahlung zu löschen! Wollen Sie fortfahren?")) {
        document.forms[0].elements('userAction').value="osiris.comptes.journalOperationVersement.supprimer";
        document.forms[0].submit();
    }

}

function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="osiris.comptes.journalOperationVersement.ajouter";
    else
        document.forms[0].elements('userAction').value="osiris.comptes.journalOperationVersement.modifier";

    return state;

}
function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="osiris.comptes.journalOperationVersement.afficher";
}
function init(){}

function postInit() {
	document.getElementById("idLog").disabled = false;
}

// Les fontionnalites !!!

function jsRechercheRubriqueEcran(){
	document.forms[0].rechercheRubriqueEcran.value="true";
	getSl('osiris.comptes.journalOperationVersement.afficher','osiris.comptes.rechercheRubrique.chercher', 'idCompte','' );
}
function jsInitRechercheRubriqueEcran(){
	document.forms[0].rechercheRubriqueEcran.value="false";
}

function jsRechercheCompteCourantEcran(){
	document.forms[0].rechercheCompteCourantEcran.value="true";
	getSl('osiris.comptes.journalOperationVersement.afficher','osiris.comptes.rechercheCompteCourant.chercher', 'idCompteCourant','' );
}
function jsInitRechercheCompteCourantEcran(){
	document.forms[0].rechercheCompteCourantEcran.value="false";
}

top.document.title = "Konti - Detail einer Einzahlung - " + top.location.href;
// stop hiding -->
</SCRIPT>
	<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Detail einer Einzahlung<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
		<input type="hidden" name="saisieEcran" value="true" tabindex="-1" >
		<input type="hidden" name="anneeCotisation" value="<%=viewBean.getAnneeCotisation()%>"/>
		<input type="hidden" name="codeDebitCredit" value="<%=viewBean.getCodeDebitCredit()%>"/>
		<input type="hidden" name="codeMaster" value="<%=viewBean.getCodeMaster()%>"/>
		<input type="hidden" name="etat" value="<%=viewBean.getEtat()%>"/>
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

		<TR>
			<TD nowrap width="129" height="34">
				<% if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getIdCompteAnnexe())) { %>
					<A href="osiris?userAction=osiris.comptes.apercuComptes.afficher&selectedId=<%=viewBean.getCompteAnnexe().getIdCompteAnnexe()%>">Konto</A>
				<% } else { %>
					Konto
				<% } %>
			</TD>
			<TD width="1">&nbsp;</TD>
            <TD width="30">
              <input type="hidden" name="idCompteAnnexe" value="<%=viewBean.getIdCompteAnnexe()%>" tabindex="-1" >
              <input type="text" name="idExterneRoleEcran" size="25" value="<%=viewBean.getIdExterneRoleEcran()%>" class="libelle"/>
            </TD>
            <TD width="11">&nbsp;</TD>
            <TD width="381">
              <select style="width:100%" name="idRoleEcran">
                <%CARole tempRole;
					 		CARoleManager manRole = new CARoleManager();
							manRole.setSession(objSession);
							manRole.find();
							for(int i = 0; i < manRole.size(); i++){
								tempRole = (CARole)manRole.getEntity(i);
                			if  (viewBean.getIdRoleEcran().equalsIgnoreCase(tempRole.getIdRole())) { %>
                <option selected value="<%=tempRole.getIdRole()%>"><%=tempRole.getDescription()%></option>
                <% } else { %>
                <option value="<%=tempRole.getIdRole()%>"><%=tempRole.getDescription()%></option>
                <% } %>
                <% } %>
              </select>
            </TD>
            <TD width="7">&nbsp;</TD>
            <TD nowrap height="34" width="329">
              <input type="text" name="descCompteAnnexe" maxlength="30" value="<%if (viewBean.getCompteAnnexe() != null) %><%=globaz.jade.client.util.JadeStringUtil.change(viewBean.getCompteAnnexe().getTiers().getNom(), "\"", "&quot;")%>" class="libelleLongDisabled" tabindex=-1 readonly>
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
            <TD width="30">
              <input type="hidden" name="idSection" value="<%=viewBean.getIdSection()%>" tabindex="-1" >
              <input type="text" name="idExterneSectionEcran" size="25" value="<%=viewBean.getIdExterneSectionEcran()%>" class="libelle"/>
            </TD>
            <TD width="11">&nbsp;</TD>
            <TD width="381">
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
            <TD width="9">&nbsp;</TD>
            <TD nowrap width="329">
              <input type="text" name="descSection" maxlength="30" value="<%if (viewBean.getSection() != null) {%><%=viewBean.getSection().getDescription()%><%}%>" class="libelleLongDisabled" tabindex=-1 readonly>
            </TD>
          </TR>

          <TR><TD nowrap colspan="7"><HR/></TD></TR>

          <TR>
            <TD nowrap width="129">Valutadatum</TD>
            <TD width="1">&nbsp;</TD>
            <TD colspan="5">
            <input type="text" name="date" value="<%=viewBean.getDate()%>" size="10" maxlength="10"/>
            </TD>
          </TR>

          <TR>
            <TD nowrap width="129">Rubrik</TD>
            <TD width="1">&nbsp;</TD>
			<TD width="30">
			<input type="text" name="idExterneRubriqueEcran" class="libelle" value="<%=viewBean.getIdExterneRubriqueEcran()%>" id="idExterneRubriqueEcran"/>
			</TD>
            <TD width="11">&nbsp;</TD>
            <TD colspan="2">
              <input type="hidden" name="idCompte" value="<%=viewBean.getIdCompte()%>" tabindex="-1" >
              <input type="hidden" name="rechercheRubriqueEcran" value="<%=viewBean.getRechercheRubriqueEcran()%>" tabindex="-1" >

              <input type="text" name="rubriqueDescription" size="30" maxlength="30" value="<%if (viewBean.getCompte() != null) {%><%=viewBean.getCompte().getDescription()%><%}%>" class="inputDisabled" tabindex="-1" readonly>
            </TD>
            <TD width="329">&nbsp;</TD>
          </TR>

          <TR>
            <TD nowrap width="129">Betrag</TD>
            <TD width="1">&nbsp;</TD>
            <TD width="30">
              <input type="text" name="montantEcran" size="30" maxlength="30" value="<%=viewBean.getMontantEcran()%>"  class="montant">
            </TD>
            <TD width="11">&nbsp;</TD>
            <TD colspan="3">
              <SELECT name="codeDebitCreditEcran" style="width : 2cm;">
                <% if (viewBean.getCodeDebitCreditEcran().equalsIgnoreCase("D")){%>
                <OPTION value="D">Soll</OPTION>
                <option value="C" >Haben</option>
                <%}else{%>
                <option value="D" >Soll</option>
                <option value="C" selected>Haben</option>
                <%}%>
              </SELECT>
            </TD>
          </TR>

          <TR>
            <TD nowrap width="129">Bemerkung</TD>
            <TD width="1">&nbsp;</TD>
            <TD colspan="5">
              <INPUT type="text" name="libelle" size="64" maxlength="40" value="<%=globaz.jade.client.util.JadeStringUtil.change(viewBean.getLibelle(), "\"", "&quot;")%>">
            </TD>
          </TR>

          <TR>
            <TD nowrap width="129">Buchhaltungsbeleg Nr.</TD>
            <TD width="1">&nbsp;</TD>
            <TD colspan="5">
              <INPUT type="text" name="piece" size="10" maxlength="10" value="<%=viewBean.getPiece()%>">
            </TD>
          </TR>

          <TR>
            <TD nowrap width="129">Kontokorrent</TD>
            <TD width="1">&nbsp;</TD>
            <TD width="30">
            	<input type="text" name="idExterneCompteCourantEcran" class="libelle" value="<%=viewBean.getIdExterneCompteCourantEcran()%>" id="idExterneCompteCourantEcran"/>
            </TD>
            <TD width="11">&nbsp;</TD>
            <TD colspan="2">
              <input type="hidden" name="idCompteCourant" value="<%=viewBean.getIdCompteCourant()%>" tabindex="-1" >
              <input type="hidden" name="rechercheCompteCourantEcran" value="<%=viewBean.getRechercheCompteCourantEcran()%>" tabindex="-1" >

              <input type="text" name="CCEcran" size="30" maxlength="30" value="<%if(viewBean.getCompteCourant() != null) {%><%=viewBean.getCompteCourant().getRubrique().getDescription()%><% } %>" class="inputDisabled"  readonly>
            </TD>
            <TD width="329">&nbsp;</TD>
          </TR>

          <TR>
            <TD nowrap width="129">Journal</TD>
            <TD width="1">&nbsp;</TD>
            <TD colspan="4">
              <input type="hidden" name="idJournal" value="<%=viewBean.getIdJournal()%>" tabindex="-1" >
              <input type="text" name="journalEcran" size="30" maxlength="30" value="<%=viewBean.getIdJournal()%> - <%=viewBean.getJournal().getLibelle()%>" class="inputDisabled" tabindex="-1" readonly>
            </TD>
            <TD width="329">&nbsp;</TD>
          </TR>

          <TR>
            <TD nowrap width="129">Status</TD>
            <TD width="1">&nbsp;</TD>
            <TD colspan="4">
              <input type="text" name="etatJournal" size="30" maxlength="30" value="<%=viewBean.getUcEtat().getLibelle()%>" class="inputDisabled" tabindex="-1" readonly>
            </TD>
            <TD width="329">&nbsp;</TD>
          </TR>

          <TR>
            <TD nowrap width="129">Mitteilung</TD>
            <TD width="1">&nbsp;</TD>
            <TD colspan="4">
              <select name="idLog" style="width : 16cm;" class="libelleErrorLongDisabled" tabindex="14" >
                <%if (viewBean.getLog() != null) {%>
                <%Enumeration e = viewBean.getLog().getMessagesToEnumeration();
					while (e.hasMoreElements()){
						FWMessage msg = (FWMessage)e.nextElement();%>
              <%if(msg.getIdLog().equalsIgnoreCase(viewBean.getIdLog())) {%>
                  <option selected  value="<%=msg.getIdLog()%>"><%=msg.getMessageText()%></option>
                <% } else {%>
                  <option value="<%=msg.getIdLog()%>"><%=msg.getMessageText()%></option>
                <% } %>
                <% } %>
                <% } %>
              </select>
            </TD>
            <TD width="329">&nbsp;</TD>
          </TR>

          <TR>
            <TD nowrap width="129">Quittieren</TD>
            <TD width="1">&nbsp;</TD>
            <TD colspan="5">
              <input type="checkbox" name="quittanceLogEcran" <%=(viewBean.getQuittanceLogEcran().booleanValue())? "checked" : "unchecked"%> tabindex="15" disabled>
            </TD>
          </TR>

          <TR>
            <TD nowrap colspan="7"><HR/></TD>
          </TR>

          <TR>
            <TD width="129">Zahlungsauftrag</TD>
            <TD width="1">&nbsp;</TD>
            <TD nowrap colspan="4">
            <input type="hidden" name="idOrdreVersement" value="<%=viewBean.getIdOrdreVersement()%>" tabindex="-1" >
            <input type="text" name="ordreVersement" size="30" maxlength="30" value="<%=viewBean.getIdOrdreVersement()%><%if (viewBean.getOrdreVersement() != null && !viewBean.getOrdreVersement().getIdOrdreGroupe().equals("0"))%><%=((globaz.osiris.db.comptes.CAOperationOrdreVersement) viewBean.getOrdreVersement()).getOrdreGroupe().getMotif()%>" class="inputDisabled" tabindex="-1" readonly >
            </TD>
            <TD width="329">&nbsp;</TD>
          </TR>

          <TR>
            <TD nowrap colspan="7"><HR/></TD>
          </TR>

          <tr>
            <td nowrap width="129">Nr. Sammelbuchung</td>
            <TD width="1">&nbsp;</TD>
            <td width="30">
              <input type="text" name="noEcritureCollectiveEcran" size="10" maxlength="10" value="<%if (!globaz.globall.util.JANumberFormatter.formatZeroValues(viewBean.getNoEcritureCollective(),false,true).equalsIgnoreCase(""))%><%=viewBean.getNoEcritureCollective()%>" class="numeroDisabled" tabindex="-1" readonly>
            </td>
            <td colspan="3" align="right">FB Journal&nbsp;
              <input type="text" name="libelle4" size="10" maxlength="10" value="<%=viewBean.getJournal().getNumeroJournalComptaGen()%>" class="numeroDisabled" tabindex="-1" readonly/>
            </td>
            <td>&nbsp;</td>
          </tr>

          <TR>
            <TD nowrap width="129">Identifikation</TD>
            <TD width="1">&nbsp;</TD>
            <TD colspan="5">
              <input type="text" name="idOperation" size="10" maxlength="10" value="<%=viewBean.getIdOperation()%>" class="numeroDisabled"  tabindex="-1" readonly>
            </TD>
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