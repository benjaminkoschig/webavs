<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="PRE3029";
	

	showProcessButton = false;

	userActionValue="corvus.process.anakin.executer";
	
	REAnakinViewBean viewBean = (REAnakinViewBean)(session.getAttribute("viewBean"));
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	String eMailAddress=objSession.getUserEMail();
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@page import="globaz.corvus.vb.process.REAnakinViewBean"%><SCRIPT>



function actionActiver(){	
	document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_ANAKIN_VALIDATOR%>.executer"
	document.forms[0].elements('traitement').value="<%=REAnakinViewBean.TRAITEMENT_ACTIVER_ANAKIN_VALIDATOR%>";
	document.forms[0].submit();
}

function actionDesactiver(){	
	document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_ANAKIN_VALIDATOR%>.executer"
	document.forms[0].elements('traitement').value="<%=REAnakinViewBean.TRAITEMENT_DESACTIVER_ANAKIN_VALIDATOR%>";
	document.forms[0].submit();
}


    
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>		
<%@page import="globaz.corvus.vb.process.REExecuterAvancesViewBean"%>

<%@page import="globaz.corvus.utils.REPmtMensuel"%><ct:FWLabel key="JSP_ANAKIN_TITLE"/> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
						<TD align="left" width="40%">
						
							<TABLE border="0">
							<TR>
								<TD>
								<INPUT type="hidden" name="traitement" value="">
								<ct:FWLabel key="JSP_ANAKIN_STATUS"/>
								</TD>
								
								<TD align="left" width="10%">
									<%if (REAnakinViewBean.isAnakinValidatorActif(objSession)) {%>
										<img src="<%=request.getContextPath()+"/images/ok.gif"%>">
									<%} else {%>
										<img src="<%=request.getContextPath()+"/images/erreur.gif"%>">
									<%} %>
								</TD>
								<TD/>
		
							</TR>						
							</TABLE>
					</TD>						
					<TD/>
					</TR>
						<TR><TD><br/></TD></TR>
					<TR>
						<TD align="left">
							<%if (REAnakinViewBean.isAnakinValidatorActif(objSession)) {%>
								<INPUT type="button" name="bt" value="<ct:FWLabel key="JSP_ANAKIN_DESACTIVER"/>" onclick="actionDesactiver();">	
							<%} else {%>
								<INPUT type="button" name="bt" value="<ct:FWLabel key="JSP_ANAKIN_ACTIVER"/>" onclick="actionActiver();">
							<%} %>
						</TD>
						<TD align="left">
						</TD>
						<TD/>
					</TR>
						
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>