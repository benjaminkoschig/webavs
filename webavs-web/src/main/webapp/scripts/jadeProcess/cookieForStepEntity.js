/**
 * 
 */


var cookieIds = {
	cookieName : 'rexecuteProcessOnEntite',
	o_ids : {},
	init: function () {
		//vide le cookie;
		this.clean();
	},
	
	clean: function () {
		$.cookie(this.cookieName, null);
	},
	
	serialize: function (t_data) {
		var s_values = "";
		for (var key in t_data) {
			s_values = s_values + ',"' + key + '":' + t_data[key];
		}
		return s_values.slice(1, s_values.length);
	},
	
	put: function ($this) {
		var o_valueIds = [],
		o_dom = $this.get(0),
		s_ids = $.cookie(this.cookieName), 
		n_id = null;
		
		n_id = $this.closest('tr').attr("identity");
		if (s_ids !== null && s_ids.length) {
			o_valueIds = $.parseJSON("{" + s_ids + "}");
		}
		if (o_valueIds[n_id]) {
			if (!o_dom.checked) {
				delete o_valueIds[n_id];
			}
		} else {
			if (o_dom.checked) {
				o_valueIds[n_id] = o_dom.checked;
			}
		}

		this.o_ids = o_valueIds;
		s_ids = this.serialize(o_valueIds);
		$.cookie(this.cookieName, s_ids, { expires: 1});
		if ($.trim($.cookie(this.cookieName)).length) {
			executeSelectedError.disable(false);
		} else {
			executeSelectedError.disable(true);
		}
	},
	
	getIds: function () {
		var s_ids = "";
		for (var id  in this.o_ids) {
			s_ids = s_ids + "-" + id;
		}
		return s_ids.slice(1, s_ids.length);
	}
};
