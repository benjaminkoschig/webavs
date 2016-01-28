<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran="GSF0002";

globaz.hera.vb.famille.SFApercuRelationFamilialeRequerantViewBean viewBean = (globaz.hera.vb.famille.SFApercuRelationFamilialeRequerantViewBean) session.getAttribute("viewBean");
boolean conjointInconnu = false;
bButtonDelete=viewBean.isDeletable();
btnDelLabel = viewBean.getSession().getLabel("BUTTON_DELETE_MEMBRE");


// Si le tiers 
if (!JadeStringUtil.isIntegerEmpty(viewBean.getIdTiers())) {
	bButtonUpdate=false;
	bButtonDelete = false;
}

if(!viewBean.hasAdresseDomicile()){
	bButtonUpdate=true;
}

if(globaz.hera.api.ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU.equals(viewBean.getIdMembreFamille())){
	// conjoint inconnu
	bButtonDelete=false;
	bButtonUpdate=true;
	conjointInconnu = true;
}


%>

		<%@ page import="globaz.commons.nss.*"%>
		<%@page import="globaz.jade.client.util.JadeStringUtil"%>

<%@page import="globaz.hera.api.ISFSituationFamiliale"%><script type="text/javascript"
			src="<%=servletContext%>/scripts/nss.js"></script>
		<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
	<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>


<ct:menuChange displayId="options" menuId="sf-optionmembre" showTab="options">
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdMembreFamille()%>"/>
	<ct:menuSetAllParams key="idMembreFamilleDepuisRelFam" value="<%=viewBean.getIdMembreFamille()%>"/>
	<ct:menuSetAllParams key="idMembreFamille" value="<%=viewBean.getIdMembreFamille()%>"/>
	<ct:menuSetAllParams key="csDomaine" value="<%=viewBean.getCsDomaineApplication()%>"/>
</ct:menuChange>
<ct:menuChange displayId="menu" menuId="sf-menuprincipal"/>



<SCRIPT>

function add() {
	nssUpdateHiddenFields();
    document.forms[0].elements('userAction').value="hera.famille.apercuRelationFamilialeRequerant.ajouterRequerant";
}

function upd() {
	nssUpdateHiddenFields();
	cantonDomicileChange();
	<%if(!JadeStringUtil.isIntegerEmpty(viewBean.getIdTiers()) &&
		 !viewBean.hasAdresseDomicile()){%>
		document.getElementById("nomAffiche").disabled=true;				    	        				
		document.getElementById("prenomAffiche").disabled=true;
		document.getElementById("csNationaliteAffiche").disabled=true;
		document.getElementById("partiallikeNoAvs").disabled=true;
		document.getElementById("dateNaissanceAffiche").disabled=true;
		document.getElementById("dateDecesAffiche").disabled=true;
		document.getElementById("csSexeAffiche").disabled=true;
	<%}%>
}

function validate() {
	nssUpdateHiddenFields();
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add") {
        document.forms[0].elements('userAction').value="hera.famille.apercuRelationFamilialeRequerant.ajouterRequerant";
    }
    else {
        <%if (conjointInconnu) {%>
        	document.forms[0].elements('userAction').value="hera.famille.apercuRelationFamilialeRequerant.modifierConjointInconnu";
        <%} else {%>
        	document.forms[0].elements('userAction').value="hera.famille.apercuRelationFamilialeRequerant.modifierMembre";
        <%}%>        
    }
    
    return state;
}

function cancel() {
 if (document.forms[0].elements('_method').value == "add")
    document.forms[0].elements('userAction').value="hera.famille.apercuRelationFamilialeRequerant.chercher";
 else
    document.forms[0].elements('userAction').value="hera.famille.apercuRelationFamilialeRequerant.chercher";
}

function del() {
    if (window.confirm("<ct:FWLabel key='ERROR_DELETE_REL_FAM_CONFIRMATION'/>")){
        document.forms[0].elements('userAction').value="hera.famille.apercuRelationFamilialeRequerant.supprimer";
        document.forms[0].submit();
    }
}

function init() {}

