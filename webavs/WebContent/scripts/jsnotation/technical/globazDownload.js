/**
 * @author DMA Le nom de la fonction doit être en minuscule!!!
 * 
 */
globazNotation.download = {
	
	author: 'DMA',
	forTagHtml: 'a',
	type: globazNotation.typesNotation.TECHNICAL_NOTATION,
	
	description: "Génére un bouton avec une image en fonction du type de document. <br />" +
			"Permet de lancer lancer le téléchargement du document<br />" +
			"Le téléchargement se fait en 2 étapes: <br />" +
			"1 une requête ajax est executé pour générer le document. <br />" +
			"2 Une fois que le serveur a répondue et que tout c'est bien dérouler on télécharge le document" +
			"La fonction définit dans serviceMethodeName doit retourner du string",

	descriptionOptions: {
		docType: {
			desc: "Définit le type de document que l'on vas télécharger",
			param: "xls,doc,pdf,xml,txt,zip,csv",
			mandatory: true,
			type: String
		},
		serviceClassName: {
			desc: "Nom de la class(chemin package) qui contient la fonction à appler",
			param: "String",
			mandatory: true,
			type: String
		},
		serviceMethodName: {
			desc: "Nom de la méthode à appeler. La méthode appelée doit retourner l'emplacement du document généré. " + 
			"Il faut donc retourné l'Url(d:\monDossertemp\mofichierGénérer)",
			param: "true, false(default)",
			mandatory: true,
			type: String
		},
		parametres: {
			desc: "Paramètres à fournir à la méthode que l'on va appeler" +
				   "<b>Attention:</b> Les paramétres doivent être dans l'ordre (Même signature). Il faut les séparer avec une virgule" +
				   "<div>EX:67634,true => public fonction superMan(String Age,booelan enleverLunette)</div>",
			param: "string or boolean"
		},
		docName: {
			desc: "Nom du document. Il ne faut pas définir d'extenstion(doc,xls,...)",
			param: "string",
			mandatory: true,
			type: String
		},
		dynParametres: {
			desc: "Permet de définir une fonction qui calcule des paramètres dynamiquement lors du click du boutton)",
			param: "function(){return ',123,t'} ou f_maFonction",
			mandatory: false,
			type: Function
		},
		dynDocName: {
			desc: "Permet de définir une fonction qui calcul dynamiquement le nom du document lors du click du boutton)",
			param: "function(){return ',123,t'} ou f_maFonction",
			mandatory: false,
			type: Function
		},
		displayOnlyImage: {
			desc: "Définit si seule l'image doit être affichée (sans le texte). Si true, aucun texte ne doit être définit sur l'élément",
			param: "true|false(default)",
			mandatory: false,
			type: String
		},
		asynchroneMode:{
			desc: "Définit si le processus doit s'executer en mode asynchrone c.a.d dans une instance de BProcess. Dans ce cas une popup sera liée au clic sur le bouton afin de récolter l'adresse email d'envoi du document",
			param: "true|false(default)",
			mandatory: false,
			type: String
		},
		defaultMail:{
			desc: "Définit le destinataire du document",
			param: "string",
			mandatory: false,
			type: String
		}, 
		byPassExtentionXml: {
			desc: "permet d'évitier la covertion de l'extenstion en xml si on fait du xls ou du doc",
			param: "string",
			mandatory: false,
			type: Boolean
		}
		
	},
	
	options : {
		docType: "",
		serviceClassName: "",
		serviceMethodName: "",
		parametres: "",
		docName: "",
		dynParametres: null,
		dynDocName: null,
		displayOnlyImage: false,
		asynchroneMode: false,
		defaultMail: "",
		byPassExtentionXml: false
	},
	
	s_href: "",
	s_userActionRight: "",
	$img: null,
	$mail: null,
	b_documentAlreadyGenerated:false,
	
	s_userAction: "widget.action.jade.download",
	
	init : function ($elementToPutObject) {
		if (!$.trim(this.$elementToPutObject.text()).length && !this.options.displayOnlyImage) {
			this.$elementToPutObject.text(this.i18n('generer'));
		}
		this.addImage();
		this.$elementToPutObject.button();
		this.bindEvent();
		this.s_userActionRight = $.getUrlVar("userAction");
	},
	
	getPrametresDynamique: function () {
		var params = "";
		if (typeof this.options.dynParametres === "function") {
			params = this.options.dynParametres();
			if (params.length) {
				return params;
			} else {
				return "";
			}
			
		}
		return "";
	},
	
	getDocNameDynamique: function () {
		var params = "";
		
		if (typeof this.options.dynDocName === "function") {
			params = this.options.dynDocName();
		}

		return params;
	},
	
	bindEvent: function () {
		var that = this;
			
		this.$elementToPutObject.click(function (event) {
			
			event.preventDefault();
			$img = that.$elementToPutObject.find('.typeDoc');
			
			//mode asynchrone
			if(that.options.asynchroneMode){
				//ouvertue de la boite de dialogue de getsion du mail
				that.openDialogsBoxForAsynchrone();
				
			}else{
				//gestion img loading synchrone			
				$img.attr('src', that.s_contextUrl + "/scripts/jsnotation/imgs/loading.gif");
				//envoi de la requete ajax
				that.sendAjaxDownloadRequest();
				
			}
		});
	},
	
	//envoi de la requête ajax au service responsable de générer le document
	sendAjaxDownloadRequest : function () {
		var that = this;
		var s_oldImag = this.$img.attr('src'),
		s_url = $('[name=formAction]').attr('content');
		
		$.ajax({
			url: s_url,
			dataType: "json",
			data: that.getParamterForAjax(),
			success: function (data) {
				//mode asynchrone
				if(that.options.asynchroneMode){
					 $.publish("growl.info",[that.i18n('growlpostprocesstitle'),that.i18n('growlpostprocessmsg') +' '+ $mail.val()]);
					 that.b_documentAlreadyGenerated = true;
				}else{
					//mode synchrone
					if (data !== undefined && !ajaxUtils.hasError(data)) {
						window.location.href = s_url + "?userAction=" + that.s_userAction +
								"&nomDocUUID=" + data.nomDocUUID +
								"&docName=" + that.options.docName + that.getDocNameDynamique() +
								"&userActionForRight:" + that.s_userActionRight +
								"&docType=" + that.options.docType+"&byPassExtentionXml="+that.options.byPassExtentionXml;
						setTimeout(function () {
							$img.attr('src', s_oldImag, '600');
						});
						that.$elementToPutObject.trigger(eventConstant.DOWNLOAD_SUCESS);
					} else {
						if (data === undefined) {
							that.addError("");
						} else {
							that.addError(data);
						}
					}
				}
			},
			
			error: function (jqXHR, textStatus, errorThrown) {
				that.addError(jqXHR.responseText);
				that.$elementToPutObject.trigger(eventConstant.DOWNLOAD_SUCESS);
			}
		});
		
	},
	//gestion de l'ouverture des boites de dialogues, en mode asynchrone.
	// Si document déjà généré, ouverture d'une boite de dialogue de confirmation de relancement
	openDialogsBoxForAsynchrone : function () {
		
		if(this.b_documentAlreadyGenerated){
			this.openProcessAlreadyLauchAskBox();
		}else{
			this.openMailAskBox();
		}
	},
	//Boite de dialogue confirmation relance
	openProcessAlreadyLauchAskBox : function () {
		var that = this;
		
		$( "body" ).append('<div id="dialog-form" title="' + this.i18n('growltitleboxrelaunch') +'">' + 
				'<p class="validateTips">' + this.i18n('growltitleboxrelaunchmsg') + '</p>' +
				'</div>');
		var $dialog = $('#dialog-form');
		
		dialog = $dialog.dialog({
		      autoOpen: false,
		      dialogClass: 'noclose',
		      height: 200,
		      width: 400,
		      modal: true,
		      resizable:false,
		      buttons: [{
		    	  text:that.i18n('cancel'),
		    	  click:function() {
			          dialog.dialog( "close" );
			          $dialog.remove();
			      }
		      },
			  {
			      text:that.i18n('ok'),
			      click:function() {
			    	  dialog.dialog( "close" );
			    	  $dialog.remove();
			    	  that.openMailAskBox();
       
			       }
			    }]
		    });
		//on enleve le bouton de fermeture du dialogue
		$('.noclose .ui-dialog-titlebar-close').css('display','none');
		//ouverture du dialogue
		dialog.dialog('open');
		
	},
	
	//Ouverure de la boite de dialogue de saisie de l'adresse email
	openMailAskBox : function () {
		
		var that = this;
		
		
			$( "body" ).append('<div id="dialog-form" title="' + that.i18n('growltitlebox') +'">' + 
					'<p class="validateTips">' + that.i18n('maildocument') + '</p>' +
				     '<input type="text" name="email" id="email" size="30" value="' + that.options.defaultMail +'" class="text ui-widget-content ui-corner-all"/><br/><br/>' + 
				     '<span id="errorField" style="display:none;color:red;">' + that.i18n('mailerror') + '</span>' + 
					'</div>');
			
		$mail = $('#email');
		var $error = $('#errorField');
		var $dialog = $('#dialog-form');
		
		
		
		$mail.on('focus', function () {
			$error.hide();
		});
		
		dialog = $dialog.dialog({
		      autoOpen: false,
		      dialogClass: 'noclose',
		      height: 200,
		      width: 400,
		      modal: true,
		      resizable:false,
		      buttons: [{
		    	  text:that.i18n('cancel'),
		    	  click:function() {
			          dialog.dialog( "close" );
			          $dialog.remove();
			      }
		      },
			  {
			      text:that.i18n('ok'),
			      click:function() {
			         
			    	  if(that.checkMail($mail.val())){
				         dialog.dialog( "close" );
				         $dialog.remove();
						 that.sendAjaxDownloadRequest();
				      }else{
				    	  $error.show();
				      }
					        
			       }
			    }]
		    });
		//on enleve le bouton de fermeture du dialogue
		$('.noclose .ui-dialog-titlebar-close').css('display','none');
		//ouverture du dialogue
		dialog.dialog('open');
		
	},
	//pas vide et formattage adresse email @ et . qqchose
	checkMail: function (email){
		
		var mailRegExp = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i;
	    
		return email.length > 0 && mailRegExp.test(email);
	},
	
	addError: function (o_error) {
		this.$elementToPutObject.find('.typeDoc').attr('src', this.s_contextUrl + '/images/small_error.png');
		ajaxUtils.displayError(o_error);
	},
	
	getParamterForAjax: function s_formAction(parmetre) {
		var that = this;
			
		var parametre = { 
				userAction: that.s_userAction,
				userActionForRight: that.s_userActionRight,
				docName: that.options.docName + that.getDocNameDynamique(),
				serviceClassName: that.options.serviceClassName,
				serviceMethodName: that.options.serviceMethodName,
				docType: that.options.docType,
				parametres: that.options.parametres + that.getPrametresDynamique(),
				preparerDoc: "on",
				tocken: that.toDateInStringJadeFormate() + (new Date()).getMilliseconds(),
				asynchrone: that.options.asynchroneMode,
				mail: (that.options.asynchroneMode)?$mail.val():'',
				byPassExtentionXml:that.byPassExtentionXml
			};
		
		return parametre;
	},
	
	addImage: function () {
		var s_image = "";
		if (this.options.docType.toUpperCase() === "XLS") {
			s_image = "excel.png";
		} else if (this.options.docType.toUpperCase() === "PDF") {
			s_image = "pdf.png";
		} else if (this.options.docType.toUpperCase() === "DOC") {
			s_image = "word.png";
		} else if (this.options.docType.toUpperCase() === "TXT") {
			s_image = "txt.png";
		} else if (this.options.docType.toUpperCase() === "XML") {
			s_image = "xml.png";
		} else if (this.options.docType.toUpperCase() === "ZIP") {
			s_image = "zip.png";
		} else if (this.options.docType.toUpperCase() === "CSV") {
			s_image = "csv.png";
		}
		this.$elementToPutObject.addClass('butonDownload');
		
		var s_leftMargin = '10px';
		if (this.options.displayOnlyImage) {
			s_leftMargin = '';
		}
		
		this.$img = $("<img class='typeDoc' height='20px' style='margin:0 0 0 " + s_leftMargin + ";padding:0' src='" + this.s_contextUrl + "/scripts/jsnotation/imgs/" + s_image + "' />");
		this.$img.appendTo(this.$elementToPutObject); 
	},
	
	preparHref: function () {
		this.s_href = this.$elementToPutObject.attr('href');
		this.$elementToPutObject.attr('href', "#");
	},
	
	toDateInStringJadeFormate: function () {
		return this.utils.date.convertJSDateToDBstringDateFormat(new Date());
	}
};
