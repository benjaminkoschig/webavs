
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<!-- Creer l'enregistrement si il n'existe pas -->
<%@ taglib uri="/WEB-INF/naos.tld" prefix="naos" %>
<%
	idEcran = "CAF0005";
	globaz.naos.db.adhesion.AFAdhesionViewBean viewBean = (globaz.naos.db.adhesion.AFAdhesionViewBean)session.getAttribute ("viewBean");	
	String method = request.getParameter("_method");
%>
<SCRIPT language="JavaScript">
</SCRIPT> 
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">


function add() {
	document.forms[0].elements('userAction').value="naos.adhesion.adhesion.ajouter"
}

function upd() {
}

function validate() {
    state = validateFields();
    
	var exit = true;
	var newTypeAdhesion = <%=viewBean.getTypeAdhesion()%>;
	document.forms[0].elements('userAction').value="naos.adhesion.adhesion.modifier";
	
	if (document.forms[0].elements('_method').value == "add"){
		
		if (newTypeAdhesion == <%=globaz.naos.translation.CodeSystem.TYPE_ADHESION_ASSOCIATION%> || newTypeAdhesion == <%=globaz.naos.translation.CodeSystem.TYPE_ADHESION_AGENCE%>){
			document.forms[0].elements('userAction').value="naos.adhesion.adhesion.ajouter";
		} else {
			document.forms[0].elements('userAction').value="naos.adhesion.adhesion.afficherSelectionCotisation";
		}
	}else
        document.forms[0].elements('userAction').value="naos.adhesion.adhesion.modifier";
	return (exit && state);
}

function cancel() {
	if (document.forms[0].elements('_method').value == "add")
 		document.forms[0].elements('userAction').value="back";
 	else
  		document.forms[0].elements('userAction').value="naos.adhesion.adhesion.afficher";
}

function del() {
	if (window.confirm("Vous êtes sur le point de supprimer une adhesion! Voulez-vous continuer?")) {
		document.forms[0].elements('userAction').value="naos.adhesion.adhesion.supprimer";
		document.forms[0].submit();
	}
}

function init(){
	var newTypeAdhesion = <%=viewBean.getTypeAdhesion()%>;
	//alert(newTypeAdhesion);
	if (newTypeAdhesion == <%=globaz.naos.translation.CodeSystem.TYPE_ADHESION_CAISSE%> || newTypeAdhesion == <%=globaz.naos.translation.CodeSystem.TYPE_ADHESION_CAISSE_PRINCIPALE%> || newTypeAdhesion == <%=globaz.naos.translation.CodeSystem.TYPE_ADHESION_AFFILIATION%>) {
		document.all('assocDisplay').style.display 	= 'none';
		document.all('caisseDisplay').style.display	= 'block';
		//document.all('caissePrincipaleDisplay').style.display= 'none';
	} else if (newTypeAdhesion == <%=globaz.naos.translation.CodeSystem.TYPE_ADHESION_ASSOCIATION%> || newTypeAdhesion == <%=globaz.naos.translation.CodeSystem.TYPE_ADHESION_AGENCE%>) {
		document.all('caisseDisplay').style.display	= 'none';
		document.all('assocDisplay').style.display	= 'block';
		//document.all('caissePrincipaleDisplay').style.display= 'none';
		//document.all('forGenreAdministration').value = "509029";
	} // else if (newTypeAdhesion == <%=globaz.naos.translation.CodeSystem.TYPE_ADHESION_CAISSE_PRINCIPALE%>) {
		//document.all('caisseDisplay').style.display	= 'none';
		 //document.all('assocDisplay').style.display	= 'none';
		//document.all('caissePrincipaleDisplay').style.display= 'block';
		//document.all('forGenreAdministration').value = "509028";
	//}
}

