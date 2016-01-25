<%-- tpl:insert page="/theme/detail_ajax.jtpl" --%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="globaz.corvus.api.ordresversements.IREOrdresVersements"%>
<%@page import="globaz.pegasus.utils.PCUserHelper"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@page import="globaz.pegasus.vb.lot.PCOrdreVersementViewBean"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail_ajax/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%@ taglib uri="/corvusRoot/corvustaglib.tld" prefix="re" %>
<%
	// Les labels de cette page commence par la préfix "JSP_LOT_ORDRE_VERSMENT_D"
	
	idEcran="PPC0095";

	PCOrdreVersementViewBean viewBean = (PCOrdreVersementViewBean) session.getAttribute("viewBean");	

	
	String idTierRequerant 		= viewBean.getPrestation().getTiersBeneficiaire().getPersonneEtendue().getIdTiers();

	bButtonNew 		= false;
	bButtonDelete 	= false;
	bButtonCancel   = false;
	bButtonUpdate   = false;
	bButtonValidate = false;

	
	String menuOptionToLoad = request.getParameter("menuOptionToLoad");
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/javascripts.jspf" %>
<%@ include file="/pegasusRoot/ajax/javascriptsAndCSS.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<script language="JavaScript"> 
	globazGlobal.idPrestation = "<%=viewBean.getPrestation().getSimplePrestation().getIdPrestation()%>";
	globazGlobal.ACTION_AJAX = "<%=IPCActions.ACTION_LOT_ORDRE_VERSEMENT_AJAX%>";
	globazGlobal.CS_ORDRE_VERSEMENT_TYPE_DETTE ="<%=IREOrdresVersements.CS_TYPE_DETTE%>";
	
	/*
	
	var o_options, 
		ajax;
	
	o_options = {
		serviceClassName: "ch.globaz.pegasus.business.services.models.lot.LotService",
		parametres: globazGlobal.idPrestation,
		cstCriterias: '',
		serviceMethodName:"generateJournalByIdPrestation",
		callBack: function (data) {
			console.log(data)
		},
		errorCallBack: null
	};
	ajax = Object.create($.extend(true, {}, globazNotation.readwidget));
	ajax.options = o_options;
	ajax.read();
	
*/
</script>

<script type="text/javascript" src="<%=servletContext%>/scripts/ajax/DefaultTableAjax.js"/></script>
<script type="text/javascript" src="<%=rootPath %>/scripts/lot/OrdreVersementPart.js"></script>
<style>

	.TitreOv{
		text-align: left !important; 
		background-color:#B7E3FE !important; 
	}


.toRight {
	right:0;
	top:0;
	position:absolute;
	display: inline-block; 
	margin: 5px 20px 0 0"
}

