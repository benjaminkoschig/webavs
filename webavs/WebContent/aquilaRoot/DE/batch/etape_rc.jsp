<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ page import="globaz.aquila.db.batch.COSequenceListViewBean"%>
<%@ page import="globaz.aquila.db.batch.COSequenceViewBean"%>
<%
	COSequenceListViewBean listViewBean = (COSequenceListViewBean) request.getAttribute("viewBean");
	rememberSearchCriterias = true;
	bButtonNew = false;
	idEcran = "GCO0020";
%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/aquilaRoot/javascript/aquila.js"></SCRIPT>
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/aquilaRoot/theme/aquila.css">
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CO-MenuPrincipal"/>

<SCRIPT language="JavaScript">
	usrAction = "aquila.batch.etape.lister";
	bFind = true;	
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Suche einer Etappe<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR><TD>
						<TABLE>
							<TR>
								<TD>Sequenz</TD>
								<TD>
									<ct:select name="forIdSequence">
										<ct:forEach items="<%=listViewBean.getContainer()%>" var="sequence">
											<% COSequenceViewBean sequence = (COSequenceViewBean) pageContext.getAttribute("sequence"); %>
											<OPTION value="<%=sequence.getIdSequence()%>"><%=sequence.getLibSequenceLibelle()%></OPTION>
										</ct:forEach>
									</ct:select>

									<INPUT type="hidden" name="orderByLibEtapeCSOrder" value="true">
								</TD>
								<TD>Etappe</TD>
								<TD>
									<ct:select name="forLibAction" wantBlank="true">
										<ct:optionsCodesSystems csFamille="COETAEP"/>
									</ct:select>
								</TD>
							</TR>
						</TABLE>
						</TD></TR>
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>