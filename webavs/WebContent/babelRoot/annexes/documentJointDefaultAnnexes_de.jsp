<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%--
INFO: les labels de cette page sont pr�fix�s avec 'JSP_CTD_D'
--%>
<%
idEcran="GCT0002";
globaz.babel.vb.annexes.CTDocumentJointDefaultAnnexesViewBean viewBean = (globaz.babel.vb.annexes.CTDocumentJointDefaultAnnexesViewBean) session.getAttribute("viewBean");

bButtonCancel = false;
selectedIdValue = viewBean.getIdDocument();

%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
		<script language="JavaScript">

  function add() {
    document.forms[0].elements('userAction').value="<%=globaz.babel.servlet.CTMainServletAction.ACTION_DEFAULT_ANNEXE%>.ajouter"
  }
  
  function upd() {}

  function validate() {
    state = true;
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="<%=globaz.babel.servlet.CTMainServletAction.ACTION_DEFAULT_ANNEXE%>.ajouter";
    else
        document.forms[0].elements('userAction').value="<%=globaz.babel.servlet.CTMainServletAction.ACTION_DEFAULT_ANNEXE%>.modifier";
    return state;
  }

  function cancel() {
    if (document.forms[0].elements('_method').value == "add")
      document.forms[0].elements('userAction').value="back";
    else
      document.forms[0].elements('userAction').value="<%=globaz.babel.servlet.CTMainServletAction.ACTION_DEFAULT_ANNEXE%>.afficher";
  }

  function del() {
    if (window.confirm("Vous �tes sur le point de supprimer l'objet s�lectionn�! Voulez-vous continuer?")){
        document.forms[0].elements('userAction').value="<%=globaz.babel.servlet.CTMainServletAction.ACTION_DEFAULT_ANNEXE%>.supprimer";
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

</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_CTA_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><LABEL for="csAnnexe"><ct:FWLabel key="JSP_CTA_D_ANNEXE"/></LABEL></TD>
							<TD>
								<ct:select name="csAnnexe" defaultValue="<%=viewBean.getCsAnnexe()%>" >
									<ct:optionsCodesSystems csFamille="<%=viewBean.getCsGroupeAnnexes()%>"/>
								</ct:select>
								<INPUT type="hidden" name="idDocument" value="<%=viewBean.getIdDocument()%>">
								<INPUT type="hidden" name="csGroupeAnnexes" value="<%=viewBean.getCsGroupeAnnexes()%>">
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