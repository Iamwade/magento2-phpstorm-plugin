/*
 * Copyright © Magento, Inc. All rights reserved.
 * See COPYING.txt for license details.
 */

package com.magento.idea.magento2plugin.actions.generation.generator;

import com.intellij.psi.PsiFile;
import com.magento.idea.magento2plugin.actions.generation.data.DeleteEntityByIdCommandData;
import com.magento.idea.magento2plugin.magento.files.commands.DeleteEntityByIdCommandFile;

public class DeleteEntityCommandGeneratorTest extends BaseGeneratorTestCase {

    private static final String MODULE_NAME = "Foo_Bar";
    private static final String ENTITY_NAME = "Book";
    private static final String MODEL_NAME = ENTITY_NAME + "Model";
    private static final String RESOURCE_MODEL_NAME = ENTITY_NAME + "Resource";
    private static final String EXPECTED_DIRECTORY = "src/app/code/Foo/Bar/Command/" + ENTITY_NAME;
    private static final String ENTITY_ID = "book_id";

    /**
     * Test generation of DeleteByIdCommand model for entity.
     */
    public void testGenerateDeleteEntityByIdCommandFile() {
        final DeleteEntityByIdCommandData deleteEntityByIdCommandData =
                new DeleteEntityByIdCommandData(
                        MODULE_NAME,
                        ENTITY_NAME,
                        ENTITY_ID,
                        MODEL_NAME,
                        RESOURCE_MODEL_NAME
                );
        final DeleteEntityByIdCommandGenerator deleteEntityByIdCommandGenerator =
                new DeleteEntityByIdCommandGenerator(
                        deleteEntityByIdCommandData,
                        myFixture.getProject(),
                        false
                );
        final String filePath = this.getFixturePath(
                new DeleteEntityByIdCommandFile(MODULE_NAME, ENTITY_NAME).getFileName()
        );
        final PsiFile expectedFile = myFixture.configureByFile(filePath);
        final PsiFile deleteEntityCommandFile = deleteEntityByIdCommandGenerator.generate("test");

        assertGeneratedFileIsCorrect(
                expectedFile,
                EXPECTED_DIRECTORY,
                deleteEntityCommandFile
        );
    }
}
