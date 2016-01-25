globazNotation.progressbar = {

	author: 'DMA',
	forTagHtml: 'div,span',
	type: globazNotation.typesNotation.TECHNICAL_NOTATION,

	description: "Cette objet permet de créer une progresse bar. On peut l'utiliser de 2 manières <br />" + 
				"<ul>" + 
					"<li>Static en déffinisant la valeur de la progresse bar</li>" + 
					"<li>Dynamique(AJAX) en donnant la class et la methode qu'il faut appeler</li>" +
				"<ul>",

	descriptionOptions: {
		className: {
			desc: "Indique la class a utiliser (path)",
			param: "String: Nom de la class"
		},
		methodName: {
			desc: "Indique la fonction de la class(service) à appler",
			param: "String: Nom de la class"
		},
		parametres: {
			desc: "Paramétres de la fonction que l'on veut fournire. Attention les paramétres doivent être dans l'ordre",
			param: "String:  paramére1,paramétre2"
		},
		callBack: {
			desc: "Fonction de callBack",
			param: "function"
		},
		timer: {
			desc: "Temps de rafrechissement en millisecondes",
			param: "Integer:Default 1000 (1seconde)"
		},	
		valide: {
			desc: "Permet de définire si l'on veut que la bar soit de couleur verte une fois la progression finit",
			param: "Boolean:(default false)"
		},
		wait: {
			desc: "Fonction de callBack",
			param: "Boolean:(default true)"
		}
	},

	bindEvent: {
		ajaxDisableEnableInput: function () {
			this.disabeldEnabeldImage();
		}
	},

	options: {
		className: "",
		methodName: "",
		parametres: null,
		callBack: null,
		value: 0,
		timer: 1000,
		valide: false,
		wait: true
	},

	vars: {
		b_hasImageWaiting: false,
		o_read: null,
		n_idInterval: null,
		n_progresse: 0,
		b_ajax: false
	},

	init: function ($elementToPutObject) {
		if (this.options.className.length) {
			this.vars.b_ajax = true;
		}
		this.vars.o_read = Object.create($.extend(true,{},globazNotation.readwidget));
		this.setOptionsForAjax();
		this.addProgressBar();
	},

	addProgressBar: function () {
		var that = this;

		if (this.options.valide) {
			this.options.value = 100;
		} 

		this.$elementToPutObject.progressbar({
			value: that.options.value
		});

		if (this.options.valide) {
			this.changeBackground("pbar-ok.png");
		}
	},
 
	changeBackground: function (s_image) {
		var $barValue = this.$elementToPutObject.find(".ui-progressbar-value");
		if (s_image.length) {
			$barValue.css("background-image", "url(" + this.getImage(s_image) + ")");
		} else {
			$barValue.css("background-image", "");
		}
	},

	peopleProgressBar: function (n_defaultValue) {
		var that = this;
		(typeof n_defaultValue === "undefined") ? this.setValue(this.options.value) : this.setValue(n_defaultValue);
		if (this.vars.b_ajax) {
			this.vars.n_idInterval = setInterval(function () {
				that.vars.o_read.read();
				that.vars.n_progresse++;
			}, that.options.timer);
		}
	},

	stop: function () {
		clearInterval(this.vars.n_idInterval);
	},

	setValue: function (n_value) {
		this.options.value = n_value;
		if (n_value === 100 && !this.b_hasImageWaiting) {
			this.stop();
		}
		
		if (n_value === 0 && this.options.wait) {
			if (!this.vars.b_hasImageWaiting) {
				this.changeBackground("pbar-wait.gif");
			}			
			n_value = 100;
			this.vars.b_hasImageWaiting = true;
		} else {
			this.changeBackground(""); 
			this.vars.b_hasImageWaiting = false;
		}
		this.$elementToPutObject.progressbar("option", "value", n_value);
	},

	setOptionsForAjax: function () {
		var that = this;
		var options = {
			serviceClassName: that.options.className,
			serviceMethodName: that.options.methodName, 
			parametres: that.options.parametres,
			errorCallBack: function (jqXHR, textStatus, errorThrown) {
				clearInterval(that.vars.n_idInterval);
				that.$elementToPutObject.hide();
			},
			callBack: function (data) {

				if (data.progress === 0 || data.progress) {
					that.setValue(data.progress * 100);
				} else {
					that.setValue((data.currentProgress / data.maxProgress) * 100);
				}				
				if (that.options.callBack !== null) {
					that.options.callBack(data, that);
				}
			}
		};
		this.vars.o_read.options = options;
	}
};