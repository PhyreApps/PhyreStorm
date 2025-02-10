package org.phyreapps.phyrestorm

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.lang.psi.elements.*
import com.jetbrains.php.lang.psi.elements.Function
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor

class UnusedVariableInspection : LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PhpElementVisitor() {
            override fun visitPhpFunction(function: Function) {
                super.visitPhpFunction(function)
                checkUnusedVariables(function, holder)
            }

            override fun visitPhpMethod(method: Method) {
                super.visitPhpMethod(method)
                checkUnusedVariables(method, holder)
            }

            override fun visitPhpClass(phpClass: PhpClass) {
                super.visitPhpClass(phpClass)
                checkUnusedVariables(phpClass, holder)
            }
        }
    }

    private fun checkUnusedVariables(element: PsiElement, holder: ProblemsHolder) {
        val declaredVariables: MutableMap<String, Variable> = HashMap()
        val usedVariables: MutableSet<String> = HashSet()

        element.accept(object : PhpElementVisitor() {
            // Използваме accept вместо acceptChildren
            override fun visitPhpVariable(variable: Variable) {
                val varName = variable.name
                if (varName != null) {
                    if (variable.parent is AssignmentExpression) {
                        declaredVariables.putIfAbsent(varName, variable)
                    } else {
                        usedVariables.add(varName)
                    }
                }
            }
        })

        for ((key, value) in declaredVariables) {
            if (!usedVariables.contains(key)) {
                holder.registerProblem(
                    value as PsiElement,
                    "Unused variable: $key",
                    ProblemHighlightType.ERROR
                )
            }
        }
    }

}