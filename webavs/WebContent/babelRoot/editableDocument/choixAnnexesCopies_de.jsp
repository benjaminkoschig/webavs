<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%--
INFO: les labels de cette page sont préfixés avec 'JSP_CTD_D'
--%>
<%
idEcran="GCT0006";
globaz.babel.api.doc.impl.CTScalableDocumentAbstractViewBeanDefaultImpl viewBean = (globaz.babel.api.doc.impl.CTScalableDocumentAbstractViewBeanDefaultImpl) session.getAttribute("viewBean");

bButtonUpdate = false;
bButtonValidate = false;
bButtonDelete = false;
bButtonCancel = false;
	
subTableWidth = "90%";

%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.jade.publish.client.JadePublishDocument"%>
<script language="JavaScript">

  function add() {
  }
  
  function upd() {}

  function validate() {
    state = true;
    return state;
  }

  function cancel() {
  }
  
  function postInit(){
  		document.all('processStarted').style.display = 'none';
  		
	//Affichage de la décision dans une autre fenêtre	
	<%if(viewBean.getDocumentsPreview() != null && viewBean.getDocumentsPreview().size()>0){
		for(int i=0;i<viewBean.getDocumentsPreview().size();i++){
			String docName = ((JadePublishDocument)viewBean.getDocumentsPreview().get(i)).getDocumentLocation();
			docName = docName.substring(docName.lastIndexOf("/"));%>
			window.open("<%=request.getContextPath()+((String)request.getAttribute("mainServletPath"))+"Root/work" + docName%>");
	<%	}
	}
	%>
  }

  function del() {
    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
        document.forms[0].elements('userAction').value="<%=globaz.babel.servlet.CTMainServletAction.ACTION_DEFAULT_COPIE%>.supprimer";
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
  
  function actionAjouterAnnexes(vall){
    if(vall!=""){
	  	document.forms[0].elements('userAction').value="<%=globaz.babel.servlet.CTMainServletAction.ACTION_ANNEXES_COPIES%>.reAfficher";
	  	document.forms[0].elements('action').value="<%=globaz.babel.servlet.CTChoixAnnexesCopiesAction.ACTION_AJOUTER_ANNEXE%>";
		document.forms[0].submit();
	}
  }
  
  function actionSupprimerAnnexes(selectIndex){
  	if(selectIndex>(-1)){
	   document.forms[0].elements('userAction').value="<%=globaz.babel.servlet.CTMainServletAction.ACTION_ANNEXES_COPIES%>.reAfficher";
       document.forms[0].elements('action').value="<%=globaz.babel.servlet.CTChoixAnnexesCopiesAction.ACTION_SUPPRIMER_ANNEXE%>";
	   document.forms[0].elements('selectedIndex').value=document.forms[0].annexesList.options[selectIndex].value;
	   document.forms[0].submit();
	}
  } 
  
  function actionSupprimerCopies(selectIndex){ 
  	if(selectIndex>(-1)){
	   document.forms[0].elements('userAction').value="<%=globaz.babel.servlet.CTMainServletAction.ACTION_ANNEXES_COPIES%>.reAfficher";
       document.forms[0].elements('action').value="<%=globaz.babel.servlet.CTChoixAnnexesCopiesAction.ACTION_SUPPRIMER_COPIE%>";
	   document.forms[0].elements('selectedIndex').value=document.forms[0].copiesList.options[selectIndex].value;
	   document.forms[0].submit();
	}
  }
  
  function arret() {
	document.forms[0].elements('userAction').value = "<%=globaz.babel.servlet.CTMainServletAction.ACTION_ANNEXES_COPIES%>.arreterGenererDocument";
  	document.forms[0].submit();
  }
  
  function versEcranSuivant() {
  	document.all('processStarted').style.display = 'block';
  	window.setTimeout("genererDoc()", 2000);

  }
  
  	function versEcranPrecedent() {
		document.forms[0].elements('userAction').value = "<%=globaz.babel.servlet.CTMainServletAction.ACTION_ANNEXES_COPIES%>.allerVersEcranPrecedent";
		document.forms[0].target = "fr_main";
		document.forms[0].submit();
  	}
  
  function genererDoc() {
	document.forms[0].elements('userAction').value = "<%=globaz.babel.servlet.CTMainServletAction.ACTION_ANNEXES_COPIES%>.genererDocument";
	document.forms[0].submit();
  }
  
  function afficherDocument(){
	document.forms[0].elements('userAction').value = "<%=globaz.babel.servlet.CTMainServletAction.ACTION_ANNEXES_COPIES%>.afficherDocument";
	document.forms[0].submit();
  }

</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_CTAC_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD colspan="3">
								<TABLE border="0" cellspacing="0" cellpadding="0" width="100%">
									<TR>
										<TD><ct:FWLabel key="JSP_CTAC_D_DOCUMENT"/></TD>
										<TD>
											<INPUT name="documentNameAndDomaine" type="text" value="<%=viewBean.getSession().getCodeLibelle(viewBean.getDocumentCsType())%> - <%=viewBean.getSession().getCodeLibelle(viewBean.getDocumentCsDomaine())%>"  class="disabled" readonly>
										</TD>
										<TD><ct:FWLabel key="JSP_CTAC_D_DESTINATAIRE"/></TD>
										<TD>
											<INPUT name="documentDestinataire" type="text" value="<%=viewBean.getSession().getCodeLibelle(viewBean.getDocumentCsDestinataire())%>" class="disabled" readonly>
										</TD>
									</TR>
									<TR>
										<TD colspan="4" height="15">&nbsp;</TD>
									</TR>
									<TR>
										<TD><ct:FWLabel key="JSP_CTAC_D_TIERS_PRINCIPAL"/></TD>
										<TD>
											<INPUT name="prenomNomTiersPrincipal" type="text" value="<%=viewBean.getNomPrenomTiersPrincipal()%>" class="disabled" readonly>
										</TD>
										<TD><ct:FWLabel key="JSP_CTAC_D_NSS_ABREGE"/></TD>
										<TD>
											<INPUT name="noAvsTiersPrincipal" type="text" value="<%=viewBean.getNoAVSTiersPrincipal()%>" class="disabled" readonly>
										</TD>
									</TR>
									<TR>
										<TD colspan="4" height="30">&nbsp;</TD>
									</TR>
								</TABLE>
							</TD>
						</TR>
						<TR>
							<TD width="55%" height="20" valign="top"><ct:FWLabel key="JSP_CTAC_D_COPIES"/>&nbsp;</TD>
							<TD width="5%"  height="20" valign="top">&nbsp;</TD>
							<TD width="40%" height="20" valign="top"><ct:FWLabel key="JSP_CTAC_D_ANNEXES"/>&nbsp;</TD>
						</TR>
						<TR>
							<TD width="55%">
								<TABLE border="0" cellspacing="0" cellpadding="0" width="90%">
									<TR>
										<TD>
											<SELECT name="copiesList" multiple size=6 style="width:10cm;">
												<%
												java.util.Iterator copIter = viewBean.getScalableDocumentProperties().getCopiesIterator();
												while(copIter.hasNext()){
													globaz.babel.api.doc.ICTScalableDocumentCopie cop = (globaz.babel.api.doc.ICTScalableDocumentCopie)copIter.next();%>
													<%if(globaz.jade.client.util.JadeStringUtil.isEmpty(cop.getCsIntervenant())){%>
														<OPTION value="<%=cop.getKey()%>" style="color:#66f">
															<%=cop.getPrenomNomTiers()%>&nbsp;
														</OPTION>
													<%} else if(globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(cop.getIdTiers())){%>
														<OPTION value="<%=cop.getKey()%>" style="color:#f66">
															<%=viewBean.getSession().getCodeLibelle(cop.getCsIntervenant())
															   + " - " +
															   cop.getPrenomNomTiers()%>&nbsp;
														</OPTION>
													<%} else{%>
														<OPTION value="<%=cop.getKey()%>">
															<%=viewBean.getSession().getCodeLibelle(cop.getCsIntervenant())
															   + " - " +
															   cop.getPrenomNomTiers()%>&nbsp;
														</OPTION>
													<%}%>
												<%}%>
											</SELECT>
										</TD>
										<TD width="100">
											<BUTTON  name="supprimerCopies" onclick="actionSupprimerCopies(document.forms.mainForm.copiesList.options.selectedIndex);"><ct:FWLabel key="JSP_CTAC_D_SUPPRIMER"/></BUTTON>
										</TD>
									</TR>
								</TABLE>	
							</TD>
							<TD width="5%">&nbsp;</TD>
							<TD width="40%">
								<TABLE border="0" cellspacing="0" cellpadding="0" width="100%">
									<TR>
										<TD>
											<SELECT name="annexesList" multiple size=6 style="width:7cm;">
												<%
												java.util.Iterator annIter = viewBean.getScalableDocumentProperties().getAnnexesIterator();
												while(annIter.hasNext()){
													globaz.babel.api.doc.ICTScalableDocumentAnnexe ann = (globaz.babel.api.doc.ICTScalableDocumentAnnexe)annIter.next();%>
													<OPTION value="<%=ann.getKey()%>"><%=ann.getLibelle()%>&nbsp;</OPTION>
												<%}%>
											</SELECT>
										</TD>
										<TD width="100">
											<BUTTON name="supprimerAnnexes" onclick="actionSupprimerAnnexes(document.forms.mainForm.annexesList.options.selectedIndex);"><ct:FWLabel key="JSP_CTAC_D_SUPPRIMER"/></BUTTON>
										</TD>
									</TR>
								</TABLE>
							</TD>
						</TR>
						<TR >
							<TD width="55%">
								<TABLE border="0" cellspacing="0" cellpadding="0" width="90%">
									<TR>
										<TD><ct:FWLabel key="JSP_CTAC_D_AJOUTER_TIERS"/></TD>
										<TD width="100">
											<ct:FWSelectorTag
											name="selecteurTiers"
											
											methods="<%=viewBean.getMethodesSelecteurIntervenant()%>"
											providerApplication="pyxis"
											providerPrefix="TI"
											providerAction="pyxis.tiers.tiers.chercher"
											target="fr_main"
											redirectUrl="<%=mainServletPath%>"/>
										</TD>
									</TR>
									<TR>
										<TD><ct:FWLabel key="JSP_CTAC_D_AJOUTER_ADMIN"/></TD>
										<TD width="100">
											<ct:FWSelectorTag
											name="selecteurAdmin"
											
											methods="<%=viewBean.getMethodesSelecteurIntervenant()%>"
											providerApplication="pyxis"
											providerPrefix="TI"
											providerAction="pyxis.tiers.administration.chercher"
											target="fr_main"
											redirectUrl="<%=mainServletPath%>"/>
										</TD>
									</TR>
								</TABLE>
							</TD>
							<TD width="5%">&nbsp;</TD>
							<TD width="40%">
								<TABLE border="0" cellspacing="0" cellpadding="0" width="100%">
									<TR>
										<TD>
											<INPUT type="text" name="nouvelAnnexe" class="libelleLong">
											<INPUT type="hidden" name="selectedIndex" value="">
											<INPUT type="hidden" name="action" value="">
											<INPUT name="wantSelectParagraph" type="hidden" value="<%=viewBean.getWantSelectParagraph()%>">
											<INPUT name="wantEditParagraph" type="hidden" value="<%=viewBean.getWantEditParagraph()%>">
											<INPUT name="wantSelectAnnexeCopie" type="hidden" value="<%=viewBean.getWantSelectAnnexeCopie()%>">
										</TD>
										<TD width="100">
											<BUTTON name="ajouterAnnexes" onclick="actionAjouterAnnexes(nouvelAnnexe.value);"><ct:FWLabel key="JSP_CTAC_D_AJOUTER"/></BUTTON>
										</TD>
									</TR>
								</TABLE>
							</TD>
						</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<INPUT type="button" value="<ct:FWLabel key="JSP_AFFICHER"/> (alt+<ct:FWLabel key="AK_PRO_AFFICHER"/>)" onclick="afficherDocument()" accesskey="<ct:FWLabel key="AK_PRO_AFFICHER"/>">
				<INPUT type="button" value="<ct:FWLabel key="JSP_ARRET"/> (alt+<ct:FWLabel key="AK_PRO_ARRET"/>)" onclick="arret()" accesskey="<ct:FWLabel key="AK_PRO_ARRET"/>">
				<INPUT type="button" value="<ct:FWLabel key="JSP_PRECEDENT"/> (alt+<ct:FWLabel key="AK_PRO_PRECEDENT"/>)" onclick="versEcranPrecedent()" accesskey="<ct:FWLabel key="AK_PRO_PRECEDENT"/>">
				<INPUT type="button" value="<ct:FWLabel key="JSP_SUIVANT"/> (alt+<ct:FWLabel key="AK_PRO_SUIVANT"/>)" onclick="versEcranSuivant()" accesskey="<ct:FWLabel key="AK_PRO_SUIVANT"/>">
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<Table width="100%">
	<TBODY id="processStarted">
		<tr>
			<td colspan="3" style="height: 2em; color: white; font-weight: bold; text-align: center;background-color: green"><ct:FWLabel key="FW_PROCESS_STARTED"/></td>
		</tr>
	</TBODY>
</Table>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>