
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA0036";
rememberSearchCriterias = true;
%>
<%
globaz.osiris.db.contentieux.CAEvenementContentieuxViewBean viewBean =
(globaz.osiris.db.contentieux.CAEvenementContentieuxViewBean)session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
usrAction = "<%=globaz.osiris.application.CAApplication.DEFAULT_OSIRIS_NAME %>.message.apercuJournalisation.lister";
bFind = true;
naviShortKeys[82] = 'r1_c2';//  R (paramètres)
naviShortKeys[73] = 'r2_c2';//  I (incréments)
naviShortKeys[76] = 'r3_c2';//  L (libellés)
naviShortKeys[84] = 'r4_c2';//  T (inc. types)
// stop hiding -->
top.document.title = "Rechtspflege - Vorschau der Journalisierungen - " + top.location.href;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Uebersicht der Journalisierungen <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
				          	<TD width=80>Konto</TD>
			    	      	<TD><TEXTAREA cols="40" rows="4" class="libelleLongDisabled" readonly><%=viewBean.getSection().getCompteAnnexe().getTitulaireEntete()%></TEXTAREA></TD>
			        	</TR>
			          	<TR>
			          		<TD width="80" valign="top">Sektion</TD>
			            	<TD>
								<input type="text" value="<%=viewBean.getSection().getIdExterne()%> - <%=viewBean.getSection().getDescription()%>" readonly size="60" maxlength="49" class="numeroDisabled">
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