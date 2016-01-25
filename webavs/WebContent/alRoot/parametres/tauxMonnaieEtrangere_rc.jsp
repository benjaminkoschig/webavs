<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>
<% 
idEcran = "AL0040";
rememberSearchCriterias = true;
%>

<%-- /tpl:insert --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>
<SCRIPT language="JavaScript">
usrAction = "al.parametres.tauxMonnaieEtrangere.lister";

function resetSearchFields(){
	

	document.getElementById("tauxMonnaieEtrangereSearch.forDebutValiditeTaux").value="";
	document.getElementById("tauxMonnaieEtrangereSearch.forTypeMonnaie").value="";
	document.getElementById("tauxMonnaieEtrangereSearch.forFinValiditeTaux").value="";
	document.forms[0].submit();	
}
</SCRIPT>


<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
		<%-- tpl:insert attribute="zoneTitle" --%>
		<ct:FWLabel key="AL0040_TITRE"/>
		<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
		<%-- tpl:insert attribute="zoneMain" --%>
		
		
		
		
<tr>
	<td>
	<div id="AL0040">
    	<table class="searchTab" id="searchTab">
        	<tr>    	
              	<td>
                	<ct:FWLabel key="AL0040_RECHERCHE_TAUX_DEBUT_VALIDITE"/>
                </td>
				<td>
					<ct:FWCalendarTag name="tauxMonnaieEtrangereSearch.forDebutValiditeTaux" tabindex="1" 
									displayType ="month" 
									value="" />
				</td>
              	<td><ct:FWLabel key="AL0040_RECHERCHE_TAUX_TYPE_MONNAIE"/></td>
				<td>	
					<ct:select name="tauxMonnaieEtrangereSearch.forTypeMonnaie" tabindex="2" wantBlank="true">	
						<ct:optionsCodesSystems csFamille="PYMONNAIE"/>		
					</ct:select>	
				</td>
            </tr> 
          
        </table>
        <hr/>      
	</div>
	</td>
</tr>


<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:insert attribute="zoneVieuxBoutons" --%>
<ct:menuChange displayId="menu" menuId="menuWEBAF" showTab="menu"/>
<ct:menuChange displayId="options" menuId="default-detail"/>				
<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyClose.jspf" %>	
<%@ include file="/theme/detail/footer.jspf" %>







