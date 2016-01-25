<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>

<%
	globaz.osiris.db.ventilation.CAVPTypeDeProcedureOrdreViewBean viewBean = (globaz.osiris.db.ventilation.CAVPTypeDeProcedureOrdreViewBean) session.getAttribute("viewBean");
	selectedIdValue = viewBean.getTypeProcedureId();
	userActionValue = "osiris.osiris.ventilation.vPTypeDeProcedureOrdre.modifier";
	String jspLocation =  servletContext + mainServletPath + "Root/rubrique_select.jsp";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%idEcran = "GCA4023"; %>
<script>
	function del() {
	    if (window.confirm("<ct:FWLabel key="GCA4023_MESSAGE_SUPPRESSION"/>")){
	        document.forms[0].elements('userAction').value="osiris.ventilation.vPTypeDeProcedureOrdre.supprimer";
	        document.forms[0].submit();
	    }
	}
	function init(){
	}
	function upd() {
	}
	function add(){
	}

	function validate() {
	    state = validateFields();
	    if (document.forms[0].elements('_method').value == "add") {
	        document.forms[0].elements('userAction').value="osiris.ventilation.vPTypeDeProcedureOrdre.ajouter";
	    } else {
	        document.forms[0].elements('userAction').value="osiris.ventilation.vPTypeDeProcedureOrdre.modifier";
	    }
	    return state;
	}
	function cancel() {
		if (document.forms[0].elements('_method').value == "add") {
	  		document.forms[0].elements('userAction').value="back";
		} else {
	  		document.forms[0].elements('userAction').value="osiris.ventilation.vPTypeDeProcedureOrdre.afficher";
		}
	}

	function updateHiddenPenal() {
		if (document.getElementById('penal').checked) {
			document.getElementById('penalStr').value = "True";
		} else {
			document.getElementById('penalStr').value = "False";
		}
	}
</script>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="GCA4023_TITRE_ECRAN"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
						<tr>
							<td><ct:FWLabel key="GCA4023_TYPE_DE_PROCEDURE"/></td>
							<td> <ct:FWCodeSelectTag
							name="typeProcedure" defaut="<%=viewBean.getTypeProcedure()%>"
							codeType="OSITYPROC" wantBlank="false" /></td>
						</tr>
						<tr>
							<td><ct:FWLabel key="GCA4023_RUBRIQUE"/></td>
							<td><ct:FWPopupList
							validateOnChange="true" value="<%=viewBean.getNumeroRubrique()%>"
							name="numeroRubrique" size="15" className="visible" jspName="<%=jspLocation%>"
							minNbrDigit="3" autoNbrDigit="11"  />
							<input type="text" name="rubriqueDescription" id="rubriqueDescription" size="30" value="<%if (!JadeStringUtil.isBlankOrZero(viewBean.getIdRubrique())) {%><%=viewBean.getLibelleRubrique(viewBean.getIdRubrique())%><%}%>" class="libelleLongDisabled"  readonly tabindex="-1">
						</td>
						</tr>
						<tr>
						<td><ct:FWLabel key="GCA4023_RUBRIQUE_AMORTISSEMENT"/></td>
							<td><ct:FWPopupList
							validateOnChange="true" value="<%=viewBean.getNumeroRubriqueIrrec()%>"
							name="numeroRubriqueIrrec" size="15" className="visible" jspName="<%=jspLocation%>"
							minNbrDigit="3" autoNbrDigit="11" />
           					<input type="text" name="rubriqueIrrecDescription" id="rubriqueIrrecDescription" size="30" value="<%if (!JadeStringUtil.isBlankOrZero(viewBean.getIdRubriqueIrrecouvrable())) {%><%=viewBean.getLibelleRubrique(viewBean.getIdRubriqueIrrecouvrable())%><%}%>" class="libelleLongDisabled"  readonly tabindex="-1">
       					</td>
						</tr>
						<tr>
						<td><ct:FWLabel key="GCA4023_RUBRIQUE_RECOUVREMENT"/></td>
							<td><ct:FWPopupList
 							validateOnChange="true" value="<%=viewBean.getNumeroRubriqueRecouvrement()%>"
 							name="numeroRubriqueRecouvrement" size="15" className="visible" jspName="<%=jspLocation%>"
 							minNbrDigit="3" autoNbrDigit="11" />
           					<input type="text" name="rubriqueRecouvrementDescription" id="rubriqueRecouvrementDescription" size="30" value="<%if (!JadeStringUtil.isBlankOrZero(viewBean.getIdRubriqueRecouvrement())) {%><%=viewBean.getLibelleRubrique(viewBean.getIdRubriqueRecouvrement())%><%}%>" class="libelleLongDisabled"  readonly tabindex="-1">
       					</td>
						</tr>
						<tr>
						<td><ct:FWLabel key="GCA4023_RUBRIQUE_SPECIALE_ASSOCIEE"/></td>
							<td><ct:FWPopupList
 							validateOnChange="true" value="<%=viewBean.getNumeroRubriqueSpecialeAssociee()%>"
 							name="numeroRubriqueSpecialeAssociee" size="15" className="visible" jspName="<%=jspLocation%>"
 							minNbrDigit="3" autoNbrDigit="11" />
           					<input type="text" name="rubriqueSpecialeAssocieeDescription" id="rubriqueSpecialeAssocieeDescription" size="30" value="<%if (!JadeStringUtil.isBlankOrZero(viewBean.getIdRubriqueSpecialeAssociee())) {%><%=viewBean.getLibelleRubrique(viewBean.getIdRubriqueSpecialeAssociee())%><%}%>" class="libelleLongDisabled"  readonly tabindex="-1">
       					</td>
						</tr>
						<tr>
							<td><ct:FWLabel key="GCA4023_TYPE_ORDRE"/></td>
							<td><ct:FWCodeSelectTag
							name="typeOrdre" defaut="<%=viewBean.getTypeOrdre()%>"
							codeType="OSIVPTYMO" wantBlank="false" /></td>
						</tr>
						<tr>
							<td><ct:FWLabel key="GCA4023_ORDRE"/></td>
							<td><input type="text" id="ordre" name="ordre" size ="2" value="<%=viewBean.getOrdre()%>"></td>
						</tr>
						<tr>
							<td><ct:FWLabel key="GCA4023_ORDRE_COLONNE"/></td>
							<td><input type="text" name="RubriqueOrdreColonne" size ="2" value="<%=viewBean.getRubriqueOrdreColonne()%>"></td>
						</tr>
						<tr>
							<td><ct:FWLabel key="GCA4023_PENAL"/></td>
							<td>
								<input type="checkbox" onclick="updateHiddenPenal();" name="penal" <%=(viewBean.isPenal().booleanValue())? "checked" : "unchecked"%>>
				              	<input type="hidden"  name="penalStr" value="<%=(viewBean.isPenal().booleanValue())? "True" : "False"%>"></td>
						</tr>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>
<script>
$(function(){
	$("#numeroRubrique").change(function(){
		$("#rubriqueDescription").val("");
	});
	$("#numeroRubriqueIrrec").change(function(){
		$("#rubriqueIrrecDescription").val("");
	});
	$("#numeroRubriqueRecouvrement").change(function(){
		$("#rubriqueRecouvrementDescription").val("");
	});
});
</script>
