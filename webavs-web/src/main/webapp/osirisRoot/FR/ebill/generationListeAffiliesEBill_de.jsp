<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ page import="globaz.osiris.print.itext.list.CAIListPlanRecouvNonRespectes"%>
<%@ page import="globaz.globall.util.JACalendar"%>
<%@ page import="globaz.osiris.db.print.CAImpressionPlanViewBean"%>
<%@ page import="globaz.naos.helpers.affiliation.AFListeAffiliationModeFacturationHelper" %>
<%@ page import="globaz.naos.db.affiliation.AFListeAffiliationModeFacturationProcess" %>
<%@ page import="globaz.osiris.process.ebill.CAProcessGenererListeAffiliesEBill" %>
<%
    idEcran="GCA3036";
    CAProcessGenererListeAffiliesEBill viewBean = (CAProcessGenererListeAffiliesEBill) session.getAttribute("viewBean");

    subTableWidth="0";

    // mettre directement la bonne valeur pour appeller le process
    userActionValue="osiris.ebill.generationListeAffiliesEBill.executer";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>Generation liste des affiliés eBill<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>

<TR>
    <TD width="23%" class="label">E-mail</TD>
    <%
        int inputSize = viewBean.getSession().getUserEMail().length()+5;
        if(inputSize<20){
            inputSize=20;
        }
    %>
    <TD class="control"><INPUT type="text" name="eMailAddress" value="<%=viewBean.getSession().getUserEMail()%>" size="<%=inputSize%>"></TD>
</TR>
<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>