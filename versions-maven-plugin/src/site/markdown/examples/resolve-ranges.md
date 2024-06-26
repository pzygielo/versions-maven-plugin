title: Resolve Ranges
author: Paul Gier, Stephen Connolly
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

# Resolve ranges

A pom may specify version ranges for certain dependencies.

```xml
<dependencies>

  <dependency>
    <groupId>org.apache.maven</groupId>
    <artifactId>maven-project</artifactId>
    <version>[2.0.7, 2.0.9)</version>
  </dependency>
  
  <dependency>
    <groupId>org.codehaus.plexus</groupId>
    <artifactId>plexus-utils</artifactId>
    <version>[1.5.0, 1.5.1 ]</version>
  </dependency>
  
  <dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>[3.0, 3.8.2)</version>
  </dependency>
  
</dependencies>
```

Using the resolve-ranges goal, the version ranges can be set to the specific version
used in the build.

```sh
mvn versions:resolve-ranges
```

The pom dependencies are modified to look like the following.

```xml
<dependencies>

  <dependency>
    <groupId>org.apache.maven</groupId>
    <artifactId>maven-project</artifactId>
    <version>2.0.8</version>
  </dependency>
  
  <dependency>
    <groupId>org.codehaus.plexus</groupId>
    <artifactId>plexus-utils</artifactId>
    <version>1.5.1</version>
  </dependency>
  
  <dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>3.8.1</version>
  </dependency>
  
</dependencies>
```

You can restrict which dependencies should have their ranges collapsed.  For example,
the following will only match dependencies that match the groupId "org.codehaus.plexus" and artifactId
"plexus-utils"

```sh
mvn versions:resolve-ranges -Dincludes=org.codehaus.plexus:plexus-utils
```

The `includes` and `excludes` parameters follow the format `groupId:artifactId:type:classifier`.
Use a comma separated separated list to specify multiple includes.  Wildcards (*) can also be used to match
multiple values.

This example will match anything with the groupId "org.codehaus.plexus" and anything with the groupId and
artifactId matching "junit".

```sh
mvn versions:resolve-ranges -Dincludes=org.codehaus.plexus:*,junit:junit
```

By default, both the `project/dependencyManagment` and `project/dependencies` sections will be processed.
You can use the `processDependencies` and `processDependencyManagement` parameters to control which sections
are processed.

This example will only process the `project/dependencyManagment` section of your pom:

```sh
mvn versions:resolve-ranges -DprocessDependencies=false
```

While this example will only process the `project/dependencies` section of your pom:

```sh
mvn versions:resolve-ranges -DprocessDependencyManagement=false
```

