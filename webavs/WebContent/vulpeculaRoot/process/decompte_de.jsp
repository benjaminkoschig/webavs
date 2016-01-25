<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ page isELIgnored ="false" %>

<script type="text/javascript">
	globazGlobal.isAnnuelle = ${viewBean.annuelle};
	globazGlobal.csComplementaire = ${viewBean.csComplementaire};
</script>
<script type="text/javascript" src="vulpeculaRoot/scripts/validations.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/vulpeculaRoot/process/decompte_de.js"></script>



<div id="properties" >
	<table>
		<tr>
			<td>
				<label><ct:FWLabel key='JSP_PROCESS_DECOMPTE_TYPE'/></label>
			</td>
			<td>
				<ct:FWCodeSelectTag name="TYPE_DECOMPTE" codeType="PTTYPEDECO" wantBlank="true" defaut="" except="${viewBean.listExceptEtats}" notation="data-g-select='mandatory:true'"/>
			</td>
		</tr>
		<tr>
			<td>
				<label for="mensuelleTrimestrielle"><ct:FWLabel key="JSP_PROCESS_DECOMPTE_MENSUELLE_TRIMESTRIELLE"/></label>
			</td>
			<td>
				<input id="mensuelleTrimestrielle" class="typePeriodicite" name="PERIODICITE" type="radio" value="MENSUELLE_TRIMESTRIELLE" checked="checked"/>
			</td>
		</tr>
		<tr>
			<td>
				<label for="annuelle"><ct:FWLabel key="JSP_PROCESS_DECOMPTE_ANNUELLE"/></label>
			</td>
			<td>
				<input id="annuelle" class="typePeriodicite" name="PERIODICITE" type="radio" value="ANNUELLE" />
			</td>
		</tr>
		</tr>
		<tr>
			<td>
				<label><ct:FWLabel key='JSP_PROCESS_DECOMPTE_DATE'/></label>
			</td>
			<td>	
				<input id="DATE_DECOMPTE" data-g-calendar="mandatory:true" value="" />
			</td>
		</tr>
		<tr>
			<td>
				<label><ct:FWLabel key='JSP_PROCESS_DECOMPTE_CONVENTION'/></label>
			</td>
			<td>	
				<ct:FWListSelectTag name="CONVENTIONS" defaut="" data="${viewBean.conventions}" notation="data-g-select='mandatory:true'"/>
			</td>
		</tr>
		<tr>
			<td>
					<label><ct:FWLabel key='JSP_PROCESS_DECOMPTE_EMPLOYEUR_SPECIAL'/></label>
			</td>
			<td>		
					<input class="jadeAutocompleteAjax" type="text" id="employeurNumeroLibelle"
						data-g-autocomplete="
							service:¦ch.globaz.naos.business.service.AffiliationService¦,
							method:¦widgetFind¦,
							criterias:¦{'likeNumeroAffilie': 'No affilié', 'likeDesignationUpper': 'designation'}¦,
							lineFormatter:¦#{affiliation.affilieNumero} #{affiliation.raisonSociale}¦,
							modelReturnVariables:¦affiliation.affiliationId,affiliation.affilieNumero,affiliation.raisonSociale¦,
							constCriterias:¦inTypeAffiliationString='804002'¦,
							functionReturn:¦
								function(element) {
									var idAffilie = $(element).attr('affiliation.affiliationId');
									$('#employeurNumero').val(idAffilie).change();
								this.value=$(element).attr('affiliation.affilieNumero')+','+$(element).attr('affiliation.raisonSociale');
							}
						¦,
						nbOfCharBeforeLaunch:¦3¦,mandatory:true">
				<input type="hidden" id="employeurNumero">
			</td>
		</tr>
		<tr id="periodeMensuelleTrimestrielle">
			<td>
				<label><ct:FWLabel key='JSP_PROCESS_DECOMPTE_PERIODE'/></label>
			</td>
			<td>		
				<input id="DATE_PERIODE_DE" data-g-calendar="type:month,mandatory:true" value="" />
				<ct:FWLabel key='JSP_PROCESS_DECOMPTE_A'/>
				&nbsp;&nbsp;
				<input id="DATE_PERIODE_A" data-g-calendar="type:month,mandatory:true" value="" />
			</td>
		</tr>
		<tr id="periodeAnnuelle">
			<td>
				<label><ct:FWLabel key='JSP_PROCESS_DECOMPTE_ANNEE'/></label>
			</td>
			<td>		
				<input id="ANNEE" value="${viewBean.currentYear}" />
			</td>
		</tr>		
	</table>
</div>

<ct:menuChange displayId="menu" menuId="vulpecula-menuprincipal" showTab="menu" />