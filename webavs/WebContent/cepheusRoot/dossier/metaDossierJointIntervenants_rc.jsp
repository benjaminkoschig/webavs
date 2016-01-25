<%-- tpl:insert page="/theme/capage.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/capage/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran="GDO0002";
globaz.cepheus.vb.dossier.DOMetaDossierJointIntervenantsViewBean viewBean = (globaz.cepheus.vb.dossier.DOMetaDossierJointIntervenantsViewBean) request.getAttribute("viewBean");

IFrameDetailHeight = "400";

String idMetaDossier = (String)session.getAttribute("idMetaDossier");
String nomTiersMetaDossier = (String)session.getAttribute("NomTiersMetaDossier");
String prenomTiersMetaDossier = (String)session.getAttribute("PrenomTiersMetaDossier");
String noAvsTiersMetaDossier = (String)session.getAttribute("NoAvsTiersMetaDossier");
String csTypeDemande = (String)session.getAttribute("csTypeDemande");
String detailRequerantDetail = (String)session.getAttribute("detailIntervenant");
%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<SCRIPT language="javascript">
	bFind = true;
	usrAction = "<%=globaz.cepheus.servlet.IDOActions.ACTION_CHERCHER_PRESTATION_INTERVENANTS%>.lister";
	detailLink = "<%=servletContext + mainServletPath%>?userAction=<%=globaz.cepheus.servlet.IDOActions.ACTION_CHERCHER_PRESTATION_INTERVENANTS%>.afficher";
	
	function loadFrames() {
		// prevenir les cursor state not valid exception
		if(bFind) {
			// document.forms[0].submit(); appelle depuis l'ecran DE
			document.fr_detail.location.href = detailLink + "&_valid=new";
		}
	}
	
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_TITRE_PRESTATION_INTERVENANTS"/><%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
						    <TD></TD>
							<TD width="100%">
								<TABLE border="0" cellspacing="0" cellpadding="0" width="80%">
									<TR>
										<TD><B>Détail assuré</B></TD>
										<TD><INPUT type="text" value="<%=detailRequerantDetail%>" size="100" class="disabled" readonly></TD>
									</TR>
									<TR>
										<TD><ct:FWLabel key="JSP_TYPE_PRRESTATION"/></TD>
										<TD>
											<INPUT type="text" name="csTypeDemande" value="<%=viewBean.getSession().getCodeLibelle(csTypeDemande)%>" disabled="disabled">
										</TD>
										<TD colspan="2">&nbsp;</TD>
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