title: Checking for new plugin updates
author: Stephen Connolly
date: 2008-09-02

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

# Checking for new plugin updates

The `display-plugin-updates` goal will check all the plugins and reports used in your project and display a list
of those plugins with newer versions available, first staying with the same Maven version prerequisite, then
additional options if you update the Maven version prerequisite.

Here are some examples of what this looks like:

```sh
svn checkout http://svn.codehaus.org/mojo/trunk/mojo/build-helper-maven-plugin build-helper-maven-plugin
cd build-helper-maven-plugin
mvn versions:display-plugin-updates
```

Which produces the following output:

```log
[INFO] ------------------------------------------------------------------------
[INFO] Building Build Helper Maven Plugin
[INFO]    task-segment: [versions:display-plugin-updates]
[INFO] ------------------------------------------------------------------------
[INFO] [versions:display-plugin-updates]
[INFO]
[INFO] The following plugin updates are available:
[INFO]   maven-checkstyle-plugin .................................. 2.6 -> 2.8
[INFO]   maven-deploy-plugin ...................................... 2.5 -> 2.7
[INFO]   maven-jar-plugin ..................................... 2.3.1 -> 2.3.2
[INFO]   maven-plugin-plugin ...................................... 2.7 -> 2.9
[INFO]   maven-resources-plugin ................................. 2.4.3 -> 2.5
[INFO]   maven-site-plugin ..................... ${sitePluginVersion} -> 2.0.1
[INFO]
[INFO] All plugins have a version specified.
[INFO]
[INFO] Project defines minimum Maven version as: 2.0.6
[INFO] Plugins require minimum Maven version of: 2.2.1
[INFO]
[WARNING] Project (which is a Maven plugin) targets Maven 2.0.6 or newer
[WARNING] but requires Maven 2.2.1 or newer to build.
[WARNING] This may or may not be a problem. A Maven Enforcer rule can help
[WARNING] enforce that the correct version of Maven is used to build this plugin.
[INFO]
[INFO] Require Maven 2.0.9 to use the following plugin updates:
[INFO]   maven-compiler-plugin ......................................... 2.3.2
[INFO]   maven-surefire-plugin .......................................... 2.10
[INFO]
[INFO] Require Maven 2.1.0 to use the following plugin updates:
[INFO]   maven-site-plugin ..................... ${sitePluginVersion} -> 2.1.1
[INFO]
[INFO] Require Maven 2.2.0 to use the following plugin updates:
[INFO]   maven-site-plugin ....................... ${sitePluginVersion} -> 3.0
[INFO]
[INFO] Require Maven 2.2.1 to use the following plugin updates:
[INFO]   maven-changes-plugin ............................................ 2.5
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESSFUL
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 6 seconds
[INFO] Finished at: Wed Nov 30 21:59:36 GMT 2011
[INFO] Final Memory: 21M/81M
[INFO] ------------------------------------------------------------------------
```

The plugin will also warn if you have not specified the versions of any plugins that you are using and tell you what
version you are currently using.  Best practice in Maven is to always specify the plugin versions in order to
ensure that builds are reproducible.

For example the following `pom.xml`:

```xml
<project>
  <modelVersion>4.0.0</modelVersion>

  <groupId>org.codehaus.mojo.versions-maven-plugin.it</groupId>
  <artifactId>parent</artifactId>
  <version>2.0</version>
  <packaging>pom</packaging>

  <build>
    <pluginManagement>
      <plugins>
        <plugin>
          <artifactId>maven-checkstyle-plugin</artifactId>
          <version>2.1</version>
        </plugin>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-javadoc-plugin</artifactId>
        </plugin>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-compiler-plugin</artifactId>
          <configuration>
            <source>1.4</source>
            <target>1.4</target>
          </configuration>
        </plugin>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>build-helper-maven-plugin</artifactId>
      </plugin>
      </plugins>
    </pluginManagement>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-clean-plugin</artifactId>
        <version>2.1</version>
      </plugin>
    </plugins>
  </build>
</project>
```

produces the following output (when run using Maven 2.0.6):

