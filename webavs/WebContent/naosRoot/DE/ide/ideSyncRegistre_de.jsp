<%-- tpl:insert page="/theme/process.jtpl" --%><%@page import="globaz.naos.db.ide.AFIdeSyncRegistreViewBean"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran = "CAF3022";
AFIdeSyncRegistreViewBean viewBean = (AFIdeSyncRegistreViewBean) session.getAttribute("viewBean");
showProcessButton = !processLaunched && viewBean.getSession().hasRight("naos.ide.ideSyncRegistre.executer",FWSecureConstants.UPDATE);
userActionValue = "naos.ide.ideSyncRegistre.executer";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="NAOS_JSP_IDE_SYNCHRO_REGISTRE_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						
						<TR> 
  				          	<TD><ct:FWLabel key="NAOS_JSP_IDE_SYNCHRO_REGISTRE_MAIL"/></TD>
 				          	<TD> 
								<INPUT name="email" size="40" type="text" style="text-align : left;" value="<%=viewBean.getEmail()!=null?viewBean.getEmail():""%>">                        
						  	</TD>
          				</TR>
          				<%-- <TR> 
  				          	<TD><ct:FWLabel key="NAOS_JSP_IDE_SYNCHRO_REGISTRE_FORCER_ALL_STATUS"/></TD>
 				          	<TD> 
								<INPUT type="checkbox" id="modeFocreAllStatus" name="modeForceAllStatus" >                        
						  	</TD>
          				</TR> --%>
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>