function nssFailure() {
	document.getElementById("idAssure").value=null;
	document.getElementById("nss").value=null;
	document.getElementById("provenance").value=null;	
	document.getElementById("nomAffiche").disabled=false;
	document.getElementById("prenomAffiche").disabled=false;
	document.getElementById("nomPrenom").value="";
	document.getElementById("csNationaliteAffiche").disabled=false;			
	document.getElementById("csCantonDomicileAffiche").disabled=false;	

	document.getElementById("dateNaissanceAffiche").disabled=false;	
	document.getElementById("dateDecesAffiche").disabled=false;	
	document.getElementById("csSexeAffiche").disabled=false;					
}
	
function nssUpdateHiddenFields() {
	document.getElementById("nom").value=document.getElementById("nomAffiche").value;
	document.getElementById("prenom").value=document.getElementById("prenomAffiche").value;
	
	document.getElementById("csNationalite").value=document.getElementById("csNationaliteAffiche").value;
	document.getElementById("csCantonDomicile").value=document.getElementById("csCantonDomicileAffiche").value;	
	
	document.getElementById("dateNaissance").value=document.getElementById("dateNaissanceAffiche").value;	
	document.getElementById("dateDeces").value=document.getElementById("dateDecesAffiche").value;	
	document.getElementById("csSexe").value=document.getElementById("csSexeAffiche").value;	
				
	document.getElementById("nss").value=document.getElementById("likeNoAvs").value;
}
	
function onChangeNationalite() {
	//if(document.getElementById("csNationaliteAffiche").value!='100') {
	//	document.getElementById("csCantonDomicileAffiche").value=505027;
	//}
}

function nssChange(tag) {
	if(tag.select==null) {
												
	}else {
			_nss = removeDots(tag.select.options[tag.select.selectedIndex].nss);

			if (_nss.length == 13) {	
				//formatte tag avec nouveau nss
				nssCheckChar(43, 'likeNoAvs');nssAction('likeNoAvs');concatPrefixAndPartial('likeNoAvs');
			}
			else {
				//formatte tag avec ancien nss
				nssCheckChar(45, 'likeNoAvs');nssAction('likeNoAvs');concatPrefixAndPartial('likeNoAvs');
			}

	
  		var element = tag.select.options[tag.select.selectedIndex];
		if (element.nss!=null){
			document.getElementById("nss").value=element.nss;
		}

		if (element.nom!=null) {
			document.getElementById("nom").value=element.nom;
			document.getElementById("nomAffiche").value=element.nom;
		}
				
		if (element.prenom!=null){		    	        					
			document.getElementById("prenom").value=element.prenom;
			document.getElementById("prenomAffiche").value=element.prenom;
		}
			
		if (element.nom!=null && element.prenom!=null){
			document.getElementById("nomPrenom").value=element.nom + " " + element.prenom;
		}
				
		if (element.provenance!=null){
			document.getElementById("provenance").value=element.provenance;
		}
			
		if (element.id!=null){
			document.getElementById("idAssure").value=element.idAssure;
		}
					
		if (element.codePays!=null) {				
			for (var i=0; i < document.getElementById("csNationaliteAffiche").length ; i++) {					
				if (element.codePays==document.getElementById("csNationaliteAffiche").options[i].value) {
					document.getElementById("csNationaliteAffiche").options[i].selected=true;
				}
			}
			document.getElementById("csNationalite").value=element.codePays;
		}

		if (element.codeCantonDomicile!=null) {
			for (var i=0; i < document.getElementById("csCantonDomicileAffiche").length ; i++) {					
				if (element.codeCantonDomicile==document.getElementById("csCantonDomicileAffiche").options[i].value) {
					document.getElementById("csCantonDomicileAffiche").options[i].selected=true;
				}
			}
			document.getElementById("csCantonDomicile").value=element.codeCantonDomicile;
		}

		if (element.codeSexe!=null) {
			for (var i=0; i < document.getElementById("csSexeAffiche").length ; i++) {					
				if (element.codeSexe==document.getElementById("csSexeAffiche").options[i].value) {
					document.getElementById("csSexeAffiche").options[i].selected=true;
				}
			}
			document.getElementById("csSexe").value=element.codeSexe;
		}
			
		if (element.dateNaissance!=null) {
			document.getElementById("dateNaissance").value=element.dateNaissance;
			document.getElementById("dateNaissanceAffiche").value=element.dateNaissance;
		}
			
		if (element.dateDeces!=null) {
			document.getElementById("dateDeces").value=element.dateDeces;
			document.getElementById("dateDecesAffiche").value=element.dateDeces;
		}

						    	        				
		if ('<%=globaz.hera.tools.nss.SFUtil.PROVENANCE_TIERS%>'==element.provenance) {
			document.getElementById("nomAffiche").disabled=true;				    	        				
			document.getElementById("prenomAffiche").disabled=true;
			document.getElementById("csNationaliteAffiche").disabled=true;
			document.getElementById("csCantonDomicileAffiche").disabled=true;
			document.getElementById("dateNaissanceAffiche").disabled=true;
			document.getElementById("dateDecesAffiche").disabled=true;
			document.getElementById("csSexeAffiche").disabled=true;
		}	
	}
	
}
	function enable($input) {
		$input.removeAttr('disabled');
		$input.removeAttr('readonly');
		$input.change();
	}
	
	function disable($input) {
		$input.attr('disabled', 'true');
		$input.attr('readonly', 'readonly');
		$input.val("");
		$input.change();
	}
	
	
	// Si canton de domicile est changé sur la valeur '' (vide) on affiche les pays de domicile
	function cantonDomicileChange(){
		var value = $csCantonDomicileAffiche.val();
		if('' == value){
			enable($pays);
		}
		else {
			disable($pays);
		}
	}
	
	$(function(){
		$pays = $('#pays');
		$csCantonDomicileAffiche = $('#csCantonDomicileAffiche');
		$csCantonDomicileAffiche.change(function(){
			cantonDomicileChange();
		});
	});



