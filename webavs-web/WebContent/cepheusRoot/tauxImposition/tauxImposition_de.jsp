<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%--
INFO: les labels de cette page sont préfixés avec 'JSP_TIM_D'
--%>
<%
idEcran="GDO0005";
globaz.cepheus.vb.tauxImposition.DOTauxImpositionViewBean viewBean = (globaz.cepheus.vb.tauxImposition.DOTauxImpositionViewBean) session.getAttribute("viewBean");

bButtonCancel = false;
selectedIdValue = viewBean.getIdTauxImposition();

%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
		<script language="JavaScript">

  function add() {
    document.forms[0].elements('userAction').value="<%=globaz.cepheus.servlet.IDOActions.ACTION_CHERCHER_TAUX_IMPOSITIONS%>.ajouter"
  }
  
  function upd() {}

  function validate() {
    state = true;
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="<%=globaz.cepheus.servlet.IDOActions.ACTION_CHERCHER_TAUX_IMPOSITIONS%>.ajouter";
    else
        document.forms[0].elements('userAction').value="<%=globaz.cepheus.servlet.IDOActions.ACTION_CHERCHER_TAUX_IMPOSITIONS%>.modifier";
    return state;
  }

  function cancel() {
    if (document.forms[0].elements('_method').value == "add")
      document.forms[0].elements('userAction').value="back";
    else
      document.forms[0].elements('userAction').value="<%=globaz.cepheus.servlet.IDOActions.ACTION_CHERCHER_TAUX_IMPOSITIONS%>.afficher";
  }

  function del() {
    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
        document.forms[0].elements('userAction').value="<%=globaz.cepheus.servlet.IDOActions.ACTION_CHERCHER_TAUX_IMPOSITIONS%>.supprimer";
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
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_TIM_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><LABEL for="csCanton"><ct:FWLabel key="JSP_TIM_D_CANTON"/></LABEL></TD>
							<TD>
								<ct:FWCodeSelectTag name="csCanton" codeType="PYCANTON" defaut="<%=viewBean.getCsCanton()%>"/>
							</TD>
							<TD><LABEL for="typeImpotSource"><ct:FWLabel key="JSP_TIM_D_TYPE_IMPOSITION"/></LABEL></TD>
							<TD>
								<ct:select name="typeImpotSource" defaultValue="<%=viewBean.getTypeImpotSource()%>">
									<ct:optionsCodesSystems csFamille="<%=globaz.prestation.tauxImposition.api.IPRTauxImposition.CS_GROUPE_TYPE_IMPOSITION_SOURCE%>"/>
								</ct:select>
							</TD>
						</TR>
						<TR>
							<TD><LABEL for="taux"><ct:FWLabel key="JSP_TIM_D_TAUX_IMPOSITION"/></LABEL></TD>
							<TD>
								<INPUT type="text" name="taux" value="<%=viewBean.getTaux()%>" class="taux" onchange="validateFloatNumber(this,6);" onkeypress="return filterCharForFloat(window.event);">
							</TD>
						</TR>
						<TR>
							<TD><LABEL for="dateDebut"><ct:FWLabel key="JSP_TIM_D_DATE_DEBUT"/></LABEL></TD>
							<TD>
								<ct:FWCalendarTag name="dateDebut" value="<%=viewBean.getDateDebut()%>"/>
							</TD>
							<TD><LABEL for="dateFin"><ct:FWLabel key="JSP_TIM_D_DATE_FIN"/></LABEL></TD>
							<TD>
								<ct:FWCalendarTag name="dateFin" value="<%=viewBean.getDateFin()%>"/>
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