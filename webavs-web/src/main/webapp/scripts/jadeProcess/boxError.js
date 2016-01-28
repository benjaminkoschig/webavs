
var boxLog = {
	$boxError: null,
	s_message: null,
	$target: null,
	
	addBoxError: function ($target, s_message) {
		this.$target = $target;
		this.s_message = s_message;
		this.removeBox();
		this.createBox();
		this.appendBox();
	},
	
	addBoxWarning: function ($target, s_message) {
		this.$target = $target;
		this.s_message = s_message;
		this.removeBox();
		this.createWarningBox();
		this.appendBox();
	},
	
	
	createMessage: function (t_message) {
		var size = t_message.length;
		var s_message = "";
		var s_log = "";
		for (var i = 0; i < size; i++) {
			s_log = t_message[i].infosLog;
			//if (s_log.length) {
				s_log += "<br>" + t_message[i].messageId;
			//}
			s_message = s_message + "<li>" + s_log + "</li>";
			s_log = "";
		}
		return "<ul>" + s_message + "</ul>";
	},
	
	removeBox: function () {
		if (this.$boxError !== null && this.$boxError.length) {
			this.$boxError.remove();
		}
	},
	
	createBox: function () {
		var s_box = '<div id="boxError" class="ui-state-error ui-corner-all" style="margin-bottom:20px; padding: 0 .7em;">' +
						'<p>' +
							'<span class="ui-icon ui-icon-alert" style="float: left; margin-right: 0.3em;"></span>' +
							'<strong>Error:</strong><div id="errorsMessages">' + this.s_message + '</div>' +
						'</p>' +
					'</div>'; 
		
		
		this.$boxError = $(s_box);
		//this.$boxError.css("width",this.$boxError.find("#errorsMessages").width()+"px");	
	},
	
	createWarningBox: function () {
		var s_box = '<div id="boxWarning" class="ui-state-highlight ui-corner-all" style="margin-bottom:20px; padding: 0 .7em;">' +
						'<p>' +
							'<span class="ui-icon ui-icon-info" style="float: left; margin-right: 0.3em;"></span>' +
							'<strong>Warning:</strong><div id="errorsMessages">' + this.s_message + '</div>' +
						'</p>' +
					'</div>';
		
		
		this.$boxError = $(s_box);
		//this.$boxError.css("width",this.$boxError.find("#errorsMessages").width()+"px");	
	},
	
	appendBox: function () {
		this.$boxError.appendTo(this.$target);
	}
};