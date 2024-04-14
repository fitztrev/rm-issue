`Incomplete List` when retrieved data from secondary
----------------------

## To reproduce:

```bash
git clone https://github.com/fitztrev/rm-issue.git
cd rm-issue


docker compose up -d

docker compose exec sbt sbt "run mongodb://primary:27017,secondary0:27017,secondary1:27017/dbname"
```

The test will create a dbname.rmbug2 collection, empty it, and insert 2000 documents.

Then it will list the documents from mongodb to stdout with different ReadPreference:
`secondaryPreferred` and `primaryPreferred`.

Expected: should print 2000 documents.
Issue: only print 101 or 202 documents.

Output:
```
|total document inserted: 2000
|  total result from Secondary Preferred : 101
|  total result from Primary Preferred   : 2000
```

## To verify the PR fixes the issue:

```bash
# in the same `rm-issue` directory
git clone -b fix/1185 https://github.com/tardigradeus/ReactiveMongo.git
cd ReactiveMongo
git tag 1.1.0-RC12-SNAPSHOT


docker compose exec -w /app/ReactiveMongo sbt sbt +publishLocal
```

Update `build.sbt`:

```diff
-  "org.reactivemongo" %% "reactivemongo" % "1.1.0-RC12",
+  "org.reactivemongo" %% "reactivemongo" % "1.1.0-RC12-SNAPSHOT",
```

Re-run the test:

```bash
docker compose exec sbt sbt "run mongodb://primary:27017,secondary0:27017,secondary1:27017/dbname"
```

New expected output:

```
|total document inserted: 2000
|  total result from Secondary Preferred : 2000
|  total result from Primary Preferred   : 2000
```
