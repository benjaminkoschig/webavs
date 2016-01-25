<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/WEB-INF/naos.tld" prefix="naos" %>
<%
	globaz.naos.process.AFCalculRetroactifProcess viewBean = (globaz.naos.process.AFCalculRetroactifProcess) session.getAttribute("viewBean");
	userActionValue = "naos.calculRetroactif.calculRetroactif.executer";
	idEcran = "CAF3011";
	
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- --%>


<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%-- --%>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Calcul rétroactif<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<naos:AFInfoAffiliation affiliation="<%=viewBean.getAffiliation()%>"/>
						<TR>
							<TD width="150" height="40">Date du calcul (mois.année)</TD>
							<TD align="left"><ct:FWCalendarTag name="dateCalcul" displayType ="month" value="<%=viewBean.getDateCalcul()%>"/></TD>
						</TR>
						<TR>
							<TD>&nbsp;</TD>
						</TR>
						<TR>
							<TD>
								<TABLE border="0" cellspacing="0" cellpadding="0" id="gestionMontantPositif">
								<TBODY>
								<TR>
									<TD>Gestion des montants positifs</TD>
								</TR>
								<TR>
									<TD>
										<INPUT type="radio" name="compenserMontantPositif" value="false" <%=!viewBean.isCompenserMontantPositif()?"checked='checked'":""%> />
										Générer un relevé
									</TD>		
										
								</TR>						
								<TR>
									<TD>
										<INPUT type="radio" name="compenserMontantPositif" value="true" <%=viewBean.isCompenserMontantPositif()?"checked='checked'":""%>/>
										Compenser lors des prochaines facturactions
									</TD>		
										
								</TR>						
								</TBODY> 
								</TABLE>
							</TD>
							<TD>
						
								<TABLE border="0" cellspacing="0" cellpadding="0" id="gestionMontantNegatif">
								<TBODY>
								<TR>
									<TD>Gestion des montants négatifs</TD>
								</TR>
								<TR>
									<TD>
										<INPUT type="radio" name="compenserMontantNegatif" value="false" <%=!viewBean.isCompenserMontantNegatif()?"checked='checked'":""%>/>
										Générer un relevé
									</TD>		
										
								</TR>						
								<TR>
									<TD>
										<INPUT type="radio" name="compenserMontantNegatif" value="true" <%=viewBean.isCompenserMontantNegatif()?"checked='checked'":""%>/>
										Compenser lors des prochaines facturactions
									</TD>		
										
								</TR>						
								</TBODY> 
								</TABLE>
							</TD>
						</TR>
						<TR>
							<TD>&nbsp;</TD>
						</TR>
						<TR>
							<TD  width="150" height="40">Adresse Email</TD>
							<TD align="left"><INPUT type="text"  name="eMailAddress" size="70" value="<%=viewBean.getEMailAddress()%>"/></TD>
						</TR>						
						<TR>
							<TD><INPUT type="hidden" name="idAffiliation" value="<%=viewBean.getIdAffiliation()%>"/></TD>
							<TD>&nbsp;</TD>
						</TR>
						
						
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>