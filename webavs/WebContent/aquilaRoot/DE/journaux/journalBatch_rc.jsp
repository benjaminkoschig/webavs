
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCO3005";
rememberSearchCriterias = true;
bButtonNew = false;
%>
<%@ page import="globaz.aquila.db.journaux.*" %>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="options" menuId="CO-OnlyDetail" showTab="menu"/>

<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
usrAction = "aquila.journaux.journalBatch.lister";
bFind = true;
naviShortKeys[82] = 'r1_c2';//  R (paramètres)
naviShortKeys[73] = 'r2_c2';//  I (incréments)
naviShortKeys[76] = 'r3_c2';//  L (libellés)
naviShortKeys[84] = 'r4_c2';//  T (inc. types)

function removeParam(str_source, str_param) {
  var result = str_source;
  var paramPos = result.indexOf(str_param);
//   if no param, do nothing
  if (paramPos < 0)
    return result;

  nextParamPos = result.indexOf("&", paramPos + 1);
  var str_end = "";
  if (nextParamPos > -1)//   there are more parameters after this one
    str_end = result.slice(nextParamPos);

  result = result.slice(0, paramPos);
  result += str_end;

//  alert ("returning " + result);
  return result;
}

top.document.title = "Kontis - Suche der Journale - " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Suche der Journale<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

			<TR>
			<TD nowrap width="70">Nummer</TD>
            <TD nowrap width="200"><input type="text" name="fromNumero" tabindex="-1" class="libelleStandard"/></TD>
            <TD width="300" align="right">&nbsp;</TD>
            <TD nowrap width="179">&nbsp;</TD>
            <TD nowrap align="right">Journal
              <SELECT name="forCsEtat" class="libelleCourt">
                <option value="" selected>Alle</option>
                <option value="<%=COJournalBatchViewBean.OUVERT%>">Offene</option>
                <option value="<%=COJournalBatchViewBean.TRAITE%>">Behandelte</option>
                <option value="<%=COJournalBatchViewBean.PARTIEL%>">Teilweise</option>
                <option value="<%=COJournalBatchViewBean.ERREUR%>">Fehlerhafte</option>
                <option value="<%=COJournalBatchViewBean.ANNULE%>">Annullierte</option>
                <option value="<%=COJournalBatchViewBean.TRAITEMENT%>">In Behandlung</option>
              </SELECT>
			</TD>
			</TR>

			<TR>
			<TD nowrap width="70">Beschreibung</TD>
            <TD nowrap width="200"><input type="text" name="forLibelleLike" class="libelleStandard"/></TD>
            <TD width="300" align="right">&nbsp;</TD>
            <TD nowrap width="179">&nbsp;</TD>
            <TD nowrap align="right">&nbsp;</TD>
			</TR>
          <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%> <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>