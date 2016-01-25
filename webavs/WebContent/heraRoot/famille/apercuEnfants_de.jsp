<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
			<%@ page import="globaz.commons.nss.*"%>
		<script type="text/javascript"
			src="<%=servletContext%>/scripts/nss.js"></script>
		<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
		<%

	idEcran="GSF0006";
	globaz.hera.vb.famille.SFApercuEnfantsViewBean viewBean = (globaz.hera.vb.famille.SFApercuEnfantsViewBean)session.getAttribute("viewBean");
	String idConjoints = request.getParameter("idConjoints");
	boolean isModifiable = true;
	String modifiable = "";
	if (!globaz.globall.util.JAUtil.isIntegerEmpty(viewBean.getIdTiers())) {
		isModifiable = false;
		modifiable = " class=\"disabled\" readonly";
	}
	%>
		<%-- /tpl:put --%>
	<%-- tpl:put name="zoneBusiness" --%>
		<%-- /tpl:put --%>
	<%@ include file="/theme/detail/javascripts.jspf" %>
	<%-- tpl:put name="zoneScripts" --%>
			<SCRIPT>
	function init(){ 
	}
	function add() {
		nssUpdateHiddenFields();
	    document.forms[0].elements('userAction').value="hera.famille.apercuEnfants.ajouterEnfant&idConjoints=";
	}
	function validate() {
		nssUpdateHiddenFields();
	    state = validateFields();
	    if (document.forms[0].elements('_method').value == "add")
	        document.forms[0].elements('userAction').value="hera.famille.apercuEnfants.ajouterEnfant";
	    else
	        document.forms[0].elements('userAction').value="hera.famille.apercuEnfants.modifier";   
	    return state;
	
	}
	function upd() {
		nssUpdateHiddenFields();
		cantonDomicileChange();
	}
	
	
	function cancel() {
	    document.forms[0].elements('userAction').value="back";
	}
	
	function del() {
	    if (window.confirm("<ct:FWLabel key='ERROR_DELETE_ENFANT_CONFIRMATION'/>")){
	        document.forms[0].elements('userAction').value="hera.famille.apercuEnfants.supprimerEnfant";
	        document.forms[0].submit();
	    }
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
							
				document.getElementById("nss").value=document.getElementById("likeNSS").value;
	}


	function nssChange(tag) {
		if(tag.select==null) {											
		}
		else {
			_nss = removeDots(tag.select.options[tag.select.selectedIndex].nss);

			if (_nss.length == 13) {	
				//formatte tag avec nouveau nss
				nssCheckChar(43, 'likeNSS');nssAction('likeNSS');concatPrefixAndPartial('likeNSS');
			}
			else {
				//formatte tag avec ancien nss
				nssCheckChar(45, 'likeNSS');nssAction('likeNSS');concatPrefixAndPartial('likeNSS');
			}
		
		
  			var element = tag.select.options[tag.select.selectedIndex];
			if (element.nss!=null)
				document.getElementById("nss").value=element.nss;
			
        	
			if (element.nom!=null) {
				document.getElementById("nom").value=element.nom;
				document.getElementById("nomAffiche").value=element.nom;
			}
				
			if (element.prenom!=null)		    	        					
				document.getElementById("prenom").value=element.prenom;
				document.getElementById("prenomAffiche").value=element.prenom;
			
			if (element.nom!=null && element.prenom!=null)
				document.getElementById("nomPrenom").value=element.nom + " " + element.prenom;
				
			
			if (element.provenance!=null)
				document.getElementById("provenance").value=element.provenance;
			
			if (element.id!=null)
				document.getElementById("idAssure").value=element.idAssure;
					
			
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
				document.getElementById("dateDeces").value=element.dateDeces;
			}

						    	        				
			if ('<%=globaz.hera.tools.nss.SFUtil.PROVENANCE_TIERS%>'==element.provenance) {
				document.getElementById("nomAffiche").disabled=true;				    	        				
				document.getElementById("prenomAffiche").disabled=true;
				document.getElementById("csNationaliteAffiche").disabled=true;
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
			if(value){
				disable($pays);
			}
			else {
				enable($pays);
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
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_AJOUT_ENFANT"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
			<input type="hidden" name="idConjoints" value="<%=idConjoints%>">
			<input type="hidden" name="idEnfant" value="<%=viewBean.getIdEnfant()%>" />
			<input type="hidden" name="idMembreFamille" value="<%=viewBean.getIdMembreFamille()%>" />
			<tr>	
				<td><ct:FWLabel key="JSP_NSS"/></td>		
				<td>
				<% if (isModifiable) {
					globaz.hera.tools.nss.SFUtil.checkNSSCompliance(viewBean);
					String params = "&provenance1=" + globaz.hera.tools.nss.SFUtil.PROVENANCE_TIERS;	
					params += "&provenance2=" + globaz.hera.tools.nss.SFUtil.PROVENANCE_CI;
					String jspLocation = servletContext + "/heraRoot/numeroSecuriteSocialeSF_select.jsp";
					%>
					<ct1:nssPopup name="likeNSS" onFailure="nssFailure();" onChange="nssChange(tag);" params="<%=params%>" 
						value="<%=viewBean.getNumeroAvsFormateSansPrefixe()%>" newnss="<%=viewBean.isNNSS()%>" 
						jspName="<%=jspLocation%>" avsMinNbrDigit="3" nssMinNbrDigit="3"  avsAutoNbrDigit="11" nssAutoNbrDigit="10"  />										
					<SCRIPT language="javascript">	    				        	
			        	document.getElementById("likeNSS").onkeypress= new Function ("","return filterCharForPositivFloat(window.event);");	
            		</SCRIPT>
					<INPUT type="hidden" name="nss" value="<%=viewBean.getNss()%>"/>
            		<INPUT type="text" name="nomPrenom" value="<%=viewBean.getNom()%> <%=viewBean.getPrenom()%>" class="libelleLongDisabled"/>				
				<% } else { %>
						<INPUT type="text" name="nss" value="<%=viewBean.getNss()%>" <%=modifiable%>/> 
						<INPUT type="hidden" name="likeNSS" value="<%=viewBean.getNss()%>" <%=modifiable%>/> 						
				<% } %>
					<INPUT type="hidden" name="idAssure" value="<%=viewBean.getIdAssure()%>"/>
					<INPUT type="hidden" name="provenance" value="<%=viewBean.getProvenance()%>"/>										
				</td>
				<TD>
				<% if (isModifiable) { %>
					<ct:FWSelectorTag name="selecteurEmployeur"	
						methods="<%=viewBean.getMethodesSelectionBeneficiaire()%>"
						providerApplication="pyxis"
						providerPrefix="TI"
						providerAction="pyxis.tiers.tiers.chercher"
						target="fr_main"
						redirectUrl="<%=mainServletPath%>"/>
				<% } %>
				</TD>
			</tr>
            <tr>						  			
	            <td><ct:FWLabel key="JSP_ENFANTS_NOM"/></td>
				<td>
					<input type="hidden" name="nom" value="<%=viewBean.getNom()%>"/>
					<INPUT type="text" name="nomAffiche" value="<%=viewBean.getNom()%>" <%=modifiable%>/>	
				</td>
				<td><ct:FWLabel key="JSP_ENFANTS_PRENOM"/></td>
				<td>
					<input type="hidden" name="prenom" value="<%=viewBean.getPrenom()%>"/>
					<input type="text" name="prenomAffiche" value="<%=viewBean.getPrenom()%>" <%=modifiable%>/>
				</td>	
				<td><ct:FWLabel key="JSP_ENFANTS_SEXE"/></td>		 
				<td>
				<% if (isModifiable) { %>
					<ct:FWCodeSelectTag name="csSexeAffiche"
						wantBlank="<%=true%>"
						codeType="PYSEXE"
						defaut="<%=viewBean.getCsSexe()%>"/>
				<% } else { %>
					<input type="text" name="csSexeAffiche" value="<%=viewBean.getCsSexe()%>" <%=modifiable%>/>
				<% } %>
					<INPUT type="hidden" name="csSexe" value="<%=viewBean.getCsSexe()%>"/>			
			 	</td>										
			</tr>
			<tr>
				<td><ct:FWLabel key="JSP_ENFANTS_DATEN"/></td>
				<td>
					<% if (isModifiable) { %>
						<ct:FWCalendarTag name="dateNaissanceAffiche" value="<%=viewBean.getDateNaissance()%>"/>
					<% } else { %>
						<input type="text" name="dateNaissanceAffiche" value="<%=viewBean.getDateNaissance()%>" <%=modifiable%>/>
					<% } %>
					<INPUT type="hidden" name="dateNaissance" value="<%=viewBean.getDateNaissance()%>"/>
				</td>
				<td><ct:FWLabel key="JSP_ENFANTS_DATEM"/></td>
				<td>
					<% if (isModifiable) { %>
						<ct:FWCalendarTag name="dateDecesAffiche" value="<%=viewBean.getDateDeces()%>"/>
					<% } else { %>
						<input type="text" name="dateDecesAffiche" value="<%=viewBean.getDateDeces()%>" <%=modifiable%>/>
					<% } %>
					<INPUT type="hidden" name="dateDeces" value="<%=viewBean.getDateDeces()%>"/>	
				</td>
				<td><ct:FWLabel key="JSP_ENFANTS_DATEA"/></td>
				<td><ct:FWCalendarTag name="dateAdo" value="<%=viewBean.getDateAdoption()%>"/></td>
			</tr>
			<tr>
				<td><ct:FWLabel key="JSP_ENFANTS_CANTON"/></td>
				<td>
					<ct:FWCodeSelectTag name="csCantonDomicileAffiche"
						wantBlank="<%=true%>"
						codeType="PYCANTON"
						defaut="<%=viewBean.getCsCantonDomicile()%>"/>
					<INPUT type="hidden" name="csCantonDomicile" value="<%=viewBean.getCsCantonDomicile()%>"/>
				</td>
				<td><ct:FWLabel key="JSP_ENFANTS_NATIONALITE"/></td>
				<td>
					<% if (isModifiable) { %>
						<ct:FWListSelectTag name="csNationaliteAffiche" data="<%=viewBean.getTiPays()%>" defaut="<%=globaz.globall.util.JAUtil.isIntegerEmpty(viewBean.getCsNationalite())?globaz.pyxis.db.adressecourrier.TIPays.CS_SUISSE:viewBean.getCsNationalite()%>"/>
					<% } else { %>
						<input type="text" name="csNationaliteAffiche" value="<%=viewBean.getLibelleNationnalite()%>" <%=modifiable%>/>
					<% } %>
					<input type="hidden" name="csNationalite" value="<%=viewBean.getCsNationalite()%>" />
				</td>
			</tr>   
			<tr>
				<td><label for="pays"><ct:FWLabel key="JSP_PAYS_DOMICILE"/></label></td>
				<td>
					<ct:FWListSelectTag name="pays"
						data="<%=viewBean.getPaysDomicile()%>" 
						defaut="<%=viewBean.getPays()%>"  />									    
				</td>	
			</tr>
			
			
			
			
			
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>