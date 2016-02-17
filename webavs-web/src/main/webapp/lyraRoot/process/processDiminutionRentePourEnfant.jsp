<%@page import="ch.globaz.common.properties.CommonProperties"%>
<%@page import="globaz.prestation.enums.CommunePolitique"%>
<%@page import="globaz.globall.db.BSession"%>
<%@page import="globaz.corvus.vb.echeances.REDiminutionRenteEnfantAjaxViewBean"%>
<%@page import="globaz.corvus.utils.REPmtMensuel"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.globall.db.BSessionUtil"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.text.SimpleDateFormat"%>
<script type="text/javascript" src="<%=request.getContextPath()%>/lyraRoot/script/process/processDiminutionRentePourEnfant_part.js"></script>
<script type="text/javascript" >
	globazGlobal.isValidationDecisionAutorise = <%=	request.getParameter("ValidationDecisionAutorise") %>;
	var $msgValidationInterdites = $('#msgValidationInterdites');
	$msgValidationInterdites.hide();
	
</script>
<%
BSession session2 = BSessionUtil.getSessionFromThreadContext();
%>
<div class="areaDetail zoneAjaxWithoutBackgroundImage" id="zoneAjaxRentePourEnfant">
	<span style="display: block;">
		<strong>
			<%=session2.getLabel("LISTE_RENTE_ENFANT_TITRE")%>
		</strong>
	</span>
	<br/>
	<div id="divBoutonsTableauAjax" class="divBoutonsTableauAjax">
		<span id="selectionnerTout">
			<%=session2.getLabel("JSP_SELECTION_TOUTES")%>
		</span>
		<span id="deselectionnerTout">
			<%=session2.getLabel("JSP_DESELECTION_TOUTES")%>
		</span>
	</div>
	<br />
	
	<%
		if(CommonProperties.ADD_COMMUNE_POLITIQUE.getBooleanValue()){
	%>
	
		<table width="100%" class="areaTable">
		<thead class="complexAjaxHeader">
			<tr>
				<th width="4%" class="notSortable" rowspan="2">
					&#160;
				</th>
				
				<th width="8%" class="notSortable" rowspan="2">
					<%=session2.getLabel(CommunePolitique.LABEL_COMMUNE_POLITIQUE_TITRE_COLONNE.getKey())%>
				</th>
				<th width="20%" rowspan="2">
					<%=session2.getLabel("LISTE_RENTE_ENFANT_NOM")%>
				</th>
				<th width="20%" rowspan="2">
					<%=session2.getLabel("LISTE_RENTE_ENFANT_PRENOM")%>
				</th>
				<th width="9%" rowspan="2" class="dateSortable">
					<%=session2.getLabel("LISTE_RENTE_ENFANT_DATE_NAISSANCE")%>Liste
				</th>
				<th width="6%" rowspan="2">
					<%=session2.getLabel("LISTE_RENTE_ENFANT_SEXE")%>
				</th>
				<th width="6%" rowspan="2">
					<%=session2.getLabel("LISTE_RENTE_ENFANT_GENRE_RENTE")%>
				</th>
				<th width="8%" rowspan="2">
					<%=session2.getLabel("LISTE_RENTE_ENFANT_MONTANT")%>
				</th>
				<th width="18%" colspan="2" class="notSortable" style="vertical-align: middle;">
					<%=session2.getLabel("LISTE_RENTE_ENFANT_PERIODES")%>
				</th>
			</tr>
			<tr>
				<th width="9%" class="dateSortable">
					<%=session2.getLabel("LISTE_RENTE_ENFANT_DATE_DEBUT_PERIODE")%>
				</th>
				<th width="9%" class="dateSortable">
					<%=session2.getLabel("LISTE_RENTE_ENFANT_DATE_FIN_PERIODE")%>
				</th>
			</tr>
		</thead>
		<tbody/>
	</table>
	
	<% }  else { %>
	
	
	
	<table width="100%" class="areaTable">
		<thead class="complexAjaxHeader">
			<tr>
				<th width="4%" class="notSortable" rowspan="2">
					&#160;
				</th>
				<th width="24%" rowspan="2">
					<%=session2.getLabel("LISTE_RENTE_ENFANT_NOM")%>
				</th>
				<th width="24%" rowspan="2">
					<%=session2.getLabel("LISTE_RENTE_ENFANT_PRENOM")%>
				</th>
				<th width="9%" rowspan="2" class="dateSortable">
					<%=session2.getLabel("LISTE_RENTE_ENFANT_DATE_NAISSANCE")%>Liste
				</th>
				<th width="6%" rowspan="2">
					<%=session2.getLabel("LISTE_RENTE_ENFANT_SEXE")%>
				</th>
				<th width="6%" rowspan="2">
					<%=session2.getLabel("LISTE_RENTE_ENFANT_GENRE_RENTE")%>
				</th>
				<th width="8%" rowspan="2">
					<%=session2.getLabel("LISTE_RENTE_ENFANT_MONTANT")%>
				</th>
				<th width="18%" colspan="2" class="notSortable" style="vertical-align: middle;">
					<%=session2.getLabel("LISTE_RENTE_ENFANT_PERIODES")%>
				</th>
			</tr>
			<tr>
				<th width="9%" class="dateSortable">
					<%=session2.getLabel("LISTE_RENTE_ENFANT_DATE_DEBUT_PERIODE")%>
				</th>
				<th width="9%" class="dateSortable">
					<%=session2.getLabel("LISTE_RENTE_ENFANT_DATE_FIN_PERIODE")%>
				</th>
			</tr>
		</thead>
		<tbody/>
	</table>
	
	
	<% } %>
	
	<div class="areaDetail" />
<%
	String moisActuel = request.getParameter("MoisDernierPaiment");
%>
	<input	type="hidden" 
			id="moisActuel" 
			value="<%=moisActuel%>" />
	<span id="messageHorsMoisActuel" style="display: none;">
		<strong>
			<%=session2.getLabel("ERREUR_MOIS_TRAITEMENT_DIFFERENT_MOIS_ACTUEL").replace("{moisActuel}", moisActuel)%>
		</strong>
	</span>
</div>

