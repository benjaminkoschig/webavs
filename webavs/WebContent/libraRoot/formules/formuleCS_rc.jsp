<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<% idEcran="GLI0023"; %>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<SCRIPT language="javaScript">
	usrAction = "<%=globaz.libra.servlet.ILIActions.ACTION_FORMULES_RC_CS%>.lister";
	bFind=true;
</SCRIPT>
<%
bButtonNew=false;

%>
<ct:menuChange displayId="menu" menuId="li-menuprincipal" showTab="options"/>
<ct:menuChange displayId="options" menuId="libra-optionsformules"/>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="ECRAN_RC_SEL_FORM_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD>
								<TABLE border="0" cellpadding="0" cellspacing="0" width="500px">

									<%-- Définition formule --%>
									<TR>
										<TD width="20%"><ct:FWLabel key="ECRAN_RCL_FORM_LIBELLE"/></TD>
										<TD width="25%">
											<INPUT type="text" name="fromCsDocumentValue">
											<%
												String mode = null;
												mode=request.getParameter("mode");
												if(mode!= null && mode.trim().length()>0){
											%>
													<input type="hidden" name="mode" value="<%=mode%>"/>
											<%	
												}
											%>
										</TD>
										<TD width="55%" colspan="3">&nbsp;</TD>
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