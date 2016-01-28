<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="GLI0016";

	globaz.libra.vb.utilisateurs.LIUtilisateursViewBean viewBean = (globaz.libra.vb.utilisateurs.LIUtilisateursViewBean) session.getAttribute("viewBean");

	bButtonCancel = false;	
	selectedIdValue = viewBean.getIdUtilisateur();

	String domaines[] = viewBean.getDomainesList(false);
	
	String noDomaine = "";
	
	if (!globaz.jade.client.util.JadeStringUtil.isBlankOrZero(viewBean.getIdDomaine(viewBean.getIdGroupe()))){
		noDomaine = viewBean.getIdDomaine(viewBean.getIdGroupe());
	} else {
		noDomaine = domaines[0];
	}
	
	String groupes[]  = viewBean.getGroupesList(noDomaine);

%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<script language="JavaScript">

  function add() {
    document.forms[0].elements('userAction').value="<%=globaz.libra.servlet.ILIActions.ACTION_UTILISATEURS_DE%>.ajouter"
  }

  function upd() {}

  function validate() {
    state = true;
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="<%=globaz.libra.servlet.ILIActions.ACTION_UTILISATEURS_DE%>.ajouter";
    else
        document.forms[0].elements('userAction').value="<%=globaz.libra.servlet.ILIActions.ACTION_UTILISATEURS_DE%>.modifier";
    return state;
  }

  function cancel() {
    if (document.forms[0].elements('_method').value == "add")
      document.forms[0].elements('userAction').value="back";
    else
      document.forms[0].elements('userAction').value="<%=globaz.libra.servlet.ILIActions.ACTION_UTILISATEURS_DE%>.afficher";
  }

  function del() {
    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
        document.forms[0].elements('userAction').value="<%=globaz.libra.servlet.ILIActions.ACTION_UTILISATEURS_DE%>.supprimer";
        document.forms[0].submit();
    }
  }
  	
  function init(){
    // recharger la page rcListe du parent si une modification a ete effectuee
	<%if ("new".equalsIgnoreCase(request.getParameter("_valid"))) {%>
	  	// mise a jour de la liste du parent
		if (parent.document.forms[0]) {
			parent.document.forms[0].submit();
		}
	<%}%>
  }
  
  function readOnly(flag) {
  	// empeche la propriete disabled des elements etant de la classe css 'forceDisable' d'etre modifiee
    for(i=0; i < document.forms[0].length; i++) {
        if (!document.forms[0].elements[i].readOnly && 
        	document.forms[0].elements[i].className != 'forceDisable' &&
        	document.forms[0].elements[i].type != 'hidden') {
            document.forms[0].elements[i].disabled = flag;
        }
    }
  }

  function rechargerPage() {
  	if (document.forms[0].elements("_method").value == "add") {
		document.forms[0].elements("userAction").value = "<%=globaz.libra.servlet.ILIActions.ACTION_UTILISATEURS_DE%>.afficher";
		document.forms[0].submit();
	}
  }
  
  function updateGroupes(selectedDomain){
		var domainList  = document.forms[0].elements('idDomaine');
		var groupesList = document.forms[0].elements('idGroupe');
		
		<%
		viewBean.loadGroupes();
		java.util.Set keys = viewBean.getSacGroupes().keySet();
			for (java.util.Iterator iterator = keys.iterator(); iterator.hasNext();) {
				String cle = (String) iterator.next();%>			
				if (selectedDomain==<%=cle%>){
					groupesList.options.length=0;			
					<% 			
					java.util.List listGroupes = (java.util.List)viewBean.getSacGroupes().get(cle); 	
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
  
</script>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="ECRAN_GED_UTI_TITRE_DE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
	<TR>
		<TD colpsan="6" align="center">						
			<table width="95%">	
  				<tr>
					<td colspan="6" class="areaGlobazBlue">
					    <table width="100%">							
							<TR>
								<TD><ct:FWLabel key="ECRAN_GED_UTI_NO_USER"/></TD>
								<TD><%= viewBean.getIdUtilisateur() %></TD>
							</TR>
							<TR>
								<TD><ct:FWLabel key="ECRAN_GES_UTI_DOMAINE"/></TD>
								<TD>
									<select name="idDomaine" onChange="updateGroupes(this.value)">
										<% for (int i=0; i < domaines.length; i=i+2){ %>			
											<OPTION value="<%=domaines[i]%>" <%=domaines[i].equals(viewBean.getIdDomaine(viewBean.getIdGroupe()))?"selected":""%>><%=domaines[i+1]%></OPTION>					
										<% } %>
									</select>
								</TD>
								<TD><ct:FWLabel key="ECRAN_GES_UTI_GROUPE"/></TD>
								<TD>
									<select name="idGroupe">
										<% for (int i=0; i < groupes.length; i=i+2){ %>			
											<OPTION value="<%=groupes[i]%>"><%=groupes[i+1]%></OPTION>					
										<% } %>	
									</select>
								</TD>								
							</TR>
							<TR>
								<TD><ct:FWLabel key="ECRAN_GES_UTI_UTILISATEUR"/></TD>
								<TD>
									<input type="text" name="idUtilisateurExterne" value="<%= viewBean.getIdUtilisateurExterne() %>">
									<ct:FWSelectorTag
										name="selecteurUser"
	
										methods="<%=viewBean.getMethodesSelectionUser()%>"
										providerApplication="libra"
										providerPrefix="LI"
										providerAction="libra.utilisateurs.utilisateursFX.chercher"
										target="fr_main"
										redirectUrl="<%=mainServletPath%>"/>				
									<b><%= viewBean.getDetailUserExterne() %></b>
								</TD>
								<TD><ct:FWLabel key="ECRAN_GED_UTI_IS_DEFAULT"/></TD>
								<TD>
									<input type="checkbox" name="isDefault" value="on" <%=viewBean.getIsDefault().booleanValue()?"CHECKED":""%>>
								</TD>
							</TR>							
						</table>
					</td>
				</tr>
			</table>
		</TD>
	</TR>

						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>