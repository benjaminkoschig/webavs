/**
 * @author DMA
 * 
 */
globazNotation.cellsum = {

	author: 'DMA',

	forTagHtml: 'td',
	type: globazNotation.typesNotation.TECHNICAL_NOTATION,

	description: "Permet de sommer des valeurs dans une colonne.<br />" + 
				"On peut définir un operteur(plus,minus,none) sur chaque cellue(td).<br />Il faut tout simplement ajouter une noation sur les cellules<br />" + 
				"On peut définir sur quel axe on veut appliquer l'operateur. Si rien n'est précisé sur la cellule on vas additionner <br />" + 
				"Ex: data-p-cellsum='vertical:plus' ou data-p-cellsum='horizontal:minus' ...   " + 
				"<div>ATTENTION: Cette notation est composé de 2 notations.<br /> " + 
				"La prmière définit ou l'on veut appliquer la somme(data-<b>g</b>-cellsum). <br />" + 
				"La deuxième premet definir l'operation qui doit être appliqué sur la cellule(data-<b>p</b>-cellsum)</div>",

	descriptionOptions: {
		horizontal: {
			desc: "Indique si il faut faire la somme horizontalement",
			param: "true, false(default)"
		},
		redIfNegative: {
			desc: "Permet de definit si il faut mettre en rouges la somme si elle est négative",
			param: "true, false(default)"
		}
	},

	options: {
		horizontal: false,
		// vertical:true,
		redifnegative: false
	},

	$cells: null,
	t_cells: [],
	n_trIndex: null,
	n_tdIndex: null,

	init: function ($elementToPutObject) {
		this.initIndex();
		this.initSelectCells();
		this.sumCells();
		this.addEventSum();
	},

	createObjParam: function ($element) {
		var s_options = '', o_options = {}, params = {};
		s_options = $element.attr('data-p-cellsum');
		if (s_options != undefined) {
			o_options = this.createParams(s_options);
		} else {
			o_options = null;
		}
		params.$element = $element;
		params.o_options = o_options;

		return params;
	},

	isTheSame: function ($element) {
		return this.$elementToPutObject.data('idCell') == $element.data('idCell');
	},

	initIndex: function () {
		this.n_trIndex = this.$elementToPutObject.closest('tr').index();
		this.n_tdIndex = this.$elementToPutObject.index();
		this.$elementToPutObject.data('idCell', this.n_trIndex + "_" + this.n_tdIndex);
	},

	initSelectCells: function () {
		var that = this, t_cells = [], $table = this.$elementToPutObject.closest('table');

		if (this.options.horizontal) {
			this.$elementToPutObject.closest('tr').find('td').each(function () {
				var $this = $(this);
				if ((!that.isTheSame($this)) && $this.index() < that.n_tdIndex) {
					t_cells.push(that.createObjParam($this));
				}
			});
		} else {
			$table.find('td:nth-child(' + (this.n_tdIndex + 1) + ')').each(function () {
				var $this = $(this);
				if ((!that.isTheSame($this)) && $this.closest('tr').index() < that.n_trIndex) {
					t_cells.push(that.createObjParam($this));
				}
			});
		}

		this.t_cells = t_cells;
	},

	addEventSum: function () {
		var that = this, n_length = this.t_cells.length;

		for (var i = 0; i < n_length; i++) {
			this.t_cells[i].$element.keyup(function () {
				that.sumCells()
			});
			this.t_cells[i].$element.change(function () {
				that.sumCells();
			});
		};
	},

	amountTofloat: function (s_amount) {
		var n_amount = this.utils.formatter.amountTofloat(s_amount);
		if (isNaN(n_amount)) {
			n_amount = 0;
		}
		return n_amount;
	},

	getValue: function ($elementTD) {
		var s_amount = null;
		s_amount = $.trim($elementTD.text());
		if (!s_amount.length) {
			s_amount = $elementTD.find('input').val();
		}
		if (this.utils.isEmpty(s_amount)) {
			s_amount = "0";
		}
		return s_amount;
	},

	getOperateur: function (o_cell) {
		var s_operateur = "PLUS";
		if (o_cell.o_options != null) {
			if (this.options.horizontal && o_cell.o_options.horizontal != undefined) {
				s_operateur = o_cell.o_options.horizontal;
			} else if (o_cell.o_options.vertical != undefined) {
				s_operateur = o_cell.o_options.vertical;
			}
		}
		return s_operateur.toUpperCase();
	},

	addcell: function (n_total, o_cell) {
		var n_amount = 0, n_sum = 0, s_amount = this.getValue(o_cell.$element), s_operateur = this.getOperateur(o_cell);

		n_amount = this.amountTofloat(s_amount);

		if (s_operateur.length === 0 || s_operateur === "PLUS") {
			n_sum = n_total + n_amount;
		} else if (s_operateur === "MINUS") {
			n_sum = n_total - n_amount;
		} else if (s_operateur === "NONE") {
			n_sum = n_total;
		}
		return n_sum;
	},

	appendsSumOnElement: function (n_sum) {
		

		
		this.$elementToPutObject.text(this.utils.formatter.formatStringToAmout(n_sum)).keyup();
		
		this.$elementToPutObject.trigger('change',{somme:n_sum});
		if (this.options.redifnegative) {
			if (n_sum < 0) {
				this.$elementToPutObject.addClass('errorText');
			} else {
				this.$elementToPutObject.removeClass('errorText');
			}
		}
	},

	sumCells: function () {
		var n_sum = 0, n_length = this.t_cells.length;
		for (var i = 0; i < n_length; i++) {
			n_sum = this.addcell(n_sum, this.t_cells[i]);
		};

		this.appendsSumOnElement(n_sum);
	}
};
