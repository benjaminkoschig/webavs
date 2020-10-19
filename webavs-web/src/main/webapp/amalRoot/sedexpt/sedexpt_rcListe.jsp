<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="globaz.amal.utils.AMSedexHelper"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="ch.globaz.amal.business.constantes.IAMCodeSysteme.AMTraitementsAnnonceSedex"%>
<%@page import="ch.globaz.amal.business.constantes.IAMCodeSysteme.AMStatutAnnonceSedex"%>
<%@page import="ch.globaz.amal.business.constantes.AMMessagesTypesAnnonceSedex"%>
<%@page import="ch.globaz.amal.business.constantes.AMMessagesSubTypesAnnonceSedex"%>
<%@page import="globaz.amal.vb.sedexpt.AMSedexptViewBean"%>
<%@page import="globaz.amal.vb.sedexpt.AMSedexptListViewBean"%>
<%@page import="ch.horizon.jaspe.util.JACalendarGregorian"%>
<%@page import="globaz.globall.util.JACalendar"%>
<%@page import="globaz.globall.util.JADate"%>
<%@page import="globaz.jade.client.util.JadeDateUtil"%>

<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="ch.globaz.amal.business.constantes.IAMCodeSysteme"%>
<%@page import="ch.globaz.amal.business.constantes.IAMActions"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Arrays"%>
<%@page import="globaz.globall.parameters.FWParametersCode"%>
<%@page import="java.util.Iterator"%>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>
	    <%
	    	AMSedexptListViewBean viewBean=(AMSedexptListViewBean)request.getAttribute("viewBean");
   	    	size=viewBean.getSize();
	    	    	    	
   	    	menuName = "amal-menuprincipal";	 
   	    	
   	    	boolean hasRightUpdate = objSession.hasRight("amal.sedexpt.sedexpt", FWSecureConstants.UPDATE);
	    %>
<%-- /tpl:insert --%>
<script type="text/javascript">
var hasRightUpdate = <%=hasRightUpdate%>;

$(document).ready(function() {
	if(hasRightUpdate) {
		$(".classImgToValidate").click(function() {
			
			if (confirm("Changer le status en \"Traité manuellement\" ?")) {
			
			var idArray = $(this).attr("id");
			var id = idArray.split('_')[1];
			var o_options= {
					serviceClassName: 'ch.globaz.amal.business.services.sedexRP.AnnoncesRPService',
					serviceMethodName:'changeTraitement',
					parametres:id+",42005701",
					callBack: setToValidateCallBack
				}
				globazNotation.readwidget.options=o_options;		
				globazNotation.readwidget.read();	
			}
		});	
	}
	
	function setToValidateCallBack(data) {
		var dataCallBackId = data.split('_')[0];
		var dataCallBackError = data.split('_')[1];
		if (dataCallBackError.length>0) {
			alert("Erreur lors de la mise à jour du traitement ! ==> "+data);
		} else {
			$("#imgSedexTraite_"+dataCallBackId).show();
			$("#imgSedexTraitementManuel_"+dataCallBackId).show();
			$("#imgToValidate_"+dataCallBackId).hide();
			
			//$("#imgToValidate_"+dataCallBackId).attr("src","<%=request.getContextPath()%>/images/amal/sedex_traite.png");
			//$("#imgToValidate_"+dataCallBackId).append("<img src=\"<%=request.getContextPath()%>/images/amal/sedex_traite.png\"");
			$("#imgToValidate_"+dataCallBackId).parent().attr("title","Traitement manuel");
		}
	}
});
</script>
<%@ include file="/theme/list/javascripts.jspf" %>	   	    
	    <%-- tpl:insert attribute="zoneHeaders" --%>
	    
	    <TH>&nbsp;</TH>
		<TH>Date</TH>  
		<TH>Assureur</TH> 		
   		<TH>Message</TH>
   		<TH>Status</TH>
