<%-- tpl:insert page="/theme/find.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA4027";
	rememberSearchCriterias = true;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<script language="JavaScript">
	usrAction="osiris.mapping.journalDebit.lister";
	bFind = true;
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>Recherche mapping<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
          <tr>
            <td width="126">ID Mandat</td>
            <td nowrap colspan="2">
              <input type="text" name="forIdMandat" value="">
            </td>
          </tr>
          <tr>
            <td width="126">Compte courant src</td>
            <td nowrap colspan="2">
              <input type="text" name="forCompteCourantSrc" value="">
            </td>
          </tr>
          <tr>
            <td width="126">Contrepartie src</td>
            <td nowrap colspan="2">
              <input type="text" name="forContrePartieSrc" value="">
            </td>
          </tr>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%> <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>