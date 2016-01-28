
<%@page import="globaz.al.vb.adi.ALSaisieAdiViewBean"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="java.util.HashMap"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>
<%
	ALSaisieAdiViewBean viewBean = (ALSaisieAdiViewBean) session.getAttribute("viewBean"); 
	selectedIdValue = viewBean.getId();
	btnUpdLabel = objSession.getLabel("MODIFIER");
	btnDelLabel = objSession.getLabel("SUPPRIMER");
	btnValLabel = objSession.getLabel("VALIDER");
	btnCanLabel = objSession.getLabel("ANNULER");
	btnNewLabel = objSession.getLabel("NOUVEAU");
	//désactive les boutons new depuis et delete cet écran
	bButtonNew = false;
	bButtonDelete = false;
	
	idEcran="AL0025";

%>
<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>


<%@page import="ch.globaz.al.business.models.prestation.DetailPrestationComplexModel"%>
<%@page import="globaz.jade.client.util.JadeNumericUtil"%><script type="text/javascript">
function add() {
	document.forms[0].elements('userAction').value="al.adi.saisieAdi.ajouter";
}
function upd() {
    document.forms[0].elements('userAction').value="al.adi.saisieAdi.modifier";
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="al.adi.saisieAdi.ajouter";
    else 
        document.forms[0].elements('userAction').value="al.adi.saisieAdi.modifier";
    return state;
}

