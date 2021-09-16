<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran="PAP0031";

globaz.apg.vb.prestation.APCotisationJointRepartitionViewBean viewBean = (globaz.apg.vb.prestation.APCotisationJointRepartitionViewBean) session.getAttribute("viewBean");
selectedIdValue = viewBean.getIdRepartitionBeneficiairePaiement();
bButtonCancel = false;
bButtonValidate = !viewBean.isDefinitif() && viewBean.getSession().hasRight(IAPActions.ACTION_COTISATION_JOINT_REPARTITION, FWSecureConstants.UPDATE);
// BZ7288
bButtonDelete= bButtonValidate;
bButtonUpdate= bButtonDelete;


%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<!--si APG -->
<%if ((String)globaz.prestation.tools.PRSessionDataContainerHelper.getData(session,globaz.prestation.tools.PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION)==globaz.prestation.api.IPRDemande.CS_TYPE_APG) {%>
	<%@page import="globaz.apg.servlet.IAPActions"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@ page import="globaz.apg.db.prestation.APCotisation" %>
<ct:menuChange displayId="menu" menuId="ap-menuprincipalapg" showTab="menu"/>
	<ct:menuChange displayId="options" menuId="ap-optionsempty"/>
<!--sinon, maternité -->
<%} else if ((String)globaz.prestation.tools.PRSessionDataContainerHelper.getData(session,globaz.prestation.tools.PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION)==globaz.prestation.api.IPRDemande.CS_TYPE_MATERNITE) {%>
	<ct:menuChange displayId="menu" menuId="ap-menuprincipalamat" showTab="menu"/>
	<ct:menuChange displayId="options" menuId="ap-optionsempty"/>
<%}%>

<script language="JavaScript">


  function add() {
    document.forms[0].elements('userAction').value="<%=globaz.apg.servlet.IAPActions.ACTION_COTISATION_JOINT_REPARTITION%>.ajouter"
  }
  function upd() {}

  function validate() {
    state = true
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="<%=globaz.apg.servlet.IAPActions.ACTION_COTISATION_JOINT_REPARTITION%>.ajouter";
    else
        document.forms[0].elements('userAction').value="<%=globaz.apg.servlet.IAPActions.ACTION_COTISATION_JOINT_REPARTITION%>.modifier";
    return state;
  }

  function cancel() {
    if (document.forms[0].elements('_method').value == "add")
      document.forms[0].elements('userAction').value="back";
    else
      document.forms[0].elements('userAction').value="<%=globaz.apg.servlet.IAPActions.ACTION_COTISATION_JOINT_REPARTITION%>.afficher";
  }

  function del() {
    if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
        document.forms[0].elements('userAction').value="<%=globaz.apg.servlet.IAPActions.ACTION_COTISATION_JOINT_REPARTITION%>.supprimer";
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
  }

  function deformatNumber(nombre) {
	  var truc = new String(nombre);
	  var result = truc.replace(/'/g, "");
	  return result;
  }

  function updateMontantSelonTaux() {
	  if (document.getElementById("taux").value != "" && document.getElementById("montant").value != "" && document.getElementById("montantBrut").value != "") {
		  <% if (APCotisation.TYPE_IMPOT.equals(viewBean.getType())) { %>
			  var taux = parseFloat(deformatNumber(document.getElementById('taux').value));
			  var montantOld = parseFloat(deformatNumber(document.getElementById('montant').value));
			  var montantBrut = parseFloat(deformatNumber(document.getElementById('montantBrut').value));
			  var montantNew = Math.round(((taux / 100) * montantBrut)*100)/100;
			  var isNegativ = parseFloat(montantOld) < 0;
			  document.getElementById('montant').value = (isNegativ ? -montantNew : montantNew).toFixed(2);
		  <% } %>
	  }
  }

</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_COTISATION"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><ct:FWLabel key="JSP_COTISATION"/></TD>
							<TD><INPUT type="text" name="" value="<%=viewBean.getNomExterne()%>" class="disabled" readonly></TD>
							<TD><ct:FWLabel key="JSP_PERIODE"/></TD>
							<TD>
								<ct:FWLabel key="JSP_DU"/>&nbsp;
								<INPUT type="text" name="" value="<%=viewBean.getDateDebut()%>" class="dateDisabled" readonly>&nbsp;
								<ct:FWLabel key="JSP_AU"/>&nbsp;
								<INPUT type="text" name="" value="<%=viewBean.getDateFin()%>" class="dateDisabled" readonly>&nbsp;
							</TD>
						</TR>
						<TR>
							<TD><LABEL for="taux"><ct:FWLabel key="JSP_TAUX_PC"/></LABEL></TD>
							<TD><INPUT id="taux" type="text" name="taux" value="<%=viewBean.getTaux()%>" onchange="updateMontantSelonTaux();" class="montant" onkeypress="return filterCharForFloat(window.event);"></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_MONTANT_BRUT"/></TD>
							<TD><INPUT id="montantBrut" type="text" name="" value="<%=viewBean.getMontantBrutCotisation()%>" class="montantDisabled"readonly></TD>
							<TD></TD>
							<TD></TD>
						</TR>
						<TR>
							<TD><LABEL for="montant"><ct:FWLabel key="JSP_COTISATION"/></LABEL></TD>
							<TD colspan="3"><INPUT id="montant" type="text" name="montant" value="<%=viewBean.getMontant()%>" class="montant" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);"></TD>
						</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>