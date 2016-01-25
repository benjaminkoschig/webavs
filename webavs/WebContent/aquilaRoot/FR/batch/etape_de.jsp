<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ page import="globaz.aquila.db.batch.COEtapeViewBean"%>
<%
	COEtapeViewBean viewBean = (COEtapeViewBean) session.getAttribute("viewBean");

	selectedIdValue = viewBean.getIdEtape();
	idEcran = "GCO0021";
%>
<%@ taglib uri="/WEB-INF/aquila.tld" prefix="co"%>
<LINK id="aquilaCSS" rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/aquilaRoot/theme/aquila.css">
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CO-MenuPrincipal"/>
<ct:menuChange displayId="options" menuId="CO-OptionsEtapes" showTab="options">
   	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdEtape()%>"/>
   	<ct:menuSetAllParams key="forIdEtape" value="<%=viewBean.getIdEtape()%>"/>
   	<ct:menuSetAllParams key="forIdEtapeSuivante" value="<%=viewBean.getIdEtape()%>"/>
   	<ct:menuSetAllParams key="forIdSequence" value="<%=viewBean.getIdSequence()%>"/>
   	<ct:menuSetAllParams key="forLibEtape" value="<%=viewBean.getLibEtape()%>"/>
   	<ct:menuSetAllParams key="forLibAction" value="<%=viewBean.getLibAction()%>"/>
   	<ct:menuSetAllParams key="forLibSequence" value="<%=viewBean.getSequence().getLibSequence()%>"/>
</ct:menuChange>

<script language="JavaScript">

	function add() {
	    document.forms[0].elements('userAction').value="aquila.batch.etape.ajouter";
	}

	function upd() {}

	function validate() {
	    state = validateFields();
	    if (document.forms[0].elements('_method').value == "add")
	        document.forms[0].elements('userAction').value="aquila.batch.etape.ajouter";
	    else
	        document.forms[0].elements('userAction').value="aquila.batch.etape.modifier";

	    return state;

	}

	function cancel() {

	if (document.forms[0].elements('_method').value == "add")
	  document.forms[0].elements('userAction').value="aquila.batch.etape.chercher";
	 else
	  document.forms[0].elements('userAction').value="aquila.batch.etape.chercher";

	}

	function del() {
	    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
	        document.forms[0].elements('userAction').value="aquila.batch.etape.supprimer";
	        document.forms[0].submit();
	    }
	}

	function effacerTransition(champIdTransition) {
		var idTransition = getIdTransition(champIdTransition);

		if (idTransition == "") {
			alert("Sélectionnez une transition auparavant");
		} else {
		    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné ! Voulez-vous continuer ?")) {
				document.location.href = '<%=formAction%>?userAction=aquila.batch.transitionEdit.supprimer&idTransition='+idTransition+'&idEtape=<%=viewBean.getIdEtape()%>';
		    }
	    }
	}

	function actionTransition(action, champIdTransition, paramIdEtape) {
		var idTransition = getIdTransition(champIdTransition);

		if (idTransition == "") {
			alert("Sélectionnez une transition auparavant");
		} else {
			document.location.href = '<%=formAction%>?userAction=aquila.batch.transitionEdit.'+action+'&selectedId='+idTransition+'&'+paramIdEtape+'=<%=viewBean.getIdEtape()%>';
		}
	}

	function getIdTransition(champIdTransition) {
		var radios = document.forms[0].elements(champIdTransition);

		if (!isFinite(radios.length)) {
			// il n'y a qu'un seul bouton radio, dans ce cas on obtiens la valeur avec value directement
			return radios.value;
		}

		// on recherche le bouton sélectionné dans la liste des boutons
		var id;

		for (id = 0; id < radios.length; ++id) {
			if (radios[id].checked) {
				return radios[id].value;
			}
		}

		// il n'y a pas de bouton sélectionné, on retourne vide
		return "";
	}

	function nouvelleTransition(paramIdEtape) {
		document.location.href='<%=formAction%>?userAction=aquila.batch.transitionEdit.afficher&_method=add&'+paramIdEtape+'=<%=viewBean.getIdEtape()%>'
	}

	function init(){}

