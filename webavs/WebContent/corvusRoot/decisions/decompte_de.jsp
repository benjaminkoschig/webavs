<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@page import="globaz.corvus.api.decisions.IREDecision"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/corvusRoot/corvustaglib.tld" prefix="re" %>
<%
	// Les labels de cette page commence par la préfix "JSP_DECOMPTE_D"
	idEcran="PRE0028";
	globaz.corvus.vb.decisions.REDecompteViewBean viewBean = (globaz.corvus.vb.decisions.REDecompteViewBean) session.getAttribute("viewBean");
	bButtonUpdate = false;
	bButtonDelete = false;	
	String menuOptionToLoad = request.getParameter("menuOptionToLoad");

%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zonescripts" --%>


<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%if(JadeStringUtil.isNull(menuOptionToLoad) || JadeStringUtil.isEmpty(menuOptionToLoad)){%>
	
<%@page import="globaz.corvus.utils.REPmtMensuel"%><ct:menuChange displayId="menu" menuId="corvus-menuprincipal"  showTab="menu"/>
	<ct:menuChange displayId="options" menuId="corvus-optionsempty" showTab="menu">
	</ct:menuChange>
<%}else if("decision".equals(menuOptionToLoad)){%>
	<ct:menuChange displayId="options" menuId="corvus-optionsdecisions" showTab="options">
		<ct:menuSetAllParams key="selectedId" checkAdd="no" value="<%=viewBean.getIdDecision()%>"/>
		<ct:menuSetAllParams key="idDecision" checkAdd="no" value="<%=viewBean.getIdDecision()%>"/>
		<ct:menuSetAllParams key="noDemandeRente" checkAdd="no" value="<%=viewBean.getIdDemandeRente()%>"/>
		<ct:menuSetAllParams key="idTierRequerant" checkAdd="no" value="<%=viewBean.getIdTierRequerant()%>"/>
		<ct:menuSetAllParams key="idTierBeneficiaire" checkAdd="no" value="<%=viewBean.getIdTierBeneficiaire()%>"/>
		<ct:menuSetAllParams key="provenance" checkAdd="no" value="<%=globaz.corvus.vb.prestations.REPrestationsJointDemandeRenteViewBean.FROM_ECRAN_DECISIONS%>"/>
		<ct:menuSetAllParams key="idPrestation" checkAdd="no" value="<%=viewBean.getIdPrestation()%>"/>
		<ct:menuSetAllParams key="montantPrestation" checkAdd="no" value="<%=viewBean.getMontantPrestation()%>"/>
<%
		if (JadeStringUtil.isBlankOrZero(viewBean.getIdLot())) {
%>		<ct:menuActivateNode active="no" nodeId="afficherLot" />
<%
		} else {
%>		<ct:menuActivateNode active="yes" nodeId="afficherLot" />
<%
		}
		if (IREDecision.CS_ETAT_ATTENTE.equals(viewBean.getCsEtatDecision())) {
%> 		<ct:menuActivateNode active="no" nodeId="imprimerDecision" />
<%
		} else {
%> 		<ct:menuActivateNode active="yes" nodeId="imprimerDecision" />
<%
		}
		if (REPmtMensuel.isValidationDecisionAuthorise(objSession)) {
%> 		<ct:menuActivateNode active="yes" nodeId="validerdecision" />
<%
		} else {
%>		<ct:menuActivateNode active="no" nodeId="validerdecision" />
<%
		}
%>	</ct:menuChange>
<%
	}
%>	<script language="javascript">

	function add(){
	}

	function upd(){
	}

	function validate() {
	}

	function cancel() {
	}

	function del() {
	}

	function init(){
	}

</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_DECOMPTE_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
							<br/>
							<tr>
								<td colspan="4">
									<ct:FWLabel key="JSP_DECOMPTE_D_NO_DEMANDE"/>
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<input type="text" value="<%=viewBean.getIdDemandeRente()%>" readonly class="disabled">
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<ct:FWLabel key="JSP_DECOMPTE_D_TYPE_DEC"/>
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<input type="text" value="<%=viewBean.getLibelleTypeDecision()%>" readonly class="disabled">
								</td>
							</tr>
							<tr><td colspan="2">&nbsp;</td></tr>
							<tr>
								<td colspan="2">
									<re:PRDisplayRequerantInfoTag
										session="<%=(globaz.globall.db.BSession)controller.getSession()%>"
										idTiers="<%=viewBean.getIdTierBeneficiaire()%>"
										style="<%=globaz.prestation.jsp.taglib.PRDisplayRequerantInfoTag.STYLE_CONDENSED_BEN%>"/>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<input type="hidden" name="menuOptionToLoad" value="<%=menuOptionToLoad%>">
									&nbsp;
								</td>
							</tr>
							<tr><td colspan="4">&nbsp;</td></tr>
							<tr>
								<td colspan="4"><b><u><ct:FWLabel key="JSP_DECOMPTE_D_DECOMPTE"/></u></b></td>
							</tr>
							<tr><td colspan="4">&nbsp;</td></tr>
							<tr>
								<td colspan="4">
									<table width="100%">
										<tr>
											<td>
												<%=viewBean.getDecompteHTML()%>
											</td>
										</tr>
									</table>
								</td>
							</tr>

						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>