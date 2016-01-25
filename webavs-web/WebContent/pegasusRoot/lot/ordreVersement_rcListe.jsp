<%-- tpl:insert page="/theme/list.jtpl" --%>
<%@page import="globaz.corvus.api.ordresversements.IREOrdresVersements"%>
<%@ page language="java" %>
<%@page import="globaz.pegasus.vb.lot.PCOrdreVersementViewBean"%>
<%@page import="globaz.pegasus.vb.lot.PCOrdreVersementListViewBean"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%
	// Les labels de cette page commence par la préfix "JSP_PC_LOT_ORDRE_VERSEMENT "

	PCOrdreVersementListViewBean viewBean = (PCOrdreVersementListViewBean) request.getAttribute("viewBean");
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
    <th><ct:FWLabel key="JSP_PC_LOT_ORDRE_VERSEMENT_L_DESIGNATION"/></th>
    <th data-g-amountformatter=" "><ct:FWLabel key="JSP_PC_LOT_ORDRE_VERSEMENT_L_MOTANT"/></th>
    <th><ct:FWLabel key="JSP_PC_LOT_ORDRE_VERSEMENT_L_TYPE"/></th>
    <th><ct:FWLabel key="JSP_PC_LOT_ORDRE_VERSEMENT_L_NO"/></th>
    <th><ct:FWLabel key="JSP_PC_LOT_ORDRE_VERSEMENT_L_TYPE_PC_ACCORDE"/></th>
	    <%-- /tpl:insert --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:insert attribute="zoneCondition" --%>
    <%
		PCOrdreVersementViewBean line = (PCOrdreVersementViewBean) viewBean.getEntity(i);
		actionDetail =  "parent.fr_detail.location.href='" + detailLink + line.getOrdreVersement().getSimpleOrdreVersement().getId()+"'";
	%>

    <%-- /tpl:insert --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:insert attribute="zoneList" --%>
		<%
		     String minus=null;
			if(IREOrdresVersements.CS_TYPE_DETTE.equals(line.getOrdreVersement().getSimpleOrdreVersement().getCsType())){
				 minus = "data-p-cellsum='vertical:minus'"; 
			}else {
				 minus = "";
			}
		%>
	<TD onClick="<%=actionDetail%>"><%=line.getBeneficiaire()%>&nbsp;</TD>
	<TD <%=minus%> onClick="<%=actionDetail%>"><%=line.getOrdreVersement().getSimpleOrdreVersement().getMontant()%>&nbsp;</TD>
	<TD onClick="<%=actionDetail%>"><%=objSession.getCodeLibelle(line.getOrdreVersement().getSimpleOrdreVersement().getCsType())%>&nbsp;</TD>
	<TD onClick="<%=actionDetail%>"><%=line.getOrdreVersement().getSimpleOrdreVersement().getIdOrdreVersement()%>&nbsp;</TD>
	<TD onClick="<%=actionDetail%>"><%=objSession.getCodeLibelle(line.getOrdreVersement().getSimpleOrdreVersement().getCsTypePcAccordee())%>&nbsp;</TD>		
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
	
	
	
	



	