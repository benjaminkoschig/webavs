<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>

<%@page import="globaz.orion.vb.pucs.EBPucsFileListViewBean"%>
<%@page import="globaz.orion.utils.EBDanUtils"%>

<%
	EBPucsFileListViewBean viewBean = (EBPucsFileListViewBean) request.getAttribute("viewBean"); 
	size = viewBean.getSize();
	String changeUserAction = baseLink+"changeUser";
%>
<%-- /tpl:insert --%>

<style>

	
	.icon-ok {
	    background-position: -288px 0;
	    background-image: url("../webavs/orionRoot/img/glyphicons-halflings.png");
	    background-repeat: no-repeat;
	    display: inline-block;
	    height: 14px;
	    line-height: 14px;
	    vertical-align: text-top;
	    width: 14px;
	}
	
	.icon-check {
	  	background-position: -144px -72px;
	    background-image: url("../webavs/orionRoot/img/glyphicons-halflings.png");
	    background-repeat: no-repeat;
	    display: inline-block;
	    height: 14px;
	    line-height: 14px;
	    vertical-align: text-top;
	    width: 14px;
	}

	
	.icon-lock {
	    background-position: -287px -24px;
	    background-image: url("../webavs/orionRoot/img/glyphicons-halflings.png");
	    background-repeat: no-repeat;
	    display: inline-block;
	    height: 14px;
	    line-height: 14px;
	    vertical-align: text-top;
	    width: 14px;
	}

</style>
<%@ include file="/theme/list/javascripts.jspf" %>


 <%-- tpl:insert attribute="zoneHeaders" --%>
			
<%@page import="globaz.orion.vb.pucs.EBPucsFileViewBean"%>			
<%@page import="ch.globaz.orion.business.models.pucs.PucsSearchCriteria"%>
<%@page import="globaz.framework.util.FWCurrency"%>

<th colspan="2"><ct:FWLabel key="NUMERO_AFFILIE"/></th>
<th><ct:FWLabel key="NOM"/></th>
<th><ct:FWLabel key="DATE_RECEPTION"/></th>
<th><ct:FWLabel key="ANNEE_DECL"/></th>
<th><ct:FWLabel key="TOTAL_CONTROLE"/></th>
<th><ct:FWLabel key="NB_SALAIRE"/></th>
<th><ct:FWLabel key="USER"/></th>
<th><ct:FWLabel key="STATUT"/></th>
<th><ct:FWLabel key="PUCS_TYPE"/></th>
<th><ct:FWLabel key="AF_SEUL"/></th>
<%-- /tpl:insert --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
<%-- tpl:insert attribute="zoneCondition" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
<%-- tpl:insert attribute="zoneList" --%>

			<%EBPucsFileViewBean line = (EBPucsFileViewBean) viewBean.get(i);%>
			<td style="text-align: center;">
				<%if(!line.hasLock() ){%>
					<% if(line.getPucsFile().isStatusHandling()) {%>
						<input type="checkbox" name="idPucsEntryHandling" value='<%= EBDanUtils.createPucsDataForProcess(line.getPucsFile())%>'/>
					<%} else {%>
						<input type="checkbox" name="idPucsEntryToHandle" value='<%= EBDanUtils.createPucsDataForProcess(line.getPucsFile())%>'/>
					<%}%>
				<%} else {%>
					<span><i title="<%=line.getMessageLock() %>" class="icon-lock" ></i></span> 
				<%}%>
				
			</td>
			<td><%=line.getPucsFile().getNumeroAffilie()%></td>
			<td>
				<%if (line.getPucsFile().isForTest()){%>
				 	<i title ="<ct:FWLabel key='PUCS_TEST_FILE'/>" class="icon-check"></i>
				<% } %>
				<%=line.getPucsFile().getNomAffilie()%>
			</td>
			<td align = "center"><%=line.getPucsFile().getDateDeReception()%></td>
			<td align="center"><%=line.getPucsFile().getAnneeDeclaration()%></td>
			<td align ="right"><%=new FWCurrency(line.getPucsFile().getTotalControle()).toStringFormat()%></td>
			<td align="center"><%=line.getPucsFile().getNbSalaires()%></td>
			<td><%=line.getPucsFile().getHandlingUser()==null?"":line.getPucsFile().getHandlingUser()%></td>
			<td><%=objSession.getCodeLibelle(line.getPucsFile().getCurrentStatus())%></td>
		<% if("1".equals(line.getPucsFile().getProvenance().getValue())) { %>
			<td><ct:FWLabel key="PUCS_TYPE_PUCS"/></td>
		<% } else if("2".equals(line.getPucsFile().getProvenance().getValue())) { %>
			<td><ct:FWLabel key="PUCS_TYPE_DAN"/></td>
		<% } else {%>
			<td><ct:FWLabel key="PUCS_TYPE_SWISS_DEC"/></td>
		<% } %>
			<td align="center">
				<%if (line.getPucsFile().isAfSeul()){%>
					<i class="icon-ok"></i>
				<% } %>
			</td> 
			<td align="center">
	
			</td> 
<%-- /tpl:insert --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:insert attribute="zoneTableFooter" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	