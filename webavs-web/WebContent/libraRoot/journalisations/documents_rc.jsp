<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<script type="text/javascript"
	src="<%=servletContext%>/scripts/nss.js"></script>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%
	idEcran="GLI0004";
 	globaz.libra.vb.journalisations.LIEcheancesJointDossiersViewBean viewBean = (globaz.libra.vb.journalisations.LIEcheancesJointDossiersViewBean) request.getAttribute("viewBean");

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
	
	// Gestion de l'affichage du détail du dossier ou non ------------------------------
	String idDossier = viewBean.getIdDossier();
	String idTiers = viewBean.getIdTiers();
	
	if (globaz.jade.client.util.JadeStringUtil.isBlankOrZero(idDossier)){
		if (null!=globaz.prestation.tools.PRSessionDataContainerHelper.getData(session, globaz.prestation.tools.PRSessionDataContainerHelper.KEY_LIBRA_DTO) &&
			globaz.prestation.tools.PRSessionDataContainerHelper.getData(session, globaz.prestation.tools.PRSessionDataContainerHelper.KEY_LIBRA_DTO) instanceof  globaz.libra.utils.LIRecherchesDTO) {
			idDossier = ((globaz.libra.utils.LIRecherchesDTO) globaz.prestation.tools.PRSessionDataContainerHelper.getData(session, globaz.prestation.tools.PRSessionDataContainerHelper.KEY_LIBRA_DTO)).getIdDossier();	
		}	
	
		if (globaz.jade.client.util.JadeStringUtil.isBlankOrZero(idDossier)){
			idDossier = request.getParameter("selectedId");
		}
	}

	if (null!=idDossier && !idDossier.equals("")){
		bButtonNew = true;
		viewBean.loadDossier(idDossier);
		actionNew  = "libra?userAction="+globaz.libra.servlet.ILIActions.ACTION_ECHEANCES_DE + ".afficher&idDossier="+viewBean.getDossier().getIdDossier()+"&idTiers="+viewBean.getDossier().getIdTiers()+"&_method=add";
	} else {
		bButtonNew = false;
	}
	// Fin gestion affichage dossier --------------------------------------------------

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
	usrAction = "<%=globaz.libra.servlet.ILIActions.ACTION_ECHEANCES_DOC%>.lister";
	bButtonNew = false;

	function clickDossierEnCours(){
		if (document.getElementById("forIdDossier").value == ""){
			document.getElementById("forIdDossier").value = "<%= idDossier %>";	
		} else {
			document.getElementById("forIdDossier").value = "";		
		}
	}

	function clearFields () {
		document.getElementsByName("forDateDebut")[0].value="";
		document.getElementsByName("forDateFin")[0].value="<%= globaz.globall.util.JACalendar.todayJJsMMsAAAA() %>";
		document.getElementsByName("forCsType")[0].value="";
		document.getElementsByName("orderBy")[0].value="<%= globaz.libra.interfaces.ILIConstantes.ECHEANCES_TRI_DATE %>";
		<% if (null!=viewBean.getDossier().getIdDossier()){ %>
			if (document.forms[0].elements('dossierEnCours') != null){
				document.getElementsByName("dossierEnCours")[0].checked=true;
			}
		<% } %>
	}

	function echeances(){
	    document.forms[0].elements('userAction').value= "<%=globaz.libra.servlet.ILIActions.ACTION_ECHEANCES_RC %>.chercher";
	    document.forms[0].target="fr_main";
	    document.forms[0].submit();
	}

	function executer(){
	    onClickPrint();
	    document.forms[0].method="post";
	    document.forms[0].target="fr_main";	    
	    document.forms[0].elements('userAction').value= "<%=globaz.libra.servlet.ILIActions.ACTION_ECHEANCES_DOC %>.afficherExecuter";
	    document.forms[0].submit();
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
							usersList.options[<%=i%>]=new Option("<%=value%>", "<%=value%>", true, true)
							<%
						} else {
							if (value.equals(viewBean.getSession().getUserId())){
								%>
								usersList.options[<%=i%>]=new Option("<%=value%>", "<%=value%>", true, true)
								<%												
							} else {
								%>
								usersList.options[<%=i%>]=new Option("<%=value%>", "<%=value%>", false, false)
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

function onClickPrint(){
	var checkboxes = top.fr_main.fr_list.document.getElementsByName("checkBox");
	var inputTag = document.createElement('input');
	inputTag.value="";
	for(var i=0; i<checkboxes.length;i++){
		if (checkboxes(i).checked && checkboxes(i).value != 'ON' && checkboxes(i).value != '') {
			var inputTag = document.createElement('input');
			inputTag.type='hidden';
			inputTag.name='listeIdRappel';
			inputTag.value=checkboxes(i).value;
			document.forms[0].appendChild(inputTag);
		}		
	}	
}

</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Documents<%-- /tpl:put --%>
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
		<TD><ct:FWLabel key="ECRAN_ECH_RC_PERIODE"/></TD>
		<TD colspan="2">
			<ct:FWCalendarTag name="forDateDebut" value=""/>
			 - 
			<ct:FWCalendarTag name="forDateFin" value="<%= globaz.globall.util.JACalendar.todayJJsMMsAAAA() %>"/>
		</TD>
		<TD valign="top">
			<ct:FWLabel key="ECRAN_ECH_RC_TYPE"/>
			&nbsp;
			<ct:select name="forCsType" defaultValue="documents" wantBlank="true">
				<ct:optionsCodesSystems csFamille="<%=ch.globaz.libra.constantes.ILIConstantesExternes.JOURNALISATIONS_TYPE %>">
					<ct:excludeCode code="<%=globaz.journalisation.constantes.JOConstantes.CS_JO_FMT_MANUELLE_RECEPTION%>"/>
					<ct:excludeCode code="<%=globaz.journalisation.constantes.JOConstantes.CS_JO_FMT_ATTRIBUTION_DOSSIER%>"/>
					<ct:excludeCode code="<%=globaz.journalisation.constantes.JOConstantes.CS_JO_FMT_AUTOMATIQUE_AVEC_LIBELLE%>"/>
					<ct:excludeCode code="<%=globaz.journalisation.constantes.JOConstantes.CS_JO_FMT_ETABLISSEMENT_DECISION%>"/>
					<ct:excludeCode code="<%=globaz.journalisation.constantes.JOConstantes.CS_JO_FMT_TRANSFERT_DOSSIER%>"/>
					<ct:excludeCode code="<%=globaz.journalisation.constantes.JOConstantes.CS_JO_FMT_AUTOMATIQUE_AVEC_BLOCAGE%>"/>
					<ct:excludeCode code="<%=globaz.journalisation.constantes.JOConstantes.CS_JO_FMT_MANDAT%>"/>
					<ct:excludeCode code="<%=globaz.journalisation.constantes.JOConstantes.CS_JO_FMT_SOUS_MANDAT%>"/>
					<ct:excludeCode code="<%=globaz.journalisation.constantes.JOConstantes.CS_JO_FMT_SOUS_MANDAT_SMR%>"/>
					<ct:excludeCode code="<%=globaz.journalisation.constantes.JOConstantes.CS_JO_FMT_FORMULE%>"/>
					<ct:excludeCode code="<%=globaz.journalisation.constantes.JOConstantes.CS_JO_FMT_FORMULE_RAPPEL%>"/>
					<ct:excludeCode code="<%=globaz.journalisation.constantes.JOConstantes.CS_JO_FMT_FORMULE_RECEPTION%>"/>
					<ct:excludeCode code="<%=globaz.journalisation.constantes.JOConstantes.CS_JO_FMT_FORMULE_DELAI%>"/>
					<ct:excludeCode code="<%=globaz.journalisation.constantes.JOConstantes.CS_JO_FMT_FORMULE_TEMP_ENVOI%>"/>
					<ct:excludeCode code="<%=globaz.journalisation.constantes.JOConstantes.CS_JO_FMT_FORMULE_REIMPRESSION%>"/>
					<ct:excludeCode code="<%=globaz.journalisation.constantes.JOConstantes.CS_JO_FMT_ENVOI_DECISION%>"/>
					<ct:excludeCode code="<%=globaz.journalisation.constantes.JOConstantes.CS_JO_FMT_ENVOI_RAPPEL_DECISION%>"/>
					<ct:excludeCode code="<%=globaz.journalisation.constantes.JOConstantes.CS_JO_FMT_ENVOI_RECEPTION_DECISION%>"/>
					<ct:excludeCode code="<%=globaz.journalisation.constantes.JOConstantes.CS_JO_FMT_ENVOI_DELAI_DECISION%>"/>
					<ct:excludeCode code="<%=globaz.journalisation.constantes.JOConstantes.CS_JO_FMT_ENVOI_TEMP_ENVOI_DECISION%>"/>
					<ct:excludeCode code="<%=globaz.journalisation.constantes.JOConstantes.CS_JO_FMT_ENVOI_REIMPRESSION_DECISION%>"/>
					<ct:excludeCode code="<%=globaz.journalisation.constantes.JOConstantes.CS_JO_FMT_MANUELLE%>"/>
					<ct:excludeCode code="<%=globaz.journalisation.constantes.JOConstantes.CS_JO_FMT_AUTOMATIQUE%>"/>
					<ct:excludeCode code="<%=globaz.journalisation.constantes.JOConstantes.CS_JO_AVS_FMT_ACTION%>"/>
					<ct:excludeCode code="<%=globaz.journalisation.constantes.JOConstantes.CS_JO_AVS_FMT_RAPPEL%>"/>
				</ct:optionsCodesSystems>
			</ct:select>
			<input type="hidden" name="documents" value="on"/>									
		</TD>
		<TD valign="top">
			<ct:FWLabel key="ECRAN_ECH_RC_TRI"/>
			&nbsp;
			<ct:FWListSelectTag data="<%=viewBean.getOrderByData()%>" defaut="" name="orderBy"/>									
		</TD>													
	</TR>
	<TR>				
		<% if (!globaz.jade.client.util.JadeStringUtil.isBlankOrZero(idDossier)) { %> 																				
			<TD colspan="4" valign="bottom">
				<input type="checkbox" name="dossierEnCours" value="on" onclick="clickDossierEnCours();" checked="checked"/> 
				<ct:FWLabel key="ECRAN_ECH_RC_DOSSIER_EN_COURS"/>
				&nbsp;(N° <%= viewBean.getDossier().getIdDossier()+ " - " + viewBean.getDossier().getDetailTiersLigne()%>)
				<input type="hidden" name="forIdDossier" value="<%=viewBean.getDossier().getIdDossier()%>">
			</TD>					
			<TD valign="bottom" colspan="2">
				<input type="button" onclick="clearFields()" accesskey="C" value="Effacer">[ALT+C]
			</TD>
		<% } else { %>	
			<TD colspan="4">&nbsp;</TD>
			<TD valign="bottom" colspan="2">
				<input type="button" onclick="clearFields()" accesskey="C" value="Effacer">[ALT+C]
			</TD>
		<% } %>
		
	</TR>											
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				
				<ct:ifhasright element="<%=globaz.libra.servlet.ILIActions.ACTION_JOURNALISATIONS %>" crud="r">
					<INPUT class="btnCtrl" type="button" id="btnEcheances" value="<ct:FWLabel key="MENU_OPTION_ECHEANCES"/>" onclick="echeances()">
				</ct:ifhasright>				

				<ct:ifhasright element="<%=globaz.libra.servlet.ILIActions.ACTION_JOURNALISATIONS %>" crud="r">
					<INPUT class="btnCtrl" type="button" id="btnExecuter" value="<ct:FWLabel key="MENU_OPTION_EXECUTER"/>" onclick="executer()">
				</ct:ifhasright>		
				
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>