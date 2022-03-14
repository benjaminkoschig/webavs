<%@ page import="globaz.eform.vb.formulaire.GFFormulaireListViewBean" %>
<%@ page import="globaz.eform.vb.formulaire.GFFormulaireViewBean" %>
<%@ page import="globaz.commons.nss.NSUtil" %>
<%@ page import="ch.globaz.eform.constant.GFTypeEForm" %>
<%@ page errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>

<%
    GFFormulaireListViewBean viewBean = (GFFormulaireListViewBean) request.getAttribute("viewBean");
    size = viewBean.getSize();
    detailLink = baseLink + "afficher&selectedId=";
%>
<%-- tpl:insert attribute="zoneScripts" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/list/javascripts.jspf" %>

<%-- tpl:insert attribute="zoneHeaders" --%>
<%-- /tpl:insert --%>

<tr>
    <TH></TH>
    <TH><ct:FWLabel key="JSP_EFORM_FORMULAIRE_TYPE"/></TH>
    <TH><ct:FWLabel key="JSP_EFORM_FORMULAIRE_NOM_FORMULAIRE"/></TH>
    <TH><ct:FWLabel key="JSP_EFORM_FORMULAIRE_DATE"/></TH>
    <TH><ct:FWLabel key="JSP_EFORM_FORMULAIRE_ID"/></TH>
    <TH><ct:FWLabel key="JSP_EFORM_FORMULAIRE_NSS"/></TH>
    <TH><ct:FWLabel key="JSP_EFORM_FORMULAIRE_NOM"/></TH>
    <TH><ct:FWLabel key="JSP_EFORM_FORMULAIRE_PRENOM"/></TH>
    <TH><ct:FWLabel key="JSP_EFORM_FORMULAIRE_STATUT"/></TH>
    <TH><ct:FWLabel key="JSP_EFORM_FORMULAIRE_GESTIONNAIRE"/></TH>
</tr>
<%@ include file="/theme/list/tableHeader.jspf" %>

<%-- tpl:insert attribute="zoneCondition" --%>
<% GFFormulaireViewBean line = (GFFormulaireViewBean)viewBean.getEntity(i);
    String detailUrl = "parent.fr_detail.location.href='" + detailLink + line.getId() + "'";
%>
<%-- /tpl:insert --%>

<%@ include file="/theme/list/lineStyle.jspf" %>
<%-- tpl:insert attribute="zoneList" --%>
<TD class="mtd dec<%=line.getFormulaire().getId()%>" nowrap width="20px">
    <ct:menuPopup menu="eform-optionsformulaires" target="top.fr_main">
        <ct:menuParam key="selectedId" value="<%=line.getFormulaire().getId()%>" />
    </ct:menuPopup>

</TD>
<TD class="mtd dec<%=line.getFormulaire().getSubject() %>" nowrap onclick="<%=detailUrl%>" data-g-periodformatter=" "><%= line.getFormulaire().getSubject() %> </TD>
<TD class="mtd dec<%=line.getSubject() %>" nowrap onclick="<%=detailUrl%>" data-g-periodformatter=" "><%= line.getSubject()%> </TD>
<TD class="mtd dec<%=line.getFormulaire().getDate() %>" nowrap onclick="<%=detailUrl%>" data-g-periodformatter=" "><%= line.getFormulaire().getDate() %> </TD>
<TD class="mtd dec<%=line.getFormulaire().getMessageId() %>" nowrap onclick="<%=detailUrl%>" data-g-periodformatter=" "><%= line.getFormulaire().getMessageId() %> </TD>
<TD class="mtd dec<%=line.getFormulaire().getBeneficiaireNss() %>" nowrap onclick="<%=detailUrl%>" data-g-periodformatter=" "><%= NSUtil.formatAVSUnknown(line.getFormulaire().getBeneficiaireNss()) %> </TD>
<TD class="mtd dec<%=line.getFormulaire().getBeneficiaireNom() %>" nowrap onclick="<%=detailUrl%>" data-g-periodformatter=" "><%= line.getFormulaire().getBeneficiaireNom() %> </TD>
<TD class="mtd dec<%=line.getFormulaire().getBeneficiairePrenom() %>" nowrap onclick="<%=detailUrl%>" data-g-periodformatter=" "><%= line.getFormulaire().getBeneficiairePrenom()%> </TD>
<TD class="mtd dec<%=line.getFormulaire().getStatus() %>" nowrap onclick="<%=detailUrl%>" data-g-periodformatter=" "><%= line.getFormulaire().getStatus() %> </TD>
<TD class="mtd dec<%=line.getFormulaire().getUserGestionnaire() %>" nowrap onclick="<%=detailUrl%>" data-g-periodformatter=" "><%= line.getFormulaire().getUserGestionnaire() %> </TD>
<%-- /tpl:insert --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:insert attribute="zoneTableFooter" --%>
<%-- /tpl:insert --%>

<%@ include file="/theme/list/tableEnd.jspf" %>
