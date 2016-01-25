<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%
 		DSAttestationFiscaleLtnGenViewBean viewBean = (DSAttestationFiscaleLtnGenViewBean)session.getAttribute("viewBean");
		userActionValue = "draco.declaration.attestationFiscaleLtnGen.executer";
		String emailAdresse = !globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getEmailAddress())?viewBean.getEmailAddress():"";
		idEcran = "CDS2003";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.draco.db.declaration.DSAttestationFiscaleLtnGenViewBean"%>
<SCRIPT language="JavaScript">
function changeEtat(i){
	if(i==0){
		document.getElementById('affilieTous').checked = false;
	}else{
		document.getElementById('fromAffilies').value = '';
		document.getElementById('untilAffilies').value = '';}
	}
</SCRIPT>


<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Impression des attestations fiscales LTN<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR> 
            				<TD>Tous les affiliés</TD>
            				<TD> 
              					<input name="affilieTous" size="20" type="checkbox" style="text-align : right;" checked onchange="changeEtat(1)">
              				</TD>
              			</TR>
              			<TR>
              				<TD>ou du num&eacute;ro</TD>
                        	<TD> 
              					<input name="fromAffilies" size="15" type="text" style="text-align : left;" value="" onchange="changeEtat(0)">
            				&nbsp;&nbsp;&nbsp;&nbsp;au num&eacute;ro&nbsp;&nbsp;&nbsp;&nbsp;
            					<INPUT name="untilAffilies" size="15" type="text" style="text-align : left;" value="" onchange="changeEtat(0)"></TD>
                        	<TD>&nbsp;</TD>
                    	</TR>
						<TR>
          					<TD>&nbsp;</TD>
                    	</TR>
						<TR> 
            				<TD>Année à traiter</TD>
            				<TD> 
              					<input name="annee" size="4" type="text" style="text-align : left;" value="">
              				</TD>
              			</TR>
						<TR>
							<TD>
								Date de document
							</TD>
							<TD>
								<ct:FWCalendarTag name="dateValeur" value="<%=viewBean.getDateValeur()%>" />
							</TD>
						</TR>
						<TR>
							<TD>
								Ré-impression pour la date de document
							</TD>
							<TD>
								<ct:FWCalendarTag name="dateImpression" value="" /> (Pour réimprimer des attestations déjà imprimées à une date antérieure en fonction de la date de document)
							</TD>
						</TR>
						<TR>
							<TD>
								E-Mail
							</TD>
							<TD>
								<input type="text" name="eMailAddress" value="<%=!globaz.jade.client.util.JadeStringUtil.isBlank(emailAdresse)?emailAdresse:""%>" size = "40">
							</TD>
						</TR>
						<TR>
							<TD>
								Simulation
							</TD>
							<TD>
								<input name="simulation" size="20" type="checkbox" checked style="text-align : right;">
							</TD>
						</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>