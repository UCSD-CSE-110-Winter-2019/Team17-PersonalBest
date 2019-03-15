const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

exports.addTimeStamp = functions.firestore
   .document('users/{userEmail}/chats/{friendEmail}/messages/{messageId}')
   .onCreate((snap, context) => {
     if (snap) {
       return snap.ref.update({
                   timestamp: admin.firestore.FieldValue.serverTimestamp()
               });
     }

     return "snap was null or empty";
   });


// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });
