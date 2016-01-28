<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<script type="text/javascript"
	src="<%=servletContext%>/scripts/nss.js"></script>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%
	idEcran="GLI0000";
 	globaz.libra.vb.dossiers.LIDossiersJointTiersViewBean viewBean = (globaz.libra.vb.dossiers.LIDossiersJointTiersViewBean) request.getAttribute("viewBean");
	bButtonNew = false;

	String[] domaines = viewBean.getDomainesForUser(true);

	String domaineDTO = "";
	String groupeDTO = "";
	String userDTO   = "";

	if (null!=globaz.prestation.tools.PRSessionDataContainerHelper.getData(session, globaz.prestation.tools.PRSessionDataContainerHelper.KEY_LIBRA_DTO) &&
		globaz.prestation.tools.PRSessionDataContainerHelper.getData(session, globaz.prestation.tools.PRSessionDataContainerHelper.KEY_LIBRA_DTO) instanceof  globaz.libra.utils.LIRecherchesDTO) {
		domaineDTO = ((globaz.libra.utils.LIRecherchesDTO) globaz.prestation.tools.PRSessionDataContainerHelper.getData(session, globaz.prestation.tools.PRSessionDataContainerHelper.KEY_LIBRA_DTO)).getIdDomaine();
		groupeDTO = ((globaz.libra.utils.LIRecherchesDTO) globaz.prestation.tools.PRSessionDataContainerHelper.getData(session, globaz.prestation.tools.PRSessionDataContainerHelper.KEY_LIBRA_DTO)).getIdGroupe();
		userDTO = ((globaz.libra.utils.LIRecherchesDTO) globaz.prestation.tools.PRSessionDataContainerHelper.getData(session, globaz.prestation.tools.PRSessionDataContainerHelper.KEY_LIBRA_DTO)).getIdUtilisateur();
	}

	String lienRetour = "";
	
	if (null!=globaz.prestation.tools.PRSessionDataContainerHelper.getData(session, globaz.prestation.tools.PRSessionDataContainerHelper.KEY_LIBRA_DTO) &&
		globaz.prestation.tools.PRSessionDataContainerHelper.getData(session, globaz.prestation.tools.PRSessionDataContainerHelper.KEY_LIBRA_DTO) instanceof  globaz.libra.utils.LIRecherchesDTO) {
		lienRetour = ((globaz.libra.utils.LIRecherchesDTO) globaz.prestation.tools.PRSessionDataContainerHelper.getData(session, globaz.prestation.tools.PRSessionDataContainerHelper.KEY_LIBRA_DTO)).getUrlRetour();	
	}
	
%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="li-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="LI-OnlyDetail"/>

