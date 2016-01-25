
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%@ page import="globaz.globall.util.*"%>
<%
	idEcran = "CCI2008";
    globaz.pavo.db.splitting.CIImprimerAnalyseViewBean viewBean = (globaz.pavo.db.splitting.CIImprimerAnalyseViewBean)session.getAttribute ("viewBean");
	userActionValue = "pavo.splitting.dossierSplitting.imprimerAnalyse";
	tableHeight = 150;
	
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	String eMailAddress = objSession.getUserEMail()!=null?objSession.getUserEMail():"";
%>
<SCRIPT language="JavaScript">
top.document.title = "CI - Impression du RCI avant splitting"
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
			<%-- tpl:put name="zoneTitle" --%>Aperçu du RCI avant splitting<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
		  <TR>
            <TD height="2">Assuré</TD>
            <TD height="2">
              <INPUT type="text" name="numeroAvsAssureInv" size="18" class="disabled" readonly value="<%=globaz.commons.nss.NSUtil.formatAVSUnknown(viewBean.getIdTiersAssure())%>"><INPUT type="text" name="nomInv" size="67" class="disabled" readonly value="<%=viewBean.getTiersAssureNomComplet()%>">
            </TD>
          </TR>
                    <tr>
			<td>
				Date de naissance&nbsp;
			</td>
			<td colspan="4">
				<input type="text" size = "10" class='disabled' name="dateNaissance" readonly tabindex='-1' value = "<%=viewBean.getDateNaissanceAss()%>">
				&nbsp;
				Sexe &nbsp;
				<input type="text" size = "7" class='disabled' name="sexe" readonly tabindex='-1' value = "<%=viewBean.getSexeLibelleAss()%>">
				Pays &nbsp;
				<input type="text" size = "50" class='disabled' name="pays" readonly tabindex='-1' value = "<%=viewBean.getPaysFormateAss()%>">
			</td>
		</tr>
          <TR>
            <TD height="2">Conjoint</TD>
            <TD height="2">
              <INPUT type="text" name="numeroAvsConjointInv" size="18" class="disabled" readonly value="<%=globaz.commons.nss.NSUtil.formatAVSUnknown(viewBean.getIdTiersConjoint())%>"><INPUT type="text" name="nomInv" size="67" class="disabled" readonly value="<%=viewBean.getTiersConjointNomComplet()%>">
            </TD>
          </TR>
		<tr>
			<td>
				Date de naissance&nbsp;
			</td>
			<td colspan="4">
				<input type="text" size = "10" class='disabled' name="dateNaissance" readonly tabindex='-1' value = "<%=viewBean.getDateNaissanceConj()%>">
				&nbsp;
				Sexe &nbsp;
				<input type="text" size = "7" class='disabled' name="sexe" readonly tabindex='-1' value = "<%=viewBean.getSexeLibelleConj()%>">
				Pays &nbsp;
				<input type="text" size = "50" class='disabled' name="pays" readonly tabindex='-1' value = "<%=viewBean.getPaysFormateConj()%>">
			</td>
		</tr>
          <tr>
            <td height="2">Adresse E-Mail</td>
            <td height="2">
              <input type="text" name="email" maxlength="40" size="40" style="width:8cm;" value="<%=eMailAddress%>">
              *</td>
          </tr>

           <TR>
            <td height="2">&nbsp;</td>
            <td height="2">&nbsp;</td>
          </TR>

          <%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%  if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<SCRIPT>
</SCRIPT>
<%  }  %>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>