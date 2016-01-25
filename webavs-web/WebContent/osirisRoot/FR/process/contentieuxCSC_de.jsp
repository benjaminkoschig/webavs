<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/conflict.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/conflict/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%idEcran = "GCA5004"; %>
<%@ page import="globaz.globall.util.*" %>
<%@ page import="globaz.osiris.db.contentieux.*" %>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%

globaz.osiris.db.process.CAContentieuxCSCViewBean viewBean = (globaz.osiris.db.process.CAContentieuxCSCViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);

globaz.osiris.db.contentieux.CAEtape elementEtape = (globaz.osiris.db.contentieux.CAEtape)globaz.globall.http.JSPUtils.useBean(request, "elementEtape", "globaz.osiris.db.contentieux.CAEtape", "session"); 

usrAction = "osiris.process.contentieuxCSC.executer";
selectedIdValue = viewBean.getIdSection();

globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
%>
<!-- seb <script>window.open('../dossier/envoiDocumentContentieux.jsp','PrintContentieux');</script> seb -->
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> <%-- /tpl:put --%>
<%@ include file="/theme/conflict/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
top.document.title = "Process - Contentieux - " + top.location.href;

function init() { } 
function onOk() {	
	if (!document.forms[0].elements('imprimerListeDeclenchement').checked ||
		!document.forms[0].elements('imprimerDocument').checked) {	
		if (window.confirm("Au moins une des deux listes à imprimer n'a pas été sélectionée, êtes vous sûre de vouloir continuer ?")){
	       	 document.forms[0].submit();
		 }
	 }
	 else {
		 document.forms[0].submit();
	}
} 
function onCancel() {
	document.forms[0].elements('userAction').value="back";
//	document.forms[0].submit();
}



// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/conflict/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Contentieux<%-- /tpl:put --%>
<%@ include file="/theme/conflict/bodyStart2.jspf" %>
			  <%-- tpl:put name="zoneMain" --%> 
          <TR> 
            <TD nowrap width="25%" height="14">E-mail</TD>
            <TD nowrap height="14" colspan="2"> 
              <INPUT type="text" name="eMailAddress" value="<%=viewBean.getEMailAddress()%>" class="libelleLong">
            </TD>
            <TD nowrap width="8%" height="14">&nbsp;</TD>
          </TR>
          <TR> 
            <TD nowrap width="25%">Date de r&eacute;f&eacute;rence</TD>
            <TD nowrap colspan="2"> 
              <ct:FWCalendarTag name="dateReference" value="<%=viewBean.getDateReference()%>" doClientValidation="CALENDAR" value=""/>
            </TD>
            <TD nowrap width="8%">&nbsp;</TD>
          </TR>
          <TR> 
            <TD nowrap width="25%">Date sur documents</TD>
            <TD nowrap colspan="2"> 
              <ct:FWCalendarTag name="dateSurDocument" value="<%=viewBean.getDateSurDocument()%>" doClientValidation="CALENDAR" value=""/>
            </TD>
            <TD nowrap width="8%">&nbsp;</TD>
          </TR>
          <TR> 
            <TD nowrap width="25%">Séquence</TD>
            <TD nowrap width="25%"> 
              <select name="sequence" >                
                <%CASequenceContentieux tempSeqCont;
					CASequenceContentieuxManager manSeqCont = new CASequenceContentieuxManager();
					manSeqCont.setSession(viewBean.getSession());
					manSeqCont.find();
					for(int i = 0; i < manSeqCont.size(); i++){
								tempSeqCont = (CASequenceContentieux)manSeqCont.getEntity(i); %>
                <option value="<%=tempSeqCont.getIdSequenceContentieux()%>"><%=tempSeqCont.getDescription()%></option>
                <%}%>
              </select>
            </TD>
            <TD nowrap>&nbsp; </TD>
            <TD nowrap width="8%">&nbsp;</TD>
          </TR>
          <TR> 
            <TD nowrap width="25%">Libellé du journal de comptabilisation</TD>
            <TD nowrap colspan="2"> 
              <input type="text" name="libelleJournal" class="libelleStandard" value="<%=viewBean.getLibelleJournal()%>">
            </TD>
            <TD nowrap width="8%">&nbsp;</TD>
          </TR>
          
<%
	String selectBlockTriSpecial = globaz.osiris.parser.CASelectBlockParser.getForTriSpecialSelectBlock(objSession);
              		
	if (!globaz.jade.client.util.JadeStringUtil.isBlank(selectBlockTriSpecial)) {
		out.print("<tr>");
		out.print("<td nowrap align=\"left\">Tri comptes annexes</td>");
		out.print("<td nowrap align=\"left\">");
		out.print(selectBlockTriSpecial);
		out.print("</td>");
		out.print("</tr>");
	}
