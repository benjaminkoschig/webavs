<%-- tpl:insert page="/theme/capage.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/capage/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="GLI0015";
 	globaz.libra.vb.utilisateurs.LIUtilisateursViewBean viewBean = (globaz.libra.vb.utilisateurs.LIUtilisateursViewBean) request.getAttribute("viewBean");
 	
	IFrameDetailHeight = "350";
	
	String domaines[] = viewBean.getDomainesList(false);
	String groupes[]  = viewBean.getGroupesList(domaines[0]);
	
	String isSelection = request.getParameter("colonneSelection");
	
%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="li-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="li-optionsempty"/>

<SCRIPT language="JavaScript">

	bFind = true;
	detailLink = "<%=actionNew%>";
	usrAction = "<%=globaz.libra.servlet.ILIActions.ACTION_UTILISATEURS_RC%>.lister";

	function rechargerPage() {
		document.forms[0].elements("userAction").value = "<%=globaz.libra.servlet.ILIActions.ACTION_UTILISATEURS_RC%>.chercher";
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
	
	function updateGroupes(selectedDomain){
		var domainList  = document.forms[0].elements('forIdDomaine');
		var groupesList = document.forms[0].elements('forIdGroupe');
		
		<%
		viewBean.loadGroupes();
		java.util.Set keys = viewBean.getSacGroupes().keySet();
			for (java.util.Iterator iterator = keys.iterator(); iterator.hasNext();) {
				String key = (String) iterator.next();%>			
				if (selectedDomain==<%=key%>){
					groupesList.options.length=0;			
					<% 			
					java.util.List listGroupes = (java.util.List)viewBean.getSacGroupes().get(key); 	
					int i = 0;		
					for (java.util.Iterator iter = listGroupes.iterator(); iter.hasNext();) {
						String groupe = (String) iter.next();
						String value = viewBean.getLibelleGroupe(groupe);

						%>
						groupesList.options[<%=i%>]=new Option("<%=value%>", "<%=groupe%>", false, false)
						<%						
						i++;
					}
					%>
				}	
				<%
			}		
		%>
	}
	
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="ECRAN_GES_UTI_TITRE_RC"/><%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						
	<TR>
		<TD>
			<TABLE border="0">
				<TR>
					<TD width="20%"><ct:FWLabel key="ECRAN_GES_UTI_DOMAINE"/></TD>
					<TD width="20%">
						<select name="forIdDomaine" onChange="updateGroupes(this.value);submit()">
							<% for (int i=0; i < domaines.length; i=i+2){ %>			
								<OPTION value="<%=domaines[i]%>"><%=domaines[i+1]%></OPTION>					
							<% } %>
						</select>	
					</TD>
					<TD width="20%">&nbsp;</TD>
					<TD width="20%"><ct:FWLabel key="ECRAN_GES_UTI_GROUPE"/></TD>
					<TD width="20%">
						<select name="forIdGroupe">
							<% for (int i=0; i < groupes.length; i=i+2){ %>			
								<OPTION value="<%=groupes[i]%>"><%=groupes[i+1]%></OPTION>					
							<% } %>						
						</select>	
						<input type="hidden" name="colonneSelection" value="<%= isSelection %>">
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