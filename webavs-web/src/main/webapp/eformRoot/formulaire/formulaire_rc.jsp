<%@ page import="globaz.eform.helpers.formulaire.GFFormulaireHelper" %>

<%@ page errorPage="/errorPage.jsp" %>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>


<%@ include file="/theme/find/header.jspf" %>
<%
	idEcran="GF0001";
	bButtonNew = false;
%>

<%-- tpl:insert attribute="zoneInit" --%>
<%-- /tpl:insert --%>

<%@ include file="/theme/find/javascripts.jspf" %>
<link rel="stylesheet" type="text/css" href="<%=servletContext%>/scripts/erichynds.multiSelect/jquery.multiselect.css" />
<link rel="stylesheet" type="text/css" href="<%=servletContext%>/scripts/eform/formulaire/formulaire_rc.css" />

<script type="text/javascript" src="<%=servletContext%>/scripts/erichynds.multiSelect/jquery.multiselect.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/menu.js"></script>


<%-- tpl:insert attribute="zoneScripts" --%>
<script >
	var bFind = true;
	var detailLink = "<%=actionNew%>";
	var usrAction = "eform.formulaire.formulaire.lister";
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
							defaut="<%=objSession.getUserId()%>"/>
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
							defaut=""
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
							defaut=""
							name="byType"/>
	<td>
	</td>
	<td>
		<LABEL for="byDate">
			<ct:FWLabel key="DATE_FORMULAIRE"/>
		</LABEL>
	</td>
	<td>
		<INPUT id="byDate" name="byDate" class="clearable" value="" data-g-calendar="mandatory:false"/>
<%--		<ct:FWCalendarTag name="byDate" value=""/>--%>
	</td>
	<td>
	</td>
	<td>
		<LABEL for="byMessageId">
			<ct:FWLabel key="ID_FORMULAIRE"/>
		</LABEL>
	</td>
	<td>
		<ct:inputText name="byMessageId" id="byMessageId"/>
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
		<nss:nssPopup avsMinNbrDigit="2" nssMinNbrDigit="2" name="likeNss" newnss="true" tabindex="3"/>

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
		<ct:inputText name="byLastName" id="byLastName"/>
	</td>
	<td>
	</td>
	<td>
		<LABEL for="byFirstName">
			<ct:FWLabel key="FIRSTNAME"/>
		</LABEL>
	</td>
	<td>
		<ct:inputText name="byFirstName" id="byFirstName"/>
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
							defaut="default"/>
	</td>
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
