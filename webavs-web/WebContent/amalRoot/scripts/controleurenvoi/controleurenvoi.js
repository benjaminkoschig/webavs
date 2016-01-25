/**
 * Fonction de recherches de lignes de job en fonction du mois et de l'année
 */
function searchLines(currentMonth, currentYear){
	// Check input values
	var checkMonth = new Boolean(0);
	if(currentMonth>0&&currentMonth<13){
		checkMonth= new Boolean(1);
	}else{
		$('#inputMonth').val("");
	}
	var checkYear = new Boolean(0);
	if(currentYear>1990 && currentYear<2200){
		checkYear = new Boolean(1);
	}else{
		$('#inputYear').val("");
	}
	// Recherche
	$('#tableauPrincipal td').each(function (iIndex) {
		var idCell = this.id;
		var idDescription = "jobDescription_";
		// get the tr
		var currentLineId = this.parentNode.id;
		var currentLineIdEmpty = currentLineId+"_empty";
		// ligne de titres ligneTitrePrincipale_15_Title0
		// A voir l'utilité de les cacher...
		var currentJob = currentLineId.split("_")[1];
		var currentLineIdTitle0 = "ligneTitrePrincipale_"+currentJob+"_Title_0";
		var currentLineIdTitle1 = "ligneTitrePrincipale_"+currentJob+"_Title_1";
		
		if(idCell!=null && idCell.length>=idDescription.length){
			if(idCell.substring(0,idDescription.length)==idDescription){
				var date = idCell.split("_")[2];
				var month = parseInt(date.split(".")[1]);
				var year = parseInt(date.split(".")[2]);
				// Check si année/mois doit être prise en compte
				if(checkYear==true && checkMonth==true){
					if(currentMonth == month && currentYear == year){
						//alert("Month/Year OK : "+currentLineId);
						$('#'+currentLineId).show();
						$('#'+currentLineIdEmpty).show();
					}else{
						//alert("Month/Year NOK : "+currentLineId);
						$('#'+currentLineId).hide();
						$('#'+currentLineIdEmpty).hide();
					}
				}else if(checkYear==true){
					if(currentYear == year){
						//alert("Year OK : "+currentLineId);
						$('#'+currentLineId).show();
						$('#'+currentLineIdEmpty).show();
					}else{
						//alert("Year NOK : "+currentLineId);
						$('#'+currentLineId).hide();
						$('#'+currentLineIdEmpty).hide();
					}
				}else if(checkMonth==true){
					if(currentMonth==month){
						// alert("Month OK : "+currentLineId);
						$('#'+currentLineId).show();
						$('#'+currentLineIdEmpty).show();
					}else{
						// alert("Month NOK : "+currentLineId);
						$('#'+currentLineId).hide();					
						$('#'+currentLineIdEmpty).hide();					
					}
				}else{
					// Filtre vide, on montre tout
					$('#'+currentLineId).show();
					$('#'+currentLineIdEmpty).show();
				}
			}
		}
	});
}

/**
 * Initialisation et fonctions particulières de gestion du tableau de bord
 */
