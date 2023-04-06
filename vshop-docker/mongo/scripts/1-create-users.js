console.log("Creating users and collections");
db.createUser(
    {
        user: "vshop",
        pwd: "vshop",
        roles: [
            {
                role: "readWrite",
                db: "vshop"
            },
            {
                role: "dbOwner",
                db: "vshop"
            }
        ]
    }
);
db.getSiblingDB('vshop').createCollection("products");
