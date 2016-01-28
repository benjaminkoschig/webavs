<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran = "CGE3001";
%>
<%
GEProcessChargementLotViewBean viewBean = (GEProcessChargementLotViewBean) session.getAttribute("viewBean");
userActionValue = "campus.process.processChargementLot.executer";
formAction= request.getContextPath()+mainServletPath+"Root/"+languePage+"/process/processChargementLotFile_de.jsp";
//Encryptage de la page
formEncType = "'multipart/form-data' method='post'";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.campus.process.chargementLot.GEProcessChargementLot"%>
<%@page import="globaz.campus.vb.process.GEProcessChargementLotViewBean"%>
<%@page import="globaz.campus.vb.etudiants.GEEtudiantsViewBean"%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

top.document.title = "Chargement lot - " + top.location.href;

// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Chargement d'un lot<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
		  <TR height="30">
		  	<TD>Libellé du lot</TD>
          	<TD><input  name="libelleLot" class="libelleLong" value=""></TD>
          </TR>
          <TR height="30">
		  	<TD>Année</TD>
          	<TD><input  name="annee" class="numeroCourt" return filterCharForInteger(window.event);"></TD>
          </TR>
          <TR height="30">
		  	<TD>Date de traitement</TD>
          	<TD><ct:FWCalendarTag name="dateTraitement" value="" doClientValidation="CALENDAR"/></TD>
          </TR>
          <TR height="30">
		  	<TD>Ecole</TD>
		  	<TD><ct:FWListSelectTag name="idTiersEcole" data="<%=GEEtudiantsViewBean.getIdsEtNomsEcole(session)%>" defaut=""/></TD>
		  </TR>
		  <TR height="30">
		  	<TD>Format du fichier</TD>
		  	<TD><ct:FWListSelectTag name="formatFichier" data="<%=GEProcessChargementLotViewBean.getIdEtNomFormatFichier()%>" defaut=""/></TD>
		  </TR>
		  <TR height="30">
		  	<TD>Fichier source</TD>
          	<TD><input  type="file" size="40" name="filename" maxlength="256"></TD>
          </TR>
		  <TR height="30">
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