<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran="PAP0011";

globaz.apg.vb.droits.APPereMatViewBean viewBean = (globaz.apg.vb.droits.APPereMatViewBean) session.getAttribute("viewBean");

bButtonCancel = false;
bButtonValidate = false;
bButtonDelete = false;
bButtonUpdate = viewBean.isModifiable() && bButtonUpdate;
bButtonNew = false;
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
	<ct:menuChange displayId="menu" menuId="ap-menuprincipalamat" showTab="menu"/>
	<ct:menuChange displayId="options" menuId="ap-optionsempty"/>

<SCRIPT>
  function add() {
    document.forms[0].elements('userAction').value="<%=globaz.apg.servlet.IAPActions.ACTION_PERE_MAT%>.ajouter"
  }
  function upd() {}

  function validate() {
    state = true
    if (<%=viewBean.isNew()%>)
        document.forms[0].elements('userAction').value="<%=globaz.apg.servlet.IAPActions.ACTION_PERE_MAT%>.ajouter";
    else
        document.forms[0].elements('userAction').value="<%=globaz.apg.servlet.IAPActions.ACTION_PERE_MAT%>.modifier";
    return state;
  }

  function cancel() {
    if (document.forms[0].elements('_method').value == "add")
      document.forms[0].elements('userAction').value="back";
    else
      document.forms[0].elements('userAction').value="<%=globaz.apg.servlet.IAPActions.ACTION_PERE_MAT%>.chercher";
  }

  function del() {
    if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
        document.forms[0].elements('userAction').value="<%=globaz.apg.servlet.IAPActions.ACTION_PERE_MAT%>.supprimer";
        document.forms[0].submit();
    }
  }
  
  function init(){
  }
  
  function arret() {
	document.location.href = "apg?userAction=<%=globaz.apg.servlet.IAPActions.ACTION_DROIT_LAPG%>.chercher";
  }
	
  function versEcran4() {
	if (validate()) {
		action(COMMIT);
	}
  }
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_TITRE_SAISIE_MAT_3"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><ct:FWLabel key="JSP_NSS_ABREGE"/></TD>
							<TD><INPUT type="text" name="" value="<%=viewBean.getNoAVSDroitMat()%>" class="disabled" readonly></TD>
							<TD><ct:FWLabel key="JSP_PRENOM"/> <ct:FWLabel key="JSP_NOM"/></TD>
							<TD><INPUT type="text" name="" value="<%=viewBean.getNomPrenomDroitMat()%>" class="libelleLong disabled" readonly></TD>
							<TD><ct:FWLabel key="JSP_DATE_DEBUT"/></TD>
							<TD><INPUT type="text" name="" value="<%=viewBean.getDateDebutDroitMat()%>" class="date disabled" readonly></TD>
						</TR>
						<TR><TD colspan="6"><HR></TD></TR>
						<TR>
							<TD><LABEL for="type"><ct:FWLabel key="JSP_TYPE_SIT_FAM"/></LABEL></TD>
							<TD><%=viewBean.getTypeLibelle()%></TD>
							<TD colspan="4"></TD>
						</TR>
						<TR><TD colspan="6">&nbsp;</TD></TR>
						<TR>
							<TD><LABEL for="noAVS"><ct:FWLabel key="JSP_NSS_ABREGE"/></LABEL></TD>
							<TD colspan="5">
								<INPUT type="text" name="noAVS" value="<%=viewBean.getNoAVS()%>">
								<INPUT type="hidden" name="<%=globaz.apg.servlet.APAbstractDroitDTOAction.PARAM_ID_DROIT%>" value="<%=viewBean.getIdDroitMaternite()%>">
							</TD>
						</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<INPUT type="button" class="btnCtrl" id="btnArret" value="<ct:FWLabel key="JSP_ARRET"/> (alt+<ct:FWLabel key="AK_MATERNITE_ARRET"/>)" onclick="arret()" accesskey="<ct:FWLabel key="AK_MATERNITE_ARRET"/>">
				<INPUT type="button" class="btnCtrl" id="btnSuivant" value="<ct:FWLabel key="JSP_SUIVANT"/> (alt+<ct:FWLabel key="AK_MATERNITE_SUIVANT"/>)" onclick="versEcran4()" accesskey="<ct:FWLabel key="AK_MATERNITE_SUIVANT"/>">
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>