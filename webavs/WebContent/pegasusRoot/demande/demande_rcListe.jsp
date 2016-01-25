<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java"  %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%
	//Les labels de cette page commence par le préfix "JSP_PC_DEM_L"
	PCDemandeListViewBean viewBean = (PCDemandeListViewBean) request.getAttribute("viewBean");
	size = viewBean.getSize();
	detailLink = "pegasus?userAction="+IPCActions.ACTION_DEMANDE_DETAIL+ ".afficher&selectedId=";
	menuName = "pegasus-menuprincipal";
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
<%@page import="globaz.pegasus.vb.demande.PCDemandeListViewBean"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDecision"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDemandes"%>
<%@page import="globaz.pegasus.vb.demande.PCDemandeViewBean"%>
<%@page import="globaz.prestation.interfaces.fx.PRGestionnaireHelper"%>
<%@page import="globaz.pegasus.utils.PCUserHelper"%><TH>&nbsp;</TH>
   	<TH><ct:FWLabel key="JSP_PC_DEM_L_DETAIL_ASSURE"/></TH>
   	<TH><ct:FWLabel key="JSP_PC_DEM_L_DATE_DEPOT"/></TH>
   	<TH><ct:FWLabel key="JSP_PC_DEM_L_DATE_ARRIVEE_CC"/></TH>
   	<TH data-g-periodformatter=" "><ct:FWLabel key="JSP_PC_DEM_L_PERIODE"/></TH>
   	<TH><ct:FWLabel key="JSP_PC_DEM_L_ETAT"/></TH>
   	<TH><ct:FWLabel key="JSP_PC_DEM_L_GEST"/></TH>
   	<TH><ct:FWLabel key="JSP_PC_DEM_L_NO"/></TH> 
	    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
		<%
			PCDemandeViewBean line = (PCDemandeViewBean) viewBean.getEntity(i);
			
			String detailUrl = "parent.location.href='" + detailLink + line.getId()+"&idDossier="+line.getListDemandes().getDossier().getId()+"'";
	
		%>
		
		<TD class="mtd" nowrap width="20px">
	     	<ct:menuPopup menu="pegasus-optionsdemandes" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=detailLink + line.getId()%>">
     			<ct:menuParam key="idDemandePc" value="<%=line.getListDemandes().getSimpleDemande().getId()%>"/> 
     			
     			<ct:menuParam key="idTiers" value="<%=line.getListDemandes().getDossier().getDemandePrestation().getDemandePrestation().getIdTiers()%>"/>
     			<ct:menuParam key="idDossier" value="<%=line.getListDemandes().getDossier().getId()%>"/>
		 		<ct:menuParam key="isFratrie" value="<%=line.getIsFratrie() %>"/>
		 		<ct:menuParam key="idGestionnaire" value="<%=line.getIdGestionnaire() %>"/>
		 		<ct:menuParam key="idDecision" value="<%= line.getIdDecision()%>"/>
		 		<ct:menuParam key="csTypeDecisionPrep" value="<%= IPCDecision.CS_TYPE_REFUS_SC %>"/>
		 		<!--  Si la demande est deja octroyee ou si deja une decision de refus,il n'est plus possibe de preparer une decision -->
		 		<% if(!line.isValidForPrepDecisionRefus()){%>
		 		<ct:menuExcludeNode nodeId="DECREFUS"/>
		 		<%}%>
		 		<!-- Si pas d'id decision de refus, pas de lien direct sur la decision de refus -->
		 		<% if(!line.hasDecisionDerefus()){%>
		 		<ct:menuExcludeNode nodeId="DECREFUS_DE"/>
		 		<%}%>
		 		<!-- Pas de synchoniser famille pour les fratries -->
		 		<% if(line.getDemande().getSimpleDemande().getIsFratrie()){%>
		 		<ct:menuExcludeNode nodeId="SYNCHRO_FAMILLE"/>
		 		<%}%>
		 		<!-- Si le transfert de dossier n'est pas possible, on cache l'option -->
		 		<% if(!line.isTransferable()){%>
		 		<ct:menuExcludeNode nodeId="DOSSIERS_TRANSFERT"/>
		 		<%}%>
		 	</ct:menuPopup>
     	</TD>		
		<TD class="mtd" nowrap onClick="<%=detailUrl%>">
			<div style="position:relative;">
				<%=PCUserHelper.getDetailAssure(objSession,line.getListDemandes().getDossier().getDemandePrestation().getPersonneEtendue())%>
				<span style="position:absolute; top:0; right:0" 
					 	data-g-note="idExterne:<%=line.getListDemandes().getSimpleDemande().getId()%>, 
					 				tableSource:PCDEMPC, inList: true">
				</span>
			</div>
		</TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=line.getListDemandes().getSimpleDemande().getDateDepot()%></TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=line.getListDemandes().getSimpleDemande().getDateArrivee()%></TD>
		<TD class="" nowrap onClick="<%=detailUrl%>"><%=line.getPeriode()%></TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=objSession.getCodeLibelle(line.getListDemandes().getSimpleDemande().getCsEtatDemande())%></TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=line.getDetailGestionnaire()%></TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=line.getId()%></TD>
		<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>