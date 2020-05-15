<%-- tpl:insert page="/theme/capage.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/capage/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran="PAP0009";

// récupère le viewBean qui a ete stocke dans la session par l'action APSituationFamilialePanAction
globaz.apg.vb.droits.APEnfantPanViewBean viewBean = (globaz.apg.vb.droits.APEnfantPanViewBean) request.getAttribute("viewBean");
bButtonNew = viewBean.isModifiable() && bButtonNew && viewBean.getSession().hasRight(IAPActions.ACTION_ENFANT_PAN, FWSecureConstants.UPDATE);
actionNew += "&" + globaz.apg.servlet.APAbstractDroitDTOAction.PARAM_ID_DROIT + "=" + viewBean.getIdDroit();
// on aggrandit un peu le frame de detail
IFrameDetailHeight = "250";

%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="globaz.apg.servlet.IAPActions"%>
<ct:menuChange displayId="menu" menuId="ap-menuprincipalpan" showTab="menu"/>
<ct:menuChange displayId="options" menuId="ap-optionsempty"/>

<SCRIPT>

	// pour le gestion des avertissements après l'action EcranSuivant
	var isModification = false;
	var isNouveau = true;

	var warningObj = new Object();
	warningObj.text = "";

	function showWarnings() {
		if (warningObj.text != "") {
			showModalDialog('<%=servletContext%>/warningModalDlg.jsp',warningObj,'dialogHeight:20;dialogWidth:25;status:no;resizable:no');
		}
	}

	bFind = true;
	usrAction = "<%=globaz.apg.servlet.IAPActions.ACTION_ENFANT_PAN%>.lister";
	detailLink = servlet + "?userAction=<%=globaz.apg.servlet.IAPActions.ACTION_ENFANT_PAN%>.afficher&_method=add&<%=globaz.apg.servlet.APAbstractDroitDTOAction.PARAM_ID_DROIT%>=<%=viewBean.getIdDroit()%>";

	function arret() {
		document.location.href = servlet + "?userAction=<%=globaz.apg.servlet.IAPActions.ACTION_DROIT_LAPG%>.chercher";
	}

	function versEcran4() {
		var destination = "?userAction=<%=globaz.apg.servlet.IAPActions.ACTION_SAISIE_CARTE_PAN_SITUATION%>.afficher&<%=globaz.apg.servlet.APAbstractDroitDTOAction.PARAM_ID_DROIT%>=<%=viewBean.getIdDroit()%>";

		if(isModification ||(isNouveau && document.fr_detail.document.forms[0].elements('nom').value.length>0)){
			warningObj.text="<ct:FWLabel key='JSP_MODIFS_NON_VALIDEES_PERDUES'/>";
			showWarnings();
			isModification=false;
			isNouveau=false;
		}else{
			document.location.href = servlet + destination;
		}
	}

	function onClickNew() {
		isNouveau = true;
		return 1;
	}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_TITRE_CONJOINT_ENFANT"/><%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR><TD>
						<TABLE border="0">
							<TR>
							<TR>
								<TD><B><ct:FWLabel key="JSP_DETAIL_REQUERANT"/></B></TD>
								<TD><INPUT type="text" value="<%=viewBean.getDetailRequerantDetail()%>" size="100" class="disabled" readonly></TD>
							</TR>
							<TR>
								<TD><ct:FWLabel key="JSP_DATE_DEBUT"/></TD>
								<TD><INPUT type="text" name="dateDebutDroit" value="<%=viewBean.getDateDebutDroitMat()%>" class="date disabled" readonly></TD>
							</TR>
						</TABLE>
						<!-- le seul champ de restriction de la recherche ACTIF est privé -->
						<INPUT type="hidden" name="forIdDroit" value="<%=viewBean.getIdDroit()%>">
						<INPUT type="hidden" name="<%=globaz.apg.servlet.APAbstractDroitDTOAction.PARAM_ID_DROIT%>" value="<%=viewBean.getIdDroit()%>">
						</TD></TR>
	 					<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<FORM action="#" target="fr_main" style="text-align: right">
	<INPUT type="button" value="<ct:FWLabel key="JSP_ARRET"/> (alt+<ct:FWLabel key="AK_APG_ARRET"/>)" onclick="arret()" accesskey="<ct:FWLabel key="AK_APG_ARRET"/>">
	<INPUT type="button" value="<ct:FWLabel key="JSP_SUIVANT"/> (alt+<ct:FWLabel key="AK_APG_SUIVANT"/>)" onclick="versEcran4()" accesskey="<ct:FWLabel key="AK_APG_SUIVANT"/>">
</FORM>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyClose.jspf" %>
<%-- /tpl:insert --%>