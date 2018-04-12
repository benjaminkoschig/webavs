<%@page import="globaz.phenix.db.principale.CPDecision"%>
<%@page import="ch.globaz.orion.db.EBDemandeModifAcompteManagerOrderBy"%>
<%@page import="ch.globaz.orion.business.domaine.demandeacompte.DemandeModifAcompteType"%>
<%@page import="ch.globaz.orion.business.domaine.demandeacompte.DemandeModifAcompteStatut"%>
<%@page import="globaz.orion.process.importpucs.EBImportSwissDecProcess"%>
<%@page import="globaz.orion.process.importpucs.EBImportPucsDanProcess"%>
<%@page import="ch.globaz.common.process.byitem.ProcessItemsService"%>
<%@page import="ch.globaz.orion.business.domaine.pucs.EtatPucsFile"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="globaz.globall.db.BSessionUtil"%>
<%@page import="ch.globaz.jade.business.models.Langues"%>
<%@page import="ch.globaz.jade.business.models.codesysteme.JadeCodeSysteme"%>
<%@page import="ch.globaz.jade.JadeBusinessServiceLocator"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery-ui.js"></script>
<%
	idEcran = "GEB0007";
	bButtonNew = false;
%>
<%-- /tpl:insert --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>
<script>
bFind = true;
usrAction = "orion.adi.demandesTransmises.lister";
$(document).ready(function(){
	$('#selectionner_to_handle').click(function(){
		var $entries = $("input[name*='demandeToHandle']", window.frames[0].document);		
		if ($('#selectionner_to_handle:checked').val() != null) {
			$entries.each(function() {
				var $this = $(this);
				if($this.hasClass('atraiter')) {
					$this.attr('checked', true);
				}
			})
		} else{
			$entries.attr('checked', false);
		}
	});
	
	$('#valider_button').click(function(){
		var ids = getSelectedIds('atraiter');
		
		if (ids.length > 0) {
			$("input[name*='userAction']").attr('value', 'orion.adi.demandesTransmises.validerBatch');
			$("<input type='hidden' name='selectedIds' value='"+ids.join(',')+"'>").appendTo('form');
			$("<input type='hidden' name='mode' value='validerBatch'>").appendTo('form');
			$(document.forms[0]).attr('target','');
			document.forms[0].submit();
		}
	});
	
	$('#refuser_button').click(function(){
		var ids = getSelectedIds('atraiter');
		$("input[name*='userAction']").attr('value', 'orion.adi.demandesTransmises.refuserBatch');
		$("<input type='hidden' name='selectedIds' value='"+ids.join(',')+"'>").appendTo('form');
		$("<input type='hidden' name='mode' value='refuserBatch'>").appendTo('form');
		$(document.forms[0]).attr('target','');
// 		document.forms[0].submit();
	});
	
});

function getSelectedIds(type) {
	var ids = [];
	var checkboxes = $("input[name*='demandeToHandle']:checked", window.frames[0].document);
	checkboxes.each(function() {
		var $this = $(this);
		if(!type || (type.length>0 && $this.hasClass(type))) {
			ids.push($(this).val());
		}
	});
	return ids;
}
</script>

<ct:menuChange displayId="menu" menuId="EBMenuPrincipal" showTab="menu">
</ct:menuChange>
	
<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
<%-- tpl:insert attribute="zoneTitle" --%><ct:FWLabel key="JSP_TITRE_DEMANDES_ISSUES_PORTAIL"/><%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
<%-- tpl:insert attribute="zoneMain" --%>

