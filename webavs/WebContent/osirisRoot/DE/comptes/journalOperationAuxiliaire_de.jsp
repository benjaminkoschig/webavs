<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA0048"; %>
<%@ page import="java.util.Enumeration" %>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%@ page import="globaz.framework.util.*" %>
<%@ page import="globaz.osiris.application.*" %>
<%
	CAAuxiliaire viewBean = null;
	globaz.framework.bean.FWViewBeanInterface viewBeanTemp = (globaz.framework.bean.FWViewBeanInterface) session.getAttribute (globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);

	if (viewBeanTemp instanceof CAAuxiliairePaiementViewBean) {
		viewBean = (CAAuxiliairePaiementViewBean) viewBeanTemp;
	} else {
		viewBean = (CAAuxiliaireViewBean) viewBeanTemp;
	}

	userActionValue = "osiris.comptes.journalOperationAuxiliaire.modifier";
	//selectedIdValue = viewBean.getIdJournal();
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
	actionNew = actionNew + "&idJournal="+viewBean.getIdJournal()+"&Etat="+viewBean.getEtat();//+"&Message="+viewBean.getMessage();

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
	document.forms[0].idExterneRubriqueEcran.value="<%=viewBean.getIdExterneRubriqueEcran()%>";
	document.forms[0].codeDebitCreditEcran.value="<%=viewBean.getCodeDebitCreditEcran()%>";
	document.forms[0].libelle.value="<%=globaz.jade.client.util.JadeStringUtil.change(viewBean.getLibelle(), "\"", "\\\"")%>";
	document.forms[0].piece.value="<%=viewBean.getPiece()%>";

	document.forms[0].elements('userAction').value="osiris.comptes.journalOperationAuxiliaire.ajouter";
	document.forms[0].idExterneRoleEcran.focus();
}

function upd() {
   document.forms[0].elements('userAction').value="osiris.comptes.journalOperationAuxiliaire.modifier";
	document.forms[0].idExterneRoleEcran.focus();
}

function del() {
	if (window.confirm("Sie sind dabei, die ausgewählte Buchung zu löschen! Wollen Sie fortfahren?")) {
        document.forms[0].elements('userAction').value="osiris.comptes.journalOperationAuxiliaire.supprimer";
        document.forms[0].submit();
    }

}

function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="osiris.comptes.journalOperationAuxiliaire.ajouter";
    else
        document.forms[0].elements('userAction').value="osiris.comptes.journalOperationAuxiliaire.modifier";

    return state;

}
function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="osiris.comptes.journalOperationAuxiliaire.afficher";
}
function init(){}

function postInit() {
	document.getElementById("idLog").disabled = false;
}

// Les fontionnalites !!!

function jsRechercheRubriqueEcran(){
	document.forms[0].rechercheRubriqueEcran.value="true";
	getSl('osiris.comptes.journalOperationAuxiliaire.afficher','osiris.comptes.rechercheRubrique.chercher', 'idCompte','');
}
function jsInitRechercheRubriqueEcran(){
	document.forms[0].rechercheRubriqueEcran.value="false";
}

