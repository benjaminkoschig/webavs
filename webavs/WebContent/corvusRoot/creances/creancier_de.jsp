<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.corvus.servlet.IREActions"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%><script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%@ taglib uri="/corvusRoot/corvustaglib.tld" prefix="re" %>
<%
	// Les labels de cette page commence par la préfix "JSP_CRE_D"

	idEcran="PRE0023";

	globaz.corvus.vb.creances.RECreancierViewBean viewBean = (globaz.corvus.vb.creances.RECreancierViewBean) session.getAttribute("viewBean");

	selectedIdValue = viewBean.getIdCreancier();
	
	String idCreancier = viewBean.getIdCreancier();
	String idTierRequerant = request.getParameter("idTierRequerant");
	String noDemandeRente = request.getParameter("noDemandeRente");
	viewBean.setIdDemandeRente(noDemandeRente);
	String csType = request.getParameter("csType");
	
	if(null != csType){
		viewBean.setCsType(csType);
	}
	
	String menuOptionToLoad = request.getParameter("menuOptionToLoad");
	
	String[] assures = viewBean.getAssuresList();

	String params = "&provenance1=TIERS";
		  params += "&provenance2=CI";
	String jspLocation = servletContext + "/corvusRoot/numeroSecuriteSocialeSF_select.jsp";
	
	boolean hasUpdateRight = controller.getSession().hasRight(IREActions.ACTION_CREANCIER, FWSecureConstants.UPDATE);
	
	bButtonDelete = bButtonDelete && viewBean.isModifiable() && hasUpdateRight;
	bButtonUpdate = bButtonUpdate && viewBean.isModifiable() && hasUpdateRight;
	bButtonValidate = bButtonValidate && viewBean.isModifiable() && hasUpdateRight;
	bButtonCancel = bButtonCancel && viewBean.isModifiable()  && hasUpdateRight;
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<script language="JavaScript">

	function add() {
	    document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_CREANCIER%>.ajouter";
	}

	function validate() {
	    state = true;
	    if (document.forms[0].elements('_method').value == "add"){
	        document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_CREANCIER%>.ajouter";
	    }else{
	    	document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_CREANCIER%>.modifier";
	    }
	    return state;
	}

	function upd(){
	}

	function cancel() {
		if (document.forms[0].elements('_method').value == "add"){
	    	document.forms[0].elements('userAction').value="back";
	    }else{
	    	document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_CREANCIER%>.afficher";
	    }
	}

	function del() {
	    if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
	        document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_CREANCIER%>.supprimer";
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

	function demandeCompensation() {
		document.forms[0].elements('userAction').value = "<%=globaz.corvus.servlet.IREActions.ACTION_GENERER_DEMANDE_COMPENSATION%>.afficher";
		document.forms[0].target = "fr_main";
		document.forms[0].elements('idCreancier').value = "<%=idCreancier%>";
		document.forms[0].submit();
	}

	function changerType() {
  		document.forms[0].elements("userAction").value = "<%=globaz.corvus.servlet.IREActions.ACTION_CREANCIER%>.afficher";
  		document.forms[0].target="fr_detail";
		document.forms[0].submit();
	}

	function postInit(){
  	}

</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_CRE_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD>
								<INPUT type="hidden" name="idTierRequerant" value="<%=idTierRequerant%>">
								<INPUT type="hidden" name="noDemandeRente" value="<%=noDemandeRente%>">
								<INPUT type="hidden" name="idDemandeRente" value="<%=noDemandeRente%>">
								<INPUT type="hidden" name="idCreancier" value="<%=viewBean.getIdCreancier()%>">
								<INPUT type="hidden" name="montantRetroactif" value="<%=viewBean.getMontantRetroactif()%>">
								<input type="hidden" name="menuOptionToLoad" value="<%=menuOptionToLoad%>">
								<ct:FWLabel key="JSP_CRE_D_TYPE"/>
							</TD>
							<TD>
								<ct:select name="csType" defaultValue="<%=viewBean.getCsType()%>" onchange="changerType();">
									<ct:optionsCodesSystems csFamille="<%=globaz.corvus.api.creances.IRECreancier.CS_GROUPE_TYPE_CREANCIER%>"/>
								</ct:select>
							</TD>
							<TD colspan="2">
								<%if (viewBean.hasRenteAccordee(noDemandeRente) && viewBean.hasCreancier(noDemandeRente)){%>
										<% if (globaz.jade.client.util.JadeStringUtil.isBlankOrZero(viewBean.getIdCreancier())){ %>
										<input type="hidden" name="isAll" value="true">
										<b><a href="#" onclick="demandeCompensation();"><ct:FWLabel key="JSP_CRE_D_GENERER_DEM_COMP"/></a></b>										
										<% } else { %>
										<input type="hidden" name="isAll" value="">
										<b><a href="#" onclick="demandeCompensation();"><ct:FWLabel key="JSP_CRE_D_GENERER_DEM_COMP"/></a></b>										
										<% } %>
								<% } else { %>
										&nbsp;
								<% } %>

							</TD>
						</TR>
						<TR>
							<TD colspan="4" align="right">
								&nbsp;
							</TD>
						</TR>
						<TR>
						<% if(globaz.corvus.api.creances.IRECreancier.CS_TIERS.equalsIgnoreCase(viewBean.getCsType()) ||
						      globaz.corvus.api.creances.IRECreancier.CS_ASSURANCE_SOCIALE.equalsIgnoreCase(viewBean.getCsType()) ||
						      globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getCsType())){%>

							<TBODY id="isTypeTiersOrAssuranceSociale">

								<TR>
									<TD><ct:FWLabel key="JSP_CRE_D_MONTANT_REVENDIQUE"/></TD>
									<TD><INPUT type="text" name="montantRevandique" value="<%=new globaz.framework.util.FWCurrency(viewBean.getMontantRevandique()).toStringFormat()%>" class="montant" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);"></TD>
									
									<TD width="100"><ct:FWLabel key="JSP_CRE_D_ASSURE"/></TD>
									<TD colspan="1">
									   	<SELECT name="idTiersRegroupement">
											<%for (int i=0; i<assures.length; i=i+2){%>
											<OPTION value="<%=assures[i]%>" <%=assures[i].equals(viewBean.getIdTiersRegroupement())?"selected":""%>><%=assures[i+1]%></OPTION>
											<%}%>
										</SELECT>
									</TD>
								</TR>
								<TR>
									<TD colspan="4">&nbsp;</TD>
								</TR>
								<TR><TD colspan="4" valign="middle"><hr></TD></TR>
								<TR>
									<TD colspan="4">&nbsp;</TD>
								</TR>
								<TR>
									<TD valign="middle">
										<INPUT type="hidden" name="idTiersAdressePmt" value="<%=viewBean.getIdTiersAdressePmt()%>">
										<INPUT type="hidden" name="idDomaineApplicatif" value="<%=viewBean.getIdDomaineApplicatif()%>">
										<LABEL for="nom"><ct:FWLabel key="JSP_CRE_D_CREANCIER"/></LABEL>
									</TD>
									<TD valign="top" width="400">
										<textarea name="nom" cols="50" readonly><%=viewBean.getTiersNomPrenom()%></textarea>
										<INPUT type="hidden" name="idTiers" value="<%=viewBean.getIdTiers()%>">
									</TD>
									<TD valign="middle" width="200" colspan="2">
										<ct:FWLabel key="JSP_CRE_D_TIERS"/>
										<ct:FWSelectorTag
											name="selecteurBeneficiaire"

											methods="<%=viewBean.getMethodesSelectionBeneficiaire()%>"
											providerApplication="pyxis"
											providerPrefix="TI"
											providerAction="pyxis.tiers.tiers.chercher"
											target="fr_main"
											redirectUrl="<%=mainServletPath%>"/>

										&nbsp;<ct:FWLabel key="JSP_CRE_D_ADMINISTRATION"/>
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
								<TR>
									<TD colspan="4">&nbsp;</TD>
								</TR>
								<TR>
									<TD valign="middle" width="250">
										<ct:FWLabel key="JSP_CRE_D_ADRESSE_PAIEMENT"/>
										<% if (viewBean.isModifiable()){ %>
												<ct:FWSelectorTag
													name="selecteurAdresses"

													methods="<%=viewBean.getMethodesSelectionAdressePaiement()%>"
													providerApplication="pyxis"
													providerPrefix="TI"
													providerAction="pyxis.adressepaiement.adressePaiement.chercher"
													target="fr_main"
													redirectUrl="<%=mainServletPath%>"/>
										<% } %>
									</TD>
									<TD colspan="3" rowspan="2">
										<PRE ><span class="IJAfficheText"><%=viewBean.getCcpOuBanqueFormatte()%></span></PRE>
									</TD>
								</TR>
								<TR><TD colspan="4" valign="middle"><hr></TD></TR>
								<TR>
									<TD colspan="4">&nbsp;</TD>
								</TR>
							</TBODY>
						<%} else if(globaz.corvus.api.creances.IRECreancier.CS_IMPOT_SOURCE.equalsIgnoreCase(viewBean.getCsType())){%>
							<TBODY id="isTypeImpotSource">
								<TR>
									<TD><ct:FWLabel key="JSP_CRE_D_MONTANT_REVENDIQUE"/></TD>
									<TD><INPUT type="text" name="montantRevandique" value="<%=new globaz.framework.util.FWCurrency(viewBean.getMontantRevandique()).toStringFormat()%>" class="montant" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);"></TD>
								</TR>
							</TBODY>
						<%}%>
						</TR>
						<TR>
							<TD valign="top"><LABEL for="refPaiement"><ct:FWLabel key="JSP_CRE_D_REFERENCE_PAIEMENT"/></LABEL></TD>
							<TD colspan="3">
								<INPUT type="text" name="refPaiement" value="<%=viewBean.getRefPaiement()%>" maxlength="24" class="libelleLong">
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