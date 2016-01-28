<%@page import="ch.globaz.perseus.business.models.impotsource.SimpleBareme"%>
<%@page import="ch.globaz.perseus.business.models.impotsource.SimpleTrancheSalaire"%>
<%@page import="ch.globaz.perseus.business.models.impotsource.TrancheSalaire"%>
<%@page import="ch.globaz.perseus.business.constantes.IPFConstantes"%>
<%@page import="ch.globaz.perseus.business.models.impotsource.Taux"%>
<%@page import="globaz.perseus.vb.impotsource.PFTauxViewBean"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%-- tpl:insert attribute="zoneInit" --%>
<%
	//Les labels de cette page commence par le préfix "JSP_PF_DECISION_A"
	idEcran = "PPF2131";
	autoShowErrorPopup = true;
	bButtonDelete = true;
	bButtonCancel = true;

	PFTauxViewBean viewBean = (PFTauxViewBean) session.getAttribute("viewBean");
	
	if(objSession.hasRight("perseus", FWSecureConstants.ADD)){
		bButtonUpdate = true;
		bButtonDelete = true;
	}else{
		bButtonUpdate = false;
		bButtonDelete = false;
	}
	
%>
<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/javascripts.jspf" %>

<script type="text/javascript">
	var userAction;
	function add() {}
	function del() {}
	function init() {}
	
	function upd() {
		$("#listeAnnee")[0].disabled = true;
		$("#baremes")[0].disabled = true;
	}
		
	function postInit() {
		$("#listeAnnee")[0].disabled = false;
		$("#baremes")[0].disabled = false;
	}
	
	//Fonction permettant la validation d'une modification ou d'un ajout
	function validate() {
		$("#listeAnnee")[0].disabled = false;
		$("#baremes")[0].disabled = false;
	    state = validateFields();
	    if (document.forms[0].elements('_method').value == "add")
	        document.forms[0].elements('userAction').value="perseus.impotsource.taux.ajouter";
	    else if (document.forms[0].elements('_method').value == "upd")
	        document.forms[0].elements('userAction').value="perseus.impotsource.taux.modifier";
	    
	    return state;
	}
	
	//Fonction permettant d'annuler une opération en cours
	function cancel() {
		if (document.forms[0].elements('_method').value == "upd")
			document.forms[0].elements('userAction').value="back";
	}
	
	//Fonction permettant de setter l'année sélectionnée pour recharger la page
	$(function () {
		userAction=$('[name=userAction]',document.forms[0])[0];
		$('[name=listeAnnee]').change(function () {
			var anneeSelectionne = ($(this).val());
			var baremeSelectionne = ($("#baremes").val());
			document.location.href="perseus?userAction=perseus.impotsource.taux.afficher&annee="+anneeSelectionne+"&bareme="+baremeSelectionne;
		});
	});
	
	//Fonction permettant de setter le bareme sélectionnée pour recharger la page
	$(function () {
		userAction=$('[name=userAction]',document.forms[0])[0];
		$('[name=baremes]').change(function () {
			var baremeSelectionne = ($(this).val());
			var anneeSelectionne = ($("#listeAnnee").val());
			document.location.href="perseus?userAction=perseus.impotsource.taux.afficher&annee="+anneeSelectionne+"&bareme="+baremeSelectionne;
		});
	});
	
</script>


<%-- tpl:insert attribute="zoneScripts" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:insert attribute="zoneTitle" --%><ct:FWLabel key="JSP_PF_TAUX_TITRE"/><%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:insert attribute="zoneMain" --%>
<tr>
	<td>
		<table>
			<tr>
				<td><label><ct:FWLabel key="JSP_PF_TAUX_ANNEE"/></label></td>
				<td>&nbsp;</td>
				<td><ct:FWListSelectTag name="listeAnnee" data="<%=viewBean.getListeAnnee()%>"  defaut="<%=viewBean.getAnnee() %>" /></td>
				<td>&nbsp;</td>
				<td><label><ct:FWLabel key="JSP_PF_TAUX_BAREME"/></label></td>
				<td>&nbsp;</td>
				<td>
				<ct:select name="baremes" id="baremes" notation="data-g-select='mandatory:false'" defaultValue="<%=viewBean.getCsTypebareme()%>" wantBlank="false">
					<ct:optionsCodesSystems csFamille="<%= IPFConstantes.CSGROUP_TYPE_BAREME %>"/>
				</ct:select>
				</td>
			</tr>
			<tr><td>&nbsp;</td><tr>
		</table>
			<tr>
				<td>
					<table border=1>
						<thead>
							<tr>
								<th width="150px"><ct:FWLabel key="JSP_PF_TAUX_TRANCHE_SALAIRE"/></th>
								<%
								for(SimpleBareme bareme:viewBean.getListeBareme()){
								%><th width="25px"><u><%=bareme.getNomCategorie()%></u>:&nbsp;<%=bareme.getNombrePersonne()%><ct:FWLabel key="JSP_PF_NOMBRE_PERSONNE"/></th><%
								}%>	
							</tr>
						</thead>
	
						<tbody>
							<% for (SimpleTrancheSalaire tranche:viewBean.getListeTrancheSalaire()){
								%><tr> 
								<td align="center"><%= tranche.getSalaireBrutInferieur()+" à "+tranche.getSalaireBrutSuperieur() %></td>
							<% 
								for(SimpleBareme bareme:viewBean.getListeBareme()){
									%>
									<td align="center"><input data-g-rate="nbMaxDecimal:2" size="14" name="taux_<%=tranche.getId()%>_<%=bareme.getId()%>" value="<%=viewBean.getTauxValue(tranche.getId(), bareme.getId())%>" /></td>
									<%
								}
							 %></tr><%
							}
							%>
						</tbody>
					</table>
				</td>
			</tr>					
						<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>