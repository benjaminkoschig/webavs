<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%--

INFO!!!!
Les labels de cette page sont prefixes avec 'LABEL_JSP_REP_D'

--%>
<%
idEcran="PIJ0015";
globaz.ij.vb.prestations.IJRepartitionJointPrestationViewBean viewBean = (globaz.ij.vb.prestations.IJRepartitionJointPrestationViewBean) session.getAttribute("viewBean");

selectedIdValue = viewBean.getIdRepartitionPaiement();
bButtonCancel = false;
bButtonValidate = (bButtonValidate && viewBean.isModifiable()) || viewBean.isCreationNouvelleRepartition() &&
				  objSession.hasRight(globaz.ij.servlet.IIJActions.ACTION_REPARTITION_PAIEMENTS +".ajoute","ADD");
bButtonDelete = bButtonDelete && viewBean.isModifiable();
bButtonUpdate = bButtonUpdate && viewBean.isModifiable();


%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="ij-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="ij-optionsempty"/>

<script language="JavaScript">

  function add() {
    document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_REPARTITION_PAIEMENTS%>.ajouter"
  }
  function upd() {}

  function validate() {
    state = true
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_REPARTITION_PAIEMENTS%>.ajouter";
    else
        document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_REPARTITION_PAIEMENTS%>.modifier";
    return state;
  }

  function cancel() {
    if (document.forms[0].elements('_method').value == "add")
      document.forms[0].elements('userAction').value="back";
    else
      document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_REPARTITION_PAIEMENTS%>.afficher";
  }

  function del() {
	  if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
        document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_REPARTITION_PAIEMENTS%>.supprimer";
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
  	document.forms[0].elements('userAction').value = "<%=globaz.ij.servlet.IIJActions.ACTION_REPARTITION_PAIEMENTS%>.actionNouvelleVentilation";
  	document.forms[0].target="fr_detail";
  	document.forms[0].submit();
  }

  function rechercherAffilie(value) {
  	if (value!=""){
		// si le numero d'affilie n'a pas une longueur de 8 -> ###.####
		// on essaye de rajouter le formatage si la longueur vaut 7 ####### -> ###.####
		if(value.length == 7){
			 var valueForm = "";
			 var valueForm = valueForm.concat(value.substring(0, 3),".",value.substring(3,7));
			 document.forms[0].elements('numAffilieEmployeur').value = valueForm;
			 value = valueForm;
		}

	   document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_REPARTITION_PAIEMENTS%>.rechercherAffilie";
	   document.forms[0].submit();
	}
  }

</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_REP_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
<%

