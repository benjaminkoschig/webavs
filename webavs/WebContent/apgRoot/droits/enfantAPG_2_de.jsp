<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
		<%@page import="globaz.apg.servlet.IAPActions"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<script type="text/javascript"
			src="<%=servletContext%>/scripts/nss.js"></script>
		<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%
idEcran="PAP0005";

globaz.apg.vb.droits.APEnfantAPG_2ViewBean viewBean = (globaz.apg.vb.droits.APEnfantAPG_2ViewBean) request.getAttribute("viewBean");
viewBean.setDroitAPGDTO((globaz.apg.vb.droits.APDroitAPGDTO) (session.getAttribute(globaz.prestation.tools.PRSessionDataContainerHelper.KEY_DROIT_DTO)));

selectedIdValue = viewBean.getIdEnfant();
bButtonDelete = viewBean.isModifiable()&& viewBean.getSession().hasRight(IAPActions.ACTION_ENFANT_APG, FWSecureConstants.UPDATE);
bButtonUpdate = viewBean.isModifiable()&& viewBean.getSession().hasRight(IAPActions.ACTION_ENFANT_APG, FWSecureConstants.UPDATE);
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="ap-menuprincipalapg" showTab="menu"/>
<ct:menuChange displayId="options" menuId="ap-optionsempty"/>

<SCRIPT language="javascript">
  function add() {
	nssUpdateHiddenFields();
    document.forms[0].elements('userAction').value="<%=globaz.apg.servlet.IAPActions.ACTION_ENFANT_APG%>.ajouter"
  }
  function upd() {
  nssUpdateHiddenFields();
  	document.forms[0].elements('userAction').value="<%=globaz.apg.servlet.IAPActions.ACTION_ENFANT_APG%>.modifier";
  	
  	document.getElementById("nomAffiche").disabled=true;				    	        				
	document.getElementById("prenomAffiche").disabled=true;
	document.getElementById("dateNaissanceAffiche").disabled=true;
	document.getElementById("csSexeAffiche").disabled=true;
	document.getElementById("csNationaliteAffiche").disabled=true;
  }
  
   function init(){

  }
  	
  	function validate(){
  	nssUpdateHiddenFields();
  	state = true;
  	// on a juste besoin de modifier puisqu'il existe deja
  		document.forms[0].elements('userAction').value="<%=globaz.apg.servlet.IAPActions.ACTION_ENFANT_APG%>.modifier";
	return state;
  	
  	}
  	
  	
  function del() {
    if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
        document.forms[0].elements('userAction').value="apg.droits.enfantAPG.supprimer";
        document.forms[0].submit();
    }
  }
  
  
	function nssFailure() {	
		alert("Numéro AVS inconnu.");
		document.getElementById("idAssure").value=null;
		document.getElementById("nss").value="";
		document.getElementById("likeNSS").value="";
		document.getElementById("provenance").value=null;	
		document.getElementById("nomPrenom").value="";		
		document.getElementById("nomAffiche").value="";
		document.getElementById("dateNaissanceAffiche").value="";
		document.getElementById("prenomAffiche").value="";
		document.getElementById("csSexeAffiche").disabled=false;
		document.getElementById("csNationaliteAffiche").disabled=false;

	}
	
	
	function nssUpdateHiddenFields() {
				document.getElementById("nom").value=document.getElementById("nomAffiche").value;
				document.getElementById("prenom").value=document.getElementById("prenomAffiche").value;
				document.getElementById("dateNaissance").value=document.getElementById("dateNaissanceAffiche").value;
				document.getElementById("nss").value=document.getElementById("likeNSS").value;
				document.getElementById("csSexe").value=document.getElementById("csSexeAffiche").value;	
				document.getElementById("csNationalite").value=document.getElementById("csNationaliteAffiche").value;				
	}
	

	function nssChange(tag) {
		if(tag.select==null) {											
		}
		else {
  			var element = tag.select.options[tag.select.selectedIndex];
			if (element.nss!=null)
				document.getElementById("nss").value=element.nss;
			
			if (element.nom!=null) {
				document.getElementById("nom").value=element.nom;
				document.getElementById("nomAffiche").value=element.nom;
			}
				
			if (element.prenom!=null)		    	        					
				document.getElementById("prenom").value=element.prenom;
				document.getElementById("prenomAffiche").value=element.prenom;
			
			if (element.nom!=null && element.prenom!=null)
				document.getElementById("nomPrenom").value=element.nom + " " + element.prenom;
				
			if (element.codeSexe!=null) {
				for (var i=0; i < document.getElementById("csSexeAffiche").length ; i++) {					
					if (element.codeSexe==document.getElementById("csSexeAffiche").options[i].value) {
						document.getElementById("csSexeAffiche").options[i].selected=true;
					}
				}
				document.getElementById("csSexe").value=element.codeSexe;
			}				
				
			if (element.codePays!=null) {				
				for (var i=0; i < document.getElementById("csNationaliteAffiche").length ; i++) {					
					if (element.codePays==document.getElementById("csNationaliteAffiche").options[i].value) {
						document.getElementById("csNationaliteAffiche").options[i].selected=true;
					}
				}
				document.getElementById("csNationalite").value=element.codePays;
			}
			
			if (element.provenance!=null)
				document.getElementById("provenance").value=element.provenance;
			
			if (element.id!=null)
				document.getElementById("idAssure").value=element.idAssure;
					
			if (element.dateNaissance!=null) {
				document.getElementById("dateNaissance").value=element.dateNaissance;
				document.getElementById("dateNaissanceAffiche").value=element.dateNaissance;
			}

						    	        				
			if ('<%=globaz.prestation.interfaces.util.nss.PRUtil.PROVENANCE_TIERS%>'==element.provenance) {
				document.getElementById("nomAffiche").disabled=true;				    	        				
				document.getElementById("prenomAffiche").disabled=true;
				document.getElementById("dateNaissanceAffiche").disabled=true;
				document.getElementById("csSexeAffiche").disabled=true;
				document.getElementById("csNationaliteAffiche").disabled=true;
			}
		}
	}  	
  	
  	
  
  </SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_ENFANT"/> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
						<TD colspan="4">
							<INPUT type="hidden" name="<%=globaz.apg.servlet.APAbstractDroitDTOAction.PARAM_ID_DROIT%>" value="<%=request.getParameter(globaz.apg.servlet.APAbstractDroitDTOAction.PARAM_ID_DROIT)%>">
						</TD>
						</TR>

						<TR>
							<TD><ct:FWLabel key="JSP_DEBUT_DROIT"/></TD>
							<TD><ct:FWCalendarTag name="dateDebutDroit" value="<%=viewBean.getDateDebutDroit()%>"/></TD>
							<TD><ct:FWLabel key="JSP_FIN_DROIT"/></TD>
							<TD><ct:FWCalendarTag name="dateFinDroit" value="<%=viewBean.getDateFinDroit()%>"/></TD>							
						</TR>
						





