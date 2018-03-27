function Ajax() {
	var ax = this;

	ax.get = function(url, run) {
		$.ajax({
			type : 'GET',
			crossDomain : true,
			url : url,
			contentType : 'application/json; charset=ISO-8859-15',
			dataType : 'json',
			success : function(jsonData, textStatus, xhr) {
				if (xhr.status === 200) {
					run(jsonData);
				}
			},
			error : function(xhr, options, exc) {
				if (xhr.status === 401) {
					run({
						status : "error"
					});
				} else {
					run({
						status : "connection"
					});
				}
			}
		});
	}

	ax.post = function(url, payload, run) {
		$.ajax({
			type : 'POST',
			crossDomain : true,
			url : url,
			data : JSON.stringify(payload),
			contentType : 'application/json; charset=ISO-8859-15',
			dataType : 'json',
			success : function(jsonData, textStatus, xhr) {
				if (xhr.status === 200) {
					run(jsonData);
				}
			},
			error : function(xhr, options, exc) {
				if (xhr.status === 401) {
					run({
						status : "error"
					});
				} else {
					run({
						status : "connection"
					});
				}

			}
		});
	}
	
	ax.remove = function(url, run) {
		$.ajax({
			type : 'DELETE',
			crossDomain : true,
			url : url,
			contentType : 'application/json; charset=ISO-8859-15',
			dataType : 'json',
			success : function(jsonData, textStatus, xhr) {
				if (xhr.status === 200) {
					run();
				}
			},
			error : function(xhr, options, exc) {
				if (xhr.status === 401) {
					run({
						status : "error"
					});
				} else {
					run({
						status : "connection"
					});
				}
			}
		});
	}
}

var ajax = new Ajax();