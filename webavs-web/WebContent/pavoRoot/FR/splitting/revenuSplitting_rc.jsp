
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/capage.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/capage/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ page import="globaz.globall.util.*" %>
<%
	idEcran = "CCI0019";
    globaz.pavo.db.splitting.CIRevenuSplittingRCViewBean viewBean = (globaz.pavo.db.splitting.CIRevenuSplittingRCViewBean)session.getAttribute ("viewBeanMandat");
	globaz.framework.bean.FWViewBeanInterface last = (globaz.framework.bean.FWViewBeanInterface)session.getAttribute ("viewBean");
	subTableHeight = 50;
	bButtonNew = false;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT>
bFind = true;
usrAction = "pavo.splitting.revenuSplitting.lister";
detailLink = servlet+"?userAction=pavo.splitting.revenuSplitting.afficher&_method=add";
top.document.title = "Splitting - Gestion des revenus";
timeWaiting = 1;
</SCRIPT>
<ct:menuChange displayId="options" menuId="revenuSplitting-detail" showTab="options">
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdMandatSplitting()%>"/>

</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Revenu
        de splitting<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
          <TR>
            <TD nowrap width="80">NSS</TD>
            <TD nowrap>
              <INPUT type="text" name="idTiersPartenaireDummy" size="17" maxlength="50" class="disabled" readonly value="<%=globaz.commons.nss.NSUtil.formatAVSUnknown(viewBean.getIdTiersPartenaire())%>">
              <INPUT type="hidden" name="forIdMandatSplitting" value="<%=viewBean.getIdMandatSplitting()%>">
            </TD>
            <TR>
				<TD nowrap>Date de naissance &nbsp;&nbsp;</TD>
				<TD nowrap colspan = "4"><INPUT type="text" name="naissance" class="disabled"
					size="11" readonly value="<%=viewBean.getDateNaissanceForEcran()%>">
				Sexe&nbsp;
				<INPUT type="text" name="sexe" class="disabled"
				size="11" readonly value="<%=viewBean.getSexeForEcran()%>">
				Pays &nbsp;
				<INPUT type="text" name="pays" class="disabled"
				size="50" readonly value="<%=viewBean.getPaysForEcran()%>"></TD>

			</TR>
             <TR>
            <TD nowrap width="60">Mandat</TD>
            <TD nowrap>
              <INPUT type="text" name="periodeDummy" size="24" class="disabled" readonly value="<%=viewBean.getPeriodeMandat()%>">
            </TD>
            <TD nowrap width="140">Total des revenus</TD>
            <TD nowrap>
              <INPUT type="text" name="totalRevDummy" size="20" class="disabled" readonly value="<%=viewBean.getMontant()%>" style="text-align: right">
            </TD>
          </TR>
          <TR>
            <TD nowrap width="60">A partir de</TD>
            <TD nowrap>
              <INPUT type="text" size="15" name="fromAnnee" value="">
            </TD>
            <TD nowrap width="140">Total actuel</TD>
            <TD nowrap>
              <INPUT type="text" name="totalDummy" size="20" class="disabled" readonly value="<%=viewBean.getTotalRevenus()%>" style="text-align: right">
            </TD>
          </TR>
          <%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>

      <%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyClose.jspf" %>
<%-- /tpl:insert --%>