<SCRIPT language="JavaScript">
	bFind = true;
	usrAction = "<%=globaz.libra.servlet.ILIActions.ACTION_DOSSIERS_RC %>.lister";
	bButtonNew = false;
	
	function clearFields () {
		document.getElementsByName("likeNumeroAVS")[0].value="";
		document.getElementsByName("partiallikeNumeroAVS")[0].value="";
		document.getElementsByName("likeNom")[0].value="";
		document.getElementsByName("likePrenom")[0].value="";
		document.getElementsByName("forCsEtat")[0].value="<%=ch.globaz.libra.constantes.ILIConstantesExternes.CS_ETAT_OUVERT %>";
		document.getElementsByName("forDateNaissance")[0].value="";
	}
	
	function printScreenHtml(){
	    document.forms[0].elements('userAction').value= "<%=globaz.libra.servlet.ILIActions.ACTION_DOSSIERS_RC%>.exporter";
	    document.forms[0].target="_blank";
	    document.forms[0].submit();
	   	document.forms[0].target="fr_list";
	    document.forms[0].elements('userAction').value= "<%=globaz.libra.servlet.ILIActions.ACTION_DOSSIERS_RC%>.lister";
	}	
	
	function updateGroupes(selectedDomain){
		var groupesList = document.forms[0].elements('forIdGroupe');
		if (groupesList != null){
		<%
		viewBean.loadGroupes();
		java.util.Set keys = viewBean.getSacGroupes().keySet();
			%>
				groupesList.options.length=0;
				groupesList.options[0]=new Option("", "", true, true);	
				updateUsers("");			
			<%
			for (java.util.Iterator iterator = keys.iterator(); iterator.hasNext();) {
				String key = (String) iterator.next();%>
		
				if (selectedDomain==<%=key%>){
					groupesList.options.length=0;			
					<% 			
					java.util.List listGroupes = (java.util.List)viewBean.getSacGroupes().get(key); 	
					int i = 0;		
					
					for (java.util.Iterator iter = listGroupes.iterator(); iter.hasNext();) {
		
						String idGroupe = (String) iter.next();
						
						String value = "";
						if (iter.hasNext()){
							value = (String) iter.next();						
						}
						
						if (idGroupe.equals(groupeDTO)){
						%>
						groupesList.options[<%=i%>]=new Option("<%=value%>", "<%=idGroupe%>", true, true)
						<%
							if (i == 0){
								%>updateUsers(<%= idGroupe %>);<%
							}
						} else {
						%>
						groupesList.options[<%=i%>]=new Option("<%=value%>", "<%=idGroupe%>", false, false)
						<%						
							if (i == 0){
								%>updateUsers(<%= idGroupe %>);<%
							}
						}
						i++;
					}
					%>
				}	
				<%
			}
			%>
			
		}
	}	

	function updateUsers(selectedGroup){
		var usersList = document.forms[0].elements('forIdUtilisateur');
		if (usersList != null){
		<%
		viewBean.loadUsers();
		java.util.Set keys1 = viewBean.getSacUsers().keySet();
			%>
			if (usersList != null){
				usersList.options.length=0;
				usersList.options[0]=new Option("<%= viewBean.getSession().getUserId() %>", "idFX", true, true);				
			}
			<%
			for (java.util.Iterator iterator = keys1.iterator(); iterator.hasNext();) {
				String key = (String) iterator.next();%>
		
				if (selectedGroup==<%=key%>){
					usersList.options.length=0;			
					<% 			
					java.util.List listUsers = (java.util.List)viewBean.getSacUsers().get(key); 	
					int i = 0;		
					
					for (java.util.Iterator iter = listUsers.iterator(); iter.hasNext();) {
						String idUser = (String) iter.next();
						String value = "";
						if (iter.hasNext()){
							value = (String) iter.next();						
						}
						if (idUser.equals(userDTO)){
							%>
							usersList.options[<%=i%>]=new Option("<%=value%>", "<%=idUser%>", true, true)
							<%
						} else {
							if (value.equals(viewBean.getSession().getUserId())){
								%>
								usersList.options[<%=i%>]=new Option("<%=value%>", "<%=idUser%>", true, true)
								<%												
							} else {
								%>
								usersList.options[<%=i%>]=new Option("<%=value%>", "<%=idUser%>", false, false)
								<%							
							}
						}
						i++;
					}
					%>
					
					usersList.options[<%=i%>]=new Option("", "", false, false)
					
				}	
				<%
			}		
		%>
		}
	}

