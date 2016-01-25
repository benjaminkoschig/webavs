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
idEcran = "TU-102";
subTableHeight=0;
%>

<% 
    globaz.tucana.db.bouclement.TUBouclementViewBean viewBean = (globaz.tucana.db.bouclement.TUBouclementViewBean) session.getAttribute("viewBean");
	actionNew += "&idBouclement=" + viewBean.getIdBouclement();
%>



usrAction = "tucana.bouclement.detail.lister";
bFind = true;
</SCRIPT>
<%
	String idBouclementLink = viewBean.getIdBouclement();
	if (!viewBean.getCsEtat().equals(globaz.tucana.constantes.ITUCSConstantes.CS_ETAT_ENCOURS)){
		bButtonNew = false;
	}
%>
<ct:menuChange displayId="options" menuId="OLTUDetail" showTab="options">
	<ct:menuSetAllParams key="idBouclement" value="<%=viewBean.getIdBouclement()%>"/>
</ct:menuChange>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="TIT_LISTE_DETAIL" /><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD width="100%" colspan="5" align="center" style="font-weight: bold;">
								<%--<%=objSession.getCodeLibelle(globaz.globall.api.GlobazSystem.getApplication(globaz.tucana.application.TUApplication.DEFAULT_APPLICATION_TUCANA).getProperty(globaz.tucana.application.TUApplication.CS_AGENCE))%>&nbsp;--%>
								<%=objSession.getCodeLibelle(viewBean.getCsAgence())%>
							</TD>
						</TR>
						<TR >
							<TD width="15%"><ct:FWLabel key="ID_BOUCLEMENT"/>&nbsp;</TD>
							<TD width="30%" style="font-weight: bold;">
								<%=viewBean.getIdBouclement()%>
								<INPUT type="hidden" name="forIdBouclement" value="<%=viewBean.getIdBouclement()%>">
								<INPUT type="hidden" name="idBouclement" value="<%=viewBean.getIdBouclement()%>">
							</TD>
							<TD width="10%">&nbsp;</TD>
							<TD width="15%">&nbsp;</TD>
							<TD width="23%">&nbsp;</TD>
						</TR>
						<TR >
							<TD width="15%"><ct:FWLabel key="MOIS_ANNEE"/>&nbsp;</TD>
							<TD width="30%" style="font-weight: bold;">
							<%=viewBean.getMoisComptable()%>
							&nbsp;/&nbsp;
							<%=viewBean.getAnneeComptable()%>
							<TD width="10%">&nbsp;</TD>
							<TD width="15%">&nbsp;</TD>
							<TD width="23%">&nbsp;</TD>
						</TR>
						<TR>
							<TD width="100%" height="20px" valign="middle" colspan="5"><HR/>&nbsp;</TD>
						</TR>						
						<TR >
							<TD><ct:FWLabel key="APPLICATION"/>&nbsp;</TD>
							<TD><ct:FWCodeSelectTag name="forCsApplication" wantBlank="<%=true%>" codeType="TU_APPLI" defaut=""/>&nbsp;</TD>
							<TD>&nbsp;</TD>
							<TD><ct:FWLabel key="CANTON"/>&nbsp;</TD>
							<TD><INPUT type="text" id="forCanton" name="forCanton" class="numeroCourt">&nbsp;</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="RUBRIQUE"/>&nbsp;</TD>
							<TD nowrap>
								<ct:select id="forCsRubrique" name="forCsRubrique" wantBlank="true" style="width:500px;">
									<ct:optionsCodesSystems csFamille="TU_RUBR"/>
								</ct:select>							
							
<%-- 							
								<INPUT type="hidden" name="csRubrique"/>
								<INPUT name="libelleRubrique" type="text" style="width:400px;" value="<%=viewBean.getForLibelleRubrique()%>"/> 
								<INPUT name="forCsRubrique" type="hidden" value="<%=viewBean.getForCsRubrique()%>"/> 
								<INPUT name="idTypeCodeSystemeRubrique" type="hidden" value="10940003">
								<INPUT type="hidden" name="selectorName" value="">
								<%	Object[] rubriqueMethodsName = new Object[]{
										new String[]{"setForCsRubrique","getCsCodeSysteme"},
										new String[]{"setForLibelleRubrique","getLibelle"},
									};
									Object[] rubriqueParams = new Object[] {
										new String[] {"idTypeCodeSystemeRubrique", "idCodeSysteme"}
									};
								%>
								<ct:FWSelectorTag name="rubriqueCodeSelector"
								viewBean="<%=viewBean%>" methods="<%=rubriqueMethodsName%>"
								providerApplication="tucana" providerPrefix="TU"
								providerAction="tucana.communs.codeSysteme.chercher"
								providerActionParams="<%=rubriqueParams%>" 
								target="fr_main" redirectUrl="/tucanaRoot/bouclement/detail_rc.jsp" />
--%>
							</TD>
							<TD>&nbsp;</TD>
							<TD><ct:FWLabel key="ORIGINE"/>&nbsp;</TD>
							<TD><ct:FWCodeSelectTag name="forCsTypeRubrique" wantBlank="<%=true%>" codeType="TU_TYRUBR" defaut=""/>&nbsp;</TD>
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