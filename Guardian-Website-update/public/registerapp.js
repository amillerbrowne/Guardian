


$("#btnLogIn2").on("click", function () {
        console.log("create user");
        var email = $("#email").val();
        var password = $("#password").val();
        var fName = $("#firstName").val();
        var lName = $("#lastName").val();
        var rId = $("#runnerID").val();

        createUser(email, password, fName, lName, rId);
    });

    var createUser = function (email, password, fName, lName, rId) {
        firebase.auth().createUserWithEmailAndPassword(email, password).catch(function (error) {
            // Handle Errors here.
            var errorCode = error.code;
            var errorMessage = error.message;
            console.log("error code: " + errorCode);
            console.log("error message: " + errorMessage);
        });
    };

// $(document).ready( function(){

// // firebase.initializeApp(config);
// var database = firebase.database();
//  var ref = new Firebase("https://guardian-92550.firebaseio.com/emergency/");
  
	

// const btnLogIn2= document.getElementById("btnLogIn2");
// btnLogIn2.OnSignUpClicked;



// //Add Login Event



// firebase.auth().onAuthStateChanged(function(firebaseUser) {
// 	if(firebaseUser){
// 		console.log(firebaseUser);
//     window.location = 'maps.html'; //After successful login, user will be redirected to home.html
 
// 	} else{
// 		console.log('not logged in');
// 	}

// });
// });


// function OnSignUpClicked(e){
// 	console.log("here!");

// 	//Get Elements
// 	const email = document.getElementById('email');
// 	const password = document.getElementById("password");
// 	const fName = document.getElementById('firstName');
// 	const lName = document.getElementById("lastName");
// 	const rId = document.getElementById('runnerID');
// 	//get email and password
// 	const email = email.value; 
// 	const password = password.value;
// 	const fName = firstName.value; 
// 	const lName = lastName.value;
// 	const rId = runnerID.value; 
// 	const auth = firebase.auth();
 

//  //Sign UP
//  	firebase.catch(function(error) {
//   // Handle Errors here.
//   var errorCode = error.code;
//   var errorMessage = error.message;
//   // ...
// });
//       const promise = auth().createUserWithEmailAndPassword(email, password, fName, lName, rId);
//       promise.catch(function(e)
//       	{ 
//       		console.log(e.message)
//       	});
// 	console.log("Created Emergency Contact!");

// }