<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@page import="globaz.osiris.db.interets.CAGenreInteretViewBean"%>
<%@page import="globaz.osiris.db.interets.CAPlanCalculInteretViewBean"%>
<%@page import="globaz.osiris.db.interets.CAPlanCalculInteret"%>
<%@ page language="java" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA4030";
rememberSearchCriterias = true;
%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ page import="globaz.osiris.db.interets.*" %>
<%@ page import="globaz.osiris.translation.*" %>
<%@ page import="globaz.jade.client.util.*" %>
<%
CAPlanCalculInteretViewBean viewBean = (CAPlanCalculInteretViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
session.setAttribute("idPlanCalculInteret",viewBean.getIdPlanCalculInteret());
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="options" menuId="CA-PlanCalculInteret" showTab="options">
</ct:menuChange>
<SCRIPT language="JavaScript">
usrAction = "<%=globaz.osiris.application.CAApplication.DEFAULT_OSIRIS_NAME %>.interets.genreInteret.lister";
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


top.document.title = "<ct:FWLabel key='GCA4030_TITRE_ECRAN'/>" + top.location.href;
// stop hiding -->

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key='GCA4030_TITRE_ECRAN'/><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
				<TR>
				<TD valign="top" ><ct:FWLabel key='GCA4030_PLAN'/>
				
				<input type="hidden" name="id" value="<%=viewBean.getIdPlanCalculInteret()%>"/>
				<input type="hidden" name="forIdPlanCalculInteret" value="<%=viewBean.getIdPlanCalculInteret()%>"/>
				<input type="hidden" name="idPlanCalculInteret" value="<%=viewBean.getIdPlanCalculInteret()%>"/>
				<input type="text" name="" size="40" maxlength="40" value="<%=viewBean.getIdPlanCalculInteret()%> - <%=viewBean.getLibelle()%>" tabindex="-1" class="libelleLongDisabled" readonly >
				</TD>
				</TR>


			</TD>
			<TD>&nbsp;</TD>
			</TR>
          <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<!--            <TD bgcolor="#FFFFFF" colspan="2" align="right">
            <A href="javascript:document.forms[0].submit();">
                <IMG name="btnFind" src="/images/btnFind.gif" border="0">
            </A>
            </TD> -->

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>