(function(){

    angular
        .module('LoggedIn', ['firebase'])
        .controller('personCtrl', function($firebaseObject){
            const rootRef = firebase.database().ref().child('scrummerz-2e3f3');
            const ref = rootRef.child('object');
            const test = ref.child('name');
            this.object = $firebaseObject(test);
        });

}());