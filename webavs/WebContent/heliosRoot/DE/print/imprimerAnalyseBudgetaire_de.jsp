<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ page import="globaz.helios.db.comptes.*,globaz.helios.db.print.*,globaz.helios.db.interfaces.*,globaz.globall.db.*" %>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
	idEcran="GCF2001";
	//Récupération du viewBean
	CGImprimerAnalyseBudgetaireViewBean viewBean = (CGImprimerAnalyseBudgetaireViewBean)session.getAttribute ("viewBean");

	//Récupération de l'exercice comptable
	CGExerciceComptable exerciceComptable = (CGExerciceComptable) session.getAttribute(CGNeedExerciceComptable.SESSION_EXERCICECOMPTABLE);

	//Label utilisé pour spécifier à l'utilisateur qu'aucune option n'est sélectionnée.
	String labelAucun = "Aucun";
	if (languePage.equalsIgnoreCase("de")) {
		labelAucun = "Keine";
	}

	userActionValue = "helios.print.imprimerAnalyseBudgetaire.executer";

	String toutLexercice = "Tout l'exercice";
	if (languePage.equalsIgnoreCase("de")) {
		toutLexercice = "Ganzes Rechnungsjahr";
	}

%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CG-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="CG-OnlyDetail"/>

<%
	globaz.framework.menu.FWMenuBlackBox bb = (globaz.framework.menu.FWMenuBlackBox) session.getAttribute(globaz.framework.servlets.FWServlet.OBJ_USER_MENU);
	bb.setNodeOpen(false, "parameters", "CG-MenuPrincipal");
%>

<script>
function init() { }
function onOk() {
	document.forms[0].submit();
}
function onCancel() {
	document.forms[0].elements('userAction').value="back";
}

