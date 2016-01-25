
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%@page import="globaz.osiris.db.retours.CALotsRetoursViewBean"%>
<%@page import="globaz.osiris.db.retours.CALotsRetours"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA0069";
rememberSearchCriterias = true;

CALotsRetours db = new CALotsRetours();
db.setSession(objSession);

boolean hasRightNew = objSession.hasRight("osiris.retours.lotsRetours.afficher", FWSecureConstants.ADD);
bButtonNew= hasRightNew; 
%>
<script>
	top.document.title = "Lots retours - " + top.location.href;
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
usrAction = "<%=globaz.osiris.application.CAApplication.DEFAULT_OSIRIS_NAME %>.retours.lotsRetours.lister";
bFind = true;
-->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Lots de retours<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
          <TR>
            <TD width="126">Etat</TD>
            <TD nowrap colspan="2">
              <ct:FWListSelectTag data="<%=db.getEtatLotData()%>" defaut="<%=CALotsRetours.CLE_ETAT_LOT_NON_LIQUIDE%>" name="forCsEtatLot"/>
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