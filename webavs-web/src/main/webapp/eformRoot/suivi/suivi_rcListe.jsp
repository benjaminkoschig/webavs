<%@ page import="globaz.commons.nss.NSUtil" %>
<%@ page import="globaz.jade.admin.user.bean.JadeUser" %>
<%@ page import="globaz.eform.vb.suivi.GFSuiviListViewBean" %>
<%@ page import="globaz.eform.vb.suivi.GFSuiviViewBean" %>
<%@ page import="ch.globaz.common.util.NSSUtils" %>
<%@ page import="ch.globaz.eform.utils.GFUtils" %>
<%@ page import="ch.globaz.eform.constant.GFTypeDADossier" %>
<%@ page import="ch.globaz.eform.constant.GFStatusDADossier" %>
<%@ page import="globaz.eform.helpers.formulaire.GFFormulaireHelper" %>
<%@ page import="globaz.pyxis.db.tiers.TITiersViewBean" %>
<%@ page import="ch.globaz.pyxis.business.model.AdministrationComplexModel" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%@ page errorPage="/errorPage.jsp" %>

<%
    GFSuiviListViewBean viewBean = (GFSuiviListViewBean) request.getAttribute("viewBean");
    JadeUser currentUser = objSession.getUserInfo();
    size = viewBean.getSize();
%>
<%-- tpl:insert attribute="zoneScripts" --%>
<%@ include file="/theme/list/javascripts.jspf" %>
<%-- /tpl:insert --%>

<%-- tpl:insert attribute="zoneHeaders" --%>
<%-- /tpl:insert --%>

<tr>
    <TH></TH>
    <TH><ct:FWLabel key="JSP_DA_DETAIL_ASSURE"/></TH>
    <TH><ct:FWLabel key="JSP_DA_DETAIL_CAISSE"/></TH>
    <TH><ct:FWLabel key="JSP_DA_TYPE_TRAITEMENT"/></TH>
    <TH><ct:FWLabel key="JSP_DA_STATUT_TRAITEMENT"/></TH>
    <TH><ct:FWLabel key="JSP_DA_DATE_DERNIER_TRAITEMENT"/></TH>
    <TH><ct:FWLabel key="JSP_DA_GESTIONNAIRE"/></TH>
    <TH><ct:FWLabel key="JSP_DA_GED"/></TH>
</tr>
<%@ include file="/theme/list/tableHeader.jspf" %>

<%-- tpl:insert attribute="zoneCondition" --%>
<%
    GFSuiviViewBean line = (GFSuiviViewBean)viewBean.getEntity(i);
    TITiersViewBean tiers = GFUtils.getTiers(line.getDaDossier().getNssAffilier(), objSession);
    AdministrationComplexModel caisse = GFUtils.getCaisse(line.getDaDossier().getCodeCaisse());
%>
<%-- /tpl:insert --%>

<%@ include file="/theme/list/lineStyle.jspf" %>
<%-- tpl:insert attribute="zoneList" --%>
<TD class="mtd" nowrap width="20px">
    <% if(GFTypeDADossier.SOLICITATION.getCodeSystem().equals(line.getDaDossier().getType())){ %>
        <ct:menuPopup menu="dadossier-optionssuivi" target="top.fr_main">
            <ct:menuParam key="selectedId" value="<%=line.getDaDossier().getId()%>" />
        </ct:menuPopup>
    <% } %>
</TD>
<TD class="mtd" nowrap>
    <div style="font-weight: bold;font-size: 12px"><%=NSSUtils.formatNss(line.getDaDossier().getNssAffilier())%></div>
    <div style="font-size: 10px"><%=GFUtils.formatTiers(tiers)%></div>
</TD>
<TD class="mtd>" nowrap><%=caisse.getTiers().getDesignation1() + " " + caisse.getTiers().getDesignation2()%></TD>
<TD class="mtd" nowrap><%=GFTypeDADossier.getByCodeSystem(line.getDaDossier().getType()).getDesignation(objSession)%></TD>
<TD class="mtd" nowrap><%=GFStatusDADossier.getByCodeSystem(line.getDaDossier().getStatus()).getDesignation(objSession)%></TD>
<TD class="mtd" nowrap><%=GFUtils.formatSpy(line.getDaDossier().getSpy()).getDate()%></TD>
<TD class="mtd" nowrap><%=line.getDaDossier().getUserGestionnaire()%></TD>
<TD class="mtd" nowrap>
    <%
        String urlGED = baseLink + "ged"
                + "&noAVSId=" + NSSUtils.formatNss(line.getDaDossier().getNssAffilier())
                + "&idTiersExtraFolder=" + tiers.getIdTiers();
    %>
    <a href="#" onclick="window.open('<%=urlGED%>','GED_CONSULT')">
        <ct:FWLabel key="JSP_LIEN_GED"/>
    </a>
</TD>
<%-- /tpl:insert --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:insert attribute="zoneTableFooter" --%>
<%-- /tpl:insert --%>

<%@ include file="/theme/list/tableEnd.jspf" %>
