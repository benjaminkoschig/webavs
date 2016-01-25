<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%@ taglib uri="/corvusRoot/corvustaglib.tld" prefix="re" %>
<%
	// Les labels de cette page commence par la préfix "JSP_CIM_D"
	idEcran="PRE0054";
	globaz.corvus.vb.interetsmoratoires.RECalculInteretMoratoireViewBean viewBean = (globaz.corvus.vb.interetsmoratoires.RECalculInteretMoratoireViewBean) session.getAttribute("viewBean");
	
	if(viewBean.isNew()){
		bButtonValidate=false;
		bButtonCancel=false;
	}
	
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
	    document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_CALCUL_INTERET_MORATOIRE%>.ajouter"
	}
	  
	function upd() {}
	
	function validate() {
	    state = true
	    if (document.forms[0].elements('_method').value == "add"){
	        document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_CALCUL_INTERET_MORATOIRE%>.ajouter";
	    }else{
	    	document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_CALCUL_INTERET_MORATOIRE%>.modifier";
	    }
	    
	   	// le montant de la dette ne peut pas etre plus grand que le montant retroactif
	  	var montantDette = toFloat(document.getElementById('montantDette').value);
	  	var montantRetro = toFloat(document.getElementById('montantRetro').value);
    	if(parseFloat(montantRetro)<parseFloat(montantDette)){
    		state = false;
    		errorObj.text = "<ct:FWLabel key='ERREUR_MONTANT_VERSE_A_TIERS_PLUS_GRAND_MONT_RETRO'/>";
    		showErrors();
    		errorObj.text = "";
    	}

	    return state;
	}
	
	function cancel() {
		if (document.forms[0].elements('_method').value == "add"){
	    	document.forms[0].elements('userAction').value="back";
	    }else{
	    	document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_CALCUL_INTERET_MORATOIRE%>.chercher";
	    }	  
	}
	
	function del() {
	    if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
	        document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_CALCUL_INTERET_MORATOIRE%>.supprimer";
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
	
	function toFloat(input){
		var output = input.replace("'", "");
		return output;
	}

</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_CIM_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><ct:FWLabel key="JSP_CIM_D_TIERS"/></TD>
							<TD colspan="3">
								<INPUT type="hidden" name="idDemandeRente" value="<%=idDemandeRente%>">
								<INPUT type="hidden" name="idTiersDemandeRente" value="<%=idTiersDemandeRente%>">
								<INPUT type="hidden" name="dateDepotDemande" value="<%=dateDepotDemande%>">
								<INPUT type="hidden" name="dateDebutDroit" value="<%=dateDebutDroit%>">
								<INPUT type="hidden" name="dateDecision" value="<%=dateDecision%>">
								<INPUT type="hidden" name="idTiers" value="<%=viewBean.getIdTiers()%>">
								<INPUT name="eMailAddress" type="hidden" value="<%=eMailAddress%>">
								<INPUT name="csTypePreparationDecision" type="hidden" value="<%=csTypePreparationDecision%>">
								<INPUT type="hidden" name="fromPreparerDecisions" value="<%="true".equals(request.getParameter("fromPreparerDecisions"))?"true":"false"%>">
								<re:PRDisplayRequerantInfoTag 
									session="<%=(globaz.globall.db.BSession)controller.getSession()%>" 
									idTiers="<%=viewBean.getIdTiers()%>"
									style="<%=globaz.prestation.jsp.taglib.PRDisplayRequerantInfoTag.STYLE_CONDENSED_WITHOUT_LABEL%>"/>
							</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_CIM_D_MONTANT_RETROACTIF"/></TD>
							<TD>
								<INPUT name="montantRetro" id="montantRetro" type="text" value="<%=new FWCurrency(viewBean.getMontantRetro()).toStringFormat()%>" class="montantDisabled" readonly="readonly" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);">
							</TD>
							<TD><ct:FWLabel key="JSP_CIM_D_MONTANT_VERSE_TIERS"/></TD>
							<TD>
								<INPUT name="montantDette" id="montantDette" type="text" value="<%=new FWCurrency(viewBean.getMontantDette()).toStringFormat()%>" class="montant" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);">
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