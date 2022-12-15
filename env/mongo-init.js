print('Start creating Database and Users');

db = db.getSiblingDB('executordb');

db.createUser({
    user: 'mongoadmin',
    pwd: 'mongopasswd',
    roles: [{role: "readWrite", db: 'executordb'}]
});

db.createCollection('jobs');

print('Finish creating Database and Users');