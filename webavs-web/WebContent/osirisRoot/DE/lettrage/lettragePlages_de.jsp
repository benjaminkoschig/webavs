<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@page import="globaz.osiris.external.IntRole"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>

<%-- tpl:put name="zoneInit"  --%>
<%@page import="globaz.pyxis.db.tiers.TIRole"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="globaz.osiris.db.comptes.*" %>
<%
bButtonCancel = false;
bButtonValidate = false;
bButtonDelete = false;
bButtonUpdate = false;
bButtonNew = false;
idEcran = "GCA70001";
globaz.osiris.vb.lettrage.CALettragePlagesViewBean viewBean = (globaz.osiris.vb.lettrage.CALettragePlagesViewBean)request.getAttribute("viewBean");

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
	document.getElementById('work').innerHTML="Daten werden geladen..."
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
		_handleTimeout(result);
		document.getElementById('work').innerHTML="Laden beendet. "+result
	} else {
		document.getElementById('work').innerHTML="Ein Fehler ist aufgetreten: "+req1.status
	}
}
function _handleTimeout(res) {
	if (res.indexOf("WebAVS - Accueil")>0) {
		top.location.href='<%=request.getContextPath()%>/pyxis'
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
			<%-- tpl:put name="zoneTitle" --%>Beschriftungsbereich<%-- /tpl:put  --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%>
						<tr><td>
						<table border="1" >
							<tr>
								<td colspan=2>
									<div id="search" >
										<input type="text" id="nbPlages">
										<select name="role" id="role" tabindex="2">
										<% List <String>roleToExclude = new ArrayList<String>();
										roleToExclude.add(IntRole.ROLE_ADMINISTRATEUR);
										%>
              								<%=CARoleViewBean.createOptionsTagsExcludeRole(objSession, request.getParameter("role"), true, roleToExclude)%>
             							 </select>

										<input type="button" id="btSearch" value="Bereiche festsetzen"
											onclick="getPlages('<%=request.getContextPath()%>/osiris?userAction=osiris.lettrage.plages.listerPlages&nbPlages='+document.getElementById('nbPlages').value+'&role='+document.getElementById('role').value)">
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