var app = angular.module('LoggedIn', ['firebase', 'ngAnimate']);

app.factory("Auth", ["$firebaseAuth",
    function ($firebaseAuth) {
        return $firebaseAuth();
    }
]);

app.controller('personCtrl', ["$scope", "$firebaseObject", "$firebaseArray", '$filter', "Auth",
    function ($scope, $firebaseObject, $firebaseArray, $filter, Auth) {
        var ref = firebase.database().ref();
        $scope.auth = Auth;
        $scope.auth.$onAuthStateChanged(function (firebaseUser) {
            var userId = firebaseUser.uid;
            $scope.firebaseUser = firebaseUser;
            $scope.user = $firebaseObject(ref.child('users').child('students/' + userId).child('details'));
            // $scope.userClass = $firebaseObject(ref.child('users').child('students/' + userId).child('details'));
            $scope.user.$loaded().then(function () {
                $scope.myClass = $scope.user.myClass;
                var klass = $scope.myClass;
                $scope.myCourses = $firebaseObject(ref.child('coursesByClass/' + klass));
                $scope.myGrades = $firebaseArray(ref.child('users').child('students/' + userId).child('grades').child('courses'));
                //console.log(myGrades);
                $scope.myGrade = firebase.database().ref().child('users').child('students/' + userId).child('grades').child('courses');
                
                $scope.date = new Date();
                $scope.myDate = new Date($scope.date.getFullYear(),
                    $scope.date.getMonth(),
                    $scope.date.getDate());
                $scope.myDate = $filter('date')($scope.myDate, 'yyyyMMdd');
                var date = $scope.myDate;
                console.log(date);
                $scope.setFeeling = function (feeling){
                    firebase.database().ref().child('testweb').child(klass).child(date).child(userId).set({
                        feeling
                    });
                }
            });
        });
    }]);

app.controller("AdminUserCtrl", ["$scope", "Auth",
    function ($scope, Auth) {
        $scope.createUser = function () {
            $scope.message = null;
            $scope.error = null;
            // Create a new user
            Auth.$createUserWithEmailAndPassword($scope.email, $scope.password)
                .then(function (firebaseUser) {
                    $scope.message = "Användare skapad med UID: " + firebaseUser.uid;
                }).catch(function (error) {
                    $scope.error = error;
                });
        };

        $scope.deleteUser = function () {
            $scope.message = null;
            $scope.error = null;
            // Delete the currently signed-in user
            Auth.$deleteUser().then(function () {
                $scope.message = "Användare raderad";
            }).catch(function (error) {
                $scope.error = error;
            });
        };
    }
]);
