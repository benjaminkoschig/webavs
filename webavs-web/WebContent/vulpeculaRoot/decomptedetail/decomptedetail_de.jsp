<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/detail_el/header.jspf" %>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° ecran et titre --%>
<c:set var="idEcran" value="PPT1108"/>
<c:set var="labelTitreEcran" value="JSP_DECOMPTE"/>
<c:set var="employeur" value="${viewBean.employeur}" />
<c:set var="decompte" value="${viewBean.decompte}" />

<c:set var="userActionSalaire" value="vulpecula.decomptesalaire" />
<c:set var="userActionTaxationOffice" value="vulpecula.taxationoffice" />

<c:set var="ETAT_ANNULE" value="${viewBean.ETAT_ANNULE}" />
<c:set var="ETAT_COMPTABILISE" value="${viewBean.ETAT_COMPTABILISE}" />
<c:set var="ETAT_ERREUR" value="${viewBean.ETAT_ERREUR}" />
<c:set var="ETAT_GENERE" value="${viewBean.ETAT_GENERE}" />
<c:set var="ETAT_OUVERT" value="${viewBean.ETAT_OUVERT}" />
<c:set var="ETAT_RECEPTIONNE" value="${viewBean.ETAT_RECEPTIONNE}" />
<c:set var="ETAT_RECTIFIE" value="${viewBean.ETAT_RECTIFIE}" />
<c:set var="ETAT_TAXATION_DOFFICE" value="${viewBean.ETAT_TAXATION_DOFFICE}" />
<c:set var="ETAT_VALIDE" value="${viewBean.ETAT_VALIDE}" />
    
<%-- visibiltés des boutons --%>
<c:set var="bButtonNew" value="false" scope="page" />
<%-- <c:set var="bButtonValidate" value="true" scope="page" /> --%>
<%-- <c:set var="bButtonCancel" value="true" scope="page" /> --%>
<%-- <c:set var="bButtonDelete" value="true" scope="page"/> --%>
<%-- <c:set var="bButtonUpdate" value="true" scope="page" /> --%>

<%--  ********************************************************************** JS CSS ***************************************************************************--%>
<%@ include file="/theme/detail_el/javascripts.jspf" %>
<script type="text/javascript" src="${rootPath}/scripts/json2.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ajax/ajaxUtils.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/vulpeculaUtils.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ajax/AbstractScalableAJAXTableZone.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ajax/AbstractSimpleAJAXDetailZone.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ajax/DefaultTableAjax.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/ajax/templateZoneAjax.css">
<link rel="stylesheet" type="text/css" href="${rootPath}/css/vulpecula.css"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/utilsFormatter.js"></script>
<script type="text/javascript" src="${rootPath}/decomptedetail/decomptedetail_de.js"></script>

<%@ include file="/theme/detail_el/bodyStart.jspf" %>
<span class="postItIcon" data-g-note="idExterne:${decompte.id}, tableSource:PT_DECOMPTES">
</span>
<ct:FWLabel key="${labelTitreEcran}"/>
<%@ include file="/theme/detail_el/bodyStart2.jspf" %>

<script type="text/javascript">
globazGlobal.idDecompte = ${viewBean.decompte.id};
globazGlobal.ligneDecompteService = '${viewBean.ligneDecompteService}';
globazGlobal.libelleBoutonModifier = '${viewBean.boutonModifierDialogLibelle}';
globazGlobal.libelleBoutonRectifier = '${viewBean.boutonRectifierDialogLibelle}';
globazGlobal.libelleDialogDevalider = '${viewBean.dialogDevaliderLibelle}';
globazGlobal.libelleDialogAnnuler = '${viewBean.dialogAnnulerLibelle}';
globazGlobal.messageSuppression = '${viewBean.messageSuppression}';
globazGlobal.decompteService = '${viewBean.decompteService}';
globazGlobal.decompteViewService = '${viewBean.decompteViewService}';
globazGlobal.IS_EDITABLE = ${viewBean.decompte.editable};
globazGlobal.isEdition = ${viewBean.edition};
globazGlobal.echecControler = ${viewBean.echecControler};
globazGlobal.isControlable = ${decompte.controlable};
</script>
<%--  ******************************************************************* Corps de la page ******************************************************************* --%>

