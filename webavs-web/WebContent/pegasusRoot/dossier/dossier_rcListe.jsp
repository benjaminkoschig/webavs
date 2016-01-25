<%@page import="ch.globaz.pegasus.businessimpl.utils.PCGedUtils"%>
<%@page import="globaz.pegasus.vb.dossier.PCDossierListViewBean"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/theme/list/header.jspf" %>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions" %>
<%@ page isELIgnored ="false" %>
	    
<script type="text/javascript">
	
	// Ouverture de la GED dans un nouvel onglet
	var openGedWindow = function (url){
		window.open(url);
	};
	
</script>
	    <%
	    	PCDossierListViewBean viewBean=(PCDossierListViewBean)request.getAttribute("viewBean");
	    	size=viewBean.getSize();
	    	
	    	detailLink = "pegasus?userAction="+IPCActions.ACTION_DOSSIER+".afficher&selectedId=";
	    	
	    	menuName = "pegasus-menuprincipal";	    	
	    	
	    %>

<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:insert attribute="zoneHeaders" --%>
	    
<%@page import="globaz.pegasus.vb.dossier.PCDossierViewBean"%>

<th colspan="2"><ct:FWLabel key="JSP_PC_DOS_L_DOSSIER"/></th>
   		<th colspan=2>
   			<ct:FWLabel key="JSP_PC_DOS_L_DETAIL_ASSURE"/>
   		</th>
   		
   		<th><ct:FWLabel key="JSP_PC_DOS_L_GEST"/></th>
   		<th><ct:FWLabel key="JSP_PC_DOS_L_NO"/></th>   	
	    <%-- /tpl:insert --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:insert attribute="zoneCondition" --%>
    <%
    	PCDossierViewBean line = (PCDossierViewBean)viewBean.getEntity(i); 
    
    	String detailUrl = "parent.location.href='" + detailLink + line.getId()+"'";
    
    %>
    <%-- /tpl:insert --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:insert attribute="zoneList" --%>
		
		<td class="mtd" nowrap width="20px">
	     	<ct:menuPopup menu="pegasus-optionsdossiers" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=detailLink + line.getId()%>">
	     			<ct:menuParam key="idDossier" value="<%=line.getId()%>"/>  
		 	</ct:menuPopup>
     	</td>
     	
		<td class="mtd" width="20px" nowrap onClick="<%=actionDetail%>">
		<img src="<%=request.getContextPath()+"/images/"+line.getImageEtatDemande()%>" title="<%=line.getInfobulleEtatDemande()%>">
		</td>
		
		<td class="mtd" nowrap onClick="<%=detailUrl%>">
			<div style="position:relative;" >
				<div><%=line.getDetailAssure()%></div>
				<span style="position:absolute; top:0; right:0" data-g-note="idExterne:<%=line.getId()%>, tableSource:PCDOSSI, inList: true">
				</span>
			</div>
		</td>
		
		<td class="mtd">
			<a href="#" onClick="openGedWindow('<%= PCGedUtils.generateAndReturnGEDUrl(line.getNoAvs(), line.getIdTiersRequerant()) %>')">
				<ct:FWLabel key="JSP_PC_GED_LINK_LABEL"/>
			</a>
		</td>
		
		
		<td class="mtd" nowrap onClick="<%=detailUrl%>">
			<%=line.getDetailGestionnaire()%>
		</td>
		<td class="mtd" nowrap onClick="<%=detailUrl%>">
			<%=line.getId()%>
		</td>		
		
		
		<%-- /tpl:insert --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:insert attribute="zoneTableFooter" --%>
	<%-- /tpl:insert --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	