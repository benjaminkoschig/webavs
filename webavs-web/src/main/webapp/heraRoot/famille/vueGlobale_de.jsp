
<%@page import="globaz.prestation.ged.PRGedHelper"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ page import="globaz.hera.helpers.famille.*"%>
<%@page import="globaz.hera.vb.famille.SFVueGlobaleViewBean"%>
<%@page import="java.util.Iterator"%> 
<%
 	idEcran = "GSF0008";
 	SFVueGlobaleViewBean viewBean = (SFVueGlobaleViewBean) request
 			.getAttribute("viewBean");
 	SFLiantVO liant = viewBean.getLiant();
 	if (liant == null) {
 		liant = new SFLiantVO(null);
 		liant.setMembreFamille(new SFMembreVO());
 		liant.getMembreFamille().setIdMembreFamille("-1");
 	}

 	

 	String gedNssFamilleParam = "";
 	
 	try {
	 	List<String> l = viewBean.getListNssMembresFamille();
	 	int i = 1;
		for (Iterator iterator = l.iterator(); iterator.hasNext();) {			
			String nss = (String) iterator.next();
			gedNssFamilleParam+="&amp;"+PRGedHelper.PARAM_NSS_MF + String.valueOf(i) + "=" + nss;
			i++;
		} 
 	} catch (Exception e) {
 		e.toString();
 	} 	 	
 	
 	
 	bButtonDelete = false;
 	bButtonUpdate = false;
 	bButtonNew = false;
 	bButtonCancel = false;
 	bButtonValidate = false;
 	List conjoints = viewBean.getConjointsDuLiant();
 	List conjointsExtend = viewBean.getConjointsDesConjointsDuLiant();

 	boolean isDisplayPeriode = false;
 	if (viewBean.getIsDisplayPeriode() != null
 			&& viewBean.getIsDisplayPeriode().booleanValue()) {
 		isDisplayPeriode = true;
 	}
 %>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@page import="java.util.Vector"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="java.util.List"%>
<%@page import="globaz.hera.vb.famille.SFRelationVO"%>
<%@page import="globaz.hera.vb.famille.SFConjointVO"%>
<%@page import="globaz.hera.vb.famille.SFMembreVO"%>


<%@page import="globaz.hera.tools.nss.SFUtil"%>
<%@page import="globaz.hera.servlet.ISFActions"%>


<%@page import="globaz.hera.vb.famille.SFPeriodeVO"%>
<%@page import="globaz.hera.vb.famille.SFLiantVO"%>
<%@page import="globaz.hera.api.ISFSituationFamiliale"%>

<%@page import="globaz.framework.bean.FWViewBean"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%><ct:menuChange displayId="menu" menuId="sf-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="sf-optionsempty"/>


