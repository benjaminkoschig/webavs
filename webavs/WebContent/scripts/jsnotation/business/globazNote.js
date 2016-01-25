globazNotation.globalVariables.note = {
	map: {}
};

globazNotation.globalVariables.count = function () {
	var s_param, that = this, s_ids = "", o_param, 
		o_notationNote, key, map = globazNotation.globalVariables.note.map;

	for (key in map) {
		s_ids += key + ",";
	}
	s_ids = s_ids.substr(0, s_ids.length - 1);
	o_notationNote = map[key];
	if (map && key && s_ids.length) {
		o_param = {
			forTableSource: o_notationNote.options.tableSource,
			forSourceIdsIn: s_ids
		};
		
		var options = {
			serviceMethodName: "findNotByIdsExtern",
			parametres: ajaxUtils.jsonToString(o_param),
			callBack: function (data) {
				for (var key in map) {
					if (!map[key].options.inList || (map[key].options.inList && data[key])) {
						s_ids += key + ",";
						$("[data-notationNoteIdExtern=" + key + "]");
						map[key].initNote((data[key]) ? data[key].length : 0);
					}
				}
			}
		};
		globazNotation.note.executeAjax(options);
	}
};

jsManager.addAfter(function () {
	globazNotation.globalVariables.count();
}, "ex");

