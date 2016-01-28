globazNotation.filllist = {
   author: "jpa",

   forTagHtml: "input,select,span",

   description: "A la r�ponse d'un change sur un �l�ment, on lance un service qui va nous remplir un input",

   descriptionOptions: {
        serviceClassName: {
             desc: "La classe du service � lancer pour aller chercher les donn�es",
             param: "Le chemin complet du service",
             type: String,
             mandatory: "true"
        },
		serviceMethodName: {
             desc: "Le nom de la m�thode du service � appeler pour aller chercher les donn�es",
             param: "Le nom de la m�thode",
             type: String,
             mandatory: "true"
        },
		serviceParametres: {
             desc: "Une liste de param�tre qui peut �tre envoy� au service pour la recherche des donn�es",
             param: "La liste. EX: �param1,param2�",
             type: String,
             mandatory: "false"
        },

        selectorElementToFill: {
             desc: "s�lecteur de l'�l�ment � renseign�",
             param: "s�lecteur",
             type: String,
             mandatory: "true"
        },
        
        value: {
        	desc: "La valeur qui sera utilis� dans l'attribut du tag <option>",
        	param: "Valeur de l'attribut value",
        	type: String,
        	mandatory: "true"
        },
        
        label: {
        	desc: "La description qui sera utilis� dans le tag <option>",
        	param: "Description au sein de la balise option",
        	type: String,
        	mandatory: "true"
        }
        
        /*,
        elementClone: {
            desc: "",
            param: "",
            type: String,
            mandatory: "false"
        },
        selectedValue: {
           desc: "Valeur selectionn�e et � afficher dans la liste",
           param: "valeur � afficher",
           type: [String,Function],
           mandatory: "false"
      }*/
    },

    options: {
		serviceClassName:'',
		serviceMethodName:'',
		serviceParametres:null,
		selectorElementToFill:'',
		elementClone:'',
		selectedValue:null,
		value:'',
		label:''
	},


    vars: {

    },
    /**
     * Ce param�tre est facultatif.<br>
     * Il permet des lancer des fonctions sur diff�rent types d'�venements.<br>
     * Liste des �v�nements :<br>
     *  <ul>
     *      <li>boutons standard de l'application. Les �v�nements se lancent sur le clique du bouton</li>
     *      <ul>
     *          <li>btnCancel</li>
     *          <li>btnAdd</li>
     *          <li>btnValidate</li>
     *          <li>btnUpdate</li>
     *          <li>btnDelete</li>
     *      </ul>
     *      <li>AJAX: toutes ces fonctions se lancent � la fin de la fonction dans AJAX</li>
     *      <ul>
     *          <li>ajaxShowDetailRefresh</li>
     *          <li>ajaxLoadData</li>
     *          <li>ajaxShowDetail</li>
     *          <li>ajaxStopEdition</li>
     *          <li>ajaxValidateEditon</li>
     *          <li>ajaxUpdateComplete</li>
     *      </ul>
     *  </ul>
     */

    init: function( ) {
    	this.lieLesEvenement();
    },

    lieLesEvenement: function () { // BindEvent sur le change du select
		var that = this;
		this.$elementToPutObject.change(function(){
			that.getDataAjax();
		});
	},

	createOptions: function ($select, data) {
		var SEPARATOR_CONSTANT = '.';
		var $parent = $select.parent(), selectedValue = null;
		$select.empty();
		//Sp�cifique pour notre ami IE :), Sinon le select ne s'agrandi pas
		$select.detach();
		var that = this;

		if (typeof that.options.selectedValue === "function") {
			selectedValue = that.options.selectedValue(data);
		};

		$.each(data, function(index, object) {
			var partsLabel = that.options.label.split(SEPARATOR_CONSTANT);
			var partsId = that.options.value.split(SEPARATOR_CONSTANT);
			
			var valueField = object;
			var labelField = object;
			for(var i = 0; i < partsId.length;i++) {
				var field = partsId[i];
				valueField = valueField[field];
			}
			
			for(var i = 0; i < partsLabel.length;i++) {
				var field = partsLabel[i];
				labelField = labelField[field];
			}
			
			if ( selectedValue && val == selectedValue) {
				$select.append($("<option selected='selected'>").attr("value", valueField).text(labelField));
			} else {
				$select.append($("<option>").attr("value", valueField).text(labelField));
			}
		});

		$parent.append($select);
		$select.css("width","auto");
	},

	ajoutSelects: function (data) {
		var that = this;
		var $selects = $(this.options.selectorElementToFill);

		if(this.options.elementClone) {
			// on vas chercher le template du clonne car lorsque l'on fait plus on veut la liste sinon elle serai vide
	 		var notationClone = $selects.closest(this.options.elementClone).data("notation_clone");
			if(notationClone.vars && notationClone.vars.$cloneModel) {
				var $templateClone = notationClone.vars.$cloneModel.find(this.options.selectorElementToFill);
				this.createOptions($templateClone, data);
			} else {
				globazNotation.utils.consoleError("Erreur : la notation clone a �t� modifi�e ($cloneModel) !");
			}
		}
		$selects.each(function() {
			that.createOptions($(this), data);
		});

		$(this.options.selectorElementToFill).change();
		this.$elementToPutObject.trigger(eventConstant.GLOBAZ_FILL_LISTE_DONE);
	},

	getDataAjax: function () {
		var that = this;
		var options = {
			serviceClassName:this.options.serviceClassName,
			serviceMethodName:this.options.serviceMethodName,
			parametres:(typeof this.options.serviceParametres === "function")? this.options.serviceParametres.call(this.$elementToPutObject): this.options.serviceParametres,
			callBack:function (data) { that.ajoutSelects(data); }
		};

		options = $.extend( {}, options);
		ajax = Object.create($.extend(true, {}, globazNotation.readwidget));
		ajax.options = options;
		ajax.read();
	}
};