<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ page import="globaz.aquila.db.batch.COTransitionEditViewBean"%>
<%
	COTransitionEditViewBean viewBean = (COTransitionEditViewBean) session.getAttribute("viewBean");

	selectedIdValue = viewBean.getIdTransition();
	idEcran = "GCO0022";
%>
<%@ taglib uri="/WEB-INF/aquila.tld" prefix="co"%>
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/aquilaRoot/theme/aquila.css">
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CO-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="CO-OptionsDefaut"/>

<script language="JavaScript">

	function add() {
	    document.forms[0].elements('userAction').value="aquila.batch.transitionEdit.ajouter";
	}

	function upd() {}

	function validate() {
	    state = validateFields();
	    if (document.forms[0].elements('_method').value == "add")
	        document.forms[0].elements('userAction').value="aquila.batch.transitionEdit.ajouter";
	    else
	        document.forms[0].elements('userAction').value="aquila.batch.transitionEdit.modifier";

	    return state;

	}

	function cancel() {
        document.forms[0].elements('userAction').value="aquila.batch.etape.afficher";
        document.forms[0].elements('selectedId').value="<%=viewBean.getIdEtapeRetour()%>";
        document.forms[0].elements('_method').value="";
	}

	function del() {
	    if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?")){
	        document.forms[0].elements('userAction').value="aquila.batch.transitionEdit.supprimer";
	        document.forms[0].submit();
	    }
	}

	function init(){}

</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Änderung einer Etappe<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD>
								Anfangsetappe

								<INPUT type="hidden" name="idEtapeRetour" value="<%=viewBean.getIdEtapeRetour()%>">
							</TD>
							<TD>
								<%@page import="globaz.aquila.db.access.batch.COEtape" %>
								<% if (viewBean.isVersEtapeRetour()) { %>
								<ct:select name="idEtape">
									<ct:forEach items="<%=viewBean.getEtapes()%>" var="etape">
									<% COEtape etape = (COEtape) pageContext.getAttribute("etape"); %>
									<OPTION value="<%=etape.getIdEtape()%>"<%=etape.getIdEtape().equals(viewBean.getIdEtape())?" selected":""%>>
										<%=etape.getLibEtapeLibelle()%>
									</OPTION>
									</ct:forEach>
								</ct:select>
								<% } else { %>
								<ct:inputText name="dummyEtape" defaultValue="<%=viewBean.getEtape().getLibEtapeLibelle()%>" styleClass="libelleLong disabled" disabled="true"/>								<ct:inputHidden name="idEtape" defaultValue="<%=viewBean.getIdEtape()%>"/>
								<% } %>
							</TD>
						</TR>
						<TR>
							<TD>Ankunftsetappe</TD>
							<TD>
								<% if (viewBean.isVersEtapeRetour()) { %>
								<ct:inputText name="dummyEtape" defaultValue="<%=viewBean.getEtapeSuivante().getLibActionLibelle()%>" styleClass="libelleLong disabled" disabled="true"/>
								<ct:inputHidden name="idEtape" defaultValue="<%=viewBean.getIdEtapeSuivante()%>"/>
								<% } else { %>
								<ct:select name="idEtapeSuivante">
									<ct:forEach items="<%=viewBean.getEtapes()%>" var="etape">
									<% COEtape etape = (COEtape) pageContext.getAttribute("etape"); %>
									<OPTION value="<%=etape.getIdEtape()%>"<%=etape.getIdEtape().equals(viewBean.getIdEtapeSuivante())?" selected":""%>>
										<%=etape.getLibActionLibelle()%>
									</OPTION>
									</ct:forEach>
								</ct:select>
								<% } %>
							</TD>
						</TR>
						<TR>
							<TD>Frist</TD>
							<TD>
								<ct:inputText
									name="duree"
									defaultValue="<%=viewBean.getDuree()%>"
									styleClass="numeroCourt alignRight"
									onchange="validateIntegerNumber(this)"
									onkeypress="return filterCharForInteger(window.event);"/>
								<ct:select name="genreDelai" defaultValue="<%=viewBean.getGenreDelai()%>">
									<ct:optionsCodesSystems csFamille="COGENRDEL"/>
								</ct:select>
							</TD>
						</TR>
						<TR>
							<TD>Paritätische Frist</TD>
							<TD>
								<ct:inputText
									name="dureeParitaire"
									defaultValue="<%=viewBean.getDureeParitaire()%>"
									styleClass="numeroCourt alignRight"
									onchange="validateIntegerNumber(this)"
									onkeypress="return filterCharForInteger(window.event);"/>
								<ct:select name="genreDelaiParitaire" defaultValue="<%=viewBean.getGenreDelaiParitaire()%>" wantBlank="true">
									<ct:optionsCodesSystems csFamille="COGENRDEL"/>
								</ct:select>
							</TD>
						</TR>
						<TR>
							<TD>Aktion</TD>
							<TD><ct:inputText name="transitionAction" defaultValue="<%=viewBean.getTransitionAction()%>" styleClass="libelleLong"/></TD>
						</TR>
						<TR>
							<TD>Snippet</TD>
							<TD>
								<ct:select name="formSnippet">
								<co:COForEachResource var="ressource" uri="/aquilaRoot/FR/batch/snippets" stripPath="true">
								<% String ressource = (String) pageContext.getAttribute("ressource"); %>
									<OPTION label="<%=ressource%>"<%=ressource.endsWith(viewBean.getFormSnippet()) ? " selected" : ""%>>
										<%=ressource%>
									</OPTION>
								</co:COForEachResource>
								</ct:select>
							</TD>
						</TR>
						<TR>
							<TD>Automatische Ausfürung gestattet</TD>
							<TD>
								<INPUT type="checkbox" name="auto" value="on"<%=viewBean.isAuto() ? " checked" : ""%>>
							</TD>
						</TR>
						<TR>
							<TD>Priorität automatische Ausfürung</TD>
							<TD colspan="3">
								<ct:inputText
									name="priorite"
									defaultValue="<%=viewBean.getPriorite()%>"
									styleClass="numeroCourt alignRight"
									onchange="validateIntegerNumber(this)"
									onkeypress="return filterCharForInteger(window.event);"/>
							</TD>
						</TR>
						<TR>
							<TD>Manuelle Ausfürung gestattet</TD>
							<TD>
								<INPUT type="checkbox" name="manuel" value="on"<%=viewBean.isManuel() ? " checked" : ""%>>
							</TD>
						</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>