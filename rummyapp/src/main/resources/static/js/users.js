function Users() {
	var ur = this;
	ur.data = null;
	ur.userData = null;
	ur.getAllUsers = function() {
		ajax.get("/rummy/players/list", function(data) {
			ur.data = data;			
			ur.bindDOM();
		})
	}
	
	ur.removeUser = function(id) {
		ajax.remove("/rummy/players/delete/"+id, function() {
			ur.getAllUsers();
		});
	}
	
	ur.editUser = function(id) {
		ajax.get("/rummy/players/"+id, function(data) {
			ur.userData = data;
			$("#update_name").val(data.name);
		}); 
	}
	
	ur.updateUser = function(id) {
		ur.userData.name = $("#update_name").val();
		ajax.post("/rummy/players/update", ur.userData, function() {
			$("#userNameUpdatedSuccessfully").fadeIn(350).delay(2000).fadeOut(350);
			ur.getAllUsers();
		});
	}
	
	ur.createUser = function() {
		var obj = {};
		obj.name = $("#name").val();
		obj.user_name = $("#user_name").val();
		if(obj.name && obj.user_name) {
			ajax.post("/rummy/players/create", obj, function(data) {
				if(data.status == "success") {
					$("#name").val("");
					$("#user_name").val("");
					$("#userNameCreatedSuccessfully").fadeIn(350).delay(2000).fadeOut(350);
					ur.getAllUsers();
				}
				else {
					$("#userNameNotAvailable").fadeIn(350).delay(2000).fadeOut(350);
				}
			});
		}
	}
	
	ur.bindDOM = function() {
		$("#users").find("tbody").empty();
		for(var i=0;i<ur.data.length;i++) {
			$("#users").find("tbody")
				.append("<tr><td>"+parseInt(i+1)+"</td><td>"+ur.data[i].name+"</td><td>"+ur.data[i].user_name+
						"</td><td><a href='#' onclick='users.editUser("+ur.data[i].id+")' data-toggle='modal' data-target='#updateModal'>Edit</a></td><td><a href='#' onclick='users.removeUser("+ur.data[i].id+")'>Delete</a></td></tr>");
		}
	}
	
	
	ur.init = function() {
		ur.getAllUsers();
	}
}

var users = new Users();
users.init();