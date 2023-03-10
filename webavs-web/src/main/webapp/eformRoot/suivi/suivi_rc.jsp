<%@ page import="ch.globaz.pyxis.business.service.PersonneEtendueService" %>
<%@ page import="ch.globaz.pyxis.business.service.AdministrationService" %>
<%@ page import="globaz.eform.helpers.dadossier.GFDADossierHelper" %>
<%@ page import="java.util.Map" %>
<%@ page import="globaz.eform.vb.suivi.GFSuiviViewBean" %>
<%@ page import="globaz.framework.secure.FWSecureConstants" %>
<%@ page import="ch.globaz.eform.web.servlet.GFSuiviServletAction" %>
<%@ page import="ch.globaz.eform.business.services.GFAdministrationService" %>
<%@ page import="globaz.eform.translation.CodeSystem" %>

<%@ page errorPage="/errorPage.jsp" %>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>

<%@ include file="/theme/find/header.jspf" %>

<%
	idEcran="GFE0121";
	GFSuiviViewBean viewBean = (GFSuiviViewBean) session.getAttribute("viewBean");
	bButtonNew = false;
	bButtonFind = objSession.hasRight(GFSuiviServletAction.ACTION_PATH, FWSecureConstants.READ);
%>

<%-- tpl:insert attribute="zoneInit" --%>
<%-- /tpl:insert --%>
<link rel="stylesheet" type="text/css" href="<%=servletContext%>/scripts/erichynds.multiSelect/jquery.multiselect.css" />
<link rel="stylesheet" type="text/css" href="<%=servletContext%>/eformRoot/css/suivi/suivi_rc.css" />

<%@ include file="/theme/find/javascripts.jspf" %>

<script type="text/javascript" src="<%=servletContext%>/scripts/erichynds.multiSelect/jquery.multiselect.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/menu.js"></script>


<%-- tpl:insert attribute="zoneScripts" --%>
<script >
	var bFind = true;
	var detailLink = "<%=actionNew%>";
	var usrAction = "<%=GFSuiviServletAction.ACTION_PATH%>.lister";

	function clearFields() {
		document.getElementsByName("likeNss")[0].value = "";
		document.getElementsByName("byCaisse")[0].value = "";
		document.getElementsByName("byIdTierAdministration")[0].value = "";
		document.getElementsByName("byType")[0].value = "";
		document.getElementsByName("byStatus")[0].value = "";
		document.getElementsByName("byGestionnaire")[0].value = "";
	}
</script>
<%-- /tpl:insert --%>

<%@ include file="/theme/find/bodyStart.jspf" %>

<%-- tpl:insert attribute="zoneTitle" --%>
<ct:FWLabel key="RECHERCHE_SUIVI_TITRE"/>
<%-- /tpl:insert --%>

<%@ include file="/theme/find/bodyStart2.jspf" %>

