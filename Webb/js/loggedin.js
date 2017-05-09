// Firebase configuration
var config = {
    apiKey: "AIzaSyCzIv9ECgSeBxgu_p8oJLDpzoLp4YydvEg",
    authDomain: "scrummerz-2e3f3.firebaseapp.com",
    databaseURL: "https://scrummerz-2e3f3.firebaseio.com",
    projectId: "scrummerz-2e3f3",
    storageBucket: "scrummerz-2e3f3.appspot.com",
    messagingSenderId: "936353564933"
};
firebase.initializeApp(config);

function toggleSignIn() {
    firebase.auth().signOut();
    window.location = "index.html";
}

var email;
var uid;
var providerData;
var userId


function initApp() {
    firebase.auth().onAuthStateChanged(function (user) {
        if (user) {
            // User is signed in.
            displayName = user.displayName;
            email = user.email;
            emailVerified = user.emailVerified;
            photoURL = user.photoURL;
            isAnonymous = user.isAnonymous;
            uid = user.uid;
            userId = user.uid;
            providerData = user.providerData;
            // ...
        } else {
            // User is signed out.
            // ...
        }

        return firebase.database().ref('/users/Pupils/' + userId).once('value').then(function (snapshot) {
            var username = snapshot.val().username;
            var name = snapshot.val().Name;
            var pnr = snapshot.val().Pnr;

        });
    });

    document.getElementById('quickstart-sign-in').addEventListener('click', toggleSignIn, false);
}

window.onload = function () {
    initApp();
}