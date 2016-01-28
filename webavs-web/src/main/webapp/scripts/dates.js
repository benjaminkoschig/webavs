Date.DAYNAMES = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];

Date.MONTHNAMES = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];

Date.FERIEN = [new Date(2009, 05, 26), new Date(2009, 06, 01)];

Date.msPERDAY = 1000 * 60 * 60 * 24;

Date.prototype.DAYNAMES = Date.DAYNAMES;

Date.prototype.MONTHNAMES = Date.MONTHNAMES;

Date.prototype.msPERDAY = Date.msPERDAY;

Date.prototype.copy = function () { 
	return new Date( this.getTime() ); 
};

Date.prototype.getFullDay = function() { 
	return this.DAYNAMES[this.getDay()]; 
};

Date.prototype.getDayAbbr = function() { 
	return this.getFullDay().slice(0, 3); 
};

Date.prototype.getFullMonth = function() {
	return this.MONTHNAMES[this.getMonth()]; 
};

Date.prototype.getMonthAbbr = function() { 
	return this.getFullMonth().slice(0, 3); 
};

Date.prototype.to12HourTimeString = function () {
	var h = this.getHours();
	var m = "0" + this.getMinutes();
	var s = "0" + this.getSeconds();
	var ap = "am";
	if (h >= 12) {
		ap = "pm";
		if (h >= 13)
			h -= 12;
	} else if (h == 0) {
		h = 12;
	}
	h = "0" + h;
	return h.slice(-2) + ":" + m.slice(-2) + ":" + s.slice(-2) + " " + ap;
};

Date.prototype.to24HourTimeString = function () {
	var h = "0" + this.getHours();
	var m = "0" + this.getMinutes();
	var s = "0" + this.getSeconds();
	return h.slice(-2) + ":" + m.slice(-2) + ":" + s.slice(-2);
};

Date.prototype.lastday = function() {
	var d = new Date(this.getFullYear(), this.getMonth() + 1, 0);
	return d.getDate();
};

Date.prototype.getDaysBetween = function(d) {
	var tmp = d.copy();
	tmp.setHours(this.getHours(), this.getMinutes(), this.getSeconds(), this.getMilliseconds());
	var diff = tmp.getTime() - this.getTime();
	return diff/this.msPERDAY;        
};

Date.prototype.getDayOfYear = function() {
	var start = new Date(this.getFullYear(), 0, 0);
	return this.getDaysBetween(start) * -1;
};

Date.prototype.addDays = function(d) {
	this.setDate( this.getDate() + d );
};

Date.prototype.addWeeks = function(w) {
	this.addDays(w * 7);
};

Date.prototype.addMonths= function(m) {
	var d = this.getDate();
	this.setMonth(this.getMonth() + m);
	if (this.getDate() < d)
		 this.setDate(0);
};

Date.prototype.addYears = function(y) {
	var m = this.getMonth();
	this.setFullYear(this.getFullYear() + y);
	if (m < this.getMonth()) {
		this.setDate(0);
	}
};

Date.prototype.addWeekDays = function(d) {
	var count = d;
	while (count > 0) {
		this.setDate(this.getDate() + 1);
		if (this.getDay() > 0 && this.getDay() < 6) {
			var isFerienTag = false;
			for (var i = 0; i < Date.FERIEN.length; i++) {
				ferienTag = Date.FERIEN[i];
				if (this.isSameDate(ferienTag)) {
					isFerienTag = true;
					break;
				}
			}
			if (!isFerienTag) {
				count --;
			}
		}
	}
};

Date.prototype.gotoNextWorkingDay = function() {
	while (!this.isWorkingDay()) {
		this.setDate(this.getDate() + 1);
	}
};

