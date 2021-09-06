/*
 * Copyright © Magento, Inc. All rights reserved.
 * See COPYING.txt for license details.
 */

package com.magento.idea.magento2uct.versioning;

import java.util.LinkedList;
import java.util.List;

public enum SupportedVersion {

    V230("2.3.0"),
    V231("2.3.1"),
    V232("2.3.2"),
    V2322("2.3.2-p2"),
    V233("2.3.3"),
    V2331("2.3.3-p1"),
    V234("2.3.4"),
    V2341("2.3.4-p1"),
    V2342("2.3.4-p2"),
    V235("2.3.5"),
    V2351("2.3.5-p1"),
    V2352("2.3.5-p2"),
    V236("2.3.6"),
    V2361("2.3.6-p1"),
    V237("2.3.7"),
    V2371("2.3.7-p1"),
    V240("2.4.0"),
    V2401("2.4.0-p1"),
    V241("2.4.1"),
    V2411("2.4.1-p1"),
    V242("2.4.2"),
    V2421("2.4.2-p1"),
    V2422("2.4.2-p2"),
    V243("2.4.3");

    private final String version;

    SupportedVersion(final String version) {
        this.version = version;
    }

    /**
     * Get version.
     *
     * @return String
     */
    public String getVersion() {
        return version;
    }

    /**
     * Get supported versions.
     *
     * @return List[String]
     */
    public static List<String> getSupportedVersions() {
        final List<String> versions = new LinkedList<>();

        for (final SupportedVersion version : SupportedVersion.values()) {
            versions.add(version.getVersion());
        }

        return versions;
    }
}
