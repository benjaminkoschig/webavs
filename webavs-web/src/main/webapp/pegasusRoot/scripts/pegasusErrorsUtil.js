var pegasusErrorsUtils = {
	

		/**
		 * Mise en forme html des erreurs
		 * @param logs
		 * @returns
		 */
		renderLogs : function (logs) {
			
			var s_template = '<div class="{{clazz}}">'
				+ '<div id="businessMesage">' + '<ul>' + '{{@each logs}}'
				+ '<li style="margin:10px 10px 10px 10px ">{{message}}</li>'
				+ '{{/@each logs}}' + '</ul>' + '</div>' + '</div>';

			return globazNotation.template.compile(logs, s_template);
		},

		/**
		 * Méthode à appeler dans la jsp
		 * On passe le message json généré
		 * @param jsonErrorsMsg
		 */
		dealErrors : function (jsonErrorsMsg, titleErrorBox){
			
			var title = (titleErrorBox || "Error");
			
			if(jsonErrorsMsg){
				//objet container
				var o_errorContainer = {};
				//objet log du container
				o_errorContainer.logs = jsonErrorsMsg;
				//chaine généré
				var s_error = this.renderLogs(o_errorContainer);
				globazNotation.utils.consoleError(s_error,title);
			}
		},
		
		/**
		 * Méthode à appeler dans la jsp
		 * On passe le message json généré
		 * @param jsonErrorsMsg
		 */
		getMessages : function (jsonErrorsMsg){
			
			if(jsonErrorsMsg){
				//objet container
				var o_errorContainer = {};
				//objet log du container
				o_errorContainer.logs = jsonErrorsMsg;
				//chaine généré
				return this.renderLogs(o_errorContainer);
			}
			return null;
		},
		
		maxLevel : function (jsonErrorsMsg) {
			var max = 0;
			if(jsonErrorsMsg){
				for(var key in jsonErrorsMsg) {
					msg = jsonErrorsMsg[key];
					if(msg.level > max){
						max = msg.level;
					}
				}
			}
			return max;
		}
		
}
