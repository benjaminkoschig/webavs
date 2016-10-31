<%@page import="java.util.LinkedHashMap"%>
<%@page import="ch.globaz.orion.business.domaine.swissdec.EtatSwissDec"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="ch.globaz.orion.business.models.pucs.PucsSearchCriteria"%>
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
	idEcran = "GEB0001";
	bButtonNew = false;
	List<JadeCodeSysteme> codes = JadeBusinessServiceLocator.getCodeSystemeService().getFamilleCodeSysteme("EB_PUCS");
	codes.addAll(JadeBusinessServiceLocator.getCodeSystemeService().getFamilleCodeSysteme(EtatSwissDec.CODE_FAMILLE));
	Langues langue = Langues.getLangueDepuisCodeIso(objSession.getIdLangueISO());
	Map<String, String> map = new LinkedHashMap<String, String>();
	String codesEnCours = PucsSearchCriteria.CS_TO_HANDLE+","+PucsSearchCriteria.CS_HANDLING+","+EtatSwissDec.A_VALIDE.getValue();
	map.put(codesEnCours, objSession.getLabel("PUCS_STATUT_EN_CROURS"));
	for(JadeCodeSysteme code: codes){
	    map.put(code.getIdCodeSysteme(), code.getTraduction(langue));
	}
	
	//CodeSystemeResolver codeSystemeResolver = new CodeSystemeResolver();
%>
<%-- /tpl:insert --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>
<script>
bFind = true;
usrAction = "orion.pucs.pucsFile.lister";
$(document).ready(function(){

	$('#selectionner_all_handling').click(function(){
		if ($('#selectionner_all_handling:checked').val() != null) {
			$("input[name*='idPucsEntryHandling']", window.frames[0].document).attr('checked', true);
		}else{	
			$("input[name*='idPucsEntryHandling']", window.frames[0].document).attr('checked', false);
		}	
	});
	$('#selectionner_to_handle').click(function(){
		if ($('#selectionner_to_handle:checked').val() != null) {
			$("input[name*='idPucsEntryToHandle']", window.frames[0].document).attr('checked', true);
		} else{
			$("input[name*='idPucsEntryToHandle']", window.frames[0].document).attr('checked', false);
		}
	});
	
	$('#simulation').click(function(){
		removeInputs();
		$("input[name*='userAction']").attr('value', 'orion.pucs.pucsImport.afficher');
		$("<input type='hidden' name='mode' value='simulation'>").appendTo('form');
		initializeForm();
		$(document.forms[0]).attr('target','');
		document.forms[0].submit();
	});
	$('#maj').click(function(){
		removeInputs();
		$("input[name*='userAction']").attr('value', 'orion.pucs.pucsImport.afficher');
		$("<input type='hidden' name='mode' value='miseajour'>").appendTo('form');
		initializeForm();
		$(document.forms[0]).attr('target','');
		document.forms[0].submit();
	});
});

function initializeForm(){
	$('input[type=checkbox][checked]', window.frames[0].document).each(
		    function() { 
		    	$("<input type='hidden' name='idPucsEntry' value='"+$(this).attr('value')+"'>").appendTo('form');
		    } 
	);
}
function removeInputs(){
	$("input[name*='idPucsEntry']").each(function(){
		$(this).remove();
	});
}
</script>

<ct:menuChange displayId="menu" menuId="EBMenuPrincipal" showTab="menu">
</ct:menuChange>
	
<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
<%-- tpl:insert attribute="zoneTitle" --%><ct:FWLabel key="TITRE_FICHIER_PUCS"/><%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
<%-- tpl:insert attribute="zoneMain" --%>

<tr>
	<td>
		<input type="checkbox" id="selectionner_all_handling"/><ct:FWLabel key="FICHIERS_TRAITEMENT"/>
	</td>
	<td>
		&nbsp;
	</td>
	<td> 
		&nbsp;<ct:FWLabel key="PUCS_STATU"/>
	</td>
	<td>
	<select id='statut' name='statut'>
		<OPTION value=''></OPTION>
		<%for(Entry<String, String> entry : map.entrySet()) {%>
			<% if(codesEnCours.equals(entry.getKey())) {%>
				<OPTION selected="selected" value='<%=entry.getKey()%>'><%=entry.getValue() %></OPTION>
			<%} else {%>
				<OPTION value='<%=entry.getKey()%>'><%=entry.getValue() %></OPTION>
			<%} %>
		<%}%>
	</select>
	</td>
	<td>
		<ct:FWLabel key="MENU_NO_AFFILIE"/>
	</td>
	<td> 
		<input type="text" name="likeAffilie">
	</td>
	<td>
		&nbsp;<ct:FWLabel key="PUCS_TYPE"/>
	</td>
	<td>
		<select name="type">
			<option value = ""></option>
			<option value = "1"><ct:FWLabel key="PUCS_TYPE_PUCS"/></option>
			<option value = "2"><ct:FWLabel key="PUCS_TYPE_DAN"/></option>
		    <option value = "4"><ct:FWLabel key="PUCS_TYPE_SWISS_DEC"/></option>
		</select>
	</td>
	<td> 
		&nbsp;<ct:FWLabel key="PUCS_TRIE_PAR"/>
	</td>
	<td>
		<select name="orderBy">
			<option value = ""></option>
			<option value = "NOM_AFFILIE"><ct:FWLabel key="PUCS_ORDERBY_NOM_AFFILIE"/></option>
		    <option value = "NUMERO_AFFILIE"><ct:FWLabel key="PUCS_ORDERBY_NUMERO_AFFILIE"/></option>
		</select>
	</td>
</tr>
<tr>
	<td>
		<input type="checkbox" id="selectionner_to_handle"/>
		<ct:FWLabel key="FICHIERS_A_TRAITER"/>
	</td>
	<td>
		&nbsp;
	</td>
	<td>
		<ct:FWLabel key="MENU_DATE_RECEPTION"/>
	</td>
	<td>
		<ct:FWCalendarTag name="dateSoumission" value = ""/>
	</td>
	<td>
		<ct:FWLabel key="PUCS_RECHERCHE"/>
	</td>
	<td>
		<input type="text" name="fullText"/>
	</td>
</tr>

<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
<%-- tpl:insert attribute="zoneButtons" --%>
<ct:ifhasright element="orion.pucs.pucsImport.afficher" crud="u">
<input type="button" id="simulation" value="<ct:FWLabel key="PUCS_SIMULATION"/>"/>&nbsp;
</ct:ifhasright>
<ct:ifhasright element="orion.pucs.pucsImport.afficher" crud="u">
<input type="button" id="maj" value="<ct:FWLabel key="PUCS_MISE_A_JOUR"/>"/>
</ct:ifhasright>

<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:insert attribute="zoneVieuxBoutons" --%><%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyClose.jspf" %>