globazNotation.note = {

	author: 'DMA',
	forTagHtml: 'span',
	type: globazNotation.typesNotation.BUSINESS_NOTATION,

	description: "Permet de gérer les postit. Si aucun id externe n'est trouvé l'icone du postit ne s'affichera pas! ",

	descriptionOptions: {
		idExterne: {
			desc: "Id externe lié à la prise de note",
			param: "String[paramétre obligatoire] ",
			mandatory: false
		},
		tableSource: {
			desc: "Nom de la table lié à l'idExetrne",
			param: "String[paramétre obligatoire]",
			mandatory: true
		},
		executeCount: {
			desc: "Definit si on veut indiqué visuellement, si une note existe et afficher le nombre de note",
			param: "Boolean(default: true)",
			mandatory: false
		},
		inList: {
			desc: "Indique que le postit se trouve dans une liste, cela permet de ne pas afficher l'icone si il n'y pas de note",
			param: "Boolean(default: true)",
			mandatory: false
		}
	},

	options: {
		idExterne: null,
		tableSource: null,
		executeCount: true,
		inList: false
	},

	view: {
		detail: '<form id="notationDetailNote">' + 
					'<div>' + 
						'<input id="idNote" type="hidden"> </input>' + 
						'<input id="spy" type="hidden"> </input>' + 
						'<input id="user" type="hidden"> </input>' + 
						'<input id="date" type="hidden"> </input>' + 
						'<label class="labelColor" for="description">{{labelDescription}} </label>' + 
						'<input data-g-string="mandatory:true" id="description" style="width:96%" type="text"> </input>' + 
					'</div>' + 
					'<div>' + 
						'<label class="labelColor" for"memo">{{labelNote}}</label>' + 
						'<textarea id="memo" style="width:100%;height:200px"></textarea>' + 
					'</div>' + 
					'<div class="btnAjax">' + 
						'<input class="btnAjaxCancel" type="button" value="{{btnCancelLabel}}">' +
						'<input class="btnAjaxAdd" type="button" value="{{btnNewLabel}}">' + 
					'</div>' + 
				'</form>',

		dialogue: '<div id="notationDialogContent" class="ui-widget notationNote" style="overflow:hidden" >' + 
					'<div id="notationTablelNote" class="content">' + 
						'<span class="ajouter">Ajouter</span>' + 
						'<table width="100%" class="" >' + 
							'<thead>' + 
								'<tr><th>{{labelDate}}</th><th>{{labelUser}}</th><th>{{labelDescription}}</th><th> {{labelAction}} </th></tr>' + 
							'</thead>' + 
							'<tbody data-bind="ajaxLignes">' + 
								'{{@each lignes}}' + 
								'<tr id="{{idNote}}" class="{{@odd odd}}" >' + 
									'<td>{{date}}</td>' + 
									'<td>{{user}}</td>' + 
									'<td>{{description}}</td>' + 
									'<td class="action">' + 
										'<span class="update ui-state-default ui-corner-all"  style="width:20px; height:20px; display:none; margin-right:4px"></span>' + 
										'<span class="delete ui-state-default ui-corner-all"  style="width:20px; height:20px; display:none"></span>' + 
									'</td>' + 
								'</tr>' + 
								'<tr class="{{@odd odd}} hide displayMemo" >' + 
									'<td colspan="4">{{memo}}</td>' + 
								'</tr>' + 
								'{{/@each lignes}}' + 
							'</tbody>' + 
						'</table>' + 
					'</div>' +
				'</div>'

	},

	bindEvent: {
		ajaxShowDetail: function () {

		}
	},

	vars: {
		$imgTrigger: null
	},

	init: function ($elementToPutObject) {
		if (this.options.executeCount && this.options.idExterne && this.options.idExterne !== 0 && this.options.idExterne !== "null") {
			globazNotation.globalVariables.note.map[this.options.idExterne] = this;
			this.$elementToPutObject.attr("data-notationNoteIdExtern", this.options.idExterne);
		} else {
			this.initNote();
		}
	},

	initNote: function (n_nbNote) {
		// this.$elementToPutObject.wrap("<span class='notationNote'>");
		this.$elementToPutObject.addClass("notationNoteImgNote");
		if(this.options.idExterne && this.options.idExterne !== 0 && this.options.idExterne !== "null"){
			this.addIcon(n_nbNote);
		}
	},

	getContent: function () {
		return $("#notationDialogContent");
	},

	getTable: function () {
		return $("#notationTablelNote");
	},

	getDetail: function () {
		return $("#notationDetailNote");
	},

	createImage: function (s_image) {
		var $img = $("<img>", {
			src: this.getImage(s_image),
			"class": "cursorHand"
		});
		return $img;
	},

	createSpanNbNote: function (n_nbNote) {
		return "<span class='nbNoteIcone'>" + n_nbNote + "</span>";
	},

	createImageIcon: function (n_nbNote) {
		var s_image, s_spanNombre = "", $img;
		if (n_nbNote && n_nbNote > 0) {
			s_image = "note_full22.png";
			s_spanNombre = this.createSpanNbNote(n_nbNote);
		} else {
			s_image = "note_empty22.png";
		}

		$img = $("<span class='notationNote'><img src='" + this.getImage(s_image) + "' class='cursorHand'>" + s_spanNombre + "</span>");
		return $img;
	},

	addIcon: function (n_nbNote) {
		var that = this, $img;
		$img = this.createImageIcon(n_nbNote);
		$img.click(function (e) {
			e.stopImmediatePropagation();
			that.searchNote();
			that.vars.$imgTrigger = $(this);
		});
		this.$elementToPutObject.append($img);
		return $img;
	},

	beforeAjax: function ($html) {
		$("#imageInfo").remove();
		var $img = $("<img>", {
			id: "imageInfo",
			"class": "imageInfo",
			src: this.getImage("loading.gif")
		});
		$html.overlay({
			id: "noteOverlay",
			zIndex: 10000
		});

		$html.find(".btnAjaxAdd").after($img);
	},

	callBackCrud: function (data, b_delete) {
		var that = this;
		$("#imageInfo").attr("src", this.getImage("symbol_check24.png"));
		$("#noteOverlay").remove();
		if (!b_delete) {
			setTimeout(function () {
				that.slide();
			}, 400);
		}
		this.searchNote(function (dataSearch) {
			var $nbNoteIcone, $html, $tr;
			/*
			 * var id = 'crudNotationNote_'+data.idNote; $('#'+id).remove(); that.getTable().find("#"+data.idNote).after("<tr id='"+id+"'><td colspan='4'>" + data.memo + "</td>");
			 */
			$html = that.getContent();
			$tr = $html.find("#" + data.idNote).next();
			that.slideTr($tr, that.getContent());

			$nbNoteIcone = that.vars.$imgTrigger.remove();
			that.vars.$imgTrigger = that.addIcon(dataSearch.length);
		});
	},

	executeAjax: function (options, b_delete) {
		var that = this, o_options, ajax;
		o_options = {
			serviceClassName: "ch.globaz.jade.noteIt.business.service.JadeNoteService",
			criterias: '',
			cstCriterias: '',
			callBack: function (data) {
				that.callBackCrud(data, b_delete);
			},
			errorCallBack: null
		};
		o_options = $.extend(o_options, options);
		ajax = Object.create($.extend(true, {}, globazNotation.readwidget));
		ajax.options = o_options;
		ajax.read();
	},

	executeSave: function ($html) {
		var s_param, options, ajax, s_action, o_param, that;
		that = this;

		o_param = {
			tableSource: this.options.tableSource,
			idSource: this.options.idExterne,
			description: $html.find("#description").val(),
			memo: $html.find("#memo").val(),
			spy: $html.find("#spy").val(),
			user: $html.find("#user").val(),
			date: $html.find("#date").val(),
			idNote: $html.find("#idNote").val()
		};

		if ($.trim(o_param.description).length) {

			this.beforeAjax($html);
			s_param = ajaxUtils.jsonToString(o_param);

			if ($.trim(o_param.idNote)) {
				s_action = "update";
			} else {
				s_action = "create";
			}
			options = {
				serviceMethodName: s_action,
				parametres: s_param
			};

			this.executeAjax(options);
		}
	},

	searchNote: function (callBack) {
		var s_param, o_param = {
			forSourceId: this.options.idExterne,
			forTableSource: this.options.tableSource,
			forUser: "",
			forDateCreation: ""
		}, that = this;
		s_param = ajaxUtils.jsonToString(o_param);

		var options = {
			serviceMethodName: "search",
			parametres: s_param,
			callBack: function (data) {
				var $notationTablelNote = that.getTable();
				if (!$notationTablelNote.length) {
					that.displayDialog(data);
				} else {
					globazNotation.template.update({
						lignes: data
					}, "ajaxLignes");
				}
				if (typeof callBack === "function") {
					callBack(data);
				}
			}
		};

		this.executeAjax(options);
	},

	deletNote: function (element) {
		var $tr = $(element).closest("tr"),
		    s_param, options, ajax, 
		    that = this;
		
	    options = {
			serviceMethodName: "delete",
			parametres: $tr.attr("id")
		};
	    
		if (confirm(this.i18n("delete"))) {
			this.beforeAjax(that.getTable());
			this.executeAjax(options, true);
		}
	},

	updateNote: function (element) {
		var $tr = $(element).closest("tr");
		var s_param, options, ajax, that = this;
		var $detail = this.displayDetail();
		
		options = {
			serviceMethodName: "read",
			parametres: $tr.attr("id"),
			callBack: function (data) {
				$detail.find("#idNote").val(data.idNote);
				$detail.find("#memo").val(data.memo);
				$detail.find("#description").val(data.description);
				$detail.find("#spy").val(data.spy);
				$detail.find("#user").val(data.user);
				$detail.find("#date").val(data.date);
				that.slide(true);
				$detail = null;
			}
		};
		that.executeAjax(options);
	},

	slide: function (b_notClearInputs) {
		var $detail = this.getDetail();
		var $table = this.getTable();
		if ($detail.is(":hidden")) {
			$detail.find("#imageInfo").remove();
			if (!b_notClearInputs) {
				$detail.find(":input").clearInputs();
			}
			$table.hide("slide", {
				direction: "down"
			}, 400, function () {
				
				$detail.show("slide", {
					direction: "up"
				}, 400, function () {
					$detail.find("#description").focus();
				});
			
			});

		} else {
			$detail.hide("slide", {
				direction: "up"
			}, 400, function () {
				$table.show("slide", {
					direction: "down"
				}, 400);
			});

		}
	},

	displayDetail: function () {
		var $detail = this.getDetail();
		if (!$detail.length) {
			this.createDetail(this.getContent());
		}
		return $detail;
	},
	
	createDetail: function ($html) {
		var $detatil, s_detail, o_label, that = this;
		o_label = {
			labelDescription: this.i18n("labelDescription"),
			labelNote: this.i18n("labelnote"),
			btnNewLabel: this.i18n("btnNewLabel"),
			btnCancelLabel: this.i18n("btnCancelLabel")

		};
		s_detail = globazNotation.template.compile(o_label, this.view.detail);
		$detatil = $(s_detail);
		notationManager.addNotationOnFragment($detatil);
		$detatil.hide();
		$html.prepend($detatil);
		$html.find(".btnAjaxAdd").button().click(function () {
			that.executeSave($html);
		});
		$detatil.find(".btnAjaxCancel").button().click(function () {
			that.slide();
		});
	},

	displayDialog: function (o_data) {
		var $html, $img, $containerTitle, $text, that;
		that = this;
		$html = this.createHtml(o_data);

		// $img = this.createImage();
		// $img.wrap("<span>");
		// $containerTitle = $img.parent();
		// $text = $("<span>", {
		// text: this.i18n("titleDetail"),
		// css: {"vertical-align": "top"}
		// });
		// $containerTitle.append($text); 
		
		var height = 350;
		if (!jQuery.support.boxModel) {
			height = 485;
		}
		$html.hide();
		$("body").append($html);
		$html.dialog({
			show: "clip",
			hide: "clip",
			height: height, 
			/*minHeight: 350,*/
			width: 450,
			close: function (event, ui) {
				$html.remove();
				$html = null;
			},
			title: $containerTitle,
			open: function (event, ui) {
				if (o_data.length === 0) {
					that.displayDetail();
					that.slide();
				}
			}
		});
	
		
		if (!jQuery.support.boxModel) {
			$html.dialog("option", "height", height);
		}

		$html.find("input").eq(0).focus();
	},

	addButtonCrud: function ($html) {
		var that = this;
		$html.find(".delete").button({
			text: false,
			icons: {
				primary: 'ui-icon-trash'
			}
		}).click(function () {
			that.deletNote(this);
		});
		$html.find(".update").button({
			text: false,
			icons: {
				primary: 'ui-icon-pencil'
			}
		}).click(function () {
			that.updateNote(this);
		});
	},

	slideTr: function ($tr, $html) {
		var $trVisible = $html.find(".notationNoteMemoShow");
		$trVisible.hide();
		$trVisible.removeClass("notationNoteMemoShow");
		$tr.show();
		$tr.addClass("notationNoteMemoShow");
	},

	createHtml: function (o_data) {
		var s_html, $html, that = this;
		var o_label = {
			labelDescription: this.i18n("labelDescription"),
			labelAction: this.i18n("labelAction"),
			labelNote: this.i18n("labelnote"),
			btnNewLabel: this.i18n("btnNewLabel"),
			labelId: 'N°',
			labelDate: 'Date',
			labelUser: 'User',
			lignes: o_data
		};

		globazNotation.template.findBindTempalte(this.view.dialogue, "ajaxLignes");

		$html = globazNotation.template.compile$(o_label, this.view.dialogue);
		$html.on("mouseover", "tr", function () {
			var $this = $(this);
			if (!$this.hasClass("displayMemo")) {
				if (!$this.find(".update").hasClass("ui-button")) {
					that.addButtonCrud($this);
				}
				that.slideTr($this.next(), $html);
				$this.addClass("hover");
				$this.find(".delete, .update").show();
			}
		});
		$html.on("mouseout", "tr", function () {
			var $this = $(this);
			$this.removeClass("hover");
			$this.find(".delete, .update").hide();
		});
		$html.find(".ajouter").button().click(function () {
			that.displayDetail();
			that.slide();
		});

		notationManager.addNotationOnFragment($html);
		return $html;
	}

};