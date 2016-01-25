<%-- tpl:insert page="/theme/process.jtpl" --%><%@page import="globaz.corvus.db.ci.TypeListeCiAdditionnels"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	// Les labels de cette page commence par la préfix "JSP_LCI_D"

	idEcran="PRE2056";
	
	REGenererListeCiAdditionnelsViewBean viewBean = (REGenererListeCiAdditionnelsViewBean)session.getAttribute("viewBean");
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	
	userActionValue=globaz.corvus.servlet.IREActions.ACTION_GENERER_LISTE_CI_ADDITIONNELS + ".executer";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.corvus.vb.process.REGenererListeCiAdditionnelsViewBean"%>
<ct:menuChange displayId="menu" menuId="corvus-menuprincipal"/>
<ct:menuChange displayId="options" menuId="corvus-optionsempty" showTab="menu">
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_LCI_D_TITRE"/> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><ct:FWLabel key="JSP_LCI_D_GENRE_CI"/></TD>
							<TD>
							   	<select name="genreCiAdd">
									<option value="<%=TypeListeCiAdditionnels.RECEPTIONNES.name()%>" selected="selected">
										<ct:FWLabel key="JSP_LCI_D_RECEPTIONNE" />
									</option>
									<option value="<%=TypeListeCiAdditionnels.TRAITES.name()%>">
										<ct:FWLabel key="JSP_LCI_D_TRAITE" />
									</option>
									<option value="<%=TypeListeCiAdditionnels.NON_TRAITES.name()%>">
										<ct:FWLabel key="JSP_LCI_D_NON_TRAITE" />
									</option>								
									<option value="<%=TypeListeCiAdditionnels.ATTENTE_CI_ADD_TOUS.name()%>">
										<ct:FWLabel key="JSP_LCI_D_ATTENTE_CI_ADD_TOUS" />
									</option>
									<option value="<%=TypeListeCiAdditionnels.ATTENTE_CI_ADD_PROVISOIRE.name()%>">
										<ct:FWLabel key="JSP_LCI_D_ATTENTE_CI_ADD_PROVISOIRE" />
									</option>
									<option value="<%=TypeListeCiAdditionnels.ATTENTE_CI_ADD_TRAITE.name()%>">
										<ct:FWLabel key="JSP_LCI_D_ATTENTE_CI_ADD_TRAITE" />
									</option>
								</select>
									
							</TD>
						</TR>
						<TR>
							<TD><LABEL for="dateDebut"><ct:FWLabel key="JSP_LCI_D_DE"/></LABEL></TD>
							<TD>
								<input	id="dateDebut"
										name="dateDebut"
										data-g-calendar="type:default"
										value="<%=viewBean.getDateDebut()%>" />
								<LABEL for="dateFin">
									<ct:FWLabel key="JSP_LCI_D_A"/>
								</LABEL>
								<input	id="dateFin"
										name="dateFin"
										data-g-calendar="type:default"
										value="<%=viewBean.getDateFin()%>" />
							</TD>			
						</TR>
						<TR>
							<TD><LABEL for="eMailAddress"><ct:FWLabel key="JSP_LCI_D_EMAIL"/></LABEL></TD>
							<TD><INPUT type="text" name="eMailAddress" value="<%=globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getEMailAddress())?controller.getSession().getUserEMail():viewBean.getEMailAddress()%>" class="libelleLong"></TD>
						</TR>						
		
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>