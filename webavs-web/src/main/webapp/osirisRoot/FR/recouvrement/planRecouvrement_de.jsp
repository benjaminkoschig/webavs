<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ page import="globaz.osiris.db.recouvrement.*,globaz.globall.util.*,globaz.jade.client.util.JadeStringUtil"%>
<%@ page import="globaz.osiris.translation.*" %>
<%@ page import="globaz.jade.client.util.*" %>
<%
idEcran="GCA60007";
	CAPlanRecouvrementViewBean viewBean = (globaz.osiris.db.recouvrement.CAPlanRecouvrementViewBean)session.getAttribute("viewBean");
	selectedIdValue = viewBean.getIdPlanRecouvrement();
	String collaborateur = viewBean.getCollaborateur();
	if (JadeStringUtil.isEmpty(collaborateur)) {
		collaborateur = viewBean.getSession().getUserId();
	}
	String compteAnnexeIdExterneRole = "";
	try {
		compteAnnexeIdExterneRole = viewBean.getCompteAnnexe().getTitulaireEnteteForCompteAnnexeParSection();
	} catch (Exception e) {
	}
%>
<%@ taglib uri="/WEB-INF/osiris.tld" prefix="os"%>
	<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="options" menuId="CA-PlanRecouvrement" showTab="options">
	<ct:menuSetAllParams key="id" value="<%=viewBean.getIdPlanRecouvrement()%>"/>
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdPlanRecouvrement()%>"/>
</ct:menuChange>

<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

	var initialEcheance='';

	function add() {
	  	document.forms[0].elements('userAction').value="osiris.recouvrement.planRecouvrement.ajouter";
	}

	function upd() {
	  	document.forms[0].elements('userAction').value="osiris.recouvrement.planRecouvrement.modifier";
	}

	function validate() {
	    state = validateFields();
	    if (document.forms[0].elements('_method').value == "add")
	        document.forms[0].elements('userAction').value="osiris.recouvrement.planRecouvrement.ajouter";
	    else
	        document.forms[0].elements('userAction').value="osiris.recouvrement.planRecouvrement.modifier";
	    return state;
	}

	function cancel() {
		if (document.forms[0].elements('_method').value == "add")
			document.forms[0].elements('userAction').value="back";
		else
			document.forms[0].elements('userAction').value="osiris.recouvrement.planRecouvrement.afficher";
	}

	function del() {
		if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")) {
	        document.forms[0].elements('userAction').value="osiris.recouvrement.planRecouvrement.supprimer";
	        document.forms[0].submit();
	    }
	}

	function init(){
	}

