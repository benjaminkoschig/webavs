<%@page import="globaz.fweb.taglib.FWSystemCodeTag"%>
<%
long currentTime = System.currentTimeMillis();
JadeLogger.info(this, "Time JSP start : " + currentTime);
%>
<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
		
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="globaz.amal.vb.contribuable.AMContribuableHistoriqueViewBean"%>
<%@page import="ch.globaz.amal.business.constantes.IAMActions"%>
<%@page import="globaz.jade.log.*"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%
	idEcran="AM000X";

	AMContribuableHistoriqueViewBean viewBean = (AMContribuableHistoriqueViewBean) session.getAttribute("viewBean");

	boolean viewBeanIsNew = "add".equals(request.getParameter("_method"));

	
	autoShowErrorPopup = true;
	
	bButtonDelete = false;
	bButtonNew = false;
	bButtonUpdate = false;
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/amal.css" rel="stylesheet"/> 
<%-- tpl:put name="zoneScripts" --%>


<script type="text/javascript">
var ACTION_CONTRIBUABLE="<%=IAMActions.ACTION_CONTRIBUABLE%>";
var actionMethod;
var userAction;


function init() {
	
}

function add() {
}

function del() {
}

function cancel() {
}

function validate() {
}




</script>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
	<%if(!viewBeanIsNew){
		
 		%>
			<TR>		
				<td colspan="4">
					<table width="100%">
						<tr>
							<td align="left" style="vertical-align:top">
							</td>
							<td></td>
						</tr>
					</table>
					<div>&nbsp;</div>
					<div>&nbsp;</div>
					<div>&nbsp;</div>					
					
				</TD>
			</TR>
			<%
				}
			%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>
<%
long endTime = System.currentTimeMillis();
JadeLogger.info(this, "Time JSP end : " + endTime);
JadeLogger.info(this, "Time JSP Contribuable : "+(endTime - currentTime));
%>