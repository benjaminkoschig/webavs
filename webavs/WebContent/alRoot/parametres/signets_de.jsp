<%@page import="ch.globaz.envoi.business.contantes.IENFormatType.ENFormatType"%>
<%@page import="ch.globaz.envoi.business.contantes.IENFormatType"%>
<%@page import="globaz.envoi.constantes.ENConstantes"%>
<%@page import="globaz.al.vb.parametres.ALSignetsViewBean"%>
<%@page import="ch.globaz.envoi.business.constantes.*" %>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>
<%	
	ALSignetsViewBean viewBean=(ALSignetsViewBean) session.getAttribute("viewBean");

	String currentIdFormule = request.getParameter("idFormule");
	if(currentIdFormule==null||currentIdFormule.length()<=0){
		currentIdFormule = viewBean.getSignetModel().getFormuleList().getFormule().getId();
	}
	viewBean.retrieveFormuleList(currentIdFormule);

	btnUpdLabel = objSession.getLabel("MODIFIER");
	btnDelLabel = objSession.getLabel("SUPPRIMER");
	btnValLabel = objSession.getLabel("VALIDER");
	btnCanLabel = objSession.getLabel("ANNULER");
	btnNewLabel = objSession.getLabel("NOUVEAU");

	//désactive le bouton new depuis cet écran
	bButtonNew = false;
	bButtonCancel = true;
	bButtonValidate = true;
	
	
	idEcran="AL0108";
	userActionValue="al.parametres.signets.modifier";
	
	boolean viewBeanIsNew = "add".equals(request.getParameter("_method"));

%>
<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>


<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.globall.util.JAUtil"%>
<%@page import="globaz.fweb.util.JavascriptEncoder"%>
<%@page import="globaz.jade.client.util.JadeNumericUtil"%>
<script type="text/javascript">

function add() {
    document.forms[0].elements('userAction').value="al.parametres.signets.ajouter";
}

function validate() {
    if (document.forms[0].elements('_method').value == "add"){
        document.forms[0].elements('userAction').value="al.parametres.signets.ajouter";
    } else { 
        document.forms[0].elements('userAction').value="al.parametres.signets.modifier";
    }
    return true;
}

function cancel() {
	document.forms[0].elements('userAction').value="al.parametres.signets.chercher";
}

function del() {
	var msgDelete = '<%=JavascriptEncoder.getInstance().encode(objSession.getLabel("MESSAGE_SUPPRESSION"))%>';
    if (window.confirm(msgDelete)){
        document.forms[0].elements('userAction').value="al.parametres.signets.supprimer";
        document.forms[0].submit();
    }
}

function upd() {
	// keep not modifiable field not modifiable
}

function init(){
}


function postInit(){
}

$(function() {
	$('#classLoaderImg').hide();
	$('#methodLoaderImg').hide();
	getListClasses();
	$('#inputClass').blur(function(){
		var s_inputValue =$('#inputClass').val();
		if(s_inputValue.length>0){
			getListMethods(s_inputValue);
		}
	});
});

//---------------------------------------------------------------------
// Récupération de la liste des classes ch.globaz
//---------------------------------------------------------------------
function getListClasses(){
	$('#classLoaderImg').show();
	var o_options= {
		serviceClassName: 'ch.globaz.al.business.services.envoi.EnvoiItemService',
		serviceMethodName:'getAllClassList',
		parametres: "ch.globaz",
		callBack: loadListClasses
	}
	globazNotation.readwidget.options=o_options;		
	globazNotation.readwidget.read();	
}

function loadListClasses(data){
	
	var s_Message = 'Classes : '+data.length+'\n';
	var a_Data = [];
	for(var i_Element=0;i_Element<data.length;i_Element++){
		s_Message +='\n'+data[i_Element];
		a_Data[i_Element] = data[i_Element];
	}
	if (typeof console !== 'undefined') {
        console.log(s_Message);    
    }

	$('#inputClass').autocomplete({
		minLength : 3,
		source: a_Data	
	});
	$('#classLoaderImg').hide();
}
//---------------------------------------------------------------------
// Récupération de la liste des méthodes à partir d'une classe
//---------------------------------------------------------------------
function getListMethods(fullClassName){
	$('#methodLoaderImg').show();
	var o_options= {
		serviceClassName: 'ch.globaz.al.business.services.envoi.EnvoiItemService',
		serviceMethodName:'getAllMethodList',
		parametres: fullClassName,
		callBack: loadListMethods
	}
	globazNotation.readwidget.options=o_options;		
	globazNotation.readwidget.read();	
}
function loadListMethods(data){
	
	var s_Message = 'Methods : '+data.length+'\n';
	var a_Data = [];
	for(var i_Element=0;i_Element<data.length;i_Element++){
		s_Message +='\n'+data[i_Element];
		a_Data[i_Element] = data[i_Element];
	}
	if (typeof console !== 'undefined') {
        console.log(s_Message);    
    }
	$('#inputMethod').autocomplete({
		minLength : 0,
		source: a_Data	
	});
	$('#methodLoaderImg').hide();
}


