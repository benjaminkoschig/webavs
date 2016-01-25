<%@page import="globaz.framework.secure.user.FWSecureGroupUser"%>
<%@page import="globaz.framework.secure.FWSecureRoleGroupController"%>
<%@page import="globaz.framework.secure.FWSecureRoleGroups"%>
<%@page import="globaz.framework.secure.FWSecureRoleGroup"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="globaz.al.vb.traitement.ALRecapImpressionViewBean"%>
<%@page import="java.lang.Integer"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>

<%-- tpl:insert attribute="zoneInit" --%>
<%
	
	ALProcessusGestionViewBean viewBean = (ALProcessusGestionViewBean) session.getAttribute("viewBean"); 
	selectedIdValue = viewBean.getId();
	btnUpdLabel = objSession.getLabel("MODIFIER");
	btnDelLabel = objSession.getLabel("SUPPRIMER");
	btnValLabel = objSession.getLabel("VALIDER");
	btnCanLabel = objSession.getLabel("ANNULER");
	btnNewLabel = objSession.getLabel("NOUVEAU");
	//désactive les boutons new depuis et delete cet écran
	bButtonNew = false;
	bButtonDelete = false;
	bButtonCancel = false;
	
	idEcran="AL0050";
	
	boolean userReadOnly = (objSession.hasRight("al", globaz.framework.secure.FWSecureConstants.ADD) || objSession.hasRight("al", globaz.framework.secure.FWSecureConstants.UPDATE))?false:true;
	
		
		

%>
<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>


<%@page import="ch.globaz.al.business.constantes.ALCSProcessus"%>
<%@page import="globaz.fweb.util.JavascriptEncoder"%>
<%@page import="ch.globaz.al.business.models.processus.ProcessusPeriodiqueModel"%>
<link rel="stylesheet"  type="text/css" href="<%=servletContext%>/theme/jquery.treeTable.css" />
<script type="text/javascript" src="<%=servletContext%>/scripts/treeview/jquery.treeTable.js"></script>


<%@page import="ch.globaz.al.business.constantes.ALConstEcheances"%>
<%@page import="globaz.al.vb.traitement.ALProcessusGestionViewBean"%>
<%@page import="ch.globaz.al.business.models.processus.TemplateTraitementListComplexSearchModel"%>
<%@page import="ch.globaz.al.business.models.processus.TemplateTraitementListComplexModel" %>
<%@page import="ch.globaz.al.business.models.processus.TraitementPeriodiqueModel" %>

<script type="text/javascript">

$(document).ready(function()  {
	  $("#AL0050traitementListe").treeTable({
	      expandable: true,
	      initialState:"collapsed"
	    });
	
	  //$("tr.expanded td > span.expander").click();

	  $("tr.open.collapsed td > span.expander").each(function(index) {
		    $(this).click();
		    //"recursivité" 1 niveau
		    $("tr.open.collapsed td > span.expander").each(function(index) {
		    	$(this).click();
			});
		    
	  });

	 
	 
 
 });

function add() {
	

}
function upd() {
    //document.forms[0].elements('userAction').value="al.traitement.processus.modifier";
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="al.traitement.processusGestion.ajouter";
    else 
        document.forms[0].elements('userAction').value="al.traitement.processusGestion.modifier";
    return state;
}

