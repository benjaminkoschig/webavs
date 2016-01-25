<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%--
INFO: les labels de cette page sont préfixés avec 'JSP_CTD_D'
--%>
<%

globaz.babel.vb.cat.CTDocumentViewBean viewBean = (globaz.babel.vb.cat.CTDocumentViewBean) session.getAttribute("viewBean");

bButtonCancel = false;
selectedIdValue = viewBean.getIdDocument();

bButtonValidate = objSession.hasRight("babel.cat.document.actionAjouter", "ADD");

// desactiver les boutons si le viewBean est non editable
if (!viewBean.isDocumentEditable()) {
	bButtonUpdate = false;
	bButtonValidate = false;
	bButtonDelete = false;
}

%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
		<script language="JavaScript">

  function add() {
    document.forms[0].elements('userAction').value="<%=globaz.babel.servlet.CTMainServletAction.ACTION_DOCUMENTS%>.ajouter"
  }
  function upd() {}

  function validate() {
    state = true;
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="<%=globaz.babel.servlet.CTMainServletAction.ACTION_DOCUMENTS%>.ajouter";
    else
        document.forms[0].elements('userAction').value="<%=globaz.babel.servlet.CTMainServletAction.ACTION_DOCUMENTS%>.modifier";
    return state;
  }

  function cancel() {
    if (document.forms[0].elements('_method').value == "add")
      document.forms[0].elements('userAction').value="back";
    else
      document.forms[0].elements('userAction').value="<%=globaz.babel.servlet.CTMainServletAction.ACTION_DOCUMENTS%>.afficher";
  }

  function del() {
    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
        document.forms[0].elements('userAction').value="<%=globaz.babel.servlet.CTMainServletAction.ACTION_DOCUMENTS%>.supprimer";
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
	<%if (!"add".equalsIgnoreCase(request.getParameter("_method"))) {%>
		document.forms[0].elements("csDomaine").disabled = true;
		document.forms[0].elements("csTypeDocument").disabled = true;
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
		document.forms[0].elements("userAction").value = "<%=globaz.babel.servlet.CTMainServletAction.ACTION_DOCUMENTS%>.afficher";
		document.forms[0].submit();
	}
  }
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_CTD_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<% if (viewBean.isDocumentEditable()) { %>
						<TR>
							<TD>
								<INPUT type="hidden" name="idTypeDocument" value="<%=viewBean.getIdTypeDocument()%>">
								<INPUT type="hidden" name="csGroupeDomaines" value="<%=viewBean.getCsGroupeDomaines()%>">
								<INPUT type="hidden" name="csGroupeTypesDocuments" value="<%=viewBean.getCsGroupeTypesDocuments()%>">
							
								<LABEL for="csTypeDocument"><ct:FWLabel key="JSP_CTD_D_TYPE_DE_DOCUMENT"/></LABEL>
							</TD>
							<TD>
								<ct:select name="csTypeDocument" defaultValue="<%=viewBean.getCsTypeDocument()%>" onchange="rechargerPage()" styleClass="forceDisable">
									<ct:optionsCodesSystems csFamille="<%=viewBean.getCsGroupeTypesDocuments()%>">
										<ct:forEach items="<%=viewBean.getCsTypesDocumentsExclus()%>" var="code">
											<ct:excludeCode id="code"/>
										</ct:forEach>
									</ct:optionsCodesSystems>
								</ct:select>
							</TD>
							<TD><LABEL for="csDomaine"><ct:FWLabel key="JSP_CTD_D_DOMAINE"/></LABEL></TD>
							<TD>
								<ct:select name="csDomaine" defaultValue="<%=viewBean.getCsDomaine()%>" onchange="rechargerPage()" styleClass="forceDisable">
									<ct:optionsCodesSystems csFamille="<%=viewBean.getCsGroupeDomaines()%>"/>
								</ct:select>
							</TD>
						</TR>
						<TR>
							<TD><LABEL for="nom"><ct:FWLabel key="JSP_CTD_D_NOM"/></LABEL></TD>
							<TD><INPUT type="text" size="80" maxlength="100" name="nom" value="<%=viewBean.getNom()%>"></TD>
							<TD><LABEL for="csDestinataire"><ct:FWLabel key="JSP_CTD_D_DESTINATAIRE"/></LABEL></TD>
							<TD>
								<ct:select name="csDestinataire" defaultValue="<%=viewBean.getCsDestinataire()%>" wantBlank="true">
									<ct:optionsCodesSystems csFamille="<%=globaz.babel.api.ICTDocument.CS_GROUPE_DESTINATAIRE%>"/>
								</ct:select>
							</TD>
						</TR>
						<TR>
							<TD colspan="4">&nbsp;</TD>
						</TR>
						<TR>
							<TD><LABEL for="actif"><ct:FWLabel key="JSP_CTD_D_ACTIF"/></LABEL></TD>
							<TD><INPUT type="checkbox" name="actif" value="on"<%=viewBean.getActif().booleanValue()?" checked":""%>></TD>
							<TD><LABEL for="dateDesactivationBidon"><ct:FWLabel key="JSP_CTD_D_DATE_DESACTIVATION"/></LABEL></TD>
							<TD><INPUT type="text" name="dateDesactivationBidon" value="<%=viewBean.getDateDesactivation()%>" class="date disabled" readonly></TD>
						</TR>
						<% if (viewBean.isAdministrateur()) {%>
						<TR>
							<TD><LABEL for="csEditable"><ct:FWLabel key="JSP_CTD_D_EDITABLE"/></LABEL></TD>
							<TD>
								<ct:select name="csEditable" defaultValue="<%=viewBean.getCsEditable()%>">
									<ct:optionsCodesSystems csFamille="<%=globaz.babel.api.ICTDocument.CS_GROUPE_EDITABLE%>"/>
								</ct:select>
							</TD>
							<TD><LABEL for="defaut"><ct:FWLabel key="JSP_CTD_D_DEFAUT"/></LABEL></TD>
							<TD><INPUT type="checkbox" name="defaut"<%=viewBean.getDefaut().booleanValue()?" checked":""%>></TD>
						</TR>

						<TR>
							<TD><LABEL for="style"><ct:FWLabel key="JSP_CTD_D_STYLE"/></LABEL></TD>
							<TD><INPUT type="checkbox" name="isStyledDocument" value="on"<%=viewBean.getIsStyledDocument().booleanValue()?" checked":""%>></TD>
							<TD>&nbsp;</TD>
							<TD>&nbsp;</TD>
						</TR>

						<% } %>
						<% } else { %>
						<TR>
							<TD colspan="4"><P style="color: red"><ct:FWLabel key="JSP_CTD_D_NON_EDITABLE"/></P></TD>
						</TR>
						<% } %>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>