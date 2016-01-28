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
globaz.pyxis.vb.adressepaiement.TISummaryViewBean viewBean = (globaz.pyxis.vb.adressepaiement.TISummaryViewBean)session.getAttribute(globaz.framework.servlets.FWServlet.VIEWBEAN);
idEcran = "GTI5001";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness"  --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>

<style>
	.module {
		background-color:white;
		display:inline-block;
		vertical-align:top;
		
		background-image : url('images/summary_bg.png');
		background-repeat: repeat-x; 
		border : solid 1px silver; 
		padding : 0.1cm 0.1cm 0.1cm 0.1cm ;
		margin : 2px 2px 2px 2px;
		
	}
	
	.title1 {
		padding : 0.1cm  0.1cm 0.1cm  0.1cm ;
		margin : 1px 1px 2px 2px;
		font-size : medium;
	}
	
</style>
<SCRIPT language="JavaScript">




top.document.title = "Tiers"
<!--hide this script from non-javascript-enabled browsers
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
			<%-- tpl:put name="zoneTitle" --%>Vue des adresses de paiement<%-- /tpl:put  --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%>
					
					<tr><td width="100%">
					Test
					</td></tr>
					
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage"  --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <SCRIPT>
		</SCRIPT> <%} %>
		
		<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>