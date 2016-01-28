<%@page import="ch.globaz.al.business.constantes.ALCSEnvoi"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>

<%@ page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page import="globaz.fweb.util.JavascriptEncoder"%>
<%@ page import="ch.globaz.al.business.constantes.ALCSEnvoi" %>


<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>

<% 
idEcran = "AL0103";
rememberSearchCriterias = true;
bButtonNew = true;
%>

<%-- /tpl:insert --%>


<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>

<script type="text/javascript">

usrAction = "al.envois.parametres.lister";
bFind = true;

function resetSearchFields(){
}

$(function() {
});


</script>

<%-- /tpl:insert --%>


<%@ include file="/theme/find/bodyStart.jspf" %>
	<%-- tpl:insert attribute="zoneTitle" --%>
			<%=objSession.getLabel("AL0103_TITRE_PRINCIPAL") %>
	<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>

<%-- tpl:insert attribute="zoneMain" --%>
<tr>
	<td>
	<table style="width:100%">
		<tr>
		<td width="5px">&nbsp;</td>
		<td style="vertical-align:middle">
			<div id="searchFieldsParametres" style="float:left;width=100%">
				<table id="searchFieldsParametresTable" class="zone" style="width=100%">
					<tr><td colspan="7">&nbsp;</td></tr>
					<tr>
						<td></td>
						<td class="subtitle" colspan ="5"><ct:FWLabel key="AL0103_SOUS_TITRE"/></td>
						<td></td>
					</tr>
					<tr><td colspan="7">&nbsp;</td></tr>
					<tr>
						<td></td>
						<td><ct:FWLabel key="AL0103_TYPE_PARAMETRE"/></td>
						<td>
							<ct:FWCodeSelectTag defaut=""
								name="searchModel.forCsTypeParametre" codeType="ALENVOIPAR" 
								wantBlank="true" />
						</td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
					</tr>
					<tr><td colspan="7">&nbsp;</td></tr>
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
<%-- /tpl:insert --%>

<%@ include file="/theme/find/bodyClose.jspf" %>
