<%@page import="globaz.perseus.utils.PFGestionnaireHelper"%>
<%@page import="globaz.perseus.utils.PFUserHelper"%>
<%@page import="ch.globaz.pyxis.business.model.PersonneEtendueComplexModel"%>
<%@page import="globaz.perseus.vb.demande.PFCopieDemandeViewBean"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>

<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>

<%

	PFCopieDemandeViewBean viewBean = (PFCopieDemandeViewBean) session.getAttribute("viewBean"); 
	idEcran="PPF0321";
	autoShowErrorPopup = true;
	
	PersonneEtendueComplexModel personne = viewBean.getDemande().getDossier().getDemandePrestation().getPersonneEtendue();
	String affichePersonnne = "";
	
	affichePersonnne = PFUserHelper.getDetailAssure(objSession,personne);
	
	//bButtonDelete = !SimpleZoneForfaitsChecker.isIdUsedInOthersTableWithOutException(viewBean.getSimpleZoneForfaits());
	
		if(objSession.hasRight("perseus", FWSecureConstants.ADD)){
			bButtonCancel = true;
			bButtonValidate = true;
		}else{
			bButtonCancel = false;
			bButtonValidate = false;
		}

%>

<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>

<<script type="text/javascript">

	function add() {}
	function upd() {}	
	function validate() {
	    document.forms[0].elements('userAction').value="perseus.demande.copieDemande.modifier";
		return true;
	}
	function cancel() {
		document.forms[0].elements('userAction').value="back";
	}
	
	
	function init(){}
	function postInit(){
	}

</script>

<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:insert attribute="zoneTitle" --%><ct:FWLabel key="JSP_PF_DEM_COPIE_TITRE"/><%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:insert attribute="zoneMain" --%>
						<tr>
							<td>
							
	<table width="100%">
		<tr>
			<td><ct:FWLabel key="JSP_PF_DEM_D_ASUURE"/></td>
			<td><%=affichePersonnne %></td>
		</tr>
		<tr><td colspan="2"><hr></td></tr>
		<tr>
			<td>
				<b><ct:FWLabel key="JSP_PF_FAMILLE_D_PERIODE"/></b>
			</td>
			<td colspan="2"><ct:FWLabel key="JSP_PF_FAMILLE_D_PERIODE_DU"/>
				<span><strong><%=" "+viewBean.getDemande().getSimpleDemande().getDateDebut()+ " " %></strong></span>
				<ct:FWLabel key="JSP_PF_FAMILLE_D_PERIODE_AU"/>
				<span><strong><%=" "+viewBean.getDemande().getSimpleDemande().getDateFin()%></strong></span>
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