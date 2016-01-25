<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran = "CGE3003";
%>
<%
GEProcessComptabilisationCIViewBean viewBean = (GEProcessComptabilisationCIViewBean) session.getAttribute("viewBean");
userActionValue = "campus.process.processComptabilisationCI.executer";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.campus.vb.process.GEProcessComptabilisationCIViewBean"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<ct:menuChange displayId="menu" menuId="GEMenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="GEMenuVide"/>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

top.document.title = "Inscription aux CIs - " + top.location.href;

// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Inscription aux CIs<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
	  	<TR>
	  	<TR>
	  		<%if (JadeStringUtil.isBlankOrZero(request.getParameter("idLot"))) {%>
		  	<TR height="50">
		  		<TD nowrap colspan="2" style="font-weight : bolder;">Toutes les annonces / imputations validées seront mises aux CI's quel que soit le lot.</TD>
		  	 </TR>
		  	<% } else { %>
		  	<TR>
		  		<TD>N° du lot</TD>
          		<TD><input  name="idLot" class="numeroCourtDisabled" value="<%=request.getParameter("idLot")%>"</TD>
          	</TR>
          	<% } %>
         <TR>
         <TR>
			<TD>Libellé du journal CI</TD>
			<TD align="left">
				<input type="text" name="libelle" class="libelleLong" value="<%=viewBean.getLibelle()%>"/>
			</TD>
		</TR>	 
		<TR>
			<TD>E-mail</TD>
			<TD align="left">
				<input type="text" name="eMailAddress" class="libelleLong" value="<%=viewBean.getEMailAddress()%>"/>
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