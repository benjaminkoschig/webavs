<%-- tpl:insert page="/theme/list.jtpl" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="ch.globaz.pegasus.businessimpl.utils.PCGedUtils"%>
<%@page import="globaz.framework.controller.FWController"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCPCAccordee"%>
<%@page import="ch.globaz.pegasus.business.constantes.EPCProperties"%>
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/theme/list/header.jspf" %>
<%@ page isELIgnored ="false" %>
<%-- tpl:put name="zoneScripts" --%>

<%
	//Les labels de cette page commence par le préfix "JSP_PC_PCACCORDEE_L"

	PCPcAccordeeListViewBean viewBean = (PCPcAccordeeListViewBean) request.getAttribute("viewBean");
	size = viewBean.getSize();
	
	detailLink = "pegasus?userAction="+IPCActions.ACTION_PCACCORDEE_DETAIL+ ".afficher&selectedId=";

	FWController controllerbis = (FWController) session.getAttribute("objController");
	boolean hasOsirisReadAccess = controllerbis.getSession().hasRight("osiris.comptes.ordresVersement", FWSecureConstants.READ);
	
	menuName = "pegasus-menuprincipal";
	String formAction =  servletContext + mainServletPath;
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
<%@page import="globaz.pegasus.vb.pcaccordee.PCPcAccordeeListViewBean"%>
<%@page import="globaz.prestation.tools.PRDateFormater"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="globaz.pegasus.vb.pcaccordee.PCPcAccordeeViewBean"%>
<%@page import="globaz.pegasus.utils.PCGestionnaireHelper"%>
<%@page import="globaz.pegasus.utils.PCUserHelper"%>
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/pcaccordees/listPca.css"/>
<script type="text/javascript">
$(function () {
	$(".btnDisplayPCAL").click(function () {
		
		
		goToDetailPCAL(this.id.split("_")[1],this.id.split("_")[2]);
	});
});

function goToDetailPCAL(idPca,idBenef){
	window.open("<%=formAction%>?userAction=pegasus.pcaccordee.planCalcul.afficher&idPca="+idPca+"&idBenef="+idBenef);
}

//Ouverture de la GED dans un nouvel onglet
var openGedWindow = function (url){
	window.open(url);
};

