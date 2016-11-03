<%@page import="globaz.orion.process.importpucs.EBImportSwissDec"%>
<%@page import="globaz.orion.process.importpucs.EBImportPucsDan"%>
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
	idEcran = "GEB0001";
	bButtonNew = false;
	List<JadeCodeSysteme> codes = JadeBusinessServiceLocator.getCodeSystemeService().getFamilleCodeSysteme(EtatPucsFile.CODE_FAMILLE);
	Langues langue = Langues.getLangueDepuisCodeIso(objSession.getIdLangueISO());
	Map<String, String> map = new LinkedHashMap<String, String>();
	String codesEnCours = EtatPucsFile.A_TRAITER.getValue()+","+EtatPucsFile.EN_TRAITEMENT.getValue()+","+EtatPucsFile.A_VALIDE.getValue();
	map.put(codesEnCours, objSession.getLabel("PUCS_STATUT_EN_COURS"));
	for(JadeCodeSysteme code: codes){
	    map.put(code.getIdCodeSysteme(), code.getTraduction(langue));
	}
	


%>
<%-- /tpl:insert --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>
<script>
bFind = true;
messageNoSwissDecSelected = '<%=BSessionUtil.getSessionFromThreadContext().getLabel("JSP_NO_SWISSDEC_SELECTED") %>';
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
		var $entries = $("input[name*='idPucsEntryToHandle']", window.frames[0].document); 
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
	
	$("#importFileInDb").click(function(){
		alert(3)
		$("input[name*='userAction']").attr('value', 'orion.pucs.pucsFile.importInDb');
		$(document.forms[0]).attr('target','');
		document.forms[0].submit();
	});
	
	$('#simulation').click(function(){
		var ids = getSelectedIds('atraiter');
		$("input[name*='userAction']").attr('value', 'orion.pucs.pucsImport.afficher');
		$("<input type='hidden' name='selectedIds' value='"+ids.join(',')+"'>").appendTo('form');
		$("<input type='hidden' name='mode' value='simulation'>").appendTo('form');
		$(document.forms[0]).attr('target','');
		document.forms[0].submit();
	});
	$('#maj').click(function(){
		var ids = getSelectedIds();
		$("input[name*='userAction']").attr('value', 'orion.pucs.pucsImport.afficher');
		$("<input type='hidden' name='mode' value='miseajour'>").appendTo('form');
		$("<input type='hidden' name='selectedIds' value='"+ids.join(',')+"'>").appendTo('form');
		$(document.forms[0]).attr('target','');
		document.forms[0].submit();
	});
	$('#ctrlSwissDec').click(function() {
		var ids = getSelectedIds('swissdec');
		if(ids.length>0) {
			$("input[name*='userAction']").attr('value', 'orion.swissdec.pucsValidationDetail.afficher');
			$("<input type='hidden' name='selectedIds' value='"+ids.join(',')+"'>").appendTo('form');
			$(document.forms[0]).attr('target','');
			document.forms[0].submit();
		} else {
			alert(messageNoSwissDecSelected);
		}
	});
});

function getSelectedIds(type) {
	var ids = [];
	var checkboxes = $("input[name*='idPucsEntryToHandle']:checked", window.frames[0].document);
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
<input type="button" id="ctrlSwissDec" value="<ct:FWLabel key="PUCS_CONTROLE_SWISSDEC"/>"/>&nbsp;
<input type="button" id="importFileInDb" value="<ct:FWLabel key="PUCS_IMPORT_FILE"/>"/>&nbsp;

</ct:ifhasright>
<ct:ifhasright element="orion.pucs.pucsImport.afficher" crud="u">
<input type="button" id="maj" value="<ct:FWLabel key="PUCS_MISE_A_JOUR"/>"/>
</ct:ifhasright>

<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:insert attribute="zoneVieuxBoutons" --%><%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyClose.jspf" %>