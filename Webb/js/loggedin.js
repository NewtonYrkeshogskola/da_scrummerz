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
        dataRef.on('child_added', snap => {
            var klass = snap.child('class').val();
            var name = snap.child('name').val();
            var pnr = snap.child('pnr').val();

            $("#table").append("<tr><td>" + name + "</td><td>" + pnr + "</td><td>" + klass + "</td></tr>");
        });
        document.getElementById('quickstart-account-details').textContent = JSON.stringify(user, null, '  ');
    });
    document.getElementById('quickstart-sign-in').addEventListener('click', toggleSignIn, false);
}

window.onload = function () {
    initApp();
}