/*
 * Copyright © Magento, Inc. All rights reserved.
 * See COPYING.txt for license details.
 */

package com.magento.idea.magento2uct.packages;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.psi.PsiElementVisitor;
import com.magento.idea.magento2uct.bundles.UctInspectionBundle;
import com.magento.idea.magento2uct.inspections.UctProblemsHolder;
import com.magento.idea.magento2uct.inspections.php.deprecation.CallingDeprecatedMethod;
import com.magento.idea.magento2uct.inspections.php.deprecation.ExtendingDeprecatedClass;
import com.magento.idea.magento2uct.inspections.php.deprecation.ImplementedDeprecatedInterface;
import com.magento.idea.magento2uct.inspections.php.deprecation.ImportingDeprecatedClass;
import com.magento.idea.magento2uct.inspections.php.deprecation.ImportingDeprecatedInterface;
import com.magento.idea.magento2uct.inspections.php.deprecation.InheritedDeprecatedInterface;
import com.magento.idea.magento2uct.inspections.php.deprecation.OverridingDeprecatedConstant;
import com.magento.idea.magento2uct.inspections.php.deprecation.OverridingDeprecatedProperty;
import com.magento.idea.magento2uct.inspections.php.deprecation.UsingDeprecatedClass;
import com.magento.idea.magento2uct.inspections.php.deprecation.UsingDeprecatedConstant;
import com.magento.idea.magento2uct.inspections.php.deprecation.UsingDeprecatedInterface;
import com.magento.idea.magento2uct.inspections.php.deprecation.UsingDeprecatedProperty;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import org.jetbrains.annotations.Nullable;

public enum SupportedIssue {

    EXTENDING_DEPRECATED_CLASS(
            1131,
            IssueSeverityLevel.WARNING,
            "customCode.warnings.deprecated.1131",
            ExtendingDeprecatedClass.class
    ),
    IMPORTING_DEPRECATED_CLASS(
            1132,
            IssueSeverityLevel.WARNING,
            "customCode.warnings.deprecated.1132",
            ImportingDeprecatedClass.class
    ),
    IMPORTING_DEPRECATED_INTERFACE(
            1332,
            IssueSeverityLevel.WARNING,
            "customCode.warnings.deprecated.1332",
            ImportingDeprecatedInterface.class
    ),
    USING_DEPRECATED_CLASS(
            1134,
            IssueSeverityLevel.WARNING,
            "customCode.warnings.deprecated.1134",
            UsingDeprecatedClass.class
    ),
    USING_DEPRECATED_INTERFACE(
            1334,
            IssueSeverityLevel.WARNING,
            "customCode.warnings.deprecated.1334",
            UsingDeprecatedInterface.class
    ),
    USING_DEPRECATED_CONSTANT(
            1234,
            IssueSeverityLevel.WARNING,
            "customCode.warnings.deprecated.1234",
            UsingDeprecatedConstant.class
    ),
    OVERRIDING_DEPRECATED_CONSTANT(
            1235,
            IssueSeverityLevel.WARNING,
            "customCode.warnings.deprecated.1235",
            OverridingDeprecatedConstant.class
    ),
    OVERRIDING_DEPRECATED_PROPERTY(
            1535,
            IssueSeverityLevel.WARNING,
            "customCode.warnings.deprecated.1535",
            OverridingDeprecatedProperty.class
    ),
    INHERITED_DEPRECATED_INTERFACE(
            1337,
            IssueSeverityLevel.WARNING,
            "customCode.warnings.deprecated.1337",
            InheritedDeprecatedInterface.class
    ),
    IMPLEMENTED_DEPRECATED_INTERFACE(
            1338,
            IssueSeverityLevel.WARNING,
            "customCode.warnings.deprecated.1338",
            ImplementedDeprecatedInterface.class
    ),
    CALLING_DEPRECATED_METHOD(
            1439,
            IssueSeverityLevel.WARNING,
            "customCode.warnings.deprecated.1439",
            CallingDeprecatedMethod.class
    ),
    USING_DEPRECATED_PROPERTY(
            1534,
            IssueSeverityLevel.WARNING,
            "customCode.warnings.deprecated.1534",
            UsingDeprecatedProperty.class
    );

    private final int code;
    private final IssueSeverityLevel level;
    private final String messageKey;
    private final Class<? extends LocalInspectionTool> inspectionClass;
    private static final UctInspectionBundle BUNDLE = new UctInspectionBundle();

    /**
     * Known issue ENUM.
     *
     * @param code IssueSeverityLevel
     * @param level IssueSeverityLevel
     * @param messageKey String
     * @param inspectionClass Class
     */
    SupportedIssue(
            final int code,
            final IssueSeverityLevel level,
            final String messageKey,
            final Class<? extends LocalInspectionTool> inspectionClass
    ) {
        this.code = code;
        this.level = level;
        this.messageKey = messageKey;
        this.inspectionClass = inspectionClass;
    }

    /**
     * Get issue code.
     *
     * @return int
     */
    public int getCode() {
        return code;
    }

    /**
     * Get issue severity level.
     *
     * @return IssueSeverityLevel
     */
    public IssueSeverityLevel getLevel() {
        return level;
    }

    /**
     * Get bundle message for the issue.
     *
     * @param args Object
     *
     * @return String
     */
    public String getMessage(final Object... args) {
        return BUNDLE.message(messageKey, args);
    }

    /**
     * Get registered inspection class.
     *
     * @return Class
     */
    public Class<? extends LocalInspectionTool> getInspectionClass() {
        return inspectionClass;
    }

    /**
     * Get issue by code.
     *
     * @param code int
     *
     * @return SupportedIssue
     */
    public static SupportedIssue getByCode(final int code) {
        for (final SupportedIssue issue : SupportedIssue.values()) {
            if (issue.getCode() == code) {
                return issue;
            }
        }
        // unsupported issue code.
        return null;
    }

    /**
     * Get visitors for all registered inspections.
     *
     * @param problemsHolder UctProblemsHolder
     *
     * @return List[PsiElementVisitor]
     */
    public static List<PsiElementVisitor> getVisitors(
            final UctProblemsHolder problemsHolder
    ) {
        final List<PsiElementVisitor> visitors = new LinkedList<>();

        for (final SupportedIssue issue : SupportedIssue.values()) {
            final PsiElementVisitor visitor = buildInspectionVisitor(
                    problemsHolder,
                    issue.getInspectionClass()
            );

            if (visitor != null) {
                visitors.add(visitor);
            }
        }

        return visitors;
    }

    /**
     * Build inspection visitor for file.
     *
     * @param problemsHolder UctProblemsHolder
     * @param inspectionClass Class
     *
     * @return PsiElementVisitor
     */
    private static @Nullable PsiElementVisitor buildInspectionVisitor(
            final UctProblemsHolder problemsHolder,
            final Class<? extends LocalInspectionTool> inspectionClass
    ) {
        LocalInspectionTool inspection;

        try {
            inspection = inspectionClass.getConstructor().newInstance();
        } catch (NoSuchMethodException
                | IllegalAccessException
                | InvocationTargetException
                | InstantiationException exception) {
            return null;
        }

        return inspection.buildVisitor(problemsHolder, false);
    }
}
