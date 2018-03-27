function Scores() {
	var sr = this;
	sr.gameDetails = null;
	sr.gameMembers = null;
	sr.gameRounds = null;
	sr.gameId = null;	
	sr.setGameId = function() {
		sr.gameId = location.queryString.gameId;
		sr.getGameDetails();
	}
	
	sr.getGameDetails = function() {
		ajax.get("/rummy/game/"+sr.gameId, function(data) {
			sr.gameDetails = data;
			sr.bindGame();
		});
	}
	
	sr.getGameMembers = function() {
		ajax.get("/rummy/members/"+sr.gameId,function(data) {
			sr.gameMembers = data;
			sr.bindTable(function() {
				sr.createRound();
			});
		});
	}
	
	sr.createRound = function() {
		ajax.get("/rummy/rounds/create/"+sr.gameId, function(data) {
			sr.gameRounds = data;
			sr.bindEditableRow();
		});
	}
	
	sr.bindGame = function() {
		$("#game_detail").find("h1 span").html(sr.gameDetails.id);
		$("#game_detail").find("h6 span").html(new Date(sr.gameDetails.createdDate));
		$("#game_detail").find("p span").html(sr.gameDetails.maxScore);
	}
	
	sr.bindTable = function(run) {
		$("#scores_table").find("thead").html("<tr><th>Round No.</th></tr>;");
		for(var i=0;i<sr.gameMembers.length;i++) {
			$("#scores_table").find("thead tr").append("<th>"+sr.gameMembers[i].name+"</th>");
		}
		run();
	}
	
	sr.bindEditableRow = function() {
		$("#scores_table").find("tbody").append("<tr data-id='"+sr.gameRounds.id+"'><td>"+sr.gameRounds.customId+"</td></tr>");
		for(var i=0;i<sr.gameMembers.length;i++) {
			$("#scores_table").find("tbody tr").append("<td data-id='"+sr.gameMembers[i].id+"'><input type='text' class='form-control' /></td>");
		}
	}
	
	sr.submitScores = function() {
		var payload = {};
		payload.round_id = sr.gameRounds.id;
		payload.players = [];
		$("#scores_table").find("tbody").find("tr[data-id='"+sr.gameRounds.id+"']").find("td").each(function() {
			var member_id = $(this).attr('data-id');
			var obj = {};
			if(member_id != undefined) {
				obj.player_id = member_id;
				obj.score = $(this).find("input").val();
				payload.players.push(obj);
			}
		});
		ajax.post("/rummy/scoreboard/add", payload, function() {
			sr.createRound();
		});
	}
	
	sr.init = function() {
		sr.setGameId();
		sr.getGameMembers();
	}
}


location.queryString = {};
location.search.substr(1).split("&").forEach(function (pair) {
    if (pair === "") return;
    var parts = pair.split("=");
    location.queryString[parts[0]] = parts[1] &&
        decodeURIComponent(parts[1].replace(/\+/g, " "));
});

var scores = new Scores();
scores.init();