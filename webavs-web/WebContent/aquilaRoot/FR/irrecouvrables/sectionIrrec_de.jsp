<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@page import="globaz.osiris.db.irrecouvrable.CAAmortissementCi"%>
<%@page import="globaz.osiris.db.irrecouvrable.CAKeyPosteContainer"%>
<%@page import="globaz.osiris.db.irrecouvrable.CALigneDePoste"%>
<%@page import="globaz.osiris.db.irrecouvrable.CAPoste"%>
<%@page import="globaz.osiris.db.irrecouvrable.CAIrrecouvrableUtils"%>
<%@page import="java.util.Map"%>
<%@page import="globaz.aquila.db.irrecouvrables.COSectionIrrecViewBean"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="globaz.globall.util.JANumberFormatter"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>


<%
	COSectionIrrecViewBean viewBean = (COSectionIrrecViewBean) session.getAttribute("viewBean");
	idEcran = "GCO0012";
	userActionValue = "aquila.irrecouvrables.sectionIrrec.executer";
	formEncType = "iso-8859-1\" method=\"POST\"";
	okButtonLabel = "Comptabiliser";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<ct:menuChange displayId="menu" menuId="CO-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="CO-OptionsDefaut" showTab="menu"/>

 <style type="text/css" media="screen">
 	.poste_div{
 		background-color: #E6E6E6;
 		height:400px;
 		overflow:auto;
 		margin-top: 5px; 
 		margin-bottom: 10px; 
 		vertical-align: middle;
 	}
 	
 	.totaux_div{
 		background-color: #E6E6E6;
 		height:50px;
 		margin-bottom: 10px; 
 		vertical-align: middle;
 	}
 	
 	.amortissementCi_div{
 		background-color: #E6E6E6;
 		height:150px;
 		overflow:auto;
 		margin-bottom: 5px; 
 		vertical-align: middle;
 	}
 	
 	.poste_div input{
 		text-align: right;
 		width: 120px;
 		/*background-color: #F2F2F2;*/
 	}
 	
 	.totaux_div input{
 		text-align: right;
 		width: 120px;
 		/*background-color: #F2F2F2;*/
 	}
 	
 	.amortissementCi_div .inputMontant{
 		text-align: right;
 		width: 120px;
 		/*background-color: #F2F2F2;*/
 	}
 	
 	.rubriqueIndeterminee{
 		color: red;
 	}
 	
 	.montantPoste{
 		
 	}
 	
 	.ligneCachee{
 		display: none;
 	}
 </style>
 
 <style type="text/css" media="print">
 	#btnOk{
 		display: none;
 	}
 	
 	.ligneCachee{
 		display: table-row;
 	}
 	
 	.img_expand_collapse{
 		display: none;
 	}
 	
 	.notDisplayForPrint{
 		display: none;
 	}
 	
 	.rubriqueIndeterminee{
 		color: red;
 	}
 	
 	.amortissementCi_div{
 		background-color: #E6E6E6;
 		height:50px;
 		margin-bottom: 20px; 
 		vertical-align: middle;
 	}
 	
 	.poste_div input{
 		text-align: right;
 		width: 120px;
 		/*background-color: #F2F2F2;*/
 	}
 	
 	.totaux_div input{
 		text-align: right;
 		width: 120px;
 		/*background-color: #F2F2F2;*/
 	}
 	
 	.amortissementCi_div input{
 		text-align: right;
 		width: 120px;
 		/*background-color: #F2F2F2;*/
 	}
 </style>

