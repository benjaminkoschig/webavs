<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ page import="globaz.naos.db.controleEmployeur.*,globaz.globall.db.*" %>
<%

	AFLettreProchainControleViewBean viewBean = (AFLettreProchainControleViewBean)session.getAttribute("viewBean");
    idEcran="CDS2001";                             
    userActionValue="naos.controleEmployeur.lettreProchainControle.executer";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.naos.db.controleEmployeur.AFLettreProchainControleViewBean"%>
<SCRIPT language="JavaScript">
function init(){}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Vordruck Lohnbescheinigung<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						
						<TR> 
  				          	<TD>E-Mail Adresse</TD>
 				          	<TD> 
								<INPUT name="eMailAddress" size="20" type="text" style="text-align : left;" value="<%=viewBean.getEMailAddress()!=null?viewBean.getEMailAddress():""%>">                        
						  	<TD>&nbsp;</TD>
          				</TR>
          				<TR>
          					<TD>&nbsp;</TD>
                    	</TR>
          				<TR>
            				<TD>Ausdruckmodelle</TD>
							<td>
								<ct:FWListSelectTag data="<%=viewBean.returnDocumentsPossible()%>" name="idDocument" defaut="<%=viewBean.getIdDocument()%>" />
							</TD>
                        	<TD>&nbsp;</TD>
                    	</TR>
                    	<TR> 
  				          	<TD>Revisor</TD>
 				          	<TD> 
								<INPUT name="reviseur" size="40" type="text" style="text-align : left;" value="<%=(!viewBean.getReviseur().equals(""))?viewBean.getReviseur():""%>">                        
						  	<TD>&nbsp;</TD>
          				</TR>
          				<TR> 
  				          	<TD>Datum und Zeit</TD>
 				          	<TD> 
								<INPUT name="dateHeure" size="40" type="text" style="text-align : left;" value="<%=viewBean.getDateHeure()!=null?viewBean.getDateHeure():""%>">                     
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