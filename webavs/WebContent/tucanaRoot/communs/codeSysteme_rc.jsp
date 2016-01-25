<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT>
<% 
bButtonNew = false;
idEcran = "TU-900";
subTableHeight=0;
 %>
usrAction = "tucana.communs.codeSysteme.lister";
bFind=true;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="TIT_LISTE_CS" /><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
					<TR>
						<TD width="180"><INPUT type="hidden" name="forIdTypeCode"
							value='<%=(request.getParameter("idCodeSysteme")==null)?"":request.getParameter("idCodeSysteme")%>'>
						</TD>
						<TD width="260"></TD>
					</TR>

					<TR>
						<TD><ct:FWLabel key="CODE" /></TD>
						<TD><INPUT type="text" name="fromCode"
							value='<%=(request.getParameter("_pos")==null)?"":request.getParameter("_pos")%>'>
						</TD>
					</TR>
					<TR>
						<TD><ct:FWLabel key="LIBELLE" /></TD>
						<TD><INPUT type="text" name="likeLibelle" value=""></TD>
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
