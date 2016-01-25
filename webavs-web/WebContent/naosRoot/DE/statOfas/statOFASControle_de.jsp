<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran="CDS2001";%>
<%@ page import="globaz.globall.db.*" %>
<%

	globaz.naos.db.statOfas.AFStatOFASControleViewBean viewBean = (globaz.naos.db.statOfas.AFStatOFASControleViewBean)session.getAttribute("viewBean");                            
    userActionValue="naos.statOfas.statOFASControle.executer";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<SCRIPT>
function init(){}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Ausdruck der BSV Statistiken<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						
						<TR> 
  				          	<TD>E-Mail Adresse</TD>
 				          	<TD> 
								<INPUT name="email" size="40" type="text" style="text-align : left;" value="<%=viewBean.getEmail()!=null?viewBean.getEmail():""%>">                        
						  	<TD>&nbsp;</TD>
          				</TR>
          				<TR>
          					<TD>&nbsp;</TD>
                    	</TR>
          				<TR> 
  				          	<TD>Jahr</TD>
 				          	<TD> 
								<INPUT name="annee" size="15" type="text" style="text-align : left;" value="<%=viewBean.getAnnee()!=null?viewBean.getAnnee():""%>">                        
						  	<TD>&nbsp;</TD>
          				</TR>       				
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<ct:menuChange displayId="menu" menuId="AFMenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="AFMenuVide"/>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>