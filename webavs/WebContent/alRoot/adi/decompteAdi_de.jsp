
<%@page import="globaz.al.vb.adi.ALDecompteAdiViewBean"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.globall.util.JANumberFormatter" %>
<%@page import="globaz.fweb.util.JavascriptEncoder"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>


<%@page import="ch.globaz.al.business.constantes.ALCSTiers"%>
<%@page import="ch.globaz.al.business.constantes.ALCSProcessus"%>
<%@page import="ch.globaz.al.business.constantes.ALCSPrestation"%><script type="text/javascript" src="<%=servletContext%>/alRoot/util_webaf.js"></script>
<%-- tpl:insert attribute="zoneInit" --%>
<%
	ALDecompteAdiViewBean viewBean = (ALDecompteAdiViewBean) session.getAttribute("viewBean"); 
	selectedIdValue = viewBean.getId();
	btnUpdLabel = objSession.getLabel("MODIFIER");
	btnDelLabel = objSession.getLabel("SUPPRIMER");
	btnValLabel = objSession.getLabel("VALIDER");
	btnCanLabel = objSession.getLabel("ANNULER");
	btnNewLabel = objSession.getLabel("NOUVEAU");
	//désactive les boutons new depuis et delete cet écran
	bButtonNew = false;
	bButtonDelete = false;
	
	if(ALCSPrestation.ETAT_CO.equals(viewBean.getDecompteAdiModel().getEtatDecompte())){
		bButtonUpdate = false;
	}
	
	idEcran="AL0024";

%>
<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>

<script type="text/javascript">
function add() {

}
function upd() {
    document.forms[0].elements('userAction').value="al.adi.decompteAdi.modifier";
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="al.adi.decompteAdi.ajouter";
    else 
        document.forms[0].elements('userAction').value="al.adi.decompteAdi.modifier";
    return state;
}

function cancel() {
	var methodElement = document.forms[0].elements('_method');
	action(methodElement.value);
	if(methodElement.value == ADD) {
		document.forms[0].elements('userAction').value="al.dossier.dossierAdi.afficher";
		document.forms[0].elements('selectedId').value='<%=JavascriptEncoder.getInstance().encode(viewBean.getDecompteAdiModel().getIdDossier()) %>';
	} else {
		document.forms[0].elements('userAction').value="al.adi.decompteAdi.afficher";
        
	}
}
function del() {	
}

function init(){
	initMoreInfosElements('detailPrest','More',<%=JavascriptEncoder.getInstance().encode(viewBean.getAdiEnfantMoisComplexSearchModel().getSize()+"") %>);
}

function postInit(){
	
}


</script>

<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:insert attribute="zoneTitle" --%>
				<%=(viewBean.getDecompteAdiModel().isNew())?objSession.getLabel("AL0024_TITRE_NEW"):objSession.getLabel("AL0024_TITRE")+viewBean.getDecompteAdiModel().getId()%>		
				(<ct:FWLabel key="AL0004_TITRE"/><%=viewBean.getDecompteAdiModel().getIdDossier() %>)
			<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
			<tr>
			<td><%-- tpl:insert attribute="zoneMain" --%>
				<table class="zone" id="AL0024mainZone">
					<tr>
						<td class="label"><ct:FWLabel key="AL0024_DECOMPTE_ANNEE"/></td>
						<td>
							
							<ct:FWCalendarTag tabindex="1" name="decompteAdiModel.anneeDecompte" displayType="year" value="<%=viewBean.getDecompteAdiModel().getAnneeDecompte() %>"/>	
						
						</td>
						<td class="label"><ct:FWLabel key="AL0024_DECOMPTE_PERIODE"/></td>	
						<td><ct:FWCalendarTag tabindex="2" name="decompteAdiModel.periodeDebut" displayType="month" value="<%=viewBean.getDecompteAdiModel().getPeriodeDebut() %>"/>&nbsp;<ct:FWCalendarTag tabindex="3" name="decompteAdiModel.periodeFin" displayType="month" value="<%=viewBean.getDecompteAdiModel().getPeriodeFin() %>"/></td>
					</tr>
					<tr>
						<td class="label"><ct:FWLabel key="AL0024_DECOMPTE_MONNAIE"/></td>	
						<td>
							<ct:select name="decompteAdiModel.codeMonnaie" tabindex="4" wantBlank="false" defaultValue="<%=ALCSTiers.MONNAIE_EURO %>">
								<ct:optionsCodesSystems csFamille="PYMONNAIE"/>
							</ct:select>
						</td>
						<td class="label"><ct:FWLabel key="AL0024_DECOMPTE_DATE"/></td>	
						<td>
							<%
		                    	
		                    	String dateDecompte = "";
		                    		try{
		                    			dateDecompte = viewBean.getDecompteAdiModel().getDateReception();
		                    		}catch(Exception e){
		                    			e.printStackTrace();
		                    		}
		                    %>
		                    

							<ct:FWCalendarTag name="dateDecompte" tabindex="5" value="<%=dateDecompte%>"
									doClientValidation="CALENDAR"/>
							<ct:inputHidden name="decompteAdiModel.dateReception" id="dateDecompteValue"/>
							<script language="JavaScript">
								document.getElementsByName('dateDecompte')[0].onblur=function(){fieldFormat(document.getElementsByName('dateDecompte')[0],'CALENDAR');document.getElementById('dateDecompteValue').value = this.value;};
								
								function theTmpReturnFunctiondateDecompte(y,m,d) { 
									if (window.CalendarPopup_targetInput!=null) {
										var d = new Date(y,m-1,d,0,0,0);
										window.CalendarPopup_targetInput.value = formatDate(d,window.CalendarPopup_dateFormat);
										document.getElementById('dateDecompteValue').value = document.getElementsByName('dateDecompte')[0].value;		
									}else {
										alert('Use setReturnFunction() to define which function will get the clicked results!'); 
									}	
								}
								cal_dateDecompte.setReturnFunction('theTmpReturnFunctiondateDecompte');
							</script>	
						</td>
					</tr>
					<tr>
						<td class="label"><ct:FWLabel key="AL0024_DECOMPTE_ETAT"/></td>	
						<td>
							<ct:select name="decompteAdiModel.etatDecompte" tabindex="6" wantBlank="false" defaultValue="<%=ALCSPrestation.ETAT_PR%>">
								<ct:optionsCodesSystems csFamille="ALPRETAPRE">
									<ct:excludeCode code="<%=ALCSPrestation.ETAT_TMP%>"/>
									<ct:excludeCode code="<%=ALCSPrestation.ETAT_TR%>"/>
								</ct:optionsCodesSystems>
							</ct:select>		
						
						</td>
						<td class="label"><ct:FWLabel key="AL0024_DECOMPTE_DATEETAT"/></td>	
						<td>
							<%
		                    	
		                    	String dateEtat = "";
		                    		try{
		                    			dateDecompte = viewBean.getDecompteAdiModel().getDateEtat();
		                    		}catch(Exception e){
		                    			e.printStackTrace();
		                    		}
		                    %>
		                    
							
							<ct:FWCalendarTag name="dateEtat" tabindex="7" value="<%=dateEtat%>"
									doClientValidation="CALENDAR"/>
							<ct:inputHidden name="decompteAdiModel.dateEtat" id="dateEtatValue"/>
							<script language="JavaScript">
								document.getElementsByName('dateEtat')[0].onblur=function(){fieldFormat(document.getElementsByName('dateEtat')[0],'CALENDAR');document.getElementById('dateEtatValue').value = this.value;};
								
								function theTmpReturnFunctiondateEtat(y,m,d) { 
									if (window.CalendarPopup_targetInput!=null) {
										var d = new Date(y,m-1,d,0,0,0);
										window.CalendarPopup_targetInput.value = formatDate(d,window.CalendarPopup_dateFormat);
										document.getElementById('dateEtatValue').value = document.getElementsByName('dateEtat')[0].value;		
									}else {
										alert('Use setReturnFunction() to define which function will get the clicked results!'); 
									}	
								}
								cal_dateEtat.setReturnFunction('theTmpReturnFunctiondateEtat');
							</script>	
						</td>
					</tr>
					<tr>
						<td class="label">
							<ct:FWLabel key="AL0024_DECOMPTE_ORGANISME"/>
						</td>
						<td>
							<ct:inputText name="decompteAdiModel.idTiersOrganismeEtranger" styleClass="small" tabindex="8"/>
	                      	 
	                      	<% 
								Object[] caisseMethodsName = new Object[]{
									new String[]{"decompteAdiModel.idTiersOrganismeEtranger","getIdTiers"},
								};
							%>
                    		<ct:FWSelectorTag name="orgSelector" 
								methods="<%=caisseMethodsName%>"
								providerApplication="pyxis" 
								providerPrefix="TI"
								providerAction="pyxis.tiers.administration.chercher"
							/>
						</td>
					
					</tr>
					<tr>
						<td class="label">
							<ct:FWLabel key="AL0024_DECOMPTE_TEXTELIBRE"/>
						</td>	
					</tr>
					<tr>
						<td colspan="4">
							<textarea style="width:100%;" tabindex="9" name="decompteAdiModel.texteLibre" rows="10" cols="120" title='<%=objSession.getLabel("AL0024_TEXTE_TITRE") %>'><%=JadeStringUtil.isEmpty(viewBean.getDecompteAdiModel().getTexteLibre())?"":viewBean.getDecompteAdiModel().getTexteLibre()%></textarea>
						</td>
					</tr>
					
						
				</table>
				
				<table class="al_list" id="moisDetails">
              	<tr>
                  <th scope="col" ><ct:FWLabel key="AL0024_PRESTATIONS_ENTETE_NOM"/></th>
                  <th scope="col" ><ct:FWLabel key="AL0024_PRESTATIONS_ENTETE_DATE"/></th>
                  <th scope="col" ><ct:FWLabel key="AL0024_PRESTATIONS_ENTETE_TYPE"/></th>
                  <th scope="col" ><ct:FWLabel key="AL0024_PRESTATIONS_ENTETE_MOIS"/></th>
                  <th scope="col" ><ct:FWLabel key="AL0024_PRESTATIONS_ENTETE_TOTAL_CH"/></th>
                  <th scope="col" ><ct:FWLabel key="AL0024_PRESTATIONS_ENTETE_TOTAL_ETR"/></th>
                  <th scope="col" ><ct:FWLabel key="AL0024_PRESTATIONS_ENTETE_TAUX"/></th>
                  <th scope="col" ><ct:FWLabel key="AL0024_PRESTATIONS_ENTETE_TOTAL_ETR_CHF"/></th>
                  <th scope="col" ><ct:FWLabel key="AL0024_PRESTATIONS_ENTETE_MONTANT_ADI"/></th>
                </tr>
                <%
                String rowStyle="";
                for(int i=0;i<viewBean.getAdiEnfantMoisComplexSearchModel().getSize();i++){
                	
					
                	boolean condition = (i % 2 == 0);
                	if(!condition)
                		rowStyle="odd";
                	else
                		rowStyle="nonodd";
                	
                	
                	%>
                <tr id='<%="detailPrest"+i%>' class="<%=rowStyle%>">
                 	<td><%= viewBean.getAdiEnfantMoisAt(i).getDroitComplexModel().getEnfantComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation1()+
                 		" "+viewBean.getAdiEnfantMoisAt(i).getDroitComplexModel().getEnfantComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation2()%>
                 	</td>
                 	<td><%= viewBean.getAdiEnfantMoisAt(i).getDroitComplexModel().getEnfantComplexModel().getPersonneEtendueComplexModel().getPersonne().getDateNaissance()%></td>
                 	<td><%= objSession.getCodeLibelle(viewBean.getAdiEnfantMoisAt(i).getDroitComplexModel().getDroitModel().getTypeDroit())%></td>
                 	<td><%= viewBean.getAdiEnfantMoisAt(i).getAdiEnfantMoisModel().getMoisPeriode()%></td>
                 	<td class="number"><%= JANumberFormatter.fmt(viewBean.getAdiEnfantMoisAt(i).getAdiEnfantMoisModel().getMontantCHTotal(), true, true, false, 2)%></td>
                 	<td class="number"><%= JANumberFormatter.fmt(viewBean.getAdiEnfantMoisAt(i).getAdiEnfantMoisModel().getMontantEtrTotal(), true, true, false, 2)%></td>
                 	<td><%= viewBean.getAdiEnfantMoisAt(i).getAdiEnfantMoisModel().getCoursChangeMonnaie()%></td>
                 	<td class="number"><%= JANumberFormatter.fmt(viewBean.getAdiEnfantMoisAt(i).getAdiEnfantMoisModel().getMontantEtrTotalEnCh(), true, true, false, 2)%></td>
                 	<td class="number"><%= JANumberFormatter.fmt(viewBean.getAdiEnfantMoisAt(i).getAdiEnfantMoisModel().getMontantAdi(), true, true, false, 2)%></td>
                </tr>
                <tr id='<%="detailPrest"+i+"More"%>' class="<%=rowStyle%> detailPrestMore" style="display:none;">
                		<td colspan="9" style="text-align:right;">
                			<!--  TODO: faire des include... -->
                			<table style="float:left;width:50%;border-top:1px black dotted;border-right:1px black dotted;">
                				<tr>
                					<td><ct:FWLabel key="AL0024_MORE_DROIT_NUM"/></td>
                					<td><input type="text" class="readonly date" value="<%=viewBean.getAdiEnfantMoisAt(i).getDroitComplexModel().getId()%>"/></td>	
                				</tr>
                				<tr>
                					<td><ct:FWLabel key="AL0024_MORE_DROIT_DEBUT"/></td>
                					<td><input type="text" class="readonly date" value="<%=viewBean.getAdiEnfantMoisAt(i).getDroitComplexModel().getDroitModel().getDebutDroit()%>"/></td>	
                				</tr>
                				<tr>
                					<td><ct:FWLabel key="AL0024_MORE_DROIT_FIN"/></td>
                					<td><input type="text" class="readonly date" value="<%=viewBean.getAdiEnfantMoisAt(i).getDroitComplexModel().getDroitModel().getFinDroitForcee()%>"/></td>	
                				</tr>
                	
                			</table>
                			<table style="float:left;width:50%;border-top:1px black dotted;">
              					<tr>
              						<td class="label subtitle" colspan="4"><ct:FWLabel key="AL0024_MORE_CH_MONTANT"/></td>
              					</tr>
                				<tr>
                					<td><ct:FWLabel key="AL0024_MORE_ALLOC_MONTANT"/></td>
                					<td><input type="text" class="readonly date" value="<%=viewBean.getAdiEnfantMoisAt(i).getAdiEnfantMoisModel().getMontantAllocCH()%>"/></td>	
                					<td><ct:FWLabel key="AL0024_MORE_REPARTI_MONTANT"/></td>
                					<td><input type="text" class="readonly date" value="<%=viewBean.getAdiEnfantMoisAt(i).getAdiEnfantMoisModel().getMontantRepartiCH()%>"/></td>	
                				</tr>
                				<tr>
              						<td class="label subtitle" colspan="4"><ct:FWLabel key="AL0024_MORE_ETR_MONTANT"/></td>
              					</tr>
                				<tr>
                					<td><ct:FWLabel key="AL0024_MORE_ALLOC_MONTANT"/></td>
                					<td><input type="text" class="readonly date" value="<%=viewBean.getAdiEnfantMoisAt(i).getAdiEnfantMoisModel().getMontantAllocEtr()%>"/>	</td>
                					<td><ct:FWLabel key="AL0024_MORE_REPARTI_MONTANT"/></td>
                					<td><input type="text" class="readonly date" value="<%=viewBean.getAdiEnfantMoisAt(i).getAdiEnfantMoisModel().getMontantRepartiEtr()%>"/></td>	
                				</tr>
                				
                				
                			</table>
                		</td>
                	</tr>   	
                	
                <%} 
                
                %>
	                <tr class="tfoot <%=rowStyle%>">
	                    <td colspan="7"></td>
	                    <td class="total"><ct:FWLabel key="MONTANT_TOTAL"/></td>	
	                    <td class="number" id="result_calcul"><%=viewBean.getMontantTotal()==null?" - ":viewBean.getMontantTotal()%></td>
	                    
	                </tr>
                </table>
			<%-- /tpl:insert --%>
			</td>
				
			</tr>								
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<ct:menuChange displayId="menu" menuId="menuWEBAF" showTab="menu"/>
<ct:menuChange displayId="options" menuId="decompte-detail" showTab="options" checkAdd="no">
	<ct:menuSetAllParams key="id" checkAdd="no" value="<%=viewBean.getDecompteAdiModel().getId()%>"  />
	<ct:menuSetAllParams key="selectedId" checkAdd="no" value="<%=viewBean.getDecompteAdiModel().getId()%>"  />	
	<ct:menuSetAllParams key="idDossier" checkAdd="no" value="<%=viewBean.getDecompteAdiModel().getIdDossier()%>"  />		
	<ct:menuSetAllParams key="fromDecompte" checkAdd="no" value="1"  />		
</ct:menuChange>	
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>