<script language="JavaScript">
	$(document).ready(function() {
		var $poste = $("#postes");
		var $amortissementDiv = $("#amortissementDiv");
		
		$("#btnOk").click(function(){
			$(this).attr("disabled","disabled");
		});
		
		// cacher ou afficher les lignes de poste lors du clique sur la ligne parente
		$poste.on("click", ".parent_line", function(){
			var posteId = $(this).closest("tr").attr("id");
			$("[data-for="+posteId+"]").toggleClass("ligneCachee");
			if ($("[data-for="+posteId+"]").hasClass("ligneCachee")) {
				$(this).closest("tr").find(".img_expand_collapse").attr("src","<%=request.getContextPath()%>/images/icon-expand.gif");   
           } else {
        	   $(this).closest("tr").find(".img_expand_collapse").attr("src","<%=request.getContextPath()%>/images/icon-collapse.gif");
           }   
		});
		
		//recalculer les différents montants lors de la modification d'un montant affecté
		$poste.on("blur",".montantAffecte", function(){
			var that = $(this);
			
			//formatter le montant
			var montant = parseFloat(that.val());
			if(isNaN(montant)){
				var montant0 = 0.00
				that.val(montant0.toFixed(2));
			}else{
				that.val(montant.toFixed(2));
			}
			
			
			//recalculer le solde de la ligne qui a été modifiée
			var ligneId = that.attr("id");
			updateLigne(ligneId.split('_')[1]);
			
			//recalculer le total du poste concerné
			var posteId = that.closest("tr").attr("data-for");
			updatePoste(posteId.split('_')[1]);
			
			//recalculer le total de tous les postes
			updateTotaux();
			
			//recalculer les montants d'amortissement si poste de type cot pers
			$currentPoste = $("#"+posteId);
			if($currentPoste.attr("class")=="cotPers"){
				updateMontantAmortissement(posteId.split('_')[1],$currentPoste.attr("annee"));
			}
			
			//activer ou désactiver le bouton ok
			activerDesactiverBtnOk();
		});
		
		$amortissementDiv.on("blur",".amortissementMontant", function(){
			var that = $(this);
			
			//formatter le montant
			var montant = parseFloat(that.val());
			if(isNaN(montant)){
				var montant0 = 0
				that.val(montant0.toFixed(0));
			}else{
				that.val(montant.toFixed(0));
			}
			
			//recalculer solde amortissement
			var amortissementId = that.attr("id");
			updateAmortissement(amortissementId.split('_')[1]);
			
			//activer ou désactiver le bouton ok
			activerDesactiverBtnOk();
		});
		
		//activer/désactiver le bouton en fonction des soldes
		activerDesactiverBtnOk();
	});
	
	function activerDesactiverBtnOk(){
		var desactiver = true;
		var $btnOk = $("#btnOk");
		var $affectationSolde = $("#affectationSolde");
		var $amortissementSoldes = $(".amortissementSolde");
		
		//vérification du solde d'affectation
		if($affectationSolde.val()==0.00){
			desactiver = false;	
		}else{
			desactiver = true;
		}
		
		//vérfication des soldes d'amortissement
		$amortissementSoldes.each(function(index,value){
			var soldeAmortissement = parseFloat($(this).val());
			if(!desactiver && soldeAmortissement<0.00){
				desactiver = true;	
			}
		});
		
		//désactivation si nécessaire
		if(desactiver){
			$btnOk.attr("disabled","disabled");	
		}else{
			$btnOk.removeAttr("disabled");
		}
	}
	
	function updateLigne(ligneId){
		var montantDu = parseFloat($("#ligneMontantDu_"+ligneId).val());
		var montantAffecte = parseFloat($("#ligneMontantAffecte_"+ligneId).val());
		var solde = montantDu-montantAffecte;
		$("#ligneSolde_"+ligneId).val(solde.toFixed(2));
	}
	
	function updatePoste(posteId){
		//calcul du montant affecté du poste
		$lignes = $("tr[data-for='poste_"+posteId+"']");
		var montantAffectePoste = 0.00;
		$lignes.each(function(index,value){
			//$ligne = $lignes[i];
			ligneId = value["id"].split('_')[1];
			var montantAffecteLigne = parseFloat($("#ligneMontantAffecte_"+ligneId).val());
			montantAffectePoste = montantAffectePoste+montantAffecteLigne;
		});
		$("#posteMontantAffecteTotal_"+posteId).val(montantAffectePoste.toFixed(2));
		
		//calcul du solde du poste
		var montantDuPoste = parseFloat($("#posteMontantDuTotal_"+posteId).val());
		var soldePoste = montantDuPoste-montantAffectePoste;
		$("#posteSolde_"+posteId).val(soldePoste.toFixed(2));
	}
	
	function updateMontantAmortissement(posteId, annee){
		//calcul du montant affecté du poste
		$lignes = $("tr[data-for='poste_"+posteId+"']");
		var amortissementTotal = 0.00;
		$lignes.each(function(index,value){
			var amortissementLigne = 0.00;
			ligneId = value["id"].split('_')[1];
			var solde = parseFloat($("#ligneSolde_"+ligneId).val());
			var revenuCi = parseFloat($("#ligneRevenuCi_"+ligneId).val());
			var cotisationAvs = parseFloat($("#ligneCotisationAvs_"+ligneId).val());
			amortissementLigne = solde*revenuCi/cotisationAvs;
			amortissementTotal = amortissementTotal+amortissementLigne;
		});
		
		$amortissementMontant = $("#amortissementMontant_"+annee);
		$amortissementMontant.val(amortissementTotal.toFixed(0));
		updateAmortissement(annee);
	}
	
	function updateTotaux(){
		//calcul du montant affecté total
		var montantAffecteTotal = 0.00;
		$lignesMontantAffecte = $(".montantAffecte");
		$lignesMontantAffecte.each(function(index,value){
			var montantAffecteInput = parseFloat($(this).val());
			montantAffecteTotal += montantAffecteInput;
		});
		$("#fullTotalAffecte").val(montantAffecteTotal.toFixed(2));
		$("#affectationAffecte").val(montantAffecteTotal.toFixed(2));
		
		//calcul solde total
		var montantDuTotal = parseFloat($("#fullTotalDu").val());
		var soldeTotal = montantDuTotal-montantAffecteTotal;
		$("#fullTotalSolde").val(soldeTotal.toFixed(2));
		
		//calcul affectation solde total
		var affectationDisponible = parseFloat($("#affectationDisponible").val());
		var affectationAffecte = parseFloat($("#affectationAffecte").val());
		var affectationSolde = affectationDisponible-affectationAffecte;
		$("#affectationSolde").val(affectationSolde.toFixed(2));
		$("#affectationSoldeToSubmit").val(affectationSolde.toFixed(2));
	}
	
	function updateAmortissement(amortissementId){
		var etatCi = parseFloat($("#amortissementEtatCi_"+amortissementId).val());
		var montantAmortissement = parseFloat($("#amortissementMontant_"+amortissementId).val());
		var solde = etatCi-montantAmortissement;
		$("#amortissementSolde_"+amortissementId).val(solde.toFixed(0));
	}