<SCRIPT>
	var warningObj = new Object();
	warningObj.text = "";


	function init(){
		<%if (FWViewBeanInterface.WARNING.equals(viewBean.getMsgType())) {%>			
			showWarnings();						
		<%}%>
	}



	function showWarnings() {
		var warningObj = new Object();
		warningObj.text = "<%=viewBean.getMessage()%>"; 
		showModalDialog('<%=servletContext%>/warningModalDlg.jsp',warningObj,'dialogHeight:20;dialogWidth:25;status:no;resizable:no');
	}
	
	function add(){
	}
	
	function validate(){	
		return true;
	}	
	
	function del() {
	}
	
	function cancel(){
	}   
	
	function upd(){
	}





		
	
	function postInit(){		
		document.getElementById('csDomaine').disabled=false;
		document.getElementById('displayMode').disabled=false;
		document.getElementById('checkBoxDisplayPeriodes').disabled=false;
		document.getElementById('checkBoxDisplayParents').disabled=false;							
		document.getElementById('ajouterConjoint').disabled=false;
		document.getElementById('ajouterRequerant').disabled=false;	
							
	}


  function displayParents() {	  	
		if (document.getElementById('checkBoxDisplayParents').checked==true) {
			document.getElementById('parentsBlock').style.display='block';
		}
		else {
			document.getElementById('parentsBlock').style.display='none';
		}
  }
   
	
	function changeDisplayMode() {

			
			if (<%=isDisplayPeriode%>) {
				if (document.getElementById('displayPeriodes_<%=liant.getMembreFamille().getIdMembreFamille()%>')!=null)
					document.getElementById('displayPeriodes_<%=liant.getMembreFamille().getIdMembreFamille()%>').style.display='block';
			}
			else {
				if (document.getElementById('displayPeriodes_<%=liant.getMembreFamille().getIdMembreFamille()%>')!=null)
					document.getElementById('displayPeriodes_<%=liant.getMembreFamille().getIdMembreFamille()%>').style.display='none';				
			}
			
		
			<%for (int i = 0; i < conjoints.size(); i++) {
				SFConjointVO con = (SFConjointVO) conjoints.get(i);%>
				if (<%=isDisplayPeriode%>) {

					if (document.getElementById('displayGraph_<%=con.getKey()%>')!=null)
						document.getElementById('displayGraph_<%=con.getKey()%>').style.display='none';

					if (document.getElementById('displayList_<%=con.getKey()%>')!=null)
						document.getElementById('displayList_<%=con.getKey()%>').style.display='none';

					if (document.getElementById('displayPeriodes_<%=con.getIdMembreFamille1()%>')!=null)
						document.getElementById('displayPeriodes_<%=con.getIdMembreFamille1()%>').style.display='block';

					if (document.getElementById('displayPeriodes_<%=con.getIdMembreFamille2()%>')!=null)
						document.getElementById('displayPeriodes_<%=con.getIdMembreFamille2()%>').style.display='block';


					<%if (con.getEnfants() != null) {
					for (int j = 0; j < con.getEnfants().size(); j++) {
						SFMembreVO enf = (SFMembreVO) con.getEnfants().get(j);%>
							if (document.getElementById('displayPeriodes_<%=enf.getIdMembreFamille()%>')!=null)
								document.getElementById('displayPeriodes_<%=enf.getIdMembreFamille()%>').style.display='block';
					<%}
				}%>
					
				}
				else {	
					if (document.getElementById('displayPeriodes_<%=con.getIdMembreFamille1()%>')!=null)		
						document.getElementById('displayPeriodes_<%=con.getIdMembreFamille1()%>').style.display='none';
					if (document.getElementById('displayPeriodes_<%=con.getIdMembreFamille2()%>')!=null)
						document.getElementById('displayPeriodes_<%=con.getIdMembreFamille2()%>').style.display='none';
					<%if (con.getEnfants() != null) {
					for (int j = 0; j < con.getEnfants().size(); j++) {
						SFMembreVO enf = (SFMembreVO) con.getEnfants().get(j);%>
							if (document.getElementById('displayPeriodes_<%=enf.getIdMembreFamille()%>')!=null)
								document.getElementById('displayPeriodes_<%=enf.getIdMembreFamille()%>').style.display='none';
					<%}
				}%>


					
					if (document.getElementById('displayMode').checked==true) {
						if (document.getElementById('displayGraph_<%=con.getKey()%>')!=null)
							document.getElementById('displayGraph_<%=con.getKey()%>').style.display='block';

						if (document.getElementById('displayList_<%=con.getKey()%>')!=null)
							document.getElementById('displayList_<%=con.getKey()%>').style.display='none';						
					}
					else {
						if (document.getElementById('displayGraph_<%=con.getKey()%>')!=null)
							document.getElementById('displayGraph_<%=con.getKey()%>').style.display='none';
						if (document.getElementById('displayList_<%=con.getKey()%>')!=null)
							document.getElementById('displayList_<%=con.getKey()%>').style.display='block';
					}
				}		
			<%}%>			
			<%for (int i = 0; i < conjointsExtend.size(); i++) {
				SFConjointVO con = (SFConjointVO) conjointsExtend.get(i);%>
				
			if (<%=isDisplayPeriode%>) {
				if (document.getElementById('displayGraph_<%=con.getKey()%>')!=null)
					document.getElementById('displayGraph_<%=con.getKey()%>').style.display='none';
				if (document.getElementById('displayList_<%=con.getKey()%>')!=null)
					document.getElementById('displayList_<%=con.getKey()%>').style.display='none';
				if (document.getElementById('displayPeriodes_exc<%=con.getIdMembreFamille1()%>')!=null)
					document.getElementById('displayPeriodes_exc<%=con.getIdMembreFamille1()%>').style.display='block';
				if (document.getElementById('displayPeriodes_exc<%=con.getIdMembreFamille2()%>')!=null)
					document.getElementById('displayPeriodes_exc<%=con.getIdMembreFamille2()%>').style.display='block';

				<%if (con.getEnfants() != null) {
					for (int j = 0; j < con.getEnfants().size(); j++) {
						SFMembreVO enf = (SFMembreVO) con.getEnfants().get(j);%>
					if (document.getElementById('displayPeriodes_exc<%=enf.getIdMembreFamille()%>')!=null)
						document.getElementById('displayPeriodes_exc<%=enf.getIdMembreFamille()%>').style.display='block';
				<%}
				}%>
				
				
			}
			else {
				if (document.getElementById('displayPeriodes_exc<%=con.getIdMembreFamille1()%>')!=null)
					document.getElementById('displayPeriodes_exc<%=con.getIdMembreFamille1()%>').style.display='none';
				if (document.getElementById('displayPeriodes_exc<%=con.getIdMembreFamille2()%>')!=null)
					document.getElementById('displayPeriodes_exc<%=con.getIdMembreFamille2()%>').style.display='none';

				<%if (con.getEnfants() != null) {
					for (int j = 0; j < con.getEnfants().size(); j++) {
						SFMembreVO enf = (SFMembreVO) con.getEnfants().get(j);%>
						if (document.getElementById('displayPeriodes_exc<%=enf.getIdMembreFamille()%>')!=null)
							document.getElementById('displayPeriodes_exc<%=enf.getIdMembreFamille()%>').style.display='none';
				<%}
				}%>
					
				if (document.getElementById('displayMode').checked==true) {
					if (document.getElementById('displayGraph_<%=con.getKey()%>')!=null)
						document.getElementById('displayGraph_<%=con.getKey()%>').style.display='block';
					if (document.getElementById('displayList_<%=con.getKey()%>')!=null)
						document.getElementById('displayList_<%=con.getKey()%>').style.display='none';
				}
				else {
					if (document.getElementById('displayGraph_<%=con.getKey()%>')!=null)
						document.getElementById('displayGraph_<%=con.getKey()%>').style.display='none';
					if (document.getElementById('displayList_<%=con.getKey()%>')!=null)
						document.getElementById('displayList_<%=con.getKey()%>').style.display='block';
				}
			}		
			<%}%>			
	}
	
  function displayFastLink(event, idMembreFamille, idRelationConjoint) {
	    
	  	document.all('idMembreFamille').value = idMembreFamille;
	  	document.all('selectedId').value = idMembreFamille;
	  	document.all('idRelationConjoint').value = idRelationConjoint;

	  	var x = event.clientX;
		var y = event.clientY;
	
		var offSetX = -5;
		var offSetY = 0;
		x = x-offSetX;
		y = y-offSetY;
		  
		document.all('popupFastLink').style.top = y + 'px';
		document.all('popupFastLink').style.left = x + 'px';
		  
		if (document.all('popupFastLink').style.display == 'block') {
			document.all('popupFastLink').style.display = 'none';    
		}
		else if (document.all('popupFastLink').style.display == 'none') {
			document.all('popupFastLink').style.display = 'block';  	
		}    
	}


  
  function displayFastLinkConjointInconnu(event, idMembreFamille, idRelationConjoint) {
	    
	  	document.all('idMembreFamille').value = idMembreFamille;
	  	document.all('selectedId').value = idMembreFamille;
	  	document.all('idRelationConjoint').value = idRelationConjoint;

	  	var x = event.clientX;
		var y = event.clientY;
	
		var offSetX = -5;
		var offSetY = 0;
		x = x-offSetX;
		y = y-offSetY;
		  
		document.all('popupFastLinkCI').style.top = y + 'px';
		document.all('popupFastLinkCI').style.left = x + 'px';
		  
		if (document.all('popupFastLinkCI').style.display == 'block') {
			document.all('popupFastLinkCI').style.display = 'none';    
		}
		else if (document.all('popupFastLinkCI').style.display == 'none') {
			document.all('popupFastLinkCI').style.display = 'block';  	
		}    
	}

  
  function displayFastLinkParentsEnfants(event, idMembreFamille) {
	    
	  	document.all('idMembreFamille').value = idMembreFamille;
	  	document.all('selectedId').value = idMembreFamille;
	  	

	  	var x = event.clientX;
		var y = event.clientY;
	
		var offSetX = -5;
		var offSetY = 0;
		x = x-offSetX;
		y = y-offSetY;
		  
		document.all('popupFastLinkEnfants').style.top = y + 'px';
		document.all('popupFastLinkEnfants').style.left = x + 'px';
		  
		if (document.all('popupFastLinkEnfants').style.display == 'block') {
			document.all('popupFastLinkEnfants').style.display = 'none';    
		}
		else if (document.all('popupFastLinkEnfants').style.display == 'none') {
			document.all('popupFastLinkEnfants').style.display = 'block';  	
		}    
	}


	function actionListerPeriodes() {
		  document.forms[0].elements('userAction').value="<%=ISFActions.ACTION_PERIODE%>.chercher";	  		  		    
		  document.forms[0].submit();

	}


	function actionDisplayPeriodes() {
			<%if (liant.isRequerant()) {%>
				document.forms[0].elements('userAction').value="<%=ISFActions.ACTION_VUE_GLOBALE%>.afficherPeriodesVGRequerant";
			<%} else {%>
				document.forms[0].elements('userAction').value="<%=ISFActions.ACTION_VUE_GLOBALE%>.afficherPeriodesVGMembreFamille";
			<%}%>
		  document.all('idMembreFamille').value = <%=liant.getMembreFamille().getIdMembreFamille()%>; 		 
		  document.forms[0].submit();
	}	



	function resetProvevance() {
			<%if (liant!=null && liant.isRequerant()) {%>
				document.forms[0].elements('userAction').value="<%=ISFActions.ACTION_VUE_GLOBALE%>.initialiserProvenance";
				document.forms[0].submit();
			<%}%>
	}
	

  function actionAfficherDetailMembre(idMembreFamille) {
	  document.forms[0].elements('userAction').value="hera.famille.apercuRelationFamilialeRequerant.afficher";
	  document.forms[0].elements('selectedId').value=idMembreFamille;
	  document.forms[0].submit();
  }
  
  function actionSelectionnerRequerant() {

	  document.forms[0].elements('userAction').value="<%=ISFActions.ACTION_RELATION_FAMILIALE_REQUERANT%>.selectionnerRequerant";
	  document.forms[0].submit();		 	  	  		
  }

  function actionAfficherDetailRelation(idRelationConjoint) {
	  document.forms[0].elements('userAction').value="<%=ISFActions.ACTION_APERCU_RELATION_CONJOINT%>.afficher";
	  document.forms[0].elements('idRelationConjoint').value=idRelationConjoint;	  
	  document.forms[0].submit();
  }

  function actionAjouterConjoint() {
	  document.forms[0].elements('userAction').value="<%=ISFActions.ACTION_APERCU_RELATION_CONJOINT%>.afficher";
	  document.forms[0].elements('_method').value='add';
	  document.forms[0].elements('idRelationConjoint').value='';
	  document.forms[0].submit();
  }


  function changerDomainePourRequerant(elm){			
			document.forms[0].elements('userAction').value='hera.famille.apercuRelationFamilialeRequerant.changerDomaineRequerant';    
	    	document.forms[0].elements('csDomaine').value=elm.value;
	    	<%if (liant != null) {%>
	    		document.forms[0].elements('selectedId').value="<%=liant.getMembreFamille().getIdTiers()%>";
	    	<%}%>	
	    	document.forms[0].target = "fr_main";
	    	document.forms[0].submit();
	   
	}

  function changerDomainePourMembreFamille(elm){
	  				
		  document.forms[0].elements('userAction').value="hera.famille.apercuRelationFamilialeRequerant.changerDomaineMF";		  
		  document.forms[0].elements('csDomaine').value=elm.value;		  
	    	<%if (liant != null) {%>
    			document.forms[0].elements('selectedId').value="<%=liant.getMembreFamille().getIdTiers()%>";
	    	<%}%>	
		  
		  document.forms[0].submit();	    	   
	}

  
  function actionAjouterRequerant() {
	  document.forms[0].elements('userAction').value="<%=ISFActions.ACTION_RELATION_FAMILIALE_REQUERANT%>.afficher";
	  document.forms[0].elements('_method').value='add';
	  document.forms[0].submit();	 
  }

	function actionAfficherEnfants() {
		  document.forms[0].elements('userAction').value="hera.famille.apercuEnfants.chercher";
		  document.forms[0].elements('idRelationConjoint').value=document.all('idRelationConjoint').value;
		  document.forms[0].elements('idConjoint').value=document.all('idMembreFamille').value;
		  document.forms[0].submit();
	}

	function actionAfficherFamille() {
		  document.forms[0].elements('userAction').value="hera.famille.vueGlobale.afficherFamilleMembre";
		  document.forms[0].submit();
	}
		
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_SF_VUE_GLOBAL_TITLE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

						<%
							if (!globaz.globall.util.JAUtil.isStringEmpty(request
									.getParameter("message"))) {
						%>
							<tr>
								<TD colspan="7">
									<font color="#FF0000"><b><%=request.getParameter("message")%></b></font>
								</TD>
							</tr>
						<%
							}
							//Si liant est null, on arret le traitement ici !!!!
							if (liant.getMembreFamille().getIdMembreFamille() == "-1") {
								if (globaz.globall.util.JAUtil.isStringEmpty(request
										.getParameter("message"))) {
						%>
							
							<tr>
								<TD colspan="7">
									<font color="#FF0000"><b><ct:FWLabel key="JSP_SF_VUE_GLOBALE_REQ_NOT_FOUND"/></b></font>
								</TD>
							</tr>
	
							<%
									}
									} else {
								%>
							<!-- --------------------------Liant info----------------------------- -->
							



							
						<%
																					if (session
																								.getAttribute(globaz.hera.tools.SFSessionDataContainerHelper.KEY_VALEUR_RETOUR) != null) {
																				%>
							<tr>
								<td colspan="7"><a accesskey="R" href='<%=servletContext
									+ mainServletPath
									+ "?userAction="
									+ globaz.hera.servlet.ISFActions.ACTION_RELATION_FAMILIALE_REQUERANT
									+ ".quitterApplication"%>')><ct:FWLabel key="JSP_URL_FROM"/></a> (alt+R) <ct:FWLabel key="JSP_SF_PUIS_ENTER"/></td>
							</tr>
							<tr>
								<td colspan="7"><br/></td>
							</tr>														
						<%
																					}
																				%>


						<%
							if (liant != null && liant.isRequerant()) {
						%>
							
<tr>
	<td colspan="1" class="ongletLightBlue" width="10%">
							<table width="10%" border="0">
							<tr>
								<td colspan="7"><strong><ct:FWLabel key="JSP_SF_REQUERANT"/></strong></td>								
							</tr>
							</table>
	</td>
	
	<%if ("1".equals(liant.getProvenance())) {%>
		<td colspan="3">&nbsp;</td>
		<td colspan="3"> <strong>&nbsp;&nbsp;&nbsp;<span style="color:red;">
			<ct:FWLabel key="JSP_SF_ISSUE_REPRISE"/>
		</span></strong><a href="#" style="text-decoration:none;" onclick="javascript:resetProvevance();">&nbsp;&nbsp;&nbsp;<ct:FWLabel key="JSP_SF_VALIDER"/> >>></a>
		</td>
	<%} else { %>
		<td colspan="6"> </td>
	<%} %>
	
</tr>
						<%
							}
						%>

							<input type="hidden" name="idMembreFamille"/>
							<input type="hidden" name="idRelationConjoint"/>
							<input type="hidden" name="idConjoint"/>

<tr><td colspan="6" class="areaLightBlue" width="80%"><table>
							<tr>
								<td colspan="7" align="left">
								<table width="900px;" border="0">
									<tr>										
									<%
																				if (isDisplayPeriode) {
																			%>															
			 							<td colspan="3">
			 								<input type="hidden" name="displayMode" value=""/>
			 							</td>
									<%
										} else {
									%>
				 							<td><ct:FWLabel key="JSP_SF_VG_VUE_GRAPHIQUE"/></td>
				 							<td/>
				 							<td><input type="checkbox" name="displayMode" onclick="changeDisplayMode();"/></td>
									<%
										}
									%>										

										<td><ct:FWLabel key="JSP_SF_VG_VISU_PERIODE"/></td>	
										<td>
											<%
												if (isDisplayPeriode) {
											%>
												<input type="checkbox" name="checkBoxDisplayPeriodes" checked onclick="actionDisplayPeriodes();"/>
											<%
												} else {
											%>
												<input type="checkbox" name="checkBoxDisplayPeriodes" onclick="actionDisplayPeriodes();"/>
											<%
												}
											%>
										</td>
											<%
												if (viewBean.getParent1Liant() != null
															&& viewBean.getParent2Liant() != null) {
											%>
												<td><ct:FWLabel key="JSP_SF_VG_VISU_PARENTS"/></td>											
												 <td><input type="checkbox" name="checkBoxDisplayParents" onclick="displayParents();"/></td>
											<%
												} else {
											%>
												<td/>
												<td/>
											<%
												}
											%>											
									</tr>
									
									<tr>										
										<td><ct:FWLabel key="JSP_SF_DOMAINE"/></td>
										<td>&nbsp;</td>	
										
										<td><input type="text" name="visuDomaine" value="<%=liant.getMembreFamille()
								.getLibelleDomaine(objSession)%>" disabled/></td>							
										<td/>
										<td/>
										<%
											Vector v = liant.getCsDomaines();
												if (v != null && v.size() > 1) {
													if (liant.isRequerant()) {
										%>
												<td><ct:FWLabel key="JSP_SF_DOMAINE"/></td>
												<td align="left">										
														<ct:FWListSelectTag data="<%=v%>" 
																			defaut="<%=liant.getMembreFamille().getCsDomaine()%>" 
																			name="csDomaine"/>		
															<script language="javascript">												
														 	  	element = document.getElementById("csDomaine");
														 	  	element.onchange=function() {changerDomainePourRequerant(this)};
														 	</script>											 	
												</td>																			
												<%
																																} else {
																															%>
												<td><ct:FWLabel key="JSP_SF_DOMAINE"/></td>
												<td align="left">										
														
														<ct:FWListSelectTag data="<%=v%>" 
																			defaut="<%=liant.getMembreFamille().getCsDomaine()%>" 
																			name="csDomaine"/>		
															<script language="javascript">						
																document.getElementById("csDomaine").disabled="false";						
														 	  	element = document.getElementById("csDomaine");														 	  	
														 	  	element.onchange=function() {changerDomainePourMembreFamille(this)};
														 	</script>
												</td>
											<%
												}
													} else {
											%>
											<td colspan="2"><input type="hidden" name="csDomaine" value="<%=liant.getMembreFamille().getCsDomaine()%>"/></td>
										<%
											}
										%>																																	
									</tr>									
									<tr>
										<td valign="top"><ct:FWLabel key="JSP_MEMBRE_FAMILLE_NOMPRENOM"/></td>
										<td>&nbsp;</td>
			
									<%
													if (!globaz.jade.client.util.JadeStringUtil
																.isIntegerEmpty(liant.getMembreFamille().getDateDeces())) {
												%>		
			
											<td valign="top"><input type="text" name="likeRequerant"  value="<%=liant.getMembreFamille().getNom() + " "
							+ liant.getMembreFamille().getPrenom()%>" size="<%=liant.getMembreFamille().getNom()
											.length()
									+ liant.getMembreFamille().getPrenom()
											.length() + 5%>" disabled />
												<br/><strong><span style="color:red;">(<span style="font-family:wingdings">U</span> <%=liant.getMembreFamille().getDateDeces()%>)</span></strong>						
											</td>
										
									<%
																				} else {
																			%>
											<td valign="top"><input type="text" name="likeRequerant"  value="<%=liant.getMembreFamille().getNom() + " "
							+ liant.getMembreFamille().getPrenom()%>" size="<%=liant.getMembreFamille().getNom()
											.length()
									+ liant.getMembreFamille().getPrenom()
											.length() + 5%>" disabled /></td>
									<%
										}
									%>
										<td valign="top"><ct:FWLabel key="JSP_MEMBRE_FAMILLE_DATEN"/></td>
										<td valign="top"><input type="text" name="dateNaissance" value="<%=liant.getMembreFamille().getDateNaissance()%>" disabled="disabled"/></td>
										<td valign="top"><ct:FWLabel key="JSP_MEMBRE_FAMILLE_SEXE"/></td>
										<td align="left" valign="top"><input type="text" name="sexe" value="<%=liant.getMembreFamille().getLibelleSexe(objSession)%>" disabled="disabled"/></td>
									</tr>
									<tr>
										<td><ct:FWLabel key="JSP_MEMBRE_FAMILLE_NUMAVS"/></td>
										<td>&nbsp;</td>
										<td><input type="text" name="likeNoAvs" value="<%=liant.getMembreFamille().getNssFormatte()%>" size="<%=liant.getMembreFamille().getNssFormatte().length()
						+ 2%>" disabled/></td>
										<td><ct:FWLabel key="JSP_PERIODE_PAYS"/></td>
										<TD><input type="text" name="pays" value="<%=liant.getMembreFamille().getLibelleNationalite(
								objSession)%>" disabled="disabled"/>
										    <input type="hidden" name="forIdConjoint" value="<%=liant.getMembreFamille().getIdMembreFamille()%>" />
										    <%
										    	if (!JadeStringUtil.isBlankOrZero(liant.getMembreFamille()
										    				.getIdTiers())) {
										    %>
												<A href="<%=request.getContextPath()%>/pyxis?userAction=pyxis.tiers.tiers.afficher&selectedId=<%=liant.getMembreFamille().getIdTiers()%>" class="external_link">
													<ct:FWCodeLibelle csCode="<%=ISFSituationFamiliale.CS_PIXIS_TYPE_TIERS %>"/>
												</A>
												&nbsp;
												<A href="#" onclick="window.open('<%=servletContext%><%=("/hera")%>?userAction=<%=globaz.hera.servlet.ISFActions.ACTION_VUE_GLOBALE%>.actionAfficherDossierGed&amp;noAVSId=<%=liant.getMembreFamille().getNssFormatte()%>&amp;idTiersExtraFolder=<%=liant.getMembreFamille().getIdTiers()%><%=gedNssFamilleParam%>','GED_CONSULT')" >GED</A>    
											<%
    												}
    											%>
							    											
										</td>							
									</tr>	
									
								</table>
								</td>								
								
							</tr>