<tr>
	<td> 
		&nbsp;<ct:FWLabel key="JSP_RC_STATUT"/>
	</td>
	<td>
		<select id='forStatut' name='forStatut'>
			<OPTION value=''></OPTION>
			<%for(DemandeModifAcompteStatut statut : DemandeModifAcompteStatut.values()) {
			    if (!statut.getLabel().isEmpty()) {
					if(statut.equals(DemandeModifAcompteStatut.A_TRAITER)){ %>
						<OPTION selected="selected" value='<%=statut.getValue()%>'><%=objSession.getLabel(statut.getLabel()) %></OPTION>
					<%}else{ %>
						<OPTION value='<%=statut.getValue()%>'><%=objSession.getLabel(statut.getLabel()) %></OPTION>
					<%} %>
				<%}
			}%>
		</select>
	</td>
	<td width="30px">
		&nbsp;
	</td>
	<td>
		<ct:FWLabel key="JSP_RC_NO_AFFILIE"/>&nbsp;
	</td>
	<td> 
		<input type="text" name="likeAffilie">
	</td>
	<td width="30px">
		&nbsp;
	</td>
	<td>
		<ct:FWLabel key="JSP_RC_ANNEE"/>&nbsp;
	</td>
	<td>
		<input type="text" name="forAnnee">
	</td>
	<td>
		&nbsp;
	</td>
	<td>
		<ct:FWLabel key="JSP_RC_TYPE"/>&nbsp;
	</td>
	<td>
		<select id='forType' name='forType'>
			<OPTION value=''></OPTION>
			<OPTION value='<%=CPDecision.CS_ACOMPTE%>'><%=objSession.getCodeLibelle(CPDecision.CS_ACOMPTE) %></OPTION>
			<OPTION value='<%=CPDecision.CS_CORRECTION%>'><%=objSession.getCodeLibelle(CPDecision.CS_CORRECTION) %></OPTION>
			<OPTION value='<%=CPDecision.CS_DEFINITIVE%>'><%=objSession.getCodeLibelle(CPDecision.CS_DEFINITIVE) %></OPTION>
			<OPTION value='<%=CPDecision.CS_PROVISOIRE%>'><%=objSession.getCodeLibelle(CPDecision.CS_PROVISOIRE) %></OPTION>
	</select>
	</td>	
</tr>
<tr>
	<td>
		<ct:FWLabel key="JSP_RC_RECU_A_PARTIR_DE"/>&nbsp;
	</td>
	<td>
		<ct:FWCalendarTag name="fromDateReception" value = ""/>
	</td>
	<td>
		&nbsp;
	</td>
	<td>
		<ct:FWLabel key="JSP_RC_NOM"/>
	</td>
	<td>
		<input type="text" name="likeNom">
	</td>
	<td>
		&nbsp;
	</td>
	<td>
		&nbsp;
	</td>
	<td>
		&nbsp;
	</td>
	<td>
		&nbsp;
	</td>
	<td> 
		&nbsp;<ct:FWLabel key="JSP_RC_TRIE_PAR"/>&nbsp;
	</td>
	<td>
		<select id="orderBy" name="orderBy">
			<%for(EBDemandeModifAcompteManagerOrderBy order : EBDemandeModifAcompteManagerOrderBy.values()) {%>
				<OPTION value='<%=order.getLabel()%>'><%=objSession.getLabel(order.getLabel()) %></OPTION>
			<%}%>
		</select>
	</td>
</tr>
<tr>
	<td colspan="11">
		Sélectionner tout <input type="checkbox" id="selectionner_to_handle" name="selectionner_to_handle"/>
	</td>
</tr>

<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
<%-- tpl:insert attribute="zoneButtons" --%>
<ct:ifhasright element="orion.adi.demandesTransmises.afficher" crud="u">
<input type="button" id="valider_button" value="<ct:FWLabel key="JSP_RC_BTN_VALIDER"/>"/>
<%-- <input type="button" id="refuser_button" value="<ct:FWLabel key="JSP_RC_BTN_REFUSER"/>"/> --%>

</ct:ifhasright>

<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:insert attribute="zoneVieuxBoutons" --%><%-- /tpl:insert --%>
<%-- <div style="color:black"><span id="listCount">0</span> <ct:FWLabel key="JSP_RC_NB_DEMANDES_TROUVEES"/></div> --%>
<%@ include file="/theme/find/bodyClose.jspf" %>