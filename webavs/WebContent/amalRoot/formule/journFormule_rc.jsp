<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%-- <%@ taglib uri="/WEB-INF/aitaglib.tld" prefix="ai" %> --%>
<%
	globaz.amal.vb.formule.AMFormuleViewBean viewBean = (globaz.amal.vb.formule.AMFormuleViewBean) session.getAttribute("viewBean");
	bButtonNew=false;
	String selectedId = request.getParameter("selectedId");
	btnFindLabel = objSession.getLabel("RECHERCHER");
	btnNewLabel = objSession.getLabel("NOUVEAU");
	btnExportLabel = objSession.getLabel("EXPORT");

%>
<% idEcran="IEN0310"; %>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<script language="JavaScript">
	bFind=true;
	usrAction = "amal.formule.journFormule.lister";
</script>
<%--<ct:menuChange displayId="options" menuId="optionsAIAdminFormule"/>
<ct:menuSetAllParams key="idFormule" value="<%=viewBean.getIdFormule()%>" menuId="optionsAIAdminFormule"/>
<ct:menuSetAllParams key="csProvenance" value="<%=globaz.ai.constantes.IConstantes.CS_PRO_ENVOI_FORMULE%>" menuId="optionsAIAdminFormule"/> --%>
<SCRIPT>
	reloadMenuFrame(top.fr_menu, MENU_TAB_OPTIONS);
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>
					<ai:AIProperty key="JOURNALISATION_IMPORTATIONS_FORMULES"/>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD width="20%"><ct:FWLabel key="JSP_AM_PARAMETRAGE_R_FORMULE"/></TD>
							<TD width="25%">
								<INPUT type="text" name="libelleDocument" value="<%=viewBean.getFormule().getLibelleDocument()%>" readonly="readonly" class="libelleLongDisabled">
							</TD>
							<TD width="55%" colspan="3">&nbsp;</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_AM_PARAM_LIB"/></TD>
							<TD>
								<input type="hidden" name="historiqueImportationSearch.forProvenance" value="<%="-"+viewBean.getId()%>" />
								<INPUT type="text" name="csDocument" value="<%=objSession.getCodeLibelle(viewBean.getDefinitionFormule().getCsDocument())%>" readonly="readonly" class="libelleLongDisabled">
							</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_AM_PARAMETRAGE_R_LANGUE"/></TD>
							<TD>
								<INPUT type="text" name="csLangue" value="<%=objSession.getCodeLibelle(viewBean.getFormule().getCsLangue())%>" readonly="readonly" class="libelleLongDisabled">
							</TD>
						</TR>
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<ct:menuChange displayId="options" menuId="amal-optionsformules"/>
<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getId()%>" menuId="amal-optionsformules"/>

<%--<%}%> --%>
<SCRIPT language="javascript">
//if (document.forms[0].elements('_method').value != "add") {
reloadMenuFrame(top.fr_menu, MENU_TAB_OPTIONS);
//}
</SCRIPT>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>