</table></td>
<td colspan="1"></td>
</tr>


						<!-- --------------------------Liant info----------------------------- -->


						<!-- --------------------------Affichage des parents----------------------- -->

						<tr>
							<td colspan="7" valign="top">
								<div id="displayPeriodes_<%=liant.getMembreFamille().getIdMembreFamille()%>" style="display:none;">
										
										<%
																					int width = 920;
																						if (viewBean.getWidth() < width) {
																							width = viewBean.getWidth();
																						}
																				%>
										
										
											<table border="0" bgcolor="#E8EEF4" width="<%=width%>px">											
											<%
																							List periodes = liant.getMembreFamille().getPeriodes();
																								if (periodes != null) {
																						%>
												
												<tr>
													<td colspan="8"><b><ct:FWLabel key="JSP_SF_VG_PERIODES"/></b></td>										
												</tr>												
												<%
																									for (int j = 0; j < periodes.size(); j++) {
																												SFPeriodeVO periode = (SFPeriodeVO) periodes.get(j);
																								%>
												  <tr>
													<td valign="top">													
														<%=SFConjointVO.startHtmlStyle()
								+ periode.getLibellePeriode()
								+ SFConjointVO.stopHtmlStyle()%>																												
													</td>
													<%
														String data = periode.getDetenteurBTE()
																			+ periode.getPays();
																	if (!JadeStringUtil.isBlankOrZero(data)) {
																		data = " (" + data + ")";
																	}
													%>
													<td valign="top"><%=SFConjointVO.startHtmlStyle()
								+ periode.getDateDebut()%> - <%=periode.getDateFin() + data
								+ SFConjointVO.stopHtmlStyle()%></td>													
												</tr>											  
											<%
											  												}
											  													}
											  											%>											
											</table>											
								</div>
								</td>
							</tr>		
							

						<%
																if (viewBean.getParent1Liant() != null
																			&& viewBean.getParent2Liant() != null) {
															%>
						
						<tr>
							<td colspan="7" valign="top">
								<div id="parentsBlock" style="display:none;">
									<table border="0" bgcolor="#E8EEF4" width="<%=width%>px">
										<tr>
											<td colspan="8"><b><ct:FWLabel key="JSP_SF_VG_PARENTS"/></b></td>										
										</tr>
										<tr>
											<td valign="top">
												<%
													if (!ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU
																	.equals(viewBean.getParent1Liant()
																			.getIdMembreFamille())) {
												%>
													<a href="#" id="toto" onclick="displayFastLinkParentsEnfants(event, <%=viewBean.getParent1Liant()
										.getIdMembreFamille()%>);" style="text-decoration:none;">
																				<IMG name="iii"  width="25px;" src="<%=request.getContextPath()%>/images/<%=viewBean.getParent1Liant().getImgName()%>" border="0">
													</a>
												<%
													} else {
												%>
																				<IMG name="iii"  width="25px;" src="<%=request.getContextPath()%>/images/darkVador.png" border="0">
												<%
													}
												%>
											</td>
											<td colspan="3" valign="top">


												<%if (objSession.hasRight("hera.famille.apercuRelationFamilialeRequerant.afficher", "ADD")) {%>
														<a style="text-decoration:none;" href="javascript:actionAfficherDetailMembre('<%=viewBean.getParent1Liant().getIdMembreFamille()%>');"><%=SFUtil
															.formatDetailMembreFamilleVueGlobale(
																	viewBean.getParent1Liant()
																			.getNssFormatte(), viewBean
																			.getParent1Liant().getNom()
																			+ " "
																			+ viewBean
																					.getParent1Liant()
																					.getPrenom(),
																	viewBean.getParent1Liant()
																			.getDateNaissance(),
																	viewBean.getParent1Liant()
																			.getDateDeces(), viewBean
																			.getParent1Liant()
																			.getCsSexe(), viewBean
																			.getParent1Liant()
																			.getCsNationalite())%>
														</a>

												<%} else {%>
														<%=SFUtil
															.formatDetailMembreFamilleVueGlobale(
																	viewBean.getParent1Liant()
																			.getNssFormatte(), viewBean
																			.getParent1Liant().getNom()
																			+ " "
																			+ viewBean
																					.getParent1Liant()
																					.getPrenom(),
																	viewBean.getParent1Liant()
																			.getDateNaissance(),
																	viewBean.getParent1Liant()
																			.getDateDeces(), viewBean
																			.getParent1Liant()
																			.getCsSexe(), viewBean
																			.getParent1Liant()
																			.getCsNationalite())%>												
												<%} %>
																																				
													
												<%
													if (!JadeStringUtil.isBlankOrZero(viewBean
																	.getParent1Liant().getIdTiers())) {
												%>
													<A href="<%=request.getContextPath()%>/pyxis?userAction=pyxis.tiers.tiers.afficher&selectedId=<%=viewBean.getParent1Liant().getIdTiers()%>" class="external_link">
													<ct:FWCodeLibelle csCode="<%=ISFSituationFamiliale.CS_PIXIS_TYPE_TIERS %>"/>
													</A>
													&nbsp;
													<A href="#" onclick="window.open('<%=servletContext%><%=("/hera")%>?userAction=<%=globaz.hera.servlet.ISFActions.ACTION_VUE_GLOBALE%>.actionAfficherDossierGed&amp;noAVSId=<%=viewBean.getParent1Liant().getNssFormatte()%>&amp;idTiersExtraFolder=<%=viewBean.getParent1Liant().getIdTiers()%><%=gedNssFamilleParam%>','GED_CONSULT')" >GED</A>
													
												<%
													}
												%>

											</td>									
									
											<td valign="top">												
												<%
																									if (!ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU
																													.equals(viewBean.getParent2Liant()
																															.getIdMembreFamille())) {
																								%>
													<a href="#" id="toto" onclick="displayFastLinkParentsEnfants(event, <%=viewBean.getParent2Liant()
										.getIdMembreFamille()%>);" style="text-decoration:none;">
													<IMG name="iii"  width="25px;" src="<%=request.getContextPath()%>/images/<%=viewBean.getParent2Liant().getImgName()%>" border="0">
													</a>
												<%
													} else {
												%>
														<IMG name="iii"  width="25px;" src="<%=request.getContextPath()%>/images/darkVador.png" border="0">
												<%
													}
												%>
											</td>
											<td colspan="3" valign="top">													

												<%if (objSession.hasRight("hera.famille.apercuRelationFamilialeRequerant.afficher", "ADD")) {%>
												<a style="text-decoration:none;" href="javascript:actionAfficherDetailMembre('<%=viewBean.getParent2Liant().getIdMembreFamille()%>');"><%=SFUtil
									.formatDetailMembreFamilleVueGlobale(
											viewBean.getParent2Liant()
													.getNssFormatte(), viewBean
													.getParent2Liant().getNom()
													+ " "
													+ viewBean
															.getParent2Liant()
															.getPrenom(),
											viewBean.getParent2Liant()
													.getDateNaissance(),
											viewBean.getParent2Liant()
													.getDateDeces(), viewBean
													.getParent2Liant()
													.getCsSexe(), viewBean
													.getParent2Liant()
													.getCsNationalite())%></a>

												<%} else {%>
<%=SFUtil
									.formatDetailMembreFamilleVueGlobale(
											viewBean.getParent2Liant()
													.getNssFormatte(), viewBean
													.getParent2Liant().getNom()
													+ " "
													+ viewBean
															.getParent2Liant()
															.getPrenom(),
											viewBean.getParent2Liant()
													.getDateNaissance(),
											viewBean.getParent2Liant()
													.getDateDeces(), viewBean
													.getParent2Liant()
													.getCsSexe(), viewBean
													.getParent2Liant()
													.getCsNationalite())%></a>
																									
												<%} %>
																			
												<%
													if (!JadeStringUtil.isBlankOrZero(viewBean
																	.getParent2Liant().getIdTiers())) {
												%>
													<A href="<%=request.getContextPath()%>/pyxis?userAction=pyxis.tiers.tiers.afficher&selectedId=<%=viewBean.getParent2Liant().getIdTiers()%>" class="external_link">
														<ct:FWCodeLibelle csCode="<%=ISFSituationFamiliale.CS_PIXIS_TYPE_TIERS %>"/>
													</A>
													&nbsp;
													<A href="#" onclick="window.open('<%=servletContext%><%=("/hera")%>?userAction=<%=globaz.hera.servlet.ISFActions.ACTION_VUE_GLOBALE%>.actionAfficherDossierGed&amp;noAVSId=<%=viewBean.getParent2Liant().getNssFormatte()%>&amp;idTiersExtraFolder=<%=viewBean.getParent2Liant().getIdTiers()%><%=gedNssFamilleParam%>','GED_CONSULT')" >GED</A>
												<%
													}
												%>

											</td>									
										</tr>
										</table>
									</div>
								</td>
							</tr>									
						<%
																} else {
															%>
							<div id="parentsBlock">
							</div>
						<%
							}
						%>
						
						<tr>
							<td colspan="7"><hr/></td>
						</tr>	
						
						<!-- --------------------------Affichage des relations direct au liant----------------------- -->
						<tr>
							<td colspan="7">
							<table border="0" width="<%=String.valueOf(viewBean.getWidth())%>px;">
			 			<tr>
							<td width="1%"></td>
			            	<td width="4%"></td>
							<td width="2%"></td>
			            	<td width="25%"></td>
			            	<td width="65%"></td>
			          	</tr>

