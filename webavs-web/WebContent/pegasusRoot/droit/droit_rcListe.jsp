<%-- tpl:insert page="/theme/list.jtpl" --%><%@page import="ch.globaz.pegasus.business.constantes.IPCDroits"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%
	//Les labels de cette page commence par le préfix "JSP_PC_DRO_L"

	PCDroitListViewBean viewBean = (PCDroitListViewBean) request.getAttribute("viewBean");
	size = viewBean.getSize();
	
	String messageErrorDroit = (String)request.getAttribute("messageErrorDroit");
	messageErrorDroit = globaz.framework.util.FWTextFormatter.slash(globaz.framework.util.FWTextFormatter.newLineToBr(messageErrorDroit), '\"');

	detailLink = "pegasus?userAction="+IPCActions.ACTION_DROIT+ ".afficher&selectedId=";
	
	menuName = "pegasus-menuprincipal";
	
	boolean hasVersionDroitModifiable = PCDroitHandler.hasVersionDroitModifiable(viewBean);	
		
%>
	
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
<script type="text/javascript">
var temp = "<%=messageErrorDroit%>";

if (temp != "null") {
	errorObj.text = temp;
}

</script>
<style>
	.errorAdaptation{
		background-color: #D58383;
	}
</style>
	    <%-- tpl:put name="zoneHeaders" --%>
<%@page import="globaz.pegasus.vb.droit.PCDroitListViewBean"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDecision"%>
<%@page import="globaz.pegasus.vb.droit.PCDroitViewBean"%>

