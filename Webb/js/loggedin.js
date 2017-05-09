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

var email = "inget@tomt.se";
var providerData;
var uid

function initApp() {
    firebase.auth().onAuthStateChanged(function (user) {
        if (user) {
            // User is signed in.
            var displayName = user.displayName;
            email = user.email;
            var isAnonymous = user.isAnonymous;
            uid = user.uid;
            var providerData = user.providerData;
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
<<<<<<< HEAD
}
=======
}

var app = angular.module("LoggedIn", []);
app.controller("loggedInUser", function ($scope) {
    $scope.uid = uid;
    $scope.email = email;

});
>>>>>>> origin/Gio-test
