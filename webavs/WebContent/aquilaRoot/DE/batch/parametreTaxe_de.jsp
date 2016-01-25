<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.aquila.db.batch.COParametreTaxeViewBean"%>
<%
	idEcran ="GCO0040";
	COParametreTaxeViewBean viewBean = (COParametreTaxeViewBean)session.getAttribute ("viewBean");
	String jspLocation = servletContext + mainServletPath + "Root/rubrique_select.jsp";

	String idTaxe = "";
	if (JadeStringUtil.isBlank(request.getParameter("idTaxe"))) {
		idTaxe = (String)session.getAttribute("idTaxe");
	} else {
		idTaxe = request.getParameter("idTaxe");
	}
%>

<script language="JavaScript">
</script>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<script language="JavaScript">

	function add() {
		document.forms[0].elements('userAction').value="aquila.batch.parametreTaxe.ajouter"
	}

	function upd() {
		 $("[name=rubrique]").attr('disabled', 'disabled');
	}

	function validate() {
	    if (document.forms[0].elements('_method').value == "add"){
	        document.forms[0].elements('userAction').value="aquila.batch.parametreTaxe.ajouter";
	    }else{
	        document.forms[0].elements('userAction').value="aquila.batch.parametreTaxe.modifier";
	    }
	    return true;
	}


	function cancel() {
		if (document.forms[0].elements('_method').value == "add")
			document.forms[0].elements('userAction').value="back";
		else
	 		document.forms[0].elements('userAction').value="aquila.batch.parametreTaxe.chercher";
	}

	function del() {
		if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?")) {
			document.forms[0].elements('userAction').value="aquila.batch.parametreTaxe.supprimer";
			document.forms[0].submit();
		}
	}
	function init() {
	}

	function rubriqueManuelleOn(){
		document.forms[0].idRubrique.value="";
		document.forms[0].rubriqueDescription.value="";
	}

	function updateRubrique(el) {
		if (el == null || el.value== "" || el.options[el.selectedIndex] == null) {
			rubriqueManuelleOn();
		} else {
			var elementSelected = el.options[el.selectedIndex];
			document.forms[0].idRubrique.value = elementSelected.idCompte;
			document.forms[0].rubriqueDescription.value = elementSelected.rubriqueDescription;
		}
	}

</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>Detail eines Gebührparameters<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
	<%-- tpl:put name="zoneMain" --%>
	<tr>
       	<td class="label">Rubrik</td>
		<td class="control">
			<input type="hidden" name="idCalculTaxe" value='<%=idTaxe%>' readonly tabindex="-1">
        	<ct:FWPopupList name="rubrique"
         		onFailure="rubriqueManuelleOn();"
				onChange="updateRubrique(tag.select);"
				value='<%=viewBean.getRubriqueEntity()!=null?viewBean.getRubriqueEntity().getIdExterne():""%>'
				className="libelle"
				jspName="<%=jspLocation%>"
				minNbrDigit="1"
				forceSelection="true"
				validateOnChange="false"/>
          	<input type="text" name="rubriqueDescription" size="60" value='<%=viewBean.getRubriqueEntity() != null?viewBean.getRubriqueEntity().getDescription():""%>' class="libelleLongDisabled"  readonly tabindex="-1">
          	<input type="hidden" name="idRubrique" value='<%=viewBean.getRubriqueEntity()!=null?viewBean.getRubriqueEntity().getIdRubrique():""%>' readonly tabindex="-1">
        </td>
	</tr>
	<tr>
		<td class="label">Erforderlich</td>
		<td class="control">
			<input type="checkbox" name="estRequis" <%=viewBean.getEstRequis().booleanValue()==true?"CHECKED":""%>>
		</td>
	</tr>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<% if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<script>
</script>
<% } %>
	<ct:menuChange displayId="menu" menuId="CO-MenuPrincipal"/>
	<ct:menuChange displayId="options" menuId="CO-ParamTaxe" showTab="options">
	<ct:menuActivateNode active="yes" nodeId="a_id_c_p2"/>
	<ct:menuActivateNode active="yes" nodeId="a_id_c_p3"/>
	<ct:menuActivateNode active="yes" nodeId="a_id_c_p4"/>
	</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>