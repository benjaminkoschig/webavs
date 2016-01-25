
<%@page import="globaz.al.vb.attribut.ALAffilieViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>
<%
	ALAffilieViewBean viewBean = (ALAffilieViewBean) session.getAttribute("viewBean"); 
	selectedIdValue=viewBean.getId();
	btnUpdLabel = objSession.getLabel("MODIFIER");
	btnDelLabel = objSession.getLabel("SUPPRIMER");
	btnValLabel = objSession.getLabel("VALIDER");
	btnCanLabel = objSession.getLabel("ANNULER");
	btnNewLabel = objSession.getLabel("NOUVEAU");

	bButtonNew = false;
	bButtonValidate = true;
	bButtonCancel = true;
	bButtonDelete = false;
	
	idEcran="AL0021";
%>
<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>
<%@page import="globaz.fweb.util.JavascriptEncoder"%>
<%@page import="globaz.jade.client.util.JadeUtil"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="ch.globaz.al.business.models.attribut.AttributEntiteModel"%>
<%@page import="ch.globaz.param.business.models.ParameterModel"%>

<%@page import="ch.globaz.al.business.constantes.ALCSAffilie"%>
<script type="text/javascript">
function add() {
    document.forms[0].elements('userAction').value="al.attribut.affilie.ajouter";
}
function upd() {
    document.forms[0].elements('userAction').value="al.attribut.affilie.modifier";
}
function validate() {
	
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="al.attribut.affilie.ajouter";
    else 
        document.forms[0].elements('userAction').value="al.attribut.affilie.modifier";
    return state;
}

function cancel() {
	var methodElement = document.forms[0].elements('_method');
	action(methodElement.value);
	if(methodElement.value == ADD) {
		document.forms[0].elements('userAction').value="al.dossier.dossierMain.afficher";
	} else {
        document.forms[0].elements('userAction').value="al.attribut.affilie.afficher";
	}
}
function del() {
	var msgDelete = '<%=JavascriptEncoder.getInstance().encode(objSession.getLabel("MESSAGE_SUPPRESSION"))%>';
    if (window.confirm(msgDelete)){
        document.forms[0].elements('userAction').value="al.attribut.affilie.supprimer";
        document.forms[0].submit();
    }
}

function init(){
	
	//document.getElementsByClassName("revenuAlloc").style.display="none";
	//document.getElementsByClassName("revenuConj").style.display="block";
}

function postInit(){
}

</script>
<%-- /tpl:insert --%>


