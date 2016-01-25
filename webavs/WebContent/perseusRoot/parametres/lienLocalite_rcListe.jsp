<%@page import="globaz.perseus.vb.parametres.PFLienLocaliteListViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>

<%@page import="ch.globaz.perseus.business.models.parametres.LienLocalite" %>
<%@page import="globaz.perseus.vb.parametres.PFLienLocaliteViewBean" %>


<%//Les labels de cette page commence par le préfix "JSP_PF_PARAM_LIEN_LOCALITE"
PFLienLocaliteListViewBean viewBean= (PFLienLocaliteListViewBean) request.getAttribute ("viewBean");
size = viewBean.getSize();

menuName = "perseus-menuprincipal";
detailLink = baseLink + "afficher&selectedId=";
%>

<%-- tpl:insert attribute="zoneScripts" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:insert attribute="zoneHeaders" --%>
	    
	    
<%@page import="globaz.perseus.utils.parametres.PFParametresHandler"%>

	<th><ct:FWLabel key="JSP_PF_PARAM_LIEN_LOCALITE_ZONE"/></th>
    <th><ct:FWLabel key="JSP_PF_PARAM_LIEN_LOCALITE_DESIGN"/></th>
    <th><ct:FWLabel key="JSP_PF_PARAM_LIEN_LOCALITE_PERIODE"/></th>
	    <%-- /tpl:insert --%> 
	    
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:insert attribute="zoneCondition" --%>
    <% PFLienLocaliteViewBean line = (PFLienLocaliteViewBean)viewBean.getEntity(i); 
       String detailUrl = "parent.fr_detail.location.href='" + detailLink + line.getLienLocalite().getSimpleLienLocalite().getId() +"'";
       String periode = line.getLienLocalite().getSimpleLienLocalite().getDateDebut()+" - "+ line.getLienLocalite().getSimpleLienLocalite().getDateFin();
    %>
    <%-- /tpl:insert --%>
    
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:insert attribute="zoneList" --%>
		     	
		<td align="" nowrap onClick="<%=detailUrl%>"><%=line.getLienLocalite().getSimpleZone().getDesignation()%></td>	
		<td align="" nowrap onClick="<%=detailUrl%>"><%=line.getLienLocalite().getLocaliteSimpleModel().getNumPostal()+" "+line.getLienLocalite().getLocaliteSimpleModel().getLocalite()%></td> <!-- .getDescLocalite(line.getZoneLocalite())%></td>-->
		<td align="" nowrap onClick="<%=detailUrl%>"><%=periode%></td>
		<%-- /tpl:insert --%>
		
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:insert attribute="zoneTableFooter" --%>
	<%-- /tpl:insert --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	