<%@page import="globaz.jade.properties.JadePropertiesService"%>
<%@page import="ch.globaz.al.business.constantes.ALConstRafam"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/ALTaglib.tld" prefix="al" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%@ include file="/theme/find/header.jspf" %>
<%@page import="java.util.Enumeration"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%-- tpl:insert attribute="zoneInit" --%>
<% 
idEcran = "AL0032";
rememberSearchCriterias = true;
bButtonNew=false;


String listeDelegueProp=JadePropertiesService.getInstance().getProperty("al.rafam.delegue.index");
String[] listeDelegueTab;
/* delimiter */
String delimiter = ",";
/* given string will be split by the argument delimiter provided. */
listeDelegueTab = listeDelegueProp.split(delimiter);



%>
<%-- /tpl:insert --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%@page import="globaz.fweb.util.JavascriptEncoder"%>
<%-- tpl:insert attribute="zoneScripts" --%>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>

<script type="text/javascript" src="<%=servletContext%>/alRoot/util_webaf.js"></script>
<SCRIPT type="text/javascript">
usrAction = "al.rafam.annonceRafamED.lister";

function ajaxQuery(query,handlerStateFunction){
	
	//if (event.keyCode== 40) { // curs DOWN
		if (window.XMLHttpRequest) req1 = new XMLHttpRequest();
			else if (window.ActiveXObject) req1 = new ActiveXObject("Microsoft.XMLHTTP");
			else return; // fall on our sword
		req1.open('GET', query,false); 	
		req1.onreadystatechange = alert;
		req1.send(null);
	//}
}

function resetSearchFields(){
	document.getElementById("searchModel.forIdAnnonce").value="";
	document.getElementById("searchModel.forRecordNumber").value="";
	
	document.getElementById("searchModel.likeNssEnfant").value="";
	document.getElementById("likeNssEnfant").value="";

	initCheckBox();
}

function initCheckBox() {

	$('input[name="partiallikeNssEnfant"]').keypress(function(e){	
	});
	$('input[name="partiallikeNssEnfant"]').keyup(function(e){
		nssOnKeyUp('likeNssEnfant');
		// pour formater a chaque passage
		nssAction('likeNssEnfant');
		var nssToSearch = $('input[name="likeNssEnfantNssPrefixe"]').val()+this.value;
		$('input[name="searchModel.likeNssEnfant"]').val(nssToSearch);
	});
}

function displayAjaxResult(result) {
	alert(result);
}
function printProtocole(){
		var internal  = "<%=ALConstRafam.PREFIX_INTERNAL_OFFICE_REFERENCE_ED+ALConstRafam.INTERNAL_OFFICE_REFERENCE_SEPARATOR%>";
		var idEmployeur = $("#likeInternalOfficeValue").val().substring(internal.length);
		var req = "<%=servletContext + mainServletPath%>?userAction=al.rafam.annonceRafamED.imprimerProtocole&idEmployeur="+idEmployeur;
		ajaxQuery(req, displayAjaxResult);
}


function ajaxValidationAnnonces(){
	var test =$("#likeInternalOfficeValue").val();
	var o_options= {
			serviceClassName: 'ch.globaz.al.business.services.rafam.AnnonceRafamDelegueBusinessService',
			serviceMethodName:'validationCafAnnonces',
			parametres: test,
			callBack: callbackAjaxValidationAnnonces
		}
	globazNotation.readwidget.options=o_options;		
	globazNotation.readwidget.read();	
	
}

function callbackAjaxValidationAnnonces(){
	$("#ajaxLoaderImg").hide();
	$("#idValiderBtn").removeAttr("disabled");
	$("form").submit();
}


$(document).ready(function () {
	$("#ajaxLoaderImg").hide();
	var tousED = "<%=ALConstRafam.PREFIX_INTERNAL_OFFICE_REFERENCE_ED+ALConstRafam.INTERNAL_OFFICE_REFERENCE_SEPARATOR%>";
	$("#likeInternalOfficeValue").change(function(){
		if(tousED==$(this).val()){
			$("#idValiderBtn").attr("disabled", "disabled");
			$("#idProtocoleBtn").attr("disabled", "disabled");
			
		}else{
			$("#idValiderBtn").removeAttr("disabled");
			$("#idProtocoleBtn").removeAttr("disabled");
		}
	});
	
	
	$("#idValiderBtn").click(function(){
		$("#ajaxLoaderImg").show();
		$(this).attr("disabled", "disabled");
	});
	
	
	
});

</script>


