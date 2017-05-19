var app = angular.module('LoggedIn', ['firebase', 'ngAnimate']);
var grades = [];

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

            $scope.user.$loaded().then(function () {

                // Get my class from scope and use it to get courses
                $scope.myClass = $scope.user.myClass;
                var klass = $scope.myClass;
                $scope.myCourses = $firebaseObject(ref.child('coursesByClass/' + klass));
                $scope.myGrades = $firebaseArray(ref.child('users').child('students/' + userId).child('grades').child('courses'));

                //console.log(myGrades);
                $scope.myGrade = firebase.database().ref().child('users').child('students/' + userId).child('grades').child('courses');

                // Get date for feelings
                $scope.date = new Date();
                $scope.myDate = new Date($scope.date.getFullYear(),
                    $scope.date.getMonth(),
                    $scope.date.getDate());
                $scope.myDate = $filter('date')($scope.myDate, 'yyyyMMdd');
                var date = $scope.myDate;

                // Register feelings to Firebase
                $scope.setFeeling = function (feeling) {
                    firebase.database().ref().child('feelings').child(klass).child(date).update({
                        [userId]: feeling
                    });
                    alert("Rösten registrerad");
                }
            });

            //Hämtar betyg
            // $scope.init = function (grades) {
            //     $scope.grade = firebase.database().ref().child('grades').child(userId).child(grades);
            //     $scope.grade.on('value', function (snap) {
            //         snap.forEach(function (Snapshot) {
            //             $scope.kursNamn = Snapshot.key;
            //             $scope.kursBetyg = Snapshot.val();
            //             // console.log($scope.kursNamn + ' ' + $scope.kursBetyg);
            //         });
            //     })
            // }

            
            var testGrades = function(){
                console.log($scope.gradesData);
            }

            $scope.grabFinalGrades = function () {
                $scope.grade = firebase.database().ref().child('grades').child(userId).child("final");
                $scope.grade.on('value', function (snap) {
                    $scope.globalgrades = {
                        "grades":[
                        ]
                    }
                    snap.forEach(function (Snapshot) {
                        $scope.test1 = Snapshot.key;
                        $scope.test2 = Snapshot.val();
                        $scope.addGrades();
                    })
                    // console.log($scope.globalgrades);
                });

                

                //Adds object to the globalgrades object
                $scope.addGrades = function () {
                    $scope.globalgrades.grades.push({
                        "courseCode" :  $scope.test1,
                        "grade" : $scope.test2
                    });
                    testGrades();
                    $scope.gradesData = $scope.globalgrades.grades;
                    
                };
                
                
            }
        });
    }
]);

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