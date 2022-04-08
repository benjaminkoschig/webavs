<%@ page import="globaz.eform.vb.formulaire.GFFormulaireListViewBean" %>
<%@ page import="globaz.eform.vb.formulaire.GFFormulaireViewBean" %>
<%@ page import="globaz.commons.nss.NSUtil" %>
<%@ page import="ch.globaz.eform.constant.GFStatusEForm" %>
<%@ page import="globaz.eform.helpers.formulaire.GFFormulaireHelper" %>
<%@ page import="globaz.jade.admin.user.bean.JadeUser" %>
<%@ page import="ch.globaz.eform.web.servlet.GFFormulaireServletAction" %>
<%@ page import="globaz.eform.vb.statistique.GFStatistiqueListViewBean" %>
<%@ page import="globaz.eform.vb.statistique.GFStatistiqueViewBean" %>
<%@ page import="ch.globaz.eform.business.models.GFStatistiqueModel" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%@ page errorPage="/errorPage.jsp" %>

<%
    GFStatistiqueListViewBean viewBean = (GFStatistiqueListViewBean) request.getAttribute("viewBean");
    size = viewBean.getStatistiqueListSize();
%>
<%-- tpl:insert attribute="zoneScripts" --%>
<%@ include file="/theme/list/javascripts.jspf" %>
<script type="text/javascript">
    $(function (){
        // surcharge l'action télécharger afin de fermer le menu
        overLoadActionMenu("<%=GFFormulaireServletAction.PATH_EFORM+"."+GFFormulaireServletAction.ACTION_TELECHARGER%>", /(selectedId)=(\d*)/g, function (values, element, defaultAction){
            FWOptionSelectorButton_win.hidePopup();
            defaultAction();
        });
    });
</script>
<style>
    .headRow {
        background-color : #226194;;
        border: solid 1px;
        border-color :  #88aaff #88aaff black black;
        font-size : 12;
        font-family : Verdana,Arial;
        color : white;
        padding : 3 10;
    }
</style>
<%-- /tpl:insert --%>

<%-- tpl:insert attribute="zoneHeaders" --%>
<%-- /tpl:insert --%>

<tr>
    <TH></TH>
    <%
        for (int i=1; i < GFStatusEForm.values().length ; i++) {
    %>
    <TH><%= GFStatusEForm.values()[i].getDesignation(objSession)%></TH>
    <%	} %>
</tr>
<%@ include file="/theme/list/tableHeader.jspf" %>

<%-- tpl:insert attribute="zoneCondition" --%>
<%
    GFStatistiqueModel line = viewBean.getStatistiqueEntity(i);
%>
<%-- /tpl:insert --%>

<%@ include file="/theme/list/lineStyle.jspf" %>
<%-- tpl:insert attribute="zoneList" --%>
<TD class="mtd headRow" nowrap width="20px" data-g-tooltip="libelle:<%= line.getType().getDesignation(objSession)%>"><%= line.getType().getCodeEForm()%></TD>
<%
    for (int j=1; j < GFStatusEForm.values().length ; j++) {
%>
<TD class="mtd" nowrap><%= line.getRecensement().get(GFStatusEForm.values()[j])%></TD>
<%	} %>
<%-- /tpl:insert --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:insert attribute="zoneTableFooter" --%>
<%-- /tpl:insert --%>

<%@ include file="/theme/list/tableEnd.jspf" %>
