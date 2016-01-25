<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%--
INFO: les labels de cette page sont préfixés avec 'JSP_TIM_D'
--%>
<%
// Les labels de cette page commence par la préfix "JSP_TAU_D"

idEcran="PRE0095";
globaz.corvus.vb.taux.RETauxViewBean viewBean = (globaz.corvus.vb.taux.RETauxViewBean) session.getAttribute("viewBean");

bButtonCancel = false;
selectedIdValue = viewBean.getIdTaux();

bButtonValidate = bButtonValidate &&  controller.getSession().hasRight(IREActions.ACTION_TAUX, FWSecureConstants.UPDATE);

%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
		<%@page import="globaz.framework.controller.FWController"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.corvus.servlet.IREActions"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<script language="JavaScript">

  function add() {
    document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_TAUX%>.ajouter"
  }
  
  function upd() {}

  function validate() {
    state = true;
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_TAUX%>.ajouter";
    else
        document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_TAUX%>.modifier";
    return state;
  }

  function cancel() {
    if (document.forms[0].elements('_method').value == "add")
      document.forms[0].elements('userAction').value="back";
    else
      document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_TAUX%>.afficher";
  }

  function del() {
    if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
        document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_TAUX%>.supprimer";
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
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_TAU_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><LABEL for="csTypeTaux"><ct:FWLabel key="JSP_TAU_D_TYPE"/></LABEL></TD>
							<TD>
								<ct:select name="csTypeTaux" defaultValue="<%=viewBean.getCsTypeTaux()%>">
									<ct:optionsCodesSystems csFamille="<%=globaz.corvus.api.taux.IRETaux.CS_GROUPE_TYPE_TAUX%>"/>
								</ct:select>
							</TD>
							<TD><LABEL for="taux"><ct:FWLabel key="JSP_TAU_D_TAUX"/></LABEL></TD>
							<TD>
								<INPUT type="text" name="taux" value="<%=viewBean.getTaux()%>" class="taux" onchange="validateFloatNumber(this,6);" onkeypress="return filterCharForFloat(window.event);">
							</TD>
						</TR>
						<TR>
							<TD><LABEL for="dateDebut"><ct:FWLabel key="JSP_TAU_D_DU"/></LABEL></TD>
							<TD>
								<input	id="dateDebut"
										name="dateDebut"
										data-g-calendar="type:default"
										value="<%=viewBean.getDateDebut()%>" />
							</TD>
							<TD><LABEL for="dateFin"><ct:FWLabel key="JSP_TAU_D_AU"/></LABEL></TD>
							<TD>
								<input	id="dateFin"
										name="dateFin"
										data-g-calendar="type:default"
										value="<%=viewBean.getDateFin()%>" />
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