```log
[INFO] ----------------------------------------------------------------------------
[INFO] Building Unnamed - org.codehaus.mojo.versions-maven-plugin.it:parent:pom:2.0
[INFO]    task-segment: [versions:display-plugin-updates]
[INFO] ----------------------------------------------------------------------------
[INFO] [versions:display-plugin-updates]
[INFO]
[INFO] The following plugin updates are available:
[INFO]   maven-clean-plugin ....................................... 2.1 -> 2.2
[INFO]
[WARNING] The following plugins do not have their version specified:
[WARNING]   maven-compiler-plugin ...................................... 2.0.2
[WARNING]   maven-deploy-plugin .......................................... 2.4
[WARNING]   maven-install-plugin ......................................... 2.2
[WARNING]   maven-javadoc-plugin ......................................... 2.0
[WARNING]   maven-site-plugin ..................................... 2.0-beta-4
[WARNING]   org.codehaus.mojo:build-helper-maven-plugin .................. 1.0
[INFO]
[WARNING] Project does not define minimum Maven version, default is: 2.0
[INFO] Plugins require minimum Maven version of: 2.0.6
[INFO]
[ERROR] Project does not define required minimum version of Maven.
[ERROR] Update the pom.xml to contain
[ERROR]     <prerequisites>
[ERROR]       <maven>2.0.6</maven>
[ERROR]     </prerequisites>
[INFO]
[INFO] Require Maven 2.0.2 to use the following plugin updates:
[INFO]   maven-checkstyle-plugin ......................................... 2.1
[INFO]   maven-javadoc-plugin ..................................... 2.0 -> 2.2
[INFO]   maven-site-plugin .......................... 2.0-beta-4 -> 2.0-beta-7
[INFO]   org.codehaus.mojo:build-helper-maven-plugin .............. 1.0 -> 1.5
[INFO]
[INFO] Require Maven 2.0.6 to use the following plugin updates:
[INFO]   maven-checkstyle-plugin .................................. 2.1 -> 2.8
[INFO]   maven-clean-plugin ..................................... 2.1 -> 2.4.1
[INFO]   maven-deploy-plugin ...................................... 2.4 -> 2.7
[INFO]   maven-install-plugin ................................... 2.2 -> 2.3.1
[INFO]   maven-javadoc-plugin ..................................... 2.0 -> 2.3
[INFO]   maven-site-plugin ............................... 2.0-beta-4 -> 2.0.1
[INFO]   org.codehaus.mojo:build-helper-maven-plugin .............. 1.0 -> 1.7
[INFO]   org.codehaus.mojo:versions-maven-plugin ......................... 1.2
[INFO]
[INFO] Require Maven 2.0.8 to use the following plugin updates:
[INFO]   maven-javadoc-plugin ..................................... 2.0 -> 2.4
[INFO]
[INFO] Require Maven 2.0.9 to use the following plugin updates:
[INFO]   maven-compiler-plugin ......................................... 2.3.2
[INFO]   maven-javadoc-plugin ..................................... 2.0 -> 2.8
[INFO]
[INFO] Require Maven 2.1.0 to use the following plugin updates:
[INFO]   maven-site-plugin ............................... 2.0-beta-4 -> 2.1.1
[INFO]
[INFO] Require Maven 2.2.0 to use the following plugin updates:
[INFO]   maven-site-plugin ................................. 2.0-beta-4 -> 3.0
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESSFUL
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 3 seconds
[INFO] Finished at: Wed Nov 30 22:02:14 GMT 2011
[INFO] Final Memory: 26M/81M
[INFO] ------------------------------------------------------------------------
```

Note: that because the POM does not specify versions for some plugins, Maven
defaults to using the latest compatible version (Maven 3 complains about the
version not being specified), and hence the reported minimum required Maven
version tends to be the same as the version of Maven that is running the mojo

When run with Maven 2.0.9 (which defines versions of core plugins in the
super-pom, and removes some plugin from the lifecycle for the pom lifecycle),
we get the following output:

