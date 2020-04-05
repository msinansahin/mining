/**
 * Sağdaki özellikler penceresini çizer
 * @param elementId
 * @returns
 */
function Properties(elementId) {
	this.elementId = elementId;
	this.excludes = ['__gohashid'];
	/**
	 * tabloya nesneyi yazar
	 */
	this.load = function(obj) {
		$('#properties-div').show();
		var data = this.prepareObject(obj);
		$('#' + this.elementId).bootstrapTable({
			data : data
		});
		$('#' + this.elementId).bootstrapTable('load', data)
	}
	
	this.remove = function() {
		$('#' + this.elementId).bootstrapTable('removeAll');
	}
	
	/**
	 * key, value ikililerini dönüştürür
	 */
	this.prepareObject = function(obj) {
		var data = [];
		for ( var key in obj) {
			if (_.includes(this.excludes, key)) {
				continue;
			}
			data.push({
				key : key,
				value : obj[key]
			});
		}
		return data;
	}

}