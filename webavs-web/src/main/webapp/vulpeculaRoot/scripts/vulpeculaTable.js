/**
 * Table permettant l'ajout, l'édition et la suppression de ligne. 
 * La modification et la validation se font par le bouton de l'écran.
 * 
 * Un bug persiste lors de l'utilisation de la notation data-g-calendar dans le template.
 */
vulpeculaTable = {
	create: function(m_options) {
		var table = {
	
			$containerTable: null,
			$lineTemplate: null,
		    
			/**
			 * Paramètres de le l'objet qui vont être utilisés pour son initialisation
			 * Cet élément est obligatoire. Si aucune option, le créer vide (options:{})
			 */
			options: {
				s_template_selector: "#lineTemplate",
				s_table_selector: "#tblParametre",
				s_message_erreur: "Erreur : une ligne comprend des champs obligatoire vide !",
				getValuesToSend: null,
				getExtrasParameters: null //Fonction retournant un objet avec les paramètres qui sont externes à la vulpeculaTable
			},
			/**
			 * Initialise le tableau
			 */
			init: function () {
				this.$lineTemplate = $($(this.options.s_template_selector).html());
				this.$containerTable = $(this.options.s_table_selector);
				
				// Ajoute le bouton "ADD"
				$(this.options.s_table_selector + " thead").prepend("<tr><td style='text-align: left;' colspan='6'><img src='images/list-add.png' title='Ajouter' alt='+' id='btnAddParametre' class='bouton' style='display: none' /></td></tr>");
				$that = this;
				// Traite les évènement
				this.$containerTable.on("click", "#btnAddParametre", function() { $that.addRow(); });
				this.$containerTable.on("click", ".btnDeleteParametre", function() { $that.deleteRow(this); });
			},
			
			initNonAjax : function () {
				this.$lineTemplate = $($(this.options.s_template_selector).html());
				this.$containerTable = $(this.options.s_table_selector);
				
				// Ajoute le bouton "ADD"
				$(this.options.s_table_selector + " thead").prepend("<tr><td style='text-align: left;' colspan='6'><img src='images/list-add.png' title='Ajouter' alt='+' id='btnAddParametre' class='bouton' style='display: none' /></td></tr>");
				$that = this;
				// Traite les évènement
				this.$containerTable.on("click", "#btnAddParametre", function() { $that.addRow(); });
				this.$containerTable.on("click", ".btnDeleteParametre", function() { $that.deleteRow(this); });
			},
			/**
			 * Reinitialise le tableau
			 */
			clear: function () {
				// On cache le bouton d'ajout
				$("#btnAddParametre").hide();
				// On vide le tableau
				$(this.options.s_table_selector + " tbody").empty();			
			},
			/**
			 * Renvoit des parametres en JSON 
			 */
			save: function () {
				var t_parametre = this.getTableauParametres();

				$(".btnDeleteParametre").hide();
				$("#btnAddParametre").hide();

				var o_map = {};
				o_map["parametres"] = ajaxUtils.jsonToString(t_parametre);
				return o_map;
			},
			saveAsString : function() {
				var t_parametre = this.getTableauParametres();

				$(".btnDeleteParametre").hide();
				$("#btnAddParametre").hide();
				
				return ajaxUtils.jsonToString(t_parametre);
			},
			saveAsStringWithJSON : function() {
				var t_parametre = this.getTableauParametres();

				$(".btnDeleteParametre").hide();
				$("#btnAddParametre").hide();
				
				return JSON.stringify(t_parametre);
			},
			saveAsStringWithJSONNonAjax : function() {
				var t_parametre = this.getTableauParametresNonAjax();

				$(".btnDeleteParametre").hide();
				$("#btnAddParametre").hide();
				
				return JSON.stringify(t_parametre);
			},
			getTableauParametres : function() {
				var t_parametre = [];
				$that = this;
				$(this.options.s_table_selector + " tbody tr").each(function () {
					var $this = $(this);				
					var s_idLine = $this.attr('id');
					var s_statut = $this.attr('statut');
					if(typeof(s_idLine)  === "undefined") {
						s_idLine = '';
					}
					
					var map = $that.getValuesToSend($this);
					if(typeof $that.options.getExtrasParameters === 'function') {
						var extras = $that.options.getExtrasParameters();
					}
					var parametres = $.extend(map, extras);
					parametres.id = s_idLine;
					parametres.statut = s_statut;	

					t_parametre.push(map);
				});
				return t_parametre;
			},
			
			getTableauParametresNonAjax : function() {
				var t_parametre = [];
				$that = this;
				$(this.options.s_table_selector + " tbody tr").each(function () {
					var $this = $(this);
					if($this.is(":visible")){
						var s_idLine = $this.attr('id');
						var s_statut = $this.attr('statut');
						if(typeof(s_idLine)  === "undefined") {
							s_idLine = '';
						}
						
						var map = $that.getValuesToSend($this);
						if(typeof $that.options.getExtrasParameters === 'function') {
							var extras = $that.options.getExtrasParameters();
						}
						var parametres = $.extend(map, extras);
						parametres.id = s_idLine;
						parametres.statut = s_statut;	

						t_parametre.push(map);
					}
				});
				return t_parametre;
			},
			/**
			 * Retourne une map contenant les valeurs à sauver.
			 * Cette fonction peut-être surchargée.
			 */
			getValuesToSend: function ($this) {
				if(typeof this.options.getValuesMap === "function"){
					return this.options.getValuesMap($this);
				} else {
					var map = ajaxUtils.createMapForSendData($this, '.');
					return map;
				}
			},
			/**
			 * Renvoit un clone du template
			 * @returns
			 */
			getLineTemplate: function () {
				var lineCloned = this.$lineTemplate.clone(false,false);
				lineCloned.append("<td style='background-color: #F2F5F7;'><img src='images/edit-delete.png' class='btnDeleteParametre bouton' title='supprimer' alt='[X]'/></td>");		
				return lineCloned;
			},
			/**
			 * Charge le tableau avec les paramètres
			 */
			load: function (t_parametres) {
				// boucle sur les parametres
				var $conteneur = $(this.options.s_table_selector + " tbody");
				$that = this;
				
				//$conteneur.detach();
				if (t_parametres != null && t_parametres.length > 0) {

					for (var i = 0; i < t_parametres.length; i++) {
						// Pour chaque parametre on créé le detail				
						var $lineCurrent = $that.getLineTemplate();
						// Gérer les CSS
						if ($($that.options.s_table_selector + ' tr').length % 2 == 1) {
							$lineCurrent.addClass("bmsRowEven");
						} else {
							$lineCurrent.addClass("bmsRowOdd");
						}

						$conteneur.append($lineCurrent);
						
						var o_parametre = t_parametres[i];
						$lineCurrent.attr('id', o_parametre.id);
						ajaxUtils.defaultLoadData(o_parametre, $lineCurrent, ".");
						
						$(this.options.s_table_selector).trigger("element.loaded",[$lineCurrent,o_parametre]);
					}
					
					// Traite les notations contenues dans le parametre
					notationManager.addNotationOnFragment($conteneur);

					$(".btnDeleteParametre").hide();
					$("#btnAddParametre").hide();
				}
			},
			
			/**
			 * Ajoute une nouvelle ligne vide avec le statut "new"
			 */
			addRow: function () {
				var $mandatoriesFields = $(this.options.s_table_selector + " tbody tr:last").find(".mandatory");
				if ($mandatoriesFields.length > 0 && $mandatoriesFields.val().length == 0) {
					$(this.options.s_table_selector + " tr:last").find(".mandatory").addClass('warning');
				} else {
					var $lineNew = this.getLineTemplate();
					$lineNew.attr("statut", "new");		
					if ($(this.options.s_table_selector + ' tr').not(":hidden").length % 2 == 1) {
						$lineNew.addClass("bmsRowEven");
					} else {
						$lineNew.addClass("bmsRowOdd");
					}
					
					$(this.options.s_table_selector + " tbody").append($lineNew);
					// Traite les notations contenues dans le parametre
					notationManager.addNotationOnFragment($lineNew);
				}
			},
			
			/**
			 * Montre les boutons d'édition.
			 * Ajoute un evenement "change" sur les champs editable afin de modifier 
			 * le statut de la ligne à "modified" lorsqu'un champ est modifié
			 */
			editRow: function () {
				$(".btnDeleteParametre").show();
				$("#btnAddParametre").show();
				$('.btnAjaxUpdate').hide();
				
				$(this.options.s_table_selector + " tbody").find("input, select").each(function() {
					$(this).on("change", function() {
						$(this).closest("tr").attr('statut', 'modified');
					});
				});
			},
			
			/**
			 * Cache la ligne supprimée et modifie l'état à "deleted"
			 */
			deleteRow: function (imgDelete) {
				var row = $(imgDelete).closest("tr");

				row.find('input,select,textarea').animate({'backgroundColor':'#fb6c6c'}, 200);
				row.find('input,select,textarea').slideUp(200, function() {
					if(row.attr("id")) {
						row.attr('statut', 'deleted');
						row.hide();
					} else {
						row.remove();
					}
				});
				
				// Gére les CSS
				row.nextAll('tr').toggleClass('bmsRowOdd bmsRowEven');
			}
		};
					
		var obj = Object.create($.extend(true, {}, table));
		obj.options = $.extend(obj.options, m_options);			
		obj.init();
		return obj;
	}
};