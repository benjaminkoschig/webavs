<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%@page import="java.util.Enumeration"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%-- tpl:insert attribute="zoneInit" --%>
<% 
	idEcran = "AL0014";
	rememberSearchCriterias = true;
	bButtonNew = false;
	//actionNew = servletContext + mainServletPath + "?userAction=al.dossier.dossierMain.afficher&_method=add";
	
	//Nom des champs (utile pour prefix du champ dynamique)
 	/*Map mapParams = new HashMap();
 
	Enumeration paramNames = request.getParameterNames();
	while (paramNames.hasMoreElements()) {
		String  param = (String) paramNames.nextElement();
		if (request.getParameter(param) != null) {
			mapParams.put(param,request.getParameter(param));
		}
	}*/
	
	
	String formatterAffilie = objSession.getApplication().getProperty("formatNumAffilie");

%>
<%-- /tpl:insert --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%@page import="globaz.fweb.util.JavascriptEncoder"%>
<%-- tpl:insert attribute="zoneScripts" --%>

<%@page import="globaz.jade.client.util.JadeStringUtil"%>

<script type="text/javascript" src="<%=servletContext%>/alRoot/util_webaf.js"></script>
<script type="text/javascript" src="<%=servletContext%>/alRoot/globazIFormatDataFormatter.js"></script>
<script type="text/javascript">
usrAction = "al.prestation.recap.lister";

function resetSearchFields(){
	
	document.getElementById("recapSearchModel.forNumeroAffilie").value="";
	document.getElementById("recapSearchModel.forIdRecap").value="";

	//document.forms[0].submit();	
}
//définir que si  rememberSearchCriterias=false
//function setFieldFromRequest(){

function initCheckBox() {
	
	/*document.getElementById('searchModel.forIdDossier').onfocus = function(){
		resetZonePartForm('AL0002');
	};*/
	
	//si l'un de ces critères est rempli, on lance une recherche
	
}

</script>


<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
		<%-- tpl:insert attribute="zoneTitle" --%>
		<ct:FWLabel key="AL0014_TITRE" />
		<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
		<%-- tpl:insert attribute="zoneMain" --%>
<tr>
	<td>
	<div id="AL0014">
    	<table class="searchTab" id="searchTab">
        	<tr>    	
              	<td>
                	<ct:FWLabel key="AL0014_RECHERCHE_NUM_AFFILIE"/>
                </td>
				<td>
					
					<!--<ct:inputText name="recapSearchModel.forNumeroAffilie" />-->
					<input type="text" name="recapSearchModel.forNumeroAffilie" tabindex="1" value="" data-g-numaffilieformatter="formatterClass:<%=formatterAffilie%>" />
				
				</td>  
              	<td><ct:FWLabel key="AL0014_RECHERCHE_ID"/></td>
				<td>
					<input name="recapSearchModel.forIdRecap" tabindex="2" type="text"/>
				</td>
				<td><ct:FWLabel key="AL0014_RECHERCHE_NUMPROCESSUS"/></td>
				<td>
					<input name="recapSearchModel.forNumProcessusLie" tabindex="3" type="text" readonly="true" class="readOnly"/>
				</td>
            </tr> 
          
        </table>
        <hr/>      
	</div>
	</td>
</tr>
	 					<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:insert attribute="zoneVieuxBoutons" --%>
<ct:menuChange displayId="menu" menuId="menuWEBAF" showTab="menu"/>
<ct:menuChange displayId="options" menuId="default-detail"/>				
<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