%>             
          
          <TR> 
            <TD nowrap width="25%">Mode pr&eacute;visionnel</TD>
            <TD nowrap colspan="2"> 
              <input type="checkbox" name="modePrevisionnel" value="on" checked >
            </TD>
            <TD nowrap width="8%">&nbsp;</TD>
          </TR>
          <TR> 
            <TD nowrap width="25%">Imprimer liste de d&eacute;clenchement </TD>
            <TD nowrap colspan="2"> 
              <input type="checkbox" name="imprimerListeDeclenchement" value="on" <%if (viewBean.getImprimerListeDeclenchement().booleanValue()) {%> checked <%}%> >
            </TD>
            <TD nowrap width="8%">&nbsp;</TD>
          </TR>
          
          <TR> 
            <TD nowrap width="25%">Imprimer les documents </TD>
            <TD nowrap colspan="2"> 
              <input type="checkbox" name="imprimerDocument" value="on" <%if (viewBean.getImprimerDocument().booleanValue()) {%> checked <%}%>>
            </TD>
            <TD nowrap width="8%">&nbsp;</TD>
          </TR>
          
<%
	String selectBlock = globaz.osiris.parser.CASelectBlockParser.getForIdGenreSelectBlock(objSession);
              		
	if (!globaz.jade.client.util.JadeStringUtil.isBlank(selectBlock)) {
		out.print("<tr>");
		out.print("<td nowrap align=\"left\">Genre</td>");
		out.print("<td nowrap align=\"left\">");
		out.print(selectBlock);
		out.print("</td>");
		out.print("</tr>");
	}
%>
  
<%
	String selectCategorieSelect= globaz.osiris.parser.CASelectBlockParser.getForIdCategorieSelectBlock(objSession);
              		
	if (!globaz.jade.client.util.JadeStringUtil.isBlank(selectCategorieSelect)) {
		out.print("<tr>");
		out.print("<td nowrap align=\"left\">Cat&eacute;gorie</td>");
		out.print("<td nowrap align=\"left\">");
		out.print(selectCategorieSelect);
		out.print("</td>");
		out.print("</tr>");
	}
%>  
          <tr> 
            <td nowrap width="128">No affilié de...</td>
            <td nowrap width="576">
            	<table cellpadding ="0" cellspacing="0">
            		<tr>
            			<td><input type="text" name="fromNoAffilie" value="" class="libelleLong"></td>		            	
		            	<td width="70" nowrap align="right">&nbsp;à...&nbsp;</td>
		            	<td nowrap><input type="text" name="untilNoAffilie" value="" class="libelleLong"></td>
            		</tr>
            	</table> 				
            </td>
          </tr>                  
          
<%
	if (!globaz.jade.client.util.JadeStringUtil.isBlank(selectBlockTriSpecial)) {
%>
	<tr> 
    	<td nowrap width="128">Satellite/Consulat&nbsp;de...</td>
        <td nowrap width="576">
        <table cellpadding ="0" cellspacing="0">
        <tr>
        	<td><input type="text" name="fromNoSatConsul" value="" class="libelleLong"></td>		            	
		    <td width="70" nowrap align="right">&nbsp;à...&nbsp;</td>
		    <td nowrap><input type="text" name="untilNoSatConsul" value="" class="libelleLong"></td>
        </tr>
        </table> 				
        </td>
	</tr>
<%
	}
%>            
          
          <TR> 
            <TD nowrap width="25%">&nbsp;</TD>
            <TD nowrap colspan="2">&nbsp;</TD>
            <TD nowrap width="8%">&nbsp;</TD>
          </TR>
          <TR> 
            <TD colspan="3" nowrap width="42%" align="left">Role</TD>
            <TD nowrap>&nbsp;</TD>
          </TR>
          <TR> 
            <TD colspan="3" nowrap width="42%"> 
              <select name="roles" size="10" multiple>
                <%CARole tempRole;
					 		CARoleManager manRole = new CARoleManager();
							manRole.setSession(viewBean.getSession());
							manRole.find();
							for(int i = 0; i < manRole.size(); i++){
								tempRole = (CARole)manRole.getEntity(i);%>
                <option value="<%=tempRole.getIdRole()%>"><%=tempRole.getDescription()%></option>
                <% } %>
              </select>
            </TD>
            <TD nowrap>&nbsp;</TD>
          </TR>
          <%-- /tpl:put --%>
<%@ include file="/theme/conflict/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<script>
	// menu 	
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/conflict/bodyClose.jspf" %>
<%-- /tpl:insert --%>
