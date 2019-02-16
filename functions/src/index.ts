import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';

class Person {
  name!: string;
  phoneNumber!: string;

  static builder() {
    return new Person();
  }

  setName(name: string) {
    this.name = name
    return this
  }

  setPhoneNumber(phoneNumber: string) {
    this.phoneNumber = phoneNumber;
    return this;
  }
}

const personRepository: string = "People";

// // Start writing Firebase Functions
// // https://firebase.google.com/docs/functions/typescript

export const createuser = functions.auth.user().onCreate((user) => {

  admin.database()
    .ref(personRepository)
    .child(user.uid)
    .set(Person
      .builder()
      .setName("request.displayName")
      .setPhoneNumber("request.phoneNumber"))
    .then(success => {
      console.log("enviado");
    })
    .catch(error => {
      console.log("Grande Erro");
    });
});



