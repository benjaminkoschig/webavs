<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA0052"; %>
<%@ page import="java.util.Enumeration" %>
<%@ page import="globaz.osiris.db.utils.*" %>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%@ page import="globaz.osiris.db.ordres.*" %>
<%@ page import="globaz.framework.util.*" %>
<%@ page import="globaz.globall.util.*" %>
<%
	CAOperationOrdreRecouvrementViewBean viewBean = (CAOperationOrdreRecouvrementViewBean) session.getAttribute (globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
	userActionValue = "osiris.comptes.journalOperationOrdreRecouvrement.modifier";
//	selectedIdValue = viewBean.getIdJournal();
	selectedIdValue = viewBean.getIdOperation();
//	bButtonUpdate = (bButtonUpdate && viewBean.isUpdatable());
	boolean isDisplayUpdate = (bButtonUpdate && viewBean.isUpdatable());
	// Interdire les modifications si le journal n'est pas mutable
	if (viewBean.getJournal() != null) {
      if (!viewBean.getJournal().isUpdatable()) {
	    bButtonNew = false;
	    bButtonUpdate = false;
	  } else {
		  bButtonNew = true;
	  }
	}
	bButtonDelete = (bButtonDelete && !viewBean.getEstComptabilise().booleanValue());
	bButtonNew = (bButtonNew && viewBean.hasRightAdd() && !viewBean.getEstComptabilise().booleanValue());
	actionNew = actionNew + "&idJournal="+viewBean.getIdJournal()+"&Etat="+viewBean.getEtat();
	// ALD Ajout
	if(CAOperationOrdreRecouvrementViewBean.ETAT_VERSE.equals(viewBean.getEtat())){
		bButtonUpdate = false;
	}

	int autoCompleteStart = globaz.osiris.parser.CAAutoComplete.getCompteAnnexeAutoCompleteStart(objSession);
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

	<%if (!globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getIdRoleEcran())) {%>
		document.forms[0].idRoleEcran.value="<%=viewBean.getIdRoleEcran()%>";
	<%}%>

	<%if (!globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getIdTypeSectionEcran())) {%>
		document.forms[0].idTypeSectionEcran.value="<%=viewBean.getIdTypeSectionEcran()%>";
	<%}%>

	document.forms[0].date.value="<%=viewBean.getDate()%>";
	document.forms[0].codeISOMonnaieBonification.value="<%=(globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getCodeISOMonnaieBonification()))?"CHF":viewBean.getCodeISOMonnaieBonification()%>";
	document.forms[0].natureOrdre.value="<%=viewBean.getNatureOrdre()%>";
	document.forms[0].idOrganeExecution.value="<%=viewBean.getIdOrganeExecution()%>";
	document.forms[0].motif.value="<%=viewBean.getMotif()%>";

    document.forms[0].elements('userAction').value="osiris.comptes.journalOperationOrdreRecouvrement.ajouter";
}

function upd() {
	document.forms[0].elements('userAction').value="osiris.comptes.journalOperationOrdreRecouvrement.modifier";
}

function del() {
	if (window.confirm("Sie sind dabei, den Inkassoauftrag zu löschen! Wollen Sie fortfahren?")) {
        document.forms[0].elements('userAction').value="osiris.comptes.journalOperationOrdreRecouvrement.supprimer";
        document.forms[0].submit();
    }

}

function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="osiris.comptes.journalOperationOrdreRecouvrement.ajouter";
    else
        document.forms[0].elements('userAction').value="osiris.comptes.journalOperationOrdreRecouvrement.modifier";

    return state;

}
function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="osiris.comptes.journalOperationOrdreRecouvrement.afficher";
}
function init(){}

function postInit() {
	document.getElementById("idLog").disabled = false;
}

top.document.title = "Comptes - détail d'un ordre de recouvrement - " + top.location.href;
function bloquer() {
	document.getElementById('estBloque').checked = "checked";
	document.getElementById('estBloque').value = "true";
	document.getElementById('estBloque').disabled = false;
	document.forms[0].elements('userAction').value = "osiris.comptes.journalOperationOrdreRecouvrement.modifier";
	
  	document.forms[0].submit();
}