function cancel() {
	/*var methodElement = document.forms[0].elements('_method');
	action(methodElement.value);
	if(methodElement.value == ADD) {
		document.forms[0].elements('userAction').value="al.traitement.recapImpression.afficher";
	} else {
		
        document.forms[0].elements('userAction').value="al.dossier.dossier.chercher";
	}*/
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
				<ct:FWLabel key="AL0050_TITRE" />
			<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
		<tr>
			<td>
				<%-- tpl:insert attribute="zoneMain" --%>
				
				<table>
					<tr>
					<% if (!userReadOnly){%>
					
					<td><ct:FWLabel key="AL0050_PROCESSUS_NEW_PERIODE" /></td>
					<td><ct:FWCalendarTag tabindex="1" name="newPeriode"
					displayType="month" value=""/></td>
					</tr>
					<%}else{%>
						<td></td>
					
					<%}%>
				
				</table>
				<%		
					String previousYear = 	new Integer(Integer.parseInt(viewBean.getYearDisplay())-1).toString();
					String nextYear = 	new Integer(Integer.parseInt(viewBean.getYearDisplay())+1).toString();
				%>
				<div id="AL0050Pagination">
					<a href="<%=servletContext + mainServletPath%>?userAction=al.traitement.processusGestion.afficher&yearDisplay=<%=previousYear %>" id="paginationPrevious" >Année <%=previousYear %></a>   
		        	| <%=viewBean.getYearDisplay() %> |		 	
		        	<a href="<%=servletContext + mainServletPath%>?userAction=al.traitement.processusGestion.afficher&yearDisplay=<%=nextYear %>" id="paginationNext" >Année <%=nextYear %></a>
				</div>
				<table id="AL0050traitementListe" class="zone">
					<thead>
					<tr>
						
						<th class='periodCol'><ct:FWLabel key="AL0050_PROCESSUS_ENTETE_PERIODE" /></th>
						<th><ct:FWLabel key="AL0050_PROCESSUS_ENTETE_NOUVEAU" /></th>
						<th><ct:FWLabel key="AL0050_PROCESSUS_ENTETE_NOM" /></th>
						<th><ct:FWLabel key="AL0050_PROCESSUS_ENTETE_ETAT" /></th>
						<th><ct:FWLabel key="AL0050_PROCESSUS_ENTETE_OPTION" /></th>
						<th><ct:FWLabel key="AL0050_PROCESSUS_ENTETE_TRAITEMENT" /></th>
						<th><ct:FWLabel key="AL0050_PROCESSUS_ENTETE_TRAITEMENT_ETAT" /></th>
						<th><ct:FWLabel key="AL0050_PROCESSUS_ENTETE_TRAITEMENT_DATE" /></th>
						<th><ct:FWLabel key="AL0050_PROCESSUS_ENTETE_TRAITEMENT_HEURE" /></th>
						<th><ct:FWLabel key="AL0050_PROCESSUS_ENTETE_TRAITEMENT_USER" /></th>
						<th><ct:FWLabel key="AL0050_PROCESSUS_ENTETE_TRAITEMENT_NO" /></th>
					</tr>
					</thead>
					<tbody>
					<%

					String linkTraitementBase = servletContext + mainServletPath + "?userAction=al.traitement.processusGestion.";	
					String linkProcessusDetail = servletContext + mainServletPath + "?userAction=al.traitement.processus.afficher";	
					int cptPeriod = 0;
					int cptProcessus = 0;
					int cptTraitement = 0;
					for(int i=0;i<viewBean.getSearchModel().getSize();i++){
						
							boolean isNewPeriodNode = false;
							boolean isNewProcessusNode = false;
							boolean isPartiel = false;
							
							TraitementPeriodiqueModel previousTraitement = null;
							String previousIdPeriod = null;
							String currentIdPeriod = null;
							
							//Récupèrer le traitement précédent dans la liste des résultats warn => ordre a changé, à voir 
							if(i>0){
								previousTraitement = ((TemplateTraitementListComplexModel)viewBean.getSearchModel().getSearchResults()[i-1]).getTraitementPeriodiqueModel();
								previousIdPeriod = ((TemplateTraitementListComplexModel)viewBean.getSearchModel().getSearchResults()[i-1]).getPeriodeAFModel().getIdPeriodeAF();
							}
							TraitementPeriodiqueModel currentTraitement = ((TemplateTraitementListComplexModel)viewBean.getSearchModel().getSearchResults()[i]).getTraitementPeriodiqueModel();
							currentIdPeriod = ((TemplateTraitementListComplexModel)viewBean.getSearchModel().getSearchResults()[i]).getPeriodeAFModel().getIdPeriodeAF();
							ProcessusPeriodiqueModel currentProcessus = ((TemplateTraitementListComplexModel)viewBean.getSearchModel().getSearchResults()[i]).getProcessusPeriodiqueModel();
							//Préparation du contenu HTML des lignes à afficher
						
							String lineTraitementContent = "<td colspan='4' class='empty'></td>";
							
							if (!userReadOnly) {						
								if(currentTraitement.getReadOnly() || (!ALCSProcessus.ETAT_ENCOURS.equals(currentTraitement.getEtat()) 
								&& !ALCSProcessus.ETAT_TERMINE.equals(currentTraitement.getEtat())&& !ALCSProcessus.ETAT_ATTENTE.equals(currentTraitement.getEtat()))){
								//lineTraitementContent+="<td></td>";
								
		
								lineTraitementContent+="<td><a onclick=\"return confirm('"
									+objSession.getLabel("MESSAGE_EXEC_TRAITEMENT")+"')\" href='"+linkTraitementBase+"executer&id="+currentTraitement.getIdTraitementPeriodique()+"&idProcessusPeriodique="+currentTraitement.getIdProcessusPeriodique()+"'>"+objSession.getLabel("LINK_EXEC_TRAITEMENT")+"</a></td>";

								}
								else{
									lineTraitementContent+="<td></td>";
								}
							}
							else{
								lineTraitementContent+="<td></td>";
							}
							lineTraitementContent = lineTraitementContent+"<td>"+objSession.getCodeLibelle(currentTraitement.getTraitementLibelle())+"</td>"
						    +"<td>"+objSession.getCodeLibelle(currentTraitement.getEtat())+"</td>"
						    +"<td>"+currentTraitement.getDateExecution()+"</td>"
						    +"<td>"+currentTraitement.getHeureExecution()+"</td>"
						    +"<td>"+currentTraitement.getUserExecution()+"</td>"
						    +"<td>"+currentTraitement.getIdTraitementPeriodique()+"</td>";
						   
							
						    String lineProcessusContent=null;
							if (!userReadOnly) {

							 lineProcessusContent = "<td></td><td><a title='"+objSession.getLabel("LINK_ADD_PROCESSUS_PARTIEL_DESC")+"'class='addLink' href='"
							+linkTraitementBase+"creerPartiel&idProcessusPeriodique="+currentTraitement.getIdProcessusPeriodique()+"' onclick=\"return confirm('"
									+objSession.getLabel("MESSAGE_ADD_PROCESSUS_PARTIEL")+"')\"/></td>"
							+"<td>"+objSession.getCodeLibelle(((TemplateTraitementListComplexModel)viewBean.getSearchModel().getSearchResults()[i]).getConfigProcessusModel().getBusinessProcessus())+" ("+currentProcessus.getIdProcessusPeriodique()+")</td>"
							+"<td>"+objSession.getCodeLibelle(currentProcessus.getEtat())+"</td>"
							+"<td colspan='7' class='empty'>&nbsp;</td>";}
							else{
								lineProcessusContent = "<td></td><td></td><td>"+objSession.getCodeLibelle(((TemplateTraitementListComplexModel)viewBean.getSearchModel().getSearchResults()[i]).getConfigProcessusModel().getBusinessProcessus())+" ("+currentProcessus.getIdProcessusPeriodique()+")"+"</td>"
								+"<td>"+objSession.getCodeLibelle(currentProcessus.getEtat())+"</td>"
								+"<td colspan='7' class='empty'>&nbsp;</td>";}
							
							
							
							String linePartielContent = "<td></td>";
							if (!userReadOnly) {
							
							
							if(!ALCSProcessus.ETAT_TERMINE.equals(((TemplateTraitementListComplexModel)viewBean.getSearchModel().getSearchResults()[i]).getProcessusPeriodiqueModel().getEtat())){
								linePartielContent+="<td><a title='"+objSession.getLabel("LINK_DEL_PROCESSUS_PARTIEL_DESC")+"' class='deleteLink' href='"
									+linkTraitementBase+"supprimerPartiel&idProcessusPeriodique="+currentTraitement.getIdProcessusPeriodique()+"' onclick=\"return confirm('"
									+objSession.getLabel("MESSAGE_DEL_PROCESSUS_PARTIEL")+"')\"/></td>";
							}
							else{
								linePartielContent +="<td></td>";
							}
							}else{
								linePartielContent = "<td></td><td></td>";
							}
							linePartielContent = linePartielContent+"<td>"+objSession.getCodeLibelle(((TemplateTraitementListComplexModel)viewBean.getSearchModel().getSearchResults()[i]).getConfigProcessusModel().getBusinessProcessus())+" ("+currentProcessus.getIdProcessusPeriodique()+")</td>"
							+"<td>"+objSession.getCodeLibelle(((TemplateTraitementListComplexModel)viewBean.getSearchModel().getSearchResults()[i]).getProcessusPeriodiqueModel().getEtat())+"</td>"	
							+"<td colspan='1' class='passage'><a  href='"
							+linkProcessusDetail+"&selectedId="+currentTraitement.getIdProcessusPeriodique()+"'>Détail</a></td>"
							+"<td class='passage'>Lié au journal : "+currentProcessus.getIdPassageFactu()+"</td>"
							+"<td colspan='5' class='empty'>&nbsp;</td>";
							
							String linePeriodContent = "<td>"+((TemplateTraitementListComplexModel)viewBean.getSearchModel().getSearchResults()[i]).getPeriodeAFModel().getDatePeriode()
							+"</td><td></td><td>"+((TemplateTraitementListComplexModel)viewBean.getSearchModel().getSearchResults()[i]).getConfigProcessusModel().getTemplate()+"</td><td colspan='8' class='empty'></td>";
							
							
							//setting des cpt et identification de la ligne
							cptTraitement++;
						
							if(previousTraitement==null || (previousTraitement != null && !currentTraitement.getIdProcessusPeriodique().equals(previousTraitement.getIdProcessusPeriodique()))){
								isNewProcessusNode = true;
								cptTraitement = 1;
								cptProcessus++;
	
								//nouveau processus, nouvelle période ?
								if(previousTraitement==null || !currentIdPeriod.equals(previousIdPeriod)){
									isNewPeriodNode = true;
									cptProcessus=  1;
									cptPeriod++;
								}
								
								if(((TemplateTraitementListComplexModel)viewBean.getSearchModel().getSearchResults()[i]).getProcessusPeriodiqueModel().getIsPartiel().booleanValue()){
									isPartiel = true;	
								}
							}
							boolean noOutput = false;
							
							//Affichage des lignes préparées en fonction de l'identification de la ligne	
							if(isNewPeriodNode){
								String classStr = "class='";
								
								//si c'est la première période à afficher ou que le processus est ouvert
								if(cptPeriod==1 || ALCSProcessus.ETAT_OUVERT.equals(currentProcessus.getEtat()))
									classStr = classStr.concat(" ").concat("open");
								else
									classStr = classStr.concat(" ").concat("close");
								classStr = classStr.concat("'");
								if(!noOutput)
								pageContext.getOut().write("<tr id='node-"+cptPeriod+"' "+classStr+">"+linePeriodContent+"</tr>");
							}
							
							if(isNewProcessusNode){
								String classStr = "class='child-of-node-"+cptPeriod;
							
								//si c'est la première période à afficher
								if(cptPeriod==1 || ALCSProcessus.ETAT_OUVERT.equals(currentProcessus.getEtat()))
									classStr = classStr.concat(" ").concat("open");
								else
									classStr = classStr.concat(" ").concat("close");
								classStr = classStr.concat("'");
								//test si on affiche le processus en cours en tant que partiel ou principal
								String lineProcessus = "";
								if(isPartiel)
									lineProcessus = linePartielContent;
								else
									lineProcessus = lineProcessusContent;
								if(!noOutput)
								pageContext.getOut().write("<tr id='node-"+cptPeriod+"-"+cptProcessus+"' "+classStr+">"+lineProcessus+"</tr>");
	
							}
							
							String classStr = "class='child-of-node-"+cptPeriod+"-"+cptProcessus;
							
							//si le traitement est ouvert 
							if(ALCSProcessus.ETAT_OUVERT.equals(currentTraitement.getEtat()))
								classStr = classStr.concat(" ").concat("open");
							else if(ALCSProcessus.ETAT_ERREUR.equals(currentTraitement.getEtat()))
								classStr = classStr.concat(" ").concat("error");
							else
								classStr = classStr.concat(" ").concat("close");
							classStr = classStr.concat("'");
							if(!noOutput)
							pageContext.getOut().write("<tr id='node-"+cptPeriod+"-"+cptProcessus+"-"+cptTraitement+"' "+classStr+">"+lineTraitementContent+"</tr>");	
							
						}
					
						%>
							
					</tbody>
				</table>
					
			</td>
		</tr>
	
		<%-- /tpl:insert --%>
	
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<ct:menuChange displayId="menu" menuId="menuWEBAF" showTab="menu"/>	
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>
