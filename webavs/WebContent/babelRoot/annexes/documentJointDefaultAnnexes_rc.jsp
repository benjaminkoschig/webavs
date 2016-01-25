<%-- tpl:insert page="/theme/capage.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/capage/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran="GCT0003";
globaz.babel.vb.annexes.CTDocumentJointDefaultAnnexesViewBean viewBean = (globaz.babel.vb.annexes.CTDocumentJointDefaultAnnexesViewBean) request.getAttribute("viewBean");

IFrameDetailHeight = "420";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	actionNew = actionNew + "&idDocument=" + viewBean.getIdDocument() + 
	                        "&csTypeDocument=" + viewBean.getCsTypeDocument() +
	                        "&csDomaineDocument=" + viewBean.getCsDomaineDocument();
%>
<SCRIPT language="JavaScript">
	bFind = true;
	detailLink = "<%=actionNew%>";
	usrAction = "<%=globaz.babel.servlet.CTMainServletAction.ACTION_DEFAULT_ANNEXE%>.lister";
	
	function rechargerPage() {
		document.forms[0].elements("userAction").value = "<%=globaz.babel.servlet.CTMainServletAction.ACTION_DEFAULT_ANNEXE%>.chercher";
		document.forms[0].target = "fr_main";
		document.forms[0].submit();
	}
	
	function onClickNew() {
		// desactive le grisement des boutons dans l'ecran rc quand on clique sur le bouton new
	}
	
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
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_CTA_R_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD>
								<TABLE border="0" cellspacing="2" cellpadding="0" width="700">
									<TR>
										<TD><LABEL for="csTypeDocument"><ct:FWLabel key="JSP_CTA_R_TYPE_DOCUMENT"/></LABEL></TD>
										<TD>
											<INPUT type="hidden" name="forIdDocument" value="<%=viewBean.getIdDocument()%>">
											<INPUT type="hidden" name="idDocument" value="<%=viewBean.getIdDocument()%>">
											<INPUT type="hidden" name="csTypeDocument" value="<%=viewBean.getCsTypeDocument()%>">
											<INPUT type="hidden" name="csGroupeAnnexes" value="<%=viewBean.getCsGroupeAnnexes()%>">
											<INPUT type="text" name="libelleTypeDocument" value="<%=viewBean.getLibelleCsTypeDocument()%>" disabled="disabled">
										</TD>
										<TD><LABEL for="csDomaineDocument"><ct:FWLabel key="JSP_CTA_R_DOMAINE"/></LABEL></TD>
										<TD>
											<INPUT type="hidden" name="csDomaineDocument" value="<%=viewBean.getCsDomaineDocument()%>">
											<INPUT type="text" name="libelleDomaineDocument" value="<%=viewBean.getLibelleCsDomaineDocument()%>" disabled="disabled">
										</TD>
									</TR>
									<TR>
										<TD><LABEL for="csDestinataireDocument"><ct:FWLabel key="JSP_CTA_R_DESTINATAIRE"/></LABEL></TD>
										<TD>
											<INPUT type="hidden" name="csDestinataireDocument" value="<%=viewBean.getCsDestinataireDocument()%>">
											<INPUT type="text" name="libelleDestinataireDocument" value="<%=viewBean.getLibelleCsDestinataireDocument()%>" disabled="disabled">
										</TD>
										<TD><LABEL for="nomDocument"><ct:FWLabel key="JSP_CTA_R_DOCUMENT"/></LABEL></TD>
										<TD>
											<INPUT type="text" name="nomDocument" value="<%=viewBean.getNomDocument()%>" disabled="disabled">
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