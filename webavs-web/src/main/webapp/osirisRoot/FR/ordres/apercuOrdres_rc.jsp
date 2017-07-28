
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA0042";
rememberSearchCriterias = true;
%>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%@ page import="globaz.osiris.db.ordres.*" %>
<%@ page import="globaz.osiris.utils.CAUtil" %>
<% bButtonNew = false; %>
<%
globaz.osiris.db.comptes.CAOperation viewBean = null;

if(session.getAttribute("typeOrdreGroupe").equals(CAOrdreGroupe.RECOUVREMENT)) {
	viewBean = (globaz.osiris.db.comptes.CAOperationOrdreRecouvrementViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
} else {
	viewBean = (globaz.osiris.db.comptes.CAOperationOrdreVersementViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
}

globaz.osiris.db.ordres.CAOrdreGroupe ordreGroupe = viewBean.getOrdreGroupe();

if (ordreGroupe==null)
	ordreGroupe = viewBean.getOrdreGroupe(request.getParameter("forIdOrdreGroupe"));
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="options" menuId="CA-OrdresGroupes" showTab="options">
	<ct:menuSetAllParams key="id" value="<%=viewBean.getIdOrdreGroupe()%>"/>
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdOrdreGroupe()%>"/>
	
	<ct:menuActivateNode active="yes" nodeId="<%=CAUtil.ID_MENU_NODE_CA_ORDRES_GROUPES_PREPARATION %>"/>
	<ct:menuActivateNode active="yes" nodeId="<%=CAUtil.ID_MENU_NODE_CA_ORDRES_GROUPES_EXECUTION %>"/>
	<ct:menuActivateNode active="yes" nodeId="<%=CAUtil.ID_MENU_NODE_CA_ORDRES_GROUPES_ANNULER %>"/>
	<% if (!"0".equals(ordreGroupe.getEtat())) { %>
		<ct:menuActivateNode active="no" nodeId="<%=CAUtil.ID_MENU_NODE_CA_ORDRES_GROUPES_PREPARATION %>"/>
	<% } %>	
		
	<% if (CAOrdreGroupe.ANNULE.equals(ordreGroupe.getEtat())) { %>
		<ct:menuActivateNode active="no" nodeId="<%=CAUtil.ID_MENU_NODE_CA_ORDRES_GROUPES_EXECUTION %>"/>
		<ct:menuActivateNode active="no" nodeId="<%=CAUtil.ID_MENU_NODE_CA_ORDRES_GROUPES_ANNULER %>"/>
	<% } %>			
</ct:menuChange>

<script language="JavaScript">
	<%String idogrp = viewBean.getIdOrdreGroupe();
		if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(idogrp))
			idogrp = ordreGroupe.getIdOrdreGroupe();
	%>

  <%
  if(session.getAttribute("typeOrdreGroupe").equals(CAOrdreGroupe.RECOUVREMENT)) {
	  %>usrAction = "<%=globaz.osiris.application.CAApplication.DEFAULT_OSIRIS_NAME %>.ordres.apercuOrdresRecouvrement.lister"; <%
  } else {
	  %>usrAction = "<%=globaz.osiris.application.CAApplication.DEFAULT_OSIRIS_NAME %>.ordres.apercuOrdresVersement.lister"; <%
  }
  %>
  bFind = true;
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Aperçu des ordres<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
          <TR>
            <TD width="126">Ordre groupé</TD>
            <TD nowrap colspan="2">
              <INPUT type="text" value="<%=ordreGroupe.getIdOrdreGroupe()%> - <%=ordreGroupe.getMotif()%>" class="libelleLongDisabled" readonly>
              <input type="hidden" name="forIdOrdreGroupe" value="<%=ordreGroupe.getIdOrdreGroupe()%>" class="libelleLongDisabled" readonly>
            </TD>
            <TD align="right" valign="bottom" width="500">Date &nbsp;
              <INPUT type="text" value="<%=ordreGroupe.getDateEcheance()%>" size="15" class="dateDisabled" readonly>
            </TD>
          </TR>
          
          <TR>
            <TD>Type</TD>
            <TD nowrap colspan="2"><input type="text" value="<%=viewBean.getOrdreGroupe().getUcTypeOrdreGroupe().getLibelle() %>" class="Disabled" readonly/>
            </TD>
            <TD align="right">Total&nbsp;
              <INPUT type="text" class="montantDisabled" value="<%=ordreGroupe.getTotalToCurrency().toStringFormat()%>" size="15" readonly>
            </TD>
          </TR>
          
          <TR><TD colspan="3">&nbsp;</TD></TR>
          
          <TR>
            <TD>N° Affilié</TD>
            <TD nowrap colspan="2"><INPUT type="text" name="forIdExterneRole" value=""/>
            </TD>
            <TD align="right">R&ocirc;le&nbsp;
            <INPUT type='hidden' name='forSelectionTri' value="1">
				<select name="forSelectionRole" tabindex="2">
              		<%=CARoleViewBean.createOptionsTags(objSession, request.getParameter("forSelectionRole"))%>
              	</select>
            </TD>
          </TR>
            
          <TR>
            <TD>Bénéficiaire</TD>
            <TD nowrap colspan="2">
              <INPUT type="text" name="forNomCacheLike" value="<%if (request.getParameter("fromNomCache") != null)%><%=request.getParameter("fromNomCache")%>" class="libelleStandard" tabindex=1>
            </TD>
            <TD align="right">Identifiant&nbsp;
              <INPUT type="text" name="forIdOperation" value="<%if (request.getParameter("forIdOperation") != null)%><%=request.getParameter("forIdOperation")%>">
            </TD>
          </TR>
          
          <TR>
            <TD>Transaction</TD>
            <TD nowrap colspan="2">
              <INPUT type="text" name="forNumTransaction" value="<%if (request.getParameter("forNumTransaction") != null)%><%=request.getParameter("forNumTransaction")%>" class="numero" tabindex=3>
            </TD>
            <TD align="right">Montant&nbsp;
              <INPUT type="text" name="forMontant" value="<%if (request.getParameter("forMontant") != null)%><%=request.getParameter("forMontant")%>" class="montant" tabindex=2>
            </TD>
          </TR>
          
          <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%> <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>