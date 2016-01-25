<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
		<%@page import="globaz.ij.application.IJApplication"%>
<%@page import="globaz.ij.api.prononces.IIJPrononce"%>

<%@page import="globaz.jade.client.util.JadeStringUtil"%><script type="text/javascript"
			src="<%=servletContext%>/scripts/nss.js"></script>
		<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%
	idEcran="PIJ0005";
	globaz.ij.vb.prononces.IJRequerantViewBean viewBean = (globaz.ij.vb.prononces.IJRequerantViewBean) session.getAttribute("viewBean");
	bButtonUpdate =  bButtonUpdate && viewBean.isModifierPermis();
	bButtonValidate = false;
	bButtonCancel = false;
	bButtonDelete = false;
	String csTypeIJ=request.getParameter("csTypeIJ");
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="ij-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="ij-optionsempty"/>

<SCRIPT language="javascript"> 

	function add() {
		nssUpdateHiddenFields();
	    document.forms[0].elements('userAction').value="ij.prononces.requerant.ajouter";
	}
	
	function upd() {
	nssUpdateHiddenFields();
	document.forms[0].elements('userAction').value="ij.prononces.requerant.modifier";
	document.forms[0].elements('modifie').value="true";
	
	document.getElementById("nomAffiche").disabled=true;				    	        				
	document.getElementById("prenomAffiche").disabled=true;
	document.getElementById("csNationaliteAffiche").disabled=true;
	document.getElementById("csCantonDomicileAffiche").disabled=true;
	document.getElementById("dateNaissanceAffiche").disabled=true;
	document.getElementById("csSexeAffiche").disabled=true;
	
	}
	
	function validate() {
		nssUpdateHiddenFields();
	    state = validateFields();
	    if (document.forms[0].elements('_method').value == "read"){
	    	document.forms[0].elements('userAction').value="ij.prononces.requerant.actionEcranSuivant";
	    }
	    else if (document.forms[0].elements('_method').value == "add"){
	        document.forms[0].elements('userAction').value="ij.prononces.requerant.ajouter";
	    }
	    else {
	        document.forms[0].elements('userAction').value="ij.prononces.requerant.modifier";
	    }
	    return state;
	
	}
	
	function arret() {
		nssUpdateHiddenFields();
		document.forms[0].elements('userAction').value = "<%=globaz.ij.servlet.IIJActions.ACTION_REQUERANT%>.arreterEtape1";
  		document.forms[0].submit();
 	}
 	
	
	function cancel() {
	
	if (document.forms[0].elements('_method').value == "add")
	  document.forms[0].elements('userAction').value="back";
	 else
	  document.forms[0].elements('userAction').value="ij.prononces.prononce.chercher";
	  
	}
	
	function del() {
		if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
	        document.forms[0].elements('userAction').value="ij.prononces.prononce.supprimer";
	        document.forms[0].submit();
	    }
	}
	


	function init(){
	}
	
	
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
		document.getElementById("csSexeAffiche").disabled=false;		
	}
	
	function nssUpdateHiddenFields() {
				document.getElementById("nom").value=document.getElementById("nomAffiche").value;
				document.getElementById("prenom").value=document.getElementById("prenomAffiche").value;
				document.getElementById("dateNaissance").value=document.getElementById("dateNaissanceAffiche").value;
				document.getElementById("csSexe").value=document.getElementById("csSexeAffiche").value;	
				document.getElementById("csNationalite").value=document.getElementById("csNationaliteAffiche").value;
				document.getElementById("csCantonDomicile").value=document.getElementById("csCantonDomicileAffiche").value;				
				document.getElementById("nss").value=document.getElementById("likeNoAvs").value;
	}
	

	function nssChange(tag) {
				
		if(tag.select==null) {											
		}
		else {
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
			
			if (element.dateNaissance!=null) {
				document.getElementById("dateNaissance").value=element.dateNaissance;
				document.getElementById("dateNaissanceAffiche").value=element.dateNaissance;
			}
			
			if (element.codeSexe!=null) {
				for (var i=0; i < document.getElementById("csSexeAffiche").length ; i++) {					
					if (element.codeSexe==document.getElementById("csSexeAffiche").options[i].value) {
						document.getElementById("csSexeAffiche").options[i].selected=true;
					}
				}
				document.getElementById("csSexe").value=element.codeSexe;
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

						    	        				
			if ('<%=globaz.prestation.interfaces.util.nss.PRUtil.PROVENANCE_TIERS%>'==element.provenance) {
				document.getElementById("nomAffiche").disabled=true;				    	        				
				document.getElementById("prenomAffiche").disabled=true;
				document.getElementById("csNationaliteAffiche").disabled=true;
				document.getElementById("csCantonDomicileAffiche").disabled=true;
				document.getElementById("dateNaissanceAffiche").disabled=true;
				document.getElementById("csSexeAffiche").disabled=true;
				
			}
		}
	}
	
	var $soumisImpotSourceCheckbox = null;
	var $soumisImpotSource = null;
	
	var $detailImpotSource = null;

	var $selectCsCantonImpositionSource = null;
	var $csCantonImpositionSource = null;

	var $tauxImpositionAffiche = null;
	var $tauxImposition = null;

	var $avecDecisionCheckbox = null;
	var $avecDecision = null;

	$(document).ready(function () {
		$soumisImpotSourceCheckbox = $('#soumisImpotSourceCheckbox');
		$soumisImpotSource = $('#soumisImpotSource');
		$detailImpotSource = $('#detailImpotSource');
		
		$soumisImpotSourceCheckbox.click(function () {
			if ($soumisImpotSourceCheckbox.is(':checked')) {
				$soumisImpotSource.val('on');
				$csCantonImpositionSource.val($selectCsCantonImpositionSource.val());
				$tauxImposition.val($tauxImpositionAffiche.val());
				
				$detailImpotSource.show();
			} else {
				$soumisImpotSource.val('off');
				$csCantonImpositionSource.val('');
				$tauxImposition.val('');
				
				$detailImpotSource.hide();
			}
		});

		if ($soumisImpotSourceCheckbox.is(':not(:checked)')) {
			$detailImpotSource.hide();
		}

		$csCantonImpositionSource = $('#csCantonImpositionSource');
		$selectCsCantonImpositionSource = $('#csCantonImpositionSourceAffiche');

		$selectCsCantonImpositionSource.change(function () {
			$csCantonImpositionSource.val($selectCsCantonImpositionSource.val());
		});

		$tauxImpositionAffiche = $('#tauxImpositionAffiche');
		$tauxImposition = $('#tauxImposition');

		$tauxImpositionAffiche.change(function () {
			$tauxImposition.val($tauxImpositionAffiche.val());
		});

		$avecDecisionCheckbox = $('#avecDecisionCheckbox');
		$avecDecision = $('#avecDecision');

		$avecDecisionCheckbox.click(function () {
			if ($avecDecisionCheckbox.is(':checked')) {
				$avecDecision.val('on');
			} else {
				$avecDecision.val('off');
			}
		});
	});
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_REQUERANT"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
							<TR>
								<TD width="25%">
									<INPUT type="hidden" name="csTypeIJ" value="<%=!globaz.globall.util.JAUtil.isIntegerEmpty(viewBean.getCsTypeIJ())?viewBean.getCsTypeIJ():csTypeIJ%>">
									<input type="hidden" name="idPrononce" value="<%=viewBean.getIdPrononce()%>">
									<INPUT type="hidden" name="modifie" value="<%=viewBean.isModifie()%>">
									
									<LABEL for="idGestionnaire"><ct:FWLabel key="JSP_GESTIONNAIRE"/></LABEL>
								</TD>
								<TD width="25%"><ct:FWListSelectTag name="idGestionnaire" data="<%=globaz.ij.utils.IJGestionnaireHelper.getResponsableData(viewBean.getSession())%>" defaut="<%=viewBean.getIdGestionnaire()%>"/></TD>		
								<td width="25%">&nbsp;</td>
								<td width="25%">&nbsp;</td>
							</TR>
							<TR><TD colspan="4">&nbsp;</TD></TR>
							<TR>
									<TD>
										<ct:FWLabel key="JSP_NSS_ABREGE"/>
									</TD>
									<TD colspan="3">
										<%	String params = "&provenance1=TIERS";	
												   params += "&provenance2=CI";
											String jspLocation = servletContext + "/ijRoot/numeroSecuriteSocialeSF_select.jsp"; %>
										<ct1:nssPopup name="likeNoAvs" onFailure="nssFailure();" onChange="nssChange(tag);" params="<%=params%>" 
													  value="<%=viewBean.getNumeroAvsFormateSansPrefixe()%>" newnss="<%=viewBean.isNNSS()%>"
													  jspName="<%=jspLocation%>" avsMinNbrDigit="3" nssMinNbrDigit="3" avsAutoNbrDigit="11" nssAutoNbrDigit="10"  />		
					            		
										<INPUT type="text" name="nomPrenom" value="<%=viewBean.getNom()%> <%=viewBean.getPrenom()%>" class="libelleLongDisabled"/>
										<INPUT type="hidden" name="nss" value="<%=viewBean.getNumeroAvsFormateSansPrefixe()%>"/>
										<INPUT type="hidden" name="idAssure" value="<%=viewBean.getIdAssure()%>"/>
										<INPUT type="hidden" name="provenance" value="<%=viewBean.getProvenance()%>"/>										
					            	</TD>
								</TR>
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
									<TD><ct:FWLabel key="JSP_SEXE"/></TD>
									<TD colspan="3">
										<ct:FWCodeSelectTag name="csSexeAffiche"
											wantBlank="<%=true%>"
									      	codeType="PYSEXE"
									      	defaut="<%=viewBean.getCsSexe()%>"/>
									      	<INPUT type="hidden" name="csSexe" value="<%=viewBean.getCsSexe()%>"/>
									</TD>
								</TR>
								<TR>
									<TD><LABEL for="pays"><ct:FWLabel key="JSP_NATIONALITE"/></LABEL></TD>

									<TD><ct:FWListSelectTag name="csNationaliteAffiche" data="<%=viewBean.getTiPays()%>" defaut="<%=globaz.globall.util.JAUtil.isIntegerEmpty(viewBean.getCsNationalite())?globaz.pyxis.db.adressecourrier.TIPays.CS_SUISSE:viewBean.getCsNationalite()%>"/>
									      	<INPUT type="hidden" name="csNationalite" value="<%=viewBean.getCsNationalite()%>"/>
									</TD>	
								
									<TD><LABEL for="pays"><ct:FWLabel key="JSP_CANTON_DOMICILE"/></LABEL></TD>
									<TD>
										<ct:FWCodeSelectTag name="csCantonDomicileAffiche" codeType="PYCANTON" wantBlank="true"	defaut="<%=viewBean.getCsCantonDomicile()%>"/>
									      	<INPUT type="hidden" name="csCantonDomicile" value="<%=viewBean.getCsCantonDomicile()%>"/>
									</TD>	
								</TR>
								<TR>
									<TD colspan="4"><HR></TD>
								</TR>																											            				            					            
								<TR>
									<TD>
										<ct:FWLabel key="JSP_SOUMIS_IMPOT_SOURCE"/>
									</TD>
									<TD align="left">
										<INPUT type="checkbox" value="on" id="soumisImpotSourceCheckbox" <%=viewBean.getSoumisImpotSource().booleanValue()?"CHECKED":""%>>
										<input type="hidden" id="soumisImpotSource" name="soumisImpotSource" value="<%=viewBean.getSoumisImpotSource().booleanValue() ? "on" : "off"%>"/>
									</TD>									
									<TD></TD>
									<TD></TD>									
								</TR>	
								<TR id="detailImpotSource">
									<TD><ct:FWLabel key="JSP_CANTON_IMPOT_SOURCE"/></TD>
									<td>
										<%if (JadeStringUtil.isBlankOrZero(viewBean.getCsCantonImpositionSource())) {%>
												<ct:FWCodeSelectTag codeType="PYCANTON" name="csCantonImpositionSourceAffiche" defaut="<%=viewBean.getDefaultCsCantonImposition()%>" wantBlank="true"/>
												<input type="hidden" id="csCantonImpositionSource" name="csCantonImpositionSource" value="<%=viewBean.getDefaultCsCantonImposition()%>" />
										<%} else {%>
												<ct:FWCodeSelectTag codeType="PYCANTON" name="csCantonImpositionSourceAffiche" defaut="<%=viewBean.getCsCantonImpositionSource()%>" wantBlank="true"/>
												<input type="hidden" id="csCantonImpositionSource" name="csCantonImpositionSource" value="<%=viewBean.getCsCantonImpositionSource()%>" />
										<%}%>
									</td>
									<TD><ct:FWLabel key="JSP_TAUX_IMPOT_SOURCE"/></TD>
									<TD>
										<INPUT type="text" id="tauxImpositionAffiche" value="<%=viewBean.getTauxImpositionSource()%>"  class="montant" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);">
										<input type="hidden" id="tauxImposition" name="tauxImpositionSource" value="<%=viewBean.getTauxImpositionSource()%>" />
									</TD>
								</TR>
								<TR>																										
									<TD>
										<%if(!((!viewBean.isNew() && (viewBean.isAit() ||
											                        viewBean.isAllocAssist())) 
											 ||
											 (viewBean.isNew() && (IIJPrononce.CS_ALLOC_ASSIST.equals(csTypeIJ) || 
															       IIJPrononce.CS_ALLOC_INIT_TRAVAIL.equals(csTypeIJ))))){ %>
										<ct:FWLabel key="JSP_AVEC_DECISION"/>
										<%}%>
										&nbsp;
									</TD>
									<TD align="left">
										<%if((!viewBean.isNew() && (viewBean.isAit() ||
											                        viewBean.isAllocAssist())) 
											 ||
											 (viewBean.isNew() && (IIJPrononce.CS_ALLOC_ASSIST.equals(csTypeIJ) || 
															       IIJPrononce.CS_ALLOC_INIT_TRAVAIL.equals(csTypeIJ)))){ %>
											<INPUT type="hidden" name="avecDecision" value="off">
										<%}else{ %>
										<%
											boolean isAvecDecisionChecked = false;
											if(JadeStringUtil.isBlankOrZero(viewBean.getIdPrononce())){
												isAvecDecisionChecked = "true".equalsIgnoreCase(IJApplication.getApplication(IJApplication.DEFAULT_APPLICATION_IJ).getProperty(IJApplication.PROPERTY_PRONONCE_AVEC_DECISION));
											} else {
												isAvecDecisionChecked = viewBean.getAvecDecision().booleanValue();
											}
										%>
											<INPUT type="checkbox" value="on" id="avecDecisionCheckbox" <%=isAvecDecisionChecked ? "checked" : "" %> />
											<input type="hidden" id="avecDecision" name="avecDecision" value="<%=isAvecDecisionChecked ? "on" : "off" %>" />
										<%}%>
									</TD>
									<TD></TD>
									<TD></TD>
								</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<INPUT type="button" value="<ct:FWLabel key="JSP_ARRET"/> (alt+<ct:FWLabel key="AK_REQ_ARRET"/>)" onclick="arret()" accesskey="<ct:FWLabel key="AK_REQ_ARRET"/>">
				<INPUT type="button" value="<ct:FWLabel key="JSP_SUIVANT"/> (alt+<ct:FWLabel key="AK_REQ_SUIVANT"/>)" onclick="if(validate()) action(COMMIT);" accesskey="<ct:FWLabel key="AK_REQ_SUIVANT"/>">
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>