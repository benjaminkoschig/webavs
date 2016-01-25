<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="PIJ0037";
	globaz.ij.vb.lots.IJFactureACompenserViewBean viewBean = (globaz.ij.vb.lots.IJFactureACompenserViewBean) session.getAttribute("viewBean");
	
	selectedIdValue = viewBean.getIdFactureACompenser();
	bButtonValidate = bButtonValidate && viewBean.isValide() &&  viewBean.isModifiable() && viewBean.getSession().hasRight("ij.lots.factureACompenser.ajouter", FWSecureConstants.UPDATE);
	bButtonDelete = bButtonDelete && viewBean.isModifiable();
	bButtonUpdate = bButtonUpdate && viewBean.isModifiable();
	bButtonCancel = false;
	
	String jspLocation = servletContext + "/ijRoot/numeroFacture_select.jsp";
	
	String params = "&idTiers=" + viewBean.getIdTiers() + "&montant=" + JadeStringUtil.removeChar(viewBean.getMontantTotal(), '\'');
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<SCRIPT language="javascript"> 
	function add() {
	    document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_FACTURES_LOT%>.ajouter";
	}
	
	function upd() {
	}
	
	function validate() {
	    state = validateFields();
	    
	    // si on a un no de facture, on doit avoir un idFacture
	    if((!(document.forms[0].elements('noFacture').value == 0 ||
		    document.forms[0].elements('noFacture').value == ""))
		    &&
	       (document.forms[0].elements('idFacture').value == 0 ||
		    document.forms[0].elements('idFacture').value == "")){
	       
	       alert("<ct:FWLabel key='JSP_NO_FACTURE_SANS_ID_FACTURE'/>");
	       state = false;
	       resetNoFacture();
	    }
	    
	    if (document.forms[0].elements('_method').value == "add")
	        document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_FACTURES_LOT%>.ajouter";
	    else
	        document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_FACTURES_LOT%>.modifier";
	    
	    return state;
	}
	
	function cancel() {
		document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_FACTURES_LOT%>.afficher";
		document.forms[0].elements('_method').value="add";
	}
	
	function del() {
	    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
	        document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_FACTURES_LOT%>.supprimer";
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
	}
	
	function chercherNoAVSOuAffilie() {
		document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_FACTURES_LOT%>.actionChercherNoAVSOuAffilie";
		document.forms[0].submit();
	}
	
  	function noFactureChange(tag) {
		if(tag.select && tag.select.selectedIndex != -1){
			document.forms[0].elements('noFacture').value = tag.select[tag.select.selectedIndex].noFacture;
			document.forms[0].elements('idFacture').value = tag.select[tag.select.selectedIndex].idFacture;
		}
		
		if(document.forms[0].elements('noFacture').value == 0 ||
		   document.forms[0].elements('noFacture').value == ""){
			resetNoFacture();
		}
	}
	
	function resetNoFacture(){
		document.forms[0].elements('noFacture').value = "0";
		document.forms[0].elements('idFacture').value = "0";
	}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_TITRE_FACTURE_A_COMPENSER"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<INPUT type="hidden" name="idTiers" value="<%=viewBean.getIdTiersCompensation()%>"/>
						<INPUT type="hidden" name="idAffilie" value="<%=viewBean.getIdAffilieCompensation()%>"/>
						<TR>
							<TD><ct:FWLabel key="JSP_MONTANT_TOTAL"/></TD>
							<TD><INPUT type="text" name="" value="<%=viewBean.getMontantTotal()%>" class="montantDisabled" readonly></TD>
							<TD><ct:FWLabel key="JSP_BENEFICIAIRE_DE_BASE"/></TD>
							<TD><INPUT type="text" name="" value="<%=viewBean.getNumAffilie()+("".equalsIgnoreCase(viewBean.getNumAffilie())?"":" ")+viewBean.getNomPrenomBeneficiaireBase()%>" class="libelleLongDisabled" readonly> </TD>
						</TR>
						<TR><TD colspan="4">&nbsp;</TD></TR>
						<TR>
							<TD><LABEL for="isCompenser"><ct:FWLabel key="JSP_COMPENSER"/></LABEL></TD>
							<TD><INPUT type="checkbox" name="isCompense"<% if (viewBean.getIsCompense().booleanValue()) { %> checked<% } %>></TD>
							<TD colspan="2"></TD>
						</TR>
						<TR>
							<TD><LABEL for="montant"><ct:FWLabel key="JSP_MONTANT_A_COMPENSER"/></LABEL></TD>
							<TD><INPUT type="text" name="montant" value="<%=viewBean.getMontant()%>" class="montant" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);"></TD>
							<TD><LABEL for="noFacture"><ct:FWLabel key="JSP_NO_FACTURE"/></LABEL></TD>
							<TD>
								<ct:FWPopupList name="noFacture" 
								onFailure="resetNoFacture();" 
								onChange="noFactureChange(tag);"		
								params="<%=params%>" 
								value="<%=viewBean.getNoFacture()%>" 
								className="libelle" 
								jspName="<%=jspLocation%>" 
								minNbrDigit="0" 
								autoNbrDigit="9"/><ct:FWLabel key="JSP_FLECHE_BAS_NO_FACT"/>
								<INPUT type="hidden" name="idFacture" value="<%=viewBean.getIdFactureCompta()%>"/>
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