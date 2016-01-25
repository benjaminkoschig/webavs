
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA0003";
rememberSearchCriterias = true;
%>
<%	String join = ""; %>
<%
globaz.osiris.db.comptes.CACompteAnnexeViewBean viewBean = (globaz.osiris.db.comptes.CACompteAnnexeViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
String requestId = request.getParameter("id");
bButtonNew = false;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
usrAction = "<%=globaz.osiris.application.CAApplication.DEFAULT_OSIRIS_NAME %>.comptes.apercuComptesCourants.lister";
bFind = true;
<!--hide this script from non-javascript-enabled browsers
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

top.document.title = "Konti - Uebersicht der Kontokorrente " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Uebersicht der Kontokorrente<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
          <%  join += "&idCompteAnnexe=" + viewBean.getIdCompteAnnexe(); %>
				<input type="hidden" name="id" value="<%=requestId%>"/>
            <TR>
              <TD width="10%" valign="top">Konto</TD>
              <TD valign="top" rowspan="3">
                <TEXTAREA cols="40" rows="3" class="libelleLongDisabled" readonly><%=viewBean.getTitulaireEntete()%></TEXTAREA>
              </TD>
              <TD width="80%" align="right" valign="top">Auswahl der Konton&nbsp;
                <SELECT name="forSelectionCompte" class="libelleCourt">
                  <option value="1000" selected>alle</option>
                  <option value="1">offene</option>
                  <option value="2">saldierte</option>
                </SELECT>
              </TD>
            </TR>

            <TR>
            	<td colspan="2">&nbsp;</td>

            	<td align="right" valign="top">
	             <%
					String idAdministrateurSrc = request.getParameter("idAdministrateurSrc");
					String idContentieuxSrc = request.getParameter("idContentieuxSrc");
					String libSequence = request.getParameter("libSequence");

					if (globaz.jade.client.util.JadeStringUtil.isNull(idContentieuxSrc)) {
						idContentieuxSrc = "";
					}

					if (globaz.jade.client.util.JadeStringUtil.isNull(libSequence)) {
						libSequence = "";
					}

					if (globaz.jade.client.util.JadeStringUtil.isNull(idAdministrateurSrc)) {
						idAdministrateurSrc = "";
					}

					if (!globaz.jade.client.util.JadeStringUtil.isBlank(idAdministrateurSrc)) {
				%>
					<A href="<%=request.getContextPath()%>/aquila?userAction=aquila.administrateurs.administrateur.afficher&selectedId=<%=idAdministrateurSrc%>" class="external_link">Verwalter</A>
				<%
					} else {
				%>
					&nbsp;
				<%
					}
				%>


			<ct:menuChange displayId="options" menuId="CA-ApercuParSectionDossier">
				<ct:menuSetAllParams key="idContentieuxSrc" value="<%=idContentieuxSrc%>"/>
				<ct:menuSetAllParams key="libSequence" value="<%=libSequence%>"/>
				<ct:menuSetAllParams key="idAdministrateurSrc" value="<%=idAdministrateurSrc%>"/>
				<ct:menuSetAllParams key="idTiers" value="<%=viewBean.getIdTiers()%>"/>
				<ct:menuSetAllParams key="forIdExterneRoleLike" value="<%=viewBean.getIdExterneRole()%>"/>
				<ct:menuSetAllParams key="forIdRole" value="<%=viewBean.getIdRole()%>"/>
			</ct:menuChange>

	             </td>
            </TR>
          <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%> <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>