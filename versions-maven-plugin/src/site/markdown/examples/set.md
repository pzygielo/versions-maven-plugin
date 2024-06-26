title: Changing the project version
author: Stephen Connolly
date: 2009-05-25

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

# Changing the project version

The `set` goal can be used to update the version of the current module.  It will automatically climb up local
directories to find the aggregation root.  It will automatically update explicitly referenced dependencies.
You specify the version to update to via the newVersion property, for example

```sh
mvn versions:set -DnewVersion=1.0.3-SNAPSHOT
```

Here is a real-life example:

```sh
svn co http://svn.apache.org/repos/asf/maven/enforcer/trunk/@778424 enforcer
```

```log
A    enforcer/maven-enforcer-plugin
[...]
A    enforcer/enforcer-rules/pom.xml
 U   enforcer
Checked out revision 778424.
```

```sh
mvn versions:set -DnewVersion=2.0.0-SNAPSHOT -f enforcer/pom.xml
```

```log
[INFO] Scanning for projects...
[INFO] Reactor build order:
[INFO]   Enforcer
[INFO]   Enforcer API
[INFO]   Enforcer Rules
[INFO]   Maven Enforcer Plugin
[INFO] ------------------------------------------------------------------------
[INFO] Building Enforcer
[INFO]    task-segment: [org.codehaus.mojo:versions-maven-plugin:1.0-beta-1:set] (aggregator-style)
[INFO] ------------------------------------------------------------------------
[INFO] [versions:set]
[INFO] Searching for local aggregator root...
[INFO] Local aggregation root: ./enforcer
[INFO] Processing org.apache.maven.enforcer:enforcer
[INFO]     Updating project org.apache.maven.enforcer:enforcer
[INFO]         from version 1.0-beta-2-SNAPSHOT to 2.0.0-SNAPSHOT
[INFO]
[INFO] Processing org.apache.maven.enforcer:enforcer-api
[INFO]     Updating parent org.apache.maven.enforcer:enforcer
[INFO]         from version 1.0-beta-2-SNAPSHOT to 2.0.0-SNAPSHOT
[INFO]     Updating project org.apache.maven.enforcer:enforcer-api
[INFO]         from version 1.0-beta-2-SNAPSHOT to 2.0.0-SNAPSHOT
[INFO]
[INFO] Processing org.apache.maven.enforcer:enforcer-rules
[INFO]     Updating parent org.apache.maven.enforcer:enforcer
[INFO]         from version 1.0-beta-2-SNAPSHOT to 2.0.0-SNAPSHOT
[INFO]     Updating project org.apache.maven.enforcer:enforcer-rules
[INFO]         from version 1.0-beta-2-SNAPSHOT to 2.0.0-SNAPSHOT
[INFO]
[INFO] Processing org.apache.maven.plugins:maven-enforcer-plugin
[INFO]     Updating parent org.apache.maven.enforcer:enforcer
[INFO]         from version 1.0-beta-2-SNAPSHOT to 2.0.0-SNAPSHOT
[INFO]     Updating project org.apache.maven.plugins:maven-enforcer-plugin
[INFO]         from version 1.0-beta-2-SNAPSHOT to 2.0.0-SNAPSHOT
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESSFUL
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 3 seconds
[INFO] Finished at: Mon May 25 15:30:00 IST 2009
[INFO] Final Memory: 22M/355M
[INFO] ------------------------------------------------------------------------
```

To illustrate how dependencies version updating and aggregator root detection works reactor, we need to edit the
dependencyManagement section of the root `pom.xml` to reference the version explicitly, as opposed to
via a property.

Change:

```xml
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>org.apache.maven.enforcer</groupId>
      <artifactId>enforcer-api</artifactId>
      <version>${project.version}</version>
    </dependency>
    <dependency>
      <groupId>org.apache.maven.enforcer</groupId>
      <artifactId>enforcer-rules</artifactId>
      <version>${project.version}</version>
    </dependency>
    <dependency>
      <groupId>org.apache.maven.enforcer</groupId>
      <artifactId>enforcer-rules</artifactId>
      <version>${project.version}</version>
      <type>test-jar</type>
      <scope>test</scope>
    </dependency>
```

To

```xml
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>org.apache.maven.enforcer</groupId>
      <artifactId>enforcer-api</artifactId>
      <version>2.0.0-SNAPSHOT</version>
    </dependency>
    <dependency>
      <groupId>org.apache.maven.enforcer</groupId>
      <artifactId>enforcer-rules</artifactId>
      <version>2.0.0-SNAPSHOT</version>
    </dependency>
    <dependency>
      <groupId>org.apache.maven.enforcer</groupId>
      <artifactId>enforcer-rules</artifactId>
      <version>2.0.0-SNAPSHOT</version>
      <type>test-jar</type>
      <scope>test</scope>
    </dependency>
```

Now if we run

```sh
mvn versions:set -DnewVersion=2.1.0-SNAPSHOT -f enforcer/enforcer-api/pom.xml
```

```log
[INFO] Scanning for projects...
[INFO] ------------------------------------------------------------------------
[INFO] Building Enforcer API
[INFO]    task-segment: [org.codehaus.mojo:versions-maven-plugin:1.0-beta-1:set] (aggregator-style)
[INFO] ------------------------------------------------------------------------
[INFO] [versions:set]
[INFO] Searching for local aggregator root...
[INFO] Local aggregation root: /home/connollys/src/enforcer/../enforcer
[INFO] Processing org.apache.maven.enforcer:enforcer-api
[INFO]     Updating project org.apache.maven.enforcer:enforcer-api
[INFO]         from version 2.0.0-SNAPSHOT to 2.1.0-SNAPSHOT
[INFO]
[INFO] Processing org.apache.maven.enforcer:enforcer
[INFO]     Updating dependency org.apache.maven.enforcer:enforcer-api
[INFO]         from version 2.0.0-SNAPSHOT to 2.1.0-SNAPSHOT
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESSFUL
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 3 seconds
[INFO] Finished at: Mon May 25 15:41:54 IST 2009
[INFO] Final Memory: 22M/350M
[INFO] ------------------------------------------------------------------------
```

In this case it has updated the version of enforcer-api, it has found that enforcer-api is part of an aggregator
build in the local filesystem, and it has updated all references to enforcer-api in the aggregator build to
reference the new version.

## Widening the scope of the change

You can increase or change the scope of the change by specifying the `groupId` or `artifactId` or
`oldVersion` on the command line directly. These can also support wildcards to help match multiple modules
which will become origins of change.

```sh
mvn versions:set -DgroupId=org.apache.maven.* -DartifactId=* -DoldVersion=2.* -DnewVersion=2.1.0-SNAPSHOT
```

