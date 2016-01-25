<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%

// Les labels de cette page commence par la préfix "JSP_NAN_D"

	idEcran="PRE0071";

	globaz.corvus.vb.annonces.REAnnoncesAbstractLevel1AViewBean viewBean = (globaz.corvus.vb.annonces.REAnnoncesAbstractLevel1AViewBean)session.getAttribute("viewBean");
	
	String noDemandeRente = request.getParameter("noDemandeRente");
	String idTierRequerant = request.getParameter("idTierRequerant");
	String idTiersBeneficiaire = request.getParameter("idTiersBeneficiaire");
	String idRenteAccordee = request.getParameter("idRenteAccordee");
	String idBaseCalcul = request.getParameter("idBaseCalcul");
	String csTypeBasesCalcul = request.getParameter("csTypeBasesCalcul");
	String csEtatRenteAccordee = request.getParameter("csEtatRenteAccordee");
	String dateFinDroit = request.getParameter("dateFinDroit");
	String isPreparationDecisionValide = request.getParameter("isPreparationDecisionValide");
	
	String menuOptionToLoad = request.getParameter("menuOptionToLoad");
	
	boolean hasRight = viewBean.getSession().hasRight("corvus.annonces.annonceAugmentation10Eme",FWSecureConstants.UPDATE);
	bButtonValidate= hasRight;
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.corvus.api.basescalcul.IRERenteAccordee"%>
<%if(JadeStringUtil.isNull(menuOptionToLoad) || JadeStringUtil.isEmpty(menuOptionToLoad)){%>
	
<%@page import="globaz.corvus.utils.REPmtMensuel"%><ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="menu"/>
	<ct:menuChange displayId="options" menuId="corvus-optionsannonces"/>
<%}else if("rentesaccordees".equals(menuOptionToLoad)){%>
	<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="options"/>
	<ct:menuChange displayId="options" menuId="corvus-optionsrentesaccordees">
		<ct:menuSetAllParams key="selectedId" value="<%=idRenteAccordee%>"/>
		<ct:menuSetAllParams key="idRenteAccordee" value="<%=idRenteAccordee%>"/>
		<ct:menuSetAllParams key="noDemandeRente" value="<%=noDemandeRente%>"/>
		<ct:menuSetAllParams key="idTierRequerant" value="<%=idTierRequerant%>"/>
		<ct:menuSetAllParams key="idTiersBeneficiaire" value="<%=idTiersBeneficiaire%>"/>
		<ct:menuSetAllParams key="idBaseCalcul" value="<%=idBaseCalcul%>"/>
		<ct:menuSetAllParams key="csTypeBasesCalcul" value="<%=csTypeBasesCalcul%>"/>	
		<% if ((IRERenteAccordee.CS_ETAT_AJOURNE.equals(csEtatRenteAccordee)
			    || IRERenteAccordee.CS_ETAT_CALCULE.equals(csEtatRenteAccordee)
			    || IRERenteAccordee.CS_ETAT_DIMINUE.equals(csEtatRenteAccordee))
			    
			  || (!globaz.jade.client.util.JadeStringUtil.isBlankOrZero(dateFinDroit))
			  || !REPmtMensuel.isValidationDecisionAuthorise(objSession)) { %>
			<ct:menuActivateNode active="no" nodeId="optdiminution"/>
		<%}%>
		<%if ("false".equals(isPreparationDecisionValide)){%>
			<ct:menuActivateNode active="no" nodeId="preparerDecisionRA"/>
		<%}%>	
	</ct:menuChange>
	<ct:menuReload/>
<%}%>


<script language="JavaScript">

  function add() {
  }
  
  function upd() {
  }

  function validate() {
    state = true;
    document.forms[0].elements('userAction').value="corvus.annonces."+document.forms[0].elements('typeAnnonce').value+".afficher";
    document.forms[0].elements('_method').value == "add";
    return state;
  }

  function cancel() {
      document.forms[0].elements('userAction').value="corvus.annonces.annonce.chercher";
  }

  function del() {
  }

  function init(){
  }

</script>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_NAN_D_NOUVELLE_ANNONCE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>				
							<TD><ct:FWLabel key="JSP_NAN_D_NO_RENTE_ACCORDEE"/></TD>
							<TD>
								<input type="text" name="idRenteAccordee" value="<%=viewBean.getIdRenteAccordee()%>" class=disabled readonly>
								<input type="hidden" name="menuOptionToLoad" value="<%=menuOptionToLoad%>">	
							</TD>
						</TR>			
						<TR>
							<TD><ct:FWLabel key="JSP_NAN_D_TYPE_NOUVELLE_ANNONCE"/></TD>
							<TD>
								<SELECT name="typeAnnonce">
									<OPTION value="annonceAugmentation10Eme"><ct:FWLabel key="JSP_NAN_D_ANNONCES_AUGMENTATION_10_REVISION"/></OPTION>
									<OPTION value="annonceAugmentation9Eme"><ct:FWLabel key="JSP_NAN_D_ANNONCES_AUGMENTATION_9_REVISION"/></OPTION>
									<OPTION value="annonceDiminution10Eme"><ct:FWLabel key="JSP_NAN_D_ANNONCES_DIMINUTION_10_REVISION"/></OPTION>
									<OPTION value="annonceDiminution9Eme"><ct:FWLabel key="JSP_NAN_D_ANNONCES_DIMINUTION_9_REVISION"/></OPTION>
								</SELECT>
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