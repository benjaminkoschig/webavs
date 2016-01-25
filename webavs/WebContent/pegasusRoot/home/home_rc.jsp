<%-- tpl:insert page="/theme/find.jtpl" --%><%@page import="globaz.prestation.interfaces.tiers.PRTiersHelper"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="PPC0102";
	//Les labels de cette page commence par le préfix "JSP_PC_PARAM_HOMES_R"

	
 	rememberSearchCriterias = true;

	actionNew =  servletContext + mainServletPath + "?userAction=" + IPCActions.ACTION_HOME + ".afficher&_method=add";
	
%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>


<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<ct:menuChange displayId="menu" menuId="pegasus-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="pegasus-optionsempty"/>

<SCRIPT language="JavaScript">
	bFind = true;
	usrAction = "<%=IPCActions.ACTION_HOME%>.lister";
</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_PC_PARAM_HOMES_R_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD>
								<TABLE border="0" cellspacing="0" cellpadding="0" width="100%">
									<TR>
										<TD><LABEL for="du"><ct:FWLabel key="JSP_PC_PARAM_HOMES_R_NOM_BATIMENT"/></LABEL></TD>
										<TD>
											<INPUT type="text" name="homeSearch.likeDesignation" value="">
											<%-- On ne veut que les adresses de domicile --%>
											<INPUT type="hidden" name="homeSearch.forTypeAdresse" value="<%=PRTiersHelper.CS_ADRESSE_DOMICILE%>">
										</TD>
										<TD><LABEL for="au"><ct:FWLabel key="JSP_PC_PARAM_HOMES_R_NUMERO_IDENTIFICATION"/></LABEL></TD>
										<TD><INPUT type="text" name="homeSearch.likeNumeroIdentification" value=""></TD>
									</TR>
									<TR>
										<TD><LABEL for="au"><ct:FWLabel key="JSP_PC_PARAM_HOMES_R_NPA_OR_LOCALITE"/></LABEL></TD>
										<TD><INPUT type="text" name="homeSearch.likeNpaOrLocalite" value=""></TD>				
										<TD><LABEL for="au"><ct:FWLabel key="JSP_PC_PARAM_HOMES_R_CANTON"/></LABEL></TD>
										<TD><ct:FWCodeSelectTag codeType="PYCANTON" name="homeSearch.forCanton" defaut="blank" wantBlank="true"/></TD>										
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