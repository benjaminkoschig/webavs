<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%
idEcran = "GCA0050";
rememberSearchCriterias = true;
CACompteAnnexeViewBean viewBean = (CACompteAnnexeViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
String requestId = request.getParameter("id");
bButtonNew = false;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<script language="JavaScript">
usrAction = "<%=globaz.osiris.application.CAApplication.DEFAULT_OSIRIS_NAME %>.comptes.historiqueCompteAnnexe.lister";
bFind = true;

function postInit() {
	document.getElementById("idExterneRubrique").title="Ersetzung von allen Zeichen\t= %\nErsetzung eines Zeichens\t= _\nZum Beispiel %0.2_12.0000";
	document.getElementById("idExterneSection").title="Ersetzung von allen Zeichen\t= %\nErsetzung eines Zeichens\t= _\nZum Beispiel %410_0";
}
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Verlauf des Abrechnungskontos<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<tr>
					      <td rowspan="3" valign="top">Konto&nbsp;</td>
					       <td rowspan="3" valign="top" width="40%">
					        <input type="hidden" name="forIdCompteAnnexe" value="<%=viewBean.getIdCompteAnnexe()%>">
					      	<textarea cols="40" rows="3" class="libelleLongDisabled" readonly><%=viewBean.getTitulaireEntete()%></textarea>
					      </td>
      <td>Sektion</td>
      <td width="30%"><input name="likeIdExterneSection" id="idExterneSection"></td>
		<td nowrap>Valutadatum&nbsp;</td>
      	<td width="30%"><ct:FWCalendarTag name="fromDate" value="" /></td>
    </tr>
    <tr>
    <td width="100">Rubrik</td>
      <td>
    <%
		String jspLocation = request.getContextPath()+ mainServletPath + "Root/rubrique_select.jsp";
	%>
       <ct:FWPopupList name="likeIdExterneRubrique" id="idExterneRubrique" onFailure="rubriqueManuelleOn();" title=""
		value=""
		className="libelle"
		jspName="<%=jspLocation%>"
		minNbrDigit="1"
		forceSelection="true"
		validateOnChange="false"
		/></td>
      <td>Sortierung</td>
      <td>
      	<select name="forSelectionTri">
      		<option value="1009">Valutadatum</option>
      		<option value="1007">Rubrik, Valutadatum</option>
      	</select>
      </td>
    </tr>
    <tr>
		<td>Jahr</td>
      <td width="30%"><input name="forAnneeCotisation"></td>

      <%
		String idAdministrateurSrc = request.getParameter("idAdministrateurSrc");
		String idContentieuxSrc = request.getParameter("idContentieuxSrc");
		String libSequence = request.getParameter("libSequence");

		if (globaz.jade.client.util.JadeStringUtil.isNull(idContentieuxSrc)) {
			idContentieuxSrc = "";
		}

		if (globaz.jade.client.util.JadeStringUtil.isNull(libSequence)) {
			libSequence = "";
		}

		if (globaz.jade.client.util.JadeStringUtil.isNull(idAdministrateurSrc)) {
			idAdministrateurSrc = "";
		}

		if (!globaz.jade.client.util.JadeStringUtil.isBlank(idAdministrateurSrc)) {
		%>
			<td colspan="2" align="right">
			<A href="<%=request.getContextPath()%>/aquila?userAction=aquila.administrateurs.administrateur.afficher&selectedId=<%=idAdministrateurSrc%>" class="external_link">Verwalter</A>
			</td>
		<%
			} else {
		%>
			<td colspan="2">&nbsp;</td>
		<%
			}
		%>

		<ct:menuChange displayId="options" menuId="CA-ApercuParSectionDossier">
			<ct:menuSetAllParams key="idContentieuxSrc" value="<%=idContentieuxSrc%>"/>
			<ct:menuSetAllParams key="libSequence" value="<%=libSequence%>"/>
			<ct:menuSetAllParams key="idAdministrateurSrc" value="<%=idAdministrateurSrc%>"/>
			<ct:menuSetAllParams key="idTiers" value="<%=viewBean.getIdTiers()%>"/>
			<ct:menuSetAllParams key="forIdExterneRoleLike" value="<%=viewBean.getIdExterneRole()%>"/>
			<ct:menuSetAllParams key="forIdRole" value="<%=viewBean.getIdRole()%>"/>
		</ct:menuChange>
    </tr>
		<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>