</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Modification d'une étape<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
					<TR><TD>
					<TABLE width="100%">
						<TR>
							<TD>Séquence</TD>
							<TD>
								<% if (viewBean.isSequenceModifiable()) { %>
								<ct:select name="idSequence">
									<%@page import="globaz.aquila.db.batch.COSequenceViewBean"%>
									<ct:forEach items="<%=viewBean.getSequences()%>" var="sequence">
										<% COSequenceViewBean sequenceViewBean = (COSequenceViewBean) pageContext.getAttribute("sequence"); %>
										<OPTION value="<%=sequenceViewBean.getIdSequence()%>"><%=sequenceViewBean.getLibSequenceLibelle()%></OPTION>
									</ct:forEach>
								</ct:select>
								<% } else { %>
								<ct:inputText name="dummySequence" defaultValue="<%=viewBean.getLibSequenceLibelle()%>" styleClass="disabled" readonly="true"/>
								<% } %>
							</TD>
							<TD>Montant minimum d'exécution</TD>
							<TD>
								<ct:inputText
									name="montantMinimal"
									styleClass="montant"
									onchange="validateFloatNumber(this)"
									onkeypress="return filterCharForFloat(window.event);"/>
							</TD>
						</TR>
						<TR>
							<TD>Etape</TD>
							<TD colspan="3">
								<ct:select name="libEtape">
									<ct:optionsCodesSystems csFamille="COETAPP"/>
								</ct:select>
							</TD>
						</TR>
						<TR>
							<TD>Action</TD>
							<TD colspan="3">
								<ct:select name="libAction">
									<ct:optionsCodesSystems csFamille="COETAEP"/>
								</ct:select>
							</TD>
						</TR>
						<TR>
							<TD>Type d'étape</TD>
							<TD>
								<ct:select name="typeEtape" wantBlank="true">
									<ct:optionsCodesSystems csFamille="COTYPEETAP"/>
								</ct:select>
							</TD>
							<TD colspan="2">
								<input id="ignorerDateExecution" type="checkbox" value="true" name="ignorerDateExecution" <%=viewBean.getIgnorerDateExecution().booleanValue()?"checked":""%>>
								<label for="ignorerDateExecution">Ignorer la date d'exécution de cette étape</label>
							</TD>
						</TR>
					</TABLE>
					<H5>Transitions depuis cette étape</H5>
					<TABLE class="pseudoRcList">
						<THEAD>
							<TR>
								<TH class="row"></TH>
								<TH class="title">Etape suivante</TH>
								<TH class="title">Délai</TH>
								<TH class="title">Genre délai</TH>
								<TH class="title">Manuel</TH>
								<TH class="title">Auto.</TH>
								<TH class="title">Priorité</TH>
							</TR>
						</THEAD>
						<%@page import="globaz.aquila.db.access.batch.COTransition"%>
						<TBODY>
							<ct:forEach items="<%=viewBean.getTransitionsDepuis()%>" var="transition">
							<% COTransition transitionDepuis = (COTransition) pageContext.getAttribute("transition"); %>
							<TR class="<co:COEvenOddAlternate even="row" odd="rowOdd"/>">
								<TD align="center">
									<INPUT type="radio" name="idTransitionDepuis" value="<%=transitionDepuis.getIdTransition()%>">
								</TD>
								<TD>
									<A href="<%=formAction%>?userAction=aquila.batch.etape.afficher&selectedId=<%=transitionDepuis.getIdEtapeSuivante()%>">
										<%=transitionDepuis.getEtapeSuivante().getLibActionLibelle()%>
									</A>
								</TD>
								<TD align="right"><%=transitionDepuis.getDuree()%></TD>
								<td><%=transitionDepuis.getGenreDelaiWithStepTransitionDepuis()%></td>
								<TD align="center"><co:COTrueFalseImg flag="<%=transitionDepuis.getManuel()%>"/></TD>
								<TD align="center"><co:COTrueFalseImg flag="<%=transitionDepuis.getAuto()%>"/></TD>
								<TD align="right"><%=transitionDepuis.getPriorite()%></TD>
							</TR>
							</ct:forEach>
						</TBODY>
					</TABLE>
				<ct:ifhasright element="aquila.batch.transitionEdit.afficher" crud="c">
					<INPUT type="button" value="Ajouter..." onclick="nouvelleTransition('idEtape')">
				</ct:ifhasright>
				<ct:ifhasright element="aquila.batch.transitionEdit" crud="u">
					<INPUT type="button" value="Modifier..." onclick="actionTransition('afficher', 'idTransitionDepuis', 'idEtape')">
				</ct:ifhasright>
				<ct:ifhasright element="aquila.batch.transitionEdit.supprimer" crud="d">
					<INPUT type="button" value="Effacer" onclick="effacerTransition('idTransitionDepuis')">
				</ct:ifhasright>
					<H5>Transitions vers cette étape</H5>
					<TABLE class="pseudoRcList">
						<THEAD>
							<TR>
								<TH class="row"></TH>
								<TH class="title">Etape précédante</TH>
								<TH class="title">Délai</TH>
								<TH class="title">Genre délai</TH>
								<TH class="title">Manuel</TH>
								<TH class="title">Auto.</TH>
								<TH class="title">Priorité</TH>
							</TR>
						</THEAD>
						<TBODY>
							<ct:forEach items="<%=viewBean.getTransitionsVers()%>" var="transition">
							<% COTransition transitionVers = (COTransition) pageContext.getAttribute("transition"); %>
							<TR class="<co:COEvenOddAlternate even="row" odd="rowOdd"/>">
								<TD align="center">
									<INPUT type="radio" name="idTransitionVers" value="<%=transitionVers.getIdTransition()%>">
								</TD>
								<TD>
									<A href="<%=formAction%>?userAction=aquila.batch.etape.afficher&selectedId=<%=transitionVers.getIdEtape()%>">
										<%=transitionVers.getEtape().getLibEtapeLibelle()%>
									</A>
								</TD>
								<TD align="right"><%=transitionVers.getDuree()%></TD>
								<TD><%=transitionVers.getGenreDelaiWithStepTransitionVers(viewBean.getLibEtapeLibelle())%></TD>
								<TD align="center"><co:COTrueFalseImg flag="<%=transitionVers.getManuel()%>"/></TD>
								<TD align="center"><co:COTrueFalseImg flag="<%=transitionVers.getAuto()%>"/></TD>
								<TD align="right"><%=transitionVers.getPriorite()%></TD>
							</TR>
							</ct:forEach>
						</TBODY>
					</TABLE>
				<ct:ifhasright element="aquila.batch.transitionEdit.afficher" crud="c">
					<INPUT type="button" value="Ajouter..." onclick="nouvelleTransition('idEtapeSuivante')">
				</ct:ifhasright>
				<ct:ifhasright element="aquila.batch.transitionEdit" crud="u">
					<INPUT type="button" value="Modifier..." onclick="actionTransition('afficher', 'idTransitionVers', 'idEtapeSuivante')">
				</ct:ifhasright>
				<ct:ifhasright element="aquila.batch.transitionEdit.supprimer" crud="d">
					<INPUT type="button" value="Effacer" onclick="effacerTransition('idTransitionVers')">
				</ct:ifhasright>
					<P></P>
					</TD></TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>