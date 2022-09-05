<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="PRE3047";
	
	//Les labels de cette page commence par la préfix "JSP_EAN_D"

	userActionValue="corvus.process.comparaisonCentrale.executer";
	
	REComparaisonCentraleViewBean viewBean = (REComparaisonCentraleViewBean)(session.getAttribute("viewBean"));
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	
	String[] lots = viewBean.getLotHermesList();
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@page import="globaz.corvus.utils.REPmtMensuel"%>
<%@page import="globaz.globall.util.JACalendar"%>
<%@page import="globaz.prestation.tools.PRDateFormater"%>
<%@page import="globaz.corvus.vb.process.REComparaisonCentraleViewBean"%>
<%@ page import="globaz.corvus.api.arc.downloader.REAnnoncesDateAugmentation5153" %>
<SCRIPT>

    
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>		
<ct:FWLabel key="JSP_COMPARAISON_CENTRALE_TITLE"/> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR><TD align="left">
								<TABLE width="60%">
								<TR>
								<TD><ct:FWLabel key="JSP_ADRESSE_EMAIL"/>&nbsp;
								</TD>
								<TD><INPUT type="text" name="eMailAddress" value="<%=globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getEMailAddress())?controller.getSession().getUserEMail():viewBean.getEMailAddress()%>" class="libelleLong"></TD>
								<TD>&nbsp;</TD>
								</TR>
								<TR>
									<TD><LABEL for="labelMoisAnnee"><ct:FWLabel key="JSP_CONCORDANCE_CENDATE_DATE"/></LABEL></TD>
									<TD>
										<input	id="moisAnnee"
												name="moisAnnee"¨
												data-g-calendar="type:month"
												value="<%=REPmtMensuel.getDateDernierPmt(viewBean.getSession())%>" />
									</TD>
									<TD>&nbsp;</TD>								
								</TR>
								<TR>
									<TD><ct:FWLabel key="JSP_DATE_IMPORTATION"/></TD>
									<TD>
										<ct:select name="dateImportation" id="dateImportation"  wantBlank="false">
											<%for(REAnnoncesDateAugmentation5153 annonce:viewBean.loadListDateImportation()){%>
											<ct:option id="<%=annonce.getValue()%>" value="<%=annonce.getValue()%>" label="<%=annonce.getLabel()%>"/>
											<%} %>
										</ct:select>
									</TD>
								</TR>
								</TBODY>							
								</TABLE>
						</TD></TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>