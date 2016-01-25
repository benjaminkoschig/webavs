<%@page import="globaz.pegasus.vb.process.PCStatistiqueOFASViewBean"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%
	PCStatistiqueOFASViewBean viewBean = (PCStatistiqueOFASViewBean) session.getAttribute("viewBean");
%>

<script language="JavaScript">
	// On surcharge la fonction qui vas afficher le resultat d'une entité.
	$(function() {
		var idInterval = setInterval(function(){
			if (typeof AbstractScalableAJAXTableZone !== "undefined"){
				clearInterval(idInterval);
				var superAferRetrive =AbstractScalableAJAXTableZone.executeAfterRetrieve;
				AbstractScalableAJAXTableZone.executeAfterRetrieve =  function (data1,idEntity) {
					
					this.afterRetrieve(data1,idEntity);
					
					var data = $.parseJSON(data1.proprietes[0].value);
					var depensesReconues = data.depense.loyerCompte + data.depense.taxeHomeCompte
					+ data.depense.personnelles + data.depense.primeMaladie
					+ data.depense.primeMaladieConjEnf + data.depense.interetFraisDeterminant
					+ data.depense.besoinsVitaux + data.depense.autresDepenses;

					var revenusDeterminants = data.revenu.RentesAvs + data.revenu.allocationImpotant
						+ data.revenu.ij + data.revenu.prestaionsLAMAL
						+ data.revenu.actLucPrisEnCompt + data.revenu.autreRentes
						+ data.revenu.revenuFortuneMobilier + data.revenu.produitFortuneImmobilier
						+ data.revenu.valeurLocative + data.revenu.usufrutit
						+ data.revenu.autresRevenues + data.revenu.forturnePriseEnCompte;
					
					var montantPCCalcule = depensesReconues - revenusDeterminants;
					if (depensesReconues < 0) {
						montantPCCalcule = 0;
					}
					
					var mimimumGaranti = data.depense.primeMaladie + data.depense.primeMaladieConjEnf;
					if ((montantPCCalcule > 0) && (montantPCCalcule < mimimumGaranti)) {
						montantPCCalcule = mimimumGaranti;
					}
					
					//les frais diététiques ne sont plus
					var montantPCEffectif = data.montantPc - 0;
					
					var immo = Math.max(((data.fortuneDettes.habitationPrincipal) - data.fortuneDettes.deductionForfaitaire), 0);
					
					var fortunePriseEnCompte = (immo + data.fortuneDettes.immobiliere + data.fortuneDettes.autresFortunes)
						- data.fortuneDettes.dettesHyp
						- data.fortuneDettes.autresDettes - data.fortuneDettes.franchise;
					
					var diff1 = montantPCCalcule - montantPCEffectif;
					
					if (fortunePriseEnCompte < 0) {
						fortunePriseEnCompte = 0;
					}
					
					var diff2 = fortunePriseEnCompte - data.fortuneDettes.prisCompte;
						
					if(typeof console !== "undefined"){
						console.log("depensesReconues:",depensesReconues);
						console.log("revenusDeterminants:",revenusDeterminants);
						console.log("fortunePriseEnCompte:"+fortunePriseEnCompte);
						console.log("mimimumGaranti:"+mimimumGaranti);
						console.log("montantPCCalcule:"+montantPCCalcule);
						console.log("montantPCEffectif:"+montantPCEffectif);
						console.log("montant pc mois:"+ (montantPCEffectif-data.depense.primeMaladie)/12);
						
						console.log("diff1:"+diff1);
						console.log("diff2:"+diff2);
						console.log("data:",data);
					}
				};
			} 
		},100)
	});

</script>

<div id="properties" >
	<div>		
		<label for="DATE_STATISTIQUE"><ct:FWLabel key="JSP_PC_STATISTIQUE_OFAS_DATE"/></label>
		<input id="DATE_STATISTIQUE" data-g-calendar="type:month,mandatory:true" value="" />
	</div>	
</div>