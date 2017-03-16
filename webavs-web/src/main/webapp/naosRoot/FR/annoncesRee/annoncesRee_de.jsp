<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />

<%-- tpl:put name="zoneInit" --%>

<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.naos.db.annoncesRee.AFAnnoncesReeViewBean"%>

<%
	idEcran="CAF3023";
	AFAnnoncesReeViewBean viewBean = (AFAnnoncesReeViewBean) session.getAttribute("viewBean");  
	viewBean.initProperties();

    userActionValue="naos.annoncesRee.annoncesRee.executer";
%>


<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<SCRIPT>
var checkMandatory = {
	init: function ()  {
		var that = this;
		$("#btnOk").attr("onClick",null);
		
		$("#btnOk").click(function(e){
			var errors = that.check()
			checkMandatory.displayError(errors);

			if(errors.length){
				console.log(e);				
				e.preventDefault();
				e.stopImmediatePropagation();
			} else {		
				// Tout les champs sont renseignés, on appel le processus REE
				doOkAction();
				console.log("Tout les champs sont renseignés, appel du processus.");
			}
			return false;
		});
	},
	
	check: function () {
		var enErreurs = [];
		$(":input").each(function(index){
			var $this=$(this);
			if($this.attr("mandatory")==="true" && $.trim($this.val()).length == 0){
				var label = $("label[for='"+$this.attr("name")+"']").text();
				enErreurs.push(label);
			}
		});
		
		var email = $("#emailReference").val();
		var regexMail = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]		{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
		if(!regexMail.test(email)){
			var label = $("label[for='emailReference']").text();
			enErreurs.push(label);
		}
		
		return enErreurs;
	},
	
	displayError: function(enErreurs) {
		if(enErreurs && enErreurs.length) {
			
			// Création du dialogue d'erreurs
			var $dialog = $('<div></div>')
			    .dialog({
			    	resizable: false,
				    width: 400,
				    height: 300,
				    modal: true,
			        title: $('#labelErreursTitre').val(),
				    buttons: {
				        "Ok" : function () {
				            $(this).dialog("close");
				        }
			    	}
			    });
			
			var html = "<ul>";
			$.each(enErreurs, function () {
				html = html+"<li>"+this+"</li>";
			});
			html = html +"</ul>";

			$dialog.dialog('open');
			$dialog.html($('#labelErreurs').val() + html);
		}
	}			
}

$(function() {
	checkMandatory.init();
	
	$('#telephoneReference').blur(function(){
		var tel = $('#telephoneReference').val();
		if(tel.length < 10)
			$('#telephoneReference').val("");
	})
});
</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key='TITRE_ANNONCES_REE'/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
<style>
	.customTD {
		padding-top: 5px;
		text-align: left;
		border: 1px black solid;
		border-color : black;
		background-color: #c4e0fc;
		
	}
	
	.largePadding{
		padding: 30px;	
	}
	
	.boldItalicText{
	    padding-top : 15px;
	 	font-style: italic;
	 	font-weight : bold;
	}
	
	.italicText{
	 	font-style: italic;
	 	padding-bottom:10px;
	}
	
	td{
		padding-left: 15px;	
	}
</style>

<input id="labelErreurs" type="hidden" value="<ct:FWLabel key="ERREURS_REE"/>"></input>
<input id="labelErreursTitre" type="hidden" value="<ct:FWLabel key="ERREURS_REE_TITRE"/>"></input>

<TR>
    <TD class="customTD largePadding" colspan="3">
    	<label for="typeAnnonce"><ct:FWLabel key="ANNONCES_A_GENERER" /></label>
    	<SELECT id="typeAnnonce" name="typeAnnonce" style="margin-left:60px;"  mandatory="true">
    		<OPTION value=""></OPTION>
			<OPTION value="annonce_registre" ><ct:FWLabel key="ANNONCE_REGISTRE_AFFILIES"/></OPTION>
			<OPTION value="annonce_registre_revenus" ><ct:FWLabel key="ANNONCE_REGISTRE_AFFILIES_REVENUS"/></OPTION>
		</SELECT>
	</TD>
</TR>

<TR>
	<TD class="boldItalicText"><ct:FWLabel key="PARAMETRES_GENERAUX"/></TD>
</TR>

<TR> 
	<TD colspan="4"> 
		<hr size="3" width="100%">
	</TD>
</TR>

<TR>
	<TD class="italicText" width="59%"><ct:FWLabel key="INFORMATIONS_PERSONNE_REFERENCE"/></TD>
	<TD width="1%"></TD>
	<TD class="italicText" width="40%"><ct:FWLabel key="INFORMATIONS_SEDEX"/></TD>
</TR>

<TR>
	<TD class="customTD" width="59%">
		<table>
			<TR>
				<TD width="20%"><label for="nomReference"><ct:FWLabel key="NOM_REFERENCE"/></label></TD>
				<TD width="30%"><input id="nomReference" name="nomReference" mandatory="true" data-g-string='mandatory:true' value="<%= viewBean.getNomReference() %>"></TD>
				
				<TD width="20%"><label for="departementReference"><ct:FWLabel key="DEPARTEMENT_REFERENCE"/></label></TD>
				<TD width="30%"><input id="departementReference" name="departementReference"  mandatory="false" value="<%= viewBean.getDepartementReference() %>" ></TD>
			</TR>
			<TR>
				<TD width="20%"><label for="telephoneReference"><ct:FWLabel key="TELEPHONE_REFERENCE"/></label></TD>
				<TD width="30%"><input id="telephoneReference"  name="telephoneReference"  mandatory="true" data-g-integer='sizeMax:20, mandatory:true' value="<%= viewBean.getTelephoneReference() %>"></TD>
				
				<TD width="20%"><label for="infoReferences"><ct:FWLabel key="INFO_COMPLEMENTAIRE_REFERENCE"/></label></TD>
				<TD width="30%"><input id="infoReferences" name="infoReferences"  mandatory="false" value="<%= viewBean.getInfoReferences() %>" ></TD>
			</TR>
			<TR>
				<TD width="20%"><label for="emailReference"><ct:FWLabel key="EMAIL_REFERENCE"/></label></TD>
				<TD width="30%"><input id="emailReference" name="emailReference" data-g-email='mandatory:true' mandatory="true" value="<%= viewBean.getEmailReference() %>"></TD>
			</TR>
		</table>
	</TD>
	<TD width="1%"></TD>
	<TD class="customTD" width="40%">
		<table>
			<TR>
				<TD width="60%"><label for="nbAnnonces"><ct:FWLabel key="NOMBRE_ANNONCES_MESSAGE"/></label></TD>
				<TD width="40%"><input id="nbAnnonces" name="nbAnnonces"  mandatory="true" data-g-integer='mandatory:true' value="<%= viewBean.getNbAnnonces() %>"></TD>
			</TR>
			<TR>
				<TD width="60%"><label for="isValidationUnitaire"><ct:FWLabel key="VALIDATION_UNITAIRE"/></label></TD>
				<TD width="40%"><input type="checkbox" id="isValidationUnitaire"  name="isValidationUnitaire"  mandatory="false" <%= viewBean.checkIfChecked() %></TD>
				
			</TR>
			<TR>
				<TD width="60%"><label for="destinataire"><ct:FWLabel key="DESTINATAIRE"/></label></TD>
				<TD width="40%"><input id="destinataire" name="destinataire"  mandatory="true" data-g-string='mandatory:true' value="<%= viewBean.getDestinataire() %>"></TD>
			</TR>
		</table>
	</TD>
</TR>
<TR> 
	<TD>&nbsp;</TD>
</TR>

<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>