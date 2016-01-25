<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
rememberSearchCriterias = true;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@page import="globaz.tucana.db.bouclement.TUBouclementListViewBean"%>
<%@page import="globaz.tucana.application.TUApplication"%>
<%@page import="globaz.tucana.db.bouclement.access.TUBouclementManager"%>
<%@page import="globaz.tucana.db.bouclement.TUBouclementViewBean"%><style type="text/css">
	#subtable {
		width:100%;
	}
</style>
<SCRIPT language="javaScript">
<%
rememberSearchCriterias = true;
//	actionNew += "&provenanceId=" + request.getParameter("provenanceId");
//	actionNew += "&provenanceType=" + request.getParameter("provenanceType");
//	actionNew += "&vueAffiche=" + sVueAffiche;
//	bButtonNew = false;
idEcran = "TU-100";
subTableHeight=0;
%>

<% TUBouclementListViewBean viewBean = (TUBouclementListViewBean) request.getAttribute("viewBean");
String defaultAgence = globaz.globall.api.GlobazSystem.getApplication(TUApplication.DEFAULT_APPLICATION_TUCANA).getProperty(TUApplication.CS_AGENCE);
%>


usrAction = "tucana.bouclement.bouclement.lister";
bFind = true;
</SCRIPT>


<ct:menuChange displayId="options" menuId="OLTUBouclement"/>
<ct:menuChange displayId="menu" menuId="menuWebTucana" showTab="menu"/>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="TIT_LISTE_BOUCLEMENT" /><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD width="100%" colspan="5">&nbsp;
								<INPUT type="hidden" id="order" name="order" value="<%=TUBouclementManager.TRI_ANNEE_MOIS%>">
							</TD>
						</TR>

						<TR >
							<TD width="20%"><ct:FWLabel key="DATE_CREATION"/>&nbsp;</TD>
							<TD width="25%"><ct:FWCalendarTag name="forDateCreation" value="" doClientValidation="CALENDAR,NOT_EMPTY"/>&nbsp;</TD>
							<TD width="10%">&nbsp;</TD>
							<TD width="20%"><ct:FWLabel key="ID_BOUCLEMENT"/>&nbsp;</TD>
							<TD width="25%"><INPUT type="text" id="forIdBouclement" name="forIdBouclement" class="numeroId">&nbsp;</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="MOIS_ANNEE"/>&nbsp;</TD>
							<TD><INPUT type="text" id="forMoisComptable" name="forMoisComptable" class="numeroCourt">&nbsp;/&nbsp;
							<INPUT type="text" id="forAnneeComptable" name="forAnneeComptable" class="numeroCourt">&nbsp;
							</TD>
							<TD>&nbsp;</TD>
							<TD><ct:FWLabel key="ID_IMPORTATION"/>&nbsp;</TD>
							<TD><INPUT type="text" id="forIdImportation" name="forIdImportation" class="numeroId">&nbsp;</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="NO_AF"/>&nbsp;</TD>
							<TD><INPUT type="text" id="forNoAF" name="forNoAF" class="numeroCourt">&nbsp;</TD>
							<TD>&nbsp;</TD>
							<TD><ct:FWLabel key="NO_CA"/>&nbsp;</TD>
							<TD><INPUT type="text" id="forNoCA" name="forNoCA" class="numeroId">&nbsp;</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="NO_ACM"/>&nbsp;</TD>
							<TD><INPUT type="text" id="forNoACM" name="forNoACM" class="numeroCourt">&nbsp;</TD>
							<TD>&nbsp;</TD>
							<TD><ct:FWLabel key="NO_CG"/>&nbsp;</TD>
							<TD><INPUT type="text" id="forNoCG" name="forNoCG" class="numeroId">&nbsp;</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="ETAT_BOUCLEMENT"/>&nbsp;</TD>
							<%-- <TD><ct:FWCodeSelectTag name="forCsEtat" wantBlank="<%=true%>" codeType="TU_ETAT" defaut="<%=ITUCSConstantes.CS_ETAT_ENCOURS%>"/>--%>
							<TD><ct:FWCodeSelectTag name="forCsEtat" wantBlank="<%=true%>" codeType="TU_ETAT" defaut=""/>
							&nbsp;</TD>
							<TD>&nbsp;</TD>
							<TD width="20%">
								<ct:FWLabel key="AGENCE" />
							</TD>
							<%--<TD width="80%" style="font-weight: bold;"><%=objSession.getCodeLibelle(globaz.globall.api.GlobazSystem.getApplication(globaz.tucana.application.TUApplication.DEFAULT_APPLICATION_TUCANA).getProperty(globaz.tucana.application.TUApplication.CS_AGENCE))%>--%>
							<TD width="80%" style="font-weight: bold;">

								<ct:select id="forCsAgence" name="forCsAgence" wantBlank="true" defaultValue="<%=defaultAgence%>">
									<ct:optionsCodesSystems csFamille="TU_AGENCE" />
								</ct:select>
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