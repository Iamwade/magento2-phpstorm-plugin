/*
 * Copyright © Magento, Inc. All rights reserved.
 * See COPYING.txt for license details.
 */

package com.magento.idea.magento2plugin.actions.generation.generator;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.magento.idea.magento2plugin.actions.generation.generator.util.DirectoryGenerator;
import com.magento.idea.magento2plugin.bundles.ValidatorBundle;
import com.magento.idea.magento2plugin.magento.packages.Areas;
import com.magento.idea.magento2plugin.util.RegExUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

@SuppressWarnings("PMD.AbstractClassWithoutAbstractMethod")
public abstract class OverrideInThemeGenerator {

    protected final Project project;
    protected final ValidatorBundle validatorBundle;

    /**
     * OverrideInThemeGenerator constructor.
     *
     * @param project Project
     */
    public OverrideInThemeGenerator(final Project project) {
        this.project = project;
        this.validatorBundle = new ValidatorBundle();
    }

    /**
     *  Get target directory.
     *
     * @param directory PsiDirectory
     * @param pathComponents List[String]
     *
     * @return PsiDirectory
     */
    protected PsiDirectory getTargetDirectory(
            final PsiDirectory directory,
            final List<String> pathComponents
    ) {
        PsiDirectory result = directory;
        PsiDirectory tempDirectory = directory;
        final DirectoryGenerator generator = DirectoryGenerator.getInstance();

        for (final String directoryName : pathComponents) {
            result = generator.findOrCreateSubdirectory(tempDirectory, directoryName);
            tempDirectory = result;
        }

        return result;
    }

    /**
     * Gt module path components.
     *
     * @param file PsiFile
     * @param componentName String
     *
     * @return List[String]
     */
    protected List<String> getModulePathComponents(
            final PsiFile file,
            final String componentName
    ) {
        final List<String> pathComponents = new ArrayList<>();
        PsiDirectory parent = file.getParent();

        while (!parent.getName().equals(Areas.frontend.toString())
                && !parent.getName().equals(Areas.adminhtml.toString())
                && !parent.getName().equals(Areas.base.toString())
        ) {
            pathComponents.add(parent.getName());
            parent = parent.getParent();
        }
        pathComponents.add(componentName);
        Collections.reverse(pathComponents);

        return pathComponents;
    }

    /**
     * Get theme path components.
     *
     * @param file PsiFile
     *
     * @return String[]
     */
    protected List<String> getThemePathComponents(final PsiFile file) {
        final List<String> pathComponents = new ArrayList<>();
        final Pattern pattern = Pattern.compile(RegExUtil.Magento.MODULE_NAME);

        PsiDirectory parent = file.getParent();
        do {
            pathComponents.add(parent.getName());
            parent = parent.getParent();
        } while (!pattern.matcher(parent.getName()).find());
        pathComponents.add(parent.getName());
        Collections.reverse(pathComponents);

        return pathComponents;
    }
}
