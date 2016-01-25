<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%--

INFO !!!!
Les labels de cette page sont prefixes avec 'LABEL_JSP_PRE_D'

--%>
<%
idEcran="PIJ0012";
globaz.ij.vb.prestations.IJPrestationJointLotPrononceViewBean viewBean = (globaz.ij.vb.prestations.IJPrestationJointLotPrononceViewBean) session.getAttribute("viewBean");

bButtonCancel = false;
bButtonDelete = false;
bButtonNew = false;
bButtonValidate = !viewBean.isNew() && viewBean.isModifierPermis();
bButtonUpdate = bButtonUpdate && viewBean.isModifierPermis();
bButtonDelete = viewBean.isSupprimerPermis();

%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@page import="globaz.ij.application.IJApplication"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<ct:menuChange displayId="menu" menuId="ij-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="ij-optionmenuprestation">
	<ct:menuSetAllParams key="idPrestation" value="<%=viewBean.getIdPrestation()%>"/>
    <ct:menuSetAllParams key="noAVS" value="<%=viewBean.getNoAVS()%>"/>		
	<ct:menuSetAllParams key="datePrononce" value="<%=viewBean.getDatePrononce()%>"/>
	<ct:menuSetAllParams key="dateDebutPrestations" value="<%=viewBean.getDateDebut()%>"/>
	<ct:menuSetAllParams key="dateFinPrestations" value="<%=viewBean.getDateFin()%>"/>
	<ct:menuSetAllParams key="montantBrutTotal" value="<%=viewBean.getMontantBrut()%>"/>
	<ct:menuSetAllParams key="forIdPrestation" value="<%=viewBean.getIdPrestation()%>"/>
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdPrestation()%>"/>
</ct:menuChange>

<script language="JavaScript">

  function add() {}
  function upd() {}

  function validate() {
    state = true
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_PRESTATION_JOINT_LOT_PRONONCE%>.ajouter";
    else
        document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_PRESTATION_JOINT_LOT_PRONONCE%>.modifier";
    return state;
  }

  function cancel() {
    if (document.forms[0].elements('_method').value == "add")
      document.forms[0].elements('userAction').value="back";
    else
      document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_PRESTATION_JOINT_LOT_PRONONCE%>.afficher";
  }

  function del() {
	  if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
        document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_PRESTATION_JOINT_LOT_PRONONCE%>.supprimer";
        document.forms[0].submit();
    }
  }
  	
  function init(){
	<%if ("new".equalsIgnoreCase(request.getParameter("_valid"))) {%>
	  	// mise a jour de la liste du parent
		if (parent.document.forms[0]) {
			parent.document.forms[0].submit();
		}
	<%}%>
	document.forms[0].target="fr_main";
  }
  
  function recalculerInterne() {
  	document.forms[0].elements("montantBrutInterne").value = toFloat(document.forms[0].elements("montantJournalierInterne").value) * toFloat(document.forms[0].elements("nombreJoursInt").value);
  	
  	recalculerBrut();
  }
  
  function recalculerExterne() {
  	document.forms[0].elements("montantBrutExterne").value = toFloat(document.forms[0].elements("montantJournalierExterne").value) * toFloat(document.forms[0].elements("nombreJoursExt").value);  	
  	recalculerBrut();
  }
  
  function recalculerBrut() {
  	var mi = parseFloat(toFloat(document.forms[0].elements("montantBrutInterne").value));
  	var me = parseFloat(toFloat(document.forms[0].elements("montantBrutExterne").value));
  	
  	validateFloatNumber(document.forms[0].elements("montantBrutInterne"));
  	validateFloatNumber(document.forms[0].elements("montantBrutExterne"));
  	   	
  	if (isNaN(mi) || isNaN(me)) {
	  	document.forms[0].elements("montantBrut").value = "0";
	} else {
	  	document.forms[0].elements("montantBrut").value = mi + me;
	}
	
	validateFloatNumber(document.forms[0].elements("montantBrut"));
  }
  
  function toFloat(input){
	var output = input.replace("'", ""); 
	return output;
  }

