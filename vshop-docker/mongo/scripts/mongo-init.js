console.log("Creating mongo users for vshop");
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
dbVshop = db.getSiblingDB('vshop')
dbVshop.createCollection("products");
