<%@page import="ch.globaz.al.business.constantes.*"%>
<%@page import="globaz.al.vb.droit.ALEnfantViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>
<%
	ALEnfantViewBean viewBean = (ALEnfantViewBean) session.getAttribute("viewBean"); 
	selectedIdValue=viewBean.getId();
	btnUpdLabel = objSession.getLabel("MODIFIER");
	btnDelLabel = objSession.getLabel("SUPPRIMER");
	btnValLabel = objSession.getLabel("VALIDER");
	btnCanLabel = objSession.getLabel("ANNULER");
	btnNewLabel = objSession.getLabel("NOUVEAU");
	
	bButtonNew = false;
	bButtonDelete = false;
	
	idEcran="AL0010";
	
%>
<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>
<%@page import="globaz.fweb.util.JavascriptEncoder"%>
<%@page import="ch.globaz.pyxis.business.model.PaysSearchSimpleModel" %>
<%@page import="ch.globaz.pyxis.business.model.PaysSimpleModel"%>

<%@page import="globaz.jade.client.util.JadeStringUtil"%><script type="text/javascript" language="Javascript">
function add() {
    document.forms[0].elements('userAction').value="al.droit.enfant.ajouter";
}
function upd() {
    document.forms[0].elements('userAction').value="al.droit.enfant.modifier";
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="al.droit.enfant.ajouter";
    else 
        document.forms[0].elements('userAction').value="al.droit.enfant.modifier";
    return state;
}

function cancel() {
	var methodElement = document.forms[0].elements('_method');
	action(methodElement.value);
	if(methodElement.value == ADD) {
		document.forms[0].elements('userAction').value="al.personne.personneAF.rechercher";
	} else {
        document.forms[0].elements('userAction').value="al.droit.enfant.afficher";
	}
}

function del() {
	var msgDelete = '<%=JavascriptEncoder.getInstance().encode(objSession.getLabel("MESSAGE_SUPPRESSION"))%>';
    if (window.confirm(msgDelete)){
        document.forms[0].elements('userAction').value="al.droit.enfant.supprimer";
        document.forms[0].submit();
    }
}

function init(){
	if(document.forms[0].elements('_method').value!='add'){
		$("#AL0010tiersZone input").each(function(index) {
			$(this).attr("disabled", true);
			$(this).attr("readonly", true);
			});
	
		$("#AL0010tiersZone select").each(function(index) {
			$(this).attr("disabled", true);
			$(this).attr("readonly", true);
			});
	}
}

function postInit(){
}




</script>

