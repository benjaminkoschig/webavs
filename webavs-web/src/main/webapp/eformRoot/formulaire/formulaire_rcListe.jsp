<%@ page import="globaz.eform.vb.formulaire.GFFormulaireListViewBean" %>
<%@ page import="globaz.eform.vb.formulaire.GFFormulaireViewBean" %>
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
<TD class="mtd dec<%=line.getId().getClass()%> </TD>
<%-- /tpl:insert --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:insert attribute="zoneTableFooter" --%>
<%-- /tpl:insert --%>

<%@ include file="/theme/list/tableEnd.jspf" %>
