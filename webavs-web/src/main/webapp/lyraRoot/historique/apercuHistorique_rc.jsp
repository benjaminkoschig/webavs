<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="GLY0002";
 	globaz.lyra.vb.historique.LYApercuHistoriqueViewBean viewBean = (globaz.lyra.vb.historique.LYApercuHistoriqueViewBean) request.getAttribute("viewBean");
	bButtonNew = false;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
	bFind = true;
	usrAction = "<%=globaz.lyra.servlet.ILYActions.ACTION_HISTORIQUE%>.lister";
	bButtonNew = false;
	
	function supprimerHistorique() {
		if (window.confirm("Vous êtes sur le point de supprimer tout l'historique des échéances! Voulez-vous continuer?")){
			document.forms[0].elements('userAction').value = "<%=globaz.lyra.servlet.ILYActions.ACTION_HISTORIQUE%>.supprimerHistorique";
			document.forms[0].target = "fr_main";
			document.forms[0].submit();
		}
	}
	
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Historique des échéances<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD>
								<TABLE border="0">
									<TR>
										<TD><LABEL for="forEtat">Etat</LABEL></TD>
										<TD>&nbsp;</TD>
										<TD>
											<ct:select name="forEtat" wantBlank="true">
												<ct:optionsCodesSystems csFamille="<%=globaz.lyra.api.ILYEcheances.CS_ETAT_ECHEANCE%>"/>
											</ct:select>
										</TD>
										
										<TD>&nbsp;</TD>
										<TD>&nbsp;</TD>
										<TD>&nbsp;</TD>
										
										<TD><LABEL for="forDate">A partir de</LABEL></TD>
										<TD>&nbsp;</TD>
										<TD><ct:FWCalendarTag name="forDate" value=""/></TD>
									</TR>

								</TABLE>
							</TD>
						</TR>
						<TR>
							<TD colspan="6" align="right">
									<INPUT type="button" value="Vider historique" onclick="supprimerHistorique()">
							</TD>
						</TR>
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>