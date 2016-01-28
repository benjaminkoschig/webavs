function isValidMonthDate(date) {
	var REGEX = /^(0[1-9]|1[0-2])\.[1-9][0-9]{3}/i;
	return REGEX.test(date);
}

function isValidDate(date) {
	var REGEX = /^(0[1-9]|[12][0-9]|3[0-1])\.(0[1-9]|1[0-2])\.[1-9][0-9]{3}/i;
	return REGEX.test(date);
}

function unformatDate(date) {
	var unformatedDate;
	var parts = date.split('.');
	
	unformatedDate = parts[2];
	unformatedDate += parts[1];
	unformatedDate += parts[0];
	return unformatedDate;
}