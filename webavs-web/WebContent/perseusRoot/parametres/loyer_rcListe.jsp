<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>

<%@page import="ch.globaz.perseus.business.models.parametres.Loyer" %>
<%@page import="globaz.perseus.vb.parametres.PFLoyerViewBean" %>
<%@page import="globaz.perseus.vb.parametres.PFLoyerListViewBean" %>

<%
//Les labels de cette page commence par le préfix "JSP_PF_PARAM_LOYER"

	PFLoyerListViewBean viewBean= (PFLoyerListViewBean) request.getAttribute ("viewBean");
	size = viewBean.getSize();

	menuName = "perseus-menuprincipal";
	detailLink = baseLink + "afficher&selectedId=";
	
%>
<%-- tpl:insert attribute="zoneScripts" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	<%-- tpl:insert attribute="zoneHeaders" --%>

	<th>&nbsp;</th>
    <th ><ct:FWLabel key="JSP_PF_PARAM_LOYER_DATE"/>&nbsp;</th>
    <th ><ct:FWLabel key="JSP_PF_PARAM_LOYER_ID_ZONE"/>&nbsp;</th>
    <th ><ct:FWLabel key="JSP_PF_PARAM_LOYER_TYPE"/>&nbsp;</th>
    <th data-g-amountformatter=" " ><ct:FWLabel key="JSP_PF_PARAM_LOYER_LIB_MONTANT"/>&nbsp;</th>    
	<%-- /tpl:insert --%> 
	    
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:insert attribute="zoneCondition" --%>
    <%
    	PFLoyerViewBean line = (PFLoyerViewBean)viewBean.getEntity(i);
    	Loyer loyer = line.getLoyer();
    	String detailUrl = "parent.fr_detail.location.href='" + detailLink + loyer.getId() +"'";
    %>
    <%-- /tpl:insert --%>
    
    
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:insert attribute="zoneList" --%>
	
 		<td width="20px">
	     	<ct:menuPopup menu="perseus-optionsLoyer" detailLink="<%=baseLink + line.getId()%>">
     			<ct:menuParam key="idZone" value="<%=line.getLoyer().getSimpleZone().getIdZone()%>"/>  
		 	</ct:menuPopup>
     	</td>
		
		<td date-g-periodeform="" onclick="<%=detailUrl%>">
			<%=loyer.getSimpleLoyer().getDateDebut()+"-"+ loyer.getSimpleLoyer().getDateFin()%>
		</td>
		
		<td onclick="<%=detailUrl%>">
			<%=line.getLoyer().getSimpleZone().getDesignation()%>
		</td>
		
	 	<td onclick="<%=detailUrl%>">
			<%=objSession.getCodeLibelle(line.getLoyer().getSimpleLoyer().getCsTypeLoyer())%>
		</td>
		
	 	<td onclick="<%=detailUrl%>">
			<%=line.getLoyer().getSimpleLoyer().getMontant()%>
		</td>
		
		
		<%-- /tpl:insert --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:insert attribute="zoneTableFooter" --%>
	<%-- /tpl:insert --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	