	<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
		
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
	<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%@ taglib uri="/libraRoot/librataglib.tld" prefix="li" %>
<%

	idEcran="GLI0001";

	globaz.libra.vb.dossiers.LIDossiersViewBean viewBean = (globaz.libra.vb.dossiers.LIDossiersViewBean) session.getAttribute("viewBean");

	bButtonUpdate = objSession.hasRight("libra.dossiers", FWSecureConstants.UPDATE);
	bButtonDelete = false;
	
	String[] groupes = viewBean.getGroupesList(viewBean.getIdDomaine());
	String[] users   = viewBean.getUsersList(viewBean.getIdGroupe());
	
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<ct:menuChange displayId="menu" menuId="li-menuprincipal" showTab="options"/>
<ct:menuChange displayId="options" menuId="libra-optionsdossiers">
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdDossier()%>"/>
	<ct:menuSetAllParams key="idTiers" value="<%=viewBean.getIdTiers()%>"/>
</ct:menuChange>

<script language="JavaScript">

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

  function upd() {
  }

  function cancel() {
		document.forms[0].elements('userAction').value="<%=globaz.libra.servlet.ILIActions.ACTION_DOSSIERS_RC%>.chercher"; 	  	
  }
	   
  function validate() { 
	    state = true;
	    if (document.forms[0].elements('_method').value == "add"){
	        document.forms[0].elements('userAction').value="<%=globaz.libra.servlet.ILIActions.ACTION_DOSSIERS%>.modifier";
	    }else{
	    	document.forms[0].elements('userAction').value="<%=globaz.libra.servlet.ILIActions.ACTION_DOSSIERS%>.modifier";
	    }
	  	return state;	    
  }
  
  function init(){
  }

  function postInit(){
  }

	function updateUsers(selectedGroup){
		var usersList = document.forms[0].elements('idGestionnaire');
		if (usersList != null){
		<%
		viewBean.loadUsers();
		java.util.Set keys1 = viewBean.getSacUsers().keySet();
			for (java.util.Iterator iterator = keys1.iterator(); iterator.hasNext();) {
				String cle = (String) iterator.next();%>
		
				if (selectedGroup==<%=cle%>){
					usersList.options.length=0;			
					<% 			
					java.util.List listUsers = (java.util.List)viewBean.getSacUsers().get(cle); 	
					int i = 0;		
					
					for (java.util.Iterator iter = listUsers.iterator(); iter.hasNext();) {
						String idUser = (String) iter.next();
						String value = "";
						if (iter.hasNext()){
							value = (String) iter.next();						
						}
						%>
						usersList.options[<%=i%>]=new Option("<%=value%>", "<%=idUser%>", false, false)
						<%							
						i++;
					}
					%>
				}	
				<%
			}		
		%>
		}
	}

</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="ECRAN_DOS_DE_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
	<TR>
		<TD colpsan="6" align="center">
			<table width="95%">
  				<tr>
					<td colspan="6" class="ongletLightBlue">
					    <table width="100%">
							<TR>
								<TD width="20%" valign="top"><b><ct:FWLabel key="ECRAN_DOS_DE_TIERS"/></b></TD>
								<TD colspan="3"><b><%= viewBean.getDetailTiersLigne() %></b></TD>
							</TR>
						</table>
					</td>
				</tr>							
			<table width="95%">
  				<tr>
					<td colspan="6" class="ongletLightBlue">
					    <table width="100%">							
							<TR>
								<TD width="10%"><ct:FWLabel key="ECRAN_DOS_DE_NO_DOSSIER"/></TD>
								<TD width="40%"><%=viewBean.getIdDossier()%>
							</TR>	
							<TR>
								<TD width="10%"><ct:FWLabel key="ECRAN_DOS_DE_ETAT"/></TD>
								<TD width="40%">
									<%=viewBean.getSession().getCodeLibelle(viewBean.getCsEtat())%>
									<INPUT type="hidden" name="csEtat" value="<%=viewBean.getCsEtat()%>">
								</TD>
							</TR>
							<TR>
								<TD width="10%"><ct:FWLabel key="ECRAN_DOS_DE_DOMAINE"/></TD>
								<TD width="40%">
									<%=viewBean.getLibelleDomaine()%>
									<INPUT type="hidden" name="idDomaine" value="<%=viewBean.getIdDomaine()%>">
								</TD>
							</TR>									
						</table>
					</td>
				</tr>
				<tr><td colspan="6"><br/></td></tr>		
  				<tr>
					<td colspan="6" class="areaGlobazBlue">
					    <table width="100%">							
							<TR>
								<TD width="30%"><ct:FWLabel key="ECRAN_DOS_DE_IS_URGENT"/></TD>
								<TD><input type="checkbox" name="isUrgent" value="on" <%=viewBean.getIsUrgent().booleanValue()?"CHECKED":""%>></TD>
							</TR>
						</table>
					</td>
				</tr>	
				<tr><td colspan="6"><br/></td></tr>				
  				<tr>
					<td colspan="6" class="areaGlobazBlue">
					    <table width="100%">
							<TR>
								<TD width="30%">Groupe</TD>
								<TD colspan="3">							
									<select name="idGroupe" id="idGroupe" onChange="updateUsers(this.value)">
										<%for (int i=0; i < groupes.length; i = i+2){%>
											<OPTION value="<%=groupes[i]%>" <%= groupes[i].equals(viewBean.getIdGroupe())?"selected":"" %>><%=groupes[i+1]%></OPTION>
										<% } %>
									</select>									
								</TD>
							</TR>
					    <table width="100%">
							<TR>
								<TD width="30%"><ct:FWLabel key="ECRAN_DOS_DE_UTILISATEUR"/></TD>
								<TD colspan="3">							
									<select name="idGestionnaire" id="idGestionnaire">
										<%for (int i=0; i < users.length; i = i+2){%>
											<OPTION value="<%=users[i]%>" <%= users[i].equals(viewBean.getIdGestionnaire())?"selected":"" %>><%=users[i+1]%></OPTION>
										<% } %>
									</select>									
								</TD>
							</TR>
							<TR>
								<TD><ct:FWLabel key="ECRAN_DOS_DE_MODIF_ECHEANCES"/></TD>
								<TD><INPUT type="checkbox" name="isModifEcheances" value="on"></TD>
							</TR>																
						</table>
					</td>
				</tr>
				<tr><td colspan="6"><br/></td></tr>
  				<tr>
					<td colspan="6" class="areaGlobazBlue">
					    <table width="100%">
							<TR>
								<TD width="30%"><a href="<%=request.getContextPath() +"/libra?userAction="+ globaz.libra.servlet.ILIActions.ACTION_DOSSIERS+".rediriger&selectedId="+ viewBean.getIdDossier() %>" class="external_link"><ct:FWLabel key="ECRAN_DOS_DE_DOSSIER"/> <%= viewBean.getLibelleDomaine() %></a></TD>
								<TD colspan="3">
								</TD>
							</TR>															
						</table>
					</td>
				</tr>
			</TABLE>
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