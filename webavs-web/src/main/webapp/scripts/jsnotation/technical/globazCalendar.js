/**
 * @author SCE
 */
globazNotation.calendar = {

	author: 'SCE',
	forTagHtml: 'input',
	type: globazNotation.typesNotation.TECHNICAL_NOTATION,

	description: "Cet objet ajoute au champ input de type text et un calendrier lié<br/>" + "<br/><b>Utilisation clavier (avec [TAB]):</b><br/>" + "mmaa, mm.aa, mmaaaa, mm.aaaa, pour le candrier mensuel<br/>" + "ddmmaa, ddmmaaaa, dd.mm.aa, dd.mm.aaaa, pour le calendrier standard.<br/>" + "Le [.] suivi de [TAB] complète avec la date actuelle.",

	descriptionOptions: {
		type: {
			desc: "Type du calendrier",
			param: "default(defaut)|month"
		},
		lang: {
			desc: "Internationalisation du calendrier",
			param: "fr(defaut)|de|it"
		},
		mandatory: {
			desc: "Défini si l'élément est obligatoire",
			param: "true, false(default)"
		},
		addSymboleMandatory: {
			desc: "Ajoute le symoble '*'. Si le paramètre mandatory est à true ce paramètre n'est pas utilisé",
			param: "true(default)|false"
		},
		yearRange: {
			desc: "Permet de définir la plage des annnées affichée dans le select",
			param: "example: 2010:2020"
		},
		currentDate: {
			desc: "Initialise par défaut le calendrier à la date actuelle du système",
			param: "true, false(default)"
		}
	},

	vars: {
		
	},
	
	/**
	 * Paramètres de le l'objet qui vont être utilisés pour son initialisation Cet élément est obligatoire. S'il n'y a pas d'option, le créer vide (options:{})
	 */
	options: {
		type: 'default',
		lang: '',
		mandatory: false,
		addSymboleMandatory: true,
		yearRange: "",
		currentDate:false
	},

	imgSrc: '',
	mainDivId: '',// identifiant unique de l'objet
	$mainDiv: '',// objet JQuery principal
	$btnIncYear: '',// bouton incrément année
	$btnDecYear: '',// bouton décrément année
	$lblYear: '',// Label des années

	// Constantes
	DEFAULT_CAL: 'default',// calendrier par défaut, mensuel
	TOUCHE_TAB: 9,// Code touche tab
	DEFAULT_CENTURY_SWITCHER: 47,// valeur à laquelle on passe au siècle suivant
	LANG_ITALIEN: 'it',
	LANG_ALLEMAND: 'de',
	LANG_FRANCAIS: 'fr',
	REGEXP_DATE: /(^[0-9\.]*$)/, // [0123456789\.]*
	
	// Membres
	previousValue: null,

	/**
	 * Ce paramètre est facultatif.<br/> Il permet des lancer des fonctions sur différent types d'évenements.<br/> Liste des événements :<br/>
	 * <ul>
	 * <li>boutons standard de l'application. Les événements se lancent sur le clique du bouton</li>
	 * <ul>
	 * <li>btnCancel</li>
	 * <li>btnAdd</li>
	 * <li>btnValidate</li>
	 * <li>btnUpdate</li>
	 * <li>btnDelete</li>
	 * </ul>
	 * <li>AJAX: tous ces fonctions se lancent à la fin de la fonction dans AJAX</li>
	 * <ul>
	 * <li>ajaxShowDetailRefresh</li>
	 * <li>ajaxLoadData</li>
	 * <li>ajaxShowDetail</li>
	 * <li>ajaxStopEdition</li>
	 * <li>ajaxValidateEditon</li>
	 * <li>ajaxUpdateComplete</li>
	 * </ul>
	 * </ul>
	 */
	bindEvent: {
		ajaxShowDetailRefresh: function (event) {
			this.utils.input.testColor(this.options, this.$elementToPutObject);
		},

		ajaxDisableEnableInput: function () {
			this.enableDisableInput();
		},

		btnUpdate: function () {
			this.enableDisableInput();
		}
	},

	enableDisableInput: function () {
		var that = this;

		this.utils.input.changeColorDisabled(this.$elementToPutObject);
		if (!this.utils.input.isDisabled(this.$elementToPutObject)) {
			if (that.options.type === this.DEFAULT_CAL) {
				if (that.$elementToPutObject.datepicker) {
					that.$elementToPutObject.datepicker('enable');
				}
			} else {
				that.addImgButtonClickBehavior(that.$elementToPutObject);
				that.$elementToPutObject.next().css('opacity', '1');
			}
		} else {
			if (that.options.type === this.DEFAULT_CAL) {
				if (that.$elementToPutObject.datepicker) {
					that.$elementToPutObject.datepicker('disable');
				}
			} else {
				that.$elementToPutObject.next().css('opacity', '0.5');
			}
		}
	},

	/**
	 * Cette fonction est obligatoire car c'est elle qui va initialiser l'objet
	 */
	init: function () {
		if(notationManager.b_debug) console.log('init calendar');
		var that = this;
		var scriptOrStyle = null;
		if (this.options.type === this.DEFAULT_CAL) {
			// Calendrier standard
			this.imgSrc = this.getImage('calendar.gif');
		} else {
			// Calendrier mensuel
			this.imgSrc = this.getImage('calendar_month.gif');
		}

		// génération identifiant unique
		this.mainDivId = "mainDiv" + this.utils.generateUniqueNumber();

		// récupération langue système
		if (this.options.lang === '') {
			var langSession = this.utils.getLangue();// $('[name=User-Lang]').prop('content');
			this.options.lang = langSession.toLowerCase();
		}
		// Gestion mandatory notation
		this.utils.input.addAllPropertyFromUtil(this.options, that.$elementToPutObject);

		// Test type et init calendrier en fonction du type
		if (this.options.type === this.DEFAULT_CAL) {
			//on test si on a la lib du datepicker sinon on la télécharge
			if (!$.ui.datepicker && !globazNotation.globalVariables.addJsDatepicker) {
				scriptOrStyle = document.createElement('script');
				scriptOrStyle.type = 'text/javascript';
				scriptOrStyle.src = this.s_contextUrl + "/scripts/jsnotation/jquery.ui.datepicker.js";
				document.getElementsByTagName('head')[0].appendChild(scriptOrStyle);
				globazNotation.globalVariables.addJsDatepicker = true;
			}
			//On bind les évènements avant que le calendrier soit chargé de manière asynchrone.
			this.initTabBeaviorForStandardCal();
			setTimeout(function () {
				that.startAsync(); 
			}, 100);
		} else {
			// Calendrier mensuel
			this.initMonthCalendar();
		}
	},
	
	startAsync: function () {
		var  i = 0, that = this;
		while (!$.ui.datepicker && i < 1000) {
			i++;
		}
		if (!$.ui.datepicker) {
			setTimeout(function () {
				that.startAsync();
			}, 5);
		} else {
			// Calendrier standard
			that.initStandardCalendar();
		}
	},

	/**
	 * Fonction qui initialise les paramètres de style CSS, et de largeur du champ
	 */
	cssFormatField: function (type, lang) {
		var that = this;
		// style spécifique pour calendrier mensuel
		if (type !== that.DEFAULT_CAL) {
			// verrou max char
			that.$elementToPutObject.prop('maxlength', '7');
			// width 100px
			that.$elementToPutObject.css('width', '105px');
			if (lang === that.LANG_ITALIEN) {
				$('.ui-datepicker-header_mc').addClass('uidpickerheadermc');
				that.$btnIncYear.addClass('btnIncYearI');
				that.$lblYear.addClass('lblYearI');
			} else {
				$('.ui-datepicker-header_mc').addClass('uidpickerheadermc');
				that.$btnIncYear.addClass('btnIncYearS');
				that.$lblYear.addClass('lblYearS');
			}

			$('.ui-datepicker td').css({
				'font-size': ' 10px'
			});
			$('.monthPickerClass').find('.ui-dialog-content').css({
				'width': ' 100px'
			});
			$('.monthPickerClass').find('.ui-dialog-titlebar-close').css({
				'display': 'none'
			});
			// Bouton décréments
			this.$btnDecYear.addClass('btnDecYear');
		} else {
			// verrou max char
			that.$elementToPutObject.prop('maxlength', '10');
			// width 100px
			that.$elementToPutObject.css('width', '105px');
		}
	},

	/**
	 * fonction qui initialise les noms du calendrier en fonction de la langue
	 */
	regionalize: function (type, lang) {
		if (type === this.DEFAULT_CAL) {
			
			$.datepicker.regional['fr'] = {
				closeText: 'Fermer',
				prevText: '&#x3c;Préc',
				nextText: 'Suiv&#x3e;',
				currentText: 'Courant',
				monthNames: ['Janvier', 'Février', 'Mars', 'Avril', 'Mai', 'Juin', 'Juillet', 'Août', 'Septembre', 'Octobre', 'Novembre', 'Décembre'],
				monthNamesShort: ['Jan', 'Fév', 'Mar', 'Avr', 'Mai', 'Jun', 'Jul', 'Aoû', 'Sep', 'Oct', 'Nov', 'Déc'],
				dayNames: ['Dimanche', 'Lundi', 'Mardi', 'Mercredi', 'Jeudi', 'Vendredi', 'Samedi'],
				dayNamesShort: ['Dim', 'Lun', 'Mar', 'Mer', 'Jeu', 'Ven', 'Sam'],
				dayNamesMin: ['Di', 'Lu', 'Ma', 'Me', 'Je', 'Ve', 'Sa'],
				weekHeader: 'Sm',
				dateFormat: 'dd.mm.yy',
				firstDay: 1,
				duration: 'fast',
				isRTL: false,
				showMonthAfterYear: false,
				yearSuffix: ''
			};
			$.datepicker.regional['de'] = {
				closeText: 'schließen',
				prevText: '&#x3c;zurück',
				nextText: 'Vor&#x3e;',
				currentText: 'heute',
				monthNames: ['Januar', 'Februar', 'März', 'April', 'Mai', 'Juni', 'Juli', 'August', 'September', 'Oktober', 'November', 'Dezember'],
				monthNamesShort: ['Jan', 'Feb', 'Mär', 'Apr', 'Mai', 'Jun', 'Jul', 'Aug', 'Sep', 'Okt', 'Nov', 'Dez'],
				dayNames: ['Sonntag', 'Montag', 'Dienstag', 'Mittwoch', 'Donnerstag', 'Freitag', 'Samstag'],
				dayNamesShort: ['So', 'Mo', 'Di', 'Mi', 'Do', 'Fr', 'Sa'],
				dayNamesMin: ['So', 'Mo', 'Di', 'Mi', 'Do', 'Fr', 'Sa'],
				weekHeader: 'Wo',
				dateFormat: 'dd.mm.yy',
				firstDay: 1,
				isRTL: false,
				showMonthAfterYear: false,
				yearSuffix: ''
			};
			$.datepicker.regional['it'] = {
				closeText: 'Chiudi',
				prevText: '&#x3c;Prec',
				nextText: 'Succ&#x3e;',
				currentText: 'Oggi',
				monthNames: ['Gennaio', 'Febbraio', 'Marzo', 'Aprile', 'Maggio', 'Giugno', 'Luglio', 'Agosto', 'Settembre', 'Ottobre', 'Novembre', 'Dicembre'],
				monthNamesShort: ['Gen', 'Feb', 'Mar', 'Apr', 'Mag', 'Giu', 'Lug', 'Ago', 'Set', 'Ott', 'Nov', 'Dic'],
				dayNames: ['Domenica', 'Luned&#236', 'Marted&#236', 'Mercoled&#236', 'Gioved&#236', 'Venerd&#236', 'Sabato'],
				dayNamesShort: ['Dom', 'Lun', 'Mar', 'Mer', 'Gio', 'Ven', 'Sab'],
				dayNamesMin: ['Do', 'Lu', 'Ma', 'Me', 'Gi', 'Ve', 'Sa'],
				weekHeader: 'Sm',
				dateFormat: 'dd/mm/yy',
				firstDay: 1,
				isRTL: false,
				showMonthAfterYear: false,
				yearSuffix: ''
			};
			// Set langage
			$.datepicker.setDefaults($.datepicker.regional['']);
			$.datepicker.setDefaults($.datepicker.regional[lang]);
		} else {
			// régionalisation
			var monthName = [];
			if (lang === "fr") {
				monthName = ['Janvier', 'Février', 'Mars', 'Avril', 'Mai', 'Juin', 'Juillet', 'Août', 'Septembre', 'Octobre', 'Novembre', 'Décembre'];
			}
			if (lang === "de") {
				monthName = ['Januar', 'Februar', 'März', 'April', 'Mai', 'Juni', 'Juli', 'August', 'September', 'Oktober', 'November', 'Dezember'];
			}
			if (lang === "it") {
				monthName = ['Gennaio', 'Febbraio', 'Marzo', 'Aprile', 'Maggio', 'Giugno', 'Luglio', 'Agosto', 'Settembre', 'Ottobre', 'Novembre', 'Dicembre'];
			}

			return monthName;
		}
	},

	/**
	 * Fonction de gestion des événements
	 */
	initEvents: function () {
		var that = this;

		// Fonction pour lock champ si formulaire locké
		that.$elementToPutObject.change(function () {
			that.enableDisableInput();
		});
		// Gestion regexp des caractères autorisés
		that.$elementToPutObject.keyup(function (e) {
			if (that.$elementToPutObject.val().search(that.REGEXP_DATE) < 0) {
				var arr = that.$elementToPutObject.val();
				var newArr = (arr.substr(arr.lenght - 2, arr.length - 1));
				that.$elementToPutObject.val(newArr);
				that.$elementToPutObject.change();
			}
		});
		// Gestion des events pour le calendrier mensuel
		if (this.options.type !== this.DEFAULT_CAL) {
			this.initEventsForMonthCal(that.$elementToPutObject);
		}
	},

	/**
	 * Fonction de gestion des événements claviers, touche Tab, pour le calendrier standard
	 */
	initTabBeaviorForStandardCal: function () {
		var that = this;
		that.$elementToPutObject.change(function(event) {
			that.formatAndHandleError(that.formatStandardCalendarField,event);
		});
		
		//Sauvegarde de l'ancienne valeur
		that.$elementToPutObject.focus(function(event) {
			var prevValue = that.$elementToPutObject.val();
			that.$elementToPutObject.prevValue = prevValue;
		});
	},

	/**
	 * Gestion des événements clavier et tabs pour le calendrier mensuel
	 */
	initTabBeaviorForMonthCal: function () {
		var that = this;
		that.$elementToPutObject.change(function (event) {
			that.formatAndHandleError(that.formatMonthCalendarField,event);
		});

		that.$elementToPutObject.keyup(function (event) {
			if (that.$elementToPutObject.val().length === 6 && that.$elementToPutObject.val().indexOf('.') === -1) {
				that.formatAndHandleError(that.formatMonthCalendarField,event);
			}
		});
		
		//Sauvegarde de l'ancienne valeur
		that.$elementToPutObject.focus(function(event) {
			var prevValue = that.$elementToPutObject.val();
			that.$elementToPutObject.prevValue = prevValue;
		});
	},
	formatAndHandleError : function(formatFunction, event) {
		var that = this;
		var result = formatFunction.call(that);
		if(result.error) {
			that.$elementToPutObject.val('');
			//Permet le comportement standard (mandatory) lorsque l'on force le vidage du champ (couleur rouge)
			this.$elementToPutObject.trigger('change.mandatory');
			//En utilisant un timeout à 0, on place la fonction dans un thread séparé. Il sera ainsi traité comme un callback après la perte de focus.
			setTimeout(function() {
				that.$elementToPutObject.focus();
			},0);
			if(notationManager.b_debug) console.log("error | stopImmediatePropagation");
			event.stopImmediatePropagation();
		}
		
		if(!result.changed) {
			if(notationManager.b_debug) console.log("value not changed | stopImmediatePropagation");
			event.stopImmediatePropagation();				
		}			
	},

	/**
	 * Méthode gérant le comportement de fermeture du calendrier sur clic à l'éxterieur
	 * 
	 * @param {Object}
	 *            $elementToPutObject
	 */
	setCalendarMonthButtonEnable: function () {
		this.$elementToPutObject.next().unbind('click');
		this.addImgButtonClickBehavior();
	},

	/**
	 * Méthode retournant l'année formatéée suivant le nombre de caractère
	 * 
	 * @param {Object}
	 *            year
	 */
	formatYear: function (year) {
		if (year.length === 2) {
			if (year > this.DEFAULT_CENTURY_SWITCHER) {
				year = "19" + year;
			} else {
				year = "20" + year;
			}
			return year;
		} else {
			return year;
		}
	},

	/**
	 * Retourne la date formatée suivant le type de calendrier
	 * 
	 * @param {Object}
	 *            year
	 * @param {Object}
	 *            month
	 * @param {Object}
	 *            day, si null ou pas défini. N'est pas traité pour calendrier mensuel
	 */
	formatDate: function (year, month, day) {
		if (day == undefined || day == null || day === 'undefined' || day == 0) {
			if (month < 10) {
				month = "0" + month;
			}
			return month + "." + year;
		} else {
			if (day < 10) {
				day = "0" + day;
			}
			if (month < 10) {
				month = "0" + month;
			}
			return day + "." + month + "." + year;
		}
	},

	/**
	 * Initialisation du calendrier mensuel
	 * 
	 * @param {Object}
	 *            $elementToPutObject
	 */
	initMonthCalendar: function () {
		var that = this;

		// Gestion de la régionalisation
		var monthName = this.regionalize(this.options.type, this.options.lang);
		// Création du balisage html
		var divMonthCalendarStr = "<div class='dialCalendar' id='" + this.mainDivId + "'>" + "<div class='ui-datepicker-div'>" + "<div>" + "<table class='monthCalTable" + this.mainDivId + " ui-datepicker-calendar'>" + "<tr>" + "<td month='01' class='ui-state-default'>" + monthName[0] + "</td>" + "<td month='02' class='ui-state-default'>" + monthName[1] + "</td>" + "</tr>" + "<tr>" + "<td month='03' class='ui-state-default'>" + monthName[2] + "</td>" + "<td month='04' class='ui-state-default'>" + monthName[3] + "</td>" + "</tr>" + "<tr>" + "<td month='05' class='ui-state-default'>" + monthName[4] + "</td>" + "<td month='06' class='ui-state-default'>" + monthName[5] + "</td>" + "</tr>" + "<tr>" + "<td month='07' class='ui-state-default'>" + monthName[6] + "</td>" + "<td month='08' class='ui-state-default'>" + monthName[7] + "</td>" + "</tr>" + "<tr>" + "<td month='09' class='ui-state-default'>" + monthName[8] + "</td>" + "<td month='10' class='ui-state-default'>" + monthName[9] + "</td>" + "</tr>" + "<tr>" + "<td month='11' class='ui-state-default'>" + monthName[10] + "</td>" + "<td month='12' class='ui-state-default'>" + monthName[11] + "</td>" + "</tr>" + "</table>" + "</div>" + "</div>" + "</div>";
		// Ajout de l'image
		$('<img class="btnOpenCal' + this.mainDivId + '" src="' + this.imgSrc + '" />').insertAfter(that.$elementToPutObject);

		// récupération de la date déjà entrée dans le champ (s'il y en a une), le calendrier commencera à l'année de la date déjà entrée
		var s_anneePourCalendar = '';
		if (that.$elementToPutObject.val() != '') {
			var s_formattedDate = this.utils.date.convertMonthDateToFullDate(that.$elementToPutObject.val());
			if (this.utils.date._isValidGlobazDate(s_formattedDate)) {
				s_anneePourCalendar = this.utils.date.convertGlobazYearDateToJSDate(s_formattedDate).getFullYear();
			}
		} else {
			s_anneePourCalendar = new Date().getFullYear();
		}

		// Création du dialogue
		var n_width = 0;
		var n_height = 330;

		if (this.options.lang === this.LANG_ITALIEN) {
			n_width = 180;
		} else {
			n_width = 210;
		}

		if ($.support.boxModel) {
			n_width = 180;
			n_height = 180;
		}

		$(divMonthCalendarStr)
				.appendTo('body')
				.dialog({
					autoOpen: false,
					resizable: false,
					draggable: false,
					width: n_width,
					height: n_height,
					dialogClass: 'monthPickerClass',
					title: '<div class="ui-datepicker-header_mc">' + '<a class="ui-datepicker-prev ui-corner-all">' + '<span class="btnDecYear' + that.mainDivId + ' ui-icon ui-icon-circle-triangle-w">' + '</span>' + '</a>' + '<span class="lblYear' + that.mainDivId + ' ui-datepicker-year">' + s_anneePourCalendar + '</span>' + '<a>' + '<span class="btnIncYear' + that.mainDivId + ' ui-icon ui-icon-circle-triangle-e">' + '</span>' + '</a>' + '<div class="ui-datepicker-title">' + '</div>' + '</div>'
				});

		// Set les variables de l'objet
		this.$mainDiv = $('#' + this.mainDivId);
		this.$btnDecYear = $('.btnDecYear' + this.mainDivId);
		this.$btnIncYear = $('.btnIncYear' + this.mainDivId);
		this.$lblYear = $('.lblYear' + this.mainDivId);

		// Formattage css
		this.cssFormatField(this.options.type, this.options.lang);
		// Gestion events
		this.initEvents();
		// Events for tab
		this.initTabBeaviorForMonthCal();
		// sI BOUTON MODIFIER INLINE, DESACTIVER
		if (this.utils.input.isDisabled(that.$elementToPutObject)) {
			that.$elementToPutObject.next().unbind();
			that.$elementToPutObject.next().css('opacity', '0.5');
		}
		// placeholder
		that.$elementToPutObject.prop('placeholder', 'mm.aaaa');
		
		// Gestion de la date system par défaut
		if(this.options.currentDate==true){
			that.$elementToPutObject.val(globazNotation.utilsDate.convertDateToMontDate(globazNotation.utilsDate.convertJSDateToGlobazStringDateFormat()));
		}
	},

	/**
	 * Initialisation du calendrier standard
	 * 
	 * @param {Object}
	 *            $elementToPutObject
	 */
	initStandardCalendar: function () {
		var that = this;
		// Gestion de la regionalisation
		this.regionalize(this.options.type, this.options.lang);
		// Création du calendrier
		that.$elementToPutObject.datepicker({
			showOn: 'button',
			buttonImage: that.imgSrc,
			buttonImageOnly: true,
			constrainInput: false,
			dateFormat: 'dd.mm.yy',
			changeYear: true,
			onSelect: function() {
				that.$elementToPutObject.change();
			}
		});
		// Gestion de la date system par défaut
		if(this.options.currentDate==true){
			that.$elementToPutObject.datepicker('setDate',new Date());
		}
		// Hack sinon une partie du calendrier s'affiche : problème version dans jquery-ui 1.8.12
		that.$elementToPutObject.datepicker('widget').hide();
		if (this.options.yearRange.length) {
			that.$elementToPutObject.datepicker("option", "yearRange", this.options.yearRange);
		}
		// Formattage css
		this.cssFormatField(this.options.type, this.options.lang);
		// Gestion events
		this.initEvents();

		// Si formulaire locké
		if (this.utils.input.isDisabled(that.$elementToPutObject)) {
			that.$elementToPutObject.datepicker('disable');
		}
	},

	/**
	 * Initialisation des évenements pour le calendrier mensuel
	 * 
	 * @param {Object}
	 *            $elementToPutObject
	 */
	initEventsForMonthCal: function () {
		var that = this;
		this.setCalendarMonthButtonEnable();

		this.$btnDecYear.click(function () {
			var year = parseInt(that.$lblYear.text(), 10);
			that.$lblYear.text(year - 1);
		});

		that.$elementToPutObject.blur(function () {

			that.formatMonthCalendarField(that.$elementToPutObject);
		});

		this.$btnIncYear.click(function () {
			var year = parseInt(that.$lblYear.text(), 10);
			that.$lblYear.text(year + 1);
		});

		// events td, clic sur les mois
		$('.monthCalTable' + that.mainDivId + ' tr td').each(function () {
			var $this = $(this);
			$this.click(function () {

				// récupération de l'année
				var year = that.$lblYear.text();
				var month = $this.attr('month');
				var strDate = month + "." + year;

				that.$elementToPutObject.val(strDate);

				that.$mainDiv.dialog('close');

				that.$elementToPutObject.change();

			});

			$this.hover(function () {
				$this.addClass('ui-state-hover');
			});
			$this.mouseout(function () {
				$this.removeClass('ui-state-hover');
			});
		});
	},

	getYearFromField: function () {
		var str = this.$elementToPutObject.val();
		if (str.length === 4 || str.length === 6) {
			return (this.formatYear(str.substr(2)));
		}
		if (str.length === 5 || str.length === 7) {
			return (this.formatYear(str.substr(3)));
		}
		var d = new Date();
		return d.getFullYear();
	},

	addImgButtonClickBehavior: function () {
		var that = this;
		// images ouverture calendrier
		that.$elementToPutObject.next().click(function () {
			// ajout div wrap pour fermeture calendrier
			// $('body').children().first().wrap('<div class="eventDivCal'+that.mainDivId+'" />');
			$(document).bind("click.calendarNoation" + that.mainDivId, function (event) {
				var $target = $(event.target);
				if (!$target.closest(".monthPickerClass").length && !$target.is(".btnOpenCal" + that.mainDivId)) {
					$('.dialCalendar').dialog('close');
					$(document).unbind("click.calendarNoation" + that.mainDivId);
				}

				if ($target.is(".btnOpenCal" + that.mainDivId)) {
					if (that.$mainDiv.length === 0) {
						that.$mainDiv = $('#' + that.mainDivId);
					}
					that.$mainDiv.dialog('open');
				}
			});

			// position du text input
			var position = that.$elementToPutObject.offset();
			var scrollTop = $(window).scrollTop();
			var inputHeight = that.$elementToPutObject.height();

			// mise à jour de l'année en fonction de la valeur de l'input
			that.$lblYear.text(that.getYearFromField());

			that.$mainDiv.dialog({
				position: [position.left, position.top + inputHeight - scrollTop]
			});

		});
	},

	formatMonthCalendarField: function () {
		var that = this;

		// recup champ input
		var dateStr = that.$elementToPutObject.val();

		// test longueur champ input
		switch (dateStr.length) {
		case 1:
			if (that.$elementToPutObject.val() === ".") {
				var date = new Date();
				that.$elementToPutObject.val(that.formatDate(date.getFullYear(), date.getMonth() + 1));
				that.$mainDiv.dialog('close');
			}
			break;
		case 4:
			that.$elementToPutObject.val(that.formatDate(parseInt(that.formatYear(dateStr.substr(2)), 10), parseInt(dateStr.substr(0, 2), 10)));
			that.$mainDiv.dialog('close');
			break;
		case 5:
			var point = dateStr.substr(2, 1);
			if (point === '.') {
				that.$elementToPutObject.val(that.formatDate(parseInt(that.formatYear(dateStr.substr(3)), 10), parseInt(dateStr.substr(0, 2), 10)));
				that.$mainDiv.dialog('close');
			} else {
				that.$elementToPutObject.val('');
				that.$mainDiv.dialog('close');
			}
			break;
		case 6:
			that.$elementToPutObject.val(that.formatDate(parseInt(that.formatYear(dateStr.substr(2)), 10), parseInt(dateStr.substr(0, 2), 10)));
			that.$mainDiv.dialog('close');
			break;
		case 7:
			break; 
		default:
			that.$elementToPutObject.val('');
			that.$mainDiv.dialog('close');
		}
		
		var value = that.$elementToPutObject.val();
		var prevValue = that.$elementToPutObject.prevValue;
		var isMonthDateValid;
		if(value.length===0) {
			isMonthDateValid = true;
		} else {
			isMonthDateValid = this.isMonthDateValid(value);
		}
		
		return {
			preValue : prevValue,
			value : value,
			changed : value !== prevValue,
			error : !isMonthDateValid
		};
	},

	formatStandardCalendarField: function () {
		var that = this;

		// recup champ input
		var dateStr = that.$elementToPutObject.val();

		// test longueur champ input
		switch (dateStr.length) {
		case 1:
			if(that.$elementToPutObject.val()=== '.') {
				date = new Date();
				that.$elementToPutObject.val(that.formatDate(date.getFullYear(), date.getMonth() + 1, date.getDate()));
			}
			break;
		// [ddmmyy]
		case 6:
			that.$elementToPutObject.val(that.formatDate(parseInt(that.formatYear(dateStr.substr(4)), 10), parseInt(dateStr.substr(2, 2), 10), parseInt(dateStr.substr(0, 2), 10)));
			break;
		// [dd.m.yy] , [d.mm.yy]
		case 7:
			// si [dd.m.yy]
			if (dateStr.substr(2, 1) === '.' && dateStr.substr(4, 1) === '.') {
				that.$elementToPutObject.val(that.formatDate(parseInt(that.formatYear(dateStr.substr(5)), 10), parseInt(dateStr.substr(3, 1), 10), parseInt(dateStr.substr(0, 2), 10)));
			}
			// si [d.mm.yy]
			else if (dateStr.substr(1, 1) === '.' && dateStr.substr(4, 1) === '.') {
				that.$elementToPutObject.val(that.formatDate(parseInt(that.formatYear(dateStr.substr(5)), 10), parseInt(dateStr.substr(2, 2), 10), parseInt(dateStr.substr(0, 1), 10)));
			}
			break;
		case 8:
			// Si format dd.mm.yy
			if (dateStr.substr(2, 1) === '.' && dateStr.substr(5, 1) === '.') {
				that.$elementToPutObject.val(that.formatDate(parseInt(that.formatYear(dateStr.substr(6)), 10), parseInt(dateStr.substr(3, 2), 10), parseInt(dateStr.substr(0, 2), 10)));
			} else { // sinon si format ########
				that.$elementToPutObject.val(that.formatDate(parseInt(that.formatYear(dateStr.substr(4)), 10), parseInt(dateStr.substr(2, 2), 10), parseInt(dateStr.substr(0, 2), 10)));
			}
			break;
		// [d.mm.yyyy], [dd.m.yyyy]
		case 9:
			// si [d.mm.yyyy]
			if (dateStr.substr(1, 1) === '.' && dateStr.substr(4, 1) === '.') {
				that.$elementToPutObject.val(that.formatDate(parseInt(that.formatYear(dateStr.substr(5)), 10), parseInt(dateStr.substr(2, 2), 10), parseInt(dateStr.substr(0, 1), 10)));
			}
			// si [dd.m.yyyy]
			else if (dateStr.substr(2, 1) === '.' && dateStr.substr(4, 1) === '.') {
				that.$elementToPutObject.val(that.formatDate(parseInt(that.formatYear(dateStr.substr(5)), 10), parseInt(dateStr.substr(3, 1), 10), parseInt(dateStr.substr(0, 2), 10)));
			}
			break;
		// defaut, [dd.mm.yyyy]
		case 10:
			break;
		default:
			var value = that.$elementToPutObject.val();
			if(value.length>0) {
				that.$elementToPutObject.val('');
			}
		}
		var value = that.$elementToPutObject.val();
		var prevValue = that.$elementToPutObject.prevValue;
		var isDateValid;
		if(value.length===0) {
			isDateValid = true;
		} else {
			isDateValid = this.isDateValid(value);
		}
		
		return {
			preValue : prevValue,
			value : value,
			changed : value !== prevValue,
			error : !isDateValid
		};
	},

	validate: function () {
		if (this.utils.input.validate(this.options, this.$elementToPutObject) != '') {
			return false;
		}
		return true;
	},
	// http://jsfiddle.net/zKb6c/
	isDateValid: function(text) {
		if(text.length!==10) {
			return false;
		}
		var parts = text.split('.');
		var d = parseInt(parts[0], 10);
		var m = parseInt(parts[1], 10);
		var y = parseInt(parts[2], 10);
		var date = new Date(y,m-1,d);
		if (date.getFullYear() == y && date.getMonth() + 1 == m && date.getDate() == d) {
		    return true;
		} else {
			return false;
		}
	},
	isMonthDateValid:function(text) {
		var REGEXP = /^([0][0-9]|1[0-2])\.[0-9]{4}$/i;
		return REGEXP.test(text);
	}
};