<div style="top:0;left:0;width:100%;height:100%;position: fixed;display:block;" id="fastLink">
	<div style="width:240px;
            height: 60px;			
            background-color: #C0C0C0;
            display: none;position:absolute;
			font-family:Courier New;
			font-size:13;
	         top: 100px;
	         left: 100px; 
	         margin-top: 0px;	         
	         padding : 3 3;
	            			        	        
	         margin-left:0px;" id="popupFastLink">
          	   <a align="right" href="#" style="text-decoration:none;" onclick="javascript:displayFastLink(event, '', '');"><ct:FWLabel key="MENU_OPTION_FERMER"/></a>
               <hr/>	         
                <a href="#" style="text-decoration:none;" onclick="javascript:actionSelectionnerRequerant();"> <ct:FWLabel key="MENU_OPTION_SELECTIONNER_REQUERANT"/></a><br/>				
                <a href="#" style="text-decoration:none;" onclick="top.fr_main.location.href='javascript:actionListerPeriodes();'"> <ct:FWLabel key="MENU_OPTION_PERIODES_MEMBRE"/></a><br/>
				<a href="#" style="text-decoration:none;" onclick="javascript:actionAfficherEnfants();"> <ct:FWLabel key="MENU_OPTION_ENFANTS"/></a><br/>
				<a href="#" style="text-decoration:none;" onclick="javascript:actionAfficherFamille();"> <ct:FWLabel key="MENU_OPTION_AFFICHER_FAMILLE"/></a><br/>

                             
	</div>
