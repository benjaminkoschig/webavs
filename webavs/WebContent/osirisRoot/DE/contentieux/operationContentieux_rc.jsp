
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA0031";
rememberSearchCriterias = true;
%>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%
CASectionViewBean viewBean = (CASectionViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
bButtonNew = false;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

usrAction = "<%=globaz.osiris.application.CAApplication.DEFAULT_OSIRIS_NAME %>.contentieux.operationContentieux.lister";
bFind = true;
naviShortKeys[82] = 'r1_c2';//  R (paramètres)
naviShortKeys[73] = 'r2_c2';//  I (incréments)
naviShortKeys[76] = 'r3_c2';//  L (libellés)
naviShortKeys[84] = 'r4_c2';//  T (inc. types)

top.document.title = "Rechtspflege - Anzeige Rechtspflege - " + top.location.href;

// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Rechtspflege<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
          <input type="hidden" name="id" value="<%=viewBean.getIdSection()%>"/>
          <input type="hidden" name="idSection" value="<%=viewBean.getIdSection()%>"/>
          <input type="hidden" name="forIdSequenceContentieux" value="<%=viewBean.getIdSequenceContentieux()%>"/>
          <TR>
            <TD width="128" rowspan="2" valign="top">Konto</TD>
            <TD rowspan="2">
              <textarea cols="40" rows="4" class="inputDisabled" readonly><%=viewBean.getCompteAnnexe().getTitulaireEntete()%></textarea>
            </TD>
            <TD rowspan="2"></TD>
            <TD colspan="2">Periode</TD>
            <TD></TD>
            <TD colspan="5">
              <input type="text" name="DateDebutDateFin" size="25" maxlength="25" value="<%=viewBean.getCompteAnnexe().getRole().getDateDebutDateFin(viewBean.getCompteAnnexe().getIdExterneRole())%>" class="libelleLongDisabled" tabindex="-1" readonly>
            </TD>
            <TD></TD>
            <TD></TD>
            <TD rowspan="2"></TD>
            <TD rowspan="2"></TD>
          </TR>
          <TR>
            <TD colspan="2">Kontosaldo</TD>
            <TD></TD>
            <TD colspan="5">
              <input type="text" name="solde" size="25" maxlength="25" value="<%=viewBean.getCompteAnnexe().getSoldeFormate()%>" class="montantDisabled" tabindex="-1" readonly>
            </TD>
            <TD></TD>
            <TD></TD>
          </TR>
          <TR>
            <TD width="128" valign="top">Sektion</TD>
            <TD>
              <input type="text" value="<%=viewBean.getIdExterne()%> - <%=viewBean.getDescription()%>" class="inputDisabled" readonly>
            </TD>
            <TD></TD>
            <TD colspan="2">Datum</TD>
            <TD></TD>
            <TD>
              <input type="text" name="date" value="<%=viewBean.getDateSection()%>" class="dateDisabled" tabindex="-1" readonly size="11" maxlength="10">
            </TD>
            <TD>&nbsp;</TD>
            <TD colspan="5">Sektionsaldo</TD>
            <TD></TD>
            <TD>
              <input type="text" name="soldeFormate" value="<%=viewBean.getSoldeFormate()%>" class="montantDisabled" tabindex="-1" readonly>
            </TD>
          </TR>
          <tr>
            <td width="128">Ablaufdatum</td>
            <td>
              <input type="text" name="echeance" value="<%=viewBean.getDateEcheance()%>" class="dateDisabled" tabindex="-1" readonly size="11" maxlength="10">
              Rechtspflege aufgeschoben/eingestellt
              <input type="checkbox" name="contentieuxEstSuspendu" <%=(viewBean.getContentieuxEstSuspendu().booleanValue())? "checked" : "unchecked"%> disabled readonly>
            </td>
            <td></td>
            <td>Grund </td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>
              <input type="text" name="motif" maxlength="30" size="31" value="<%=viewBean.getUcMotifContentieuxSuspendu().getCodeUtiLib()%>" class="inputDisabled" tabindex="-1" readonly>
            </td>
            <td>&nbsp;</td>
            <td> bis </td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>
              <input type="text" name="dateSuspendu" value="<%=viewBean.getDateSuspendu()%>" class="dateDisabled" readonly>
            </td>
          </tr>
          <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%> <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>