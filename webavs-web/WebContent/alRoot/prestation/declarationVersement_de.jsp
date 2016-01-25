<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="globaz.al.vb.prestation.ALDeclarationVersementViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>
<%
	ALDeclarationVersementViewBean viewBean = (ALDeclarationVersementViewBean) session.getAttribute("viewBean"); 
	selectedIdValue = viewBean.getId();
	btnUpdLabel = objSession.getLabel("MODIFIER");
	btnDelLabel = objSession.getLabel("SUPPRIMER");
	btnValLabel = objSession.getLabel("VALIDER");
	btnCanLabel = objSession.getLabel("ANNULER");
	btnNewLabel = objSession.getLabel("NOUVEAU");
	//désactive les boutons new depuis et delete cet écran
	bButtonNew = false;
	bButtonDelete = false;
	
	boolean hasCreateRight = objSession.hasRight("al.prestation.declarationVersement", FWSecureConstants.ADD);
	bButtonValidate = hasCreateRight;
	
	idEcran="AL0022";

%>
<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>

<%@page import="ch.globaz.al.business.constantes.ALConstPrestations"%>
<%@page import="ch.globaz.al.business.constantes.ALCSDeclarationVersement"%>
<%@page import="ch.globaz.al.business.constantes.ALConstDeclarationVersement"%>
<%@page import="globaz.al.vb.prestation.ALDeclarationVersementViewBean"%>

<script type="text/javascript">


$(document).ready(function() {
	// attribue une id à tous les champs ayant un nom mais pas encore d'id
	$('*',document.forms[0]).each(function(){
		if(this.name!=null && this.id==""){
			this.id=this.name+'Id';
		}
	});
	// initialisation
	if($('#idTypeDecla').val()==<%=ALCSDeclarationVersement.DECLA_VERS_DIR_IMP_SOURCE%>){
		$('#idContextDossier').show();
		$('#idLabelSelectDossier').show();
		$('#idLabelSelectAffilie').show();
		$('#idContextAffilie').show();
		$('#idLabelSelectGlobal').show();
	}
	// Choix des id à afficher ou non en fonction de la liste choisie
	$('#idTypeDecla').change(function(){

		if($(this).val()==<%=ALCSDeclarationVersement.DECLA_VERS_DEMANDE%>){
			$('#idContextDossier').show();
			$('#idLabelSelectDossier').show();
			$('#idLabelSelectAffilie').show();
			$('#idContextAffilie').show();
			$('#idLabelSelectGlobal').hide();		
		}else{
			$('#idContextDossier').show();
			$('#idLabelSelectDossier').show();
			$('#idLabelSelectAffilie').show();
			$('#idContextAffilie').show();
			$('#idLabelSelectGlobal').show();
		}
	
	});
		
		
});

function add() {	
}

function upd() { 
}

function validate() {
    state = validateFields();
    
    
    
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="al.prestation.declarationVersement.ajouter";
    else 
        document.forms[0].elements('userAction').value="al.prestation.declarationVersement.modifier";
    return state;
}

function cancel() {
	var methodElement = document.forms[0].elements('_method');
	action(methodElement.value);
	if(methodElement.value == ADD) {
		document.forms[0].elements('userAction').value="al.prestation.declarationVersement.afficher";
	} else {
		
        document.forms[0].elements('userAction').value="al.prestation.declarationVersement.chercher";
	}
}

function del() {	
}

function init(){
}

function postInit(){
}

</script>


<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:insert attribute="zoneTitle" --%>
				<ct:FWLabel key="AL0022_TITRE" />
			<%-- /tpl:insert --%>
			
<%@ include file="/theme/detail/bodyStart2.jspf" %>
	
				
		<tr>		
				<td>
						<table  id="AL0022typePonctuelle" class="zone">
						
						<tr>
							<td class="label"> <ct:FWLabel key="AL0022_IMPRESSION_DATE"/></td>
							<td>
								<ct:FWCalendarTag name="dateImpression" tabindex="1" 
									displayType ="dayMonth" 
									value="" />
								
							</td>
						</tr>
						<tr>
							<td class="label"> <ct:FWLabel key="AL0022_LISTE_TYPE_DECLARATION"/></td>
							
							<td colspan=3>  <ct:select id="idTypeDecla" name="typeDeclarationVersement" tabindex="2" wantBlank="false">	
								<ct:optionsCodesSystems csFamille="ALDECLTYPE" />		
								</ct:select>
							</td>
						</tr>	
						
						<tr>
							<td class="label"> <ct:FWLabel key="AL0022_IMPRESSION_DATE_DEBUT"/></td>
							<td>
								<ct:FWCalendarTag name="periodeDebut" tabindex="6" 
									displayType ="dayMonth" 
									value="" />
							</td>
								<td class="label"> <ct:FWLabel key="AL0022_IMPRESSION_DATE_FIN"/></td>
							<td>
								<ct:FWCalendarTag name="periodeFin" tabindex="7" 
									displayType ="dayMonth" 
									value="" />
							</td>
							
						</tr>
						
							<tr>
							<td  class="label"> <ct:FWLabel key="AL0022_SELECTION_ID_DOSSIER"/></td>
							<td><input type="text"  name="idDossier" tabindex="8"  value=""/>
							</td>
							<td> <input type="radio"  name="typeDocument" value="<%=ALConstDeclarationVersement.DECLA_TYPE_DOC_DET%>" checked="<%=ALConstDeclarationVersement.DECLA_TYPE_DOC_DET%>" />
								<ct:FWLabel key="AL0022_SELECTION_DOC_DET"/></td>
						</tr>
						<tr>
							<td  class="label"> <ct:FWLabel key="AL0022_SELECTION_NUM_AFFILIE"/></td>
							<td  ><input type="text"  name="numAffilie" tabindex="9"  value=""/>
							</td>
								<td id="idLabelSelectGlobal"> <input type="radio"  name="typeDocument" value="<%=ALConstDeclarationVersement.DECLA_TYPE_DOC_GLOB %>"/> 
									<ct:FWLabel key="AL0022_SELECTION_DOC_GLOB"/></td>
						</tr>
						<tr>
						<td  class="label"/>
						<td  class="label"/>
					
						 <td id="idLabelBoxTextImpot">  		               	
	                    	<input type="checkbox" checked="checked" name="texteImpot" />
	                 		<ct:FWLabel key="AL0022_TEXTE_IMPOT"/>             		
	                 	</td> 
						</tr>
					
					</table>
					<td>
				
		
		</tr>








<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<ct:menuChange displayId="menu" menuId="menuWEBAF" showTab="menu"/>	
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>