<%@ include file="/theme/detail/bodyStart.jspf" %>
				
			<%-- tpl:insert attribute="zoneTitle" --%>
			<ct:FWLabel key="AL0021_TITRE"/>
			<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
			<tr><td>
				<%-- tpl:insert attribute="zoneMain" --%>
	     		
	     	<table class="zone" id="AL0021affilieZone">
	            <tr>
	                <td class="subtitle" colspan="2">		
	                		<ct:FWLabel key="AL0021_AFFILIE_ZONE"/>
	                </td>
	            </tr>
	            <tr>
	                <td class="label"><ct:FWLabel key="AL0021_AFFILIE_NUMERO"/></td>
	                <td><input type="text" value="<%=viewBean.getAffilie().getAffilieNumero() %>" disabled="disabled" class="readOnly" readonly="readonly"/></td> 
	            </tr>
	            <tr>
	                <td class="label"><ct:FWLabel key="AL0021_AFFILIE_NOM"/></td>
	                <td><input type="text" value="<%=viewBean.getAffilie().getRaisonSociale() %>" disabled="disabled" class="readOnly" readonly="readonly"/></td>     
	            </tr>
	              
            </table>
	     	
	     	
	     	<table class="zone" id="AL0021attributsZone">
   
	            <tr>
	               	<td class="subtitle" colspan="2">
	                	<ct:FWLabel key="AL0021_ATTRIBUTS_ZONE"/>
	                </td>
	            </tr>
	            
	             <tr>
	                <td class="label"><ct:FWLabel key="AL0021_ATTRIBUT_RECAPFORMAT"/></td> 
	                <td>
	                	
	                
	    				<ct:select name="attributFormatRecap" styleClass="longSelect" tabindex="1">
							<ct:optionsCodesSystems csFamille="ALATTREN">
								<ct:excludeCode code="<%=ALCSAffilie.ATTRIBUT_AVIS_ECH_AFFILIE %>"/>
								<ct:excludeCode code="<%=ALCSAffilie.ATTRIBUT_AVIS_ECH_ALLOCATAIRE %>"/>
								<ct:excludeCode code="<%=ALCSAffilie.ATTRIBUT_SANS_AVIS_ECH %>"/>
								<ct:excludeCode code="<%=ALCSAffilie.ATTRIBUT_RECAP_ENVOI_COURRIER %>"/>
								<ct:excludeCode code="<%=ALCSAffilie.ATTRIBUT_RECAP_ENVOI_EBUSINESS %>"/>
							</ct:optionsCodesSystems>
						</ct:select>
	                </td>
	             </tr>
	             <tr>
	                <td class="label"><ct:FWLabel key="AL0021_ATTRIBUT_RECAPENVOI"/></td> 
	                <td>
	                	
	                	<input type="text" value="Récap envoyée par courier" disabled="disabled" class="readOnly" readonly="readonly" size="26"/>
						<%--
						<ct:select name="attributEnvoiRecap" styleClass="longSelect" tabindex="2">
							<ct:optionsCodesSystems csFamille="ALATTREN">
								<ct:excludeCode code="<%=ALCSAffilie.ATTRIBUT_AVIS_ECH_AFFILIE %>"/>
								<ct:excludeCode code="<%=ALCSAffilie.ATTRIBUT_AVIS_ECH_ALLOCATAIRE %>"/>
								<ct:excludeCode code="<%=ALCSAffilie.ATTRIBUT_SANS_AVIS_ECH %>"/>
								<ct:excludeCode code="<%=ALCSAffilie.ATTRIBUT_RECAP_FORMAT_CSV %>"/>
								<ct:excludeCode code="<%=ALCSAffilie.ATTRIBUT_RECAP_FORMAT_PDF_CSV %>"/>
							</ct:optionsCodesSystems>
						</ct:select> --%>
	                </td>
	             </tr>
	             <tr>
	                <td class="label"><ct:FWLabel key="AL0021_ATTRIBUT_DESTAVISECH"/></td> 
	                <td>
	                
	    				<ct:select name="attributAvisEcheance" styleClass="longSelect" tabindex="3" >
							<ct:optionsCodesSystems csFamille="ALATTREN">
								<ct:excludeCode code="<%=ALCSAffilie.ATTRIBUT_RECAP_ENVOI_COURRIER %>"/>
								<ct:excludeCode code="<%=ALCSAffilie.ATTRIBUT_RECAP_ENVOI_EBUSINESS %>"/>
								<ct:excludeCode code="<%=ALCSAffilie.ATTRIBUT_RECAP_FORMAT_PDF %>"/>
								<ct:excludeCode code="<%=ALCSAffilie.ATTRIBUT_RECAP_FORMAT_CSV %>"/>
								<ct:excludeCode code="<%=ALCSAffilie.ATTRIBUT_RECAP_FORMAT_PDF_CSV %>"/>
							</ct:optionsCodesSystems>
						</ct:select>
				
	                </td>
	             </tr>
	             <tr>
	                <td class="label"><ct:FWLabel key="AL0021_ATTRIBUT_PMTDIRECT"/></td> 
	                <td>	
	    				<ct:inputHidden name="attributPaiementDirect"/>
	                 	<% if("true".equals(viewBean.getAttributPaiementDirect())){ %> 
	                    	<input type="checkbox" checked="checked" name="attributPaiementDirectValue" tabindex="4"
	                    		onclick="document.getElementById('attributPaiementDirect').value=this.checked;"   		
	                    	/>
	                    	
	                    <%}else{%>
	                    	<input type="checkbox" tabindex="17" name="attributPaiementDirectValue" onclick="document.getElementById('attributPaiementDirect').value=this.checked;"/>
	                    <%}%>

	                </td>
	             </tr>
	             <tr>
	                <td class="label"><ct:FWLabel key="AL0021_ATTRIBUT_DISPNUMSAL"/></td> 
	                <td>
	    				<ct:inputHidden name="attributAffichageNumSalarie"/>
	                 	<% if("true".equals(viewBean.getAttributAffichageNumSalarie())){ %> 
	                    	<input type="checkbox" checked="checked" name="attributAffichageNumSalarieValue" tabindex="5"
	                    		onclick="document.getElementById('attributAffichageNumSalarie').value=this.checked;"   		
	                    	/>
	                    	
	                    <%}else{%>
	                    	<input type="checkbox" tabindex="17" name="attributAffichageNumSalarieValue" onclick="document.getElementById('attributAffichageNumSalarie').value=this.checked;"/>
	                    <%}%>
	    				
	                </td>
	             </tr>

            </table>
			<%-- /tpl:insert --%>
			</td></tr>
									
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>

<% if(!JadeStringUtil.isEmpty(request.getParameter("idDossier"))){ %>
<ct:menuChange displayId="options" menuId="default-detail" showTab="options" checkAdd="no">
	<ct:menuSetAllParams key="idDossier" checkAdd="no" value='<%=request.getParameter("idDossier")%>'  />
</ct:menuChange>
<%} %>

<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>
