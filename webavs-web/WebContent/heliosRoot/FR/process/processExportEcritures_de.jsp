<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran = ""; 

globaz.helios.db.comptes.CGExerciceComptableViewBean exerciceComptable = (globaz.helios.db.comptes.CGExerciceComptableViewBean )session.getAttribute(globaz.helios.db.interfaces.CGNeedExerciceComptable.SESSION_EXERCICECOMPTABLE);
globaz.helios.db.process.CGProcessExportEcrituresViewBean viewBean = (globaz.helios.db.process.CGProcessExportEcrituresViewBean) session.getAttribute("viewBean");
userActionValue = "helios.process.processExportEcritures.executer";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

top.document.title = "Process - Exportation d'écritures - " + top.location.href;

// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>Process - Exportation d'écritures<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>

		  <tr>
			 <td>Mandat</td>
			 <td>
			 	<input name="libelle" class="libelleLongDisabled" readonly value="<%=exerciceComptable.getMandat().getLibelle()%>"/> 
			 	<input type="hidden" name="idMandat" value="<%=exerciceComptable.getMandat().getIdMandat()%>"/>
			 	<input type="hidden" name="idExerciceComptable" value="<%=exerciceComptable.getIdExerciceComptable()%>"/>
			 </td>
		  </tr>
		  <tr>
            <td align="left" width="180" height="21" valign="middle">E-mail</td>
            <td align="left">
              <input type="text" name="eMailAddress" class="libelleLong" value="<%=viewBean.getEMailAddress()%>"/>
            </td>
          </tr>
          <tr>
			<td>Comptabilité</td>
			<td><ct:FWCodeSelectTag name="idComptabilite" defaut="<%=viewBean.getIdComptabilite()%>" codeType="CGPRODEF" /></td>
		  </tr>
          <tr>
			<td>Période comptable</td>
			<td><ct:FWListSelectTag name="idPeriodeComptable" defaut="<%=viewBean.getIdPeriodeComptable()%>" data="<%=globaz.helios.translation.CGListes.getPeriodeComptableListe(session)%>"/> </td>
		  </tr>
		   <tr>
			<td>Libellé pour exportation</td>			
			<td><input type="text" name="libelleExportation" value="<%=viewBean.getLibelleExportation()%>" size="10" maxlength="10" /></td>
		  </tr>
		  <tr>
			<td>No. de pièce comptable</td>
			<td><input type="text" name="numeroPieceComptable" value="<%=viewBean.getNumeroPieceComptable()%>" size="10" maxlength="10"/></td>
		  </tr>
		  

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