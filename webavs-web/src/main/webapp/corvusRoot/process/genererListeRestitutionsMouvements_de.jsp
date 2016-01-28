<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%@ page import="globaz.corvus.vb.process.REGenererListeRestitutionsMouvementsViewBean"%>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="PRE2058";
	
	REGenererListeRestitutionsMouvementsViewBean viewBean = (REGenererListeRestitutionsMouvementsViewBean) session.getAttribute("viewBean");
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	
	userActionValue=globaz.corvus.servlet.IREActions.ACTION_GENERER_LISTE_RESTITUTIONS_MOUVEMENTS + ".executer";
	
	String jspLocation = servletContext + mainServletPath + "Root/rubrique_select.jsp";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="corvus-menuprincipal"/>
<ct:menuChange displayId="options" menuId="corvus-optionsempty" showTab="menu"></ct:menuChange>
<script type="text/javascript">
function updateRubrique(el) {
	if (el == null || el.value== "" || el.options[el.selectedIndex] == null) {
		rubriqueManuelleOn();
	} else {
		var elementSelected = el.options[el.selectedIndex];
		document.forms[0].idRubrique.value = elementSelected.idCompte;
		document.forms[0].idExterneRubriqueEcran.value = elementSelected.idExterneRubriqueEcran;
		document.forms[0].rubriqueDescription.value = elementSelected.rubriqueDescription;
	}
}
function rubriqueManuelleOn() {
	document.forms[0].idRubrique.value="";
	document.forms[0].rubriqueDescription.value="";
}
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_LISTE_RESTITUTION_MOUVEMENTS_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
<tr>
	<td><label for="eMailAddress"><ct:FWLabel key="JSP_RESTITUTIONS_MOUVEMENTS_MAIL"/></label></td>
	<td><input type="text" name="eMailAddress" value="<%=globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getEMailAddress())?controller.getSession().getUserEMail():viewBean.getEMailAddress()%>" class="libelleLong"></td>
</tr>
<tr> 
  <td nowrap><label for="forRole"><ct:FWLabel key="JSP_RESTITUTIONS_MOUVEMENTS_ROLE"/></label></td>
  <td nowrap> 
	<select id="forRole" name="forRole" >
		<%=viewBean.createOptionsTags(objSession, globaz.osiris.external.IntRole.ROLE_RENTIER)%>
	</select>
  </td>
</tr>
<tr>
	<td><label for="dateDe"><ct:FWLabel key="JSP_RESTITUTIONS_MOUVEMENTS_PERIODE_DE"/></label></td>
	<td>
		<input	id="dateDe"
				name="dateDe"
				data-g-calendar="type:default,mandatory:true"
				value="<%=viewBean.premierjanvier()%>" />
		&nbsp;<ct:FWLabel key="JSP_RESTITUTIONS_MOUVEMENTS_PERIODE_A"/>&nbsp;				
		<input	id="dateA"
				name="dateA"
				data-g-calendar="type:default,currentDate:true,mandatory:true"
				value="" />		
	</td>
</tr>

<tr>
	<td nowrap><label for="idRubrique"><ct:FWLabel key="JSP_RESTITUTIONS_MOUVEMENTS_RUBRIQUE"/></label></td>
	<td>
	  	<ct:FWPopupList name="idExterneRubriqueEcran"
        	onFailure="rubriqueManuelleOn();"
			onChange="updateRubrique(tag.select);"
			value=""
			className="libelle"
			jspName="<%=jspLocation%>"
			minNbrDigit="1"
			forceSelection="true"
			validateOnChange="false"
	 	/>
		<input type="hidden" name="idRubrique" value="" >
		<input type="text" name="rubriqueDescription" size="30" value="" class="libelleLongDisabled" readonly tabindex="-1">
	</td>
</tr>

<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>