</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
			<%if(!JadeStringUtil.isIntegerEmpty(viewBean.getIdPrestation())){ %>
			<span class="postItIcon"><ct:FWNote sourceId="<%=viewBean.getIdPrestation()%>" tableSource="<%=IJApplication.KEY_POSTIT_PRESTATIONS %>"/></span>
			<%}%>
			<ct:FWLabel key="JSP_PRE_D_TITRE"/>
			<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD colspan="6"><H6><ct:FWLabel key="JSP_PRE_D_MONTANT_INTERNE"/></H6></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_PRE_D_MONTANT_JOURNALIER"/></TD>
							<TD><INPUT type="text" name="montantJournalierInterne" value="<%=viewBean.getMontantJournalierInterne()%>" onchange="validateFloatNumber(this);recalculerInterne()" onkeypress="return filterCharForFloat(window.event);"<%=(viewBean.isRestitution()||viewBean.isAitOrAllocAssist())?"class=\"montant disabled\" readonly":" class=\"montant\""%>></TD>
							<TD><ct:FWLabel key="JSP_PRE_D_NB_JOURS"/></TD>
							<TD><INPUT type="text" name="nombreJoursInt" value="<%=viewBean.getNombreJoursInt()%>" onchange="validateIntegerNumber(this);recalculerInterne()" onkeypress="return filterCharForInteger(window.event);"<%=(viewBean.isRestitution()||viewBean.isAitOrAllocAssist())?"class=\"disabled\" readonly":""%>></TD>
							<TD><ct:FWLabel key="JSP_PRE_D_MONTANT_BRUT"/></TD>
							<TD><INPUT type="text" name="montantBrutInterne" value="<%=viewBean.getMontantBrutInterne()%>" class="montant disabled" readonly></TD>
						</TR>
						<TR><TD colspan="6">&nbsp;</TD></TR>
						<TR>
							<TD colspan="6"><H6><ct:FWLabel key="JSP_PRE_D_MONTANT_EXTERNE"/></H6></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_PRE_D_MONTANT_JOURNALIER"/></TD>
							<TD><INPUT type="text" name="montantJournalierExterne" value="<%=viewBean.getMontantJournalierExterne()%>" onchange="validateFloatNumber(this);recalculerExterne()" onkeypress="return filterCharForFloat(window.event);"<%=(viewBean.isRestitution()||viewBean.isAitOrAllocAssist())?"class=\"montant disabled\" readonly":" class=\"montant\""%>></TD>
							<TD><ct:FWLabel key="JSP_PRE_D_NB_JOURS"/></TD>
							<TD><INPUT type="text" name="nombreJoursExt" value="<%=viewBean.getNombreJoursExt()%>" onchange="validateIntegerNumber(this);recalculerExterne()" onkeypress="return filterCharForInteger(window.event);"<%=(viewBean.isRestitution()||viewBean.isAitOrAllocAssist())?"class=\"disabled\" readonly":""%>></TD>
							<TD><ct:FWLabel key="JSP_PRE_D_MONTANT_BRUT"/></TD>
							<TD><INPUT type="text" name="montantBrutExterne" value="<%=viewBean.getMontantBrutExterne()%>" class="montant disabled" readonly></TD>
						</TR>
						<TR><TD colspan="6"></TD></TR>
						<TR>
							<TD><ct:FWLabel key="JSP_PRE_D_MONTANT_TOTAL"/></TD>
							<TD colspan="5"><INPUT type="text" name="montantBrut" value="<%=viewBean.getMontantBrut()%>" class="montant disabled" readonly></TD>
						</TR>
						<TR>
							<TD colspan="6">
								<input type="hidden" name="forNoBaseIndemnisation" value="<%=viewBean.getIdBaseIndemnisation()%>">
								&nbsp;
							</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_PRE_D_ETAT"/></TD>
							<TD colspan="5">
								<ct:select name="csEtat" defaultValue="<%=viewBean.getCsEtat()%>">
									<ct:optionsCodesSystems csFamille="IJETATPRES">
									<%if (!globaz.ij.api.prestations.IIJPrestation.CS_MIS_EN_LOT.equals(viewBean.getCsEtat())){%>
										<ct:excludeCode code="<%=globaz.ij.api.prestations.IIJPrestation.CS_MIS_EN_LOT%>"/>
									<%}%>
									<%if (!globaz.ij.api.prestations.IIJPrestation.CS_DEFINITIF.equals(viewBean.getCsEtat())){%>
										<ct:excludeCode code="<%=globaz.ij.api.prestations.IIJPrestation.CS_DEFINITIF%>"/>
									<%}%>
									</ct:optionsCodesSystems>
								</ct:select>
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