title: Locking snapshot dependencies
author: Stephen Connolly, Paul Gier
date: 2009-04-20

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

# Locking snapshot dependencies

If your pom contains a lot of -SNAPSHOT dependencies and those -SNAPSHOT dependencies are a moving target, it
can sometimes be helpful to temporarily replace the -SNAPSHOT with a locked -YYYYMMDD.HHMMSS-NNN snapshot.
In the long term, you will need to return to the -SNAPSHOT dependencies and then replace them with their
release version, but if you need a short term semi-reproducible build, locked -SNAPSHOTs can sometimes be a
useful hack.

A pom will most likely specify -SNAPSHOT versions for certain dependencies.

```xml
<dependencies>

<dependency>
  <groupId>org.codehaus.cargo</groupId>
  <artifactId>cargo-core-api</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>

</dependencies>
```

Using the `lock-snapshots` goal, the version can be locked down to the specific timestamped snapshot version used
in the build.

```sh
mvn versions:lock-snapshots
```

The pom dependencies are modified to look like the following.

```xml
<dependencies>

<dependency>
  <groupId>org.codehaus.cargo</groupId>
  <artifactId>cargo-core-api</artifactId>
  <version>1.0-20081117.213112-16</version>
</dependency>

</dependencies>
```

You can restrict which dependencies should have their -SNAPSHOT versions locked down.  For example,
the following will only match dependencies that match the groupId "org.codehaus.plexus" and artifactId
"plexus-utils"

```sh
mvn versions:lock-snapshots -Dincludes=org.codehaus.plexus:plexus-utils
```

The `includes` and `excludes` parameters follow the format `groupId:artifactId:type:classifier`.
Use a comma separated list to specify multiple includes.  Wildcards (*) can also be used to match
multiple values.

This example will match anything with the groupId "org.codehaus.plexus" and anything with the groupId and
artifactId matching "junit".

```sh
mvn versions:lock-snapshots -Dincludes=org.codehaus.plexus:*,junit:junit
```

By default, both the `project/dependencyManagment` and `project/dependencies` sections will be processed.
You can use the `processDependencies` and `processDependencyManagement` parameters to control which sections
are processed.

This example will only process the `project/dependencyManagment` section of your pom:

```sh
mvn versions:lock-snapshots -DprocessDependencies=false
```

While this example will only process the `project/dependencies` section of your pom:

```sh
mvn versions:lock-snapshots -DprocessDependencyManagement=false
```

