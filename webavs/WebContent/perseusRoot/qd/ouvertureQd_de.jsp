<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="ch.globaz.perseus.business.models.situationfamille.MembreFamille"%>
<%@page import="globaz.perseus.utils.PFUserHelper"%>
<%@page import="ch.globaz.pyxis.business.model.PersonneEtendueComplexModel"%>
<%@page import="globaz.perseus.vb.qd.PFOuvertureQdViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>
<%

	PFOuvertureQdViewBean viewBean = (PFOuvertureQdViewBean) session.getAttribute("viewBean");
	idEcran="PPF1141";
	
	PersonneEtendueComplexModel personne = viewBean.getDemande().getDossier().getDemandePrestation().getPersonneEtendue();
	String affichePersonnne = PFUserHelper.getDetailAssure(objSession,personne);
	
	if(objSession.hasRight("perseus", FWSecureConstants.ADD)){
		bButtonCancel = true;
		bButtonValidate = true;
	}else{
		bButtonCancel = false;
		bButtonValidate = false;
	}
	
%>
<script type="text/javascript">

	function validate() {
	    state = true;
	    
	    var listMfSelected = "";
	    
	    $('.mfqd').each(function() { 
                var $this = $(this); 
                if ($this.is(":checked")) { 
                	listMfSelected += $this.val() + "," 
                } 
        }); 
	    if (listMfSelected.length > 0) {
	    	listMfSelected = listMfSelected.substring(0, listMfSelected.length-1);
	    }
	    
	    $("#qdToOpen").val(listMfSelected); 
	    
	    document.forms[0].elements('userAction').value="perseus.qd.ouvertureQd.modifier";
	    return state;
	}    
	
	function add() {}
	function upd() {}
	function del() {}
	
	//Fonction permettant d'annuler une opération en cours
	function cancel() {
		document.forms[0].elements('userAction').value="perseus.demande.demande.chercher";
	}
	
	function init() {}
	function postInit() {}

</script>
<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:insert attribute="zoneTitle" --%><ct:FWLabel key="MENU_PF_OPTION_OUVERTURE_QD"/><%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:insert attribute="zoneMain" --%>
						<tr>
							<td>
							
	<table width="100%">
		<tr>
			<td valign="top" width="300"><ct:FWLabel key="JSP_PF_DEM_D_ASUURE"/></td>
			<td>
				<%=affichePersonnne %>
				<input type="hidden" name="idDemande" value="<%=viewBean.getDemande().getId() %>"/>	
			</td>
		</tr>
		<tr>
			<td>
				<ct:FWLabel key="JSP_PF_FAMILLE_D_PERIODE"/>
			</td>
			<td><ct:FWLabel key="JSP_PF_FAMILLE_D_PERIODE_DU"/>
				<span><strong><%=" "+viewBean.getDemande().getSimpleDemande().getDateDebut()+ " " %></strong></span>
				<ct:FWLabel key="JSP_PF_FAMILLE_D_PERIODE_AU"/>
				<span><strong><%=" "+viewBean.getDemande().getSimpleDemande().getDateFin()%></strong></span>
			</td>
		</tr>
		<tr><td colspan="2"><hr></td></tr>
		<tr>
			<td>
				<ct:FWLabel key="JSP_PF_QD_OUVERTURE_ANNEE"/>
			</td>
			<td>
				<%=viewBean.getDemande().getSimpleDemande().getDateDebut().substring(6) %>
			</td>
		</tr>
		<tr>
			<td valign="top">
				<ct:FWLabel key="JSP_PF_QD_OUVERTURE_MEMBRES_FAMILLES"/>
			</td>
			<td>
			<% 
				for (MembreFamille mf : viewBean.getListMembresFamilles()) { 
			%>
				<% if (viewBean.getListIdMembresFamillesAvecQD().contains(mf.getId())) { %>
					<input type="checkbox" name="qdOpened" checked="checked" readonly="readonly" disabled="disabled"/>
				<% } else { %>
					<input type="checkbox" class="mfqd" name="qdToOpen_<%=mf.getId() %>" value="<%=mf.getId() %>"/>
				<% } %>
				<%=mf.getPersonneEtendue().getTiers().getDesignation1() + " " + mf.getPersonneEtendue().getTiers().getDesignation2() %>
				<br/>
			<%  
				} 
			%>
			<input type="hidden" name="qdToOpen" id="qdToOpen" value=""/>
			</td>
		</tr>
	</table>						
							</td>
						</tr>
						<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>
