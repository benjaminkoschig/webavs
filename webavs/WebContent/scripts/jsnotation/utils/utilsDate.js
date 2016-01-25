/**
 * @author SCE
 */
globazNotation.utilsDate = {
	author: 'SCE',
	forTagHtml: 'Utils',
	type: globazNotation.typesNotation.UTILITIES,

	description: "Regroupement de fonction utilitaire pour le traitement des dates",
	/* Constantes définissant les plages de valeurs min et max pour les jours, mois année */
	RANGE_DAY: {
		MIN: 1,
		MAX: 31
	},
	RANGE_MONTH: {
		MIN: 1,
		MAX: 12
	},
	RANGE_YEAR: {
		MIN: 1000,
		MAX: 9999
	},

	/*
	 * Fonction qui convertit une date format Globaz[jj.mm.aaaa] en un objet Date JS
	 */
	convertGlobazYearDateToJSDate: function (globazDate) {
		if (this._isValidGlobazDate(globazDate)) {
			// Recup date format globaz et construction date js
			var elem = globazDate.split('.');
			// construction date, !!! -1 pour les mois (0-11)
			return new Date(elem[2], elem[1] - 1, elem[0]);
		}
		return null;
	},
	
	/**
	 * Fonction qui essaye de convertir une date format Globaz[jj.mm.aaaa] en un objet Date JS.
	 * Retourne l'object convertit ou le param�tre si celui-ci est d�j� de type Date JS.
	 * Si le param�tre n'est pas une date valide, renvoit un exception.
	 * Si le param�tre est absent ou vide, retourne la date du jour.
	 */
	tryToConvertToJsDate: function (jsDate) {
		var d_date;
		
		if (!this._isJSDate(jsDate)) {
			if (globazNotation.utils.isEmpty(jsDate) || typeof jsDate == 'undefined') {
				d_date = new Date();
			} else {
				d_date = this.convertGlobazYearDateToJSDate(jsDate);
				if (d_date == null) {
					throw "jsDate value " + jsDate + " is not a valid date";
				}
			}
		} else {
			d_date = jsDate;
		}
	
		return d_date;
	},
	
	/*
	 * Fonction qui convertit une date Javascript en une date Globaz format jj.mm.aaaa
	 */
	convertJSDateToGlobazStringDateFormat: function (jsDate) {
		var d_date = this.tryToConvertToJsDate(jsDate); // objet Date
		var s_date = '';// chaine de retour, fomat GLobaz
		
		s_date = this.formateDate(d_date);
		return s_date;
	},
	
	formateDate: function (d_date) {
		return this._format(d_date.getDate()) + "." + this._format(d_date.getMonth() + 1) + "." + this._format(d_date.getFullYear());
	},

	toDayFromated : function (d_date) {
		return this.formateDate(new Date());
	},
	
	toDayInStringJadeFormate: function () {
		return this.convertJSDateToDBstringDateFormat(new Date());
	},

	/*
	 * Fonction qui convertir une date fomat JS, en chaine de caractère formatée DB Si la date passé en param est null, on retourne la date du jour
	 */
	convertJSDateToDBstringDateFormat: function (jsDate) {
		var d_date = this.tryToConvertToJsDate(jsDate); // objet Date
		var s_date = '';// chaine de retour, fomat DB
		
		// Construction chaine retour
		s_date = d_date.getFullYear() + '' + this._format((d_date.getMonth() + 1)) + '' + this._format(d_date.getDate());
		return s_date;
	},
	/*
	 * Fonction convertisant une date mensuelle en format string, tel que "03.2011" en date complète -> "01.03.2011"
	 * 
	 * Retourne la date en format string
	 */
	convertMonthDateToFullDate: function (monthDate) {
		if (typeof (monthDate) !== 'string') {
			return null;
		}
		return '01.' + monthDate;
	},
	
	/*
	 * 
	 * Fonction qui permet de calculer le nombre de mois entre 2 dates
	 * Prends en param�tre un date au fromate globaz tel que "01.01.2011"
	 * 
	 * Retourne la date en format string
	 */
	countMonth: function (s_date1,s_date2) {
			var d_jsDate1 = this.convertGlobazYearDateToJSDate(s_date1), 
				d_jsDate2 = this.convertGlobazYearDateToJSDate(s_date2),
				d1,d2;
			d1 = d_jsDate1; 
			d2 = d_jsDate2; 
			
			if(d_jsDate1 < d_jsDate2){
				d1 = d_jsDate2;
				d2 = d_jsDate1;
			}
			var m = (d1.getFullYear() - d2.getFullYear()) * 12 + (d1.getMonth() - d2.getMonth());
			if(d1.getDate() < d2.getDate()){
				--m;
			} 
			return m;
	},
	
	/*
	 * Fonction convertisant une date "01.03.2011" en  "03.2011"
	 * 
	 * Retourne la date en format string
	 */
	convertDateToMontDate: function (s_date) {
		var s_dateTrim = $.trim(s_date);
		if (typeof (s_date) !== 'string') {
			return null;
		}
		return s_dateTrim.slice(3, s_dateTrim.length);
	},
	/*
	 * Fonction qui s'assure que la date passé en paramètre est bien une date globaz (jj.mm.aaaa), pour les calculs de dates
	 */
	normalizeFullGlobazDate: function (dateToNormalize) {
		// Check longueur, si pas 10 on traite
		dateToNormalize = $.trim(dateToNormalize);
		if (dateToNormalize.length === 7) {
			return this.convertMonthDateToFullDate(dateToNormalize);
		} else if (dateToNormalize.length === 10) {
			return dateToNormalize;
		} else {
			return null;
		}
	},
	areDatesSame: function (globazDate1, globazDate2) {
		// si diff<0 alors date avant sinon apres
		return (this._compareDiffDate(globazDate1, globazDate2) == 0);
	},
	/*
	 * Fonction qui test si la date passé en 1er paramètre est antérieur à la deuxième date passé en paramètre Return true si c'est le cas, false sinon
	 */
	isDateBefore: function (globazDate1, globazDate2) {
		// si diff<0 alors date avant sinon apres
		return (this._compareDiffDate(globazDate1, globazDate2) < 0);
	},
	/*
	 * Fonction qui test si la date passé en paramètre est antérieur a aujourd'hui Return true si c'est le cas, false sinon
	 */
	isDateBeforeNow: function (globazDate) {
		return (this.isDateBefore(globazDate, this.convertJSDateToGlobazStringDateFormat(new Date())));
	},
	/*
	 * Fonction qui test si la date passé en 1er paramètre est ultérieur à la deuxième date passé en paramètre Return true si c'est le cas, false sinon
	 */
	isDateAfter: function (globazDate1, globazDate2) {
		// si diff>0 alors date apres sinon avant
		return (this._compareDiffDate(globazDate1, globazDate2) > 0);
	},
	/*
	 * Fonction qui test si la date passé en paramètre est postérieur a aujourd'hui Return true si c'est le cas, false sinon
	 */
	isDateAfterNow: function (globazDate) {
		return this.isDateAfter(globazDate, this.convertJSDateToGlobazStringDateFormat(new Date()));
	},

	/*
	 * Retrun la diffenrence entre 2 date.
	 */
	_compareDiffDate: function (globazDate1, globazDate2) {
		// Conversion des dates globaz reçues en param
		var date1 = this.convertGlobazYearDateToJSDate(this.normalizeFullGlobazDate(globazDate1));
		var date2 = this.convertGlobazYearDateToJSDate(this.normalizeFullGlobazDate(globazDate2));
		// resultat difference
		return date1.getTime() - date2.getTime();
	},

	/*
	 * Fonction qui formatte les jours et mois au format ##
	 */
	_format: function (value) {
		if (value < 10) {
			value = "0" + value;
		}
		return String(value);
	},
	/*
	 * fonction qui s'assure que la date passé en paramètre est bien un objet date
	 */
	_isJSDate: function (jsDate) {
		return (!globazNotation.utils.isEmpty(jsDate) && Object.prototype.toString.call(jsDate) == '[object Date]');
	},
	/*
	 * Teste si la chaine passé en paramètre est correctement formatté pour être une date Globaz
	 */
	_isValidGlobazDate: function (s_globazDate) {
		// slice
		var dateToken = s_globazDate.split('.');

		// test qu'il y ait au moins 3 tken pour la date
		if (dateToken.length < 3) {
			return false;
		} else {
			// test la taille des valeurs
			if (dateToken[0].length !== 2 || dateToken[1].length !== 2 || dateToken[2].length !== 4) {
				return false;
			}
			// Test la plage de calidité des valeurs
			if (Number(dateToken[0]) < this.RANGE_DAY.MIN || Number(dateToken[0]) > this.RANGE_DAY.MAX || Number(dateToken[1]) < this.RANGE_MONTH.MIN || Number(dateToken[1]) > this.RANGE_MONTH.MAX || Number(dateToken[2]) < this.RANGE_YEAR.MIN || Number(dateToken[2]) > this.RANGE_YEAR.MAX) {
				return false;
			}
			return true;
		}
	}
};
