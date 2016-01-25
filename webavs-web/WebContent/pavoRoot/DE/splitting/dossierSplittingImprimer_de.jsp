
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%@ page import="globaz.globall.util.*"%>
<%
	idEcran = "CCI2005";
    globaz.pavo.db.compte.CICompteIndividuelImprimerViewBean viewBean = (globaz.pavo.db.compte.CICompteIndividuelImprimerViewBean)session.getAttribute ("viewBean");
	userActionValue = "pavo.compte.compteIndividuelImprimer.executer";
	tableHeight = 150;
%>
<SCRIPT language="JavaScript">
top.document.title = "IK - IK-Auszug ausdrucken"
var langue = "<%=languePage%>";
</SCRIPT>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

function init()
{
/*if (document.forms[0].elements('_method').value == "add")
   document.forms[0].elements('KcidIn').disabled = true;
   document.forms[0].elements('KcidLabel').disabled = true;
else
   document.forms[0].elements('KcidIn').disabled = true;
   document.forms[0].elements('KcidLabel').disabled = true;*/
}
/*
*/
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>IK-Auszug<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
		  <TR>
            <TD height="2"><a href="<%=servletContext + mainServletPath + "?userAction=pavo.compte.compteIndividuel.afficher&selectedId="+viewBean.getCompteIndividuelId()%>">Versicherte</a></TD>
            <TD height="2">
              <INPUT type="text" name="numeroAvsInv" size="15" class="disabled" readonly value="<%=globaz.commons.nss.NSUtil.formatAVSUnknown(viewBean.getCi().getNumeroAvs())%>"><INPUT type="text" name="nomInv" size="55" class="disabled" readonly value="<%=viewBean.getCi().getNomPrenom()%>">
            </TD>
          </TR>
          <tr>
            <td height="2">E-Mail Adresse</td>
            <td height="2">
              <input type="text" name="eMailAddress" maxlength="40" size="40" style="width:8cm;" value="<%=viewBean.getEMailAddress()!=null?viewBean.getEMailAddress():""%>">
              *</td>
          </tr>
		  <TR>
            <td height="2">Abschlüsse</td>
            <td height="2">
            	<ct:FWListSelectTag name="etatEcritures" defaut="active" data="<%=viewBean.getClotures()%>"/></TD>
          </TR>

          <%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>