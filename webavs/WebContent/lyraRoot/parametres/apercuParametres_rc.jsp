<%-- tpl:insert page="/theme/capage.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/capage/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="GLY0001";
 	globaz.lyra.vb.parametres.LYApercuParametresViewBean viewBean = (globaz.lyra.vb.parametres.LYApercuParametresViewBean) request.getAttribute("viewBean");
	
	actionNew =  servletContext + mainServletPath + "?userAction=" + globaz.lyra.servlet.ILYActions.ACTION_PARAMETRES + ".afficher&_method=add&idEcheances=" + viewBean.getIdEcheances();
		
	IFrameDetailHeight = "350";
	bButtonFind = false;
	
%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
	
	// afficher un message d'erreur si le viewBean est en erreur
	
	<% if (globaz.framework.bean.FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) { %>
		var errorObj = new Object();
		errorObj.text = "<%=globaz.framework.util.FWTextFormatter.slash(globaz.framework.util.FWTextFormatter.newLineToBr(viewBean.getMessage()), '\"')%>";
		showModalDialog('<%=servletContext%>/errorModalDlg.jsp',errorObj,'dialogHeight:20;dialogWidth:25;status:no;resizable:no');	
	<% } %>

	bFind = true;
	detailLink = "<%=actionNew%>";
	usrAction = "<%=globaz.lyra.servlet.ILYActions.ACTION_PARAMETRES%>.lister";

	function rechargerPage() {
		document.forms[0].elements("userAction").value = "<%=globaz.lyra.servlet.ILYActions.ACTION_ECHEANCES%>.chercher";
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
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_PARAMETRES"/><%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD>
								<TABLE border="0">
									<TR>
										<TD><INPUT type="hidden" name="forIdEcheances" value="<%=viewBean.getIdEcheances()%>">
											<INPUT type="hidden" name="idEcheances" value="<%=viewBean.getIdEcheances()%>">
										    
										    <LABEL for="csDomaineBidon"><ct:FWLabel key="JSP_ECHEANCES_DOMAINE_APPLICATIF"/></LABEL></TD>
										<TD>&nbsp;</TD>
										<TD><INPUT type="text" name="csDomaineBidon" value="<%=viewBean.getLibelleDomaineApplicatif(viewBean.getDomaineApplicatifEcheance())%>" class="disabled" readonly></TD>
									</TR>
									<TR>
										<TD><LABEL for="csDescriptifBidon"><ct:FWLabel key="JSP_ECHEANCES_DESCRIPTIF"/></LABEL></TD>
										<TD>&nbsp;</TD>
										<TD><INPUT type="text" name="csDescriptifBidon" value="<%=viewBean.getDescriptifEcheance()%>" class="libelleLongDisabled" readonly></TD>
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