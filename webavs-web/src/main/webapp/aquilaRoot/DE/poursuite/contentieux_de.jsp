<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ page import="globaz.aquila.db.poursuite.*"%>
<%
	COContentieuxViewBean viewBean = (COContentieuxViewBean) session.getAttribute("contentieuxViewBean");
	selectedIdValue = viewBean.getIdContentieux();

	String idSection = request.getParameter("idSection");

	idEcran = "GCO0002";

	bButtonNew = false;
	bButtonDelete = false;

%>
<script language="JavaScript" src="<%=request.getContextPath()%>/aquilaRoot/javascript/aquila.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/aquilaRoot/theme/aquila.css">
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<script language="javascript">
	function add() {
	    document.forms[0].elements('userAction').value="aquila.poursuite.contentieux.ajouter"
	}

	function upd() {
	}

	function validate() {
	    state = validateFields();
	    if (document.forms[0].elements('_method').value == "add")
	        document.forms[0].elements('userAction').value="aquila.poursuite.contentieux.ajouter";
	    else
	        document.forms[0].elements('userAction').value="aquila.poursuite.contentieux.modifier";

	    return state;

	}

	function cancel() {

	if (document.forms[0].elements('_method').value == "add")
	  document.forms[0].elements('userAction').value="back";
	 else
	  document.forms[0].elements('userAction').value="aquila.poursuite.contentieuxAVS.afficher";
	}

	function del() {
	    if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?")){
	        document.forms[0].elements('userAction').value="aquila.poursuite.contentieux.supprimer";
	        document.forms[0].submit();
	    }
	}

	function init(){
	<% if (viewBean.isNew()) {%>
	document.forms[0].elements('_method').value = "add";
	<%}%>
	}
</script>
<jsp:include flush="true" page="../menuChange.jsp"/>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>
<% if (viewBean != null && !viewBean.getIdContentieux().equals("-1")) {%>
	<span class="postItIcon">
	<ct:FWNote sourceId="<%=viewBean.getIdContentieux()%>" tableSource="<%=viewBean.getTableName()%>"/>
	</span>
<% } %>
Detail eines Dossiers<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
<tr>
	<td>
		<table>
								<jsp:include flush="true" page="../headerContentieux.jsp"/>
			<tr>
				<td colspan="8"><br /><hr noshade size="3"><br /></td>
			</tr>
			<tr>
				<td class="label">Sequenztyp</td>
				<td class="control" colspan="3">
					<input type="text" value="<%=viewBean.getSequence().getLibSequenceLibelle()%>" class="disabled" style="width: 100%" readonly>
					<input type=hidden name=idSequence value="<%=viewBean.getSequence().getIdSequence()%>">
				</td>
				<td class="control" colspan="4">Nächstes Auslösungsdatum&nbsp;&nbsp;<ct:FWCalendarTag name="prochaineDateDeclenchement" doClientValidation="CALENDAR" value="<%=viewBean.getProchaineDateDeclenchement()%>"/></td>
			</tr>

			<tr>
				<td class="label">Suspendiert</td>
				<td class="control" colspan="3">
					<input type="checkbox" <%=viewBean.estSuspendu() ? "checked=\"checked\"" : ""%> disabled>
					&nbsp;bis am&nbsp;
					<input type="text" value="<%=viewBean.getDateFinSuspendu()%>" class="dateDisabled" readonly>
				</td>
				<td class="label">Dossier erstellt durch</td>
				<td class="control"><input type="text" name="user" value="<%=viewBean.getUser()==""?viewBean.getSession().getUserId():viewBean.getUser()%>" class="disabled" readonly></td>				
				<td class="label"></td>
				<td class="control"></td>
			</tr>
			<tr>
				<td class="label">Grund</td>
				<td class="control"><input type="text" value="<%=viewBean.getMotifSuspendu().getCodeUtiLib()%>" class="disabled" readonly></td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>				
				<td class="label">Eröffnungsdatum</td>
				<td class="control"><input name="dateOuverture" type="text" value="<%=globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getDateOuverture())?globaz.globall.util.JACalendar.todayJJsMMsAAAA():viewBean.getDateOuverture()%>" class="dateDisabled" readonly></td>
				<td class="label">Gebührenbetrag</td>
				<td class="control"><input type="text" value="<%=viewBean.getMontantTotalTaxesFormatte()%>" class="montantDisabled" readonly></td>
			</tr>

			<tr>
				<td class="label">Bemerkung</td>
				<td class="control" colspan="7"><textarea name="remarque"><%=viewBean.getRemarque()%></textarea></td>
			</tr>
		</table>
	</td>
</tr>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>