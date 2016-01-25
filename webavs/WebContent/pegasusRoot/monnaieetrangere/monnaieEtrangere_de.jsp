<%-- Page de detail des monnaies etrangeres --%>
<%-- Creator: SCE, 6.10 --%>

<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
// Les labels de cette page commence par la préfix "JSP_PC_PARAM_MONETR_D"
idEcran="PPC0101";
PCMonnaieEtrangereViewBean viewBean = (PCMonnaieEtrangereViewBean) session.getAttribute("viewBean");
boolean viewBeanIsNew="add".equals(request.getParameter("_method"));

bButtonValidate = true; //bButtonValidate &&  controller.getSession().hasRight(IPCActions.ACTION_PARAMETRES_VARIABLES_METIER, FWSecureConstants.UPDATE);

autoShowErrorPopup = true;
bButtonCancel = true;
selectedIdValue = viewBean.getId();


%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%@ include file="/pegasusRoot/ajax/javascriptsAndCSS.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>

<%@page import="globaz.pegasus.vb.monnaieetrangere.PCMonnaieEtrangereViewBean"%>


<script language="JavaScript">

  function add() {
    document.forms[0].elements('userAction').value="pegasus.monnaieetrangere.monnaieEtrangere.ajouter";
  }
  
  function upd() {}

  function validate() {
    state = true;
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="pegasus.monnaieetrangere.monnaieEtrangere.ajouter";
    else
        document.forms[0].elements('userAction').value="pegasus.monnaieetrangere.monnaieEtrangere.modifier";
    return state;
  }

  function cancel() {
    if (document.forms[0].elements('_method').value == "add")
      document.forms[0].elements('userAction').value="back";
    else
      document.forms[0].elements('userAction').value="pegasus.monnaieetrangere.monnaieEtrangere.afficher";
  }

  function del() {
    if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
        document.forms[0].elements('userAction').value="pegasus.monnaieetrangere.monnaieEtrangere.supprimer";
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
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_PC_PARAM_MONETR_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><LABEL for="monnaieEtrangere.simpleMonnaieEtrangere.csTypeMonnaie"><ct:FWLabel key="JSP_PC_PARAM_MONETR_D_MONNAIE"/></LABEL></TD>
							<TD>
								<ct:select name="monnaieEtrangere.simpleMonnaieEtrangere.csTypeMonnaie" wantBlank="true" defaultValue="<%=viewBean.getMonnaieEtrangere().getSimpleMonnaieEtrangere().getCsTypeMonnaie()%>" notation="data-g-select='mandatory:true'">
									<ct:optionsCodesSystems csFamille="PYMONNAIE"/>
								</ct:select>
							</TD>							
						</TR>
						<TR>
							<TD><LABEL for="monnaieEtrangere.simpleMonnaieEtrangere.dateDebut"><ct:FWLabel key="JSP_PC_PARAM_MONETR_D_DATE_DEBUT"/></LABEL></TD>
							<TD>
								<input type="text" name="monnaieEtrangere.simpleMonnaieEtrangere.dateDebut" value="<%=JadeStringUtil.toNotNullString(viewBean.getMonnaieEtrangere().getSimpleMonnaieEtrangere().getDateDebut())%>" data-g-calendar="mandatory:true,type:month"/>
							</TD>

							<TD><LABEL for="monnaieEtrangere.simpleMonnaieEtrangere.dateFin"><ct:FWLabel key="JSP_PC_PARAM_MONETR_D_DATE_FIN"/></LABEL></TD>
							<TD>
								<input type="text" name="monnaieEtrangere.simpleMonnaieEtrangere.dateFin" value="<%=JadeStringUtil.toNotNullString(viewBean.getMonnaieEtrangere().getSimpleMonnaieEtrangere().getDateFin())%>" data-g-calendar="mandatory:false,type:month"/>
							</TD>
						</TR>
						<TR>
							<TD><LABEL for="monnaieEtrangere.simpleMonnaieEtrangere.taux"><ct:FWLabel key="JSP_PC_PARAM_MONETR_D_TAUX"/></LABEL></TD>
							<TD>
								<INPUT type="text" name="monnaieEtrangere.simpleMonnaieEtrangere.taux" value="<%=JadeStringUtil.toNotNullString(viewBean.getMonnaieEtrangere().getSimpleMonnaieEtrangere().getTaux())%>" data-g-rate="mandatory:true" />
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