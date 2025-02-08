package org.phyreapps.phyrestorm


import com.intellij.codeInspection.*
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiRecursiveElementVisitor
import org.jetbrains.annotations.Nls


import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.openapi.diagnostic.Logger
import com.jetbrains.php.lang.psi.elements.*
import com.jetbrains.php.lang.psi.elements.Function


class CamelCaseVariableInspection : LocalInspectionTool() {

    private val whitelistedMethods = setOf("__construct", "__destruct", "__get", "__set")

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
                if (element is Method) {
                    var methodName = element.name
                    var methodNameWithoutUnderscope = element.name.replace(Regex("^_+"), "")
                    // Check private methods for _ prefix
                    if (element.access == PhpModifier.Access.PRIVATE && !methodName.startsWith("_")) {
                        holder.registerProblem(
                            element,
                            "Private method name '$methodName' must start with '_'",
                            ProblemHighlightType.ERROR,
                            *emptyArray()
                        )
                    }
                    if (!isCamelCase(methodNameWithoutUnderscope) && !isWhitelistedMethod(methodName)) {
                        holder.registerProblem(
                            element,
                            "Method name '$methodNameWithoutUnderscope' does not follow camelCase convention",
                            ProblemHighlightType.ERROR,
                            *emptyArray()
                        )
                    }
                }
                if (element is Function) {
                    var functionName = element.name
                    var functionNameWithoutUnderscope = element.name.replace(Regex("^_+"), "")
                    
                    if (!isCamelCase(functionNameWithoutUnderscope) && !isWhitelistedMethod(functionName)) {
                        holder.registerProblem(
                            element,
                            "Function name '$functionName' does not follow camelCase convention",
                            ProblemHighlightType.ERROR,
                            *emptyArray()
                        )
                    }
                }
                super.visitElement(element)
            }
        }
    }

    private fun isWhitelistedMethod(methodName: String): Boolean {
        // Check if the method is in the whitelist (like __construct, __destruct, etc.)
        return whitelistedMethods.contains(methodName)
    }

    private fun isCamelCase(variableName: String): Boolean {
        // CamelCase pattern: starts with lowercase letter, followed by uppercase letters for new words
        val camelCaseRegex = "^[a-z][a-zA-Z0-9]*$".toRegex()
        return variableName.matches(camelCaseRegex)
    }
}