</div>

<div style="top:0;left:0;width:100%;height:100%;position: fixed;display:block;" id="fastLinkEnfants">
	<div style="width:240px;
            height: 60px;			
            background-color: #C0C0C0;
            display: none;position:absolute;
			font-family:Courier New;
			font-size:13;
	         top: 100px;
	         left: 100px; 
	         margin-top: 0px;
	         padding : 3 3;   			        	        
	         margin-left: opx;" id="popupFastLinkEnfants">
          	   <a align="right" href="#" style="text-decoration:none;" onclick="javascript:displayFastLinkParentsEnfants(event, '');"><ct:FWLabel key="MENU_OPTION_FERMER"/></a>
               <hr/>	                         			
                <a href="#" style="text-decoration:none;" onclick="javascript:actionListerPeriodes();"> <ct:FWLabel key="MENU_OPTION_PERIODES_MEMBRE"/></a><br/>
                <a href="#" style="text-decoration:none;" onclick="javascript:actionAfficherFamille();"> <ct:FWLabel key="MENU_OPTION_AFFICHER_FAMILLE"/></a>				  
	</div>
</div>


<div style="top:0;left:0;width:100%;height:100%;position: fixed;display:block;" id="fastLinkConjointInconnu">
	<div style="width:240px;
            height: 60px;			
            background-color: #C0C0C0;
            display: none;position:absolute;
			font-family:Courier New;
			font-size:13;
	         top: 100px;
	         left: 100px; 
	         margin-top: 0px;
	         padding : 3 3;   			        	        
	         margin-left: opx;" id="popupFastLinkCI">
          	   <a align="right" href="#" style="text-decoration:none;" onclick="javascript:displayFastLinkConjointInconnu(event, '', '');"><ct:FWLabel key="MENU_OPTION_FERMER"/></a>
               <hr/>
				<a href="#" style="text-decoration:none;" onclick="javascript:actionAfficherEnfants();"> <ct:FWLabel key="MENU_OPTION_ENFANTS"/></a><br/>
	</div>
