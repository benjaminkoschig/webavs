<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ page import="globaz.osiris.db.recouvrement.*,globaz.globall.util.*,globaz.jade.client.util.JadeStringUtil"%>
<%@ page import="globaz.osiris.translation.*" %>
<%@ page import="globaz.jade.client.util.*" %>
<%
	idEcran = "GCA60015";
	CASursisViewBean viewBean = (globaz.osiris.db.recouvrement.CASursisViewBean) session.getAttribute("viewBean");
	//selectedIdValue = viewBean.getIdPlanRecouvrement();
	String collaborateur = viewBean.getCollaborateur();
	if (JadeStringUtil.isEmpty(collaborateur)) {
		collaborateur = viewBean.getSession().getUserId();
	}
	String compteAnnexeDescription = "";
	viewBean.setIdModeVentilation(request.getParameter("idModeVentilation"));
	viewBean.setIdCompteAnnexe(request.getParameter("idCompteAnnexe"));
	viewBean.setSelectedIds(request.getParameter("selectedIds"));

	if (request.getAttribute("soldeSections") == null && request.getParameter("plafond") != null) {
		viewBean.setPlafond((String) request.getParameter("plafond"));
	} else if (request.getAttribute("soldeSections") != null) {
		viewBean.setPlafond((String) request.getAttribute("soldeSections"));
	}
	try {
		compteAnnexeDescription = viewBean.getCompteAnnexe().getTitulaireEnteteForCompteAnnexeParSection();
	} catch (Exception e) {
	}
%>
<%@ taglib uri="/WEB-INF/osiris.tld" prefix="os"%>
	<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

	var initialEcheance='';

	function add() {
	  	document.forms[0].elements('userAction').value="osiris.recouvrement.sursis.ajouter";
	}

	function validate() {
	    state = validateFields();
	    if (document.forms[0].elements('_method').value == "add")
	        document.forms[0].elements('userAction').value="osiris.recouvrement.sursis.ajouter";
	    else
	        document.forms[0].elements('userAction').value="osiris.recouvrement.planRecouvrement.modifier";
	    return state;
	}

	function cancel() {
		if (document.forms[0].elements('_method').value == "add")
			document.forms[0].elements('userAction').value="back";
		else
			document.forms[0].elements('userAction').value="osiris.recouvrement.sursis.afficher";
	}

	function init(){
		// Desactivation du mode de recouvrement
		document.getElementById('idModeRecouvrement').disabled=true;
		//document.getElementById('idModeRecouvrement').options[1].disable=true
		jscss("add", document.getElementById("idModeRecouvrement"), "disabled");
		document.getElementById('idModeRecouvrement').tabIndex=-1;
	}


