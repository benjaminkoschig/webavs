<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@page import="globaz.hercule.db.controleEmployeur.CEAffilieForAttrPts"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored ="false" %>
<%@ include file="/theme/detail/header.jspf"%>
<%-- tpl:put name="zoneInit" --%>

<%
	idEcran = "CCE0002";
	CEGestionAttributionPtsViewBean viewBean = (CEGestionAttributionPtsViewBean) request.getAttribute("viewBean");
	
	if(!viewBean.isAttributionActive()) {
		bButtonUpdate = false;
	}
	bButtonDelete = false;
	
	boolean isFromControle = false;
	String redirectAnnulation = "";
	String numAffilieFromControle = request.getParameter("numAffilie");
	String idAffilieFromControle = request.getParameter("idAffilie");
	
	if("add".equals(request.getParameter("_method")) && !JadeStringUtil.isEmpty(idAffilieFromControle)){
		viewBean.setNumAffilie(numAffilieFromControle);
		viewBean.setIdControle(request.getParameter("idControle"));
		viewBean.setIdAffilie(idAffilieFromControle);
		viewBean._getAffilieForAttribution();
		isFromControle = true;
		redirectAnnulation =  formAction + "?userAction=hercule.controleEmployeur.controleEmployeur.afficher&selectedId="+request.getParameter("idControle");
	}
	
	String idControle = viewBean.getIdControle();
	String redirectControle = formAction + "?userAction=hercule.controleEmployeur.controleEmployeur.afficher&selectedId="+idControle;
	
	String redirectAffilie = servletContext + "/naos?userAction=naos.affiliation.autreDossier.modifier&numAffilie=" + viewBean.getNumAffilie();

	String idCompteAnnexe = viewBean._getIdCompteAnnexe();
	String redirectCompteAnnexe = servletContext + "/osiris?userAction=osiris.comptes.apercuComptes.afficher&id=" + idCompteAnnexe;
	
	String actif = "";
    if(viewBean.isAttributionActive()) {
    	actif = "<img title=\"Actif\" src=\"" + request.getContextPath()+"/images/ok.gif\" />";
    } else {
    	actif = "<img title=\"Inactif\" src=\"" + request.getContextPath()+"/images/verrou.gif\" />";
    }
%>

<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.hercule.db.controleEmployeur.CEGestionAttributionPts"%>
<%@page	import="globaz.hercule.db.controleEmployeur.CEGestionAttributionPtsViewBean"%>
<%@page import="globaz.hercule.db.controleEmployeur.CEAffilieForAttrPtsManager"%>

<%-- /tpl:put --%><%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf"%>
<%-- tpl:put name="zoneScripts" --%>

<script type="text/javascript" src="<%=servletContext%>/scripts/widget/globazwidget.js"></script>

<link rel="stylesheet" type="text/css" href="<%=servletContext%>/theme/widget.css" />

<SCRIPT language="JavaScript">
top.document.title = "Web@AVS - <ct:FWLabel key='CONTROLE_EMPLOYEUR'/>";

var MAIN_URL = "<%=formAction%>";

function add() {
	document.forms[0].elements('userAction').value="hercule.controleEmployeur.gestionAttributionPts.ajouter";
}

function upd() {
	$("#collaboration").attr("disabled", true);
	document.forms[0].elements('userAction').value="hercule.controleEmployeur.gestionAttributionPts.modifier";
}

function validate() {
	state = validateFields();
   	if (document.forms[0].elements('_method').value == "add")
      	document.forms[0].elements('userAction').value="hercule.controleEmployeur.gestionAttributionPts.ajouter";
  	 else
  		document.forms[0].elements('userAction').value="hercule.controleEmployeur.gestionAttributionPts.modifier";
   
   	return state;
}

function cancel() {
	$("#collaboration").attr("disabled", true);
	
	if (document.forms[0].elements('_method').value == "add") {
		document.forms[0].elements('userAction').value="back";
	} else {
		document.forms[0].elements('userAction').value="hercule.controleEmployeur.gestionAttributionPts.afficher";
	}

}

function del() {}

function init(){
	<%
		String infoTiers = ""; 
		if(!JadeStringUtil.isBlank(viewBean.getNumAffilie())){ %>
			$('#widgetNumeroAffilie').val('<%=viewBean.getNumAffilie()%>');
	<%  } %>
}