</script>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Gestion des irrécouvrables<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<!-- Entete -->
						<tr>
							<td colspan="3">
							<div id="entete" name="entete">
								<table>
									<tr>
										<td>Affilié</td>
										<td><INPUT type="text" name="affilie" value="<%=viewBean.getNumeroAffilie()+" - "+viewBean.getDescriptionAffilie() %>" style="width: 600px;" disabled="disabled"></td>
									</tr>
									<tr class="notDisplayForPrint">
										<td>E-mail</td>
										<Td><INPUT type="text" name="email" value="<%=viewBean.getEmail()%>" class="libelleLong"></td>
									</tr>
								</table>
							</div>
							</td>
						</tr>
						
						<!--Affichage des postes -->
						<tr>
							<td>
								<div class="poste_div" id="postes" name="postes">
									<table>
										<tr>
											<th colspan="3"></th>
											<th>Montant dû</th>
											<th>Montant affecté</th>
											<th>Solde</th>
										</tr>
										<% 
										int poste_id = 0;
										int ligne_id=0;
										Map<CAKeyPosteContainer, CAPoste> postesMap = viewBean.getPostesMap();
										for(Map.Entry<CAKeyPosteContainer, CAPoste>entry : postesMap.entrySet()){
											poste_id = poste_id+1;
											CAKeyPosteContainer keyPosteContainer = entry.getKey();
											CAPoste poste = entry.getValue();
										%>
											<tr id="poste_<%=poste_id%>" annee="<%=poste.getAnnee() %>" <%if(CAIrrecouvrableUtils.isRubriqueCotPers(poste.getNumeroRubriqueIrrecouvrable())){%>class="cotPers"<%} %> >
												<td width="20px"><img class="img_expand_collapse parent_line" src="<%=request.getContextPath()%>/images/icon-expand.gif"></td>
												<td class="parent_line <%if(poste.isRubriqueIndetermine()){%>rubriqueIndeterminee<%} %>" <%if(poste.isRubriqueIndetermine()){%>rubriqueIndeterminee<%} %> width="900px"><b><%=poste.getNumeroRubriqueIrrecouvrable()+" - "+poste.getLibelleRubriqueIrrecouvrable()%> <%=(poste.getAnnee().equals(0)) ? " " : " - "+poste.getAnnee()%></b></td>
												<% if(poste.isPosteOnError()){ %>
													<td width="20px"><img src="<%=request.getContextPath()%>/images/dialog-warning.png"></td>
												<%}else{ %>
													<td></td>
												<%} %>
												<td><input type="text" class="montantPoste" id="posteMontantDuTotal_<%=poste_id%>" name="" value="<%=poste.getMontantDuTotal() %>" disabled="disabled"></td>
												<td><input type="text" class="montantPoste" id="posteMontantAffecteTotal_<%=poste_id%>" name="" value="<%=poste.getMontantAffecteTotal()%>" disabled="disabled"></td>
												<td><input type="text" class="montantPoste" id="posteSolde_<%=poste_id%>" name="" value="<%=poste.getMontantDuTotal().subtract(poste.getMontantAffecteTotal())%>" disabled="disabled"></td>
											</tr>
											
											<%
											for(CALigneDePoste ligneDePoste : poste.getLignesDePosteList()){
												ligne_id=ligne_id+1;
											%>
												<tr id="ligne_<%=ligne_id%>" data-for="poste_<%=poste_id%>" class="ligneCachee">
													<td></td>
													<td>&nbsp;&nbsp;<%=ligneDePoste.getNumeroSection()+" "+ligneDePoste.getLibelleSection()%></td>
													<% if(ligneDePoste.getMessageErreurList().size()>0){ %>
														<td width="20px">
															<div id="erreur_<%=ligne_id%>">
																<%for(String message : ligneDePoste.getMessageErreurList()){%> 
																	<%="- "+message+"<br><br>" %>						
																<%}%>
															</div>
															<img data-g-bubble="wantMarker:false,title:¦Erreur(s)¦,type:hover,id:¦<%="erreur_"+ligne_id%>¦" src="<%=request.getContextPath()%>/images/dialog-warning.png">
														</td>
													<%}else{ %>
														<td></td>
													<%} %>
													<td><input type="text" id="ligneMontantDu_<%=ligne_id%>" name="" class="montantDu" value="<%=ligneDePoste.getMontantDu()%>" disabled="disabled"></td>
													<td><input type="text" id="ligneMontantAffecte_<%=ligne_id%>" name="<%="ligneMontantAffecte_"+keyPosteContainer.getStringValue("_")+"_"+ligneDePoste.getIdSection() %>" class="montantAffecte" value="<%=ligneDePoste.getMontantAffecte()%>"></td>
													<td><input type="text" id="ligneSolde_<%=ligne_id%>" name="" class="montantSolde" value="<%=ligneDePoste.getMontantDu().subtract(ligneDePoste.getMontantAffecte()) %>" disabled="disabled"></td>
													<td>
														<input type="hidden" id="ligneRevenuCi_<%=ligne_id%>" name="" value="<%=ligneDePoste.getRevenuCi()%>">
														<input type="hidden" id="ligneCotisationAvs_<%=ligne_id%>" name="" value="<%=ligneDePoste.getCotisationAvs()%>">
													</td>
												</tr>
											<%} %>
										<%} %>
									</table>
								</div>
							</td>
						</tr>
						
						<!-- Totaux -->
						<tr>
							<td>
								<div class="totaux_div">
									<table>
										<tr>
											<td width="20px"><img src="<%=request.getContextPath()%>/images/fleche_selection2.gif"></td>
											<td width="900px"><b>Total</b></td>
											<td><input type="text" id="fullTotalDu" name="fullTotalDu" value="<%=viewBean.getMontantDuTotal() %>" disabled="disabled"></td>
											<td><input type="text" id="fullTotalAffecte" name="fullTotalAffecte" value="<%=viewBean.getMontantAffecteTotal() %>" disabled="disabled"></td>
											<td><input type="text" id="fullTotalSolde" name="fullTotalSolde" value="<%=viewBean.getSoldeTotal() %>" disabled="disabled"></td>
										</tr>
										<tr>
											<td></td>
											<td><b>Affectation</b></td>
											<td><input type="text" id="affectationDisponible" name="affectationDisponible" value="<%=viewBean.getAffectationDisponible() %>" disabled="disabled"></td>
											<td><input type="text" id="affectationAffecte" name="affectationAffecte" value="<%=viewBean.getAffectationAffecte() %>" disabled="disabled"></td>
											<td>
												<input type="text" id="affectationSolde" name="affectationSolde" value="<%=viewBean.getAffectationSolde() %>" disabled="disabled">
												<input type=hidden id="affectationSoldeToSubmit" name="affectationSoldeToSubmit" value="<%=viewBean.getAffectationSolde() %>">
											</td>
										</tr>
									</table>
								</div>
							</td>
						</tr>
						
						<!-- Amortissement CI -->
						<tr>
							<td>
								<%if(viewBean.getIdCompteIndividuelAffilie()!=null){ %>
								<span><input type="checkbox" id="wantTraiterAmortissementCi" name="wantTraiterAmortissementCi" checked="checked" style="" /> Effectuer l'extourne</span>
								<span>
									<ct:ifhasright element="pavo.compte.ecriture.chercherEcriture" crud="r">	
										<a href="<%=request.getContextPath()%>/pavo?userAction=pavo.compte.ecriture.chercherEcriture&compteIndividuelId=<%= viewBean.getIdCompteIndividuelAffilie() %>">(voir l'extrait de CI)</a>
									</ct:ifhasright>
								</span>
								<%}else if (viewBean.getAmortissementCiMap().isEmpty() && viewBean.hasRubriqueCotPers()){ %>
									<span class="rubriqueIndeterminee"> Amortissement du CI impossible, vérifier l'état du CI : <%if (viewBean.getTiers() != null) { %> <%=viewBean.getTiers().getNumAvsActuel() %> <%} %></span>
								<%}%>
								<% if (viewBean.displayBlocAmortissementCi()){ %>
								
								<div class="amortissementCi_div" id="amortissementDiv">
									<table>
										<tr>
											<th colspan="2" width="1010px">Amortissement CI</th>
											<th>Etat au CI</th>
											<th>Amortissement</th>
											<th>Solde net</th>
										</tr>
										<% 
										Map<Integer, CAAmortissementCi> amortissementCiMap = viewBean.getAmortissementCiMap();
										for(Map.Entry<Integer, CAAmortissementCi>entry : amortissementCiMap.entrySet()){
											Integer annee = entry.getKey();
											CAAmortissementCi amortissementCi = entry.getValue();
										%>
											<tr>
												<td colspan="2"><b><%="Amortissement "+amortissementCi.getAnnee() %></b></td>
												<td><input class="inputMontant" type="text" id="amortissementEtatCi_<%=annee %>" name="amortissementEtatCi_<%=annee %>" value="<%=amortissementCi.getMontantEtatCi() %>" disabled="disabled"></td>
												<td><input class="inputMontant amortissementMontant" type="text" id="amortissementMontant_<%=annee %>" name="amortissementMontant_<%=annee %>" value="<%=amortissementCi.getMontantAmortissement() %>"></td>
												<td><input class="amortissementSolde inputMontant" type="text" id="amortissementSolde_<%=annee %>" name="amortissementSolde_<%=annee %>" value="<%=amortissementCi.getMontantEtatCi().subtract(amortissementCi.getMontantAmortissement()) %>" disabled="disabled"></td>
											</tr>
										<%} %>
									</table>
								</div>
								<%} %>
							</td>
						</tr>
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>