<%@page import="ch.globaz.pegasus.business.models.droit.Droit"%>
<%@page import="ch.globaz.pyxis.business.model.PersonneEtendueComplexModel"%>
<%@page import="globaz.pegasus.utils.PCUserHelper"%>
<%@page import="globaz.jade.client.util.JadeDateUtil"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.prestation.tools.PRDateFormater"%>
<%@page import="globaz.pegasus.utils.PCDroitHandler"%><TH>&nbsp;</TH>
   	<TH><ct:FWLabel key="JSP_PC_DRO_L_DETAIL_ASSURE"/></TH>
   	<TH><ct:FWLabel key="JSP_PC_DRO_L_DATE_ANNONCE"/></TH>
   	<TH><ct:FWLabel key="JSP_PC_DRO_L_MOTIF"/></TH>
   	<TH><ct:FWLabel key="JSP_PC_DRO_L_ETAT"/></TH>
   	<TH><ct:FWLabel key="JSP_PC_DRO_L_NO_DROIT"/></TH>
   	<TH><ct:FWLabel key="JSP_PC_DRO_L_NO_VERSION"/></TH>

	    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
		<%
		PCDroitViewBean courant = (PCDroitViewBean) viewBean.get(i);
		Droit droitCourant=courant.getDroit();
		PersonneEtendueComplexModel personne=droitCourant.getDemande().getDossier().getDemandePrestation().getPersonneEtendue();
			
			String detailUrlParams=detailLink + droitCourant.getSimpleVersionDroit().getId() +
            "&idDemandePc=" + droitCourant.getSimpleDroit().getIdDemandePC() +
            "&idDroit=" + droitCourant.getSimpleDroit().getIdDroit() +
            "&noVersion=" + droitCourant.getSimpleVersionDroit().getNoVersion() +
            "&idVersionDroit=" + droitCourant.getSimpleVersionDroit().getIdVersionDroit();
		
			String detailUrl = "parent.location.href='" + detailUrlParams +
			                   "'";
		%>
		
		<TD class="mtd <%= courant.getCssDroitDiffAdaptaion() %>" nowrap width="20px">
	     	<ct:menuPopup menu="pegasus-optionsdroitsdetail" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=detailUrlParams%>">
	     		<ct:menuParam key="selectedId" value="<%=droitCourant.getSimpleDroit().getId()%>"/>
	     		<ct:menuParam key="idDemandePc" value="<%=droitCourant.getSimpleDroit().getIdDemandePC()%>"/>
	     		<ct:menuParam key="idDroit" value="<%=droitCourant.getSimpleDroit().getIdDroit()%>"/>
	     		<ct:menuParam key="noVersion" value="<%=droitCourant.getSimpleVersionDroit().getNoVersion()%>"/>
	     		<ct:menuParam key="idVersionDroit" value="<%=droitCourant.getSimpleVersionDroit().getIdVersionDroit()%>"/>
	            <ct:menuParam key="idDossier" value="<%=droitCourant.getDemande().getSimpleDemande().getIdDossier()%>" />
	     		
	     		<!--  Si l'état pas octroi ou revision on cache le menu -->
		 		<% if(!courant.getReadyForDecisionSuppression()){%>
		 			<ct:menuExcludeNode nodeId="DECSUPP"/>
		 		<%}else{%>
		 			<!-- on utilise le meme parametre pour les deux types de decision car on peut 
		 			preparer l'une ou l'autre mais pas les deux en meme temps -->
		 			<ct:menuParam key="csTypeDecisionPrep" value="<%= IPCDecision.CS_TYPE_SUPPRESSION_SC %>"/>
		 		<%}%>
		 		<!--  Si l'état pas calcule  le noeud decision apres calcul -->
		 		<% if(!courant.getReadyForDecisionApresCalcul()){%>
		 			<ct:menuExcludeNode nodeId="DECAPCA"/>
		 		<%}else{%>
		 			<!-- on utilise le meme parametre pour les deux types de decision car on peut 
		 			preparer l'une ou l'autre mais pas les deux en meme temps -->
		 			<ct:menuParam key="csTypeDecisionPrep" value="<%= IPCDecision.CS_TYPE_OCTROI_AC %>"/>
		 		<%}%>
		 		<!--  Uniquement pour la version du droit validee si il nexiste pas une autre version "exploitable" -->
		 		<% if(!PCDroitHandler.isVersionDroitCorrigeable(droitCourant.getSimpleVersionDroit().getCsEtatDroit(),hasVersionDroitModifiable)) {%>
	     		<ct:menuExcludeNode nodeId="pcDroitCorriger"/>
	     		<%}%>
		 		<!--  Si le droit n'est pas en état calculé, au calcul ou enregistré, cacher l'option calculer
		 			On ne donne pas la possibilité de calculer un droit d'adaptation car le calcule doit être executer depuis l'écran de l'adaptation
		 		 -->
		 		<% if(!(courant.getReadyForCalcul() && !IPCDroits.CS_MOTIF_DROIT_ADAPTATION.equals(droitCourant.getSimpleVersionDroit().getCsMotif()))){%>
		 			<ct:menuExcludeNode nodeId="pcDroitCalcul"/>
		 		<%}%>
	     		<%-- Jamais de suppression de version de droit depuis la liste des droit (pour eviter les suppression involontaires)--%>
	     		<ct:menuExcludeNode nodeId="pcDroitSupprimer"/>
		 	</ct:menuPopup>
     	</TD>
		<TD class="mtd <%= courant.getCssDroitDiffAdaptaion() %>" nowrap onClick="<%=detailUrl%>">
			<div style="position:relative;">
				<%=PCUserHelper.getDetailAssure(objSession,personne)%>
				<span style="position:absolute; top:0; right:0" 
					 	data-g-note="idExterne:<%=droitCourant.getSimpleVersionDroit().getIdVersionDroit()%>, 
					 				tableSource:PCVERDRO, inList: true">
				</span>
			</div>
		</TD>
		<TD class="mtd <%= courant.getCssDroitDiffAdaptaion() %>" nowrap onClick="<%=detailUrl%>">
			<%=droitCourant.getSimpleVersionDroit().getDateAnnonce()%>
		</TD>
		<TD class="mtd <%= courant.getCssDroitDiffAdaptaion() %>" nowrap onClick="<%=detailUrl%>"><%=objSession.getCodeLibelle(droitCourant.getSimpleVersionDroit().getCsMotif())%></TD>
		<TD class="mtd <%= courant.getCssDroitDiffAdaptaion() %>" nowrap onClick="<%=detailUrl%>"><%=objSession.getCodeLibelle(droitCourant.getSimpleVersionDroit().getCsEtatDroit())%></TD>
		<TD class="mtd <%= courant.getCssDroitDiffAdaptaion() %>" nowrap onClick="<%=detailUrl%>"><%=droitCourant.getSimpleVersionDroit().getIdVersionDroit()%></TD>
		<TD class="mtd <%= courant.getCssDroitDiffAdaptaion() %>" nowrap onClick="<%=detailUrl%>"><%=droitCourant.getSimpleVersionDroit().getNoVersion()%></TD>
		
		<%-- /tpl:put --%>

<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>