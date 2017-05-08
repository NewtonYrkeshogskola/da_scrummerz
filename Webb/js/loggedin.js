function toggleSignIn() {
    firebase.auth().signOut();
    window.location = "index.html";
}


var config = {
    apiKey: "AIzaSyCzIv9ECgSeBxgu_p8oJLDpzoLp4YydvEg",
    authDomain: "scrummerz-2e3f3.firebaseapp.com",
    databaseURL: "https://scrummerz-2e3f3.firebaseio.com",
    projectId: "scrummerz-2e3f3",
    storageBucket: "scrummerz-2e3f3.appspot.com",
    messagingSenderId: "936353564933"
};


firebase.initializeApp(config);

function initApp() {
    firebase.auth().onAuthStateChanged(function (user) {
        var displayName = user.displayName;
        var email = user.email;
        var emailVerified = user.emailVerified;
        var photoURL = user.photoURL;
        var isAnonymous = user.isAnonymous;
        var uid = user.uid;
        var providerData = user.providerData;
        var data = document.getElementById("data");
        var dataRef = firebase.database().ref().child('test1');
        dataRef.on('value', snap => {
            data.innerText = snap.child('test22').val();
        });
        document.getElementById('quickstart-account-details').textContent = JSON.stringify(user, null, '  ');
    });
    document.getElementById('quickstart-sign-in').addEventListener('click', toggleSignIn, false);
}

window.onload = function () {
    initApp();
}