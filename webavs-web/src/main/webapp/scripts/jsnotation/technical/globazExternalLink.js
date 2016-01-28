/**
 * le nom du nouvelle objet(json) doit être écrit en MINUSCUL. Nous allons ajouter un nouvelle objet "nouvellenotation" à notre espace de nom "globazNotation" Toutes les fonctions
 * est attributs décrit ci-dessous son obligatoire...
 */
globazNotation.externallink = {
	/** ****************************DOCUMENTATION************************************** */
	author: "DMA",

	forTagHtml: "a",
	type: globazNotation.typesNotation.TECHNICAL_NOTATION,

	description: "Créer une fenêtre de type dialogue (modale). Celle-ci contiendra une iframe." + "La source de l’iframe et définit dans l’éléments ou se trouve la notation donc la source " + "corresponds à la vleur de l’attribut href.<br />" + "ATTENTION si une erreur est detecter le dialogue ne se fermera pas.<br />" + "On peut définir quels éléments de l’iframe fermera le dialogue et si il faut rafraichir la page courante.<br />" + "(Fonctionne seulement avec le même domaine)",

	descriptionOptions: {
		selectorForClose: {
			desc: "Induique les éléments qui seront binder(click) dans l'iframe pour la fermeture du dialogue",
			param: "String: Ex:#btnPreValid,#btnCan,#btnVal",
			mandatory: false,
			type: String
		},
		reLoad: {
			desc: "Indique s’il faut recharger la page appelante après la fermeture du dialogue.<br />" + "Si une erreur est detecté le rechargement ne se fera pas!",
			param: "true(default), false"
		},
		forceReload: {
			desc: "Indique s’il faut forcer le chargerment de la page appelante sur le click de l' élément qui a été définit" + " dans l'option \"selectorForClose\"",
			param: "true, false (default)"
		},
		autoResize: {
			desc: "Si la page charger est plus petite que la fenêtre (frame) le dialogue sera automatiquement ajuster en hauteur",
			param: "true(default), false"
		}
	},

	options: {
		selectorForClose: "",
		reLoad: true,
		forceReload: false,
		autoResize: true
	},

	$iframe: {},
	n_maxHeight: 0,
	n_width: 0,
	$dialog: null,
	n_index: null,

	init: function ($elementToPutObject) {
		this.n_maxHeight = $(window).height() - 60;
		this.n_width = $(window).width() - 60;
		this.bindEvent();
		this.n_index = this.utils.generateUniqueNumber();
		$elementToPutObject.addClass("external_link");
	},

	reload: function () {
		if (this.options.reLoad) {
			window.location.reload();
		}
	},

	getErrorInframe: function () {
		var errorObj = null, frame = null;

		frame = window.frames["iframe" + this.n_index];
		if (frame === undefined) { // spéciallement pour IE :(
			frame = this.$iframe[0].contentWindow;
		}

		if (frame !== undefined) {
			errorObj = frame.errorObj;
			if (errorObj === undefined) {
				errorObj = frame.b_errors;
			} else {
				errorObj = errorObj.text();
			}
		}

		if (errorObj === undefined || errorObj === "") {
			errorObj = null;
		}

		return errorObj;
	},

	/**
	 * Resize la hauteur du dialogue
	 */
	autoResize: function () {
		if (this.options.autoResize) {
			var $body = this.$iframe.contents().find('body'), $children = $body.children().not("script"), n_height = 0, that = this, $mainElementForHeight = {};

			if ($children.length > 1) {
				// Sépicalement pour IE :( Si on ne wrap pas on a pas la bonne hauteur
				$children.wrapAll('<div>');
				$mainElementForHeight = $body.children('div');
			} else {
				$mainElementForHeight = $children;
			}

			n_height = $mainElementForHeight.height() + 60;

			if (n_height < that.n_maxHeight) {
				this.$iframe.dialog("option", "height", n_height);
				this.$iframe.css({
					width: that.n_width - 8,
					height: (n_height - 20)
				});
			}
		}
	},

	/**
	 * Lie un selecteur s'il est fourni afin de fermer le dialogue
	 */
	bindEventForClose: function () {
		var that = this, $ele = null, $contents = null;

		this.$iframe.load(function () {

			// Suppression de l'image de loading
			that.$dialog.find('.loading').remove();
			that.$iframe.show();
			that.$iframe.closest('div').css('height', 'auto');

			that.autoResize();

			if (!that.utils.isEmpty(that.options.selectorForClose)) {
				$contents = $(that.$iframe.contents());
				$contents.find('body').css("width", that.n_width - 40);
				$ele = $contents.find(that.options.selectorForClose);

				$ele.click(function () {
					if (that.options.forceReload) {
						that.reload();
					}
					that.$iframe.load(function () {
						if (that.utils.isEmpty(that.getErrorInframe())) {
							that.reload();
						}
					});
				});
			}
		});
	},

	/**
	 * Lie les différents éléments aux événements
	 */
	bindEvent: function () {
		var that = this;
		this.$elementToPutObject.click(function (event) {
			event.preventDefault();
			if (that.$dialog === null) {
				that.createIframe();
				that.createDialogue();
			}
			that.$iframe.dialog("open");
			// that.$iframe.hide();
			that.$iframe.css({
				width: that.n_width - 8,
				height: that.n_maxHeight
			});
			that.bindEventForClose();
		});
	},

	createDialogue: function () {
		var that = this;
		this.$iframe.dialog({
			autoOpen: false,
			position: ["center", "center"],
			width: that.n_width,
			height: that.n_maxHeight,
			modal: true,
			resizable: false,
			draggable: true,
			close: function () {
				that.reload();
			}
		});
		// On ajout une div entourante pour que le dialogue pense avoir un contnue // HACK
		// that.$iframe.wrap("<div style='height:"+that.n_maxHeight+"'>");
		this.$dialog = this.$iframe.dialog("widget");
		$("<img />", {
			src: that.s_contextUrl + "/scripts/jsnotation/imgs/loading_big.gif",
			"class": "loading",
			style: "position:absolute; z-index:1000; text-align:center; left:" + ((that.n_width / 2) - 70) + "px; top:" + ((that.n_maxHeight / 2) - 70) + "px"
		}).appendTo(that.$dialog);
	},

	createIframe: function () {
		var that = this;
		this.$iframe = $("<iframe>", {
			name: "iframe" + this.n_index,
			src: this.$elementToPutObject.attr('href'),
			style: "margin:0;padding:0;width:" + (that.n_width - 20) + "px",
			scrolling: "yes"
		});
	}
};