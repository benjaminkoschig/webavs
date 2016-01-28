<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	selectedIdValue = request.getParameter("selectedId");
	idEcran="PIJ0034";
	IJPrononceAllocAssistanceViewBean viewBean = (IJPrononceAllocAssistanceViewBean) session.getAttribute("viewBean");
	String detailRequerant = request.getParameter("detailRequerant");
	String noAVS = request.getParameter("noAVS");

	bButtonUpdate = bButtonUpdate && viewBean.isModifierPermis();
	bButtonValidate = !IIJPrononce.CS_ATTENTE.equals(viewBean.getCsEtat());
	bButtonDelete = false;
	bButtonCancel = false;
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.ij.vb.prononces.IJPrononceAitViewBean"%>
<%@page import="globaz.ij.vb.prononces.IJPrononceAllocAssistanceViewBean"%>
<%@page import="globaz.ij.api.prononces.IIJPrononce"%>
<ct:menuChange displayId="menu" menuId="ij-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="ij-optionsempty"/>
<SCRIPT language="JavaScript1.2" src="<%=servletContext%>/ijRoot/masks.js"></SCRIPT>

<SCRIPT language="JavaScript1.2">
	function add() {
	    //deja créé qd on arrive ici
	}

	function upd() {
		document.forms[0].elements('userAction').value="ij.prononces.saisiePrononceAllocAssistance.modifier";
		document.forms[0].elements('modifie').value="true";
	}

	function validate() {
		document.forms[0].elements('userAction').value="ij.prononces.saisiePrononceAllocAssistance.modifier";

	    return true;

	}

	function arret() {
		document.forms[0].elements('userAction').value = "<%=globaz.ij.servlet.IIJActions.ACTION_SAISIE_PRONONCE_ALLOC_ASSIST%>.arreterEtape3";
  		document.forms[0].submit();
 	}

	function cancel() {

	if (document.forms[0].elements('_method').value == "add")
	  document.forms[0].elements('userAction').value="back";
	 else
	  document.forms[0].elements('userAction').value="ij.prononces.prononceJointDemande.chercher";

	}

	function del() {
	    //On peut pas ici
	}

	function init(){
	}

	function arret() {
		document.forms[0].elements('userAction').value = "<%=globaz.ij.servlet.IIJActions.ACTION_SAISIE_PRONONCE_ALLOC_ASSIST%>.arreterEtape3";
  		document.forms[0].submit();
 	}

  function calculer(){
	document.forms[0].elements('userAction').value = "<%=globaz.ij.servlet.IIJActions.ACTION_SAISIE_PRONONCE_ALLOC_ASSIST%>.calculer";
	document.forms[0].target = "fr_main";
	document.forms[0].submit();
  }

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_SAISIE_PRONONCE_ALLOC_ASS"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD colspan="6">
								<INPUT type="hidden" name="csTypeIJ" value="<%=viewBean.getCsTypeIJ()%>">
								<INPUT type="hidden" name="modifie" value="<%=viewBean.isModifie()%>">
								<INPUT type="hidden" name="noAVS" value="<%=noAVS%>">
								<INPUT type="hidden" name="detailRequerant" value="<%=detailRequerant%>">
							</TD>
						</TR>
						<TR>
							<TD><b><ct:FWLabel key="JSP_DETAIL_REQUERANT"/></b></TD>
							<TD colspan="5">
								<INPUT type="text" value="<%=detailRequerant%>" size="100" class="disabled" readonly>
							</TD>
						</TR>
						<TR>
							<TD colspan="6"><HR></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_DATE_PRONONCE"/></TD>
							<TD colspan="2"><ct:FWCalendarTag name="dateDebutPrononce" value="<%=viewBean.getDateDebutPrononce()%>"/></TD>
							<TD><ct:FWLabel key="JSP_OFFICE_AI"/></TD>
							<TD colspan="2">
								<ct:FWListSelectTag data="<%=viewBean.getListeOfficeAI()%>" defaut="<%=globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getOfficeAI())?viewBean.getNoOfficeAICantonal():viewBean.getOfficeAI()%>" name="officeAI"/>
							</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_MONTANT_TOTAL"/></TD>
							<TD colspan="2"><INPUT type="text" name="montantTotal" value="<%=viewBean.getMontantTotal()%>" class="montant" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);"></TD>
							<TD><ct:FWLabel key="JSP_GENRE_READAPTATION"/></TD>
							<TD colspan="2">
								<ct:select name="csGenre" defaultValue="<%=viewBean.getCsGenre()%>">
									<ct:optionsCodesSystems csFamille="<%=globaz.ij.api.prononces.IIJPrononce.CS_GROUPE_GENRE_READAPTATION%>">
									</ct:optionsCodesSystems>
								</ct:select>
							</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_NO_DECISION_AI_COMMUNICATION"/></TD>
							<TD colspan="5"><INPUT type="text" name="noDecisionAI" value="<%=viewBean.getNoDecisionAI()%>" class="libelleLong">&nbsp;<ct:FWLabel key="JSP_FORMAT_NO_DECISION"/></TD>
						</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<INPUT type="button" value="<ct:FWLabel key="JSP_ARRET"/> (alt+<ct:FWLabel key="AK_PRO_ARRET"/>)" onclick="arret()" accesskey="<ct:FWLabel key="AK_PRO_ARRET"/>">
				<%if(IIJPrononce.CS_ATTENTE.equals(viewBean.getCsEtat())){%>
					<ct:ifhasright element="ij.prononces.saisiePrononceAllocAssistance.calculer" crud="upd">
						<INPUT type="button" value="<ct:FWLabel key="JSP_CALCULER"/> (alt+<ct:FWLabel key="AK_SITPRO_CALCULER"/>)" onclick="calculer()" accesskey="<ct:FWLabel key="AK_SITPRO_CALCULER"/>">
					</ct:ifhasright>
				<%}%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>