Date.prototype.isWorkingDay = function() {
	if (this.getDay() > 0 && this.getDay() < 6) {
		for (var i = 0; i < Date.FERIEN.length; i++) {
			ferienTag = Date.FERIEN[i];
			if (this.isSameDate(ferienTag)) {
				return false;
			}
		}
		return true;
	} else {
		return false;
	}
}
/*Date.prototype.addWeekDays = function(d) {
	var startDay = this.getDay();  //current weekday 0 thru 6
	var wkEnds = 0;                //# of weekends needed
	var partialWeek = d % 5;       //# of weekdays for partial week
	if (d < 0) {                 //subtracting weekdays 
		wkEnds = Math.ceil(d/5); //negative number weekends
		switch (startDay) {
			case 6:                  //start Sat. 1 less weekend
				if (partialWeek == 0 && wkEnds < 0) 
				wkEnds++;
				break;
			case 0:                   //starting day is Sunday
				if (partialWeek == 0) 
					d++;              //decrease days to add
				else 
					d--;              //increase days to add
				break;
			default:
				if (partialWeek <= -startDay) 
					wkEnds--;
		}
	} else if (d > 0) {            //adding weekdays
		wkEnds = Math.floor(d/5);
		var w = wkEnds;
		switch (startDay) {
			case 6:
				// If staring day is Sat. and
				// no partial week one less day needed
				// if partial week one more day needed
				if (partialWeek == 0) 
					d--;
				else 
					d++;
				break;
			case 0:        //Sunday
				if (partialWeek == 0 && wkEnds > 0) 
				wkEnds--;
				break;
			default:
				if (5 - d < partialWeek) 
					wkEnds++;
		}
	}
	d += wkEnds * 2;
	this.addDays(d);
};
*/
Date.prototype.getWeekDays = function(d) {
	var wkEnds = 0;
	var days = Math.abs(this.getDaysBetween(d));
	var startDay = 0, endDay = 0;
	if (days) {
		if (d < this) {
			startDay = d.getDay();
			endDay = this.getDay();
		} else {
			startDay = this.getDay();
			endDay = d.getDay();
		}
		wkEnds = Math.floor(days/7);
		if (startDay != 6 && startDay > endDay) 
			wkEnds++;
		if (startDay != endDay && (startDay == 6 || endDay == 6) ) 
			days--;
		days -= (wkEnds * 2);
	}
	return days;
};

Date.prototype.getMonthsBetween = function(d) {
	var sDate, eDate;   
	var d1 = this.getFullYear() * 12 + this.getMonth();
	var d2 = d.getFullYear() * 12 + d.getMonth();
	var sign;
	var months = 0;
	if (this == d) {
		months = 0;
	} else if (d1 == d2) { //same year and month
		months = (d.getDate() - this.getDate())/this.lastday();
	} else {
		if (d1 <  d2) {
			sDate = this;
			eDate = d;
			sign = 1;
		} else {
			sDate = d;
			eDate = this;
			sign = -1;
		}
		var sAdj = sDate.lastday() - sDate.getDate();
		var eAdj = eDate.getDate();
		var adj = (sAdj + eAdj)/sDate.lastday() -1;
		months = Math.abs(d2 - d1) + adj;
		months = (months * sign);
	}
	return months;
};

Date.prototype.getYearsBetween = function(d) {
	var months = this.getMonthsBetween(d);
	return months/12;
};

Date.prototype.getAge = function() {
	var today = new Date();
	return this.getYearsBetween(today).toFixed(2);
};

Date.prototype.sameDayEachWeek = function (day, date) {
	var aDays = new Array();
	var eDate, nextDate, adj;
	if (this > date) {
		eDate = this;
		nextDate = date.copy();
	} else {
		eDate = date;
		nextDate = this.copy();
	}
	adj = (day - nextDate.getDay() + 7) %7;
	nextDate.setDate(nextDate.getDate() + adj);
	while (nextDate < eDate) {
		aDays[aDays.length] = nextDate.copy();
		nextDate.setDate(nextDate.getDate() + 7);
	}
	return aDays;
};

Date.toDate = function(d) {
	var newDate;
	if (arguments.length == 0) {
		newDate = new Date();
	} else if (d instanceof Date) {
		newDate = new Date(d.getTime());
	} else if (typeof d == "string") {
		newDate = new Date(d);
	} else if (arguments.length >= 3) {
		var dte = [0, 0, 0, 0, 0, 0];
		for (var i = 0; i < arguments.length && i < 7; i++) {
			dte[i] = arguments[i];
		}
		newDate = new Date(dte[0], dte[1], dte[2], dte[3], dte[4], dte[5]);
	} else if (typeof d == "number") {
		newDate = new Date(d);
	} else {
		newDate = null;
	}  
	if (newDate == "Invalid Date")
		return null;
	else
		return newDate;
};

Date.prototype.isSameDate = function(anotherDate) {
	//alert("Comparing " + formatDate(this, "dd.MM.yyyy") + " to " + formatDate(anotherDate, "dd.MM.yyyy"));
	if (anotherDate == null) {
		return false;
	}
	if (this.getDate() != anotherDate.getDate()) {
		return false;
	}
	if (this.getMonth() != anotherDate.getMonth()) {
		return false;
	}
	if (this.getYear() != anotherDate.getYear()) {
		return false;
	}
	return true;
}
