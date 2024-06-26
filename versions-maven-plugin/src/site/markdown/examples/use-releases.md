title: Replacing -SNAPSHOT versions with their corresponding releases
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

# Replacing -SNAPSHOT versions with their corresponding releases

If you need a new feature that is being developed in a dependency of your project, and that feature has not been
released yet, you may decide to add a dependency on the -SNAPSHOT version of the next release:

```xml
<dependencies>

  <dependency>
    <groupId>org.codehaus.cargo</groupId>
    <artifactId>cargo-core-api</artifactId>
    <version>1.0-alpha-7-SNAPSHOT</version>
  </dependency>

</dependencies>
```

At some stage, this -SNAPSHOT version will be released. The `use-releases` goal, will look at all your project
dependencies and see what -SNAPSHOT versions have been released.  If any of your projects -SNAPSHOT versions have
a corresponding release version, then it will replace the -SNAPSHOT with its corresponding release version.

```shell
mvn versions:use-releases
```

When org.codehaus.cargo:cargo-core-api releases the 1.0-alpha-7 version this would update the pom to look like:

```xml
<dependencies>

  <dependency>
    <groupId>org.codehaus.cargo</groupId>
    <artifactId>cargo-core-api</artifactId>
    <version>1.0-alpha-7</version>
  </dependency>
   
</dependencies>
```

You can restrict which dependencies should be processed.  For example,
the following will only match dependencies that match the groupId "org.codehaus.plexus" and artifactId
"plexus-utils"

```shell
mvn versions:use-releases -Dincludes=org.codehaus.plexus:plexus-utils
```

The `includes` and `excludes` parameters follow the format `groupId:artifactId:type:classifier`.
Use a comma separated separated list to specify multiple includes.  Wildcards (*) can also be used to match
multiple values.

This example will match anything with the groupId "org.codehaus.plexus" and anything with the groupId and
artifactId matching "junit".

```shell
mvn versions:use-releases -Dincludes=org.codehaus.plexus:*,junit:junit
```

By default, both the `project/dependencyManagment` and `project/dependencies` sections will be processed.
You can use the `processDependencies` and `processDependencyManagement` parameters to control which sections
are processed.

This example will only process the `project/dependencyManagment` section of your pom:

```shell
mvn versions:use-releases -DprocessDependencies=false
```

While this example will only process the `project/dependencies` section of your pom:

```shell
mvn versions:use-releases -DprocessDependencyManagement=false
```

