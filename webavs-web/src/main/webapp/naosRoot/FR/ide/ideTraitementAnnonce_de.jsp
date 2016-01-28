<%-- tpl:insert page="/theme/process.jtpl" --%><%@page import="globaz.naos.db.ide.AFIdeTraitementAnnonceViewBean"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran = "CAF3021";
AFIdeTraitementAnnonceViewBean viewBean = (AFIdeTraitementAnnonceViewBean) session.getAttribute("viewBean");
showProcessButton = !processLaunched && viewBean.getSession().hasRight("naos.ide.ideTraitementAnnonce.executer",FWSecureConstants.UPDATE);
userActionValue = "naos.ide.ideTraitementAnnonce.executer";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="NAOS_JSP_IDE_TRAITEMENT_ANNONCE_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						
						<TR> 
  				          	<TD><ct:FWLabel key="NAOS_JSP_IDE_TRAITEMENT_ANNONCE_TYPE_TRAITEMENT"/></TD>
 				          	<TD> 
								<ct:FWCodeSelectTag name="forTypeTraitement" defaut="" codeType="VEIDECATAN" wantBlank="true"  />   
							</TD>                    
          				</TR>
          				
          				<TR>
          					<TD>&nbsp;</TD>
          					<TD>&nbsp;</TD>
                    	</TR>
						
						<TR> 
  				          	<TD><ct:FWLabel key="NAOS_JSP_IDE_TRAITEMENT_ANNONCE_MAIL"/></TD>
 				          	<TD> 
								<INPUT name="email" size="40" type="text" style="text-align : left;" value="<%=viewBean.getEmail()!=null?viewBean.getEmail():""%>">                        
						  	</TD>
          				</TR>
          				
          				<!-- 
          				<TR> 
  				          	<TD><ct:FWLabel key="NAOS_JSP_IDE_TRAITEMENT_ANNONCE_TEST_SEDEX"/></TD>
 				          	<TD> 
								<INPUT type="checkbox" id="modeTestSedex" name="modeTestSedex">                        
						  	</TD>
          				</TR>
          				 -->
          				
          				
            
          				
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>