<div class="content">
	<div class="blocLeft">
		<%@ include file="/vulpeculaRoot/blocs/decompte.jspf" %>
	</div>
	<div class="blocRight">
		<%@ include file="/vulpeculaRoot/blocs/employeur.jspf" %>
	</div>
	<div>
	<table style="width:100%;">
	<c:if test="${decompte.taxationOffice}">
			<tr>
				<a href="vulpecula?userAction=${userActionTaxationOffice}.afficher&amp;idDecompte=${viewBean.entity.id}"><ct:FWLabel key="JSP_TAXATION_OFFICE"/></a>
			</tr>
	</c:if>	
			<c:if test="${decompte.periodique or decompte.complementaire}">
			<tr>
				<td class="label">
					<label><ct:FWLabel key='JSP_DATE_RAPPEL'/></label>
				</td>
				<td><input type="text" id="dateRappel" name="dateRappel" value="${decompte.dateRappelAsSwissValue}" data-g-calendar=" " /></td>
			</tr>
			<tr>
				<td>
					<label><ct:FWLabel key='JSP_DECOMPTE_MOTIF_PROLONGATION'/></label>
					<td><ct:FWCodeSelectTag name="motifProlongation" codeType="PTMOTIFPR" wantBlank="true" defaut="${viewBean.decompte.motifProlongation.value}"/></td>
				</td>
			</tr>
			<tr>
				<td width="20%">
					<label><ct:FWLabel key='JSP_DECOMPTE_RECU_LE'/></label>
				</td>		   
				<td>
					<input type="text" id="date_reception" name="dateReception" value="${viewBean.dateReception}" data-g-calendar=" " />
				</td>   
			</tr>
		</c:if> 
		<c:if test="${decompte.controleEmployeur}">
			<tr>
				<td width="20%">
					<label><ct:FWLabel key='JSP_RAPPORT_CONTROLE'/></label>
				</td>
				<td>
					<ct:select name="idControle">
						<option value="" />
						<c:forEach var="controleEmployeur" items="${viewBean.controlesEmployeur}">
							<c:choose>
								<c:when test="${decompte.idRapportControle == controleEmployeur.idControle }">
									<option selected="selected" value="${controleEmployeur.idControle}">${controleEmployeur.idControle}, ${controleEmployeur.dateDebutPeriodeControle} - ${controleEmployeur.dateFinPeriodeControle }</option>
								</c:when>
								<c:otherwise>
									<option value="${controleEmployeur.idControle}">${controleEmployeur.idControle}, ${controleEmployeur.dateDebutPeriodeControle} - ${controleEmployeur.dateFinPeriodeControle }</option>
								</c:otherwise>
							</c:choose>
							
						</c:forEach>
					</ct:select>
				</td>
			</tr>	
		</c:if>	
		<c:if test="${decompte.controleEmployeur or decompte.special}">
			<tr>
				<td style="width:20%">
					<label><ct:FWLabel key='JSP_INTERETS_MORATOIRES'/></label>
				</td>
				<td>
					<ct:FWCodeSelectTag name="interetsMoratoires" codeType="OSIIMINMO" defaut="${decompte.interetsMoratoires.value}" wantBlank="true" except="${viewBean.exceptionInteret}" />
				</td>
			</tr>
		</c:if>
		<tr>
			<td>
				<label><ct:FWLabel key='JSP_DECOMPTE_MONTANT_CONTROLE'/></label>
			</td>
			<td>
				<input id="montantControle" name="montantControle" value="${viewBean.montantControle}" type="text" data-g-amount="blankAsZero:false, mandatory:true"> 
				<c:choose><c:when test="${viewBean.pasDeControleDisplay}">
						<label class="aCacher"><input type="checkbox" id="pasDeControle" name="pasDeControle" value="on" checked="checked"><ct:FWLabel key='JSP_PAS_DE_CONTROLE'/></label>
					</c:when>
					<c:otherwise>
						<label class="aCacher"><input type="checkbox" id="pasDeControle" name="pasDeControle" value="on"><ct:FWLabel key='JSP_PAS_DE_CONTROLE'/></label>
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
		<tr>
			<td>
				<label for="controleAC2" ><ct:FWLabel key='JSP_CONTROLE_AC2'/></label>
			</td>
			<td>
				<c:choose>
					<c:when test="${viewBean.controleAC2}">
						<input id="controleAC2" name="controleAC2" type="checkbox" checked="checked">
					</c:when>
					<c:otherwise>
						<input id="controleAC2" name="controleAC2" type="checkbox">
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
	</table>
	</div>
