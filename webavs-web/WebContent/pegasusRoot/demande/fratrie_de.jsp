<%-- tpl:insert page="/theme/detail.jtpl" --%>

<%@page import="globaz.hera.api.ISFMembreFamilleRequerant"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ page import="globaz.pegasus.vb.demande.PCFratrieViewBean"%>
<%@ page import="globaz.hera.api.ISFMembreFamilleRequerant"%>
<%@ page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@ page import="ch.globaz.hera.business.vo.famille.MembreFamilleVO"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%
	//Les labels de cette page commence par le préfix "JSP_PC_FRATRIE_D"
	idEcran="PPC0089";//???
	//viewbean
	PCFratrieViewBean viewBean = (PCFratrieViewBean) session.getAttribute("viewBean");
	//PopUp
	autoShowErrorPopup = true;
	//Bouton delete
	bButtonNew      = false;
	bButtonUpdate   = false;
	bButtonCancel 	= true;
	bButtonValidate = true;
	bButtonDelete   = false;
	boolean viewBeanIsNew=true;
	
	String idDroit = request.getParameter("idDroit");
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%@ include file="/pegasusRoot/ajax/javascriptsAndCSS.jspf" %>
<script type="text/javascript" src="<%=rootPath %>/scripts/demande/fratrie_de.js"></script>
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/demande/detailFratrie.css"/>

<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="pegasus-menuprincipal" showTab="options"/>
<script language="JavaScript">
var ACTION_FRATRIE = "<%= IPCActions.ACTION_FRATRIE %>";
var ACTION_CANCEL = "<%= IPCActions.ACTION_DROIT %>";
var servletContext = "<%=servletContext%>";
var actionMethod;
var userAction;

function add() {}

//update
function upd() {}

//init
function init(){
	
};

$(function () {
	//actionMethod=$('[name=_method]',document.forms[0]).val();
	actionMethod="add";
	userAction=$('[name=userAction]',document.forms[0])[0];
});



function cancel() {
	userAction.value =ACTION_CANCEL+"chercher"; 
}  

function validate() {
	state = true;
	userAction.value=ACTION_FRATRIE+".ajouter";
	return state;
}    


function del() {
}

</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_PC_FRATRIE_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
<tr>
	<td colspan="6" align="center">
		<div id="ligneRequerant">
			<span class="label"><ct:FWLabel key="JSP_PC_FRATRIE_D_REQ"/></span>
			<span id="valRequerant"><%=viewBean.getRequerantInfosAsString() %></span><span id="isChild"><ct:FWLabel key="JSP_PC_FRATRIE_D_IS_ENFANT"/></span>
		</div>	
	</td>  
</tr><tr>
<td align="center" colspan="6">

<table id="fratrieTable" >
	<thead>
		<tr>
			<th id="thChkBox"><input id="headerCheckBox" type="checkbox" data-g-mastercheckbox=" "/></th>
			<th><ct:FWLabel key="JSP_PC_FRATRIE_D_INFOTIERS" /></th>
		<tr>
	</thead>
	<tbody>
		<!--  si pas d'enfant retrouve -->
		<%if(viewBean.getFratrie().size()==0){
		%>
		<tr>
			<td></td><td><span class="noChildFound"><ct:FWLabel key="JSP_PC_FRATRIE_D_NOCHILD"/></span></td>
		</tr>
		<%} %>
		<%for(MembreFamilleVO model:viewBean.getFratrie()){
			if(!model.getIdTiers().equals(viewBean.getIdTiersRequerantEnfant())){
			%>   
			
				<tr id="<%=model.getIdMembreFamille() %>">
					<td class="tdChkChild"><input class="chkChild" value="<%=model.getIdMembreFamille() %>" name="listSelected" type="checkbox"/></td>
			   		<td><%=viewBean.formatTiersFratrieAsString(model)%></td>
				</tr>
			<% } 
		}%>
	</tbody>
</table>
<input type="hidden" name="idDemandePc" value="<%=viewBean.getDemande().getSimpleDemande().getId() %>"/>
<input type="hidden" name="idDossier" value="<%=viewBean.getDemande().getDossier().getDossier().getId() %>"/>
<input type="hidden" name="idDroit" value="<%=idDroit %>"/>

</td>
</tr>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>

<%@ include file="/theme/detail/footer.jspf" %>
	
<%-- /tpl:insert --%>