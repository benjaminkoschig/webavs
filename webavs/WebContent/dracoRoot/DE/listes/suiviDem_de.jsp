 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%
	idEcran="CDS2005";
%>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%@ page import="globaz.draco.db.listes.*"%>
<%
	//Récupération des beans
	DSSuiviDemViewBean viewBean = (DSSuiviDemViewBean) session.getAttribute ("viewBean");

	//Définition de l'action pour le bouton valider
	userActionValue = "draco.listes.suiviDem.executer";
%>

<SCRIPT language="JavaScript">
top.document.title = "Draco - Fortsetzung des Verarbeitungsprozess der Lohnbescheinigungen"
</SCRIPT>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> 
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/ValidationGroups.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

function init()
{}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Fortsetzung des Verarbeitungsprozess der Lohnbescheinigungen<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD>
								Jahr
							</TD>
							<TD>
								<input type="text" name="annee" size="10" maxlength="10" value="">
							</TD>
						</TR>
						<TR>
          					<TD>&nbsp;</TD>
                    	</TR>
						<TR>
							<TD>
								Lohnbescheinigungstyp
							</TD>
								<TD nowrap>
									<ct:FWCodeSelectTag 
		                				name="typeDeclaration" 
										defaut="<%=viewBean.getTypeDeclaration()%>"
										codeType="VEDECLARAT"
										wantBlank="true"/>&nbsp;&nbsp;
								</TD>
						</TR>
          				<TR>
          					<TD>&nbsp;</TD>
                    	</TR>
						<TR> 
  				          	<TD>E-Mail Adresse</TD>
 				          	<TD> 
								<INPUT name="email" size="40" type="text" style="text-align : left;" value="<%=viewBean.getSession().getUserEMail()%>">                        
						  	<TD>&nbsp;</TD>
          				</TR>
          				<TR>
          					<TD>&nbsp;</TD>
                    	</TR> 
          <%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>