</style>
<script>
	$(function () {
		//$("#linkDecompte").button();
	
	})
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>
<ct:FWLabel key="JSP_OVE_D_TItrE"/>
<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyStart2.jspf" %>
			<%-- tpl:put name="zoneMain" --%>

			<tr>
				<td colspan="6" class="formTableLess">
				
					<span class="ui-widget-header"><ct:FWLabel key="JSP_LOT_ORDRE_VERSEMENT_R_BENEFICIAIRE"/></span>			
							
					<div class="likeMainContainerAjax"  style="position: relative">
						<span> <%=PCUserHelper.getDetailAssure(idTierRequerant) %> </span>
							<span id="linkDecompte" class="toRight">
								<a data-g-externallink="reLoad:false" 
								   href="<%=servletContext%>/pegasus?userAction=pegasus.decision.decompt.afficher&idVersionDroit=<%=viewBean.getPrestation().getSimplePrestation().getIdVersionDroit()%>">
										<ct:FWLabel key="JSP_PC_PRE_D_DECOMPTE"/>
								</a>
							</span>
							
					</div>
					
					<span class="ui-widget-header"><ct:FWLabel key="JSP_PC_PRE_D_TITRE"/></span>
					<div class="likeMainContainerAjax ">
						<table class="areaTable formTableLess" width="100%">
							<tr>
								<td>
									<span class="lable"><ct:FWLabel key="JSP_PC_PRE_D_NO"/></span>
									<span class="data"><%=viewBean.getPrestation().getSimplePrestation().getIdPrestation()%></span>
								</td>
								<td>
									<span class="lable"><ct:FWLabel key="JSP_PC_PRE_D_NO_LOT"/></span>
									<span class="data"><%=viewBean.getPrestation().getSimplePrestation().getIdLot()%></span>
								</td>
								<td>
									<span class="lable"><ct:FWLabel key="JSP_PC_PRE_D_ETAT"/></span>
									<span class="data"><%=objSession.getCodeLibelle(viewBean.getPrestation().getSimplePrestation().getCsEtat())%></span>
								</td>
							</tr>
							<tr>
								<td>
									<span class="lable"><ct:FWLabel key="JSP_PC_PRE_D_MOIS_ANNEE"/></span>
									<span class="data"><%=viewBean.getPrestation().getSimplePrestation().getDateDebut() +" - "+viewBean.getPrestation().getSimplePrestation().getDateFin()%></span>
								</td>
								<td>
				
								</td>
								<td>
									<span class="lable"><ct:FWLabel key="JSP_LOT_ORDRE_VERSEMENT_R_MONTANT"/></span>
									<span class="data" data-g-amountForm><%= new FWCurrency (viewBean.getPrestation().getSimplePrestation().getMontantTotal()).toStringFormat()%></span>
								</td>
							</tr>
	
						</table>
					</div>
				
				<span class="ui-widget-header"><ct:FWLabel key="JSP_OVE_D_TItrE"/></span>
					<div class="area areaOV" style="position: relative;">
					<span id="displayRowOv" class="toRight"></span>
						<table class="areaTable" width="100%">
							<!-- 
							<thead>
								<tr> 
									<th><ct:FWLabel key="JSP_PC_LOT_ORDRE_VERSEMENT_L_DESIGNATION"/></th>
								    <th><ct:FWLabel key="JSP_PC_LOT_ORDRE_VERSEMENT_L_MOTANT"/></th>
								    <th><ct:FWLabel key="JSP_PC_LOT_ORDRE_VERSEMENT_L_TYPE"/></th>
								    <th><ct:FWLabel key="JSP_PC_LOT_ORDRE_VERSEMENT_L_NO"/></th>
								    <th><ct:FWLabel key="JSP_PC_LOT_ORDRE_VERSEMENT_L_TYPE_PC_ACCORDE"/></th>
								</tr>
							</thead>
							 -->
							<tbody>
					
							</tbody>
						</table>
						<div id="main" class="formTableLess areaDetail"> 
							<table  width="80%">
								<tr>
									<td>
										<label><ct:FWLabel key="JSP_LOT_ORDRE_VERSEMENT_D_BENEFICIAIRE"/></label>
									</td>
									<td>
										<span class="data" id="beneficiaire"> </span>
									</td>
									<td>
										<label><ct:FWLabel key="JSP_LOT_ORDRE_VERSEMENT_D_TYPE"/></label>
									</td>
									<td>
										<span class="data" id="ordreVersementCsTypeLibelle"></span>
									</td>
									<td>
										<label><ct:FWLabel key="JSP_LOT_ORDRE_VERSEMENT_D_MONTANT"/></label>	
									</td>
									<td>
										<span class="data" id="montantOv" data-g-amountformater=" " > </span>
									</td>
								</tr>
								<tr class="notInDette">
									<td>
										<label><ct:FWLabel key="JSP_LOT_ORDRE_VERSEMENT_D_DOMAINE"/></label>	
									</td>
									<td>
										<span class="data" type="text" id="csDomaineOv" ></span>
									</td>
									<%if(viewBean.isSousTypeGenrePrestationActif()) { %>
									<td>
										<label><ct:FWLabel key="JSP_LOT_ORDRE_VERSEMENT_SOUS_TYPE"/></label>	
									</td>
									<td>
										<span class="data" type="text" id="ordreVersement.sousTypeGenrePrestation" ></span>
									</td>
									<td colspan="2">&nbsp;</td>
									<%} else {%>
										<td colspan="4">&nbsp;</td>
									<%}%>
									
								</tr>
								<tr  class="notInDette">	
									<td colspan="6">
										<label><ct:FWLabel key="JSP_LOT_ORDRE_VERSEMENT_D_ADRESSE_PAIEMENT"/></label>
										<pre style="position:relative; left:56px"><span class="data" id="adressePaiement" class="adressePre"></span></pre>
									</td>
								</tr>
							</table>
						</div>
					</div>
				</td>
			</tr>
			
				<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/footer.jspf" %>
<%-- /tpl:insert --%>