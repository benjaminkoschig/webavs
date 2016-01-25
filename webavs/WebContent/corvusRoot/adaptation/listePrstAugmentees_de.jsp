<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	// Les labels de cette page commence par la préfix "JSP_LAN_D"

	idEcran="PRE2053";
	
	REListePrstAugmenteesViewBean viewBean = (REListePrstAugmenteesViewBean)session.getAttribute("viewBean");
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	
	userActionValue=globaz.corvus.servlet.IREActions.ACTION_ADAPTATION_LISTE_PRST_AUGMENTEES + ".executer";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@page import="globaz.corvus.utils.REPmtMensuel"%>
<%@page import="globaz.corvus.vb.adaptation.REListePrstAugmenteesViewBean"%>

<ct:menuChange displayId="menu" menuId="corvus-menuprincipal"/>
<ct:menuChange displayId="options" menuId="corvus-optionsempty" showTab="menu">
</ct:menuChange>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_LIST_LST_PREST_AUG_TITRE_PAGE"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

		<TR>
			<TD colpsan="6" align="center">
				<table width="95%">
					<TR>
						<TD><ct:FWLabel key="JSP_CIRC_EMAIL"/></TD>
						<TD><INPUT type="text" name="eMailAddress" value="<%=globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getEMailAddress())?controller.getSession().getUserEMail():viewBean.getEMailAddress()%>" class="libelleLong"></TD>
					</TR>	
					<TR>
						<TD><ct:FWLabel key="JSP_CIRC_MOIS_ANN"/></TD>
						<TD>
							<input	id="moisAnnee"
									name="moisAnnee"
									data-g-calendar="type:month"
									value="<%=REPmtMensuel.getDateProchainPmt(viewBean.getSession())%>" />
						</TD>
					</TR>				
				</table>
			</TD>
		</TR>		
		<tr><td colspan="6"><br/></td></tr>
		<TR>
			<TD colpsan="6" align="center">
				<table width="95%">
	  				<tr>
						<td colspan="6" class="ongletLightBlue">
						    <table width="100%">							
								<tr>
									<td colspan="2"><b><ct:FWLabel key="JSP_LIST_LST_PREST_AUG_CHX_LST"/></b></td>
								</tr>
								<tr>
					 		</table>
					 	</td>
					 </tr>
					 <tr>
						<td colspan="6" class="areaGlobazBlue">
						    <table width="100%">							
								<tr>
									<td width="50%"><ct:FWLabel key="JSP_LIST_LST_PREST_AUG_CHX_LST_1"/></td>
									<td width="50%" colspan="3">
										<input type="checkbox" value="on" name="isLstPrestTraitementAutomatique" CHECKED/>
									</td>
								</tr>
								<tr>
									<td width="50%"><ct:FWLabel key="JSP_LIST_LST_PREST_AUG_CHX_LST_2"/></td>
									<td width="50%" colspan="3">
										<input type="checkbox" value="on" name="isLstPrestProgrammeCentrale" CHECKED/>
									</td>
								</tr>
								<tr>
									<td width="50%"><ct:FWLabel key="JSP_LIST_LST_PREST_AUG_CHX_LST_3"/></td>
									<td width="50%" colspan="3">
										<input type="checkbox" value="on" name="isLstPrestAugManuellement" CHECKED/>
									</td>
								</tr>
								<tr>
									<td width="50%"><ct:FWLabel key="JSP_LIST_LST_PREST_AUG_CHX_LST_5"/></td>
									<td width="50%" colspan="3">
										<input type="checkbox" value="on" name="isLstPrestNonAdapte" CHECKED/>
									</td>
								</tr>
								<tr>
									<td width="50%"><ct:FWLabel key="JSP_LIST_LST_PREST_AUG_CHX_LST_4"/></td>
									<td width="50%" colspan="3">
										<input type="checkbox" value="on" name="isLstRecapAdaptation" CHECKED/>
									</td>
								</tr>								
							</table>
						</td>
					</tr>
				</table>
			</TD>
		</TR>
		<tr><td colspan="6"><br/></td></tr>
																	
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>