<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<% idEcran="IEN0666"; %>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%-- @ taglib uri="/WEB-INF/aitaglib.tld" prefix="ai"--%>
<SCRIPT language="javaScript">
	usrAction = "amal.formule.champformule.lister";
	bFind=true;
</SCRIPT>
<%--
<ct:menuChange displayId="options" menuId="optionsAIAdminFormule"/>
<ct:menuSetAllParams key="idFormule" value="<%=viewBean.getIdFormule()%>" menuId="optionsAIAdminFormule"/>
<ct:menuSetAllParams key="csProvenance" value="<%=globaz.ai.constantes.IConstantes.CS_PRO_ENVOI_FORMULE%>" menuId="optionsAIAdminFormule"/>
--%>
<SCRIPT>
	reloadMenuFrame(top.fr_menu, MENU_TAB_OPTIONS);
</SCRIPT>
<%
	globaz.amal.vb.formule.AMChampformuleViewBean viewBean = (globaz.amal.vb.formule.AMChampformuleViewBean) session.getAttribute("viewBean");

//	globaz.amal.vb.formule.AMFormuleViewBean viewBean = (globaz.amal.vb.formule.AMFormuleViewBean)session.getAttribute("viewBean");
	String actionGroupe = "document.location.href='" + request.getContextPath() + "/amal?userAction=amal.envoi.groupeChamps.chercher&idFormule="+viewBean.getId()+"'";
	String actionNouveau = "document.location.href='" + request.getContextPath() + "/amal?userAction=amal.formule.champformule.afficher&_method=add&idFormule="+request.getParameter("selectedId")+"'";
	String btnGroupeLabel = objSession.getLabel("BUTTON_FORMULE_GROUP");
	//actionNew += "&idFormule="+viewBean.getIdFormule();userAction=amal.formule.champformule.afficher&_method=add
	String idFormule = request.getParameter("selectedId");
	
	if ((idFormule == null) ||(idFormule == "")) {
			idFormule = viewBean.getIdFormuleSearch();
	}
	
	btnFindLabel = objSession.getLabel("RECHERCHER");
	btnNewLabel = objSession.getLabel("NOUVEAU");
	btnExportLabel = objSession.getLabel("EXPORT");
	
%>

<%--
//	bButtonFind = true;
//	btnFindLabel=objSession.getLabel("BUTTON_FORMULE_GROUP");
//	actionFind = "document.location.href='/webai/ai?userAction=ai.envoi.groupeChamps.chercher&selectedId="+viewBean.getIdFormule()+"'";
--%>
<SCRIPT language="javaScript">
	usrAction = "amal.formule.champformule.lister";
	bFind=true;
</SCRIPT>
<%--
<ct:menuChange displayId="options" menuId="optionsAIAdminFormule"/>
<ct:menuSetAllParams key="idFormule" value="<%=viewBean.getIdFormule()%>" menuId="optionsAIAdminFormule"/>
<ct:menuSetAllParams key="csProvenance" value="<%=globaz.ai.constantes.IConstantes.CS_PRO_ENVOI_FORMULE%>" menuId="optionsAIAdminFormule"/>

<SCRIPT>
	reloadMenuFrame(top.fr_menu, MENU_TAB_OPTIONS);
</SCRIPT>--%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_AM_CH_R_TITLE"/> <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<%-- Définition Formule --%>
						<TR>
							<TD width="20%"><ct:FWLabel key="JSP_AM_PARAMETRAGE_R_FORMULE"/></TD>
							<TD width="25%">
								<INPUT type="text" name="libelleDocument" value="<%=viewBean.getLibelleFormule()%>" readonly="readonly" class="libelleLongDisabled">
								<INPUT type="hidden" name="forIdFormule" value="<%=idFormule%>">
								<%--<INPUT type="hidden" name="forIdFormule" value="<%=request.getParameter("selectedId")%>"> --%>
								<INPUT type="hidden" name="_meth" value="loic">
							</TD>
							<TD width="55%" colspan="3">&nbsp;</TD>
						</TR>
						<TR>
							<TD width="20%"><ct:FWLabel key="LIBELLE"/></TD>
							<TD width="25%">
								<INPUT type="text" name="csDocument" value="<%=objSession.getCodeLibelle(viewBean.getNameFormule())%>" readonly="readonly" class="libelleLongDisabled">
							</TD>
							<TD width="55%" colspan="3">&nbsp;</TD>
						</TR>
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%--<INPUT type="button" name="btnGroupe" value="<%=btnGroupeLabel%>" onclick="actionGroupe"> --%>
				<ct:ifhasright element="amal.formule.champformule.afficher" crud="c">
					<INPUT type="button" name="btnGroupe" value="<%=btnNewLabel%>" onclick="<%=actionNouveau%>">
				</ct:ifhasright>
				<SCRIPT language="javascript">
				<%-- rend innaccessible le bouton "rechercher" --%>
				//document.getElementById('btnFind').style.visibility="hidden";
				document.getElementById('btnNew').style.display="none";
				</SCRIPT>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>