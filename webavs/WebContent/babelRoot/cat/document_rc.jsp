<%-- tpl:insert page="/theme/capage.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/capage/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%-- 
INFO: les labels de cette page sont prefixes avec 'JSP_CTD_R'
--%>
<%
idEcran="GCT0000";
globaz.babel.vb.cat.CTDocumentViewBean viewBean = (globaz.babel.vb.cat.CTDocumentViewBean) request.getAttribute("viewBean");
globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();

IFrameDetailHeight = "420";

bButtonNew = objSession.hasRight("babel.cat.document.actionAjouter", "ADD");
%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	actionNew = actionNew + "&csGroupeDomaines=" + viewBean.getCsGroupeDomaines() + "&csGroupeTypesDocuments=" + viewBean.getCsGroupeTypesDocuments();
%>
<SCRIPT language="JavaScript">
	// afficher un message d'erreur si le viewBean est en erreur
	<% if (globaz.framework.bean.FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) { %>
	var errorObj = new Object();
	errorObj.text = "<%=globaz.framework.util.FWTextFormatter.slash(globaz.framework.util.FWTextFormatter.newLineToBr(viewBean.getMessage()), '\"')%>";
	showModalDialog('<%=servletContext%>/errorModalDlg.jsp',errorObj,'dialogHeight:20;dialogWidth:25;status:no;resizable:no');	
	<% } %>

	bFind = true;
	detailLink = "<%=actionNew%>";
	usrAction = "<%=globaz.babel.servlet.CTMainServletAction.ACTION_DOCUMENTS%>.lister";
	
	function rechargerPage() {
		document.forms[0].elements("userAction").value = "<%=globaz.babel.servlet.CTMainServletAction.ACTION_DOCUMENTS%>.chercher";
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
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_CTD_R_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR><TD>
							<TABLE border="0">
								<TR>
									<TD><LABEL for="forCsTypeDocument"><ct:FWLabel key="JSP_CTD_R_TYPE_DE_DOCUMENT"/></LABEL></TD>
									<TD>
										<ct:select name="forCsTypeDocument" defaultValue="<%=viewBean.getCsTypeDocument()%>" wantBlank="true">
											<ct:optionsCodesSystems csFamille="<%=viewBean.getCsGroupeTypesDocuments()%>">
												<ct:forEach items="<%=viewBean.getCsTypesDocumentsExclus()%>" var="code">
													<ct:excludeCode id="code"/>
												</ct:forEach>
											</ct:optionsCodesSystems>
										</ct:select>
									</TD>
									<TD><LABEL for="forCsDomaine"><ct:FWLabel key="JSP_CTD_R_DOMAINE"/></LABEL></TD>
									<TD>
										<ct:select name="forCsDomaine" onchange="rechargerPage()" defaultValue="<%=viewBean.getCsDomaine()%>">
											<ct:optionsCodesSystems csFamille="<%=viewBean.getCsGroupeDomaines()%>"/>
										</ct:select>
										
										<INPUT type="hidden" name="csGroupeDomaines" value="<%=viewBean.getCsGroupeDomaines()%>">
										<INPUT type="hidden" name="csGroupeTypesDocuments" value="<%=viewBean.getCsGroupeTypesDocuments()%>">
									</TD>
								</TR>
								<TR>
									<TD><LABEL for=""><ct:FWLabel key="JSP_CTD_R_DESTINATAIRE"/></LABEL></TD>
									<TD>
										<ct:select name="forCsDestinataire" wantBlank="true">
											<ct:optionsCodesSystems csFamille="<%=globaz.babel.api.ICTDocument.CS_GROUPE_DESTINATAIRE%>"/>
										</ct:select>
									</TD>
									<TD></TD>
									<TD></TD>
								</TR>
							</TABLE>
						</TD></TR>
	 					<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyClose.jspf" %>
<%-- /tpl:insert --%>