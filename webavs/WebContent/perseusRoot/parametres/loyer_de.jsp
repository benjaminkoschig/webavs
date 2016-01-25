<%@page import="globaz.perseus.utils.parametres.PFParametresHandler"%>
<%@page import="ch.globaz.perseus.business.models.parametres.SimpleZone"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%@ page import="globaz.perseus.vb.parametres.PFLoyerViewBean" %>
<%@page import="globaz.jade.persistence.model.JadeAbstractModel"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@page import="globaz.framework.secure.FWSecureConstants"%>

<%-- tpl:insert attribute="zoneInit" --%>

<%//Les labels de cette page commence par le préfix "JSP_PF_PARAM_LOYER"
idEcran="PPF0921";
PFLoyerViewBean viewBean = (PFLoyerViewBean) session.getAttribute ("viewBean");

autoShowErrorPopup = true;

if(objSession.hasRight("perseus", FWSecureConstants.ADD)){
	bButtonDelete = true;
	bButtonCancel = true;
	bButtonValidate = true;
}else{
	bButtonDelete = false;
	bButtonCancel = false;
	bButtonValidate = false;
}

%>

<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneScripts" --%>

<%@ include file="/theme/detail/javascripts.jspf" %>
<script type="text/javascript">

var url = "perseus.parametres.loyer";
var messageDelete = "<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>";

var userAction;
function add() {}
function del() {
	if (window.confirm("<%=objSession.getLabel("JSP_PF_PARAM_LOYER_SUPPRESSION_CONFIRMATION")%>")){
        document.forms[0].elements('userAction').value="perseus.parametres.loyer.supprimer";
        document.forms[0].submit();
    }
}
function init() {

}
function upd() {
	
}
function postInit() {
	
}
function validate() {
	 state = validateFields();
	 if (document.forms[0].elements('_method').value == "add")
	   document.forms[0].elements('userAction').value="perseus.parametres.loyer.ajouter";
	 else if (document.forms[0].elements('_method').value == "upd")
	   document.forms[0].elements('userAction').value="perseus.parametres.loyer.modifier";
	    
	 return state;
}

function cancel() {
	if (document.forms[0].elements('_method').value == "upd")
		document.forms[0].elements('userAction').value="perseus.parametres.loyer.chercher";
}

</script>
<%@ include file="/perseusRoot/ajax/javascriptsAndCSS.jspf" %>

<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:insert attribute="zoneTitle" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:insert attribute="zoneMain" --%> 
<tr>
	<td>
		<table>
			<tr>
		  		<td>
		  			<label for="loyer.simpleLoyer.idZone">
						<ct:FWLabel key="JSP_PF_PARAM_LOYER_ID_ZONE" />
					</label>
				</td>
				<td>
		 			<ct:select 
		 				name="loyer.simpleZone.idZone" 
		 				wantBlank="true" 
		 				notation="data-g-select='mandatory:true'"
		 				defaultValue="">
					    	<%for( JadeAbstractModel model : PFParametresHandler.getListZone()){ 
					     		SimpleZone simpleZone = (SimpleZone)model;
					    	 %>
							<ct:option 
								value="<%=simpleZone.getId()%>" 
								label="<%=simpleZone.getDesignation() %>"/>
							<%} %>
					</ct:select>
				</td>
			</tr>
			<td>&nbsp;</td>
			<tr>
				<td>
					<label for="loyer.simpleLoyer">
						<ct:FWLabel key="JSP_PF_PARAM_LOYER_CODE_SYSTEME" />
					</label>
				</td>
				<td>
					<ct:select 
						name="loyer.simpleLoyer.csTypeLoyer" 
						wantBlank="true" 
						notation="data-g-select='mandatory:true'"
						defaultValue="">
						<ct:optionsCodesSystems csFamille="PFTYPELOY"/>
					</ct:select>
				</td>
				<td colspan="1"> &nbsp; </td>
				<td>
					<label for="loyer.simpleLoyer.montant">
						<ct:FWLabel key="JSP_PF_PARAM_LOYER_MONTANT" />
					</label>
				</td>
				<td>
					<ct:inputText 
						name="loyer.simpleLoyer.montant" 
						notation="data-g-amount='mandatory:true, blankAsZero:false'"
					   	id="loyer.simpleLoyer.montant"/>
				</td>
			</tr>
			<td>&nbsp;</td>
			<tr>
				<td>
					<label for="loyer.simpleLoyer.dateDebut">
						<ct:FWLabel key="JSP_PF_PARAM_LOYER_DATE_DEBUT" />
					</label>
				</td>
				<td>
				 	<input width="15" 
				 	   type="text"
					   name="setDateDebutConverter" 
					   id="loyer.simpleLoyer.dateDebut" 
					   value="<%=viewBean.getDateDebutConverter()%>" 
					   data-g-calendar="mandatory:true, type:month"/>				 
				</td>
				<td colspan="1"> &nbsp; </td>
				<td>
					<label for="loyer.simpleLoyer.dateFin">
						<ct:FWLabel key="JSP_PF_PARAM_LOYER_DATE_FIN" />
					</label>
				</td>
				<td>
					<input width="15" 
					   name="setDateFinConverter" 
					   id="loyer.simpleLoyer.dateFin" 
					   value="<%=viewBean.getDateFinConverter()%>" 
					   data-g-calendar="mandatory:false, type:month"/>
				</td>
			</tr>
		</table>
	</td>
</tr>

						<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>