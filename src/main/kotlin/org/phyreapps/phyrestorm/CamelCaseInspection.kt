package org.phyreapps.phyrestorm


import com.intellij.codeInspection.*
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiRecursiveElementVisitor


import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.lang.psi.elements.*
import com.jetbrains.php.lang.psi.elements.Function
import com.jetbrains.php.lang.psi.elements.PhpClass


class CamelCaseInspection : LocalInspectionTool() {

    private val whitelistedMethods = setOf("__construct", "__destruct", "__get", "__set")

    private val whitelistedClassReferences = setOf("self", "static", "parent", "string", "int", "float", "bool", "array", "callable", "iterable", "mixed","void", "null")

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
                if (element is PhpClass) {
                    var className = element.name
                    if (!isCamelCaseFirstUpper(className)) {
                        holder.registerProblem(
                            element,
                            "Class name '$className' does not follow CamelCase convention",
                            ProblemHighlightType.ERROR,
                            *emptyArray()
                        )
                    }
                }
                if (element is ClassReference) {
                    var className = element.name.toString()
                    if (!isCamelCaseFirstUpper(className) && !isWhitelistedClassReference(className)) {
                        holder.registerProblem(
                            element,
                            "ClassReference name '$className' does not follow CamelCase convention",
                            ProblemHighlightType.ERROR,
                            *emptyArray()
                        )
                    }
                }
                super.visitElement(element)
            }
        }
    }

    private fun isWhitelistedClassReference(classReference: String): Boolean {
        return whitelistedClassReferences.contains(classReference)
    }

    private fun isWhitelistedMethod(methodName: String): Boolean {
        // Check if the method is in the whitelist (like __construct, __destruct, etc.)
        return whitelistedMethods.contains(methodName)
    }

    // Function to check if the class name follows CamelCase
    private fun isCamelCaseFirstUpper(name: String): Boolean {
        // Starts with uppercase, no underscores
        val camelCaseRegex = "^[A-Z][a-zA-Z0-9]*$".toRegex()
        return name.matches(camelCaseRegex)
    }

    private fun isCamelCase(variableName: String): Boolean {
        // CamelCase pattern: starts with lowercase letter, followed by uppercase letters for new words
        val camelCaseRegex = "^[a-z][a-zA-Z0-9]*$".toRegex()
        return variableName.matches(camelCaseRegex)
    }
}