function onChangeTypeAdhesion(){
	var newTypeAdhesion = document.forms[0].elements('typeAdhesion').value;
	//alert(newTypeAdhesion);
	if (newTypeAdhesion == <%=globaz.naos.translation.CodeSystem.TYPE_ADHESION_CAISSE%> || newTypeAdhesion == <%=globaz.naos.translation.CodeSystem.TYPE_ADHESION_CAISSE_PRINCIPALE%> || newTypeAdhesion == <%=globaz.naos.translation.CodeSystem.TYPE_ADHESION_AFFILIATION%>) {
		document.all('assocDisplay').style.display 	= 'none';
		document.all('caisseDisplay').style.display	= 'block';
		//document.all('caissePrincipaleDisplay').style.display= 'none';
	} else if (newTypeAdhesion == <%=globaz.naos.translation.CodeSystem.TYPE_ADHESION_ASSOCIATION%> || newTypeAdhesion == <%=globaz.naos.translation.CodeSystem.TYPE_ADHESION_AGENCE%>) {
		document.all('caisseDisplay').style.display	= 'none';
		document.all('assocDisplay').style.display	= 'block';
		//document.all('caissePrincipaleDisplay').style.display= 'none';
		//document.all('forGenreAdministration').value = "509029";
	} //else if (newTypeAdhesion == <%=globaz.naos.translation.CodeSystem.TYPE_ADHESION_CAISSE_PRINCIPALE%>) {
		//document.all('caisseDisplay').style.display	= 'none';
		//document.all('assocDisplay').style.display	= 'none';
		//document.all('caissePrincipaleDisplay').style.display= 'block';
		//document.all('forGenreAdministration').value = "509028";
	//}
}

