function Members() {
	var mb = this;
	
	mb.gameId = null;
	
	
	mb.setGame = function() {
		mb.gameId = parseInt(location.queryString.gameId);
		$("#game_id").find("span").html(mb.gameId);
	}
	
	mb.getAllPlayers = function() {
		ajax.get("/rummy/players/list", function(data) {
			mb.bindPlayers(data);
		});
	}
	
	mb.bindPlayers = function(data) {
		$("#select_box").empty();
		for(var i=0;i<data.length;i++) {
			$("#select_box").append("<label><input type='checkbox' name='select_1' value='"+data[i].id+"'/>" +
					data[i].name + "</label>");			
		}
	}
	
	mb.addPlayers = function() {
		var payload = {};
		payload.game_id = mb.gameId;
		payload.members_id = [];
		$("#select_box").find("label").each(function() {
			if($(this).find("input").prop('checked'))
				payload.members_id.push(parseInt($(this).find("input").val()));
		});
		ajax.post("/rummy/members/add", payload, function() {
			console.log("players added");
		});
	}
	
	mb.init = function() {
		mb.setGame();
		mb.getAllPlayers();
	}
}

location.queryString = {};
location.search.substr(1).split("&").forEach(function (pair) {
    if (pair === "") return;
    var parts = pair.split("=");
    location.queryString[parts[0]] = parts[1] &&
        decodeURIComponent(parts[1].replace(/\+/g, " "));
});

var members = new Members();
members.init();