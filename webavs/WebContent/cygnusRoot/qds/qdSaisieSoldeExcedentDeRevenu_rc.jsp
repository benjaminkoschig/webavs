<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/capage/header.jspf" %>
<%@page import="globaz.cygnus.servlet.IRFActions"%>
<%@page import="globaz.cygnus.application.RFApplication"%>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="PRF0044";
	//Les labels de cette page commence par le préfix "JSP_RF_S_SOEXR_R"
	
	
	actionNew =  servletContext + mainServletPath + "?userAction=" + IRFActions.ACTION_SAISIE_QD_SOLDE_EXCEDENT_DE_REVENU + 
	             ".afficher&_method=add&idQd="+request.getParameter("idQd");
	
	String retourDetailQD =   servletContext + "/" + RFApplication.CYGNUS_RELATIVE_URL + IRFActions.ACTION_SAISIE_QD_PIED_DE_PAGE + 
	                          ".afficher&selectedId="+request.getParameter("idQd")+"&csGenreQd="+request.getParameter("csGenreQdHistorique");
%>

<%-- /tpl:put --%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<SCRIPT language="JavaScript">
	bFind = true;
	detailLink = "<%=actionNew%>";
	usrAction = "<%=IRFActions.ACTION_SAISIE_QD_SOLDE_EXCEDENT_DE_REVENU%>.lister";
	
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_RF_S_SOEXR_R_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><ct:FWLabel key="JSP_RF_S_SOEXR_R_QD_NO"/></TD>
							<TD>
								<span>
									<b>
										<a id="retourQd"  href="<%=retourDetailQD%>" style="text-decoration: none;">
											&nbsp;<%= request.getParameter("idQd")%>
										</a>
									</b>
								</span>
								<input type="hidden" name="forIdQd" value="<%=request.getParameter("idQd")%>" />
								<input type="hidden" name="forDerniereVersion" value="true" />
							</TD>
						</TR>
	 					<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyClose.jspf" %>
<%-- /tpl:insert --%>