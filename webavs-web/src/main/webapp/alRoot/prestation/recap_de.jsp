<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="globaz.fweb.util.JavascriptEncoder"%>
<%@page import="ch.globaz.al.business.constantes.ALCSPrestation"%>
<%@page	import="ch.globaz.al.business.models.processus.ProcessusPeriodiqueModel"%>
<%@page import="globaz.al.vb.prestation.ALRecapViewBean"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1"%>
<%@page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
<%@include file="/theme/detail_ajax/header.jspf"%>
<%
	idEcran="AL0017";
	ALRecapViewBean viewBean = (ALRecapViewBean) session.getAttribute("viewBean");
	
	//Contrôle des droits sur la page
	final String pageRight = "al.prestation.recap";
	boolean hasUpdateRight = objSession.hasRight(pageRight, FWSecureConstants.UPDATE);
	boolean hasDeleteRight = objSession.hasRight(pageRight, FWSecureConstants.REMOVE);
	
	//Mise à jour de l'affichage des boutons selon les droits de l'utlisateur et l'état de la récupatilation
	bButtonUpdate = hasUpdateRight;
	bButtonDelete = hasDeleteRight;
	
	String rootPath = servletContext+(mainServletPath+"Root");
%>
<%@ include file="/theme/detail_ajax/javascripts.jspf"%>

<link rel="stylesheet" type="text/css" href="<%=servletContext%>/theme/widget.css" />
<script type="text/javascript" src="<%=servletContext%>/scripts/widget/globazwidget.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/ajax/DefaultTableAjax.js" /></script>
<script type="text/javascript" src="<%=rootPath %>/scripts/prestation/RecapPart.js"></script>
<SCRIPT type="text/javascript" src="<%=servletContext%>/scripts/params.js"></SCRIPT>
<SCRIPT type="text/javascript" src="<%=servletContext%>/scripts/actionsForButtons.js"></SCRIPT>
<SCRIPT type="text/javascript" src="<%=servletContext%>/scripts/utils.js"></SCRIPT>
<script type="text/javascript" src="<%=servletContext%>/alRoot/util_webaf.js"></script>
<script type="text/javascript" src="<%=servletContext%>/alRoot/ajax_webaf.js"></script>
<script type="text/javascript">
	globazGlobal.ACTION_AJAX = "al.prestation.recapAjax";
	globazGlobal.ID_RECAP_MODEL = ${viewBean.recapModel.idRecap};
	globazGlobal.MSG_DELETE = "<%=JavascriptEncoder.getInstance().encode(objSession.getLabel("MESSAGE_SUPPRESSION"))%>";
	globazGlobal.ACTION_GED = "<%=servletContext + mainServletPath%>?userAction=al.prestation.recap.executer&printGed="+"inGed";
	globazGlobal.MESSAGE_GENDOSSIER_NOTID = "<%= JavascriptEncoder.getInstance().encode(objSession.getLabel("MESSAGE_GENDOSSIER_NOTID"))%>";
	globazGlobal.ACTION_ADD_LINK = '<%=servletContext + mainServletPath + "?userAction=al.prestation.generationDossier.afficher&_method=add&idDossier="%>';
</script>

<%@ include file="/theme/detail_ajax/bodyStart.jspf"%>
<span style="float: left; text-align: left;">
<c:choose>
	<c:when test="${viewBean.recapVerrouillee}">
		<img src="images/cadenas.gif"/>
	</c:when>
	<c:otherwise>
		<img src="images/cadenas_ouvert.gif"/>
	</c:otherwise>