<%--   		<TH>Traité</TH>--%>
   		<TH>Membre</TH>
   		<TH>Période</TH>
   		<TH>Contribution</TH>   		
	    <%-- /tpl:insert --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:insert attribute="zoneCondition" --%>
    <%
    	AMSedexptViewBean line = (AMSedexptViewBean)viewBean.getEntity(i);
    %>
    <%-- /tpl:insert --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
<% String detailSubsideLink = "amal?userAction=amal.detailfamille.detailfamille.afficher&selectedId="+line.getComplexAnnonceSedex().getSimpleDetailFamille().getIdDetailFamille(); %>
		<%-- tpl:insert attribute="zoneList" --%>
			<TD class="mtd" nowrap >
			<ct:menuPopup menu="amal-optionsannoncesrp" detailLabelId="MENU_SEDEXRP_SUBSIDE_DETAIL" detailLink="<%=detailSubsideLink%>">
	     			<ct:menuParam key="selectedId" value="<%=line.getComplexAnnonceSedex().getSimpleFamille().getIdFamille()%>"/>  
		 	</ct:menuPopup>
				<%
				String msgType = line.getComplexAnnonceSedex().getSimpleAnnonceSedex().getMessageType();
				if (AMMessagesTypesAnnonceSedex.DEMANDE_PRIME_TARIFAIRE.getValue().equals(msgType)) {
				%>
					<img
					width="22px"
					height="22px"
					src="<%=request.getContextPath()%>/images/amal/sedex_outbox.png" title="Envoi (<%=line.getComplexAnnonceSedex().getId()%>)" border="0">
				<% } else { %>
					<img
					width="22px"
					height="22px"
					src="<%=request.getContextPath()%>/images/amal/sedex_inbox.png" title="Réception (<%=line.getComplexAnnonceSedex().getId()%>)" border="0">				
				<% }%>
				<!-- Icone E/R --> 
			</TD>
			<TD class="mtd" nowrap >
				<%=line.getComplexAnnonceSedex().getSimpleAnnonceSedex().getDateMessage() %>&nbsp;
			</TD>
			<TD class="mtd" nowrap >
				<%=line.getComplexAnnonceSedex().getCaisseMaladie().getTiers().getDesignation1() %>&nbsp;
			</TD>	
			<TD class="mtd" nowrap >
				<%=AMMessagesSubTypesAnnonceSedex.getSubTypeCSLibelle(line.getComplexAnnonceSedex().getSimpleAnnonceSedex().getMessageSubType())%>&nbsp;
			</TD>
			<TD class="mtd" nowrap align="center">
			<%
			String imgName = "";
			String libelleImg = "";
			String status = line.getComplexAnnonceSedex().getSimpleAnnonceSedex().getStatus();
			if (AMStatutAnnonceSedex.CREE.getValue().equals(status)) {
				imgName = "sedex_creation_ok.png";
				libelleImg = "Crée";
			} else if (AMStatutAnnonceSedex.ERROR_CREE.getValue().equals(status)) {
				imgName = "sedex_creation_ko.png";
				libelleImg = "Erreur création";
			} else if (AMStatutAnnonceSedex.ENVOYE.getValue().equals(status)) {
				imgName = "sedex_envoi_ok.png";
				libelleImg = "Envoyé";
			} else if (AMStatutAnnonceSedex.ERROR_ENVOYE.getValue().equals(status)) {
				imgName = "sedex_envoi_ko.png";
				libelleImg = "Erreur envoyé";
			} else if (AMStatutAnnonceSedex.RECU.getValue().equals(status)) {
				imgName = "sedex_recu_ok.png";
				libelleImg = "Reçu";
			} else if (AMStatutAnnonceSedex.ERREUR_RECU.getValue().equals(status)) {
				imgName = "sedex_recu_ko.png";
				libelleImg = "Erreur reçu";
			} else {
				imgName = "";
			}
			%>
			<%
			if (!JadeStringUtil.isEmpty(imgName)) { %>
			<img
					width="20px"
					height="20px"
					src="<%=request.getContextPath()%>/images/amal/<%=imgName%>" title="<%=libelleImg%>" border="0">
			<% } else { %>
				&nbsp;
			<% } %>
			</TD>
