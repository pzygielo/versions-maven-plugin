title: Comparing dependency versions
author: Paul Gier
date: 2009-01-09

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

# Compare dependency versions

The `compare-dependencies` goal can be used to compare the dependencies in the current project to
the dependencyManagement of a remote project.  This is useful, for example, for finding differences
between the current project dependencies and an organizational bill of materials (BOM) pom.
You specify the remote groupId, artifactId, and the version to which the current project
should be compared, for example

```sh
mvn versions:compare-dependencies -DremotePom=org.foo:bom-pom:1.0
```

By default the list of dependency differences will be output to the command line.  This can be
written to a file using the "reportOutputFile" parameter.

```sh
mvn versions:compare-dependencies -DremotePom=org.foo:bom-pom:1.0 -DreportOutputFile=${project.build.directory}/depDiffs.txt
```

The plugin can also automatically update the dependencies of the current project to match
the remote project using the "updateDependencies" parameter.  By default this is turned off.

```sh
mvn versions:compare-dependencies -DremotePom=org.foo:bom-pom:1.0 -DupdateDependencies=true
```

In addition to that, any property defined in the current project that represent the version
of one of several artifacts can be updated to match the remote project using the "updatePropertyVersions"
parameter. By default this is turned off.

```sh
mvn versions:compare-dependencies -DremotePom=org.foo:bom-pom:1.0 -updatePropertyVersions=true
```

Such update will only occur if the dependencies linked to that version exist in the remote project
and all these dependencies have a single version. In any other case, the property is not updated.
