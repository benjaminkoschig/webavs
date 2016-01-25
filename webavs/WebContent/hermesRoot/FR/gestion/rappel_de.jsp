
<!-- Sample JSP file -->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2 //EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@page import="globaz.commons.nss.NSUtil"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<% 
globaz.hermes.db.gestion.HERappelViewBean viewBean = (globaz.hermes.db.gestion.HERappelViewBean)session.getAttribute("viewBean");
userActionValue = "hermes.gestion.rappelImprimer.executer";
subTableWidth="50%";
idEcran="GAZ2001";
%>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> 
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="Javascript">
top.document.title="ARC - Detail d'une impression de rappel de CI";
</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Détail d'un envoi de rappel<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
					<%
globaz.hermes.db.gestion.HEOutputAnnonceViewBean annonceDepart = viewBean.getAnnonceDepart();

%>
					<TR>
						<TD nowrap align="left"><B>ARC :</B></TD>
						<TD nowrap align="left" width="211">
						<DIV align="left"><INPUT type="text" name="libelle"
							value="<%=annonceDepart.getTypeAnnonceLibelle()%>"
							class="disabled" readonly> &nbsp;</DIV>
						</TD>

					</TR>
					<TR>
						<TD nowrap align="left"><B>Numéro de la caisse :</B>&nbsp; &nbsp;</TD>
						<TD nowrap align="left"><INPUT type="text" name="forCaisse"
							value="<%=viewBean.getNumeroCaisse()%>" class="disabled" readonly>							
						</TD>

					</TR>
					<TR>
						<TD nowrap align="left"><B>Motif RCI :</B></TD>
						<TD nowrap align="left"><INPUT type="text" name="forMotif"
							value="<%=viewBean.getMotif()%>" class="disabled" readonly></TD>
	
					</TR>
					<TR>
						<TD nowrap align="left"><B>NSS :</B>&nbsp;</TD>
						<%--<TD nowrap align="left"><INPUT type="text" name="forNumAVS"
							value="<%=globaz.hermes.utils.AVSUtils.formatAVS8Or9(viewBean.getNumeroAvs())%>"
							class="disabled" readonly></TD>--%>
							
							<TD nowrap align="left"><INPUT type="text" name="forNumAVS"
							value="<%=(viewBean.getNumeroAvs().equals(""))?"&nbsp;": viewBean.getNumeroAvs()%>"
							class="disabled" readonly></TD>
	
					</TR>
					<TR>
						<TD nowrap align="left"><B>NSS bénéficiaire<br>(ayant droit ou partenaire) : </B>&nbsp;</TD>
						<TD nowrap align="left"><INPUT type="text" name="numAVSBeneficiaire"
							value="<%=JadeStringUtil.isEmpty(viewBean.getNumAVSBeneficiaire())?"":NSUtil.formatAVSUnknown(viewBean.getNumAVSBeneficiaire())%>"
							class="disabled" readonly></TD>
							
							
						<%--	<TD nowrap align="left"><INPUT type="text" name="numAVSBeneficiaire"
							value="<%=<TD nowrap align="left"><INPUT type="text" name="numAVSBeneficiaire"
							value="<%=globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getNumAVSBeneficiaire())?"":globaz.hermes.utils.AVSUtils.formatAVS8Or9(viewBean.getNumAVSBeneficiaire())%>"
							class="disabled" readonly></TD>%>"
							class="disabled" readonly></TD>--%>
							
	
					</TR>
					
					<TR>
						<TD nowrap align="left"><B>Date de l'ordre :</B>&nbsp;</TD>
						<TD nowrap align="left"><INPUT type="text" name="forDateOrdre"
							value="<%=annonceDepart.getDateAnnonceJMA()%>" class="disabled"
							readonly></TD>

					</TR>
					<TR>
						<TD nowrap align="left"><B>Adresse : </B></TD>
						<TD nowrap align="left"><TEXTAREA rows="6" name="adresse" cols="35" class="disabled" readonly><%=viewBean._getCaisseAdresseWithInactive()%></TEXTAREA></TD>	
					</TR>
					<% if(!viewBean.isInactif() && !viewBean.isAucuneCaisse()) {	%>
					<TR>
						<TD nowrap align="left"><b>Envoyer la lettre à : </b></TD>
						<TD nowrap align="left"><INPUT type="text" maxlength="40" size="40" style="width:8cm;" name="email" value="<%=viewBean.getSession().getUserEMail()==null?"":viewBean.getSession().getUserEMail()%>"></TD>
						
					</TR>
					<% } else { showProcessButton = false; %>
					<TR>
						<TD nowrap align="left">&nbsp;</TD>
						<TD nowrap align="left">&nbsp;</TD>
					</TR>							
					<TR>
						<TD nowrap align="left" colspan=2><font color="red">
							<% if(viewBean.isAucuneCaisse()) {	%>
								Aucune caisse de compensation correspond à ce numéro
							<% } else {%>
								La caisse de compensation est inactive
							<% } %>
								</font></TD>						
					</TR>					
					<TR>
						<TD nowrap align="left">&nbsp;</TD>
						<TD nowrap align="left">&nbsp;</TD>
					</TR>					
					
					<%}%>
					<input type="hidden" name="nom" value="<%=viewBean.getNom()%>">
					<input type="hidden" name="langueCaisse" value="<%=viewBean.getLangueCaisse()%>">
					<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<%	}%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>