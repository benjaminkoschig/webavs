<%@ page import="globaz.commons.nss.NSUtil" %>
<%@ page import="globaz.jade.admin.user.bean.JadeUser" %>
<%@ page import="globaz.eform.vb.suivi.GFSuiviListViewBean" %>
<%@ page import="globaz.eform.vb.suivi.GFSuiviViewBean" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%@ page errorPage="/errorPage.jsp" %>

<%
    GFSuiviListViewBean viewBean = (GFSuiviListViewBean) request.getAttribute("viewBean");
    JadeUser currentUser = objSession.getUserInfo();
%>
<%-- tpl:insert attribute="zoneScripts" --%>
<%@ include file="/theme/list/javascripts.jspf" %>
<%-- /tpl:insert --%>

<%-- tpl:insert attribute="zoneHeaders" --%>
<%-- /tpl:insert --%>

<tr>
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
%>
<%-- /tpl:insert --%>

<%@ include file="/theme/list/lineStyle.jspf" %>
<%-- tpl:insert attribute="zoneList" --%>

<%-- /tpl:insert --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:insert attribute="zoneTableFooter" --%>
<%-- /tpl:insert --%>

<%@ include file="/theme/list/tableEnd.jspf" %>
