<%@page import="globaz.jade.properties.JadePropertiesService"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="globaz.framework.bean.FWViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/ALTaglib.tld" prefix="al" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%@ include file="/theme/find/header.jspf" %>
<%@page import="java.util.Enumeration"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="ch.globaz.al.business.services.parameters.ParametersServices"%>
<%@page import="ch.globaz.param.business.models.ParameterModel"%>
<%@page import="ch.globaz.param.business.service.*"%>
<%@page import="ch.globaz.al.business.constantes.ALConstParametres"%>
<%@page import="globaz.jade.client.util.JadeDateUtil"%>
<%@page import="java.util.Date"%>

<%-- tpl:insert attribute="zoneInit" --%>
<% 
idEcran = "AL0030";
rememberSearchCriterias = true;
bButtonNew=false;
FWViewBeanInterface viewBean = (FWViewBeanInterface)session.getAttribute("viewBean");
%>
<%-- /tpl:insert --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%@page import="globaz.fweb.util.JavascriptEncoder"%>
<%-- tpl:insert attribute="zoneScripts" --%>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>

<script type="text/javascript" src="<%=servletContext%>/alRoot/util_webaf.js"></script>
<SCRIPT language="JavaScript">
usrAction = "al.rafam.annonceRafam.lister";

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

var errorObj = new Object();
errorObj.text = "";

<%
if (viewBean != null && !JadeStringUtil.isEmpty(viewBean.getMessage())) {
%>
errorObj.text = "<%=viewBean.getMessage().trim()%>";
<%
	viewBean.setMessage("");
	viewBean.setMsgType(FWViewBeanInterface.OK);
}
%>
function showErrors() {
	if (errorObj.text != "") {
		showModalDialog('<%=servletContext%>/errorModalDlg.jsp',errorObj,'dialogHeight:20;dialogWidth:25;status:no;resizable:no');	
	}
}


function resetSearchFields(){
	document.getElementById("searchModel.forIdAnnonce").value="";
	document.getElementById("searchModel.forRecordNumber").value="";
	document.getElementById("searchModel.forIdDossier").value="";
	document.getElementById("searchModel.likeNssEnfant").value="";
	document.getElementById("likeNssEnfant").value="";
	document.getElementById("searchModel.forCodeEtat").label="Tous";
	document.getElementById("searchModel.forCodeRetour").label="Tous";
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

function postInit() {
	showErrors();
}
function displayAjaxResult(result) {
	alert(result);
}
function printProtocole(){
		var req = "<%=servletContext + mainServletPath%>?userAction=al.rafam.annonceRafam.imprimerProtocole";
		ajaxQuery(req, displayAjaxResult);
}
</SCRIPT>


<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
		<%-- tpl:insert attribute="zoneTitle" --%>
		<ct:FWLabel key="AL0030_TITRE" />
		<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
		<%-- tpl:insert attribute="zoneMain" --%>
<tr>
	<td>
	<div id="AL0030">
    	<table class="searchTab" id="searchTab">
        	<tr>
              	<td><ct:FWLabel key="AL0030_ANNONCE_NUMERO"/></td>
				<td>
					<ct:inputText name="searchModel.forIdAnnonce" tabindex="1" styleClass="normal"/>
				</td>
                <td><ct:FWLabel key="AL0030_ANNONCE_NO_DOSSIER"/></td>
				<td>
					<ct:inputText name="searchModel.forIdDossier" tabindex="2" styleClass="normal"/>
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
				<td>
                	<ct:FWLabel key="AL0030_ANNONCE_NUM_DROIT"/>
                </td>
				<td>
						<ct:inputText name="searchModel.forIdDroit" tabindex="5" styleClass="normal"/>
				</td>    
			</tr>
			<tr>
                <td>
                	<ct:FWLabel key="AL0030_ANNONCE_ETAT"/>
                </td>
				<td>
					<ct:select tabindex="6" name="searchModel.forEtat">
						<option value = "" label="Tous"/>
	                    <ct:optionsCodesSystems csFamille="ALRAFAMETA"/>
	                </ct:select>
					
				</td>
				<td colspan="2"></td>
		        <td>
                	<ct:FWLabel key="AL0030_ANNONCE_DIAGNOSTIC"/>
                </td>
				<td>
					<ct:select tabindex="7" name="searchModel.forCodeRetour">
	                    <option value = "" label="Tous"/>
	                    <!--<ct:optionsCodesSystems csFamille="ALRAFAMCOR"/>-->
	                    <al:alOptionEnumTag mLibelle="getLibelle" mValue="getCode" enumName="ch.globaz.al.business.constantes.enumerations.RafamReturnCode" />
	                </ct:select>
				</td>
				<td>
					Affiche que la dernière par recordNumber
				</td>
				<td>
				   <%ParameterModel param = ParamServiceLocator.getParameterModelService().getParameterByName(
	            				ALConstParametres.APPNAME, ALConstParametres.RAFAM_AFFICHAGE_ANNONCE,
	            				JadeDateUtil.getGlobazFormattedDate(new Date())); %>
	             
	                   <%  if("true".equals(param.getValeurAlphaParametre())){ %> 
	                    	<input type="checkbox"  checked="checked" name="filterLastAnnonce"/>
	                   
	                    <%}else{%>
	                    	<input type="checkbox" name="filterLastAnnonce"/>  		
	                    	
	                    <%}%>	
					
				</td>  
            </tr> 
          
        </table>  
        <hr/>
        <table>
        	
        	<tr>
        		<td colspan="2">
        			<input type="button" value="Clear" style="width:100px;" onclick="resetSearchFields();" accesskey="C" tabindex="6"/>&nbsp;<ct:FWLabel key="AL0002_RECHERCHE_CLEAR"/>
        		</td>
        		
        	</tr>
        	<tr>
       			<ct:ifhasright element="al.rafam.annonceRafam" crud="u">
	        		<td>
	        			<input type="button" value="Protocole" style="width:100px;" onclick="printProtocole();" tabindex="7"/>
	        		</td>
        		</ct:ifhasright>
        		<td align="right">
        			<%if(JadePropertiesService.getInstance().getProperty("al.rafam.delegue.index")!=null){%>
        				<a href="<%=servletContext + mainServletPath + "?userAction=al.rafam.annonceRafamED.chercher"%>"><ct:FWLabel key="MENU_RECHERCHE_ANNONCES_RAFAM_ED"/></a>
        			<%} %>
        		</td>
        	</tr>
        </table>      
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
