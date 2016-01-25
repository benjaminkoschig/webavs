<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%>
<%
bButtonCancel = false;
bButtonValidate = false;
bButtonDelete = false;
bButtonUpdate = false;
bButtonNew = false;
idEcran = "GTI5004";
globaz.pyxis.vb.test.TILettragePlagesViewBean viewBean = (globaz.pyxis.vb.test.TILettragePlagesViewBean)request.getAttribute("viewBean");

%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness"  --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<%@page import="globaz.pyxis.summary.TISummary"%>
<%@page import="globaz.pyxis.summary.ITIBaseSummarizable"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>

<SCRIPT language="JavaScript">
top.document.title = "Test"
<!-- hide this script from non-javascript-enabled browsers



function getPlages(query) {
	document.getElementById('work').innerHTML="Chargement des données en cours..."
	if (window.XMLHttpRequest) req1 = new XMLHttpRequest();
	else if (window.ActiveXObject) req1 = new ActiveXObject("Microsoft.XMLHTTP");
	else return; // fall on our sword
	req1.open('GET', query,true);
	req1.onreadystatechange = onReady;
	req1.send(null);
}
function onReady() {
	if (req1.readyState != 4) { 
		return;
	}
	if ((req1.status == 0)||(req1.status == 200)) {
		var result = req1.responseText;
		document.getElementById('work').innerHTML="Chargement terminé. "+result
	}
}

function add() {
}
function upd() {
}
function validate() {
}
function cancel() {
}
function del() {
}
function init(){}

// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Test<%-- /tpl:put  --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%>
						<tr><td>
						<table border="1" >
							<tr>
								<td colspan=2>
									<div id="search" >
										<input type="text" id="nbPlages">
										<input type="button" id="btSearch" value="Définir les plages"
											onclick="getPlages('<%=request.getContextPath()%>/pyxis?userAction=pyxis.test.lettrage.chercherPlages&nbPlages='+document.getElementById('nbPlages').value)">
									</div>
								</td>
							</tr>
							<tr style="background:white">
								<td valign="top" width="100%"><div id="work" style="background:white"></div></td>
							</tr>
						</table>	
						</td></tr>				
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage"  --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>