<%--			<%--%>
<%--			imgName = "";--%>
<%--			libelleImg = "";--%>
<%--			String traitement = line.getComplexAnnonceSedex().getSimpleAnnonceSedex().getTraitement();--%>
<%--			if (AMTraitementsAnnonceSedex.TRAITE_AUTO.getValue().equals(traitement)) {--%>
<%--				%>--%>
<%--				<TD class="mtd" nowrap align="center" title="Traitement automatique">--%>
<%--				<img--%>
<%--					width="16px"--%>
<%--					height="16px"--%>
<%--					src="<%=request.getContextPath()%>/images/amal/sedex_traite.png" border="0">--%>
<%--				<img--%>
<%--					width="16px"--%>
<%--					height="16px"--%>
<%--					src="<%=request.getContextPath()%>/images/amal/sedex_traitement_auto.png" border="0">--%>
<%--				</TD>				--%>
<%--				<%--%>
<%--			} else if (AMTraitementsAnnonceSedex.TRAITE_MANU.getValue().equals(traitement)) {--%>
<%--				%>--%>
<%--				<TD class="mtd" nowrap align="center" title="Traitement manuel">--%>
<%--				<img--%>
<%--					width="16px"--%>
<%--					height="16px"--%>
<%--					src="<%=request.getContextPath()%>/images/amal/sedex_traite.png" border="0"/>--%>
<%--				<img--%>
<%--					width="16px"--%>
<%--					height="16px"--%>
<%--					src="<%=request.getContextPath()%>/images/amal/sedex_traitement_manual.png" border="0"/>--%>
<%--					</TD>--%>
<%--				<%--%>
<%--			}  else {--%>
<%--				%>--%>
<%--					<TD class="mtd classImgToValidate" nowrap align="center" title="A traiter" id="toValidate_<%=line.getComplexAnnonceSedex().getId()%>">--%>
<%--						<img--%>
<%--							id="imgToValidate_<%=line.getComplexAnnonceSedex().getId()%>" --%>
<%--							width="16px"--%>
<%--							height="16px"--%>
<%--							src="<%=request.getContextPath()%>/images/amal/valide_manual.png" border="0">--%>
<%--					--%>
<%--						--%>
<%--						<img--%>
<%--							style="display:none"--%>
<%--							id="imgSedexTraite_<%=line.getComplexAnnonceSedex().getId()%>"--%>
<%--							width="16px"--%>
<%--							height="16px"--%>
<%--							src="<%=request.getContextPath()%>/images/amal/sedex_traite.png" border="0"/>--%>
<%--						<img--%>
<%--							style="display:none"--%>
<%--							id="imgSedexTraitementManuel_<%=line.getComplexAnnonceSedex().getId()%>"--%>
<%--							width="16px"--%>
<%--							height="16px"--%>
<%--							src="<%=request.getContextPath()%>/images/amal/sedex_traitement_manual.png" border="0"/>--%>
<%--					</TD>--%>
<%--				<%--%>
<%--			}--%>
<%--			%>--%>
<%--			</TD>					--%>
			<TD class="mtd" nowrap title="Contribuable principal : <%=line.getComplexAnnonceSedex().getContribuable().getFamille().getNomPrenom() %>">
				<%=line.getComplexAnnonceSedex().getSimpleFamille().getNomPrenom() %>&nbsp;
			</TD>
			<TD class="mtd" nowrap >
				<%=AMSedexHelper.getPeriodeInfo(line.getComplexAnnonceSedex().getSimpleAnnonceSedex().getMessageSubType(), line.getComplexAnnonceSedex().getSimpleAnnonceSedex().getMessageContent()) %>
				&nbsp;
			</TD>
			<TD class="mtd" nowrap align="right">
				<%=AMSedexHelper.getMontant(line.getComplexAnnonceSedex())%>&nbsp;
			</TD>
		<%-- /tpl:insert --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:insert attribute="zoneTableFooter" --%>
	<%-- /tpl:insert --%>
<%@ include file="/theme/list/tableEnd.jspf" %>