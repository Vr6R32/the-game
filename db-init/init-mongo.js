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