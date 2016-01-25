/**
 * @author CBU
 */
globazNotation.globalVariables.upload = {};
globazNotation.upload = {

	author: 'CBU',
	forTagHtml: 'input',
	type: globazNotation.typesNotation.TECHNICAL_NOTATION,

	description: "Cette objet permet de générer un champ input de type file pour uploader des fichiers. " +			
			"<br /><br />" + "Les paramètres '*Checker' permettent de définir une méthode qui sera appelé après l'upload du fichier (par exemple pour valider le fichier). <br /><br/>Signature de la méthode : <br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<i>methodeName(String path, String fileName, [*])</i> "+
			"<br /><br />" + "*Selon param supplémentaires définis dans 'ParametresChecker'"+
			"<br /><br />" + "Le fichier est uploadé dans 'persistence'"+
			"<br /><br />" + "Il est possible de tester le comportement des notations <a href=\"./testUpload.jsp\">sur cette page</a>",

	descriptionOptions: {
		mandatory: {
			desc: "Test si élément est obligatoire",
			param: "true, false(default)"
		},
		protocole:{
			desc:"Indique le protocole à utiliser pour sauvegarder le fichier. " +
				 "Si aucun protocole n'est spécifié le fichier sera sauvé dans le répertoire persistance du serveur web" +
				 "Protocol: <ul><li>jdbc: Permet de sauver le fichier en BD. Utils si on utilise le fichier sur le serveur de batch</li></ul>",
			param: "jdbc|null(default)"
		},
		addSymboleMandatory: {
			desc: "Ajoute le symoble *. Si le paramétre mandatory est à true ce paramétre n'est pas utilisé",
			param: "true|false(default)"
		},
		allowMultiplesUploads: {
			desc: "Permet plusieurs uploads simultanéments. <br />Attention : tout les éléments doivent le définir si true !",
			param: "true|false(default)"
		},
		classNameChecker: {
			desc: "nom de la class(chemin package) qui contient la fonction à appeler",
			param: "String"
		},
		methodNameChecker: {
			desc: "Nom de la methode à appeler",
			param: "true, false(default)"
		},
		parametresChecker: {
			desc: "Parametre de la méthode (serviceMethodName) que l'on veut appeler",
			param: "string or boolean"
		}
	},

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
	 * <li>AJAX: toutes ces fonctions se lancent à la fin de la fonction dans AJAX</li>
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

	},

	/**
	 * Paramètre de le l'objet qui vont être utilisés pour son initialisation Cet élément est obligatoire. Si aucune option, le créer vide (options:{})
	 */
	options: {
		mandatory: false,
		addSymboleMandatory: false,
		allowMultiplesUploads: false,
		classNameChecker: '',
		methodNameChecker: '',
		parametresChecker: '',
		classNameProgress: '',
		methodNameProgress: '',
		parametresProgress: '',
		protocole:'',
		callBack: function () {
		}
	},

	/**
	 * Cette fonction est obligatoire car c'est elle qui va initialiser l'objet
	 */
	init: function () {
		var that = this;
		this.utils.input.addAllPropertyFromUtil(this.options, that.$elementToPutObject);		
		// //// Version avec input personalisés ////////////
		// $div = $("<div style='vertical-align:middle'></div>");
		// $that.$elementToPutObject.css("height","0");
		// $that.$elementToPutObject.css("display","none");
		// $that.$elementToPutObject.after($div);

		// $spanButtonChoose = $("<button class='uploadChooseFile ui-state-default ui-corner-all' type='button' id='uploadChooseFile'>Choisir un fichier</button>").button();
		// $spanButtonSave = $("<button class='uploadSaveFile ui-state-default ui-corner-all' id='uploadSaveFile'>Sauver</button>").button();
		// $spanButtonSave = $("<input type='button' class='uploadSaveFile' id='uploadSaveFile' value='Sauver'/>");

		// $div.append($spanButtonChoose);
		// $div.append($spanButtonSave);
		// $div.append(this.createIframe());
		// ////////////////////////////////////////////////

		var $div = $("<span id='uploadDiv' class='uploadDiv ui-widget'></span>");
		that.$elementToPutObject.wrap($div);
		var $spanButtonSave = $("<input type='button' class='uploadSaveFile' value='Sauver' disabled='disabled'/>");
		that.$elementToPutObject.addClass("uploadBrowseFile");
		that.$elementToPutObject.after($spanButtonSave);
		that.$elementToPutObject.after(this.createIframe());

		if(!that.$elementToPutObject.attr("name")) {
			that.$elementToPutObject.attr("name", "inputFileGlobazUpload");
		}
		
		that.$elementToPutObject.change(function () {
			if (that.$elementToPutObject.val().length > 0) {
				$spanButtonSave.prop("disabled", false);
			} else {
				$spanButtonSave.prop("disabled", true);
			}
		});
		
		that.$elementToPutObject.click(function () {
			$(this).parent().find("img").remove();
		});

		this.getUploadSaveInputFile().click(function (event, element) {			
			var $element = $(this);
			$element.prop('disabled', true);			

			var $img = $("<img>", {
				id: "imageUploading",
				"class": "imageUploading",
				src: that.s_contextUrl + "/scripts/jsnotation/imgs/loading.gif"
			});

			//Avant de lancer l'upload, on test si on en as pas déjà un en cours. 
			//(Court-circuité si allowMultiplesUploads == true)
			if (!globazNotation.globalVariables.upload.b_Uploading || that.options.allowMultiplesUploads) {
				globazNotation.globalVariables.upload.b_Uploading = true;
				var $f = $('<form>', {
					enctype: 'multipart/form-data',
					action: that.s_contextUrl + '/jade/ajax/file/jadeUploadAjax.jsp',
					target: "iframe_form_upload",
					method: "post",
					id: "formUpload",
					style: "display:inline"
				});
			

				$element.after($img);
				
				that.$elementToPutObject.wrap($f);
				// On reselection le fomulaire car avant il était déconécté du dom;
				$f = $("#formUpload");
				if(that.options.protocole.length){
					$f.append("<input id='protocol' name='protocole' type='hidden' value="+that.options.protocole+">");
				}
				that.addEvent();
				$f .submit();
				that.$elementToPutObject.prop('disabled', true);
			} else {
				alert('Un processus est déjà en cours !');
				$element.prop('disabled', false);
			}
		});
	},

	addEvent: function () {
		var that = this;
		$("#formUpload")
				.bind(eventConstant.UPLOAD_RETURN, function (event, data) {
					that.$elementToPutObject.unwrap();
					$("#imageUploading").remove();
					if (data.hasError) {
						var $img_error = $("<img>", {
							id: "imageError",
							"class": "imageError",
							src: that.s_contextUrl + "/scripts/jsnotation/imgs/error.png",
							title: data.messageError,
							width: "16px",
							height: "16px"
						});
						that.getUploadSaveInputFile().after($img_error);
					} else {
						if (that.options.classNameChecker && that.options.methodNameChecker) {
							var $spanContainer = $("<div>", {
								width: "270px"
							});

							if (that.options.classNameProgress && that.options.methodNameProgress) {
								
								/* {
								"data-g-progressbar": "className:" + that.options.classNameProgress + "," + "methodName:" + that.options.methodNameProgress + ",timer:300, " + "parametres:" + that.options.parametresProgress
							}*/
								var $spanProgress = $("<div>");
								$spanProgress.appendTo($spanContainer);
								
								var o_progressBar = $spanProgress.notationProgressbar(
										{className: that.options.classNameProgress, 
										 methodName: that.options.methodNameProgress,
										 timer: 300, 
										 parametres: that.options.parametresProgress});	
								
								that.getUploadSaveInputFile().after($spanContainer);
							
								// Set la progress bar de manière statique pour le moment
								o_progressBar.peopleProgressBar(100);
							} else {
								var $spanText = $("<span>", {
									text: "Checking..."
								});
								var $imgChecking = $("<img>", {
									id: "imageChecking",
									"class": "imageChecking",
									src: that.s_contextUrl + "/scripts/jsnotation/imgs/checking.gif"
								});
								$imgChecking.appendTo($spanContainer);
								$spanText.appendTo($spanContainer);
								that.getUploadSaveInputFile().after($spanContainer);
							}

							var options = {
								serviceClassName: that.options.classNameChecker,
								serviceMethodName: that.options.methodNameChecker,

								parametres: data.path + ',' + data.fileName + ',' + that.options.parametresChecker,
								criterias: '',
								cstCriterias: '',
								callBack: function (donne) {
									that.callBack(donne);
									that.options.callBack(that.getUploadCheckedImage(), donne);									
									$spanContainer.remove();
								},
								errorCallBack: null
							};

							globazNotation.readwidget.options = options;
							globazNotation.readwidget.read();
						} else {
							that.callBack();
							that.options.callBack(data);
						}
					}					
				});
	},
	
	createIframe: function () {
		var that = this;
		var $iframe = $("<iframe name='iframe_form_upload'>");
		$iframe.attr({
			style: "margin:0;padding:0;width:0px;height:0px;border:0px;",
			scrolling: "no"
		});
		
		return $iframe;
	},

	getUploadSaveInputFile: function () {
		return this.$elementToPutObject.parent().find(".uploadSaveFile");
	},
	
	getUploadCheckedImage: function () {
		return this.$elementToPutObject.parent().find(".imageChecked");
	},
	
	callBack: function () {
		globazNotation.globalVariables.upload.b_Uploading = false;
		var $saveFile = this.getUploadSaveInputFile();
		$saveFile.removeAttr('disabled', 'disabled');
		var $imgChecked = $("<img>", {
			"class": "imageChecked",
			src: this.s_contextUrl + "/scripts/jsnotation/imgs/symbol_check24.png"
		});
		$saveFile.after($imgChecked);
		$saveFile.prop("disabled", true);
		this.$elementToPutObject.prop("disabled", true);		
	}

};
