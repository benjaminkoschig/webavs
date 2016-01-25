/**
 * @author DMA Le nom de la fonction doit être en minuscule!!!
 * 
 */
globazNotation.selectautocomplete = {

	author: 'DMA',
	forTagHtml: 'input,select',
	type: globazNotation.typesNotation.TECHNICAL_NOTATION,

	description: "Permet de simuler une recherche d'autocompétion sur un champ SELECT",

	descriptionOptions: {},

	/**
	 * Ce paramètre est facultatif Ce paramètre permet des lancer des fonctions sur différent types d'évenements Liste des Evenements : boutons sandard de l'application. Les
	 * événements ce lance sur le clique du bouton btnCancel btnAdd btnValidate btnUpdate btnDelete AJAX: tous ces fonctions ce lance à la fin de l'a fonction dans ajax
	 * ajaxShowDetailRefresh ajaxLoadData ajaxShowDetail ajaxStopEdition ajaxValidateEditon ajaxUpdateComplete
	 */
	bindEvent: {
		ajaxDisableEnableInput: function () {
			// this.disabeldEnabeldImage();
		}
	},

	/**
	 * Parmétre de le l'objet qui vont être pour son initilisation Cet élément est obligatoire, Si pas d'option le crééer mais vide (options:{})
	 */
	options: {
		classSelector: "",
		widthSameAsInput:true,
		addIcon:true,
		mandatory: false,
		addSymboleMandatory: true,
		heigth:"200px"
	},

	vars: {
		$icon: {}
	},
	
	t_cloneds: [],

	/**
	 * Cette fonction est obligatoire car c'est elle qui va initiliser l'objet
	 */
	$newInput: null,
	sourc: null,
	b_tagSelect: false,
	init: function ($elementToPutObject) {
		var s_css = $elementToPutObject.attr("style");
		var s_class = $elementToPutObject.attr("class");
		var that = this;
		var n_width = $elementToPutObject.outerWidth();
		var n_heigth = $elementToPutObject.height();
		var $div = $("<span class=''></span>");
		
		var trident = navigator.appVersion.substr(navigator.appVersion.indexOf("Trident/"),11);
		var versionTrident = null;
		if(trident.length){
			versionTrident = trident.split("/")[1]*1;
		}
		//hack pour la désactivation du composant
		if(versionTrident < 6 || !versionTrident) {
			$elementToPutObject.hide();
			this.$newInput = $("<input type='text'/>");
			this.$newInput.addClass("selectAutoComplete");
			this.$newInput.attr("style", s_css);
			this.$newInput.attr("class", s_class);
			
			
			
			$elementToPutObject.parent().append($div);
			$div.append(this.$newInput);
			
			if ($elementToPutObject.get(0).nodeName.toLowerCase() === "select") {
				this.b_tagSelect = true;
				this.createSource();
			} 
			this.addAutoComplete();
			
			
			this.addEvent();
			if(this.options.widthSameAsInput){
				this.$newInput.css("width",n_width+"px");
			}
			 
			if(this.options.addIcon){
				this.addIcon();
			}
			
			if(this.options.mandatory){
				this.utils.input.addSymboleMandatory($div);
			}
		}
		//this.utils.input.addAllPropertyFromUtil(this.options, $div);

	},
	
	addIcon: function () {
		var that = this;
		// ui-icon-search
		//ui-icon-triangle-1-s
		var s_htmlIcon = '<span class="ui-state-default"><span class="ui-icon ui-icon-search"></span></span>';
		this.vars.$icon = $(s_htmlIcon);
		this.vars.$icon.css({ position:"relative", 
						     left:"-3px",
							 display: "inline-block",
							 "vertical-align": "middle"
							});
		this.$newInput.after(this.vars.$icon);
		
		this.$newInput.css("border","1px solid #A6C9E2");
		if(!jQuery.support.boxModel ){
			this.vars.$icon.css({ 
				 height:"20px", 
			     width:"20px",
			     top:"-2px"
			});
		} else {
			this.$newInput.css("height","16px");
		}

		this.vars.$icon.hover(function () {that.vars.$icon.addClass("ui-state-hover");}, function () {that.vars.$icon.removeClass("ui-state-hover");});
		
		this.vars.$icon.click(function () {
			that.$newInput.autocomplete("search", "");
		});
		this.$newInput.css("width",(this.$elementToPutObject.outerWidth()-25)+"px");
	},

	createSource: function () {
		this.source = [];
		var that = this;
		var o_objet = {};
		this.$elementToPutObject.find("option").each(function () {
			var $option = $(this);
			var s_value = $option.html();
			var s_lable = "";
			s_lable = s_value;
			if ($.trim(s_value) === "") {
				s_lable = "&#160;";
				s_value = "";
			}
			o_objet = {
				id: $option.val(),
				label: s_lable,
				value: s_value
			};
			that.source.push(o_objet);
		});
	},

	addAutoComplete: function () {
		var that = this;
		this.$newInput.autocomplete({
			minLength: 0,
			source: this.source,
			select: function (event, ui) {
				that.select(event, ui);
			}
		}).data("autocomplete")._renderItem = function (ul, item) {
			return $("<li style='font-size:0.9em'></li>").data("item.autocomplete", item).append("<a>" + item.label + "</a>").appendTo(ul);
		};

		
		if(!jQuery.support.boxModel){
			this.$newInput.autocomplete().data("autocomplete").menu.element.css( "height",this.options.heigth);
		}
		
		this.$newInput.autocomplete().data("autocomplete").menu.element.css(	
			{	   maxHeight: this.options.heigth,
				   overflowY: "auto",
				   overflowX: "hidden" //prevent horizontal scrollbar 
			});
		
		/*
		 * .ui-autocomplete {
        max-height: 100px;
        overflow-y: auto;
        /* prevent horizontal scrollbar 
        overflow-x: hidden;
    }
		 */
	},

	select: function (event, ui) {
		this.value = ui.label;
		this.$elementToPutObject.val(ui.item.id);
		this.$elementToPutObject.change();
		return true;
	},

	addEvent: function () {
		var that = this;
		this.$newInput.focus(function () {
			that.$newInput.autocomplete("search", "");
		});
	
		this.$newInput.change(function () {
			if (!this.value.length) {
				that.$elementToPutObject.val("");
				that.$elementToPutObject.change();
			}
		});
	}
};