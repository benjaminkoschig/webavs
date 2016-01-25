<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
		<%@ page import="globaz.commons.nss.*"%>
		<%@page import="globaz.jade.client.util.JadeStringUtil"%>

<%@page import="java.util.Vector"%><script type="text/javascript"
			src="<%=servletContext%>/scripts/nss.js"></script>
		<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%
	idEcran = "GSF0003";
	globaz.hera.vb.famille.SFApercuRelationFamilialeRequerantViewBean viewBean = new globaz.hera.vb.famille.SFApercuRelationFamilialeRequerantViewBean();
	globaz.globall.db.BSession bSession = (globaz.globall.db.BSession) ((globaz.framework.controller.FWController) session.getAttribute("objController")).getSession();
	viewBean.setISession(bSession);
	String idMembreFamilleDepuisRelFam = request.getParameter("idMembreFamilleDepuisRelFam");
	String idMembreFamille = null; //request.getParameter("idMembreFamille"); Il n'est pas permis de charger cette page avec seulement idMembreFamille
	boolean hasRequerant = false;
	boolean hasError = false;
	
	globaz.hera.db.famille.SFRequerantDTO requerant = null;
	if (globaz.globall.util.JAUtil.isStringEmpty(idMembreFamilleDepuisRelFam)) {
				
		// pas de membre selectionné, on recherche dans le DTO
		requerant = viewBean.getRequerantDTO(session);
		if (requerant != null) {
			viewBean.setParameters(requerant);
			idMembreFamille = requerant.getIdMembreFamille();
			hasRequerant = true;
		} else {
			// Erreur l'action a été appelé sans parametre idMembreFamilleDepuisRelFam, ni requerantDTO dans la session
			hasError = true;
			bButtonFind = false;
			bButtonNew = false;
			%><SCRIPT>alert("<%=bSession.getLabel("JSP_APERCU_CONJOINT_ERROR_NO_MEMBRE_REQUERANT")%>");</SCRIPT><%
		}
	} else {
		// idMembreFamilleDepuisRelFam est donné
		idMembreFamille = idMembreFamilleDepuisRelFam;
		requerant = viewBean.getRequerantDTO(session);
		if (requerant != null && requerant.getIdMembreFamille().equals(idMembreFamilleDepuisRelFam)) {
			//Vérifie s'il s'agit du requérant pour pouvoir donner des droits supplémentaires
			hasRequerant = true;
			viewBean.setParameters(requerant);			
		} else {
			viewBean.retrieveMembre(idMembreFamille);
		}
	}
	
	
		
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="sf-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="sf-optionsempty"/>

<SCRIPT>
function init(){
}