function jsRechercheCompteCourantEcran(){
	document.forms[0].rechercheCompteCourantEcran.value="true";
	getSl('osiris.comptes.journalOperationAuxiliaire.afficher','osiris.comptes.rechercheCompteCourant.chercher', 'idCompteCourant','' );
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
top.document.title = "Konto - Detail einer Buchung - " + top.location.href;

<!-- AUTO COMPLETE SECTION -->

<%
	String jspAffilieSelectLocation = servletContext + mainServletPath + "Root/affilie_aux_select.jsp";
	String jspSectionsSelectLocation = servletContext + mainServletPath + "Root/sections_select.jsp";

	String jspLocation = servletContext + mainServletPath + "Root/rubrique_select.jsp";
%>

var tempIdCompteAnnexe = "";

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

            <input type="hidden" name="nomEcran" value="<%=viewBean.getNomEcran()%>"/>

            <TR>
            <TD nowrap width="129">
              <input type="hidden" name="saisieEcran" value="true">
				<% if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getIdCompteAnnexe())) { %>
					<A href="osiris?userAction=osiris.comptes.apercuComptes.afficher&selectedId=<%=viewBean.getCompteAnnexe().getIdCompteAnnexe()%>">Konto</A>
				<% } else { %>
					Konto
				<% } %>
			</TD>
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
            <TD width="7">&nbsp;</TD>
            <TD>
              <select style="width:100%" name="idRoleEcran">
				<%=CARoleViewBean.createOptionsTags(objSession, viewBean.getIdRoleEcran(), false)%>
              </select>
            </TD>
			<TD nowrap align="right">
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
            <TD width="147">
              <input type="hidden" name="idSection" value="<%=viewBean.getIdSection()%>">

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
            <TD width="300">
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
            <TD nowrap align="right">
              <input type="text" name="descSection" maxlength="30" value="<%if (viewBean.getSection() != null) {%><%=viewBean.getSection().getDescription()%><%}%>" class="libelleLongDisabled" readonly tabindex="-1">
            </TD>
          </TR>

		  <TR><TD nowrap colspan="5"><HR/></TD></TR>

          <TR>
            <TD nowrap width="129">Buchungsdatum</TD>
            <TD width="147">
				<ct:FWCalendarTag name="date" doClientValidation="CALENDAR" value="<%=viewBean.getDate()%>"/>
            </TD>
            <TD colspan="3">&nbsp;</TD>
          </TR>

          <TR>
            <TD nowrap width="129">Rubrik</TD>
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
			<TD width="7">&nbsp;<input type="hidden" name="idCompte" value="<%=viewBean.getIdCompte()%>" ></TD>
            <TD width="300">
              <input type="text" name="rubriqueDescription" size="30" value="<%if (viewBean.getCompte() != null) {%><%=viewBean.getCompte().getDescription()%><%}%>" class="libelleLongDisabled"  readonly tabindex="-1">
            </TD>
            <TD>&nbsp;</TD>
          </TR>

          <TR>
            <TD nowrap width="129">Betrag</TD>
            <TD width="147">
              <INPUT type="text" name="montantEcran" size="30" maxlength="30" value="<%=viewBean.getMontantEcran()%>"  class="montant"  >
            </TD>
            <TD width="7">&nbsp;</TD>
            <TD width="300">
              <SELECT name="codeDebitCreditEcran" style="width : 2cm;" >
                <% if (viewBean.getCodeDebitCreditEcran().equalsIgnoreCase("D")){%>
                <OPTION value="D">Soll</OPTION>
                <option value="C" >Haben</option>
                <%}else{%>
                <option value="D" >Soll/option>
                <option value="C" selected>Haben</option>
                <%}%>
              </SELECT>
            </TD>
            <TD>&nbsp;</TD>
          </TR>

          <TR>
            <TD nowrap width="129">Bezeichnung</TD>
            <TD colspan="4">
              <INPUT type="text" name="libelle" size="64" maxlength="40" value="<%=globaz.jade.client.util.JadeStringUtil.change(viewBean.getLibelle(), "\"", "&quot;")%>"/>
            </TD>
          </TR>
          <TR>
            <TD nowrap width="129">Buchhaltungsbelegs-Nr.</TD>
            <TD colspan="4">
              <input type="text" name="piece" size="10" maxlength="10" value="<%=viewBean.getPiece()%>"  >
            </TD>
          </TR>

          <tr>
          	<TD nowrap width="129">Typ</TD>
          	<TD width="147">
          	<select name="idTypeOperation">
          	<%
          		CATypeOperationManagerListViewBean manager = new CATypeOperationManagerListViewBean();
          		manager.setSession(objSession);
          		manager.setLikeIdTypeOperation(globaz.osiris.api.APIOperation.CAAUXILIAIRE);
          		manager.find();

          		for (int i=0; i<manager.getSize(); i++) {
          			CATypeOperation operation = (CATypeOperation) manager.getEntity(i);

          			if (viewBean.getIdTypeOperation().equals(operation.getIdTypeOperation())) {
          				out.write("<option selected value=\"" + operation.getIdTypeOperation() + "\">" + operation.getDescription() + "</option>");
          			} else {
	          			out.write("<option value=\"" + operation.getIdTypeOperation() + "\">" + operation.getDescription() + "</option>");
          			}
          		}
          	%>
          	</select>
          	</TD>
            <TD colspan="3">&nbsp;</TD>
          </tr>

          
		<tr>
			<td>Zahlungsherkunft</td>
			<td colspan="4">
             <div align="left">
                <%	viewBean.getCsProvenancePmt();
					globaz.globall.parameters.FWParametersSystemCode _motifContentieuxSus = null;
				%>
               <select name="provenancePmt">
               <%	for (int i=0; i < viewBean.getCsProvenancePmt().size(); i++) {
						_motifContentieuxSus = (globaz.globall.parameters.FWParametersSystemCode) viewBean.getCsProvenancePmt().getEntity(i);
						if (_motifContentieuxSus.getIdCode().equalsIgnoreCase(viewBean.getProvenancePmt())) { %>
               <option selected value="<%=_motifContentieuxSus.getIdCode()%>"><%=_motifContentieuxSus.getCurrentCodeUtilisateur().getLibelle()%></option>
               <%		} else { %>
               <option value="<%=_motifContentieuxSus.getIdCode()%>"><%=_motifContentieuxSus.getCurrentCodeUtilisateur().getLibelle()%></option>
               <%		}
					} %>
                </select>
              </div>
            </td>
		</tr>
		
          <SCRIPT language="JavaScript">
          updateSectionsAutoCompleteLink();
          </SCRIPT>


          <TR>
            <TD nowrap width="129">Journal</TD>
            <TD colspan="3">
              <input type="hidden" name="idJournal" value="<%=viewBean.getIdJournal()%>">
              <%	globaz.osiris.db.comptes.CAJournal journal = viewBean.getJournal();              %>
              <input type="text" name="journalEcran" size="30" maxlength="30" value="<%=viewBean.getIdJournal()%> - <%=journal==null?"":journal.getLibelle()%>" class="inputDisabled" readonly tabindex="-1">
            </TD>
            <TD>
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
            <TD nowrap width="129">Status</TD>
            <TD colspan="3">
              <input type="text" name="etatJournal" size="30" maxlength="30" value="<%=viewBean.getUcEtat().getLibelle()%>" class="inputDisabled"  readonly tabindex="-1">
            </TD>
            <TD>&nbsp;</TD>
          </TR>

          <TR>
            <TD nowrap width="129">Mitteilung</TD>
            <TD colspan="4">
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
            <TD nowrap width="129">&nbsp;</TD>
            <TD colspan="4" align="left">Quittieren
              <input type="checkbox" name="_quittanceLogEcran" value="on" disabled  onClick="quittancer()">
			  <input type="hidden" name="quittanceLogEcran" value="">
            </TD>
        </TR>

			<TR><TD nowrap colspan="5"><HR/></TD></TR>

          <TR>
            <TD nowrap width="129">Identifikation</TD>
            <TD colspan="4">
              <input type="text" name="idOperation" size="10" maxlength="10" value="<%=viewBean.getIdOperation()%>" class="numeroDisabled"  readonly tabindex="-1">
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