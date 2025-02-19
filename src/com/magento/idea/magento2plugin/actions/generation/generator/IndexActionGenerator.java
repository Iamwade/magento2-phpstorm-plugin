/*
 * Copyright © Magento, Inc. All rights reserved.
 * See COPYING.txt for license details.
 */

package com.magento.idea.magento2plugin.actions.generation.generator;

import com.intellij.openapi.project.Project;
import com.magento.idea.magento2plugin.actions.generation.data.IndexActionData;
import com.magento.idea.magento2plugin.actions.generation.generator.util.PhpClassGeneratorUtil;
import com.magento.idea.magento2plugin.actions.generation.generator.util.PhpClassTypesBuilder;
import com.magento.idea.magento2plugin.magento.files.AbstractPhpFile;
import com.magento.idea.magento2plugin.magento.files.actions.IndexActionFile;
import com.magento.idea.magento2plugin.magento.packages.HttpMethod;
import com.magento.idea.magento2plugin.magento.packages.code.BackendModuleType;
import com.magento.idea.magento2plugin.magento.packages.code.FrameworkLibraryType;
import java.util.Properties;
import org.jetbrains.annotations.NotNull;

public class IndexActionGenerator extends PhpFileGenerator {

    private final IndexActionData data;

    /**
     * Constructor for adminhtml index (list) controller generator.
     *
     * @param data EntityIndexAdminhtmlActionData
     * @param project Project
     */
    public IndexActionGenerator(
            final @NotNull IndexActionData data,
            final @NotNull Project project
    ) {
        this(data, project, true);
    }

    /**
     * Constructor for adminhtml index (list) controller generator.
     *
     * @param data EntityIndexAdminhtmlActionData
     * @param project Project
     * @param checkFileAlreadyExists boolean
     */
    public IndexActionGenerator(
            final @NotNull IndexActionData data,
            final @NotNull Project project,
            final boolean checkFileAlreadyExists
    ) {
        super(project, checkFileAlreadyExists);
        this.data = data;
    }

    @Override
    protected AbstractPhpFile initFile() {
        return new IndexActionFile(data.getModuleName(), data.getEntityName());
    }

    /**
     * Fill index controller file attributes.
     *
     * @param attributes Properties
     */
    @Override
    protected void fillAttributes(final @NotNull Properties attributes) {
        final PhpClassTypesBuilder phpClassTypesBuilder = new PhpClassTypesBuilder();

        phpClassTypesBuilder
                .appendProperty("ENTITY_NAME", data.getEntityName())
                .appendProperty("CLASS_NAME", IndexActionFile.CLASS_NAME)
                .appendProperty("ACL", data.getAcl())
                .appendProperty("MENU", data.getMenu())
                .appendProperty("NAMESPACE", file.getNamespace())
                .append("PARENT_CLASS_NAME", BackendModuleType.EXTENDS.getType())
                .append("HTTP_GET_METHOD", HttpMethod.GET.getInterfaceFqn())
                .append("RESULT", FrameworkLibraryType.RESULT_INTERFACE.getType())
                .append("RESPONSE", FrameworkLibraryType.RESPONSE_INTERFACE.getType())
                .append("RESULT_FACTORY", FrameworkLibraryType.RESULT_FACTORY.getType())
                .mergeProperties(attributes);

        attributes.setProperty(
                "USES",
                PhpClassGeneratorUtil.formatUses(phpClassTypesBuilder.getUses())
        );
    }
}