</c:choose>
</span>
<ct:FWLabel key="AL0017_TITRE" />${viewBean.recapModel.id}
<%@ include file="/theme/detail_ajax/bodyStart2.jspf"%>
<input name="selectedId" type="hidden" value="${viewBean.recapModel.idRecap}" />
<tr>
	<td>
		<table id="AL0017recapZone" class="zone">
			<tr>
				<td class="subtitle" colspan="5"><ct:FWLabel key="AL0017_TITRE_ZONE_RECAP" /></td>
			</tr>
			<tr>
				<td><ct:FWLabel key="AL0017_NUM_RECAP" /></td>
				<td><ct:inputText name="recapModel.id" readonly="readonly" styleClass="readOnly medium" disabled="true" /></td>
				<td colspan="2"></td>
				<td><input type="text" value="<ct:FWCodeLibelle csCode="${viewBean.recapModel.etatRecap}"/>(<%= objSession.getCode(viewBean.getRecapModel().getEtatRecap()) %>)" class="readOnly" readonly="readonly" disabled="disabled" /></td>
			</tr>
			<tr>
				<td><ct:FWLabel key="AL0017_RECAP_PERIODE" /></td>
				<td><ct:inputText name="recapModel.periodeDe" readonly="readonly" styleClass="readOnly medium" disabled="true" /></td>
				<td><ct:inputText name="recapModel.periodeA" readonly="readonly" styleClass="readOnly medium" disabled="true" /></td>
				<td><ct:FWLabel key="AL0017_NUM_FACTURE" /></td>
				<td><ct:inputText name="recapModel.numeroFacture" readonly="readonly" styleClass="readOnly medium" disabled="true" /></td>
			</tr>
		</table>
		<table id="AL0017affilieZone" class="zone">
			<tr>
				<td class="subtitle" colspan="2"><ct:FWLabel key="AL0017_TITRE_ZONE_AFFILIE" /></td>
			</tr>
			<tr>
				<td><ct:FWLabel key="CRITERIA_NOM_AFFILIE" /></td>
				<td><ct:inputText name="affilie.raisonSociale" readonly="readonly" styleClass="readOnly long" disabled="true" /></td>
			</tr>
			<tr>
				<td><ct:FWLabel key="AL0017_RECAP_NUM_AFFILIE" /></td>
				<td><ct:inputText name="recapModel.numeroAffilie" readonly="readonly" styleClass="readOnly medium" disabled="true" /></td>
			</tr>
			<tr>
				<td>
					<!--<ct:FWLabel key="AL0017_RECAP_NOM_AFFILIE"/>-->
				</td>
				<td>
					<!--<ct:inputText name="recapModel.nomAffilie" defaultValue="TODO" readonly="readonly" styleClass="readOnly long" disabled="true"/>	-->
				</td>
			</tr>
		</table>
		<table id="AL0017processusZone" class="zone">
			<tr>
				<td class="subtitle" colspan="2"><ct:FWLabel key="AL0017_TITRE_ZONE_PROCESSUS" /></td>
			</tr>
			<tr>
				<td><ct:FWLabel key="AL0017_RECAP_OPTION_PROCESSUS" /></td>
				<td>
					<c:choose>
						<c:when test="${viewBean.recapModel.saisie}">
							<c:choose>
								<c:when test="${viewBean.sizeProcessusSelectableList>0}">
									<%int i=0;%>
									<c:set var="selected" value="false" />
									<c:set var="alreadySelected" value="false" />
									
									<select name="recapModel.idProcessusPeriodique"	id="numProcessus" size="5" style="height: auto;" disabled="disabled">
								
									<c:forEach var="currentProcessus" items="${viewBean.processusSelectableList}">
										<c:set var="selected" value="false"></c:set>
										<c:choose>
											<c:when test="${(empty viewBean.recapModel.idProcessusPeriodique || viewBean.recapModel.idProcessusPeriodique == 0) && !currentProcessus.isPartiel}">
												<c:set var="selected" value="true" />
												<c:set var="alreadySelected" value="true" />
											</c:when>
											<c:when test="${currentProcessus.idProcessusPeriodique==viewBean.recapModel.idProcessusPeriodique}">
												<c:set var="selected" value="true" />
												<c:set var="alreadySelected" value="true" />										
											</c:when>
										</c:choose>
										<option ${selected?'selected="selected"':''} 
												${currentProcessus.isPartiel?'':'class="main"'} 
												value="${currentProcessus.isPartiel?currentProcessus.id:'0'}">
												${selected?"-> ":""}<%=viewBean.getDescriptionProcessusSelectable(i) %>
										</option>
									<%i++;%>
									</c:forEach>
									<c:set var="selected" value="${!alreadySelected}" />
									<option ${selected?'selected="selected"':''} value='0'>${selected?'-> ':''}	<ct:FWLabel	key="AL0017_RECAP_DETACHER_PROCESSUS" /></option>
									</select> 
								</c:when>
								<c:otherwise>
									<ct:FWLabel key="AL0017_ZONE_PROCESSUS_AUCUN" />
								</c:otherwise>
							</c:choose>
						</c:when>
						<c:otherwise>
							<input type="text" value="${viewBean.descriptionProcessusLie}" readonly="readonly" class="readOnly max" disabled="true" />
						</c:otherwise>
					</c:choose>
				</td>
				<td colspan="2" width="50%"></td>
			</tr>
		</table>
