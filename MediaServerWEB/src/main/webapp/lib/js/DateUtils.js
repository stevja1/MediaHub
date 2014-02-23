function DateUtils() {};

/**
 * Extracts the hours and minutes from a unix timestamp and formats them to look like a time. Uses 24-hour format.
 * @param long inTime Contains a unix epoch timestamp
 * @return String the formatted time (e.g. 03:00)
 */
DateUtils.formatTimeFromTimestamp = function ( inTime )
{
	var date = new Date();
	date.setTime( inTime );
	var hours = date.getHours();
	var minutes = date.getMinutes();
	if( hours < 10 ) hours = '0' + hours;
	if( minutes < 10 ) minutes = '0' + minutes;
	return hours+':'+minutes;
};

/**
 * Parses the time out of the time string and configures the date object that was passed in.
 * @param timeString Will parse the following patterns:
 *   "8" - Will set the time to 8 am
 *   "8:30" - Will set the time to 8:30am
 *   "8pm" - Will set the time to 8pm
 *   "8:30pm" - Will set the time to 8:30pm
 *   "8 pm" - Will set the time to 8pm
 *   "8:30 pm" - Will set the time to 8:30pm
 *   "20" - Will set the time to 8pm
 *   "20:30" - Will set the time to 8:30pm
 * @param date This should contain a date where the year, month, and day have been set. This object will be returned.
 * @return Date Returns the date object that was passed in, but with the hour and minute parts set
 */
DateUtils.parseTime = function( timeString, date ) {
	var retVal = false;
	var hours = 0;
	var minutes = 0;
	
	// Clean up if needed
	timeString = timeString.trim();

	// Extract hours
	hours = DateUtils.parseHours(timeString);
	
	// Deal with times that have a ':'
	if(timeString.indexOf(':') > 0) {
		minutes = DateUtils.parseMinutes(timeString);
	} else {
		minutes = "0";
	}
	if(hours.length > 0 && minutes.length > 0) {
		date.setHours(hours);
		date.setMinutes(minutes);
		date.setSeconds(0);
		date.setMilliseconds(0);
		retVal = date;
	}
	return retVal;
};

/**
 * Parses the hour part out of a string in the following format hh:mm
 * Assumes 24-hour format. For example, "23:03" would return 23.
 * If "3:00pm" or "3:00 pm" is passed in, 15 is returned.
 * If "3pm" or "3 pm" is passed in, 15 is also returned.
 * Finally, if "3" is passed in, 3 will be returned. If "15" is passed in, 15 will be returned.
 * @param String Contains a time in format hh:mm
 * @return String Returns the hour part of the time.
 */
DateUtils.parseHours = function ( timeString )
{
	var retVal = "";
	// Clean up our string if needed
	timeString = timeString.trim();
	
	// Find a postfix if there is one
	var postfix = "";
	if(timeString.indexOf('am') > 0 || timeString.indexOf('AM') > 0 || timeString.indexOf('Am') > 0)
		postfix = "am";
	else if(timeString.indexOf('pm') > 0 || timeString.indexOf('PM') > 0  || timeString.indexOf('Pm') > 0)
		postfix = "pm";
	
	var timeParts;
	// Deal with times that have a ':'
	if(timeString.indexOf(":") >= 0 ) {
		timeParts = timeString.split(":");
		if(postfix.length > 0 && postfix == "pm" && timeParts[0] >= 0 && timeParts[0] <= 12)
			retVal = timeParts[0]+12;
		else
			retVal = timeParts[0];
	}
	// Deal with times that don't have a ':'
	else {
		// Find postfix if there is one
		var postfixIndex = 0;
		postfixIndex = timeString.indexOf('am');
		if(postfixIndex <= 0) postfixIndex = timeString.indexOf('AM');
		if(postfixIndex <= 0) postfixIndex = timeString.indexOf('Am');
		if(postfixIndex <= 0) postfixIndex = timeString.indexOf('pm');
		if(postfixIndex <= 0) postfixIndex = timeString.indexOf('PM');
		if(postfixIndex <= 0) postfixIndex = timeString.indexOf('Pm');
		// No postfix...
		if(postfixIndex <= 0 && timeString >= 0 && timeString < 24) retVal = timeString;
		// Found a postfix...
		else {
			// Parse out the hour
			var hour = timeString.substring(0,postfixIndex);
			hour = hour.trim();
			if(postfix == "pm" && hour >= 0 && hour <= 12) hour+=12;
			retVal = hour;
		}
	}
	return retVal;
};

/**
 * Parses the minute part out of a string in the following format hh:mm
 * For example, 23:03 would return 03.
 * @param String Contains a time in format hh:mm
 * @return String Returns the minute part of the time.
 */
DateUtils.parseMinutes = function ( timeString )
{
	var retVal = "";
	var timeParts;
	if(timeString.indexOf(":") >= 0 ) {
		timeParts = timeString.split(":");
		if(timeParts[1] >= 0 && timeParts[1] < 60)
			retVal = timeParts[1];
	}
	return retVal;
};

/**
 * Takes time expressed in minutes and formats it so its in the format hh:mm. For example, 303 would become 05:03.
 * @param String Contains the number of minutes
 * @return String Returns a string representing the input in the format hh:mm.
 */
DateUtils.formatDuration = function (duration)
{
	var hours = Math.floor(duration / 60);
	var minutes = duration % 60;
	if(hours < 10) hours = '0'+hours;
	if(minutes < 10) minutes = '0'+minutes;
	var retVal = hours+':'+minutes;
	return retVal;
};