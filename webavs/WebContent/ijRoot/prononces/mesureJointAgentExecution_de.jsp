<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="PIJ0001";
	globaz.ij.vb.prononces.IJMesureJointAgentExecutionViewBean viewBean = (globaz.ij.vb.prononces.IJMesureJointAgentExecutionViewBean) session.getAttribute("viewBean");
	selectedIdValue = viewBean.getIdMesure();
	String idPrononce = request.getParameter("idPrononce");
	String noAVS = request.getParameter("noAVS");
	String dateDebutPrononce = request.getParameter("dateDebutPrononce");
	String csTypeIJ = request.getParameter("csTypeIJ");
	bButtonCancel = false;
	
	// on doit toujours pouvoir modifier les agents d'execution 
	//bButtonUpdate = bButtonUpdate && viewBean.isModifierPermis();
	//bButtonValidate = bButtonValidate && viewBean.isModifierPermis();
	//bButtonDelete = bButtonDelete && viewBean.isModifierPermis();
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="ij-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="ij-optionsempty"/>

<SCRIPT language="javascript"> 

var servlet = "<%=(servletContext + mainServletPath)%>";
	
	<% if(viewBean.getSession().hasRight("ij.prononces.mesureJointAgentExecution.ajouter", FWSecureConstants.UPDATE)){
		bButtonValidate = true;
	} else {
		bButtonValidate = false;
	}%>

	function add() {
	    document.forms[0].elements('userAction').value="ij.prononces.mesureJointAgentExecution.ajouter";
	}
	
	var warningObj = new Object();
	warningObj.text = "";
	
	function showWarnings() {
		if (warningObj.text != "") {
			showModalDialog('<%=servletContext%>/warningModalDlg.jsp',warningObj,'dialogHeight:20;dialogWidth:25;status:no;resizable:no');	
		}
	}
	
	function upd() {
     	parent.isModification = true;
		document.forms[0].elements('userAction').value="ij.prononces.mesureJointAgentExecution.modifier";
	}
	
	function validate() {
	    state = true;
    	parent.isNouveau = false;
    	parent.isModification = false;
	    
	    if (document.forms[0].elements('_method').value == "add"){
	        document.forms[0].elements('userAction').value="ij.prononces.mesureJointAgentExecution.ajouter";
	    }
	    else{
	        document.forms[0].elements('userAction').value="ij.prononces.mesureJointAgentExecution.modifier";
	    }
	    return state;
	}
	
	function arret() {
		top.fr_main.location.href = servlet + "?userAction=ij.prononces.prononceJointDemande.chercher";
 	}
	
	function cancel() {
		//pas de cancel ici
	}
	
	function del() {
	    if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
	        document.forms[0].elements('userAction').value="ij.prononces.mesureJointAgentExecution.supprimer";
	        document.forms[0].submit();
	    }
	}

	function init(){
  		parent.isNouveau = false;
		<%if ("new".equalsIgnoreCase(request.getParameter("_valid"))) {%>
	  		// mise a jour de la liste du parent
			if(parent.document.forms[0]) {
				parent.document.forms[0].submit();
			}
    		parent.isNouveau = true;
		<%}%>
	}
	
	function versEcranSuivant() {
	  	if(parent.isModification ||(parent.isNouveau && document.forms[0].elements('nomAgentExecution').value.length>0)){
			warningObj.text="<ct:FWLabel key='JSP_MODIF_PERDUES_WARN'/>";
			showWarnings();
			parent.isModification=false;
			parent.isNouveau=false;
		}else{
			var url = servlet + "?userAction=";
			<%if(globaz.ij.api.prononces.IIJPrononce.CS_GRANDE_IJ.equals(csTypeIJ) ||
				 globaz.ij.api.prononces.IIJPrononce.CS_ALLOC_INIT_TRAVAIL.equals(csTypeIJ)){%>
				url += "ij.prononces.situationProfessionnelle.chercher&forIdPrononce=<%=idPrononce%>&noAVS=<%=noAVS%>&dateDebutPrononce=<%=dateDebutPrononce%>&csTypeIJ=<%=csTypeIJ%>";
			<%}else{%>	
				url += "ij.prononces.petiteIJJointRevenu.afficher&selectedId=<%=idPrononce%>&noAVS=<%=noAVS%>&dateDebutPrononce=<%=dateDebutPrononce%>";
			<%}%>
				
			top.fr_main.location.href = url;
		}
	}
	
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_AGENTS_EXECUTION"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
							<TR>
								<TD><LABEL for="dateDebut"><ct:FWLabel key="JSP_DATE_DEBUT"/></LABEL></TD>
								<TD>
									<ct:FWCalendarTag name="dateDebut" value="<%=viewBean.getDateDebut()%>"/>
									<script language="javascript">
									 	  	firstElement = document.getElementById("dateDebut");
									 	  	buttonFirstElement  = document.getElementById("anchor_dateDebut");
									 	  	firstElement.onblur = function() {buttonFirstElement.focus();};				 	  	
								 	</script>
								</TD>
								<TD><LABEL for="dateFin"><ct:FWLabel key="JSP_DATE_FIN"/></LABEL></TD>
								<TD ><ct:FWCalendarTag name="dateFin" value="<%=viewBean.getDateFin()%>"/></TD>
							</TR>
							<TR>
								<TD><LABEL><ct:FWLabel key="JSP_AGENT_EXECUTION"/></LABEL></TD>
								<TD>
									<TABLE>
									    <TR>
									    	<TD colspan="2"><INPUT type="text" name="nomAgentExecution" class="PRlibelleLongDisabled" readonly value="<%=viewBean.getNomAgentExecution()%>"></TD>
									    </TR>
										<TR>
											<TD><LABEL><ct:FWLabel key="JSP_TIERS"/></LABEL>
												<ct:FWSelectorTag
														name="selecteurTiersAgentExecution"
														
														methods="<%=viewBean.getMethodesSelecteurAgentExecution()%>"
														providerApplication="pyxis"
														providerPrefix="TI"
														providerAction="pyxis.tiers.tiers.chercher"
														target="fr_main"
														redirectUrl="<%=mainServletPath%>"/>
											</TD>
											<TD><LABEL><ct:FWLabel key="JSP_ADMINISTRATION"/></LABEL>
												<ct:FWSelectorTag
														name="selecteurAdminAgentExecution"
														
														methods="<%=viewBean.getMethodesSelecteurAgentExecution()%>"
														providerApplication="pyxis"
														providerPrefix="TI"
														providerAction="pyxis.tiers.administration.chercher"
														target="fr_main"
														redirectUrl="<%=mainServletPath%>"/>
											</TD>
										</TR>
									</TABLE>
								</TD>
								<TD><LABEL for="codeIsoLangueAttestation"><ct:FWLabel key="JSP_LANGUE"/></LABEL></TD>
								<TD>
								   <ct:select name="codeIsoLangueAttestation" defaultValue="<%=viewBean.getCodeIsoLangueAttestation()%>" wantBlank="true">
										<ct:option label="<%=objSession.getLabel(\"JSP_FRANCAIS\")%>" value="<%=globaz.pyxis.api.ITITiers.CS_FRANCAIS%>"></ct:option>
										<ct:option label="<%=objSession.getLabel(\"JSP_ALLEMAND\")%>" value="<%=globaz.pyxis.api.ITITiers.CS_ALLEMAND%>"></ct:option>
									</ct:select>
								</TD>
							</TR>
							<TR>
								<TD colspan="4">
									<INPUT type="hidden" name="idPrononce" value="<%=idPrononce%>">
									<INPUT type="hidden" name="noAVS" value="<%=noAVS%>">
									<INPUT type="hidden" name="dateDebutPrononce" value="<%=dateDebutPrononce%>">
									<INPUT type="hidden" name="csTypeIJ" value="<%=csTypeIJ%>">
									<INPUT type="hidden" name="idTiers" value="<%=viewBean.getIdTiers()%>">	
								</TD>
							</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>

	<INPUT type="button" value="<ct:FWLabel key="JSP_ARRET"/> (alt+<ct:FWLabel key="AK_AGE_ARRET"/>)" onclick="arret()" accesskey="<ct:FWLabel key="AK_AGE_ARRET"/>">
	<INPUT type="button" value="<ct:FWLabel key="JSP_SUIVANT"/> (alt+<ct:FWLabel key="AK_AGE_SUIVANT"/>)" onclick="versEcranSuivant()" accesskey="<ct:FWLabel key="AK_AGE_SUIVANT"/>">
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>