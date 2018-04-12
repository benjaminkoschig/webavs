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
	idEcran = "GEB0005";
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
	usrAction = "orion.recap.recapAf.lister";
	bFind = true;


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
<%-- tpl:insert attribute="zoneTitle" --%><ct:FWLabel key="HEADER_TITRE_LISTE_RECAP_AF"/><%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
<%-- tpl:insert attribute="zoneMain" --%>

<tr>
	<td> 
		&nbsp;<ct:FWLabel key="RECAP_STATUT"/>
	</td>
	<td>
	<select id='statut' name='statut'>
		<OPTION value=''></OPTION>
		<OPTION value='GENEREE'><ct:FWLabel key="RECAP_STATUT_GENEREE"/></OPTION>
		<OPTION selected="selected" value='A_TRAITER'><ct:FWLabel key="RECAP_STATUT_A_TRAITER"/></OPTION>
		<OPTION value='TRAITEE'><ct:FWLabel key="RECAP_STATUT_TRAITEE"/></OPTION>
		<OPTION value='AUCUN_CHANGEMENT'><ct:FWLabel key="RECAP_STATUT_AUCUN_CHANGEMENT"/></OPTION>
		<OPTION value='CLOTUREE'><ct:FWLabel key="RECAP_STATUT_CLOTUREE"/></OPTION>
	</select>
	</td>
	<td>
		&nbsp;<ct:FWLabel key="NO_AFFILIE"/>
	</td>
	<td> 
		&nbsp;<input type="text" name="likeAffilie">
	</td>
	<td>
		&nbsp;<ct:FWLabel key="RECAP_PERIODE"/>
	</td>
	<td>
		&nbsp;<input	id="anneeMoisRecap" 
						name="anneeMoisRecap"
						data-g-calendar="type:month,mandatory:false"
						class="obligatoirePourRecherche" 
						value="" />
	</td>
</tr>
<tr>
	<td>
		&nbsp;<ct:FWLabel key="RECAP_DATE_RECEPTION"/>
	</td>
	<td>
		&nbsp;<input	id="dateSoumission" 
						name="dateSoumission"
						data-g-calendar="yearRange:¦1900:<%=java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)%>¦"
						class="obligatoirePourRecherche" 
						value="" />
	</td>
	<td>
		&nbsp;<ct:FWLabel key="RECAP_NOM"/>
	</td>
	<td>
		&nbsp;<input name="likeNom" type="text"/>
	</td>
	<td> 
		&nbsp;<ct:FWLabel key="RECAP_TRIE_PAR"/>
	</td>
	<td>
		&nbsp;<select name="orderBy">
			<option value = ""></option>
			<option value = "ORDER_BY_NOM_AFFILIE"><ct:FWLabel key="RECAP_ORDERBY_NOM_AFFILIE"/></option>
		    <option value = "ORDER_BY_NUM_AFFILIE"><ct:FWLabel key="RECAP_ORDERBY_NUMERO_AFFILIE"/></option>
		    <option value = "ORDER_BY_DATE_MODIFICATION"><ct:FWLabel key="RECAP_ORDERBY_DATE_RECEPTION"/></option>
		</select>
	</td>
</tr>

<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
<%-- tpl:insert attribute="zoneButtons" --%>


<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:insert attribute="zoneVieuxBoutons" --%><%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyClose.jspf" %>