</tr>
<tr>
	<td colspan="2">
		<div class="area">

			<table class="areaTable" width="100%">
				<thead>
					<tr class="ui-widget-header search">
						<th class="notSortable"></th>
						<th class="notSortable"><ct:FWLabel	key="AL0017_PREST_ENTETE_NOM" /></th>
						<th class="notSortable"><ct:FWLabel key="AL0017_PREST_ENTETE_NSS" /></th>
						<th class="notSortable"><ct:FWLabel key="AL0017_PREST_ENTETE_DEBUTJM" /></th>
						<th class="notSortable"><ct:FWLabel key="AL0017_PREST_ENTETE_DEBUTMM" /></th>
						<th class="notSortable"><ct:FWLabel	key="AL0017_PREST_ENTETE_FINJM" /></th>
						<th class="notSortable"><ct:FWLabel	key="AL0017_PREST_ENTETE_FINMM" /></th>
						<th class="notSortable"><ct:FWLabel	key="AL0017_PREST_ENTETE_NBUNITE" /></th>
						<th class="notSortable"><ct:FWLabel	key="AL0017_PREST_ENTETE_UNITE" /></th>
						<th class="notSortable"><ct:FWLabel	key="AL0017_PREST_ENTETE_NBENF" /></th>
						<th class="notSortable"><ct:FWLabel	key="AL0017_PREST_ENTETE_TAUX" /></th>
						<th class="notSortable"><ct:FWLabel	key="AL0017_PREST_ENTETE_MONTANT" /></th>
						<th class="notSortable"><ct:FWLabel	key="AL0017_PREST_ENTETE_ETAT" /></th>
						<th class="notSortable"></th>
					</tr>
					</tr>
				</thead>
				<tbody>
				</tbody>
				<tfoot>
					<tr>
						<td><!--<c:if test="${viewBean.recapModel.saisieOuProvisoire}">
								<a href="#" class="addLink" id="genererPrestation-link" title="<ct:FWCodeLibelle csCode="LINK_NEW_PRESTATION_DESC"/>" />
							</c:if>-->
						</td>
						<td colspan="10"></td>
						<td style="text-align: right" class="number mtd">${viewBean.montantTotalFormatte}</td>
						<td colspan="2"></td>
					</tr>
				</tfoot>
			</table>
		</div>
	</td>
</tr>


<%@ include file="/theme/detail/bodyButtons.jspf"%>
<input class="btnCtrl" id="btnPrint" type="button" value="Aperçu" onclick="printRecap(false);">
<ct:ifhasright element="<%=userActionNew%>" crud="c">
	<input class="btnCtrl" id="btnSend" type="button" value="Ins. GED" onclick="printRecap(true);">
</ct:ifhasright>

<%@ include file="/theme/detail_ajax/bodyErrors.jspf"%>
<ct:menuChange displayId="menu" menuId="menuWEBAF" showTab="menu"/>
<c:choose>
	<c:when test="${viewBean.nbPrestationsASaisir>0}">
		<ct:menuChange displayId="options" menuId="recap-detail" showTab="options" checkAdd="no">
			<ct:menuSetAllParams key="idRecap" checkAdd="no" value="${viewBean.recapModel.id}"  />		
		</ct:menuChange>
	</c:when>
	<c:otherwise>
		<ct:menuChange displayId="options" menuId="empty-detail"/>	
	</c:otherwise>
</c:choose>
<%@ include file="/theme/detail_ajax/footer.jspf"%>
