// Fonction de mise en forme d'une chaîne de caractère par substitution des occurences {X}
if (!String.prototype.format) {
    String.prototype.format = function() {
          var args = arguments;
          return this.replace(/{(\d+)}/g, function(match, number) { 
                 return typeof args[number] != 'undefined' ? args[number] : match ;
          });
    };
}

// Efface la ligne de tableau "src"
function deleteRow(src)
{
	// Garder des références sur les parents, car src disparaît avec remove();
	var tbody = $(src).closest("tbody");
	var table = $(src).closest("table");
	
	$(src).closest("tr").remove();
	
	// S'il n'y a plus de lignes, en créer une vide
	if (tbody.find("[data-usage='montant']").length == 0)
	{
		addRow(table, '','','');
	}
}

// Ajoute une ligne à la table "destTable" avec les valeurs "assure", "periode", "montant"
// dans les colonnes 1,2 et 3
function addRow(destTable, assure, periode, montant) {
	var templateRow = $("#templateRow").html();
	
	templateRow = templateRow.format(assure, periode, montant);

	$(destTable).children('tbody').append(templateRow);
}

// Sucre syntaxique
function getEl(elm) {
	return document.getElementById(elm);
}

// Collecte le contenu de toutes les lignes de la table "table" et
// inscrit les valeurs dans les hidden input's préfixés par "prefix"
function collectTableData(table, prefix) {
	var nameAssure	= prefix + "_assure";
	var namePeriode	= prefix + "_periode";
	var nameMontant	= prefix + "_montant";

	// Stocke les valeurs actuelles des input's hidden
	var assureBuffer	= getEl(nameAssure).value;
	var periodeBuffer	= getEl(namePeriode).value;
	var montantBuffer	= getEl(nameMontant).value;

	var tableRows = table.tBodies[0].rows;
	var doAssign = false;
	
	// Itérer sur chaque ligne du tableau
	for (var idx = 0; idx < tableRows.length; idx++) {
		var row = tableRows[idx];
		
		// Extraire les champs Assuré, Période et Montant pour la ligne
		var $foundAssure	= $(row).find("[data-usage='assure']");
		var $foundPeriode 	= $(row).find("[data-usage='periode']");
		var $foundMontant 	= $(row).find("[data-usage='montant']");

		// Si tous les champs sont trouvés ...
		if ($foundAssure.length == 1 && $foundPeriode.length == 1 && $foundMontant.length == 1) {
			var valAssure	= $.trim($foundAssure.val());
			var valPeriode 	= $.trim($foundPeriode.val());
			var valMontant 	= $.trim($foundMontant.val());
			var montantAsFloat = globazNotation.utilsFormatter.amountTofloat(valMontant);
			
			// Si le montant n'est pas un numérique on le vide
			if (isNaN(montantAsFloat)){
				valMontant = '';
			}
			
			// Si tous les champs possèdent une valeur ...
			if (valAssure.length > 0 || valPeriode.length > 0 || valMontant.length > 0) {
				// Prendre en compte la ligne courante
				doAssign = true;
				
				assureBuffer	+= valAssure + "¢";
				periodeBuffer 	+= valPeriode + "¢";
				montantBuffer 	+= valMontant + "¢";
			}
		}
	}

	// Si au moins une ligne valide a été extraite de la table, mettre à jour les valeurs
	// des input's hidden
	if (doAssign) {
		document.forms[0].elements(nameAssure).value	= assureBuffer;
		document.forms[0].elements(namePeriode).value	= periodeBuffer;
		document.forms[0].elements(nameMontant).value	= montantBuffer;
	}
}

// Collecte du contenu des deux tables
function validate() {
	collectTableData(getEl('attTable'), 'attTable');
	collectTableData(getEl('Table'), 'table');

	return true;
}