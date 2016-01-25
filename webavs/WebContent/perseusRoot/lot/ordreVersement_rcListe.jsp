<%-- tpl:insert page="/theme/list.jtpl" --%>
<%@ page language="java" %>
<%@page import="globaz.perseus.vb.lot.PFOrdreVersementViewBean"%>
<%@page import="globaz.perseus.vb.lot.PFOrdreVersementListViewBean"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%
	// Les labels de cette page commence par la préfix "JSP_PF_LOT_ORDRE_VERSEMENT "
	
	PFOrdreVersementListViewBean viewBean = (PFOrdreVersementListViewBean) request.getAttribute("viewBean");
	size = viewBean.getSize();
	detailLink = baseLink+"afficher&selectedId=";
	
	String idTierRequerant 		= request.getParameter("idTierRequerant");
	String idPrestation 		= request.getParameter("idPrestation");
	String montantPrestation	= request.getParameter("montantPrestation");
	String idDecision			= request.getParameter("idDecision");
	String decalage ="";
%>
<%-- tpl:insert attribute="zoneScripts" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	 <%-- tpl:insert attribute="zoneHeaders" --%>
    <th><ct:FWLabel key="JSP_PF_LOT_ORDRE_VERSEMENT_L_DESIGNATION"/></th>
    <th data-g-amountformatter=" "><ct:FWLabel key="JSP_PF_LOT_ORDRE_VERSEMENT_L_MOTANT"/></th>
    <th><ct:FWLabel key="JSP_PF_LOT_ORDRE_VERSEMENT_L_TYPE"/></th>
    <th><ct:FWLabel key="JSP_PF_LOT_ORDRE_VERSEMENT_L_NO"/></th>
	    <%-- /tpl:insert --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:insert attribute="zoneCondition" --%>
    <%
    	PFOrdreVersementViewBean line = (PFOrdreVersementViewBean) viewBean.getEntity(i);
		actionDetail =  "parent.fr_detail.location.href='" + detailLink + line.getOrdreVersement().getSimpleOrdreVersement().getId()+"'";
	%>

    <%-- /tpl:insert --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:insert attribute="zoneList" --%>
	<TD onClick="<%=actionDetail%>"><%=line.getBeneficiaire()%>&nbsp;</TD>
	<TD onClick="<%=actionDetail%>"><%=line.getOrdreVersement().getSimpleOrdreVersement().getMontantVersement()%>&nbsp;</TD>
	<TD onClick="<%=actionDetail%>"><%=objSession.getCodeLibelle(line.getOrdreVersement().getSimpleOrdreVersement().getCsTypeVersement())%>&nbsp;</TD>
	<TD onClick="<%=actionDetail%>"><%=line.getOrdreVersement().getSimpleOrdreVersement().getIdOrdreVersement()%>&nbsp;</TD>	
	<%decalage = "<span>&nbsp;&nbsp;</span>";%>
	<%-- /tpl:insert --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<tr>
    <td style="font-weight: bold;"><ct:FWLabel key="JSP_LOT_ORDRE_VERSEMENT_L_TOTAL"/></td>
    <td style="border-top:double 3px black;font-weight: bold;" data-g-cellsum=" " data-g-amountformatter=" ">
    </td><td></td><td></td>
 </tr>
	<%-- tpl:insert attribute="zoneTableFooter" --%>
	<%-- /tpl:insert --%>
<%@ include file="/theme/list/tableEnd.jspf" %> 
	
	
	
	



	