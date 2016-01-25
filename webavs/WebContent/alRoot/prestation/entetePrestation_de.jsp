<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="ch.globaz.al.business.constantes.*"%>
<%@page import="globaz.al.vb.prestation.ALEntetePrestationViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%@ include file="/theme/detail/header.jspf" %>


<%@page import="ch.globaz.al.business.models.prestation.DetailPrestationComplexModel"%>
<%-- tpl:insert attribute="zoneInit" --%>
<%
	ALEntetePrestationViewBean viewBean = (ALEntetePrestationViewBean) session.getAttribute("viewBean"); 
	selectedIdValue=viewBean.getId();
	btnUpdLabel = objSession.getLabel("MODIFIER");
	btnDelLabel = objSession.getLabel("SUPPRIMER");
	btnValLabel = objSession.getLabel("VALIDER");
	btnCanLabel = objSession.getLabel("ANNULER");
	btnNewLabel = objSession.getLabel("NOUVEAU");
	
	
	bButtonNew = false;
	bButtonUpdate = false;
	bButtonDelete = false;
	
	idEcran="AL0007";

%>
<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>
<%@page import="globaz.fweb.util.JavascriptEncoder"%>

<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.jade.client.util.JadeNumericUtil"%>
<%@page import="globaz.helios.translation.CodeSystem"%>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<script type="text/javascript" src="<%=servletContext%>/alRoot/util_webaf.js"></script>
<script type="text/javascript" src="<%=servletContext%>/alRoot/ajax_webaf.js"></script>

<script type="text/javascript" language="Javascript">

function add() {
   
}
function upd() {
  
}
function validate() {
    state = validateFields();

    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="al.prestation.entetePrestation.ajouter";
    else 
        document.forms[0].elements('userAction').value="alprestation.entetePrestation.modifier";
    return state;
}

function cancel() {
	var methodElement = document.forms[0].elements('_method');
	action(methodElement.value);
	if(methodElement.value == ADD) {
		document.forms[0].elements('selectedId').value='<%=JavascriptEncoder.getInstance().encode(viewBean.getEntetePrestationModel().getIdDossier()) %>';
		document.forms[0].elements('userAction').value="al.dossier.dossierMain.afficher";
	} else {
        document.forms[0].elements('userAction').value="al.droit.droit.afficher";
	}
}

function del() {
	var msgDelete = '<%=JavascriptEncoder.getInstance().encode(objSession.getLabel("MESSAGE_SUPPRESSION"))%>';
    if (window.confirm(msgDelete)){
        document.forms[0].elements('userAction').value="al.prestation.entetePrestation.supprimer";
        document.forms[0].submit();
    }
}

function init(){
	initDebugManager();
	initMoreInfosElements('detailPrest','More',<%=JavascriptEncoder.getInstance().encode(viewBean.getDetailPrestationComplexSearchModel().getSize()+"") %>);
}

function postInit(){
}

//définir cette méthode si traitement après remplissage ajax du formulaire
function callbackFillInputAjax(){
}


</script>

