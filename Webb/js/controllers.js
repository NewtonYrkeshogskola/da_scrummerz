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

        // Top level variables
        $scope.auth = Auth;
        $scope.globalGrades = [];
        $scope.globalAssignments = [];


        $scope.auth.$onAuthStateChanged(function (firebaseUser) {
            var userId = firebaseUser.uid;
            $scope.firebaseUser = firebaseUser;
            $scope.globalActiveAssignments = [];
            $scope.user = $firebaseObject(ref.child('users').child('students/' + userId).child('details'));
            $scope.userId = firebaseUser.uid;
            $scope.user.$loaded().then(function () {

                // Get my class from scope and use it to get courses
                $scope.myClass = $scope.user.myClass;
                var klass = $scope.myClass;
                $scope.myCourses = $firebaseObject(ref.child('coursesByClass/' + klass));
                $scope.myGrades = $firebaseArray(ref.child('users').child('students/' + userId).child('grades').child('courses'));

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
                }

                // Get the latest user's feeling
                ref.child('feelings').child(klass).child(date).on('value', function (snapshot) {
                    $scope.myLatestFeeling = [];
                    snapshot.forEach(function (childSnapshot) {
                        var childKey = childSnapshot.key;
                        var myFeelingsValue = childSnapshot.val();
                        $scope.myLatestFeeling.push({
                            key: childKey,
                            grade: myFeelingsValue
                        })

                    })
                });

                //function which gets path to database from input string (närvaro kod) and stores value to the db
                //attendance code should have following structure: courseCode/date(format:yyyymmdd)/code, ex: YAPP-APP/20170523/randomnumber
                $scope.attendance = function (pathToFirebase) {
                    var pathParts = pathToFirebase.split("/");
                    var kursPath = pathParts[0];
                    var datePath = pathParts[1];
                    var codePath = pathParts[2];
                    firebase.database().ref().child('coursesByClass').child(klass).child(kursPath).child(datePath).child(codePath).update({
                        [userId]: true
                    });
                    alert(kursPath + ' ' + datePath + ' ' + codePath);
                }
                /*!!!!!!!!!!! DO NOT DELETE THIS. WE MAY NEED THIS LATER*/
                /*$scope.giveFeedbackToTheFinishedCourse = function (kurs, q1, q2, q3, q4, q5, q6, q7) {
                     firebase.database().ref().child('finishedCourseFeedback').child(kurs).child(q1).update({
                         q1
                     });
                      firebase.database().ref().child('finishedCourseFeedback').child(kurs).child(q2).update({
                         q2
                     });
                      firebase.database().ref().child('finishedCourseFeedback').child(kurs).child(q3).update({
                         q3
                     });
                      firebase.database().ref().child('finishedCourseFeedback').child(kurs).child(q4).update({
                         q4
                     });
                      firebase.database().ref().child('finishedCourseFeedback').child(kurs).child(q5).update({
                         q5
                     });
                      firebase.database().ref().child('finishedCourseFeedback').child(kurs).child(q6).update({
                         q6
                     });
                      firebase.database().ref().child('finishedCourseFeedback').child(kurs).child(q7).update({
                         q7
                     });
                    alert(kurs + " " + q1 + " " + q2 + " " + q3 + " " + q4 + " " + q5 + " " + q6 + " " + q7);
                }*/
                // Loop through all active assignments under personal node and push to globalAssignments
                ref.child('coursesByClass').child(klass).once('value', function (snapshot) {
                    snapshot.forEach(function (childSnapshot) {
                        var childKey
                        var childData
                        var courseKey = childSnapshot.key;

                        ref.child('coursesByClass').child(klass).child(courseKey).child('assignments').once('value', function (snapshot) {
                            snapshot.forEach(function (childSnapshot) {
                                childKey = childSnapshot.key;
                                childData = childSnapshot.val();

                                // For each child under each 
                                $scope.globalActiveAssignments.push({
                                    key: courseKey,
                                    childKey: childKey,
                                    assignment: childData
                                })
                            })
                        });
                    });
                });


            });
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

app.controller("gradesCtrl", ["$scope", "$firebaseObject", "$firebaseArray", '$filter', "Auth",
    function ($scope, $firebaseObject, $firebaseArray, $filter, Auth) {

        // Top level variables
        var ref = firebase.database().ref();
        var userId;
        $scope.auth = Auth;


        $scope.auth.$onAuthStateChanged(function (firebaseUser) {
            userId = firebaseUser.uid;
            $scope.firebaseUser = firebaseUser;
            $scope.user = $firebaseObject(ref.child('users').child('students/' + userId).child('details'));
            $scope.globalGrades = [];
            $scope.globalAssignments = [];

            // This will be executed after the user has been loaded
            $scope.user.$loaded().then(function () {

                // Get my class from scope and use it to get courses
                $scope.myClass = $scope.user.myClass;
                var klass = $scope.myClass;
                $scope.myCourses = $firebaseObject(ref.child('coursesByClass/' + klass));
                $scope.myGrades = $firebaseArray(ref.child('users').child('students/' + userId).child('grades').child('courses'));

                // Loop through all grades under personal node and push to globalGrades
                ref.child('grades').child(userId).child('final').once('value', function (snapshot) {
                    snapshot.forEach(function (childSnapshot) {
                        var childKey = childSnapshot.key;
                        var childData = childSnapshot.val();
                        $scope.globalGrades.push({
                            key: childKey,
                            grade: childData
                        })
                    });
                });

                // Loop through all assignments under personal node and push to globalAssignments
                ref.child('grades').child(userId).child('assignments').once('value', function (snapshot) {
                    snapshot.forEach(function (childSnapshot) {
                        var childKey
                        var childData
                        var courseKey = childSnapshot.key;

                        ref.child('grades').child(userId).child('assignments').child(courseKey).once('value', function (snapshot) {
                            snapshot.forEach(function (childSnapshot) {
                                childKey = childSnapshot.key;
                                childData = childSnapshot.val();

                                // For each child under each 
                                $scope.globalAssignments.push({
                                    key: courseKey,
                                    childKey: childKey,
                                    grade: childData
                                })
                            })
                        });
                    });
                });






            })
        })
    }
]);