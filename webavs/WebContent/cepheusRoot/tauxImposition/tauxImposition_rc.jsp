<%-- tpl:insert page="/theme/capage.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/capage/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran="GDO0004";
globaz.cepheus.vb.tauxImposition.DOTauxImpositionViewBean viewBean = (globaz.cepheus.vb.tauxImposition.DOTauxImpositionViewBean) request.getAttribute("viewBean");

IFrameDetailHeight = "420";

actionNew += "&csCanton=" + viewBean.getCSCaisseDefaultCanton();
%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<SCRIPT language="JavaScript">
	bFind = true;
	detailLink = "<%=actionNew%>";
	usrAction = "<%=globaz.cepheus.servlet.IDOActions.ACTION_CHERCHER_TAUX_IMPOSITIONS%>.lister";
	
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
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_TIM_R_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD>
								<TABLE border="0" cellspacing="2" cellpadding="0" width="900">
									<TR>
										<TD><LABEL for="forCsCanton"><ct:FWLabel key="JSP_TIM_R_CANTON"/></LABEL></TD>
										<TD>
											<ct:FWCodeSelectTag name="forCsCanton" codeType="PYCANTON" wantBlank="true" defaut="<%=viewBean.getCSCaisseDefaultCanton()%>"/>
										</TD>
										<TD><LABEL for="forTypeImpot"><ct:FWLabel key="JSP_TIM_R_TYPE_IMPOSITION"/></LABEL></TD>
										<TD>
											<ct:select name="forTypeImpot" wantBlank="true" defaultValue="<%=viewBean.getTypeImpotSource()%>">
												<ct:optionsCodesSystems csFamille="<%=globaz.prestation.tauxImposition.api.IPRTauxImposition.CS_GROUPE_TYPE_IMPOSITION_SOURCE%>"/>
											</ct:select>
										</TD>
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