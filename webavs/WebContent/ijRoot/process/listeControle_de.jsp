<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="PIJ2001";
	userActionValue="ij.process.listeControle.executer";
	globaz.ij.vb.process.IJListeControleViewBean viewBean = (globaz.ij.vb.process.IJListeControleViewBean)(session.getAttribute("viewBean"));
	
	//On redirige vers le _rc des lots si on n'a rien à faire ici si le lot n'est pas compensé
	

	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	String eMailAddress=objSession.getUserEMail();
	showProcessButton = viewBean.getSession().hasRight(userActionValue, FWSecureConstants.UPDATE);
%>


<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="ij-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="ij-optionsempty"/>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_LISTE_CONTROLE"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><ct:FWLabel key="JSP_ADRESSE_EMAIL"/></TD>
							<TD><INPUT type="text" name="eMailAddress" value="<%=eMailAddress!=null?eMailAddress:""%>"></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_NO_LOT"/></TD>
							<TD><INPUT type="text" name="lot" class="disabled" readonly value="<%=viewBean.getIdLot()%> - <%=viewBean.getDescriptionLot()%>">
								<INPUT type="hidden" name="idLot" value="<%=viewBean.getIdLot()%>">
							</TD>
							
						</TR>						
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> 
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>