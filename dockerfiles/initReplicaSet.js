// Initialize a MongoDB replica set with authentication
const conn = new Mongo("mongodb1:27017");
const adminDB = conn.getDB("admin");
adminDB.auth("admin", "adminpassword");

adminDB.runCommand({
  replSetInitiate: {
    _id: "replica_set_single",
    version: 1,
    members: [
      { _id: 0, host : "mongodb1:27017" },
      { _id: 1, host : "mongodb2:27018" },
      { _id: 2, host : "mongodb3:27019" }
    ]
  }
});
