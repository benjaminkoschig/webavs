
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA0044";
rememberSearchCriterias = true;
%>
<%
globaz.osiris.db.ordres.CAOrdreGroupeViewBean viewBean =
  (globaz.osiris.db.ordres.CAOrdreGroupeViewBean)session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="options" menuId="CA-OnlyDetail" showTab="menu"/>

<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
usrAction = "<%=globaz.osiris.application.CAApplication.DEFAULT_OSIRIS_NAME %>.ordres.ordresGroupes.lister";
bFind = true;
naviShortKeys[82] = 'r1_c2';//  R (paramètres)
naviShortKeys[73] = 'r2_c2';//  I (incréments)
naviShortKeys[76] = 'r3_c2';//  L (libellés)
naviShortKeys[84] = 'r4_c2';//  T (inc. types)

function add() {

document.forms[0].typeOrdreGroupe.value = "207001";


}
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Aperçu des ordres groupés<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
          <TR>
            <TD>Date</TD>
            <TD nowrap width="400" colspan="2">
				<ct:FWCalendarTag name="forDateEcheance" doClientValidation="CALENDAR" value=""/>
            </TD>
            <TD width="500" align="right" valign="bottom">&nbsp; </TD>
          </TR>
          <TR>
            <TD width="100">Description</TD>
            <TD nowrap width="400" colspan="2">
              <input type="text" name="likeMotif" >
            </TD>
            <TD width="500" align="right" valign="bottom"> Type d'ordre
              <%	viewBean.getCsTypeOrdreGroupe();
							globaz.globall.parameters.FWParametersSystemCode _type = null; %>
              <SELECT name="forTypeOrdreGroupe">
                <%	for (int i=0; i < viewBean.getCsTypesOrdreGroupe().size(); i++) {
								_type = (globaz.globall.parameters.FWParametersSystemCode) viewBean.getCsTypesOrdreGroupe().getEntity(i);
								if (_type.getIdCode().equalsIgnoreCase(viewBean.getTypeOrdreGroupe())) { %>
                <OPTION selected value="<%=_type.getIdCode()%>"><%=_type.getCurrentCodeUtilisateur().getLibelle()%></OPTION>
                <%	} else { %>
                <OPTION value="<%=_type.getIdCode()%>"><%=_type.getCurrentCodeUtilisateur().getLibelle()%></OPTION>
                <%	}
							} %>
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