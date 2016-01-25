<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA0002";
rememberSearchCriterias = true;
%>
<%@ page import="globaz.osiris.application.*" %>
<%@ page import="globaz.globall.api.*" %>
<%@ page import="globaz.globall.db.*" %>
<%@ page import="globaz.osiris.external.*" %>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%
CACompteAnnexeViewBean viewBean = (CACompteAnnexeViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
IntTiers element1 = (IntTiers) session.getAttribute(globaz.osiris.servlet.action.compte.CAComptesAnnexesAction.VB_TIERS);
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
usrAction = "<%=globaz.osiris.application.CAApplication.DEFAULT_OSIRIS_NAME %>.comptes.apercuComptesAnnexes.lister";
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

top.document.title = "Comptes - Aperçu des comptes annexes - " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Aper&ccedil;u des comptes annexes<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
		  <input type="hidden" name="idTiers" value="<%=element1.getIdTiers()%>"/>
            <TR>
              <TD width="128" align="left" valign="top">Tiers</TD>
              <TD nowrap colspan="2">
                <TEXTAREA cols="40" rows="2" class="libelleLongDisabled" readonly><%=element1.getTitulaireNomLieu()%></TEXTAREA>
              </TD>
              <TD width="4"></TD>
              <TD width="515" align="right" valign="bottom"> S&eacute;lection
                des comptes
                <SELECT name="forSelectionCompte" class="libelleCourt">
                  <option selected value="1000">tous</option>
                  <option value="1">ouverts</option>
                  <option value="2">sold&eacute;s</option>
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