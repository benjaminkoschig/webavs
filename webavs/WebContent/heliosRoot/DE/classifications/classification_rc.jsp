<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">


<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%
idEcran="GCF4005";
String tous = "Tous";
if (languePage.equalsIgnoreCase("de")) {
	tous = "Alle";
}
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT>
usrAction = "helios.classifications.classification.lister";

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Klassifikationen Uebersicht<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
                        <TR>
                                <TD nowrap width="128">Bezeichnung</TD>
                                <TD nowrap colspan="2" width="298">
	                                <INPUT type="text" name="fromLibelle" size="35" maxlength="40" value="" tabindex="-1">
                                </TD>
                         </TR>

	        			<TR>           
	        			<TD width="108">Für den Mandant</TD>         
			            <TD width="282">
						 <ct:FWListSelectTag name="forIdMandat"
							 defaut=""
							 data="<%=globaz.helios.translation.CGListes.getMandatListe(session,tous)%>"/>
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