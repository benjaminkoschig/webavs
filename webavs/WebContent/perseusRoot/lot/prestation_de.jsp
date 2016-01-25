<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="ch.globaz.perseus.business.constantes.CSEtatPrestation"%>
<%@page import="ch.globaz.perseus.business.constantes.CSEtatLot"%>
<%@page import="ch.globaz.perseus.business.constantes.CSTypeLot"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/corvusRoot/corvustaglib.tld" prefix="re" %>
<%
	// Les labels de cette page commence par la préfix "JSP_PF_PRE_D"

	idEcran="PPF0751";

	PFPrestationViewBean viewBean = (PFPrestationViewBean) session.getAttribute("viewBean");
	selectedIdValue = viewBean.getId();
	if ((CSTypeLot.LOT_FACTURES.getCodeSystem().equals(viewBean.getPrestation().getLot().getSimpleLot().getTypeLot()) || CSTypeLot.LOT_FACTURES_RP.getCodeSystem().equals(viewBean.getPrestation().getLot().getSimpleLot().getTypeLot())) 
			&& !CSEtatPrestation.COMPTABILISE.getCodeSystem().equals(viewBean.getPrestation().getSimplePrestation().getEtatPrestation())
			&& objSession.hasRight("perseus", FWSecureConstants.ADD)) {
		bButtonDelete = true;
	} else {
		bButtonDelete = false;
	}
	bButtonUpdate = false;
	bButtonCancel = true;
	
	String idTiers = viewBean.getIdTiersRequerant();
	
	
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@page import="globaz.perseus.vb.lot.PFPrestationViewBean"%>
<%@page import="globaz.prestation.jsp.taglib.PRDisplayRequerantInfoTag"%>
<%@page import="globaz.globall.db.BSession"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<ct:menuChange displayId="menu" menuId="perseus-menuprincipal"/>
<ct:menuChange displayId="options" menuId="perseus-optionsprestation" showTab="options">
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getPrestation().getSimplePrestation().getId()%>"/>
	<ct:menuSetAllParams key="montantPrestation" value="<%=viewBean.getPrestation().getSimplePrestation().getMontantTotal()%>"/>
	<ct:menuSetAllParams key="idTierRequerant" value="<%=idTiers%>"/>
	<ct:menuSetAllParams key="idFacture" value="<%=viewBean.getPrestation().getSimplePrestation().getIdFacture()%>"/>
	<ct:menuSetAllParams key="idDecision" value="<%=viewBean.getPrestation().getSimplePrestation().getIdDecisionPcf()%>"/>
			
	<!-- Ajout d'une vérification pour afficher dans l'option, le lien permettant de voir le detail d'une facture -->
	<%if (!CSTypeLot.LOT_DECISION.getCodeSystem().equals(viewBean.getPrestation().getLot().getSimpleLot().getTypeLot())) {%>
			<ct:menuActivateNode active="no" nodeId="DECISION" />
	<%} else { %>
			<ct:menuActivateNode active="yes" nodeId="DECISION" />
	<%} %>
	<%if (!CSTypeLot.LOT_FACTURES.getCodeSystem().equals(viewBean.getPrestation().getLot().getSimpleLot().getTypeLot())) {%>
			<ct:menuActivateNode active="no" nodeId="FACTURE" />
	<%} else { %>
			<ct:menuActivateNode active="yes" nodeId="FACTURE" />
	<%} %>
</ct:menuChange>

<SCRIPT language="javascript"> 
	
	function add(){
	}
	
	function upd(){
	}
	
	function validate() {
	}
	
	function cancel() {
		if (document.forms[0].elements('_method').value == "add"){
		  document.forms[0].elements('userAction').value="back";
		}else{
		  document.forms[0].elements('userAction').value="perseus.lot.prestation.chercher";
		}
	}
	
	function del() {
	    if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
	        document.forms[0].elements('userAction').value="perseus.lot.prestation.supprimer";
	        document.forms[0].submit();
	    }
	}

	function init(){
	}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_PF_PRE_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><ct:FWLabel key="JSP_PF_PRE_D_PRESTATAIRE"/></TD>
							<TD colspan="5">
								<input type="hidden" name="idLot" value="<%=viewBean.getPrestation().getSimplePrestation().getIdLot() %>" />
							
								<re:PRDisplayRequerantInfoTag 
								session="<%=(BSession)controller.getSession()%>" 
								idTiers="<%=idTiers%>"
								style="<%=PRDisplayRequerantInfoTag.STYLE_CONDENSED_WITHOUT_LABEL%>"/>
							</TD>
							<TD>
								<INPUT type="hidden" name=csTypeLot value="<%=viewBean.getPrestation().getLot().getSimpleLot().getTypeLot()%>">
								<INPUT type="hidden" name=csEtatLot value="<%=viewBean.getPrestation().getLot().getSimpleLot().getEtatCs()%>">
								<INPUT type="hidden" name=descriptionLot value="<%=viewBean.getPrestation().getLot().getSimpleLot().getDescription()%>">
							</TD>
							
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_PF_PRE_D_NO"/></TD>
							<TD><INPUT name="no" type="text" class="disabled" readonly value="<%=viewBean.getPrestation().getSimplePrestation().getIdPrestation()%>"></TD>
							<TD><ct:FWLabel key="JSP_PF_PRE_D_NO_LOT"/></TD>
							<TD><INPUT name="noLot" type="text" value="<%=viewBean.getPrestation().getSimplePrestation().getIdLot()%>" class="disabled" readonly></TD>
							<TD><ct:FWLabel key="JSP_PF_PRE_D_ETAT"/></TD>
							<TD><INPUT name="csEtatLibelle" type="text" value="<%=objSession.getCodeLibelle(viewBean.getPrestation().getSimplePrestation().getEtatPrestation())%>" class="disabled" readonly></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_PF_PRE_D_MOIS_ANNEE"/></TD>
							<TD><INPUT name="moisAnnee" type="text" class="disabled"  readonly value="<%=viewBean.getPrestation().getSimplePrestation().getDatePrestation()%>"></TD>
							<TD><ct:FWLabel key="JSP_PF_PRE_D_MONTANT"/></TD>
							<TD colspan="3"><INPUT name="montant" type="text" value="<%=new FWCurrency(viewBean.getPrestation().getSimplePrestation().getMontantTotal()).toStringFormat()%>" class="montantDisabled" readonly></TD>
						</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>