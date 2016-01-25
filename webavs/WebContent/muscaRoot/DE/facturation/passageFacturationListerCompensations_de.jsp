<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%@ page import="globaz.musca.db.facturation.*"%>

<%

	idEcran="CFA2002";
	
	//Récupération des beans
	 FAPassageListerCompensationViewBean viewBean = (FAPassageListerCompensationViewBean) session.getAttribute ("viewBean");

	//Définition de l'action pour le bouton valider
	userActionValue = "musca.facturation.passageFacturationListerCompensations.executer";

%>

<%-- /tpl:put --%><%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="TITRE_ECRAN_CFA2002"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>

          <TR>
            <TD width="200"><ct:FWLabel key="NUMERO"/></TD>
            <TD><INPUT name="idPassage" type="text" value="<%=viewBean.getIdPassage()%>" class="numeroCourtDisabled" readonly="readonly"></TD>
          </TR>
          <tr> 
            <td width="23%" height="2"><ct:FWLabel key="EMAIL"/></td>
            <TD nowrap height="2" width="304"> 
              <input type="text" name="eMailAddress" maxlength="40" size="40" style="width:8cm;" value="<%=viewBean.getEMailAddress()%>">*
             </TD>
          </tr>
          <TR>
            <TD width="23%"><ct:FWLabel key="MODULE"/></TD>
            <TD width="304"><ct:FWListSelectTag name="idModuleFacturation"
			defaut="<%=viewBean.getIdModuleFacturation()%>"
			data="<%=globaz.musca.util.FAUtil.getModuleList(session,\"ACCEPT_PCOMP\")%>"/></TD>
          </TR>
          <tr> 
            <td width="23%" height="2"><ct:FWLabel key="TRI"/></td>
            <TD height="2" width="304"><ct:FWSystemCodeSelectTag name="idTri"
            		defaut="<%=viewBean.getIdTri()%>"
            		codeSystemManager="<%=globaz.musca.translation.CodeSystem.getLcsTriDecomptePassageWithoutBlank(session)%>"/></TD>
          </tr>
		  <td><ct:FWLabel key="TYPE_IMPRESSION"/> : </td>
		   	<TD>
		    	<input type="radio" name="typeImpression" value="pdf" <%="pdf".equals(viewBean.getTypeImpression()) ? "checked='checked'" : "" %>/>PDF&nbsp;
		    	<input type="radio" name="typeImpression" value="xls" <%="xls".equals(viewBean.getTypeImpression()) ? "checked='checked'" : "" %>/>Excel
		    </TD>
		  </TR>  
		          
<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>