<%-- tpl:insert attribute="zoneMain" --%>
<INPUT type="hidden" id="byIdTierAdministration" name="byIdTierAdministration" value="">
<tr>
	<td style="width:150px">
		<LABEL for="likeNss">
			<ct:FWLabel key="NSS"/>
		</LABEL>
	</td>
	<td>
		<ct:widget id='likeNss' name='likeNss' defaultValue='<%=viewBean != null ? viewBean.getLikeNss() : ""%>'>
			<ct:widgetService methodName="find" className="<%=PersonneEtendueService.class.getName()%>">
				<ct:widgetCriteria criteria="forNumeroAvsActuel" label="NSS"/>
				<ct:widgetLineFormatter format="#{tiers.designation1} #{tiers.designation2} #{personneEtendue.numAvsActuel} #{personne.dateNaissance}"/>
				<ct:widgetJSReturnFunction>
					<script type="text/javascript">
						function(element){
							this.value=$(element).attr('personneEtendue.numAvsActuel');
						}
					</script>
				</ct:widgetJSReturnFunction>
			</ct:widgetService>
		</ct:widget>
	</td>
	<td style="width:150px">
		<LABEL for="byCaisse">
			<ct:FWLabel key="CAISSE"/>
		</LABEL>
	</td>
	<td>

		<ct:widget id='byCaisse' name='byCaisse' defaultValue='<%=viewBean != null ? viewBean.getByCaisse() : ""%>'>
			<ct:widgetService defaultLaunchSize="1" methodName="find" className="<%=GFAdministrationService.class.getName()%>">
				<ct:widgetCriteria criteria="forCodeAdministrationLike" label="CODE"/>
				<ct:widgetCriteria criteria="inGenreAdministration" label="GENRE" fixedValue="<%=CodeSystem.GENRE_ADMIN_CAISSE_COMP+'_'+CodeSystem.GENRE_OFFICE_AI%>" />
				<ct:widgetCriteria criteria="notNull" label="SEDEX" fixedValue="true"/>
				<ct:widgetCriteria criteria="forDesignation1Like" label="DESIGNATION"/>
				<ct:widgetLineFormatter format="#{admin.codeAdministration} - #{tiers.designation1} #{tiers.designation2} #{tiers.designation3}"/>
				<ct:widgetJSReturnFunction>
					<script type="text/javascript">
						function(element){
							$('#byIdTierAdministration').val($(element).attr('admin.idTiersAdministration'));
							this.value=$(element).attr('admin.codeAdministration') + ' - ' +  $(element).attr('tiers.designation1') + ' ' + $(element).attr('tiers.designation2');
						}
					</script>
				</ct:widgetJSReturnFunction>
			</ct:widgetService>
		</ct:widget>
	</td>
	<td style="width:150px">
		<LABEL for="byType">
			<ct:FWLabel key="TYPE"/>
		</LABEL>
	</td>
	<td>
		<ct:FWCodeSelectTag name="byType"
							defaut='<%=viewBean != null ? viewBean.getByType() : ""%>'
							wantBlank="true"
							codeType="GFDATYPE"/>
	</td>
</tr>
<tr>
	<td colspan="6" style="height: 10px"></td>
</tr>
<tr>
	<td style="width:150px">
		<LABEL for="byStatus">
			<ct:FWLabel key="STATUS"/>
		</LABEL>
	</td>
	<td>
		<ct:FWCodeSelectTag name="byStatus"
							defaut='<%=viewBean != null ? viewBean.getByStatus() : ""%>'
							wantBlank="true"
							codeType="GFDASTATUS"/>
	</td>
	<td style="width:150px;">
		<LABEL for="byGestionnaire">
			<ct:FWLabel key="GESTIONNAIRE"/>
		</LABEL>
	</td>
	<td>
		<ct:select id="byGestionnaire" styleClass="noFocus" name="byGestionnaire" notation="data-g-selectautocomplete=''" wantBlank="true">
			<%
				for(Map.Entry<String, String> e: GFDADossierHelper.getGestionnairesData(objSession).entrySet()){
			%>
				<ct:option label="<%=e.getValue()%>" value="<%=e.getKey()%>" />
			<%
				}
			%>
		</ct:select>
	</td>
	<td style="width:10px" colspan="2"></td>
</tr>
<tr>
	<td colspan="6" style="height: 10px"></td>
</tr>
<tr>
	<td></td>
	<td><input type="button" onclick="clearFields()" accesskey="C" value="<ct:FWLabel key="EFFACER"/>"> [ALT+C]</td>
</tr>

<%-- /tpl:insert --%>

<%@ include file="/theme/find/bodyButtons.jspf" %>
<%-- tpl:insert attribute="zoneButtons" --%>
<%-- /tpl:insert --%>

<%@ include file="/theme/find/bodyEnd.jspf" %>

<%-- tpl:insert attribute="zoneVieuxBoutons" --%>
<ct:menuChange displayId="menu" menuId="eform-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="eform-optionsempty"/>
<%-- /tpl:insert --%>

<%@ include file="/theme/find/bodyClose.jspf" %>
