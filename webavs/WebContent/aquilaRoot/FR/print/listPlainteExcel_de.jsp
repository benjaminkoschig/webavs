 <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCO2005"; %>
<%@ page import="globaz.globall.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="globaz.aquila.servlet.CODefaultServletAction" %>
<%@ page import="globaz.aquila.db.print.*" %>
<%@ page import="globaz.aquila.db.access.batch.*"%>
<%@page import="globaz.globall.util.JACalendarGregorian"%>
<%@page import="java.util.ArrayList"%>
<%@page import="globaz.osiris.db.comptes.*"%>
<%@page import="globaz.aquila.db.access.paiement.COPaiementManager"%>
<%
COListPlainteExcelViewBean viewBean = (COListPlainteExcelViewBean) session.getAttribute("viewBean");
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> <%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
<%
userActionValue = "aquila.print.listPlainteExcel.executer";
%>
top.document.title = "Liste des plaintes - contentieux " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Liste des plaintes (Excel)<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
<tr>
        <td style="width:200;" >E-mail</td>
        <td colspan="3">
			<input type="text" name="eMailAddress" value="<%=viewBean.getEMailAddress()%>" class="libelleLong">
		</td>
</tr>
<TR>
	<TD style="width:200;">Période du</TD>
	<td style="width:300;">
	<ct:FWCalendarTag name="fromDate" value="" />
	</td>
	<TD >Au</td>
	<TD>
	<ct:FWCalendarTag name="untilDate" value=""/>
	</TD>
 </TR>
 <tr>
 <TD style="width:200;" >Sans date de fin</TD>
		<td><input type="checkbox" name="withoutEndDate" onclick="document.forms[0].elements['untilDate'].disabled=this.checked; document.forms[0].elements['anchor_untilDate'].disabled=this.checked; if(this.checked)document.forms[0].elements['untilDate'].value=''"></td>
 </tr>
<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<%	}%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>