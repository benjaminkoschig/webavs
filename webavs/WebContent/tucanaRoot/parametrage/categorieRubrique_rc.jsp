<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<style type="text/css">
	#subtable {
		width:100%;
	}
</style>
<SCRIPT language="javaScript">
<%
//	actionNew += "&provenanceId=" + request.getParameter("provenanceId");
//	actionNew += "&provenanceType=" + request.getParameter("provenanceType");
//	actionNew += "&vueAffiche=" + sVueAffiche;
//	bButtonNew = false;
idEcran = "TU-402";
subTableHeight=0;
%>

<% 

globaz.tucana.db.parametrage.TUGroupeCategorieViewBean viewBean = (globaz.tucana.db.parametrage.TUGroupeCategorieViewBean) session.getAttribute("viewBean");
actionNew += "&idGroupeCategorie=" + viewBean.getIdGroupeCategorie();
%>



usrAction = "tucana.parametrage.categorieRubrique.lister";
bFind = true;
</SCRIPT>
<%
	String idGroupeCategorieLink = viewBean.getIdGroupeCategorie();
%>

<ct:menuChange displayId="options" menuId="OLTUCategorieRubrique" showTab="options">
	<ct:menuSetAllParams key="idGroupeCategorie" value="<%=viewBean.getIdGroupeCategorie()%>"/>
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="TIT_LISTE_CATEGORIE_RUBRIQUE" /><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD width="100%" colspan="5">&nbsp;</TD>
						</TR>
						<TR>
							<TD width="20%"><INPUT type="hidden" name="forIdGroupeRubrique" value="<%=viewBean.getIdGroupeCategorie()%>">
							<INPUT type="hidden" name="idGroupeRubrique" value="<%=viewBean.getIdGroupeCategorie()%>">
							</TD>
							<TD width="25%"></TD>
							<TD width="10%"></TD>
							<TD width="20%"></TD>
							<TD width="25%"></TD>
						</TR>

						<TR>
							<TD><ct:FWLabel key="GROUPE_RUBRIQUE"/>&nbsp;</TD>
							<TD colspan="4"><INPUT type="text" value="<%=viewBean.getSession().getCodeLibelle(viewBean.getCsGroupeRubrique())%>" readonly="true" style="width:75%;"></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="CATEGORIE"/>&nbsp;</TD>
							<TD colspan="4"><INPUT type="text" value="<%=viewBean.getSession().getCodeLibelle(viewBean.getCsCategorie())%>" readonly="true" style="width:75%;"></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="TYPE"/>&nbsp;</TD>
							<TD colspan="4"><INPUT type="text" value="<%=viewBean.getSession().getCodeLibelle(viewBean.getCsType())%>" readonly="true" style="width:75%;"></TD>
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