</SCRIPT>
	<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_APERCU_RELATION_FAMILIALE_REQUERANT_DETAIL_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>   
							<!--Gestion du NSS -->
							<tr><td colspan="2">
							<TABLE>														
								<TR>
									<TD>
										<ct:FWLabel key="JSP_REFERENCE_INTERNE"/>
									</TD>
									<TD colspan="3">
										<INPUT type="text" name="dummy" value="<%=viewBean.getIdMembreFamille()%>" class="libelleDisabled" readonly />
									</TD>
								</TR>
								
								<TR>
									<TD>
										<ct:FWLabel key="JSP_NSS"/>
									</TD>
									<TD colspan="3">
										
										<%	String params = "&provenance1=TIERS";	
												   params += "&provenance2=CI";
											String jspLocation = servletContext + "/heraRoot/numeroSecuriteSocialeSF_select.jsp"; %>
							
									<ct1:nssPopup name="likeNoAvs" onFailure="nssFailure();" onChange="nssChange(tag);" params="<%=params%>" 
									value="<%=viewBean.getNumeroAvsFormateSansPrefixe()%>" newnss="<%=viewBean.isNNSS()%>" 
									jspName="<%=jspLocation%>" avsMinNbrDigit="3" nssMinNbrDigit="3" avsAutoNbrDigit="11" nssAutoNbrDigit="10"  />
										<SCRIPT language="javascript">	    				        	
			    				        	document.getElementById("likeNoAvs").onkeypress= new Function ("","return filterCharForPositivFloat(window.event);");	
					            		</SCRIPT>	
										<INPUT type="text" name="nomPrenom" value="<%=viewBean.getNom()%> <%=viewBean.getPrenom()%>" class="libelleLongDisabled"/>
										<INPUT type="hidden" name="nss" value="<%=viewBean.getNumeroAvsFormateSansPrefixe()%>"/>
										<INPUT type="hidden" name="idAssure" value="<%=viewBean.getIdAssure()%>"/>
										<INPUT type="hidden" name="provenance" value="<%=viewBean.getProvenance()%>"/>										
										<INPUT type="hidden" name="idMembreFamille" value="<%=viewBean.getIdMembreFamille()%>"/>
										<INPUT type="hidden" name="idTiers" value="<%=viewBean.getIdTiers()%>"/>
										<INPUT type="hidden" name="hasAdresseDomicile" value="<%=viewBean.hasAdresseDomicile()%>"/>									
					            	</TD>
								</TR>


								<TR>
									<TD><ct:FWLabel key="JSP_DOMAINE"/></TD>
									<TD>
									<%boolean isReadOnly = "add".equals(request.getParameter("_method"))?false:true;
									if (isReadOnly) {%>	
										<INPUT type="text" name="dummy" value="<%=viewBean.getSession().getCodeLibelle(viewBean.getCsDomaineApplication())%>" class="libelleLongDisabled"/>
									<%} else {%>
									
									<ct:FWCodeSelectTag name="csDomaine"
											wantBlank="<%=false%>"
									      	codeType="SFDOAPP"
									      	defaut=""/>
  									<%}%>


									</TD>
								<TR>

								<TR>
									<TD><ct:FWLabel key="JSP_NOM"/></TD>
									<TD><INPUT type="hidden" name="nom" value="<%=viewBean.getNom()%>"/>
  										<INPUT type="text" name="nomAffiche" value="<%=viewBean.getNom()%>"/>
									</TD>
									<TD><ct:FWLabel key="JSP_PRENOM"/></TD>
									<TD><INPUT type="hidden" name="prenom" value="<%=viewBean.getPrenom()%>"/>
										<INPUT type="text" name="prenomAffiche" value="<%=viewBean.getPrenom()%>"/>
									</TD>
								</TR>

								<TR>
									<TD><ct:FWLabel key="JSP_DATE_NAISSANCE"/></TD>
									<TD><INPUT type="hidden" name="dateNaissance" value="<%=viewBean.getDateNaissance()%>"/>
  										<ct:FWCalendarTag name="dateNaissanceAffiche" value="<%=viewBean.getDateNaissance()%>"/>
									</TD>
									<TD><ct:FWLabel key="JSP_DATE_DECES"/></TD>
									<TD><INPUT type="hidden" name="dateDeces" value="<%=viewBean.getDateDeces()%>"/>
										<ct:FWCalendarTag name="dateDecesAffiche" value="<%=viewBean.getDateDeces()%>"/>
									</TD>
								</TR>
										
								<TR>
									<TD><LABEL for="sexe"><ct:FWLabel key="JSP_SEXE"/></LABEL></TD>

									<TD colspan="3">
										<ct:FWCodeSelectTag name="csSexeAffiche"
											wantBlank="<%=true%>"
									      	codeType="PYSEXE"
									      	defaut="<%=viewBean.getCsSexe()%>"/>
									      	<INPUT type="hidden" name="csSexe" value="<%=viewBean.getCsSexe()%>"/>
									</TD>	
								</TR>																												            				            					            
										
																
								<TR>
									<TD><LABEL for=csNationaliteAffiche><ct:FWLabel key="JSP_NATIONALITE"/></LABEL></TD>

									<TD>
										<ct:FWListSelectTag name="csNationaliteAffiche" data="<%=viewBean.getTiPays()%>" defaut="<%=JadeStringUtil.isIntegerEmpty(viewBean.getCsNationalite())?globaz.pyxis.db.adressecourrier.TIPays.CS_SUISSE:viewBean.getCsNationalite()%>"/>
										<script language="javascript">
									 	  	element = document.getElementById("csNationaliteAffiche");
									 	  	element.onchange=function() {onChangeNationalite();}
									 	</script> 	
									    <INPUT type="hidden" name="csNationalite" value="<%=JadeStringUtil.isIntegerEmpty(viewBean.getCsNationalite())?globaz.pyxis.db.adressecourrier.TIPays.CS_SUISSE:viewBean.getCsNationalite()%>"/>
									</TD>	
								
									<TD><LABEL for="csCantonDomicileAffiche"><ct:FWLabel key="JSP_CANTON_DOMICILE"/></LABEL></TD>
									<TD>
										<ct:FWCodeSelectTag name="csCantonDomicileAffiche"
											wantBlank="<%=true%>"
									      	codeType="PYCANTON"
									      	defaut="<%=viewBean.getCsCantonDomicile()%>"/>
									      	<INPUT type="hidden" name="csCantonDomicile" value="<%=viewBean.getCsCantonDomicile()%>"/>
									</TD>	
								</TR>		
								
								
								<tr>
									<td></td>
									<td></td>
									
									<td><label for="pays"><ct:FWLabel key="JSP_PAYS_DOMICILE"/></label></td>
									<td>
										<ct:FWListSelectTag name="pays" 
											data="<%=viewBean.getPaysDomicile()%>" 
											defaut="<%=viewBean.getPays()%>"/>									    
									</td>									
								</tr>
								
								
								
								
								
								
																																		            				            					            
							</TABLE>
						</TD>
					</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>