function postInit(){
	$("#collaboration").attr("disabled", true);
	if (document.forms[0].elements('_method').value == "add") {
		<% if(!isFromControle) {%>
			$('#btnVal').attr("disabled", true);
		<% } else {%>
			$('#derniereRevision').focus();
		<%} %>
	}
	
	$('.dataFixe').css('font-weight','bold');
	$('.dataFixe').css('font-family','Verdana,Arial');
}

$(document).ready(function() {

	<% if(isFromControle) {%>
		$('#btnCan').click(function() {
			window.location.href = '<%=redirectAnnulation%>';			
		});		
	<% } %>
	
	$('.textLimit').keypress(function() {
		var $this = $(this);
		if($this.val().length > 254) {
			var result = $this.val().substr(0, 254);
			$this.text(result);
		}
	})
	
});

</SCRIPT>


<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf"%>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="TITRE_ECRAN_ATTRIBUTION_POINT"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf"%>
<%-- tpl:put name="zoneMain" --%>

	<input type="hidden" name="idTiers" id="idTiers" value="<%=viewBean.getIdTiers()%>" />
	<INPUT type="hidden" name="periodeDebut" id="periodeDebut" value="<%=viewBean.getDateDebutControle()%>"/>
	<INPUT type="hidden" name="periodeFin" id="periodeFin" value="<%=viewBean.getDateFinControle()%>"/>
	<input type="hidden" name="isModificationUtilisateur" id="isModificationUtilisateur" value="<%=viewBean.isModificationUtilisateur()%>"/>
	<input type="hidden" name="oldIdAttributionPts" id="oldIdAttributionPts" value="<%=viewBean.getIdAttributionPts()%>"/>	
	<input type="hidden" name="selectedId" id="selectedId" value="<%=viewBean.getIdAttributionPts()%>"/>	

	
		<TR>
		<td colspan="2">
			<table border="0" cellspacing="0" cellpadding="0" width="<%=subTableWidth%>">
				<tr>
					<td><ct:FWLabel key="NUMERO_AFFILIE"/></td>
					<td><ct:widget name="widgetNumeroAffilie" id="widgetNumeroAffilie" styleClass="libelleLong">
							<ct:widgetManager managerClassName="<%=CEAffilieForAttrPtsManager.class.getName()%>" defaultSearchSize="20">
								<ct:widgetCriteria criteria="likeNumeroAffilie" label="AFFILIE" />
								<ct:widgetLineFormatter format="#{numAffilie} - #{nom} (#{dateDebutAffiliation} - #{dateFinAffiliation}) #{dateDebutPeriodeControle} - #{dateFinPeriodeControle} (#{typeAffiliationLabel})" />
								<ct:widgetJSReturnFunction>
									<script type="text/javascript">
										function(element){			
											$('#widgetNumeroAffilie').val($(element).attr('numAffilie'));
											$('#idAffilie').val($(element).attr('idAffiliation'));
											$('#numAffilie').val($(element).attr('numAffilie'));
											$('#dateDebutControle').val($(element).attr('dateDebutPeriodeControle'));
											$('#dateFinControle').val($(element).attr('dateFinPeriodeControle'));
											$('#periodeDebut').val($(element).attr('dateDebutPeriodeControle'));
											$('#periodeFin').val($(element).attr('dateFinPeriodeControle'));
											$('#datePrevue').val($(element).attr('dateEffective'));
											$('#idControle').val($(element).attr('idControle'));
											$('#infoTiers').val($(element).attr('nom') + '\n' + $(element).attr('dateDebutAffiliation') + ' - ' + $(element).attr('dateFinAffiliation'));	
											$('#numAffilieExterne').val($(element).attr('numAffilieExterne'));
											$('#btnVal').attr('disabled', false);
										}
									</script>
								</ct:widgetJSReturnFunction>
							</ct:widgetManager>
						</ct:widget> 
						<ct:inputHidden name="idAffilie" id="idAffilie" /> 
						<ct:inputHidden name="numAffilie" id="numAffilie" />
						<ct:inputHidden name="idControle" id="idControle" />
						&nbsp;&nbsp;
						<% if(!JadeStringUtil.isEmpty(viewBean.getNumAffilie())) { %>
							<A href="<%=redirectAffilie%>" class="external_link"><ct:FWLabel key="AFFILIE"/></A>&nbsp;&nbsp;
						<% }%>                                  
						<% if(!JadeStringUtil.isEmpty(idCompteAnnexe)) { %>
							/&nbsp;&nbsp;<A href="<%=redirectCompteAnnexe%>" class="external_link"><ct:FWLabel key="COMPTE"/></A>
						<% }%>
					</td>
				</tr>
				<TR>
				<% if(JadeStringUtil.isEmpty(idControle)) { %>
					<TD>&nbsp;</TD>
				<% } else {%>
					<TD><A href="<%=redirectControle%>"><ct:FWLabel key="CONTROLE"/></A></TD>
				<% }%>
					<TD>
						<TEXTAREA name="infoTiers" id="infoTiers" cols="45" rows="5" style="overflow: auto; background-color: #b3c4db;" readonly="readonly" tabindex="-1"><%=viewBean.getInfoTiers()%></TEXTAREA>
					</TD>
				</TR>
			</table>
		</td>
		<td  colspan="2">
			<table border="0" cellspacing="0" cellpadding="0" width="<%=subTableWidth%>">
				<tr>
					<td><ct:FWLabel key="HERCULE_JSP_CCE0002_BRA_ECO" /></td>
					<td nowrap><span class='dataFixe'><%=viewBean._getBrancheEcoLibelle()%></span></td>
				</tr>
				<tr>
					<td><ct:FWLabel key="HERCULE_JSP_CCE0002_CODE_NOGA" /></td>
					<td><span class='dataFixe'><%=viewBean._getCodeNogaLibelle()%></span></td>
				</tr>
				<tr>
					<td><ct:FWLabel key="HERCULE_JSP_CCE0002_NUM_AFF_EXTERNE" /></td>
					<td><span class='dataFixe'><%=viewBean.getNumAffilieExterne()%></span></td>
				</tr>
				<c:if test="${viewBean.reviseurSuva}">
				<tr>
					<td><ct:FWLabel key="HERCULE_JSP_CCE0002_CODE_SUVA" /></td>
					<td><span class='dataFixe'><%=viewBean._getCodeLibelleSuva()%></span></td>
				</tr>
				</c:if>
			</table>
		</td>
	</TR>
	
	
	<TR>
		<TD colspan="4"><HR></HR></TD>
	</TR>
	<tr>
		<TD align="left"><ct:FWLabel key="ATTRIBUTION_POINT_DERNIERE_PERIODE_CONTROLEE"/></TD>
		<TD>
			<INPUT name="dateDebutControle" id="dateDebutControle" class="disabled" value="<%=viewBean.getDateDebutControle()%>" readonly="readonly" size="10" tabindex="-1" />
			 -
			<INPUT name="dateFinControle" id="dateFinControle" class="disabled" value="<%=viewBean.getDateFinControle()%>" readonly="readonly" size="10" tabindex="-1" />
			/
			<INPUT name="datePrevue" id="datePrevue" class="disabled" value="<%=viewBean.getDateEffective()%>" readonly="readonly" size="10" tabindex="-1" />
		</TD>

		<TD align="left">&nbsp;</TD>
		<% if(viewBean.isAttributionActive()) { %>
			<TD align="left"><ct:FWLabel key="ACTIF"/>&nbsp;&nbsp;<%=actif%></TD>
		<% } else {%>
			<TD align="left"><ct:FWLabel key="INACTIF"/>&nbsp;&nbsp;<%=actif%></TD>
		<% } %>

	</tr>
	<TR>
		<TD colspan="4"><HR></HR></TD>
	</TR>
	<tr>
		<td colspan="2"> 
			<table border="0" cellspacing="0" cellpadding="0" width="<%=subTableWidth%>">
			
				<tr>
					<TD align="left"><ct:FWLabel key="ATTRIBUTION_POINT_RESULTAT_DERNIERE_REVISION"/></TD>
				</tr>
				<tr>
					<TD><ct:FWCodeSelectTag name="derniereRevision"	defaut="<%=viewBean.getDerniereRevision()%>" wantBlank="<%=false%>" codeType="AFCEDERNRE"/></TD>
					<td><textarea class="textLimit" name="derniereRevisionCom" cols="50" rows="3" ><%=viewBean.getDerniereRevisionCom()%></textarea></td>
				</tr>
				<tr>
					<TD align="left"><ct:FWLabel key="ATTRIBUTION_POINT_QUALITE_RESSOURCE_HUMAINE"/></TD>
				</tr>
				<tr>
					<TD align="left"><ct:FWCodeSelectTag name="qualiteRH" defaut="<%=viewBean.getQualiteRH()%>" wantBlank="<%=false%>" codeType="AFCEQUALRH"/></TD>
					<td><TEXTAREA class="textLimit" rows=3 COLS=50 name="qualiteRHCom"><%=viewBean.getQualiteRHCom()%></TEXTAREA></td>
				</tr>
				<tr>
					<TD align="left"><ct:FWLabel key="ATTRIBUTION_POINT_COLLABORATION"/></TD>
				</tr>
				<tr>
					<TD align="left"><ct:FWCodeSelectTag name="collaboration" defaut="<%=viewBean.getCollaboration()%>" wantBlank="<%=false%>" codeType="AFCECOLLAB" /></TD>
					<td><TEXTAREA class="textLimit" rows=3 COLS=50 name="collaborationCom"><%=viewBean.getCollaborationCom()%></TEXTAREA></td>
				</tr>
				<tr>
					<TD align="left"><ct:FWLabel key="ATTRIBUTION_POINT_CRITERE_SPECIFIQUE_ENTREPRISE" /></TD>
				</tr>
				<tr>
					<TD align="left"><ct:FWCodeSelectTag name="criteresEntreprise" defaut="<%=viewBean.getCriteresEntreprise()%>" wantBlank="<%=false%>" codeType="AFCECRIENT" /></TD>
					<td><TEXTAREA class="textLimit" rows=3 COLS=50 name="criteresEntrepriseCom"><%=viewBean.getCriteresEntrepriseCom()%></TEXTAREA></td>
				</tr>
			
			</table>
		</td>
		<td colspan="2">
			<table border="0" cellspacing="0" cellpadding="0" width="<%=subTableWidth%>">
				<tr>
					<td colspan="3"><ct:FWLabel key="HERCULE_JSP_CCE0002_MASSE_SALARIALE" /></td>
					<td colspan="2"><ct:FWLabel key="HERCULE_JSP_CCE0002_NB_CI" /></td>
				</tr>
				<tr>
					<td colspan="5">&nbsp;</td>
				</tr>
				<tr>
					<td align="left"><%=viewBean._getAnneeN0()%></td>
					<td align="right"><span class='dataFixe'><%=viewBean._getMasseSalarialeN0()%></span></td>
					<td >&nbsp;&nbsp;</td>
					<td align="left"><%=viewBean._getAnneeN0()%></td>
					<td align="right"><span class='dataFixe'><%=viewBean._getNombreCIN0()%></span></td>
				</tr>
				<tr>
					<td align="left"><%=viewBean._getAnneeN1()%></td>
					<td align="right"><span class='dataFixe'><%=viewBean._getMasseSalarialeN1()%></span></td>
					<td >&nbsp;&nbsp;</td>
					<td align="left"><%=viewBean._getAnneeN1()%></td>
					<td align="right"><span class='dataFixe'><%=viewBean._getNombreCIN1()%></span></td>
				</tr>
				<tr>
					<td align="left"><%=viewBean._getAnneeN2()%></td>
					<td align="right"><span class='dataFixe'><%=viewBean._getMasseSalarialeN2()%></span></td>
					<td >&nbsp;&nbsp;</td>
					<td align="left"><%=viewBean._getAnneeN2()%></td>
					<td align="right"><span class='dataFixe'><%=viewBean._getNombreCIN2()%></span></td>
				</tr>
				<tr>
					<td align="left"><%=viewBean._getAnneeN3()%></td>
					<td align="right"><span class='dataFixe'><%=viewBean._getMasseSalarialeN3()%></span></td>
					<td >&nbsp;&nbsp;</td>
					<td align="left"><%=viewBean._getAnneeN3()%></td>
					<td align="right"><span class='dataFixe'><%=viewBean._getNombreCIN3()%></span></td>
				</tr>
				<tr>
					<td align="left"><%=viewBean._getAnneeN4()%></td>
					<td align="right"><span class='dataFixe'><%=viewBean._getMasseSalarialeN4()%></span></td>
					<td >&nbsp;&nbsp;</td>
					<td align="left"><%=viewBean._getAnneeN4()%></td>
					<td align="right"><span class='dataFixe'><%=viewBean._getNombreCIN4()%></span></td>
				</tr>
				<tr>
					<td colspan="5">&nbsp;</td>
				</tr>
				<tr>
					<td colspan="5">&nbsp;</td>
				</tr>
				<tr>
					<td colspan="3"><ct:FWLabel key="HERCULE_JSP_CCE0002_MASSE_AVS_REPRISE" /></td>
					<td colspan="2"><ct:FWLabel key="HERCULE_JSP_CCE0002_NB_CI_REPRIS" /></td>
				</tr>
				<tr>
					<td colspan="5">&nbsp;</td>
				</tr>
				<tr>
					<td align="left"><%=viewBean._getAnneeN0()%></td>
					<td align="right"><span class='dataFixe'><%=viewBean._getMasseAvsRepriseN0()%></span></td>
					<td >&nbsp;&nbsp;</td>
					<td align="left"><%=viewBean._getAnneeN0()%></td>
					<td align="right"><span class='dataFixe'><%=viewBean._getNombreCIReprisN0()%></span></td>
				</tr>
				<tr>
					<td align="left"><%=viewBean._getAnneeN1()%></td>
					<td align="right"><span class='dataFixe'><%=viewBean._getMasseAvsRepriseN1()%></span></td>
					<td >&nbsp;&nbsp;</td>
					<td align="left"><%=viewBean._getAnneeN1()%></td>
					<td align="right"><span class='dataFixe'><%=viewBean._getNombreCIReprisN1()%></span></td>
				</tr>
				<tr>
					<td align="left"><%=viewBean._getAnneeN2()%></td>
					<td align="right"><span class='dataFixe'><%=viewBean._getMasseAvsRepriseN2()%></span></td>
					<td >&nbsp;&nbsp;</td>
					<td align="left"><%=viewBean._getAnneeN2()%></td>
					<td align="right"><span class='dataFixe'><%=viewBean._getNombreCIReprisN2()%></span></td>
				</tr>
				<tr>
					<td align="left"><%=viewBean._getAnneeN3()%></td>
					<td align="right"><span class='dataFixe'><%=viewBean._getMasseAvsRepriseN3()%></span></td>
					<td >&nbsp;&nbsp;</td>
					<td align="left"><%=viewBean._getAnneeN3()%></td>
					<td align="right"><span class='dataFixe'><%=viewBean._getNombreCIReprisN3()%></span></td>
				</tr>
				<tr>
					<td align="left"><%=viewBean._getAnneeN4()%></td>
					<td align="right"><span class='dataFixe'><%=viewBean._getMasseAvsRepriseN4()%></span></td>
					<td >&nbsp;&nbsp;</td>
					<td align="left"><%=viewBean._getAnneeN4()%></td>
					<td align="right"><span class='dataFixe'><%=viewBean._getNombreCIReprisN4()%></span></td>
				</tr>
		 	</table>
		</td>
	</tr>
	

	<TR>
		<TD colspan="4"><HR></HR></TD>
	</TR>
	<tr>
		<TD align="left"><ct:FWLabel key="ATTRIBUTION_POINT_POINT_RISQUE"/></TD>
		<TD align="left"><INPUT name="nbrePoints" id="nbrePoints" class="disabled" readonly="readonly" align="center" value="<%=viewBean.getNbrePoints()%>" size="2" tabindex="-1"/></TD>
		<TD align="left"><ct:FWLabel key="CVS"/></TD>
		<TD align="left">
			<INPUT class="disabled" readonly="readonly" align="center" value="<%=viewBean._getCategorieMasseSalariale()%>" size="1" tabindex="-1"/>
			<INPUT class="disabled" readonly="readonly" style="text-align : right" align="right" value="<%=viewBean._getMasseSalariale()%>" tabindex="-1"/>
		</TD>
	</tr>
	<tr>
		<TD align="left"><ct:FWLabel key="ATTRIBUTION_POINT_PERIODE_PREVUE"/></TD>
		<TD align="left">
		<INPUT name="periodePrevueDebut" value="<%=viewBean.getPeriodePrevueDebut()%>" class="disabled" readonly="readonly" align="center" size="10" tabindex="-1"/>
		-
		<INPUT name="periodePrevueFin" value="<%=viewBean.getPeriodePrevueFin()%>" class="disabled" readonly="readonly" align="center" size="10" tabindex="-1"/>
		/
		<INPUT name="anneePrevue" class="disabled" readonly="readonly" align="center" value="<%=viewBean._getAnneeDeControle()%>" size="4" tabindex="-1"/>
		</TD>
		<TD align="left"><ct:FWLabel key="CVS_FIN_CONTROLE"/></TD>
		<TD align="left">
			<INPUT class="disabled" readonly="readonly" align="center" value="<%=viewBean._getCategorieMasseSalarialeFinControle()%>" size="1" tabindex="-1"/>
			<INPUT class="disabled" readonly="readonly" style="text-align : right" align="right" value="<%=viewBean._getMasseSalarialeFinControle()%>" tabindex="-1"/>
		</TD>
	</tr>

	<tr height="25">
		<TD align="left"><ct:FWLabel key="HERCULE_JSP_CCE0002_REVISEUR" /></TD>
		<TD align="left"><span class='dataFixe'><%=viewBean.getNomReviseur()%></span></TD>
		<TD align="left"><ct:FWLabel key="HERCULE_JSP_CCE0002_TPS_CONTROLE" /></TD>
		<TD align="left"><span class='dataFixe'><%=viewBean.getTempsJour()%> <ct:FWLabel key="HERCULE_JSP_CCE0002_JOURS" /></span></TD>
	</tr>
	<TR>
		<TD colspan="4"><HR></HR></TD>
	</TR>
	<tr>
		<td colspan="2"> 
			<table border="0" cellspacing="0" cellpadding="0" width="<%=subTableWidth%>">
				<tr>
					<td align="left"><ct:FWLabel key="COMMENTAIRE"/></td>
					<td><TEXTAREA class="textLimit" rows=3 COLS=50 name="commentaires"><%=viewBean.getCommentaires()%></TEXTAREA></td>
				</tr>
				<tr>
					<td align="left"><ct:FWLabel key="OBSERVATION_GENERALE"/></td>
					<td><TEXTAREA class="textLimit" rows=3 COLS=50 name="observations"><%=viewBean.getObservations()%></TEXTAREA></td>
				</tr>
				<tr>
					<td height="20" align="left"><ct:FWLabel key="GENRE_CONTROLE"/></td>
					<td><span class='dataFixe'><%=viewBean.getLibelleGenreControle()%></span></td>
				</tr>
			</table>
		</td>
		<td colspan="2"> 
			<table border="0" cellspacing="0" cellpadding="0" width="<%=subTableWidth%>">
				<tr>
					<td align="left"><ct:FWLabel key="HERCULE_JSP_CCE0002_TOT_FACTURE" /></td>
					<td align="right"><span class='dataFixe'><%=viewBean._getTotaFacture()%></span></td>
				</tr>
				<tr>
					<td align="left"><ct:FWLabel key="HERCULE_JSP_CCE0002_DATE_FACTURE" /></td>
					<td align="right"><span class='dataFixe'><%=viewBean._getDateFacturation()%></span></td>
				</tr>
			</table>
		</td>	
	</tr>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf"%>
<%-- tpl:put name="zoneButtons" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf"%>
<%-- tpl:put name="zoneEndPage" --%>

<ct:menuChange displayId="menu" menuId="CE-MenuPrincipal"/>
<ct:menuChange displayId="options" menuId="CE-OptionsControlEmployeurAttributionPts" showTab="options">
		<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getNumAffilie()%>"/>
		<ct:menuSetAllParams key="numAffilie" value="<%=viewBean.getNumAffilie()%>"/>
</ct:menuChange>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf"%>
<%-- /tpl:insert --%>