<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@page import="globaz.pegasus.vb.liste.PCListeEcheanceEnfants11AnsViewBean" %>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions" %>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%@page import="globaz.jade.client.util.JadeStringUtil" %>
<%-- tpl:put name="zoneInit" --%>
<%
    // Les labels de cette page commence par la préfix "JSP_LOV_D"

    idEcran = "PPC2010";

    PCListeEcheanceEnfants11AnsViewBean viewBean = (PCListeEcheanceEnfants11AnsViewBean) session.getAttribute("viewBean");

    userActionValue = IPCActions.ACTION_ECHEANCE_LISTE_ENFANTS_11_ANS + ".executer";

    String rootPath = servletContext + (mainServletPath + "Root");

%>
<%-- /tpl:put --%>

<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>


<%@ include file="/theme/detail/javascripts.jspf" %>
<script type="text/javascript" src="<%=rootPath %>/scripts/jadeBaseFormulaire.js"></script>

<SCRIPT language="javascript">
    $(function(){
        $("#btnCtrlJade").hide();
        $dateDebutMonth = $("#dateDebutMonth");
        $dateFinMonth = $("#dateFinMonth");
        $buttonMonth =  $("#monthButton") ;
    });

    getDates = function () {
        return  $dateDebutMonth.val() + "," + $dateFinMonth.val();
    }

</script>
<STYLE TYPE="text/css">
    table tr.separator { height: 50px; }
</STYLE>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_TITRE_ECHEANCE_ENFANTS_11_ANS"/> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
<tr>
    <td colspan="2" align="center">
        <label for="dateDebutMonth"><ct:FWLabel key="PEGASUS_JSP_PPC2010_DATE_DEBUT"/></label>
        <input tabindex="1"  type="text" id="dateDebutMonth" data-g-calendar="mandatory:true, type:month" value="">
        <label for="dateFinMonth"><ct:FWLabel key="PEGASUS_JSP_PPC2010_DATE_FIN"/></label>
        <input tabindex="1"  type="text" id="dateFinMonth" data-g-calendar="mandatory:true, type:month" value="">
    </td>
</tr>
<tr class="separator" />
<tr>
    <td colspan="2" align="center">
    <a  tabindex="2" id="monthButton" data-g-download="docType:xls,
					dynParametres: getDates,
                    serviceClassName: ch.globaz.pegasus.business.services.doc.excel.ListeDeControleService,
                    serviceMethodName: createListeEnfants11Ans,
                    docName: listeEcheanceEnfants11Ans"
    ><ct:FWLabel key="PEGASUS_JSP_PPC2010_LISTE_SIMPLE"/></a>
    </td>
</tr>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>