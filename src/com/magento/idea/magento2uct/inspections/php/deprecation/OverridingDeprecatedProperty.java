/*
 * Copyright © Magento, Inc. All rights reserved.
 * See COPYING.txt for license details.
 */

package com.magento.idea.magento2uct.inspections.php.deprecation;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElementVisitor;
import com.jetbrains.php.lang.inspections.PhpInspection;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.impl.ClassConstImpl;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeAnalyserVisitor;
import com.magento.idea.magento2uct.inspections.UctProblemsHolder;
import com.magento.idea.magento2uct.packages.SupportedIssue;
import com.magento.idea.magento2uct.settings.UctSettingsService;
import com.magento.idea.magento2uct.versioning.VersionStateManager;
import org.jetbrains.annotations.NotNull;

public class OverridingDeprecatedProperty extends PhpInspection {

    @Override
    @SuppressWarnings("PMD.CognitiveComplexity")
    public @NotNull PsiElementVisitor buildVisitor(
            final @NotNull ProblemsHolder problemsHolder,
            final boolean isOnTheFly
    ) {
        return new PhpTypeAnalyserVisitor() {

            @Override
            public void visitPhpField(final Field field) {
                final Project project = field.getProject();
                final UctSettingsService settings = UctSettingsService.getInstance(project);

                if (!settings.isEnabled() || !settings.isIssueLevelSatisfiable(
                        SupportedIssue.OVERRIDING_DEPRECATED_PROPERTY.getLevel())
                ) {
                    return;
                }
                super.visitPhpField(field);

                if (field instanceof ClassConstImpl) {
                    return;
                }
                final PhpClass phpClass = field.getContainingClass();

                if (phpClass == null) {
                    return;
                }
                PhpClass parentClass = phpClass.getSuperClass();
                boolean isFound = false;

                while (parentClass != null && !isFound) {
                    for (final Field parentField : parentClass.getOwnFields()) {
                        if (!(parentField instanceof ClassConstImpl)
                                && parentField.getName().equals(field.getName())
                                && VersionStateManager
                                .getInstance(field.getProject())
                                .isDeprecated(parentField.getFQN())
                        ) {
                            if (problemsHolder instanceof UctProblemsHolder) {
                                ((UctProblemsHolder) problemsHolder).setReservedErrorCode(
                                        SupportedIssue.OVERRIDING_DEPRECATED_PROPERTY.getCode()
                                );
                            }
                            problemsHolder.registerProblem(
                                    field,
                                    SupportedIssue.OVERRIDING_DEPRECATED_PROPERTY.getMessage(
                                            parentClass.getFQN()
                                                    .concat("::")
                                                    .concat(parentField.getName())
                                    ),
                                    ProblemHighlightType.LIKE_DEPRECATED
                            );
                            isFound = true;
                            break;
                        }
                    }
                    parentClass = parentClass.getSuperClass();
                }
            }
        };
    }
}
