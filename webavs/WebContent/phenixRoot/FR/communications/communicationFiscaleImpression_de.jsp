<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%                                  
    	globaz.phenix.db.communications.CPCommunicationFiscaleImpressionViewBean viewBean = (globaz.phenix.db.communications.CPCommunicationFiscaleImpressionViewBean)session.getAttribute ("viewBean");
		userActionValue = "phenix.communications.communicationFiscaleImpression.executer";
		idEcran="CCP1003";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CP-MenuPrincipal" showTab="menu"/>
<SCRIPT language="JavaScript">
top.document.title = "Communications fiscales - Impression"
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Impression d'une communication fiscale<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD>Prénom, Nom &nbsp;</TD>
							<TD><INPUT type="text" name="nomPrenom" class="libelleLongDisabled"
								readonly value="<%=viewBean.getNomPrenom()%>"></TD>
							<TD></TD>
							<TD></TD>	
						</TR>
		  				<TR>
		  					<TD><input type="hidden" name="idAffiliation" class='Disabled' readonly
								value='<%=viewBean.getIdAffiliation()%>'></TD>
						</TR>
						<TR>
							<TD>&nbsp;</TD>
						</TR>
          				<TR>
          					<TD><HR width=100%/></TD>
          					<TD><HR width=100%/></TD>
          					<TD><HR width=100%/></TD>
          					<TD><HR width=100%/></TD>
          				</TR>
          				<TR>
          					<TD>&nbsp;</TD>
          				</TR>
						<TR>
            				<TD width="279" height="20">Date d'édition</TD>
							<TD><ct:FWCalendarTag name="dateEdition" 
            					value='<%=globaz.globall.util.JACalendar.todayJJsMMsAAAA()%>'
								doClientValidation="CALENDAR"/></TD>
						</TR>		
						<TR>
            				<TD><INPUT name="idCommunication" type="hidden"
            					value="<%=viewBean.getIdCommunication()%>">
            				</TD>
            			</TR>
						<TR>
							<TD height="20">Adresse E-Mail</TD>
            				<TD><input name='eMailAddress' class='libelleLong' data-g-string="mandatory:true" value='<%=viewBean.getSession().getUserEMail()%>'></TD>
						</TR>               
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>