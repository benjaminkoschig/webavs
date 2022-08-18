<%@ page import="globaz.eform.vb.formulaire.GFFormulaireListViewBean" %>
<%@ page import="globaz.eform.vb.formulaire.GFFormulaireViewBean" %>
<%@ page import="globaz.commons.nss.NSUtil" %>
<%@ page import="ch.globaz.eform.constant.GFStatusEForm" %>
<%@ page import="globaz.eform.helpers.formulaire.GFFormulaireHelper" %>
<%@ page import="globaz.jade.admin.user.bean.JadeUser" %>
<%@ page import="ch.globaz.eform.web.servlet.GFFormulaireServletAction" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%@ page errorPage="/errorPage.jsp" %>

<%
    GFFormulaireListViewBean viewBean = (GFFormulaireListViewBean) request.getAttribute("viewBean");
    JadeUser currentUser = objSession.getUserInfo();
    boolean manager = objSession.hasRight("eform.formulaire.formulaire.manager", "UPDATE");
    size = viewBean.getSize();
    detailLink = baseLink + "afficher&selectedId=";
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
<%-- /tpl:insert --%>

<%-- tpl:insert attribute="zoneHeaders" --%>
<%-- /tpl:insert --%>

<tr>
    <TH></TH>
    <TH><ct:FWLabel key="JSP_EFORM_FORMULAIRE_TYPE"/></TH>
    <TH><ct:FWLabel key="JSP_EFORM_FORMULAIRE_NOM_FORMULAIRE"/></TH>
    <TH><ct:FWLabel key="JSP_EFORM_FORMULAIRE_DATE"/></TH>
    <TH><ct:FWLabel key="JSP_EFORM_FORMULAIRE_BUSINESS_PROCESS_ID"/></TH>
    <TH><ct:FWLabel key="JSP_EFORM_FORMULAIRE_NSS"/></TH>
    <TH><ct:FWLabel key="JSP_EFORM_FORMULAIRE_NOM"/></TH>
    <TH><ct:FWLabel key="JSP_EFORM_FORMULAIRE_PRENOM"/></TH>
    <TH><ct:FWLabel key="JSP_EFORM_FORMULAIRE_STATUT"/></TH>
    <TH><ct:FWLabel key="JSP_EFORM_FORMULAIRE_GESTIONNAIRE"/></TH>
</tr>
<%@ include file="/theme/list/tableHeader.jspf" %>

<%-- tpl:insert attribute="zoneCondition" --%>
<%
    GFFormulaireViewBean line = (GFFormulaireViewBean)viewBean.getEntity(i);
    String detailUrl = "parent.location.href='" + detailLink + line.getId() + "'";
%>
<%-- /tpl:insert --%>

<%@ include file="/theme/list/lineStyle.jspf" %>
<%-- tpl:insert attribute="zoneList" --%>
<TD class="mtd dec<%=line.getFormulaire().getId()%>" nowrap width="20px">
     <ct:menuPopup menu="eform-optionsformulaires" target="top.fr_main">
         <ct:menuParam key="selectedId" value="<%=line.getFormulaire().getId()%>" />
         <ct:menuParam key="idTiers" value="<%=line.getIdTiers()%>" />
         <% if (!(manager || currentUser.getVisa().equals(line.getFormulaire().getUserGestionnaire()))) {%>
            <ct:menuExcludeNode nodeId="STATUT_ENCOURS"/>
            <ct:menuExcludeNode nodeId="STATUT_TRAITE"/>
         <% } %>
         <% if(line.getIdTiers().isEmpty()){ %>
            <ct:menuExcludeNode nodeId="VISU_TIERS"/>
         <% } %>
     </ct:menuPopup>
    <%
        String subjectLine= line.getSubject();
        if(subjectLine.length()>60){
           subjectLine=subjectLine.substring(0,60);
        }
    %>
</TD>
<TD class="mtd dec<%=line.getFormulaire().getSubject() %>" nowrap onclick="<%=detailUrl%>"><%= line.getFormulaire().getSubject() %> </TD>
<TD class="mtd dec<%=line.getSubject() %>" nowrap onclick="<%=detailUrl%>"><%=subjectLine%> </TD>
<TD class="mtd dec<%=line.getFormulaire().getDate() %>" nowrap onclick="<%=detailUrl%>"><%= line.getFormulaire().getDate() %> </TD>
<TD class="mtd dec<%=line.getFormulaire().getBusinessProcessId() %>" nowrap onclick="<%=detailUrl%>"><%= line.getFormulaire().getBusinessProcessId() %> </TD>
<TD class="mtd dec<%=line.getFormulaire().getBeneficiaireNss() %>" nowrap onclick="<%=detailUrl%>"><%= NSUtil.formatAVSUnknown(line.getFormulaire().getBeneficiaireNss()) %> </TD>
<TD class="mtd dec<%=line.getFormulaire().getBeneficiaireNom() %>" nowrap onclick="<%=detailUrl%>"><%= line.getFormulaire().getBeneficiaireNom() %> </TD>
<TD class="mtd dec<%=line.getFormulaire().getBeneficiairePrenom() %>" nowrap onclick="<%=detailUrl%>"><%= line.getFormulaire().getBeneficiairePrenom()%> </TD>
<TD class="mtd dec<%=line.getFormulaire().getStatus() %>" nowrap onclick="<%=detailUrl%>"><%= GFStatusEForm.getStatusByCodeSystem(line.getFormulaire().getStatus()).getDesignation(objSession)%></TD>
<TD class="mtd dec<%=line.getFormulaire().getUserGestionnaire() %>" nowrap onclick="<%=detailUrl%>"><%= GFFormulaireHelper.getGestionnaireDesignation(line.getFormulaire().getUserGestionnaire()) %> </TD>
<%-- /tpl:insert --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:insert attribute="zoneTableFooter" --%>
<%-- /tpl:insert --%>

<%@ include file="/theme/list/tableEnd.jspf" %>
