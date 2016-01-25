
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA0041"; %>
<%@ page import="java.util.Enumeration" %>
<%@ page import="globaz.osiris.db.utils.*" %>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%@ page import="globaz.osiris.db.ordres.*" %>
<%@ page import="globaz.framework.util.*" %>
<%@ page import="globaz.globall.util.*" %>
<%@ page import="globaz.osiris.utils.CAUtil" %>
<%
globaz.osiris.db.comptes.CAOperationOrdreVersementViewBean viewBean =
  (globaz.osiris.db.comptes.CAOperationOrdreVersementViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
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
<ct:menuChange displayId="options" menuId="CA-OrdresGroupes" showTab="options">
	<ct:menuSetAllParams key="id" value="<%=viewBean.getIdOrdreGroupe()%>"/>
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdOrdreGroupe()%>"/>
	
	<ct:menuActivateNode active="yes" nodeId="<%=CAUtil.ID_MENU_NODE_CA_ORDRES_GROUPES_PREPARATION %>"/>
	<ct:menuActivateNode active="yes" nodeId="<%=CAUtil.ID_MENU_NODE_CA_ORDRES_GROUPES_EXECUTION %>"/>
	<ct:menuActivateNode active="yes" nodeId="<%=CAUtil.ID_MENU_NODE_CA_ORDRES_GROUPES_ANNULER %>"/>
	<% if (!"0".equals(viewBean.getOrdreGroupe().getEtat())) { %>
		<ct:menuActivateNode active="no" nodeId="<%=CAUtil.ID_MENU_NODE_CA_ORDRES_GROUPES_PREPARATION %>"/>
	<% } %>	
		
	<% if (CAOrdreGroupe.ANNULE.equals(viewBean.getOrdreGroupe().getEtat())) { %>
		<ct:menuActivateNode active="no" nodeId="<%=CAUtil.ID_MENU_NODE_CA_ORDRES_GROUPES_EXECUTION %>"/>
		<ct:menuActivateNode active="no" nodeId="<%=CAUtil.ID_MENU_NODE_CA_ORDRES_GROUPES_ANNULER %>"/>
	<% } %>	
			
</ct:menuChange>

<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
function add() {
    document.forms[0].elements('userAction').value="osiris.ordres.apercuOrdresVersement.ajouter";
    document.forms[0].date.focus();
}
function upd() {
  document.forms[0].elements('userAction').value="osiris.ordres.apercuOrdresVersement.modifier";
  document.forms[0].date.focus();
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="osiris.ordres.apercuOrdresVersement.ajouter";
    else
        document.forms[0].elements('userAction').value="osiris.ordres.apercuOrdresVersement.modifier";

    return state;
}
function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="osiris.ordres.apercuOrdresVersement.afficher";
}
function del() {
	if (window.confirm("Vous êtes sur le point de supprimer l'ordre de versement sélectionné! Voulez-vous continuer?")) {
        document.forms[0].elements('userAction').value="osiris.ordres.apercuOrdresVersement.supprimer";
        document.forms[0].submit();
    }
}
function init(){
}

function postInit() {
	document.getElementById("idLog").disabled = false;
}

// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Saisir un ordre de versement<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
          <TR>
            <TD nowrap width="129" height="34">
              <input type="hidden" name="saisieEcran" value="true"  >
                <% if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getIdCompteAnnexe())) { %>
					<A href="osiris?userAction=osiris.comptes.apercuComptes.afficher&selectedId=<%=viewBean.getCompteAnnexe().getIdCompteAnnexe()%>">Compte</A>
				<% } else { %>
					Compte
				<% } %>
            </TD>
            <TD width="5" height="34">&nbsp;</TD>
            <TD width="147">
              <input type="hidden" name="idCompteAnnexe" value="<%=viewBean.getIdCompteAnnexe()%>"  >
              <input type="text" name="idExterneRoleEcran" size="16" maxlength="15" value="<%=viewBean.getIdExterneRoleEcran()%>" class="inputDisabled" tabindex=-1 readonly>
              <input type="hidden" name ="forIdOrdreGroupe" value="<%=viewBean.getIdOrdreGroupe()%>">
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
					<A href="osiris?userAction=osiris.comptes.apercuParSection.afficher&id=<%=viewBean.getSection().getIdSection()%>">Section</A>
				<% } else { %>
					Section
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
				    	if(viewBean.getSection() != null)
						if ((viewBean.getSection() != null) && viewBean.getSection().getIdTypeSection().equalsIgnoreCase(tempTypeSection.getIdTypeSection())) { %>
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
            <TD nowrap width="129">Type d'ordre</TD>
            <TD width="5"></TD>
            <TD width="147">
              <%	viewBean.getCsTypeVirements();
							globaz.globall.parameters.FWParametersSystemCode _typeVirement = null;
                			for (int i=0; i < viewBean.getCsTypeVirements().size(); i++) {
								_typeVirement = (globaz.globall.parameters.FWParametersSystemCode) viewBean.getCsTypeVirements().getEntity(i);
								if (_typeVirement.getIdCode().equalsIgnoreCase(viewBean.getTypeVirement())) { %>
              <input type="text" name="typeOrdre" size="16" maxlength="15" value="<%=_typeVirement.getCurrentCodeUtilisateur().getLibelle()%>" class="inputDisabled" tabindex=-1 readonly>
              <%	}
							} %>
            </TD>
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
            <TD nowrap width="129">Date d'&eacute;ch&eacute;ance</TD>
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
            <TD nowrap width="129">Montant &agrave; bonifier</TD>
            <TD nowrap width="5">&nbsp;</TD>
            <TD nowrap colspan="3">
              <INPUT type="text" name="montant" size="10" maxlength="10" value="<%=JANumberFormatter.formatNoRound(viewBean.getMontant())%>" class="montantDisabled" tabindex="-1" readonly>
              <%	viewBean.getCsMonnaies();
							globaz.globall.parameters.FWParametersSystemCode _monnaieBonification = null;
							for (int i=0; i < viewBean.getCsMonnaies().size(); i++) {
								_monnaieBonification = (globaz.globall.parameters.FWParametersSystemCode) viewBean.getCsMonnaies().getEntity(i);
								if (_monnaieBonification.getCurrentCodeUtilisateur().getCodeUtilisateur().equalsIgnoreCase(viewBean.getCodeISOMonnaieBonification())) { %>
              <input type="text" name="monnaieBonification" size="5" maxlength="5" value="<%=_monnaieBonification.getCurrentCodeUtilisateur().getCodeUtilisateur()%>" tabindex="-1" readonly class="inputDisabled" style="width : 1.5cm;">
              <%	}
							} %>
            </TD>
            <TD nowrap width="7">&nbsp;</TD>
            <TD nowrap width="100">Monnaie de d&eacute;p&ocirc;t</TD>
            <TD nowrap colspan="5">
              <%	viewBean.getCsMonnaies();
							globaz.globall.parameters.FWParametersSystemCode _monnaieDepot = null;
							for (int i=0; i < viewBean.getCsMonnaies().size(); i++) {
								_monnaieDepot = (globaz.globall.parameters.FWParametersSystemCode) viewBean.getCsMonnaies().getEntity(i);
								if (_monnaieDepot.getCurrentCodeUtilisateur().getCodeUtilisateur().equalsIgnoreCase(viewBean.getCodeISOMonnaieDepot())) { %>
              <input type="text" name="monnaieDepot" size="5" maxlength="5" value="<%=_monnaieDepot.getCurrentCodeUtilisateur().getCodeUtilisateur()%>" tabindex="-1" readonly class="inputDisabled" style="width : 1.5cm;">
              <%	}
							} %>
            </TD>
            <TD width="15"></TD>
          </TR>
          <TR>
            <TD nowrap width="129">Adresse de paiement</TD>
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
					_fullAdr[0] = "ERREUR lors de la récupération de l'adresse de paiement !";
			  	}
			  %>
              <textarea cols="25" rows="4" tabindex="-1" <%if (_fullAdr.length == 1 && _fullAdr[0].startsWith("ERREUR")) { %> style="background-color: red;color: white;" <% } %> readonly><%for (int i = 0; i < _fullAdr.length; i++){%><%=_fullAdr[i] +"\r\n"%><%}%></textarea>
            </TD>
            <TD width="5">&nbsp;</TD>
            <TD width="20" valign="bottom">
			&nbsp;
            </TD>
            <TD width="7">&nbsp;</TD>
            <TD width="100">Motif du versement</TD>
            <TD colspan="5">
              <TEXTAREA name="motif" cols="35" tabindex="12" rows="4"><%=viewBean.getMotif()%></TEXTAREA>
            </TD>
            <TD width="15"></TD>
          </TR>
          <TR>
            <TD nowrap width="129">Nature du versement</TD>
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
            <TD width="100">R&eacute;f&eacute;rence BVR</TD>
            <TD colspan="5">
              <input type="text" name="referenceBVR" size="35" maxlength="27" value="<%=viewBean.getReferenceBVR()%>" class="inputDisabled" tabindex=-1 readonly >
            </TD>
            <TD width="15"></TD>
          </TR>
          <TR>
            <TD nowrap width="129">Bloquer le versement</TD>
            <TD width="5"></TD>
            <TD width="147">
              <INPUT type="checkbox" name="estBloque" <%=(viewBean.getEstBloque().booleanValue())? "checked" : "unchecked"%> tabindex="5">
            </TD>
            <TD width="5"></TD>
            <TD width="20">&nbsp;</TD>
            <TD width="7">&nbsp;</TD>
            <TD width="100">Retrait effectu&eacute;</TD>
            <TD colspan="5">
              <input type="checkbox" name="estRetire" <%=(viewBean.getEstRetire().booleanValue())? "checked" : "unchecked"%> readonly disabled>
            </TD>
            <TD width="15"></TD>
          </TR>
          <TR>
            <TD nowrap width="129">Organe d'ex&eacute;cution d&eacute;sir&eacute;</TD>
            <TD width="5"></TD>
            <TD colspan="5">
              <%//	viewBean.getOrdreGroupe().getCsOrganeExecution();
				CAOrganeExecution _organeExecution = null;
				CAOrganeExecutionManager _CsOrganeExecution = new CAOrganeExecutionManager();
				_CsOrganeExecution.setSession(objSession);
				_CsOrganeExecution.setForIdTypeTraitementOG(true);
				_CsOrganeExecution .find();
				for (int i=0; i < _CsOrganeExecution.size(); i++) {
					_organeExecution = (CAOrganeExecution) _CsOrganeExecution.getEntity(i);
					if (_organeExecution .getIdOrganeExecution().equalsIgnoreCase(viewBean.getIdOrganeExecution())) { %>
              <input type="text" name="organeExecution" size="35" maxlength="27" value="<%=_organeExecution.getNom()%>" class="inputDisabled" tabindex=-1 readonly >
              <% }
			} %>
            </TD>
            <TD width="75">&nbsp;</TD>
            <TD width="5"></TD>
            <TD colspan="3">&nbsp;</TD>
            <TD width="15"></TD>
          </TR>
          <TR>
            <TD nowrap width="129">Journal</TD>
            <TD width="5"></TD>
            <TD colspan="6">
              <input type="hidden" name="idJournal" value="<%=viewBean.getIdJournal()%>"  >
              <input type="text" name="journalEcran" size="30" maxlength="30" value="<%=viewBean.getIdJournal()%> - <%=viewBean.getJournal().getLibelle()%>" class="inputDisabled" tabindex=-1 readonly>
            </TD>
            <TD width="5"></TD>
            <TD width="20">&nbsp; </TD>
            <TD width="5"></TD>
            <TD nowrap width="160">&nbsp; </TD>
            <TD width="15"></TD>
          </TR>
          <TR>
            <TD nowrap width="129">Etat</TD>
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
            <TD nowrap width="129">Message</TD>
            <TD width="5"></TD>
            <TD colspan="10">
              <select name="idLog" style="width : 16cm;" class="libelleErrorLongDisabled" tabindex="7">
                <%if (viewBean.getLog() != null) {%>
                <%Enumeration e = viewBean.getLog().getMessagesToEnumeration();
					while (e.hasMoreElements()){
						FWMessage msg = (FWMessage)e.nextElement();%>
            <%if(msg.getIdLog().equals(viewBean.getIdLog())) { %>
              <option selected  value="<%=msg.getIdLog()%>"><%=msg.getMessageText()%></option>
              <% } else {%>
              <option value="<%=msg.getIdLog()%>"><%=msg.getMessageText()%></option>
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
            <TD colspan="10"> Quittancer
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
              <p>Ordre group&eacute; associ&eacute; </p>
            </td>
            <td nowrap width="5">&nbsp;</td>
            <td nowrap colspan="3">
              <input type="text" name="ordreGroupe" size="30" maxlength="30" value="<%if (!globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getIdOrdreGroupe())){%><%=viewBean.getIdOrdreGroupe()%> - <%if (!viewBean.getIdOrdreGroupe().equalsIgnoreCase("0"))%><%=viewBean.getOrdreGroupe().getMotif()%><%}%>" class="inputDisabled" tabindex=-1 readonly >
            </td>
            <td nowrap width="7">&nbsp;</td>
            <td nowrap width="100">N&deg; transaction</td>
            <td nowrap colspan="5">
              <input type="text" name="numTransaction" size="10" maxlength="10" value="<%=viewBean.getNumTransaction()%>" class="numeroDisabled"  tabindex=-1 readonly>
            </td>
            <td width="15"></td>
          </tr>
          <TR>
            <TD width="129">
              <p>Identifiant</p>
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