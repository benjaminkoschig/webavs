
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA4016";
rememberSearchCriterias = true;
%>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%@ page import="globaz.osiris.db.contentieux.*" %>
<%
globaz.osiris.db.contentieux.CASequenceContentieuxViewBean viewBean = (globaz.osiris.db.contentieux.CASequenceContentieuxViewBean)session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<script>
	top.document.title = "Contentieux - Recherche des séquences contentieux - " + top.location.href;
  usrAction = "<%=globaz.osiris.application.CAApplication.DEFAULT_OSIRIS_NAME %>.contentieux.sequenceContentieux.lister";
  bFind = true;
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Recherche des séquences contentieux<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
          <TR>
            <TD width="126">Num&eacute;ro</TD>
            <TD nowrap colspan="2">
              <input type="text" name="fromIdSequenceContentieux" value="<%=viewBean.getIdSequenceContentieux()%>">
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