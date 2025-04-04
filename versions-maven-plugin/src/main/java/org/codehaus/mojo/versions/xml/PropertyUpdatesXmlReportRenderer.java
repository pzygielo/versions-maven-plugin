package org.codehaus.mojo.versions.xml;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import javax.xml.stream.XMLStreamException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.codehaus.mojo.versions.api.AbstractVersionDetails;
import org.codehaus.mojo.versions.api.ArtifactVersionsCache;
import org.codehaus.mojo.versions.api.Property;
import org.codehaus.mojo.versions.api.PropertyVersions;
import org.codehaus.mojo.versions.reporting.OverviewStats;
import org.codehaus.mojo.versions.reporting.model.PropertyAssociation;
import org.codehaus.mojo.versions.reporting.model.PropertyInfo;
import org.codehaus.mojo.versions.reporting.model.PropertyReportSummary;
import org.codehaus.mojo.versions.reporting.model.PropertyUpdatesModel;
import org.codehaus.mojo.versions.reporting.model.PropertyUpdatesReport;
import org.codehaus.mojo.versions.reporting.model.io.stax.PropertyUpdatesReportStaxWriter;
import org.codehaus.mojo.versions.reporting.util.ReportRenderer;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static org.codehaus.mojo.versions.api.Segment.INCREMENTAL;
import static org.codehaus.mojo.versions.api.Segment.MAJOR;
import static org.codehaus.mojo.versions.api.Segment.MINOR;
import static org.codehaus.mojo.versions.xml.CommonXmlReportRendererUtils.setSection;
import static org.codehaus.mojo.versions.xml.CommonXmlReportRendererUtils.statusFor;

/**
 * XML renderer for PropertyUpdatesReport creates an xml file in target directory and writes report about available
 * dependency/dependency management updates.
 */
public class PropertyUpdatesXmlReportRenderer implements ReportRenderer {
    private final PropertyUpdatesModel model;
    private final Path outputFile;
    private final ArtifactVersionsCache newestUpdateCache =
            new ArtifactVersionsCache(AbstractVersionDetails::getNewestUpdateWithinSegment);
    private final boolean allowSnapshots;

    /**
     * Creates a new instance
     * @param model object containing the updates model
     * @param outputFile output file for the report
     */
    public PropertyUpdatesXmlReportRenderer(PropertyUpdatesModel model, Path outputFile, boolean allowSnapshots) {
        this.model = model;
        this.outputFile = outputFile;
        this.allowSnapshots = allowSnapshots;
    }

    @Override
    public boolean isAllowSnapshots() {
        return allowSnapshots;
    }

    @Override
    public String getTitle() {
        return "Property updates";
    }

    /**
     * Creates an XML report
     */
    @Override
    public void render() {
        try (BufferedWriter writer = Files.newBufferedWriter(outputFile, StandardCharsets.UTF_8)) {
            new PropertyUpdatesReportStaxWriter().write(writer, new PropertyUpdatesReport() {
                {
                    setSummary(new PropertyReportSummary() {
                        {
                            OverviewStats overviewStats = OverviewStats.fromUpdates(
                                    model.getAllUpdates().values(), newestUpdateCache, isAllowSnapshots());
                            setUsingLastVersion(String.valueOf(overviewStats.getUpToDate()));
                            setNextVersionAvailable(String.valueOf(overviewStats.getAny()));
                            setNextIncrementalAvailable(String.valueOf(overviewStats.getIncremental()));
                            setNextMinorAvailable(String.valueOf(overviewStats.getMinor()));
                            setNextMajorAvailable(String.valueOf(overviewStats.getMajor()));
                        }
                    });
                    setProperties(createPropertyInfo(model.getAllUpdates(), allowSnapshots));
                }
            });
        } catch (IOException | XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<PropertyInfo> createPropertyInfo(
            Map<Property, PropertyVersions> versions, boolean allowSnapshots) {
        return versions.entrySet().stream()
                .map(e -> new PropertyInfo() {
                    {
                        setPropertyName(e.getKey().getName());
                        if (e.getValue().getAssociations() != null
                                && e.getValue().getAssociations().length != 0) {
                            setPropertyAssociations(Arrays.stream(e.getValue().getAssociations())
                                    .map(a -> {
                                        PropertyAssociation pa = new PropertyAssociation();
                                        pa.setGroupId(a.getArtifact().getGroupId());
                                        pa.setArtifactId(a.getArtifact().getArtifactId());
                                        return pa;
                                    })
                                    .collect(Collectors.toList()));
                        }
                        setCurrentVersion(e.getKey().getVersion());
                        ofNullable(e.getValue().getNewestUpdateWithinSegment(empty(), allowSnapshots))
                                .map(ArtifactVersion::toString)
                                .ifPresent(this::setLastVersion);

                        setSection(e.getValue(), INCREMENTAL, this::setIncrementals, allowSnapshots);
                        setSection(e.getValue(), MINOR, this::setMinors, allowSnapshots);
                        setSection(e.getValue(), MAJOR, this::setMajors, allowSnapshots);

                        setStatus(statusFor(getLastVersion(), getIncrementals(), getMinors()));
                    }
                })
                .collect(Collectors.toList());
    }
}
