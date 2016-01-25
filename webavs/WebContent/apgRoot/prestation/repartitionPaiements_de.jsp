<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran="PAP0041";
globaz.apg.vb.prestation.APRepartitionPaiementsViewBean viewBean = (globaz.apg.vb.prestation.APRepartitionPaiementsViewBean) session.getAttribute("viewBean");

selectedIdValue = viewBean.getIdRepartitionBeneficiairePaiement();
bButtonCancel = false;
bButtonValidate = bButtonValidate && viewBean.isModifiable() && !viewBean.isRestitution() && viewBean.getSession().hasRight(IAPActions.ACTION_REPARTITION_PAIEMENTS, FWSecureConstants.UPDATE);
bButtonDelete = bButtonDelete && viewBean.isVentilation() && viewBean.isModifiable() && !viewBean.isRestitution() && viewBean.getSession().hasRight(IAPActions.ACTION_REPARTITION_PAIEMENTS, FWSecureConstants.UPDATE);
bButtonUpdate = bButtonUpdate && viewBean.isModifiable() && !viewBean.isRestitution() && viewBean.getSession().hasRight(IAPActions.ACTION_REPARTITION_PAIEMENTS, FWSecureConstants.UPDATE);


%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.apg.servlet.IAPActions"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<script language="JavaScript">

  function add() {
    document.forms[0].elements('userAction').value="<%=globaz.apg.servlet.IAPActions.ACTION_REPARTITION_PAIEMENTS%>.ajouter"
  }
  function upd() {}

  function validate() {
    state = true
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="<%=globaz.apg.servlet.IAPActions.ACTION_REPARTITION_PAIEMENTS%>.ajouter";
    else
        document.forms[0].elements('userAction').value="<%=globaz.apg.servlet.IAPActions.ACTION_REPARTITION_PAIEMENTS%>.modifier";
    return state;
  }

  function cancel() {
    if (document.forms[0].elements('_method').value == "add")
      document.forms[0].elements('userAction').value="back";
    else
      document.forms[0].elements('userAction').value="<%=globaz.apg.servlet.IAPActions.ACTION_REPARTITION_PAIEMENTS%>.afficher";
  }

  function del() {
    if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
        document.forms[0].elements('userAction').value="<%=globaz.apg.servlet.IAPActions.ACTION_REPARTITION_PAIEMENTS%>.supprimer";
        document.forms[0].submit();
    }
  }

  function init(){
	<%if ("new".equalsIgnoreCase(request.getParameter("_valid"))) {%>
	  	// mise a jour de la liste du parent
		if (parent.document.forms[0]) {
			parent.document.forms[0].submit();
		}
	<%}%>
		document.forms[0].target="fr_main";
  }

  function repartirPaiement() {
  	document.forms[0].elements('userAction').value = "<%=globaz.apg.servlet.IAPActions.ACTION_REPARTITION_PAIEMENTS%>.actionNouvelleVentilation";
  	document.forms[0].target="fr_detail";
  	document.forms[0].submit();
  }

</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_TITRE_REPARTITION_PAIEMENT"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD>
								<INPUT type="hidden" name="idDroit" value="<%=viewBean.getIdDroit()%>">
								<INPUT type="hidden" name="genreService" value="<%=viewBean.getGenreService()%>">
								<INPUT type="hidden" name="idTiersAdressePaiement" value="<%=viewBean.getIdTiersAdressePaiement()%>">
								<INPUT type="hidden" name="idDomaineAdressePaiement" value="<%=viewBean.getIdDomaineAdressePaiement()%>">
								<input type="hidden" name="idAffilieAdrPmt" value="<%=viewBean.getIdAffilieAdrPmt()%>">

								<LABEL for="montantNet"><ct:FWLabel key="JSP_MONTANT_NET"/></LABEL>
							</TD>
							<TD><INPUT type="text" name="montantNet" value="<%=viewBean.getMontantNet()%>" class="montantDisabled" readonly></TD>
							<TD><LABEL for="nom"><ct:FWLabel key="JSP_BENEFICIAIRE"/></LABEL></TD>
							<TD><INPUT type="text" name="nomPlus" value="<%=viewBean.getNomPlus()%>" class="libelleLongDisabled" readonly></TD>
						</TR>
						<% if (viewBean.isModifiable() && viewBean.isVentilation()) { %>
							<TR>
								<TD>&nbsp;</TD>
								<TD>&nbsp;</TD>
								<TD>&nbsp;</TD>
								<TD>
									<ct:FWLabel key="JSP_TIERS"/>
									<ct:FWSelectorTag
										name="selecteurBeneficiaire"

										methods="<%=viewBean.getMethodesSelectionBeneficiaire()%>"
										providerApplication="pyxis"
										providerPrefix="TI"
										providerAction="pyxis.tiers.tiers.chercher"
										target="fr_main"
										redirectUrl="<%=mainServletPath%>"/>
									&nbsp;
									<ct:FWLabel key="JSP_ADMINISTRATION"/>
									<ct:FWSelectorTag
										name="selecteurBeneficiaire2"

										methods="<%=viewBean.getMethodesSelectionBeneficiaire()%>"
										providerApplication="pyxis"
										providerPrefix="TI"
										providerAction="pyxis.tiers.administration.chercher"
										target="fr_main"
										redirectUrl="<%=mainServletPath%>"/>
								</TD>
							</TR>
						<% } %>
						<TR><TD colspan="4">&nbsp;</TD></TR>
						<% if (viewBean.isVentilation()) { %>
							<TR>
								<TD><LABEL for="montantVentile"><ct:FWLabel key="JSP_MONTANT_VENTILE"/></LABEL></TD>
								<TD><INPUT type="text" name="montantVentile" value="<%=viewBean.getMontantVentile()%>" class="montant" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);"></TD>
								<TD><LABEL for="referenceInterne"><ct:FWLabel key="JSP_REP_D_REFERENCE_INTERNE"/></LABEL></TD>
								<TD><INPUT type="text" name="referenceInterne" value="<%=viewBean.getReferenceInterne()%>" class="libelleLong"></TD>
							</TR>
						<% } %>
						<% if (!viewBean.isDefinitif()) {%>
							<TR><TD colspan="4">&nbsp;</TD></TR>
							<TR>
								<TD valign="top"><LABEL for="addressePaiement"><ct:FWLabel key="JSP_ADDRESSE_PAIEMENT"/></LABEL></TD>
								<TD><PRE><span class="IJAfficheText"><%=viewBean.getCcpOuBanqueFormatte()%></span></PRE></TD>
								<TD><PRE><span class="IJAfficheText"><%=viewBean.getAdresseFormattee()%></span></PRE></TD>
								<TD>
								<% if (viewBean.isModifiable() && viewBean.isVentilation()) { %>
									<ct:FWSelectorTag
										name="selecteurAdresses"

										methods="<%=viewBean.getMethodesSelectionAdresse()%>"
										providerApplication="pyxis"
										providerPrefix="TI"
										providerAction="pyxis.adressepaiement.adressePaiement.chercher"
										target="fr_main"
										redirectUrl="<%=mainServletPath%>"/>
								<% } %>
								</TD>
							</TR>
						<% } %>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<% if (!viewBean.isVentilation() && viewBean.isModifiable() && !viewBean.isRestitution() && viewBean.getSession().hasRight(IAPActions.ACTION_REPARTITION_PAIEMENTS, FWSecureConstants.UPDATE)) { %>
					<INPUT type="button" name="" value="<ct:FWLabel key="JSP_AJOUTER_REPARTITION"/>" onclick="repartirPaiement()">
				<% } %>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>