
globazNotation.utilsFormatter = {
	author: 'DMA',
	forTagHtml: 'Utils',
	type: globazNotation.typesNotation.UTILITIES,

	description: "Regroupement de fonctions utilitaire pour le formattage",

	applyFormateOnElementContainNotation: function ($elementToPutObjetc, callback, b_applyOnTh_) {
		var b_applyOnTh = b_applyOnTh_ || false;
		var s_nodeName = $elementToPutObjetc[0].nodeName.toLowerCase();
		if (s_nodeName === 'td' || b_applyOnTh) {
			callback.apply($elementToPutObjetc);
			this.addEvent($elementToPutObjetc, callback);
		}
	},

	addEvent: function ($element, callback) {
		$element.change(function () {
			callback.apply($element);
		});
	},

	applyFunctionOnCellsOfTable: function ($elementToPutObjetc, callback, b_applyOnTh) {
		this.applyFormateOnElementContainNotation($elementToPutObjetc, callback, b_applyOnTh);
		var s_nodeName = $elementToPutObjetc[0].nodeName.toLowerCase();
		var that = this;
		if (s_nodeName === 'th') {
			var indexHeader = $elementToPutObjetc.index();
			// Recup table parente
			var $table = $elementToPutObjetc.closest('table');
			// Sur chaque ligne tr sans le header
			$table.find('tr:gt(0)').each(function () {
				// recup cellule
				var $cell = $(this).find('td:eq(' + indexHeader + ')');
				callback.apply($cell);
				that.addEvent($cell, callback);
			});
		}
	},

	amountTofloat: function (s_amount) {
		var n_amount = 0;
		if (!globazNotation.utils.isEmpty(s_amount) && globazNotation.utils.isString(s_amount)) {
			n_amount = parseFloat(s_amount.replace("'", ""));
		}
		return n_amount;
	},

	formatStringToAmout: function (s_value, n_precision, b_withZero) {
		var valueStr = '';
		var decote = /'/g;

		if (!n_precision) {
			n_precision = 2;
		}
		valueStr = (s_value + "").replace(decote, "");
		// on regarde si on permet la valeur 0.00...
		if (isNaN(valueStr) || ($.trim(valueStr) === '' && !b_withZero)) {
			valueStr = "";
		} else {
			valueStr = (new Number(valueStr)).toFixed(n_precision);
			var i = valueStr.indexOf(".");
			var j = 0;
			while (i > 0) {
				if (j % 3 === 0 && j !== 0 && valueStr.charAt(i - 1) !== '-') {
					valueStr = valueStr.substring(0, i) + "'" + valueStr.substring(i);
					j++;
					i--;
				}
				i--;
				j++;
			}
		}
		return valueStr;
	},

	formateTime: function (miliscondes) {
		var date = new Date(0, 0, 0, 0, 0, 0, miliscondes), minutes = null, secondes = null;
		// "Day:"+(date.getDate()==="31")?"":date.getDate()+
		var n_jour = (date.getDate() === 31) ? 0 : date.getDate();
		var s_jour = "";
		if (n_jour > 0) {
			s_jour = "Day:" + n_jour;
		}
		minutes = (date.getMinutes() > 9) ? date.getMinutes() : "0" + date.getMinutes();
		secondes = (date.getSeconds() > 9) ? date.getSeconds() : "0" + date.getSeconds();
		return s_jour + " " + date.getHours() + ":" + minutes + ":" + secondes;
	},
	
	fromateSpy:  function(spy) {
		
		var dateSqlAvecHeur =  spy.match(/(\d\d*)/gim)[0],
			dateSql =  dateSqlAvecHeur.substring(0,8),
			user =  spy.match(/(\D\D*)/gim)[0];
		

		
		var year =  dateSql.substring(0,4),
			month = dateSql.substring(4,6),
			day = dateSql.substring(6,8),
			heur,
		    minute,
		    seconde,
			dayFromatted,
			heureFromatted,
			spyFromatted;
	
		heure = dateSqlAvecHeur.substring(8,10);
		minute = dateSqlAvecHeur.substring(10,12);
	    seconde = dateSqlAvecHeur.substring(12);
		
	    dayFromatted = day + "." + month + "." + year;
	    
	    heureFromatted = heure + ":" + minute+ ":" + seconde;
	    
	    spyFromatted = dayFromatted + "," +heureFromatted + " - " + user;
	    
	    return spyFromatted;
	},

	displaySpy: function ($container, spyCreation, spyUpdate ) {
		var s_spyCreation = "",
			s_spyUpdate = "",
			$spys;
		
		if(s_spyCreation){
			s_spyCreation =  globazNotation.utilsFormatter.fromateSpy(spyCreation);
		}
	
		s_spyUpdate =  globazNotation.utilsFormatter.fromateSpy(spyUpdate);
		$container.find(".spyFormatted").remove();
		
		$spys = $( "<div class='spyFormatted'>" +
						"<span class='spyCreated'>" +
							"<span class='label'>Create: </span>" +
							"<span>"+s_spyCreation+"</span>" +
						" :: </span>" +
						"<span class='spyUpdated'>" +
							"<span class='label'>Update: </span>" +
							"<span>"+s_spyUpdate+"</span>" +
						"</span>" +
					"</div>");
		
		$container.append($spys);
		
		if(s_spyCreation){
			$spys.hover(function () {
				$spys.find(".spyCreated").show();
			}, function () {
				$spys.find(".spyCreated").hide();
			});
		}
	}
	
	
};