<!--Gestion du NSS -->
<tr><td colspan="6">
<TABLE>
							<TR>
									<TD>
										<ct:FWLabel key="JSP_NSS_ABREGE"/>
									</TD>
									<TD colspan="3">
									<%	String params = "&provenance1=TIERS";	
											   params += "&provenance2=CI";
										String jspLocation = servletContext + "/ijRoot/numeroSecuriteSocialeSF_select.jsp"; %>
										<ct1:nssPopup name="likeNSS" onFailure="nssFailure();" onChange="nssChange(tag);" params="<%=params%>" 
													  value="<%=viewBean.getNumeroAvsFormateSansPrefixe()%>" newnss="<%=viewBean.isNNSS()%>"
													  jspName="<%=jspLocation%>" avsMinNbrDigit="3" nssMinNbrDigit="3" avsAutoNbrDigit="11" nssAutoNbrDigit="10"  />
													  
										<INPUT type="text" name="nomPrenom" value="<%=viewBean.getNom()%> <%=viewBean.getPrenom()%>" class="libelleLongDisabled"/>
										<INPUT type="hidden" name="nss" value="<%=viewBean.getNss()%>"/>
										<INPUT type="hidden" name="idAssure" value="<%=viewBean.getIdAssure()%>"/>
										<INPUT type="hidden" name="provenance" value="<%=viewBean.getProvenance()%>"/>
																				
										<input type="hidden" name="idDroit" value="<%=viewBean.getIdDroit()%>">
										<input type="hidden" name="<%=globaz.apg.servlet.APAbstractDroitDTOAction.PARAM_ID_DROIT%>" value="<%=viewBean.getIdDroit()%>">
										
					            	</TD>
								</TR>

								<TR>
									<TD><ct:FWLabel key="JSP_NOM"/></TD>
									<TD><INPUT type="hidden" name="nom" value="<%=viewBean.getNom()%>"/>
  										<INPUT type="text" name="nomAffiche" value="<%=viewBean.getNom()%>"/>
									</TD>
									<TD><ct:FWLabel key="JSP_PRENOM"/></TD>
									<TD><INPUT type="hidden" name="prenom" value="<%=viewBean.getPrenom()%>"/>
										<INPUT type="text" name="prenomAffiche" value="<%=viewBean.getPrenom()%>"/>
									</TD>
								</TR>

								<TR>
									<TD><ct:FWLabel key="JSP_DATE_NAISSANCE"/></TD>
									<TD><INPUT type="hidden" name="dateNaissance" value="<%=viewBean.getDateNaissance()%>"/>
									<ct:FWCalendarTag name="dateNaissanceAffiche" value="<%=viewBean.getDateNaissance()%>"/>
									</TD>
									<TD><ct:FWLabel key="JSP_SEXE"/></TD>
									<TD>
										<ct:FWCodeSelectTag name="csSexeAffiche"
											wantBlank="<%=true%>"
									      	codeType="PYSEXE"
									      	defaut="<%=viewBean.getCsSexe()%>"/>
									      	<INPUT type="hidden" name="csSexe" value="<%=viewBean.getCsSexe()%>"/>
									</TD>
								</TR>															
								<TR>
									<TD><LABEL for="pays"><ct:FWLabel key="JSP_NATIONALITE"/></LABEL></TD>
									<TD><ct:FWListSelectTag name="csNationaliteAffiche" data="<%=viewBean.getTiPays()%>" defaut="<%=globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getCsNationalite())?globaz.pyxis.db.adressecourrier.TIPays.CS_SUISSE:viewBean.getCsNationalite()%>"/>
									      	<INPUT type="hidden" name="csNationalite" value="<%=viewBean.getCsNationalite()%>"/>
									</TD>
								</TR>
																								
				            	<SCRIPT language="javascript">	    				        	
	    				        	document.getElementById("likeNSS").onkeypress= new Function ("","return filterCharForPositivFloat(window.event);");	
			            		</SCRIPT>				            	
				            
</TABLE>
</td></tr>








						
						
						
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>