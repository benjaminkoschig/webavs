 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
globaz.hermes.db.gestion.HEImpressionciViewBean viewBean = (globaz.hermes.db.gestion.HEImpressionciViewBean)session.getAttribute("viewBean");
userActionValue = "hermes.gestion.extraitAnnonce.executer";
subTableWidth = "";
idEcran="GAZ2002";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> 
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="javascript"> 

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Ausdruck eines Nachtrags-IK<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
  <tr> 
    <td>Datum :&nbsp;</td>
    <td> 
      <input type="text" name="date" value="<%=viewBean.getDateAnnonce()%>" class="disabled" readonly>
    </td>
  </tr>
  <tr> 
    <td>SVN :&nbsp;</td>
    <td> 
    <%--  <input type="text" name="numeroAVS" value="<%=globaz.hermes.utils.AVSUtils.formatAVS8Or9(viewBean.getNumeroAVS())%>" class="disabled" readonly>--%>
    <input type="text" name="numeroAVS" value="<%=(viewBean.getNumeroAVS().equals(""))?"&nbsp;":globaz.commons.nss.NSUtil.formatAVSNew(viewBean.getNumeroAVS(),viewBean.getNumeroAvsNNSS().equals("true"))%>" class="disabled" readonly>
    </td>
  </tr>
  <tr> 
    <td>SZ :&nbsp;</td>
    <td> 
      <input type="text" name="motif" value="<%=viewBean.getMotif()%>" class="disabled" readonly>
    </td>
  </tr>
  <%if(viewBean.getForNumeroCaisse().length()>0){%>
  <tr> 
    <td>Caisse :&nbsp;</td>
    <td> 
      <input type="text" name="caisse" value="<%=viewBean.getForNumeroCaisse()%>" class="disabled" readonly>
    </td>
  </tr>  
  <%}%>
  <tr> 
    <td>IK-Auszug senden an &nbsp;</td>
    <td> 
      <input type="text" name="email" class="input" maxlength="40" size="40" style="width:8cm;" value="<%=request.getParameter("email")!=null?request.getParameter("email"):(viewBean.getEmail()==null?"":viewBean.getEmail())%>">
    </td>
  </tr>  
<input type="hidden" name="idAnnonce" value="<%=viewBean.getIdAnnonce()%>">
<input type="hidden" name="isArchivage" value="<%=viewBean.getArchivage()%>">

<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<%	}%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>