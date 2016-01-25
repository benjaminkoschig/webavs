 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2 //EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<%
detailLink = "hermes?userAction=hermes.gestion.configurationService.afficher&selectedId=";
HEConfigurationServiceListViewBean viewBean = (HEConfigurationServiceListViewBean)request.getAttribute("viewBean");
size = viewBean.size();
//menuName = "lot-detail";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 
    
<%@page import="globaz.hermes.db.gestion.HEConfigurationServiceListViewBean"%>
<%@page import="globaz.hermes.db.gestion.HEConfigurationServiceViewBean"%><Th width="16">&nbsp;</Th>
    <th>Nom du Service</th>
    <th>Référence Interne</th>
    <th>Email</th>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%> 
    <%
		HEConfigurationServiceViewBean line = (HEConfigurationServiceViewBean)viewBean.getEntity(i);
		//actionDetail = targetLocation  + "='" + detailLink + line.getIdLot() + "&nbAnnonces="+line.getNbAnnonces()+"'";
		actionDetail = targetLocation  + "='" + "hermes?userAction=hermes.gestion.configurationService.afficher&selectedId=" + line.getIdService() + "&serviceName="+line.getServiceName()+"&referenceInterne="+line.getReferenceInterne()+"&email="+line.getEmailAdresse()+"'";
		//String optionString = "'"+line.getIdLot()+"&nbAnnonces="+line.getNbAnnonces()+"&="+line.isArchivage();
	%>
    <TD class="mtd" width="">
    	<ct:menuPopup menu="HE-ConfigurationService" label="<%=optionsPopupLabel%>" target="top.fr_main">
    		<ct:menuParam key="selectedId" value="<%=line.getIdService()%>"/>
			<ct:menuParam key="selectedServiceName" value="<%=line.getServiceName()%>"/>
			<ct:menuParam key="selectedReferenceInterne" value="<%=line.getReferenceInterne()%>"/>
			<ct:menuParam key="selectedServiceEmail" value="<%=line.getEmailAdresse()%>"/>
     	</ct:menuPopup>   
    </TD>
    <td class="mtd" onClick="<%=actionDetail%>"><%= (line.getServiceName().equals(""))?"&nbsp;":line.getServiceName() %></td>
    <td class="mtd" onClick="<%=actionDetail%>"><%= (line.getReferenceInterne().equals(""))?"&nbsp;":line.getReferenceInterne() %></td>
    <td class="mtd" onClick="<%=actionDetail%>"><%= (line.getEmailAdresse().equals(""))?"&nbsp;":line.getEmailAdresse() %></td>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>