</script>
	<th>&nbsp;</th>
	<th>&nbsp;</th>
   	<th  colspan=2><ct:FWLabel key="JSP_PC_PCACCORDEE_L_DETAIL_ASSURE"/></th>
   	
   	<% if (EPCProperties.GESTION_JOURS_APPOINTS.getBooleanValue()){ %>
   	<th><ct:FWLabel key="JSP_PC_PCACCORDEE_L_JA"/></th>
   	<%} %>

   	<% if (EPCProperties.ALLOCATION_NOEL.getBooleanValue()){ %>
   	<TH><ct:FWLabel key="JSP_PC_PCACCORDEE_L_ALOCATION_NOEL"/></TH>
   	<%} %>
   	<TH><ct:FWLabel key="JSP_PC_PCACCORDEE_L_CC"/></TH>
   	<TH><ct:FWLabel key="JSP_PC_PCACCORDEE_L_MONTANT"/></TH>
   	<TH><ct:FWLabel key="JSP_PC_PCACCORDEE_L_BLOCAGE"/></TH>
   	<TH><ct:FWLabel key="JSP_PC_PCACCORDEE_L_PERIODE"/></TH>
   	<TH><ct:FWLabel key="JSP_PC_PCACCORDEE_L_ETAT"/></TH>
   	<TH><ct:FWLabel key="JSP_PC_PCACCORDEE_L_PCAL"/></TH>
   	<TH><ct:FWLabel key="JSP_PC_PCACCORDEE_L_NO_VERSION_DROIT"/></TH>  
   	<TH><ct:FWLabel key="JSP_PC_PCACCORDEE_L_NO"/></TH>  

	    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
		<%
		PCPcAccordeeViewBean line = (PCPcAccordeeViewBean) viewBean.getEntity(i);
		String idParent = "";
		if(line.isPcaChildrenFromOtherPca()){
			idParent = "&idParent="+line.getPcAccordee().getSimplePCAccordee().getIdPcaParent();
		}
		
		String detailUrl = "parent.location.href='" + detailLink + line.getId()+idParent+"'";
		String buildUrl = detailLink + line.getId();
		String csEtat = line.getPcAccordee().getSimplePCAccordee().getCsEtatPC();
		String cssToTd = "";
		//Si etat pas valide, set class css italic pour chaque td
		if(!csEtat.equals(IPCPCAccordee.CS_ETAT_PCA_VALIDE)){
			cssToTd = "italForNonValid";
		}
		%>
		
		<TD class="mtd" nowrap width="20px">
			<ct:menuPopup menu="pegasus-optionspcaccorde" detailLabelId="MENU_OPTION_PCACCORDES_DETAIL" detailLink="<%=buildUrl%>">
				<ct:menuParam key="idVersionDroit" value="<%=line.getVersionDroit().getIdVersionDroit()%>"/>
		 		<ct:menuParam key="idDroit" value="<%=line.getIdDroit()%>"/>
				<ct:menuParam key="idDemandePc" value="<%= line.getIdDemandePc() %>"/>
				<ct:menuParam key="noVersion" value="<%= line.getVersionDroit().getNoVersion() %>"/>
				
				<% if(!line.isPcaChildrenFromOtherPca()) {%>
	     			<ct:menuExcludeNode nodeId="pcParent"/>
	     			<ct:menuParam key="selectedId" value="<%= line.getId() %>"/>
	     			
	     		<% }else{%>
	     			<ct:menuParam key="selectedId" value="<%= line.getPcAccordee().getSimplePCAccordee().getIdPcaParent() %>"/>
	     			<ct:menuParam key="idChild" value="<%=line.getId() %>"/>
	     		<%} %>
				<% if(!line.getPcAccordee().getSimplePlanDeCalcul().getEtatPC().equals(IPCValeursPlanCalcul.STATUS_OCTROI)) {%>
	     		<ct:menuExcludeNode nodeId="pcRetenues"/>
	     		<% } %>
	     		<ct:menuExcludeNode nodeId="pcEnfant"/>
			</ct:menuPopup>
		</TD>		
	<%if (!JadeStringUtil.isBlankOrZero(line.getPcAccordee().getIdCompteAnnexe()) && hasOsirisReadAccess && 
			line.getPcAccordee().getSimplePCAccordee().getCsEtatPC().equalsIgnoreCase(IPCPCAccordee.CS_ETAT_PCA_VALIDE) && 
			line.getPcAccordee().isCourante()) {%>
		<TD  class="mtd" nowrap>
			<A href="<%=request.getContextPath()%>/osiris?userAction=osiris.comptes.odresVersement.chercher&selectedId=<%=line.getPcAccordee().getIdCompteAnnexe()%>&id=<%=line.getPcAccordee().getIdCompteAnnexe()%>&idCompteAnnexe=<%=line.getPcAccordee().getIdCompteAnnexe()%>" class="external_link" target="_parent">
				<ct:FWLabel key="JSP_PC_PCACCORDEE_L_OV_LINK"/>
			</A>
		</TD>
	<%}else{ %>
		<TD  class="mtd" nowrap>&nbsp</TD>
	<%} %>			
		<TD class="mtd <%= cssToTd %>" nowrap onClick="<%=detailUrl%>">
		
				<div style="position:relative;">
					<%=PCUserHelper.getDetailAssure(objSession,line.getPersonneBeneficiaire())%>
					<span style="position:absolute; top:0; right:0" 
					 	data-g-note="idExterne:<%=line.getPcAccordee().getSimplePCAccordee().getId()%>, 
					 				tableSource:PCPCACC, inList: true">
				</span>
			</div>
	
	
		</TD>
	
		
		<td class="mtd">
			<a href="#" onClick="openGedWindow('<%= PCGedUtils.generateAndReturnGEDUrl(line.getNoAvs(), line.getIdTiersRequerant()) %>')">
				<ct:FWLabel key="JSP_PC_GED_LINK_LABEL"/>
			</a>
		</td>
		
		<!-- si jours appoint -->
		<% if (EPCProperties.GESTION_JOURS_APPOINTS.getBooleanValue()){ %>
   			<TD class="mtd <%= cssToTd %>" nowrap onClick="<%=detailUrl%>"><% if(line.hasJoursAppoint()){%>
			<IMG src="<%=request.getContextPath()+"/images/ok.gif" %>" title="<%= objSession.getLabel("JSP_PC_D_PC_ACCORDEE_TITRE_JA") %>"/><%}else {%>&nbsp;<%}%></TD>
   		<%} %>
   		<% if (EPCProperties.ALLOCATION_NOEL.getBooleanValue()){ %>
   			<TD class="mtd <%= cssToTd %>" nowrap onClick="<%=detailUrl%>">
   			<% if(viewBean.hasAllocationNoel(line.getPcAccordee())){%>
			<IMG src="<%=request.getContextPath()+"/images/ok.gif" %>" title="<%= objSession.getLabel("JSP_PC_PCACCORDEE_L_ALOCATION_NOEL") %>"/>
			<%}else {%>&nbsp;<%}%>
			</TD>
   		<%} %>
		<TD class="mtd <%= cssToTd %>" nowrap onClick="<%=detailUrl%>"><% if(line.getPcAccordee().getSimplePCAccordee().getHasCalculComparatif()){%>
		<IMG src="<%=request.getContextPath()+"/images/ok.gif" %>"/><%}else {%>&nbsp;<%}%></TD>
		<TD class="mtd <%= cssToTd %>" style="text-align:right;" nowrap onClick="<%=detailUrl%>" data-g-amountformatter=" "><%= line.getPCAResultState() %></TD>
				
		<TD class="mtd <%= cssToTd %>" nowrap onClick="<%=detailUrl%>"><%=line.getRetenueBlocageLibelle()%>&nbsp;</TD>
		
		<TD class="mtd <%= cssToTd %>" nowrap onClick="<%=detailUrl%>" data-g-periodformatter=" "><%= line.getDateDebut() %> - <%= line.getDateFin() %></TD>		
		<TD class="mtd <%= cssToTd %>" nowrap onClick="<%=detailUrl%>"><%= objSession.getCodeLibelle(csEtat) %><%= line.dealInfoPcaCopieFromPreviousVersion(i) %></TD>
		<!--  si ne provient pas de la reprise de donnée, on affiche le plan de calcul -->
		<%if(!line.getIsPlanCAlculAccessible()) {
		%>
			<TD class="mtd <%= cssToTd %>" nowrap ><img src="<%= servletContext%>/images/calcule_nondispo.png"/></TD>
		<% 	
		}else{
		%>
			<TD class="mtd <%= cssToTd %>" nowrap ><img src="<%= servletContext%>/images/calcule.png" class="btnDisplayPCAL" id="<%= line.getIdPlanCalculToDisplay() %>"/></TD>
		<%	
		}%>
		
		<TD class="mtd <%= cssToTd %>" nowrap onClick="<%=detailUrl%>"><%= line.getPcAccordee().getSimpleVersionDroit().getNoVersion() %></TD>
		
		<TD class="mtd <%= cssToTd %>" nowrap onClick="<%=detailUrl%>"><%= line.getPcAccordee().getSimplePCAccordee().getId() %></TD>
		<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>
	