<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:insert attribute="zoneTitle" --%>
			<%=objSession.getLabel("AL0010_TITRE")+viewBean.getEnfantComplexModel().getId()%>
			<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<tr><td>
			<%-- tpl:insert attribute="zoneMain" --%>
              <table id="AL0010tiersZone" class="zone">
             	<tr>
                	<td class="label subtitle">
                		<%if(viewBean.getEnfantComplexModel().isNew()){ %>
                			<ct:FWLabel key="AL0010_TITRE_TIERS"/>
                		<%}else{ %>  				
                			<ct:FWLabel key="AL0010_TITRE_TIERS"/> (<a href="<%=servletContext+"/pyxis?userAction=pyxis.tiers.tiers.afficher&selectedId="+viewBean.getEnfantComplexModel().getEnfantModel().getIdTiersEnfant()%>"><%=viewBean.getEnfantComplexModel().getPersonneEtendueComplexModel().getId()%></a>)
                			<div id="statutSynchroTiers"><img src="images/dialog-ok-apply.png" alt="Synchronisation réussie" width="16" height="16"/></div>
                		<%}%>           		
					</td>
                </tr>
                <ct:inputHidden name="enfantComplexModel.enfantModel.idTiersEnfant"/>
                <tr>
                    <td class="label"><ct:FWLabel key="AL0010_TIERS_NSS"/></td>
                    <td>            	
                    	<ct:inputText name="enfantComplexModel.personneEtendueComplexModel.personneEtendue.numAvsActuel" styleClass="nss readOnly" readonly="true"/>                	
                  	</td>
                    <td class="label"><ct:FWLabel key="AL0010_TIERS_SEXE"/></td>
                    <td>
		              	<ct:select tabindex="4" name="enfantComplexModel.personneEtendueComplexModel.personne.sexe" wantBlank="true">
	                    		<ct:optionsCodesSystems csFamille="PYSEXE"></ct:optionsCodesSystems>	
	                    </ct:select>  
                    </td>
                </tr>
                <tr>
                    <td class="label"><ct:FWLabel key="AL0010_TIERS_TITRE"/></td>
                    <td>
                    	<ct:select tabindex="1" name="enfantComplexModel.personneEtendueComplexModel.tiers.titreTiers" wantBlank="true">
	                    	<ct:optionsCodesSystems csFamille="PYTITRE"></ct:optionsCodesSystems>	
	                    </ct:select>  
                    </td>
                    <td class="label"><ct:FWLabel key="AL0010_TIERS_ETATCIVIL"/></td>
                    <td>
                    	<ct:select tabindex="5" name="enfantComplexModel.personneEtendueComplexModel.personne.etatCivil" defaultValue="<%=viewBean.getEnfantComplexModel().getPersonneEtendueComplexModel().getPersonne().getEtatCivil() %>">
	                    	<ct:optionsCodesSystems csFamille="PYETATCIVI"></ct:optionsCodesSystems>	
	                    </ct:select> 
                    </td>
                </tr>
                <tr>
                    <td class="label"><ct:FWLabel key="AL0010_TIERS_NOM"/></td>
                    <td><ct:inputText tabindex="2" name="enfantComplexModel.personneEtendueComplexModel.tiers.designation1" styleClass="normal"/></td>
                    <td class="label"><ct:FWLabel key="AL0010_TIERS_NAISSANCE"/> </td>
                    <td>		
					<ct:FWCalendarTag name="dateNaissance" tabindex="6"
							value="<%=viewBean.getEnfantComplexModel().getPersonneEtendueComplexModel().getPersonne().getDateNaissance()%>" 
							doClientValidation="CALENDAR"/>
					<ct:inputHidden name="enfantComplexModel.personneEtendueComplexModel.personne.dateNaissance" id="dateNaissanceValue"/>
					<script language="JavaScript">
							document.getElementsByName('dateNaissance')[0].onblur=function(){fieldFormat(document.getElementsByName('dateNaissance')[0],'CALENDAR');document.getElementById('dateNaissanceValue').value = this.value;};
									
							function theTmpReturnFunctiondateNaissance(y,m,d) { 
								if (window.CalendarPopup_targetInput!=null) {
									var d = new Date(y,m-1,d,0,0,0);
									window.CalendarPopup_targetInput.value = formatDate(d,window.CalendarPopup_dateFormat);
									document.getElementById('dateNaissanceValue').value = document.getElementsByName('dateNaissance')[0].value;		
								}else {
									alert('Use setReturnFunction() to define which function will get the clicked results!'); 
								}
							}
							cal_dateNaissance.setReturnFunction('theTmpReturnFunctiondateNaissance');
					</script>
					</td>
                </tr>
                <tr>
                    <td class="label"><ct:FWLabel key="AL0010_TIERS_PRENOM"/> </td>
                    <td>
                    	<ct:inputText tabindex="3" name="enfantComplexModel.personneEtendueComplexModel.tiers.designation2" styleClass="normal"/>
                  	</td>
                    <td class="label"><ct:FWLabel key="AL0010_TIERS_NATIONALITE"/> </td>
                    <td>
			           <%
		                	PaysSearchSimpleModel paysSearchModel = (PaysSearchSimpleModel) request.getAttribute("list_pays");
		               %>
					  <ct:select tabindex="7" name="enfantComplexModel.personneEtendueComplexModel.tiers.idPays" wantBlank="true">
					  	<%for(int i=0; i<paysSearchModel.getSize(); i++){ 
					  		PaysSimpleModel pays = (PaysSimpleModel) paysSearchModel.getSearchResults()[i];%>
					  		<ct:option value="<%=pays.getIdPays()%>" label="<%=pays.getLibelleFr()%>"></ct:option>
					  	<%} %>
					  </ct:select>		                    	
                    </td>
                </tr>
          </table>
    	  <table class="zone" id="AL0010naissanceZone"> 
	                <tr>
	                	<td class="subtitle" colspan="4"><ct:FWLabel key="AL0003_TITRE_NAISSANCE"/></td>
	                </tr> 
	               
	                <tr>
	                    <td class="label"><ct:FWLabel key="AL0003_NAISSANCE_ALLOC"/></td>
	                    <td>
	                    	<ct:select tabindex="8" name="enfantComplexModel.enfantModel.typeAllocationNaissance" defaultValue="<%=ALCSDroit.NAISSANCE_TYPE_NAIS%>">
	                    		<ct:optionsCodesSystems csFamille="ALDRONAITY"></ct:optionsCodesSystems>	
	                    	</ct:select> 
	                    </td>
	                    <td class="label"><ct:FWLabel key="AL0003_NAISSANCE_MONTANT_FORCE"/></td>
	                    <td>
		                    <ct:inputText tabindex="9" name="enfantComplexModel.enfantModel.montantAllocationNaissanceFixe" styleClass="montant"/>
	                    </td> 
	                </tr>
	      </table>
          <table id="AL0010enfantZone" class="zone">
                <tr>
                	<td class="subtitle label">
                		<ct:FWLabel key="AL0010_TITRE_ENFANT"/>
                		<div id="statutWarningEnfant"><%=viewBean.getNbDroitsActifs()>1?"<img src='images/dialog-warning.png' alt='Enfant utilisé dans plusieurs droits actifs' width='16' height='16'/>":"" %></div>
                	</td>
                </tr>
                <tr>
                    <td class="label"><ct:FWLabel key="AL0010_ENFANT_PAYS"/></td>
                    <td >
					  <ct:select tabindex="10" name="enfantComplexModel.enfantModel.idPaysResidence">
					  	<%for(int i=0; i<paysSearchModel.getSize(); i++){ 
					  		PaysSimpleModel pays = (PaysSimpleModel) paysSearchModel.getSearchResults()[i];%>
					  		<ct:option value="<%=pays.getIdPays()%>" label="<%=pays.getLibelleFr()%>"></ct:option>
					  	<%} %>
					  </ct:select>		                    	
                    </td>
                    <td class="label"></td>
	                 <td>
	             		                      
					</td>
                </tr>
                <tr>
                    <td class="label"><ct:FWLabel key="AL0010_ENFANT_CANTON"/></td>
                    <td>
                    	<ct:select tabindex="11" name="enfantComplexModel.enfantModel.cantonResidence" wantBlank="true">
	                    	<ct:optionsCodesSystems  csFamille="ALCANTON" ></ct:optionsCodesSystems>	
	                    </ct:select> 
          			</td>
                    <td class="label">&nbsp;</td>
                    <td>
                    	    <% if(viewBean.getEnfantComplexModel().getEnfantModel().getCapableExercer().booleanValue()){ %> 
	                    	<input tabindex="13" type="checkbox" checked="checked" name="enfantComplexModel.enfantModel.capableExercer"
     								value="true" onclick="if(this.checked)this.value='true';else this.value='false';"   		
	                    	/>
	                    <%}else{%>
	                    	<input tabindex="13" type="checkbox" name="enfantComplexModel.enfantModel.capableExercer"
	               					value="false" onclick="if(this.checked)this.value='true';else this.value='false';"    		
	                    	/>
	                    <%}%>
	                    <ct:FWLabel key="AL0010_ENFANT_CAPABLE"/>
                    </td>
                </tr>
                <tr>
                    <td class="label"></td>
                    <td></td>                
                </tr>
          </table>
         
			<%-- /tpl:insert --%>			
</td></tr>											
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>

<ct:menuChange displayId="options" menuId="enfant-detail" showTab="options" >			
	<ct:menuSetAllParams  key="idEnfant" value="<%=viewBean.getId()%>"  />
	<ct:menuSetAllParams  key="nomEnfant" value="<%=viewBean.getEnfantComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation1()%>"  />	
	<ct:menuSetAllParams  key="prenomEnfant" value="<%=viewBean.getEnfantComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation2()%>"  />			
</ct:menuChange>

<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>
