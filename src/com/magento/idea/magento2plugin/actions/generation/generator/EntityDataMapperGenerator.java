/*
 * Copyright © Magento, Inc. All rights reserved.
 * See COPYING.txt for license details.
 */

package com.magento.idea.magento2plugin.actions.generation.generator;

import com.intellij.openapi.project.Project;
import com.magento.idea.magento2plugin.actions.generation.data.EntityDataMapperData;
import com.magento.idea.magento2plugin.actions.generation.generator.util.PhpClassGeneratorUtil;
import com.magento.idea.magento2plugin.actions.generation.generator.util.PhpClassTypesBuilder;
import com.magento.idea.magento2plugin.magento.files.AbstractPhpFile;
import com.magento.idea.magento2plugin.magento.files.DataModelFile;
import com.magento.idea.magento2plugin.magento.files.DataModelInterfaceFile;
import com.magento.idea.magento2plugin.magento.files.EntityDataMapperFile;
import com.magento.idea.magento2plugin.magento.files.ModelFile;
import com.magento.idea.magento2plugin.magento.packages.code.FrameworkLibraryType;
import java.util.Properties;
import org.jetbrains.annotations.NotNull;

public class EntityDataMapperGenerator extends PhpFileGenerator {

    private final EntityDataMapperData data;

    /**
     * Entity data mapper generator constructor.
     *
     * @param data EntityDataMapperData
     * @param project Project
     */
    public EntityDataMapperGenerator(
            final @NotNull EntityDataMapperData data,
            final @NotNull Project project
    ) {
        this(data, project, true);
    }

    /**
     * Entity data mapper generator constructor.
     *
     * @param data EntityDataMapperData
     * @param project Project
     * @param checkFileAlreadyExists boolean
     */
    public EntityDataMapperGenerator(
            final @NotNull EntityDataMapperData data,
            final @NotNull Project project,
            final boolean checkFileAlreadyExists
    ) {
        super(project, checkFileAlreadyExists);
        this.data = data;
    }

    @Override
    protected AbstractPhpFile initFile() {
        return new EntityDataMapperFile(data.getModuleName(), data.getEntityName());
    }

    /**
     * Fill entity data mapper file attributes.
     *
     * @param attributes Properties
     */
    @Override
    protected void fillAttributes(final @NotNull Properties attributes) {
        final PhpClassTypesBuilder phpClassTypesBuilder = new PhpClassTypesBuilder();

        final ModelFile modelFile = new ModelFile(data.getModuleName(), data.getModelName());
        final DataModelFile dtoFile = new DataModelFile(data.getDtoName(), data.getModuleName());
        final DataModelInterfaceFile dtoInterfaceFile =
                new DataModelInterfaceFile(data.getModuleName(), data.getDtoInterfaceName());
        String dtoType;

        if (data.isDtoWithInterface()) {
            dtoType = dtoInterfaceFile.getClassFqn();
        } else {
            dtoType = dtoFile.getClassFqn();
        }

        phpClassTypesBuilder
                .appendProperty("NAMESPACE", file.getNamespace())
                .appendProperty("ENTITY_NAME", data.getEntityName())
                .appendProperty("CLASS_NAME", file.getClassName())
                .append("DATA_OBJECT", FrameworkLibraryType.DATA_OBJECT.getType())
                .append("DTO_TYPE", dtoType)
                .append("MAGENTO_MODEL_TYPE", modelFile.getClassFqn())
                .append("DTO_FACTORY", dtoType.concat("Factory"))
                .append("ABSTRACT_COLLECTION", FrameworkLibraryType.ABSTRACT_COLLECTION.getType())
                .mergeProperties(attributes);

        attributes.setProperty(
                "USES",
                PhpClassGeneratorUtil.formatUses(phpClassTypesBuilder.getUses())
        );
    }
}
