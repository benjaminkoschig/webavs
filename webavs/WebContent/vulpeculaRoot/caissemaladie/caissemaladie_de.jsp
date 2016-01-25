<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>
<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/detail_el/header.jspf" %>
<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° ecran et titre --%>
<c:set var="idEcran" value="PPT1012"/>
<c:set var="labelTitreEcran" value="JSP_CAISSE_MALADIE"/>

<%-- visibilité des boutons --%>
<c:set var="bButtonDelete" value="${viewBean.supprimable}" scope="page"/>
<c:set var="bButtonUpdate" value="${viewBean.modifiable}" scope="page"/>
<c:set var="travailleur" value="${viewBean.travailleur}" />
<c:set var="actionNew" value="${actionNew}&idTravailleur=${viewBean.idTravailleur}" />

<%--  ********************************************************************** JS CSS ***************************************************************************--%>
<%@ include file="/theme/detail_el/javascripts.jspf" %>
<script type="text/javascript" src="${rootPath}/scripts/validations.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/vulpeculaTable.js"></script>
<script type="text/javascript" src="${rootPath}/caissemaladie/caissemaladie_de.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/json2.js"></script>
<%--  *************************************************************** Script propre à la page **************************************************************** --%>
<c:set var="tableHeight" value="500" scope="page" />
<script id="suiviDocument" type="jade/template">
	<tr statut="">
		<td>
			<ct:FWCodeSelectTag name="typeDocument" codeType="PTTYPDOC" defaut="" notation=" class='typeDocument' style='width:100%'" />
		</td>
		<td>
			<input name="dateEnvoi" class="dateEnvoi" data-g-calendar="mandatory:true" />
		</td>
		<td align="center">
			<input type="checkbox" name="envoye" class='envoye' />
		</td>
	</tr>
</script>

<script type="text/javascript">
	globazGlobal.idTravailleur = "${viewBean.idTravailleur}";
	globazGlobal.idPosteTravail = "${viewBean.idPosteTravail}";
	globazGlobal.SUIVI = ${viewBean.listeSuiviGSON};
</script>

<%@ include file="/theme/detail_el/bodyStart.jspf" %>
<ct:FWLabel key="${labelTitreEcran}"/>
<%@ include file="/theme/detail_el/bodyStart2.jspf" %>
<%--  ******************************************************************* Corps de la page ******************************************************************* --%>
<div class="content">
	<div class="blocLeft">
		<div class="bloc blocMedium">
			<%@ include file="/vulpeculaRoot/blocs/travailleur.jspf" %>
		</div>
	</div>
</div>

<div style="margin-top: 16px;">
	<table>
		<tr>
			<td><ct:FWLabel key="JSP_NUMERO"/></td>
			<td id="tdCaisseMaladie">${viewBean.idCaisseMaladie}</td>
		</tr>
		<tr>
			<td><ct:FWLabel key='JSP_CAISSE_MALADIE'/></td>
			<td>
			<input 
				<c:if test="${viewBean.nouveau}">
					readonly="readonly"
					class="readOnly"
				</c:if>
			id="caisseMaladie"
			class="jadeAutocompleteAjax"
			name="tiersWidget"
			value="${viewBean.libelleCaisseMaladie}"
			type="text"
			data-g-autocomplete="service:¦ch.globaz.vulpecula.business.services.administration.AdministrationService¦,
								 method:¦find¦,
								 criterias:¦{'forDesignation1Like':'Désignation'}¦,
								 constCriterias:¦forGenreAdministration=68900003¦,
								 lineFormatter:¦#{tiers.designation1} #{tiers.designation2}¦,
								 modelReturnVariables:¦tiers.id,tiers.designation1,tiers.designation2¦,nbReturn:¦20¦,
								 functionReturn:¦
								 	function(element){
								 		var display =  $(element).attr('tiers.designation1')+' '+$(element).attr('tiers.designation2');
								 		this.value= display;
								 		var idTiers = $(element).attr('tiers.id');
								 		$('#idCaisseMaladie').val(idTiers);
								 		$('#tdCaisseMaladie').text(idTiers);
								 		$('#tempNomCaisseMaladie').val(display);
								 	}¦
								 ,nbOfCharBeforeLaunch:¦3¦"
			/>
			<input type="hidden" name="idCaisseMaladie" value="${viewBean.idCaisseMaladie}" id="idCaisseMaladie"/>
			<input type="hidden" name="tempNomCaisseMaladie" id="tempNomCaisseMaladie" />
			</td>
		<tr>
			<td>
				<ct:FWLabel key="JSP_POSTE_TRAVAIL"/>
			</td>
			<td>
				<select id="idPosteTravail" name="idPosteTravail">
					<c:forEach var="posteTravail" items="${viewBean.posteTravails}">
						<option value="${posteTravail.id}">${posteTravail.id} - ${posteTravail.raisonSocialeEmployeur} (${posteTravail.affilieNumero})</option>				
					</c:forEach>
				</select>
				<span id="adressePaiementEmployeur"/>
			</td>
		</tr>
		</tr>
		<tr>
			<td colspan="2">
				<table>
					<tr>
						<td><ct:FWLabel key="JSP_MOIS_DEBUT"/></td>
						<td><input id="moisDebut" name="moisDebut" value="${viewBean.moisDebutFormatte}" data-g-calendar="type:month" /></td>
						<td><ct:FWLabel key="JSP_MOIS_FIN"/></td>
						<td><input id="moisFin" name="moisFin" value="${viewBean.moisFinFormatte}" data-g-calendar="type:month" /></td>
					</tr>
					<tr>
						<td><ct:FWLabel key="JSP_DATE_ANNONCE_DEBUT"/></td>
						<td><input name="dateDebutAnnonce" data-g-calendar="" value="${viewBean.dateAnnonceDebutAsSwissValue}" disabled="disabled" readonly="readonly"/></td>
						<td><ct:FWLabel key="JSP_DATE_ANNONCE_FIN"/></td>
						<td><input name="dateFinAnnonce" data-g-calendar="" value="${viewBean.dateAnnonceFinAsSwissValue}" disabled="disabled" readonly="readonly"/></td>
					</tr>
				</table>
			<td>
		</tr>
	</table>
</div>

<br/>
<input name="suiviDocumentString" id="suiviDocumentString" type="hidden" />
<div id="divSuivi" class="bloc blocMedium">
	<table id="tblSuivi" style="width:600px">
		<thead>
			<tr>
				<th style='width:60%'><ct:FWLabel key='JSP_TYPE_DOCUMENT_CAISSE_MALADIE'/></th>
				<th style='width:20%'><ct:FWLabel key='JSP_DATE_ENVOI'/></th>
				<th style='width:20%'><ct:FWLabel key='JSP_DOCUMENT_ENVOYE'/></th>
			</tr>
		</thead>
		<tbody id="tblSuiviContent">
		</tbody>
	</table>
</div>

<input name="tab" type="hidden" value="" />
<ct:menuChange displayId="menu" menuId="vulpecula-menuprincipal" showTab="menu" />
<%--  **************************************************************** Fin Corps de la page ******************************************************************* --%>
<%@ include file="/theme/detail_el/bodyButtons.jspf" %>
<%@ include file="/theme/detail_el/bodyErrors.jspf" %>
<%@ include file="/theme/detail_el/footer.jspf" %>