title: Fixing a multi-module build
author: Stephen Connolly
date: 2008-09-25

<!---
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at
https://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
-->

# Fixing a multi-module build

If you have a multi-module build where the aggregator pom (i.e. the one with packaging of `pom` and the
`modules` section) is also the parent referenced by its child modules, and the aggregator version does not
match the version specified in the parent section of the child modules, Maven will not let you build the project.

The `update-child-modules` is designed to try and help you fix these kind of build failures.

You know you have this kind of problem when you see output from Maven like this:

```log
[INFO] Scanning for projects...
[INFO] ------------------------------------------------------------------------
[ERROR] FATAL ERROR
[INFO] ------------------------------------------------------------------------
[INFO] Error building POM (may not be this project's POM).


Project ID: com.foo.bar:bar-child:jar:null

Reason: Cannot find parent: com.foo.bar:bar for project: com.foo.bar:bar-child:jar:null
```

The above example was for a project structured like this:

- /pom.xml
  - /bar-child/pom.xml

With `/pom.xml` looking like

```xml
<project>
  <modelVersion>4.0.0</modelVersion>

  <groupId>com.foo.bar</groupId>
  <artifactId>bar</artifactId>
  <version>2.0</version>
  <packaging>pom</packaging>

  <modules>
    <module>bar-child</module>
  </modules>

  ...

</project>
```

And with `/bar-child/pom.xml` referencing a previous version of the parent:

```xml
<project>
  <modelVersion>4.0.0</modelVersion>

  <parent>
    <groupId>com.foo.bar</groupId>
    <artifactId>bar</artifactId>
    <version>1.0</version>
  </parent>

  <artifactId>bar-child</artifactId>

  ...

</project>
```

In order to fix the project, you need to either update the version of the child-module, or roll-back the version of
the parent module. When there is only one child module, this is easiest to do by hand. When there are many modules,
some referencing the new version and some referencing old versions of the parent, this is easiest to do using the
[versions:update-child-modules](../update-child-modules-mojo.html) goal.

Note: it is essential in this situation to run Maven with the `-N` option, as otherwise Maven will be unable
to run the goal.

```shell
mvn -N versions:update-child-modules
```

Which produces the following output:

```log
[INFO] Scanning for projects...
[INFO] ------------------------------------------------------------------------
[INFO] Building update-child-modules
[INFO]    task-segment: [versions:update-child-modules] (aggregator-style)
[INFO] ------------------------------------------------------------------------
[INFO] [versions:update-child-modules]
[INFO] Module: bar-child
[INFO]   Parent was com.foo.bar:bar:1.0, now com.foo.bar:bar:2.0
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESSFUL
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 3 seconds
[INFO] Finished at: Thu Nov 20 15:37:40 GMT 2008
[INFO] Final Memory: 12M/288M
[INFO] ------------------------------------------------------------------------
```

If you have several layers of projects, and the versions are really out of whack, you may have to change directory
into the child aggregator projects and re-run the [versions:update-child-modules](../update-child-modules-mojo.html)
goal in order to finish fixing the project.
