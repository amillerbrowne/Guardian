

$(document).ready( function(){

// firebase.initializeApp(config);
var database = firebase.database();
  
	

const btnLogIn= document.getElementById("btnLogIn");



//Add Login Event



firebase.auth().onAuthStateChanged(function(firebaseUser) {
	if(firebaseUser){
		loadFirebaseUser(firebaseUser);
		console.log(firebaseUser);
    window.location = 'maps.html'; //After successful login, user will be redirected to home.html
 
	} else{
		console.log('not logged in');
	}

});
});

var currentUser;

function loadFirebaseUser(newUser){
	currentUser = newUser;
}

function OnLoginClicked(e){
	console.log("here!");

	//Get Elements
	const email = document.getElementById('email');
	const password = document.getElementById("password");
	//get email and password
	const txtEmail = email.value; 
	const txtPass = password.value;
	const auth = firebase.auth();
 //Sign in
      const promise = auth.signInWithEmailAndPassword(txtEmail, txtPass);
      promise.catch(function(e)
      	{ 
      		console.log(e.message)
      	});
	console.log("logged in!");

}