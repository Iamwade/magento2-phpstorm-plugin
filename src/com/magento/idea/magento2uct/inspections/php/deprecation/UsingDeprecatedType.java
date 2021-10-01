/*
 * Copyright © Magento, Inc. All rights reserved.
 * See COPYING.txt for license details.
 */

package com.magento.idea.magento2uct.inspections.php.deprecation;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.jetbrains.php.lang.PhpLangUtil;
import com.jetbrains.php.lang.inspections.PhpInspection;
import com.jetbrains.php.lang.psi.PhpPsiElementFactory;
import com.jetbrains.php.lang.psi.elements.ClassReference;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.impl.ClassConstImpl;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeAnalyserVisitor;
import com.magento.idea.magento2uct.packages.IssueSeverityLevel;
import com.magento.idea.magento2uct.settings.UctSettingsService;
import com.magento.idea.magento2uct.versioning.VersionStateManager;
import org.jetbrains.annotations.NotNull;

public abstract class UsingDeprecatedType extends PhpInspection {

    /**
     * Register type specific problem.
     *
     * @param problemsHolder ProblemsHolder
     * @param field Field
     * @param deprecatedType String
     * @param isInterface boolean
     */
    protected abstract void registerProblem(
            final @NotNull ProblemsHolder problemsHolder,
            final Field field,
            final String deprecatedType,
            final boolean isInterface
    );

    /**
     * Register type specific problem.
     *
     * @param problemsHolder ProblemsHolder
     * @param reference ClassReference
     * @param deprecatedType String
     * @param isInterface boolean
     */
    protected abstract void registerReferenceProblem(
            final @NotNull ProblemsHolder problemsHolder,
            final ClassReference reference,
            final String deprecatedType,
            final boolean isInterface
    );

    /**
     * Get issue severity level for concrete inspection.
     *
     * @return IssueSeverityLevel
     */
    protected abstract IssueSeverityLevel getSeverityLevel();

    @Override
    @SuppressWarnings({"PMD.CognitiveComplexity", "PMD.NPathComplexity"})
    public @NotNull PsiElementVisitor buildVisitor(
            final @NotNull ProblemsHolder problemsHolder,
            final boolean isOnTheFly
    ) {
        return new PhpTypeAnalyserVisitor() {

            @Override
            public void visitPhpField(final Field field) {
                final Project project = field.getProject();
                final UctSettingsService settings = UctSettingsService.getInstance(project);

                if (!settings.isEnabled()
                        || !settings.isIssueLevelSatisfiable(getSeverityLevel())) {
                    return;
                }
                super.visitPhpField(field);
                final PhpClass phpClass = field.getContainingClass();

                if (phpClass == null || field instanceof ClassConstImpl) {
                    return;
                }
                final PhpType type = this.getType().filterScalarPrimitives();
                final String fieldType = type.toString();

                if (!PhpLangUtil.isFqn(fieldType)) {
                    return;
                }
                final ClassReference phpReference = PhpPsiElementFactory.createClassReference(
                        project,
                        fieldType
                );
                boolean isInterface = false;
                final PsiElement element = phpReference.resolve();

                if (element instanceof PhpClass && ((PhpClass) element).isInterface()) {
                    isInterface = true;
                }

                if (VersionStateManager.getInstance(project).isDeprecated(fieldType)) {
                    registerProblem(problemsHolder, field, fieldType, isInterface);
                }
            }

            @Override
            public void visitPhpClassReference(final ClassReference reference) {
                final Project project = reference.getProject();
                final UctSettingsService settings = UctSettingsService.getInstance(project);

                if (!settings.isEnabled()
                        || !settings.isIssueLevelSatisfiable(getSeverityLevel())) {
                    return;
                }
                final PsiElement resolved = reference.resolve();

                if (!(resolved instanceof PhpClass)) {
                    return;
                }
                final PhpClass phpClass = (PhpClass) resolved;
                final boolean isInterface = phpClass.isInterface();

                if (VersionStateManager.getInstance(project)
                        .isDeprecated(phpClass.getFQN())) {
                    registerReferenceProblem(
                            problemsHolder,
                            reference,
                            reference.getFQN(),
                            isInterface
                    );
                }
            }
        };
    }
}