</div>
<!-- ZONE BOUTONS, SURCHARGEES POUR AFFICHER LE BOUTON CONTROLER -->
		<input type="hidden" name="selectedId" value="${selectedIdValue}">
				<input type="hidden" name="userAction" value="${userActionValue}">
				<input type="hidden" name="_method" value="${param['_method']}">
				<input type="hidden" name="_valid" value="${param['_valid']}">
				<input type="hidden" name="_sl" value="">
				<input type="hidden" name="selectorName" value="">
				<input type="hidden" name="controler" value="false" />
			</form>
	 	</div>
		
			</div>
			<c:if test="${not empty creationSpy or not empty lastModification}">
				<div class="lastModification">
						<c:out value="${creationSpy}" /> Update: <c:out value="${lastModification}" />
				</div>	
			</c:if>
			<c:choose>
				<c:when test="${autoShowErrorPopup || !vBeanHasErrors}">
				</c:when>
				<c:otherwise>
				[ <a id=\"showErrorLink\" href=\"javascript:showErrors();\">visualiser les erreurs</a> ]
				</c:otherwise>
			</c:choose>
			<c:if test="${bButtonNew || bButtonUpdate || bButtonDelete || bButtonValidate || bButtonCancel }">
				<div style="background-color=#FFFFFF;height:18;text-align: right;" id="btnCtrlJade">
				
				
					<c:if test="${bButtonNew}">
						<input class="btnCtrl" type="button" id="btnNew" value="${btnNewLabel}" onclick="onClickNew();btnNew.onclick='';hideAllButtons();document.location.href='${actionNew}'" />
					</c:if>
					
					<c:if test="${decompte.editable}">
						<c:if test="${bButtonUpdate}">
							<input class="btnCtrl" id="btnUpd" type="button" value="${btnUpdLabel}" onclick="action(UPDATE);upd();"/>
						</c:if>
					</c:if>

					<c:if test="${decompte.annulable}">
						<input class="btnCtrl" id="btnDel" type="button" value="<ct:FWLabel key="JSP_ANNULER_BTN"/>" onclick="annuler(${viewBean.decompte.id});"/>
					</c:if>

					<c:if test="${bButtonValidate}">
						<input class="btnCtrl" id="btnVal" type="button" value="${btnValLabel}" onclick="if(validate()) action(COMMIT);"/>
					</c:if>

					<c:if test="${bButtonCancel}">
						<input class="btnCtrl" id="btnCan" type="button" value="${btnCanLabel}" onclick="cancel(); action(ROLLBACK);"/>
					</c:if>
					<c:if test="${decompte.controlable}"> 
  						<input class="btnCtrl" id="btnControler" type="button"  value='<ct:FWLabel key="JSP_CONTROLER_BTN"/>' onclick="controler(${viewBean.decompte.id})"/>	
					</c:if>
					<c:if test="${decompte.devalidable}"> 
  						<input class="btnCtrl" id="btnDevalide" type="button"  value='<ct:FWLabel key="JSP_DEVALIDE_BTN"/>' onclick="devalider(${viewBean.decompte.id})"/>	
					</c:if> 
					
					<c:if test="${viewBean.etat == ETAT_ANNULE or viewBean.etat == ETAT_GENERE or viewBean.etat == ETAT_OUVERT}">
						<c:if test="${bButtonDelete}">
							<span style="margin-left: 5em;"><input class="btnCtrl" id="btnDel" type="button" value="${btnDelLabel}" onclick="del();"/></span>
						</c:if>
					</c:if>
				</div>
			</c:if>
<!-- FIN ZONE BOUTONS, SURCHARGEES POUR AFFICHER LE BOUTON CONTROLER -->
<div id="tabs" style="width:100%;">
  <ul>
  	<li><a href="#salaires"><ct:FWLabel key='JSP_SALAIRES'/></a></li>
    <li><a href="#cotisation"><ct:FWLabel key='JSP_COTISATIONS'/></a></li>
    <li><a href="#totalSalaires"><ct:FWLabel key='JSP_TOTAL_SALAIRES'/></a></li>
    <li><a href="#historique"><ct:FWLabel key='JSP_HISTORIQUE'/></a></li>
  </ul>
  <div id="salaires" style="width:100%;">
	  <div class="area">
			<div>
				<table class="areaTable" style="width:100%;">
					<thead>
						<tr>
							<th> </th>
							<th><ct:FWLabel key='JSP_DECOMPTE_NUMERO_POSTE'/></th>
							<th><ct:FWLabel key='JSP_DECOMPTE_TRAVAILLEUR'/></th>
							<th><ct:FWLabel key='JSP_PERIODE'/></th>
							<th><ct:FWLabel key='JSP_DECOMPTE_QUALIFICATION'/></th>
							<c:if test="${viewBean.periodique}">
								<th><ct:FWLabel key='JSP_DECOMPTE_NB_HEURES'/></th>
							</c:if>
							<th><ct:FWLabel key='JSP_DECOMPTE_SALAIRE_HORAIRE'/></th>
							<th><ct:FWLabel key='JSP_DECOMPTE_SALAIRE_TOTAL'/></th>
							<th><ct:FWLabel key='JSP_DECOMPTE_ABSENCES'/></th>
							<th><ct:FWLabel key='JSP_DECOMPTE_TAUX_CONTRIBUABLE'/></th>
						</tr>
						<tr>
							<th></th>
							<th class="notSortable">
								<input style="text-align:center;" id="idPosteTravail" />
							</th>
							<th class="notSortable">
								<input id="nomTravailleur" />
							</th>
							<th></th>
							<th></th>
							<c:if test="${viewBean.periodique}">
								<th></th>
							</c:if>
							<th></th>
							<th></th>
							<th></th>
							<th></th>
						</tr>
			  			<tr><!-- Ajout d'un decompte salaire -->
			  				<td style="text-align: left" colspan="10">
			  					<ct:ifhasright element="${userActionSalaire}" crud="c">
			  					<c:if test="${viewBean.decompte.editable}">
				  					<a href="vulpecula?userAction=${userActionSalaire}.afficher&amp;_method=add&amp;idDecompte=${viewBean.entity.id}" class="ajouterSalaire">
				  						<img style="margin-left:4px;" src="images/amal/view_right.png" />
				  					</a>
				  				</c:if>
			  					</ct:ifhasright>
			  				</td>
			  			</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
		</div>
  </div>
  <div id="cotisation">
  </div>
  
  <div id="totalSalaires">
  </div>
  
  <div id="historique">
	<table>
		<tr>
			<th width="150px"><ct:FWLabel key='JSP_ETAT'/></th>
			<th><ct:FWLabel key='JSP_DATE'/></th>
			<th><ct:FWLabel key='JSP_REMARQUE'/></th>
		</tr>
		
		<c:forEach var="historique" items="${viewBean.historiqueDecompte}">
		<c:choose>
				   <c:when test="${historique.etat.value=='68012006'}">			   
				        <tr>				        
							<td><ct:FWCodeLibelle csCode="${historique.etat.value}"/></td>
							<td><c:out value="${historique.date}" />&nbsp;<c:out value="${historique.heureMinuteSpy}" /></td>
							<td><c:out value="${decompte.remarqueRectification}" /></td>
						</tr>   
				    </c:when>
				    <c:otherwise>
				       <tr>
							<td><ct:FWCodeLibelle csCode="${historique.etat.value}"/></td>
							<td><c:out value="${historique.date}" />&nbsp;<c:out value="${historique.heureMinuteSpy}" /></td>
							<td></td>
						</tr>   
				    </c:otherwise>
		</c:choose>			
		</c:forEach>
		
  	</table>
  </div>
</div>

<div id="dialog-confirm" title='<ct:FWLabel key="JSP_DIALOG_CONTROLE_ATTENTION"/>' style="display: none">

	<p>
<!-- 		<span class="ui-icon ui-icon-alert" ></span> -->
		<ct:FWLabel key="JSP_DIALOG_CONTROLE_ECHEC"/>
	</p>
	<p>
		<ct:FWLabel key="JSP_DIALOG_CONTROLE_MONTANT_CONTROLE"/>
	</p>
	<p>
		<span id="dialogMontantControle">${viewBean.differenceControle.montantControle}</span>
	</p>
	<p>
		<ct:FWLabel key="JSP_DIALOG_CONTROLE_TOTAL_SALAIRES"/>
	</p>
	<p>
		<span id="dialogTotalSalaires">${viewBean.differenceControle.totalContributions}</span>
	</p>
	<p>
		<ct:FWLabel key="JSP_DIALOG_CONTROLE_DIFFERENCE"/>
	</p>
	<p>
		<span id="dialogDifference">${viewBean.differenceControle.difference}</span>
	</p>
	<p>
		<ct:FWLabel key="JSP_DIALOG_CONTROLE_DECISION"/>
	</p>
	<p>
		<ct:FWLabel key="JSP_DIALOG_CONTROLE_REMARQUE"/>
	</p>
	<p>
		<textarea onKeyPress="return ( this.value.length < 5000 );" id="remarqueRectificatif" cols="60" rows="8">${viewBean.differenceControle.remarqueRectification}</textarea>
	</p>
</div>

<ct:menuChange displayId="menu" menuId="vulpecula-menuprincipal" showTab="menu" />
<%--  **************************************************************** Fin Corps de la page ******************************************************************* --%>
				
<%@ include file="/theme/detail_el/bodyErrors.jspf" %>

<%@ include file="/theme/detail_el/footer.jspf" %>