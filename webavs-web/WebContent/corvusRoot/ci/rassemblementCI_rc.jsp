<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/corvusRoot/corvustaglib.tld" prefix="re" %>
<%
	// Les labels de cette page commence par la préfix "JSP_RCI_R"

	idEcran="PRE0042";

	globaz.corvus.vb.ci.RERassemblementCIViewBean viewBean = (globaz.corvus.vb.ci.RERassemblementCIViewBean) request.getAttribute("viewBean");

	String forIdTiers = request.getParameter("forIdTiers");

	actionNew =  servletContext + mainServletPath + "?userAction=" + globaz.corvus.servlet.IREActions.ACTION_RASSEMBLEMENT_CI +
				 ".afficher&_method=add&forIdTiers="+forIdTiers+"&idTiers=" +forIdTiers;
	
	bButtonNew = controller.getSession().hasRight(IREActions.ACTION_RASSEMBLEMENT_CI, FWSecureConstants.ADD);
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.corvus.db.ci.RERassemblementCI"%>
<%@page import="globaz.corvus.servlet.IREActions"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="corvus-optionsempty"/>

<SCRIPT language="javascript">
	bFind = true;
	usrAction = "corvus.ci.rassemblementCI.lister";
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_RCI_R_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD>
								<TABLE border="0" cellspacing="0" cellpadding="0" width="100%">
									<TR>
										<TD><LABEL for="requerantDescription"><ct:FWLabel key="JSP_RCI_R_REQUERANT"/></LABEL></TD>
										<TD colspan="3">
											<re:PRDisplayRequerantInfoTag
													session="<%=(globaz.globall.db.BSession)controller.getSession()%>"
													idTiers="<%=forIdTiers%>"
													style="<%=globaz.prestation.jsp.taglib.PRDisplayRequerantInfoTag.STYLE_CONDENSED_WITHOUT_LABEL%>"/>
											<INPUT type="hidden" name="forIdTiers" value="<%=forIdTiers%>">
											<INPUT type="hidden" name="idTiers" value="<%=forIdTiers%>">
											<INPUT type="hidden" name="orderBy" value="<%=RERassemblementCI.FIELDNAME_DATE_RASSEMBLEMENT%> DESC">
										</TD>
									</TR>
									<TR>
										<TD><LABEL for="forCsEtat"><ct:FWLabel key="JSP_RCI_R_ETAT"/></LABEL>&nbsp;</TD>
										<TD><ct:FWCodeSelectTag codeType="<%=globaz.corvus.api.ci.IRERassemblementCI.CS_GROUPE_ETAT_RASSEMBLEMENT%>" name="forCsEtat" wantBlank="true" defaut=""/></TD>
									</TR>
								</TABLE>
							</TD>
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