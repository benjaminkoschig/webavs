<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="globaz.al.vb.prestation.ALAttestationVersementViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>

<%
ALAttestationVersementViewBean viewBean = (ALAttestationVersementViewBean) session.getAttribute("viewBean"); 
	selectedIdValue = viewBean.getId();
	btnUpdLabel = objSession.getLabel("MODIFIER");
	btnDelLabel = objSession.getLabel("SUPPRIMER");
	btnValLabel = objSession.getLabel("VALIDER");
	btnCanLabel = objSession.getLabel("ANNULER");
	btnNewLabel = objSession.getLabel("NOUVEAU");
	//désactive les boutons new depuis et delete cet écran
	bButtonNew = false;
	bButtonDelete = false;
	bButtonValidate =  objSession.hasRight(userActionUpd, FWSecureConstants.UPDATE);
	
	idEcran="AL0027";

%>
<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>

<%@page import="ch.globaz.al.business.constantes.ALConstPrestations"%>
<%@page import="globaz.al.vb.prestation.ALAttestationVersementViewBean"%><script type="text/javascript">


function add() {	
}

function upd() { 
}

function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="al.prestation.attestationVersement.ajouter";
    else 
        document.forms[0].elements('userAction').value="al.prestation.attestationVersement.modifier";
    return state;
}

function cancel() {
	var methodElement = document.forms[0].elements('_method');
	action(methodElement.value);
	if(methodElement.value == ADD) {
		document.forms[0].elements('userAction').value="al.prestation.attestationVersement.afficher";
	} else {
		
        document.forms[0].elements('userAction').value="al.prestation.attestationVersement.chercher";
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
				<ct:FWLabel key="AL0027_TITRE" />
			<%-- /tpl:insert --%>
			
<%@ include file="/theme/detail/bodyStart2.jspf" %>

	<tr>
			<td><%-- tpl:insert attribute="zoneMain" --%>
				<table id="AL0027zone" class="zone">
				
					<tr>
	              	 		<td> <ct:FWLabel key="AL0027_E_MAIL"/>
							</td>
							<td>
							<input tabindex="1"  type="text"  name=email  value="<%= viewBean.getEmail() != null ? viewBean.getEmail() : "" %>"/>
	                		</td>
	                		
						</tr>
					
						
					<tr>
						<td> <ct:FWLabel key="AL0027_AFFILIE"/></td>
						<td>
	                			<input type="text" name="numAffilie" tabindex="2"  value=""/>
	                	</td> 
					</tr> 
					<tr>
						<td> <ct:FWLabel key="AL0027_ALLOCATAIRE"/></td>
						<td>
	                			<input type="text" name="nssAllocataire" tabindex="3"  value=""/>
	                	</td> 
					</tr> 
					<tr>
						<td> <ct:FWLabel key="AL0027_DATE_DE"/></td>
						<td>
	                		<input tabindex="4" class="clearable" type="text"
							name="dateDebut" value=""
							data-g-calendar="mandatory:false" />	
	                	</td>
	                	<td> <ct:FWLabel key="AL0027_DATE_A"/></td>
						<td>
	                		<input tabindex="5" class="clearable" type="text"
							name="dateFin" value=""
							data-g-calendar="mandatory:false" />		
	                	</td>  
					</tr> 
						<tr>
						 <td>  		 
						 <ct:FWLabel key="AL0027_VERSEMENTS_INDIRECTS"/>
						 </td>
						 <td>  	               	
	                    	<input type="checkbox" checked="checked" name="avecVersementIndirect" tabindex="6" />            		
	                 	</td> 
					</tr> 
					<tr>
						 <td>  		 
						 <ct:FWLabel key="AL0027_VERSEMENTS_DIRECTS"/>  
						</td>
						<td>             	
	                    	<input type="checkbox" checked="checked" name="avecVersementDirec" tabindex="6" />            		
	                 	</td> 
					</tr> 
						
					<tr>
						 <td>  		 
						 <ct:FWLabel key="AL0027_VERSEMENTS_TIERS_BENEF"/>  
						 </td>
						 <td>             	
	                    	<input type="checkbox" checked="checked" name="avecVersementTiersBeneficiaire" tabindex="8" />            		
	                 	</td> 
					</tr> 
						
					<tr>
						 <td>  		 
						 <ct:FWLabel key="AL0027_DETAILLE_PERIODE"/> </td>            	
	                    <td>	<input type="checkbox" checked="checked" name="documentDetaillePeriode" tabindex="9"/>            		
	                 	</td> 
					</tr> 
					<tr>
						 <td>  		 
						 <ct:FWLabel key="AL0027_DETAILLE_ALLOCATAIRE"/>   
						 </td> 
						 <td><input type="checkbox" checked="checked" name="documentDetailleParAllocataire" tabindex="10"/></td> 
					</tr> 
				</table>
		</td>
	</tr>

<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<ct:menuChange displayId="menu" menuId="menuWEBAF" showTab="menu"/>	
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>
	