// stop hiding -->
</SCRIPT>
	<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Détail d'un sursis au paiement<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
	<tr>
		<td class="label">
			<a href="osiris?userAction=osiris.comptes.apercuComptes.afficher&selectedId=<%=viewBean.getCompteAnnexe().getIdCompteAnnexe()%>" class="external_link">Compte</a>
		</td>
		<td class="control">
			<textarea cols="40" rows="4" class="libelleLongDisabled" readonly tabIndex=-1><%=compteAnnexeIdExterneRole%></textarea>
			<%
				Object[] methods = new Object[]{new String[]{"setIdCompteAnnexe", "getIdCompteAnnexe"}};
				Object[] params = new Object[]{new String[]{"idCompteAnnexe", "idCompteAnnexe"}};
			%>
		</td>
		<td class="label" rowspan="1">
			<ct:ifhasright element="pyxis.tiers.tiers.afficher" crud="r">
				<a href="pyxis?userAction=pyxis.tiers.tiers.afficher&idTiers=<%=viewBean.getCompteAnnexe().getIdTiers()%>" class="external_link">
			</ct:ifhasright>
					Adresse de paiement
			<ct:ifhasright element="pyxis.tiers.tiers.afficher" crud="r">
				</a>
			</ct:ifhasright>
		</td>
		<td class="control" rowspan="1">
			<textarea rows="4" cols="30" class="libelleLongDisabled" readonly tabIndex=-1><%=viewBean.getAdressePaiementTiers()%></textarea>
		</td>
	</tr>
		<% if (viewBean.getCompteAnnexe() != null && !JadeStringUtil.isDecimalEmpty(viewBean.getCompteAnnexe().getInformation())) { %>
	<tr>
       	<td rowspan="2">Information</td>
       	<td rowspan="2">
        	<input style="color:#FF0000" type="text" name="" value="<%=CACodeSystem.getLibelle(session, viewBean.getCompteAnnexe().getInformation())%>" class="libelleLongDisabled" tabindex="-1" readonly>
       	</td>
	</tr>
       <% } else { %>
 	<tr>
       	<td nowrap></td>
       	<td nowrap></td>
       	<td nowrap></td>
       	<td nowrap></td>
	</tr>
       <% } %>
	<tr><td colspan=4><hr /></td></tr>
	<tr>
		<td class="label">Numéro du plan</td>
		<td class="control"><input type="text" name="" value="<%=viewBean.getIdPlanRecouvrement()%>" class="disabled" readonly tabIndex=-1></td>
        <td class="label">Etat</td>
			<%if (viewBean.getIdEtat().equals(viewBean.CS_ANNULE)) {
				bButtonUpdate = false;
			  }%>
			<%if(viewBean.isNew()){%>
				<td class="control">
					<input type="text" class="disabled" readonly value="<%=viewBean.getSession().getCodeLibelle(viewBean.CS_INACTIF)%>" tabIndex=-1>
					<input type="hidden" value="<%=viewBean.CS_INACTIF%>" name="idEtat"/>
				</td>
			<%} else if (!viewBean.getIdEtat().equals(viewBean.CS_ACTIF)) {%>
				<td class="control">
					<input type="text" class="disabled" readonly value="<%=viewBean.getSession().getCodeLibelle(viewBean.getIdEtat())%>" tabIndex=-1>
					<input type="hidden" value="<%=viewBean.getIdEtat()%>" name="idEtat"/>
				</td>
			<%} else {
	        	// HashSet pour définir les codes systèmes qui ne doivent pas venir dans la liste
	            java.util.HashSet set = new java.util.HashSet();
					//set.add("226001"); // Actif
				    set.add("226002"); // Inactif
					//set.add("226003"); // Soldé
					set.add("226004"); // Annule
			%>
				<td class="control"><ct:FWCodeSelectTag name="idEtat" codeType="OSIPLRETA" defaut="<%=viewBean.getIdEtat()%>" except="<%=set%>" /></td>
			<%}%>
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
			exceptMode.add(globaz.osiris.db.access.recouvrement.CAPlanRecouvrement.CS_AVANCE_PTRA);
	    %>
		<td class="control">
			<ct:FWCodeSelectTag codeType="OSIPLRMOD" defaut="<%=viewBean.getIdModeRecouvrement()%>" name="idModeRecouvrement" except="<%=exceptMode%>" />
		</td>
	</tr>
	<tr>
		<td class="label">Part pénale</td>
		<td nowrap>
              <input type="checkbox" name="partPenale" <%=(viewBean.getPartPenale().booleanValue())? "checked" : "unchecked"%> >
        </td>
		<TD class="label">Mode de ventilation</TD>
		<%
		// HashSet pour définir les codes systèmes qui ne doivent pas venir dans la liste
	    	java.util.HashSet exceptVen = new java.util.HashSet();
	    	exceptVen.add(globaz.osiris.db.access.recouvrement.CAPlanRecouvrement.CS_VEN_VPRO);
	    %>
		<TD class="control"><ct:FWCodeSelectTag codeType="OSIPLRVEN" defaut="<%=viewBean.getIdModeVentilation()%>" name="idModeVentilation" except="<%=exceptVen%>" /></TD>
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