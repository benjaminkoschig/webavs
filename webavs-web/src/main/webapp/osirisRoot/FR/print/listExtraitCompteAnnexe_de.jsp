
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA2003"; %>
<%@ page import="globaz.globall.util.*" %>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%@ page import="globaz.osiris.servlet.action.CADefaultServletAction" %>
<%@ page import="globaz.osiris.db.print.*" %>
<%
CAListExtraitCompteAnnexeViewBean viewBean = (CAListExtraitCompteAnnexeViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);

globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> <%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
<%
userActionValue = globaz.osiris.application.CAApplication.DEFAULT_OSIRIS_NAME + ".print.listExtraitCompteAnnexe.executer";
%>
top.document.title = "Liste - Impression de la liste des extraits des comptes - " + top.location.href;
// stop hiding -->
</SCRIPT>

<SCRIPT language="JavaScript">
	function manageFrom() {
		if (document.forms['mainForm'].elements['forSelectionTri'].value == '<%=globaz.osiris.db.comptes.extrait.CAExtraitCompteManager.ORDER_BY_IDSECTION%>') {
			document.getElementById("fromSectionField").style.display = "block";
		} else {
			document.getElementById("fromSectionField").style.display = "none";
		}

		if (document.forms['mainForm'].elements['forSelectionTri'].value == '<%=globaz.osiris.db.comptes.extrait.CAExtraitCompteManager.ORDER_BY_DATE_COMPTABLE%>' || document.forms['mainForm'].elements['forSelectionTri'].value == '<%=globaz.osiris.db.comptes.extrait.CAExtraitCompteManager.ORDER_BY_DATE_VALEUR%>') {
			document.getElementById("fromDateField").style.display = "block";
		} else {
			document.getElementById("fromDateField").style.display = "none";
		}
	}
</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Impression de la liste des extraits des comptes annexe<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

          <tr>
            <td nowrap width="128">Affili?</td>
            <td nowrap width="576">
                <TEXTAREA cols="40" rows="2" class="libelleLongDisabled" readonly><%=viewBean.getDescriptionAffilie()%></TEXTAREA>
              </TD>
          </tr>


          <tr>
            <td nowrap width="128">E-mail</td>
            <td nowrap width="576">
              <input type="text" name="eMailAddress" value="<%=viewBean.getEMailAddress()%>" class="libelleLong">
            </td>
          </tr>

          <tr>
            <td nowrap width="128">Langue d'impression</td>
            <td nowrap width="576">

            <%
            	java.util.HashSet except = new java.util.HashSet();
            	except.add(globaz.osiris.translation.CACodeSystem.CS_ANGLAIS);
            	except.add(globaz.osiris.translation.CACodeSystem.CS_ROMANCHE);
            	except.add(globaz.osiris.translation.CACodeSystem.CS_ESPAGNOL);
            	except.add(globaz.osiris.translation.CACodeSystem.CS_PORTUGAIS);
            %>

            <ct:FWSystemCodeSelectTag name="printLanguageFromScreen" defaut="" codeSystemManager="<%=globaz.osiris.translation.CACodeSystem.getLcsLangue(objSession)%>" except="<%=except%>"/>
            </td>
          </tr>

          <tr>
            <td nowrap width="128">Sections</td>
            <td nowrap width="576">
              <select name="forSelectionSections" class="libelleCourt">
                <option value="<%=globaz.osiris.db.comptes.extrait.CAExtraitCompteManager.SOLDE_ALL%>" selected>Toutes</option>
                <option value="<%=globaz.osiris.db.comptes.extrait.CAExtraitCompteManager.SOLDE_OPEN%>">ouvertes</option>
                <option value="<%=globaz.osiris.db.comptes.extrait.CAExtraitCompteManager.SOLDE_CLOSED%>">sold&eacute;es</option>
              </select>
            </td>
          </tr>

          <tr>
            <td nowrap width="128">Tri</td>
            <td nowrap width="576">
				<select name="forSelectionTri"  class="libelleCourt"  onchange="manageFrom()">
                	<option selected value="<%=globaz.osiris.db.comptes.extrait.CAExtraitCompteManager.ORDER_BY_DATE_COMPTABLE%>">par date comptable</option>
                	<option value="<%=globaz.osiris.db.comptes.extrait.CAExtraitCompteManager.ORDER_BY_DATE_VALEUR%>">par date valeur</option>
	                <option value="<%=globaz.osiris.db.comptes.extrait.CAExtraitCompteManager.ORDER_BY_IDSECTION%>">par section</option>
    			</select>
            </td>
          </tr>

          <tr>
            <td nowrap width="128">Op?rations</td>
            <td nowrap width="576">
              <select name="forIdTypeOperation" >
                <option value="<%=globaz.osiris.db.comptes.extrait.CAExtraitCompteManager.FOR_ALL_IDTYPEOPERATION%>">Toutes</option>
                <%CATypeOperation tempTypeOperation;
					CATypeOperationManager manTypeOperation = new CATypeOperationManager();
					manTypeOperation.setSession(objSession);
					manTypeOperation.find();
						for(int i = 0; i < manTypeOperation.size(); i++){
							tempTypeOperation = (CATypeOperation)manTypeOperation.getEntity(i);
							if (!tempTypeOperation.getIdTypeOperation().equalsIgnoreCase("PEND"))%>
                <option value="<%=tempTypeOperation.getIdTypeOperation()%>"

                <%
               		if (tempTypeOperation.getIdTypeOperation().equals(globaz.osiris.api.APIOperation.CAECRITURE)) {
               			out.print("SELECTED");
               		}
               	%>

                > <%=tempTypeOperation.getDescription()%>
                </option>
                <%}%>
              </select>
            </td>
          </tr>

          <tr id="fromDateField">
          <td nowrap width="128">Date ? partir de</td>
          <td nowrap width="576">
          <%
          String year = "01.01.";
          year += "" + (globaz.globall.util.JACalendarGregorian.today().getYear() - 1);
          %>

          <ct:FWCalendarTag name="fromDate" doClientValidation="CALENDAR" value="<%=year%>"/>

          &nbsp;jusqu'au
          <ct:FWCalendarTag name="untilDate" doClientValidation="CALENDAR" value="<%=viewBean.getFormatedDateToday()%>"/>
          </td>
          </tr>

          <tr id="fromSectionField">
          <td nowrap width="128">Sections de</td>
          <td nowrap width="576">
          <input type="text" name="fromSection" class="libelleStandard"/>
          &nbsp;&agrave;&nbsp;
          <input type="text" name="untilSection" class="libelleStandard"/>
          </td>
          </tr>

          <SCRIPT language="JavaScript">
          manageFrom();
          </SCRIPT>

          <tr>
            <td nowrap width="128">Date sur document</td>
            <td nowrap width="576">
				<ct:FWCalendarTag name="documentDate" doClientValidation="CALENDAR" value="<%=viewBean.getFormatedDateToday()%>"/>
            </td>
          </tr>
          
          <tr>
            <td nowrap width="128">Imprimer fichier de travail Excel</td>
            <td nowrap width="576">
				<input type="checkbox" name="imprimerExtraitCompteExcelml" value="on" <%if (viewBean.getImprimerExtraitCompteExcelml().booleanValue()) {%> checked <%}%> >
            </td>
          </tr>

   			<input type="hidden" name="fromNoAffilie" value="<%=viewBean.getIdExterneRole()%>">
           	<input type="hidden" name="untilNoAffilie" value="<%=viewBean.getIdExterneRole()%>">
           	<input type="hidden" name="descriptionAffilie" value="<%=viewBean.getDescriptionAffilie()%>">

          <%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<%	}%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>