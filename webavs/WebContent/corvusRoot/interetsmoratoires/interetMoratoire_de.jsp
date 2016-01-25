<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.framework.util.FWCurrency"%>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%@ taglib uri="/corvusRoot/corvustaglib.tld" prefix="re" %>
<%
	// Les labels de cette page commence par la préfix "JSP_IMO_D"
	idEcran="PRE0052";
	globaz.corvus.vb.interetsmoratoires.REInteretMoratoireViewBean viewBean = (globaz.corvus.vb.interetsmoratoires.REInteretMoratoireViewBean) session.getAttribute("viewBean");
	
	if(viewBean.isNew()){
		bButtonValidate=false;
		bButtonCancel=false;
	}
	
	bButtonUpdate = viewBean.isModifiable() && bButtonUpdate;
	bButtonDelete = viewBean.isModifiable() && bButtonDelete;
	
	String idDemandeRente = request.getParameter("idDemandeRente");
	String idTiersDemandeRente = request.getParameter("idTiersDemandeRente");  
	String dateDepotDemande = request.getParameter("dateDepotDemande");
	String dateDebutDroit = request.getParameter("dateDebutDroit");
	String dateDecision = request.getParameter("dateDecision");
	
	// lorsque l'on arrive depuis preparerDecisions
	String eMailAddress = request.getParameter("eMailAddress");
	String csTypePreparationDecision = request.getParameter("csTypePreparationDecision");
	String fromPreparerDecisions = request.getParameter("fromPreparerDecisions");
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<script language="JavaScript">

	function add() {
	    document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_INTERET_MORATOIRE%>.ajouter"
	}
	  
	function upd() {}
	
	function validate() {
	    state = true
	    if (document.forms[0].elements('_method').value == "add"){
	        document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_INTERET_MORATOIRE%>.ajouter";
	    }else{
	    	document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_INTERET_MORATOIRE%>.modifier";
	    }
	    return state;
	}
	
	function cancel() {
		if (document.forms[0].elements('_method').value == "add"){
	    	document.forms[0].elements('userAction').value="back";
	    }else{
	    	document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_INTERET_MORATOIRE%>.chercher";
	    }	  
	}
	
	function del() {
	    if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
	        document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_INTERET_MORATOIRE%>.supprimer";
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

</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_IMO_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><ct:FWLabel key="JSP_IMO_D_TIERS"/></TD>
							<TD colspan="3">
								<INPUT type="hidden" name="idDemandeRente" value="<%=idDemandeRente%>">
								<INPUT type="hidden" name="idTiersDemandeRente" value="<%=idTiersDemandeRente%>">
								<INPUT type="hidden" name="dateDepotDemande" value="<%=dateDepotDemande%>">
								<INPUT type="hidden" name="dateDebutDroit" value="<%=dateDebutDroit%>">
								<INPUT type="hidden" name="dateDecision" value="<%=dateDecision%>">
								<INPUT type="hidden" name="idTiers" value="<%=viewBean.getIdTiersAdrPmt()%>">
								<INPUT name="eMailAddress" type="hidden" value="<%=eMailAddress%>">
								<INPUT name="csTypePreparationDecision" type="hidden" value="<%=csTypePreparationDecision%>">
								<INPUT type="hidden" name="fromPreparerDecisions" value="<%="true".equals(request.getParameter("fromPreparerDecisions"))?"true":"false"%>">
								<re:PRDisplayRequerantInfoTag 
									session="<%=(globaz.globall.db.BSession)controller.getSession()%>" 
									idTiers="<%=viewBean.getIdTiers()%>"
									style="<%=globaz.prestation.jsp.taglib.PRDisplayRequerantInfoTag.STYLE_CONDENSED_WITHOUT_LABEL%>"/>
							</TD>
							<TD valign="top">
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
							<TD rowspan="2" valign="top">
								<PRE><span class="IJAfficheText"><%=viewBean.getCcpOuBanqueFormatte()%></span></PRE>
								<PRE><span class="IJAfficheText"><%=viewBean.getAdresseFormattee()%></span></PRE>
							</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_IMO_D_INTERETS"/></TD>
							<TD colspan="3">
								<INPUT name="MontantInteret" type="text" value="<%=new FWCurrency(viewBean.getMontantInteret()).toStringFormat()%>" class="montant" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);">
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