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
	
	idEcran="AL0022";

%>
<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>

<%@page import="ch.globaz.al.business.constantes.ALConstPrestations"%>
<%@page import="ch.globaz.al.business.constantes.ALConstDeclarationVersement"%>
<%@page import="globaz.al.vb.prestation.ALDeclarationVersementViewBean"%><script type="text/javascript">


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
				<table id="AL0022dateZone" class="zone" >
						<tr>
							<td class="label"> <ct:FWLabel key="AL0022_IMPRESSION_DATE"/></td>
							<td>
								<ct:FWCalendarTag name="dateImpression" tabindex="1" 
									displayType ="dayMonth" 
									value="" />
							</td>
						</tr>
					</table>
					<hr/>
						<table id="AL0022typeSource" class="zone">
						<tr>
							<td> <input type="radio"  name="typeDeclarationVersement" value="<%=ALConstDeclarationVersement.DECLA_VERSE_IMP_SOURCE %>"/> </td>
								<td><ct:FWLabel key="AL0022_SELECTION_SOURCE"/></td>
						</tr>

						<tr>
							<td class="label"> <ct:FWLabel key="AL0022_IMPRESSION_DATE_MONTH_YEAR"/></td>
						<td>
								<ct:FWCalendarTag name="periodePrestationImposeSource" tabindex="2" 
									displayType ="month" 
									value="" />
							</td>
						</tr>
					</table>
					<hr/>
					<table id="AL0022typeNonSource" class="zone">
						<tr>
							<td> <input type="radio"  name="typeDeclarationVersement" value="<%=ALConstDeclarationVersement.DECLA_VERSE_ATTEST_IMPOT %>"/> </td>
								<td><ct:FWLabel key="AL0022_SELECTION_NON_SOURCE"/></td>
						</tr>
						<tr>
							<td  class="label"> <ct:FWLabel key="AL0022_IMPRESSION_DATE_YEAR"/></td>
							<td><input type="text" name="periodePrestationNonImposeSource" tabindex="3"  value=""/>
							</td>
						</tr>
					</table>
					<hr/>
		
						<table id="AL0022typeFrontalier" class="zone">
						<tr>
							<td> <input type="radio"  name="typeDeclarationVersement" value="<%=ALConstDeclarationVersement.DECLA_VERSE_FRONTALIER %>"/> </td>
								<td><ct:FWLabel key="AL0022_SELECTION_FRONTALIER"/></td>
						</tr>

						<tr>
							<td  class="label"> <ct:FWLabel key="AL0022_IMPRESSION_DATE_YEAR"/></td>
							<td><input type="text" name="periodePrestationFontaliers" tabindex="4"  value=""/>
							</td>
						</tr>
					</table>
					<hr/>
					
						<table id="AL0022typeNonActifs" class="zone">
							<tr>
							<td> <input type="radio"  name="typeDeclarationVersement" value="<%=ALConstDeclarationVersement.DECLA_VERSE_NON_ACTIF %>"/> </td>
								<td><ct:FWLabel key="AL0022_SELECTION_NON_ACTIFS"/></td>
						</tr>
						
					<tr>
							<td  class="label"> <ct:FWLabel key="AL0022_IMPRESSION_DATE_YEAR"/></td>
							<td><input type="text" name="periodePrestationNonActifs" tabindex="5"  value=""/>
							
							</td>
						</tr>
					</table>
					<hr/>
			
					
						<table id="AL0022typePonctuelle" class="zone">
						
						<tr>
							<td> <input type="radio"  name="typeDeclarationVersement" value="<%=ALConstDeclarationVersement.DECLA_VERSE_PONCTUELLE %>"/> </td>
								<td><ct:FWLabel key="AL0022_SELECTION_PONCTUEL"/></td>
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
							<td><input type="text" name="idDossier" tabindex="8"  value=""/>
							</td>
							<td> <input type="radio"  name="typeDocument" value="<%=ALConstDeclarationVersement.DECLA_TYPE_DOC_DET %>"/> </td>
								<td><ct:FWLabel key="AL0022_SELECTION_DOC_DET"/></td>
						</tr>
						<tr>
							<td  class="label"> <ct:FWLabel key="AL0022_SELECTION_NUM_AFFILIE"/></td>
							<td><input type="text" name="numAffilie" tabindex="9"  value=""/>
							</td>
								<td> <input type="radio"  name="typeDocument" value="<%=ALConstDeclarationVersement.DECLA_TYPE_DOC_GLOB %>"/> </td>
								<td><ct:FWLabel key="AL0022_SELECTION_DOC_GLOB"/></td>
						</tr>
						
					

					</table>
	
		
					<hr/>
				
		
				</td>
		</tr>








<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<ct:menuChange displayId="menu" menuId="menuWEBAF" showTab="menu"/>	
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>
