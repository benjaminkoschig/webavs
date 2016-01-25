<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%-- 

INFO!!!!
les labels de cette page sont prefixes avec 'LABEL_IJC_R'

--%>
<%
idEcran="PIJ0011";

rememberSearchCriterias = true;

globaz.ij.vb.prestations.IJIJCalculeeJointIndemniteListViewBean viewBean = (globaz.ij.vb.prestations.IJIJCalculeeJointIndemniteListViewBean) request.getAttribute("viewBean");

bButtonNew = false;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="ij-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="ij-optionsempty"/>

<SCRIPT language="javascript">
	bFind = true;
	usrAction = "<%=globaz.ij.servlet.IIJActions.ACTION_IJ_CALCULEES%>.lister";
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_IJC_R_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR><TD>
						<TABLE width="100%">
							<TR>
								<TD>
									<B><ct:FWLabel key="JSP_DETAIL_REQUERANT"/></B>
									
									<!-- les champs de recherche -->
									<INPUT type="hidden" name="forIdPrononce" value="<%=viewBean.getForIdPrononce()%>">
									<INPUT type="hidden" name="csTypeIJ" value="<%=viewBean.getCsTypeIJ()%>">
								</TD>
								<TD colspan="3">
									<INPUT type="text" value="<%=viewBean.getDetailRequerantDetail()%>" size="100" class="disabled" readonly>
								</TD>
							</TR>
							<TR>
								<TD><LABEL for="noPrononce"><ct:FWLabel key="JSP_NO_PRONONCE"/></LABEL></TD>
								<TD><INPUT type="text" name="noPrononce" value="<%=viewBean.getForIdPrononce()%>" class="numeroDisabled" readonly>&nbsp;
								<LABEL for="datePrononce"><ct:FWLabel key="JSP_DATE_PRONONCE"/></LABEL>
								<INPUT type="text" name="datePrononce" value="<%=viewBean.getDatePrononce()%>" class="dateDisabled" readonly></TD>
							</TR>
							<TR>
								<TD colspan="4">
									<A href="<%=servletContext + mainServletPath + "?userAction=" + globaz.ij.servlet.IIJActions.ACTION_BASE_INDEMNISATION + ".chercher&idPrononce=" + viewBean.getForIdPrononce()+"&csTypeIJ="+viewBean.getCsTypeIJ() + "&idTiers=" + viewBean.getIdTiers()%> ">
										<ct:FWLabel key="JSP_BASES_INDEMNISATION"/>
									</A>
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