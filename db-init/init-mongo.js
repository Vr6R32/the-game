db.createUser({
    user: "mongo",
    pwd: "dontgotosql",
    roles: [
        {
            role: "readWrite",
            db: "user_session"
        },
        {
            role: "readWrite",
            db: "admin"
        }
    ]
});

db = db.getSiblingDB('user_session');

db.userSession.insertMany([
    { "username": "michal", "userId": 4, "status": "OFFLINE", "logoutTime": new Date("2024-04-25T15:51:46.897Z") },
    { "username": "tester", "userId": 1, "status": "OFFLINE", "logoutTime": new Date("2024-04-25T19:51:46.897Z") },
    { "username": "thebossguy", "userId": 2, "status": "OFFLINE", "logoutTime": new Date("2024-04-25T20:51:46.897Z") },
    { "username": "likedone", "userId": 3, "status": "OFFLINE", "logoutTime": new Date("2024-04-23T18:42:02.422Z") },
    { "username": "gymbro", "userId": 5, "status": "OFFLINE", "logoutTime": new Date("2024-04-26T17:42:08.793Z") },
    { "username": "annoying", "userId": 6, "status": "OFFLINE", "logoutTime": new Date("2024-04-26T18:14:19.921Z") },
    { "username": "hrlady", "userId": 7, "status": "OFFLINE", "logoutTime": new Date("2024-04-26T18:01:26.427Z") },
    { "username": "elon", "userId": 8, "status": "OFFLINE", "logoutTime": new Date("2024-04-26T17:35:21.494Z") }
]);