<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>

<%@ page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page import="globaz.fweb.util.JavascriptEncoder"%>


<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>

<% 
idEcran = "AL0107";
String currentIdFormule = request.getParameter("searchModel.forIdFormule");

actionNew += "&idFormule="+currentIdFormule;

String formuleFullDescription = "";
rememberSearchCriterias = true;
%>

<%-- /tpl:insert --%>


<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>

<script type="text/javascript">

usrAction = "al.parametres.signets.lister";
bFind = true;

function resetSearchFields(){
}

$(function() {
});


</script>

<%-- /tpl:insert --%>


<%@ include file="/theme/find/bodyStart.jspf" %>
	<%-- tpl:insert attribute="zoneTitle" --%>
		<%=objSession.getLabel("AL0107_TITRE_PRINCIPAL") %>
		<%=" "+ currentIdFormule%>
	<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>

<%-- tpl:insert attribute="zoneMain" --%>
<tr>
	<td>
	<INPUT type="hidden" name="searchModel.forIdFormule" value="<%=request.getParameter("searchModel.forIdFormule")%>">
	<table style="width:100%">
		<tr>
		<td width="5px">&nbsp;</td>
		<td style="vertical-align:middle">
			<div id="searchFieldsSignetsDiv" style="float:left;width=100%">
				<table id="searchFieldsSignetsTable" class="zone" style="width=100%">
					<tr><td colspan="6">&nbsp;</td></tr>
					<tr>
						<td></td>
						<td class="subtitle" colspan ="3"><ct:FWLabel key="AL0107_SOUS_TITRE"/><%=" "+ currentIdFormule%></td>
						<td></td>
						<td></td>
					</tr>
					<tr><td colspan="6">&nbsp;</td></tr>
					<tr>
						<td></td>
						<td><ct:FWLabel key="AL0107_NOM_SIGNET"/></td>
						<td>
							<input tabindex="1" class="clearable" type="text" name="searchModel.forSignetName" value="">
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
<%-- /tpl:insert --%>

<%@ include file="/theme/find/bodyClose.jspf" %>