</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="ECRAN_DOS_RC_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
	<TR>
		<TD colspan="4">	
			
			<!-- DOMAINES  -->
			<ct:FWLabel key="ECRAN_DOS_RC_DOMAINE"/>
			&nbsp;
			<select name="forIdDomaine" onChange="updateGroupes(this.value)">
				<%for (int i=0; i < domaines.length; i = i+2){%>
					<OPTION value="<%=domaines[i]%>" <%= domaines[i].equals(domaineDTO)?"selected":"" %>><%=domaines[i+1]%></OPTION>
				<% } %>
			</select>

			
			<!-- GROUPES -->
			&nbsp;&nbsp;&nbsp;
			<ct:FWLabel key="ECRAN_DOS_RC_GROUPE"/>
			&nbsp;
		   	<select name="forIdGroupe" onChange="updateUsers(this.value)">
			</select>	
			
			<% if (!globaz.jade.client.util.JadeStringUtil.isBlankOrZero(domaineDTO)){ %>
				<SCRIPT language="JavaScript">
					updateGroupes(<%= domaineDTO %>);
				</SCRIPT>
			<% } %>							

		   	
		   	<!-- UTILISATEURS  -->
			&nbsp;&nbsp;&nbsp;
			<ct:FWLabel key="ECRAN_DOS_RC_UTILISATEUR"/>
			&nbsp;
		   	<select name="forIdUtilisateur">
		   		<option value="idFX"><%= viewBean.getSession().getUserId() %></option>
			</select>
							
			<% if (!globaz.jade.client.util.JadeStringUtil.isBlankOrZero(groupeDTO)){ %>
				<SCRIPT language="JavaScript">
					updateUsers(<%= groupeDTO %>);
				</SCRIPT>
			<% } %>	

		</TD>
		<TD colspan="2">
			<% if (!globaz.jade.client.util.JadeStringUtil.isBlankOrZero(lienRetour)){ %>
				<a href="<%=request.getContextPath() + "/" + lienRetour %>" class="external_link">
					<ct:FWLabel key="MENU_OPTION_RET_APP"/>
				</a>
			<% } %>
		</TD>
	</TR>
	<TR>
		<TD colspan="6">
			<hr>
		</TD>
	</TR>
	<TR>
		<TD><ct:FWLabel key="ECRAN_DOS_RC_NSS"/></TD>
		<TD>
			<ct1:nssPopup avsMinNbrDigit="99"
						  nssMinNbrDigit="99"
						  newnss=""
						  name="likeNumeroAVS"	
						  value=""/>
		</TD>
		<TD><ct:FWLabel key="ECRAN_DOS_RC_NOM"/></TD>
		<TD><INPUT type="text" name="likeNom" value=""></TD>
		<TD><ct:FWLabel key="ECRAN_DOS_RC_PRENOM"/></TD>
		<TD><INPUT type="text" name="likePrenom" value=""></TD>							
	</TR>	
	<TR>						
		<TD><ct:FWLabel key="ECRAN_DOS_RC_DATE_NAISSANCE"/></TD>
		<TD><ct:FWCalendarTag name="forDateNaissance" value=""/></TD>
		<TD><ct:FWLabel key="ECRAN_DOS_RC_ETAT"/></TD>
		<TD>
			<ct:select name="forCsEtat" defaultValue="<%=ch.globaz.libra.constantes.ILIConstantesExternes.CS_ETAT_OUVERT%>" wantBlank="false">
				<ct:optionsCodesSystems csFamille="<%=ch.globaz.libra.constantes.ILIConstantesExternes.CS_ETAT_DOSSIERS%>"/>
			</ct:select>
		</TD>							
		<TD><ct:FWLabel key="ECRAN_DOS_RC_AFFICHAGE"/></TD>
		<TD colspan="2" rowspan="2">
			<input type="radio" name="forAffichage" onclick="submit()" value="<%=ch.globaz.libra.constantes.ILIConstantesExternes.CS_AFF_TOUS %>" checked><ct:FWLabel key="ECRAN_DOS_RC_AFF_TOUS"/><br>
			<input type="radio" name="forAffichage" onclick="submit()" value="<%=ch.globaz.libra.constantes.ILIConstantesExternes.CS_AFF_SUSPENS %>"><ct:FWLabel key="ECRAN_DOS_RC_AFF_SUSPENS"/>
		</TD>
	</TR>
	<TR>
		<TD valign="top"><input type="button" onclick="clearFields()" accesskey="C" value="Effacer">[ALT+C]</TD>
	</TR>
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				
				<ct:ifhasright element="<%=globaz.libra.servlet.ILIActions.ACTION_DOSSIERS %>" crud="r">
					<% if (globaz.libra.utils.LIEcransUtil.isWantButtonPrint(viewBean.getSession())){ %>
						<INPUT class="btnCtrl" type="button" id="btnImprimer" value="<ct:FWLabel key="ECRAN_ALL_RC_EDITER"/>" onclick="printScreenHtml();">
					<% } %>
				</ct:ifhasright>				
				
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>