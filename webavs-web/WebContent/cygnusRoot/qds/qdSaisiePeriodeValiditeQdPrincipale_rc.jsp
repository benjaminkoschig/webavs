<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/capage/header.jspf" %>
<%@page import="globaz.cygnus.servlet.IRFActions"%>
<%@page import="globaz.cygnus.application.RFApplication"%>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="PRF0057";
	//Les labels de cette page commence par le préfix "JSP_RF_S_PERIODE_VALIDITE_R_QD_"
	
	actionNew =  servletContext + mainServletPath + "?userAction=" + IRFActions.ACTION_SAISIE_QD_PERIODE_VALIDITE_QD_PRINCIPALE + 
	             ".afficher&_method=add&idQd="+request.getParameter("idQd")+"&idTiers="+request.getParameter("idTiers")+"&anneeQd="+
	             request.getParameter("anneeQd");
	
	String retourDetailQD =   servletContext + "/" + RFApplication.CYGNUS_RELATIVE_URL + 
							  IRFActions.ACTION_SAISIE_QD_PIED_DE_PAGE + 
	                          ".afficher&selectedId="+request.getParameter("idQd")+"&csGenreQd="+request.getParameter("csGenreQdHistorique");
%>

<%-- /tpl:put --%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<SCRIPT language="JavaScript">
	bFind = true;
	detailLink = "<%=actionNew%>";
	usrAction = "<%=IRFActions.ACTION_SAISIE_QD_PERIODE_VALIDITE_QD_PRINCIPALE%>.lister";
	
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_RF_S_PERIODE_VALIDITE_S_QD_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><ct:FWLabel key="JSP_RF_S_PERIODE_VALIDITE_R_QD_NO"/></TD>
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
								<input type="hidden" name="idTiers" value="<%=request.getParameter("idTiers")%>" />
								<input type="hidden" name="anneeQd" value="<%=request.getParameter("anneeQd")%>"/>
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