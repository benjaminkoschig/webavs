/**
 * @author DMA
 */

var jsManager = {

	_t_before: [],// tableau contenant les références des fonctions à executer avant le moteur de la notation
	_t_after: [],// tableau contenant les références des fonctions à executer après le moteur de la notation.
	_t_executed: [],

	_executeFunctionInArray: function (t_array) {
		var n_length = t_array.length, f_fn, s_msg, time, time2;
		for (var i = 0; i < n_length; i++) {
			time = (new Date()).getTime();
			f_fn = t_array[i].f_fn;
			s_msg = t_array[i].s_msg;
			f_fn();
			time2 = (new Date()).getTime() - time;
			this._t_executed.push({
				f_fn: f_fn,
				s_infos: s_msg,
				time: time2
			});
			if (typeof console !== "undefined") {
				if (s_msg) {
					console.log("jsManager: " + s_msg + "->" + time2);
				} else {
					console.log("jsManager: " + f_fn.toString() + "->" + time2);
				}
			}
		}
	},

	showFunction: function () {

	},

	add: function (f_function, s_infos) {
		this.addBefore(f_function, s_infos);
	},

	createObject: function (f_function, s_infos) {
		return {
			f_fn: f_function,
			s_msg: s_infos
		};
	},

	addBefore: function (f_function, s_infos) {
		this._t_before.push(this.createObject(f_function, s_infos));
	},

	addAfter: function (f_function, s_infos) {
		this._t_after.push(this.createObject(f_function, s_infos));
	},

	executeAfter: function () {
		this._executeFunctionInArray(this._t_after);
		this._t_after = [];
	},

	executeBefore: function () {
		this._executeFunctionInArray(this._t_before);
		this._t_before = [];
	},

	showInfos: function () {
		var s_html = "<table class='notation'><thead><tr><th>Fonction</th><th>Info</th><th>T(ms)</th></tr></thead><tbody>";
		var i = 0;
		var t_time = 0;
		var o_obj = {};
		for ( var el in this._t_executed) {
			i++;
			var odd = i % 2, c;
			o_obj = this._t_executed[el];
			t_time = t_time + o_obj.time;
			if (odd === 1) {
				c = 'odd';
			} else {
				c = 'even';
			}
			s_html += "<tr class='" + c + "'><td>" + o_obj.f_fn.toString() + "</td>" + "<td>" + o_obj.s_infos + "</td>" + "<td>" + o_obj.time + "</td>" + "</tr>";

		}
		var total = '<tr><td></td><td></td><td></td></tr>' + '<tr style="font-weight: bold;"><td>Totale</td><td></td><td>' + t_time + '</td></tr>';
		s_html += total + "</tbody></table>";
		globazNotation.utils.console(s_html, 'Infos', 'infos', '90%');
	}
};