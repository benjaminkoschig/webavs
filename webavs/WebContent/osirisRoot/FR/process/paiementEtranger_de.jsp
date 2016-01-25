 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%idEcran = "GCA5007"; %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%
	
	formAction= request.getContextPath()+mainServletPath+"Root/"+languePage+"/process/paiementEtrangerChargementFile_de.jsp";
	
	String taux="1.0";
globaz.osiris.db.process.CAPaiementEtrangerViewBean viewBean = (globaz.osiris.db.process.CAPaiementEtrangerViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
userActionValue = "osiris.process.paiementEtranger.executer";
formEncType = "multipart/form-data";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> <%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
top.document.title = "Process - Fichier paiement �tranger - " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Lecture du fichier de paiement<%-- /tpl:put --%>
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
            <TD nowrap width="128">Provenance du fichier</TD>
            <TD nowrap width="376"> 
              <select name="csTypeFichier" class="list">
<%
				globaz.globall.parameters.FWParametersSystemCodeManager csmgr = new globaz.globall.parameters.FWParametersSystemCodeManager();
				csmgr.setSession(viewBean.getSession());
			  	csmgr.setForIdGroupe("OSIPMTETR");
	  			csmgr.find();
	  			
	  			for (int i=0; i<csmgr.size(); i++) {
				  globaz.globall.parameters.FWParametersSystemCode cs = (globaz.globall.parameters.FWParametersSystemCode)csmgr.getEntity(i);
%>
	                <option value="<%=cs.getIdCode()%>"><%=cs.getCurrentCodeUtilisateur().getLibelle()%></option>
                <%
				}
			  %>
              </select>
            </TD>
            <TD width="65"></TD>
            <TD width="67" nowrap>&nbsp;</TD>
            <TD nowrap width="167">&nbsp;</TD>            
          </TR>

        
          <TR>
            <TD nowrap width="128">Taux de conversion</TD>
            <TD nowrap width="376"> 
            	<INPUT type="text" name="taux" maxlength="8" value="<%=taux%>">
            </TD>
            <TD width="65"></TD>
            <TD width="67" nowrap>&nbsp;</TD>
            <TD nowrap width="167">&nbsp;</TD>            

          </TR>

          
          <TR> 
            <TD nowrap width="128">Chemin et nom du fichier BVR</TD>
            <TD nowrap width="576"> 
              <input type="file" name="fileName" maxlength="256" class="libelleLong">
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