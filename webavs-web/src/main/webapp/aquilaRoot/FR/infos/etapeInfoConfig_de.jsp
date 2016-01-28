<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.aquila.vb.infos.COEtapeInfoConfigViewBean" %>
<%

	idEcran="GCO0019";

	COEtapeInfoConfigViewBean viewBean = (COEtapeInfoConfigViewBean)session.getAttribute("viewBean");
	
	bButtonCancel = false;
	bButtonUpdate = bButtonUpdate && viewBean.isNew();
	bButtonDelete = bButtonDelete && COEtapeInfoConfigViewBean.CS_PERSONNALISE.equals(viewBean.getCsLibelle());
	
	selectedIdValue = viewBean.getIdEtapeInfoConfig();
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CO-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="CO-OptionsDefaut" showTab="menu"/>

<script language="JavaScript">
	
	function add() {
	    document.forms[0].elements('userAction').value="aquila.infos.etapeInfoConfig.ajouter";
	}
	
	function upd() {}
	
	function validate() {
	    state = validateFields();
	    if (document.forms[0].elements('_method').value == "add")
	        document.forms[0].elements('userAction').value="aquila.infos.etapeInfoConfig.ajouter";
	    else
	        document.forms[0].elements('userAction').value="aquila.infos.etapeInfoConfig.modifier";
	    
	    return state;
	
	}
	
	function cancel() {
	
	if (document.forms[0].elements('_method').value == "add")
	  document.forms[0].elements('userAction').value="aquila.infos.etapeInfoConfig.chercher";
	 else
	  document.forms[0].elements('userAction').value="aquila.infos.etapeInfoConfig.chercher";
	  
	}
	
	function del() {
	    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
	        document.forms[0].elements('userAction').value="aquila.infos.etapeInfoConfig.supprimer";
	        document.forms[0].submit();
	    }
	}
	
	function init(){}

</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Détail d'une configuration d'information par étape<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<% if (viewBean.isNew()) { %>
						<TR>
							<TD>Séquence</TD>
							<TD>
								<ct:select name="libSequence">
									<ct:optionsCodesSystems csFamille="COSEQP"/>
								</ct:select>
							</TD>
							<TD>Etape</TD>
							<TD colspan="3">
								<ct:select name="libEtape">
									<ct:optionsCodesSystems csFamille="COETAPP"/>
								</ct:select>
							</TD>
						</TR>
						<TR>
							<TD>Libellé</TD>
							<TD colspan="3">
								<INPUT type="hidden" name="csLibelle" value="<%=COEtapeInfoConfigViewBean.CS_PERSONNALISE%>">
								<INPUT type="text" name="description" style="width: 100%">
							</TD>
						</TR>
						<TR>
							<TD>Type</TD>
							<TD>
								<ct:select name="csType">
									<ct:optionsCodesSystems csFamille="COTYPETINF"/>
								</ct:select>
							</TD>
							<TD>Ordre</TD>
							<TD><INPUT type="text" name="ordre" class="numeroCourt"></TD>
						</TR>
						<TR>
							<TD>Requis</TD>
							<TD colspan="3"><INPUT type="checkbox" name="requis" value="on"></TD>
						</TR>
						<TR>
							<TD>Remplace date d'exécution</TD>
							<TD colspan="3"><INPUT type="checkbox" name="remplaceDateExecution" value="on"></TD>
						</TR>
						<TR>
							<TD>Automatique</TD>
							<TD colspan="3"><INPUT type="checkbox" name="automatique" value="on"></TD>
						</TR>
						<% } else { %>
						<TR>
							<TD>Séquence</TD>
							<TD><INPUT type="text" name="" value="<%=viewBean.getSequence().getLibSequenceLibelle()%>" class="disabled" style="width:100%" readonly></TD>
							<TD>Etape</TD>
							<TD><INPUT type="text" name="" value="<%=viewBean.getEtape().getLibEtapeLibelle()%>" class="disabled" style="width:100%" readonly></TD>
						</TR>
						<TR>
							<TD>Libellé</TD>
							<TD colspan="3"><INPUT type="text" name="" value="<%=viewBean.getLibelle()%>" class="disabled" style="width:100%" readonly></TD>
						</TR>
						<TR>
							<TD>Type</TD>
							<TD><INPUT type="text" name="" value="<%=viewBean.getLibelleType()%>" class="disabled" style="width:100%" readonly></TD>
							<TD>Ordre</TD>
							<TD><INPUT type="text" name="" value="<%=viewBean.getOrdre()%>" class="numeroCourt disabled" readonly></TD>
						</TR>
						<TR>
							<TD>Requis</TD>
							<TD colspan="3"><INPUT type="checkbox" name="requis" value="true" <%=viewBean.getRequis().booleanValue()?"checked":""%> disabled></TD>
						</TR>
						<TR>
							<TD>Remplace date d'exécution</TD>
							<TD colspan="3"><INPUT type="checkbox" name="remplaceDateExecution" value="true" <%=viewBean.getRemplaceDateExecution().booleanValue()?"checked":""%> disabled></TD>
						</TR>
						<TR>
							<TD>Automatique</TD>
							<TD colspan="3"><INPUT type="checkbox" name="automatique" value="true" <%=viewBean.getAutomatique().booleanValue()?"checked":""%> disabled></TD>
						</TR>
						<% } %>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>