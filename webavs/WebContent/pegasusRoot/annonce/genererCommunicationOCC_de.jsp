<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@page import="ch.globaz.pegasus.business.models.annonce.CommunicationOCC"%>
<%@page import="globaz.pegasus.vb.annonce.PCGenererCommunicationOCCViewBean"%>
<%@page import="java.util.Iterator"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%-- tpl:put name="zoneInit" --%>
<%
	// Les labels de cette page commence par la préfix "JSP_ANNONCE_COMMUNICATION_OCC"

	idEcran="PPC3002";
	
	PCGenererCommunicationOCCViewBean viewBean = (PCGenererCommunicationOCCViewBean)session.getAttribute("viewBean");
	
	String rootPath = servletContext+(mainServletPath+"Root");
	
/*	String idLot = request.getParameter("idLot");
	if(JadeStringUtil.isEmpty(idLot)){
		idLot = request.getParameter("selectedId");
	}*/
%>
<%-- /tpl:put --%>

<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>


<%@ include file="/theme/detail/javascripts.jspf" %>
<SCRIPT language="javascript">
var userAction = "<%=IPCActions.ACTION_ANNONCE_COMMUNICATION_OCC_GENERER %>.executer";

$(function(){
	$("#btnCtrlJade").hide();
	
	$("#printCommBtn").click(function(){
		$("[name=userAction]").val(userAction);
		$("[name=mainForm]").submit();
	});
});

function init(){}
function add(){}
function upd(){}
function del(){}

function readOnly(flag) {
 	// empeche la propriete disabled des elements etant de la classe css 'forceDisable' d'etre modifiee
	$('input,select',document.forms[0]).each(function(){
       if (!this.readOnly && 
       	this.className != 'forceDisable' &&
       	!$(this).hasClass('ignoreReadOnly') &&
       	this.type != 'hidden') {
           //this.disabled = flag;
       }
 	});
}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_ANNONCE_COMMUNICATION_OCC_D_TITRE"/> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><LABEL for="eMailAddress"><ct:FWLabel key="JSP_ANNONCE_COMMUNICATION_OCC_D_EMAIL"/></LABEL></TD>
							<TD><INPUT type="text" name="mailAddress" value="<%=controller.getSession().getUserEMail()%>" class="libelleLong" /></TD>
						</TR>
						<tr> 						
							<TD><LABEL for="eMailAddress"><ct:FWLabel key="JSP_ANNONCE_COMMUNICATION_OCC_D_SEMAINE"/></LABEL></TD>
							<td><input type="text" name="noSemaine" /></td>
							<TD><LABEL for="eMailAddress"><ct:FWLabel key="JSP_ANNONCE_COMMUNICATION_OCC_D_ANNEE"/></LABEL></TD>
							<td><input type="text" name="annee" /></td>
						</TR> 						
						<tr>
							<td colspan="2" align="right">
								<button class="ignoreReadOnly" id="printCommBtn"><ct:FWLabel key="JSP_ANNONCE_COMMUNICATION_OCC_D_BTN_GENERER"/></button>
							</td>
						</tr>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>