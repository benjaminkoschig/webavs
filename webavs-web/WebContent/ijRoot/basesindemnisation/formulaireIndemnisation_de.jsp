<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%--
INFO: les labels de cette page sont préfixés avec 'JSP_FIN_D'
--%>
<%
	idEcran = "PIJ0029";
	globaz.ij.vb.basesindemnisation.IJFormulaireIndemnisationViewBean viewBean = (globaz.ij.vb.basesindemnisation.IJFormulaireIndemnisationViewBean) session.getAttribute("viewBean");
	selectedIdValue = viewBean.getIdFormulaireIndemnisation();	
	bButtonCancel = false;
	
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="ij-menuprincipal"/>
<ct:menuChange displayId="options" menuId="ij-formulaires" showTab="options">
	<ct:menuSetAllParams key="idFormulaire" value="<%=viewBean.getIdFormulaireIndemnisation()%>"/>
	<ct:menuSetAllParams key="idPrononce" value="<%=viewBean.getIdPrononce()%>"/>
	<ct:menuSetAllParams key="csTypeIJ" value="<%=viewBean.getCsTypeIJ()%>"/>
</ct:menuChange>

<SCRIPT language="javascript"> 

	function add() {
	    document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_FORMULAIRE_INDEMNISATION%>.ajouter";
	}
	
	function upd() {}
	
	function validate() {
	    state = validateFields();
	    if (document.forms[0].elements('_method').value == "add")
	        document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_FORMULAIRE_INDEMNISATION%>.ajouter";
	    else
	        document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_FORMULAIRE_INDEMNISATION%>.modifier";
	    
	    return state;
	
	}
	
	function cancel() {
	  document.forms[0].elements('userAction').value="back";
	}
	
	function del() {
		if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
	        document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_FORMULAIRE_INDEMNISATION%>.supprimer";
	        document.forms[0].submit();
	    }
	}

	function init(){}
	
	function mettreAjourEtat(date){
		if (date.value.length>0){
			document.getElementsByName("etat")[0].value="<%=globaz.ij.api.basseindemnisation.IIJFormulaireIndemnisation.CS_RECU %>";
		}
	}
	
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_FIN_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD valign="top">
								<LABEL for="idPrononce"><ct:FWLabel key="JSP_FIN_D_PRONONCE"/></LABEL>
							</TD>
							<TD valign="top" colspan="5">
								<span><%=viewBean.getFullDescriptionPrononce()[0]%><br>
									  <%=viewBean.getDetailRequerant()%></span>
							</TD>							
						</TR>
						<TR><TD colspan="6">&nbsp;</TD></TR>
						<TR>
							<TD><LABEL for="noBaseIndemnisation"><ct:FWLabel key="JSP_FIN_D_NO_BASE_IND"/></LABEL></TD>
							<TD><INPUT type="text" name="noBaseIndemnistation" value="<%=viewBean.getIdIndemnisation()%>" class="numeroCourt disabled" disabled></TD>
							<TD><LABEL for="dateDebutBase"><ct:FWLabel key="JSP_FIN_D_DATE_DEBUT_BASE"/></LABEL></TD>
							<TD><INPUT type="text" name="dateDebutBase" value="<%=viewBean.getDateDebutBase()%>" class="date disabled" disabled></TD>
							<TD><LABEL for="dateFinBase"><ct:FWLabel key="JSP_FIN_D_DATE_FIN_BASE"/></LABEL></TD>
							<TD><INPUT type="text" name="dateFinBase" value="<%=viewBean.getDateFinBase()%>" class="date disabled" disabled></TD>
						</TR>
						<TR><td colspan="6"><HR></td></TR>
						<TR>
							<TD><LABEL for="idInstitutionResponsable"><ct:FWLabel key="JSP_FIN_D_NOM_AGENT"/></LABEL></TD>
							<TD><ct:FWListSelectTag data="<%=viewBean.getNomsAgents()%>" defaut="<%=viewBean.getIdInstitutionResponsable()%>" name="idInstitutionResponsable"/></TD>
							<TD><LABEL for="etat"><ct:FWLabel key="JSP_FIN_D_ETAT"/></LABEL></TD>
							<TD colspan="3">
								<ct:select name="etat" defaultValue="<%=viewBean.getEtat()%>">
									<ct:optionsCodesSystems csFamille="<%=globaz.ij.api.basseindemnisation.IIJFormulaireIndemnisation.CS_GROUPE_ETAT_FORMULAIRE%>"/>
								</ct:select>
							</TD>
						</TR>
						<TR>
							<TD><LABEL for="dateEnvoi"><ct:FWLabel key="JSP_FIN_D_DATE_ENVOI"/></LABEL></TD>
							<TD><ct:FWCalendarTag name="dateEnvoi" value="<%=viewBean.getDateEnvoi()%>"/></TD>
							<TD><LABEL for="dateReception"><ct:FWLabel key="JSP_FIN_D_DATE_RECEPTION"/></LABEL></TD>
							<TD colspan="3"><ct:FWCalendarTag name="dateReception" value="<%=viewBean.getDateReception()%>" doClientValidation="' onchange='mettreAjourEtat(this)"/></TD>
						</TR>
						<TR>
							<TD><LABEL for="nombreRappel"><ct:FWLabel key="JSP_FIN_D_NOMBRE_RAPPELS"/></LABEL></TD>
							<TD colspan="5">
								<INPUT type="text" name="nombreRappel" value="<%=viewBean.getNombreRappel()%>" class="numeroCourt" onchange="validateIntegerNumber(this);" onkeypress="return filterCharPositiveInteger(window.event);">
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