<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran = "CGE3002";
%>
<%
GEProcessValidationAnnoncesViewBean viewBean = (GEProcessValidationAnnoncesViewBean) session.getAttribute("viewBean");
userActionValue = "campus.process.processValidationAnnonces.executer";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.campus.vb.process.GEProcessValidationAnnoncesViewBean"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<ct:menuChange displayId="menu" menuId="GEMenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="GEMenuVide"/>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

top.document.title = "Chargement lot - " + top.location.href;

// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Validation des annonces / imputations<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
		  <TR>
		  	<%if (JadeStringUtil.isBlankOrZero(request.getParameter("idLot"))) {%>
		  	<TR height="50">	
		  		<TD nowrap colspan="2" style="font-weight : bolder;">Toutes les annonces / imputations "à traiter" seront validées quel que soit le lot.</TD>
		  	 </TR>
		  	<% } else { %>
		  	<TR>
		  		<TD>N° du lot</TD>
          		<TD><input  name="idLot" class="numeroCourtDisabled" value="<%=request.getParameter("idLot")%>"</TD>
          	</TR>
          	<% } %>
          <TR>
          	<TD>Passage de facturation</TD>
            <TD>
            	<INPUT name="idPassageFacturation" class="numeroCourtDisabled" value="<%=viewBean.getIdPassageFacturation()!=null?viewBean.getIdPassageFacturation():""%>">
              	<%
				Object[] psgMethodsName = new Object[]{
				new String[]{"setIdPassageFacturation","getIdPassage"},
				new String[]{"setLibellePassageFacturation","getLibelle"},
				new String[]{"etatPassage","getStatus"}
				};
				Object[] psgParams= new Object[]{};
				String redirectUrl = ((String)request.getAttribute("mainServletPath")+"Root")+"/"
					+globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session)
					+"/process/processValidationAnnonces_de.jsp";
				%>
	            <ct:FWSelectorTag 
				name="passageSelector" 
				methods="<%=psgMethodsName%>"
				providerApplication ="musca"
				providerPrefix="FA"					
				providerAction ="musca.facturation.passage.chercher&forStatus=902001"	
				providerActionParams ="<%=psgParams%>"
				redirectUrl="<%=redirectUrl%>"			
				/>
				<input type="hidden" name="selectorName" value="">
			</TD>
		</TR>
		<TR>
	   		<TD></TD>
	   		<TD><input name="libellePassageFacturation" class="libelleLongDisabled" value="<%=viewBean.getLibellePassageFacturation()!=null?viewBean.getLibellePassageFacturation():""%>" readonly></TD>
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