
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA0039"; %>
<%@ page import="java.util.Enumeration" %>
<%@ page import="globaz.osiris.db.utils.*" %>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%@ page import="globaz.osiris.db.ordres.*" %>
<%@ page import="globaz.framework.util.*" %>
<%@ page import="globaz.globall.util.*" %>

<%
globaz.osiris.db.comptes.CAOperationOrdreRecouvrementViewBean viewBean = (globaz.osiris.db.comptes.CAOperationOrdreRecouvrementViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<% // Si état est versé , uniquement visualiser
if (viewBean.getEtat().equals(CAOperation.ETAT_VERSE)) {
	bButtonUpdate = false;
}

bButtonDelete = false;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
function add() {
    document.forms[0].elements('userAction').value="osiris.ordres.apercuOrdresRecouvrement.ajouter";
    document.forms[0].date.focus();
}
function upd() {
  document.forms[0].elements('userAction').value="osiris.ordres.apercuOrdresRecouvrement.modifier";
  document.forms[0].date.focus();
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="osiris.ordres.apercuOrdresRecouvrement.ajouter";
    else
        document.forms[0].elements('userAction').value="osiris.ordres.apercuOrdresRecouvrement.modifier";

    return state;

}
function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="osiris.ordres.apercuOrdresRecouvrement.afficher";
}
function del() {
	if (window.confirm("Sie sind im dabei den ausgewählten Abschreibungs-Auftrag zu löschen! Wollen Sie fortfahren?")) {
        document.forms[0].elements('userAction').value="osiris.ordres.apercuOrdresRecouvrement.supprimer";
        document.forms[0].submit();
    }
}
function init(){
}

function postInit() {
	document.getElementById("idLog").disabled = false;
}

-->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Abschreibungs-Auftrag eingeben<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
          <TR>
            <TD nowrap width="129" height="34">
              <input type="hidden" name="saisieEcran" value="true"  >
                <% if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getIdCompteAnnexe())) { %>
					<A href="osiris?userAction=osiris.comptes.apercuComptes.afficher&selectedId=<%=viewBean.getCompteAnnexe().getIdCompteAnnexe()%>">Konto</A>
				<% } else { %>
					Konto
				<% } %>
            </TD>
            <TD width="5" height="34">&nbsp;</TD>
            <TD width="147">
              <input type="hidden" name="_idCompteAnnexe" value="<%=viewBean.getIdCompteAnnexe()%>"  >
              <input type="text" name="idExterneRoleEcran" size="16" maxlength="15" value="<%=viewBean.getIdExterneRoleEcran()%>" class="inputDisabled" tabindex=-1 readonly>
            </TD>
            <TD width="5"></TD>
            <TD colspan="4">
              <%CARole tempRole;
					 		CARoleManager manRole = new CARoleManager();
							manRole.setSession(objSession);
							manRole.find();
							for(int i = 0; i < manRole.size(); i++){
								tempRole = (CARole)manRole.getEntity(i);
                			if  (viewBean.getIdRoleEcran().equalsIgnoreCase(tempRole.getIdRole())) { %>
              <input type="hidden" name="idRoleEcran" value="<%=viewBean.getIdRoleEcran()%>"  >
              <input type="text" name="roleEcran" size="16" maxlength="15" value="<%=tempRole.getDescription()%>" class="inputDisabled" tabindex=-1 readonly>
              <% } %>
              <% } %>
            </TD>
            <TD width="5"></TD>
            <TD width="20">
              <input type="hidden" name="rechercheCompteAnnexeEcran" value="<%=viewBean.getRechercheCompteAnnexeEcran()%>" tabindex=-1 >
            </TD>
            <TD width="5"></TD>
            <TD nowrap height="34" width="160">
              <input type="text" name="descrCpteAnnexe" size="30" maxlength="30" value="<%if (viewBean.getCompteAnnexe() != null) %><%=globaz.jade.client.util.JadeStringUtil.change(viewBean.getCompteAnnexe().getTiers().getNom(), "\"", "&quot;")%>" class="inputDisabled" tabindex=-1 readonly>
            </TD>
            <TD width="15"></TD>
          </TR>
          <TR>
            <TD nowrap width="129">
				<% if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getIdSection())) { %>
					<A href="osiris?userAction=osiris.comptes.apercuParSection.afficher&id=<%=viewBean.getSection().getIdSection()%>">Sektion</A>
				<% } else { %>
					Sektion
				<% } %>
			</TD>
            <TD width="5"></TD>
            <TD width="147">
              <input type="hidden" name="idSection" value="<%=viewBean.getIdSection()%>"  >
              <input type="text" name="idExterneSectionEcran" size="16" maxlength="15" value="<%=viewBean.getIdExterneSectionEcran()%>" class="inputDisabled" tabindex=-1 readonly >
            </TD>
            <TD width="5"></TD>
            <TD colspan="4">
              <%CATypeSection tempTypeSection;
				  CATypeSectionManager manTypeSection = new CATypeSectionManager();
				  manTypeSection.setSession(objSession);
				  manTypeSection.find();
				  for(int i = 0; i < manTypeSection.size(); i++){
				    	tempTypeSection = (CATypeSection)manTypeSection.getEntity(i);
						if (viewBean.getSection().getIdTypeSection().equalsIgnoreCase(tempTypeSection.getIdTypeSection())) { %>
              <input type="text" name="typeSectionEcran" value="<%=tempTypeSection.getDescription()%>" class="inputDisabled" tabindex=-1 readonly>
              <% } %>
              <% } %>
            </TD>
            <TD width="5"></TD>
            <TD width="20">
              <input type="hidden" name="rechercheSectionEcran" value="<%=viewBean.getRechercheSectionEcran()%>"  >
