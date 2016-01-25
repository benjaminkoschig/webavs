/**
 * @author DMA Le nom de la fonction doit être en minuscule!!!
 * 
 */
globazNotation.growl = {
	
	author: 'DMA',
	forTagHtml: '*',
	type: globazNotation.typesNotation.TECHNICAL_NOTATION,
	
	description: "Génère une boite d'information style growl. <br />" +
			"Permet de lancer lancer le téléchargement du document<br />" +
			"Le téléchargement se fait en 2 étapes: <br />" +
			"1 une requête ajax est executé pour générer le document. <br />" +
			"2 Une fois que le serveur a répondue et que tout c'est bien dérouler on télécharge le document" +
			"La fonction définit dans serviceMethodeName doit retourner du string",

	descriptionOptions: {
		expire: {
			desc: "Délai, en [ms] avant la disparition de la boite",
			param: 'temps en [ms]',
			type: Number
		},
		title: {
			desc: "Titre de la boîte"
		},
		text: {
			desc: "Texte contenu dans la boîte"
		},
		type: {
			desc: "Type de message (warn, info ou error)"
		}
	},
	
	options : {
		expires: 6000,
		title:'',
		text:'',
		type:''
	},
	
	isInit:false,
	n_idInterval: null,

	s_containerGrowl: '<div id="containerGrowl" style="display:none">'+
						'<div id="growl-basic"> '+
							'<a class="ui-notify-cross ui-notify-close" href="#">x</a>'+
							'<h1>#{title}</h1>'+
							'<p>#{text}</p>'+
						'</div>'+
						'<div id="growl-warn" class="ui-state-highlight" > '+
							'<a class="ui-notify-cross ui-notify-close" href="#">x</a>'+
							'<h1><span class="ui-icon ui-icon-info" style="float: left; margin-right: .3em;"></span>#{title}</h1>'+
							'<p>#{text}</p>'+
						'</div>'+
						'<div id="growl-error" class="ui-state-error" > '+
							'<a class="ui-notify-cross ui-notify-close" href="#">x</a>'+
							'<h1><span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>#{title}</h1>'+
							'<p>#{text}</p>'+
						'</div>'+	
					'</div>',
	$containerGrowl: null,
	
	
	init: function () {
		if(!this.isInit) {
			var that = this;
			this.$containerGrowl = $(this.s_containerGrowl); 
			this.$containerGrowl.appendTo("body");
			this.$containerGrowl.notify({
				 speed: 500,
				 custom: true,
				 expires: that.options.expires,
				 queue: false
			});  
			
			this.isInit = true;
	
			this.pollInterval();
			
			if(!jQuery.support.boxModel){
				this.$containerGrowl.css({
					position: "absolute",
					top: "expression("+document.body.scrollTop + 20 + "px)",
					right: "10px"
				});
			}
			
			
				$.subscribe("growl.info", function (s_title, s_text) {
					that.info(s_title, s_text);
				});
				$.subscribe("growl.warn", function (s_title, s_text) {
					that.warn(s_title, s_text);
				});
				$.subscribe("growl.error", function (s_title, s_text) {
					that.error(s_title, s_text);
				});
			
		}
	},
	
	callBack: function (data) {
		var notification,
			time = 200;
		for(var i=0; i<data.length;i++){
			notification = data[i];
//			if(i%5){
//				time = time + that.options.expires;
//			}
			this.displayJsonWithTimeOut(notification, time);
			time = time + 500;
		}
	}, 
	
	displayJsonWithTimeOut: function (notification, time) {
		var that = this;

		setTimeout(function () {
			that.displayJson(notification);
		}, time);
	},
	
	displayJson: function (notification) {
		if(notification.type === 'WARN' ){
			this.warn(notification.title, notification.text);
	    } else if(notification.type === 'ERROR' ){
			this.error(notification.title, notification.text);
		} else {
			this.info(notification.title, notification.text);
		}
	},
	
	getServerNotification: function () {
		var ajax = Object.create(globazNotation.readwidget);
		var that = this;
		ajax.options =  {
			serviceClassName: 'ch.globaz.jade.notification.business.service.JadeNotificationService',
			serviceMethodName: 'findByUserSession',
			parametres: '',
			callBack: function (data) {that.callBack(data);},
			errorCallBack:  function (data) {that.stop();}, 
			wantInitThreadContext: false,
			forceParametres: true,
			notification:null
		};
		
		ajax.read();
	},
	
	
	poll: function () {
		$.cookie("timeNotificationGrowl", $.now());
		this.getServerNotification();
	},
	
	pollInterval: function () {
		var that = this;
		
		this.n_idInterval = setInterval(function () {
			var lastTimeExecut = $.cookie("timeNotificationGrowl");
			if(!lastTimeExecut){
				$.cookie("timeNotificationGrowl", $.now());
			}
			if (($.now()) - lastTimeExecut > 30000 * 1 ) {
				that.poll();
				//$.cookie("timeNotificationGrowl", $.now());
				//that.getServerNotification();
			}else {
				
			}
		}, 30000);
	},

	stop: function () {
		clearInterval(this.n_idInterval);
	},
	
	notify: function (s_title, s_text, s_template) {
		//this.init();
		if (this.$containerGrowl && this.$containerGrowl.length) {
			this.$containerGrowl.notify("create", s_template, {
			    title: s_title,
			    text: s_text
			},{ custom:true });
		}
	},
	
	info: function (s_title, s_text) {
	//	this.init();
		if (this.$containerGrowl && this.$containerGrowl.length) {
			this.$containerGrowl.notify("create", "growl-basic", {
			    title: s_title,
			    text: s_text
			},{ custom:false });
		}
	},
	
	warn: function (s_title, s_text) {
		this.notify(s_title, s_text, "growl-warn");
	},
	
	error: function (s_title, s_text) {
		this.notify(s_title, s_text, "growl-error");
	}
};
		
		