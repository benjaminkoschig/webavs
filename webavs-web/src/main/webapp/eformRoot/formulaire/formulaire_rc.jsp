<%@ page import="globaz.eform.helpers.GFGestionnaireHelper" %>
<%@ page import="globaz.eform.helpers.GFFormulaireHelper" %>
<%@ page import="ch.globaz.eform.constant.GFStatusEForm" %>
<%@ page errorPage="/errorPage.jsp" %>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>

<%@ include file="/theme/find/header.jspf" %>

<%-- tpl:insert attribute="zoneInit" --%>

<ct:menuChange displayId="menu" menuId="eform-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="eform-optionsempty"/>
<%-- /tpl:insert --%>

<%@ include file="/theme/find/javascripts.jspf" %>
<link rel="stylesheet" type="text/css" href="<%=servletContext%>/scripts/erichynds.multiSelect/jquery.multiselect.css" rel="stylesheet" />

<script type="text/javascript" src="<%=servletContext%>/scripts/erichynds.multiSelect/jquery.multiselect.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>

<%-- tpl:insert attribute="zoneScripts" --%>
<%-- /tpl:insert --%>

<%@ include file="/theme/find/bodyStart.jspf" %>

<%-- tpl:insert attribute="zoneTitle" --%>
<ct:FWLabel key="RECHERCHE_FORMULAIRE_TITRE"/>
<%-- /tpl:insert --%>

<%@ include file="/theme/find/bodyStart2.jspf" %>

<%-- tpl:insert attribute="zoneMain" --%>
<tr>
	<td width="50px">
	</td>
	<td width="150px">
		<LABEL for="byGestionnaire">
			<ct:FWLabel key="GESTIONNAIRE"/>
		</LABEL>
	</td>
	<td>
		<ct:FWListSelectTag data="<%=GFFormulaireHelper.getGestionnairesData(objSession)%>"
							defaut=""
							name="formulaireSearch.byGestionnaire"/>
	</td>
	<td width="25px">
	</td>
	<td width="150px">
		<LABEL for="byStatus">
			<ct:FWLabel key="STATUS"/>
		</LABEL>
	</td>
	<td>
		<ct:FWCodeSelectTag name="formulaireSearch.forStatus"
							defaut="<%=GFStatusEForm.RECEIVE%>"
							codeType="GFSTATUS"/>
	</td>
	<td width="25px">
	</td>
	<td width="150px">
	</td>
	<td>
	</td>
</tr>
<tr>
	<td colspan="9"></td>
</tr>
<tr>
	<td width="50px">
	</td>
	<td width="150px">
		<LABEL for="byType">
			<ct:FWLabel key="TYPE_FORMULAIRE"/>
		</LABEL>
	</td>
	<td>
		<ct:FWListSelectTag data="<%=GFFormulaireHelper.getTypeData(objSession)%>"
							defaut=""
							name="formulaireSearch.byStatus"/>
	</td>
	<td width="25px">
	</td>
	<td width="150px">
		<LABEL for="byDate">
			<ct:FWLabel key="DATE_FORMULAIRE"/>
		</LABEL>
	</td>
	<td>
		<ct:FWCalendarTag name="formulaireSearch.byDate" value=""/>
	</td>
	<td width="25px">
		<LABEL for="byId">
			<ct:FWLabel key="ID_FORMULAIRE"/>
		</LABEL>
	</td>
	<td width="150px">
		<ct:inputText name="envoiComplexModel.envoiJobSimpleModel.id"/>
	</td>
	<td>
	</td>
</tr>
<tr>
	<td colspan="9"></td>
</tr>
<tr>
	<td width="50px">
	</td>
	<td width="150px">
		<LABEL for="likeNSS">
			<ct:FWLabel key="NSS"/>
		</LABEL>
	</td>
	<td>
		<nss:nssPopup avsMinNbrDigit="2" nssMinNbrDigit="2" name="likeNss" newnss="true" tabindex="3"/>

		<ct:inputHidden name="searchModel.likeNss"/>
	</td>
	<td width="25px">
	</td>
	<td width="150px">
		<LABEL for="forStatus">
			<ct:FWLabel key="STATUS"/>
		</LABEL>
	</td>
	<td>

	</td>
	<td width="25px">
	</td>
	<td width="150px">
	</td>
	<td>
	</td>
</tr>
<tr>
	<td colspan="9"></td>
</tr>
<tr>
	<td width="50px">
	</td>
	<td width="150px">
		<LABEL for="forGestionnaire">
			<ct:FWLabel key="GESTIONNAIRE"/>
		</LABEL>
	</td>
	<td>
	</td>
	<td width="25px">
	</td>
	<td width="150px">
	</td>
	<td>
	</td>
	<td width="25px">
	</td>
	<td width="150px">
	</td>
	<td>
	</td>
</tr>
<%-- /tpl:insert --%>

<%@ include file="/theme/find/bodyButtons.jspf" %>
<%-- tpl:insert attribute="zoneButtons" --%>
<%-- /tpl:insert --%>

<%@ include file="/theme/find/bodyEnd.jspf" %>

<%-- tpl:insert attribute="zoneVieuxBoutons" --%>
<%-- /tpl:insert --%>

<%@ include file="/theme/find/bodyClose.jspf" %>