</script>


<style type="text/css">
.ui-autocomplete { height: 200px; overflow-y: scroll; overflow-x: hidden;}
</style>


<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:insert attribute="zoneTitle" --%>
				<%=(viewBeanIsNew?objSession.getLabel("AL0108_TITRE_PRINCIPAL_CREATION"):objSession.getLabel("AL0108_TITRE_PRINCIPAL_MODIFICATION"))%>		
			<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:insert attribute="zoneMain" --%>
			<tr>
				<td>
					<INPUT type="hidden" name="searchModel.forIdFormule" value="<%=currentIdFormule%>">
					<div id="signetDiv" style="float:left;width=100%">
						<table id="signetTable" class="zone" style="width=100%">
							<tr><td colspan="8">&nbsp;</td></tr>
							<tr>
								<td></td>
								<td class="subtitle"><ct:FWLabel key="AL0108_DETAIL"/></td>
								<td></td>
								<td><ct:FWLabel key="AL0108_NOM"/></td>
								<td>
									<input size="80" data-g-string="sizeMax:150,autoFit:false" tabindex="1" class="clearable" type="text" name="signetModel.simpleSignetModel.signet" 
									value="<%=(!viewBeanIsNew?viewBean.getSignetModel().getSimpleSignetModel().getSignet():"")%>">
								</td>
								<td></td>
								<td></td>
							</tr>
							<tr>
								<td></td>
								<td></td>
								<td></td>
								<td><ct:FWLabel key="AL0108_DESCRIPTION"/></td>
								<td>
									<input size="80" data-g-string="sizeMax:250,autoFit:false" tabindex="2" class="clearable" type="text" name="signetModel.simpleSignetModel.libelle" 
									value="<%=(!viewBeanIsNew?viewBean.getSignetModel().getSimpleSignetModel().getLibelle():"")%>">
								</td>
								<td></td>
								<td></td>
							</tr>
							<tr>
								<td></td>
								<td></td>
								<td></td>
								<td><ct:FWLabel key="AL0108_TYPE_DONNEES"/></td>
								<td>
								<select  tabindex="3" name="signetModel.simpleSignetModel.code">
								<%
									IENFormatType.ENFormatType[] allValues = IENFormatType.ENFormatType.values();
									for(int iValue = 0; iValue<allValues.length;iValue++){
										IENFormatType.ENFormatType currentFormat=allValues[iValue];
										boolean bSelected = false;
										String bSelectedString = "";
										if(!viewBeanIsNew){
											if(currentFormat.getValue().equals(viewBean.getSignetModel().getSimpleSignetModel().getCode())){
												bSelected=true;
											}
										}else{
											if(currentFormat==IENFormatType.ENFormatType.NATIF){
												bSelected = true;
											}
										}
										if(bSelected){
											bSelectedString = " selected=\"selected\"";
										}
										
								%>
									<option value="<%=currentFormat.getValue()%>"<%=bSelectedString%>><%=currentFormat.toString()%></option>
								<%
									}
								%>								
								</select>
								</td>
								<td></td>
								<td></td>
								<td></td>
							</tr>
							<tr>
								<td></td>
								<td></td>
								<td></td>
								<td><ct:FWLabel key="AL0108_NOM_CLASSE"/></td>
								<td>
									<input id="inputClass" size="80" data-g-string="sizeMax:250,autoFit:false" tabindex="4" class="clearable" type="text" name="signetModel.simpleSignetModel.model" 
									value="<%=(!viewBeanIsNew?viewBean.getSignetModel().getSimpleSignetModel().getModel():"")%>">
								</td>
								<td style="width:20px" align="left">
									<img id="classLoaderImg"
										src="<%=request.getContextPath()%>/images/al/ajax-loader-1.gif" 
										>
								</td>
								<td></td>
								<td></td>
							</tr>
							<tr>
								<td></td>
								<td></td>
								<td></td>
								<td><ct:FWLabel key="AL0108_NOM_METHODE"/></td>
								<td>
									<input id="inputMethod" size="80" data-g-string="sizeMax:250,autoFit:false" tabindex="5" class="clearable" type="text" name="signetModel.simpleSignetModel.methode" 
									value="<%=(!viewBeanIsNew?viewBean.getSignetModel().getSimpleSignetModel().getMethode():"")%>">
								</td>
								<td style="width:20px" align="left">
									<img id="methodLoaderImg"
										src="<%=request.getContextPath()%>/images/al/ajax-loader-1.gif" 
										>
								</td>
								<td></td>
								<td></td>
							</tr>
							<tr><td colspan="8">&nbsp;</td></tr>
						</table>
					</div>
				</td>
			</tr>
<%-- /tpl:put --%>

<%-- tpl:insert attribute="zoneButtons" --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>

<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>
