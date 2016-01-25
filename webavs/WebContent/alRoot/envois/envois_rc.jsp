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
idEcran = "AL0101";
rememberSearchCriterias = true;
bButtonNew = false;

String detailLinkParametres = "al?userAction=al.envois.parametres.chercher";


String currentUserId = objSession.getUserId();

%>

<%-- /tpl:insert --%>


<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>

<script type="text/javascript">

usrAction = "al.envois.envois.lister";
bFind = true;

function resetSearchFields(){
}

$(function() {
});


</script>

<%-- /tpl:insert --%>


<%@ include file="/theme/find/bodyStart.jspf" %>
	<%-- tpl:insert attribute="zoneTitle" --%>
		<%=objSession.getLabel("AL0101_TITRE_PRINCIPAL") %>
	<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>

<%-- tpl:insert attribute="zoneMain" --%>
<tr>
	<td>
	<table style="width:100%">
		<tr>
		<td width="5px">&nbsp;</td>
		<td style="vertical-align:middle">
			<div id="searchFieldsEnvoi" style="float:left;width=100%">
				<table id="searchFieldsEnvoiTable" class="zone" style="width=100%">
					<tr>
						<td colspan="5">&nbsp;</td>
						<td colspan="2" align="right">
							<%if (objSession.hasRight("al.envois.parametres", globaz.framework.secure.FWSecureConstants.UPDATE)) {%>
								<a href="<%=detailLinkParametres%>">
									<ct:FWLabel key="AL0101_LIEN_AVANCE"/>
								</a>
							<%
							}
							%>
						</td>
					</tr>
					
					<tr>
						<td></td>
						<td class="subtitle" colspan ="5"><ct:FWLabel key="AL0101_AFFICHER_TRAVAUX_TITRE"/></td>
						<td></td>
					</tr>
					<tr><td colspan="7">&nbsp;</td></tr>
					<tr>
						<td></td>
						<td><ct:FWLabel key="AL0101_AFFICHER_UTILISATEUR"/></td>
						<td><input tabindex="1" class="clearable" type="text" name="searchModel.forJobUser" value="<%=currentUserId%>">
						</td>
						<td></td>
						<td><ct:FWLabel key="AL0101_AFFICHER_DATE_CREATIONDESLE"/></td>
						<td><input tabindex="3" class="clearable" type="text"
							name="searchModel.forJobDateMin" value=""
							data-g-calendar="mandatory:false" />
						</td>
						<td></td>
					</tr>
					<tr>
						<td></td>
						<td><ct:FWLabel key="AL0101_AFFICHER_STATUS"/></td>
						<td><ct:FWCodeSelectTag tabindex="2" 
							name="searchModel.forJobStatus" codeType="ALENVOISTS"
							defaut="<%=ALCSEnvoi.STATUS_ENVOI_GENERATED%>" wantBlank="true" />
						</td>
						<td></td>
						<td><ct:FWLabel key="AL0101_AFFICHER_DATE_CREATIONJUSQUAU"/></td>
						<td><input tabindex="4" class="clearable" type="text"
							name="searchModel.forJobDateMax" value=""
							data-g-calendar="mandatory:false" />
						</td>
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
<ct:menuChange displayId="menu" menuId="menuWEBAF" showTab="menu"/>
<ct:menuChange displayId="options" menuId="empty-detail"/>				
<%-- /tpl:insert --%>

<%@ include file="/theme/find/bodyClose.jspf" %>