<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:insert attribute="zoneTitle" --%>
			<span style="float:left;text-align:left;">
			<%=viewBean.isRecapVerrouillee()?"<img src='images/cadenas.gif'/>":"<img src='images/cadenas_ouvert.gif'/>" %>
			</span>	
			(<ct:FWLabel key="AL0007_TITRE"/>
			<%=viewBean.getEntetePrestationModel().getId() %> - <%=viewBean.getDossierComplexModel().getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation1() %>
			<%=viewBean.getDossierComplexModel().getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation2()%>)
			<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<tr><td>
		
	<%-- tpl:insert attribute="zoneMain" --%>
    	<table id="AL0007Entete" class="zone">
			<tr>
				<td class="label subtitle" colspan="6"><ct:FWLabel key="AL0007_TITRE_ENTETE"/></td>
			</tr>
			<tr>
				<td><ct:FWLabel key="AL0007_PERIODE_DEBUT"/></td>
				<td><ct:inputText name="entetePrestationModel.periodeDe" readonly="readonly" styleClass="readOnly medium" disabled="true"/></td>
				<td><ct:inputText name="entetePrestationModel.periodeA" readonly="readonly" styleClass="readOnly medium" disabled="true"/></td>
				<td><ct:FWLabel key="AL0007_VERSEMENT"/></td>
				<td><ct:inputText name="entetePrestationModel.dateVersComp" readonly="readonly" styleClass="readOnly medium" disabled="true"/></td>
			</tr>
    	</table>
    	 	
    	<table class="al_list">             
            <tr>
	             <th scope="col"><ct:FWLabel key="AL0007_LIST_ENTETE_MOIS"/></th>
	             <th scope="col"><ct:FWLabel key="AL0007_LIST_ENTETE_NOM"/></th>
	             <th scope="col"><ct:FWLabel key="AL0007_LIST_ENTETE_PRENOM"/></th>
	             <th scope="col"><ct:FWLabel key="AL0007_LIST_ENTETE_ALLOCATION"/></th>
	             <th scope="col"><ct:FWLabel key="AL0007_LIST_ENTETE_TARIF"/></th>
	             <th scope="col"><ct:FWLabel key="AL0007_LIST_ENTETE_MONTANT"/></th>    
            </tr>
	
    	<% 	
    	  		int cptDetailPrestDisplay = 0;
    			String rowStyle="";
                for(int i=0;i<viewBean.getDetailPrestationComplexSearchModel().getSize();i++){
                
                	DetailPrestationComplexModel currentDetailPrestation = (DetailPrestationComplexModel)viewBean.getDetailPrestationComplexSearchModel().getSearchResults()[i];
                	if(!(cptDetailPrestDisplay % 2 == 0))
                		rowStyle="odd";
                	else
                		rowStyle="nonodd";
	
                	%>
                	<tr class="<%=rowStyle%>" id='<%="detailPrest"+i%>' onMouseOver="jscss('swap', this, '<%=rowStyle%>', 'highlighted')" onMouseOut="jscss('swap', this, 'highlighted', '<%=rowStyle%>')">          
		                <td><%=currentDetailPrestation.getDetailPrestationModel().getPeriodeValidite() %></td>
						<td><%=currentDetailPrestation.getDroitComplexModel().getEnfantComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation1() %></td>
						<td><%=currentDetailPrestation.getDroitComplexModel().getEnfantComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation2() %></td>
						<td><%=objSession.getCodeLibelle(currentDetailPrestation.getDetailPrestationModel().getTypePrestation()) %></td>
						<td><%=objSession.getCodeLibelle(currentDetailPrestation.getDetailPrestationModel().getCategorieTarif()) %></td>
						<td><%=currentDetailPrestation.getDetailPrestationModel().getMontant() %></td>               		
                	</tr>
                	<tr id='<%="detailPrest"+i+"More"%>' class="<%=rowStyle%> detailPrestMore" style="display:none;">
                		<td colspan="6" style="text-align:right;">
                			<!--  TODO: faire des include... -->
                			<table class="max" style="border-top:1px black dotted;border-bottom:1px black dotted;">
                				<tr>
                					<td><ct:FWLabel key="AL0007_PRESTATION_VERSEMENT"/>
                					
                					<%
                						if(JadeNumericUtil.isEmptyOrZero(currentDetailPrestation.getDetailPrestationModel().getIdTiersBeneficiaire())){
                							%><ct:FWLabel key="AL0000_VERSEMENT_A_EMPLOYEUR"/><%
                						}
                						else{
                							%><a href="<%=servletContext+"/pyxis?userAction=pyxis.tiers.tiers.afficher&selectedId="+currentDetailPrestation.getDetailPrestationModel().getIdTiersBeneficiaire()%>" title="Détail bénéficiaire"><%=currentDetailPrestation.getTiersBeneficiaireModel().getDesignation1()%>&nbsp;<%=currentDetailPrestation.getTiersBeneficiaireModel().getDesignation2()%></a><%
                						}
                					
                					%>
                					</td>			
                				</tr>
                	
                			</table>
                			<table style="float:left;width:30%;border-right:1px black dotted;">
              						<tr>
              							<td class="label subtitle"><ct:FWLabel key="AL0007_TITRE_CALCUL"/></td>
              						</tr>
                					<tr>
                						<td class="label"><ct:FWLabel key="AL0007_CALCUL_CANTON"/></td>
                						<td><input type="text" class="readonly date" value="<%=objSession.getCode(currentDetailPrestation.getDetailPrestationModel().getTypePrestation()) %>" disabled="disabled"/></td>
                						<td><input type="text" class="readonly date" value="<%=currentDetailPrestation.getDetailPrestationModel().getMontantCanton() %>" disabled="disabled"/></td>
                				
                					</tr>
                					<tr>
                						<td class="label"><ct:FWLabel key="AL0007_CALCUL_CAISSE"/></td>
                						<td><input type="text" class="readonly date" value="<%=objSession.getCode(currentDetailPrestation.getDetailPrestationModel().getTypePrestation()) %>" disabled="disabled"/></td>
                						<td><input type="text" class="readonly date" value="<%=currentDetailPrestation.getDetailPrestationModel().getMontantCaisse() %>" disabled="disabled"/></td>
                					</tr>
                				
                					<tr>
                						<td class="label"><ct:FWLabel key="AL0007_CALCUL_SUPLEG"/></td>
                						<td></td>
                						<td><input type="text" class="readonly date" value="0.00" disabled="disabled"/></td> 					
                					</tr>
                				
                			</table>

                			<table style="float:left;width:65%;">
                				<tr>
              						<td class="label subtitle"><ct:FWLabel key="AL0007_DONNEES_COMPL"/></td>
              					</tr>
                				<!-- <tr>
                					<td>Mis à jour le</td>
                					<td><input type="text" class="readonly date" value="TODO" disabled="disabled"/></td>
									<td></td>
                					<td></td>
                				</tr> -->
                				<tr>
                					<td class="label"><ct:FWLabel key="AL0007_DONNEES_IDDROIT"/></td>
                					<td><input type="text" class="readonly date" value="<%=currentDetailPrestation.getDetailPrestationModel().getIdDroit() %>" disabled="disabled"/></td>
                					<td class="label"><ct:FWLabel key="AL0007_DONNEES_IDTARIF"/></td>
                					<td><input type="text" class="readonly date" value="TODO" disabled="disabled"/></td>
                				</tr>
                				<tr>
                					<td class="label"><ct:FWLabel key="AL0007_DONNEES_IDENTETE"/></td>
                					<td><input type="text" class="readonly date" value="<%=currentDetailPrestation.getEntetePrestationModel().getIdEntete() %>" disabled="disabled"/></td>
                					<td class="label"><ct:FWLabel key="AL0007_DONNEES_IDDETAIL"/></td>
                					<td><input type="text" class="readonly date" value="<%=currentDetailPrestation.getDetailPrestationModel().getIdDetailPrestation()%>" disabled="disabled"/></td>
                				</tr>
                				<tr>
                					<td class="label"><ct:FWLabel key="AL0007_DONNEES_RUBRIQUE"/></td>
                					<td><input type="text" class="readonly" value="<%=currentDetailPrestation.getDetailPrestationModel().getNumeroCompte() %>" disabled="disabled"/></td>
                					<td class="label"></td>
                					<td>
                				</tr>
                			</table>
                	
                		
                		</td>
                	</tr>
       	
                <%
                cptDetailPrestDisplay++;
          	
                } 	

                %> 
		</table> 
	<%-- /tpl:insert --%>			
</td></tr>											
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<ct:menuChange displayId="menu" menuId="menuWEBAF" showTab="menu"/>
<%if(!JadeNumericUtil.isEmptyOrZero(viewBean.getEntetePrestationModel().getIdRecap())){ %>
<ct:menuChange displayId="options" menuId="generationDossier-detail" showTab="options" checkAdd="no">
	<ct:menuSetAllParams key="selectedId" checkAdd="no" value="<%=viewBean.getEntetePrestationModel().getIdRecap()%>"  />
	<ct:menuSetAllParams key="idDossier" checkAdd="no" value="<%=viewBean.getDossierComplexModel().getId()%>"  />		
</ct:menuChange>
<%}else{ %>
<ct:menuChange displayId="options" menuId="default-detail" showTab="options" checkAdd="no">
	<ct:menuSetAllParams key="idDossier" checkAdd="no" value="<%=viewBean.getDossierComplexModel().getId()%>"  />		
</ct:menuChange>
<%} %>

<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>
