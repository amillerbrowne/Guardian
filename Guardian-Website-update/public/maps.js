
 var config = {
      apiKey: "AIzaSyBVVVyzApUOkFFp2JBQl1lUvOP5YnVPvd8",
      authDomain: "guardian-92550.firebaseapp.com",
      databaseURL: "https://guardian-92550.firebaseio.com",
      projectId: "guardian-92550",
      storageBucket: "guardian-92550.appspot.com",
    };

    firebase.initializeApp(config);



// firebase.initializeApp(config);
var database = firebase.database();
 var ref = new Firebase("https://guardian-92550.firebaseio.com/emergency/");
 // var userId = firebase.auth().currentUser.uid;

//PRINTS OUT FIRST NAME!!!
firebase.auth().onAuthStateChanged(function(user) {
	console.log(user);
  if (user) {
    ref.on("value", function(snapshot) {
    if(user = snapshot.val()) {
    	var userId = firebase.auth().currentUser.uid;
    	// var xRef = firebase.database().ref('emergency/' + user.sb + '/runnerid');
        var Ref = new Firebase("https://guardian-92550.firebaseio.com/emergency/"+userId+"/");
        console.log(firebase.auth().currentUser.uid);
         Ref.once("value", function(snapshot) {
  		var data = snapshot.val();
  		var name = data["efirstName"];
  		var runner = data["runnerid"];
  		console.log(runner);
  		getRunnerInfo(runner);
 	document.getElementById("Ename").innerHTML = ("Welcome: " + name);
		});
}
});
  } else {
    // No user is signed in.
  }
});


function getRunnerInfo(runner){
	console.log(runner);
var rRef = new Firebase("https://guardian-92550.firebaseio.com/runner/"+runner+"/");
	 rRef.once("value", function(snapshot) {
  		var data = snapshot.val();
  		var rlong = data["longitude"];
  		var rlat = data["latitude"];
  		console.log(rlong);
  		console.log(rlat);
  		document.getElementById("latitude").innerHTML = ("Runner's Last Latitude: " + rlat);
	 document.getElementById("longitude").innerHTML = ("Runner's Last longitude: " + rlong);
	 initMap(rlat, rlong);
		});
	 
	}
	 function initMap(rlat, rlong) {
        var uluru = {lat: rlat, lng: rlong};
        var map = new google.maps.Map(document.getElementById('map'), {
          zoom: 8,
          center: uluru
        });
        var marker = new google.maps.Marker({
          position: uluru,
          map: map
        });
      }

	function logUserOut(e){
	console.log("here!");

	firebase.auth().signOut().then(function() {
        console.log("Sign-out successful.");
         window.location = 'login.html'; //After successful logout, user will be redirected to login.html
      }, function(error) {
        console.log("An error happened.");
      });
	console.log("logged Out!");

}





//works to display all user information
// var ref = new Firebase("https://guardian-92550.firebaseio.com/emergency/");
// firebase.auth().onAuthStateChanged(function(user) {
//   if (user) {
//     ref.on("value", function(snapshot) {
//     for (x in snapshot.val()) {
//         var xRef = new Firebase("https://guardian-92550.firebaseio.com/emergency/"+x+"/");
//         xRef.once("value", function(xsnapshot) {
//             var data = xsnapshot.val();
//             var name = data["runnerid"];
//             console.log(name);
//         });
//     }
// });
//   } else {
//     // No user is signed in.
//   }
// });





    

