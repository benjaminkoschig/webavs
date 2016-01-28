
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA0046"; %>
<%@ page import="globaz.globall.util.*" %>
<%@ page import="globaz.osiris.db.contentieux.*" %>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%

globaz.osiris.db.process.CAContentieuxViewBean viewBean = (globaz.osiris.db.process.CAContentieuxViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);

globaz.osiris.db.contentieux.CAEtape elementEtape = (globaz.osiris.db.contentieux.CAEtape)globaz.globall.http.JSPUtils.useBean(request, "elementEtape", "globaz.osiris.db.contentieux.CAEtape", "session");

userActionValue = "osiris.process.contentieux.executer";
selectedIdValue = viewBean.getIdSection();

globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
%>
<!-- seb <script>window.open('../dossier/envoiDocumentContentieux.jsp','PrintContentieux');</script> seb -->
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> <%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="options" menuId="CA-OnlyDetail" showTab="menu"/>

<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
top.document.title = "Process - Contentieux - " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Contentieux<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
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
	          <ct:FWCalendarTag name="dateReference" doClientValidation="CALENDAR" value="<%=viewBean.getDateReference()%>"/>
            </TD>
            <TD nowrap width="8%">&nbsp;</TD>
          </TR>
          <TR>
            <TD nowrap width="25%">Date de d&eacute;lai de paiement</TD>
            <TD nowrap colspan="2">
	          <ct:FWCalendarTag name="dateDelaiPaiement" doClientValidation="CALENDAR" value="<%=viewBean.getDateDelaiPaiement()%>"/>
            </TD>
            <TD nowrap width="8%">&nbsp;</TD>
          </TR>
          <TR>
            <TD nowrap width="25%">Date sur documents</TD>
            <TD nowrap colspan="2">
	          <ct:FWCalendarTag name="dateSurDocument" doClientValidation="CALENDAR" value="<%=viewBean.getDateSurDocument()%>"/>
            </TD>
            <TD nowrap width="8%">&nbsp;</TD>
          </TR>
          <TR>
            <TD nowrap width="25%">Séquence</TD>
            <TD nowrap width="25%">
              <select name="sequence" >
                <option selected value="-1">Toutes</option>
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
            <TD nowrap width="25%">1er Tri liste pr&eacute;visionnelle et documents</TD>
            <TD nowrap width="25%">
              <select name="selectionTriListeCA" >
                <option value="1">Compte annexe (nom)</option>
                <option selected value="2">Compte annexe (num&eacute;ro)</option>
              </select>
            </TD>
            <TD nowrap>&nbsp;</TD>
            <TD nowrap width="8%">&nbsp;</TD>
          </TR>
          <TR>
            <TD nowrap width="25%">2&egrave;me Tri liste pr&eacute;visionnelle
              et documents</TD>
            <TD nowrap width="25%">
              <select name="selectionTriListeSection" >
                <option selected value="1">Section (num&eacute;ro)</option>
                <option value="2">Section (date)</option>
              </select>
            </TD>
            <TD nowrap>&nbsp;</TD>
            <TD nowrap width="8%">&nbsp;</TD>
          </TR>
          <TR>
            <TD nowrap width="25%">Libellé du journal de comptabilisation</TD>
            <TD nowrap colspan="2">
              <input type="text" name="libelleJournal" class="libelleStandard" value="<%=viewBean.getLibelleJournal()%>">
            </TD>
            <TD nowrap width="8%">&nbsp;</TD>
          </TR>
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
              <input type="checkbox" name="imprimerListeDeclenchement" value="on" checked>
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
          <TR>
            <TD nowrap width="25%">Imprimer liste des poursuites par office </TD>
            <TD nowrap colspan="2">
              <input type="checkbox" name="imprimerListePoursuiteOffice" value="on" <%if (viewBean.getImprimerListePoursuiteOffice().booleanValue()) {%> checked <%}%> >
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
          <TR>
            <TD nowrap width="25%">&nbsp;</TD>
            <TD nowrap colspan="2">&nbsp;</TD>
            <TD nowrap width="8%">&nbsp;</TD>
          </TR>
          <TR>
            <TD nowrap width="25%">Etape*</TD>
            <TD nowrap width="25%">Type de section</TD>
            <TD nowrap width="42%">Role</TD>
            <TD nowrap width="8%">&nbsp;</TD>
          </TR>
          <TR>
            <TD nowrap width="25%">
              <select name="typeEtapes" size="10" multiple>
                <%CAEtape tempEtape;
			    CAEtapeManager manEtape = new CAEtapeManager();
				manEtape.setSession(viewBean.getSession());
			    manEtape.find();
				for (int i=0; i < manEtape.size(); i++) {
								tempEtape = (CAEtape) manEtape.getEntity(i); %>
                <option value="<%=tempEtape.getIdEtape()%>"><%=tempEtape.getDescription()%></option>
                <%	} %>
              </select>
            </TD>
            <TD nowrap width="25%">
              <select name="typeSections" size="10" multiple>
                <%CATypeSection tempTypeSection;
				  CATypeSectionManager manTypeSection = new CATypeSectionManager();
				  manTypeSection.setSession(viewBean.getSession());
				  manTypeSection.find();
				  for(int i = 0; i < manTypeSection.size(); i++){
				    	tempTypeSection = (CATypeSection)manTypeSection.getEntity(i);%>
                <option value="<%=tempTypeSection.getIdTypeSection()%>"><%=tempTypeSection.getDescription()%></option>
                <% } %>
              </select>
            </TD>
            <TD nowrap width="42%">
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
            <TD nowrap width="8%">&nbsp;</TD>
          </TR>
          <%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<%	}%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>