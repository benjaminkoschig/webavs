<%-- tpl:insert page="/theme/capage.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/capage/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%--
INFO: les labels de cette page sont prefixes avec 'JSP_CTT_R'
--%>
<%
idEcran="GCT0001";
globaz.babel.vb.cat.CTDocumentViewBean viewBean = (globaz.babel.vb.cat.CTDocumentViewBean) request.getAttribute("viewBean");
globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();

IFrameDetailHeight = "440";
IFrameListHeight = "130";

actionNew =  servletContext + mainServletPath + "?userAction=" + globaz.babel.servlet.CTMainServletAction.ACTION_TEXTES_SAISIE + ".afficher&_method=add&idDocument=" + viewBean.getIdDocument() + "&borneInferieure=" + viewBean.getBorneInferieure();

bButtonNew = objSession.hasRight("babel.cat.texteSaisie.actionAjouter", "ADD");

String langueSession = viewBean.getSession().getIdLangueISO();

%>
	<%-- /tpl:put --%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
	bFind = true;
	usrAction = "<%=globaz.babel.servlet.CTMainServletAction.ACTION_TEXTES%>.lister";
	detailLink = "<%=actionNew%>";
	
	function loadFrames() {
		// prevenir les cursor state not valid exception
		if(bFind) {
			// document.forms[0].submit(); appelle depuis l'ecran DE
			document.fr_detail.location.href = detailLink + "&_valid=new";
		}
		document.forms[0].elements('forCodeIsoLangue').value = "<%=langueSession%>";
	}
</SCRIPT>
	<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_CTT_R_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR><TD>
							<TABLE border="0">
								<TR>
									<TD><LABEL for="nomBidon"><ct:FWLabel key="JSP_CTT_R_NOM"/></LABEL></TD>
									<TD><INPUT type="text" name="nomBidon" value="<%=viewBean.getNom()%>" class="libelleLong disabled" readonly></TD>
									<TD><LABEL for="csDomaineBidon"><ct:FWLabel key="JSP_CTT_R_DOMAINE"/></LABEL></TD>
									<TD><INPUT type="text" name="csDomaineBidon" value="<%=viewBean.getLibelleDomaine()%>" class="disabled" readonly></TD>
									
								</TR>
								<TR>
								
									<TD><LABEL for="typeDocument_"><ct:FWLabel key="JSP_CTD_R_TYPE_DE_DOCUMENT"/></LABEL></TD>
									<TD><INPUT type="text" name="typeDoc_" value="<%=viewBean.getLibelleTypeDocument()%>" class="libelleLong disabled" readonly></TD>								

								
									<TD><LABEL for="forCodeIsoLangue"><ct:FWLabel key="JSP_CTT_R_CODE_ISO_LANGUE"/></LABEL></TD>
									<TD>
										<ct:select name="forCodeIsoLangue" wantBlank="true">
											<ct:option value="<%=globaz.babel.vb.cat.CTTexteSaisieViewBean.CODE_ISO_ALLEMAND%>" label="<%=globaz.babel.vb.cat.CTTexteSaisieViewBean.CODE_ISO_ALLEMAND%>"/>
											<ct:option value="<%=globaz.babel.vb.cat.CTTexteSaisieViewBean.CODE_ISO_FRANCAIS%>" label="<%=globaz.babel.vb.cat.CTTexteSaisieViewBean.CODE_ISO_FRANCAIS%>"/>
											<ct:option value="<%=globaz.babel.vb.cat.CTTexteSaisieViewBean.CODE_ISO_ITALIEN%>"  label="<%=globaz.babel.vb.cat.CTTexteSaisieViewBean.CODE_ISO_ITALIEN%>"/>
										</ct:select>
										<INPUT type="hidden" name="forIdDocument" value="<%=viewBean.getIdDocument()%>">
										<INPUT type="hidden" name="borneInferieure" value="<%=viewBean.getBorneInferieure()%>">
										
									</TD>
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