<%@page import="ch.globaz.orion.business.domaine.pucs.DeclarationSalaireType"%>
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
<%@ page import="globaz.orion.vb.pucs.EBPucsFileListViewBean" %>
<%@ page import="org.springframework.web.servlet.ModelAndView" %>
<%@ page import="java.util.Objects" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/theme/find/header.jspf" %>
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<link rel="stylesheet" href="/resources/demos/style.css">
<%-- tpl:insert attribute="zoneInit" --%>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery-ui.js"></script>
<%--<script src="https://code.jquery.com/jquery-1.12.4.js"></script>--%>
<%--<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>--%>
<script type="text/javascript" src="<%=request.getContextPath()%>/orionRoot/scripts/pucsFile_rc.js"></script>
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

	EBPucsFileListViewBean viewBean = (EBPucsFileListViewBean) session.getAttribute("viewBean");
	Map usersJson = viewBean.getUsersJson();

	// Récupération d'une viewBean précédente
	Object pucsBean = session.getAttribute ("listViewBean");
	EBPucsFileListViewBean viewBeanFind = null;
	if(pucsBean != null && pucsBean instanceof EBPucsFileListViewBean) {
		viewBeanFind = (EBPucsFileListViewBean)pucsBean;
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

	var listUsers = <%=usersJson.get("usersJson")%>;
	var aUser = "<%=Objects.isNull(viewBeanFind)? "":viewBeanFind.getForUser()%>";

	$.each(listUsers, function( index, value ) {
		if(value == aUser) {
			$('#forUser').append('<option selected>'+value+'</option>');
		} else {
			$('#forUser').append("<option>"+value+"</option>");
		}
	});

	$('#isSelectionnerAllHandling').click(function(){
		var $entries = $("input[name*='idPucsEntryToHandle']", window.frames[0].document);
		if ($('#isSelectionnerAllHandling:checked').val() != null) {
			$entries.each(function() {
				var $this = $(this);
				if($this.hasClass('avalider')) {
					$this.attr('checked', true);
				}
			})
		} else{
			$entries.attr('checked', false);
		}
	});
	$('#isSelectionnerToHandle').click(function(){
		var $entries = $("input[name*='idPucsEntryToHandle']", window.frames[0].document);
		if ($('#isSelectionnerToHandle:checked').val() != null) {
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

<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js">
function postInit() {
	<%
	Boolean isSelectionnerAllHandling = false;
	Boolean isSelectionnerToHandle = false;
	String statut = "11020003,11020004,11020001";
	String recherche = "";
	String likeAffilie = "";
	String forTypeDeclaration = "3";
	String type = "";
	String forUser = "";
	String orderBy = "";
	String forDateDebut = "";
	String forDateFin = "";

	if(viewBeanFind != null) {
		isSelectionnerAllHandling = viewBeanFind.getIsSelectionnerAllHandling();
		isSelectionnerToHandle = viewBeanFind.getIsSelectionnerToHandle();
		statut = viewBeanFind.getForStatut();
		recherche = viewBeanFind.getFullText();
		likeAffilie = viewBeanFind.getLikeAffilie();
		forTypeDeclaration = viewBeanFind.getForTypeDeclaration();
		type = viewBeanFind.getTypeValue();
		forUser = viewBeanFind.getForUser();
		orderBy = viewBeanFind.getOrderBy();
		forDateDebut = viewBeanFind.getForDateDebut();
		forDateFin = viewBeanFind.getForDateFin();
	}

	%>
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
		<input type="checkbox" name="isSelectionnerAllHandling" id="isSelectionnerAllHandling" <%=isSelectionnerAllHandling? "checked" : ""%>/>
		<ct:FWLabel key="FICHIERS_AVALIDER"/>
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
			<% if(Objects.equals(entry.getKey(), statut)) {%>
				<OPTION selected="selected" value='<%=entry.getKey()%>'><%=entry.getValue() %></OPTION>
			<%} else if(codesEnCours.equals(entry.getKey())) {%>
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
		<input type="text" name="likeAffilie" value="<%=Objects.isNull(likeAffilie)? "":likeAffilie%>">
	</td>
	<td>
		&nbsp;<ct:FWLabel key="PUCS_TYPE"/>
	</td>
	<td>
		<select name="type">
			<option value = ""></option>
			<option value = "1" <%=Objects.equals(type,"1")? "selected" : ""%>><ct:FWLabel key="PUCS_TYPE_PUCS"/></option>
			<option value = "2" <%=Objects.equals(type,"2")? "selected" : ""%>><ct:FWLabel key="PUCS_TYPE_DAN"/></option>
		    <option value = "4" <%=Objects.equals(type,"4")? "selected" : ""%>><ct:FWLabel key="PUCS_TYPE_SWISS_DEC"/></option>
		</select>
	</td>
	<td> 
		&nbsp;<ct:FWLabel key="PUCS_TRIE_PAR"/>
	</td>
	<td>
		<select name="orderBy">
			<option value = ""></option>
			<option value = "NOM_AFFILIE" <%=Objects.equals(orderBy,"NOM_AFFILIE")? "selected" : ""%> ><ct:FWLabel key="PUCS_ORDERBY_NOM_AFFILIE"/></option>
		    <option value = "NUMERO_AFFILIE" <%=Objects.equals(orderBy,"NUMERO_AFFILIE")? "selected" : ""%> ><ct:FWLabel key="PUCS_ORDERBY_NUMERO_AFFILIE"/></option>
		    <option value = "DATE_RECEPTION" <%=Objects.equals(orderBy,"DATE_RECEPTION")? "selected" : ""%> ><ct:FWLabel key="PUCS_ORDERBY_DATE_RECEPTION"/></option>
			<option value = "HANDLING_USER" <%=Objects.equals(orderBy, "HANDLING_USER")? "selected" : ""%> ><ct:FWLabel key="PUCS_ORDERBY_USER"/></option>
		</select>
	</td>
</tr>
<tr>
	<td>
		<input type="checkbox" name="isSelectionnerToHandle" id="isSelectionnerToHandle" <%=isSelectionnerToHandle? "checked" : ""%> />
		<ct:FWLabel key="FICHIERS_A_TRAITER"/>
	</td>
	<td>
		&nbsp;
	</td>
	<td>
		<ct:FWLabel key="PUCS_RECHERCHE"/>
	</td>
	<td>
		<input type="text" name="fullText" value="<%=Objects.isNull(recherche)? "":recherche%>"/>
	</td>
	<td>
		<ct:FWLabel key="JSP_GEB0001_TYPE_DECLARATION"/>
	</td>
	<td>
		<select name="forTypeDeclaration">
			<option value = ""></option>
			<option value = "<%=DeclarationSalaireType.PRINCIPALE.getValue()%>" <%=Objects.equals(forTypeDeclaration, String.valueOf(DeclarationSalaireType.PRINCIPALE.getValue()))? "selected" : ""%>><ct:FWLabel key="JSP_GEB0001_TYPE_DECLARATION_PRINCIPALE"/></option>
			<option value = "<%=DeclarationSalaireType.COMPLEMENTAIRE.getValue()%>" <%=Objects.equals(forTypeDeclaration, String.valueOf(DeclarationSalaireType.COMPLEMENTAIRE.getValue()))? "selected" : ""%>><ct:FWLabel key="JSP_GEB0001_TYPE_DECLARATION_COMPLEMENTAIRE"/></option>
		</select>
	</td>
	<td>
		<ct:FWLabel key="USER"/>
	</td>
	<td>
		<select name="forUser" id="forUser">
			<option value = ""></option>
		</select>
	</td>
</tr>
<tr>
	<td>

	</td>
	<td>

	</td>
	<td>
		<ct:FWLabel key="MENU_DATE_RECEPTION"/>
	</td>
	<td></td>
	<td>
		<ct:FWLabel key="DU"/>
		&nbsp;<input	id="forDateDebut"
						name="forDateDebut"
						data-g-calendar="yearRange:¦1900:<%=java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)%>¦"
						class="obligatoirePourRecherche"
						value="<%=Objects.isNull(forDateDebut)? "":forDateDebut%>" />
	</td>
	<td></td>
	<td>
		<ct:FWLabel key="AU"/>

		&nbsp;<input	id="forDateFin"
						name="forDateFin"
						data-g-calendar="yearRange:¦1900:<%=java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)%>¦"
						class="obligatoirePourRecherche"
						value="<%=Objects.isNull(forDateFin)? "":forDateFin%>" />
	</td>
</tr>

<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
<%-- tpl:insert attribute="zoneButtons" --%>
<ct:ifhasright element="orion.pucs.pucsImport.afficher" crud="u">
<input type="button" id="simulation" value="<ct:FWLabel key="PUCS_SIMULATION"/>"/>&nbsp;
<input type="button" id="ctrlSwissDec" value="<ct:FWLabel key="PUCS_CONTROLE_SWISSDEC"/>"/>&nbsp;
<%-- <input type="button" id="importFileInDb" value="<ct:FWLabel key="PUCS_IMPORT_FILE"/>"/>&nbsp;  --%>

</ct:ifhasright>
<ct:ifhasright element="orion.pucs.pucsImport.afficher" crud="u">
<input type="button" id="maj" value="<ct:FWLabel key="PUCS_MISE_A_JOUR"/>"/>
</ct:ifhasright>

<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:insert attribute="zoneVieuxBoutons" --%><%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
