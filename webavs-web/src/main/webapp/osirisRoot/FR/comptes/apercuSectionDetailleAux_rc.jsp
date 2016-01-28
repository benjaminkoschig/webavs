<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA0012";
rememberSearchCriterias = true;
%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%
CASectionViewBean viewBean = (CASectionViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
globaz.osiris.api.APICompteAnnexe compteAnnexe = viewBean.getCompteAnnexe();
String requestId = request.getParameter("id");
bButtonNew = false;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
	<ct:menuChange displayId="options" menuId="CA-DetailSectionGauche" showTab="options">
		<ct:menuSetAllParams key="id" value="<%=viewBean.getIdSection()%>"/>
		<ct:menuSetAllParams key="idSection" value="<%=viewBean.getIdSection()%>"/>
		<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdSection()%>"/>
		<ct:menuSetAllParams key="noAffiliationId" value="<%=compteAnnexe.getIdExterneRole()%>"/>
		<ct:menuSetAllParams key="osiris.section.idExterne" value="<%=viewBean.getIdExterne()%>"/>
		<ct:menuSetAllParams key="idCompteAnnexe" value="<%=viewBean.getIdCompteAnnexe()%>"/>
		<ct:menuSetAllParams key="idPlanRecouvrement" value="<%=viewBean.getIdPlanRecouvrement()%>"/>
		<ct:menuSetAllParams key="forIdSection" value="<%=viewBean.getIdSection()%>"/>
		
		<% if (globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getIdPlanRecouvrement())) {%>
		<ct:menuActivateNode active="no" nodeId="echeances_plan"/>
		<% } else { %>
		<ct:menuActivateNode active="yes" nodeId="echeances_plan"/>
		<% } %>
	</ct:menuChange>

<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
usrAction = "<%=globaz.osiris.application.CAApplication.DEFAULT_OSIRIS_NAME %>.comptes.apercuSectionDetaille.lister";
bFind = true;
naviShortKeys[82] = 'r1_c2';//  R (paramètres)
naviShortKeys[73] = 'r2_c2';//  I (incréments)
naviShortKeys[76] = 'r3_c2';//  L (libellés)
naviShortKeys[84] = 'r4_c2';//  T (inc. types)
// stop hiding -->

function removeParam(str_source, str_param) {
  var result = str_source;
  var paramPos = result.indexOf(str_param);
//   if no param, do nothing
  if (paramPos < 0)
    return result;

  nextParamPos = result.indexOf("&", paramPos + 1);
  var str_end = "";
  if (nextParamPos > -1)//   there are more parameters after this one
    str_end = result.slice(nextParamPos);

  result = result.slice(0, paramPos);
  result += str_end;

//  alert ("returning " + result);
  return result;
}


top.document.title = "Comptes - Aperçu comptable de la section par cc - " + top.location.href;
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Aper&ccedil;u comptable de la section par compte courant<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
		<input type="hidden" name="id" value="<%=requestId%>"/>
        <TR>
          <TD width="128" rowspan="3">
          	<% if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getIdCompteAnnexe())) { %>
				<A href="osiris?userAction=osiris.comptes.apercuComptes.afficher&selectedId=<%=viewBean.getIdCompteAnnexe()%>">Compte</A>
			<% } else { %>
				Compte
			<% } %>
		</TD>
          <TD nowrap colspan="2" rowspan="3">
            <TEXTAREA cols="40" rows="3" class="libelleLongDisabled" readonly><%=viewBean.getCompteAnnexe().getTitulaireEntete()%></TEXTAREA>
          </TD>
          <TD width="13" rowspan="3"></TD>
          <TD width="89">
            <p>&nbsp;</p>
            <p></p>
          </TD>
          <TD align="right">
            <p>Période </p>
          </TD>
          <TD>
            <input type="text" name="DateDebutDateFin" size="25" maxlength="25" value="<%=viewBean.getCompteAnnexe().getRole().getDateDebutDateFin(viewBean.getCompteAnnexe().getIdExterneRole())%>" class="libelleLongDisabled" tabindex="-1" readonly>
          </TD>
        </TR>
        <TR>
          <TD width="89">&nbsp;</TD>
          <TD align="right">Opérations</TD>
            <TD width="199" align="right" valign="bottom">
              <select name="forIdTypeOperation" class="libelleStandard" >
                <option value="1000">Toutes</option>
                <%
                	CATypeOperation tempTypeOperation;
					CATypeOperationManager manTypeOperation = new CATypeOperationManager();
					manTypeOperation.setSession(objSession);
					manTypeOperation.find();
					for(int i = 0; i < manTypeOperation.size(); i++){
						tempTypeOperation = (CATypeOperation)manTypeOperation.getEntity(i);
						if ((!tempTypeOperation.getIdTypeOperation().equalsIgnoreCase("PEND"))  && (tempTypeOperation.getIdTypeOperation().startsWith("A"))) { %>
                <option value="<%=tempTypeOperation.getIdTypeOperation()%>"> <%=tempTypeOperation.getDescription()%>
                </option>
                <%}%>
                <%}%>
              </select>
            </TD>
        </TR>
        <TR>
          <TD width="89">Solde compte</TD>
          <TD>
            <input type="text" name="soldeCompteCourant" size="25" maxlength="25" value="<%=viewBean.getCompteAnnexe().getSoldeFormate()%>" class="montantDisabled" tabindex="-1" readonly>
          </TD>
          <TD width="199" align="right" valign="bottom">&nbsp; </TD>
        </TR>
        <TR>
          <TD width="128" height="22">Section</TD>
          <TD nowrap colspan="2" height="22">
            <input type="text" size="40" name="section" class="libelleLongDisabled" readonly value="<%=viewBean.getFullDescription()%>">
          </TD>
          <TD width="13" height="22"></TD>
          <TD width="89" height="22">Solde section</TD>
          <TD height="22">
            <INPUT type="text" name="soldeSection" size="25" maxlength="25" value="<%=viewBean.getSoldeFormate()%>" class="montantDisabled" tabindex="-1" readonly>
          </TD>
          <TD width="199" align="right" height="22">&nbsp;
          <input type="hidden" name="forSelectionTri" value="1"/>
          </TD>
        </TR>
          <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<!--            <TD bgcolor="#FFFFFF" colspan="2" align="right">
            <A href="javascript:document.forms[0].submit();">
                <IMG name="btnFind" src="/images/btnFind.gif" border="0">
            </A>
            </TD> -->

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>