<!-- AUTO COMPLETE SECTION -->

<%
	String jspAffilieSelectLocation = servletContext + mainServletPath + "Root/affilie_select.jsp";
	String jspSectionsSelectLocation = servletContext + mainServletPath + "Root/sections_select.jsp";
%>

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

	} else {
		document.getElementById('idSection').value = "";
	}
}

function updateSectionsAutoCompleteLink() {
	tempIdExterneRole = document.getElementById('idExterneRoleEcran').value;
	if (document.getElementById('idRoleEcran') != null) {
		tempIdRole = document.getElementById('idRoleEcran').value;
	}
	idExterneSectionEcranPopupTag.updateJspName('<%=jspSectionsSelectLocation%>?tempIdExterneRole=' + tempIdExterneRole + '&tempIdRole=' + tempIdRole + '&like=');
}


// stop hiding -->
</SCRIPT>  <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Inkassoauftrag<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
              <input type="hidden" name="saisieEcran" value="true" tabindex="-1" >
              <input type="hidden" name="codeMaster" value="<%=viewBean.getCodeMaster()%>"/>
              <input type="hidden" name="coursConversion" value="<%=viewBean.getCoursConversion()%>"/>
              <input type="hidden" name="etat" value="<%=viewBean.getEtat()%>"/>
              <input type="hidden" name="idCompteCourant" value="<%=viewBean.getIdCompteCourant()%>"/>
              <input type="hidden" name="idExterneCompteCourantEcran" value="<%=viewBean.getIdExterneCompteCourantEcran()%>"/>
              <input type="hidden" name="idOrdreGroupe" value="<%=viewBean.getIdOrdreGroupe()%>"/>
              <input type="hidden" name="idTypeOperation" value="<%=viewBean.getIdTypeOperation()%>"/>
              <input type="hidden" name="nomCache" value="<%=viewBean.getNomCache()%>"/>
              <input type="hidden" name="nomEcran" value="<%=viewBean.getNomEcran()%>"/>
              <input type="hidden" name="rechercheCompteCourantEcran" value="<%=viewBean.getRechercheCompteCourantEcran()%>"/>
              <input type="hidden" name="valeurConversion" value="<%=viewBean.getValeurConversion()%>"/>

          <TR>
            <TD nowrap height="34" width="100">
				<% if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getIdCompteAnnexe())) { %>
					<A href="osiris?userAction=osiris.comptes.apercuComptes.afficher&selectedId=<%=viewBean.getCompteAnnexe().getIdCompteAnnexe()%>">Konto</A>
				<% } else { %>
					Konto
				<% } %>
			</TD>
            <TD>
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
            <TD colspan="2" width="300">
              <select style="width:100%" name="idRoleEcran" class="<%=isDisplayUpdate?"libelleCourt":"libelleCourtDisabled"%>">
              	<%=CARoleViewBean.createOptionsTags(objSession, viewBean.getIdRoleEcran(), false)%>
              </select>
            </TD>
            <TD nowrap height="34" align="right">
              <input type="text" name="descCompteAnnexe" maxlength="30" value="<%if (viewBean.getCompteAnnexe() != null) %><%=globaz.jade.client.util.JadeStringUtil.change(viewBean.getCompteAnnexe().getTiers().getNom(), "\"", "&quot;")%>" class="libelleLongDisabled" tabindex="-1" readonly>
            </TD>
          </TR>
          <TR>
            <TD>
				<% if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getIdSection())) { %>
					<A href="osiris?userAction=osiris.comptes.apercuParSection.afficher&id=<%=viewBean.getSection().getIdSection()%>">Sektion</A>
				<% } else { %>
					Sektion
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
	           onChange="updateIdSection(tag);"
	           minNbrDigit="1"
	           params="tempIdExterneRole=' + tempIdExterneRole + '&tempIdRole=' + tempIdRole + '"
	          />

            </TD>
            <TD width="7">&nbsp;</TD>
            <TD colspan="2">
              <select style="width:100%" name="idTypeSectionEcran" class="<%=isDisplayUpdate?"libelleCourt":"libelleCourtDisabled"%>">
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
              <input type="text" name="descSection" maxlength="30" value="<%if (viewBean.getSection() != null) {%><%=viewBean.getSection().getDescription()%><%}%>" class="libelleLongDisabled" tabindex="-1" readonly>
            </TD>
          </TR>

          <TR><TD nowrap colspan="6"><HR/></TD></TR>
          <TR>
            <TD>Fälligkeitsdatum</TD>
            <TD width="147">
				<ct:FWCalendarTag name="date" doClientValidation="CALENDAR" value="<%=viewBean.getDate()%>"/>
            </TD>
            <TD colspan="4">&nbsp;</TD>
          </TR>
          <TR>
            <TD>Betrag zu belasten</TD>
            <TD width="230">
              <input type="text" name="montant" onchange="validateFloatNumber(this)" onkeypress="filterCharForFloat(window.event)" maxlength="10" value="<%=JANumberFormatter.formatNoRound(viewBean.getMontant())%>"  class="Montant" <%=isDisplayUpdate?"":"readonly"%>>
              <%	viewBean.getCsMonnaies();
							globaz.globall.parameters.FWParametersSystemCode _monnaieBonification = null; %>
              <select name="codeISOMonnaieBonification" style="width : 1.5cm;" class="<%=isDisplayUpdate?"libelleCourt":"libelleCourtDisabled"%>">
                <%	for (int i=0; i < viewBean.getCsMonnaies().size(); i++) {
								_monnaieBonification = (globaz.globall.parameters.FWParametersSystemCode) viewBean.getCsMonnaies().getEntity(i);
								if ((!globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getCodeISOMonnaieBonification().trim()) && _monnaieBonification.getCurrentCodeUtilisateur().getCodeUtilisateur().equalsIgnoreCase(viewBean.getCodeISOMonnaieBonification().trim())) || "CHF".equalsIgnoreCase(_monnaieBonification.getCurrentCodeUtilisateur().getCodeUtilisateur())) { %>
                <option selected value="<%=_monnaieBonification.getCurrentCodeUtilisateur().getCodeUtilisateur()%>"><%=_monnaieBonification.getCurrentCodeUtilisateur().getCodeUtilisateur()%></option>
                <%	} else { %>
                <option value="<%=_monnaieBonification.getCurrentCodeUtilisateur().getCodeUtilisateur()%>"><%=_monnaieBonification.getCurrentCodeUtilisateur().getCodeUtilisateur()%></option>
                <%	}
							} %>
              </select>
            </TD>
            <TD nowrap width="7">&nbsp;</TD>
            <TD></TD>
            <TD>
            </TD>
          </TR>
          <TR>
            <TD>Forderungsadresse</TD>
            <TD width="147">
              <input type="hidden" name="idAdressePaiement" value="<%=viewBean.getIdAdressePaiement()%>" tabindex="-1" >
              <%
              	CAAdressePaiementFormatter _adr = new CAAdressePaiementFormatter();
			  	String[] _fullAdr = new String[0];

			  	try {
				  	if (viewBean.getAdressePaiement() != null) {
				  		_adr.setAdressePaiement(viewBean.getAdressePaiement());
						_fullAdr = _adr.getFullAddress();
					}
				} catch (Exception e) {
					_fullAdr = new String[1];
					_fullAdr[0] = "ERREUR lors de la récupération de l'adresse de débit !";
			  	}
			  %>
              <textarea cols="25" rows="4" tabindex="-1" <%if (_fullAdr.length == 1 && _fullAdr[0].startsWith("ERREUR")) { %> style="background-color: red;color: white;" <% } %> readonly><%for (int i = 0; i < _fullAdr.length; i++){%><%=_fullAdr[i] +"\r\n"%><%}%></textarea>
            </TD>
            <TD width="7">&nbsp;
            </TD>
            <TD width="100">Auszahlungsgrund</TD>
            <TD colspan="2">
              <textarea cols="35" rows="4" <%=isDisplayUpdate?"name='motif'":"readonly"%>><%=viewBean.getMotif()%></textarea>
            </TD>
          </TR>
          <TR>
            <TD>Grund der Forderung</TD>
            <TD width="147">
            <%
            viewBean.getCsNatureOrdres();
			globaz.globall.parameters.FWParametersSystemCode _natureOrdre = null;
			%>
            <SELECT name="natureOrdre" class="<%=isDisplayUpdate?"libelleCourt":"libelleCourtDisabled"%>">
            <%
            	for (int i=0; i < viewBean.getCsNatureOrdres().size(); i++) {
					_natureOrdre = (globaz.globall.parameters.FWParametersSystemCode) viewBean.getCsNatureOrdres().getEntity(i);
					if (_natureOrdre.getIdCode().equalsIgnoreCase(viewBean.getNatureOrdre())) { %>
                		<option selected value="<%=_natureOrdre.getIdCode()%>"><%=_natureOrdre.getCurrentCodeUtilisateur().getLibelle()%></option>
                <%	} else { %>
                	<option value="<%=_natureOrdre.getIdCode()%>"><%=_natureOrdre.getCurrentCodeUtilisateur().getLibelle()%></option>
                <%	}
			} %>
            </SELECT>
            </TD>
            <TD width="7">&nbsp;</TD>
            <TD width="100">Referenz</TD>
            <TD colspan="2">
              <input type="text" name="referenceBVR" size="35" maxlength="27" value="<%=viewBean.getReferenceBVR()%>" <%=isDisplayUpdate?"":"readonly"%>>
            </TD>
          </TR>
          <TR>
            <TD>Forderung blockieren</TD>
            <TD width="147">
              <input type="checkbox" name="estBloque" <%=(viewBean.getEstBloque().booleanValue())? "checked" : "unchecked"%>>
            </TD>
            <TD width="7">&nbsp;</TD>
            <TD width="100">Rückzug gemacht</TD>
            <TD colspan="2">
              <input type="checkbox" name="estRetire" <%=(viewBean.getEstRetire().booleanValue())? "checked" : "unchecked"%> readonly disabled>
            </TD>
          </TR>
          <TR>
            <TD>gewünschtes Ausführungsorgan</TD>
            <TD>
              <%//	viewBean.getOrdreGroupe().getCsOrganeExecution();
				CAOrganeExecution _organeExecution = null;
				CAOrganeExecutionManager _CsOrganeExecution = new CAOrganeExecutionManager();
				_CsOrganeExecution.setSession(objSession);
				_CsOrganeExecution.setForIdTypeTraitementOG(true);
				_CsOrganeExecution.find(); %>
              <select size="width:100%" name="idOrganeExecution" class="<%=isDisplayUpdate?"libelleCourt":"libelleCourtDisabled"%>">
              <option value=""></option>
                <%	for (int i=0; i < _CsOrganeExecution.size(); i++) {
				_organeExecution = (CAOrganeExecution) _CsOrganeExecution.getEntity(i);
				if (_organeExecution .getIdOrganeExecution().equalsIgnoreCase(viewBean.getIdOrganeExecution())) { %>
                <option selected value="<%=_organeExecution .getIdOrganeExecution()%>"><%=_organeExecution.getNom()%></option>
                <%	} else { %>
                <option value="<%=_organeExecution .getIdOrganeExecution()%>"><%=_organeExecution .getNom()%></option>
                <%	}
			} %>
              </select>
            </TD>
            <td></td>
            <td>Verweigerungsgrund</td>
            <td>
            	<input type="text" name="" value="" class="inputDisabled" tabindex="-1" readonly="readonly" size="35">
            </td>
          </TR>

          <SCRIPT language="JavaScript">
          updateSectionsAutoCompleteLink();
          </SCRIPT>

          <TR>
            <TD>Journal</TD>
            <TD colspan="4">
              <input type="hidden" name="idJournal" value="<%=viewBean.getIdJournal()%>" tabindex="-1" >
             <%	globaz.osiris.db.comptes.CAJournal journal = viewBean.getJournal();              %>
              <input type="text" name="journalEcran" size="30" maxlength="30" value="<%=viewBean.getIdJournal()%> - <%=journal==null?"":journal.getLibelle()%>" class="inputDisabled" tabindex="-1" readonly>
            </TD>
			<TD>&nbsp;
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
            <TD>Status</TD>
            <TD colspan="4">
              <input type="text" name="etatJournal" size="30" maxlength="30" value="<%=viewBean.getUcEtat().getLibelle()%>" class="inputDisabled" tabindex="-1" readonly>
            </TD>
            <TD>&nbsp;</TD>
          </TR>
          <TR>
            <TD>Mitteilung</TD>
            <TD colspan="5">
              <select name="idLog" style="width : 16cm;" class="libelleErrorLongDisabled" tabindex="18" >
                <%if (viewBean.getLog() != null) {%>
                <%Enumeration e = viewBean.getLog().getMessagesToEnumeration();
					while (e.hasMoreElements()){
						FWMessage msg = (FWMessage)e.nextElement();%>
            <%if(msg.getIdLog().equalsIgnoreCase(viewBean.getIdLog())) {%>
              <option value="<%=msg.getIdLog()%>" selected><%=msg.getMessageText()%></option>
              <%}else{%>
              <option value="<%=msg.getIdLog()%>"><%=msg.getMessageText()%></option>
              <% } %>
                <% } %>
                <% } %>
              </select>
            </TD>
          </TR>
          <TR>
            <TD> Quittieren </td>
            <TD colspan="5" align="left">
              <input type="checkbox" name="quittanceLogEcran" <%=(viewBean.getQuittanceLogEcran().booleanValue())? "checked" : "unchecked"%> tabindex="19" disabled>
            </TD>
          </TR>

          <TR><TD nowrap colspan="6"><HR/></TD></TR>

          <TR>
            <td width="129">
				<% if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getIdOrdreGroupe())) { %>
					<A href="osiris?userAction=osiris.ordres.ordresGroupes.afficher&selectedId=<%=viewBean.getIdOrdreGroupe()%>">Sammelauftrag</A>
				<% } else { %>
					Ordre group&eacute; associ&eacute;
				<% } %>
			</td>
            <td nowrap colspan="2">
              <input type="text" name="ordreGroupe" size="30" maxlength="30" value="<%if (!globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getIdOrdreGroupe())){%><%=viewBean.getIdOrdreGroupe()%> - <%if (!viewBean.getIdOrdreGroupe().equalsIgnoreCase("0"))%><%=viewBean.getOrdreGroupe().getMotif()%><%}%>" class="inputDisabled" tabindex="-1" readonly >
            </td>
            <td nowrap width="7">&nbsp;</td>
            <td nowrap width="100">Transaktion-Nr.</td>
            <td nowrap>
              <input type="text" name="numTransaction" size="10" maxlength="10" value="<%=viewBean.getNumTransaction()%>" class="numeroDisabled"  tabindex="-1" readonly>
            </td>
          </TR>

          <TR>
            <TD width="129">Identifizierung</TD>
            <TD nowrap colspan="5">
              <input type="text" name="idOperation" size="10" maxlength="10" value="<%=viewBean.getIdOperation()%>" class="numeroDisabled"  tabindex="-1" readonly>
            </TD>
          </TR>

          <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
<%				
if ((viewBean.getJournal() != null) && (viewBean.getJournal().isComptabilise()) && (globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getIdOrdreGroupe())) && !viewBean.getEstBloque().booleanValue()) {
%>
<INPUT type="button" value="Sperren" onclick="bloquer()"/>	
<%
}
%>					

				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <SCRIPT>
		//mettreTauxBase();
		</SCRIPT> <%	}
%> <%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>