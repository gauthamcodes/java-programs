function Home() {
	var hm = this;
	hm.highScore = null;
	hm.getHighScores = function() {
		ajax.get("/rummy/game/maxscore", function(data) {
			for(var i=0;i<data.length;i++) {
				$("#max_score").append("<li><a onclick='home.setHighScore("+data[i]+")'>"+data[i]+"</a></li>");
			}
		});
	}
	
	hm.setHighScore = function(elem) {
		hm.highScore = elem;
		$("#set_high_score").html(elem);
	}
	
	hm.createGame = function() {
		var payload = {};
		payload.max_score = hm.highScore;
		ajax.post("/rummy/game/create", payload, function() {
			console.log("done");
		});
	}
	
	hm.init = function() {
		hm.getHighScores();
	}
}

var home = new Home();

home.init();