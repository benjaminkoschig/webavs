<%@ page import="globaz.eform.helpers.formulaire.GFFormulaireHelper" %>
<%@ page import="ch.globaz.eform.business.search.GFFormulaireSearch" %>
<%@ page import="ch.globaz.eform.utils.GFSessionDataContainerHelper" %>
<%@ page import="globaz.commons.nss.NSUtil" %>

<%@ page errorPage="/errorPage.jsp" %>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>


<%@ include file="/theme/find/header.jspf" %>
<%
	idEcran="GFE0001";
	GFFormulaireSearch gfFormulaireSearch = GFSessionDataContainerHelper.getGFFormulaireSearchFromSession(session);
	bButtonNew = false;
%>

<%-- tpl:insert attribute="zoneInit" --%>
<%-- /tpl:insert --%>

<%@ include file="/theme/find/javascripts.jspf" %>
<link rel="stylesheet" type="text/css" href="<%=servletContext%>/scripts/erichynds.multiSelect/jquery.multiselect.css" />
<link rel="stylesheet" type="text/css" href="<%=servletContext%>/eformRoot/css/formulaire/formulaire_rc.css" />

<script type="text/javascript" src="<%=servletContext%>/scripts/erichynds.multiSelect/jquery.multiselect.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/menu.js"></script>


<%-- tpl:insert attribute="zoneScripts" --%>
<script >
	var bFind = true;
	var detailLink = "<%=actionNew%>";
	var usrAction = "eform.formulaire.formulaire.lister";

	function clearFields() {
		document.getElementsByName("byGestionnaire")[0].value = "<%=objSession.getUserId()%>";
		document.getElementsByName("byStatus")[0].value = "";
		document.getElementsByName("byType")[0].value = "";
		document.getElementsByName("byDate")[0].value = "";
		document.getElementsByName("byBusinessProcessId")[0].value = "";
		document.getElementsByName("likeNss")[0].value = "";
		document.getElementsByName("byLastName")[0].value = "";
		document.getElementsByName("byFirstName")[0].value = "";
		document.getElementsByName("orderBy")[0].value = "default";
	}

</script>
<%-- /tpl:insert --%>

<%@ include file="/theme/find/bodyStart.jspf" %>

<%-- tpl:insert attribute="zoneTitle" --%>
<ct:FWLabel key="RECHERCHE_FORMULAIRE_TITRE"/>
<%-- /tpl:insert --%>

<%@ include file="/theme/find/bodyStart2.jspf" %>

<%-- tpl:insert attribute="zoneMain" --%>
<tr>
	<td style="width:10px"></td>
	<td style="width:150px">
		<LABEL for="byGestionnaire">
			<ct:FWLabel key="GESTIONNAIRE"/>
		</LABEL>
	</td>
	<td>
		<ct:FWListSelectTag name="byGestionnaire"
							data="<%=GFFormulaireHelper.getGestionnairesData(objSession)%>"
							defaut="<%=gfFormulaireSearch != null ? gfFormulaireSearch.getByGestionnaire() : objSession.getUserId()%>"/>
	</td>
	<td style="width:25px">
	</td>
	<td style="width:150px">
		<LABEL for="byStatus">
			<ct:FWLabel key="STATUS"/>
		</LABEL>
	</td>
	<td>
		<ct:FWCodeSelectTag name="byStatus"
							defaut='<%=gfFormulaireSearch != null ? gfFormulaireSearch.getByStatus() : ""%>'
							wantBlank="true"
							codeType="GFSTATUS"/>
	</td>
	<td style="width:25px;height: 10px">
	</td>
	<td style="width:150px">
	</td>
	<td></td>
	<td style="width:10px"></td>
</tr>
<tr>
	<td colspan="10" style="height: 10px"></td>
</tr>
<tr>
	<td>
	</td>
	<td>
		<LABEL for="byType">
			<ct:FWLabel key="TYPE_FORMULAIRE"/>
		</LABEL>
	</td>
	<td>
		<ct:FWListSelectTag data="<%=GFFormulaireHelper.getTypeData(objSession)%>"
							defaut='<%=gfFormulaireSearch != null ? gfFormulaireSearch.getByType() : ""%>'
							name="byType"/>
	<td>
	</td>
	<td>
		<LABEL for="byDate">
			<ct:FWLabel key="DATE_FORMULAIRE"/>
		</LABEL>
	</td>
	<td>
		<INPUT id="byDate" name="byDate" class="clearable" value='<%=gfFormulaireSearch != null ? gfFormulaireSearch.getByDate() : ""%>' data-g-calendar="mandatory:false"/>
	</td>
	<td>
	</td>
	<td>
		<LABEL for="byBusinessProcessId">
			<ct:FWLabel key="ID_FORMULAIRE"/>
		</LABEL>
	</td>
	<td>
		<ct:inputText name="byBusinessProcessId" id="byBusinessProcessId" defaultValue='<%=gfFormulaireSearch != null ? gfFormulaireSearch.getByBusinessProcessId() : ""%>'/>
	</td>
	<td></td>
</tr>
<tr>
	<td colspan="10" style="height: 10px"></td>
</tr>
<tr>
	<td>
	</td>
	<td>
		<LABEL for="likeNss">
			<ct:FWLabel key="NSS"/>
		</LABEL>
	</td>
	<td>
		<nss:nssPopup avsMinNbrDigit="2" nssMinNbrDigit="2" name="likeNss" newnss="true" tabindex="3"
					  value='<%=gfFormulaireSearch != null ? NSUtil.formatWithoutPrefixe(gfFormulaireSearch.getLikeNss(), true) : ""%>'/>

		<ct:inputHidden name="likeNss"/>
	</td>
	<td>
	</td>
	<td>
		<LABEL for="byLastName">
			<ct:FWLabel key="LASTNAME"/>
		</LABEL>
	</td>
	<td>
		<ct:inputText name="byLastName" id="byLastName" defaultValue='<%=gfFormulaireSearch != null ? gfFormulaireSearch.getByLastName() : ""%>'/>
	</td>
	<td>
	</td>
	<td>
		<LABEL for="byFirstName">
			<ct:FWLabel key="FIRSTNAME"/>
		</LABEL>
	</td>
	<td>
		<ct:inputText name="byFirstName" id="byFirstName" defaultValue='<%=gfFormulaireSearch != null ? gfFormulaireSearch.getByFirstName() : ""%>'/>
	</td>
	<td></td>
</tr>
<tr>
	<td colspan="10" style="height: 10px"></td>
</tr>
<tr>
	<td>
	</td>
	<td>
		<LABEL for="orderBy">
			<ct:FWLabel key="SORT_BY"/>
		</LABEL>
	</td>
	<td colspan="8">
		<ct:FWListSelectTag name="orderBy"
							data="<%=GFFormulaireHelper.getSortByData(objSession)%>"
							defaut='<%=gfFormulaireSearch != null ? gfFormulaireSearch.getOrderBy() : "default"%>'/>
	</td>
</tr>
<tr>
	<td colspan="10" style="height: 10px"></td>
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
