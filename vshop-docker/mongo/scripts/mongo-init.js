db.createUser(
    {
        user: "vshop",
        pwd: "vshop",
        roles: [
            {
                role: "readWrite",
                db: "vshop"
            }
        ]
    }
);
rs.initiate();