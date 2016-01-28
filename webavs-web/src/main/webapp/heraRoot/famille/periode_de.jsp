<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@page import="globaz.hera.enums.TypeDeDetenteur"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>

<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
	<%@ page import="globaz.hera.helpers.famille.*"%> 
	<%
		globaz.hera.vb.famille.SFPeriodeViewBean viewBean =(globaz.hera.vb.famille.SFPeriodeViewBean)session.getAttribute("viewBean");
		String idMembreFamille = request.getParameter("idMembreFamille");
		idEcran="GSF0009";
		
		String warningMsg = "";
		if (FWViewBeanInterface.WARNING.equals(viewBean.getMsgType())) {
			warningMsg = viewBean.getMessage();
			viewBean.setMessage("");
			viewBean.setMsgType(FWViewBeanInterface.OK);
		}
	%>
<%-- /tpl:put --%>
	
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>

<%@ include file="/theme/detail/javascripts.jspf" %>

<%-- tpl:put name="zoneScripts" --%>
	<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
	<%@page import="globaz.jade.client.util.JadeStringUtil"%>

	<script>
		function init() {}
		
		function add() {}
		
		function validate() {
			state = true;
			if (document.getElementsByName('_method')[0].value == "add"){
				document.getElementsByName('userAction')[0].value="hera.famille.periode.ajouter";
			}
			else{
				document.getElementsByName('userAction')[0].value="hera.famille.periode.modifier";
			}
			return state;
		}
		
		function del() {
			if (window.confirm("<ct:FWLabel key='ERROR_DELETE_PERIODE_CONFIRMATION'/>")){
				document.getElementsByName('userAction')[0].value="hera.famille.periode.supprimer";
				document.getElementsByName('mainForm')[0].submit();
			}
		}
		
		function cancel() {
			if (document.getElementsByName('_method')[0].value == "add"){
				document.getElementsByName('userAction')[0].value="hera.famille.periode.chercher";
				document.getElementsByName('mainForm')[0].submit();
			}else{
				document.getElementsByName('userAction')[0].value="hera.famille.periode.afficher";
			}
		}
		
		function upd() {
			$selectTypeDeDetenteur.change();
		}
	
		function changeTypePeriode() {
			// Met le champ pays inactif sauf si la période est de type assurance étrangère
			if ($selectTypePeriode.val() == "<%=globaz.hera.api.ISFSituationFamiliale.CS_TYPE_PERIODE_ASSURANCE_ETRANGERE%>") {
				enable($selectPays);
				enable($dateDebut);
				disable($selectDetenteur);
				disable($selectTypeDeDetenteur);
			} 
		
			// Met le champ détenteur inactif sauf si la période est de garde BTE
			else if ($selectTypePeriode.val() == "<%=globaz.hera.api.ISFSituationFamiliale.CS_TYPE_PERIODE_GARDE_BTE%>") {
				disable($selectPays);
				enable($dateDebut);
				enable($selectTypeDeDetenteur);
				disable($selectDetenteur);
				initTypeDeDetenteurForBTE();
			} 
			
			// Enfant recueilli 
			else if ($selectTypePeriode.val() == "<%=globaz.hera.api.ISFSituationFamiliale.CS_TYPE_PERIODE_ENFANT%>") {
				disable($selectPays);
				enable($dateDebut);
				initTypeDeDetenteurForEnfant();
				enable($selectTypeDeDetenteur);
				disable($selectDetenteur);
			} 
			
			// si le type est "Certificat de vie" seule la date de fin est utilisable
			else if ($selectTypePeriode.val() == "<%=globaz.hera.api.ISFSituationFamiliale.CS_TYPE_PERIODE_CERTIFICAT_VIE%>") {
				disable($selectPays);
				disable($selectTypeDeDetenteur);
				disable($selectDetenteur);
				disable($dateDebut);
			}			
			// si le type est "Certificat de vie" seule la date de fin est utilisable
			else if ($selectTypePeriode.val() == "<%=globaz.hera.api.ISFSituationFamiliale.CS_TYPE_PERIODE_INCARCERATION%>") {
				disable($selectPays);
				disable($selectTypeDeDetenteur);
				disable($selectDetenteur);
				enable($dateDebut);
				enable($dateFin);
			}			
			else{
				disable($selectTypeDeDetenteur);
				disable($selectDetenteur);
			}
		}
		
		function initTypeDeDetenteurForBTE(){
			clearTypeDeDetenteur();
			
			var csTiers = "<%=TypeDeDetenteur.TIERS.getCodeSystem()%>";
			var valueTiers = '<%=objSession.getLabel(TypeDeDetenteur.TIERS.getLabelKey())%>';
			$selectTypeDeDetenteur.find('option').end().append('<option value="'+csTiers+'">'+valueTiers+'</option>');
			
			var csFamille = "<%=TypeDeDetenteur.FAMILLE.getCodeSystem()%>";
			var valueFamille = '<%=objSession.getLabel(TypeDeDetenteur.FAMILLE.getLabelKey())%>';
			$selectTypeDeDetenteur.find('option').end().append('<option value="'+csFamille+'">'+valueFamille+'</option>');
			
			$selectTypeDeDetenteur.val('');
		}
		
		function initTypeDeDetenteurForEnfant(){
			clearTypeDeDetenteur();
			
			var csTuteur = "<%=TypeDeDetenteur.TUTEUR_LEGAL.getCodeSystem()%>";
			var valueTuteur = '<%=objSession.getLabel(TypeDeDetenteur.TUTEUR_LEGAL.getLabelKey())%>';
			$selectTypeDeDetenteur.find('option').end().append('<option value="'+csTuteur+'">'+valueTuteur+'</option>');
			
			$selectTypeDeDetenteur.val('');
		}
		
		function changeTypeDeDetenteur(){
				if ($selectTypeDeDetenteur.val() == "<%=TypeDeDetenteur.FAMILLE.getCodeSystem()%>") {
					enable($selectDetenteur);
				}
				else {
					disable($selectDetenteur);				
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
		
		function clearTypeDeDetenteur(){
			 $selectTypeDeDetenteur
			     .find('option')
			     .remove()
			     .end().append('<option value=""></option>')
			     .val('');
		}
		
		function initSelectValues(){
			<%
			// En fonction du type de période (read) on va charger les bonnes données dans les combos csTypeDeDetenteur et idDetenteurBTE
			if(!JadeStringUtil.isBlankOrZero(viewBean.getType())){			
			    // Type de pPériode = BTE 
			    if(globaz.hera.api.ISFSituationFamiliale.CS_TYPE_PERIODE_GARDE_BTE.equals(viewBean.getType())){
			        %>
			        $selectTypeDeDetenteur.val(<%=viewBean.getCsTypeDeDetenteur()%>);
			        $selectDetenteur.val(<%=viewBean.getIdDetenteurBTE()%>);			       
			        <%
			    }
			    // Type de période = ENFANT
			    else if(globaz.hera.api.ISFSituationFamiliale.CS_TYPE_PERIODE_ENFANT.equals(viewBean.getType())){
			        %>
			        $selectTypeDeDetenteur.val(<%=viewBean.getCsTypeDeDetenteur()%>);
			        <%
			    }		
			}
			    
			%>
		}

		$(function() {
			$dateDebut = $("#dateDebut");
			$dateFin = $("#dateFin");
			$selectPays = $("#pays");
			$selectDetenteur = $("#idDetenteurBTE");
			$selectTypeDeDetenteur = $("#csTypeDeDetenteur");
			$selectTypePeriode = $("#type");
			changeTypePeriode();
			initSelectValues();
		});
		
	</script>
<%-- /tpl:put --%> 
	
<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>
				<ct:FWLabel key="JSP_PERIODE_TITLE"/>
<%-- /tpl:put --%>

<%@ include file="/theme/detail/bodyStart2.jspf" %>
					<%-- tpl:put name="zoneMain" --%>
						<input type="hidden" name="idMembreFamille" value="<%=idMembreFamille%>">
						<tr>
							<td>
								<ct:FWLabel key="JSP_PERIODE_TYPE"/>
							</td>
							<td>
								<ct:FWCodeSelectTag	name="type"
													wantBlank="<%=false%>"
													codeType="SFTYPPER"
													defaut="<%=viewBean.getType()%>"
													doClientValidation="' onchange='changeTypePeriode()"/>
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_PERIODE_DATED"/>
							</td>
							<td>
								<input	type="text" 
										id="dateDebut" 
										name="dateDebut" 
										value="<%=viewBean.getDateDebut()%>" 
										data-g-calendar="mandatory:false" />
							</td>
							<td>
								<ct:FWLabel key="JSP_PERIODE_DATEF"/>
							</td>
							<td>
								<input	type="text" 
										id="dateFin" 
										name="dateFin" 
										value="<%=viewBean.getDateFin()%>" 
										data-g-calendar="mandatory:false" />
							</td>
						</tr>
						
						<tr>
						
							<td>
								<ct:FWLabel key="JSP_PERIODE_ETAT"/>
							</td>
							<td>
								<ct:FWCodeSelectTag	name="pays" 
													wantBlank="<%=true%>" 
													codeType="CIPAYORI" 
													defaut="<%=viewBean.getPays()%>" />
							</td>
							
							
							<td>
								<ct:FWLabel key="JSP_PERIODE_TYPE_DETENTEUR"/>
							</td>
							<td>
								<select	name="csTypeDeDetenteur" id="csTypeDeDetenteur"
													defaut="<%=viewBean.getCsTypeDeDetenteur()%>"
													onchange="changeTypeDeDetenteur()"/>
							</td>
						</tr>
							
							
							<tr>
								<td></td>
								<td></td>
							<td>
								<ct:FWLabel key="JSP_PERIODE_DETENTEUR"/>
							</td>
							<%
								globaz.hera.helpers.famille.SFRequerantHelper rh = new globaz.hera.helpers.famille.SFRequerantHelper();
							%>
							<td>
								<ct:FWListSelectTag	name="idDetenteurBTE" 
													data="<%=rh.getDetenteur(idMembreFamille, viewBean.getIdDetenteurBTE(), objSession)%>"  
													defaut="<%=viewBean.getIdDetenteurBTE()%>" />
							</td>
						</tr>
					<%-- /tpl:put --%>
					
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
				
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>

<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>