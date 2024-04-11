`Incomplete List` when retrieved data from secondary
----------------------

To reproduce, you need a replicaset with at least two secondary nodes.

```
sbt "run mongodb://primary:27017,secondary0:27017,secondary1:27017/dbname"
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