<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
		<%-- tpl:insert attribute="zoneTitle" --%>
		<ct:FWLabel key="AL0032_TITRE" />
		<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
		<%-- tpl:insert attribute="zoneMain" --%>
<tr>
	<td>
	<div id="AL0030">
    	<table class="searchTab" id="searchTab">
    	
    		<tr>
    			<td>
                	<ct:FWLabel key="AL0032_ANNONCE_EMPLOYEUR"/>
                </td>
				<td>
					<ct:select tabindex="1" name="searchModel.likeInternalOffice" id="likeInternalOfficeValue">
						<option value="<%=ALConstRafam.PREFIX_INTERNAL_OFFICE_REFERENCE_ED+ALConstRafam.INTERNAL_OFFICE_REFERENCE_SEPARATOR%>" label="Tous"/>
						
						<% for(int i =0; i < listeDelegueTab.length ; i++){
							String propDescriptionDelegue = JadePropertiesService.getInstance().getProperty("al.rafam.delegue."+listeDelegueTab[i]+".desc");
						%>
						<option value="<%=ALConstRafam.PREFIX_INTERNAL_OFFICE_REFERENCE_ED+ALConstRafam.INTERNAL_OFFICE_REFERENCE_SEPARATOR%><%=listeDelegueTab[i]%>" label="<%=propDescriptionDelegue%>"/>
						<%}%> 

	                </ct:select>
				</td>
    		</tr>
        	<tr>
              	<td><ct:FWLabel key="AL0030_ANNONCE_NUMERO"/></td>
				<td>
					<ct:inputText name="searchModel.forIdAnnonce" tabindex="2" styleClass="normal"/>
				</td>
               
                <td><ct:FWLabel key="AL0030_ANNONCE_NSS_ENFANT"/></td>
				<td>
					<nss:nssPopup avsMinNbrDigit="2" nssMinNbrDigit="2" name="likeNssEnfant" newnss="true" tabindex="3"/>
			
					<ct:inputHidden name="searchModel.likeNssEnfant"/>	
				</td>
			</tr>
			<tr>
				<td><ct:FWLabel key="AL0030_ANNONCE_RECNUM"/></td>
				<td>
					<ct:inputText name="searchModel.forRecordNumber" tabindex="4" styleClass="normal"/>
				</td>
				
			</tr>
			<tr>
                <td>
                	<ct:FWLabel key="AL0030_ANNONCE_ETAT"/>
                </td>
				<td>
					<ct:select tabindex="5" name="searchModel.forEtatAnnonce">
						<option value = "" label="Tous"/>
	                    <ct:optionsCodesSystems csFamille="ALRAFAMETA"/>
	                </ct:select>
					
				</td>
				<td colspan="2"></td>
		        <td>
                	<ct:FWLabel key="AL0030_ANNONCE_DIAGNOSTIC"/>
                </td>
				<td>
					<ct:select tabindex="6" name="searchModel.forCodeRetour">
	                    <option value = "" label="Tous"/>
	                    <!--<ct:optionsCodesSystems csFamille="ALRAFAMCOR"/>-->
	                    <al:alOptionEnumTag mLibelle="getLibelle" mValue="getCode" enumName="ch.globaz.al.business.constantes.enumerations.RafamReturnCode" />
	                </ct:select>
				</td>
				<td>
					Affiche que la dernière par recordNumber
				</td>
				<td>
					<input type="checkbox" name="filterLastAnnonce"/>
				</td>  
            </tr> 
          
        </table>  
        <hr/>
        <table>
        	
        	<tr>
        		<td>
        			<input type="button" value="Clear" style="width:100px;" onclick="resetSearchFields();" accesskey="C" tabindex="6"/>&nbsp;<ct:FWLabel key="AL0002_RECHERCHE_CLEAR"/>
        		</td>
        	</tr>
        	<tr>
        		<td>
        			<input id="idProtocoleBtn" type="button" disabled="disabled" value="Protocole" style="width:100px;" onclick="printProtocole();" tabindex="7"/>
        		</td>
        		<td>
        			<input id="idValiderBtn" type="button"  disabled="disabled" value="Valider" style="width:100px;" onclick="ajaxValidationAnnonces();" tabindex="8"/>
        			<span id="ajaxLoaderImg_container">
        			<img id="ajaxLoaderImg"
							width="16px"
							height="16px"
							src="<%=request.getContextPath()%>/images/al/ajax-loader-3.gif" title="Chargement" border="0"/>
					</span>
        		</td>
        	</tr>
        </table>
      
			</td>     
						
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
<ct:menuChange displayId="options" menuId="empty-detail"/>				
<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
