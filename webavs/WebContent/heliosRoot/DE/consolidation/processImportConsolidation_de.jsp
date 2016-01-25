<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCF3012"; %>
<%
globaz.helios.db.comptes.CGExerciceComptableViewBean exerciceComptable = (globaz.helios.db.comptes.CGExerciceComptableViewBean )session.getAttribute(globaz.helios.db.interfaces.CGNeedExerciceComptable.SESSION_EXERCICECOMPTABLE);

globaz.helios.db.consolidation.CGProcessImportConsolidationViewBean viewBean = (globaz.helios.db.consolidation.CGProcessImportConsolidationViewBean) session.getAttribute("viewBean");

formAction= request.getContextPath()+mainServletPath+"Root/"+languePage+"/consolidation/processImportConsolidationLoadFile_de.jsp";
userActionValue = "helios.consolidation.processImportConsolidation.executer";
formEncType = "'multipart/form-data'";

globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
showProcessButton = showProcessButton && objSession.hasRight(userActionValue, globaz.framework.secure.FWSecureConstants.ADD);

viewBean.setIdExerciceComptable(exerciceComptable.getIdExerciceComptable());
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="options" menuId="CG-consolidation" showTab="options"/>

<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

top.document.title = "Prozess - Konsolidierung Import - " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Prozess - Konsolidierung Import<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
			<TR> 
            	<TD align="left" width="180" height="21" valign="middle">Mandant</TD>
	            <TD align="left"> 
              		<INPUT type="text" name="libelleMandat" class="libelleLongDisabled" readonly value="<%=exerciceComptable.getMandat().getLibelle()%>"/>
        	   </TD>
          	</TR>
          	
			<TR> 
            	<TD align="left" width="180" height="21" valign="middle">Kasse Nummer</TD>
	            <TD align="left"> 
              		<INPUT type="text" name="libelleMandat" class="libelleLongDisabled" readonly value="<%=viewBean.getNoCaisse()%>"/>
        	   </TD>
          	</TR>   
          	
			<TR> 
            	<TD align="left" width="180" height="21" valign="middle">Agentur Nummer</TD>
	            <TD align="left"> 
              		<INPUT type="text" name="libelleMandat" class="libelleLongDisabled" readonly value="<%=viewBean.getNoAgence()%>"/>
        	   </TD>
          	</TR>          	       	
						
		  	<TR> 
            	<TD align="left" width="180" height="21" valign="middle">E-Mail</TD>
            	<TD align="left"> 
              		<INPUT type="text" name="eMailAddress" class="libelleLong" value="<%=viewBean.getEMailAddress()%>"/>
              		<input type="hidden" name="idExerciceComptable" value="<%=exerciceComptable.getIdExerciceComptable()%>"/>
            	</TD>
          	</TR>	
          	
          	<TR> 
            <TD align="left" width="180" height="21" valign="middle">Pfad und XML-Dateiname</TD>
            <TD align="left"> 
				<input type="file" name="fileName" maxlength="256" class="libelleLong"/>
            </TD>
          	</TR>	 
          						
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> 
<SCRIPT>
document.forms[0].enctype = "multipart/form-data";
document.forms[0].method = "post";
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>