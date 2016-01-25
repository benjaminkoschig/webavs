<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran = "CGE";
%>
<%
GEProcessTraitementImpayesViewBean viewBean = (GEProcessTraitementImpayesViewBean) session.getAttribute("viewBean");
userActionValue = "campus.process.processTraitementImpayes.executer";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.campus.vb.process.GEProcessComptabilisationCIViewBean"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.campus.vb.process.GEProcessTraitementImpayesViewBean"%>
<ct:menuChange displayId="menu" menuId="GEMenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="GEMenuVide"/>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
function calculMontant(elem){
	
}

top.document.title = "Traitement impayés - " + top.location.href;

// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Traitement impayés<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
	  	<TR>
	  	<TR>
		  	<TR height="50">
		  		<TD nowrap colspan="2" style="font-weight : bolder;">Passation en irrécouvrable des impayés.</TD>
		  	 </TR>
         <TR> 
         <TR>
         	 <TD>Année</TD>
	         <TD nowrap>
	         	<INPUT name="annee" type="text" size="4" value="<%=viewBean.getAnnee()%>" maxlength="4" doClientValidation="NOT_EMPTY" class="numeroCourt" onchange="calculMontant(this);">
	         </TD>
         </TR>
         <TR>
         	 <TD>Montant</TD>
	         <TD nowrap>
	         	<INPUT name="montant" type="text" size="8" value="" doClientValidation="NOT_EMPTY" class="numeroLong">
	         	Si vide, prend le montant par défaut de l'année choisie !
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