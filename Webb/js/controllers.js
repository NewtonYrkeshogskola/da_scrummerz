var guid;


var app = angular.module('LoggedIn', ['firebase', 'ngAnimate']);

app.factory("Auth", ["$firebaseAuth",
    function ($firebaseAuth) {
        return $firebaseAuth();
    }
]);

app.controller('personCtrl', ["$scope", "$firebaseObject", "$firebaseArray", "Auth",
    function ($scope, $firebaseObject, $firebaseArray, Auth) {
        var ref = firebase.database().ref();
        $scope.auth = Auth;
        $scope.data = $firebaseObject(ref.child('courses'));
        $scope.auth.$onAuthStateChanged(function (firebaseUser) {
            var userId = firebaseUser.uid;
            $scope.firebaseUser = firebaseUser;
            $scope.user = $firebaseObject(ref.child('users').child('students/' + userId).child('details'));
            // $scope.userClass = $firebaseObject(ref.child('users').child('students/' + userId).child('details'));
            $scope.user.$loaded().then(function () {
                $scope.data1 = firebase.database().ref().child('courses');
                $scope.myClass = $scope.user.myClass;
                var data1 = $scope.data1;
                var klass = $scope.myClass;
                $scope.myCourses = $firebaseObject(ref.child('coursesByClass/' + klass));
                $scope.myGrades = $firebaseObject(ref.child('users').child('students/' + userId).child('grades').child('courses'));
                $scope.myGrade = firebase.database().ref().child('users').child('students/' + userId).child('grades').child('courses');
                $scope.grade = null;
                $scope.init = function(gra){
                    $scope.grade = firebase.database().ref().child('users').child('students/' + userId).child('grades').child('courses/' + gra);
                     $scope.grade.on('value', function (snap) {
                    snap.forEach(function (Snapshot) {
                        $scope.kursNamn = Snapshot.key;
                        $scope.kursBetyg= Snapshot.val();
                        console.log($scope.kursNamn + ' ' + $scope.kursBetyg);
                    });
                })
                }
/*                $scope.myGrade.on('value', function (snap) {
                    snap.forEach(function (Snapshot) {
                        $scope.kursNamn = Snapshot.key;
                        $scope.kursBetyg= Snapshot.val();
                        console.log($scope.kursNamn + ' ' + $scope.kursBetyg);
                    });
                })*/
                //var query = firebase.database().ref().child('coursesByClass/' + klass);
                /*                query.once('value', snap => console.log(snap.val()));
                                query.once('value', function (snap) {
                                    console.log(snap.val())
                                });
                            $scope.list = $firebaseArray(query);
                                console.log(list);
                                $scope.klassCoursedetails = function () {
                                    query.on('child_added', snap => {
                                        var courseref = data1.child(snap.key);
                                        console.log(courseref.once('value'));
                
                                    });
                                }
                                var query444 = $scope.klassCoursedetails($scope.myClass);
                                console.log(query444);
                                var query2 = firebase.database().ref().child('courses');
                                var list2 = $firebaseArray(query2);
                                // console.log(list2);
                                console.log("loaded record:", list2.$getRecord("Courses"));
                                var rec = $scope.myCourses.$id;
                                console.log(rec);
                                return firebase.database().ref('courses').once('value').then(function (snapshot) {
                                    var username = snapshot.val();
                                    console.log(username);
                                });*/
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
