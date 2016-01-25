<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%--
INFO: les labels de cette page sont prefixes avec 'JSP_FIN_R'
--%>
<%
	idEcran = "PIJ0028";
	
	rememberSearchCriterias = true;
	
	globaz.ij.vb.basesindemnisation.IJFormulaireIndemnisationViewBean viewBean = (globaz.ij.vb.basesindemnisation.IJFormulaireIndemnisationViewBean) request.getAttribute("viewBean");
	actionNew =  servletContext + mainServletPath + "?userAction="+globaz.ij.servlet.IIJActions.ACTION_FORMULAIRE_INDEMNISATION+".afficher&_method=add&idIndemnisation=" + viewBean.getIdIndemnisation();
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="ij-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="ij-optionsempty"/>

<SCRIPT language="javascript">
	bFind = true;
	usrAction = "<%=globaz.ij.servlet.IIJActions.ACTION_FORMULAIRE_INDEMNISATION%>.lister";
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_FIN_R_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR><TD><TABLE width="100%">
						<TR>
							<TD><LABEL for="noBaseIndemnisation"><ct:FWLabel key="JSP_FIN_R_NO_BASE_IND"/></LABEL></TD>
							<TD><INPUT type="text" name="noBaseIndemnisation" value="<%=viewBean.getIdIndemnisation()%>" class="numeroCourt disabled" disabled></TD>
							<TD colspan="2">
								<LABEL for="dateDebutBase"><ct:FWLabel key="JSP_FIN_R_DU"/></LABEL>&nbsp;
								<INPUT type="text" class="date disabled" name="dateDebutBase" value="<%=viewBean.getDateDebutBase()%>" disabled>
								<LABEL for="dateFinBase"><ct:FWLabel key="JSP_FIN_R_AU"/></LABEL>&nbsp;
								<INPUT type="text" class="date disabled" name="dateFinBase" value="<%=viewBean.getDateFinBase()%>" disabled>
							</TD>
						</TR>
						<TR>	
							<TD valign="top"><ct:FWLabel key="JSP_FIN_R_PRONONCE"/></TD>
							<TD valign="top" colspan="3">
								<span><%=viewBean.getFullDescriptionPrononce()[0]%><br>
									  <%=viewBean.getDetailRequerant()%></span>
									  
								<INPUT type="hidden" name="forIdBaseIndemnisation" value="<%=viewBean.getIdIndemnisation()%>">
							</TD>							
						</TR>
						</TABLE></TD></TR>
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>				
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>