</SCRIPT> 
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
			Adh&eacute;sion - D&eacute;tail
			<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
						<naos:AFInfoAffiliation affiliation="<%=viewBean.getAffiliation()%>"/>
						<TR>
							<TD nowrap  height="11" colspan="4"> 
								<hr size="3" width="100%">
							</TD>
						</TR>
						<TR>
							<TD nowrap width="125" colspan="3">&nbsp;
								<INPUT type="hidden" name="selectedId" value='<%=viewBean.getAdhesionId()%>'>
								<INPUT type="hidden" name="affiliationId" value='<%=viewBean.getAffiliationId()%>'>
								<INPUT type="hidden" name="planCaisseId" value='<%=viewBean.getPlanCaisseId()%>'>
								<INPUT type="hidden" name="idTiers" value='<%=viewBean.getIdTiers()%>'>
							</TD>
						</TR>
						<TR>
							<TD nowrap width="157" height="14">Date D&eacute;but</TD>
							
							<TD nowrap width="269"> 
								<ct:FWCalendarTag name="dateDebut" doClientValidation="CALENDAR" value="<%=viewBean.getDateDebut()%>"/>
							</TD>
						</TR>	
						<TR>
							<TD nowrap width="157" height="14">Date Fin</TD>
							
							<TD nowrap width="269"> 
								<ct:FWCalendarTag name="dateFin" doClientValidation="CALENDAR" value="<%=viewBean.getDateFin()%>"/>
							</TD>
						</TR>	
						<TR>
							
						<TD nowrap width="157" height="14">Type d'adhesion</TD>
							
	                		<TD nowrap height="31" colspan="1">
	                			<% if (method != null && method.equalsIgnoreCase("add")) { %>
	                			<ct:FWCodeSelectTag 
	                				name="typeAdhesion" 
									defaut="<%=viewBean.getTypeAdhesion()%>"
									codeType="VETYPEADHE"/> 
								<script>
									document.getElementsByName('typeAdhesion')[0].onchange = function() {onChangeTypeAdhesion();};
								</script>
								<% } else if(globaz.naos.translation.CodeSystem.TYPE_ADHESION_CAISSE.equals(viewBean.getTypeAdhesion()) || globaz.naos.translation.CodeSystem.TYPE_ADHESION_CAISSE_PRINCIPALE.equals(viewBean.getTypeAdhesion())) { 
									java.util.HashSet except = new java.util.HashSet();
									except.add(globaz.naos.translation.CodeSystem.TYPE_ADHESION_ASSOCIATION);
									except.add(globaz.naos.translation.CodeSystem.TYPE_ADHESION_AFFILIATION);
									except.add(globaz.naos.translation.CodeSystem.TYPE_ADHESION_AGENCE);
								%>
								<ct:FWCodeSelectTag 
	                				name="typeAdhesion" 
									defaut="<%=viewBean.getTypeAdhesion()%>"
									codeType="VETYPEADHE"
									except="<%=except%>"/>
								<% } else { %>
									<INPUT name="typeAdhesionReadOnly"  type="text" size="30"
										value="<%=globaz.naos.translation.CodeSystem.getLibelle(session, viewBean.getTypeAdhesion())%>" tabindex="-1" class="disabled" readOnly>																
								<% } %>
	                		</TD>
						</TR>
						<TR>
							<TD nowrap width="125" colspan="3">&nbsp;</TD>
						</TR>


							
						<TR>
							<%
								String 			caisseNom = "";
								String 			nomPlan = "";
								//StringBuffer 	caisseLocaliteLong = new StringBuffer();
								//String 			caisseCantonDomicile = "";

								String 			associationNom = "";
								StringBuffer 	associationLocaliteLong = new StringBuffer();
								//String 			associationCantonDomicile = "";
								
								
								// CAISSE
								if (viewBean.getAdministrationCaisse() != null) {
									caisseNom = viewBean.getAdministrationCaisse().getCodeAdministration() + " - " + viewBean.getAdministrationCaisse().getNom();
									nomPlan = viewBean.getPlanCaisse().getLibelle();
									//caisseLocaliteLong = new StringBuffer(viewBean.getAdministrationCaisse().getAdresseAsString());
									
			              			//caisseLocaliteLong = new StringBuffer(viewBean.getAdministrationCaisse().getRue().trim());
			              			//if (caisseLocaliteLong.length() != 0) {
			              			//	caisseLocaliteLong = caisseLocaliteLong.append(", ");
			              			//}
			              			//caisseLocaliteLong.append(viewBean.getAdministrationCaisse().getLocaliteLong());
			              			
			              			//caisseCantonDomicile = viewBean.getAdministrationCaisse().getCantonDomicile();
								}
								
								// ASSOCIATION ou caisse principale
								if (viewBean.getAdminstrationAssocia() != null) {
									associationNom = viewBean.getAdminstrationAssocia().getNom();
									
									associationLocaliteLong = new StringBuffer(viewBean.getAdminstrationAssocia().getLocaliteLong());
									
			              			//associationLocaliteLong = new StringBuffer(viewBean.getAdminstrationAssocia().Rue().trim());
			              			//if (associationLocaliteLong.length() != 0) {
			              			//	associationLocaliteLong = associationLocaliteLong.append(", ");
			              			//}
			              			//associationLocaliteLong.append(viewBean.getAdminstrationAssocia().getLocaliteLong());
			              			
			              			//associationCantonDomicile = viewBean.getAdminstrationAssocia().getCantonDomicile();
								}
								
								
							%>
							<%	if (viewBean.getAdministrationCaisse() != null) { %>
            				<TD nowrap width="120"><A href="<%=request.getContextPath()%>\pyxis?userAction=pyxis.tiers.tiers.diriger&selectedId=<%=viewBean.getAdministrationCaisse().getIdTiers()%>">Adhère à</A></TD>
							<% 	} else if (viewBean.getAdminstrationAssocia() != null) { %>
            				<TD nowrap width="120"><A href="<%=request.getContextPath()%>\pyxis?userAction=pyxis.tiers.tiers.diriger&selectedId=<%=viewBean.getAdminstrationAssocia().getIdTiers()%>">Adhère à</A></TD>
            				<% 	} else { %>
            				<TD nowrap width="120">Adhère à</TD>							
							<%  } %>
            				
							<TD nowrap width="500">
							<INPUT type="hidden" name="forGenreAdministration" value="">
								<TABLE border="0" cellspacing="0" cellpadding="0" id="caisseDisplay">
									<TBODY> 
									<TR><TD> 
										<INPUT type="text" name="nom" size="60" maxlength="60" value="<%=caisseNom%>" tabindex="-1" class="disabled" readOnly>
					            		<INPUT type="text" name="nomPlan" size="60" maxlength="60" value="<%=nomPlan%>" tabindex="-1" class="disabled" readOnly>
						    			
						    			<% if (method != null && method.equalsIgnoreCase("add")) { 
											Object[] caisseMethods= new Object[]{
												new String[]{"setPlanCaisseId","getPlanCaisseId"}
											};
					              		%>
					              		<ct:FWSelectorTag 
											name="caisseSelector" 
											
											methods="<%=caisseMethods%>"
											providerApplication ="naos"
											providerPrefix="AF"
											providerAction ="naos.planCaisse.planCaisse.chercher"/> 
										<% } %>
									</TD></TR> 
									<% if (method != null && method.equalsIgnoreCase("add")) { %>
									<TR>
									<TD>
										<select name="planAffiliationId">
											<%=globaz.naos.util.AFUtil.getPlanAffiliation(viewBean.getAffiliationId(), viewBean.getPlanAffiliationId(), session, false)%>
										</select>
									</TD>
									</TR>
									<% } %>
									</TBODY> 
								</TABLE>
								<TABLE border="0" cellspacing="0" cellpadding="0" id="assocDisplay">
									<TBODY> 
									<TR><TD> 
										<INPUT type="text" name="nom" size="60" maxlength="60" value="<%=associationNom%>" tabindex="-1" class="disabled" readOnly>
					            		<INPUT type="text" name="localiteLong" size="60" maxlength="60" value="<%=associationLocaliteLong.toString()%>" tabindex="-1" class="disabled" readOnly>
						    			
						    			<% if (method != null && method.equalsIgnoreCase("add")) { 
											Object[] caisseMethods= new Object[]{
												new String[]{"setIdTiers","getIdTiersAdministration"}
											};
					              		%>
					              		
										<ct:FWSelectorTag 
											name="associationSelector" 
											
											methods="<%=caisseMethods%>"
											providerApplication ="pyxis"
											providerPrefix="TI"
											providerAction ="pyxis.tiers.administration.chercher"
											providerActionParams="<%=new Object[] {new String[] {\"forGenreAdministration\", \"selGenre\"}}%>"/> 
										<% } %>
									</TD></TR> 
									</TBODY> 
								</TABLE>
								<!--TABLE border="0" cellspacing="0" cellpadding="0" id="caissePrincipaleDisplay">
									<TBODY> 
									<TR><TD> 
										<INPUT type="text" name="nom" size="60" maxlength="60" value="<%=associationNom%>" tabindex="-1" class="disabled" readOnly>
					            		<INPUT type="text" name="localiteLong" size="60" maxlength="60" value="<%=associationLocaliteLong.toString()%>" tabindex="-1" class="disabled" readOnly>
						    			
						    			<% if (method != null && method.equalsIgnoreCase("add")) { 
											Object[] caisseMethods= new Object[]{
												new String[]{"setIdTiers","getIdTiersAdministration"}
											};
					              		%>
					              	
										<ct:FWSelectorTag 
											name="associationSelector" 
											
											methods="<%=caisseMethods%>"
											providerApplication ="pyxis"
											providerPrefix="TI"
											providerAction ="pyxis.tiers.administration.chercher"
											providerActionParams="<%=new Object[] {new String[] {\"forGenreAdministration\", \"selGenre\"}}%>"/> 
										<% } %>
									</TD></TR> 
									</TBODY> 
								</TABLE-->
							</TD>
							<TD width="100"></TD>	
						</TR>	
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> 
<SCRIPT>
</SCRIPT> 
<%	} %>
	<ct:menuChange displayId="menu" menuId="AFMenuPrincipal"/>
	<ct:menuChange displayId="options" menuId="AFOptionsAdhesion" showTab="options">
		<ct:menuSetAllParams key="adhesionId" value="<%=viewBean.getAdhesionId()%>"/>
		<ct:menuSetAllParams key="planCaisseId" value="<%=viewBean.getPlanCaisseId()%>"/>
	</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>