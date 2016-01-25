<%-- tpl:insert page="/theme/capage.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/capage/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="GLI0021";
 	globaz.libra.vb.formules.LIChampsViewBean viewBean = (globaz.libra.vb.formules.LIChampsViewBean) request.getAttribute("viewBean");
		
	IFrameDetailHeight = "350";

	
%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="li-menuprincipal" showTab="options"/>
<ct:menuChange displayId="options" menuId="libra-optionsformules"/>

<SCRIPT language="JavaScript">

	bFind = true;
	detailLink = "<%=actionNew%>";
	usrAction = "<%=globaz.libra.servlet.ILIActions.ACTION_CHAMPS_RC%>.lister";

	function rechargerPage() {
		document.forms[0].elements("userAction").value = "<%=globaz.libra.servlet.ILIActions.ACTION_CHAMPS_RC%>.chercher";
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
				<%-- tpl:put name="zoneTitle" --%>Champs<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						
	<TR>
		<TD>
			<TABLE border="0">
				<TR>
					<TD>Libellé formule</TD>
					<TD>&nbsp;</TD>
					<TD>
						<input type="text" name="forFormule" value="<%= viewBean.getLibelleFormule() %>" size="50" readonly disabled="disabled">
						<input type="hidden" name="forCsDocument" value="<%= viewBean.getCsDocument() %>">
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