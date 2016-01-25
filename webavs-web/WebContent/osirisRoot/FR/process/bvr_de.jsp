 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%idEcran = "GCA3008"; %>
<%

formAction= request.getContextPath()+mainServletPath+"Root/"+languePage+"/process/bvrChargementFile_de.jsp";
globaz.osiris.api.process.APIProcessUploadBVR viewBean = (globaz.osiris.api.process.APIProcessUploadBVR) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
userActionValue = "osiris.process.bvr.executer";

if (!globaz.jade.client.util.JadeStringUtil.isBlank(request.getParameter("typeBvrFtp"))) {
	if (request.getParameter("typeBvrFtp").equals("bvr")) {
		userActionValue = "osiris.process.bvr.executer";
	} else {
		userActionValue = "osiris.process.recouvrementDirect.executer";
	}
}

formEncType = "multipart/form-data";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> <%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<ct:menuChange displayId="options" menuId="CA-BVR" showTab="options"/>

<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
top.document.title = "Process - Lecture d'un fichier BVR - " + top.location.href;
// stop hiding -->

function manageTypeBvr() {
	if (document.getElementById("typeBvr").value == 'bvr') {
		document.getElementById("userAction").value = 'osiris.process.bvr.executer';
	} else {
		document.getElementById("userAction").value = 'osiris.process.recouvrementDirect.executer';	
	}
}

function validate(){
	if (document.getElementById("fileName").value == '') {
		alert("Chemin et nom du fichier BVR non renseigné !");
		return false;
	} else {
		return true;
	}
}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Lecture du fichier BVR<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          <TR> 
            <TD nowrap width="128">E-mail</TD>
            <TD nowrap width="576"> 
              <INPUT type="text" name="eMailAddress" class="libelleLong" value="<%=viewBean.getEMailAddress()%>">
            </TD>
            <TD width="65">&nbsp;</TD>
            <TD width="67" nowrap>&nbsp;</TD>
            <TD nowrap width="167">&nbsp;</TD>
          </TR>
          <TR> 
            <TD nowrap width="128">Organe d'ex&eacute;cution</TD>
            <TD nowrap width="576"> 
              <select name="idOrganeExecution" class="list">
                <%globaz.osiris.db.ordres.CAOrganeExecutionManager mgr = new globaz.osiris.db.ordres.CAOrganeExecutionManager();
			    globaz.osiris.db.ordres.CAOrganeExecution org;
				mgr.setSession(viewBean.getSession());
			    mgr.find();
				for (int i = 0; i < mgr.size(); i++) {
				  org = (globaz.osiris.db.ordres.CAOrganeExecution)mgr.getEntity(i);
				  if (!org.getIdTypeTraitementBV().equals(globaz.osiris.db.ordres.CAOrganeExecution.BVR_AUCUN)) {
				  %>
                <option value="<%=org.getIdOrganeExecution()%>"><%=org.getNom()%></option>
                <%
				  }
				}
			  %>
              </select>
            </TD>
            <TD width="65">&nbsp;</TD>
            <TD width="67" nowrap>&nbsp;</TD>
            <TD nowrap width="167">&nbsp;</TD>
          </TR>
          
          <TR> 
            <TD nowrap width="128">Type</TD>
            <TD nowrap width="576"> 
				<%if (globaz.jade.client.util.JadeStringUtil.isBlank(request.getParameter("typeBvrFtp"))) {%>
              		<select name="typeBvr" onchange="manageTypeBvr()">
              			<option value="bvr">BVR</option>
              			<option value="debitdirect">Débit direct</option>              			
              		</select>
                <%} else {
                	if (request.getParameter("typeBvrFtp").equals("bvr")) {
                	%>
					<INPUT type="text" name="typeBvrLibelle" style="width:7cm" size="16" maxlength="256" value="BVR" class="libelleDisabled" tabindex="-1" readonly/>                	
                	<%
                	} else {
                	%>
					<INPUT type="text" name="typeBvrLibelle" style="width:7cm" size="16" maxlength="256" value="Débit direct" class="libelleDisabled" tabindex="-1" readonly/>
					<%
					}
                %>
                <%}%>
            </TD>
            <TD width="65">&nbsp;</TD>
            <TD width="67" nowrap>&nbsp;</TD>
            <TD nowrap width="167">&nbsp;</TD>
          </TR>
          
          <TR> 
            <TD nowrap width="128">Date valeur</TD>
            <TD nowrap width="576"> 
              <ct:FWCalendarTag name="dateValeur" doClientValidation="CALENDAR" value=""/>
            </TD>
            <TD width="65"></TD>
            <TD width="67" nowrap>&nbsp;</TD>
            <TD nowrap width="167">&nbsp;</TD>
          </TR>
          <TR> 
            <TD nowrap width="128">Libell&eacute;</TD>
            <TD nowrap width="576"> 
              <INPUT type="text" name="libelle" maxlength="80" class="libelleLong">
            </TD>
            <TD width="65"></TD>
            <TD width="67" nowrap>&nbsp;</TD>
            <TD nowrap width="167">&nbsp;</TD>
          </TR>
          <TR> 
            <TD nowrap width="128">Chemin et nom du fichier BVR</TD>
            <TD nowrap width="576"> 
				<%if (globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getFileName())) {%>
              <input type="file" name="fileName" maxlength="256" class="libelleLong">
                <%} else {%>
                <INPUT type="text" name="fileName" style="width:7cm" size="16" maxlength="256" value="<%=viewBean.getFileName()%>" class="libelleDisabled" tabindex="-1" readonly>
                <%}%>
            </TD>
            <TD width="65"></TD>
            <TD width="67" nowrap>&nbsp;</TD>
            <TD nowrap width="167">&nbsp;</TD>
          </TR>
          
          <TR> 
            <TD nowrap width="128">Simulation</TD>
            <TD nowrap width="576"> 
              <input type="checkbox" name="simulation" value="on">
            </TD>
            <TD width="65"></TD>
            <TD width="67" nowrap>&nbsp;</TD>
            <TD nowrap width="167">&nbsp;</TD>
          </TR>
          <%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<%	}%>
<SCRIPT>
document.forms[0].enctype = "multipart/form-data";
document.forms[0].method = "post";
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>