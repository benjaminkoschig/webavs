<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %> 
<%	idEcran="GCF0014";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT>
usrAction = "helios.comptes.mandat.lister";
bFind = true;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Mandat<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
                        <TR>
            <TD nowrap width="82">Libellé</TD>
            <TD nowrap colspan="2" width="327">
                                		<INPUT type="text" name="reqLibelle" size="35" maxlength="40" value="" tabindex="-1">
                                	  </TD>
            <TD width="51">Tri</TD>
            <TD width="106">
		<ct:FWCodeSelectTag name="reqCritere" defaut="" codeType="CGRECHMAND" />
		<input type="hidden" name="colonneSelection" value="<%= request.getParameter("colonneSelection")%>">		
    	     </TD>

          </TR>

                        <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>