function cancel() {
	var methodElement = document.forms[0].elements('_method');
	action(methodElement.value);
	if(methodElement.value == ADD) {
		document.forms[0].elements('userAction').value="al.adi.saisieAdi.afficher";
	} else {
        document.forms[0].elements('userAction').value="al.adi.saisieAdi.chercher";
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
				<ct:FWLabel key="AL0025_TITRE"/><%=viewBean.getDecompteModel().getIdDossier() %>
			<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
			<tr>
			<td><%-- tpl:insert attribute="zoneMain" --%>
				<table class="zone" id="AL0025infoZone">
					<%
					String previousIdEnfant="0";
					String choixEnfant = "<option value=\'0\'></option>";
					for(int i=0;i<viewBean.getPrestationComplexSearchModel().getSize();i++){
					
						DetailPrestationComplexModel currentResult  = (DetailPrestationComplexModel)viewBean.getPrestationComplexSearchModel().getSearchResults()[i];
						String currentIdEnfant = currentResult.getDroitComplexModel().getEnfantComplexModel().getId();
						//on affiche les données enfant que une fois par enfant et pas une fois par ligne détail prestation
						if(!currentIdEnfant.equals(previousIdEnfant)){
							
							//Construction de la liste du choix enfant, pendant qu'on traite enfant par enfant
							String selected="";
							if(currentIdEnfant.equals(viewBean.getAdiSaisieComplexModel().getAdiSaisieModel().getIdEnfant()))
									selected="selected='selected'";
							choixEnfant += "<option value='"+currentIdEnfant+"' "+selected+">";
							choixEnfant +=((DetailPrestationComplexModel)(viewBean.getPrestationComplexSearchModel().getSearchResults()[i]))
											.getDroitComplexModel().getEnfantComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation1();
							choixEnfant += " "+((DetailPrestationComplexModel)(viewBean.getPrestationComplexSearchModel().getSearchResults()[i]))
							.getDroitComplexModel().getEnfantComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation2();
							choixEnfant += "</option>";
							//on ferme le précédent quand on commence un nouveau enfant, sauf si c'est le premier
							if(!"0".equals(previousIdEnfant)){
								%></td></tr>
								<tr height="10px;"><td style="border-bottom:1px solid black;" colspan="4"></td></tr>
								<%
							}
						%>
						<!--  Données enfant (entête nom / date) -->		
						<tr>
							<td class="label"><ct:FWLabel key="AL0025_PRESTATION_NOM_ENFANT"/></td>
							<td><input type="text" value='<%=((DetailPrestationComplexModel)(viewBean.getPrestationComplexSearchModel().getSearchResults()[i]))
									.getDroitComplexModel().getEnfantComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation1()+" "+
									((DetailPrestationComplexModel)(viewBean.getPrestationComplexSearchModel().getSearchResults()[i]))
									.getDroitComplexModel().getEnfantComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation2() %>'
									class="readOnly long"/>		
							</td>
							
							<td class="label"><ct:FWLabel key="AL0025_PRESTATION_DATE_ENFANT"/></td>	
							<td><input type="text" value="<%=((DetailPrestationComplexModel)(viewBean.getPrestationComplexSearchModel().getSearchResults()[i]))
									.getDroitComplexModel().getEnfantComplexModel().getPersonneEtendueComplexModel().getPersonne().getDateNaissance() %>"
									class="readOnly date"/>
							</td>
							<td></td>
							<td></td>
						</tr>
						
						<tr>
							<td colspan="6">
								<div class="adiYear"><%=currentResult.getEntetePrestationModel().getPeriodeA().substring(3) %></div>
							<%
							
						}//fin if affichage données enfant
						%>	
							
							<!--  Données prestations enfant (tableau), sur chaque détail prestation on affiche si on doit saisir -->			
							
							<%
							//on parcourt les 12 mois de l'année voir si il sont dans la liste à saisir de l'enfant

							HashMap enfantMap = (HashMap)viewBean.getListeASaisir().get(currentIdEnfant);
							Integer monthValue = (Integer)enfantMap.get(new Integer(currentResult.getDetailPrestationModel().getPeriodeValidite().substring(0,2)));
							if(monthValue.compareTo(new Integer(-1))==0){
						 		%><div class="adiMonthDisabled"><%=currentResult.getDetailPrestationModel().getPeriodeValidite().substring(0,2)%></div><%
							}
							if(monthValue.compareTo(new Integer(0))==0){
						 		%><div class="adiMonthUnfilled"><%=currentResult.getDetailPrestationModel().getPeriodeValidite().substring(0,2)%></div><%
							}
							if(monthValue.compareTo(new Integer(1))==0){
						 		%><div class="adiMonthFilled"><%=currentResult.getDetailPrestationModel().getPeriodeValidite().substring(0,2)%></div><%
							}
						 	
							previousIdEnfant = currentIdEnfant;
							
							
							
					} //fin for
					%>
									
				</table>

				<table class="al_list" id="saisies">
	              	<tr>
	                  <th scope="col" style="width:5%;"></th>
	                  <th scope="col" style="width:15%;"><ct:FWLabel key="AL0025_SAISIE_ENTETE_PERIODE"/></th>
	                  <th scope="col" style="width:25%;"><ct:FWLabel key="AL0025_SAISIE_ENTETE_MONTANT"/></th>
	                  <th scope="col" style="width:25%;"><ct:FWLabel key="AL0025_SAISIE_ENTETE_ENFANT"/></th>   
	                </tr>
	            <%
	            String rowStyle = "";
        		
                int nbSaisies = viewBean.getSaisieComplexSearchModel().getSize();
                int cpt = 0;
             
                for (int i=0; i < nbSaisies ; i++) {
                	cpt++;
                %>  
                	<tr class="<%=(cpt % 2 == 0)?"odd":"nonodd"%>">
                		<td><a title="Supprimer saisie" class="deleteLink" href="<%=servletContext + mainServletPath + "?userAction=al.adi.saisieAdi.supprimerSaisie&id="+viewBean.getSaisieHistoriqueAt(i).getAdiSaisieModel().getIdSaisieAdi()%>"/></td>
                		<td><input type="text" class="readOnly date" value="<%=viewBean.getSaisieHistoriqueAt(i).getAdiSaisieModel().getPeriodeDe()%>" readonly="readonly" disabled="disabled" /> - <input type="text" class="readOnly date" value="<%=viewBean.getSaisieHistoriqueAt(i).getAdiSaisieModel().getPeriodeA()%>" readonly="readonly" disabled="disabled"/></td>
                		<td><input type="text" class="readOnly montant" value="<%=viewBean.getSaisieHistoriqueAt(i).getAdiSaisieModel().getMontantSaisi()%>" readonly="readonly" disabled="disabled"/></td>
                		<td><input type="text" class="readOnly" value="<%=viewBean.getSaisieHistoriqueAt(i).getEnfantComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation2()%>" readonly="readonly" disabled="disabled"/></td>
                	</tr>
  	
                <%
                }
				%>
					
					<tr class="<%=((cpt+1) % 2 == 0)?"odd":"nonodd"%>">
					 	<td><ct:inputHidden name="adiSaisieComplexModel.adiSaisieModel.idDecompte"/></td>
                		<td><ct:inputText styleClass="date" name="adiSaisieComplexModel.adiSaisieModel.periodeDe"/> - <ct:inputText styleClass="date" name="adiSaisieComplexModel.adiSaisieModel.periodeA"/></td>
                		<td><ct:inputText styleClass="montant" name="adiSaisieComplexModel.adiSaisieModel.montantSaisi"/></td>
                		<td><select name="adiSaisieComplexModel.adiSaisieModel.idEnfant" ><%=choixEnfant%></select></td>
                	</tr>
					 
				</table>
			<%-- /tpl:insert --%>
			</td>
				
			</tr>								
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<ct:menuChange displayId="options" menuId="saisieAdi-detail" showTab="options" checkAdd="no">
	<ct:menuSetAllParams key="selectedId" checkAdd="no" value="<%=viewBean.getDecompteModel().getIdDossier()%>" />
</ct:menuChange>		
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>