<!--              <input type="hidden" name="btnSection" value="..." onClick="jsRechercheSectionEcran()" >-->
			<%
			Object[] sectionMethodsName = new Object[]{
				new String[]{"setIdSection","getIdSection"}
			};
			%>
			<ct:FWSelectorTag
				name="SectionSelector"

				methods="<%=sectionMethodsName%>"
				providerApplication ="osiris"
				providerPrefix="CA"
				providerAction ="osiris.comptes.rechercheSection.chercher"
				/>


            </TD>
            <TD width="5"></TD>
            <TD nowrap width="160">
              <input type="text" name="descrSection" size="30" maxlength="30" value="<%if (viewBean.getSection() != null) {%><%=viewBean.getSection().getDescription()%><%}%>" class="inputDisabled" tabindex=-1 readonly>
            </TD>
            <TD width="15"></TD>
          </TR>
          <TR>
            <TD nowrap colspan="12">
              <hr>
            </TD>
            <TD width="15"></TD>
          </TR>
          <TR>
            <TD nowrap width="129">&nbsp;</TD>
            <TD width="5"></TD>
            <TD width="147">&nbsp; </TD>
            <TD width="5">&nbsp;</TD>
            <TD width="20">&nbsp;</TD>
            <TD width="5">&nbsp;</TD>
            <TD width="100">&nbsp;</TD>
            <TD width="75">&nbsp;</TD>
            <TD width="5"></TD>
            <TD colspan="3"></TD>
            <TD width="15"></TD>
          </TR>
          <TR>
            <TD nowrap width="129">Ausführungsdatum</TD>
            <TD width="5"></TD>
            <TD width="147">
              <input type="text" name="date" size="10" maxlength="10" value="<%=viewBean.getDate()%>" class="date" tabindex="1" >
            </TD>
            <TD width="5"></TD>
            <TD width="20">&nbsp;</TD>
            <TD width="7">&nbsp;</TD>
            <TD width="100">&nbsp;</TD>
            <TD width="75">&nbsp;</TD>
            <TD width="5"></TD>
            <TD colspan="3">&nbsp; </TD>
            <TD width="15"></TD>
          </TR>
          <TR>
            <TD nowrap width="129">Betrag</TD>
            <TD nowrap width="5">&nbsp;</TD>
            <TD nowrap colspan="3">
              <INPUT type="text" name="_montant" size="10" maxlength="10" value="<%=JANumberFormatter.formatNoRound(viewBean.getMontant())%>" class="montantDisabled" tabindex="-1" readonly>
            </TD>
            <TD nowrap width="7">&nbsp;</TD>
            <TD nowrap width="100">&nbsp;</TD>
            <TD nowrap colspan="5">&nbsp; </TD>
            <TD width="15"></TD>
          </TR>
          <TR>
            <TD nowrap width="129">Abschreibungsadresse</TD>
            <TD width="5"></TD>
            <TD width="147">
              <input type="hidden" name="idAdressePaiement" value="<%=viewBean.getIdAdressePaiement()%>"  >
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
            <TD width="5">&nbsp;</TD>
            <TD width="20" valign="bottom">&nbsp;
            </TD>
            <TD width="7">&nbsp;</TD>
            <TD width="100">Abschreibungsmotiv</TD>
            <TD colspan="5">
              <TEXTAREA name="motif" cols="35" tabindex="12" rows="4"><%=viewBean.getMotif()%></TEXTAREA>
            </TD>
            <TD width="15"></TD>
          </TR>
          <TR>
            <TD nowrap width="129">Abschreibungsmotiv</TD>
            <TD width="5"></TD>
            <TD width="147">
              <%	viewBean.getCsNatureOrdres();
							globaz.globall.parameters.FWParametersSystemCode _natureOrdre = null; %>
              <select name="natureOrdre" class="libelleCourt" style="width : 5cm;" tabindex="5">
                <%	for (int i=0; i < viewBean.getCsNatureOrdres().size(); i++) {
								_natureOrdre = (globaz.globall.parameters.FWParametersSystemCode) viewBean.getCsNatureOrdres().getEntity(i);
								if (_natureOrdre.getIdCode().equalsIgnoreCase(viewBean.getNatureOrdre())) { %>
                <option selected value="<%=_natureOrdre.getIdCode()%>"><%=_natureOrdre.getCurrentCodeUtilisateur().getLibelle()%></option>
                <%	} else { %>
                <option value="<%=_natureOrdre.getIdCode()%>"><%=_natureOrdre.getCurrentCodeUtilisateur().getLibelle()%></option>
                <%	}
							} %>
              </select>
            </TD>
            <TD width="5">&nbsp;</TD>
            <TD width="20">&nbsp; </TD>
            <TD width="7">&nbsp;</TD>
            <TD width="100">Referenz</TD>
            <TD colspan="5">
              <input type="text" name="_referenceBVR" size="35" maxlength="27" value="<%=viewBean.getReferenceBVR()%>" class="inputDisabled" tabindex=-1 readonly >
            </TD>
            <TD width="15"></TD>
          </TR>
          <TR>
            <TD nowrap width="129">Abschreibung sperren</TD>
            <TD width="5"></TD>
            <TD width="147">
              <INPUT type="checkbox" name="estBloque" <%=(viewBean.getEstBloque().booleanValue())? "checked" : "unchecked"%> tabindex="5">
            </TD>
            <TD width="5"></TD>
            <TD width="20">&nbsp;</TD>
            <TD width="7">&nbsp;</TD>
            <TD width="100">Abhebung erfolgt</TD>
            <TD colspan="5">
              <input type="checkbox" name="_estRetire" <%=(viewBean.getEstRetire().booleanValue())? "checked" : "unchecked"%> readonly disabled>
            </TD>
            <TD width="15"></TD>
          </TR>
          <TR>
            <TD nowrap width="129">Gewünschtes Ausführungsorgan</TD>
            <TD width="5"></TD>
            <TD colspan="4">
              <%//	viewBean.getOrdreGroupe().getCsOrganeExecution();
				CAOrganeExecution _organeExecution = null;
				CAOrganeExecutionManager _CsOrganeExecution = new CAOrganeExecutionManager();
				_CsOrganeExecution.setSession(objSession);
				_CsOrganeExecution.setForIdTypeTraitementOG(true);
				_CsOrganeExecution .find();
				for (int i=0; i < _CsOrganeExecution.size(); i++) {
					_organeExecution = (CAOrganeExecution) _CsOrganeExecution.getEntity(i);
					if (_organeExecution .getIdOrganeExecution().equalsIgnoreCase(viewBean.getIdOrganeExecution())) { %>
              <input type="text" name="organeExecution" value="<%=_organeExecution.getNom()%>" class="Disabled" style="width:10cm" readonly="readonly" tabindex=-1  >
              <% }
			} %>
            </TD>
            <TD width="75">Verweigerungsgrund</TD>
            <TD width="5">
            	<select name="motifRefus">
            	</select>
            </TD>
            <TD colspan="3"></TD>
            <TD width="15"></TD>
          </TR>
          <TR>
            <TD nowrap width="129">Journal</TD>
            <TD width="5"></TD>
            <TD colspan="6">
              <input type="hidden" name="idJournal" value="<%=viewBean.getIdJournal()%>"  >
              <input type="text" name="journalEcran" value="<%=viewBean.getIdJournal()%> - <%=viewBean.getJournal().getLibelle()%>" class="Disabled" style="width:10cm" tabindex=-1 readonly>
            </TD>
            <TD width="5"></TD>
            <TD width="20">&nbsp; </TD>
            <TD width="5"></TD>
            <TD nowrap width="160">&nbsp; </TD>
            <TD width="15"></TD>
          </TR>
          <TR>
            <TD nowrap width="129">Status</TD>
            <TD width="5"></TD>
            <TD width="147">
              <input type="text" name="etatJournal" size="30" maxlength="30" value="<%=viewBean.getUcEtat().getLibelle()%>" class="inputDisabled"  readonly>
            </TD>
            <TD width="5">&nbsp;</TD>
            <TD width="20">&nbsp;</TD>
            <TD width="7">&nbsp;</TD>
            <TD width="100">&nbsp;</TD>
            <TD colspan="5">&nbsp; </TD>
            <TD width="15"></TD>
          </TR>
          <TR>
            <TD nowrap width="129">Mitteilung</TD>
            <TD width="5"></TD>
            <TD colspan="10">
              <select name="idLog" style="width : 16cm;" class="libelleErrorLongDisabled" tabindex="7">
                <%if (viewBean.getLog() != null) {%>
                <%Enumeration e = viewBean.getLog().getMessagesToEnumeration();
					while (e.hasMoreElements()){
						FWMessage msg = (FWMessage)e.nextElement();%>
              <%if(msg.getIdLog().equals(viewBean.getIdLog())) { %>
                <option selected  value="<%=msg.getIdLog()%>" ><%=msg.getMessageText()%></option>
               <%} else {%>
               <option value="<%=msg.getIdLog()%>" ><%=msg.getMessageText()%></option>
               <%}%>
                <% } %>
                <% } %>
              </select>
            </TD>
            <TD width="15"></TD>
          </TR>
          <TR>
            <TD nowrap width="129">&nbsp;</TD>
            <TD width="5"></TD>
            <TD colspan="10"> Quittieren
              <input type="checkbox" name="quittanceLogEcran" <%=(viewBean.getQuittanceLogEcran().booleanValue())? "checked" : "unchecked"%> tabindex="8" disabled>
            </TD>
            <TD width="15"></TD>
          </TR>
          <TR>
            <TD nowrap colspan="12">
              <hr>
            </TD>
            <TD width="15"></TD>
          </TR>
          <tr>
            <td width="129">
              <p>Gruppierter Auftrag Gesellschafter</p>
            </td>
            <td nowrap width="5">&nbsp;</td>
            <td nowrap colspan="3">
              <input type="hidden" name="forIdOrdreGroupe" value="<%=viewBean.getIdOrdreGroupe()%>"/>
              <input type="text" name="ordreGroupe" size="30" maxlength="30" value="<%if (!globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getIdOrdreGroupe())){%><%=viewBean.getIdOrdreGroupe()%> - <%if (!viewBean.getIdOrdreGroupe().equalsIgnoreCase("0"))%><%=viewBean.getOrdreGroupe().getMotif()%><%}%>" class="inputDisabled" tabindex=-1 readonly >
            </td>
            <td nowrap width="7">&nbsp;</td>
            <td nowrap width="100">Transaktions-Nr.</td>
            <td nowrap colspan="5">
              <input type="text" name="numTransaction" size="10" maxlength="10" value="<%=viewBean.getNumTransaction()%>" class="numeroDisabled"  tabindex=-1 readonly>
            </td>
            <td width="15"></td>
          </tr>
          <TR>
            <TD width="129">
              <p>Identifizierung</p>
            </TD>
            <TD nowrap width="5">&nbsp;</TD>
            <TD nowrap colspan="3">
              <INPUT type="text" name="idOperation" size="10" maxlength="10" value="<%=viewBean.getIdOperation()%>" class="numeroDisabled" tabindex="-1" readonly>
            </TD>
            <TD nowrap width="7">&nbsp;</TD>
            <TD nowrap width="100">&nbsp;</TD>
            <TD nowrap colspan="5">&nbsp; </TD>
            <TD width="15"></TD>
          </TR>
          <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<%	} %>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>