<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>

<%@ page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page import="globaz.fweb.util.JavascriptEncoder"%>


<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>

<% 
idEcran = "AL0105";
rememberSearchCriterias = true;
%>

<%-- /tpl:insert --%>


<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>

<script type="text/javascript">

usrAction = "al.parametres.formules.lister";
bFind = true;

function resetSearchFields(){
}

$(function() {
	$('#searchModel\\.forLangue').prop('disabled','disabled');
});


</script>

<%-- /tpl:insert --%>


<%@ include file="/theme/find/bodyStart.jspf" %>
	<%-- tpl:insert attribute="zoneTitle" --%>
			<%=objSession.getLabel("AL0105_TITRE_PRINCIPAL") %>
	<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>

<%-- tpl:insert attribute="zoneMain" --%>
<tr>
	<td>
	<table style="width:100%">
		<tr>
		<td width="5px">&nbsp;</td>
		<td style="vertical-align:middle">
			<div id="searchFieldsEnvoiTemplate" style="float:left;width=100%">
				<table id="searchFieldsEnvoiTemplateTable" class="zone" style="width=100%">
					<tr><td colspan="6">&nbsp;</td></tr>
					<tr>
						<td></td>
						<td class="subtitle" colspan ="3"><ct:FWLabel key="AL0105_SOUS_TITRE"/></td>
						<td></td>
						<td></td>
					</tr>
					<tr><td colspan="6">&nbsp;</td></tr>
					<tr>
						<td></td>
						<td><ct:FWLabel key="AL0105_DOCUMENT"/></td>
						<td><ct:FWCodeSelectTag
							name="searchModel.forCsDocument" codeType="ALENVOIDOC"
							defaut="" wantBlank="true" />
						</td>
						<td></td>
						<td></td>
						<td></td>
					</tr>
					<tr>
						<td></td>
						<td><ct:FWLabel key="AL0105_LANGUAGE"/></td>
						<td><ct:FWCodeSelectTag
							name="searchModel.forLangue" codeType="PYLANGUE"
							defaut="503001" wantBlank="true" />
						</td>
						<td></td>
						<td></td>
						<td></td>
					</tr>
					<tr><td colspan="6">&nbsp;</td></tr>
				</table>
			</div>
		</td>
		<td  width="5px">&nbsp;</td>
		</tr>
		</table>
	</td>
</tr>
<%-- /tpl:insert --%>

<%@ include file="/theme/find/bodyButtons.jspf" %>
<%-- tpl:insert attribute="zoneButtons" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:insert attribute="zoneVieuxBoutons" --%>
<ct:menuChange displayId="menu" menuId="menuWEBAF" showTab="menu"/>
<ct:menuChange displayId="options" menuId="empty-detail"/>				
<%-- /tpl:insert --%>

<%@ include file="/theme/find/bodyClose.jspf" %>
