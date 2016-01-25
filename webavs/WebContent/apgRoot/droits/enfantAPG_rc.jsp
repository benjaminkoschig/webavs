<%-- tpl:insert page="/theme/capage.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/capage/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran="PAP0006";

// récupère le viewBean qui a ete stocke dans la session par l'action APEnfantAPGAction
globaz.apg.vb.droits.APEnfantAPGViewBean viewBean = (globaz.apg.vb.droits.APEnfantAPGViewBean) request.getAttribute("viewBean");
bButtonNew = false;
actionNew += "&" + globaz.apg.servlet.APAbstractDroitDTOAction.PARAM_ID_DROIT + "=" + request.getParameter(globaz.apg.servlet.APAbstractDroitDTOAction.PARAM_ID_DROIT);
IFrameDetailHeight = "250";


%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="ap-menuprincipalapg" showTab="menu"/>
<ct:menuChange displayId="options" menuId="ap-optionsempty"/>

<SCRIPT>
	bFind = true;
	usrAction = "<%=globaz.apg.servlet.IAPActions.ACTION_ENFANT_APG%>.lister";
	detailLink = servlet + "?userAction=<%=globaz.apg.servlet.IAPActions.ACTION_ENFANT_APG%>.afficher&_method=add&<%=globaz.apg.servlet.APAbstractDroitDTOAction.PARAM_ID_DROIT%>=<%=request.getParameter(globaz.apg.servlet.APAbstractDroitDTOAction.PARAM_ID_DROIT)%>";
	
	function arret() {
		document.forms[0].elements('userAction').value = "<%=globaz.apg.servlet.IAPActions.ACTION_ENFANT_APG%>.arreterEtape3";
  		document.forms[0].submit();
	}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_TITRE_SAISIE_APG_3"/><%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR><TD>
						<TABLE border="0">
							<TR>
								<TD><B><ct:FWLabel key="JSP_DETAIL_REQUERANT"/></B></TD>
								<TD><INPUT type="text" value="<%=viewBean.getDetailRequerantDetail()%>" size="100" class="disabled" readonly></TD>
							</TR>
							<TR>	
								<TD><ct:FWLabel key="JSP_DATE_DEBUT"/></TD>
								<TD><INPUT type="text" name="dateDebutDroit" value="<%=viewBean.getDateDebutDroitAPG()%>" class="date disabled" readonly></TD>
							</TR>
							<TR>
						</TABLE>
						<!-- les seuls champs de restriction de la recherche ACTIF sont privés -->
						<INPUT type="hidden" name="forIdSituationFamiliale" value="<%=viewBean.getIdSituationFamilialeAPG()%>">
						<INPUT type="hidden" name="<%=globaz.apg.servlet.APAbstractDroitDTOAction.PARAM_ID_DROIT%>" value="<%=request.getParameter(globaz.apg.servlet.APAbstractDroitDTOAction.PARAM_ID_DROIT)%>">
						<!--<INPUT type="hidden" name="idSituationFamiliale" value="<%=viewBean.getIdSituationFamilialeAPG()%>">-->
						</TD></TR>
	 					<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<FORM action="#" target="fr_main" style="text-align: right">

</FORM>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyClose.jspf" %>
<%-- /tpl:insert --%>