//Dans ce cas, l'on veut créer un nouveau bénéficiaire.
if (viewBean.isCreationNouvelleRepartition()) { %>

						<TR>
							<TD>
								<INPUT type="hidden" name="idPrestation" value="<%=viewBean.getIdPrestation()%>">
								<INPUT type="hidden" name="idTiersAdressePaiement" value="<%=viewBean.getIdTiersAdressePaiement()%>">
								<INPUT type="hidden" name="idDomaineAdressePaiement" value="<%=viewBean.getIdDomaineAdressePaiement()%>">

								<LABEL for="montantBrut"><ct:FWLabel key="JSP_REP_D_MONTANT_BRUT"/></LABEL>
							</TD>
							<TD><INPUT type="text" name="montantBrut" value="<%=viewBean.getMontantBrut()%>" class="libelleLong"></TD>

							<TD><LABEL for="nom"><ct:FWLabel key="JSP_REP_D_BENEFICIAIRE"/></LABEL></TD>
							<TD>
								<INPUT type="text" name="nomPlus" value="<%=viewBean.getNomPlus()%>" class="libelleLongDisabled" readonly>

							</TD>
						</TR>
						<TR>
							<TD>&nbsp;</TD>
							<TD>&nbsp;</TD>
							<TD>N° affilié</TD>
							<TD><INPUT type="text" name="numAffilieEmployeur" value="<%=viewBean.getNumAffilieEmployeur()%>"  onchange="rechercherAffilie(value)" class="montant">
						</TR>
						
						<TR>
						<TD colspan="3"></TD>
							<TD>
								<LABEL for="nom"><ct:FWLabel key="JSP_TIERS"/></LABEL>&nbsp;
																
								<ct:FWSelectorTag
									name="selecteurBeneficiaire"

									methods="<%=viewBean.getMethodesSelectionBeneficiaire()%>"
									providerApplication="pyxis"
									providerPrefix="TI"
									providerAction="pyxis.tiers.tiers.chercher"
									target="fr_main"
									redirectUrl="<%=mainServletPath%>"/>
								
								<LABEL for="nom"><ct:FWLabel key="JSP_ADMINISTRATION"/></LABEL>&nbsp;
									
									<ct:FWSelectorTag
									name="selecteurBeneficiaire2"

									methods="<%=viewBean.getMethodesSelectionBeneficaire2()%>"
									providerApplication="pyxis"
									providerPrefix="TI"
									providerAction="pyxis.tiers.administration.chercher"
									target="fr_main"
									redirectUrl="<%=mainServletPath%>"/>
								</TD>
						
						</TR>
						
						
						
						
						<% if (!viewBean.isDefinitif()) {%>
							<TR><TD colspan="4">&nbsp;</TD></TR>
							<TR><TD colspan="3">&nbsp;</TD></TR>

							<TR>
								<TD valign="top"><LABEL for="addressePaiement"><ct:FWLabel key="JSP_REP_D_ADRESSE_PAIEMENT"/></LABEL></TD>
								<TD><PRE><span class="IJAfficheText"><%=viewBean.getCcpOuBanqueFormatte()%></span></PRE></TD>
								<TD><PRE><span class="IJAfficheText"><%=viewBean.getAdresseFormattee()%></span></PRE></TD>
								
								<TD>
								<ct:FWSelectorTag
										name="selecteurAdresses"

										methods="<%=viewBean.getMethodesSelectionAdresse()%>"
										providerApplication="pyxis"
										providerPrefix="TI"
										providerAction="pyxis.adressepaiement.adressePaiement.chercher"
										target="fr_main"
										redirectUrl="<%=mainServletPath%>"/>&nbsp;
								</TD>
							</TR>
						<% } %>

<%} else {%>
						<TR>
							<TD>
								<INPUT type="hidden" name="idPrestation" value="<%=viewBean.getIdPrestation()%>">
								<INPUT type="hidden" name="idTiersAdressePaiement" value="<%=viewBean.getIdTiersAdressePaiement()%>">
								<INPUT type="hidden" name="idDomaineAdressePaiement" value="<%=viewBean.getIdDomaineAdressePaiement()%>">

								<LABEL for="montantNet"><ct:FWLabel key="JSP_REP_D_MONTANT_NET"/></LABEL>
							</TD>
							<TD><INPUT type="text" name="montantNet" value="<%=viewBean.getMontantNet()%>" class="montantDisabled" readonly></TD>
							<TD><LABEL for="nom"><ct:FWLabel key="JSP_REP_D_BENEFICIAIRE"/></LABEL></TD>
							<TD>
								<INPUT type="text" name="nomPlus" value="<%=viewBean.getNomPlus()%>" class="libelleLongDisabled" readonly>
								<% if (viewBean.isModifiable() && viewBean.isVentilation()) { %>
						</TR>
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

								&nbsp;<ct:FWLabel key="JSP_ADMINISTRATION"/>
								<ct:FWSelectorTag
									name="selecteurBeneficiaire2"

									methods="<%=viewBean.getMethodesSelectionBeneficaire2()%>"
									providerApplication="pyxis"
									providerPrefix="TI"
									providerAction="pyxis.tiers.administration.chercher"
									target="fr_main"
									redirectUrl="<%=mainServletPath%>"/>

								<% } %>
							</TD>
						</TR>
						<TR><TD colspan="4">&nbsp;</TD></TR>
						<% if (viewBean.isVentilation()) { %>
						<TR>
							<TD><LABEL for="montantVentile"><ct:FWLabel key="JSP_REP_D_MONTANT_VENTILE"/></LABEL></TD>
							<TD><INPUT type="text" name="montantVentile" value="<%=viewBean.getMontantVentile()%>" class="montant" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);"></TD>
							<TD><LABEL for="referenceInterne"><ct:FWLabel key="JSP_REP_D_REFERENCE_INTERNE"/></LABEL></TD>
							<TD><INPUT type="text" name="referenceInterne" value="<%=viewBean.getReferenceInterne()%>" class="libelleLong"></TD>
						</TR>
						<% } %>
						<% if (!viewBean.isDefinitif()) {%>
						<TR><TD colspan="4">&nbsp;</TD></TR>
						<TR>
							<TD valign="top"><LABEL for="addressePaiement"><ct:FWLabel key="JSP_REP_D_ADRESSE_PAIEMENT"/></LABEL></TD>
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
<%}%>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<% if (!viewBean.isVentilation() && viewBean.isModifiable()) { %>
					<INPUT type="button" name="" value="<ct:FWLabel key="JSP_AJOUTER_REPARTITION"/>" onclick="repartirPaiement()">
				<% } %>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>