<%@page import="globaz.pegasus.vb.parametre.PCZoneForfaitsListViewBean" %>
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%@page import="globaz.pegasus.vb.parametre.PCForfaitPrimesAssuranceMaladieViewBean" %>
<%@page import="globaz.pegasus.vb.parametre.PCForfaitPrimesAssuranceMaladieListViewBean" %>
<%@page import="ch.globaz.pegasus.business.models.parametre.SimpleForfaitPrimesAssuranceMaladie" %>
<%
    // Les labels de cette page commence par la préfix "JSP_PC_PARAM_FORFAIT_PRIME_AM_L"

    PCForfaitPrimesAssuranceMaladieListViewBean viewBean = (PCForfaitPrimesAssuranceMaladieListViewBean) request.getAttribute("viewBean");
    size = viewBean.getSize();

    menuName = "pegasus-menuprincipal";
    detailLink = baseLink + "afficher&selectedId=";
%>
<%-- tpl:insert attribute="zoneScripts" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/list/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneHeaders" --%>
<th>&nbsp;</th>
<th><ct:FWLabel key="JSP_PC_PARAM_FORFAIT_PRIME_AM_L_PERIODE"/></th>
<th><ct:FWLabel key="JSP_PC_PARAM_FORFAIT_PRIME_AM_L_ZONE"/></th>
<th><ct:FWLabel key="JSP_PC_PARAM_FORFAIT_PRIME_AM_L_TYPE_DE_PRIME"/></th>
<th data-g-amountformatter=" "><ct:FWLabel key="JSP_PC_PARAM_FORFAIT_PRIME_AM_L_MONTANT_REDUCTION_MAX"/></th>
<th data-g-amountformatter=" "><ct:FWLabel key="JSP_PC_PARAM_FORFAIT_PRIME_AM_L_MONTANT_PRIME_MOY"/></th>
<%-- /tpl:insert --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
<%-- tpl:insert attribute="zoneCondition" --%>
<% PCForfaitPrimesAssuranceMaladieViewBean line = (PCForfaitPrimesAssuranceMaladieViewBean) viewBean.getEntity(i);
    SimpleForfaitPrimesAssuranceMaladie fpam = line.getForfaitPrimesAssuranceMaladie().getSimpleForfaitPrimesAssuranceMaladie();
    String detailUrl = "parent.fr_detail.location.href='" + detailLink + fpam.getId() + "'";
%>
<%-- /tpl:insert --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
<%-- tpl:insert attribute="zoneList" --%>

<td width="20px">
    <ct:menuPopup menu="pegasus-forfaitPrimesAssuranceMaladie" detailLink="<%=baseLink + line.getId()%>">
        <ct:menuParam key="zoneLocalite.simpleLienZoneLocalite.idZoneForfait"
                      value="<%=line.getForfaitPrimesAssuranceMaladie().getSimpleZoneForfaits().getIdZoneForfait()%>"/>
        <ct:menuParam key="zoneLocalite.simpleLienZoneLocalite.anneeDebut" value="<%=fpam.getDateDebut()%>"/>
    </ct:menuPopup>
</td>
<td date-g-periodeformatter=" " onClick="<%=detailUrl%>"><%=fpam.getDateDebut() + "-" + fpam.getDateFin()%>
</td>
<td onClick="<%=detailUrl%>"><%=line.getDescZone(objSession)%>
</td>
<td onClick="<%=detailUrl%>"><%=objSession.getCodeLibelle(fpam.getCsTypePrime())%>
</td>
<td onClick="<%=detailUrl%>"><%=fpam.getMontantPrimeReductionMaxCanton()%>
</td>
<td onClick="<%=detailUrl%>"><%=fpam.getMontantPrimeMoy()%>
</td>
<%-- /tpl:insert --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:insert attribute="zoneTableFooter" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	