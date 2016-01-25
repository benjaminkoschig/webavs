<%-- tpl:insert page="/theme/capage.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/capage/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="GLI0013";
 	globaz.libra.vb.groupes.LIGroupesViewBean viewBean = (globaz.libra.vb.groupes.LIGroupesViewBean) request.getAttribute("viewBean");
		
	IFrameDetailHeight = "350";
	
	String domaines[] = viewBean.getDomainesList(true);
	
%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="li-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="li-optionsempty"/>

<SCRIPT language="JavaScript">

	bFind = true;
	detailLink = "<%=actionNew%>";
	usrAction = "<%=globaz.libra.servlet.ILIActions.ACTION_GROUPES_RC%>.lister";

	function rechargerPage() {
		document.forms[0].elements("userAction").value = "<%=globaz.libra.servlet.ILIActions.ACTION_GROUPES_RC%>.chercher";
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
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="ECRAN_GES_GROUPES_TITRE_RC"/><%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						
	<TR>
		<TD>
			<TABLE border="0">
				<TR>
					<TD><ct:FWLabel key="ECRAN_GES_DOMAINE"/></TD>
					<TD>
						<select name="forIdDomaine" onChange="submit()">
							<% for (int i=0; i < domaines.length; i=i+2){ %>			
								<OPTION value="<%=domaines[i]%>" <%=globaz.jade.client.util.JadeStringUtil.isBlankOrZero(domaines[i])?"selected":""%>><%=domaines[i+1]%></OPTION>					
							<% } %>
						</select>
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