</div>



							<!-- Itération sur toute les relations -->
							<%
								String color1 = "#FFFFFF";
									String color2 = "#E8EEF4";

									for (int i = 0; i < conjoints.size(); i++) {
										int mod = (i % 2);
										SFConjointVO con = (SFConjointVO) conjoints.get(i);
										String descriptionRelation = "";
										String idMF = "";
										String idTiers = "";
										String nssIdTiers = "";
										String img = "";
										if (liant.getMembreFamille().getIdMembreFamille().equals(
												con.getIdMembreFamille1())) {
											descriptionRelation = con.getDescriptionConjoint2();
											idMF = con.getIdMembreFamille2();
											idTiers = con.getIdTiers2();
											nssIdTiers = con.getNss2();
											img = con.getImgName2();
										} else {
											descriptionRelation = con.getDescriptionConjoint1();
											idMF = con.getIdMembreFamille1();
											idTiers = con.getIdTiers1();
											nssIdTiers = con.getNss1();
											img = con.getImgName1();
										}
							%>								
								<tr bgcolor="<%=mod == 1 ? color1 : color2%>">

								<td colspan="4" valign="top">
									<table border="0"><tr>
										<td valign="top">
											
											<%
																							if (!ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU
																											.equals(idMF)) {
																						%>
												<a href="#" id="toto" onclick="displayFastLink(event, <%=idMF%>, <%=con.getIdRelationConjoint()%>);" style="text-decoration:none;">
													<IMG name="iii" width="25px;" src="<%=request.getContextPath()%>/images/<%=img%>" border="0">
												</a>
											<%
												} else {
											%>
												<a href="#" id="toto" onclick="displayFastLinkConjointInconnu(event, <%=ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU%>, <%=con.getIdRelationConjoint()%>);" style="text-decoration:none;">
																	<IMG name="iii"  width="25px;" src="<%=request.getContextPath()%>/images/darkVador.png" border="0">												
											<%
																								}
																							%>
										</td>
										<td colspan="3" valign="top">
																							
										<%if (objSession.hasRight("hera.famille.apercuRelationFamilialeRequerant.afficher", "ADD")) {%>
													<a style="text-decoration:none;" href="javascript:actionAfficherDetailMembre('<%=idMF%>');"><%=descriptionRelation%></a>
										<%} else {%>
													<%=descriptionRelation%>
										<%} %>
																	
											
											<%
												if (!JadeStringUtil.isBlankOrZero(idTiers)) {
											%>
												<A href="<%=request.getContextPath()%>/pyxis?userAction=pyxis.tiers.tiers.afficher&selectedId=<%=idTiers%>" class="external_link">
													<ct:FWCodeLibelle csCode="<%=ISFSituationFamiliale.CS_PIXIS_TYPE_TIERS %>"/>
												</A>
												&nbsp;
												<A href="#" onclick="window.open('<%=servletContext%><%=("/hera")%>?userAction=<%=globaz.hera.servlet.ISFActions.ACTION_VUE_GLOBALE%>.actionAfficherDossierGed&amp;noAVSId=<%=nssIdTiers%>&amp;idTiersExtraFolder=<%=idTiers%><%=gedNssFamilleParam%>','GED_CONSULT')" >GED</A>
											<%
												}
											%>

										</td>
									</tr></table>
									
	
									
								</td>
																
									<td valign="top">
										<div id="displayGraph_<%=con.getKey()%>" style="display:none;">
											<%=con.getHtmlTimeLine()%><br/><%=con.getHtmlDateTimeLine()%>
										</div>


											<%
												periodes = null;
														String idMFF = null;
														if (liant.getMembreFamille().getIdMembreFamille().equals(
																con.getIdMembreFamille1())) {
															periodes = con.getPeriodesConjoint2();
															idMFF = con.getIdMembreFamille2();
														} else {
															periodes = con.getPeriodesConjoint1();
															idMFF = con.getIdMembreFamille1();
														}
											%>
											<div id="displayPeriodes_<%=idMFF%>" style="display:none;">
											<table border="0" cellspacing="5">
											
											<%
																							if (periodes != null) {
																										for (int j = 0; j < periodes.size(); j++) {
																											SFPeriodeVO periode = (SFPeriodeVO) periodes.get(j);
																						%>
												  <tr>
													<td valign="top">													
														<%=SFConjointVO.startHtmlStyle()
									+ periode.getLibellePeriode()
									+ SFConjointVO.stopHtmlStyle()%>																												
													</td>
													<%
														String data = periode.getDetenteurBTE()
																				+ periode.getPays();
																		if (!JadeStringUtil.isBlankOrZero(data)) {
																			data = " (" + data + ")";
																		}
													%>
													
													<td valign="top"><%=SFConjointVO.startHtmlStyle()
									+ periode.getDateDebut()%> - <%=periode.getDateFin() + data
									+ SFConjointVO.stopHtmlStyle()%></td>													
												</tr>											  
											<%
											  												}
											  														}
											  											%>											
											</table>											
										</div>

										<div id="displayList_<%=con.getKey()%>">
											<table border="0" cellspacing="5">
											<%
												List relations = con.getRelations();
														for (int j = 0; j < relations.size(); j++) {
															SFRelationVO relation = (SFRelationVO) relations.get(j);
											%>
												  <tr>
													<td>
													<%
														if (liant.isRequerant()) {
													%>
										<%if (objSession.hasRight("hera.famille.apercuRelationConjoint.afficher", "ADD")) {%>
											<a href="javascript:actionAfficherDetailRelation('<%=relation.getIdRelation()%>')" style="text-decoration:none;">
															<%=SFConjointVO.startHtmlStyle()+ objSession.getCodeLibelle(relation.getCsTypeRelation())+ SFConjointVO.stopHtmlStyle()%>
														</a>
											
										<%} else {%>
											<%=SFConjointVO.startHtmlStyle()+ objSession.getCodeLibelle(relation.getCsTypeRelation())+ SFConjointVO.stopHtmlStyle()%>
										<%} %>													
													
														<%
															} else {
														%>
															<%=SFConjointVO.startHtmlStyle()
									+ objSession.getCodeLibelle(relation
											.getCsTypeRelation())
									+ SFConjointVO.stopHtmlStyle()%>
														<%
															}
														%>
														
													</td>
													<td><%=SFConjointVO.startHtmlStyle()
								+ relation.getDateDebut()%> - <%=relation.getDateFin()
								+ SFConjointVO.stopHtmlStyle()%></td>													
												</tr>											  
											<%
											  												}
											  											%>											
											</table>
										</div>

									</td>
								</tr>
								<%
									List enfants = con.getEnfants();
											if (enfants != null) {
												for (int j = 0; j < enfants.size(); j++) {
													SFMembreVO enfant = (SFMembreVO) enfants.get(j);
													String imgE = "child.png";
													if (!JadeStringUtil.isBlankOrZero(enfant
															.getDateDeces())) {
														imgE = "childDead.png";
													}
								%>
						 			<tr bgcolor="<%=mod == 1 ? color1 : color2%>">
										<td colspan="4">
											<table border="0"><tr>												
								            	<td valign="top" colspan="3">
								            		<%
								            			if (!ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU
								            									.equals(enfant.getIdMembreFamille())) {
								            		%>
								            			<a href="#" id="toto" onclick="displayFastLinkParentsEnfants(event, <%=enfant.getIdMembreFamille()%>, '');" style="text-decoration:none;">
								            				<IMG name="iii" width="25px;" src="<%=request.getContextPath()%>/images/<%=imgE%>" border="0">
								            			</a>
								            		<%
								            			}
								            		%>
								            	</td>
												<td colspan="3">													
													<i>
													
										<%if (objSession.hasRight("hera.famille.apercuRelationFamilialeRequerant.afficher", "ADD")) {%>
													<a style="text-decoration:none;" href="javascript:actionAfficherDetailMembre('<%=enfant.getIdMembreFamille()%>');"><%=SFUtil
													.formatDetailMembreFamilleVueGlobale(
															enfant.getNssFormatte(),
															enfant.getNom() + " " + enfant.getPrenom(),
															enfant.getDateNaissance(),
															enfant.getDateDeces(),
															enfant.getCsSexe(),
															enfant.getCsNationalite())%></a>
										<%} else {%>
												<%=SFUtil
													.formatDetailMembreFamilleVueGlobale(
															enfant.getNssFormatte(),
															enfant.getNom()+ " "+ enfant.getPrenom(),
															enfant.getDateNaissance(),
															enfant.getDateDeces(),
															enfant.getCsSexe(),
															enfant.getCsNationalite())%>
										<%} %>													
										</i>													
														<%
																												if (!JadeStringUtil.isBlankOrZero(enfant
																																		.getIdTiers())) {
																											%>
															<A href="<%=request.getContextPath()%>/pyxis?userAction=pyxis.tiers.tiers.afficher&selectedId=<%=enfant.getIdTiers()%>" class="external_link">
																<ct:FWCodeLibelle csCode="<%=ISFSituationFamiliale.CS_PIXIS_TYPE_TIERS %>"/>
															</A>
															&nbsp;
															<A href="#" onclick="window.open('<%=servletContext%><%=("/hera")%>?userAction=<%=globaz.hera.servlet.ISFActions.ACTION_VUE_GLOBALE%>.actionAfficherDossierGed&amp;noAVSId=<%=enfant.getNssFormatte()%>&amp;idTiersExtraFolder=<%=enfant.getIdTiers()%><%=gedNssFamilleParam%>','GED_CONSULT')" >GED</A>
														<%
															}
														%>

												</td>					            	
								            											
											</tr></table>											
										</td>									
			            				<td>
											
											<div id="displayPeriodes_<%=enfant.getIdMembreFamille()%>" style="display:none;">
											<table border="0" cellspacing="5">
											
											<%
																							periodes = enfant.getPeriodes();
																											if (periodes != null) {
																												for (int k = 0; k < periodes.size(); k++) {
																													SFPeriodeVO periode = (SFPeriodeVO) periodes
																															.get(k);
																						%>
												  <tr>
													<td valign="top">													
														<%=SFConjointVO.startHtmlStyle()
											+ periode.getLibellePeriode()
											+ SFConjointVO.stopHtmlStyle()%>																												
													</td>
													<%
														String data = periode.getDetenteurBTE()
																						+ periode.getPays();
																				if (!JadeStringUtil.isBlankOrZero(data)) {
																					data = " (" + data + ")";
																				}
													%>
													
													<td valign="top"><%=SFConjointVO.startHtmlStyle()
											+ periode.getDateDebut()%> - <%=periode.getDateFin() + data
											+ SFConjointVO.stopHtmlStyle()%></td>													
												</tr>											  
											<%
											  												}
											  																}
											  											%>											
											</table>											
										</div>			            							            				
			            				</td>
						          	</tr>





									<%
										}
												}
									%>	
									<tr><td colspan="4"><br/></td></tr>
									
									
															
							<%
																																									}
																																								%>
						<!-- --------------------------Fin Affichage des relations -------------------------- -->

						<!-- --------------------------Affichage des conjoints des conjoints----------------- -->
							<!-- Itération sur toute les relations -->
							<%
								if (conjointsExtend != null && !conjointsExtend.isEmpty()) {
							%>

							<tr>
								<td colspan="5"><hr/></td>
				          	</tr>
							<tr>
								<td colspan="5"><ct:FWLabel key="JSP_SF_VG_RELATION_ETENDUE"/></td>
				          	</tr>

							<%
								for (int i = 0; i < conjointsExtend.size(); i++) {
											int mod = (i % 2);
											SFConjointVO con = (SFConjointVO) conjointsExtend
													.get(i);
											String descriptionRelation1 = "";
											String descriptionRelation2 = "";
											String idMF1 = "";
											String idMF2 = "";
											String idTiers1 = "";
											String idTiers2 = "";
											String nssIdTiers1 = "";
											String nssIdTiers2 = "";
											String img1 = "";
											String img2 = "";

											descriptionRelation2 = con.getDescriptionConjoint2();
											idMF2 = con.getIdMembreFamille2();
											descriptionRelation1 = con.getDescriptionConjoint1();
											idMF1 = con.getIdMembreFamille1();
											idTiers1 = con.getIdTiers1();
											idTiers2 = con.getIdTiers2();
											nssIdTiers1 = con.getNss1();
											nssIdTiers2 = con.getNss2();											
											img1 = con.getImgName1();
											img2 = con.getImgName2();
											
							%>								
									<tr bgcolor="<%=mod == 1 ? color1 : color2%>">

										<!-- ////////////////////////////////////////////////////////////////////////////// -->
										<!--  2 modes d'affichage différents selon que l'on affiche les périodes ou non !!! -->
										<!--  Affichage des périodes														-->
										<!-- ////////////////////////////////////////////////////////////////////////////// -->
										<%
											if (isDisplayPeriode) {
										%>
											<td colspan="5" valign="top">
												<table border="0">											
												<tr>
													<td valign="top">
														<%
															if (!ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU
																					.equals(idMF1)) {
														%>
															<a href="#" id="toto" onclick="displayFastLink(event, <%=idMF1%>, <%=con.getIdRelationConjoint()%>);" style="text-decoration:none;">
																<IMG name="iii" width="25px;" src="<%=request.getContextPath()%>/images/<%=img1%>" border="0">
															</a>
														<%
															} else {
														%>
															<a href="#" id="toto" onclick="displayFastLinkConjointInconnu(event, <%=ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU%>, <%=con.getIdRelationConjoint()%>);" style="text-decoration:none;">
																<IMG name="iii"  width="25px;" src="<%=request.getContextPath()%>/images/darkVador.png" border="0">												
															<%
																												}
																											%>
													</td>
													<td colspan="3" valign="top">


										<%if (objSession.hasRight("hera.famille.apercuRelationFamilialeRequerant.afficher", "ADD")) {%>
											<a style="text-decoration:none;" href="javascript:actionAfficherDetailMembre('<%=idMF1%>');"><%=descriptionRelation1%></a>
										<%} else {%>
											<%=descriptionRelation1%>
										<%} %>													

														
														<%
															if (!JadeStringUtil.isBlankOrZero(idTiers1)) {
														%>
															<A href="<%=request.getContextPath()%>/pyxis?userAction=pyxis.tiers.tiers.afficher&selectedId=<%=idTiers1%>" class="external_link">
																<ct:FWCodeLibelle csCode="<%=ISFSituationFamiliale.CS_PIXIS_TYPE_TIERS %>"/>
															</A>
															&nbsp;
															<A href="#" onclick="window.open('<%=servletContext%><%=("/hera")%>?userAction=<%=globaz.hera.servlet.ISFActions.ACTION_VUE_GLOBALE%>.actionAfficherDossierGed&amp;noAVSId=<%=nssIdTiers1%>&amp;idTiersExtraFolder=<%=idTiers1%><%=gedNssFamilleParam%>" >GED</A>
														<%
															}
														%>

													</td>
													<td>
														<div id="displayPeriodes_exc<%=idMF1%>" style="display:none;">
															<table border="0" cellspacing="5">
															
															<%
																															periodes = con.getPeriodesConjoint1();
																																			boolean isDecesConjoint = false;

																																			if (periodes != null) {
																																				for (int j = 0; j < periodes.size(); j++) {
																																					SFPeriodeVO periode = (SFPeriodeVO) periodes
																																							.get(j);
																														%>
																  <tr>
																	<td valign="top">													
																		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=SFConjointVO.startHtmlStyle()
											+ periode.getLibellePeriode()
											+ SFConjointVO.stopHtmlStyle()%>																												
																	</td>
																	<%
																		String data = periode.getDetenteurBTE()
																										+ periode.getPays();
																								if (!JadeStringUtil.isBlankOrZero(data)) {
																									data = " (" + data + ")";
																								}
																	%>
																	
																	<td valign="top"><%=SFConjointVO.startHtmlStyle()
											+ periode.getDateDebut()%> - <%=periode.getDateFin() + data
											+ SFConjointVO.stopHtmlStyle()%></td>													
																</tr>											  
															<%
											  																}
											  																				}
											  															%>											
															</table>											
														</div>
													</td>																																	
												</tr>
												
												<tr>
													<td valign="top">
														<%
															if (!ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU
																					.equals(idMF2)) {
														%>
															<a href="#" id="toto" onclick="displayFastLink(event, <%=idMF2%>, <%=con.getIdRelationConjoint()%>);" style="text-decoration:none;">
																<IMG name="iii" width="25px;" src="<%=request.getContextPath()%>/images/<%=img2%>" border="0">
															</a>
															<%
																} else {
															%>
															<a href="#" id="toto" onclick="displayFastLinkConjointInconnu(event, <%=ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU%>, <%=con.getIdRelationConjoint()%>);" style="text-decoration:none;">
																<IMG name="iii"  width="25px;" src="<%=request.getContextPath()%>/images/darkVador.png" border="0">												
															<%
																												}
																											%>
													</td>
													<td colspan="3" valign="top">										
														<%if (objSession.hasRight("hera.famille.apercuRelationFamilialeRequerant.afficher", "ADD")) {%>
																<a style="text-decoration:none;" href="javascript:actionAfficherDetailMembre('<%=idMF2%>');"><%=descriptionRelation2%></a>
														<%} else {%>
															<%=descriptionRelation2%>
														<%} %>													
																	
														
														<%
															if (!JadeStringUtil.isBlankOrZero(idTiers2)) {
														%>
															<A href="<%=request.getContextPath()%>/pyxis?userAction=pyxis.tiers.tiers.afficher&selectedId=<%=idTiers2%>" class="external_link">
																<ct:FWCodeLibelle csCode="<%=ISFSituationFamiliale.CS_PIXIS_TYPE_TIERS %>"/>
															</A>
															&nbsp;
															<A href="#" onclick="window.open('<%=servletContext%><%=("/hera")%>?userAction=<%=globaz.hera.servlet.ISFActions.ACTION_VUE_GLOBALE%>.actionAfficherDossierGed&amp;noAVSId=<%=nssIdTiers2%>&amp;idTiersExtraFolder=<%=idTiers2%><%=gedNssFamilleParam%>','GED_CONSULT')" >GED</A>
															
														<%
															}
														%>

													</td>
													<td>
														<div id="displayPeriodes_exc<%=idMF2%>" style="display:none;">
															<table border="0" cellspacing="5">

															<%
																periodes = con.getPeriodesConjoint2();
																				if (periodes != null) {
																					for (int j = 0; j < periodes.size(); j++) {
																						SFPeriodeVO periode = (SFPeriodeVO) periodes
																								.get(j);
															%>
																  <tr>
																	<td valign="top">													
																		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=SFConjointVO.startHtmlStyle()
											+ periode.getLibellePeriode()
											+ SFConjointVO.stopHtmlStyle()%>																												
																	</td>
																	<%
																		String data = periode.getDetenteurBTE()
																										+ periode.getPays();
																								if (!JadeStringUtil.isBlankOrZero(data)) {
																									data = " (" + data + ")";
																								}
																	%>
																	
																	<td valign="top"><%=SFConjointVO.startHtmlStyle()
											+ periode.getDateDebut()%> - <%=periode.getDateFin() + data
											+ SFConjointVO.stopHtmlStyle()%></td>													
																</tr>											  
															<%
											  																}
											  																				}
											  															%>											
															</table>											
														</div>													
													</td>																						
												</tr>
											</table>
										</td>																					
											
											
									<!-- ////////////////////////////////////////////////////////////////////////////// -->
									<!-- Affichage des relations -->
									<!-- ////////////////////////////////////////////////////////////////////////////// -->											
									<%
																					} else {
																				%>
										<td colspan="4" valign="top">
											<table border="0">
												<tr>
													<td valign="top">
														<%
															if (!ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU
																					.equals(idMF1)) {
														%>
															<a href="#" id="toto" onclick="displayFastLink(event, <%=idMF1%>, <%=con.getIdRelationConjoint()%>);" style="text-decoration:none;">
																<IMG name="iii" width="25px;" src="<%=request.getContextPath()%>/images/<%=img1%>" border="0">
															</a>
														<%
															} else {
														%>
															<a href="#" id="toto" onclick="displayFastLinkConjointInconnu(event, <%=ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU%>, <%=con.getIdRelationConjoint()%>);" style="text-decoration:none;">
																<IMG name="iii"  width="25px;" src="<%=request.getContextPath()%>/images/darkVador.png" border="0">												
															<%
																												}
																											%>
													</td>
													<td colspan="3" valign="top">
														<%if (objSession.hasRight("hera.famille.apercuRelationFamilialeRequerant.afficher", "ADD")) {%>
																<a style="text-decoration:none;" href="javascript:actionAfficherDetailMembre('<%=idMF1%>');"><%=descriptionRelation1%></a>
														<%} else {%>
															<%=descriptionRelation1%>
														<%} %>													
																							
																							
														
														<%
															if (!JadeStringUtil.isBlankOrZero(idTiers1)) {
														%>
															<A href="<%=request.getContextPath()%>/pyxis?userAction=pyxis.tiers.tiers.afficher&selectedId=<%=idTiers1%>" class="external_link">
																<ct:FWCodeLibelle csCode="<%=ISFSituationFamiliale.CS_PIXIS_TYPE_TIERS %>"/>
															</A>
															&nbsp;
															<A href="#" onclick="window.open('<%=servletContext%><%=("/hera")%>?userAction=<%=globaz.hera.servlet.ISFActions.ACTION_VUE_GLOBALE%>.actionAfficherDossierGed&amp;noAVSId=<%=nssIdTiers1%>&amp;idTiersExtraFolder=<%=idTiers1%><%=gedNssFamilleParam%>" >GED</A>
														<%
															}
														%>

													</td>									
												</tr>
												<tr>
													<td valign="top">
														<%
															if (!ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU
																					.equals(idMF2)) {
														%>
															<a href="#" id="toto" onclick="displayFastLink(event, <%=idMF2%>, <%=con.getIdRelationConjoint()%>);" style="text-decoration:none;">
																<IMG name="iii" width="25px;" src="<%=request.getContextPath()%>/images/<%=img2%>" border="0">
															</a>
														<%
															} else {
														%>
															<a href="#" id="toto" onclick="displayFastLinkConjointInconnu(event, <%=ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU%>, <%=con.getIdRelationConjoint()%>);" style="text-decoration:none;">
																<IMG name="iii"  width="25px;" src="<%=request.getContextPath()%>/images/darkVador.png" border="0">												
														<%
																											}
																										%>
													</td>
													<td colspan="3" valign="top">										
														<%if (objSession.hasRight("hera.famille.apercuRelationFamilialeRequerant.afficher", "ADD")) {%>
																<a style="text-decoration:none;" href="javascript:actionAfficherDetailMembre('<%=idMF2%>');"><%=descriptionRelation2%></a>
														<%} else {%>
																<%=descriptionRelation2%>
														<%} %>													
													
														
														<%
															if (!JadeStringUtil.isBlankOrZero(idTiers2)) {
														%>
															<A href="<%=request.getContextPath()%>/pyxis?userAction=pyxis.tiers.tiers.afficher&selectedId=<%=idTiers2%>" class="external_link">
																<ct:FWCodeLibelle csCode="<%=ISFSituationFamiliale.CS_PIXIS_TYPE_TIERS %>"/>
															</A>
															&nbsp;
															<A href="#" onclick="window.open('<%=servletContext%><%=("/hera")%>?userAction=<%=globaz.hera.servlet.ISFActions.ACTION_VUE_GLOBALE%>.actionAfficherDossierGed&amp;noAVSId=<%=nssIdTiers2%>&amp;idTiersExtraFolder=<%=idTiers2%><%=gedNssFamilleParam%>','GED_CONSULT')" >GED</A>
															
														<%
															}
														%>

													</td>																						
												</tr>

											</table>
										</td>											
										<td>										
											<table>
												<tr>
													<td valign="top">
														<div id="displayGraph_<%=con.getKey()%>" style="display:none;">
															<%=con.getHtmlTimeLine()%><br/><%=con.getHtmlDateTimeLine()%>
														</div>					

														<div id="displayList_<%=con.getKey()%>">
															<table border="0" cellspacing="5">
															<%
																List relations = con.getRelations();
																				for (int j = 0; j < relations.size(); j++) {
																					SFRelationVO relation = (SFRelationVO) relations
																							.get(j);
															%>
																  <tr valign="top">
																	<td valign="top">
																	<%
																		if (liant.isRequerant()) {
																	%>
																	
										<%if (objSession.hasRight("hera.famille.apercuRelationConjoint.afficher", "ADD")) {%>
																		<a href="javascript:actionAfficherDetailRelation('<%=relation.getIdRelation()%>')" style="text-decoration:none;">
																			<%=SFConjointVO.startHtmlStyle()+ objSession.getCodeLibelle(relation.getCsTypeRelation())+ SFConjointVO.stopHtmlStyle()%>
																		</a>
											
										<%} else {%>
											<%=SFConjointVO.startHtmlStyle()+ objSession.getCodeLibelle(relation.getCsTypeRelation())+ SFConjointVO.stopHtmlStyle()%>
										<%} %>													

																	
																		<%
																			} else {
																		%>
																			<%=SFConjointVO
															.startHtmlStyle()
													+ objSession
															.getCodeLibelle(relation
																	.getCsTypeRelation())
													+ SFConjointVO
															.stopHtmlStyle()%>
																		<%
																			}
																		%>
																	</td>
																	<td valign="top"><%=SFConjointVO.startHtmlStyle()
										+ relation.getDateDebut()%> - <%=relation.getDateFin()
										+ SFConjointVO.stopHtmlStyle()%></td>
																</tr>											  
															<%
											  																}
											  															%>											
															</table>
														</div>						
													</td>
												</tr>
												
												<tr>
													<td valign="top">
													</td>																																				
												</tr>												
											</table>
										</td>		
										<%
													}
												%>
										
									</tr>
									<%
										List enfants = con.getEnfants();
													if (enfants != null) {
														for (int j = 0; j < enfants.size(); j++) {
															SFMembreVO enfant = (SFMembreVO) enfants.get(j);
															String imgE = "child.png";
															if (!JadeStringUtil.isBlankOrZero(enfant
																	.getDateDeces())) {
																imgE = "childDead.png";
															}
									%>
							 			<tr bgcolor="<%=mod == 1 ? color1 : color2%>">
							            	<td colspan ="4">
												<table><tr>
													
									            	<td colspan="3" valign="top">
									            		<%
									            			if (!ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU
									            										.equals(enfant.getIdMembreFamille())) {
									            		%>
									            			<a href="#" id="toto" onclick="displayFastLinkParentsEnfants(event, <%=enfant.getIdMembreFamille()%>, '');" style="text-decoration:none;">
									            				<IMG name="iii" width="25px;" src="<%=request.getContextPath()%>/images/<%=imgE%>" border="0">
									            			</a>
									            		<%
									            			}
									            		%>
									            	</td>
													<td valign="top" colspan="3">
														<i>
														
										<%if (objSession.hasRight("hera.famille.apercuRelationFamilialeRequerant.afficher", "ADD")) {%>
													<a style="text-decoration:none;" href="javascript:actionAfficherDetailMembre('<%=enfant.getIdMembreFamille()%>');"><%=SFUtil
														.formatDetailMembreFamilleVueGlobale(
																enfant.getNssFormatte(),
																enfant.getNom()+ " "+ enfant.getPrenom(),
																enfant.getDateNaissance(),
																enfant.getDateDeces(),
																enfant.getCsSexe(),
																enfant.getCsNationalite())%></a>
										<%} else {%>
											<%=SFUtil
														.formatDetailMembreFamilleVueGlobale(
																enfant.getNssFormatte(),
																enfant.getNom()+ " "+ enfant.getPrenom(),
																enfant.getDateNaissance(),
																enfant.getDateDeces(),
																enfant.getCsSexe(),
																enfant.getCsNationalite())%>
										<%} %>													
														
														
</i>
														<%
															if (!JadeStringUtil.isBlankOrZero(enfant
																						.getIdTiers())) {
														%>
															<A href="<%=request.getContextPath()%>/pyxis?userAction=pyxis.tiers.tiers.afficher&selectedId=<%=enfant.getIdTiers()%>" class="external_link">
																<ct:FWCodeLibelle csCode="<%=ISFSituationFamiliale.CS_PIXIS_TYPE_TIERS %>"/>
															</A>
															&nbsp;
															<A href="#" onclick="window.open('<%=servletContext%><%=("/hera")%>?userAction=<%=globaz.hera.servlet.ISFActions.ACTION_VUE_GLOBALE%>.actionAfficherDossierGed&amp;noAVSId=<%=enfant.getNssFormatte()%>&amp;idTiersExtraFolder=<%=enfant.getIdTiers()%><%=gedNssFamilleParam%>','GED_CONSULT')" >GED</A>
														<%
															}
														%>

													</td>					            	
									            	<td/>
												</tr></table>
											</td>
											<td>
											
												<div id="displayPeriodes_exc<%=enfant.getIdMembreFamille()%>" style="display:none;">
												<table border="0" cellspacing="5">
												
												<%
																									periodes = enfant.getPeriodes();
																														if (periodes != null) {
																															for (int k = 0; k < periodes.size(); k++) {
																																SFPeriodeVO periode = (SFPeriodeVO) periodes
																																		.get(k);
																								%>
													  <tr>
														<td valign="top">													
															<%=SFConjointVO.startHtmlStyle()
												+ periode.getLibellePeriode()
												+ SFConjointVO.stopHtmlStyle()%>																												
														</td>
														<%
															String data = periode.getDetenteurBTE()
																								+ periode.getPays();
																						if (!JadeStringUtil.isBlankOrZero(data)) {
																							data = " (" + data + ")";
																						}
														%>
														
														<td valign="top"><%=SFConjointVO.startHtmlStyle()
												+ periode.getDateDebut()%> - <%=periode.getDateFin()
												+ data
												+ SFConjointVO.stopHtmlStyle()%></td>													
													</tr>											  
												<%
											  													}
											  																		}
											  												%>											
												</table>											
											</div>			            							            				
											
											</td>
							          	</tr>
										<%
											}
														}
										%>
										<tr><td colspan="5"><br/></td></tr>	
								<%
										}
											}
									%>
							</table>
							</td>
						</tr>
						<!-- --------------------------Fin Affichage des conjoints des conjoints------------- -->
					</table>
					</td>
				</tr>
			<%
				}
			%>
				<script>
					changeDisplayMode();
				</script>
				
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>