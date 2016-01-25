globazNotation.echeance = {

	author : 'DMA',
	forTagHtml : 'input',
	type : globazNotation.typesNotation.BUSINESS_NOTATION,

	description : "Permet d'ajouter une échance dans libra. Permet aussi de visuliser les différentes échances de types manuels",

	descriptionOptions : {
		idExterne : {
			desc : "Id du dossier de la personne",
			param : "String",
			mandatory : false
		},
		idTiers : {
			desc : "Id tiers de la peronne qui se trouve dans le dossier",
			param : "String",
			mandatory : false
		},
		csDomaine : {
			desc : "Code système du domaine pour la visualisation et la création des échances",
			param : "String",
			mandatory : false
		},
		libelle : {
			desc : "Libelle que l'on veut afficher par défaut dans le dialgue",
			param : "String| ou fonction qui retroune une String",
			mandatory : false
		},
		mandatory : {
			desc : "Défini si l'élément est obligatoire",
			param : "true, false(default)",
			mandatory : false
		}

	},

	options : {
		idExterne : null,
		idTiers : null,
		csDomaine : null,
		position : 'right',
		libelle : null,
		mandatory : false,
		display : "normale",
		tiers : null,
		type : "",
		className : "ch.globaz.prestation.business.services.models.echeance.EcheanceServiceJson"// "ch.globaz.libra.business.services.EcheancesService"
	},

	view : {
		templateDetailCreate : '<div>'
				+ '<div class="content">'
				+ '<form">'
				+ '<div class="notationEcheanceDateRappel">'
				+ '<label class="labelColor" for="dateRappel">{{labelDateRappel}} </label>'
				+ '<input data-g-calendar="mandatory:true" id="dateRappel" type="text" value=""></input>'
				+ '</div>'
				+ '<div>'
				+ '<label class="labelColor" for="libelle"> {{labelLibelle}}</label>'
				+ '<input data-g-string="mandatory:true, addSymboleMandatory:false" value="" id="libelle" type="text" style="width:100%"> </input>'
				+ '</div>'
				+ '<div>'
				+ '<label class="labelColor" for= "description">{{labelRemarque}}</label>'
				+ '<textarea id="description" style="width:100%;height:150px"></textarea>'
				+ '</div>'
				+ '<div class="btnAjax">'
				+ '<input class="btnAjaxValid" type="button" value="{{btnNewLabel}}"> </input>'
				+ '</div>' + '</form>' + '</div>' + '</div>',

		templateDetailUpdate : '<div>'
				+ '<div class="content">'
				+ '<form data-id="{{id}}">'
				+ '<div>'
				+ '<label class="labelColor" for="dateRappel">{{labelDateRappel}} </label>'
				+ '<input data-g-calendar="mandatory:true" id="dateRappel" type="text" value="{{dateEcheance}}"></input>'
				+ '<input id="idEcheance" type="hidden" value="{{id}}"></input>'
				+ '</div>'
				+ '<div>'
				+ '<label class="labelColor" for="libelle"> {{labelLibelle}}</label>'
				+ '<input data-g-string="mandatory:true, addSymboleMandatory:false" value="{{libelle}}" id="libelle" type="text" style="width:100%"> </input>'
				+ '</div>'
				+ '<div>'
				+ '<label class="labelColor" for= "remarque">{{labelRemarque}}</label>'
				+ '<textarea id="description" style="width:100%;height:150px">{{description}}</textarea>'
				+ '</div>'
				+ '<div class="btnAjax">'
				+ '<input class="btnAjaxValid" type="button" value="Modifier"> </input>'
				+ '</div>' + '</form>' + '</div>' + '</div>',

		templateListe : '<div class="ui-widget echeanceListe">'
				+ '<div class="content">' + '<table>' + '<thead>'
				+ '<tr>{{header}}</tr>' + '</thead>' + '<tbody>' + '{{liste}}'
				+ '</tbody>' + '</table>' + '</div>' + '</div>',

		templateListeHeader : '<th>{{libelle}}</th><th>{{dateEcheance}}</th>',

		templateListeHeaderWithAll : '<th>{{libelle}}</th><th>{{dateEcheance}}</th><th>{{etat}}</th><th>Actions</th>',

		templateListeHeaderIdExterne : '<th>Mbr. fam.</th><th>{{libelle}}</th><th>{{dateEcheance}}</th><th>{{etat}}</th><th>Actions</th>',

		templateLigneLibra : '<tr><td>{{libelle}}</td><td class="date">'
				+ '<a data-g-externallink="reLoad:false, selectorForClose:#btnVal" href="libra?userAction=libra.journalisations.echeancesDetail.afficher&selectedId={{idJournalisation}}">'
				+ '{{dateRappel}}' + '</td></tr>',

		templateLigne : '<tr>' + '<td>{{libelle}}</td>' + '<td class="date">'
				+ '{{dateEcheance}}' + '</td>' + '</tr>',

		templateLigneIdExtenre : '<tr data-id="{{id}}" class="{{classEchue}}">'
				+ '<td>'
				+ '{{nom}} '
				+ '{{prenom}}'
				+ '</td>'
				+ '<td><a href="#">{{libelle}}</a></td>'
				+ '<td>{{dateEcheance}}</td>'
				+ '<td>{{etat}}</td>'
				+ '<td class="{{actions}}">'
				+ '<span class="btn"><a title="Marquer comme traité" href="#" class="traitee"><span class="ui-icon ui-icon-green ui-icon-check"></span></a></span>'
				+ '<span class="btn"><a title="Supprimer" href="#" class="supprimer"><span class="ui-icon ui-icon-red ui-icon-trash"></span></a></span>'
				+ '</td>' + '</tr>',

		templateLigneAll : '<tr data-id="{{id}}" class="{{classEchue}}">'
				+ '<td><a href="#">{{libelle}}</a></td>'
				+ '<td>{{dateEcheance}}</td>'
				+ '<td>{{etat}}</td>'
				+ '<td class="{{actions}}">'
				+ '<span class="btn"><a title="Marquer comme traité" href="#" class="traitee"><span class="ui-icon ui-icon-green ui-icon-check"></span></a></span>'
				+ '<span class="btn"><a title="Supprimer" href="#" class="supprimer"><span class="ui-icon ui-icon-red ui-icon-trash"></span></a></span>'
				+ '</td>' + '</tr>'

	},

	vars : {
		$replaceElementToPutObject : null,
		$img : null
	},

	bindEvent : {
		ajaxShowDetail : function() {

		},
		ajaxDisableEnableInput : function() {
			// this.enableDisable();
		}
	},

	init : function($elementToPutObject) {
		this.$elementToPutObject.wrap("<span class='notationEchance'>");
		var that = this;
		var css = {
			"margin" : 0,
			"padding" : 0,
			"border-color" : "#DCDCDC",
			"border-style" : "solid",
			"border-width" : "1px",
			"background-color" : "white",
			display : "inline-block",
			height : /* this.$elementToPutObject.height() + */"18px",
			width : /* this.$elementToPutObject.width() + */"104px",
			color : this.$elementToPutObject.css("color")
		};

		this.vars.$replaceElementToPutObject = $("<div>", {
			css : css,
			"class" : "echanceCopie"
		// "class": this.$elementToPutObject.attr("class"),
		// id: this.$elementToPutObject.attr("id") + "echanceCopie"
		});
		this.$elementToPutObject.parent().append(
				this.vars.$replaceElementToPutObject);
		this.$elementToPutObject.hide();
		// this.$elementToPutObject.css({width: "104px"});

		if (this.options.mandatory) {
			this.utils.input
					.addSymboleMandatory(this.vars.$replaceElementToPutObject);
		}
		this.addIcon();

		this.$elementToPutObject.change(function() {
			if (this.value) {
				that.vars.$replaceElementToPutObject.text(this.value);
			}
		});

		if (!this.utils.isEmpty(this.options.idExterne)
				&& !this.utils.isEmpty(this.options.idTiers)) {

			var options = {
				serviceClassName : this.options.className,
				serviceMethodName : "findNearestTerm",
				parametres : this.options.idExterne + ","
						+ this.options.idTiers + "," + this.options.type + ","
						+ this.options.csDomaine,
				criterias : '',
				cstCriterias : '',
				callBack : function(donne) {
					if (donne) {
						that.vars.$replaceElementToPutObject
								.text(donne.dateEcheance);
					}
				}
			};

			this.exectueAjax(options);
		}

		// this.$elementToPutObject.focus(function () {
		// that.displayDialog();
		// });
	},

	createImage : function() {
		var $img = $("<img>", {
			src : this.getImage("deadLine16.png")
		});
		return $img;
	},

	addIcon : function() {
		var that = this;
		var $img = this.createImage();
		var idTimer;
		var idTimer2;

		this.vars.$img = $img
		$img.mouseenter(function() {
			idTimer2 = setTimeout(function() {
				$img.css("cursor", "wait");
			}, 300);

			idTimer = setTimeout(function() {
				that.listEcheance();
			}, 800);
		});

		$img.mouseleave(function() {
			clearTimeout(idTimer);
			that.vars.$img.css("cursor", "pointer");
			clearTimeout(idTimer2);
		});

		$img.click(function() {
			that.displayDialog();
			clearTimeout(idTimer);
		});

		this.vars.$replaceElementToPutObject.after($img);
		$img.wrap("<a href='#'>");

		this.vars.$replaceElementToPutObject.parent().find("a").keypress(
				function(e) {
					if (e.keyCode === 13) { // ENTER
						that.displayDialog();
						clearTimeout(idTimer);
					} else if (e.keyCode === 0) { // TAB
						that.listEcheance();
						clearTimeout(idTimer);
					}
				});

		// this.$elementToPutObject.prop("disabled", true);
	},

	listEcheance : function(all) {
		var that = this, serviceMethodName;

		var call = {
			"byIdExeterne" : {
				treat : "findToTreatByIdExterneAndType",
				all : "findByIdExterneAndType",
				param : this.options.idExterne + "," + this.options.type + ","
						+ this.options.csDomaine
			},
			"normale" : {
				treat : "findToTreatByIdExterneAndIdTiersAndType",
				all : "findByIdExterneAndIdTiersAndType",
				param : this.options.idExterne + "," + this.options.idTiers
						+ "," + this.options.type + ","
						+ this.options.csDomaine
			}
		};

		var methodesToCall = call[this.options.display];
		serviceMethodName = methodesToCall.treat;
		if (all && all.length) {
			serviceMethodName = methodesToCall.all;
		}

		var options = {
			serviceClassName : this.options.className,
			serviceMethodName : serviceMethodName,
			parametres : methodesToCall.param,
			criterias : '',
			cstCriterias : '',
			callBack : function(donne) {
				that.vars.$img.css("cursor", "pointer");
				that.displayListe(donne);

			}
		};

		this.exectueAjax(options);
	},

	displayDialog : function(echeance) {
		var $html, $img, $containerTitle, $text, that;
		that = this;
		$html = this.createHtml(echeance);

		$html.find(".btnAjaxValid").button().click(function() {
			that.createEcheance($html);
		});
		$img = this.createImage();
		$img.wrap("<span>");
		$containerTitle = $img.parent();
		var s_title = "<span>" + $containerTitle.html()
				+ "</span><span style='vertical-align:top'>"
				+ this.i18n("titleDetail") + "</span>";
		$html.hide();
		$("body").append($html);
		$html.dialog({
			show : "clip",
			hide : "clip",
			close : function(event, ui) {
				$html.remove();
				$html = null;
			},
			title : s_title
		});
		$html.find("input").eq(0).focus();

	},

	displayListe : function(donne) {
		var o_lignes, s_html, that = this, templateHeader, templateLigne;

		templateHeader = this.view.templateListeHeaderWithAll;
		templateLigne = this.view.templateLigneAll;

		if (this.options.display) {
			templateHeader = this.view.templateListeHeaderIdExterne;
			templateLigne = this.view.templateLigneIdExtenre;
		}

		o_lignes = {
			liste : "",
			header : templateHeader,
			libelle : this.i18n("libelle"),
			type : "Type",
			etat : " Etat",
			supprimer : "Sup.",

			dateEcheance : this.i18n("dateEcheance")
		};

		for (var i = 0; i < donne.length; i++) {
			if (donne[i].libelle.length > 30) {
				donne[i].libelle = donne[i].libelle.substring(0, 30) + "...";
			}
			if (donne[i].codeEtat !== 'ANNULEE') {
				donne[i].actions = "actions";
				if (donne[i].codeEtat == 'ECHUE') {
					donne[i].classEchue = "echue";
				} else {
					donne[i].classEchue = "";
				}
			} else {
				donne[i].actions = "hide";
			}

			o_lignes.liste += globazNotation.template.compile(donne[i],
					templateLigne);
		}

		s_html = globazNotation.template.compile(o_lignes,
				this.view.templateListe);

		var notation = that.vars.$img
				.notationBubble({
					ajax : true,
					wantMarker : false,
					title : this.i18n("titleList")
							+ '<span class="afficherTout" style="float:right">Afficher tout</span>',
					position : that.options.position,
					padding : 0
				});

		s_html = notation.createTemplate().replace('{innerHTML}', s_html);
		notation.vars.$buble = $(s_html);
		notationManager.addNotationOnFragment(notation.vars.$buble);

		$("body").append(notation.vars.$buble);
		notation.vars.$buble.on("click", ".supprimer", function() {
			var id = $(this).closest("tr").data("id");

			$(this).blink(300);
			var options = {
				serviceClassName : that.options.className,
				serviceMethodName : "delete",
				parametres : id + "",
				criterias : '',
				cstCriterias : '',
				callBack : function(data) {
					$("[data-id='" + data.id + "']").remove();
				}
			};

			that.exectueAjax(options);

		});

		notation.vars.$buble.find("a").eq(0).focus();

		notation.vars.$buble.on("click", ".afficherTout", function() {
			notation.vars.$buble.remove();
			that.listEcheance("all");
		});

		notation.vars.$buble.on("click", ".traitee", function() {
			var id = $(this).closest("tr").data("id");
			$(this).blink(300);
			var options = {
				serviceClassName : that.options.className,
				serviceMethodName : "toggleEtatToTraiteeOrPlanifiee",
				parametres : id + "",
				criterias : '',
				cstCriterias : '',
				callBack : function(data) {
					$("[data-id='" + data.id + "']").remove();
				}
			};

			that.exectueAjax(options);

		});

		notation.vars.$buble.css({
			"max-width" : "750px"
		});
		notation.vars.$buble.on("click", "td:not(.actions)", function(event) {
			var id = $(this).closest("tr").data("id");

			var options = {
				serviceClassName : that.options.className,
				serviceMethodName : "read",
				parametres : id + "",
				criterias : '',
				cstCriterias : '',
				callBack : function(data) {
					notation.vars.$buble.remove();
					that.echeance = data;
					that.displayDialog(data);
				}
			};

			that.exectueAjax(options);

		});

		notation.vars.$buble.hide();
		notation.open();
		notation.vars.$buble.mouseleave(function() {
			notation.close();
			notation.vars.$buble.remove();
		});

	},

	createEcheance : function($html) {
		var that = this, s_dateRappel = $html.find("#dateRappel").val(), s_text = $html
				.find("#libelle").val(), id = $html.find("#idEcheance").val(), idTiers = $html
				.find("#notationEcheanceIdTiers").val(), serviceMethodName, s_remarque = $html
				.find("#remarque").val();

		if (s_dateRappel.length && s_text.length) {
			$("#imageInfo").remove();
			var $img = $("<img>", {
				id : "imageInfo",
				"class" : "imageInfo",
				src : this.getImage("loading.gif")
			});

			$html.find(".btnAjaxAdd").after($img);

			var echeance = {
				idExterne : this.options.idExterne,
				idTiers : idTiers || this.options.idTiers,
				codeDomaine : this.options.csDomaine,
				codeType : this.options.type,
				dateEcheance : $html.find("#dateRappel").val(),
				libelle : s_text,
				description : $html.find("#description").val()
			};

			if (that.echeance) {
				echeance = $.extend(true, that.echeance, echeance);
				serviceMethodName = "update";
			} else {
				echeance.codeEtat = "PLANIFIEE";
				serviceMethodName = "add";
			}

			s_param = ajaxUtils.jsonToString(echeance);
			var options = {
				serviceClassName : this.options.className,
				serviceMethodName : serviceMethodName,
				parametres : s_param,
				criterias : '',
				cstCriterias : '',
				callBack : function(donne) {
					$img.attr("src", that.getImage("symbol_check24.png"));
					that.$elementToPutObject.prop("disabled", true);
					that.$elementToPutObject.val(s_dateRappel);
					that.vars.$replaceElementToPutObject.text(s_dateRappel);
					that.$elementToPutObject.prop("disabled", true);
					setTimeout(function() {
						$html.dialog("close");
						that.$elementToPutObject.focusNextInputField();
					}, 400);
				}
			};

			this.exectueAjax(options);
		}
	},

	createHtml : function(echeance) {
		var s_html, $html, libelle = this.options.libelle;

		if (typeof libelle === "function") {
			libelle = libelle(this.$elementToPutObject);
		}

		var o_data = {
			btnNewLabel : this.i18n("btnNewLabel"),
			labelRemarque : this.i18n("remarque"),
			labelLibelle : this.i18n("libelle"),
			labelDateRappel : this.i18n("labelDateRappel")
		};

		if (echeance) {
			s_html = globazNotation.template.compile(o_data,
					this.view.templateDetailUpdate);
			s_html = globazNotation.template.compile(echeance, s_html);
		} else {
			s_html = globazNotation.template.compile(o_data,
					this.view.templateDetailCreate);
		}

		$html = $(s_html);
		var nb = 0;
		if (this.options.tiers) {
			var htmlSelect = '<div>	<label class="labelColor" for="notationEcheanceIdTiers">Membre de famille </label>'
					+ '<select  style="width:100%" name="notationEcheanceIdTiers" id="notationEcheanceIdTiers" >';
			for ( var key in this.options.tiers) {
				htmlSelect = htmlSelect + '<option value="' + key + '">'
						+ this.options.tiers[key] + '</option>';
				nb++;
			}
			if (nb > 1) {
				htmlSelect = htmlSelect + '</select></div>';
				$html.find(".notationEcheanceDateRappel").append(htmlSelect);
			}
		}

		notationManager.addNotationOnFragment($html);
		return $html;
	},

	exectueAjax : function(options) {
		var ajax = Object.create($.extend(true, {}, globazNotation.readwidget));
		ajax.options = options;
		ajax.read();
	}

};