// stop hiding -->
</SCRIPT>
	<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Création d'un sursis - Phase 3 - Détail d'un sursis au paiement<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
	<%-- tpl:put name="zoneMain" --%>

	<tr>
		<td class="label">
			<a href="osiris?userAction=osiris.comptes.apercuComptes.afficher&selectedId=<%=viewBean.getCompteAnnexe().getIdCompteAnnexe()%>" class="external_link">Compte</a>
		</td>
		<td class="control">
			<textarea cols="40" rows="4" class="libelleLongDisabled" readonly tabIndex=-1><%=compteAnnexeDescription%></textarea>
			<%
				Object[] methods = new Object[] { new String[] { "setIdCompteAnnexe", "getIdCompteAnnexe" } };
				Object[] params = new Object[] { new String[] { "idCompteAnnexe", "idCompteAnnexe" } };
			%>
		</td>
		<td class="label" rowspan="1">
		<a href="pyxis?userAction=pyxis.tiers.tiers.afficher&idTiers=<%=viewBean.getCompteAnnexe().getIdTiers()%>" class="external_link">Adresse de paiement</a>
		</td>
		<td class="control" rowspan="1">
			<textarea rows="4" cols="30" class="libelleLongDisabled" readonly tabIndex=-1><%=viewBean.getAdressePaiementTiers()%></textarea>
		</td>
	</tr>
		<%
			if (viewBean.getCompteAnnexe() != null && !JadeStringUtil.isDecimalEmpty(viewBean.getCompteAnnexe().getInformation())) {
		%>
	<tr>
       	<td rowspan="2">Information</td>
       	<td rowspan="2">
        	<input style="color:#FF0000" type="text" name="" value="<%=CACodeSystem.getLibelle(session, viewBean.getCompteAnnexe().getInformation())%>" class="libelleLongDisabled" tabindex="-1" readonly>
       	</td>
	</tr>
       <%
       	} else {
       %>
 	<tr>
       	<td nowrap></td>
       	<td nowrap></td>
       	<td nowrap></td>
       	<td nowrap></td>
	</tr>
       <%
       	}
       %>
	<tr><td colspan=4><hr /></td></tr>
	<tr>
		<td class="label">Numéro du plan</td>
		<td class="control"><input type="text" name="idPlanRecouvrement" value="" class="disabled" readonly tabIndex=-1></td>
        <td class="label">Etat</td>
			<%
				if (viewBean.getIdEtat().equals(viewBean.CS_ANNULE)) {
					bButtonUpdate = false;
				}
			%>
			<%
				if (viewBean.isNew()) {
			%>
				<td class="control">
					<input type="text" class="disabled" readonly value="<%=viewBean.getSession().getCodeLibelle(viewBean.CS_INACTIF)%>" tabIndex=-1>
					<input type="hidden" value="<%=viewBean.CS_INACTIF%>" name="idEtat"/>
				</td>
			<%
				} else if (!viewBean.getIdEtat().equals(viewBean.CS_ACTIF)) {
			%>
				<td class="control">
					<input type="text" class="disabled" readonly value="<%=viewBean.getSession().getCodeLibelle(viewBean.getIdEtat())%>" tabIndex=-1>
					<input type="hidden" value="<%=viewBean.getIdEtat()%>" name="idEtat"/>
				</td>
			<%
				} else {
					// HashSet pour définir les codes systèmes qui ne doivent pas venir dans la liste
					java.util.HashSet set = new java.util.HashSet();
					//set.add("226001"); // Actif
					set.add("226002"); // Inactif
					//set.add("226003"); // Soldé
					set.add("226004"); // Annule
			%>
				<td class="control"><ct:FWCodeSelectTag name="idEtat" codeType="OSIPLRETA" defaut="<%=viewBean.getIdEtat()%>" except="<%=set%>" /></td>
			<%
				}
			%>
	</tr>
	<tr>
		<td class="label">Libellé</td>
		<td class="control"><input type="text" name="libelle" value="<%=viewBean.getLibelle()%>" class="libelleLong"></td>
		<td class="label">Collaborateur</td>
		<td class="control"><input type="text" name="collaborateur" value="<%=collaborateur%>" class="disabled" readonly tabIndex=-1></td>
	</tr>
	<tr>
   		<td class="label">Date d'octroi</td>
		<td class="control"><ct:FWCalendarTag name="date" doClientValidation="CALENDAR" value="<%=viewBean.getDate()%>"/></td>
		<td class="label">Mode de recouvrement</td>
		<%
			// HashSet pour définir les codes systèmes qui ne doivent pas venir dans la liste
			java.util.HashSet exceptMode = new java.util.HashSet();
			exceptMode.add(globaz.osiris.db.access.recouvrement.CAPlanRecouvrement.CS_DIRECT);
			exceptMode.add(globaz.osiris.db.access.recouvrement.CAPlanRecouvrement.CS_VERSEMENT);
			exceptMode.add(globaz.osiris.db.access.recouvrement.CAPlanRecouvrement.CS_COMPENSATION);
			exceptMode.add(globaz.osiris.db.access.recouvrement.CAPlanRecouvrement.CS_RECOUVREMENT);
			exceptMode.add(globaz.osiris.db.access.recouvrement.CAPlanRecouvrement.CS_AVANCE_APG);
			exceptMode.add(globaz.osiris.db.access.recouvrement.CAPlanRecouvrement.CS_AVANCE_RENTES);
			exceptMode.add(globaz.osiris.db.access.recouvrement.CAPlanRecouvrement.CS_AVANCE_IJAI);
		%>
		<td class="control">
			<ct:FWCodeSelectTag codeType="OSIPLRMOD" defaut="<%=viewBean.getIdModeRecouvrement()%>" name="idModeRecouvrement" except="<%=exceptMode%>" />
		</td>
	</tr>
	<tr>
		<td class="label">Part pénale</td>
		<td nowrap>
              <input type="checkbox" name="partPenale" <%=(viewBean.getPartPenale().booleanValue()) ? "checked" : "unchecked"%> >
        </td>
		<td></td>
		<td>
			<input type="hidden" name="selectedIds" value="<%=viewBean.getSelectedIds()%>">
			<input type="hidden" name="idCompteAnnexe" value="<%=viewBean.getIdCompteAnnexe()%>">
			<input type="hidden" name="idModeVentilation" value="<%=viewBean.getIdModeVentilation()%>">
		</td>
	</tr>
	<tr>
		<td class="label">Montant maximum à recouvrir</td>
		<td class="control"><input type="text" name="plafond" value="<%=viewBean.getPlafondFormate()%>" class="montant"></td>
		<td class="label">Solde résiduel</td>
		<td class="control"><input type="text" value="<%=viewBean.getSoldeResiduelPlanFormate()%>" class="montantDisabled" readonly tabIndex=-1></td>
	</tr>
	<tr><td colspan=4><hr /></td></tr>
		<tr>
		<td class="label">Echéance</td>
		<%
			// HashSet pour définir les codes systèmes qui ne doivent pas venir dans la liste
			java.util.HashSet except = new java.util.HashSet();
			except.add(globaz.osiris.db.access.recouvrement.CAPlanRecouvrement.CS_ECHEANCE_FACTURATION_PERIODIQUE);
			except.add(globaz.osiris.db.access.recouvrement.CAPlanRecouvrement.CS_ECHEANCE_FACTURATION_EXTERNE);
			except.add(globaz.osiris.db.access.recouvrement.CAPlanRecouvrement.CS_ECHEANCE_FACTURATION_INTERNE);
		%>
		<td class="control"><ct:FWCodeSelectTag codeType="OSIPLRECH" defaut="<%=viewBean.getIdTypeEcheance()%>" name="idTypeEcheance" except="<%=except%>"/></td>
		<td class="label">1ère date d'échéance</td>
		<td class="control"><ct:FWCalendarTag name="dateEcheance" doClientValidation="CALENDAR" value="<%=viewBean.getDateEcheance()%>"/></td>
	</tr>
	<tr>
		<td class="label">Acompte</td>
		<td class="control"><input type="text" name="acompte" value="<%=viewBean.getAcompteFormate()%>" class="montant"></td>
		<td class="label">1er Acompte</td>
		<td class="control"><input type="text" name="premierAcompte" value="<%=viewBean.getPremierAcompteFormate()%>" class="montant"></td>
	</tr>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>