function onCompteFailure(event) {
	//si touche different de [DEL] ou [BACKSPACE]
	if(event.keyCode != 8 && event.keyCode != 46) {
		alert(" Le compte n'existe pas.");
	}
}
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="GCF2001_ANALYSEBUDGETAIRE_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
		<tr>
			<td><ct:FWLabel key="GCF20XX_CRITERE_MANDAT"/></td>
			<td> <input name='libelle' class='libelleLongDisabled' readonly value='<%=exerciceComptable.getMandat().getLibelle()%>'> <input type="hidden" name="idExerciceComptable" value="<%=exerciceComptable.getIdExerciceComptable()%>"></td>
		</tr>
		<tr>
			<td><ct:FWLabel key="GCF20XX_CRITERE_EXERCICE"/></td>
			<td> <input name='libelle' class='libelleLongDisabled' readonly value='<%=exerciceComptable.getFullDescription()%>'>
				<input name='idMandat' type="hidden" value='<%=exerciceComptable.getIdMandat()%>'>
			</td>
		</tr>
		<tr>
			<td><ct:FWLabel key="GCF20XX_CRITERE_EMAIL"/></td>
			<td> <input name='eMailAddress' class='libelleLong' value='<%=viewBean.getEMailAddress()==null?"":viewBean.getEMailAddress()%>'> * </td>
		</tr>
		<tr>
			<td><ct:FWLabel key="GCF20XX_CRITERE_COMPTABILITE"/></td>
			<td> <ct:FWCodeSelectTag name="idComptabilite" defaut="<%=viewBean.getIdComptabilite()%>" codeType="CGPRODEF" /></td>
		</tr>

		<%
			globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
			globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();

			int autoCompleteStart = globaz.helios.parser.CGAutoComplete.getAutoCompleteAutoStart();

			String jspLocation = servletContext + "/heliosRoot/compte_select.jsp";
			String params = "idExerciceComptable=" + exerciceComptable.getIdExerciceComptable() + "&isMandatAVS=" + exerciceComptable.getMandat().isEstComptabiliteAVS();
		%>

		<tr>
			<td><ct:FWLabel key="GCF2001_ANALYSEBUDGETAIRE_COMPTEDE"/></td>
			<td>
			<ct:FWPopupList name="compteDe"
				onFailure="onCompteFailure(window.event);"
				validateOnChange="true"
				params="<%=params%>"
				value=""
				className="libelle"
				jspName="<%=jspLocation%>"
				minNbrDigit="1"
				autoNbrDigit="<%=autoCompleteStart%>"
				forceSelection="true"/>

			&nbsp;<ct:FWLabel key="GCF2001_ANALYSEBUDGETAIRE_COMPTEA"/>&nbsp;

			<ct:FWPopupList name="compteA"
				onFailure="onCompteFailure(window.event);"
				validateOnChange="true"
				params="<%=params%>"
				value=""
				className="libelle"
				jspName="<%=jspLocation%>"
				minNbrDigit="1"
				autoNbrDigit="<%=autoCompleteStart%>"
				forceSelection="true"/>
			</td>
		</tr>

		<%if (!globaz.helios.translation.CGListes.getExerciceComptableListe(session, exerciceComptable.getIdMandat(), null, exerciceComptable.getIdExerciceComptable()).isEmpty()) {%>
		<tr>
			<td><ct:FWLabel key="GCF2001_ANALYSEBUDGETAIRE_COMPAREREXERCICE"/></td>
			<td><ct:FWListSelectTag name="idExerciceForComparaison" defaut="<%=viewBean.getIdPeriodeComptable()%>" data="<%=globaz.helios.translation.CGListes.getExerciceComptableListe(session, exerciceComptable.getIdMandat(), null, exerciceComptable.getIdExerciceComptable())%>"/> </td>
		</tr>
		<%}%>

		<tr>
			<td><ct:FWLabel key="GCF20XX_CRITERE_PERIODECOMPTABLE"/></td>
			<td><ct:FWListSelectTag name="idPeriodeComptable" defaut="<%=viewBean.getIdPeriodeComptable()%>" data="<%=globaz.helios.translation.CGListes.getPeriodeComptableListe(session, toutLexercice)%>"/> </td>
		</tr>

		<tr>
			<td><ct:FWLabel key="GCF20XX_CRITERE_PERIODEPRECEDENTE"/></td>
			<TD><input type="checkbox" name="inclurePeriodePrecedente" <%=(viewBean.isInclurePeriodePrecedente().booleanValue())?"CHECKED":""%>></TD>
		</tr>

		<tr>
			<td><ct:FWLabel key="GCF20XX_CRITERE_DOMAINECOMPTES"/></td>
			<td> <ct:FWCodeSelectTag name="idDomaine" defaut="<%=viewBean.getIdDomaine()%>" codeType="CGDOMCPT" except="<%=CGComptePertesProfitsListViewBean.getExcept()%>" /></td>
		</tr>

		<tr>
			<td><ct:FWLabel key="GCF20XX_CRITERE_CENTRECHARGE"/></td>
			<td> <ct:FWListSelectTag name="idCentreCharge" defaut="<%=viewBean.getIdCentreCharge()%>" data="<%=globaz.helios.translation.CGListes.getCentreChargeListe(session, labelAucun)%>"/> </td>
		</tr>
		<tr>
			<td><ct:FWLabel key="GCF20XX_CRITERE_TYPEIMPRESSION"/></td>
			<td>
				<input type="radio" name="typeImpression" id="pdf" value="<%=globaz.helios.itext.list.utils.CGImpressionUtils.TYPE_IMPRESSION_PDF%>" <%="pdf".equals(viewBean.getTypeImpression()) ? "checked='checked'" : "" %>/><label for="pdf">PDF</label>&nbsp;
				<input type="radio" name="typeImpression" id="excel" value="<%=globaz.helios.itext.list.utils.CGImpressionUtils.TYPE_IMPRESSION_XLS%>" <%="xls".equals(viewBean.getTypeImpression()) ? "checked='checked'" : "" %>/><label for="excel">Excel</label>
			</td>
		</tr>		
		<tr>
			<td><ct:FWLabel key="GCF20XX_CRITERE_PRINT_PAGEGARDE"/></td>
           	<TD><input type="checkbox" name="imprimerPageGarde" checked="checked" /></TD>
        </tr>
		<tr>
			<td><ct:FWLabel key="GCF20XX_CRITERE_PRINT_TITRE"/></td>
           	<TD><input type="checkbox" name="imprimerTitres" checked="checked" /></TD>
          </tr>
		<tr>
			<td><ct:FWLabel key="GCF20XX_CRITERE_PRINT_COMPTE"/></td>
            <TD><input type="checkbox" name="imprimerComptes" checked="checked" /></TD>
        </tr>
	    <tr>
	    	<td><ct:FWLabel key="GCF20XX_CRITERE_CLASSIFICATION"/></td>
	    	<td>
	    		<select id="niveauClassification" name="niveauClassification">
	    			<option value=0>0</option>
	    			<option value=1 selected="selected">1</option>
	    			<option value=2>2</option>
	    			<option value=3>3</option>
	    			<option value=4>4</option>
	    			<option value=5>5</option>
	    			<option value=6>6</option>
	    			<option value=7>7</option>
	    			<option value=8>8</option>
	    			<option value=9>9</option>
	    		</select>
	    	</td>
	    </tr>
	    <tr>
			<td><ct:FWLabel key="GCF20XX_CRITERE_DEFINITIONLISTE"/></td>
			<td>
				<ct:FWListSelectTag name="idDefinitionListe" defaut="<%=globaz.helios.db.utils.CGUtils.getDefautIdDefinitionListe(viewBean.getSession(), exerciceComptable)%>" data="<%=globaz.helios.translation.CGListes.getDefinitionListeListe(session)%>" /> *
				<script>
				function updateIFrame() {
					document.all['classeCompteIFrame'].src = "<%=servletContext%><%=((String)request.getAttribute("mainServletPath")+"Root")%>/<%=globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session)%>/comptes/classeCompte_select.jsp?idDefinitionListe="+document.all['idDefinitionListe'].value;
				}
				document.all['idDefinitionListe'].onchange = updateIFrame;
				</script>
				<input type="hidden" name="classesCompteList" value="">
			</td>
		</tr>
		<tr>
			<td></td>
			<td>
			<iframe id="classeCompteIFrame" src="<%=servletContext%><%=((String)request.getAttribute("mainServletPath")+"Root")%>/<%=globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session)%>/comptes/classeCompte_select.jsp?idDefinitionListe=<%=viewBean.getIdDefinitionListe()%>" width=500px height=200px></iframe>
			</td>
		</tr>
		<script>updateIFrame();</script>
<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<script>
	$("#btnOk").click(function() {
		$("#btnOk").attr('disabled', 'disabled');
	});
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>