```log
[INFO] ------------------------------------------------------------------------
[INFO] Building Unnamed - org.codehaus.mojo.versions-maven-plugin.it:parent:pom:2.0
[INFO]    task-segment: [versions:display-plugin-updates]
[INFO] ------------------------------------------------------------------------
[INFO] [versions:display-plugin-updates]
[INFO]
[INFO] All plugins with a version specified are using the latest versions.
[INFO]
[WARNING] The following plugins do not have their version specified:
[WARNING]   maven-deploy-plugin ......................... (from super-pom) 2.4
[WARNING]   maven-install-plugin ........................ (from super-pom) 2.2
[WARNING]   maven-site-plugin .................... (from super-pom) 2.0-beta-4
[WARNING]   org.codehaus.mojo:build-helper-maven-plugin .................. 1.0
[INFO]
[WARNING] Project does not define minimum Maven version, default is: 2.0
[INFO] Plugins require minimum Maven version of: 2.0.6
[INFO]
[ERROR] Project does not define required minimum version of Maven.
[ERROR] Update the pom.xml to contain
[ERROR]     <prerequisites>
[ERROR]       <maven>2.0.6</maven>
[ERROR]     </prerequisites>
[INFO]
[INFO] Require Maven 2.0.2 to use the following plugin updates:
[INFO]   maven-checkstyle-plugin ......................................... 2.1
[INFO]   maven-site-plugin .......................... 2.0-beta-4 -> 2.0-beta-7
[INFO]   org.codehaus.mojo:build-helper-maven-plugin .............. 1.0 -> 1.5
[INFO]
[INFO] Require Maven 2.0.6 to use the following plugin updates:
[INFO]   maven-checkstyle-plugin .................................. 2.1 -> 2.8
[INFO]   maven-clean-plugin ..................................... 2.3 -> 2.4.1
[INFO]   maven-deploy-plugin ...................................... 2.4 -> 2.7
[INFO]   maven-install-plugin ................................... 2.2 -> 2.3.1
[INFO]   maven-site-plugin ............................... 2.0-beta-4 -> 2.0.1
[INFO]   org.codehaus.mojo:build-helper-maven-plugin .............. 1.0 -> 1.7
[INFO]   org.codehaus.mojo:versions-maven-plugin ......................... 1.2
[INFO]
[INFO] Require Maven 2.1.0 to use the following plugin updates:
[INFO]   maven-site-plugin ............................... 2.0-beta-4 -> 2.1.1
[INFO]
[INFO] Require Maven 2.2.0 to use the following plugin updates:
[INFO]   maven-site-plugin ................................. 2.0-beta-4 -> 3.0
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESSFUL
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 5 seconds
[INFO] Finished at: Wed Nov 30 22:09:59 GMT 2011
[INFO] Final Memory: 20M/81M
[INFO] ------------------------------------------------------------------------
```

And the following POM:

```xml
<project>
  <modelVersion>4.0.0</modelVersion>

  <parent>
    <groupId>org.codehaus.mojo.versions-maven-plugin.it</groupId>
    <artifactId>parent</artifactId>
    <version>2.0</version>
    <relativePath>../parent-2.0/pom.xml</relativePath>
  </parent>

  <artifactId>child</artifactId>
  <packaging>jar</packaging>

  <prerequisites>
    <maven>2.0.2</maven>
  </prerequisites>

  <build>
    <pluginManagement>
      <plugins>
        <plugin>
          <artifactId>maven-checkstyle-plugin</artifactId>
          <version>2.0</version>
        </plugin>
      </plugins>
    </pluginManagement>
    <plugins>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>build-helper-maven-plugin</artifactId>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-clean-plugin</artifactId>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-javadoc-plugin</artifactId>
      </plugin>
    </plugins>
  </build>

</project>
```

produces the following output:

```log
[INFO] ------------------------------------------------------------------------
[INFO] Building Unnamed - org.codehaus.mojo.versions-maven-plugin.it:child:pom:2.0
[INFO]    task-segment: [versions:display-plugin-updates]
[INFO] ------------------------------------------------------------------------
[INFO] [versions:display-plugin-updates]
[INFO]
[INFO] The following plugin updates are available:
[INFO]   maven-checkstyle-plugin .................................. 2.0 -> 2.8
[INFO]   maven-clean-plugin ..................................... 2.1 -> 2.4.1
[INFO]
[WARNING] The following plugins do not have their version specified:
[WARNING]   org.codehaus.mojo:build-helper-maven-plugin .................. 1.7
[INFO]
[INFO] Project defines minimum Maven version as: 2.0.8
[INFO] Plugins require minimum Maven version of: 2.0.8
[INFO]
[INFO] No plugins require a newer version of Maven than specified by the pom.
[INFO]
[INFO] Require Maven 2.0.9 to use the following plugin updates:
[INFO]   maven-javadoc-plugin .................................... 2.7 ->  2.8
[INFO]
[INFO] Require Maven 2.1.0 to use the following plugin updates:
[INFO]   maven-site-plugin ...................................... 2.0 -> 2.1.1
[INFO]
[INFO] Require Maven 2.2.0 to use the following plugin updates:
[INFO]   maven-site-plugin ........................................ 2.0 -> 3.0
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESSFUL
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 4 seconds
[INFO] Finished at: Fri Nov 25 18:28:34 GMT 2011
[INFO] Final Memory: 17M/81M
[INFO] ------------------------------------------------------------------------
```

