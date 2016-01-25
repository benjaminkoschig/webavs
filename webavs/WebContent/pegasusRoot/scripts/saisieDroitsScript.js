
/**
 * 
 * Librairie de fonctions js commun à tous les écrans AJAX de saisie de données financières
 * Requiert jQuery 1.3
 */


var elementSelectedForDisplay = null;
var elementSelectedForUpdate = false;

function init(){
}  

function postInit(){
	$("#btnCan").attr("class","btnCtrl");
}

/**
* retourne le noeud DOM ancetre représentant le membre de la famille.
* L'enfant peut se trouver à plusieurs niveaux sous l'ancetre.
* L'ancetre est identifié par la class 'areaMember'.
* @parameter node noeud DOM enfant dont on cherche l'ancetre. 
* @return noeud DOM de l'ancetre, ou null si aucun ancetre valide n'est trouvé.
*/
function getMemberParent(node){
	try {
		return $(node).parents('.areaMember')[0];
	} catch(e) {
		return null;
	}
}

function highlightDonneeFinanciere(row){
	$('.areaMemberDataTable tbody tr').removeClass('highlight');
	if(row){
		$(row).addClass('highlight');
	}
}

/**
 * ajoute des événements à une ligne d'une table, ou le cas échéant à toutes les lignes des tables de la page.
 * @param row ligne <TR/> représentant une donnée financière
 * @return
 */
function initTables(row){
	// initialise le selecteur d'element DOM à modifier. 
	// Si row n'est pas donné, étendre la selection à toutes les lignes de toutes les tables de la page
	var $selector,$selectorHeader;
	if(row) {
		$selector=$(row);
	} else {
		$selectorHeader=$('.areaMemberDataTable thead tr');
		$selector=$('.areaMemberDataTable tbody tr');
	}
	// gere l'affichage dynamique des dataTable
		
	$selector.hover(function(){
		if(!elementSelectedForUpdate && getMemberParent(this)==elementSelectedForDisplay){
			$(this).addClass('hover');
		}
		},function(){
			// dans tous les cas, enlever le hover
			$(this).removeClass('hover');
		}) // gestion du click
		.click(function(){
			if(!elementSelectedForUpdate && getMemberParent(this)==elementSelectedForDisplay){
				highlightDonneeFinanciere(this);
				loadMemberData(getMemberParent(this),$(this).attr('idDonneeFinanciere'));
			}
		}) // formate la ligne selon le formatage personnalisé de l'écran
		.each(formatTableRow);
	if($selectorHeader)
		$selectorHeader.each(formatTableRow);
		
}

// *********************** méthodes AJAX communes *******************************
/**
 * charge le détail d'une donnée financière et retourne le résultat dans la fonction onLoadMemberData
 * @param member objet DIV du membre selectionné
 * @param idDonneeFinanciere id de la donnée financière demandée
 */
function loadMemberData(member,idDonneeFinanciere){
	member.selectedIdDonneeFinanciere=idDonneeFinanciere;
	$.ajax({
		data: {"userAction": ACTION_AJAX_URL+".recharger.ajax",
				"idDonneeFinanciere":idDonneeFinanciere},
		success: function(data){onLoadMemberData(data, member);},
		type: "GET"			
	});			
}

/**
 * fonction traitant le résultat de la requete de détail d'une donnée financière
 * @param data résultat de la requete en objet XML
 * @param member objet DIV du membre selectionné
 * @return
 */
function onLoadMemberData(data, member){
	// affiche les boutons d'edition
	$(member).find('.btnUpdate').show();
	$(member).find('.btnDelete').show();

	// appelle la fonction pour afficher les données sur la page, si elle existe
	if(onReadProprietes!=null){
		onReadProprietes(member,data);
	}
}

/**
 * sauvegarde une donnée financière. S'il s'agit d'une nouvelle donnée financière, elle est créée.
 * La fonction onSaveMemberData est appelée à la fin de la requête.
 * @param member objet DIV du membre de famille selectionné
 * @return
 */
function saveMemberData(member){
	// récupère les parametres de la donnée financière à sauvegarder
	var parametres=member.getParametres();
	if(member.isNew){
		parametres["userAction"]=ACTION_AJAX_URL+".ajouterNouveau.ajax";
	} else {
		parametres["userAction"]=ACTION_AJAX_URL+".sauvegarder.ajax";
	}
	parametres["idDonneeFinanciere"]=member.selectedIdDonneeFinanciere;
	parametres["idMapVerDroitJointMbrFam"]=member.memberMapDroitId;

	$.ajax({
		data: parametres,
		success: function(data) {onSaveMemberData(data,member);},
		type: "POST"			
	});			
}

/**
 * fonction traitant le résultat de la requete de sauvegarde d'une donnée financière.
 * Le résultat contient le texte formaté en HTML de la ligne modifié.
 * @param data résultat de la requete en objet XML
 * @param member objet DIV du membre selectionné
 * @return
 */
