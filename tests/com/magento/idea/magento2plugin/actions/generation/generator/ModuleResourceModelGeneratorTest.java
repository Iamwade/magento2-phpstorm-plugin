/*
 * Copyright © Magento, Inc. All rights reserved.
 * See COPYING.txt for license details.
 */

package com.magento.idea.magento2plugin.actions.generation.generator;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.magento.idea.magento2plugin.actions.generation.data.ResourceModelData;

public class ModuleResourceModelGeneratorTest extends BaseGeneratorTestCase {

    private static final String EXPECTED_DIR = "src/app/code/Foo/Bar/Model/ResourceModel";

    /**
     * Test generation of resource model file.
     */
    public void testGenerateFile() {
        final Project project = myFixture.getProject();
        final ResourceModelData resourceModelData = new ResourceModelData(
                "Foo_Bar",
                "my_table",
                "TestResourceModel",
                "entity_id"
        );
        final ModuleResourceModelGenerator generator = new ModuleResourceModelGenerator(
                resourceModelData,
                project
        );
        final PsiFile resourceModelFile = generator.generate("test");
        final String filePath = this.getFixturePath("TestResourceModel.php");
        final PsiFile expectedFile = myFixture.configureByFile(filePath);

        assertGeneratedFileIsCorrect(
                expectedFile,
                EXPECTED_DIR,
                resourceModelFile
        );
    }
}
