/*
 * Copyright © Magento, Inc. All rights reserved.
 * See COPYING.txt for license details.
 */

package com.magento.idea.magento2plugin.actions.generation.generator.util;

import com.intellij.openapi.project.Project;
import com.magento.idea.magento2plugin.magento.files.ModuleFileInterface;
import com.magento.idea.magento2plugin.magento.files.QueueTopologyXml;

public class FindOrCreateQueueTopologyXml extends FindOrCreateQueueXml {
    /**
     * Constructor.
     */
    public FindOrCreateQueueTopologyXml(Project project) {
        super(project);
    }

    @Override
    protected ModuleFileInterface instantiateXmlFile() {
        return new QueueTopologyXml();
    }
}