$(document).ready(function() {
	
	// Hide Show Help text
	// ----------------------------------------------------
	$('#helpPicture').click(function(e){
		$('#divExplication').slideToggle("fast");
	});
	
	// Recherche dans la liste
	// ----------------------------------------------------
	$('#inputMonth').keyup(function (e){
		var s_currentMonth = $('#inputMonth').val();
		var currentMonth = 0;
		if(s_currentMonth!=null && s_currentMonth.length>0){
			currentMonth = parseInt(s_currentMonth);
		}
		var s_currentYear = $('#inputYear').val();
		var currentYear = 0;
		if(s_currentYear!=null && s_currentYear.length>0){
			currentYear = parseInt(s_currentYear);
		}
		searchLines(currentMonth, currentYear);
	});
	// Recherche dans la liste
	// ----------------------------------------------------
	$('#inputYear').keyup(function (e){
		if($('#inputYear').val().length>3 || $('#inputYear').val().length<1){
			var s_currentMonth = $('#inputMonth').val();
			var currentMonth = 0;
			if(s_currentMonth!=null && s_currentMonth.length>0){
				currentMonth = parseInt(s_currentMonth);
			}
			var s_currentYear = $('#inputYear').val();
			var currentYear = 0;
			if(s_currentYear!=null && s_currentYear.length>0){
				currentYear = parseInt(s_currentYear);
			}
			searchLines(currentMonth, currentYear);
		}
	});
	// Effacement des champs de recherches
	// ----------------------------------------------------------
	$('#imgClearYear').click(function() {
		$('#inputYear').val("");
		$('#inputYear').keyup();
	});	
	$('#imgClearMonth').click(function() {
		$('#inputMonth').val("");
		$('#inputMonth').keyup();
	});	


	// Cacher les lignes enfant
	// du tableauPrincipal
	// ----------------------------------------------------
	$('#tableauPrincipal tr').each(function (iIndex) {
		var idLigne=this.id;
		var ligneEnfant="ligneEnfant_";
		// check the name to find the children only
		if(idLigne.length>=ligneEnfant.length){
			if(idLigne.substring(0,ligneEnfant.length)==ligneEnfant){
				$('#'+idLigne).hide();
			}
		}
	});

	// Gestion du highlight sur le tableau
	// ----------------------------------------------------------------
	 $("#tableauPrincipal tr").mouseover(function(){
		// Highlight all tr from the table tableauMembreFamilleInfo
		var currentLineId = this.id;
		var childLine="ligneEnfant_";
		var mainLine = "lignePrincipale_";
		var emptyLine = "empty";
		var titleLine = "Title";
		if(currentLineId.length>=childLine.length){
			var currentLineSplit = currentLineId.split("_");
			// check si pas ligne empty de séparation ou autre
			if(emptyLine!=currentLineSplit[currentLineSplit.length-1]
				&&
				titleLine!=currentLineSplit[currentLineSplit.length-2]){
				// récupération de l'ancien Nom de la classe utilisée
				oldClassNameLine = $('#'+currentLineId).attr('class');
				$('#'+currentLineId).removeClass().addClass('amalRowHighligthed');
			}
		}
    }).mouseout(function(){
		var currentLineId = this.id;
		if("amalRowHighligthed"==$('#'+currentLineId).attr('class')){
        	$('#'+currentLineId).removeClass().addClass(oldClassNameLine);		
		}
    });

	
	// Cacher les les checkbox
	// du tableauPrincipal
	// Seront réactivés lors du click sur le bouton expand
	// ----------------------------------------------------
	$('#tableauPrincipal [type=checkbox]').each(function (iIndex) {
		var currentId=this.id;
		toggleOpacityElement(currentId,0.1,1.0);
	});

	// Action click sur les elements de la classe imgExpand
	// ----------------------------------------------------
	$('.imgExpand').click(function() {
		// get the job id. expected : imgExpand_#idJob
		var idImg=this.id;
		var idJob=idImg.split("_")[1];
		var searchedLine="ligneEnfant_"+idJob+"_";
		var searchedMainLine = "lignePrincipale_"+idJob+"_";
		var bAllChildChecked = new Boolean(true);
		var nbRow = 0;
		// toggle the child lines
		$('#tableauPrincipal tr').each(function (iIndex) {
			var idLigne=this.id;
			if(idLigne.length>=searchedLine.length){
				// check if we find the ligneEnfant
				if(idLigne.substring(0,searchedLine.length)==searchedLine){
					// increment the line count
					if(idLigne.split("_")[3]!="empty"){
						nbRow++;
					}
					// toggle the line
					$('#'+idLigne).toggle();
					
					// check the value of the checkbox
					if(idLigne.split("_")[3]!= "empty" && bAllChildChecked){
						var idStatus = idLigne.split("_")[2];
						var idCheckBox = "checkboxEnfant_"+idJob+"_"+idStatus+"_";
						bAllChildChecked = $('#'+idCheckBox).prop('checked');
					}
				}
			}
		});
		// toggle the checkboxes
		// check si toutes les checkboxes sont activées pour un job
		// si oui, active uniquement la checkbox principale
		// si non, active le tout
		// toggle the child lines
		$('#tableauPrincipal tr').each(function (iIndex) {
			var idLigne=this.id;
			// check if we find the ligneEnfant and work on it
			if(idLigne.length>=searchedLine.length){
				if(idLigne.substring(0,searchedLine.length)==searchedLine){
					// get the checkbox and work on it
					if(idLigne.split("_")[3]!= "empty"){
						var idStatus = idLigne.split("_")[2];
						var idCheckBox = "checkboxEnfant_"+idJob+"_"+idStatus+"_";
						var idComboBox = "comboboxEnfant_"+idJob+"_"+idStatus+"_";
						var idButtonImprimer = "buttonImprimerEnfant_"+idJob+"_"+idStatus+"_";
						var idButtonSupprimer = "buttonSupprimerEnfant_"+idJob+"_"+idStatus+"_";
						if(bAllChildChecked){
							setOpacityElement(idCheckBox,0.2);
							setDisabledElement(idCheckBox, true);
							setOpacityElement(idComboBox,0.2);
							setDisabledElement(idComboBox, true);
							setOpacityElement(idButtonImprimer,0.2);
							setDisabledElement(idButtonImprimer, true);
							setOpacityElement(idButtonSupprimer,0.2);
							setDisabledElement(idButtonSupprimer, true);
						}else{
							// check si le status des combobox n'est pas envoyé
							if($('#'+idComboBox).val() != statusDocumentSent){
								setOpacityElement(idCheckBox,1.0);
								setDisabledElement(idCheckBox, false);
								setOpacityElement(idComboBox,1.0);
								setDisabledElement(idComboBox, false);
								setOpacityElement(idButtonImprimer,1.0);
								setDisabledElement(idButtonImprimer, false);
								setOpacityElement(idButtonSupprimer,1.0);
								setDisabledElement(idButtonSupprimer, false);
							}
						}
					}
				}
			}
			// check if we find the main line and work on it
			if(idLigne.length>=searchedMainLine.length){
				if(idLigne.substring(0,searchedMainLine.length)==searchedMainLine){
					// Activate the checkbox only if we have more than 1 row
					if(nbRow>1){
						// get the checkbox and work on it
						if(idLigne.split("_")[2]!= "empty"){
							var idCheckBox = "checkboxPrincipale_"+idJob+"_";
							// TOGGLE UNIQUEMENT SI LE JOB N'EST PAS IN PROGRESS
							// DONC UNIQUEMENT SI ON A UNE COMBOBOX PRINCIPALE IJOB
							var idComboBoxPrincipale = "comboboxPrincipale_"+idJob+"_";
							if(document.getElementById(idComboBoxPrincipale)){
								toggleOpacityElement(idCheckBox,0.2,1.0);
								if(bAllChildChecked){
									$('#'+idCheckBox).prop('checked',true);
								}
							}
						}
					}
				}
			}
		});
		
		// toggle the image button
		if($('#'+idImg).attr('src')==iconCollapse){
			$('#'+idImg).attr('src',iconExpand);
			expandedJob="";
		}else{
			$('#'+idImg).attr('src',iconCollapse);
			expandedJob=idJob;
		}
	});
	
	// action click sur les checkbox
	// active désactive l'affichage des checkbox, combobox, button
	// du bébé en dessous
	// -----------------------------------------------------------
	$('#tableauPrincipal [type=checkbox]').click(function() {
		var idCheckBox = $(this).attr('id');
		var idJob = idCheckBox.split("_")[1];
		var idCheckBoxPrincipale = "checkboxPrincipale_"+idJob+"_";
		var idCheckBoxEnfant = "checkboxEnfant_"+idJob+"_";
		// Detect if we got a click from the main checkbox or a children checkbox
		if(idCheckBox.length>=idCheckBoxPrincipale.length){
			// Click from a checkboxPrincipale
			// ----------------------------------------------------
			if(idCheckBox.substring(0,idCheckBoxPrincipale.length)==idCheckBoxPrincipale){
				
				if($('#'+idCheckBox).prop('checked')){
					// unactivate the enfant line element
					$('#tableauPrincipal [type=checkbox]').each(function (iIndex) {
						var currentId=this.id;
						// check the name to find the children only
						if(currentId.length>=idCheckBoxEnfant.length){
							if(currentId.substring(0,idCheckBoxEnfant.length)==idCheckBoxEnfant){
								var idStatus=currentId.split("_")[2];
								// unactivate the checkbox
								setOpacityElement(currentId,0.2);
								setDisabledElement(currentId, true);
								$('#'+currentId).prop('checked','true');
								// unactivate the combobox
								var idComboBoxEnfant="comboboxEnfant_"+idJob+"_"+idStatus+"_";
								setOpacityElement(idComboBoxEnfant,0.2);
								setDisabledElement(idComboBoxEnfant, true);
								// unactivate the button Imprimer
								var idButtonImprimerEnfant="buttonImprimerEnfant_"+idJob+"_"+idStatus+"_";
								setOpacityElement(idButtonImprimerEnfant,0.2);
								setDisabledElement(idButtonImprimerEnfant, true);
								// unactivate the button Supprimer
								var idButtonSupprimerEnfant="buttonSupprimerEnfant_"+idJob+"_"+idStatus+"_";
								setOpacityElement(idButtonSupprimerEnfant,0.2);
								setDisabledElement(idButtonSupprimerEnfant, true);
							}
						}
					});
				}else{
					// Activate the enfant line element
					$('#tableauPrincipal [type=checkbox]').each(function (iIndex) {
						var currentId=this.id;
						// check the name to find the children only
						if(currentId.length>=idCheckBoxEnfant.length){
							if(currentId.substring(0,idCheckBoxEnfant.length)==idCheckBoxEnfant){
								var idStatus=currentId.split("_")[2];
								var idComboBoxEnfant="comboboxEnfant_"+idJob+"_"+idStatus+"_";
								// check si le status des combobox n'est pas envoyé
								if($('#'+idComboBoxEnfant).val() != statusDocumentSent){
									// Activate the enfant checkbox
									setOpacityElement(currentId,1.0);
									setDisabledElement(currentId, false);
									// Activate the combobox
									setOpacityElement(idComboBoxEnfant,1.0);
									setDisabledElement(idComboBoxEnfant, false);
									// Activate the button Imprimer
									var idButtonImprimerEnfant="buttonImprimerEnfant_"+idJob+"_"+idStatus+"_";
									setOpacityElement(idButtonImprimerEnfant,1.0);
									setDisabledElement(idButtonImprimerEnfant, false);
									// Activate the button Supprimer
									var idButtonSupprimerEnfant="buttonSupprimerEnfant_"+idJob+"_"+idStatus+"_";
									setOpacityElement(idButtonSupprimerEnfant,1.0);
									setDisabledElement(idButtonSupprimerEnfant, false);
								}
							}
						}
					});
				}
			}
		}
		// Detect if we got a click from the main checkbox or a children checkbox
		// ----------------------------------------------------
		if(idCheckBox.length>=idCheckBoxEnfant.length){
			// Click from a checkboxEnfant
			if(idCheckBox.substring(0,idCheckBoxEnfant.length)==idCheckBoxEnfant){
			}
		}
	});	

	// Activer les combobox (events)
	// du tableauPrincipal
	// ----------------------------------------------------
	$('#tableauPrincipal select').each(function (iIndex) {
		var idCurrent=this.id;
		var idJob = idCurrent.split("_")[1];
		// Get the principale combobox and create the event handler
		var idComboBoxPrincipale="comboboxPrincipale_"+idJob+"_";
		if(idCurrent.length>=idComboBoxPrincipale.length){
			if(idCurrent.substring(0,idComboBoxPrincipale.length)==idComboBoxPrincipale){

				// combobox event change handler
				// -------------------------------------------
				$('#'+idComboBoxPrincipale).change(function(){
					// ALL CHILDREN COMBOBOX MUST BE SET
			       	var selectedValue=$(this).val();
					var allStatusId = "";
			     	$('#tableauPrincipal select').each(function (iIndex) {
			    		var idCurrentMain=this.id;
			    		var idJobMain = idCurrent.split("_")[1];
			    		// Get the children combobox and SET THE SELECTED INDEX
			    		var idComboBoxEnfantMain="comboboxEnfant_"+idJobMain+"_";
			    		if(idCurrentMain.length>=idComboBoxEnfantMain.length){
			    			if(idCurrentMain.substring(0,idComboBoxEnfantMain.length)==idComboBoxEnfantMain){
			    				var idStatus = idCurrentMain.split("_")[2];
			    				idComboBoxEnfantMain+=idStatus+"_";
			    				// set combobox values si checkbox activée && status != sent
			    				var idRelatedCheckBox="checkboxEnfant_"+idJobMain+"_"+idStatus+"_";
			    				if($('#'+idRelatedCheckBox).prop('checked')){
									// check si le status des combobox n'est pas envoyé
									if($('#'+idComboBoxEnfantMain).val() != statusDocumentSent){
										$('#'+idComboBoxEnfantMain +' option[value='+selectedValue+']').prop('selected', 'selected');
										if(allStatusId.length>0){
											allStatusId+=",";	
										}
										allStatusId+=idStatus;
									}
			    				}
			    			}
			    		}
			    	});
			     	// Procède au changement de status
					var idRelatedCheckBox="checkboxPrincipale_"+idJob+"_";
					if($('#'+idRelatedCheckBox).prop('checked')){
					    
						if (window.confirm("Voulez-vous changer le status du job "+idJob+" ?")){
							// Show Progress Windows
							showProgress('#inProgressDialog');
							// Proceed with status change			
						    document.forms[0].elements('userAction').value = actionStatus;
						    var idImg= "imgExpand_"+idJob;
							if($('#'+idImg).attr('src')==iconCollapse){
							    document.forms[0].elements('expandedJob').value = idJob;
							}
						    document.forms[0].elements('selectedJob').value = idJob;
						    document.forms[0].elements('newStatus').value = selectedValue;
							document.forms[0].submit();
					    }else{
					    	// New Status à 0, ré-initialise la page
							// Show Progress Windows
							showProgress('#inProgressDialog');
							// Proceed with status change			
						    document.forms[0].elements('userAction').value = actionStatus;
						    var idImg= "imgExpand_"+idJob;
							if($('#'+idImg).attr('src')==iconCollapse){
							    document.forms[0].elements('expandedJob').value = idJob;
							}
						    document.forms[0].elements('selectedJob').value = idJob;
						    document.forms[0].elements('newStatus').value = "";
							document.forms[0].submit();
					    }
						
					}else if(allStatusId.length>0){

						if (window.confirm("Voulez-vous changer le status des éléments sélectionnés ?")){
							// Show Progress Windows
							showProgress('#inProgressDialog');
							// Proceed with status change			
						    document.forms[0].elements('userAction').value = actionStatus;
						    var idImg= "imgExpand_"+idJob;
							if($('#'+idImg).attr('src')==iconCollapse){
							    document.forms[0].elements('expandedJob').value = idJob;
							}
						    document.forms[0].elements('selectedJob').value = idJob;
						    document.forms[0].elements('selectedStatus').value = allStatusId;
						    document.forms[0].elements('newStatus').value = selectedValue;
							document.forms[0].submit();
					    }else{
					    	// New Status à 0, ré-initialise la page
							// Show Progress Windows
							showProgress('#inProgressDialog');
							// Proceed with status change			
						    document.forms[0].elements('userAction').value = actionStatus;
						    var idImg= "imgExpand_"+idJob;
							if($('#'+idImg).attr('src')==iconCollapse){
							    document.forms[0].elements('expandedJob').value = idJob;
							}
						    document.forms[0].elements('selectedJob').value = idJob;
						    document.forms[0].elements('selectedStatus').value = allStatusId;
						    document.forms[0].elements('newStatus').value = "";
							document.forms[0].submit();
					    }
					}else{
						alert("Pas d'élément sélectionné, aucune action ne sera générée");
					}
				});
				
				
			}
		}
		// Get the children combobox and create the event handler
		var idComboBoxEnfant="comboboxEnfant_"+idJob+"_";
		if(idCurrent.length>=idComboBoxEnfant.length){
			if(idCurrent.substring(0,idComboBoxEnfant.length)==idComboBoxEnfant){
				var idStatus = idCurrent.split("_")[2];
				idComboBoxEnfant+=idStatus+"_";
				// combobox event change handler
				// -------------------------------------------
				$('#'+idComboBoxEnfant).change(function(){
			       	var selectedValue=$(this).val();
				    if (window.confirm("Voulez-vous changer le status de ce document ?")){
						// Show Progress Windows
						showProgress('#inProgressDialog');
						// Proceed with status change			
					    document.forms[0].elements('userAction').value = actionStatus;
					    document.forms[0].elements('expandedJob').value = idJob;
					    document.forms[0].elements('selectedJob').value = idJob;
					    document.forms[0].elements('selectedStatus').value = idStatus;
					    document.forms[0].elements('newStatus').value = selectedValue;
						document.forms[0].submit();
				    }else{
				    	// New Status à 0, ré-initialise la page
						// Show Progress Windows
						showProgress('#inProgressDialog');
						// Proceed with status change			
					    document.forms[0].elements('userAction').value = actionStatus;
					    document.forms[0].elements('expandedJob').value = idJob;
					    document.forms[0].elements('selectedJob').value = idJob;
					    document.forms[0].elements('selectedStatus').value = idStatus;
					    document.forms[0].elements('newStatus').value = "";
						document.forms[0].submit();
				    }
					
				});

			}
		}
		
	});
	
	
	// Activer les actions sur les boutons
	// ----------------------------------------------------
	$(':button').click(function() {
		var idCurrent=this.id;
		var idButtonImprimerPrincipale = "buttonImprimerPrincipale_";
		var idButtonImprimerEnfant = "buttonImprimerEnfant_";
		var idButtonSupprimerPrincipale = "buttonSupprimerPrincipale_";
		var idButtonSupprimerEnfant = "buttonSupprimerEnfant_";

		if(idCurrent.substring(0,idButtonImprimerEnfant.length)==idButtonImprimerEnfant){

		    if (window.confirm("Voulez-vous imprimer ce document ?")){
				// Show Progress Windows
				showProgress('#inProgressDialog');
				// Proceed with deletion			
				var idJob=idCurrent.split("_")[1];
				var idStatus=idCurrent.split("_")[2];
			    document.forms[0].elements('userAction').value = actionImprimer;
			    document.forms[0].elements('expandedJob').value = idJob;
			    document.forms[0].elements('selectedJob').value = idJob;
			    document.forms[0].elements('selectedStatus').value = idStatus;
				document.forms[0].submit();
		    }
			
		}else if(idCurrent.substring(0,idButtonSupprimerEnfant.length)==idButtonSupprimerEnfant){

		    if (window.confirm("Voulez-vous réellement supprimer ce document ?")){
				// Show Progress Windows
				showProgress('#inProgressDialog');
				// Proceed with deletion			
				var idJob=idCurrent.split("_")[1];
				var idStatus=idCurrent.split("_")[2];
			    document.forms[0].elements('userAction').value = actionSupprimer;
			    document.forms[0].elements('expandedJob').value = idJob;
			    document.forms[0].elements('selectedJob').value = idJob;
			    document.forms[0].elements('selectedStatus').value = idStatus;
				document.forms[0].submit();
		    }

		}else if(idCurrent.substring(0,idButtonImprimerPrincipale.length)==idButtonImprimerPrincipale){

			var idJob=idCurrent.split("_")[1];

			// Get the status of the main checkbox
			// if checked, print the whole job
			// if not, print the selected childrens
			var idRelatedCheckBox="checkboxPrincipale_"+idJob+"_";
			if($('#'+idRelatedCheckBox).prop('checked')){
			    if (window.confirm("Voulez-vous imprimer l'ensemble du job "+idJob+" ?")){
					// Show Progress Windows
					showProgress('#inProgressDialog');
					// Proceed with the print process			
				    document.forms[0].elements('userAction').value = actionImprimer;
				    document.forms[0].elements('selectedJob').value = idJob;
				    var idImg= "imgExpand_"+idJob;
					if($('#'+idImg).attr('src')==iconCollapse){
					    document.forms[0].elements('expandedJob').value = idJob;
					}
					document.forms[0].submit();
			    }
			}else{
				var allStatusId = "";
				var nbLines = 0;
				// check the children status (checked) and get the ids
				$('#tableauPrincipal [type=checkbox]').each(function (iIndex) {
					var currentCheckBoxId=this.id;
					var checkBoxEnfant = "checkboxEnfant_"+idJob+"_";
					if(currentCheckBoxId.substring(0,checkBoxEnfant.length)==checkBoxEnfant){
						nbLines++;
						if($('#'+currentCheckBoxId).prop('checked')){
							var statusId = currentCheckBoxId.split("_")[2];
							if(allStatusId.length>0){
								allStatusId+=",";	
							}
							allStatusId+=statusId;
						}
					}
				});
				// We got the status, we can proceed with print process
				if(allStatusId.length>0){
					// Job entier
					if(nbLines == allStatusId.split(",").length){
					    if (window.confirm("Voulez-vous imprimer l'ensemble du job "+idJob+" ?")){
							// Show Progress Windows
							showProgress('#inProgressDialog');
							// Proceed with the print process
						    document.forms[0].elements('userAction').value = actionImprimer;
						    document.forms[0].elements('selectedJob').value = idJob;
						    var idImg= "imgExpand_"+idJob;
							if($('#'+idImg).attr('src')==iconCollapse){
							    document.forms[0].elements('expandedJob').value = idJob;
							}
							document.forms[0].submit();
					    }
					}else{
						// Sélection d'éléments
					    if (window.confirm("Voulez-vous Imprimer les éléments sélectionnés ?")){
							// Show Progress Windows
							showProgress('#inProgressDialog');
							// Proceed with deletion			
						    document.forms[0].elements('userAction').value = actionImprimer;
						    document.forms[0].elements('selectedJob').value = idJob;
						    document.forms[0].elements('selectedStatus').value = allStatusId;
						    var idImg= "imgExpand_"+idJob;
							if($('#'+idImg).attr('src')==iconCollapse){
							    document.forms[0].elements('expandedJob').value = idJob;
							}
							document.forms[0].submit();
					    }
					}
				}else{
					alert("Pas d'élément sélectionné, aucune action ne sera générée");
				}
			}
			
		}else if(idCurrent.substring(0,idButtonSupprimerPrincipale.length)==idButtonSupprimerPrincipale){

			var idJob=idCurrent.split("_")[1];

			// Get the status of the main checkbox
			// if checked, delete the job
			// if not, delete the childrens
			var idRelatedCheckBox="checkboxPrincipale_"+idJob+"_";
			if($('#'+idRelatedCheckBox).prop('checked')){
			    if (window.confirm("Voulez-vous réellement supprimer l'ensemble du job "+idJob+" ?")){
					// Show Progress Windows
					showProgress('#inProgressDialog');
					// Proceed with deletion			
				    document.forms[0].elements('userAction').value = actionSupprimer;
				    document.forms[0].elements('selectedJob').value = idJob;
				    var idImg= "imgExpand_"+idJob;
					if($('#'+idImg).attr('src')==iconCollapse){
					    document.forms[0].elements('expandedJob').value = idJob;
					}
					document.forms[0].submit();
			    }
			}else{
				var allStatusId = "";
				var nbLines = 0;
				// check the children status (checked) and get the ids
				$('#tableauPrincipal [type=checkbox]').each(function (iIndex) {
					var currentCheckBoxId=this.id;
					var checkBoxEnfant = "checkboxEnfant_"+idJob+"_";
					if(currentCheckBoxId.substring(0,checkBoxEnfant.length)==checkBoxEnfant){
						nbLines++;
						if($('#'+currentCheckBoxId).prop('checked')){
							var statusId = currentCheckBoxId.split("_")[2];
							if(allStatusId.length>0){
								allStatusId+=",";	
							}
							allStatusId+=statusId;
						}
					}
				});
				// We got the status, we can proceed with the deletion
				if(allStatusId.length>0){
					// Job entier
					if(nbLines == allStatusId.split(",").length){
					    if (window.confirm("Voulez-vous réellement supprimer l'ensemble du job "+idJob+" ?")){
							// Show Progress Windows
							showProgress('#inProgressDialog');
							// Proceed with deletion			
						    document.forms[0].elements('userAction').value = actionSupprimer;
						    document.forms[0].elements('selectedJob').value = idJob;
						    var idImg= "imgExpand_"+idJob;
							if($('#'+idImg).attr('src')==iconCollapse){
							    document.forms[0].elements('expandedJob').value = idJob;
							}
							document.forms[0].submit();
					    }
					}else{
						// Sélection d'éléments
					    if (window.confirm("Voulez-vous réellement supprimer les éléments sélectionnés ?")){
							// Show Progress Windows
							showProgress('#inProgressDialog');
							// Proceed with deletion			
						    document.forms[0].elements('userAction').value = actionSupprimer;
						    document.forms[0].elements('selectedJob').value = idJob;
						    document.forms[0].elements('selectedStatus').value = allStatusId;
						    var idImg= "imgExpand_"+idJob;
							if($('#'+idImg).attr('src')==iconCollapse){
							    document.forms[0].elements('expandedJob').value = idJob;
							}
							document.forms[0].submit();
					    }
					}
				}else{
					alert("Pas d'élément sélectionné, aucune action ne sera générée");
				}
			}
			
		}else{
			alert('Bouton non pris en charge :'+idCurrent);
		}
	});

	// Refresh the screen
	// avec le bon job expanded
	// ----------------------------------------------------------
	$('#refreshPageImg').click(function() {
		var s_location = "" + location;
		if(expandedJob!=null && expandedJob!="null"){
			var locationWithoutParameters = "";
			if(s_location.indexOf("&")>=0){
				locationWithoutParameters=s_location.split("&")[0];
			}else{
				locationWithoutParameters=s_location;
			}
			var newLocation= locationWithoutParameters +"&expandedJob="+expandedJob;
			location.href=newLocation;
		}else{
			location.reload();
		}
	});	

	
});

/**
 * Toggle the visibility of an graphic element
 */
function toggleOpacityElement(idElement, opacityHidden, opacityShown){
	if($('#'+idElement).css('opacity') <= opacityHidden) {
		setOpacityElement(idElement,opacityShown);
		setDisabledElement(idElement, false);
	}else{
		setOpacityElement(idElement, opacityHidden);
		setDisabledElement(idElement, true);
	}		
}

/**
 * set the opacity of a specific element
 */
function setOpacityElement(idElement, myOpacity){
	$('#'+idElement).css({opacity:myOpacity});
}

/**
 * set the disabled attribute of a specific element
 */
function setDisabledElement(idElement, toDisabled){
	if(toDisabled){
		$('#'+idElement).prop('disabled','disabled');
	}else{
		$('#'+idElement).removeProp('disabled');
	}
}
