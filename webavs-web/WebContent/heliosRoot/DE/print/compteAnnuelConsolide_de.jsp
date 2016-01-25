<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCF2013"; %>
<%
globaz.helios.db.comptes.CGExerciceComptableViewBean exerciceComptable = (globaz.helios.db.comptes.CGExerciceComptableViewBean )session.getAttribute(globaz.helios.db.interfaces.CGNeedExerciceComptable.SESSION_EXERCICECOMPTABLE);

globaz.helios.db.print.CGCompteAnnuelConsolideViewBean viewBean = (globaz.helios.db.print.CGCompteAnnuelConsolideViewBean) session.getAttribute("viewBean");
userActionValue = "helios.print.compteAnnuelConsolide.executer";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

top.document.title = "Prozess - Jährliche Konzernrechnung ausdrucken - " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Prozess - Jährliche Konzernrechnung  ausdrucken<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
			<TR> 
            	<TD align="left" width="180" height="21" valign="middle">Mandant</TD>
	            <TD align="left"> 
              		<INPUT type="text" name="libelleMandat" class="libelleLongDisabled" readonly value="<%=exerciceComptable.getMandat().getLibelle()%>"/>
        	   </TD>
          	</TR>
          				
		  	<TR> 
            	<TD align="left" width="180" height="21" valign="middle">E-Mail</TD>
            	<TD align="left"> 
              		<INPUT type="text" name="eMailAddress" class="libelleLong" value="<%=viewBean.getEMailAddress()%>"/>
              		<input type="hidden" name="idExerciceComptable" value="<%=exerciceComptable.getIdExerciceComptable()%>"/>
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