# mongo --eval "db.auth('$MONGO_INITDB_ROOT_USERNAME', '$MONGO_INITDB_ROOT_PASSWORD'); db = db.getSiblingDB('$DB_NAME'); db.createUser({ user: '$DB_USER', pwd: '$DB_PASSWORD', roles: [{ role: 'readWrite', db: '$DB_NAME' }] });"
set -e

mongo <<EOF
db = db.getSiblingDB('$DB_NAME')

db.createUser({
  user: '$DB_USER',
  pwd: '$DB_PASSWORD',
  roles: [{ role: 'readWrite', db: '$DB_NAME' }],
});
db.createCollection('jobs')

EOF