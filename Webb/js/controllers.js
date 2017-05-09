var guid;

(function () {

    angular
        .module('LoggedIn', ['firebase'])
        .factory("Auth", ["$firebaseAuth",
            function ($firebaseAuth) {
                return $firebaseAuth();
            }
        ])
        .controller('personCtrl', ["$scope", "$firebaseObject", "Auth",
            function ($scope, $firebaseObject, Auth) {
                var ref = firebase.database().ref();
                $scope.auth = Auth;
                $scope.data = $firebaseObject(ref.child('courses').child('2017'));
                $scope.auth.$onAuthStateChanged(function (firebaseUser) {
                    $scope.firebaseUser = firebaseUser;
                    guid = firebaseUser.uid;
                });
            }]);
}());