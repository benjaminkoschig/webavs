
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA0008";
rememberSearchCriterias = true;
%>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="options" menuId="CA-OnlyDetail" showTab="menu"/>

<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
usrAction = "<%=globaz.osiris.application.CAApplication.DEFAULT_OSIRIS_NAME %>.comptes.apercuJournal.lister";
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

top.document.title = "Comptes - Recherche des journaux - " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Recherche des journaux<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

			<TR>
			<TD nowrap width="70">Num&eacute;ro</TD>
            <TD nowrap width="200">
            <input type="text" name="fromNumero" tabindex="-1" class="libelleStandard">
            </TD>
             <td nowrap width="70">Propriétaire</td>
			  <td nowrap width="300"><input type="text" name="forProprietaire" class="libelleStandard" value="<%=globaz.osiris.application.CAApplication.getApplicationOsiris().getCAParametres().isApercuJournauxUser()?objSession.getUserId():""%>"/></td>
            <TD nowrap align="right">Journaux
              <SELECT name="forSelectionJournaux" class="libelleCourt">
                <option value="1000">Tous</option>
                <option value="1">Ouverts</option>
                <option value="2">Comptabilis&eacute;s</option>
                <option value="3">Partiels</option>
                <option value="4">En erreur</option>
                <option value="5">Annul&eacute;s</option>
                <option value="6">En traitement</option>
                <option value="7" selected>En cours</option>
              </SELECT>
			</TD>
			</TR>

			<TR>
			<TD nowrap width="70">Description</TD>
            <TD nowrap width="200">
            <input type="text" name="forLibelleLike" class="libelleStandard"/>
            </TD>
            <TD nowrap width="100" align="left">Date comptable</TD>
            <TD nowrap width="300" align="left">
				<ct:FWCalendarTag name="untilDateValeurCG" value="" />
			</TD>
            <TD nowrap align="right">Tri
              <SELECT name="forSelectionTri" class="libelleCourt">
                <option value="1000" selected>Par num&eacute;ro</option>
                <option value="1">Par date création</option>
                <option value="2">Par date comptable</option>
                <option value="3">Par description</option>
                <option value="4">Par propri&eacute;taire, num&eacute;ro</option>
                <option value="5">Par propri&eacute;taire, date création</option>
                <option value="6">Par propri&eacute;taire, description</option>
              </SELECT>
			</TD>
			</TR>

          <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%> <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>