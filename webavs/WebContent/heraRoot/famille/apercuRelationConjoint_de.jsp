<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
			<%@ page import="globaz.commons.nss.*"%>
		<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.hera.api.ISFSituationFamiliale"%>

<%@page import="globaz.framework.bean.FWViewBeanInterface"%><script type="text/javascript"
			src="<%=servletContext%>/scripts/nss.js"></script>
		<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>

<%
	
	idEcran = "GSF0007";
	bButtonCancel = false;
	String btnNewRelLabel = objSession.getLabel("JSP_APERCU_CONJOINT_NOUVELLE_RELATION");
	globaz.hera.vb.famille.SFApercuRelationConjointViewBean viewBean = (globaz.hera.vb.famille.SFApercuRelationConjointViewBean) session.getAttribute("viewBean");
	String idRelationConjoint = request.getParameter("idRelationConjoint");
	String idMembreFamille = request.getParameter("idMembreFamille");
	globaz.hera.db.famille.SFMembreFamille membre = viewBean.getMembreFamilleMembre(idMembreFamille);
	
	boolean bButtonNewRelation = !viewBean.isNew();
	
	
%>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT>
	function init(){ 
	//    proceedButton(btnNewRel.id);	
		<%if (bButtonNewRelation) {%>
	    	jscss("add", btnNewRel, "inactive", null);
	    <%}%>
	//	btnNewRel.active = "false";
	}
	function add() {
		nssUpdateHiddenFields();
	    document.forms[0].elements('userAction').value="hera.famille.apercuRelationConjoint.ajouterRelation"
	}
	function ajouterConjointInconnu(){
		document.forms[0].elements('userAction').value="hera.famille.apercuRelationConjoint.ajouterConjointInconnu";
		action(COMMIT);
	}

	function nouvelleRelation(){
		document.forms[0].elements('userAction').value="hera.famille.apercuRelationConjoint.nouvelleRelation";
		document.forms[0].submit();
	}
	function validate() {
		nssUpdateHiddenFields();
	    state = validateFields();
	    if (document.forms[0].elements('_method').value == "add")
	        document.forms[0].elements('userAction').value="hera.famille.apercuRelationConjoint.ajouterRelation";
	    else if (document.forms[0].elements('_type').value == "addNewRelation")
	    	document.forms[0].elements('userAction').value="hera.famille.apercuRelationConjoint.nouvelleRelation";
		else 
	        document.forms[0].elements('userAction').value="hera.famille.apercuRelationConjoint.modifier";
	    
	    
	    return state;
	
	}
	function upd() {
		nssUpdateHiddenFields();
	//	btnNewRel.active = "true";	
		<%if (bButtonNewRelation) {%>
		    jscss("remove", btnNewRel, "inactive", null);
		    btnNewRel.style.display = "inline";    
		    btnNewRel.onclick=nouvelleRelation;
	    <%}%>
	}


	function cancel() {
	 if (document.forms[0].elements('_method').value == "add")
	    document.forms[0].elements('userAction').value="back";
	 else
	    document.forms[0].elements('userAction').value="hera.famille.apercuRelationConjoint.afficher"
	}
	
	function del() {
	    if (window.confirm("<ct:FWLabel key='ERROR_DELETE_RELATION_CONFIRMATION'/>")){
	        document.forms[0].elements('userAction').value="hera.famille.apercuRelationConjoint.supprimer";
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
		document.getElementById("pays").disabled=false;					
	}
	
	
	function nssUpdateHiddenFields() {
			try {
				document.getElementById("nom").value=document.getElementById("nomAffiche").value;
				document.getElementById("prenom").value=document.getElementById("prenomAffiche").value;
				
				document.getElementById("csNationalite").value=document.getElementById("csNationaliteAffiche").value;
				document.getElementById("csCantonDomicile").value=document.getElementById("csCantonDomicileAffiche").value;	
				
				document.getElementById("dateNaissance").value=document.getElementById("dateNaissanceAffiche").value;	
				document.getElementById("dateDeces").value=document.getElementById("dateDecesAffiche").value;	
				document.getElementById("csSexe").value=document.getElementById("csSexeAffiche").value;	
							
				document.getElementById("nss").value=document.getElementById("likeNSS").value;
			} catch (ex){}	
			
	}
	
	
	function onChangeNationalite() {
		//if(document.getElementById("csNationaliteAffiche").value!='100') {
		//	document.getElementById("csCantonDomicileAffiche").value=505027;
		//}
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
			
			if (element.dateDeces!=null && element.dateDeces!="null") {
				document.getElementById("dateDeces").value=element.dateDeces;
				document.getElementById("dateDecesAffiche").value=element.dateDeces;
			}else{
				document.getElementById("dateDeces").value="";
				document.getElementById("dateDecesAffiche").value="";
			}
						    	        				
			if ('<%=globaz.hera.tools.nss.SFUtil.PROVENANCE_TIERS%>'==element.provenance) {
				document.getElementById("nomAffiche").disabled=true;				    	        				
				document.getElementById("prenomAffiche").disabled=true;
				document.getElementById("csNationaliteAffiche").disabled=true;
				document.getElementById("csCantonDomicileAffiche").disabled=true;
				document.getElementById("dateNaissanceAffiche").disabled=true;
				document.getElementById("dateDecesAffiche").disabled=true;
				document.getElementById("csSexeAffiche").disabled=true;
				document.getElementById("pays").disabled=true;
				
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
		cantonDomicileChange();
	});

</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_AJOUT_CONJOINT"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

			<%
				boolean conjointInconnu = false;
				String modifiable = "";
				boolean isModifiable = true;
				if(globaz.hera.api.ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU.equals(idMembreFamille)){
					modifiable = " class=\"disabled\" readonly";
					conjointInconnu = true;
					isModifiable = false;
				}
				// Si idTiers est renseigné, on ne peut pas modifier les attributs du membre
				if (!globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getIdTiersMembre(idMembreFamille)) || !viewBean.isNew()) {
					modifiable = " class=\"disabled\" readonly";
					isModifiable = false;
				}
			%>
	
	
	

			<%if(!conjointInconnu){%>
			<tr>	
				<td><ct:FWLabel key="JSP_NSS"/></td>		
				<TD colspan="3">
					<% if (!isModifiable) { %>
					 	<INPUT type="text" name="likeNSS" value="<%=membre.getNss()%>" <%=modifiable%>/>
					 	<INPUT type="hidden" name="nss" value="<%=membre.getNss()%>" /> 
					<% } else { 
						
						globaz.hera.tools.nss.SFUtil.checkNSSCompliance(membre);
						String params = "&provenance1=" + globaz.hera.tools.nss.SFUtil.PROVENANCE_TIERS;	
						params += "&provenance2=" + globaz.hera.tools.nss.SFUtil.PROVENANCE_CI;
						String jspLocation = servletContext + "/heraRoot/numeroSecuriteSocialeSF_select.jsp";
					%>										
						<ct1:nssPopup name="likeNSS" onFailure="nssFailure();" onChange="nssChange(tag);" params="<%=params%>" 
							value="<%=membre.getNumeroAvsFormateSansPrefixe()%>" newnss="<%=membre.isNNSS()%>" 
							jspName="<%=jspLocation%>" avsMinNbrDigit="3" nssMinNbrDigit="3" avsAutoNbrDigit="11" nssAutoNbrDigit="10"  />
						<SCRIPT language="javascript">	    				        	
				        	document.getElementById("likeNSS").onkeypress= new Function ("","return filterCharForPositivFloat(window.event);");	
	            		</SCRIPT>	
						<INPUT type="text" name="nomPrenom" value="<%=membre.getNom()%> <%=membre.getPrenom()%>" class="libelleLongDisabled"/>
						<INPUT type="hidden" name="nss" value="<%=membre.getNss()%>"/>
            		<% } %>
					<INPUT type="hidden" name="idAssure" value="<%=membre.getIdAssure()%>"/>
					<INPUT type="hidden" name="provenance" value="<%=membre.getProvenance()%>"/>										
            	</TD>
				<TD>
				<% if (isModifiable && !conjointInconnu) { %>
					<ct:FWSelectorTag
						name="selecteurEmployeur"
						
						methods="<%=viewBean.getMethodesSelectionBeneficiaire()%>"
						providerApplication="pyxis"
						providerPrefix="TI"
						providerAction="pyxis.tiers.tiers.chercher"
						target="fr_main"
						redirectUrl="<%=mainServletPath%>"/>
				<% } %>
				</TD>	
				<td>
					<ct:ifhasright element="hera.famille.apercuRelationConjoint.ajouterConjointInconnu" crud="c">
						<input  type="button" onclick="ajouterConjointInconnu();" value="<ct:FWLabel key="JSP_MEMBRE_CONJOINT_INCONNU"/>" />
					</ct:ifhasright>
				</td>	  				
			</tr>
			<%}%>
				<td><input type="hidden" name="idRelationConjoint" value="<%=viewBean.getIdRelationConjoint()%>"/></td>
				<td><input type="hidden" name="idMembreFamille" value="<%=idMembreFamille%>"/></td>
				<td><input type="hidden" name="idConjoints" value="<%=viewBean.getIdConjoints()%>"/></td>
            <tr>						  			
	            <td><ct:FWLabel key="JSP_MEMBRE_FAMILLE_NOM"/></td>
				<td><INPUT type="hidden" name="nom" value="<%=membre.getNom()%>"/>
					<input type="text" name="nomAffiche" value="<%=membre.getNom()%>" <%=modifiable%>/></td>
				<td><ct:FWLabel key="JSP_MEMBRE_FAMILLE_PRENOM"/></td>
				<td><INPUT type="hidden" name="prenom" value="<%=membre.getPrenom()%>"/>
					<input type="text" name="prenomAffiche" value="<%=membre.getPrenom()%>" <%=modifiable%>/></td>	
				<%if(!conjointInconnu){%>
					<td><ct:FWLabel key="JSP_MEMBRE_FAMILLE_SEXE"/></td>		 
					<td>
						<% if (isModifiable) { %>
							<ct:FWCodeSelectTag name="csSexeAffiche"
									wantBlank="<%=true%>"
								    codeType="PYSEXE" 
								    defaut="<%=membre.getCsSexe()%>"/>
						<% } else { %>
							<input type="text" name="csSexeAffiche" value="<%=objSession.getCodeLibelle(membre.getCsSexe())%>" <%=modifiable%>/>
						<% } %>
						<input type="hidden" name="csSexe" value="<%=membre.getCsSexe()%>" />
				 	</td>	
				<%} else {%>
					<TD>&nbsp;</TD>
					<TD>&nbsp;</TD>
				<%}%>									
			</tr>
			<%if(!conjointInconnu){%>			
			<tr>
				<td><ct:FWLabel key="JSP_MEMBRE_FAMILLE_DATEN"/></td>
				<td>
					<% if (globaz.jade.client.util.JadeStringUtil.isEmpty(modifiable)) { %>
						<ct:FWCalendarTag  name="dateNaissanceAffiche" value="<%=membre.getDateNaissance()%>"/>
					<% } else { %>
							<input type="text" name="dateNaissanceAffiche" value="<%=membre.getDateNaissance()%>" <%=modifiable%>/>
							
					<% } %>
					<input type="hidden" name="dateNaissance" value="<%=membre.getDateNaissance()%>" />
				</td>
				<td><ct:FWLabel key="JSP_MEMBRE_FAMILLE_DATEM"/></td>
				<td>
					<% if (globaz.jade.client.util.JadeStringUtil.isEmpty(modifiable)) { %>
						<ct:FWCalendarTag  name="dateDecesAffiche" value="<%=membre.getDateDeces()%>"/>
					<% } else { %>
							<input type="text" name="dateDecesAffiche" value="<%=membre.getDateDeces()%>" <%=modifiable%>/>
					<% } %>
					<input type="hidden" name="dateDeces" value="<%=membre.getDateDeces()%>" />
				</td>
			</tr>
			<tr>
				<td><ct:FWLabel key="JSP_MEMBRE_FAMILLE_NATIONALITE"/></td>
				<td>	
					<% if (isModifiable) { %>				
						<ct:FWListSelectTag name="csNationaliteAffiche" data="<%=membre.getTiPays()%>" defaut="<%=globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(membre.getCsNationalite())?globaz.pyxis.db.adressecourrier.TIPays.CS_SUISSE:membre.getCsNationalite()%>"/>
						<script language="javascript">
					 	  	element = document.getElementById("csNationaliteAffiche");
					 	  	element.onchange=function() {onChangeNationalite();}
					 	</script> 	
					 <% } else { %>
							<input type="text" name="csNationaliteAffiche" value="<%=viewBean.getLibellePays()%>" <%=modifiable%>/>
					 <% } %>
					
					<input type="hidden" name="csNationalite" value="<%=viewBean.getLibellePays() %>" />
				</td>
			
				<td><ct:FWLabel key="JSP_CANTON_DOMICILE"/></td>
				<td>
					<% if (isModifiable) { %>				
							<ct:FWCodeSelectTag name="csCantonDomicileAffiche"
									wantBlank="<%=true%>"
								      codeType="PYCANTON"
								      defaut="<%=membre.getCsCantonDomicile()%>"/>
					 <% } else { %>
							<input type="text" name="csCantonDomicileAffiche" value="<%=objSession.getCodeLibelle(membre.getCsCantonDomicile())%>" <%=modifiable%>/>
					 <% } %>

						<input type="hidden" name="csCantonDomicile" value="<%=objSession.getCodeLibelle(membre.getCsCantonDomicile())%>" />
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td><label for="pays"><ct:FWLabel key="JSP_PAYS_DOMICILE"/></label></td>
				<td>
					<ct:FWListSelectTag name="pays" 
						data="<%=viewBean.getPaysDomicile()%>" 
						defaut="<%=viewBean.getPays()%>" />									    
					
				</td>	
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
			<%}
			java.util.HashSet toExclude = new java.util.HashSet();
			toExclude.add(globaz.hera.api.ISFSituationFamiliale.CS_REL_CONJ_RELATION_INDEFINIE);			
			%>			
			<tr>
				<td><ct:FWLabel key="JSP_MEMBRE_FAMILLE_RELATION"/></td>
				<td>
					<ct:select name="typeRelation" wantBlank="false"  defaultValue="<%=JadeStringUtil.isBlankOrZero(viewBean.getTypeRelation())?globaz.hera.api.ISFSituationFamiliale.CS_ETAT_CIVIL_MARIE:viewBean.getTypeRelation()%>">
						<ct:optionsCodesSystems  csFamille="SFRELAT">
							<ct:forEach items="<%=toExclude%>" var="code">
								<ct:excludeCode id="code"/>
							</ct:forEach>
						</ct:optionsCodesSystems>
					</ct:select>
					
			 	</td>
				<td><ct:FWLabel key="JSP_MEMBRE_FAMILLE_DE"/> </td>
				<td><ct:FWCalendarTag name="dateDebut" value="<%=viewBean.getDateDebut()%>"/></td>
				<td colspan="2"> &nbsp;</td>
			</tr>
			<tr>
				<td colspan="2"> &nbsp;</td>
				<td><ct:FWLabel key="JSP_MEMBRE_FAMILLE_A"/></td>
				<td><ct:FWCalendarTag name="dateFin" value="<%=viewBean.getDateFin()%>"/></td>
				<%if(viewBean.isNew()){%>
				<td><ct:FWLabel key="JSP_MOTIF"/></td>					
				<td>					
					<ct:select name="motif" wantBlank="true"  defaultValue="">
						<ct:optionsCodesSystems csFamille="SFRELAT">
							<ct:excludeCode code="<%=ISFSituationFamiliale.CS_REL_CONJ_RELATION_INDEFINIE%>"/>
							<ct:excludeCode code="<%=ISFSituationFamiliale.CS_REL_CONJ_ENFANT_COMMUN%>"/>
							<ct:excludeCode code="<%=ISFSituationFamiliale.CS_REL_CONJ_MARIE%>"/>
						</ct:optionsCodesSystems>
					</ct:select>
				</td>
				<%}else{ %>
					<td colspan="2">&nbsp;</td>
				<%}%>
			</tr>
			<INPUT type="hidden" name="_type" value='<%=request.getParameter("_type")%>'>

						
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%if (bButtonNewRelation) {%>
					<ct:ifhasright element="hera.famille.apercuRelationConjoint.nouvelleRelation" crud="c">
						<INPUT class="btnCtrl inactive" id="btnNewRel" type="button" value="<%=btnNewRelLabel%>" onclick="nouvelleRelation();">
					</ct:ifhasright>
				<%}%>							
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<%if (request.getParameter("userAction")!=null && request.getParameter("userAction").endsWith("reAfficher") && bButtonNewRelation) {%>
	
	<script>		
	    jscss("remove", btnNewRel, "inactive", null);
	    btnNewRel.style.display = "inline";    
	    btnNewRel.onclick=nouvelleRelation;
	</script>
<%}%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>