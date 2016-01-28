<%--
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
 --%>

<%@page import="globaz.framework.controller.FWController"%>
<%@page import="globaz.globall.db.BSession"%>
<%@page import="ch.globaz.perseus.business.constantes.IPFConstantes"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/capage/header.jspf" %>
<%@page import="globaz.framework.secure.FWSecureConstants"%>

<%-- tpl:insert attribute="zoneInit" --%>
<%
// Les labels de cette page commence par la préfix ""
	idEcran="PPF1412";
	IFrameDetailHeight = "420";
	
	FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	
	if(objSession.hasRight("perseus", FWSecureConstants.ADD)){
		bButtonNew = true;
	}else{
		bButtonNew = false;
	}
%>
<%-- /tpl:insert --%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>

<%@page import="ch.globaz.perseus.business.constantes.IPFActions"%>
<ct:menuChange displayId="menu" menuId="perseus-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="perseus-optionsempty"/>

<SCRIPT language="JavaScript">
	bFind = true;
	detailLink = "<%=actionNew%>";
	usrAction = "<%=IPFActions.ACTION_PARAMETRES_VARIABLE_METIER + ".lister" %>";
</SCRIPT>

<%-- /tpl:insert --%>
<%@ include file="/theme/capage/bodyStart.jspf" %>
				<%-- tpl:insert attribute="zoneTitle" --%><ct:FWLabel key="JSP_PF_PARAM_VARMET_R_TITRE"/><%-- /tpl:insert --%>
<%@ include file="/theme/capage/bodyStart2.jspf" %>
						<%-- tpl:insert attribute="zoneMain" --%>
						
	<TR>
		<TD>
			<TABLE border="0" cellspacing="0" cellpadding="0" width="900">
				<TR>
					<TD><label for="du"><ct:FWLabel key="JSP_PF_PARAM_VARMET_R_DATE_VALABLE"/></label></TD>
					<TD><input type="text" name="variableMetierSearch.forDateValable" value="" data-g-calendar="mandatory:false,type:month"/></TD>
					<TD><label for="du"><ct:FWLabel key="JSP_PF_PARAM_VARMET_R_TYPE_VARIABLE"/></label></TD>
					<TD><ct:FWCodeSelectTag codeType="<%=IPFConstantes.CSGROUP_TYPE_VARIABLES_METIER%>" name="variableMetierSearch.forCsTypeVariableMetier" wantBlank="true" defaut="blank"/></TD>
				</TR>
			</TABLE>
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