function changerDomaine(elm){	
	<%if (requerant!=null) {%>
		document.forms[0].elements('userAction').value='hera.famille.apercuRelationFamilialeRequerant.changerDomaineRequerant';    
    	document.forms[0].elements('csDomaine').value=elm.value;
    	document.forms[0].elements('selectedId').value="<%=requerant.getIdTiers()%>";
    	document.forms[0].target = "fr_main";
    	document.forms[0].submit();
    <%}%>
}



	usrAction = "hera.famille.apercuRelationConjoint.lister";
	<%=hasError?"bFind = false;":"bFind = true;"%>
	detailLink= <%=hasRequerant?"servlet+'?userAction=hera.famille.apercuRelationConjoint.afficher&_method=add'":"'about:blank'"%>;	
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><% if (hasRequerant) { %>
														<ct:FWLabel key='JSP_APERCU_CONJOINT_CONJOINT_REQUERANT'/>
													<% } else { %>
														<ct:FWLabel key='JSP_APERCU_CONJOINT_CONJOINT_MEMBRE'/>
													<% } %><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

						<%if(!globaz.globall.util.JAUtil.isStringEmpty(request.getParameter("message"))){%>
							<tr>
								<TD colspan="6">
									<font color="#FF0000"><b><%=request.getParameter("message")%></b></font>
								</TD>
							</tr>
						<%}%>


						<%if(session.getAttribute(globaz.hera.tools.SFSessionDataContainerHelper.KEY_VALEUR_RETOUR)!=null){
						%>
							<tr>
								<td><a href="<%=servletContext + mainServletPath + "?userAction=" +  globaz.hera.servlet.ISFActions.ACTION_RELATION_FAMILIALE_REQUERANT + ".quitterApplication"%>"><ct:FWLabel key="JSP_URL_FROM"/></a></td>
	 							<td>&nbsp;</td> 
								<td>&nbsp;</td>	
	 							<td>&nbsp;</td>
								<td>&nbsp;</td>																						
							</tr>
						<%
						}%>



						
							<tr>
							<td><ct:FWLabel key="JSP_SF_DOMAINE"/></td>
							<td>&nbsp;</td>								
							<td><input type="text" name="dummy" value="<%=objSession.getCodeLibelle(viewBean.getCsDomaineApplication())%>" disabled/></td>							
 							<td>&nbsp;</td>
							<td>&nbsp;</td>
						
							<%if (hasRequerant) {%>
								<td><ct:FWLabel key="JSP_SF_DOMAINE"/></td>
								<td>
										<input type="hidden" name="selectedId" value=""/>
										<%Vector v = viewBean.getCsDomaines();%>
										<ct:FWListSelectTag data="<%=v%>" 
															defaut="<%=requerant.getIdDomaineApplication()%>" 
															name="csDomaine"/>
	
											<script language="javascript">
										 	  	element = document.getElementById("csDomaine");
										 	  	element.onchange=function() {changerDomaine(this);}									 	  										 	  
										 	</script>
	
	
										</td>																						
								</tr>						
							<%} else {%>
 								<td>&nbsp;</td>
								<td>&nbsp;</td>
							<%} %>
						<tr>
							<td><ct:FWLabel key="JSP_MEMBRE_FAMILLE_NOMPRENOM"/></td>
							<td>&nbsp;</td>

						<%if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getDateDeces())) {%>		

								<td><input type="text" name="likeRequerant"  value="<%=viewBean.getNom()+" "+viewBean.getPrenom()%>" size="<%=viewBean.getNom().length()+viewBean.getPrenom().length()+5%>" disabled />
									<span style="font-family:wingdings">U</span> <%=viewBean.getDateDeces()%>
								</td>		
						<%} else {%>
								<td><input type="text" name="likeRequerant"  value="<%=viewBean.getNom()+" "+viewBean.getPrenom()%>" size="<%=viewBean.getNom().length()+viewBean.getPrenom().length()+5%>" disabled /></td>
						<%} %>
							<td><ct:FWLabel key="JSP_MEMBRE_FAMILLE_DATEN"/></td>
							<td><input type="text" name="dateNaissance" value="<%=viewBean.getDateNaissance()%>" disabled="disabled"/></td>
							<td><ct:FWLabel key="JSP_MEMBRE_FAMILLE_SEXE"/></td>
							<td><input type="text" name="sexe" value="<%=viewBean.getLibelleSexe() %>" disabled="disabled"/></td>

							
						</tr>
						<tr>	
							<td><ct:FWLabel key="JSP_MEMBRE_FAMILLE_NUMAVS"/></td>
							<td>&nbsp;</td>
							<td><input type="text" name="likeNoAvs" value="<%=viewBean.getVisibleNoAvs()%>" size="<%=viewBean.getVisibleNoAvs().length()+2%>" disabled/></td>
							<td><ct:FWLabel key="JSP_PERIODE_PAYS"/></td>
							<TD><input type="text" name="pays" value="<%=viewBean.getLibellePays() %>" disabled="disabled"/>
							    <input type="hidden" name="forIdConjoint" value="<%=idMembreFamille%>" />
							    <%if(!JadeStringUtil.isBlankOrZero(viewBean.getIdTiers())){%>
									<A href="<%=request.getContextPath()%>\pyxis?userAction=pyxis.tiers.tiers.afficher&selectedId=<%=viewBean.getIdTiers()%>" class="external_link">Tiers</A>    
								<%}%>
							</td>
							
						</tr>		
						
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>