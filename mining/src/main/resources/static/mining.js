var LINK_TEMPLATES = [
	{sayi: 0,	category: ''}, // 100'e kadar
	{sayi: 1,	category: '1'}, // 200'e kadar
	{sayi: 2,	category: '2'},
	{sayi: 3,	category: '3'},
	{sayi: 4,	category: '4'},
	{sayi: 5,	category: '5'},
	{sayi: -6,	category: '-1'} // 500'den büyükler için
	];

function Data(nodeDataArray, linkDataArray) {
  this.nodeDataArray = nodeDataArray;
  this.linkDataArray = linkDataArray;
  
  this.prepare = function() {
	$.each(this.nodeDataArray, function( index, value ) {
		value['key'] = value.oid;
		value['color'] = 'pink';
		if (!_.isEmpty(value['sayi'])) {
			value['text'] = value['text'] + '\n' + value['sayi'];
		}			
    });
	
	/**
	 * kalınlıkları ayarlar
	 */
	$.each(this.linkDataArray, function( index, value ) {
		var sayi = parseInt(parseInt(value['text']) / 100);
		var template = _.find(LINK_TEMPLATES, function(o) { return o.sayi === sayi; });
		if (!_.isEmpty(template)) {
			value['category'] = template.category;
		} else {
			value['category'] = '-1';
		}
    });
  }
}

function ProcessMap(data) {
	this.data = _.isString(data) ? JSON.parse(data) : data;
	this.activities = data.activities || [];
	this.paths = data.paths || [];
	this.events = data.events || [];
	
	/**
	 * path'leri ve activite'leri gezerek data nesnesini oluşturur
	 */
	this.toData = function() {
		var nodeDataArray = [],
			linkDataArray = [];
		var _this = this;
		console.log("activities length: " + this.activities.length);
		this.activities.forEach(function(activity) {
			nodeDataArray.push(_this.createActivity(activity));
		});
		
		// If Start does not exists in activities add it
		
		console.log("paths length: " + this.paths.length);
		this.paths.forEach(function(path) {
			linkDataArray.push(_this.createPath(path, this));
		});
		
		var data = new Data(nodeDataArray, linkDataArray);
		console.log(nodeDataArray);
		console.log(linkDataArray);
		return data;
	}
	
	this.createActivity = function(activity) {
		return {
			"oid"		: activity.activityId,
			"sayi"		: (activity.patients ? activity.patients.length : activity.patientCount) + '',
			"text"		: isStart(activity) ? "" : activity.activityName,
			"category"	: isStart(activity) ? "start" : ""
		};
	}
	
	this.createPath = function(path) {
		return {
			"from"	: this.findActivityIdByName(path.from),
			"to"	: this.findActivityIdByName(path.to), 
			"text"	: (path.patients ? path.patients.length : 0) + '',
		};
	}
	
	this.findActivityIdByName = function (activityName) {
		for (var i = 0; i < this.activities.length; i++) {
			var activity = this.activities[i];
			if (activity.activityName.toUpperCase() === activityName.toUpperCase()) {
				return activity.activityId;
			}
		}
		alert(activityName + ' aktivitesi bulunamadı. Diyagram çizilemeyecek.');
		return 'NF';
	}
	
	function isStart(activity) {
		return activity.activityName.toLowerCase() === "start";
	}
}