function onSaveMemberData(data,member){
	if($(data).find('status').text()=='OK'){

		// récupère le texte formaté de la ligne modifiée et enlève les tags inutiles
		var newRow=$(data).find('row')[0].xml.replace("<row>","").replace("</row>","");
		var $row;
		
		if(member.isNew){
			// création d'un noeud DOM représentant la nouvelle ligne à afficher dans la table
			$row=$("<tr idDonneeFinanciere='"+$(data).find('idDonneeFinanciere').text()+"'/>");
			$(member).find('.areaMemberDataTable tbody').append($row);
		} else {
			$row=$(member).find('tr[idDonneeFinanciere='+member.selectedIdDonneeFinanciere+']');
		}
		// insère le texte formaté de la ligne dans la ligne de la page
		$row.html(newRow);
		// ajoute des événements à la ligne
		initTables($row[0]);
		// quitte le mode edition
		member.clearChamps();
		// recolorie les lignes de la table
		$('.areaMemberDataTable tbody tr:odd',member).addClass('odd');				
		$('.areaMemberDataTable tbody tr:even',member).removeClass('odd');
		
		if(onAfterSaveMember){
			onAfterSaveMember(member);
		}
		
	} else {
		alert($(data).find('message').text());
	}
}

/**
 * envoie une requete AJAX pour supprimer la donnée financière en cours de modification.
 * Appelle la fonction onDeleteMember lorsque la requete est traitée.
 * @param member objet DIV représentant le membre de famille du contexte. Le membre contient l'id de la donnée fin. à supprimer.
 * @return
 */
function deleteMember(member){
    if (!window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
        return;
    }
	
	var parametres={};
	parametres["userAction"]=ACTION_AJAX_URL+".supprimerCustom.ajax";

	parametres["idDonneeFinanciere"]=member.selectedIdDonneeFinanciere;
	parametres["idMapVerDroitJointMbrFam"]=member.memberMapDroitId;

	$.ajax({
		data: parametres,
		success: function(data) {onDeleteMember(data,member);},
		type: "POST"			
	});		
}

/**
 * Fonction traitant le résultat de la requête de suppression de la donnée financière en cours d'édition
 * @param data donnée XML resultante
 * @param member objet DIV du membre en édition
 * @return
 */
function onDeleteMember(data,member){
	if($(data).find('status').text()=='OK'){
		$(member).find('tr[idDonneeFinanciere='+member.selectedIdDonneeFinanciere+']').remove();
		member.clearChamps();
		// recolorie les lignes de la table
		$('.areaMemberDataTable tbody tr:odd',member).addClass('odd');				
		$('.areaMemberDataTable tbody tr:even',member).removeClass('odd');
	} else {
		alert($(data).find('message').text());
	}
}

// *******************************************************************************



/**
 * fonction d'initialisation de la page
 * 
 * Note: ce n'est pas forcément la seule fonction d'initalisation, 
 * et l'ordre d'execution des fonctions d'initialisation n'est pas garantie.
 * Il faut donc supposer que les variables utilisées soient initialement null. 
 */
$(function(){
	
	// configure les blocs areaMember
	$('.areaMember').each(function(){
		
		this.clearChamps=function(){
			highlightDonneeFinanciere(null);
			elementSelectedForUpdate=false;
			this.isNew=true;
			$(this).find('.areaMemberButtons *').hide().end()
					 .find('.btnAdd').show().end()
					 .find('.areaMemberProprietes *').attr('disabled',true).end()
					 .find('.areaMemberProprietes input:text').val('');
			$('.areaMemberDescription img').show();
		};
		
		this.editChamps=function(){
			elementSelectedForUpdate=true;
			$('.areaMemberDescription img').hide();
			$(this).find('.areaMemberProprietes *').attr('disabled',false).end()
					 .find('.areaMemberButtons *').hide().end()
					 .find('.btnValidate').show().end()
					 .find('.btnCancel').show().end();
		};				
		
		this.hideDetail=function(){
			this.clearChamps();
			$('.areaMemberDetail',this).hide();
			if($('.areaMemberDataTable tbody tr',this).length==0){
				$('.areaMemberDataTable',this).hide();
			}
		}	
		
		// cache tous les détails par défaut
		this.hideDetail();			
	});	
	
	// affiche tous les boutons Nouveau
	$('.btnAdd').show();


	// met tous les champs de propriété (détail) de chaque membre à disabled
	$('.areaMemberProprietes *').attr('disabled',true);
	
	// gère l'affichage du détail d'un membre en cliquant sur son entete
	$('.areaMemberDescription').click(function(){
		if(elementSelectedForUpdate){
			return;
		}

		// deselectionne le précédent membre
		if(elementSelectedForDisplay!=null){
			$(elementSelectedForDisplay).removeClass('areaMemberSelected');				
			elementSelectedForDisplay.hideDetail();
		}

		var parent=getMemberParent(this);
		var localId=parent.memberId;
		var previousId=( elementSelectedForDisplay==null ? -1 : elementSelectedForDisplay.memberId);

		if (previousId==localId) {
			elementSelectedForDisplay=null;
		} else {
			elementSelectedForDisplay=parent;
			// affiche zone de détail
			$(parent).find('.areaMemberDetail').show().end()
					 .find('.areaMemberDataTable').show();
			$(parent).addClass('areaMemberSelected');
		}
		
	});
	
	initTables();
	$('.areaMemberDataTable tbody tr:odd').addClass('odd');			
	
	//ajoute des actions aux boutons
	$('.btnUpdate').click(function(){
		var parent=getMemberParent(this);
		parent.isNew=false;
		parent.editChamps();
	});

	$('.btnCancel').click(function(){
		getMemberParent(this).clearChamps();
	});

	$('.btnValidate').click(function(){
		saveMemberData(getMemberParent(this));
	});

	$('.btnAdd').click(function(){
		var parent=getMemberParent(this);
		parent.clearChamps();
		parent.isNew=true;
		parent.editChamps();
	});

	$('.btnDelete').click(function(){
		deleteMember(getMemberParent(this));
	});

	
});
