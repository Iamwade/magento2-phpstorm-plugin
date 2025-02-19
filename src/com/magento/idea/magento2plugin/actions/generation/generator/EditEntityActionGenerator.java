/*
 * Copyright © Magento, Inc. All rights reserved.
 * See COPYING.txt for license details.
 */

package com.magento.idea.magento2plugin.actions.generation.generator;

import com.intellij.openapi.project.Project;
import com.magento.idea.magento2plugin.actions.generation.data.EditEntityActionData;
import com.magento.idea.magento2plugin.actions.generation.generator.util.PhpClassGeneratorUtil;
import com.magento.idea.magento2plugin.actions.generation.generator.util.PhpClassTypesBuilder;
import com.magento.idea.magento2plugin.magento.files.AbstractPhpFile;
import com.magento.idea.magento2plugin.magento.files.actions.EditActionFile;
import com.magento.idea.magento2plugin.magento.packages.HttpMethod;
import com.magento.idea.magento2plugin.magento.packages.code.BackendModuleType;
import com.magento.idea.magento2plugin.magento.packages.code.FrameworkLibraryType;
import java.util.Properties;
import org.jetbrains.annotations.NotNull;

public class EditEntityActionGenerator extends PhpFileGenerator {

    private final EditEntityActionData data;

    /**
     * Edit entity action/controller file generator constructor.
     *
     * @param data EditEntityActionData
     * @param project Project
     */
    public EditEntityActionGenerator(
            final @NotNull EditEntityActionData data,
            final @NotNull Project project
    ) {
        this(data, project, true);
    }

    /**
     * Edit entity action/controller file generator constructor.
     *
     * @param data EditEntityActionData
     * @param project Project
     * @param checkFileAlreadyExists boolean
     */
    public EditEntityActionGenerator(
            final @NotNull EditEntityActionData data,
            final @NotNull Project project,
            final boolean checkFileAlreadyExists
    ) {
        super(project, checkFileAlreadyExists);
        this.data = data;
    }

    @Override
    protected AbstractPhpFile initFile() {
        return new EditActionFile(data.getModuleName(), data.getEntityName());
    }

    /**
     * Fill edit action file attributes.
     *
     * @param attributes Properties
     */
    @Override
    protected void fillAttributes(final @NotNull Properties attributes) {
        final PhpClassTypesBuilder phpClassTypesBuilder = new PhpClassTypesBuilder();

        phpClassTypesBuilder
                .appendProperty("NAMESPACE", file.getNamespace())
                .appendProperty("ENTITY_NAME", data.getEntityName())
                .appendProperty("CLASS_NAME", file.getClassName())
                .appendProperty("ADMIN_RESOURCE", data.getAcl())
                .appendProperty("MENU_IDENTIFIER", data.getMenu())
                .append("EXTENDS", BackendModuleType.EXTENDS.getType())
                .append("IMPLEMENTS", HttpMethod.GET.getInterfaceFqn())
                .append("RESULT_INTERFACE", FrameworkLibraryType.RESULT_INTERFACE.getType())
                .append("RESULT_FACTORY", FrameworkLibraryType.RESULT_FACTORY.getType())
                .append("RESULT_PAGE", BackendModuleType.RESULT_PAGE.getType())
                .mergeProperties(attributes);

        attributes.setProperty("USES",
                PhpClassGeneratorUtil.formatUses(phpClassTypesBuilder.getUses()));
    }
}
