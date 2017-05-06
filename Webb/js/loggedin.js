function toggleSignIn() {  
        firebase.auth().signOut();
        window.location = "index.html";
}

function initApp() {
    firebase.auth().onAuthStateChanged(function (user) {
            var displayName = user.displayName;
            var email = user.email;
            var emailVerified = user.emailVerified;
            var photoURL = user.photoURL;
            var isAnonymous = user.isAnonymous;
            var uid = user.uid;
            var providerData = user.providerData;

            document.getElementById('quickstart-account-details').textContent = JSON.stringify(user, null, '  ');
    });
    document.getElementById('quickstart-sign-in').addEventListener('click', toggleSignIn, false);
}

window.onload = function () {
    initApp();
}