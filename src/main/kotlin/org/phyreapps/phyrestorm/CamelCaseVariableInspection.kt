package org.phyreapps.phyrestorm


import com.intellij.codeInspection.*
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiRecursiveElementVisitor
import com.jetbrains.php.lang.psi.elements.If
import org.jetbrains.annotations.Nls


import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.openapi.diagnostic.Logger
import com.jetbrains.php.lang.psi.elements.Field
import com.jetbrains.php.lang.psi.elements.Variable


class CamelCaseVariableInspection : LocalInspectionTool() {

    override fun getShortName(): String {
        return "CamelCaseVariableInspection"
    }

    override fun getDisplayName(): String {
        return "Check variable names follow camelCase"
    }

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PsiRecursiveElementVisitor() {
            override fun visitElement(element: PsiElement) {
                if (element is Variable) {
                    var variableName = element.name
                    if (!isCamelCase(variableName)) {
                        holder.registerProblem(
                            element,
                            "Variable name '$variableName' does not follow camelCase convention",
                            ProblemHighlightType.ERROR,
                            *emptyArray()
                        )
                    }
                }
                if (element is Field) {
                    var fieldName = element.name
                    if (!isCamelCase(fieldName)) {
                        holder.registerProblem(
                            element,
                            "Property name '$fieldName' does not follow camelCase convention",
                            ProblemHighlightType.ERROR,
                            *emptyArray()
                        )
                    }
                }
                super.visitElement(element)
            }
        }
    }

    private fun isCamelCase(variableName: String): Boolean {
        // CamelCase pattern: starts with lowercase letter, followed by uppercase letters for new words
        val camelCaseRegex = "^[a-z][a-zA-Z0-9]*$".toRegex()
        return variableName.matches(camelCaseRegex)
    }
}
